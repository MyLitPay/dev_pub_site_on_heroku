package main.exception;

import main.api.response.AuthCheckResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationAdvice {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AuthCheckResponse> handleException(BadCredentialsException ex) {
        return new ResponseEntity<>(new AuthCheckResponse(), HttpStatus.OK);
    }
}
