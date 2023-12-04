package coffeeshopproject.CoffeeShopAPI.model.product;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Component
public class CreateProductModel {


    @NotEmpty
    private String id;
    @NotEmpty
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductCategoryModel category;
    @Min(1L)
    @NotNull
    private Long price;
    @NotEmpty
    private String description;

    @NotEmpty
    private String extras;
    @NotNull
    @Min(0)
    private Integer quantity;




}
