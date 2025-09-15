package com.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.model.MyAppUserService;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyAppUserService userService; // mocked dependency if needed

    @Test
    @WithMockUser(username = "test@example.com") // simulates logged-in user
    void authenticated_returnsTrueForAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/authenticated"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.authenticated").value(true))
               .andExpect(jsonPath("$.username").value("test@example.com"));
    }
}
