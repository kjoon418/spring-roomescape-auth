package roomescape.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireAuth;
import roomescape.auth.Role;
import roomescape.controller.dto.ReservationTimeCreateRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.controller.mapper.ReservationTimeMapper;
import roomescape.domain.EntityId;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.ReservationTimeCreateCommand;

@RestController
@RequestMapping("/admin/shops/{shopId}/times")
@RequireAuth(roles = {Role.ADMIN})
@RequiredArgsConstructor
public class AdminReservationTimeController {

    private final ReservationTimeService service;
    private final ReservationTimeMapper mapper;

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @PathVariable UUID shopId,
            @RequestBody ReservationTimeCreateRequest createRequest
    ) {
        ReservationTimeCreateCommand createCommand = mapper.mapToCommand(createRequest, EntityId.fromUuid(shopId));
        ReservationTimeResponse response = service.create(createCommand);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findByShopId(
            @PathVariable UUID shopId
    ) {
        List<ReservationTimeResponse> responses = service.findByShopId(EntityId.fromUuid(shopId));

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
