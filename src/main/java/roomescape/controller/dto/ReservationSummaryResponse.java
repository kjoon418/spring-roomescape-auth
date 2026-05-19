package roomescape.controller.dto;

import java.time.LocalDate;

public record ReservationSummaryResponse(
        String id,
        LocalDate date,
        boolean canceled,
        String timeId,
        String themeId
) {
}
