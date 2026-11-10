package co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad;

import co.edu.clase.microservicio_usuarios.dominio.spi.TokenSeguridadPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.EstadoToken;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private static final String PREFIJO_AUTORIDAD = "ROLE_";

    private final TokenSeguridadPuerto tokenSeguridadPuerto;

    public FiltroAutenticacionJwt(
		TokenSeguridadPuerto tokenSeguridadPuerto) {

        this.tokenSeguridadPuerto = tokenSeguridadPuerto;
    }

    @Override
    protected void doFilterInternal(
			HttpServletRequest solicitud,
			HttpServletResponse respuesta,
			FilterChain cadenaFiltros)
			throws ServletException, IOException {

        String encabezadoAutorizacion = solicitud.getHeader(
				ConstantesSeguridad.ENCABEZADO_AUTORIZACION
        );

        if (encabezadoAutorizacion == null
				|| !encabezadoAutorizacion.startsWith(
				ConstantesSeguridad.PREFIJO_TOKEN
        )) {
            cadenaFiltros.doFilter(solicitud, respuesta);
            return;
        }

        String token = encabezadoAutorizacion.substring(
			ConstantesSeguridad.PREFIJO_TOKEN.length()
        );

        // if (!tokenSeguridadPuerto.esValido(token)) {
        //     cadenaFiltros.doFilter(solicitud, respuesta);
        //     return;
        // }

		EstadoToken estadoToken =
			tokenSeguridadPuerto.validar(token);

		if (EstadoToken.EXPIRADO.equals(estadoToken)) {
			registrarErrorAutenticacion(
				solicitud,
				CodigosError.TOKEN_EXPIRADO,
				MensajesError.TOKEN_EXPIRADO
			);

			cadenaFiltros.doFilter(solicitud, respuesta);
			return;
		}

		if (EstadoToken.INVALIDO.equals(estadoToken)) {
			registrarErrorAutenticacion(
				solicitud,
				CodigosError.TOKEN_INVALIDO,
				MensajesError.TOKEN_INVALIDO
			);

			cadenaFiltros.doFilter(solicitud, respuesta);
			return;
		}

        establecerAutenticacion(token);
        cadenaFiltros.doFilter(solicitud, respuesta);
    }

    private void establecerAutenticacion(String token) {
        String correo = tokenSeguridadPuerto.extraerCorreo(token);
        Long usuarioId =
			tokenSeguridadPuerto.extraerUsuarioId(token);
        PerfilUsuario perfil =
			tokenSeguridadPuerto.extraerPerfil(token);

        SimpleGrantedAuthority autoridad =
			new SimpleGrantedAuthority(
				PREFIJO_AUTORIDAD + perfil.name()
			);

        UsuarioAutenticado usuarioAutenticado =
			new UsuarioAutenticado(
				usuarioId,
				correo,
				perfil
			);

        UsernamePasswordAuthenticationToken autenticacion =
			new UsernamePasswordAuthenticationToken(
				usuarioAutenticado,
				null,
				List.of(autoridad)
			);

        SecurityContextHolder
			.getContext()
			.setAuthentication(autenticacion);
    }

	private void registrarErrorAutenticacion(
			HttpServletRequest solicitud,
			String codigo,
			String mensaje) {

		solicitud.setAttribute(
				ConstantesSeguridad.ATRIBUTO_CODIGO_ERROR,
				codigo
		);

		solicitud.setAttribute(
				ConstantesSeguridad.ATRIBUTO_MENSAJE_ERROR,
				mensaje
		);
	}
}