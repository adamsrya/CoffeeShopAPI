package coffeeshopproject.CoffeeShopAPI.model.address;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class CreateAddressModel {
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
    @NotNull
    private LabelCategoriAddressModel labelas;
}
