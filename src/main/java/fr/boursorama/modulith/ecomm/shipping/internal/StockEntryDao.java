package fr.boursorama.modulith.ecomm.shipping.internal;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockEntryDao extends JpaRepository<StockEntry, UUID> {

	Optional<StockEntry> findByProductId(UUID productId);
}
