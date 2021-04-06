package main.repo;

import main.model.Post;
import main.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    List<PostComment> findByPost(Post post);
    Optional<PostComment> findFirstByIdAndPost_Id(int id, int postId);
//    int countAllByPost(Post post);
    Optional<PostComment> findFirstByPost_Id(int postId);
    Optional<PostComment> findFirstByPost_IdAndParentIdAndUser_IdOrderByIdDesc(int postId, Integer parentId, int userId);
}
