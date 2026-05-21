package roomescape.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.NoRequireAuth;
import roomescape.auth.RequireAuth;
import roomescape.auth.Role;
import roomescape.auth.UserId;
import roomescape.controller.dto.ManagedShopResponse;
import roomescape.controller.dto.ShopResponse;
import roomescape.domain.EntityId;
import roomescape.service.ShopService;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    @NoRequireAuth
    public ResponseEntity<List<ShopResponse>> findAll() {
        return ResponseEntity.ok(shopService.findAll());
    }

    @GetMapping("/managed")
    @RequireAuth(roles = {Role.MANAGER})
    public ResponseEntity<ManagedShopResponse> findManagedShop(
            @UserId EntityId userId
    ) {
        ManagedShopResponse response = shopService.findManagedShop(userId);

        return ResponseEntity.ok(response);
    }
}
