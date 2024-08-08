package com.example.demo.service.test;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.helpers.RestrictHelper;
import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;
import com.example.demo.repositories.PostsRepository;
import com.example.demo.services.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private RestrictHelper helper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePost() {
        Post post = new Post();
        when(postsRepository.create(any(Post.class))).thenReturn(post);

        Post createdPost = postService.createPost(post);

        assertNotNull(createdPost);
        verify(postsRepository, times(1)).create(post);
    }

    @Test
    void testGetPostById() {
        Post post = new Post();
        when(postsRepository.findById(anyInt())).thenReturn(Optional.of(post));

        Post foundPost = postService.getPostById(1);

        assertNotNull(foundPost);
        verify(postsRepository, times(1)).findById(1);
    }

    @Test
    void testGetPostById_NotFound() {
        when(postsRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> postService.getPostById(1));
    }

    @Test
    void testGetAllPosts() {
        List<Post> posts = List.of(new Post(), new Post());
        when(postsRepository.findAll()).thenReturn(posts);

        List<Post> foundPosts = postService.getAllPosts();

        assertNotNull(foundPosts);
        assertEquals(2, foundPosts.size());
        verify(postsRepository, times(1)).findAll();
    }

    @Test
    void testDeletePostById() {
        Post post = new Post();
        User user = new User();
        when(postsRepository.findById(anyInt())).thenReturn(Optional.of(post));

        postService.deletePostById(1, user);

        verify(helper, times(1)).deletePermission(post, user);
        verify(postsRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeletePostById_NotFound() {
        int invalidPostId = 999;
        User user = new User();

        when(postsRepository.findById(invalidPostId)).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> {
            postService.deletePostById(invalidPostId, user);
        });
    }

    @Test
    void testGetTop10MostCommentedPosts() {
        List<Post> posts = List.of(new Post(), new Post());
        when(postsRepository.findTop10ByOrderByCommentsCountDesc()).thenReturn(posts);

        List<Post> topPosts = postService.getTop10MostCommentedPosts();

        assertNotNull(topPosts);
        assertEquals(2, topPosts.size());
        verify(postsRepository, times(1)).findTop10ByOrderByCommentsCountDesc();
    }

    @Test
    void testGetTop10NewestPosts() {
        List<Post> posts = List.of(new Post(), new Post());
        when(postsRepository.findTop10ByOrderByCreatedDateDesc()).thenReturn(posts);

        List<Post> topPosts = postService.getTop10NewestPosts();

        assertNotNull(topPosts);
        assertEquals(2, topPosts.size());
        verify(postsRepository, times(1)).findTop10ByOrderByCreatedDateDesc();
    }

    @Test
    void testGetPostsByUserId() {
        List<Post> posts = List.of(new Post(), new Post());
        when(postsRepository.findByUserId(anyInt())).thenReturn(posts);

        List<Post> userPosts = postService.getPostsByUserId(1);

        assertNotNull(userPosts);
        assertEquals(2, userPosts.size());
        verify(postsRepository, times(1)).findByUserId(1);
    }

    @Test
    void testGetPostsByTitleContaining() {
        List<Post> posts = List.of(new Post(), new Post());
        when(postsRepository.findByTitleContaining(anyString())).thenReturn(posts);

        List<Post> foundPosts = postService.getPostsByTitleContaining("title");

        assertNotNull(foundPosts);
        assertEquals(2, foundPosts.size());
        verify(postsRepository, times(1)).findByTitleContaining("title");
    }

    @Test
    void testUpdatePost() {
        Post post = new Post();
        post.setId(1);
        User user = new User();
        Post existingPost = new Post();

        when(postsRepository.findById(anyInt())).thenReturn(Optional.of(existingPost));

        postService.updatePost(post, user);

        verify(helper, times(1)).isUserACreator(post, user);
        verify(postsRepository, times(1)).update(existingPost);
    }

    @Test
    void testUpdatePost_NotFound() {
        Post post = new Post();
        post.setId(1);
        User user = new User();

        when(postsRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> postService.updatePost(post, user));
    }

    @Test
    void testLikePost() {
        Post post = new Post();
        User user = new User();

        when(postsRepository.findById(anyInt())).thenReturn(Optional.of(post));

        postService.like(1, user);

        verify(postsRepository, times(1)).addLike(post, user);
    }

    @Test
    void testLikePost_NotFound() {
        User user = new User();

        when(postsRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> postService.like(1, user));
    }
}
