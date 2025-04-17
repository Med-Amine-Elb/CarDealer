package com.bmw.store.services;

import com.bmw.store.models.Product;
import com.bmw.store.models.ProductFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductQueryService {
    Page<Product> getFilteredProducts(ProductFilterDTO filterDTO, Pageable pageable);

    List<Product> findByFuelTypes(String fuelTypes);

    List<Product> findAll();
}