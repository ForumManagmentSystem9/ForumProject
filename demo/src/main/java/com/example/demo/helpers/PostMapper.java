package com.example.demo.helpers;

import com.example.demo.models.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PostMapper {

    private static final Logger logger = LoggerFactory.getLogger(PostMapper.class);

    public Post fromDto(PostDTO postDTO, User user) {
        if (postDTO == null) {
            logger.error("PostDTO is null");
            throw new IllegalArgumentException("PostDTO cannot be null");
        }
        if (user == null) {
            logger.error("User is null");
            throw new IllegalArgumentException("User cannot be null");
        }

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUser(user);
        post.setCreatedDate(postDTO.getCreatedDate());

        logger.debug("Created Post from PostDTO: {}", post);

        return post;
    }

    public Post fromDto(int id, PostDTO postDTO, User user) {
        if (id <= 0) {
            logger.error("Invalid post id: {}", id);
            throw new IllegalArgumentException("Post id must be greater than 0");
        }

        Post post = fromDto(postDTO, user);
        post.setId(id);

        logger.debug("Created Post with id from PostDTO: {}", post);

        return post;
    }

    public PostDTO toDto(Post post) {
        if (post == null) {
            logger.error("Post is null");
            throw new IllegalArgumentException("Post cannot be null");
        }

        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());

        logger.debug("Converted Post to PostDTO: {}", postDTO);

        return postDTO;
    }
}
