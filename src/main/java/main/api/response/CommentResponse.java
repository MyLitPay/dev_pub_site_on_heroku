package main.api.response;

import main.model.PostComment;

public class CommentResponse {
    private int id;

    public CommentResponse() {
    }

    public CommentResponse(PostComment comment) {
        this.id = comment.getId();
    }

    public CommentResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
