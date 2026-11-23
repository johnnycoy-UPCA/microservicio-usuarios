package co.edu.clase.microservicio_usuarios.infraestructura.configuracion;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesSeguridad;

@Configuration
public class OpenApiConfiguracion {

    // private static final String ESQUEMA_SEGURIDAD = "autenticacionBearer";
	private static final String ESQUEMA_AUTENTICACION_BEARER = "autenticacionBearer";

	private static final String ESQUEMA_API_KEY_INTERNA = "apiKeyInterna";

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
			)
			.components(new Components()
				.addSecuritySchemes(
					ESQUEMA_AUTENTICACION_BEARER,
					new SecurityScheme()
						.name(ConstantesSeguridad.ENCABEZADO_AUTORIZACION)
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
				)
                .addSecuritySchemes(
                        ESQUEMA_API_KEY_INTERNA,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(ConstantesSeguridad.ENCABEZADO_API_KEY_INTERNA)
                )
			);
    }
}