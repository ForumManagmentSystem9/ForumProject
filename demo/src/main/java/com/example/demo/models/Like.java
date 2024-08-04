package com.example.demo.models;

import com.example.demo.models.Post;
import com.example.demo.models.userfolder.User;
import jakarta.persistence.*;

@Entity
@Table(name = "post_likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int post_like_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Like(){};

    public int getId() {
        return post_like_id;
    }

    public void setId(int post_like_id) {
        this.post_like_id = post_like_id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

