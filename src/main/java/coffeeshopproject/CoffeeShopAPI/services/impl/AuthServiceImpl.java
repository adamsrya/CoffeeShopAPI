package coffeeshopproject.CoffeeShopAPI.services.impl;

import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.TokenResponse;
import coffeeshopproject.CoffeeShopAPI.model.user.CreateUserModel;
import coffeeshopproject.CoffeeShopAPI.model.user.LoginUserRequest;
import coffeeshopproject.CoffeeShopAPI.model.user.RoleUserModel;
import coffeeshopproject.CoffeeShopAPI.repository.UserRepository;
import coffeeshopproject.CoffeeShopAPI.security.jwt.JwtService;
import coffeeshopproject.CoffeeShopAPI.services.AuthService;
import coffeeshopproject.CoffeeShopAPI.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;
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
        boolean adminexist = userRepository.existsByRole(RoleUserModel.ADMIN);
        User users = User.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .revoked(false)
                .expired(false)
                .created(new Date())
                .updated(null)
                .build();
        if (adminexist && RoleUserModel.ADMIN.equals(user.getRole())){
            users.setRole(RoleUserModel.USER);
        }else {
            users.setRole(user.getRole());
        }
        userRepository.save(users);

    }
    @Override
    public TokenResponse login(LoginUserRequest request) {
        validationUtil.validate(request);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (request.getEmail(),request.getPassword()));
        User user = userRepository.findById(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email or Password wrong"));
        revokeAllUserTokens(user);
        var jwttoken = jwtService.generateToken(user);
        var refreshtoken = jwtService.generateRefreshToken(user);
        user.setRevoked(false);
        user.setExpired(false);
        user.setToken(jwttoken);
            userRepository.save(user);
            return TokenResponse.builder()
                    .token(user.getToken())
                    .refreshtoken(refreshtoken)
                    .build();

    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String email;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        email = jwtService.extractUsername(refreshToken);
        if (email != null) {
            User user = this.userRepository.findByEmail(email)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String token = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                user.setToken(token);
                user.setRevoked(false);
                user.setExpired(false);
                userRepository.save(user);
                TokenResponse authResponse = TokenResponse.builder()
                        .token(token)
                        .refreshtoken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
    @Override
    public void logout(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setToken(null);
        user.setRevoked(true);
        user.setExpired(true);
        userRepository.save(user);
    }
    private void revokeAllUserTokens(User user) {
        List<User> validUserTokens = userRepository.findByEmailAndRevokedFalseAndExpiredFalse(user.getEmail());

        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });

            userRepository.saveAll(validUserTokens);
        }
    }
}
