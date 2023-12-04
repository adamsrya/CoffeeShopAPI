package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.Cart;
import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartItemRequest;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartResponse;
import coffeeshopproject.CoffeeShopAPI.model.product.ProductCategoryModel;
import coffeeshopproject.CoffeeShopAPI.repository.CartRepository;
import coffeeshopproject.CoffeeShopAPI.repository.ProductRepository;
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

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
    ObjectMapper objectMapper;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {

        cartRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();

        user = User.builder()
                .firstname("adam")
                .lastname("surya")
                .email("test@.com")
                .password(BCrypt.hashpw("Test1234", BCrypt.gensalt()))
                .repassword(BCrypt.hashpw("Test1234", BCrypt.gensalt()))
                .token("test")
                .tokenExpiredAt(System.currentTimeMillis() + 1000000L)
                .build();
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
                        .header("X-TOKEN-API", "test")
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
    void AddtoCartUnauthorized() throws Exception {
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
                status().isUnauthorized()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNull(response.getData());

        });
    }
    @Test
    void AddtoCartDuplicate() throws Exception {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(5);
        cartRepository.save(cart);

        CartItemRequest request = new CartItemRequest();
        request.setQuantity(1);
        request.setProductid("CF0001");
        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-TOKEN-API", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getMessage());
            assertNull(response.getData());

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
                        .header("X-TOKEN-API", "test")
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
                        .header("X-TOKEN-API", "test")
        ).andExpect(
                status().isOk());

        mockMvc.perform(
                get("/api/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API", "test")
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
                        .header("X-TOKEN-API", "test")
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
    void GetCartUnauthorized() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(1);
        request.setProductid("CF0001");

        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-TOKEN-API", "test")
        ).andExpect(
                status().isOk());

        mockMvc.perform(
                get("/api/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API", "salah")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<CartResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNull(response.getData());
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
                        .header("X-TOKEN-API", "test")
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
                        .header("X-TOKEN-API", "test")
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
                        .header("X-TOKEN-API", "test")
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
                        .header("X-TOKEN-API", "test")
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
    void UpdateCartUnauthorized() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setQuantity(1);
        request.setProductid("CF0001");
        mockMvc.perform(
                post("/api/cart/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-TOKEN-API", "test")
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
                status().isUnauthorized()
        ).andDo(result -> {
            Response<CartResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNull(response.getData());
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

        for (int i = 1; i < 2; i++) {
            CartItemRequest request = new CartItemRequest();
            request.setQuantity(1);
            request.setProductid("CF000"+i);
            mockMvc.perform(
                    post("/api/cart/add")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .header("X-TOKEN-API", "test")
            ).andExpect(
                    status().isOk()
            );
        }
        mockMvc.perform(
                delete("/api/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNotNull(response.getData());
        });
    }


}
