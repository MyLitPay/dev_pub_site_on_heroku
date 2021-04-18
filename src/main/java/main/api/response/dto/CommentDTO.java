package main.api.response.dto;

import main.model.PostComment;

public class CommentDTO {
    private int id;
    private long timestamp;
    private String text;
    private UserInCommentDTO user;
    private Integer parentId;

    public CommentDTO() {
    }

    public CommentDTO(PostComment comment) {
        this.id = comment.getId();
        this.timestamp = (comment.getTime().getTime()) / 1000;
        this.text = comment.getText(); // Должен быть в формате HTML
        this.user = new UserInCommentDTO(comment.getUser());
        this.parentId = comment.getParentId();
    }

    public CommentDTO(int id, long timestamp, String text, UserInCommentDTO user, Integer parentId) {
        this.id = id;
        this.timestamp = timestamp;
        this.text = text;
        this.user = user;
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserInCommentDTO getUser() {
        return user;
    }

    public void setUser(UserInCommentDTO user) {
        this.user = user;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
