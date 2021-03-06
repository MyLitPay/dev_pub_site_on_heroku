package main.service;

import main.model.PostVote;

import java.util.List;

public interface VoteService {
    List<PostVote> getAllVotes();
}
