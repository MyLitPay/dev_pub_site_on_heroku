package main.exception;

import java.util.Map;

public class ShortTextException extends RuntimeException{
    private Map<String, String> error;

    public ShortTextException(Map<String, String> error) {
        this.error = error;
    }

    public Map<String, String> getError() {
        return error;
    }
}
