package co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad;

import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
// import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class PuntoEntradaNoAutorizado
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public PuntoEntradaNoAutorizado(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            AuthenticationException excepcion)
            throws IOException {

        respuesta.setStatus(
			HttpServletResponse.SC_UNAUTHORIZED
        );
        respuesta.setContentType(
			MediaType.APPLICATION_JSON_VALUE
        );
        respuesta.setCharacterEncoding("UTF-8");

        String codigo = obtenerAtributo(
			solicitud,
			ConstantesSeguridad.ATRIBUTO_CODIGO_ERROR,
			CodigosError.TOKEN_AUSENTE
        );

        String mensaje = obtenerAtributo(
			solicitud,
			ConstantesSeguridad.ATRIBUTO_MENSAJE_ERROR,
			MensajesError.TOKEN_AUSENTE
        );

        Map<String, Object> cuerpo = Map.of(
			"fechaHora", LocalDateTime.now().toString(),
			"estado", HttpServletResponse.SC_UNAUTHORIZED,
			// "codigo", CodigosError.TOKEN_AUSENTE,
			// "mensaje", MensajesError.TOKEN_AUSENTE,
			"codigo", codigo,
			"mensaje", mensaje,
			"ruta", solicitud.getRequestURI(),
			"detalles", Map.of()
        );

        objectMapper.writeValue(
			respuesta.getOutputStream(),
			cuerpo
        );
    }

	private String obtenerAtributo(
			HttpServletRequest solicitud,
			String nombreAtributo,
			String valorPredeterminado) {

		Object valor = solicitud.getAttribute(nombreAtributo);

		if (valor instanceof String texto
			&& !texto.isBlank()) {

			return texto;
		}

		return valorPredeterminado;
	}
}