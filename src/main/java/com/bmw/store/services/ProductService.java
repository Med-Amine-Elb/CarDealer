package com.bmw.store.services;

import com.bmw.store.Repositories.ProductRepository;
import com.bmw.store.models.ProductDto;
import com.bmw.store.models.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findByFeaturedTrue();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
    }

    public ResponseEntity<Product> createOrUpdateProduct(ProductDto dto, Long id, String imageFileName) {
        Product product;

        if (id != null) {
            // This is an update
            Optional<Product> existingProduct = productRepository.findById(id);
            if (existingProduct.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Get existing product
            product = existingProduct.get();
        } else {
            // This is a new product - create new instance
            product = new Product();
        }

        // Map all fields from DTO to entity
        mapDtoToEntity(dto, product);

        // Set image filename if provided (overriding what might have been in the DTO)
        if (imageFileName != null) {
            product.setImageFileName(imageFileName);
        }

        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    public Product createProduct(ProductDto dto) {
        Product product = new Product();
        mapDtoToEntity(dto, product);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDto dto) {
        Product product = getProductById(id);
        mapDtoToEntity(dto, product);
        return productRepository.save(product);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsByFuelType(Pageable pageable, String fuelType) {
        if (fuelType == null || fuelType.isEmpty()) {
            return productRepository.findAll(pageable);  // If no filter is provided, return all products
        }
        return productRepository.findByFuelTypesCustom(fuelType, pageable);  // Use custom query with fuelType filter
    }


    /*public List<Product> findByFuelTypes(String fuelTypes) {
        return productRepository.findByFuelTypes(fuelTypes);
    }*/

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    private void mapDtoToEntity(ProductDto dto, Product product) {
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getBrand() != null) product.setBrand(dto.getBrand());
        if (dto.getCategory() != null) product.setCategory(dto.getCategory());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getModelYear() != null) product.setModelYear(dto.getModelYear());
        if (dto.getMileage() != null) product.setMileage(dto.getMileage());
        if (dto.getEngine() != null) product.setEngine(dto.getEngine());
        if (dto.getCcBatteryCapacity() != null) product.setCcBatteryCapacity(dto.getCcBatteryCapacity());
        if (dto.getTotalSpeed() != null) product.setTotalSpeed(dto.getTotalSpeed());
        if (dto.getPerformance() != null) product.setPerformance(dto.getPerformance());
        if (dto.getFuelTypes() != null) product.setFuelTypes(dto.getFuelTypes());
        if (dto.getSeats() != null) product.setSeats(dto.getSeats());
        if (dto.getTorque() != null) product.setTorque(dto.getTorque());
        if (dto.getFeatured() != null) product.setFeatured(dto.getFeatured());
        if (dto.getStatus() != null) product.setStatus(dto.getStatus());
        if (dto.getAutomatic() != null) product.setAutomatic(dto.getAutomatic());
        if (dto.getHorsepower() != null) product.setHorsepower(dto.getHorsepower());
        if (dto.getImageFileName() != null) product.setImageFileName(dto.getImageFileName());
    }



}
