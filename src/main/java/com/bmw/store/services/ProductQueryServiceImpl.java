package com.bmw.store.services;

import com.bmw.store.Repositories.ProductRepository;
import com.bmw.store.models.Product;
import com.bmw.store.models.ProductFilterDTO;
import com.bmw.store.models.ProductSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQueryServiceImpl implements IProductQueryService {

    private final ProductRepository productRepository;

    public ProductQueryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> getFilteredProducts(ProductFilterDTO filterDTO, Pageable pageable) {
        Specification<Product> specification = ProductSpecificationBuilder.build(filterDTO);
        return productRepository.findAll(specification, pageable);
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
