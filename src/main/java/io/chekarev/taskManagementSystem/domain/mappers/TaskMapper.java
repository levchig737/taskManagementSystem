package io.chekarev.taskManagementSystem.domain.mappers;

import io.chekarev.taskManagementSystem.domain.dto.TaskDTO;
import io.chekarev.taskManagementSystem.domain.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    TaskDTO toDto(Task task);

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "assignee.id", source = "assigneeId")
    Task toEntity(TaskDTO taskDTO);
}
