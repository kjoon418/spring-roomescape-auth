package roomescape.auth;

public record UserSession(
        String userId,
        String role
) {
}
