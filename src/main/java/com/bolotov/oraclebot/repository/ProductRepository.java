package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Product;
import com.bolotov.oraclebot.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM product p where p.price = 0 order by p.name")
    public List<Product> getProductByFreeAccess();

    @Query("SELECT p FROM product p where p.category =:category order by p.name")
    public List<Product> getProductByCategory(@Param("category") ProductCategory category);

    default public Product find(Long name) {
        Optional<Product> optionalProduct = findById(name);
        if(optionalProduct.isPresent())
            return optionalProduct.get();
        else
            return null;
    }
}