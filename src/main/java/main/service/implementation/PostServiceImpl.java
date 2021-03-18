package main.service.implementation;

import main.api.response.CalendarResponse;
import main.api.response.PostResponse;
import main.api.response.dto.*;
import main.exception.PostNotFoundException;
import main.model.*;
import main.repo.PostRepository;
import main.service.CommentService;
import main.service.PostService;
import main.service.UserService;
import main.service.accessory.OffsetBasedPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    final PostRepository postRepository;
    final UserService userService;
    final CommentService commentService;

    public PostServiceImpl(PostRepository postRepository, UserService userService, CommentService commentService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService = commentService;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findByIsActiveAndModerationStatusAndTimeBefore((byte) 1,
                ModerationStatus.ACCEPTED, new Date());
    }

    @Override
    public int countOfNoModeratedPosts() {
        return postRepository.countAllByModerationStatus(ModerationStatus.NEW);
    }

    @Override
    public PostResponse getPostResponse(int offset, int limit, String mode) {
        PostResponse postResponse = new PostResponse();
        List<Post> postList = getAllPosts(offset, limit);
        List<PostDTO> postDTOList = getPostDTOList(postList, mode);
        postResponse.setCount(postDTOList.size());
        postResponse.setPosts(postDTOList);
        return postResponse;
    }

    @Override
    public PostResponse getPostResponseByQuery(int offset, int limit, String query) {
        String trimQuery = query.trim();
        if (trimQuery.isEmpty()) {
            return getPostResponse(offset, limit, "recent");
        }

        PostResponse postResponse = new PostResponse();
        List<Post> postList = getPostsByQuery(offset, limit, query);
        List<PostDTO> postDTOList = getPostDTOList(postList, "recent");
        postResponse.setCount(postDTOList.size());
        postResponse.setPosts(postDTOList);
        return postResponse;
    }

    @Override
    public PostResponse getPostResponseByDate(int offset, int limit, String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));
        Calendar start = new GregorianCalendar(year, month - 1, day);
        Calendar end = new GregorianCalendar(year, month - 1, day, 23, 59, 59);
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        List<Post> postList = postRepository.findByTimeBetweenAndIsActiveAndModerationStatusAndTimeBefore(
                start.getTime(), end.getTime(), (byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
        List<PostDTO> postDTOList = getPostDTOList(postList, "recent");
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postDTOList.size());
        postResponse.setPosts(postDTOList);
        return postResponse;
    }

    @Override
    public PostResponse getPostResponseByTag(int offset, int limit, String tag) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        List<Post> postList = postRepository.findDistinctByIsActiveAndModerationStatusAndTimeBeforeAndTagSet_NameLike(
                (byte) 1, ModerationStatus.ACCEPTED, new Date(), tag, pageable);
        List<PostDTO> postDTOList = getPostDTOList(postList, "recent");
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postDTOList.size());
        postResponse.setPosts(postDTOList);
        return postResponse;
    }

    @Override
    public CalendarResponse getCalendar(int year) {
        if (year == 0) {
            year = new GregorianCalendar().getWeekYear();
        }
        CalendarResponse calendarResponse = new CalendarResponse();
        Map<String, Integer> postsMap = new HashMap<>();
        List<Integer> years = new ArrayList<>();
        for (Post post : getAllPosts()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            Integer postYear = Integer.parseInt(dateFormat.format(post.getTime()));
            if (!years.contains(postYear)) {
                years.add(postYear);
            }
        }
        Collections.sort(years);

        for (Post p : getPostsByYear(year)) {
            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(p.getTime());
            if (postsMap.containsKey(dateString)) {
                postsMap.put(dateString, postsMap.get(dateString) + 1);
            } else {
                postsMap.put(dateString, 1);
            }
        }

        calendarResponse.setYears(years);
        calendarResponse.setPosts(postsMap);

        return calendarResponse;
    }

    @Override
    public PostByIdDTO getPostByIdDTO(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        if (post.getIsActive() != 1 ||
                !(post.getModerationStatus().equals(ModerationStatus.ACCEPTED)) ||
                post.getTime().after(new Date())) {
//            if ("не модератор и не автор поста") {
//                throw new PostNotFoundException("Post not found");
//            }
            throw new PostNotFoundException("Post not found");
        }

        PostByIdDTO postByIdDTO = new PostByIdDTO();

        postByIdDTO.setId(post.getId());
        postByIdDTO.setTimestamp((post.getTime().getTime()) / 1000);
        postByIdDTO.setActive(post.getIsActive() != 0);
        postByIdDTO.setUser(getUserInPost(post));
        postByIdDTO.setTitle(post.getTitle());
        postByIdDTO.setText(post.getText()); // Должны быть в HTML
        postByIdDTO.setLikeCount(getVotes(post).get(0));
        postByIdDTO.setDislikeCount(getVotes(post).get(1));
        postByIdDTO.setViewCount(post.getViewCount());
        postByIdDTO.setComments(getCommentDTOList(post));
        Set<String> tags = new HashSet<>();
        post.getTagSet().forEach(t -> tags.add(t.getName()));
        postByIdDTO.setTags(tags);

        return postByIdDTO;
    }

    private List<Post> getAllPosts(int offset, int limit) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        return postRepository.findByIsActiveAndModerationStatusAndTimeBefore(
                (byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
//        return postRepository.findAll(pageable).getContent();
    }

    private List<Post> getPostsByQuery(int offset, int limit, String query) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        return postRepository.findByTitleContainingAndIsActiveAndModerationStatusAndTimeBefore(
                query, (byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
    }

    private List<Post> getPostsByYear(int year) {
        Calendar startCalendar = new GregorianCalendar(year, Calendar.JANUARY, 1);
        Calendar endCalendar = new GregorianCalendar(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return postRepository.findByTimeBetweenAndIsActiveAndModerationStatusAndTimeBefore(
                startCalendar.getTime(), endCalendar.getTime(), (byte) 1, ModerationStatus.ACCEPTED, new Date());
    }

    private List<PostDTO> getPostDTOList(List<Post> postList, String mode) {
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
            case "popular":
                sortedPostDTOList = postDTOList.stream()
                        .sorted(Comparator.comparingInt(PostDTO::getCommentCount).reversed())
                        .collect(Collectors.toList());
                break;
            case "best":
                sortedPostDTOList = postDTOList.stream()
                        .sorted(Comparator.comparingInt(PostDTO::getLikeCount).reversed())
                        .collect(Collectors.toList());
                break;
            case "early":
                sortedPostDTOList = postDTOList.stream()
                        .sorted(Comparator.comparingLong(PostDTO::getTimestamp))
                        .collect(Collectors.toList());
                break;
            default:
                sortedPostDTOList = postDTOList.stream()
                        .sorted(Comparator.comparingLong(PostDTO::getTimestamp).reversed())
                        .collect(Collectors.toList());
                break;
        }

        return sortedPostDTOList;
    }

    private UserInPostDTO getUserInPost(Post post) {
        User user = userService.getUserById(post.getUser().getId());
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
        if (text.length() > 150) {
            text = text.substring(0, 150);
        }
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

    private List<CommentDTO> getCommentDTOList(Post post) {
        List<PostComment> postCommentList = commentService.getByPost(post);
        List<CommentDTO> commentDTOList = new ArrayList<>();

        for (PostComment comment : postCommentList) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setTimestamp((comment.getTime().getTime()) / 1000);
            commentDTO.setText(comment.getText()); // Должен быть в формате HTML
            commentDTO.setUser(getUserInCommentDTO(comment));

            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    private UserInCommentDTO getUserInCommentDTO(PostComment comment) {
        User user = comment.getUser();
        UserInCommentDTO userInCommentDTO = new UserInCommentDTO();
        userInCommentDTO.setId(user.getId());
        userInCommentDTO.setName(user.getName());
        userInCommentDTO.setPhoto(user.getPhoto());
        return userInCommentDTO;
    }
}
