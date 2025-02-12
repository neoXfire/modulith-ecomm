package fr.boursorama.modulith.ecomm.order;


import java.util.List;
import java.util.UUID;

public record OrderConfirmedEvent(
		UUID orderId,
		double totalPrice,
		List<CartEntry> cartEntries
) {

	public record CartEntry(UUID productId, int quantity) {

	}
}
