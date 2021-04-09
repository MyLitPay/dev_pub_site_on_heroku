package main.repo;

import main.model.Post;
import main.model.PostVote;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<PostVote, Integer> {
    Optional<PostVote> findFirstByPostAndUser(Post post, User user);
}
