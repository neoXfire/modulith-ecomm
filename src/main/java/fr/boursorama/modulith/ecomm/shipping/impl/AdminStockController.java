package fr.boursorama.modulith.ecomm.shipping.impl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/admin/stock")
@SecurityRequirement(name = "basicAuth")
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
