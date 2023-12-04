package coffeeshopproject.CoffeeShopAPI.model.product;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Component
public class ProductResponse {


    private String id;


    private ProductCategoryModel category;


    private String name;

    private Long price;

    private String description;

    private String extras;

    private Integer quantity;

    private Date created;

    private Date updated;


}
