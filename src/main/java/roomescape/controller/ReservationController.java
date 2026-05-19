package roomescape.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireAuth;
import roomescape.auth.Role;
import roomescape.auth.UserId;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationDetailResponse;
import roomescape.controller.dto.ReservationSummaryResponse;
import roomescape.controller.dto.ReservationUpdateRequest;
import roomescape.controller.mapper.ReservationMapper;
import roomescape.domain.EntityId;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationCreateCommand;
import roomescape.service.dto.ReservationUpdateCommand;

@RestController
@RequestMapping("/reservations")
@RequireAuth(roles = {Role.MEMBER, Role.ADMIN})
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;
    private final ReservationMapper mapper;

    @PostMapping
    public ResponseEntity<ReservationSummaryResponse> create(
            @RequestBody ReservationCreateRequest createRequest
    ) {
        ReservationCreateCommand createCommand = mapper.mapToCreateCommand(createRequest);
        ReservationSummaryResponse response = service.create(createCommand);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDetailResponse>> findByUserId(
            @UserId EntityId userId
    ) {
        List<ReservationDetailResponse> responses = service.findAllIncludeDetail(userId);

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationSummaryResponse> updateDateTime(
            @PathVariable UUID id,
            @RequestBody ReservationUpdateRequest updateRequest
    ) {
        ReservationUpdateCommand updateCommand = mapper.mapToUpdateCommand(
                EntityId.fromUuid(id),
                updateRequest
        );
        ReservationSummaryResponse response = service.update(updateCommand);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationSummaryResponse> cancel(
            @PathVariable UUID id
    ) {
        ReservationSummaryResponse response = service.cancel(EntityId.fromUuid(id));

        return ResponseEntity.ok(response);
    }
}
