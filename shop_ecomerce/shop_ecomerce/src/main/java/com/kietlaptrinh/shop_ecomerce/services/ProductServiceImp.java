package com.kietlaptrinh.shop_ecomerce.services;

import com.kietlaptrinh.shop_ecomerce.dto.ProductDto;
import com.kietlaptrinh.shop_ecomerce.entities.*;
import com.kietlaptrinh.shop_ecomerce.exceptions.ResourceNotFoundEx;
import com.kietlaptrinh.shop_ecomerce.mapper.ProductMapper;
import com.kietlaptrinh.shop_ecomerce.repositories.ProductRepository;
import com.kietlaptrinh.shop_ecomerce.specification.ProductSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public Product addProduct(ProductDto productDto){

        Product product = productMapper.mapToProductEntity(productDto);
        return productRepository.save(product);
    }

    @Override
    public List<ProductDto> getAllProducts(UUID categoryId, UUID typeId) {
        Specification<Product> productSpecification = Specification.where(null);

        if(null != categoryId){
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryId(categoryId));
        }
        if(null != typeId){
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryTypeId(typeId));
        }
        List<Product> products = productRepository.findAll(productSpecification);

        return productMapper.getProductDtos(products);
    }

    @Override
    public ProductDto getProductBySlug(String slug) {
        Product product= productRepository.findBySlug(slug);
        if(null == product){
            throw new ResourceNotFoundEx("Product Not Found!");
        }
        ProductDto productDto = productMapper.mapProductToDto(product);
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryTypeId(product.getCategoryType().getId());
        productDto.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        productDto.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return productDto;
    }

    @Override
    public ProductDto getProductById(UUID id) {
        Product product= productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundEx("Product Not Found!"));
        ProductDto productDto = productMapper.mapProductToDto(product);
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryTypeId(product.getCategoryType().getId());
        productDto.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        productDto.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return productDto;
    }

    @Override
    public Product updateProduct(ProductDto productDto, UUID id) {
        Product product= productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundEx("Product Not Found!"));
        productDto.setId(product.getId());
        return productRepository.save(productMapper.mapToProductEntity(productDto));
    }
    @Override
    public Product fetchProductById(UUID id) throws Exception{
        return productRepository.findById(id).orElseThrow(BadRequestException::new);
    }

    @Override
    public boolean deleteProduct(UUID id) {
        if (productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
