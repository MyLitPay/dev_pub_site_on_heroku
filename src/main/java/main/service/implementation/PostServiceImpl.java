package main.service.implementation;

import main.api.response.CalendarResponse;
import main.api.response.PostResponse;
import main.api.response.dto.PostByIdDTO;
import main.api.response.dto.PostDTO;
import main.exception.PostNotFoundException;
import main.model.ModerationStatus;
import main.model.Permission;
import main.model.Post;
import main.model.User;
import main.repo.PostRepository;
import main.service.CommentService;
import main.service.PostService;
import main.service.UserService;
import main.service.accessory.OffsetBasedPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(userEmail);
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
        return postRepository.findByTitleContainingAndIsActiveAndModerationStatusAndTimeBefore(
                query, (byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        String authorityEmail = auth.getName();

        if (post.getIsActive() != 1 ||
                !(post.getModerationStatus().equals(ModerationStatus.ACCEPTED)) ||
                post.getTime().after(new Date())) {

            if (!authorities.contains(Permission.MODERATE.getAuthority())
                    && !authorityEmail.equals(post.getUser().getEmail())) {
                throw new PostNotFoundException("Post not found");
            }
        }

        return new PostByIdDTO(post);
    }
}
