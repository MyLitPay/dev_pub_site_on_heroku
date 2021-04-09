package main.repo;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByNameStartingWith(String name);
    Optional<Tag> findFirstByNameLike(String name);

    @Query(value = "SELECT * FROM tags WHERE " +
            "(SELECT distinct tag_id FROM tag2post WHERE tag_id=tags.id AND " +
            "(SELECT id FROM posts WHERE id=tag2post.post_id " +
            "AND is_active = ?1 AND moderation_status = ?2 AND time < ?3))",
            nativeQuery = true)
    List<Tag> findT(byte isActive,
                    String moderationStatus,
                    Date time);
}
