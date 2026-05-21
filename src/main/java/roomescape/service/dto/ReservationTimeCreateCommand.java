package roomescape.service.dto;

import java.time.LocalTime;
import roomescape.domain.EntityId;

public record ReservationTimeCreateCommand(
        LocalTime startAt,
        EntityId shopId
) {
}
