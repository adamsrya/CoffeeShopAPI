package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.user.RoleUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UpdateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UserResponse;
import coffeeshopproject.CoffeeShopAPI.repository.UserRepository;
import coffeeshopproject.CoffeeShopAPI.security.jwt.JwtService;
import coffeeshopproject.CoffeeShopAPI.util.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void GetUserSuccess() throws Exception {
        User user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(passwordEncoder.encode("Test1234"));
        user.setExpired(false);
        user.setRevoked(false);
        user.setCreated(new Date());
        user.setUpdated(null);
        user.setRole(RoleUserModel.ADMIN);
        String token = jwtService.generateToken(user);
        user.setToken(token);
        userRepository.save(user);
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getMessage());
        });
    }

    @Test
    void GetUserForbidden() throws Exception {
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
            if (result.getResponse().getContentLength() > 0) {
                Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });
                assertNotNull(response.getMessage());
            }
        });
    }


    @Test
    void UpdateUserForbidden() throws Exception {
        UpdateUserModel req = new UpdateUserModel();
        mockMvc.perform(
                put("/api/users/current/profile")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
            if (result.getResponse().getContentLength() > 0) {
                Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });
                assertNotNull(response.getMessage());
            }
        });
    }

    @Test
    void UpdateUserProfileSuccess() throws Exception {
        User user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(passwordEncoder.encode("Test1234"));
        user.setExpired(false);
        user.setRevoked(false);
        user.setCreated(new Date());
        user.setUpdated(null);
        user.setRole(RoleUserModel.ADMIN);
        String token = jwtService.generateToken(user);
        user.setToken(token);
        userRepository.save(user);
        UpdateUserModel req = new UpdateUserModel();
        req.setFirstname("Jeki");
        req.setLasttname("Konz");
        mockMvc.perform(
                put("/api/users/current/profile")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Authorization", "Bearer " + token)

        ). andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertEquals("Jeki", response.getData().getFirstname());
            assertEquals("damsuryap@com", response.getData().getEmail());
        });
    }

    @Test
    void UpdateUserProfileBadRequest() throws Exception {
        User user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(passwordEncoder.encode("Test1234"));
        user.setExpired(false);
        user.setRevoked(false);
        user.setCreated(new Date());
        user.setUpdated(null);
        user.setRole(RoleUserModel.ADMIN);
        String token = jwtService.generateToken(user);
        user.setToken(token);
        userRepository.save(user);
        UpdateUserModel req = new UpdateUserModel();
        req.setFirstname("");
        req.setLasttname("");
        mockMvc.perform(
                put("/api/users/current/profile")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Authorization", "Bearer " + token)

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void UpdateUserPasswordBadRequest() throws Exception {
        User user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(passwordEncoder.encode("Test1234"));
        user.setExpired(false);
        user.setRevoked(false);
        user.setCreated(new Date());
        user.setUpdated(null);
        user.setRole(RoleUserModel.ADMIN);
        String token = jwtService.generateToken(user);
        user.setToken(token);
        userRepository.save(user);
        UpdateUserModel req = new UpdateUserModel();
        req.setPassword("dasdasdasd ");
        req.setRepassword("");
        mockMvc.perform(
                put("/api/users/current/auth")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Authorization", "Bearer " + token)

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void UpdateUserPasswordSuccess() throws Exception {
        User user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(passwordEncoder.encode("Test1234"));
        user.setExpired(false);
        user.setRevoked(false);
        user.setCreated(new Date());
        user.setUpdated(null);
        user.setRole(RoleUserModel.ADMIN);
        String token = jwtService.generateToken(user);
        user.setToken(token);
        userRepository.save(user);
        UpdateUserModel req = new UpdateUserModel();
        req.setPassword("Kintamani0");
        req.setRepassword("Kintamani0");
        mockMvc.perform(
                put("/api/users/current/auth")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Authorization", "Bearer " + token)

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }
}
