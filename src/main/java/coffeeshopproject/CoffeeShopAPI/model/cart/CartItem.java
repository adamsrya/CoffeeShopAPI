package coffeeshopproject.CoffeeShopAPI.model.cart;

import coffeeshopproject.CoffeeShopAPI.entity.Cart;
import coffeeshopproject.CoffeeShopAPI.entity.Product;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Component
public class CartItem {

    @NotNull
    private Integer quantity;

    @NotNull
    private Product product;

    public CartItem(Cart cart) {
        this.setQuantity(cart.getQuantity());
        this.setProduct(cart.getProduct());
    }
}
