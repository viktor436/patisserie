package com.patisserie.patisserie.payload.response;

import java.sql.Timestamp;
import java.util.List;

public class OrderResponse {
    private String telNumber;
    private Timestamp orderDate;
    private Double totalAmount;
    private List<OrderItemResponse> orderItems;

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String teleNumber) {
        this.telNumber = teleNumber;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }
}
