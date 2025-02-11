package fr.boursorama.modulith.ecomm.catalog.impl;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/catalog")
@SecurityRequirement(name = "basicAuth")
public class AdminCatalogController {

    private final CatalogService catalogService;

    @Autowired
    public AdminCatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @PutMapping("/product/{productId}")
    public void registerProductInCatalog(@PathVariable UUID productId,
                                         @RequestParam String name,
                                         @RequestParam String description,
                                         @RequestParam double price) {
        final var command = new CatalogService.RegisterProductInCatalogCommand(productId, name, description, price);
        catalogService.execute(command);
    }
}
