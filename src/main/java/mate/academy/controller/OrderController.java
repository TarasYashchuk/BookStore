package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.PlaceOrderRequestDto;
import mate.academy.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Operations pertaining to orders in Online Book App")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Place an order",
            description = "Allows a user to place an order with the items in their shopping cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public OrderDto placeOrder(
            @AuthenticationPrincipal User userDetails,
            @RequestBody @Parameter(description = "Details for placing an order", required = true)
            PlaceOrderRequestDto requestDto) {
        return orderService.createOrder(userDetails.getId(), requestDto.shippingAddress());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get order history",
            description = "Allows a user to view their order history.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Order history retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public List<OrderDto> getOrderHistory(
            @AuthenticationPrincipal User userDetails,
            @ParameterObject @PageableDefault Pageable pageable) {
        return orderService.getOrderHistory(userDetails.getId(), pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items",
            description = "Allows a user to view all items in a specific order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order items retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public List<OrderItemDto> getOrderItems(
            @PathVariable
            @Parameter(description = "ID of the order to retrieve items from", required = true)
            Long orderId,
            @ParameterObject @PageableDefault Pageable pageable) {
        return orderService.getOrderItems(orderId, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get specific order item",
            description = "Allows a user to view a specific item in an order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order item retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order or item not found")
    })
    public OrderItemDto getOrderItem(
            @PathVariable
            @Parameter(description = "ID of the order to retrieve the item from", required = true)
            Long orderId,
            @PathVariable @Parameter(description = "ID of the item to retrieve", required = true)
            Long itemId) {
        return orderService.getOrderItem(orderId, itemId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order status",
            description = "Allows an admin to update the status of an order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderDto updateOrderStatus(
            @PathVariable
            @Parameter(description = "ID of the order to update", required = true) Long orderId,
            @RequestBody
            @Parameter(description = "Details for updating the order status", required = true)
            UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(orderId, requestDto.status().name());
    }
}
