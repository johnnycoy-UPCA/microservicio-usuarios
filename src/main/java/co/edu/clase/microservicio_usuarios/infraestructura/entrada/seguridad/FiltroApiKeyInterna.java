package co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad;

import co.edu.clase.microservicio_usuarios.infraestructura.configuracion.SeguridadInternaPropiedades;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@Component
public class FiltroApiKeyInterna
        extends OncePerRequestFilter {

    private final SeguridadInternaPropiedades propiedades;

    public FiltroApiKeyInterna(
            SeguridadInternaPropiedades propiedades) {

        this.propiedades = propiedades;
    }

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest solicitud) {

        String patron = ConstantesApi.API_USUARIOS
                + "/\\d+/contacto";

        boolean esContacto =
                HttpMethod.GET.matches(
                        solicitud.getMethod()
                )
                        && solicitud.getRequestURI()
                        .matches(patron);

        return !esContacto;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            FilterChain cadena)
            throws ServletException, IOException {

        String apiKey = solicitud.getHeader(
                ConstantesSeguridad
                        .ENCABEZADO_API_KEY_INTERNA
        );

        if (!esValida(apiKey)) {
            respuesta.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );
            respuesta.setContentType(
                    "application/json"
            );
            respuesta.setCharacterEncoding("UTF-8");
        //     respuesta.getWriter().write(
        //             "{\"codigo\":\"AUT-007\","
        //                     + "\"mensaje\":"
        //                     + "\"Credencial interna inválida\"}"
        //     );
			respuesta.getWriter().write(
					"{\"codigo\":\""
							+ CodigosError
							.CREDENCIAL_INTERNA_INVALIDA
							+ "\",\"mensaje\":\""
							+ MensajesError
							.CREDENCIAL_INTERNA_INVALIDA
							+ "\"}"
			);
            return;
        }

        UsernamePasswordAuthenticationToken autenticacion =
                new UsernamePasswordAuthenticationToken(
                        "microservicio-plazoleta",
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        ConstantesSeguridad
                                                .AUTORIDAD_INTERNA
                                )
                        )
                );

        SecurityContextHolder.getContext()
                .setAuthentication(autenticacion);

        cadena.doFilter(solicitud, respuesta);
    }

    private boolean esValida(String apiKeyRecibida) {
        if (apiKeyRecibida == null
                || apiKeyRecibida.isBlank()
                || propiedades.getApiKey() == null) {

            return false;
        }

        return MessageDigest.isEqual(
                apiKeyRecibida.getBytes(
                        StandardCharsets.UTF_8
                ),
                propiedades.getApiKey().getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }
}