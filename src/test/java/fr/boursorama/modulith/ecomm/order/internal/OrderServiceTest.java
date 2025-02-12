package fr.boursorama.modulith.ecomm.order.internal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import fr.boursorama.modulith.ecomm.catalog.ProductService;
import fr.boursorama.modulith.ecomm.order.internal.OrderService.InitNewOrderCommand;
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


}