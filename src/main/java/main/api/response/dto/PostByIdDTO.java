package main.api.response.dto;

import main.model.Post;
import main.model.PostComment;
import main.model.Tag;
import main.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostByIdDTO {
    private int id;
    private long timestamp;
    private boolean active;
    private UserInPostDTO user;
    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private List<CommentDTO> comments;
    private Set<String> tags;

    public PostByIdDTO() {
    }

    public PostByIdDTO(Post post) {
        this.id = post.getId();
        this.timestamp = (post.getTime().getTime()) / 1000;
        this.active = post.getIsActive() == 1;
        this.user = new UserInPostDTO(post.getUser());
        this.title = post.getTitle();
        this.text = post.getText(); // Должны быть в HTML
        this.likeCount = post.getVotes().get(1);
        this.dislikeCount = post.getVotes().get(0);
        this.viewCount = post.getViewCount();
        this.comments = post.getPostCommentList().stream()
                .map(CommentDTO::new).collect(Collectors.toList());
        this.tags = post.getTagSet().stream()
                .map(Tag::getName).collect(Collectors.toSet());
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
