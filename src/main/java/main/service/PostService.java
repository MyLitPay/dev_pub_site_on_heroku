package main.service;

import main.api.response.PostResponse;
import main.api.response.dto.PostDTO;
import main.model.Post;

import java.util.Date;
import java.util.List;

public interface PostService {

    List<Post> getAllPosts();
    List<Post> getAllPosts(int offset, int limit);
    PostResponse getPostResponse(int offset, int limit, String mode);
}
