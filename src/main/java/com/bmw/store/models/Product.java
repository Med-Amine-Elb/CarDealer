package com.bmw.store.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private String category;
    private Double price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_file_name")
    private String imageFileName;

    @Column(name = "model_year")
    private Integer modelYear; // Use Integer (wrapper)

    @Column(name = "horsepower")
    private Integer horsepower;

    @Column(name = "mileage")
    private Integer mileage;

    @Column(name = "automatic")
    private Boolean automatic; // Use Boolean (wrapper)

    @Column(name = "engines") // Keep the case consistent with the CSV
    private String engine;

    @Column(name = "cc_battery_capacity")
    private Integer ccBatteryCapacity; // Use camelCase

    @Column(name = "total_speed")
    private Integer totalSpeed; // Use camelCase

    @Column(name = "performance")
    private String performance;

    @Column(name = "fuel_types") // Keep the case consistent with the CSV
    private String  fuelTypes;  // Use camelCase

    @Column(name = "seats")
    private Integer seats;

    @Column(name = "torque")
    private Integer torque;

    @Column(name = "featured")
    private Boolean featured = true; // Use Boolean (wrapper)

    @Column(name = "status", nullable = false)
    private boolean status = true; // Primitive is okay here, has a default

}