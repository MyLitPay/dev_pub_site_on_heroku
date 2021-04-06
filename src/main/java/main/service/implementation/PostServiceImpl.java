package main.service.implementation;

import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
import main.api.response.CalendarResponse;
import main.api.response.PostResponse;
import main.api.response.ResultResponse;
import main.api.response.dto.PostByIdDTO;
import main.api.response.dto.PostDTO;
import main.exception.PostNotFoundException;
import main.exception.TagNotFoundException;
import main.model.*;
import main.repo.PostRepository;
import main.security.UserDetailsServiceImpl;
import main.service.CommentService;
import main.service.PostService;
import main.service.TagService;
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
    final TagService tagService;
    final UserDetailsServiceImpl userDetailsService;
    private static final int MIN_TEXT_LENGTH = 50;
    private static final int MIN_TITLE_LENGTH = 3;

    public PostServiceImpl(PostRepository postRepository, UserService userService, CommentService commentService, TagService tagService, UserDetailsServiceImpl userDetailsService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService = commentService;
        this.tagService = tagService;
        this.userDetailsService = userDetailsService;
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
        List<Post> postList = getSortedPosts(offset, limit, mode);
        List<PostDTO> postDTOList = getPostDTOList(postList);

        PostResponse postResponse = new PostResponse();
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
        List<PostDTO> postDTOList = getPostDTOList(postList);
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
        List<PostDTO> postDTOList = getPostDTOList(postList);
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
        List<PostDTO> postDTOList = getPostDTOList(postList);
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
    public PostResponse getMyPosts(int offset, int limit, String status) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        User user = userDetailsService.getUserFromContextHolder();
        List<Post> postList;
        switch (status) {
            case "inactive" :
                postList = postRepository.findByUserAndIsActive(user, (byte) 0, pageable);
                break;
            case "pending" :
                postList = postRepository.findByUserAndIsActiveAndModerationStatus(
                        user, (byte) 1, ModerationStatus.NEW, pageable);
                break;
            case "declined" :
                postList = postRepository.findByUserAndIsActiveAndModerationStatus(
                        user, (byte) 1, ModerationStatus.DECLINED, pageable);
                break;
            case "published" :
                postList = postRepository.findByUserAndIsActiveAndModerationStatus(
                        user, (byte) 1, ModerationStatus.ACCEPTED, pageable);
                break;
            default:
                postList = new ArrayList<>();
                break;
        }
        List<PostDTO> postDTOList = getPostDTOList(postList);
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postDTOList.size());
        postResponse.setPosts(postDTOList);
        return postResponse;
    }

    @Override
    public PostResponse getModerationPosts(int offset, int limit, String status) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        User user = userDetailsService.getUserFromContextHolder();
        List<Post> postList;
        switch (status) {
            case "new" :
                postList = postRepository.findByModerationStatus(
                        ModerationStatus.NEW, pageable);
                break;
            case "declined" :
                postList = postRepository.findByModerationStatusAndModeratorId(
                        ModerationStatus.DECLINED, user.getId(), pageable);
                break;
            case "accepted" :
                postList = postRepository.findByModerationStatusAndModeratorId(
                        ModerationStatus.ACCEPTED, user.getId(), pageable);
                break;
            default:
                postList = new ArrayList<>();
        }
        List<PostDTO> postDTOList = getPostDTOList(postList);

        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postDTOList.size());
        postResponse.setPosts(postDTOList);
        return postResponse;
    }

    @Override
    public ResultResponse createPost(PostRequest request) {
        ResultResponse resultResponse = getCheckErrorsResponse(request);

        if (!resultResponse.isResult()) {
            return resultResponse;
        }

        Post post = request.getNewPost(userDetailsService.getUserFromContextHolder());
        addTagsToPost(request.getTags(), post);

        postRepository.saveAndFlush(post);

        return resultResponse;
    }

    @Override
    public ResultResponse updatePostById(int id, PostRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("Post not found"));

        User user = userDetailsService.getUserFromContextHolder();

        if (user.getId() == (post.getUser().getId())) {
            post.setModerationStatus(ModerationStatus.NEW);
        } else {
            throw new PostNotFoundException("Post not found");
        }

        ResultResponse resultResponse = getCheckErrorsResponse(request);

        if (!resultResponse.isResult()) {
            return resultResponse;
        }

        post.setTime(new Date(request.getTimestamp()));
        post.setIsActive(request.getActive());
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        addTagsToPost(request.getTags(), post);

        postRepository.saveAndFlush(post);

        return resultResponse;
    }

    private void addTagsToPost(Set<String> tags, Post post) {
        for (String t : tags) {
            Tag tag = new Tag();
            tag.setName(t);
            try {
                Tag tagFromDB = tagService.getTagByName(t);
                if (!post.getTagSet().contains(tagFromDB)) {
                    post.addTag(tagFromDB);
                }
            } catch (TagNotFoundException ex) {
                post.addTag(tag);
            }
        }
    }

    private ResultResponse getCheckErrorsResponse(PostRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        Map<String, String> errors = new HashMap<>();
        if (request.getTitle().trim().length() < MIN_TITLE_LENGTH) {
            errors.put("title", "Заголовок не установлен");
        }
        if (request.getText().trim().length() < MIN_TEXT_LENGTH) {
            errors.put("text", "Текст публикации слишком короткий");
        }

        if (!errors.isEmpty()) {
            resultResponse.setResult(false);
            resultResponse.setErrors(errors);
        } else {
            resultResponse.setResult(true);
        }

        return resultResponse;
    }

    private List<Post> getSortedPosts(int offset, int limit, String mode) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        List<Post> postList;
        switch (mode) {
            case "popular":
                postList =  postRepository.findPopular(
                        (byte) 1, ModerationStatus.ACCEPTED.toString(), new Date(), pageable);
                break;
            case "best":
                postList = postRepository.findBest(
                        (byte) 1, ModerationStatus.ACCEPTED.toString(), new Date(), pageable);
                break;
            case "early":
                postList = postRepository.findEarly(
                        (byte) 1, ModerationStatus.ACCEPTED.toString(), new Date(), pageable);
                break;
            default:
                postList = postRepository.findByIsActiveAndModerationStatusAndTimeBefore(
                        (byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
                break;
        }
        return postList;
    }

    private List<Post> getPostsByQuery(int offset, int limit, String query) {
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);
        return postRepository.findByTitleContainingIgnoreCaseOrTextContainingIgnoreCaseAndIsActiveAndModerationStatusAndTimeBefore(
                query, query, (byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
    }

    private List<Post> getPostsByYear(int year) {
        Calendar startCalendar = new GregorianCalendar(year, Calendar.JANUARY, 1);
        Calendar endCalendar = new GregorianCalendar(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return postRepository.findByTimeBetweenAndIsActiveAndModerationStatusAndTimeBefore(
                startCalendar.getTime(), endCalendar.getTime(), (byte) 1, ModerationStatus.ACCEPTED, new Date());
    }

    private List<PostDTO> getPostDTOList(List<Post> postList) {
        return postList.stream()
                .map(PostDTO::new).collect(Collectors.toList());
    }

    @Override
    public PostByIdDTO getPostByIdDTO(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Set<String> authorities = auth.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
//        String authorityEmail = auth.getName();
        User user = userDetailsService.getUserFromContextHolder();

        if (post.getIsActive() != 1 ||
                !(post.getModerationStatus().equals(ModerationStatus.ACCEPTED)) ||
                post.getTime().after(new Date())) {

            if (user.getIsModerator() == 1 || user.getId() == (post.getUser().getId())) {
                return new PostByIdDTO(post);
            }

            throw new PostNotFoundException("Post not found");
        }

        if (user.getIsModerator() != 1 && user.getId() != (post.getUser().getId())) {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.saveAndFlush(post);
        }

        return new PostByIdDTO(post);
    }

    @Override
    public ResultResponse moderate(ModerationRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        ResultResponse resultResponse = new ResultResponse();

        try {
            if (request.getDecision().equals("accept")) {
                post.setModerationStatus(ModerationStatus.ACCEPTED);
            } else {
                post.setModerationStatus(ModerationStatus.DECLINED);
            }

            post.setModeratorId(userDetailsService.getUserFromContextHolder().getId());
            postRepository.saveAndFlush(post);

            resultResponse.setResult(true);

        } catch (Exception ex) {
            resultResponse.setResult(false);
        }

        return resultResponse;
    }
}
