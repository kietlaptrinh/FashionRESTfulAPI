package com.kietlaptrinh.shop_ecomerce.controllers;

import com.kietlaptrinh.shop_ecomerce.dto.ProductDto;
import com.kietlaptrinh.shop_ecomerce.entities.Product;
import com.kietlaptrinh.shop_ecomerce.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    //get all product
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(required = false, name = "categoryId", value = "categoryId") UUID categoryId
            , @RequestParam(required = false,name = "typeId",value = "typeId") UUID typeId
            , @RequestParam(required = false) String slug
            , HttpServletResponse response) {
        List<ProductDto> productList = new ArrayList<>();
        if(StringUtils.isNotBlank(slug)){
            ProductDto productDto = productService.getProductBySlug(slug);
            productList.add(productDto);
        }
        else {
            productList = productService.getAllProducts(categoryId, typeId);
        }
        response.setHeader("Content-Range",String.valueOf(productList.size()));
        return new ResponseEntity<>(productList, HttpStatus.OK);
    };

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id){
        ProductDto productDto = productService.getProductById(id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    //create product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto){
        Product product = productService.addProduct(productDto);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDto productDto,@PathVariable UUID id){
        Product product = productService.updateProduct(productDto,id);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id){
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted){
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.NO_CONTENT);

        }else {
            return new ResponseEntity<>("Product not found",HttpStatus.NOT_FOUND);
        }
    }

}
