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
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.controller.mapper.ThemeMapper;
import roomescape.domain.EntityId;
import roomescape.service.ThemeService;
import roomescape.service.dto.ThemeCreateCommand;

@RestController
@RequestMapping("/admin/themes")
@RequireAuth(roles = {Role.ADMIN})
@RequiredArgsConstructor
public class AdminThemeController {

    private final ThemeService service;
    private final ThemeMapper mapper;

    @PostMapping
    public ResponseEntity<ThemeResponse> create(
            @RequestBody ThemeCreateRequest createRequest
    ) {
        ThemeCreateCommand createCommand = mapper.mapToCommand(createRequest);
        ThemeResponse response = service.create(createCommand);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(EntityId.fromUuid(id));

        return ResponseEntity.ok().build();
    }
}
