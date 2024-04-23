package coffeeshopproject.CoffeeShopAPI.entity;

import coffeeshopproject.CoffeeShopAPI.model.address.LabelCategoriAddressModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "handphone")
    private String handphone;

    @Column(name = "country")
    private String country;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String streetname;

    @Column(name = "postal_code")
    private String postalcode;

    @Column(name = "details")
    private String otherdetails;

    @Column(name = "label_as")
    @Enumerated(EnumType.STRING)
    private LabelCategoriAddressModel labelas;

    @Column(columnDefinition = "timestamp default now()",updatable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date created;

    @Column(columnDefinition = "timestamp default now()")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updated;
    @ManyToOne
    @JoinColumn(name = "email",referencedColumnName = "email")
    private User user;
}
