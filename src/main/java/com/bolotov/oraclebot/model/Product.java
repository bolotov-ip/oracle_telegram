package com.bolotov.oraclebot.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    /**Количество использований за промежуток времени countDay*/
    @Column(name = "count_use")
    private int countUse;

    /**Промежуток времени в который нельзя превысить количество использований count_use*/
    @Column(name = "count_day")
    private int countDay;

    /**Стоимость услуги*/
    @Column(name = "price")
    private double price;

    /**Метка указывает есть ли автоматическое решение*/
    @Column(name = "is_automatic")
    private boolean isAutomatic = false;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCountUse() {
        return countUse;
    }

    public void setCountUse(int countUse) {
        this.countUse = countUse;
    }

    public int getCountDay() {
        return countDay;
    }

    public void setCountDay(int countDay) {
        this.countDay = countDay;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAutomatic() {
        return isAutomatic;
    }

    public void setAutomatic(boolean automatic) {
        isAutomatic = automatic;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
