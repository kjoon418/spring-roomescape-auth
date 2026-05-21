package roomescape.service.dto;

import java.time.LocalDate;
import roomescape.domain.EntityId;

public record ReservationCreateCommand(
        LocalDate date,
        EntityId timeId,
        EntityId themeId,
        EntityId userId,
        EntityId shopId
) {
}
