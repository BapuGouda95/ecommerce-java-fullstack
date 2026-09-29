package com.shopsphere.order;
import com.fasterxml.jackson.annotation.JsonIgnore; import jakarta.persistence.*; import java.math.BigDecimal;
@Entity @Table(name="order_items") public class OrderItem{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private Long productId; private String productName; private int quantity; @Column(precision=12,scale=2) private BigDecimal unitPrice;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="order_id",nullable=false) @JsonIgnore private Order order;
 public OrderItem(){} public OrderItem(Long productId,String productName,int quantity,BigDecimal unitPrice){this.productId=productId;this.productName=productName;this.quantity=quantity;this.unitPrice=unitPrice;}
 public Long getId(){return id;} public Long getProductId(){return productId;} public String getProductName(){return productName;} public int getQuantity(){return quantity;} public BigDecimal getUnitPrice(){return unitPrice;} public void setOrder(Order o){order=o;}
}