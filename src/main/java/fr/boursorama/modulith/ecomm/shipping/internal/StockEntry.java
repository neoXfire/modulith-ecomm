package fr.boursorama.modulith.ecomm.shipping.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
public class StockEntry {

	@Id
	private UUID stockEntryId;

	@Column(unique = true, nullable = false, updatable = false)
	private UUID productId;

	private int quantity;

	@Column(nullable = false)
	private Instant lastResupplyOn;

	public UUID getStockEntryId() {
		return stockEntryId;
	}

	public void setStockEntryId(UUID stockEntryId) {
		this.stockEntryId = stockEntryId;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Instant getLastResupplyOn() {
		return lastResupplyOn;
	}

	public void setLastResupplyOn(Instant lastResupplyOn) {
		this.lastResupplyOn = lastResupplyOn;
	}
}
