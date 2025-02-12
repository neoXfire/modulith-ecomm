package fr.boursorama.modulith.ecomm.catalog;

import fr.boursorama.modulith.ecomm.EntityNotFoundException;
import java.util.UUID;

public interface ProductService {

	/**
	 * @param productId
	 * @throws EntityNotFoundException in case the product does not exist
	 */
	void ensureProductExists(UUID productId);

	double getProductPrice(UUID productId);
}
