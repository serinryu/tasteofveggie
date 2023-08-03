package com.serinryu.springproject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serinryu.springproject.config.AccessTokenRequest;
import com.serinryu.springproject.config.jwt.JwtProvider;
import com.serinryu.springproject.controller.TokenApiController;
import com.serinryu.springproject.config.jwt.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenApiController.class)
class TokenApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private JwtProvider jwtProvider; // Mocking JwtProvider

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
    @Test
    @WithMockUser
    public void createNewAccessToken() throws Exception {
        // Mocked refresh token and new access token
        String refreshToken = "mocked-refresh-token";
        String newAccessToken = "mocked-new-access-token";

        // Mock the behavior of the TokenService's createNewAccessToken method
        Mockito.when(tokenService.createNewAccessToken(refreshToken)).thenReturn(newAccessToken);

        // Create the request body
        AccessTokenRequest request = new AccessTokenRequest();
        request.setRefreshToken(refreshToken);
        final String requestBody = objectMapper.writeValueAsString(request);

        // Perform the POST request to the endpoint
        mockMvc.perform(post("/api/token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))

                // Expect HTTP status 201 Created
                .andExpect(status().isCreated())

                // Expect the response to have a non-empty access token
                .andExpect(jsonPath("$.accessToken").value(newAccessToken));
    }
}
