package fr.boursorama.modulith.ecomm.persistence.dao;

import fr.boursorama.modulith.ecomm.persistence.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartDao extends JpaRepository<Cart, UUID> {
}
