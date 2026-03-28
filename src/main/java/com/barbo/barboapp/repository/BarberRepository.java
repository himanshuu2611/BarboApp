package com.barbo.barboapp.repository;

import com.barbo.barboapp.entity.Barber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarberRepository extends MongoRepository<Barber, String> {
    List<Barber> findByAvailableTrue();
    List<Barber> findByLocation(String location);
}