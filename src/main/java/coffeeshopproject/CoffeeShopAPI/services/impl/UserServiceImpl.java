package coffeeshopproject.CoffeeShopAPI.services.impl;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.user.RoleUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UpdateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UserResponse;
import coffeeshopproject.CoffeeShopAPI.repository.UserRepository;
import coffeeshopproject.CoffeeShopAPI.security.BCrypt;
import coffeeshopproject.CoffeeShopAPI.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserResponse get() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            // You may need to adjust this based on your UserDetails implementation
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Map the user details to a UserResponse object
            return UserResponse.builder()
                    .name(user.getFirstname() + " " + user.getLastname())
                    .firstname(user.getFirstname())
                    .lasttname(user.getLastname())
                    .email(user.getEmail())
                    .build();
        } else {
            throw new RuntimeException("User details not available");
        }
    }

    @Override
    public void updateAuthUser(UpdateUserModel req, String userId) {

        if (!req.getPassword().matches(req.getRepassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passsword not matches");
        }
        if (req.getPassword().isEmpty() && req.getRepassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passsword not matches or password not blank");
        }
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        users.setPassword(BCrypt.hashpw(req.getPassword(), BCrypt.gensalt()));
        users.setUpdated(new Date());

        userRepository.save(users);
    }

    @Override
    public UserResponse updateProfilUser(UpdateUserModel req, String userId) {
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        users.setFirstname(req.getFirstname());
        users.setLastname(req.getLasttname());
        users.setUpdated(new Date());
        userRepository.save(users);
        return UserResponse.builder()
                .firstname(req.getFirstname())
                .lasttname(req.getLasttname())
                .email(users.getEmail())
                .name(users.getFirstname() + " " + users.getLastname())
                .build();

    }

    @Override
    public void deleteUser(String id, String admin) {
        User userAdmin = userRepository.findByEmail(admin)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (!userAdmin.getRole().equals(RoleUserModel.ADMIN)) {
            throw new
                    ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authorized to perform this action");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        userRepository.delete(user);

    }
}
