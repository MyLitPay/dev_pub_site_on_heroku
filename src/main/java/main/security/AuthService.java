package main.security;

import main.api.request.LoginRequest;
import main.api.response.AuthCheckResponse;
import main.api.response.dto.AuthUserDTO;
import main.model.User;
import main.service.PostService;
import main.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    final AuthenticationManager authenticationManager;
    final UserService userService;
    final PostService postService;

    public AuthService(AuthenticationManager authenticationManager, UserService userService, PostService postService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.postService = postService;
    }

    public void addUserToContextHolder(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public AuthCheckResponse getAuthCheckResponse() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email.equals("anonymousUser")) {
            return new AuthCheckResponse();
        }

        User user = userService.getUserByEmail(email);

        AuthCheckResponse response = new AuthCheckResponse();
        int moderationCount = user.getIsModerator() == 1 ? postService.countOfNoModeratedPosts() : 0;
        AuthUserDTO authUserDTO = new AuthUserDTO(user, moderationCount);

        response.setResult(true);
        response.setUser(authUserDTO);
        return response;
    }
}
