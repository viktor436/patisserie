package com.patisserie.patisserie.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                    property = "id")
@Entity
@Table(name = "orders")   //OrderTable
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column()
    private Boolean finished = false;

    @OneToMany(mappedBy = "order")
    private Set<OrderMenu> orders;

    private Timestamp orderDate;

    //@Transient   //stava nevidimo za bazata, no sega prosto shte si naprawim edin getter i taka nqma da e pole
    // jakson pri serializaciq ideserializaciq se orientira po getters i setters ne e nujno da ima pole.!

    public Order() {
    }

    public Order(Client client, Timestamp orderDate) {
        this.client = client;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<OrderMenu> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderMenu> orders) {
        this.orders = orders;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public Set<OrderMenu> getOrderMenus(){
        return orders;
    }

    public String getTelNumber(){
        return client.getTelNumber();
    }

    public Double getTotalAmount(){
        double totalAmount = 0.0;
        for(OrderMenu orderMenu:orders){
            totalAmount+=(orderMenu.getQuantity()*orderMenu.getItemPrice());
        }
        return totalAmount;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
