package co.edu.clase.microservicio_usuarios.infraestructura.configuracion;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguracion {

    @Bean
    public OpenAPI documentacionApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Usuarios")
                        .description(
                                "API para la gestión de usuarios, "
                                        + "perfiles y autenticación de la "
                                        + "plazoleta de comidas"
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Johnny Coy Herrera")
                        )
                );
    }
}