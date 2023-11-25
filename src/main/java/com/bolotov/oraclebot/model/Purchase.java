package com.bolotov.oraclebot.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity(name = "purchase")
public class Purchase {

    public enum STATE {
        WAIT_ANSWER, SELECTED, DONE, REJECTED;
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

    private Long oracleId;

    @Enumerated
    private STATE state = STATE.WAIT_ANSWER;

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

    public Timestamp getDatePurchase() {
        return datePurchase;
    }

    public void setDatePurchase(Timestamp datePurchase) {
        this.datePurchase = datePurchase;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getOracleId() {
        return oracleId;
    }

    public void setOracleId(Long oracleId) {
        this.oracleId = oracleId;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }
}
