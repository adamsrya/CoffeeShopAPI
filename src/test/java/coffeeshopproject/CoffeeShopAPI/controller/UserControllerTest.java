package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.user.CreateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UpdateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UserResponse;
import coffeeshopproject.CoffeeShopAPI.repository.UserRepository;
import coffeeshopproject.CoffeeShopAPI.security.BCrypt;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

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
    void CreateUserSuccess() throws Exception {
        CreateUserModel req = new CreateUserModel();
        req.setFirstname("Adam");
        req.setLastname("Surya");
        req.setEmail("damsuryap@com");
        req.setPassword("Test1234");
        req.setRepassword("Test1234");

        mockMvc.perform(
                post("/api/users")
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

        mockMvc.perform(
                post("/api/users")
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
        user.setRepassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        userRepository.save(user);

        CreateUserModel req = new CreateUserModel();
        req.setFirstname("Adam");
        req.setLastname("Surya");
        req.setEmail("damsuryap@com");
        req.setPassword("Test1234");
        req.setRepassword("Test1234");


        mockMvc.perform(
                post("/api/users")
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
    void GetUserSuccess() throws Exception {
        User user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setRepassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API","test")

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getMessage());
            assertEquals("Adam",response.getData().getFirstname());
            assertEquals("damsuryap@com",response.getData().getEmail());
        });
    }
    @Test
    void GetUserUnauthorized() throws Exception {
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API","test")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());

        });
    }
    @Test
    void GetUserTokenNotSendUnauthorized() throws Exception {
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void GetUserTokenExpired() throws Exception {
        User user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setRepassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000000L);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API","test")

        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());

        });
    }
    @Test
    void UpdateUserUnauthorized() throws Exception {
        UpdateUserModel req = new UpdateUserModel();
        mockMvc.perform(
                put("/api/users/current/profile")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());

        });
    }
    @Test
    void UpdateUserProfileSuccess() throws Exception {
        User user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setRepassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);
        UpdateUserModel req = new UpdateUserModel();
        req.setFirstname("Jeki");
        req.setLasttname("Konz");
        mockMvc.perform(
                put("/api/users/current/profile")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("X-TOKEN-API","test")

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertEquals("Jeki",response.getData().getFirstname());
            assertEquals("damsuryap@com",response.getData().getEmail());
        });
    }
    @Test
    void UpdateUserProfileBadRequest() throws Exception {
        User user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setRepassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);
        UpdateUserModel req = new UpdateUserModel();
        req.setFirstname("");
        req.setLasttname("");
        mockMvc.perform(
                put("/api/users/current/profile")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("X-TOKEN-API","test")

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
        user.setPassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setRepassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);
        UpdateUserModel req = new UpdateUserModel();
        req.setPassword("dasdasdasd ");
        req.setRepassword("");
        mockMvc.perform(
                put("/api/users/current/auth")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("X-TOKEN-API","test")

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
        user.setPassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setRepassword(BCrypt.hashpw("Test1234",BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);
        UpdateUserModel req = new UpdateUserModel();
        req.setPassword("Kintamani0");
        req.setRepassword("Kintamani0");
        mockMvc.perform(
                put("/api/users/current/auth")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("X-TOKEN-API","test")

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }
}
