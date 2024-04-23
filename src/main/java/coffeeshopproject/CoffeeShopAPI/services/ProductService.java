package coffeeshopproject.CoffeeShopAPI.services;

import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.model.product.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {


    ProductResponse createProduct(CreateProductModel productModel);

    ProductResponse getProduct(String id);

    ProductResponse updateProduct(UpdateProductModel productModel, Product product);
    void deleteProduct(String id);
    Page<ProductResponse> search(Product product, SearchProductModel request);
    Page<ProductResponse> category(Product product, CategoryRequestModel request);
    List<Product>findall();

}
