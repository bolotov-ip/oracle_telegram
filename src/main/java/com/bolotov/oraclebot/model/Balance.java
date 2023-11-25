package com.bolotov.oraclebot.model;

import com.bolotov.oraclebot.service.UserService;
import jakarta.persistence.*;

@Entity(name = "balance")
public class Balance {

    @Id
    @Column(name = "chat_id")
    private Long id;

    private double amount = 0;

    private String currency;

    @OneToOne
    @MapsId
    @JoinColumn(name = "chat_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
