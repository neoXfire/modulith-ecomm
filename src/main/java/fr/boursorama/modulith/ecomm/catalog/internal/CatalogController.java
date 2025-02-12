package fr.boursorama.modulith.ecomm.catalog.internal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/catalog/products")
public class CatalogController {

    private final CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<ProductSummaryDTO> listAll() {
        return catalogService.listAllProducts();
    }

    @GetMapping("/{productId}")
    public ProductDetailsDTO getProductDetails(@PathVariable UUID productId) {
        return catalogService.getProductDetails(productId);
    }
}
