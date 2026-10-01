package com.shopsphere.order;

import com.shopsphere.product.Product;
import com.shopsphere.product.ProductRepository;
import com.shopsphere.user.User;
import com.shopsphere.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orders;
    private final ProductRepository products;
    private final UserRepository users;

    public OrderService(OrderRepository orders, ProductRepository products, UserRepository users) {
        this.orders = orders;
        this.products = products;
        this.users = users;
    }

    @Transactional
    public Order create(Long userId, OrderDtos.CreateOrderRequest req) {
        User u = users.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Order o = new Order(u.getId(), u.getEmail(), req.shippingAddress().trim());
        BigDecimal total = BigDecimal.ZERO;

        for (var item : req.items()) {
            Product p = products.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.productId()));

            if (p.getStock() < item.quantity()) {
                throw new IllegalArgumentException("Insufficient stock for " + p.getName());
            }

            p.setStock(p.getStock() - item.quantity());
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
            o.addItem(new OrderItem(p.getId(), p.getName(), item.quantity(), p.getPrice()));
        }

        if (total.signum() <= 0) throw new IllegalArgumentException("Order total must be greater than zero");
        o.setTotalAmount(total);
        return orders.save(o);
    }

    public List<Order> myOrders(Long userId) {
        return orders.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Order> all() {
        return orders.findAll(org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
    }

    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        Order o = orders.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        o.setStatus(status);
        return orders.save(o);
    }
}
