package coffeeshopproject.CoffeeShopAPI.entity;


import coffeeshopproject.CoffeeShopAPI.model.product.ProductCategoryModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product  {

    @Id
    @Column( name = "id_product")
    private String id;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ProductCategoryModel category;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private Long price;
    @Column(name = "description")
    private String description;
    @Column(name = "extras")
    private String extras;
    @Column(name = "quantity")
    private Integer quantity;

    @Column(columnDefinition = "timestamp default now()",updatable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date created;

    @Column(columnDefinition = "timestamp default now()")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updated;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product",cascade = CascadeType.ALL)
    private List<Cart> carts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
