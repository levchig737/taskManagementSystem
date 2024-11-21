package io.chekarev.taskManagementSystem.domain.dto;

import io.chekarev.taskManagementSystem.models.TaskPriority;
import io.chekarev.taskManagementSystem.models.TaskStatus;
import lombok.Data;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long authorId;
    private Long assigneeId;
}
