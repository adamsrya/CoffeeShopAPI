package coffeeshopproject.CoffeeShopAPI.controller;

import coffeeshopproject.CoffeeShopAPI.entity.Product;
import coffeeshopproject.CoffeeShopAPI.model.PagingResponse;
import coffeeshopproject.CoffeeShopAPI.model.product.CreateProductModel;
import coffeeshopproject.CoffeeShopAPI.model.product.ProductResponse;
import coffeeshopproject.CoffeeShopAPI.model.product.SearchProductModel;
import coffeeshopproject.CoffeeShopAPI.model.product.UpdateProductModel;
import coffeeshopproject.CoffeeShopAPI.services.ProductService;
import coffeeshopproject.CoffeeShopAPI.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unchecked")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(
            path = "/api/products",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<ProductResponse> createProduct(@RequestBody CreateProductModel productModel) {
        ProductResponse product = productService.createProduct(productModel);
       return Response.<ProductResponse>builder()
               .message("Success Create Data")
               .data(product)
               .build();

    }




    @PutMapping(
            path = "/api/products/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<ProductResponse> updateProduct(Product product ,@RequestBody UpdateProductModel
              productModel, @PathVariable String id) {
        productModel.setId(id);
      var products = productService.updateProduct(productModel,product);
        return Response.<ProductResponse>builder()
                .message("Success Update Data")
                .data(products)
                .build();

    }


    @GetMapping(
            path = "/api/products/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public Response<ProductResponse> getProduct( @PathVariable String id) {
        ProductResponse products = productService.getProduct(id);
        return Response.<ProductResponse>builder()
                .message("Success Get Data")
                .data(products)
                .build();

    }




   /* @GetMapping(
            path = "/api/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<List<Product>> findall(){
       var product =  productService.findall();
        return Response.<List<Product>>builder()
                .data(product)
                .build();
    }*/

    @GetMapping(
            path = "/api/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<List<ProductResponse>> search(Product product,
    @RequestParam(value = "name",required = false)String name,
    @RequestParam(value = "description",required = false)String description,
    @RequestParam(value = "page",required = false,defaultValue = "0")Integer page,
    @RequestParam(value = "size",required = false,defaultValue = "10")Integer size){
        SearchProductModel request = SearchProductModel.builder()
                .page(page)
                .size(size)
                .name(name)
                .description(description)
                .build();
        Page<ProductResponse> productResponses =productService.search(product,request);
        return Response.<List<ProductResponse>>builder()
                .data(productResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(productResponses.getNumber())
                        .totalPage(productResponses.getTotalPages())
                        .size(productResponses.getSize())
                        .build())
                .build();
    }

    @DeleteMapping(
            path = "/api/products/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<String> deleteById(@PathVariable String id){
        productService.deleteProduct(id);
        return Response.<String>builder()
                .message("Success Delete Data")
                .data("OK")
                .build();


    }


    }

