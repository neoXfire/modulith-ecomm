package fr.boursorama.modulith.ecomm.order.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import fr.boursorama.modulith.ecomm.catalog.ProductService;
import fr.boursorama.modulith.ecomm.order.OrderConfirmedEvent;
import fr.boursorama.modulith.ecomm.order.OrderConfirmedEvent.CartEntry;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.AddItemToCartCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.ConfirmCartCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.InitNewOrderCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.PayCommand;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@ApplicationModuleTest
@ActiveProfiles("test")
class OrderServiceTest {

	@Autowired
	OrderService orderService;

	@Autowired
	OrderDao orderDao;

	@MockitoBean
	ProductService productService;

	@MockitoSpyBean
	PaymentService paymentService;

	@Nested
	@DisplayName("Check InitOrderCommand behaviour")
	class InitOrderCommandTest {

		@Test
		@DisplayName("An entity with the same id that the one returned is persisted after execution")
		void test() {
			// Given
			InitNewOrderCommand command = new InitNewOrderCommand();
			// When
			UUID orderId = orderService.execute(command);
			// Then
			Optional<Order> optionalOrder = orderDao.findById(orderId);
			assertThat(optionalOrder).isPresent();
			Order order = optionalOrder.get();
			assertThat(order.getOrderId()).isEqualTo(orderId);
		}

	}

	@Nested
	class PayCommandSuccessTest {

		@Test
		@DisplayName("Check an event is published after payment success and the order status is updated")
		void test(Scenario scenario) {
			// Given
			final var initNewOrderCommand = new InitNewOrderCommand();
			final var orderId = orderService.execute(initNewOrderCommand);
			final var productId = UUID.randomUUID();
			final var addItemToCartCommand = new AddItemToCartCommand(orderId, productId);
			orderService.execute(addItemToCartCommand);
			final var confirmCartCommand = new ConfirmCartCommand(orderId);
			orderService.execute(confirmCartCommand);
			doReturn(true).when(paymentService).proceedWithPayment(anyDouble(),
					anyString(), anyString(), anyString(), anyString());

			// Expect
			final Consumer<OrderConfirmedEvent> eventAssertions = event -> {
				assertThat(event.orderId()).isEqualTo(orderId);
				final List<CartEntry> cartEntries = event.cartEntries();
				assertThat(cartEntries).hasSize(1);
				final CartEntry cartEntry = cartEntries.getFirst();
				assertThat(cartEntry.productId()).isEqualTo(productId);
			};

			// When
			final var nom = "M. DUJARDIN Jean";
			final var numero = "1234123412341234";
			final var ccv = "987";
			final var dateExpiration = "12/23";
			final var payCommand = new PayCommand(orderId, nom, numero, dateExpiration, ccv);
			scenario
					.stimulate(() -> orderService.execute(payCommand))
					.andWaitForEventOfType(OrderConfirmedEvent.class)
					.toArriveAndVerify(eventAssertions);

			// And check additionally
			Optional<Order> optionalOrder = orderDao.findById(orderId);
			assertThat(optionalOrder).isPresent();
			Order order = optionalOrder.get();
			assertThat(order.getOrderId()).isEqualTo(orderId);
			assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_CONFIRMED);
		}
	}

	@Nested
	class PayCommandFailedTest {



		@Test
		@DisplayName("Check the order status is updated")
		void test(Scenario scenario) {
			// Given
			final var initNewOrderCommand = new InitNewOrderCommand();
			final var orderId = orderService.execute(initNewOrderCommand);
			final var productId = UUID.randomUUID();
			final var addItemToCartCommand = new AddItemToCartCommand(orderId, productId);
			orderService.execute(addItemToCartCommand);
			final var confirmCartCommand = new ConfirmCartCommand(orderId);
			orderService.execute(confirmCartCommand);
			doReturn(false).when(paymentService).proceedWithPayment(anyDouble(),
					anyString(), anyString(), anyString(), anyString());


			// When
			final var nom = "M. DUJARDIN Jean";
			final var numero = "1234123412341234";
			final var ccv = "987";
			final var dateExpiration = "12/23";
			final var payCommand = new PayCommand(orderId, nom, numero, dateExpiration, ccv);
			scenario
					.stimulate(() -> orderService.execute(payCommand))
					.andWaitForStateChange(
							() -> orderDao.findById(orderId).map(Order::getStatus).orElse(null),
							OrderStatus.PAYMENT_FAILED::equals);

		}
	}


}