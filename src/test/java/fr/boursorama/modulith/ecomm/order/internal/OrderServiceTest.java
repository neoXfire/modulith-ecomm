package fr.boursorama.modulith.ecomm.order.internal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.boursorama.modulith.ecomm.catalog.ProductService;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.ConfirmCartCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.InitNewOrderCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.PayCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.ShipOrderCommand;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.AddItemToCartCommand;
import fr.boursorama.modulith.ecomm.shipping.StockService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
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

	@MockitoBean
	StockService stockService;

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
	@DisplayName("Check ShipOrderCommand behaviour")
	class ShipOrderCommandTest {

		@Test
		@DisplayName("Check that order status switch correctly and that shipping module is called for stock update")
		void test() {
			// Given
			UUID productId = UUID.randomUUID();
			final var command1 = new InitNewOrderCommand();
			UUID orderId = orderService.execute(command1);
			final var command2 = new AddItemToCartCommand(orderId, productId);
			orderService.execute(command2);
			final var command3 = new ConfirmCartCommand(orderId);
			orderService.execute(command3);
			final var command4 = new PayCommand(orderId, "nom", "numero", "dateExpiration", "ccv");
			when(paymentService.proceedWithPayment(anyDouble(), any(), any(), any(), any())).thenReturn(true);
			orderService.execute(command4);

			// When
			final var command = new ShipOrderCommand(orderId);
			orderService.execute(command);

			// Then
			Optional<Order> optionalOrder = orderDao.findById(orderId);
			assertThat(optionalOrder).isPresent();
			Order order = optionalOrder.get();
			assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPED);
			verify(stockService).updateStockForShippedProduct(productId, 1);
		}

	}


}