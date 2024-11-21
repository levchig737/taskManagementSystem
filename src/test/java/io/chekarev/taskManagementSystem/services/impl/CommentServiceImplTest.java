package io.chekarev.taskManagementSystem.services.impl;

import io.chekarev.taskManagementSystem.domain.dto.CommentDTO;
import io.chekarev.taskManagementSystem.domain.entities.Comment;
import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.domain.mappers.CommentMapper;
import io.chekarev.taskManagementSystem.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private Comment comment;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");
        comment.setUser(user);

        commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setText("Test comment DTO");
        commentDTO.setUserId(user.getId());
    }

    @Test
    void testAddComment() {
        when(commentMapper.toEntity(commentDTO)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentDTO);

        CommentDTO result = commentService.addComment(commentDTO);

        assertNotNull(result);
        assertEquals(commentDTO.getText(), result.getText());
    }

    @Test
    void testDeleteComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(commentRepository, Mockito.times(1)).delete(comment);
    }

    @Test
    void testDeleteCommentForCurrentUser() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteCommentForCurrentUser(1L, user);

        verify(commentRepository, Mockito.times(1)).delete(comment);
    }
}
