package com.barbo.barboapp.service;

import com.barbo.barboapp.entity.Barber;
import com.barbo.barboapp.repository.BarberRepository;
import com.barbo.barboapp.repository.SalonServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;
    private final SalonServiceRepository salonServiceRepository;

    // Create a new barber
    public Barber createBarber(Barber barber) {
        // validate that all serviceIds exist
        for (String serviceId : barber.getServiceIds()) {
            salonServiceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));
        }
        return barberRepository.save(barber);
    }

    // Get all barbers
    public List<Barber> getAllBarbers() {
        return barberRepository.findAll();
    }

    // Get barber by ID
    public Barber getBarberById(String id) {
        return barberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barber not found with id: " + id));
    }

    // Get available barbers
    public List<Barber> getAvailableBarbers() {
        return barberRepository.findByAvailableTrue();
    }

    // Get barbers by location
    public List<Barber> getBarbersByLocation(String location) {
        return barberRepository.findByLocation(location);
    }

    // Update barber
    public Barber updateBarber(String id, Barber updatedBarber) {
        Barber existing = getBarberById(id);
        existing.setName(updatedBarber.getName());
        existing.setShopName(updatedBarber.getShopName());
        existing.setLocation(updatedBarber.getLocation());
        existing.setExperience(updatedBarber.getExperience());
        existing.setRating(updatedBarber.getRating());
        existing.setServiceIds(updatedBarber.getServiceIds());
        existing.setAvailable(updatedBarber.isAvailable());
        return barberRepository.save(existing);
    }

    // Toggle barber availability
    public Barber toggleAvailability(String id) {
        Barber barber = getBarberById(id);
        barber.setAvailable(!barber.isAvailable());
        return barberRepository.save(barber);
    }

    // Delete barber
    public void deleteBarber(String id) {
        getBarberById(id); // check if exists
        barberRepository.deleteById(id);
    }
}