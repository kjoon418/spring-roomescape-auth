package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.EntityId;
import roomescape.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class ManagerValidateInterceptor implements HandlerInterceptor {

    private final SessionManager sessionManager;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws MissingServletRequestParameterException {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }
        RequireManager requireManager = getRequireManager(method);
        if (requireManager == null) {
            return true;
        }

        UserSession userInfo = findUserInfo(request);
        validateManager(userInfo);

        String shopId = findShopId(request, requireManager);
        validateManagerOfShop(userInfo.userId(), shopId);

        return true;
    }

    private RequireManager getRequireManager(HandlerMethod method) {
        RequireManager requireAuth = method.getMethodAnnotation(RequireManager.class);
        if (requireAuth != null) {
            return requireAuth;
        }

        return method.getBeanType()
                .getAnnotation(RequireManager.class);
    }

    private UserSession findUserInfo(HttpServletRequest request) {
        UserSession userInfo = sessionManager.getUserInfo(request);
        if (userInfo == null) {
            throw new AuthenticationException("인증에 실패했습니다.");
        }

        return userInfo;
    }

    private String findShopId(
            HttpServletRequest request,
            RequireManager requireManager
    ) throws MissingServletRequestParameterException {
        String shopIdParameterName = requireManager.shopIdParameterName();
        String shopId = request.getParameter(shopIdParameterName);
        if (shopId == null) {
            throw new MissingServletRequestParameterException(shopIdParameterName, UUID.class.getTypeName());
        }

        return shopId;
    }

    private EntityId parseStringToEntityId(String id) {
        try {
            return EntityId.fromUuid(UUID.fromString(id));
        } catch (IllegalArgumentException exception) {
            throw new AuthenticationException("부적절한 형태의 ID입니다. id = " + id);
        }
    }

    private void validateManager(UserSession userInfo) {
        Role role = Role.valueOf(userInfo.role());

        if (role != Role.MANAGER) {
            throw new AuthorizationException("매니저 권한이 없습니다.");
        }
    }

    private void validateManagerOfShop(String managerId, String shopId) {
        boolean isManagerOfShop = userRepository.existsByUserIdAndShopId(
                parseStringToEntityId(managerId),
                parseStringToEntityId(shopId)
        );

        if (!isManagerOfShop) {
            throw new AuthorizationException("해당 매장에 접근할 권한이 없습니다.");
        }
    }
}
