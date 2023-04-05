package com.patisserie.patisserie.repositories;

import com.patisserie.patisserie.entities.OrderMenu;
import com.patisserie.patisserie.entities.keys.OrderMenuKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, OrderMenuKey> {
}
