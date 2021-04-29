package main.service.implementation;

import main.api.request.CommentRequest;
import main.api.response.CommentResponse;
import main.exception.BadCommentRequestException;
import main.exception.CommentNotFoundException;
import main.exception.PostNotFoundException;
import main.exception.ShortTextException;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostComment;
import main.model.User;
import main.repo.CommentRepository;
import main.repo.PostRepository;
import main.security.UserDetailsServiceImpl;
import main.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {
    final CommentRepository commentRepository;
    final PostRepository postRepository;
    final UserDetailsServiceImpl userDetailsService;
    private static final int MIN_COMMENT_LENGTH = 5;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserDetailsServiceImpl userDetailsService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public List<PostComment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public CommentResponse addComment(CommentRequest request) {
        int postId = request.getPostId();
        Integer parentId = request.getParentId();

        if (parentId != null) {
            if (commentRepository.findFirstByIdAndPost_Id(parentId, postId).isEmpty()) {
                throw new BadCommentRequestException("Bad request");
            }
        } else {
            if (postRepository.findById(postId).isEmpty()) {
                throw new BadCommentRequestException("Bad request");
            }
        }

        String trimText = request.getText().trim();
        if (trimText.isEmpty() || trimText.length() < MIN_COMMENT_LENGTH) {
            throw new ShortTextException(Map.of("text", "Текст комментария не задан или слишком короткий"));
        }

        Post post =  postRepository.findByIdAndIsActiveAndModerationStatusAndTimeBefore(
                postId, (byte) 1, ModerationStatus.ACCEPTED, new Date())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        User user = userDetailsService.getUserFromContextHolder();

        PostComment comment = new PostComment();
        comment.setParentId(parentId);
        comment.setTime(new Date());
        comment.setText(request.getText()); // HTML
        comment.setUser(user);
        post.addPostComment(comment);

        postRepository.saveAndFlush(post);
        PostComment commentFromDB = commentRepository.findFirstByPost_IdAndParentIdAndUser_IdOrderByIdDesc(
                postId, parentId, user.getId())
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        return new CommentResponse(commentFromDB);
    }
}
