package main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad request")
public class BadCommentRequestException extends RuntimeException{
    public BadCommentRequestException(String message) {
        super(message);
    }
}
