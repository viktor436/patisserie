package com.patisserie.patisserie.repositories;

import com.patisserie.patisserie.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order,Long> {
    public Order findOrderById(Long id);
    public List<Order> findOrdersByClient_TelNumber(String telNumber);


    public Set<Order> findOrdersByFinished(boolean finished);
}
