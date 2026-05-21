package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.NoRequireAuth;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Duration;
import roomescape.repository.dto.ReservedTheme;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/shops/{shopId}/themes")
@NoRequireAuth
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService service;

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAll(
            @PathVariable UUID shopId
    ) {
        List<ThemeResponse> responses = service.findByShopId(shopId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/most-reserved")
    public ResponseEntity<List<ReservedTheme>> findMostReserved(
            @PathVariable UUID shopId,
            @RequestParam int limit,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        Duration duration = new Duration(startDate, endDate);
        List<ReservedTheme> responses = service.findMostReserved(shopId, limit, duration);

        return ResponseEntity.ok(responses);
    }
}
