package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

public class AuthRegisterRequest {
    @JsonProperty("e_mail")
    private String email;

    private String password;
    private String name;
    private String captcha;

    @JsonProperty("captcha_secret")
    private String captchaSecret;

    public User getNewUser(PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setIsModerator((byte) 0);
        user.setRegTime(new Date());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    public AuthRegisterRequest(String email, String password, String name, String captcha, String captchaSecret) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.captcha = captcha;
        this.captchaSecret = captchaSecret;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaSecret() {
        return captchaSecret;
    }

    public void setCaptchaSecret(String captchaSecret) {
        this.captchaSecret = captchaSecret;
    }
}
