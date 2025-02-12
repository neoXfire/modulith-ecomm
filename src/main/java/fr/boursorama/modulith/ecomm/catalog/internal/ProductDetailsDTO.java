package fr.boursorama.modulith.ecomm.catalog.internal;

import java.util.UUID;

public record ProductDetailsDTO(
        UUID productId,
        String name,
        String description
) {

}
