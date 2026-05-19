package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final SessionManager sessionManager;

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }
        if (isNotRequireAuth(method)) {
            return true;
        }

        UserSession userInfo = sessionManager.getUserInfo(request);
        if (userInfo == null) {
            throw new AuthenticationException("인증에 실패했습니다.");
        }

        return true;
    }

    private boolean isNotRequireAuth(HandlerMethod method) {
        return getRequireAuth(method) == null;
    }

    private RequireAuth getRequireAuth(HandlerMethod method) {
        RequireAuth requireAuth = method.getMethodAnnotation(RequireAuth.class);
        if (requireAuth != null) {
            return requireAuth;
        }

        return method.getBeanType()
                .getAnnotation(RequireAuth.class);
    }
}
