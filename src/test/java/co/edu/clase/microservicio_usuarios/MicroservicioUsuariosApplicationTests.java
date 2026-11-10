package co.edu.clase.microservicio_usuarios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import co.edu.clase.microservicio_usuarios.configuracion.SeguridadPruebaConfiguracion;

@Import (SeguridadPruebaConfiguracion.class)
@ActiveProfiles ("prueba")
@SpringBootTest
class MicroservicioUsuariosApplicationTests {

	@Test
	void contextLoads() {
	}

}
