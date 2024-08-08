package com.example.demo.controlers.tests;

import com.example.demo.controllers.PostRestController;
import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.helpers.AuthorizationHelper;
import com.example.demo.helpers.PostMapper;
import com.example.demo.models.Post;
import com.example.demo.models.PostDTO;
import com.example.demo.models.userfolder.User;
import com.example.demo.response.PostsResponse;
import com.example.demo.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class PostRestControllerTest {

    private PostService postService;
    private PostMapper postMapper;
    private AuthorizationHelper authorizationHelper;
    private PostRestController postRestController;
    private Authentication authentication;
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        postService = mock(PostService.class);
        postMapper = mock(PostMapper.class);
        authorizationHelper = mock(AuthorizationHelper.class);
        postRestController = new PostRestController(postService, postMapper, authorizationHelper);

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAllPosts_Authenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authorizationHelper.extractUserFromToken(authentication)).thenReturn(new User());

        List<Post> allPosts = Collections.singletonList(new Post());
        PostsResponse mockResponse = new PostsResponse(allPosts);

        when(postService.getAllPosts()).thenReturn(allPosts);

        ResponseEntity<PostsResponse> response = postRestController.getAllPosts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getAllPosts().size());
    }


    @Test
    void testGetPostById_Success() {
        Post post = new Post();
        when(postService.getPostById(anyInt())).thenReturn(post);

        Post result = postRestController.getPostById(1);

        assertNotNull(result);
        assertEquals(post, result);
    }

    @Test
    void testCreatePost_AuthorizationException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authorizationHelper.extractUserFromToken(authentication)).thenThrow(new AuthorizationException("Unauthorized"));

        PostDTO postDTO = new PostDTO();

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            postRestController.createPost(postDTO);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatusCode());
        assertEquals("Unauthorized", thrown.getReason());
    }

    @Test
    void testUpdatePost_EntityNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authorizationHelper.extractUserFromToken(authentication)).thenReturn(new User());
        when(postMapper.fromDto(anyInt(), any(PostDTO.class), any(User.class))).thenReturn(new Post());
        doThrow(new EntityNotFoundException("Not Found")).when(postService).updatePost(any(Post.class), any(User.class));

        PostDTO postDTO = new PostDTO();

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            postRestController.updatePost(1, postDTO);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("Not Found", thrown.getReason());
    }

    @Test
    void testDeletePost_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authorizationHelper.extractUserFromToken(authentication)).thenReturn(new User());

        assertDoesNotThrow(() -> postRestController.deletePost(1));
        verify(postService, times(1)).deletePostById(anyInt(), any(User.class));
    }

    @Test
    void testLikePost_EntityNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authorizationHelper.extractUserFromToken(authentication)).thenReturn(new User());
        doThrow(new EntityNotFoundException("Post not found")).when(postService).like(anyInt(), any(User.class));

        ResponseEntity<String> response = postRestController.likePost(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Post not found", response.getBody());
    }

}
