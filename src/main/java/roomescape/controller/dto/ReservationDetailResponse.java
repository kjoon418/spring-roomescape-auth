package roomescape.controller.dto;

import java.time.LocalDate;

public record ReservationDetailResponse(
        String id,
        LocalDate date,
        boolean canceled,
        boolean cancelable,
        ReservationTimeResponse time,
        ThemeResponse theme,
        String userName
) {
}
