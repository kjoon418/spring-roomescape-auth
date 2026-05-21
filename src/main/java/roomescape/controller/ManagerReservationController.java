package roomescape.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireAuth;
import roomescape.auth.Role;
import roomescape.auth.UserId;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationSummaryResponse;
import roomescape.controller.mapper.ReservationMapper;
import roomescape.domain.EntityId;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationCreateCommand;

@RestController
@RequestMapping("/manager/shops/{shopId}/reservations")
@RequireAuth(roles = {Role.MANAGER})
@RequiredArgsConstructor
public class ManagerReservationController {

    private final ReservationService service;

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
