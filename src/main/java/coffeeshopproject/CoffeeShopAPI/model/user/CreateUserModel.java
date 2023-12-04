package coffeeshopproject.CoffeeShopAPI.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Component
public class CreateUserModel {


    @NotBlank
    private String email;
    @NotBlank()
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,20}$" ,message = "Password must be at least 8 characters long, contain at least one lowercase letter, one uppercase letter, and one digit.")
    private String password;
    @NotBlank
    private String repassword;
    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;




}
