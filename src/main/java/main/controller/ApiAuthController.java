package main.controller;

import main.api.request.AuthRegisterRequest;
import main.api.request.LoginRequest;
import main.api.response.AuthCheckResponse;
import main.api.response.AuthRegisterResponse;
import main.api.response.CaptchaResponse;
import main.security.AuthService;
import main.security.SecurityUser;
import main.service.CaptchaService;
import main.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    final CaptchaService captchaService;
    final UserService userService;
    final AuthService authService;

    public ApiAuthController(CaptchaService captchaService, UserService userService, AuthService authService) {
        this.captchaService = captchaService;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/check")
    public AuthCheckResponse authCheck() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return email.isEmpty() ?
                new AuthCheckResponse() : authService.getAuthCheckResponse(email);
    }

    @PostMapping("/login")
    public AuthCheckResponse login(@RequestBody LoginRequest loginRequest) {
        SecurityUser securityUser = authService.addUserToContextHolderAndGetSecurityUser(loginRequest);
        return authService.getAuthCheckResponse(securityUser.getUsername());
    }

    @GetMapping("/logout")
    public AuthCheckResponse logout() {
        SecurityContextHolder.clearContext();
        AuthCheckResponse authCheckResponse = new AuthCheckResponse();
        authCheckResponse.setResult(true);
        return authCheckResponse;
    }

    @GetMapping("/captcha")
    public CaptchaResponse getCaptcha() {
        return captchaService.getCaptchaResponse();
    }

    @PostMapping("/register")
    public AuthRegisterResponse createUser(@RequestBody AuthRegisterRequest requestUser) {
        return userService.createUser(requestUser);
    }
}
