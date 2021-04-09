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

    @Query(value = "SELECT * FROM tags WHERE id IN " +
            "(SELECT tag_id FROM tag2post JOIN posts ON posts.id = tag2post.post_id " +
            "WHERE posts.is_active = ?1 AND posts.moderation_status = ?2 " +
            "AND posts.time < ?3)",
            nativeQuery = true)
    List<Tag> findActiveTags(byte isActive,
                             String moderationStatus,
                             Date time);

    @Query(value = "SELECT COUNT(*) AS c FROM tag2post WHERE post_id IN " +
            "(SELECT id FROM posts WHERE " +
            "is_active = ?1 AND moderation_status = ?2 AND time < ?3) " +
            "GROUP BY tag_id ORDER BY c DESC " +
            "LIMIT 1",
            nativeQuery = true)
    int countOfPostsMostPopularTag(byte isActive,
                                   String moderationStatus,
                                   Date time);

    @Query(value = "SELECT COUNT(*) AS c FROM tag2post WHERE post_id IN " +
            "(SELECT id FROM posts WHERE " +
            "is_active = ?1 AND moderation_status = ?2 AND time < ?3) " +
            "AND tag_id = ?4 GROUP BY tag_id",
            nativeQuery = true)
    int countOfPostsWithTagById(byte isActive,
                                String moderationStatus,
                                Date time,
                                int id);
}
