package fr.boursorama.modulith.ecomm.shipping.internal;


import fr.boursorama.modulith.ecomm.InvalidTransitionException;
import fr.boursorama.modulith.ecomm.shipping.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    @Autowired
    private final StockEntryDao stockEntryDao;

    public StockServiceImpl(StockEntryDao stockEntryDao) {
        this.stockEntryDao = stockEntryDao;
    }

    public boolean isAvailable(UUID productId) {
        return stockEntryDao.findByProductId(productId)
                .map(stockEntry -> stockEntry.getQuantity() > 0)
                .orElse(false);
    }

    public void execute(ProductResupplyCommand command) {
        StockEntry stockEntry = stockEntryDao.findByProductId(command.productId())
                .orElseGet(() -> initNewStockEntry(command.productId()));
        int newQuantity = stockEntry.getQuantity() + command.quantity();
        stockEntry.setQuantity(newQuantity);
        stockEntry.setLastResupplyOn(Instant.now());
        stockEntryDao.save(stockEntry);
    }

    public void updateStockForShippedProduct(UUID productId, int quantity) {
        Optional<StockEntry> stockEntry = stockEntryDao.findByProductId(productId);
        boolean hasEnoughProductsInStock = stockEntry.map(StockEntry::getQuantity).orElse(0) >= quantity;
        if (!hasEnoughProductsInStock) {
            throw new InvalidTransitionException("There is no enough product in stock. Please resupply.");
        }
        StockEntry updatedStockEntry = stockEntry.orElseThrow();
        updatedStockEntry.setQuantity(updatedStockEntry.getQuantity() - quantity);
        stockEntryDao.save(updatedStockEntry);
    }

    private StockEntry initNewStockEntry(UUID productId) {
        StockEntry stockEntry = new StockEntry();
        stockEntry.setStockEntryId(UUID.randomUUID());
        stockEntry.setProductId(productId);
        return stockEntry;
    }

    public record ProductResupplyCommand(
            UUID productId,
            int quantity
    ) {
    }
}
