package main.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false)
    private byte isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status",
            columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED')",
            nullable = false)
    private ModerationStatus moderationStatus;

    @Column(name = "moderator_id")
    private int moderatorId;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(nullable = false)
    private String title;

    @Type(type = "text")
    @Column(nullable = false)
    private String text;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PostComment> postCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PostVote> postVoteList = new ArrayList<>();

    @ManyToMany(mappedBy = "postSet", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Tag> tagSet = new HashSet<>();

    public Post() {
    }

    public void addPostComment(PostComment postComment) {
        postCommentList.add(postComment);
        postComment.setPost(this);
    }

    public void deletePostComment(PostComment postComment) {
        postCommentList.remove(postComment);
        postComment.setPost(null);
    }

    public void addPostVote(PostVote postVote) {
        postVoteList.add(postVote);
        postVote.setPost(this);
    }

    public void deletePostVote(PostVote postVote) {
        postVoteList.remove(postVote);
        postVote.setPost(null);
    }

    public void addTag(Tag tag) {
        tagSet.add(tag);
        tag.getPostSet().add(this);
    }

    public void deleteTag(Tag tag) {
        tagSet.remove(tag);
        tag.getPostSet().remove(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public Set<Tag> getTagSet() {
        return tagSet;
    }

    public List<PostComment> getPostCommentList() {
        return postCommentList;
    }

    public List<PostVote> getPostVoteList() {
        return postVoteList;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(user);
        hcb.append(title);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Post)) {
            return false;
        }
        Post that = (Post) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(user, that.user);
        eb.append(title, that.title);
        return eb.isEquals();
    }

}
