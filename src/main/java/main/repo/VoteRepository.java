package main.repo;

import main.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<PostVote, Integer> {
}
