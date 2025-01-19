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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    private SignupRequestDto signupRequestDto;
    private SigninRequestDto signinRequestDto;
    private String encodedPassword;
    private User savedUser;
    private String mockToken;

    @BeforeEach
    void setUp() {
        signupRequestDto = new SignupRequestDto("username", "password", "nickname");
        signinRequestDto = new SigninRequestDto("username", "password");
        encodedPassword = "encodedPassword";
        savedUser = new User(1L, signupRequestDto.getUsername(), encodedPassword, signupRequestDto.getNickname(), UserRole.ROLE_USER);
        mockToken = "mockBearerToken";
    }

    @Test
    void 회원가입_성공() {
        // given
        given(userRepository.existsByUsername(anyString())).willReturn(false);
        given(passwordEncoder.encode(signupRequestDto.getPassword())).willReturn(encodedPassword);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        SignupResponseDto signupResponseDto = authService.signup(signupRequestDto);

        // then
        assertThat(signupRequestDto.getUsername()).isEqualTo(signupResponseDto.getUsername());
    }

    @Test
    void 회원가입_실패_사용자이름_중복() {
        // given
        given(userRepository.existsByUsername(signupRequestDto.getUsername())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.signup(signupRequestDto))
                .isInstanceOf(DuplicateUsernameException.class);
    }

    @Test
    void 로그인_성공() {
        // given
        given(userRepository.findByUsername(signinRequestDto.getUsername())).willReturn(Optional.of(savedUser));
        given(passwordEncoder.matches(signinRequestDto.getPassword(), savedUser.getPassword())).willReturn(true);


        given(jwtUtil.createToken(savedUser.getId(), savedUser.getUsername(), savedUser.getUserRole())).willReturn(mockToken);

        // when
        SigninResponseDto signinResponseDto = authService.signin(signinRequestDto);

        // then
        assertThat(signinResponseDto.getToken()).isEqualTo(mockToken);
    }

    @Test
    void 로그인_실패_등록되지_않은_사용자() {
        // given
        given(userRepository.findByUsername(signinRequestDto.getUsername())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.signin(signinRequestDto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void 로그인_실패_비밀번호_불일치() {
        // given
        given(userRepository.findByUsername(signinRequestDto.getUsername())).willReturn(Optional.of(savedUser));
        given(passwordEncoder.matches(signinRequestDto.getPassword(), savedUser.getPassword())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.signin(signinRequestDto))
                .isInstanceOf(UnauthorizedPasswordException.class);
    }

}