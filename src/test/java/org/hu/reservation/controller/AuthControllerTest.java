package org.hu.reservation.controller;

import org.hu.reservation.SecurityTestConfig;
import org.hu.reservation.dto.AccessToken;
import org.hu.reservation.dto.AuthRequest;
import org.hu.reservation.dto.Error;
import org.hu.reservation.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityTestConfig.class)
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @Test
    void testAuthenticateAndGetTokenSuccess() throws Exception {
        AuthRequest authRequest = new AuthRequest("user", "password");
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        Mockito.when(jwtService.generateToken(anyString())).thenReturn("jwt-token");

        ResultActions result = mockMvc.perform(post("/api/auth/accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user\", \"password\": \"password\"}"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"));
    }

    @Test
    void testAuthenticateAndGetTokenInvalidUserRequest() throws Exception {
        AuthRequest authRequest = new AuthRequest("user", "password");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(false);

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        ResultActions result = mockMvc.perform(post("/api/auth/accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user\", \"password\": \"password\"}"));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Invalid user request"));
    }

    @Test
    void testAuthenticateAndGetTokenServerError() throws Exception {
        AuthRequest authRequest = new AuthRequest("user", "password");

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Server error"));

        ResultActions result = mockMvc.perform(post("/api/auth/accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user\", \"password\": \"password\"}"));

        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Server unavailable"));
    }
}
