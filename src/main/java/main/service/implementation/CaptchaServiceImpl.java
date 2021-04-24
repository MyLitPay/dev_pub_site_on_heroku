package main.service.implementation;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repo.CaptchaRepository;
import main.service.CaptchaService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    final CaptchaRepository captchaRepository;

    public CaptchaServiceImpl(CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    @Override
    public CaptchaResponse getCaptchaResponse() {
        String code;
        String secret;
        String image;

        Cage cage = new GCage();
//        code = cage.getTokenGenerator().next();
        code = RandomStringUtils.randomAlphabetic(5).toLowerCase();
        secret = String.valueOf(UUID.randomUUID());

        byte[] draw = cage.draw(code);
        String encodedDraw = Base64.getEncoder().encodeToString(draw);
        image = "data:image/png;base64, " + encodedDraw;

        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCode(code);
        captchaCode.setSecretCode(secret);
        captchaCode.setTime(new Date());
        captchaRepository.saveAndFlush(captchaCode);

        CaptchaResponse captchaResponse = new CaptchaResponse();
        captchaResponse.setImage(image);
        captchaResponse.setSecret(secret);

        return captchaResponse;
    }
}
