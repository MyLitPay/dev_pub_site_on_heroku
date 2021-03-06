package main.repo;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {
}
