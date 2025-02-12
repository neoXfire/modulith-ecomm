package fr.boursorama.modulith.ecomm.shipping.internal;

import fr.boursorama.modulith.ecomm.SecurityConfig;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/admin/stock")
@SecurityRequirement(name = "basicAuth")
@Tag(name = SecurityConfig.OPEN_API_SECURED_TAG_NAME)
public class AdminStockController {

    private final StockServiceImpl stockServiceImpl;

    @Autowired
    public AdminStockController(StockServiceImpl stockServiceImpl) {
        this.stockServiceImpl = stockServiceImpl;
    }

    @PostMapping("/product/{productId}/stock/resupply")
    public void resupplyStock(@PathVariable UUID productId, @RequestParam int quantity) {
        final var command = new StockServiceImpl.ProductResupplyCommand(productId, quantity);
        stockServiceImpl.execute(command);
    }
}
