package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.address.CreateAddressModel;
import coffeeshopproject.CoffeeShopAPI.model.address.AddressResponse;
import coffeeshopproject.CoffeeShopAPI.model.address.UpdateAddressModel;
import coffeeshopproject.CoffeeShopAPI.services.AddressService;
import coffeeshopproject.CoffeeShopAPI.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AddressController {

    @Autowired
    AddressService addressService;

    @PostMapping(
            path = "/api/address",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<AddressResponse> createaddress(@RequestBody CreateAddressModel req, User user) {
        AddressResponse address = addressService.createShpAddress(user,req) ;
        return Response.<AddressResponse>builder()
                .message("Success Create Data")
                .data(address)
                .build();

    }
    @PutMapping(
            path = "/api/address/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<AddressResponse> updateAddress(@RequestBody UpdateAddressModel
            req, @PathVariable int id, User user) {
        req.setId_address(id);
        var address = addressService.updateShpAddress(user,req);
        return Response.<AddressResponse>builder()
                .message("Success Update Data")
                .data(address)
                .build();

    }


    @GetMapping(
            path = "/api/address/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public Response<AddressResponse> getAddress(@PathVariable int id, User user) {
        AddressResponse address = addressService.getShpAddress(user,id);
        return Response.<AddressResponse>builder()
                .message("Success Get Data")
                .data(address)
                .build();

    }


    @DeleteMapping(
            path = "/api/address/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> deleteById(@PathVariable int id,User user){
        addressService.deleteaddress(user,id);
        return Response.<String>builder()
                .message("Success Delete Data")
                .data("OK")
                .build();


    }
}
