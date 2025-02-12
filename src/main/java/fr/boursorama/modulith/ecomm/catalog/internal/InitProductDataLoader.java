package fr.boursorama.modulith.ecomm.catalog.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@Transactional
public class InitProductDataLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitProductDataLoader.class);

    private final ProductDao productDao;

    @Autowired
    public InitProductDataLoader(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        productDao.saveAllAndFlush(List.of(product1(), product2()));
        logger.info("Product initialized");
    }

    private Product product1() {
        final Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setName("Livre A");
        product.setDescription("Un superbe polar");
        product.setPrice(25.0);
        return product;
    }

    private Product product2() {
        final Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setName("Livre B");
        product.setDescription("Une pièce de théâtre assez longue mais très drôle !");
        product.setPrice(10.0);
        return product;
    }
}
