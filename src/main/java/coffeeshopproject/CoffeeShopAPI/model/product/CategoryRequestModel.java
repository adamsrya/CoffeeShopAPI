package coffeeshopproject.CoffeeShopAPI.model.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequestModel {
    private ProductCategoryModel productCategoryModel;
    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
