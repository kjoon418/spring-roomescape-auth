package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.SessionManager;
import roomescape.controller.dto.LoginRequestDto;
import roomescape.domain.EntityId;
import roomescape.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final SessionManager sessionManager;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        EntityId userId = userService.login(requestDto.loginId(), requestDto.password());
        sessionManager.saveUserInfo(userId, servletRequest);

        return ResponseEntity.ok().build();
    }
}
