package main.service.implementation;

import main.model.PostVote;
import main.repo.VoteRepository;
import main.service.VoteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {
    final VoteRepository voteRepository;

    public VoteServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public List<PostVote> getAllVotes() {
        return (List<PostVote>) voteRepository.findAll();
    }
}
