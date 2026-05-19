package roomescape.domain;

import roomescape.auth.Role;

public record User(
        EntityId id,
        String loginId,
        String password,
        String name,
        Role role
) {
}
