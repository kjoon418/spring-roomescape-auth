package roomescape.controller.dto;

import java.time.LocalTime;
import java.util.UUID;

public record ReservationTimeCreateRequest(
        LocalTime startAt,
        UUID shopId
) {
}
