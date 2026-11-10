package co.edu.clase.microservicio_usuarios.configuracion;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class SeguridadPruebaConfiguracion {

    @Bean
    public SecurityFilterChain filtroSeguridadPrueba(
            HttpSecurity seguridadHttp) throws Exception {

        seguridadHttp
			.csrf(csrf -> csrf.disable())
			.sessionManagement(sesion -> sesion
				.sessionCreationPolicy(
						SessionCreationPolicy.STATELESS
				)
			)

			.authorizeHttpRequests(autorizacion ->
				autorizacion
					.anyRequest()
					.permitAll()
			);

        return seguridadHttp.build();
    }
}