package com.example.demo.helpers;

import com.example.demo.models.Comment;
import com.example.demo.models.CommentDTO;
import com.example.demo.models.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class CommentMapper {

    private static final Logger logger = LoggerFactory.getLogger(PostMapper.class);

    public Comment fromDto(CommentDTO commentDTO, User user) {
        if (commentDTO == null) {
            logger.error("Comment is null");
            throw new IllegalArgumentException("Comment cannot be null");
        }
        if (user == null) {
            logger.error("User is null");
            throw new IllegalArgumentException("User cannot be null");
        }

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setLikes(0);
        comment.setUser(user);

        logger.debug("Created Comment from CommentDTO: {}", comment);

        return comment;
    }

    public Comment fromDto(int id, CommentDTO commentDTO, User user) {
        if (id <= 0) {
            logger.error("Invalid comment id: {}", id);
            throw new IllegalArgumentException("Post id must be greater than 0");
        }

        Comment comment = fromDto(commentDTO, user);
        comment.setId(id);

        logger.debug("Created Post with id from PostDTO: {}", comment);

        return comment;
    }

    public CommentDTO toDto(Comment comment) {
        if (comment == null) {
            logger.error("Comment is null");
            throw new IllegalArgumentException("Comment cannot be null");
        }

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(comment.getContent());

        logger.debug("Converted Comment to CommentDTO: {}", commentDTO);

        return commentDTO;
    }
}

