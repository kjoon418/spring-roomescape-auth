package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.UserResponse;
import roomescape.domain.EntityId;
import roomescape.domain.User;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ErrorCode;
import roomescape.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse login(String loginId, String password) {
        User user = userRepository.findByLoginIdAndPassword(loginId, password)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "아이디와 비밀번호로 회원을 조회할 수 없습니다."
                                + " loginId: " + loginId
                                + " password: " + password
                ));

        return new UserResponse(
                user.id(),
                user.role()
        );
    }

    @Transactional(readOnly = true)
    public UserResponse findById(EntityId userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "식별자로 회원을 조회할 수 없습니다."
                                + " userId: " + userId
                ));

        return new UserResponse(
                user.id(),
                user.role()
        );
    }
}
