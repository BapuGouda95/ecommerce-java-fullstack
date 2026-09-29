package com.shopsphere.order;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.LocalDateTime; import java.util.*;
@Entity @Table(name="orders") public class Order{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private Long userId; private String customerEmail; @Enumerated(EnumType.STRING) private OrderStatus status=OrderStatus.PLACED; @Column(precision=12,scale=2) private BigDecimal totalAmount; private LocalDateTime createdAt;
 @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true) private List<OrderItem> items=new ArrayList<>();
 public Order(){} public Order(Long userId,String customerEmail){this.userId=userId;this.customerEmail=customerEmail;} @PrePersist void onCreate(){createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public Long getUserId(){return userId;} public String getCustomerEmail(){return customerEmail;} public OrderStatus getStatus(){return status;} public BigDecimal getTotalAmount(){return totalAmount;} public LocalDateTime getCreatedAt(){return createdAt;} public List<OrderItem> getItems(){return items;}
 public void setStatus(OrderStatus s){status=s;} public void setTotalAmount(BigDecimal t){totalAmount=t;} public void addItem(OrderItem i){items.add(i);i.setOrder(this);}
}