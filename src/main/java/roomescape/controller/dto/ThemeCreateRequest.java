package roomescape.controller.dto;

import java.util.UUID;

public record ThemeCreateRequest(
        String name,
        String description,
        String imageUrl,
        UUID shopId
) {
}
