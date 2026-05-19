package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.EntityId;

@Component
@RequiredArgsConstructor
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final SessionManager sessionManager;

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(UserId.class);
        boolean isEntityId = EntityId.class.isAssignableFrom(parameter.getParameterType());

        return hasAnnotation && isEntityId;
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new IllegalStateException("HttpServletRequest를 가져올 수 없습니다.");
        }

        UserSession userInfo = sessionManager.getUserInfo(request);
        if (userInfo == null) {
            throw new AuthenticationException("인증에 실패했습니다.");
        }

        UUID userId = UUID.fromString(userInfo.userId());
        return EntityId.fromUuid(userId);
    }
}
