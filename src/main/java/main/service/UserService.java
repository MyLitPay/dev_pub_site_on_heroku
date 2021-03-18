package main.service;

import main.api.request.AuthRegisterRequest;
import main.api.response.AuthRegisterResponse;
import main.model.User;

public interface UserService {
    User getUserById(int id);
    AuthRegisterResponse createUser(AuthRegisterRequest requestUser);
    User getUserByEmail(String email);
}
