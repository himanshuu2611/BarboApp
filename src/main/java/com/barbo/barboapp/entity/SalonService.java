package com.barbo.barboapp.entity;


import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection = "services")
public class SalonService {

    @Id
    private String id;

    @NotBlank(message = "SalonService name is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private double price;

    @NotNull(message = "Duration is required")
    @Min(value = 5, message = "Minimum duration is 5 minutes")
    @Max(value = 300, message = "Duration too long")
    private int duration; // in minutes

    @Size(max = 200, message = "Description too long")
    private String description;
}