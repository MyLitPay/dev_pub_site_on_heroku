package main.service.implementation;

import main.api.request.AuthRegisterRequest;
import main.api.request.CheckRestoreRequest;
import main.api.response.ResultResponse;
import main.config.SecurityConfig;
import main.exception.UserNotFoundException;
import main.model.CaptchaCode;
import main.model.User;
import main.repo.CaptchaRepository;
import main.repo.UserRepository;
import main.service.EmailService;
import main.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final CaptchaRepository captchaRepository;
    final PasswordEncoder passwordEncoder;
    final EmailService emailService;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final long MS_IN_DAY = 86400000;

    public UserServiceImpl(UserRepository userRepository, CaptchaRepository captchaRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.captchaRepository = captchaRepository;
        this.emailService = emailService;
        this.passwordEncoder = SecurityConfig.passwordEncoder();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public ResultResponse createUser(AuthRegisterRequest requestUser) {
        ResultResponse response = new ResultResponse();
        Map<String, String> errors = new HashMap<>();

        if (userRepository.findByEmail(requestUser.getEmail()).isPresent()) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (!requestUser.getName().matches("\\w+")) {
            errors.put("name", "Имя указано неверно");
        }
        if (requestUser.getPassword().length() < MIN_PASSWORD_LENGTH) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (captchaRepository.findFirstByCodeAndSecretCode(requestUser.getCaptcha(),
                requestUser.getCaptchaSecret()).isEmpty()) {
            errors.put("captcha", "Код с картинки введён неверно");
        }


        if (errors.isEmpty()) {
            User user = requestUser.getNewUser(passwordEncoder);
            userRepository.saveAndFlush(user);

            response.setResult(true);

        } else {
            response.setResult(false);
            response.setErrors(errors);
        }

        return response;
    }

    @Override
    public ResultResponse sendRestoreCode(String email) {
        ResultResponse response = new ResultResponse();

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            String hash = UUID.randomUUID().toString().replaceAll("-", "");
            user.setCode(hash);
            userRepository.saveAndFlush(user);

            String rootUri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
            String restoreHTMLLink = "<a href=\"" +
                    rootUri + "/login/change-password/" + hash +
                    "\">Click to restore</a>";

            String text = "Link to restore your password:\n" + restoreHTMLLink;
            System.out.println(text);
            emailService.send(email, "Restore password", text);

            response.setResult(true);

        } catch (Exception ex) {
            response.setResult(false);
        }

        return response;
    }

    @Override
    public ResultResponse checkRestoreCode(CheckRestoreRequest request) {
        ResultResponse response = new ResultResponse();
        Map<String, String> errors = new HashMap<>();
        User user = userRepository.findByCode(request.getCode())
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            errors.put("password", "Пароль короче 6-ти символов");
        }

        CaptchaCode captcha = captchaRepository.findFirstByCodeAndSecretCode(
                request.getCaptcha(), request.getCaptchaSecret()).orElse(null);
        if (captcha == null) {
            errors.put("captcha", "Код с картинки введён неверно");
        } else {
            Date captchaTime = captcha.getTime();
            Date finalCaptchaTime = new Date(captchaTime.getTime() + MS_IN_DAY);

            if (new Date().after(finalCaptchaTime)) {
                String rootUri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
                errors.put("code", "Ссылка для восстановления пароля устарела.\n" +
                        "<a href=\"" + rootUri + "/auth/restore\">Запросить ссылку снова</a>");
            }
        }

        if (errors.isEmpty()) {
            String newPass = passwordEncoder.encode(request.getPassword());
            user.setPassword(newPass);
            userRepository.saveAndFlush(user);
            response.setResult(true);
        } else {
            response.setResult(false);
            response.setErrors(errors);
        }

        return response;
    }
}
