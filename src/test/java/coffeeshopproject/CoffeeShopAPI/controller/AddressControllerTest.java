package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.Address;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.address.AddressResponse;
import coffeeshopproject.CoffeeShopAPI.model.address.CreateAddressModel;
import coffeeshopproject.CoffeeShopAPI.model.address.LabelCategoriAddressModel;
import coffeeshopproject.CoffeeShopAPI.model.address.UpdateAddressModel;
import coffeeshopproject.CoffeeShopAPI.model.user.RoleUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UserResponse;
import coffeeshopproject.CoffeeShopAPI.repository.AddressRepository;
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
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
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
                        .header("Authorization", "Bearer " + token)
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
    void CreateAddressForbidden() throws Exception{
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
                        .header("Authorization", "Bearer " + token)
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
                        .header("Authorization", "Bearer " + token)

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
    void UpdateAddressForbidden() throws Exception{
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
                        .header("Authorization", "Bearer " + token)

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
                        .header("Authorization", "Bearer " + token)

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
                        .header("Authorization", "Bearer " + token)

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
    void GetAddressForbidden() throws Exception{
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
                        .header("Authorization", "Bearer " + token)

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
                        .header("Authorization", "Bearer " + token)

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
                delete("/api/address/500")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)

        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            Response<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getMessage());
        });
    }

    @Test
    void DeleteAddressForbidden() throws Exception{
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
                status().isForbidden()
        ).andDo(result -> {
            if (result.getResponse().getContentLength() > 0) {
                Response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });
                assertNotNull(response.getMessage());
            }
        });
    }
}
