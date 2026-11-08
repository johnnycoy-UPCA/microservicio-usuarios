package co.edu.clase.microservicio_usuarios.arquitectura;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArquitecturaHexagonalTest {

    private static final String PAQUETE_BASE =
            "co.edu.clase.microservicio_usuarios";

    private static JavaClasses clases;

    @BeforeAll
    static void importarClases() {
        clases = new ClassFileImporter()
                .importPackages(PAQUETE_BASE);
    }

    @Test
    void dominioNoDebeDependerDeAplicacion() {
        ArchRule regla = noClasses()
                .that()
                .resideInAPackage("..dominio..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..aplicacion..");

        regla.check(clases);
    }

    @Test
    void dominioNoDebeDependerDeInfraestructura() {
        ArchRule regla = noClasses()
                .that()
                .resideInAPackage("..dominio..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..infraestructura..");

        regla.check(clases);
    }

    @Test
    void dominioNoDebeDependerDeSpring() {
        ArchRule regla = noClasses()
                .that()
                .resideInAPackage("..dominio..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence.."
                );

        regla.check(clases);
    }

    @Test
    void aplicacionNoDebeDependerDePersistencia() {
        ArchRule regla = noClasses()
                .that()
                .resideInAPackage("..aplicacion..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage(
                        "..infraestructura.salida.persistencia.."
                )
                .allowEmptyShould(true);

        regla.check(clases);
    }

    @Test
    void controladoresNoDebenDependerDeRepositoriosJpa() {
        ArchRule regla = noClasses()
                .that()
                .resideInAPackage(
                        "..infraestructura.entrada.rest.controlador.."
                )
                .should()
                .dependOnClassesThat()
                .resideInAPackage(
                        "..infraestructura.salida.persistencia.repositorio.."
                )
                .allowEmptyShould(true);

        regla.check(clases);
    }

    @Test
    void casosDeUsoNoDebenDependerDeAdaptadores() {
        ArchRule regla = noClasses()
                .that()
                .resideInAPackage("..dominio.casouso..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage(
                        "..infraestructura.salida.."
                );

        regla.check(clases);
    }
}