package com.han.internproject.domain.auth.service;

import com.han.internproject.config.JwtUtil;
import com.han.internproject.domain.auth.dto.request.SigninRequestDto;
import com.han.internproject.domain.auth.dto.request.SignupRequestDto;
import com.han.internproject.domain.auth.dto.response.SigninResponseDto;
import com.han.internproject.domain.auth.dto.response.SignupResponseDto;
import com.han.internproject.domain.auth.exception.DuplicateUsernameException;
import com.han.internproject.domain.auth.exception.UnauthorizedPasswordException;
import com.han.internproject.domain.user.entity.User;
import com.han.internproject.domain.user.enums.UserRole;
import com.han.internproject.domain.user.exception.NotFoundUserException;
import com.han.internproject.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 회원가입
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        // 사용자 이름 중복 확인
        checkIfUsernameExists(signupRequestDto.getUsername());

        // Password Encoding
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        User newUser = new User(
                signupRequestDto.getUsername(),
                encodedPassword,
                signupRequestDto.getNickname(),
                UserRole.ROLE_USER
        );

        // UserRole -> AuthorityResponse 로 매핑
        List<SignupResponseDto.AuthorityResponse> authorityResponses = List.of(
                new SignupResponseDto.AuthorityResponse(newUser.getUserRole().getAuthorityName())
        );

        // DB 저장
        User savedUser = userRepository.save(newUser);

        return new SignupResponseDto(
                savedUser.getUsername(),
                savedUser.getNickname(),
                authorityResponses
        );
    }

    // 로그인
    public SigninResponseDto signin(@Valid SigninRequestDto signinRequestDto) {
        // 사용자 확인
        User user = findUserByUsername(signinRequestDto.getUsername());

        // 비밀번호 검증
        validatePassword(signinRequestDto.getPassword(), user.getPassword());

        // 토큰 발급
        String bearerToken = jwtUtil.createAccessToken(user.getId(), user.getUsername(), user.getUserRole());
        jwtUtil.createRefreshToken(user.getId(), user.getUsername(), user.getUserRole());

        return new SigninResponseDto(bearerToken);
    }

    // 사용자 이름 존재 여부 확인
    private void checkIfUsernameExists(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException();
        }
    }

    // 사용자 조회
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);
    }

    // 비밀번호 검증
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new UnauthorizedPasswordException();
        }
    }
}
