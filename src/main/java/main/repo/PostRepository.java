package main.repo;

import main.model.ModerationStatus;
import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByIsActiveAndModerationStatusAndTimeBefore(byte isActive,
                                                             ModerationStatus moderationStatus,
                                                             Date time,
                                                             Pageable pageable);

    List<Post> findByIsActiveAndModerationStatusAndTimeBefore(byte isActive,
                                                              ModerationStatus moderationStatus,
                                                              Date time);

}
