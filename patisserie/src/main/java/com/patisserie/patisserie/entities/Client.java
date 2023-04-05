package com.patisserie.patisserie.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String telNumber;
    @JsonIgnore
    private String password;

    private String address;
    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;

    @JsonManagedReference
    @OneToOne(mappedBy = "client")
    private RoyaltyBadge badge;

    public Client() {
    }

    public Client(String firstName, String lastName, String telNumber, String password, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telNumber = telNumber;
        this.password = password;
        this.address = address;
    }

    public Client(String firstName, String lastName, String telNumber, String password, String address, City city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telNumber = telNumber;
        this.password = password;
        this.address = address;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public RoyaltyBadge getBadge() {
        return badge;
    }

    public void setBadge(RoyaltyBadge badge) {
        this.badge = badge;
    }
}
