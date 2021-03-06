package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import main.api.response.dto.AuthUserDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthCheckResponse {

    private boolean result;

    private AuthUserDTO user;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public AuthUserDTO getUser() {
        return user;
    }

    public void setUser(AuthUserDTO user) {
        this.user = user;
    }
}
