package coffeeshopproject.CoffeeShopAPI.model.cart;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Component
public class CartResponse {

    private List<CartItem> cartItems;

    private Long amount;
}
