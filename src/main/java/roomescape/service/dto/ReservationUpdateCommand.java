package roomescape.service.dto;

import java.time.LocalDate;
import roomescape.domain.EntityId;

public record ReservationUpdateCommand(
        EntityId reservationId,
        LocalDate date,
        EntityId timeId
) {
}
