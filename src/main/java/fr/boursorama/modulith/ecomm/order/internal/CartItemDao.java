package fr.boursorama.modulith.ecomm.order.internal;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemDao extends JpaRepository<CartItem, UUID> {

	Optional<CartItem> findByCartAndProductId(Cart cart, UUID productId);

}
