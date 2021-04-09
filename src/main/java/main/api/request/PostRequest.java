package main.api.request;

import main.model.ModerationStatus;
import main.model.Post;
import main.model.Tag;
import main.model.User;

import java.util.Date;
import java.util.Set;

public class PostRequest {
    private long timestamp;
    private byte active;
    private String title;
    private Set<String> tags;
    private String text;

    public Post getNewPost(User user, boolean preModeration) {
        Post post = new Post();

        Date requestTime = new Date(timestamp * 1000);
        if (requestTime.before(new Date())) {
            requestTime = new Date();
        }

        if (preModeration) {
            post.setModerationStatus(ModerationStatus.NEW);
        } else {
            post.setModerationStatus(ModerationStatus.ACCEPTED);
        }

        post.setUser(user);
        post.setTime(requestTime);
        post.setIsActive(active);
        post.setTitle(title);
        post.setText(text);   // HTML
        post.setViewCount(0);

        return post;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte getActive() {
        return active;
    }

    public void setActive(byte active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
