package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final SessionManager sessionManager;

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws IOException {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }
        if (isNotRequireAuth(method)) {
            return true;
        }

        UserSession userInfo = sessionManager.getUserInfo(request);
        RequireAuth requireAuth = getRequireAuth(method);
        Role userRole = Role.valueOf(userInfo.role());

        boolean hasNotRole = Arrays.stream(requireAuth.roles())
                .noneMatch(role -> role == userRole);
        if (hasNotRole) {
            throw new AuthorizationException("권한이 부족합니다.");
        }

        return true;
    }

    private boolean isNotRequireAuth(HandlerMethod method) {
        RequireAuth requireAuth = getRequireAuth(method);

        return requireAuth == null
                || requireAuth.roles().length == 0;
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
