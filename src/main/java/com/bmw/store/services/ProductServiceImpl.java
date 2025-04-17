package com.bmw.store.services;

import com.bmw.store.Repositories.ProductRepository;
import com.bmw.store.models.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    private ProductRepository productRepository;

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getFeaturedProducts() {
        return productRepository.findByFeaturedTrue();
    }

    @Override
    public List<Product> findByFuelTypes(String fuelTypes) {
        return productRepository.findByFuelTypes(fuelTypes);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}