package fr.boursorama.modulith.ecomm.catalog.internal;

import fr.boursorama.modulith.ecomm.EntityNotFoundException;
import fr.boursorama.modulith.ecomm.catalog.ProductService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductDao productDao;

	@Autowired
	public ProductServiceImpl(ProductDao productDao) {
		this.productDao = productDao;
	}

	@Override
	public void ensureProductExists(UUID productId) {
		productDao.findById(productId)
				.orElseThrow(() -> EntityNotFoundException.of("product", "productId"));

	}

	@Override
	public double getProductPrice(UUID productId) {
		return productDao.findById(productId)
				.orElseThrow(() -> EntityNotFoundException.of("product", "productId"))
				.getPrice();
	}
}
