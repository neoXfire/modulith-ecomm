package fr.boursorama.modulith.ecomm;

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

@Component
@Transactional
public class InitDataLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitDataLoader.class);

    private final UserDetailsManager userDetailsManager;

    @Autowired
    public InitDataLoader(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserDetails adminUser = adminUser();
        if (!userDetailsManager.userExists(adminUser.getUsername())) {
            userDetailsManager.createUser(adminUser);
        }
        logger.info("User data initialized");
    }

    private UserDetails adminUser() {
        return User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
    }

}
