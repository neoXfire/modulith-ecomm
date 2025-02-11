package fr.boursorama.modulith.ecomm.shipping;

import fr.boursorama.modulith.ecomm.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockEntryDao extends JpaRepository<StockEntry, UUID> {

    Optional<StockEntry> findByProduct(Product productId);
}
