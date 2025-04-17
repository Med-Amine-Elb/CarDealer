
package com.bmw.store.controllers;

    import com.bmw.store.Repositories.UserRepository;
    import com.bmw.store.models.ProductDto;
    import com.bmw.store.models.ProductFilterDTO;
    import com.bmw.store.models.User;
    import com.bmw.store.services.IProductQueryService;
    import jakarta.persistence.EntityNotFoundException;
    import jakarta.validation.Valid;
    import org.springframework.data.domain.Sort;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.ui.Model;
    import com.bmw.store.Repositories.ProductRepository;
    import com.bmw.store.models.Product;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.validation.BindingResult;
    import org.springframework.validation.FieldError;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    import com.bmw.store.services.ProductService;
    import java.nio.file.Files;
    import java.nio.file.*;

    import java.nio.file.StandardCopyOption;
    import java.security.Principal;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
/*
    @Controller
    @ComponentScan("com.bmw.store")

    public class ProductController {

        private final IProductQueryService productQueryService;
        String uploadDir = "src/public/image/";

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private ProductService productService; // Ensure this is an instance, not static

        @Autowired
        private UserRepository userRepository; // This is crucial


        @Autowired
        public ProductController(IProductQueryService productQueryService) {
            this.productQueryService = productQueryService;
        }


        @GetMapping({"","/products"})
        public String listProducts(Model model) {
            List<Product> products = productRepository.findByStatusTrue(Sort.by(Sort.Direction.DESC, "id"));
            model.addAttribute("listProducts", products);
            return "products/list"; // Assurez-vous que le nom de la vue correspond au fichier HTML
        }

        @GetMapping("index")
        public String index(Model model, Principal principal) {
            List<Product> featuredProducts = productRepository.findFeaturedProductsRandomly();

            if (featuredProducts.isEmpty()) {
                featuredProducts = productRepository.findByStatusTrue(Sort.by(Sort.Direction.DESC, "id"));
            }

            List<Product> limitedProducts = featuredProducts.subList(0, Math.min(8, featuredProducts.size()));

            System.out.println("Number of featured products: " + featuredProducts.size());
            System.out.println("Number of limited products: " + limitedProducts.size());

            if (limitedProducts.isEmpty()) {
                System.out.println("listProducts is EMPTY!");
            }

            model.addAttribute("listProducts", limitedProducts); // This is the crucial line!

            if (principal != null) {
                String email = principal.getName();
                User user = userRepository.findByemail(email).orElse(null);
                if (user != null) {
                    model.addAttribute("firstName", user.getFirstName());
                    model.addAttribute("lastName", user.getLastName());
                }
            }
            return "index";

        }

        /*@GetMapping("")
       /* public String showCreatePage(Model model) {
            ProductDto productDto = new ProductDto();
            model.addAttribute("productDto", productDto);
            return "products/create-product"; // Change to match your template name exactly
        }

        // Add POST mapping for form submission
        @PostMapping("/products/save")
        public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result, Model model) {
            // Validation for image file (only required for new products)
            if (productDto.getId() == null && productDto.getImageFile().isEmpty()) {
                result.addError(new FieldError("productDto", "imageFile", "Image file is required"));
            }

            // If there are validation errors, return to the appropriate form
            if (result.hasErrors()) {
                if (productDto.getId() != null) { // Editing an existing product
                    Product product = productRepository.findById(productDto.getId()).orElseThrow();
                    model.addAttribute("product", product); // Add product to model for the edit page
                    return "products/edit-product"; // Return to the edit page with errors
                } else { // Creating a new product
                    return "products/create-product"; // Return to the create page with errors
                }
            }

            // Save or update the product
            Product product;
            Date createdAt = new Date();

            if (productDto.getId() != null) { // Update existing product
                product = productRepository.findById(productDto.getId()).orElseThrow();
                product.setName(productDto.getName());
                product.setBrand(productDto.getBrand());
                product.setCategory(productDto.getCategory());
                product.setPrice(productDto.getPrice());
                product.setDescription(productDto.getDescription());
            } else { // Create new product
                product = new Product();
                product.setCreateDate(createdAt);
                product.setStatus(true);
                product.setName(productDto.getName());
                product.setBrand(productDto.getBrand());
                product.setCategory(productDto.getCategory());
                product.setPrice(productDto.getPrice());
                product.setDescription(productDto.getDescription());
            }

            // Handle image upload (only if a new file is provided)
            if (!productDto.getImageFile().isEmpty()) {
                MultipartFile image = productDto.getImageFile();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try {
                    String uploadDir = "public/image/";
                    Path uploadPath = Paths.get(uploadDir);

                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Files.copy(image.getInputStream(), Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                    product.setImageFileName(storageFileName);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }
            }

            productRepository.save(product);
            return "redirect:/products";
        }

        @GetMapping("/products/edit-product")
        public String showEditPage(Model model,@RequestParam Long id) {

            try {

                Product product = productRepository.findById(id).get();
                model.addAttribute("product", product);

                ProductDto productDto = new ProductDto();
                productDto.setId(product.getId());
                productDto.setName (product.getName() );
                productDto.setBrand (product.getBrand () );
                productDto.setCategory (product.getCategory());
                productDto.setPrice (product.getPrice());
                productDto.setDescription (product.getDescription());
                model.addAttribute("productDto", productDto) ;


                return "products/edit-product"; // Changed from EditProduct to edit
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                return "redirect:/products";
            }
        }

        @GetMapping("/products/delete")
        public String deleteProduct(@RequestParam Long id) {
            Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            product.setStatus(false);
            productRepository.save(product);
            return "redirect:/products";
        }
        @GetMapping("/products/details/{name}")
        public String showProductDetails(@PathVariable String name, Model model) {
            Product product = productRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Product not found"));
            model.addAttribute("product", product);
            return "products/details_product";
        }

        @GetMapping("/car/{id}")
        public String getCarDetails(@PathVariable Long id, Model model) {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);

            List<Product> featuredProducts = productService.getFeaturedProducts();
            if (featuredProducts == null) {
                featuredProducts = new ArrayList<>();
            }
            System.out.println("Featured Products size: " + featuredProducts.size());
            model.addAttribute("featuredProducts", featuredProducts);



            return "products/details_product";
        }



        @GetMapping("/shop")
        public String showshop(
                @RequestParam(required = false) String name,
                @RequestParam(required = false) String brand,
                @RequestParam(required = false) String category,
                @RequestParam(required = false) Integer minPrice,
                @RequestParam(required = false) Integer maxPrice,
                @RequestParam(required = false) Integer minYear,
                @RequestParam(required = false) Integer maxYear,
                @RequestParam(required = false) Integer minSeats,
                @RequestParam(required = false) Boolean automatic,
                @RequestParam(required = false) List<String> fuelTypes,
                @RequestParam(required = false) String engine,
                @RequestParam(required = false) String ccBatteryCapacity,
                @RequestParam(required = false) String performance,
                @RequestParam(required = false) String totalSpeed,
                @RequestParam(required = false) Boolean featured,

                // Pagination and sorting
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(defaultValue = "ASC") String sortOrder,

                Model model
        ) {
            // Create filter DTO with all parameters
            ProductFilterDTO filterDTO = new ProductFilterDTO();
            filterDTO.setName(name);
            filterDTO.setBrand(brand);
            filterDTO.setCategory(category);
            filterDTO.setMinPrice(minPrice);
            filterDTO.setMaxPrice(maxPrice);
            filterDTO.setMinYear(minYear);
            filterDTO.setMaxYear(maxYear);
            filterDTO.setMinSeats(minSeats);
            filterDTO.setAutomatic(automatic);
            filterDTO.setFuelTypes(fuelTypes);
            filterDTO.setEngine(engine);
            filterDTO.setCcBatteryCapacity(ccBatteryCapacity);
            filterDTO.setPerformance(performance);
            filterDTO.setTotalSpeed(totalSpeed);
            filterDTO.setFeatured(featured);

            // Create pageable
            Sort sort = sortOrder.equalsIgnoreCase("DESC") ?
                    Sort.by(Sort.Direction.DESC, sortBy) :
                    Sort.by(Sort.Direction.ASC, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            // Get filtered, paged results using your service
            Page<Product> products = productQueryService.getFilteredProducts(filterDTO, pageable);

            // Add pagination info to model
            model.addAttribute("products", products);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", products.getTotalPages());


            return "products/shop";  // Thymeleaf view name
        }



        @ModelAttribute("user")
        public User getCurrentUser(Principal principal) {
            if (principal != null) {
                String email = principal.getName();
                return userRepository.findByemail(email).orElse(null);
            }
            return null;
        }

        @ModelAttribute("cartCount")
        public Integer getCartCount(Principal principal) {
            // You can implement cart count logic here
            // For now, returning 0 as placeholder
            return 0;
        }
    }*/