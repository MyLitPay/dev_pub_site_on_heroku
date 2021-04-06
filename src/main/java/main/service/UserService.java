package main.service;

import main.api.request.AuthRegisterRequest;
import main.api.request.CheckRestoreRequest;
import main.api.response.ResultResponse;
import main.model.User;

import java.util.Map;

public interface UserService {
    User getUserById(int id);
    ResultResponse createUser(AuthRegisterRequest requestUser);
    User getUserByEmail(String email);
    ResultResponse sendRestoreCode(String email);
    ResultResponse checkRestoreCode(CheckRestoreRequest request);
}
