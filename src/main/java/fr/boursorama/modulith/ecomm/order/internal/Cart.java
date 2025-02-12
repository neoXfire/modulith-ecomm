package fr.boursorama.modulith.ecomm.order.internal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import java.util.UUID;

@Entity
public class Cart {

	@Id
	private UUID cartId;

	@OneToOne(optional = false, mappedBy = "cart")
	private Order order;

	@OneToMany(mappedBy = "cart")
	private List<CartItem> cartEntries;

	public UUID getCartId() {
		return cartId;
	}

	public void setCartId(UUID cartId) {
		this.cartId = cartId;
	}

	public List<CartItem> getCartEntries() {
		return cartEntries;
	}

	public void setCartEntries(List<CartItem> cartEntries) {
		this.cartEntries = cartEntries;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
