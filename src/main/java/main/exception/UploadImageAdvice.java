package main.exception;

import main.api.response.ResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UploadImageAdvice {

    @ExceptionHandler(UploadImageException.class)
    public ResponseEntity<ResultResponse> handleException(UploadImageException ex) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setResult(false);
        resultResponse.setErrors(ex.getError());

        return new ResponseEntity<>(resultResponse, HttpStatus.BAD_REQUEST);
    }
}
