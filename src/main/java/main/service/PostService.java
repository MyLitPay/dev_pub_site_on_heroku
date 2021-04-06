package main.service;

import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
import main.api.response.CalendarResponse;
import main.api.response.PostResponse;
import main.api.response.ResultResponse;
import main.api.response.dto.PostByIdDTO;
import main.model.Post;

import java.util.List;

public interface PostService {

    List<Post> getAllPosts();
    PostResponse getPostResponse(int offset, int limit, String mode);
    PostResponse getPostResponseByQuery(int offset, int limit, String query);
    CalendarResponse getCalendar(int year);
    PostResponse getPostResponseByDate(int offset, int limit, String date);
    PostResponse getPostResponseByTag(int offset, int limit, String tag);
    PostByIdDTO getPostByIdDTO(int id);
    int countOfNoModeratedPosts();
    PostResponse getMyPosts(int offset, int limit, String status);
    PostResponse getModerationPosts(int offset, int limit, String status);
    ResultResponse createPost(PostRequest request);
    ResultResponse updatePostById(int id, PostRequest request);
    ResultResponse moderate(ModerationRequest request);
}
