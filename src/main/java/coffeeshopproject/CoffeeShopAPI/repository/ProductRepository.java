package coffeeshopproject.CoffeeShopAPI.repository;

import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.entity.User;
import coffeeshopproject.CoffeeShopAPI.model.product.ProductCategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> , JpaSpecificationExecutor<Product> {
    Optional<Product> findByid(String id);
    Page<Product> findByNameIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
            (String name, String description, Pageable pageable);
    Page<Product> findByCategory(ProductCategoryModel productCategoryModel,Pageable pageable);
}
