package com.barbo.barboapp.repository;

import com.barbo.barboapp.entity.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByUserId(String userId);
    List<Appointment> findByBarberId(String barberId);
    List<Appointment> findByBarberIdAndAppointmentDate(String barberId, String appointmentDate);
}