package coffeeshopproject.CoffeeShopAPI.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class UpdateProductModel {

    @JsonIgnore
    @NotBlank
    private String id;
    @NotEmpty
    private String name;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductCategoryModel category;
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
