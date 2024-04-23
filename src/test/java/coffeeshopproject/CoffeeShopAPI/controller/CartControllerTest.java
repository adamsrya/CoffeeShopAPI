package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.Cart;
import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartItemRequest;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartResponse;
import coffeeshopproject.CoffeeShopAPI.model.product.ProductCategoryModel;
import coffeeshopproject.CoffeeShopAPI.model.user.RoleUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UserResponse;
import coffeeshopproject.CoffeeShopAPI.repository.CartRepository;
import coffeeshopproject.CoffeeShopAPI.repository.ProductRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    ObjectMapper objectMapper;
    private String token;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {

        cartRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();

        user = new User();
        user.setFirstname("Adam");
        user.setLastname("Surya");
        user.setEmail("damsuryap@com");
        user.setPassword(passwordEncoder.encode("Test1234"));
        user.setExpired(false);
        user.setRevoked(false);
        user.setCreated(new Date());
        user.setUpdated(null);
        user.setRole(RoleUserModel.ADMIN);
        token = jwtService.generateToken(user);
        user.setToken(token);
        userRepository.save(user);

        product = new Product();
        product.setId("CF0001");
        product.setCategory(ProductCategoryModel.valueOf("COFFEE"));
        product.setName("test");
        product.setPrice(1L);
        product.setDescription("test");
        product.setExtras("test");
        product.setQuantity(2);
        productRepository.save(product);
    }

    @Test
    void AddtoCartFailedNotFound() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(2);
        request.setProductid("test");
        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNull(response.getData());
        });

    }

    @Test
    void AddtoCartForbidden() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(1);
        request.setProductid("CF0001");
        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-TOKEN-API", "salah")
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
    void AddtoCartSuccess() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(1);
        request.setProductid("CF0001");
        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNotNull(response.getData());

            List<Cart> carts = cartRepository.findAllByUser(user);
            assertThat(carts).isNotNull().hasSize(1);

            Cart cart = carts.get(0);
            assertThat(cart).isNotNull();
            assertThat(cart.getUser()).isEqualTo(user);
            assertThat(cart.getProduct()).isEqualTo(product);
            assertThat(cart.getQuantity()).isEqualTo(request.getQuantity());

        });
    }

    @Test
    void GetCartSuccess() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(1);
        request.setProductid("CF0001");

        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token)
        ).andExpect(
                status().isOk());

        mockMvc.perform(
                get("/api/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<CartResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNotNull(response.getData());
            assertThat(response.getData().getAmount()).isNotNull();
        });
    }
    @Test
    void GetCartFailed() throws Exception {
        mockMvc.perform(
                get("/api/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Response<CartResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNull(response.getData());

        });
    }
    @Test
    void GetCartForbidden() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(1);
        request.setProductid("CF0001");

        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer "+ token)
        ).andExpect(
                status().isOk());

        mockMvc.perform(
                get("/api/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API", "salah")
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
    void UpdateCartSuccess() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(1);
        request.setProductid("CF0001");
        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token)
        ).andExpect(
                status().isOk()
        );

        CartItemRequest requestupdt = new CartItemRequest();
        requestupdt.setQuantity(4);
        mockMvc.perform(
                patch("/api/cart/CF0001")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestupdt))
                        .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<CartResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNotNull(response.getData());

            List<Cart> carts = cartRepository.findAllByUser(user);
            assertThat(carts).isNotNull().hasSize(1);

            Cart cart = carts.get(0);
            assertThat(cart).isNotNull();
            assertThat(cart.getUser()).isEqualTo(user);
            assertThat(cart.getProduct()).isEqualTo(product);
            assertThat(cart.getQuantity()).isEqualTo(requestupdt.getQuantity());

        });
    }
    @Test
    void UpdateCartItemNotFound() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(1);
        request.setProductid("CF0001");
        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token)
        ).andExpect(
                status().isOk()
        );

        CartItemRequest requestupdt = new CartItemRequest();
        requestupdt.setQuantity(4);
        mockMvc.perform(
                patch("/api/cart/CF0002")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestupdt))
                        .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            Response<CartResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNull(response.getData());
        });
    }

    @Test
    void UpdateCartForbidden() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(1);
        request.setProductid("CF0001");
        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token)
        ).andExpect(
                status().isOk()
        );

        CartItemRequest requestupdt = new CartItemRequest();
        requestupdt.setQuantity(0);
        mockMvc.perform(
                patch("/api/cart/CF0001")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestupdt))
                        .header("X-TOKEN-API", "salah")
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
    void DeleteAllItemInCarts() throws Exception {

        product = new Product();
        product.setId("CF0001");
        product.setCategory(ProductCategoryModel.valueOf("COFFEE"));
        product.setName("test");
        product.setPrice(1L);
        product.setDescription("test");
        product.setExtras("test");
        product.setQuantity(2);
        productRepository.save(product);

        Product product2 = new Product();
        product2.setId("CF0002");
        product2.setCategory(ProductCategoryModel.valueOf("COFFEE"));
        product2.setName("test");
        product2.setPrice(1L);
        product2.setDescription("test");
        product2.setExtras("test");
        product2.setQuantity(2);
        productRepository.save(product2);

        for (int i = 1; i < 3; i++) {
            CartItemRequest request = new CartItemRequest();
            request.setQuantity(1);
            request.setProductid("CF000"+i);
            mockMvc.perform(
                    post("/api/cart/add")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + token)
            ).andExpect(
                    status().isOk()
            );
        }
        mockMvc.perform(
                delete("/api/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNotNull(response.getData());
        });
    }
    @Test
    void DeleteItemInCarts() throws Exception {

        Product product = new Product();
        product.setId("CF0001");
        product.setCategory(ProductCategoryModel.valueOf("COFFEE"));
        product.setName("test");
        product.setPrice(1L);
        product.setDescription("test");
        product.setExtras("test");
        product.setQuantity(2);
        productRepository.save(product);

            CartItemRequest request = new CartItemRequest();
            request.setQuantity(1);
            request.setProductid("CF0001");
            mockMvc.perform(
                    post("/api/cart/add")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .header("Authorization", "Bearer " + token)
            ).andExpect(
                    status().isOk()
            );

        CartItemRequest requestupdt = new CartItemRequest();
        requestupdt.setQuantity(0);
        mockMvc.perform(
                patch("/api/cart/CF0001")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestupdt))
                        .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<CartResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNotNull(response.getData());
        });
    }

}
