package co.edu.clase.microservicio_usuarios.infraestructura.configuracion;

import co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad.FiltroAutenticacionJwt;
import co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad.ManejadorAccesoDenegado;
import co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad.PuntoEntradaNoAutorizado;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("desarrollo")
public class SeguridadDesarrolloConfiguracion {

    private final FiltroAutenticacionJwt filtroAutenticacionJwt;
    private final PuntoEntradaNoAutorizado
            puntoEntradaNoAutorizado;
    private final ManejadorAccesoDenegado
            manejadorAccesoDenegado;

    public SeguridadDesarrolloConfiguracion(
            FiltroAutenticacionJwt filtroAutenticacionJwt,
            PuntoEntradaNoAutorizado puntoEntradaNoAutorizado,
            ManejadorAccesoDenegado manejadorAccesoDenegado) {

        this.filtroAutenticacionJwt =
                filtroAutenticacionJwt;
        this.puntoEntradaNoAutorizado =
                puntoEntradaNoAutorizado;
        this.manejadorAccesoDenegado =
                manejadorAccesoDenegado;
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
                                        + ConstantesApi.PROPIETARIOS
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
                                ConstantesApi.AUTENTICACION
                        ).permitAll()
                        .requestMatchers(
                                ConstantesApi.API_USUARIOS
                                        + ConstantesApi.PROPIETARIOS
                        ).hasRole(
                                PerfilUsuario.ADMINISTRADOR.name()
                        )
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                        filtroAutenticacionJwt,
                        UsernamePasswordAuthenticationFilter.class
                );

        return seguridadHttp.build();
    }
}