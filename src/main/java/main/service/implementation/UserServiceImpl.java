package main.service.implementation;

import main.api.request.AuthRegisterRequest;
import main.api.response.AuthRegisterResponse;
import main.exception.UserNotFoundException;
import main.model.User;
import main.repo.CaptchaRepository;
import main.repo.UserRepository;
import main.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final CaptchaRepository captchaRepository;

    public UserServiceImpl(UserRepository userRepository, CaptchaRepository captchaRepository) {
        this.userRepository = userRepository;
        this.captchaRepository = captchaRepository;
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
    public AuthRegisterResponse createUser(AuthRegisterRequest requestUser) {
        AuthRegisterResponse response = new AuthRegisterResponse();
        Map<String, String> errors = new HashMap<>();

        if (userRepository.findByEmail(requestUser.getEmail()).isPresent()) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (userRepository.findByName(requestUser.getName().trim()).isPresent()) {
            errors.put("name", "Имя указано неверно");
        }
        if (requestUser.getPassword().length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (captchaRepository.findFirstByCodeAndSecretCode(requestUser.getCaptcha(),
                requestUser.getCaptchaSecret()).isEmpty()) {
            errors.put("captcha", "Код с картинки введён неверно");
        }


        if (errors.isEmpty()) {
            User user = new User();
            user.setIsModerator((byte) 0);
            user.setRegTime(new Date());
            user.setName(requestUser.getName());
            user.setEmail(requestUser.getEmail());
            user.setPassword(requestUser.getPassword());
            userRepository.saveAndFlush(user);

            response.setResult(true);

        } else {
            response.setResult(false);
            response.setErrors(errors);
        }

        return response;
    }
}
