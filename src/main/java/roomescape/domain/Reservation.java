package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import org.springframework.util.StringUtils;
import roomescape.exception.ErrorCode;
import roomescape.exception.InvalidDomainStateException;
import roomescape.exception.NotAcceptableReservationException;

@Getter
public class Reservation {

    private final EntityId id;
    private final LocalDate date;
    private final boolean canceled;

    private final ReservationTime time;
    private final EntityId themeId;
    private final EntityId userId;
    private final EntityId shopId;

    private Reservation(
            EntityId id,
            LocalDate date,
            boolean canceled,
            ReservationTime time,
            EntityId themeId,
            EntityId userId,
            EntityId shopId
    ) {
        validateId(id);
        validateDate(date);
        validateTime(time);
        validateTheme(themeId);
        validateShopId(shopId);

        this.id = id;
        this.date = date;
        this.canceled = canceled;
        this.time = time;
        this.themeId = themeId;
        this.userId = userId;
        this.shopId = shopId;
    }

    public static Reservation create(
            EntityId id,
            LocalDate date,
            ReservationTime time,
            EntityId themeId,
            EntityId userId,
            EntityId shopId
    ) {
        validateFuture(date, time);

        boolean defaultCanceled = false;

        return new Reservation(
                id,
                date,
                defaultCanceled,
                time,
                themeId,
                userId,
                shopId
        );
    }

    public static Reservation retrieve(
            EntityId id,
            LocalDate date,
            boolean canceled,
            ReservationTime time,
            EntityId themeId,
            EntityId userId,
            EntityId shopId
    ) {
        return new Reservation(
                id,
                date,
                canceled,
                time,
                themeId,
                userId,
                shopId
        );
    }

    public static boolean isAvailable(LocalDate date, ReservationTime time) {
        return !isNotFuture(date, time);
    }

    public Reservation updateDateAndTime(LocalDate date, ReservationTime time) {
        validateUpdatable();
        validateFuture(date, time);

        return new Reservation(
                this.id,
                date,
                this.canceled,
                time,
                this.themeId,
                this.userId,
                this.shopId
        );
    }

    public Reservation updateCanceled(boolean canceled) {
        validateUpdatable();

        return new Reservation(
                this.id,
                this.date,
                canceled,
                this.time,
                this.themeId,
                this.userId,
                this.shopId
        );
    }

    private static void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidDomainStateException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 날짜가 존재해야 합니다."
            );
        }
    }

    private static void validateTime(ReservationTime time) {
        if (time == null) {
            throw new InvalidDomainStateException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 시간이 존재해야 합니다."
            );
        }
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new InvalidDomainStateException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 이름이 존재해야 합니다."
            );
        }
    }

    private static void validateFuture(LocalDate date, ReservationTime time) {
        if (isNotFuture(date, time)) {
            throw new NotAcceptableReservationException(
                    ErrorCode.PAST_RESERVATION,
                    "미래 시간의 예약만 생성/취소/수정할 수 있습니다."
                            + " 예약 희망 시간: " + date
                            + " 현재 시간: " + LocalDateTime.now()
            );
        }
    }

    private static boolean isNotFuture(LocalDate date, ReservationTime time) {
        if (date == null || time == null) {
            return true;
        }

        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.startAt());

        return reservationDateTime.isBefore(LocalDateTime.now());
    }

    private void validateId(EntityId id) {
        if (id == null) {
            throw new InvalidDomainStateException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 식별자가 존재해야 합니다."
            );
        }
    }

    private void validateTheme(EntityId themeId) {
        if (themeId == null) {
            throw new InvalidDomainStateException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 테마가 존재해야 합니다."
            );
        }
    }

    private void validateShopId(EntityId shopId) {
        if (shopId == null) {
            throw new InvalidDomainStateException(
                    ErrorCode.INVALID_RESERVATION,
                    "예약엔 샵이 존재해야 합니다."
            );
        }
    }

    private void validateUpdatable() {
        if (isNotFuture(date, time)) {
            throw new NotAcceptableReservationException(
                    ErrorCode.PAST_RESERVATION,
                    "과거의 예약은 수정할 수 없습니다."
                            + " 예약 시각: " + LocalDateTime.of(date, time.startAt())
                            + " 현재 시각: " + LocalDateTime.now()
            );
        }

        if (canceled) {
            throw new NotAcceptableReservationException(
                    ErrorCode.CANCELED_RESERVATION,
                    "취소된 예약은 수정할 수 없습니다."
            );
        }
    }

    public boolean isCancelable() {
        return !isNotFuture(date, time)
                && !canceled;
    }

    public EntityId getTimeId() {
        return time.id();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
