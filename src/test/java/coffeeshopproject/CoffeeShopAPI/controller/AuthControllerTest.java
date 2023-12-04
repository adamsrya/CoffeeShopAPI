package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.TokenResponse;
import coffeeshopproject.CoffeeShopAPI.model.user.LoginUserRequest;
import coffeeshopproject.CoffeeShopAPI.repository.AddressRepository;
import coffeeshopproject.CoffeeShopAPI.repository.UserRepository;
import coffeeshopproject.CoffeeShopAPI.security.BCrypt;
import coffeeshopproject.CoffeeShopAPI.util.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void LoginFailedUserNotFound() throws Exception {
        LoginUserRequest req = new LoginUserRequest();
        req.setEmail("test");
        req.setPassword("Testing1234");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))

        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void LoginFailedPasswordValidation() throws Exception {
        LoginUserRequest req = new LoginUserRequest();
        req.setEmail("test");
        req.setPassword("Test");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void LoginFailedWrongPassword() throws Exception {
        User user = User.builder()
                .firstname("adam")
                .lastname("surya")
                .email("test")
                .password(BCrypt.hashpw("Test1234",BCrypt.gensalt()))
                .repassword(BCrypt.hashpw("Test1234", BCrypt.gensalt()))
                .build();
        userRepository.save(user);
        LoginUserRequest req = new LoginUserRequest();
        req.setEmail("test");
        req.setPassword("Test12345");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))

        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void LoginSuccess() throws Exception {
        User user = User.builder()
                .firstname("adam")
                .lastname("surya")
                .email("test")
                .password(BCrypt.hashpw("Test1234",BCrypt.gensalt()))
                .repassword(BCrypt.hashpw("Test1234", BCrypt.gensalt()))
                .build();
        userRepository.save(user);
        LoginUserRequest req = new LoginUserRequest();
        req.setEmail("test");
        req.setPassword("Test1234");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getMessage());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getExpiretAt());

            User userDb = userRepository.findById("test").orElse(null);
            assertNotNull(userDb);
            assertEquals(userDb.getToken(),response.getData().getToken());
            assertEquals(userDb.getTokenExpiredAt(),response.getData().getExpiretAt());
        });
    }

    @Test
    void LogoutFailed() throws Exception {
        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<String> response =objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void LogoutSuccess() throws Exception {
        User user = User.builder()
                .firstname("adam")
                .lastname("surya")
                .email("test")
                .password(BCrypt.hashpw("Test1234",BCrypt.gensalt()))
                .repassword(BCrypt.hashpw("Test1234", BCrypt.gensalt()))
                .token("test")
                .tokenExpiredAt(System.currentTimeMillis() + 1000000L)
                .build();
        userRepository.save(user);
        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<String> response =objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getMessage());
            assertEquals("OK",response.getData());

            User userDb = userRepository.findById("test").orElse(null);
            assertNotNull(userDb);
            assertNull(userDb.getTokenExpiredAt());
            assertNull(userDb.getToken());
        });
    }


}
