package com.example.demo.helpers;

import com.example.demo.models.PostDTO;
import com.example.demo.models.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public Post fromDto(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        return post;
    }

    public Post fromDto(int id, PostDTO postDTO) {
        Post post = fromDto(postDTO);
        post.setId(id);
        return post;
    }

    public PostDTO toDto(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        return postDTO;
    }
}
