package com.example.demo.helpers;

import com.example.demo.models.PostDTO;
import com.example.demo.models.Posts;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public Posts fromDto(PostDTO postDTO) {
        Posts post = new Posts();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        return post;
    }

    public Posts fromDto(int id, PostDTO postDTO) {
        Posts post = fromDto(postDTO);
        post.setId(id);
        return post;
    }

    public PostDTO toDto(Posts post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        return postDTO;
    }
}
