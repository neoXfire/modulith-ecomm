package fr.boursorama.modulith.ecomm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import org.springframework.modulith.docs.Documenter.CanvasOptions;
import org.springframework.modulith.docs.Documenter.DiagramOptions;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
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

    @Test
    void generateDocumentation() {
        ApplicationModules modules = ApplicationModules.of(ModulithEcommApplication.class);
        Documenter documenter = new Documenter(modules);
        DiagramOptions diagramOptions = DiagramOptions.defaults();
        CanvasOptions canvasOptions = CanvasOptions.defaults();
        documenter.writeDocumentation(diagramOptions, canvasOptions);
    }


}
