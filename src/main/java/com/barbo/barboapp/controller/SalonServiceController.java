package com.barbo.barboapp.controller;

import com.barbo.barboapp.entity.SalonService;
import com.barbo.barboapp.service.SalonServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class SalonServiceController {

    private final SalonServiceService salonServiceService;

    // POST /api/services
    @PostMapping
    public ResponseEntity<SalonService> createService(@Valid @RequestBody SalonService salonService) {
        SalonService created = salonServiceService.createService(salonService);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/services
    @GetMapping
    public ResponseEntity<List<SalonService>> getAllServices() {
        return ResponseEntity.ok(salonServiceService.getAllServices());
    }

    // GET /api/services/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SalonService> getServiceById(@PathVariable String id) {
        return ResponseEntity.ok(salonServiceService.getServiceById(id));
    }

    // PUT /api/services/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SalonService> updateService(@PathVariable String id,
                                                      @Valid @RequestBody SalonService salonService) {
        return ResponseEntity.ok(salonServiceService.updateService(id, salonService));
    }

    // DELETE /api/services/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable String id) {
        salonServiceService.deleteService(id);
        return ResponseEntity.ok("Service deleted successfully");
    }
}