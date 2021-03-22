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
import org.springframework.security.access.prepost.PreAuthorize;
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
        return authService.getAuthCheckResponse();
    }

    @PostMapping("/login")
    public AuthCheckResponse login(@RequestBody LoginRequest loginRequest) {
        authService.addUserToContextHolder(loginRequest);
        return authService.getAuthCheckResponse();
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('READ_AUTHORITY')")
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
