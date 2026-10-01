package com.shopsphere.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public final class OrderDtos {
    private OrderDtos() {}

    public record ItemRequest(@NotNull Long productId, @Min(1) int quantity) {}

    public record CreateOrderRequest(
            @NotEmpty List<@Valid ItemRequest> items,
            @NotBlank @Size(max = 500) String shippingAddress
    ) {}
}
