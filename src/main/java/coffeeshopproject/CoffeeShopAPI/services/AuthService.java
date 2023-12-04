package coffeeshopproject.CoffeeShopAPI.services;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.TokenResponse;
import coffeeshopproject.CoffeeShopAPI.model.user.LoginUserRequest;

public interface AuthService {

     TokenResponse login(LoginUserRequest request);

     void logout(User user);
}
