package fr.boursorama.modulith.ecomm.shipping.internal;

import fr.boursorama.modulith.ecomm.SecurityConfig;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/admin/stock")
@SecurityRequirement(name = "basicAuth")
@Tag(name = SecurityConfig.OPEN_API_SECURED_TAG_NAME)
public class AdminStockController {

	private final StockService stockService;

	@Autowired
	public AdminStockController(StockService stockService) {
		this.stockService = stockService;
	}

	@PostMapping("/product/{productId}/stock/resupply")
	public void resupplyStock(@PathVariable UUID productId, @RequestParam int quantity) {
		final var command = new StockService.ProductResupplyCommand(productId, quantity);
		stockService.execute(command);
	}
}
