package fr.boursorama.modulith.ecomm.shipping.internal;


import fr.boursorama.modulith.ecomm.InvalidTransitionException;
import fr.boursorama.modulith.ecomm.catalog.ProductAvailabilityProvider;
import fr.boursorama.modulith.ecomm.order.OrderConfirmedEvent;
import fr.boursorama.modulith.ecomm.order.OrderConfirmedEvent.CartEntry;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockService implements ProductAvailabilityProvider {

	@Autowired
	private final StockEntryDao stockEntryDao;

	public StockService(StockEntryDao stockEntryDao) {
		this.stockEntryDao = stockEntryDao;
	}

	@Override
	public boolean isAvailable(UUID productId) {
		return stockEntryDao.findByProductId(productId)
				.map(stockEntry -> stockEntry.getQuantity() > 0)
				.orElse(false);
	}

	@ApplicationModuleListener
	void on(OrderConfirmedEvent event) {
		for (CartEntry cartEntry : event.cartEntries()) {
			int quantity = cartEntry.quantity();
			Optional<StockEntry> stockEntry = stockEntryDao.findByProductId(cartEntry.productId());
			boolean hasEnoughProductsInStock =
					stockEntry.map(StockEntry::getQuantity).orElse(0) >= quantity;
			if (!hasEnoughProductsInStock) {
				throw new InvalidTransitionException("There is no enough product in stock. Please resupply.");
			}
			StockEntry updatedStockEntry = stockEntry.orElseThrow();
			updatedStockEntry.setQuantity(updatedStockEntry.getQuantity() - quantity);
			stockEntryDao.save(updatedStockEntry);
		}
	}

	public void execute(ProductResupplyCommand command) {
		StockEntry stockEntry = stockEntryDao.findByProductId(command.productId())
				.orElseGet(() -> initNewStockEntry(command.productId()));
		int newQuantity = stockEntry.getQuantity() + command.quantity();
		stockEntry.setQuantity(newQuantity);
		stockEntry.setLastResupplyOn(Instant.now());
		stockEntryDao.save(stockEntry);
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
