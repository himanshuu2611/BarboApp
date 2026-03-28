package com.barbo.barboapp.service;

import com.barbo.barboapp.entity.SalonService;
import com.barbo.barboapp.repository.SalonServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceService {

    private final SalonServiceRepository salonServiceRepository;

    // Create a new service
    public SalonService createService(SalonService salonService) {
        return salonServiceRepository.save(salonService);
    }

    // Get all services
    public List<SalonService> getAllServices() {
        return salonServiceRepository.findAll();
    }

    // Get service by ID
    public SalonService getServiceById(String id) {
        return salonServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

    // Update service
    public SalonService updateService(String id, SalonService updatedService) {
        SalonService existing = getServiceById(id);
        existing.setName(updatedService.getName());
        existing.setPrice(updatedService.getPrice());
        existing.setDuration(updatedService.getDuration());
        existing.setDescription(updatedService.getDescription());
        return salonServiceRepository.save(existing);
    }

    // Delete service
    public void deleteService(String id) {
        getServiceById(id); // check if exists
        salonServiceRepository.deleteById(id);
    }
}