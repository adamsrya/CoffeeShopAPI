package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.Address;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.address.AddressResponse;
import coffeeshopproject.CoffeeShopAPI.model.address.CreateAddressModel;
import coffeeshopproject.CoffeeShopAPI.model.address.LabelCategoriAddressModel;
import coffeeshopproject.CoffeeShopAPI.model.address.UpdateAddressModel;
import coffeeshopproject.CoffeeShopAPI.repository.AddressRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    private User user;


    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
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
    }

    @Test
    void CreateAddressSuccess() throws Exception{
        CreateAddressModel req = new CreateAddressModel();
        req.setName("adam");
        req.setHandphone("08124324322");
        req.setCountry("Indonesia");
        req.setProvince("sulsel");
        req.setCity("makassar");
        req.setStreetname("jalanan");
        req.setPostalcode("40321");
        req.setOtherdetails("lorem ipsum");
        req.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        mockMvc.perform(
                post("/api/address")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("X-TOKEN-API", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNotNull(response.getData());
            assertEquals("adam",response.getData().getName());
            assertEquals("jalanan",response.getData().getStreetname());
        });
    }
    @Test
    void CreateAddressUnauthorized() throws Exception{
        CreateAddressModel req = new CreateAddressModel();
        req.setName("adam");
        req.setHandphone("08124324322");
        req.setCountry("Indonesia");
        req.setProvince("sulsel");
        req.setCity("makassar");
        req.setStreetname("jalanan");
        req.setPostalcode("40321");
        req.setOtherdetails("lorem ipsum");
        req.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        mockMvc.perform(
                post("/api/address")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))

        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void CreateAddressBadRequest() throws Exception{
        CreateAddressModel req = new CreateAddressModel();
        req.setName("adam");
        req.setHandphone("08124324322");
        req.setCountry("Indonesia");
        req.setProvince("sulsel");
        req.setCity("makassar");
        req.setStreetname("");
        req.setPostalcode("");
        req.setOtherdetails("");
        req.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        mockMvc.perform(
                post("/api/address")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("X-TOKEN-API", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Response<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());


        });
    }
    @Test
    void UpdateAddressSuccess() throws Exception{
        Address address = new Address();
        address.setName("adam");
        address.setHandphone("08124324322");
        address.setCountry("Indonesia");
        address.setProvince("sulsel");
        address.setCity("makassar");
        address.setStreetname("jalanan");
        address.setPostalcode("40321");
        address.setOtherdetails("lorem ipsum");
        address.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        address.setUser(user);
        addressRepository.save(address);

        UpdateAddressModel req = new UpdateAddressModel();
        req.setName("jeki");
        req.setHandphone("08124324322");
        req.setCountry("Indonesia");
        req.setProvince("sulsel");
        req.setCity("makassar");
        req.setStreetname("jalan bakti");
        req.setPostalcode("40321");
        req.setOtherdetails("lorem ipsum");
        req.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        mockMvc.perform(
                put("/api/address/{id}", address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("X-TOKEN-API", "test")

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNotNull(response.getData());
            assertEquals("jeki",response.getData().getName());
            assertEquals("jalan bakti",response.getData().getStreetname());
        });
    }

    @Test
    void UpdateAddressUnauthorized() throws Exception{
        Address address = new Address();
        address.setName("adam");
        address.setHandphone("08124324322");
        address.setCountry("Indonesia");
        address.setProvince("sulsel");
        address.setCity("makassar");
        address.setStreetname("jalanan");
        address.setPostalcode("40321");
        address.setOtherdetails("lorem ipsum");
        address.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        address.setUser(user);
        addressRepository.save(address);

        UpdateAddressModel req = new UpdateAddressModel();
        req.setName("jeki");
        req.setHandphone("08124324322");
        req.setCountry("Indonesia");
        req.setProvince("sulsel");
        req.setCity("makassar");
        req.setStreetname("jalan bakti");
        req.setPostalcode("40321");
        req.setOtherdetails("lorem ipsum");
        req.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        mockMvc.perform(
                put("/api/address/{id}", address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))

        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void UpdateAddressBadRequest() throws Exception{
        Address address = new Address();
        address.setName("adam");
        address.setHandphone("08124324322");
        address.setCountry("Indonesia");
        address.setProvince("sulsel");
        address.setCity("makassar");
        address.setStreetname("jalanan");
        address.setPostalcode("40321");
        address.setOtherdetails("lorem ipsum");
        address.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        address.setUser(user);
        addressRepository.save(address);

        UpdateAddressModel req = new UpdateAddressModel();
        req.setName("jeki");
        req.setHandphone("08124324322");
        req.setCountry("Indonesia");
        req.setProvince("sulsel");
        req.setCity("makassar");
        req.setStreetname("");
        req.setPostalcode("");
        req.setOtherdetails("");
        req.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        mockMvc.perform(
                put("/api/address/{id}", address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("X-TOKEN-API", "test")

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            Response<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());

        });
    }
    @Test
    void UpdateAddressNotFound() throws Exception{
        Address address = new Address();
        address.setName("adam");
        address.setHandphone("08124324322");
        address.setCountry("Indonesia");
        address.setProvince("sulsel");
        address.setCity("makassar");
        address.setStreetname("jalanan");
        address.setPostalcode("40321");
        address.setOtherdetails("lorem ipsum");
        address.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        address.setUser(user);
        addressRepository.save(address);

        UpdateAddressModel req = new UpdateAddressModel();
        req.setName("jeki");
        req.setHandphone("08124324322");
        req.setCountry("Indonesia");
        req.setProvince("sulsel");
        req.setCity("makassar");
        req.setStreetname("dasdsad");
        req.setPostalcode("dasdsa");
        req.setOtherdetails("dasdsa");
        req.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        mockMvc.perform(
                put("/api/address/8")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("X-TOKEN-API", "test")

        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            Response<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());

        });
    }
    @Test
    void GetAddressSuccess() throws Exception{
        Address address = new Address();
        address.setName("adam");
        address.setHandphone("08124324322");
        address.setCountry("Indonesia");
        address.setProvince("sulsel");
        address.setCity("makassar");
        address.setStreetname("jalanan");
        address.setPostalcode("40321");
        address.setOtherdetails("lorem ipsum");
        address.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        address.setUser(user);
        addressRepository.save(address);

        mockMvc.perform(
                get("/api/address/{id}", address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API", "test")

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
            assertNotNull(response.getData());
            assertEquals("adam",response.getData().getName());
            assertEquals("jalanan",response.getData().getStreetname());
        });
    }
    @Test
    void GetAddressUnauthorized() throws Exception{
        Address address = new Address();
        address.setName("adam");
        address.setHandphone("08124324322");
        address.setCountry("Indonesia");
        address.setProvince("sulsel");
        address.setCity("makassar");
        address.setStreetname("jalanan");
        address.setPostalcode("40321");
        address.setOtherdetails("lorem ipsum");
        address.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        address.setUser(user);
        addressRepository.save(address);

        mockMvc.perform(
                get("/api/address/{id}", address.getId())
                        .accept(MediaType.APPLICATION_JSON)

        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());

        });
    }

    @Test
    void GetAddressNotFound() throws Exception{
        Address address = new Address();
        address.setName("adam");
        address.setHandphone("08124324322");
        address.setCountry("Indonesia");
        address.setProvince("sulsel");
        address.setCity("makassar");
        address.setStreetname("jalanan");
        address.setPostalcode("40321");
        address.setOtherdetails("lorem ipsum");
        address.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        address.setUser(user);
        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/address/5")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API", "test")

        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            Response<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }
    @Test
    void DeleteAddressSuccess() throws Exception{
        Address address = new Address();
        address.setName("adam");
        address.setHandphone("08124324322");
        address.setCountry("Indonesia");
        address.setProvince("sulsel");
        address.setCity("makassar");
        address.setStreetname("jalanan");
        address.setPostalcode("40321");
        address.setOtherdetails("lorem ipsum");
        address.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        address.setUser(user);
        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/address/{id}", address.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API", "test")

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }
    @Test
    void DeleteAddressNotFound() throws Exception{
        Address address = new Address();
        address.setName("adam");
        address.setHandphone("08124324322");
        address.setCountry("Indonesia");
        address.setProvince("sulsel");
        address.setCity("makassar");
        address.setStreetname("jalanan");
        address.setPostalcode("40321");
        address.setOtherdetails("lorem ipsum");
        address.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        address.setUser(user);
        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/address/4")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-TOKEN-API", "test")

        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void DeleteAddressUnauthorized() throws Exception{
        Address address = new Address();
        address.setName("adam");
        address.setHandphone("08124324322");
        address.setCountry("Indonesia");
        address.setProvince("sulsel");
        address.setCity("makassar");
        address.setStreetname("jalanan");
        address.setPostalcode("40321");
        address.setOtherdetails("lorem ipsum");
        address.setLabelas(LabelCategoriAddressModel.valueOf("HOME"));
        address.setUser(user);
        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/address/{id}", address.getId())
                        .accept(MediaType.APPLICATION_JSON)

        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }
}
