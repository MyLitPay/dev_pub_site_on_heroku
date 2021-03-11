package main.service;

import main.api.response.CalendarResponse;
import main.api.response.PostResponse;
import main.model.Post;

import java.util.List;

public interface PostService {

    List<Post> getAllPosts();
    PostResponse getPostResponse(int offset, int limit, String mode);
    PostResponse getPostResponseByQuery(int offset, int limit, String query);
    CalendarResponse getCalendar(int year);
}
