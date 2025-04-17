package com.bmw.store.controllers;

import com.bmw.store.models.ProductDto;
import com.bmw.store.models.ProductFilterDTO;
import com.bmw.store.models.Product;
import com.bmw.store.Repositories.ProductRepository;
import com.bmw.store.services.ProductService;
import com.bmw.store.services.IProductQueryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000"})
public class klhController {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final IProductQueryService productQueryService;

    private static final String IMAGE_DIR = "public/image/";

    public klhController(ProductRepository productRepository, ProductService productService, IProductQueryService productQueryService) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.productQueryService = productQueryService;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable); // Pagination handled here
    }

    // ✅ 1. Get all products (with pagination and sorting)
    @GetMapping("/all")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortOrder
    ) {
        Sort sort = sortOrder.equalsIgnoreCase("DESC") ?
                Sort.by(Sort.Direction.DESC, sortBy) :
                Sort.by(Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> products = productService.getAllProducts(pageable);

        return ResponseEntity.ok(products);
    }

    // Get all products (with no pagination and no sorting)
    @GetMapping("/all-no-pagination")
    public ResponseEntity<List<Product>> getAllProductsWithoutPagination() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }


    // ✅ 2. Get filtered products (with filters, pagination, and sorting)


    // ✅ 3. Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 4. Create new product with multipart/form-data (image upload)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProductWithImage(
            @RequestPart("dto") ProductDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            String imageFileName = null;

            if (imageFile != null && !imageFile.isEmpty()) {
                imageFileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path imagePath = Paths.get(IMAGE_DIR);
                if (!Files.exists(imagePath)) Files.createDirectories(imagePath);
                Files.copy(imageFile.getInputStream(), imagePath.resolve(imageFileName), StandardCopyOption.REPLACE_EXISTING);
            }

            return productService.createOrUpdateProduct(dto, null, imageFileName);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // New endpoint for JSON requests (for testing without image)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProductJson(@RequestBody ProductDto dto) {
        try {
            // Just pass null for imageFileName since no image is uploaded
            return productService.createOrUpdateProduct(dto, null, null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ 5. Update product with multipart/form-data (image upload)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> updateProductWithImage(
            @PathVariable Long id,
            @RequestPart("dto") ProductDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        String imageFileName = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(IMAGE_DIR);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imageFileName = fileName;
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return productService.createOrUpdateProduct(dto, id, imageFileName);
    }

    // New endpoint for JSON update requests (for testing without image)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProductJson(
            @PathVariable Long id,
            @RequestBody ProductDto dto
    ) {
        try {
            // Null imageFileName since we're not uploading an image
            return productService.createOrUpdateProduct(dto, id, null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ 6. Soft delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id).map(product -> {
            product.setStatus(false);  // Soft delete
            productRepository.save(product);
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Product with ID " + id + " not found")); // Custom 404 message
    }

    // ✅ 7. Get all brands
    @GetMapping("/brands")
    public ResponseEntity<List<String>> getAllBrands() {
        List<String> brands = productRepository.findAll().stream()
                .map(Product::getBrand)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(brands);
    }

    // ✅ 8. Get all categories
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = productRepository.findAll().stream()
                .map(Product::getCategory)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    // ✅ 9. Get min/max price range
    @GetMapping("/price-range")
    public ResponseEntity<Map<String, Double>> getPriceRange() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) return ResponseEntity.ok(Map.of("min", 0.0, "max", 0.0));

        double min = products.stream().mapToDouble(Product::getPrice).min().orElse(0);
        double max = products.stream().mapToDouble(Product::getPrice).max().orElse(0);
        return ResponseEntity.ok(Map.of("min", min, "max", max));
    }

    // ✅ 10. Get year range
    @GetMapping("/year-range")
    public ResponseEntity<Map<String, Integer>> getYearRange() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return ResponseEntity.ok(Map.of("min", 0, "max", 0));
        }

        int minYear = products.stream()
                .map(Product::getModelYear)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);

        int maxYear = products.stream()
                .map(Product::getModelYear)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        return ResponseEntity.ok(Map.of("min", minYear, "max", maxYear));
    }

    // ✅ 11. Get fuel types
    @GetMapping("/fuel-types")
    public ResponseEntity<List<String>> getAllFuelTypes() {
        List<String> fuelTypes = productRepository.findDistinctFuelTypes();
        return ResponseEntity.ok(fuelTypes);
    }


    /*public ResponseEntity<Page<Product>> getProductsByFuelType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortOrder,
            @RequestParam String filterFuel // Fuel type filter
    ) {
        Pageable pageable = PageRequest.of(page, size,
                sortOrder.equalsIgnoreCase("DESC") ? Sort.by(Sort.Direction.DESC, sortBy) :
                        Sort.by(Sort.Direction.ASC, sortBy));
        Page<Product> products = productService.getProductsByFuelType(pageable, filterFuel);
        return ResponseEntity.ok(products);
    }*/

    // ✅ 12. Get products filtered by fuel type
    @GetMapping("/filterFuel")
    public ResponseEntity<Page<Product>> getProductsByFuelType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortOrder,
            @RequestParam String filterFuel  // This is the filter parameter for fuel type
    ) {
        // Set sorting options based on the parameters
        Sort sort = sortOrder.equalsIgnoreCase("DESC") ?
                Sort.by(Sort.Direction.DESC, sortBy) :
                Sort.by(Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch products filtered by fuel type
        Page<Product> products = productService.getProductsByFuelType(pageable, filterFuel);

        return ResponseEntity.ok(products);
    }


}
