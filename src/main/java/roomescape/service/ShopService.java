package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.AuthorizationException;
import roomescape.auth.Role;
import roomescape.controller.dto.ManagedShopResponse;
import roomescape.controller.dto.ShopResponse;
import roomescape.domain.EntityId;
import roomescape.domain.Shop;
import roomescape.domain.User;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ShopRepository;
import roomescape.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    @Transactional(readOnly = true)
    public List<ShopResponse> findAll() {
        return shopRepository.findAll().stream()
                .map(shop -> new ShopResponse(shop.id().getValueAsString(), shop.name()))
                .toList();
    }

    @Transactional(readOnly = true)
    public ManagedShopResponse findManagedShop(EntityId userId) {
        User user = findUserById(userId);
        Shop shop = findManagingShopOfUser(user);

        return new ManagedShopResponse(
                shop.id().getValueAsString(),
                shop.name()
        );
    }

    private Shop findManagingShopOfUser(User user) {
        validateManager(user);

        if (user.managingShopId() == null) {
            throw new EntityNotFoundException(
                    ErrorCode.SHOP_NOT_FOUND,
                    "관리 중인 매장이 없습니다. userId = " + user.id()
            );
        }

        return findShopById(user.managingShopId());
    }

    private void validateManager(User user) {
        if (user.role() != Role.MANAGER) {
            throw new AuthorizationException("매니저가 아닙니다.");
        }
    }

    private User findUserById(EntityId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "유저를 조회할 수 없습니다. userId = " + userId
                ));
    }

    private Shop findShopById(EntityId shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.SHOP_NOT_FOUND,
                        "매장을 조회할 수 없습니다. shopId = " + shopId
                ));
    }
}
