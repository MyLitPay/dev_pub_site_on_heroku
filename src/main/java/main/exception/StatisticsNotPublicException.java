package main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Statistics not public")
public class StatisticsNotPublicException extends RuntimeException {
    public StatisticsNotPublicException(String message) {
        super(message);
    }
}
