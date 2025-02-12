package fr.boursorama.modulith.ecomm.catalog.internal;


import fr.boursorama.modulith.ecomm.SecurityConfig;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/catalog")
@SecurityRequirement(name = "basicAuth")
@Tag(name = SecurityConfig.OPEN_API_SECURED_TAG_NAME)
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
		final var command = new CatalogService.RegisterProductInCatalogCommand(productId, name,
				description, price);
		catalogService.execute(command);
	}
}
