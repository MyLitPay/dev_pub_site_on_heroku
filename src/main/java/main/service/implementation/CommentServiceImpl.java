package main.service.implementation;

import main.model.PostComment;
import main.repo.CommentRepository;
import main.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<PostComment> getAllComments() {
        return (List<PostComment>) commentRepository.findAll();
    }
}
