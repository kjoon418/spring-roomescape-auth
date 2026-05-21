package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.AuthorizationException;
import roomescape.auth.Role;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.controller.dto.ReservationSummaryResponse;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.User;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.UserRepository;
import roomescape.service.dto.AssembledReservation;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationUpdateCommand;
import roomescape.service.mapper.ReservationResponseMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements AdminReservationService, ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;

    private final ReservationResponseMapper reservationResponseMapper;

    @Transactional
    public ReservationSummaryResponse create(
            ReservationCreateCommand command
    ) {
        validateReservationNotDuplicate(command.date(), command.themeId(), command.timeId());
        validateThemeExist(command.themeId());

        EntityId reservationId = EntityId.random();
        ReservationTime time = findTimeById(command.timeId());
        Reservation reservation = Reservation.create(
                reservationId,
                command.date(),
                time,
                command.themeId(),
                command.userId(),
                command.shopId()
        );

        Reservation persisted = reservationRepository.persist(reservation);

        return reservationResponseMapper.mapToSummaryResponse(persisted);
    }

    @Transactional(readOnly = true)
    public List<ReservationDetailResponse> findAllIncludeDetail() {
        List<Reservation> reservations = reservationRepository.findAll();

        return mapToDetailResponses(reservations);
    }

    @Transactional(readOnly = true)
    public List<ReservationDetailResponse> findAllIncludeDetail(EntityId userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);

        return mapToDetailResponses(reservations);
    }

    @Transactional
    public ReservationSummaryResponse update(
            ReservationUpdateCommand command
    ) {
        Reservation reservation = findReservationById(command.reservationId());

        ReservationTime timeToUpdate = findTimeById(command.timeId());
        validateReservationNotDuplicate(command.date(), reservation.getThemeId(), timeToUpdate.id());

        Reservation updatedReservation = reservationRepository.updateDateAndTimeId(
                reservation,
                command.date(),
                timeToUpdate
        );

        return reservationResponseMapper.mapToSummaryResponse(updatedReservation);
    }

    @Transactional
    public void delete(EntityId managerId, EntityId shopId, EntityId reservationId) {
        User manager = findUserById(managerId);
        validateManageAuthority(manager, shopId);

        Reservation reservation = findReservationById(reservationId);
        validateReservationBelongsToShop(reservation, shopId);

        boolean deleted = reservationRepository.delete(reservationId);
        if (!deleted) {
            throw new EntityNotFoundException(
                    ErrorCode.RESERVATION_NOT_FOUND,
                    "삭제할 예약을 조회하지 못했습니다. reservationId = " + reservationId
            );
        }
    }

    @Transactional
    public void delete(EntityId reservationId) {
        boolean deleted = reservationRepository.delete(reservationId);

        if (!deleted) {
            throw new EntityNotFoundException(
                    ErrorCode.RESERVATION_NOT_FOUND,
                    "삭제할 예약을 조회하지 못했습니다. reservationId = " + reservationId
            );
        }
    }

    @Transactional
    public ReservationSummaryResponse cancel(EntityId shopId, EntityId reservationId) {
        Reservation reservation = findReservationById(reservationId);
        validateReservationBelongsToShop(reservation, shopId);

        Reservation updatedReservation = reservationRepository.updateCanceled(reservation, true);

        return reservationResponseMapper.mapToSummaryResponse(updatedReservation);
    }

    private List<ReservationDetailResponse> mapToDetailResponses(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::assembleReservation)
                .map(reservationResponseMapper::mapToDetailResponse)
                .toList();
    }

    private AssembledReservation assembleReservation(Reservation reservation) {
        ReservationTime time = reservation.getTime();
        Theme theme = findThemeById(reservation.getThemeId());
        User user = findUserById(reservation.getUserId());

        return new AssembledReservation(reservation, time, theme, user);
    }

    private User findUserById(EntityId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "유저를 조회할 수 없습니다. userId = " + userId
                ));
    }

    private void validateReservationNotDuplicate(
            LocalDate date,
            EntityId themeId,
            EntityId timeId
    ) {
        if (reservationRepository.existNotCanceledByDateAndThemeIdAndTimeId(date, themeId, timeId)) {
            throw new DuplicateReservationException(
                    "같은 테마의 같은 날짜/시간에는 하나의 예약만 가능합니다."
                            + " 요청한 날짜: " + date
                            + ", 요청한 테마 ID: " + themeId
                            + ", 요청한 시간 ID: " + timeId
            );
        }
    }

    private void validateThemeExist(EntityId themeId) {
        findThemeById(themeId);
    }

    private Reservation findReservationById(EntityId reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.RESERVATION_NOT_FOUND,
                        "예약을 조회할 수 없습니다. reservationId = " + reservationId
                ));
    }

    private ReservationTime findTimeById(EntityId timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.RESERVATION_TIME_NOT_FOUND,
                        "예약 시간을 조회할 수 없습니다. timeId = " + timeId
                ));
    }

    private Theme findThemeById(EntityId themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.THEME_NOT_FOUND,
                        "테마를 조회할 수 없습니다. themeId = " + themeId
                ));
    }

    private void validateReservationBelongsToShop(Reservation reservation, EntityId shopId) {
        if (!shopId.equals(reservation.getShopId())) {
            throw new AuthorizationException("해당 매장의 예약이 아닙니다.");
        }
    }

    private void validateManageAuthority(User manager, EntityId shopId) {
        if (manager.role() != Role.MANAGER) {
            throw new AuthorizationException("매니저가 아닙니다.");
        }

        if (!Objects.equals(manager.managingShopId(), shopId)) {
            throw new AuthorizationException("해당 매장에 대한 관리 권한이 없습니다.");
        }
    }
}
