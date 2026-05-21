package roomescape.service.dto;

import roomescape.domain.EntityId;

public record ThemeCreateCommand(
        String name,
        String description,
        String imageUrl,
        EntityId shopId
) {
}
