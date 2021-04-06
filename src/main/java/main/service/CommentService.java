package main.service;

import main.api.request.CommentRequest;
import main.api.response.CommentResponse;
import main.model.Post;
import main.model.PostComment;

import java.util.List;

public interface CommentService {

    List<PostComment> getAllComments();
    List<PostComment> getByPost(Post post);
//    int countAllByPost(Post post);
    CommentResponse addComment(CommentRequest request);
}
