package fr.boursorama.modulith.ecomm.shipping;


import fr.boursorama.modulith.ecomm.catalog.Product;
import fr.boursorama.modulith.ecomm.catalog.ProductDao;
import fr.boursorama.modulith.ecomm.InvalidTransitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class StockService {

    @Autowired
    private final StockEntryDao stockEntryDao;

    @Autowired
    private final ProductDao productDao;

    public StockService(StockEntryDao stockEntryDao, ProductDao productDao) {
        this.stockEntryDao = stockEntryDao;
        this.productDao = productDao;
    }

    public void execute(ProductResupplyCommand command) {
        Product product = productDao.getReferenceById(command.productId());
        StockEntry stockEntry = stockEntryDao.findByProduct(product)
                .orElseGet(() -> initNewStockEntry(product));
        int newQuantity = stockEntry.getQuantity() + command.quantity();
        stockEntry.setQuantity(newQuantity);
        stockEntry.setLastResupplyOn(Instant.now());
        stockEntryDao.save(stockEntry);
    }

    public void updateStockForShippedProduct(UUID productId, int quantity) {
        Product product = productDao.getReferenceById(productId);
        Optional<StockEntry> stockEntry = stockEntryDao.findByProduct(product);
        boolean hasEnoughProductsInStock = stockEntry.map(StockEntry::getQuantity).orElse(0) >= quantity;
        if (!hasEnoughProductsInStock) {
            throw new InvalidTransitionException("There is no enough product in stock. Please resupply.");
        }
        StockEntry updatedStockEntry = stockEntry.orElseThrow();
        updatedStockEntry.setQuantity(updatedStockEntry.getQuantity() - quantity);
        stockEntryDao.save(updatedStockEntry);
    }

    private StockEntry initNewStockEntry(Product product) {
        StockEntry stockEntry = new StockEntry();
        stockEntry.setStockEntryId(UUID.randomUUID());
        stockEntry.setProduct(product);
        return stockEntry;
    }

    public record ProductResupplyCommand(
            UUID productId,
            int quantity
    ) {
    }
}
