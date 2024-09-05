package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.config.PasswordEncoder;
import com.sparta.newsfeed.config.PasswordUtil;
import com.sparta.newsfeed.domain.dto.ProfileSaveRequestDto;
import com.sparta.newsfeed.domain.dto.ProfileViewResponseDto;
import com.sparta.newsfeed.domain.dto.UserDto;
import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.exception.*;
import com.sparta.newsfeed.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordUtil passwordUtil;

    /**
     *
     * @param userId
     * @param userDto
     * @return
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
