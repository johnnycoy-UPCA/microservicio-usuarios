package co.edu.clase.microservicio_usuarios.infraestructura.configuracion;

import co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad.FiltroApiKeyInterna;
import co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad.FiltroAutenticacionJwt;
import co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad.ManejadorAccesoDenegado;
import co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad.PuntoEntradaNoAutorizado;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("desarrollo")
public class SeguridadDesarrolloConfiguracion {

    private final FiltroAutenticacionJwt filtroAutenticacionJwt;
	private final FiltroApiKeyInterna filtroApiKeyInterna;
    private final PuntoEntradaNoAutorizado puntoEntradaNoAutorizado;
    private final ManejadorAccesoDenegado manejadorAccesoDenegado;

    public SeguridadDesarrolloConfiguracion(
            FiltroAutenticacionJwt filtroAutenticacionJwt,
			FiltroApiKeyInterna filtroApiKeyInterna,
            PuntoEntradaNoAutorizado puntoEntradaNoAutorizado,
            ManejadorAccesoDenegado manejadorAccesoDenegado) {

        this.filtroAutenticacionJwt = filtroAutenticacionJwt;
		this.filtroApiKeyInterna = filtroApiKeyInterna;
        this.puntoEntradaNoAutorizado = puntoEntradaNoAutorizado;
        this.manejadorAccesoDenegado = manejadorAccesoDenegado;
    }

    @Bean
    public SecurityFilterChain filtroSeguridadDesarrollo(
            HttpSecurity seguridadHttp) throws Exception {

        seguridadHttp
                .csrf(csrf -> csrf
					.ignoringRequestMatchers(
						"/h2-console/**",
						ConstantesApi.AUTENTICACION,
						ConstantesApi.API_USUARIOS
						+ ConstantesApi.PROPIETARIOS,
						ConstantesApi.API_USUARIOS
						+ ConstantesApi.CLIENTES,
						ConstantesApi.API_USUARIOS
        				+ ConstantesApi.EMPLEADOS
					)
                )
                .headers(encabezados -> encabezados
					.frameOptions(frameOptions ->
						frameOptions.sameOrigin()
					)
                )
                .sessionManagement(sesion -> sesion
					.sessionCreationPolicy(
						SessionCreationPolicy.STATELESS
					)
                )
                .exceptionHandling(excepciones -> excepciones
					.authenticationEntryPoint(
						puntoEntradaNoAutorizado
					)
					.accessDeniedHandler(
						manejadorAccesoDenegado
					)
                )
                .authorizeHttpRequests(autorizacion -> autorizacion
					.requestMatchers(
						"/h2-console/**",
						"/swagger-ui.html",
						"/swagger-ui/**",
						"/v3/api-docs/**",
						ConstantesApi.AUTENTICACION,
						ConstantesApi.API_USUARIOS
						+ ConstantesApi.CLIENTES
					)
					.permitAll()
					.requestMatchers(
							HttpMethod.GET,
							ConstantesApi.API_USUARIOS
									+ "/*/contacto"
					)
					.hasRole("INTERNAL")
					.requestMatchers(
						ConstantesApi.API_USUARIOS
						+ ConstantesApi.PROPIETARIOS
					)
					.hasRole(
						PerfilUsuario.ADMINISTRADOR.name()
					)
					.requestMatchers(
						"/api/v1/usuarios/*/validacion"
					// ).permitAll()
					)
					.hasRole(PerfilUsuario.ADMINISTRADOR.name())
					.requestMatchers(
						ConstantesApi.API_USUARIOS
						+ ConstantesApi.EMPLEADOS
					)
					.hasRole(
						PerfilUsuario.PROPIETARIO.name()
					)
					.requestMatchers(
						HttpMethod.GET,
						ConstantesApi.API_USUARIOS
						+ "/*/validacion-empleado"
					)
					.hasRole(
						PerfilUsuario.EMPLEADO.name()
					)
					.anyRequest()
					.authenticated()
                )
				.addFilterBefore(
						filtroApiKeyInterna,
						// FiltroAutenticacionJwt.class
						UsernamePasswordAuthenticationFilter.class
				)
				.addFilterBefore(
						filtroAutenticacionJwt,
						UsernamePasswordAuthenticationFilter.class
				);
        return seguridadHttp.build();
    }
}