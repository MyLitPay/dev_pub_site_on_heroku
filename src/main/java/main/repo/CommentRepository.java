package main.repo;

import main.model.Post;
import main.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    List<PostComment> findByPost(Post post);
}
