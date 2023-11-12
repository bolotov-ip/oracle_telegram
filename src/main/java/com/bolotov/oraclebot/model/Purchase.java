package com.bolotov.oraclebot.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity(name = "purchase")
public class Purchase {

    enum STATE {
        PURCHASED, DONE, REJECTED;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "customer_chat_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "oracle_user_chat_id")
    private User oracleUser;

    private Timestamp datePurchase;

    private double price;

    @Enumerated
    private STATE state = STATE.PURCHASED;

    public User getOracleUser() {
        return oracleUser;
    }

    public void setOracleUser(User oracleUser) {
        this.oracleUser = oracleUser;
    }


    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
