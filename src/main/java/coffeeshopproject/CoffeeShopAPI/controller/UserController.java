package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.user.CreateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UpdateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UserResponse;
import coffeeshopproject.CoffeeShopAPI.services.UserService;
import coffeeshopproject.CoffeeShopAPI.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unchecked")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> create(@RequestBody CreateUserModel request) {
        userService.create(request);
        return Response.<String>builder()
                .message("Register is Success")
                .build();

    }

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<UserResponse> get(User request) {
        UserResponse userResponse = userService.get(request);
        return Response.<UserResponse>builder()
                .data(userResponse)
                .build();

    }

    @PutMapping(
            path = "/api/users/current/auth",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> updateauth(@RequestBody UpdateUserModel request,User user) {
        userService.updateAuthUser(request,user);
        return Response.<String>builder()
                .message("Success change passsword")
                .build();
    }

    @PutMapping(
            path = "/api/users/current/profile",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<UserResponse> updateprofil(@RequestBody UpdateUserModel request,User user) {
        UserResponse userResponse = userService.updateProfilUser(request,user);
        return Response.<UserResponse>builder()
                .message("Success update profil")
                .data(userResponse)
                .build();

    }
}
