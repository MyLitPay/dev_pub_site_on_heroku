package main.api.response.dto;

import main.model.Post;

public class PostDTO {

    private int id;
    private long timestamp;
    private UserInPostDTO user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

    public PostDTO() {
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.timestamp = (post.getTime().getTime()) / 1000;
        this.user = new UserInPostDTO(post.getUser());
        this.title = post.getTitle();
        this.announce = post.getAnnounce();
        this.likeCount = post.getVotes().get(1);
        this.dislikeCount = post.getVotes().get(0);
        this.commentCount = post.getPostCommentList().size();
        this.viewCount = post.getViewCount();
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

    public UserInPostDTO getUser() {
        return user;
    }

    public void setUser(UserInPostDTO user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
