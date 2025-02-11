package fr.boursorama.modulith.ecomm.order;

import fr.boursorama.modulith.ecomm.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartEntryDao extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
