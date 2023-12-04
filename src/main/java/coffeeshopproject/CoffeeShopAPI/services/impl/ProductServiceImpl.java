package coffeeshopproject.CoffeeShopAPI.services.impl;

import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.model.product.CreateProductModel;
import coffeeshopproject.CoffeeShopAPI.model.product.ProductResponse;
import coffeeshopproject.CoffeeShopAPI.model.product.SearchProductModel;
import coffeeshopproject.CoffeeShopAPI.model.product.UpdateProductModel;
import coffeeshopproject.CoffeeShopAPI.repository.ProductRepository;
import coffeeshopproject.CoffeeShopAPI.services.ProductService;
import coffeeshopproject.CoffeeShopAPI.util.ValidationUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ValidationUtil validationUtil;

    @Override
    public ProductResponse createProduct(CreateProductModel product) {
        validationUtil.validate(product);
        if (productRepository.existsById(product.getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Id already exist");
        }
        var productsave = Product.builder()
                .id(product.getId())
                .category(product.getCategory())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .extras(product.getExtras())
                .quantity(product.getQuantity())
                .created(new Date())
                .updated(null)
                .build();
        productRepository.save(productsave);
        return convertproducttoresponse(productsave);
    }

    @Override
    public ProductResponse getProduct(String id) {
        Product products = productRepository.findByid(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product Not Found"));
        return convertproducttoresponse(products);
    }

    @Override
    public ProductResponse updateProduct(UpdateProductModel productmdl,Product product ) {
        validationUtil.validate(productmdl);
        Product producttosave = productRepository.findByid(productmdl.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product Not Found"));
        producttosave.setCategory(productmdl.getCategory());
        producttosave.setName(productmdl.getName());
        producttosave.setPrice(productmdl.getPrice());
        producttosave.setDescription(productmdl.getDescription());
        producttosave.setExtras(productmdl.getExtras());
        producttosave.setQuantity(productmdl.getQuantity());
        producttosave.setUpdated(new Date());
        productRepository.save(producttosave);
        return convertproducttoresponse(producttosave);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = productRepository.findByid(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product Not Found"));
        productRepository.delete(product);
    }


    @Override
    public Page<ProductResponse> search(Product product, SearchProductModel request) {

        Specification<Product> specification = (root, query, builder) -> {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(request.getName())){
            predicates.add(builder.like(root.get("name"),"%"+request.getName()+"%"));
        }
        if (Objects.nonNull(request.getDescription())){
                predicates.add(builder.like(root.get("description"),"%"+request.getDescription()+"%"));
        }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(),request.getSize());
        Page<Product> products = productRepository.findAll(specification,pageable);
        List<ProductResponse> productResponses = products.getContent().stream()
                .map(this::convertproducttoresponse)
                .toList();
        return new PageImpl<>(productResponses,pageable,products.getTotalElements());
    }

   /* @Override
    public List<Product> findall() {
        return productRepository.findAll();
    }*/


    private ProductResponse convertproducttoresponse(Product product){
        return ProductResponse.builder()
                .id(product.getId().toUpperCase())
                .category(product.getCategory())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .extras(product.getExtras())
                .quantity(product.getQuantity())
                .created(product.getCreated())
                .updated(product.getUpdated())
                .build();
    }

}
