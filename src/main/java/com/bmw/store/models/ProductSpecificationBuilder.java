package com.bmw.store.models;


import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecificationBuilder {

    public static Specification<Product> build(ProductFilterDTO dto) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getName() != null && !dto.getName().isBlank())
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + dto.getName().toLowerCase() + "%"));

            if (dto.getBrand() != null && !dto.getBrand().isBlank())
                predicates.add(cb.equal(cb.lower(root.get("brand")), dto.getBrand().toLowerCase()));

            if (dto.getCategory() != null && !dto.getCategory().isBlank())
                predicates.add(cb.equal(cb.lower(root.get("category")), dto.getCategory().toLowerCase()));

            if (dto.getMinPrice() != null)
                predicates.add(cb.ge(root.get("price"), dto.getMinPrice()));

            if (dto.getMaxPrice() != null)
                predicates.add(cb.le(root.get("price"), dto.getMaxPrice()));

            if (dto.getMinYear() != null)
                predicates.add(cb.ge(root.get("modelYear"), dto.getMinYear()));

            if (dto.getMaxYear() != null)
                predicates.add(cb.le(root.get("modelYear"), dto.getMaxYear()));

            if (dto.getAutomatic() != null)
                predicates.add(cb.equal(root.get("automatic"), dto.getAutomatic()));

            if (dto.getFuelTypes() != null && !dto.getFuelTypes().isEmpty())
                predicates.add(root.get("fuelTypes").in(dto.getFuelTypes()));

            if (dto.getMinSeats() != null) {
                predicates.add(cb.ge(root.get("seats"), dto.getMinSeats()));
            }

            if (dto.getEngine() != null)
                predicates.add(cb.equal(cb.lower(root.get("engines")), dto.getEngine().toLowerCase()));

            if (dto.getCcBatteryCapacity() != null)
                predicates.add(cb.equal(cb.lower(root.get("ccBatteryCapacity")), dto.getCcBatteryCapacity().toLowerCase()));

            if (dto.getPerformance() != null)
                predicates.add(cb.equal(cb.lower(root.get("performance")), dto.getPerformance().toLowerCase()));

            if (dto.getTotalSpeed() != null)
                predicates.add(cb.equal(cb.lower(root.get("totalSpeed")), dto.getTotalSpeed().toLowerCase()));

            if (dto.getFeatured() != null)
                predicates.add(cb.equal(root.get("featured"), dto.getFeatured()));

            predicates.add(cb.isTrue(root.get("status"))); // always filter active products

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

