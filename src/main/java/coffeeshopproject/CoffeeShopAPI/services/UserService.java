package coffeeshopproject.CoffeeShopAPI.services;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.user.CreateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UpdateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UserResponse;

public interface UserService {


    UserResponse get();

    void updateAuthUser(UpdateUserModel req, String userId);

    UserResponse updateProfilUser(UpdateUserModel req, String userId);

    void deleteUser(String id,String admin);


}
