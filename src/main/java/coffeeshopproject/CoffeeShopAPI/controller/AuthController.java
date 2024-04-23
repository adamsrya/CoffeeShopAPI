package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.TokenResponse;
import coffeeshopproject.CoffeeShopAPI.model.user.CreateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.LoginUserRequest;
import coffeeshopproject.CoffeeShopAPI.services.AuthService;
import coffeeshopproject.CoffeeShopAPI.util.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping(
            path = "/api/auth/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> create(@RequestBody CreateUserModel request) {
        authService.create(request);
        return Response.<String>builder()
                .message("Register is Success")
                .build();

    }
    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return Response.<TokenResponse>builder()
                .data(tokenResponse)
                .build();
    }
    @PostMapping(
            path = "/api/auth/refresh-token",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        authService.refreshToken(request,response);
        return Response.<String>builder().data("OK").build();
    }
    @DeleteMapping("/api/auth/logout")
    public Response<String> logout(@AuthenticationPrincipal User user) {
        authService.logout(user.getUsername());
        return Response.<String>builder().data("OK").build();
    }
}
