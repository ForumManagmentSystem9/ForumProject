package com.example.demo.response;

import com.example.demo.models.Post;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostsResponse {
    private List<Post> top10MostCommented;
    private List<Post> top10Newest;
    private List<Post> allPosts;

    public PostsResponse(List<Post> top10MostCommented, List<Post> top10Newest) {
        this.top10MostCommented = top10MostCommented;
        this.top10Newest = top10Newest;
    }

    public PostsResponse(List<Post> allPosts){
        this.allPosts = allPosts;
    }
    public List<Post> getAllPosts() {
        return allPosts;
    }

    public void setAllPosts(List<Post> allPosts) {
        this.allPosts = allPosts;
    }
    public List<Post> getTop10MostCommented() {
        return top10MostCommented;
    }

    public void setTop10MostCommented(List<Post> top10MostCommented) {
        this.top10MostCommented = top10MostCommented;
    }

    public List<Post> getTop10Newest() {
        return top10Newest;
    }

    public void setTop10Newest(List<Post> top10Newest) {
        this.top10Newest = top10Newest;
    }
}
