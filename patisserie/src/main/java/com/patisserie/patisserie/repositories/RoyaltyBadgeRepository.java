package com.patisserie.patisserie.repositories;

import com.patisserie.patisserie.entities.RoyaltyBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoyaltyBadgeRepository extends JpaRepository<RoyaltyBadge,Long> {
    boolean existsById(Long id);
}
