package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Query("SELECT pc FROM product_category pc where pc.parentId is null order by pc.name")
    public List<ProductCategory> getRootCategories();

    @Query("SELECT pc FROM product_category pc where pc.parentId =:category order by pc.name")
    public List<ProductCategory> getChildCategories(@Param("category") ProductCategory category);

    default public ProductCategory find(Long id) {
        Optional<ProductCategory> productType = findById(id);
        if(productType.isPresent())
            return productType.get();
        else
            return null;
    }
}
