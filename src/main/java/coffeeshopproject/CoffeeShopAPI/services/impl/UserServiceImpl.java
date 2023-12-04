package coffeeshopproject.CoffeeShopAPI.services.impl;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.user.CreateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UpdateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.UserResponse;
import coffeeshopproject.CoffeeShopAPI.repository.UserRepository;
import coffeeshopproject.CoffeeShopAPI.security.BCrypt;
import coffeeshopproject.CoffeeShopAPI.services.UserService;
import coffeeshopproject.CoffeeShopAPI.util.ValidationUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ValidationUtil validationUtil;
    @Override
    public void create(CreateUserModel user) {
        validationUtil.validate(user);
        if (!user.getPassword().matches(user.getRepassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password not matches");
        }
        if (userRepository.existsById(user.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email already exist");
        }


        User users = User.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .password(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()))
                .repassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()))
                .created(new Date())
                .updated(null)
                .build();
        userRepository.save(users);

    }

    @Override
    public UserResponse get(User user) {
        return UserResponse.builder()
                .firstname(user.getFirstname())
                .lasttname(user.getLastname())
                .email(user.getEmail())
                .name(user.getFirstname() + " " + user.getLastname())
                .build();
    }

    @Override
    public void updateAuthUser(UpdateUserModel req, User user) {
        if (!req.getPassword().matches(req.getRepassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passsword not matches");
        }
        if (req.getPassword().isEmpty() && req.getRepassword().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passsword not matches or password not blank");
        }
        User users = userRepository.findById(user.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        users.setPassword(BCrypt.hashpw(req.getPassword(),BCrypt.gensalt()));
        users.setRepassword(BCrypt.hashpw(req.getRepassword(),BCrypt.gensalt()));
        users.setUpdated(new Date());
        userRepository.save(users);
    }



    @Override
    public UserResponse updateProfilUser(UpdateUserModel req,User user) {
        User users = userRepository.findById(user.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        users.setFirstname(req.getFirstname());
        users.setLastname(req.getLasttname());
        users.setUpdated(new Date());
        userRepository.save(users);
        return UserResponse.builder()
                .firstname(req.getFirstname())
                .lasttname(req.getLasttname())
                .email(user.getEmail())
                .name(user.getFirstname()+" "+user.getLastname())
                .build();
                
    }
}
