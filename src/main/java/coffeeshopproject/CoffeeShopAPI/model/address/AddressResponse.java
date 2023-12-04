package coffeeshopproject.CoffeeShopAPI.model.address;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Component
public class AddressResponse {
    private String name;
    private String handphone;
    private String country;
    private String province;
    private String city;
    private String streetname;
    private String postalcode;

    private String otherdetails;

    private LabelCategoriAddressModel labelas;

    private Date created;

    private Date updated;

}
