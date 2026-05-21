package roomescape.controller.dto;

import roomescape.auth.Role;
import roomescape.domain.EntityId;

public record UserResponse(
        EntityId id,
        Role role,
        String managingShopId
) {
}
