package main.repo;

import main.model.ModerationStatus;
import main.model.Post;
import main.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByIsActiveAndModerationStatusAndTimeBefore(byte isActive,
                                                              ModerationStatus moderationStatus,
                                                              Date time,
                                                              Pageable pageable);

    List<Post> findByIsActiveAndModerationStatusAndTimeBefore(byte isActive,
                                                              ModerationStatus moderationStatus,
                                                              Date time);

    List<Post> findAllByOrderByTimeAsc(Pageable pageable);

    @Query(value = "SELECT * FROM posts WHERE " +
            "(is_active = ?1 AND moderation_status = ?2 AND time < ?3) " +
            "ORDER BY " +
            "(SELECT COUNT(*) FROM post_comments WHERE post_id = posts.id) " +
            "DESC",
            countQuery = "SELECT COUNT(*) FROM posts",
            nativeQuery = true)
    List<Post> findPopular(byte isActive,
                           String moderationStatus,
                           Date time,
                           Pageable pageable);

    @Query(value = "SELECT * FROM posts WHERE " +
            "(is_active = ?1 AND moderation_status = ?2 AND time < ?3) " +
            "ORDER BY " +
            "(SELECT COUNT(*) FROM post_votes WHERE post_id = posts.id AND value = 1) " +
            "DESC",
            countQuery = "SELECT COUNT(*) FROM posts",
            nativeQuery = true)
    List<Post> findBest(byte isActive,
                        String moderationStatus,
                        Date time,
                        Pageable pageable);

    @Query(value = "SELECT * FROM posts WHERE " +
            "(is_active = ?1 AND moderation_status = ?2 AND time < ?3) " +
            "ORDER BY time ASC",
            countQuery = "SELECT COUNT(*) FROM posts",
            nativeQuery = true)
    List<Post> findEarly(byte isActive,
                         String moderationStatus,
                         Date time,
                         Pageable pageable);

    List<Post> findByTitleContainingIgnoreCaseOrTextContainingIgnoreCaseAndIsActiveAndModerationStatusAndTimeBefore(String title,
                                                                                String text, byte isActive,
                                                                                ModerationStatus moderationStatus,
                                                                                Date time,
                                                                                Pageable pageable);

    List<Post> findByTimeBetweenAndIsActiveAndModerationStatusAndTimeBefore(Date startDate,
                                                                            Date endDate,
                                                                            byte isActive,
                                                                            ModerationStatus moderationStatus,
                                                                            Date time);

    List<Post> findByTimeBetweenAndIsActiveAndModerationStatusAndTimeBefore(Date startDate,
                                                                            Date endDate,
                                                                            byte isActive,
                                                                            ModerationStatus moderationStatus,
                                                                            Date time,
                                                                            Pageable pageable);

    List<Post> findDistinctByIsActiveAndModerationStatusAndTimeBeforeAndTagSet_NameLike(
            byte isActive,
            ModerationStatus moderationStatus,
            Date time,
            String tag,
            Pageable pageable);

    int countAllByModerationStatus(ModerationStatus moderationStatus);

    List<Post> findByUserAndIsActive(User user, byte isActive, Pageable pageable);

    List<Post> findByModerationStatus(ModerationStatus moderationStatus, Pageable pageable);

    List<Post> findByUserAndIsActiveAndModerationStatus(User user, byte isActive,
                                                        ModerationStatus moderationStatus,
                                                        Pageable pageable);

    List<Post> findByModerationStatusAndModeratorId(ModerationStatus moderationStatus,
                                                    int id,
                                                    Pageable pageable);

    Optional<Post> findByIdAndIsActiveAndModerationStatusAndTimeBefore(
            int id,
            byte isActive,
            ModerationStatus moderationStatus,
            Date time);

    List<Post> findByUserAndIsActiveAndModerationStatusAndTimeBefore(
            User user,
            byte isActive,
            ModerationStatus moderationStatus,
            Date time);


}
