package com.han.internproject.domain.auth.service;

import com.han.internproject.domain.auth.dto.request.SignupRequestDto;
import com.han.internproject.domain.auth.dto.response.SignupResponseDto;
import com.han.internproject.domain.auth.exception.DuplicateUsernameException;
import com.han.internproject.domain.user.entity.User;
import com.han.internproject.domain.user.enums.UserRole;
import com.han.internproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    private SignupRequestDto signupRequestDto;
    private String encodedPassword;
    private User newUser;
    private User savedUser;

    @BeforeEach
    void setUp() {
        signupRequestDto = new SignupRequestDto("username", "password", "nickname");
        encodedPassword = "encodedPassword";
        newUser = new User(signupRequestDto.getUsername(), encodedPassword, signupRequestDto.getNickname(), UserRole.ROLE_USER);
        savedUser = new User(1L, signupRequestDto.getUsername(), encodedPassword, signupRequestDto.getNickname(), UserRole.ROLE_USER);
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
    void signin() {
    }
}