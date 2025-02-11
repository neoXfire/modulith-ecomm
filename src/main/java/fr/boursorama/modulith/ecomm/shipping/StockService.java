package fr.boursorama.modulith.ecomm.shipping;

import java.util.UUID;

public interface StockService {

    boolean isAvailable(UUID productId);

    void updateStockForShippedProduct(UUID productId, int quantity);

}
