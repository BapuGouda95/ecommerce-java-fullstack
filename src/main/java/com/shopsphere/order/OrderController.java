package com.shopsphere.order;
import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/orders") public class OrderController{
 private final OrderService service; public OrderController(OrderService service){this.service=service;}
 @PostMapping public Order create(@RequestAttribute("userId") Long userId,@Valid @RequestBody OrderDtos.CreateOrderRequest req){return service.create(userId,req);}
 @GetMapping("/my") public List<Order> mine(@RequestAttribute("userId") Long userId){return service.myOrders(userId);}
 @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<Order> all(){return service.all();}
 @PatchMapping("/{id}/status") @PreAuthorize("hasRole('ADMIN')") public Order status(@PathVariable Long id,@RequestParam OrderStatus status){return service.updateStatus(id,status);}
}