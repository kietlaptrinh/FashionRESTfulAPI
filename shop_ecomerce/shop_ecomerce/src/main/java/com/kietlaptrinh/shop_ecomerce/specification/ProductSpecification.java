package com.kietlaptrinh.shop_ecomerce.specification;

import com.kietlaptrinh.shop_ecomerce.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ProductSpecification {

    //Phương thức này tạo tiêu chí truy vấn
    // tìm kiếm tất cả các Product có thuộc tính category.id trùng với categoryId đã truyền vào.
    public static Specification<Product> hasCategoryId(UUID categorId){
        return  (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"),categorId);
    }
//Tạo tiêu chí truy vấn
// tìm kiếm các Product có thuộc tính categoryType.id bằng với typeId
    public static Specification<Product> hasCategoryTypeId(UUID typeId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("categoryType").get("id"),typeId);
    }
}
