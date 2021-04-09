package main.service;

import main.api.request.VoteRequest;
import main.api.response.ResultResponse;
import main.model.PostVote;

import java.util.List;

public interface VoteService {
    ResultResponse setLike(VoteRequest request);
    ResultResponse setDislike(VoteRequest request);
}
