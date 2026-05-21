package roomescape.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireAuth;
import roomescape.auth.Role;
import roomescape.auth.UserId;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.domain.EntityId;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/manager/shops/{shopId}/reservations")
@RequireAuth(roles = {Role.MANAGER})
@RequiredArgsConstructor
public class ManagerReservationController {

    private final ReservationService service;

    @GetMapping
    public ResponseEntity<List<ReservationDetailResponse>> findByShopId(
            @UserId EntityId userId,
            @PathVariable UUID shopId
    ) {
        List<ReservationDetailResponse> responses = service.findAllByShopId(
                userId,
                EntityId.fromUuid(shopId)
        );

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @UserId EntityId userId,
            @PathVariable UUID shopId,
            @PathVariable UUID id
    ) {
        service.delete(userId, EntityId.fromUuid(shopId), EntityId.fromUuid(id));

        return ResponseEntity.ok().build();
    }
}
