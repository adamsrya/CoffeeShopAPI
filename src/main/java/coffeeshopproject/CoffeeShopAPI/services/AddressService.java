package coffeeshopproject.CoffeeShopAPI.services;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.address.CreateAddressModel;
import coffeeshopproject.CoffeeShopAPI.model.address.AddressResponse;
import coffeeshopproject.CoffeeShopAPI.model.address.UpdateAddressModel;
import org.springframework.stereotype.Service;


public interface AddressService {

    AddressResponse createShpAddress(User user,CreateAddressModel request);

    AddressResponse getShpAddress(User user,int id);

    AddressResponse updateShpAddress(User user,UpdateAddressModel request);

    void deleteaddress(User user,int id);
}
