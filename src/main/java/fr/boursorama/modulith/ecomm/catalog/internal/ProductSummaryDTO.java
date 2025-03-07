package fr.boursorama.modulith.ecomm.catalog.internal;

import java.util.UUID;

public record ProductSummaryDTO(
		UUID productId,
		String name,
		String smallDescription,
		boolean available
) {

}
