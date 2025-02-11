package fr.boursorama.modulith.ecomm.shipping;

import fr.boursorama.modulith.ecomm.catalog.Product;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
public class StockEntry {

    @Id
    private UUID stockEntryId;

    @OneToOne(optional = false)
    @JoinColumn(name = "PRODUCT_ID", unique = true, nullable = false, updatable = false)
    private Product product;

    private int quantity;

    @Column(nullable = false)
    private Instant lastResupplyOn;

    public UUID getStockEntryId() {
        return stockEntryId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Instant getLastResupplyOn() {
        return lastResupplyOn;
    }

    public void setStockEntryId(UUID stockEntryId) {
        this.stockEntryId = stockEntryId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setLastResupplyOn(Instant lastResupplyOn) {
        this.lastResupplyOn = lastResupplyOn;
    }
}
