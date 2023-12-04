package coffeeshopproject.CoffeeShopAPI.services.impl;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.TokenResponse;
import coffeeshopproject.CoffeeShopAPI.model.user.LoginUserRequest;
import coffeeshopproject.CoffeeShopAPI.repository.UserRepository;
import coffeeshopproject.CoffeeShopAPI.security.BCrypt;
import coffeeshopproject.CoffeeShopAPI.services.AuthService;
import coffeeshopproject.CoffeeShopAPI.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ValidationUtil validationUtil;
    @Override
    public TokenResponse login(LoginUserRequest request) {
        validationUtil.validate(request);
        User user = userRepository.findById(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or Password wrong"));
        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);
            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiretAt(user.getTokenExpiredAt())
                    .build();
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or Password wrong");
    }

    private Long next30Days() {
        return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
    }
    @Override
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);
        userRepository.save(user);
    }
}
