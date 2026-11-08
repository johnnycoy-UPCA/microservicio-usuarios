package co.edu.clase.microservicio_usuarios.infraestructura.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesApi;

@Configuration
@Profile("desarrollo")
public class SeguridadDesarrolloConfiguracion {

	@Bean
	public SecurityFilterChain filtroSeguridadDesarrollo(
			HttpSecurity seguridadHttp) throws Exception {

		seguridadHttp
			// .authorizeHttpRequests(autorizacion -> autorizacion
			// .requestMatchers("/h2-console/**").permitAll()
			// .anyRequest().authenticated()
			// )
			.authorizeHttpRequests(autorizacion -> autorizacion
				.requestMatchers(
					"/h2-console/**",
					"/swagger-ui.html",
					"/swagger-ui/**",
					"/v3/api-docs/**"
				)
				.permitAll()
				.requestMatchers(
						ConstantesApi.API_USUARIOS
								+ ConstantesApi.PROPIETARIOS)
				.permitAll()
				.anyRequest().authenticated()
			)
			.csrf(csrf -> csrf
				.ignoringRequestMatchers(
					"/h2-console/**",
					ConstantesApi.API_USUARIOS
							+ ConstantesApi.PROPIETARIOS
				)
			)
			.headers(encabezados -> encabezados
				.frameOptions(frameOptions -> frameOptions.sameOrigin())
			)
			.httpBasic(Customizer.withDefaults());

		return seguridadHttp.build();
	}
}