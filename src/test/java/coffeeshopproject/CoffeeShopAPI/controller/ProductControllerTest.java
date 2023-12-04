package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.model.product.CreateProductModel;
import coffeeshopproject.CoffeeShopAPI.model.product.ProductCategoryModel;
import coffeeshopproject.CoffeeShopAPI.model.product.ProductResponse;
import coffeeshopproject.CoffeeShopAPI.model.product.UpdateProductModel;
import coffeeshopproject.CoffeeShopAPI.repository.ProductRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {


        productRepository.deleteAll();
    }


    @Test
    void CreateProductSuccess() throws Exception {
        String idproduct = "test";
        CreateProductModel request = new CreateProductModel();
        request.setId(idproduct.toUpperCase());
        request.setCategory(ProductCategoryModel.valueOf("COFFEE"));
        request.setName("test");
        request.setPrice(1L);
        request.setDescription("test");
        request.setExtras("test");
        request.setQuantity(1);

        mockMvc.perform(
                post("/api/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertEquals(idproduct.toUpperCase(), response.getData().getId());
            assertEquals("COFFEE", String.valueOf(response.getData().getCategory()));
            assertEquals("test", response.getData().getName());
            assertEquals(1L, response.getData().getPrice());
            assertEquals("test", response.getData().getDescription());
            assertEquals("test", response.getData().getExtras());
            assertEquals(1, response.getData().getQuantity());
            assertTrue(productRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void CreateProductBadRequest() throws Exception {
        CreateProductModel request = new CreateProductModel();
        request.setId("");
        request.setCategory(null);
        request.setName("");
        request.setPrice(0L);
        request.setDescription("");
        request.setExtras("");
        request.setQuantity(0);

        mockMvc.perform(
                post("/api/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Response<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());

        });
    }

    @Test
    void CreateProductDuplicate() throws Exception {
        Product product = new Product();
        product.setId("cf0001");
        product.setCategory(ProductCategoryModel.valueOf("COFFEE"));
        product.setName("test");
        product.setPrice(1L);
        product.setDescription("test");
        product.setExtras("test");
        product.setQuantity(2);
        productRepository.save(product);

        CreateProductModel productModel = new CreateProductModel();
        productModel.setId("cf0001");
        productModel.setCategory(ProductCategoryModel.valueOf("COFFEE"));
        productModel.setName("test");
        productModel.setPrice(1L);
        productModel.setDescription("test");
        productModel.setExtras("test");
        productModel.setQuantity(2);

        mockMvc.perform(
                post("/api/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productModel))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Response<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());

        });
    }

    @Test
    void GetProductNotFound() throws Exception {
        mockMvc.perform(
                get("/api/products/CF0001")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            Response<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());

        });
    }

    @Test
    void GetProductSuccess() throws Exception {
        Optional<Product> productDB = productRepository.findByid("test");
        Product product = new Product();
        product.setId(String.valueOf(productDB).toUpperCase());
        product.setCategory(ProductCategoryModel.valueOf("COFFEE"));
        product.setName("test");
        product.setPrice(1L);
        product.setDescription("test");
        product.setExtras("test");
        product.setQuantity(2);
        productRepository.save(product);
        mockMvc.perform(
                get("/api/products/" + product.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<ProductResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
            assertNotNull(response.getMessage());
            assertEquals(product.getId(), response.getData().getId());
            assertEquals(product.getCategory(), response.getData().getCategory());
            assertEquals(product.getName(), response.getData().getName());
            assertEquals(product.getPrice(), response.getData().getPrice());
            assertEquals(product.getDescription(), response.getData().getDescription());
            assertEquals(product.getExtras(), response.getData().getExtras());
            assertEquals(product.getQuantity(), response.getData().getQuantity());
        });

    }

    @Test
    void UpdateProductSuccess() throws Exception {
        Optional<Product> productDB = productRepository.findByid("test");
        Product product = new Product();
        product.setId(String.valueOf(productDB).toUpperCase());
        product.setCategory(ProductCategoryModel.valueOf("COFFEE"));
        product.setName("test");
        product.setPrice(1L);
        product.setDescription("test");
        product.setExtras("test");
        product.setQuantity(2);
        productRepository.save(product);

        UpdateProductModel request = new UpdateProductModel();
        request.setCategory(ProductCategoryModel.valueOf("NONCOFFEE"));
        request.setName("Adam");
        request.setPrice(3L);
        request.setDescription("LoremIpsum");
        request.setExtras("KG");
        request.setQuantity(10);

        mockMvc.perform(
                put("/api/products/" + product.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<ProductResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
            assertNotNull(response.getMessage());
            assertEquals(request.getCategory(), response.getData().getCategory());
            assertEquals(request.getName(), response.getData().getName());
            assertEquals(request.getPrice(), response.getData().getPrice());
            assertEquals(request.getDescription(), response.getData().getDescription());
            assertEquals(request.getExtras(), response.getData().getExtras());
            assertEquals(request.getQuantity(), response.getData().getQuantity());
            assertTrue(productRepository.existsById(response.getData().getId()));
        });

    }


    @Test
    void SearchNotFound() throws Exception {
        mockMvc.perform(
                get("/api/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<List<Product>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertEquals(0, response.getData().size());
            assertEquals(0, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getSize());
        });
    }

    @Test
    void SearchProductSuccess() throws Exception {
        for (int i = 0; i < 20; i++) {
            Product product = new Product();
            product.setId("CF000" + UUID.randomUUID());
            product.setCategory(ProductCategoryModel.valueOf("COFFEE"));
            product.setName("coffee toraja " + i);
            product.setPrice(1L);
            product.setDescription("test");
            product.setExtras("test");
            product.setQuantity(2);
            productRepository.save(product);
        }

        mockMvc.perform(
                get("/api/products")
                        .queryParam("name", "toraja")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<List<Product>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertEquals(10, response.getData().size());
            assertEquals(2, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getSize());

        });

        mockMvc.perform(
                get("/api/products")
                        .queryParam("description", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<List<Product>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertEquals(10, response.getData().size());
            assertEquals(2, response.getPaging().getTotalPage());
            assertEquals(0, response.getPaging().getCurrentPage());
            assertEquals(10, response.getPaging().getSize());

        });
    }

    @Test
    void DeleteProductSuccess() throws Exception {
        Optional<Product> productDB = productRepository.findByid("test");
        Product product = new Product();
        product.setId(String.valueOf(productDB).toUpperCase());
        product.setCategory(ProductCategoryModel.valueOf("COFFEE"));
        product.setName("test");
        product.setPrice(1L);
        product.setDescription("test");
        product.setExtras("test");
        product.setQuantity(2);
        productRepository.save(product);

        mockMvc.perform(
                delete("/api/products/" + product.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals("OK", response.getData());
        });
    }

    @Test
    void DeleteProductNotFound() throws Exception {
        mockMvc.perform(
                delete("/api/products/adsada")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }
}
