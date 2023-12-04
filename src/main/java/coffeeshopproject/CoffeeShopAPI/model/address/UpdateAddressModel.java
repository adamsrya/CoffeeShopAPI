package coffeeshopproject.CoffeeShopAPI.model.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Component
public class UpdateAddressModel {

    @JsonIgnore
    private Integer id_address;
    @NotEmpty
    private String name;
    @NotEmpty
    private String handphone;
    @NotEmpty
    private String country;
    @NotEmpty
    private String province;
    @NotEmpty
    private String city;
    @NotEmpty
    private String streetname;
    @NotEmpty
    private String postalcode;

    @NotEmpty
    private String otherdetails;

    @Enumerated(EnumType.STRING)
    private LabelCategoriAddressModel labelas;
}
