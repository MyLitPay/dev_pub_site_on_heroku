package main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Multiuser mode disabled")
public class MultiuserModeDisabledException extends RuntimeException{
    public MultiuserModeDisabledException(String message) {
        super(message);
    }
}
