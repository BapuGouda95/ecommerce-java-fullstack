package com.shopsphere.product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) { super("Product not found: " + id); }
}
