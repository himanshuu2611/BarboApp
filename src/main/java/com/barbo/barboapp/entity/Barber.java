package com.barbo.barboapp.entity;


import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection = "barbers")
public class Barber {

    @Id
    private String id;

    @NotBlank(message = "Barber name is required")
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Shop name is required")
    private String shopName;

    @NotBlank(message = "Location is required")
    private String location;

    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 50, message = "Experience seems invalid")
    private int experience; // in years

    @DecimalMin(value = "0.0", message = "Rating cannot be less than 0")
    @DecimalMax(value = "5.0", message = "Rating cannot be more than 5")
    private double rating;

    private List<String> serviceIds; // 🔥 relation with SalonService

    private boolean available; // true = available, false = not available
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "BARBER", message = "Role must be BARBER")
    private String role;
}