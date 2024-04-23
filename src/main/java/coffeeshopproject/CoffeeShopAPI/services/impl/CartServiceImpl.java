package coffeeshopproject.CoffeeShopAPI.services.impl;

import coffeeshopproject.CoffeeShopAPI.entity.Cart;
import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartItem;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartItemRequest;
import coffeeshopproject.CoffeeShopAPI.model.cart.CartResponse;
import coffeeshopproject.CoffeeShopAPI.repository.CartRepository;
import coffeeshopproject.CoffeeShopAPI.repository.ProductRepository;
import coffeeshopproject.CoffeeShopAPI.repository.UserRepository;
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
    @Autowired
    UserRepository userRepository;


    @Override
    public void addtocart(CartItemRequest request, String userId) {
        validationUtil.validate(request);
        Product product = productRepository.findById(request.getProductid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
        if (product.getQuantity() < 1) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Product is Sold");
        }
        User userexist = userRepository.findByEmail(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (cartRepository.findFirstByProductAndUser(product, userexist).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Cart cart = new Cart();
        cart.setUser(userexist);
        cart.setProduct(product);
        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);

    }

    @Override
    public CartResponse cartitems(String userId) {
        User user = userRepository.findByEmail(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Cart> carts = cartRepository.findAllByUser(user);
        List<CartItem> cartItems = new ArrayList<>();
        long amount = 0;
        if (!carts.isEmpty()) {
            for (Cart cart : carts) {
                CartItem cartItem = new CartItem(cart);
                cartItems.add(cartItem);

            }
            for (CartItem cartItem : cartItems) {
                amount += (cartItem.getProduct().getPrice() * cartItem.getQuantity());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Carts its empty");
        }

        return CartResponse.builder()
                .cartItems(cartItems)
                .amount(amount)
                .build();
    }

    //Delete Product in Carts
    /*  @Override
    public void deletecartitem(String productid,User userId) {
        Product product = productRepository.findById(productid)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product Not found"));
        Cart cart = cartRepository.findFirstByProductAndUser(product,userId)
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
    public CartResponse updatecarts(CartItemRequest request, String userId) {
        Product product = productRepository.findById(request.getProductid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not found"));
        User userexist = userRepository.findByEmail(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Cart cart = cartRepository.findFirstByProductAndUser(product, userexist)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product in carts not found"));
        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        if (request.getQuantity() < 1 ) {
            cartRepository.delete(cart);
        }
        List<Cart> carts = cartRepository.findAllByUser(userexist);

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
    public void deleteall(String userId) {
        User user = userRepository.findByEmail(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product Not Found"));
        List<Cart> carts = cartRepository.findAllByUser(user);
        cartRepository.deleteAll(carts);
    }


}
