package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.TokenResponse;
import coffeeshopproject.CoffeeShopAPI.model.user.CreateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UpdateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UserResponse;
import coffeeshopproject.CoffeeShopAPI.security.jwt.JwtService;
import coffeeshopproject.CoffeeShopAPI.services.UserService;
import coffeeshopproject.CoffeeShopAPI.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unchecked")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserResponse getCurrentUser() {
        return userService.get();
    }

    @PutMapping(
            path = "/api/users/current/auth",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> updateauth(@RequestBody UpdateUserModel request, @AuthenticationPrincipal User user) {
        userService.updateAuthUser(request, user.getUsername());
        return Response.<String>builder()
                .message("Success change passsword")
                .build();
    }

    @PutMapping(
            path = "/api/users/current/profile",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<UserResponse> updateprofil(@RequestBody UpdateUserModel request, @AuthenticationPrincipal User user) {

        UserResponse userResponse = userService.updateProfilUser(request, user.getUsername());
        return Response.<UserResponse>builder()
                .message("Success update profil")
                .data(userResponse)
                .build();

    }

    @DeleteMapping(
            path = "/api/users/current/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<String> deleteUser(@PathVariable String id, @AuthenticationPrincipal User user) {
        userService.deleteUser(id, user.getEmail());
        return Response.<String>builder()
                .message("Success delete user")
                .data("Ok")
                .build();

    }
}
