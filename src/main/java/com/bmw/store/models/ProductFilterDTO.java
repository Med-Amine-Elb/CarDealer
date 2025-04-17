package com.bmw.store.models;

import lombok.*;

import java.util.List;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterDTO {
    private String name;
    private String brand;
    private String category;

    private Integer minPrice;
    private Integer maxPrice;

    private Integer minYear;
    private Integer maxYear;

    private Boolean automatic;

    private String fuelTypes;

    private Integer minSeats;

    private Integer minHorsepower;
    private Integer maxHorsepower;

    private Integer minMileage;
    private Integer maxMileage;


    private String engine;
    private String ccBatteryCapacity;
    private String performance;

    private String totalSpeed;
    private Boolean featured;

    public ProductFilterDTO(String name, String brand, String category,
                            Integer minPrice, Integer maxPrice,
                            Integer minYear, Integer maxYear,
                            Integer minSeats) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minYear = minYear;
        this.maxYear = maxYear;
        this.minSeats = minSeats;
    }
}
