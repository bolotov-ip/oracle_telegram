package com.bolotov.oraclebot.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**Категория продукта*/
@Entity(name = "product_category")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ProductCategory parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getParentId() {
        return parentId;
    }

    public void setParentId(ProductCategory parentId) {
        this.parentId = parentId;
    }

    public String getHierarchy() {
        List<String> names = new ArrayList<>();
        ProductCategory productCategory = this;
        while(productCategory!=null) {
            names.add(productCategory.getName());
            productCategory = productCategory.getParentId();
        }
        Collections.reverse(names); /**Корневая категория сначала*/

        return String.join(";", names);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategory that = (ProductCategory) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentId);
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
