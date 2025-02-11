package fr.boursorama.modulith.ecomm.service;

import fr.boursorama.modulith.ecomm.persistence.dao.ProductDao;
import fr.boursorama.modulith.ecomm.persistence.dao.StockEntryDao;
import fr.boursorama.modulith.ecomm.persistence.entity.Product;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CatalogService {

    private final ProductDao productDao;

    private final ProductMappers productsMapper;

    private final StockEntryDao stockEntryDao;

    @Autowired
    public CatalogService(ProductDao productDao, ProductMappers productSummaryMapper, StockEntryDao stockEntryDao) {
        this.productDao = productDao;
        this.productsMapper = productSummaryMapper;
        this.stockEntryDao = stockEntryDao;
    }

    public List<ProductSummaryDTO> listAllProducts() {
        return productDao.findAll().stream()
                .map(this::withAvailabilityInfo)
                .map(p -> productsMapper.asProductSummaryDTO(p.getFirst(), p.getSecond()))
                .toList();
    }

    public ProductDetailsDTO getProductDetails(UUID productId) {
        return productDao.findById(productId)
                .map(this::withAvailabilityInfo)
                .map(p -> productsMapper.asProductDetailsDTO(p.getFirst(), p.getSecond()))
                .orElseThrow(EntityNotFoundException::new);
    }

    public void execute(RegisterProductInCatalogCommand registerNewProductCommand) {
        Product productEntity = productsMapper.asProductEntity(registerNewProductCommand);
        productDao.save(productEntity);
    }

    private Pair<Product, Boolean> withAvailabilityInfo(Product product) {
        boolean available = stockEntryDao.findByProduct(product)
                .map(stockEntry -> stockEntry.getQuantity() > 0)
                .orElse(false);
        return Pair.of(product, available);
    }

    public record RegisterProductInCatalogCommand(
            UUID productId,
            String name,
            String description,
            double price
    ) {

    }
}
