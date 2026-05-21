package roomescape.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.AuthorizationException;
import roomescape.auth.Role;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Duration;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.User;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ErrorCode;
import roomescape.exception.InUseEntityException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.UserRepository;
import roomescape.repository.dto.ReservedTheme;
import roomescape.service.dto.ThemeCreateCommand;
import roomescape.service.mapper.ThemeResponseMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ThemeResponseMapper themeResponseMapper;

    @Transactional
    public ThemeResponse create(
            EntityId actorId,
            ThemeCreateCommand command
    ) {
        User actor = findUserById(actorId);
        if (actor.role() == Role.MANAGER) {
            validateManageAuthority(actor, command.shopId());
        } else if (actor.role() != Role.ADMIN) {
            throw new AuthorizationException("테마 생성 권한이 없습니다.");
        }

        EntityId id = EntityId.random();
        Theme theme = new Theme(
                id,
                command.name(),
                command.description(),
                command.imageUrl(),
                command.shopId()
        );

        Theme persisted = themeRepository.persist(theme);
        return themeResponseMapper.map(persisted);
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> findByShopId(UUID shopId) {
        return themeRepository.findByShopId(EntityId.fromUuid(shopId))
                .stream()
                .map(themeResponseMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservedTheme> findMostReserved(
            UUID shopId,
            long limit,
            Duration duration
    ) {
        List<Reservation> reservations = reservationRepository.findBetweenDurationAndShopId(duration, EntityId.fromUuid(shopId));
        Map<EntityId, Long> themeReservedCounts = collectCountByThemeId(reservations);
        Map<EntityId, Theme> themes = themeRepository.findByThemeIdsAndShopId(
                themeReservedCounts.keySet(),
                EntityId.fromUuid(shopId)
        );

        return themeReservedCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<EntityId, Long>comparingByValue().reversed())
                .limit(limit)
                .map(countEntry -> mapToReservedTheme(countEntry, themes))
                .toList();
    }

    @Transactional
    public void delete(
            EntityId actorId,
            EntityId shopId,
            EntityId themeId
    ) {
        User actor = findUserById(actorId);
        Theme theme = findThemeById(themeId);

        if (actor.role() == Role.MANAGER) {
            validateManageAuthority(actor, shopId);
            validateThemeOwnership(shopId, theme);
        } else if (actor.role() != Role.ADMIN) {
            throw new AuthorizationException("테마 삭제 권한이 없습니다.");
        }

        validateThemeNotUsed(themeId);

        boolean deleted = themeRepository.delete(themeId);
        validateDeleted(deleted, themeId);
    }

    private void validateManageAuthority(User manager, EntityId shopId) {
        if (manager.role() != Role.MANAGER) {
            throw new AuthorizationException("매니저가 아닙니다.");
        }

        if (!Objects.equals(manager.managingShopId(), shopId)) {
            throw new AuthorizationException("해당 매장에 대한 관리 권한이 없습니다.");
        }
    }

    private void validateThemeOwnership(EntityId shopId, Theme theme) {
        if (!shopId.equals(theme.shopId())) {
            throw new AuthorizationException("해당 테마에 대한 접근 권한이 없습니다.");
        }
    }

    private void validateThemeNotUsed(EntityId themeId) {
        if (reservationRepository.existByThemeId(themeId)) {
            throw new InUseEntityException(
                    ErrorCode.THEME_IN_USE,
                    "사용되지 않는 테마만 제거할 수 있습니다. themeId = " + themeId.getValueAsString()
            );
        }
    }

    private void validateDeleted(boolean deleted, EntityId themeId) {
        if (!deleted) {
            throw new EntityNotFoundException(
                    ErrorCode.THEME_NOT_FOUND,
                    "삭제할 테마를 조회하지 못했습니다. themeId = " + themeId
            );
        }
    }

    private Map<EntityId, Long> collectCountByThemeId(List<Reservation> reservations) {
        return reservations.stream()
                .collect(Collectors.groupingBy(
                        Reservation::getThemeId,
                        Collectors.counting()
                ));
    }

    private ReservedTheme mapToReservedTheme(
            Map.Entry<EntityId, Long> themeReservedCount,
            Map<EntityId, Theme> themes
    ) {
        EntityId themeId = themeReservedCount.getKey();
        Theme theme = themes.get(themeId);

        if (theme == null) {
            throw new EntityNotFoundException(
                    ErrorCode.THEME_NOT_FOUND,
                    "테마를 조회할 수 없습니다. themeId = " + themeId
            );
        }

        return new ReservedTheme(
                theme.id().getValueAsString(),
                theme.name(),
                theme.description(),
                theme.imageUrl(),
                themeReservedCount.getValue()
        );
    }

    private Theme findThemeById(EntityId themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.THEME_NOT_FOUND,
                        "테마를 조회할 수 없습니다. themeId = " + themeId
                ));
    }

    private User findUserById(EntityId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "식별자로 회원을 조회할 수 없습니다."
                ));
    }
}
