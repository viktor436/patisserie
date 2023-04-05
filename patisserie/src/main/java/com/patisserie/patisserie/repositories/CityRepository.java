package com.patisserie.patisserie.repositories;

import com.patisserie.patisserie.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City,Long> {
   City findCityByName(String name);
}
