package com.bolotov.oraclebot.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Set;

@Entity(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    private Timestamp createDate;

    @ElementCollection
    @CollectionTable(name="media_list")
    private Set<String> mediaList;

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
