package com.shopsphere.product;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) { this.repository = repository; }

    public List<Product> findAll(String search, String category) {
        if (search != null && !search.isBlank()) return repository.findByNameContainingIgnoreCase(search);
        if (category != null && !category.isBlank()) return repository.findByCategoryIgnoreCase(category);
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product create(Product product) { return repository.save(product); }
    public Product update(Long id, Product incoming) {
        Product p = findById(id);
        p.setName(incoming.getName()); p.setCategory(incoming.getCategory());
        p.setPrice(incoming.getPrice()); p.setDescription(incoming.getDescription());
        p.setImageUrl(incoming.getImageUrl()); p.setStock(incoming.getStock());
        return repository.save(p);
    }
    public void delete(Long id) { repository.delete(findById(id)); }
}
