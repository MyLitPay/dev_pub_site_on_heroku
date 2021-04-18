package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoteRequest {

    @JsonProperty("post_id")
    private int postId;

    public VoteRequest() {
    }

    public VoteRequest(int postId) {
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
