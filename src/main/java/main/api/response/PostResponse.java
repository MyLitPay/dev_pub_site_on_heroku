package main.api.response;

import main.api.response.dto.PostDTO;

import java.util.List;

public class PostResponse {

    private long count;
    private List<PostDTO> posts;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }
}
