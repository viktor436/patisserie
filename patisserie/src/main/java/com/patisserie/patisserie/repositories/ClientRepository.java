package com.patisserie.patisserie.repositories;

import com.patisserie.patisserie.entities.City;
import com.patisserie.patisserie.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ClientRepository extends JpaRepository<Client,Long> {

//    @Query("SELECT c " +
//            "FROM Client c "+
//            "WHERE "+
//            "lower(c.firstName) "+
//            "LIKE :#{#firstName ==null || #firstName.isEmpty()? '%': #firstName+'%'} "+
//            "AND lower(c.lastName) "+
//            "LIKE :#{#lastName ==null||#lasttName.isEmpty()? '%': #lastName+'%'}")
    Optional<Client> findClientByFirstNameAndLastName(String firstName, String lastName);

    @Query("Select c "+
            "FROM Client c "+
            "WHERE "+
            "c.city.name "+
            "LIKE :#{#cityName ==null || #cityName.isEmpty()? '%': #cityName+'%'} ")
    Set<Client> findClientsByCity(String cityName);

    Optional<Client> findClientByTelNumber(String telNumber);
}
