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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireManager;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.controller.mapper.ThemeMapper;
import roomescape.domain.EntityId;
import roomescape.service.ThemeService;
import roomescape.service.dto.ThemeCreateCommand;

@RestController
@RequestMapping("/manager/themes")
@RequireManager
@RequiredArgsConstructor
public class ManagerThemeController {

    private final ThemeService service;
    private final ThemeMapper mapper;

    @PostMapping
    public ResponseEntity<ThemeResponse> create(
            @RequestParam UUID shopId,
            @RequestBody ThemeCreateRequest createRequest
    ) {
        ThemeCreateCommand createCommand = mapper.mapToCommand(createRequest, EntityId.fromUuid(shopId));
        ThemeResponse response = service.create(createCommand);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity<Void> delete(
            @RequestParam UUID shopId,
            @PathVariable UUID themeId
    ) {
        service.delete(EntityId.fromUuid(shopId), EntityId.fromUuid(themeId));

        return ResponseEntity.ok().build();
    }
}
