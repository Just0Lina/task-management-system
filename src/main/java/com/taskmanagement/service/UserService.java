package com.taskmanagement.service;


import com.taskmanagement.entity.User;
import com.taskmanagement.exception.validation.UserNotFoundException;
import com.taskmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> UserNotFoundException.notFoundUserWithId(id));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> UserNotFoundException.notFoundUserWithEmail(email));
    }
}