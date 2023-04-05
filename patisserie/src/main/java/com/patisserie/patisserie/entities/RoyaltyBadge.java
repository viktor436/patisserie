package com.patisserie.patisserie.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class RoyaltyBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String badgeName;
    private Integer discountPercentage;

    @JsonBackReference
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Client client;

    public RoyaltyBadge() {
    }

    public RoyaltyBadge(String badgeName, Integer discountPercentage, Client client) {
        this.badgeName = badgeName;
        this.discountPercentage = discountPercentage;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
