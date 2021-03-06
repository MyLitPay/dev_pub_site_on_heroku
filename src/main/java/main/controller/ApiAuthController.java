package main.controller;

import main.api.response.AuthCheckResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    // Дописать после добавления авторизации
    @GetMapping("/check")
    public AuthCheckResponse authCheck() {
        return new AuthCheckResponse();
    }
}
