package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.NoRequireAuth;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Duration;
import roomescape.repository.dto.ReservedTheme;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
@NoRequireAuth
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService service;

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAll() {
        List<ThemeResponse> responses = service.findAll();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/most-reserved")
    public ResponseEntity<List<ReservedTheme>> findMostReserved(
            @RequestParam int limit,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        Duration duration = new Duration(startDate, endDate);
        List<ReservedTheme> responses = service.findMostReserved(limit, duration);

        return ResponseEntity.ok(responses);
    }
}
