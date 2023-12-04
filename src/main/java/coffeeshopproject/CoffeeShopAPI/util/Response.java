package coffeeshopproject.CoffeeShopAPI.util;

import coffeeshopproject.CoffeeShopAPI.model.PagingResponse;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Response <T>{

    private String message;
    private T data;
    private PagingResponse paging;



}


