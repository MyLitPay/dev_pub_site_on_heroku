package main.service.implementation;

import main.api.response.PostResponse;
import main.api.response.dto.PostDTO;
import main.api.response.dto.UserInPostDTO;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.repo.PostRepository;
import main.service.PostService;
import main.service.UserService;
import main.service.accessory.OffsetBasedPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    final PostRepository postRepository;
    final UserService userService;

    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findByIsActiveAndModerationStatusAndTimeBefore((byte) 1,
                ModerationStatus.ACCEPTED, new Date());
    }

    @Override
    public List<Post> getAllPosts(int offset, int limit) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        return postRepository.findByIsActiveAndModerationStatusAndTimeBefore((byte) 1,
                ModerationStatus.ACCEPTED, new Date(), pageable);
//        return postRepository.findAll(pageable).getContent();
    }

    @Override
    public PostResponse getPostResponse(int offset, int limit, String mode) {
        PostResponse postResponse = new PostResponse();
        List<PostDTO> postDTOList = getPostDTOList(offset, limit, mode);
        postResponse.setCount(postDTOList.size());
        postResponse.setPosts(postDTOList);
        return postResponse;
    }

    private List<PostDTO> getPostDTOList(int offset, int limit, String mode) {
        List<Post> postList = getAllPosts(offset, limit);
        List<PostDTO> postDTOList = new ArrayList<>();

        for (Post p : postList) {

//            if (p.getIsActive() != 1 ||
//                    !(p.getModerationStatus().equals(ModerationStatus.ACCEPTED)) ||
//                    p.getTime().after(new Date())) {
//                continue;
//            }

            PostDTO postDTO = new PostDTO();

            postDTO.setId(p.getId());
            postDTO.setTimestamp((p.getTime().getTime()) / 1000);
            postDTO.setUser(getUserInPost(p));
            postDTO.setTitle(p.getTitle());
            postDTO.setAnnounce(getAnnounce(p));
            postDTO.setLikeCount(getVotes(p).get(0));
            postDTO.setDislikeCount(getVotes(p).get(1));
            postDTO.setCommentCount(p.getPostCommentList().size());
            postDTO.setViewCount(p.getViewCount());

            postDTOList.add(postDTO);
        }

        return getSortedPostDTOList(postDTOList, mode);
    }

    private List<PostDTO> getSortedPostDTOList(List<PostDTO> postDTOList, String mode) {
        List<PostDTO> sortedPostDTOList;

        switch (mode) {
            case "popular" :
                sortedPostDTOList = postDTOList.stream()
                        .sorted(Comparator.comparingInt(PostDTO::getCommentCount).reversed())
                        .collect(Collectors.toList());
                break;
            case "best" :
                sortedPostDTOList = postDTOList.stream()
                        .sorted(Comparator.comparingInt(PostDTO::getLikeCount).reversed())
                        .collect(Collectors.toList());
                break;
            case "early" :
                sortedPostDTOList = postDTOList.stream()
                        .sorted(Comparator.comparingLong(PostDTO::getTimestamp))
                        .collect(Collectors.toList());
                break;
            default :
                sortedPostDTOList = postDTOList.stream()
                        .sorted(Comparator.comparingLong(PostDTO::getTimestamp).reversed())
                        .collect(Collectors.toList());
                break;
        }

        return sortedPostDTOList;
    }

    private UserInPostDTO getUserInPost(Post post) {
        User user = userService.getUserById(post.getId());
        UserInPostDTO userInPostDTO = new UserInPostDTO();
        userInPostDTO.setId(user.getId());
        userInPostDTO.setName(user.getName());

        return userInPostDTO;
    }

    private String getAnnounce(Post post) {
        String text = post.getText();
        StringBuilder sb = new StringBuilder();
        text = text.replaceAll("<.*?>", " ");
        text = text.replaceAll("\\s+", " ");
        text = text.substring(0, 150);
        sb.append(text).append("...");

        return sb.toString();
    }

    private List<Integer> getVotes(Post p) {
        List<Integer> votes = new ArrayList<>();
        List<PostVote> voteList = p.getPostVoteList();
        int likeCount = 0;
        int dislikeCount = 0;

        for (PostVote x : voteList) {
            if (x.getValue() == 1) {
                likeCount++;
            } else {
                dislikeCount++;
            }
        }
        votes.add(0, likeCount);
        votes.add(1, dislikeCount);

        return votes;
    }
}
