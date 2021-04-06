package main.repo;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByNameStartingWith(String name);
    Optional<Tag> findFirstByNameLike(String name);

    @Query(value = "SELECT * FROM posts WHERE " +
            "(SELECT * FROM tags " +
            "(is_active = ?1 AND moderation_status = ?2 AND time < ?3) " +
            "ORDER BY " +
            "(SELECT COUNT(*) FROM post_comments WHERE post_id = posts.id) " +
            "DESC",
            countQuery = "SELECT COUNT(*) FROM posts",
            nativeQuery = true)
    List<Tag> findT();
}
