package coffeeshopproject.CoffeeShopAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart")
    private Integer id_cart;

    @ManyToOne
    @JoinColumn(name = "id",referencedColumnName = "id_product")
    private Product product;

    @JsonIgnore
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "email",referencedColumnName = "email")
    private User user;

    private Integer quantity;

}
