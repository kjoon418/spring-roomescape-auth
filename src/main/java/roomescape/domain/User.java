package roomescape.domain;

public record User(
        EntityId id,
        String loginId,
        String password,
        String name
) {
}
