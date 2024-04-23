package coffeeshopproject.CoffeeShopAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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


    @ManyToOne
    @JoinColumn(name = "email",referencedColumnName = "email")
    private User user;

    private Integer quantity;

}
