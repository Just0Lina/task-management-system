package com.taskmanagement.service;


import com.taskmanagement.dto.UserPasswordDto;
import com.taskmanagement.entity.User;
import com.taskmanagement.exception.validation.UserNotFoundException;
import com.taskmanagement.mappers.UserMapper;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.security.AuthServiceCommon;
import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> UserNotFoundException.notFoundUserWithId(id));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> UserNotFoundException.notFoundUserWithEmail(email));
    }

    public User addUser(UserPasswordDto userDto) throws AuthException, BadRequestException {
        if (userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new AuthException("User already exists");
        }
        AuthServiceCommon.checkRegisterConstraints(userDto);
        User user = userMapper.toEntity(userDto, passwordEncoder);
        return userRepository.save(user);
    }
}