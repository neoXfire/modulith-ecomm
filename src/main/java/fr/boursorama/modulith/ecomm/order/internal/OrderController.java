package fr.boursorama.modulith.ecomm.order.internal;

import fr.boursorama.modulith.ecomm.order.internal.OrderService.CancelOrderCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.ConfirmCartCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.InitNewOrderCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.PayCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.RemoveItemFromCartCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.AddItemToCartCommand;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	public record IdDTO(
			UUID id
	) {

	}
}
