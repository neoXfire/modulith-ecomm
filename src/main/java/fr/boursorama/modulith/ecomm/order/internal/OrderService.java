package fr.boursorama.modulith.ecomm.order.internal;

import fr.boursorama.modulith.ecomm.EntityNotFoundException;
import fr.boursorama.modulith.ecomm.InvalidTransitionException;
import fr.boursorama.modulith.ecomm.catalog.ProductService;
import fr.boursorama.modulith.ecomm.order.OrderConfirmedEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

	private final CartDao cartDao;

	private final OrderDao orderDao;

	private final CartItemDao cartItemDao;
	private final ProductService productService;
	private final PaymentService paymentService;
	private final CartItemMapper cartItemMapper;
	private final ApplicationEventPublisher eventPublisher;

	@Autowired
	public OrderService(CartDao cartDao,
			OrderDao orderDao,
			CartItemDao cartItemDao,
			ProductService productService,
			PaymentService paymentService,
			CartItemMapper cartItemMapper,
			ApplicationEventPublisher eventPublisher) {
		this.cartDao = cartDao;
		this.orderDao = orderDao;
		this.cartItemDao = cartItemDao;
		this.productService = productService;
		this.paymentService = paymentService;
		this.cartItemMapper = cartItemMapper;
		this.eventPublisher = eventPublisher;
	}

	public UUID execute(InitNewOrderCommand __) {
		Cart cart = initCart();
		Order order = initOrder(cart);
		cartDao.save(cart);
		orderDao.save(order);
		return order.getOrderId();
	}

	public void execute(ConfirmCartCommand command) {
		Order order = orderDao.findById(command.orderId())
				.orElseThrow(() -> EntityNotFoundException.of("order", "orderId"));
		ensureOrderStatusIs(order, OrderStatus.CART_SELECTION,
				"The cart cannot be modified because it has already been confirmed");
		order.setStatus(OrderStatus.CART_CONFIRMED);
		order.setCartConfirmedOn(Instant.now());
		orderDao.save(order);
	}

	public void execute(AddItemToCartCommand command) {
		Order order = orderDao.findById(command.orderId())
				.orElseThrow(() -> EntityNotFoundException.of("order", "orderId"));
		productService.ensureProductExists(command.productId());
		ensureOrderStatusIs(order, OrderStatus.CART_SELECTION,
				"The cart cannot be modified because it has already been confirmed");

		Cart cart = order.getCart();
		CartItem cartItem = cartItemDao.findByCartAndProductId(cart, command.productId())
				.orElseGet(() -> CartItem.of(cart, command.productId()));
		cartItem.setQuantity(cartItem.getQuantity() + 1);
		cartItemDao.save(cartItem);
	}

	public void execute(PayCommand command) {
		Order order = orderDao.findById(command.orderId())
				.orElseThrow(() -> EntityNotFoundException.of("order", "orderId"));
		ensureOrderStatusIs(order, OrderStatus.CART_CONFIRMED,
				"The specified order does not require a payment");
		double totalPrice = computeTotalPrice(command.orderId());
		boolean paymentHasSucceeded = paymentService
				.proceedWithPayment(totalPrice, command.nom(), command.numero(), command.dateExpiration(),
						command.ccv());
		if (paymentHasSucceeded) {
			order.setStatus(OrderStatus.PAYMENT_CONFIRMED);
			order.setPaymentConfirmedOn(Instant.now());
			final var event = new OrderConfirmedEvent(order.getOrderId(), totalPrice,
					order.getCart().getCartEntries().stream()
							.map(cartItemMapper::asOrderConfirmedCartEntry)
							.toList()
			);
			eventPublisher.publishEvent(event);
		} else {
			order.setStatus(OrderStatus.PAYMENT_FAILED);
		}
		orderDao.save(order);
	}


	public void execute(RemoveItemFromCartCommand command) {
		Order order = orderDao.findById(command.orderId())
				.orElseThrow(() -> EntityNotFoundException.of("order", "orderId"));
		productService.ensureProductExists(command.productId());
		ensureOrderStatusIs(order, OrderStatus.CART_SELECTION,
				"The cart cannot be modified because it has already been confirmed");
		CartItem cartItem = cartItemDao.findByCartAndProductId(order.getCart(), command.productId())
				.orElseThrow(this::noExistingEntryForProductInCartException);
		int newQuantity = cartItem.getQuantity() - 1;
		if (newQuantity == 0) {
			cartItemDao.delete(cartItem);
		} else {
			cartItem.setQuantity(newQuantity);
			cartItemDao.save(cartItem);
		}
	}

	public void execute(CancelOrderCommand command) {
		Order order = orderDao.findById(command.orderId())
				.orElseThrow(() -> EntityNotFoundException.of("order", "orderId"));
		ensureOrderStatusIs(order, OrderStatus.CART_SELECTION,
				"Order can not be cancelled at this stage : " + order.getStatus().toString());
		order.setStatus(OrderStatus.CANCELED);
		order.setCancelledOn(Instant.now());
		orderDao.save(order);
	}

	public double computeTotalPrice(UUID orderId) {
		Order order = orderDao.findById(orderId)
				.orElseThrow(() -> EntityNotFoundException.of("order", "orderId"));
		Cart cart = order.getCart();
		double price = 0.0;
		for (CartItem cartEntry : cart.getCartEntries()) {
			double productPrice = productService.getProductPrice(cartEntry.getProductId());
			price += productPrice * cartEntry.getQuantity();
		}
		return price;
	}

	public void ensureOrderStatusIs(Order order, OrderStatus status, String errorMessage) {

		if (order.getStatus() != status) {
			throw new InvalidTransitionException(errorMessage);
		}
	}


	private Cart initCart() {
		Cart cart = new Cart();
		cart.setCartId(UUID.randomUUID());
		return cart;
	}

	private Order initOrder(Cart cart) {
		Order order = new Order();
		order.setOrderId(UUID.randomUUID());
		order.setCart(cart);
		order.setCreatedOn(Instant.now());
		order.setStatus(OrderStatus.CART_SELECTION);
		return order;
	}

	private InvalidTransitionException noExistingEntryForProductInCartException() {
		return new InvalidTransitionException("No more product with that product in the cart !");
	}

	public record InitNewOrderCommand() {

	}

	public record AddItemToCartCommand(
			UUID orderId,
			UUID productId
	) {

	}

	public record RemoveItemFromCartCommand(
			UUID orderId,
			UUID productId
	) {

	}

	public record ConfirmCartCommand(UUID orderId) {

	}


	public record PayCommand(
			UUID orderId,
			String nom,
			String numero,
			String dateExpiration,
			String ccv
	) {

	}

	public record CancelOrderCommand(UUID orderId) {

	}

	public record ShipOrderCommand(UUID orderId) {

	}
}
