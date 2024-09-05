package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.config.filter.JwtUtil;
import com.sparta.newsfeed.config.passwordconfig.PasswordEncoder;
import com.sparta.newsfeed.config.passwordconfig.PasswordUtil;
import com.sparta.newsfeed.domain.dto.user.UserDto;
import com.sparta.newsfeed.domain.dto.user.UserLoginRequestDto;
import com.sparta.newsfeed.domain.dto.user.UserSignUpRequestDto;
import com.sparta.newsfeed.domain.entity.Token;
import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.exception.UnityException;
import com.sparta.newsfeed.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.newsfeed.domain.exception.ErrorCode.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;
    private final BlacklistTokenService blacklistTokenService;

    public UserService(UserRepository userRepository,
                       PasswordUtil passwordUtil,
                       PasswordEncoder passwordEncoder,
                       TokenService tokenService,
                       JwtUtil jwtUtil,
                       BlacklistTokenService blacklistTokenService
    ) {
        this.userRepository  = userRepository;
        this.passwordUtil = passwordUtil;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.blacklistTokenService = blacklistTokenService;
    }

    /**
     * 회원가입 메서드
     * @param userSignUnRequestDto 유저의 입력 정보
     */
    @Transactional
    public void signUp(UserSignUpRequestDto userSignUnRequestDto) {
        // 비밀번호 조건 확인
        if (!passwordUtil.isValidPassword(userSignUnRequestDto.getPassword())) {
            throw new UnityException(INVALID_PASSWORD_FORM);
        }

        // 이메일 중복 확인
        Optional<User> user = userRepository.findByEmail(userSignUnRequestDto.getEmail());
        if (user.isPresent()) {
            throw new UnityException(CONFLICT_EMAIL);
        }

        // 비밀번호 암호화 후 저장
        String bcryptPassword = passwordEncoder.encode(userSignUnRequestDto.getPassword());
        User newUser = new User(userSignUnRequestDto.getEmail(), bcryptPassword, userSignUnRequestDto.getName(), null);

        userRepository.save(newUser);
    }

    /**
     * 로그인 후 토큰을 반환하는 메서드
     * @param userLoginRequestDto 유저 로그인 입력값
     * @return 해당 유저의 Jwt토큰
     */
    @Transactional
    public String login(UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        // 유저 검증
        User user = userRepository.findByEmail(userLoginRequestDto.getEmail()).orElseThrow();

        if (!user.isActivate()) {
            throw new UnityException(USER_NOT_EXIST);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            throw new UnityException(WRONG_PASSWORD);
        }

        // 유저가 로그인 했으므로 토큰 갱신
        Token token = tokenService.createToken(user);
        jwtUtil.addJwtToCookie(response, token.getAccessToken());
        user.giveToken(token);

        return token.getAccessToken();
    }

    /**
     * 유저를 로그아웃 처리하는 메서드
     * @param userDto 유저 정보
     */
    @Transactional
    public void logout(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(
                ()-> new UnityException(USER_NOT_EXIST)
        );

        // 토큰을 Blacklist에 삽입
        addBlacklistToken(user.getToken().getAccessToken());
    }

    /**
     * 해당 토큰을 BlackList에 등록하는 메서드
     * @param token BlackList에 등록할 토큰
     */
    private void addBlacklistToken(String token) {
        blacklistTokenService.addBlacklistToken(token);
    }
    /**
     * 로그아웃 하는 메서드
     * @param userDto 현재 로그인 중인 유저 정보
     */
    @Transactional
    public void signOut(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow();
        user.signOut();

        addBlacklistToken(user.getToken().getAccessToken());
    }

    /**
     * id로 유저를 조회하는 메서드
     * @param id 유저 id
     * @return 조회한 유저 객체
     */
    @Transactional
    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UnityException(USER_NOT_EXIST));
    }
}
