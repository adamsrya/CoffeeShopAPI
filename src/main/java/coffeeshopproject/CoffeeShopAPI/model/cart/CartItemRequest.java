package coffeeshopproject.CoffeeShopAPI.model.cart;

import coffeeshopproject.CoffeeShopAPI.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Component
public class CartItemRequest {
    private Integer id;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private String productid;
}
