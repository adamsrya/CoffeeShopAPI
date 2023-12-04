package coffeeshopproject.CoffeeShopAPI.services;

import coffeeshopproject.CoffeeShopAPI.entity.Cart;
import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartItemRequest;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartResponse;

public interface CartService {

    void addtocart(CartItemRequest request, User user);

    CartResponse cartitems(User user);

    //Delete Product in Carts
    //void deletecartitem(String productid,User user);
    CartResponse updatecarts(CartItemRequest request,User user);

    void deleteall(User user);
}
