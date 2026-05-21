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
import roomescape.auth.RequireManager;
import roomescape.auth.UserId;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.domain.EntityId;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/manager/reservations")
@RequireManager
@RequiredArgsConstructor
public class ManagerReservationController {

    private final ReservationService service;

    @GetMapping
    public ResponseEntity<List<ReservationDetailResponse>> findByShopId(
            @UserId EntityId managerId,
            @RequestParam UUID shopId
    ) {
        List<ReservationDetailResponse> responses = service.findAllByShopId(
                managerId,
                EntityId.fromUuid(shopId)
        );

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> delete(
            @RequestParam UUID shopId,
            @PathVariable UUID reservationId
    ) {
        service.delete(EntityId.fromUuid(shopId), EntityId.fromUuid(reservationId));

        return ResponseEntity.ok().build();
    }
}
