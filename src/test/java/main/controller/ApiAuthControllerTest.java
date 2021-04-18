package main.controller;

import main.api.request.AuthRegisterRequest;
import main.api.request.LoginRequest;
import main.api.response.AuthCheckResponse;
import main.api.response.CaptchaResponse;
import main.api.response.dto.AuthUserDTO;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.*;

public class ApiAuthControllerTest extends AbstractIntegrationTest {
    final AuthCheckResponse authCheckResponse;

    {
        authCheckResponse = new AuthCheckResponse(true,
                new AuthUserDTO(2, "Fredo", "/upload/EVn/RjG/wVS/Panda.jpeg", "fredo@m.com",
                        false, 0, false));
    }

    @Test
    public void authCheck() throws Exception {
        login("fredo@m.com", "111111");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/check"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(authCheckResponse)));
    }

    @Test
    public void login() throws Exception {
        LoginRequest request = new LoginRequest("fredo@m.com", "111111");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(authCheckResponse)));
    }

    @Test
    public void logout() throws Exception {
        login("fredo@m.com", "111111");
        AuthCheckResponse response = new AuthCheckResponse(true);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/logout"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(response)));
    }

    @Test
    public void getCaptcha() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/captcha"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void createUser() throws Exception {
        AuthRegisterRequest request = new AuthRegisterRequest(
                "test@m.com", "111111", "Test", "ahapikiyo", "40b35ec5-a4b2-457a-8d8e-06958a1c13b1");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}