package coffeeshopproject.CoffeeShopAPI.model.product;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductModel {

    private String name;

    private String description;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
