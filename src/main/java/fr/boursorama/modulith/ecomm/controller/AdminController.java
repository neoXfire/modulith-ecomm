package fr.boursorama.modulith.ecomm.controller;

import fr.boursorama.modulith.ecomm.service.CatalogService;
import fr.boursorama.modulith.ecomm.service.CatalogService.RegisterProductInCatalogCommand;
import fr.boursorama.modulith.ecomm.service.OrderService;
import fr.boursorama.modulith.ecomm.service.OrderService.ShipOrderCommand;
import fr.boursorama.modulith.ecomm.service.StockService;
import fr.boursorama.modulith.ecomm.service.StockService.ProductResupplyCommand;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/admin")
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
@SecurityRequirement(name = "basicAuth")
public class AdminController {

    private final StockService stockService;

    private final CatalogService catalogService;

    private final OrderService orderService;

    @Autowired
    public AdminController(StockService stockService, CatalogService catalogService, OrderService shipingService) {
        this.stockService = stockService;
        this.catalogService = catalogService;
        this.orderService = shipingService;
    }

    @PostMapping("/product/{productId}/stock/resupply")
    public void resupplyStock(@PathVariable UUID productId, @RequestParam int quantity) {
        final var command = new ProductResupplyCommand(productId, quantity);
        stockService.execute(command);
    }


    @PutMapping("/product/{productId}")
    public void registerProductInCatalog(@PathVariable UUID productId,
                                         @RequestParam String name,
                                         @RequestParam String description,
                                         @RequestParam double price) {
        final var command = new RegisterProductInCatalogCommand(productId, name, description, price);
        catalogService.execute(command);
    }

    @PostMapping("/order/{orderId}/ship")
    public void ship(@PathVariable UUID orderId) {
        final var command = new ShipOrderCommand(orderId);
        orderService.execute(command);
    }




}
