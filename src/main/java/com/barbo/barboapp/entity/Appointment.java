package com.barbo.barboapp.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection = "appointments")
public class Appointment {

    @Id
    private String id;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Barber ID is required")
    private String barberId;

    @NotBlank(message = "SalonService ID is required")
    private String serviceId;

    @NotBlank(message = "Appointment date is required")
    private String appointmentDate; // format: YYYY-MM-DD

    @NotBlank(message = "Time slot is required")
    private String timeSlot; // e.g., "10:00 AM - 10:30 AM"

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "BOOKED|COMPLETED|CANCELLED",
            message = "Status must be BOOKED, COMPLETED or CANCELLED")
    private String status;
}