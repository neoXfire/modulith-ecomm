package fr.boursorama.modulith.ecomm.catalog;

import java.util.UUID;

public interface ProductAvailabilityProvider {

	boolean isAvailable(UUID productId);

}
