package com.taskmanagement.mappers;

import com.taskmanagement.dto.CommentDto;
import com.taskmanagement.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "user.email", target = "userEmail")
    CommentDto commentToCommentDto(Comment comment);
}
