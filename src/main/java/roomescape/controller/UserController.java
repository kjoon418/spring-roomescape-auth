package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireAuth;
import roomescape.auth.UserId;
import roomescape.controller.dto.UserResponse;
import roomescape.domain.EntityId;
import roomescape.service.UserService;

@RestController
@RequestMapping("/users")
@RequireAuth
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findUserInfo(
            @UserId EntityId userId
    ) {
        UserResponse response = userService.findById(userId);

        return ResponseEntity.ok(response);
    }
}
