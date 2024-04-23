package coffeeshopproject.CoffeeShopAPI.services;

import coffeeshopproject.CoffeeShopAPI.entity.Cart;
import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartItemRequest;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface CartService {

    void addtocart(CartItemRequest request, String userId);

    CartResponse cartitems(String userId);

    //Delete Product in Carts
    //void deletecartitem(String productid,User user);
    CartResponse updatecarts(CartItemRequest request, String userId);

    void deleteall(String userId);
}
