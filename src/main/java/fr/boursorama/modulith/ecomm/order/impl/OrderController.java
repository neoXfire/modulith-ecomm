package fr.boursorama.modulith.ecomm.order.impl;

import fr.boursorama.modulith.ecomm.order.impl.OrderService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public IdDTO initNewOrder() {
        final var command = new InitNewOrderCommand();
        UUID cartId = orderService.execute(command);
        return new IdDTO(cartId);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable UUID orderId) {
        final var command = new CancelOrderCommand(orderId);
        orderService.execute(command);
    }

    @PostMapping("/{orderId}/cart/product/{productId}")
    public void addItemToCart(@PathVariable UUID orderId, @PathVariable UUID productId) {
        final var command = new AddItemToCartCommand(orderId, productId);
        orderService.execute(command);
    }

    @DeleteMapping("/{orderId}/cart/product/{productId}")
    public void removeItemFromCart(@PathVariable UUID orderId, @PathVariable UUID productId) {
        final var command = new RemoveItemFromCartCommand(orderId, productId);
        orderService.execute(command);
    }

    @PostMapping("/{orderId}/cart/confirmation")
    public void confirmCart(@PathVariable UUID orderId) {
        final var command = new ConfirmCartCommand(orderId);
        orderService.execute(command);
    }

    @PostMapping("/{orderId}/payment")
    public void pay(@PathVariable UUID orderId,
                    @RequestParam String nom,
                    @RequestParam String numero,
                    @RequestParam String dateExpiration,
                    @RequestParam String ccv) {
        final var command = new PayCommand(
                orderId,
                nom,
                numero,
                dateExpiration,
                ccv
        );
        orderService.execute(command);
    }

}
