package coffeeshopproject.CoffeeShopAPI.services.impl;

import coffeeshopproject.CoffeeShopAPI.entity.Cart;
import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartItem;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartItemRequest;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartResponse;
import coffeeshopproject.CoffeeShopAPI.repository.CartRepository;
import coffeeshopproject.CoffeeShopAPI.repository.ProductRepository;
import coffeeshopproject.CoffeeShopAPI.services.CartService;
import coffeeshopproject.CoffeeShopAPI.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ValidationUtil validationUtil;
    @Autowired
    CartRepository cartRepository;


    @Override
    public void addtocart(CartItemRequest request, User user) {
        validationUtil.validate(request);
        Product product = productRepository.findById(request.getProductid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
        if (product.getQuantity() < 1) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Product is Sold");
        }
        if (cartRepository.findFirstByProductAndUser(product, user).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);

    }

    @Override
    public CartResponse cartitems(User user) {
        List<Cart> carts = cartRepository.findAllByUser(user);
        List<CartItem> cartItems = new ArrayList<>();
        long amount = 0;
        if (!carts.isEmpty()){
            for (Cart cart : carts) {
                CartItem cartItem = new CartItem(cart);
                cartItems.add(cartItem);

            }
            for (CartItem cartItem : cartItems) {
                amount += (cartItem.getProduct().getPrice() * cartItem.getQuantity());
            }
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Carts its empty");
        }

        return CartResponse.builder()
                .cartItems(cartItems)
                .amount(amount)
                .build();
    }

    //Delete Product in Carts
    /*  @Override
    public void deletecartitem(String productid,User user) {
        Product product = productRepository.findById(productid)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product Not found"));
        Cart cart = cartRepository.findFirstByProductAndUser(product,user)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product in carts not found"));
        cartRepository.delete(cart);
    }*/
    //maybe for checkout service
    /*   cartItems.stream().peek((item ->{
                    if (item.getProduct().getId() == item.getProduct().getId()){
                        item.setQuantity(item.getQuantity());
                        if (item.getProduct().getId() == product.getId()){
                            product.setQuantity(product.getQuantity() - item.getQuantity());
                        }
                    }
                })).collect(Collectors.toList());*/
    @Override
    public CartResponse updatecarts(CartItemRequest request, User user) {
        Product product = productRepository.findById(request.getProductid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not found"));
        Cart cart = cartRepository.findFirstByProductAndUser(product, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product in carts not found"));
        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        if (request.getQuantity() < 1) {
            cartRepository.delete(cart);
        }
        List<Cart> carts = cartRepository.findAllByUser(user);

        List<CartItem> cartItems = new ArrayList<>();
        for (Cart cartup : carts) {
            CartItem cartItem = new CartItem(cartup);
            cartItems.add(cartItem);
        }
        long amount = 0;
        for (CartItem cartItem : cartItems) {
            amount += (cartItem.getProduct().getPrice() * cartItem.getQuantity());
        }
        return CartResponse
                .builder()
                .cartItems(cartItems)
                .amount(amount)
                .build();
    }

    @Override
    public void deleteall(User user) {
        List<Cart> carts = cartRepository.findAllByUser(user);
        cartRepository.deleteAll(carts);
    }


}
