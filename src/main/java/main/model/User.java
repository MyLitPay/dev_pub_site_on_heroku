package main.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_moderator", nullable = false)
    private byte isModerator;

    @Column(name = "reg_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date regTime;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String code;

    @Type(type = "text")
    private String photo;

    @Column(name = "code_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date codeTime;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PostComment> postCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PostVote> postVoteList = new ArrayList<>();

    public User() {
    }

    public void addPost(Post post) {
        postList.add(post);
        post.setUser(this);
    }

    public void deletePost(Post post) {
        postList.remove(post);
        post.setUser(null);
    }

    public void addPostComment(PostComment postComment) {
        postCommentList.add(postComment);
        postComment.setUser(this);
    }

    public void deletePostComment(PostComment postComment) {
        postCommentList.remove(postComment);
        postComment.setUser(null);
    }

    public void addPostVote(PostVote postVote) {
        postVoteList.add(postVote);
        postVote.setUser(this);
    }

    public void deletePostVote(PostVote postVote) {
        postVoteList.remove(postVote);
        postVote.setUser(null);
    }

    public Role getRole() {
        return isModerator == 1 ? Role.MODERATOR : Role.USER;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(byte isModerator) {
        this.isModerator = isModerator;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getCodeTime() {
        return codeTime;
    }

    public void setCodeTime(Date codeTime) {
        this.codeTime = codeTime;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public List<PostComment> getPostCommentList() {
        return postCommentList;
    }

    public List<PostVote> getPostVoteList() {
        return postVoteList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
