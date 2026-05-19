package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireAuth;
import roomescape.auth.Role;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.domain.EntityId;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
@RequireAuth(roles = {Role.MEMBER, Role.ADMIN})
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService service;

    @GetMapping("/available-times")
    public ResponseEntity<List<ReservationTimeResponse>> findAvailableTimes(
            @RequestParam UUID themeId,
            @RequestParam LocalDate date
    ) {
        List<ReservationTimeResponse> responses = service.findAvailableTimes(
                EntityId.fromUuid(themeId),
                date
        );

        return ResponseEntity.ok(responses);
    }
}
