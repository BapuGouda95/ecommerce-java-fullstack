package com.shopsphere.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 120)
    private String name;

    @NotBlank @Size(max = 60)
    private String category;

    @NotNull @Positive
    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Size(max = 500)
    private String description;

    @Size(max = 500)
    private String imageUrl;

    @Min(0)
    private int stock;

    public Product() {}

    public Product(String name, String category, BigDecimal price, String description, String imageUrl, int stock) {
        this.name=name; this.category=category; this.price=price; this.description=description; this.imageUrl=imageUrl; this.stock=stock;
    }

    public Long getId(){return id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getCategory(){return category;}
    public void setCategory(String category){this.category=category;}
    public BigDecimal getPrice(){return price;}
    public void setPrice(BigDecimal price){this.price=price;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
    public String getImageUrl(){return imageUrl;}
    public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}
    public int getStock(){return stock;}
    public void setStock(int stock){this.stock=stock;}
}
