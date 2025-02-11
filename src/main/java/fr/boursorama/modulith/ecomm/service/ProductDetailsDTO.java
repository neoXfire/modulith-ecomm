package fr.boursorama.modulith.ecomm.service;

import java.util.UUID;

public record ProductDetailsDTO(
        UUID productId,
        String name,
        String description
) {

}
