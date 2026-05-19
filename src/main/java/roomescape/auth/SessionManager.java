package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import roomescape.domain.EntityId;

@Component
public class SessionManager {

    private static final String USER_INFO_KEY = "LOGIN_USER";

    public void saveUserInfo(
            EntityId userId,
            Role role,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(true);
        UserSession userSession = new UserSession(userId.getValueAsString(), role.name());

        session.setAttribute(USER_INFO_KEY, userSession);
    }

    public UserSession getUserInfo(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        return (UserSession) session.getAttribute(USER_INFO_KEY);
    }
}
