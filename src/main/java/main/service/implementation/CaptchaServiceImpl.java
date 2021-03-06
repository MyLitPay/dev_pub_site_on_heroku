package main.service.implementation;

import main.repo.CaptchaRepository;
import main.service.CaptchaService;
import org.springframework.stereotype.Service;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    final CaptchaRepository repository;

    public CaptchaServiceImpl(CaptchaRepository repository) {
        this.repository = repository;
    }
}
