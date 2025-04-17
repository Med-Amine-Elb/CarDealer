package com.bmw.store.models;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductDto {
    private Long id;

    @NotEmpty(message = "The name is required")
    private String name;

    @NotEmpty(message = "The brand is required")
    private String brand;

    @NotEmpty(message = "The category is required")
    private String category;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;

    @Size(min = 10, max = 2000, message = "The description should be between 10 and 2000 characters")
    private String description;

    private String imageFileName;
    private Boolean status = true;

    private Integer modelYear;

    private Integer mileage;

    private String engine;

    private Boolean featured;
    private Integer horsepower;
    private Boolean automatic;
    private Integer ccBatteryCapacity;
    private Integer totalSpeed;
    private String performance;
    private String fuelTypes; // Changed to String to match the entity
    private Integer seats;
    private Integer torque;
}

