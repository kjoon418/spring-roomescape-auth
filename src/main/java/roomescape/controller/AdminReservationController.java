package roomescape.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireAuth;
import roomescape.auth.Role;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.domain.EntityId;
import roomescape.service.AdminReservationService;

@RestController
@RequestMapping("/admin/reservations")
@RequireAuth(roles = {Role.ADMIN})
@RequiredArgsConstructor
public class AdminReservationController {

    private final AdminReservationService service;

    @GetMapping
    public ResponseEntity<List<ReservationDetailResponse>> findByShopId(
            @RequestParam UUID shopId
    ) {
        List<ReservationDetailResponse> responses = service.findAllIncludeDetailByShopId(
                EntityId.fromUuid(shopId)
        );

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(EntityId.fromUuid(id));

        return ResponseEntity.ok().build();
    }
}
