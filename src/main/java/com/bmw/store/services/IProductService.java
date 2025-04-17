package com.bmw.store.services;

import com.bmw.store.models.Product;

import java.util.List;

public interface IProductService {

    Product getProductById(Long id);

    List<Product> getFeaturedProducts();

    List<Product> findByFuelTypes(String fuelTypes);

    List<Product> findAll();
}
