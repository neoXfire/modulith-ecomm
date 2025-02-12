package fr.boursorama.modulith.ecomm.order.internal;

import fr.boursorama.modulith.ecomm.order.OrderConfirmedEvent.CartEntry;
import org.mapstruct.Mapper;

@Mapper
public interface CartItemMapper {

	CartEntry asOrderConfirmedCartEntry(CartItem cartItem);

}
