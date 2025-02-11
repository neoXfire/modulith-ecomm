package fr.boursorama.modulith.ecomm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class ModulithEcommApplicationTests {

    @Test
    @DisplayName("Ensure the complete application context is loaded")
    @Disabled
    void contextLoads() {
    }

    @Test
    void ensureModularityRules() {
        ApplicationModules modules = ApplicationModules.of(ModulithEcommApplication.class);
        modules.forEach(System.out::println);
        modules.verify();
    }


}
