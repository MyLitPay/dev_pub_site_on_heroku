package main.service;

import main.model.PostComment;

import java.util.List;

public interface CommentService {

    List<PostComment> getAllComments();
}
