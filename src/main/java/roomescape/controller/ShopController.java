package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.UserId;
import roomescape.controller.dto.ManagedShopResponse;
import roomescape.domain.EntityId;
import roomescape.service.ShopService;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/managed")
    public ResponseEntity<ManagedShopResponse> findManagedShop(
            @UserId EntityId userId
    ) {
        ManagedShopResponse response = shopService.findManagedShop(userId);

        return ResponseEntity.ok(response);
    }
}
