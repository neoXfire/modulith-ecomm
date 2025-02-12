package fr.boursorama.modulith.ecomm.catalog.internal;

import fr.boursorama.modulith.ecomm.catalog.ProductAvailabilityProvider;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CatalogService {

	private final ProductDao productDao;

	private final ProductMappers productsMapper;

	private final ProductAvailabilityProvider productAvailabilityProvider;

	@Autowired
	public CatalogService(ProductDao productDao, ProductMappers productMappers,
			ProductAvailabilityProvider productAvailabilityProvider) {
		this.productDao = productDao;
		this.productsMapper = productMappers;
		this.productAvailabilityProvider = productAvailabilityProvider;
	}

	public List<ProductSummaryDTO> listAllProducts() {
		return productDao.findAll().stream()
				.map(this::withAvailabilityInfo)
				.map(p -> productsMapper.asProductSummaryDTO(p.getFirst(), p.getSecond()))
				.toList();
	}

	public ProductDetailsDTO getProductDetails(UUID productId) {
		return productDao.findById(productId)
				.map(this::withAvailabilityInfo)
				.map(p -> productsMapper.asProductDetailsDTO(p.getFirst(), p.getSecond()))
				.orElseThrow(EntityNotFoundException::new);
	}

	public void execute(RegisterProductInCatalogCommand registerNewProductCommand) {
		Product productEntity = productsMapper.asProductEntity(registerNewProductCommand);
		productDao.save(productEntity);
	}

	private Pair<Product, Boolean> withAvailabilityInfo(Product product) {
		boolean available = productAvailabilityProvider.isAvailable(product.getProductId());
		return Pair.of(product, available);
	}

	public record RegisterProductInCatalogCommand(
			UUID productId,
			String name,
			String description,
			double price
	) {

	}
}
