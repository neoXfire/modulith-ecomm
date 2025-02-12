package fr.boursorama.modulith.ecomm.order.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemDao extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByCartAndProductId(Cart cart, UUID productId);

}
