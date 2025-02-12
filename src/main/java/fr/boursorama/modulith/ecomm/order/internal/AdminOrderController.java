package fr.boursorama.modulith.ecomm.order.internal;

import fr.boursorama.modulith.ecomm.SecurityConfig;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.ShipOrderCommand;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
@SecurityRequirement(name = "basicAuth")
@Tag(name = SecurityConfig.OPEN_API_SECURED_TAG_NAME)

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
