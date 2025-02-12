package fr.boursorama.modulith.ecomm.catalog;

import fr.boursorama.modulith.ecomm.EntityNotFoundException;

import java.util.UUID;

public interface ProductService {

    /**
     *
     * @throws EntityNotFoundException in case the product does not exist
     * @param productId
     */
    void ensureProductExists(UUID productId);

    double getProductPrice(UUID productId);
}
