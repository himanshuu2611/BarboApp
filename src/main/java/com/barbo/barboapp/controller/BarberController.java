package com.barbo.barboapp.controller;

import com.barbo.barboapp.entity.Barber;
import com.barbo.barboapp.service.BarberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
public class BarberController {

    private final BarberService barberService;

    // POST /api/barbers
    @PostMapping("register")
    public ResponseEntity<Barber> createBarber(@Valid @RequestBody Barber barber) {
        Barber created = barberService.createBarber(barber);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/barbers
    @GetMapping
    public ResponseEntity<List<Barber>> getAllBarbers() {
        return ResponseEntity.ok(barberService.getAllBarbers());
    }

    // GET /api/barbers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Barber> getBarberById(@PathVariable String id) {
        return ResponseEntity.ok(barberService.getBarberById(id));
    }

    // GET /api/barbers/available
    @GetMapping("/available")
    public ResponseEntity<List<Barber>> getAvailableBarbers() {
        return ResponseEntity.ok(barberService.getAvailableBarbers());
    }

    // GET /api/barbers/location/{location}
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Barber>> getBarbersByLocation(@PathVariable String location) {
        return ResponseEntity.ok(barberService.getBarbersByLocation(location));
    }

    // PUT /api/barbers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Barber> updateBarber(@PathVariable String id,
                                               @Valid @RequestBody Barber barber) {
        return ResponseEntity.ok(barberService.updateBarber(id, barber));
    }

    // PATCH /api/barbers/{id}/toggle-availability
    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<Barber> toggleAvailability(@PathVariable String id) {
        return ResponseEntity.ok(barberService.toggleAvailability(id));
    }

    // DELETE /api/barbers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBarber(@PathVariable String id) {
        barberService.deleteBarber(id);
        return ResponseEntity.ok("Barber deleted successfully");
    }
}