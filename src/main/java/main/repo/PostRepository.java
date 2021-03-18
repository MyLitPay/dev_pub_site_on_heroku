package main.repo;

import main.model.ModerationStatus;
import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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

    List<Post> findByTitleContainingAndIsActiveAndModerationStatusAndTimeBefore(String title,
                                                                          byte isActive,
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

}
