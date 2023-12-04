package coffeeshopproject.CoffeeShopAPI.services;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.user.CreateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UpdateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UserResponse;

public interface UserService {

    void create(CreateUserModel user);

    UserResponse get(User user);

    void updateAuthUser(UpdateUserModel req, User user);
    UserResponse updateProfilUser(UpdateUserModel req,User user);


}
