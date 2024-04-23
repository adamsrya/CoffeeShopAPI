package coffeeshopproject.CoffeeShopAPI.services;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.TokenResponse;
import coffeeshopproject.CoffeeShopAPI.model.user.CreateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.LoginUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface AuthService {
     void create(CreateUserModel user);
     TokenResponse login(LoginUserRequest request);
     void logout(String userId);
     void refreshToken(HttpServletRequest request,HttpServletResponse response) throws IOException;
}
