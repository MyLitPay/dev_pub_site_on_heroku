package main.exception;

import java.util.Map;

public class UploadImageException extends RuntimeException{
    private Map<String, String> error;

    public UploadImageException(Map<String, String> error) {
        this.error = error;
    }

    public Map<String, String> getError() {
        return error;
    }
}
