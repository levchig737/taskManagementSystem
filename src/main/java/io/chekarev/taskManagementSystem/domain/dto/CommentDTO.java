package io.chekarev.taskManagementSystem.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private Long taskId;
    private Long userId;
    private LocalDateTime createdAt;
}
