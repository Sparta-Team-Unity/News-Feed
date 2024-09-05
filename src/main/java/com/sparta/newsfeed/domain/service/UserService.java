package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.config.JwtUtil;
import com.sparta.newsfeed.config.PasswordEncoder;
import com.sparta.newsfeed.config.PasswordUtil;
import com.sparta.newsfeed.domain.dto.UserLoginRequestDto;
import com.sparta.newsfeed.domain.dto.UserSignUpRequestDto;
import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordUtil passwordUtil, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository  = userRepository;
        this.passwordUtil = passwordUtil;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 회원가입 메서드
     * @param userSignUnRequestDto 유저의 입력 정보
     */
    @Transactional
    public void signUp(UserSignUpRequestDto userSignUnRequestDto) {
        // 비밀번호 조건 확인
        if (!passwordUtil.isValidPassword(userSignUnRequestDto.getPassword())) {
            throw new RuntimeException("회원가입 오류 뭐가 문제?");
        }

        // 이메일 중복 확인
        Optional<User> user = userRepository.findByEmail(userSignUnRequestDto.getEmail());
        if (user.isPresent()) {
            throw new RuntimeException("회원가입 email 문제?");
        }

        // 비밀번호 암호화 후 저장
        String bcryptPassword = passwordEncoder.encode(userSignUnRequestDto.getPassword());
        User newUser = new User(userSignUnRequestDto.getEmail(), bcryptPassword, userSignUnRequestDto.getName());

        userRepository.save(newUser);
    }

    /**
     * 로그인 후 토큰을 반환하는 메서드
     * @param userLoginRequestDto 유저 로그인 입력값
     * @return 해당 유저의 Jwt토큰
     */
    @Transactional(readOnly = true)
    public String login(UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        // 유저 검증
        User user = userRepository.findByEmail(userLoginRequestDto.getEmail()).orElseThrow();

        // 비밀번호 검증
        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            return null;
        }

        String token = jwtUtil.createToken(user.getUserId(), "Access Token");
        jwtUtil.addJwtToCookie(response, token);
        return token;
    }

    /**
     * 로그아웃 처리하는 함수 : cookie에 담겨져 있는 토큰을 제거
     * @param request Jwt토큰을 삭제할 Request객체
     * @param response 토큰이 갱신될 Response객체
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        jwtUtil.deleteJwtInCookie(request, response);
    }
}
