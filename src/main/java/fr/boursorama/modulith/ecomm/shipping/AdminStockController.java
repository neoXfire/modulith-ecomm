package fr.boursorama.modulith.ecomm.shipping;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/admin/stock")
@SecurityRequirement(name = "basicAuth")
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
