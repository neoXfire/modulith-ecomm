package fr.boursorama.modulith.ecomm;

import fr.boursorama.modulith.ecomm.catalog.ProductDao;
import fr.boursorama.modulith.ecomm.catalog.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@Transactional
public class InitDataLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitDataLoader.class);

    private final ProductDao productDao;

    private final UserDetailsManager userDetailsManager;

    @Autowired
    public InitDataLoader(ProductDao productDao, UserDetailsManager userDetailsManager) {
        this.productDao = productDao;
        this.userDetailsManager = userDetailsManager;
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserDetails adminUser = adminUser();
        if (!userDetailsManager.userExists(adminUser.getUsername())) {
            userDetailsManager.createUser(adminUser);
        }
        productDao.saveAllAndFlush(List.of(product1(), product2()));
        logger.info("Data initialized");
    }

    private UserDetails adminUser() {
        return User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
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
