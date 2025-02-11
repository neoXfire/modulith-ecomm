package fr.boursorama.modulith.ecomm.persistence.dao;

import fr.boursorama.modulith.ecomm.persistence.entity.Product;
import fr.boursorama.modulith.ecomm.persistence.entity.StockEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockEntryDao extends JpaRepository<StockEntry, UUID> {

    Optional<StockEntry> findByProduct(Product productId);
}
