package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ErrorCode;
import roomescape.exception.InUseEntityException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ReservationTimeCreateCommand;
import roomescape.service.mapper.ReservationTimeResponseMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationTimeService {

    private final ReservationTimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeResponseMapper reservationTimeResponseMapper;

    @Transactional
    public ReservationTimeResponse create(
            ReservationTimeCreateCommand command
    ) {
        EntityId id = EntityId.random();
        ReservationTime reservationTime = new ReservationTime(id, command.startAt(), command.shopId());

        ReservationTime persisted = timeRepository.persist(reservationTime);
        return reservationTimeResponseMapper.map(persisted);
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeResponse> findByShopId(EntityId shopId) {
        return timeRepository.findByShopId(shopId)
                .stream()
                .map(reservationTimeResponseMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeResponse> findAvailableTimes(
            EntityId themeId,
            LocalDate date
    ) {
        List<Reservation> existReservations = reservationRepository.findNotCanceledByDateAndThemeId(date, themeId);
        Set<EntityId> usedTimeIds = existReservations.stream()
                .map(Reservation::getTimeId)
                .collect(Collectors.toUnmodifiableSet());

        // 테마의 매장에 속한 시간만 조회
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.THEME_NOT_FOUND,
                        "테마를 조회할 수 없습니다. themeId = " + themeId
                ));
        List<ReservationTime> shopTimes = timeRepository.findByShopId(theme.shopId());

        return shopTimes.stream()
                .filter(time -> Reservation.isAvailable(date, time))
                .filter(time -> isNotUsedTime(time, usedTimeIds))
                .map(reservationTimeResponseMapper::map)
                .toList();
    }

    @Transactional
    public void delete(EntityId timeId) {
        validateTimeNotUsed(timeId);

        boolean deleted = timeRepository.delete(timeId);
        validateDeleted(deleted, timeId);
    }

    private boolean isNotUsedTime(
            ReservationTime time,
            Set<EntityId> usedTimeIds
    ) {
        return !usedTimeIds.contains(time.id());
    }

    private void validateTimeNotUsed(EntityId timeId) {
        if (reservationRepository.existByTimeId(timeId)) {
            throw new InUseEntityException(
                    ErrorCode.TIME_IN_USE,
                    "사용되지 않는 시간만 제거할 수 있습니다. timeId = " + timeId.getValueAsString()
            );
        }
    }

    private void validateDeleted(boolean deleted, EntityId timeId) {
        if (!deleted) {
            throw new EntityNotFoundException(
                    ErrorCode.RESERVATION_TIME_NOT_FOUND,
                    "삭제할 시간을 조회하지 못했습니다. timeId = " + timeId
            );
        }
    }
}
