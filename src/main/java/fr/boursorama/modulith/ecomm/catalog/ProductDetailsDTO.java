package fr.boursorama.modulith.ecomm.catalog;

import java.util.UUID;

public record ProductDetailsDTO(
        UUID productId,
        String name,
        String description
) {

}
