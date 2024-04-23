package coffeeshopproject.CoffeeShopAPI.services;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.address.CreateAddressModel;
import coffeeshopproject.CoffeeShopAPI.model.address.AddressResponse;
import coffeeshopproject.CoffeeShopAPI.model.address.UpdateAddressModel;
import org.springframework.stereotype.Service;


public interface AddressService {

    AddressResponse createShpAddress(String userId,CreateAddressModel request);

    AddressResponse getShpAddress(String userId,int id);

    AddressResponse updateShpAddress(String userId,UpdateAddressModel request);

    void deleteaddress(String userId,int id);
}
