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

    public SecurityUser addUserToContextHolderAndGetSecurityUser(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return (SecurityUser) auth.getPrincipal();
    }

    public AuthCheckResponse getAuthCheckResponse(String email) {
        User user = userService.getUserByEmail(email);

        AuthCheckResponse response = new AuthCheckResponse();
        AuthUserDTO authUserDTO = new AuthUserDTO();
        authUserDTO.setId(user.getId());
        authUserDTO.setName(user.getName());
        authUserDTO.setPhoto(user.getPhoto());
        authUserDTO.setEmail(user.getEmail());
        authUserDTO.setModeration(user.getIsModerator() == 1);
        authUserDTO.setModerationCount(user.getIsModerator() == 1 ? postService.countOfNoModeratedPosts() : 0);
        authUserDTO.setSettings(user.getIsModerator() == 1);

        response.setResult(true);
        response.setUser(authUserDTO);

        return response;
    }
}
