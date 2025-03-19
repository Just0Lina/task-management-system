package com.taskmanagement.mappers;

import com.taskmanagement.dto.UserPasswordDto;
import com.taskmanagement.entity.User;
import com.taskmanagement.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", imports = {UserRole.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(UserRole.USER)")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.password()))")
    User toEntity(UserPasswordDto dto, PasswordEncoder passwordEncoder);

}
