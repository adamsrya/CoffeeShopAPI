package coffeeshopproject.CoffeeShopAPI.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Component
public class UpdateUserModel {


    @NotBlank()
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,20}$" ,message = "Password must be at least 8 characters long, contain at least one lowercase letter, one uppercase letter, and one digit.")
    private String password;
    @NotBlank
    private String repassword;
    @NotBlank
    private String firstname;

    @NotBlank
    private String lasttname;




}
