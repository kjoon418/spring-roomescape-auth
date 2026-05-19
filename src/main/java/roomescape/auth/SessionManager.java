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
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_INFO_KEY, userId);
    }
}
