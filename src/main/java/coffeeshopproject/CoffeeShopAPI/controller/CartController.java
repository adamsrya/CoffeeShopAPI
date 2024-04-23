package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.Cart;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartItemRequest;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartResponse;
import coffeeshopproject.CoffeeShopAPI.repository.ProductRepository;
import coffeeshopproject.CoffeeShopAPI.services.CartService;
import coffeeshopproject.CoffeeShopAPI.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    CartService cartService;
    @Autowired
    ProductRepository productRepository;
    @PostMapping(
            path = "/api/cart/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> addcarts(@RequestBody CartItemRequest request, @AuthenticationPrincipal User user) {
        cartService.addtocart(request, user.getUsername());
        return Response.<String>builder()
                .message("Success create carts")
                .data("Ok")
                .build();
    }

    @GetMapping(
            path = "/api/cart",
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public Response<CartResponse> getcart(@AuthenticationPrincipal User user) {
        CartResponse cartResponse = cartService.cartitems(user.getUsername());
        return Response.<CartResponse>builder()
                .message("Success Get Data")
                .data(cartResponse)
                .build();

    }

    //Delete Product in Carts
  /*  @DeleteMapping(
            path = "/api/cart/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> deletecartitem(@PathVariable String productId,User user) {
        cartService.deletecartitem(productId,user);
        return Response.<String>builder()
                .message("Success delete Data")
                .data("OK")
                .build();
    }*/

    @PatchMapping(
            path = "/api/cart/{productid}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<CartResponse> updateProduct(@RequestBody CartItemRequest request,
                                                @PathVariable String productid ,@AuthenticationPrincipal User user) {
        request.setProductid(productid);
        CartResponse cartResponse = cartService.updatecarts(request, user.getUsername());
        return Response.<CartResponse>builder()
                .message("Success Update Data")
                .data(cartResponse)
                .build();

    }

      @DeleteMapping(
            path = "/api/cart",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> deletecartitem(@AuthenticationPrincipal User user) {
        cartService.deleteall(user.getUsername());
        return Response.<String>builder()
                .message("Success delete Data")
                .data("OK")
                .build();
    }


}
