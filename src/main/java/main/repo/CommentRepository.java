package main.repo;

import main.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {
}
