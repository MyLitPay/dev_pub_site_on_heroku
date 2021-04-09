package main.service.implementation;

import main.api.request.VoteRequest;
import main.api.response.ResultResponse;
import main.exception.PostNotFoundException;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.repo.PostRepository;
import main.repo.VoteRepository;
import main.security.UserDetailsServiceImpl;
import main.service.VoteService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {
    final VoteRepository voteRepository;
    final PostRepository postRepository;
    final UserDetailsServiceImpl userDetailsService;

    public VoteServiceImpl(VoteRepository voteRepository, PostRepository postRepository, UserDetailsServiceImpl userDetailsService) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public ResultResponse setLike(VoteRequest request) {
        return setVote(request.getPostId(), (byte) 1, (byte) -1);
    }

    @Override
    public ResultResponse setDislike(VoteRequest request) {
        return setVote(request.getPostId(), (byte) -1, (byte) 1);
    }

    private ResultResponse setVote(int postId, byte likeVote, byte dislikeVote) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        User user = userDetailsService.getUserFromContextHolder();

        if (user.getId() == post.getUser().getId()) {
            return new ResultResponse(false);
        }

        PostVote vote = voteRepository.findFirstByPostAndUser(post, user).orElse(null);

        if (vote != null) {
            if (vote.getValue() == dislikeVote) {
                vote.setValue(likeVote);
                vote.setTime(new Date());
            } else {
                return new ResultResponse(false);
            }
        } else {
            PostVote like = new PostVote();
            like.setUser(user);
            like.setValue(likeVote);
            like.setTime(new Date());

            post.addPostVote(like);
        }

        postRepository.saveAndFlush(post);
        return new ResultResponse(true);
    }
}
