package io.chekarev.taskManagementSystem.domain.mappers;

import io.chekarev.taskManagementSystem.domain.dto.CommentDTO;
import io.chekarev.taskManagementSystem.domain.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "user.id", target = "userId")
    CommentDTO toDto(Comment comment);

    @Mapping(target = "task.id", source = "taskId")
    @Mapping(target = "user.id", source = "userId")
    Comment toEntity(CommentDTO commentDTO);
}
