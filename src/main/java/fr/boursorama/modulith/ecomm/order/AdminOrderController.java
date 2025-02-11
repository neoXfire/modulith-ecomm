package fr.boursorama.modulith.ecomm.order;

import fr.boursorama.modulith.ecomm.order.OrderService.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/order")
@SecurityRequirement(name = "basicAuth")
public class AdminOrderController {

    private final OrderService orderService;

    @Autowired
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{orderId}/ship")
    public void ship(@PathVariable UUID orderId) {
        final var command = new ShipOrderCommand(orderId);
        orderService.execute(command);
    }

}
