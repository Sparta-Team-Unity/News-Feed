package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.config.passwordconfig.PasswordEncoder;
import com.sparta.newsfeed.config.passwordconfig.PasswordUtil;
import com.sparta.newsfeed.domain.dto.profile.ProfileSaveRequestDto;
import com.sparta.newsfeed.domain.dto.profile.ProfileViewResponseDto;
import com.sparta.newsfeed.domain.dto.user.UserDto;
import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.exception.ErrorCode;
import com.sparta.newsfeed.domain.exception.UnityException;
import com.sparta.newsfeed.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordUtil passwordUtil;

    /**
     * 유저 정보를 조회하는 메서드
     * @param userId 조회할 유저 Id
     * @param userDto 로그인 중인 유저 Id
     * @return 조회한 유저 객체
     */
    @Transactional(readOnly = true)
    public ProfileViewResponseDto viewUsersProfile(Integer userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnityException(ErrorCode.USER_NOT_EXIST));
        User loginUser = userRepository.findById(userDto.getId()).orElseThrow(() -> new UnityException(ErrorCode.USER_NOT_EXIST));

        ProfileViewResponseDto profileViewResponseDto = new ProfileViewResponseDto(user.getEmail(), user.getCreateAt());

        if (!user.getUserId().equals(loginUser.getUserId())) {
            profileViewResponseDto.maskPrivacy();
        }

        return profileViewResponseDto;
    }

    /**
     * 유저 정보를 갱신하는 메서드 ( 비밀번호 변경 )
     * @param profileSaveRequestDto 변경할 내용 객체
     * @param userDto 로그인 중인 유저 정보
     */
    @Transactional
    public void updateProfile(ProfileSaveRequestDto profileSaveRequestDto, UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new UnityException(ErrorCode.USER_NOT_EXIST));

        if (!passwordEncoder.matches(profileSaveRequestDto.getCurrentPassword(), user.getPassword())) {
            throw new UnityException(ErrorCode.MISS_MATCH_PASSWORD);
        }
        if(profileSaveRequestDto.getCurrentPassword().equals(profileSaveRequestDto.getNewPassword())) {
            throw new UnityException(ErrorCode.DUPLICATE_PASSWORD);
        }
        if (!passwordUtil.isValidPassword(profileSaveRequestDto.getNewPassword())) {
            throw new UnityException(ErrorCode.INVALID_PASSWORD_FORM);
        }
        user.updatePassword(passwordEncoder.encode(profileSaveRequestDto.getNewPassword()));
        userRepository.save(user);
    }
}
