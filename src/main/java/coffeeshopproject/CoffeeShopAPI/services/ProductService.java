package coffeeshopproject.CoffeeShopAPI.services;

import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.model.product.CreateProductModel;
import coffeeshopproject.CoffeeShopAPI.model.product.ProductResponse;
import coffeeshopproject.CoffeeShopAPI.model.product.SearchProductModel;
import coffeeshopproject.CoffeeShopAPI.model.product.UpdateProductModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {


    ProductResponse createProduct(CreateProductModel productModel);

    ProductResponse getProduct(String id);

    ProductResponse updateProduct(UpdateProductModel productModel, Product product);
    void deleteProduct(String id);
    Page<ProductResponse> search(Product product, SearchProductModel request);
   /* List<Product>findall();*/

}
