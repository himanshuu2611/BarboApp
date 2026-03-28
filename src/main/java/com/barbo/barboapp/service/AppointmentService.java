package com.barbo.barboapp.service;

import com.barbo.barboapp.entity.Appointment;
import com.barbo.barboapp.repository.AppointmentRepository;
import com.barbo.barboapp.repository.BarberRepository;
import com.barbo.barboapp.repository.SalonServiceRepository;
import com.barbo.barboapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final BarberRepository barberRepository;
    private final SalonServiceRepository salonServiceRepository;

    // Book a new appointment
    public Appointment bookAppointment(Appointment appointment) {

        // validate user exists
        userRepository.findById(appointment.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + appointment.getUserId()));

        // validate barber exists
        barberRepository.findById(appointment.getBarberId())
                .orElseThrow(() -> new RuntimeException("Barber not found with id: " + appointment.getBarberId()));

        // validate service exists
        salonServiceRepository.findById(appointment.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + appointment.getServiceId()));

        // check barber is available
        barberRepository.findById(appointment.getBarberId())
                .filter(b -> b.isAvailable())
                .orElseThrow(() -> new RuntimeException("Barber is not available"));

        // check time slot is not already booked
        List<Appointment> existingAppointments = appointmentRepository
                .findByBarberIdAndAppointmentDate(appointment.getBarberId(), appointment.getAppointmentDate());

        boolean slotTaken = existingAppointments.stream()
                .anyMatch(a -> a.getTimeSlot().equals(appointment.getTimeSlot())
                        && !a.getStatus().equals("CANCELLED"));

        if (slotTaken) {
            throw new RuntimeException("Time slot already booked: " + appointment.getTimeSlot());
        }

        // set default status
        appointment.setStatus("BOOKED");

        return appointmentRepository.save(appointment);
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get appointment by ID
    public Appointment getAppointmentById(String id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    // Get appointments by user
    public List<Appointment> getAppointmentsByUser(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return appointmentRepository.findByUserId(userId);
    }

    // Get appointments by barber
    public List<Appointment> getAppointmentsByBarber(String barberId) {
        barberRepository.findById(barberId)
                .orElseThrow(() -> new RuntimeException("Barber not found with id: " + barberId));
        return appointmentRepository.findByBarberId(barberId);
    }

    // Cancel appointment
    public Appointment cancelAppointment(String id) {
        Appointment appointment = getAppointmentById(id);
        if (appointment.getStatus().equals("COMPLETED")) {
            throw new RuntimeException("Cannot cancel a completed appointment");
        }
        if (appointment.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Appointment is already cancelled");
        }
        appointment.setStatus("CANCELLED");
        return appointmentRepository.save(appointment);
    }

    // Complete appointment
    public Appointment completeAppointment(String id) {
        Appointment appointment = getAppointmentById(id);
        if (appointment.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Cannot complete a cancelled appointment");
        }
        if (appointment.getStatus().equals("COMPLETED")) {
            throw new RuntimeException("Appointment is already completed");
        }
        appointment.setStatus("COMPLETED");
        return appointmentRepository.save(appointment);
    }

    // Delete appointment
    public void deleteAppointment(String id) {
        getAppointmentById(id); // check if exists
        appointmentRepository.deleteById(id);
    }
}