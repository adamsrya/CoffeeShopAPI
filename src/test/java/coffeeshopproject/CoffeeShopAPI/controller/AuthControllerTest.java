package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.TokenResponse;
import coffeeshopproject.CoffeeShopAPI.model.user.CreateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.LoginUserRequest;
import coffeeshopproject.CoffeeShopAPI.model.user.RoleUserModel;
import coffeeshopproject.CoffeeShopAPI.repository.UserRepository;
import coffeeshopproject.CoffeeShopAPI.security.BCrypt;
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
    JwtService jwtService;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
    @Test
    void CreateUserSuccess() throws Exception {
        CreateUserModel req = new CreateUserModel();
        req.setFirstname("Adam");
        req.setLastname("Surya");
        req.setEmail("damsuryap@com");
        req.setPassword("Test1234");
        req.setRepassword("Test1234");
        req.setRole(RoleUserModel.ADMIN);

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }
    @Test
    void CreateUserBadRequest() throws Exception {
        CreateUserModel req = new CreateUserModel();
        req.setFirstname("");
        req.setLastname("");
        req.setEmail("");
        req.setPassword("");
        req.setRepassword("");
        req.setRole(RoleUserModel.ADMIN);

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }
    @Test
    void CreateUserDuplicate() throws Exception {
        User user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setRole(RoleUserModel.ADMIN);
        userRepository.save(user);

        CreateUserModel req = new CreateUserModel();
        req.setFirstname("Adam");
        req.setLastname("Surya");
        req.setEmail("damsuryap@com");
        req.setPassword("Test1234");
        req.setRepassword("Test1234");
        req.setRole(RoleUserModel.ADMIN);


        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
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
                status().isForbidden()
        ).andDo(result -> {
            if (result.getResponse().getContentLength() > 0) {
                Response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });
                assertNotNull(response.getMessage());
            }
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
                .role(RoleUserModel.ADMIN)
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
                status().isForbidden()
        ).andDo(result -> {
            if (result.getResponse().getContentLength() > 0) {
                Response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });
                assertNotNull(response.getMessage());
            }
        });
    }

    @Test
    void LoginSuccess() throws Exception {
        User user = User.builder()
                .firstname("adam")
                .lastname("surya")
                .email("test")
                .password(BCrypt.hashpw("Test1234",BCrypt.gensalt()))
                .role(RoleUserModel.ADMIN)
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


            User userDb = userRepository.findById("test").orElse(null);
            assertNotNull(userDb);
            assertEquals(userDb.getToken(),response.getData().getToken());
        });
    }

  /*  @Test
    void LogoutFailed() throws Exception {
        User user = User.builder()
                .firstname("adam")
                .lastname("surya")
                .email("test")
                .password(BCrypt.hashpw("Test1234",BCrypt.gensalt()))
                .role(RoleUserModel.ADMIN)
                .revoked(false)
                .expired(false)
                .build();
        String token = jwtService.generateToken(user);
        user.setToken(token);
        userRepository.save(user);
        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer ")
        ).andExpectAll(
                status().isInternalServerError()
        ).andDo(result -> {
            Response<String> response =objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }*/

    @Test
    void LogoutSuccess() throws Exception {
        User user = User.builder()
                .firstname("adam")
                .lastname("surya")
                .email("test")
                .password(BCrypt.hashpw("Test1234",BCrypt.gensalt()))
                .role(RoleUserModel.ADMIN)
                .revoked(false)
                .expired(false)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<String> response =objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getMessage());
            assertEquals("OK",response.getData());

            User userDb = userRepository.findById("test").orElse(null);
            assertNotNull(userDb);
            assertNull(userDb.getToken());
        });
    }


}
