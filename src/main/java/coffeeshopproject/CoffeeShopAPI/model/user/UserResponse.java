package coffeeshopproject.CoffeeShopAPI.model.user;

import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Component
public class UserResponse {

    private String firstname;


    private String lasttname;


    private String email;

    private String name;






}
