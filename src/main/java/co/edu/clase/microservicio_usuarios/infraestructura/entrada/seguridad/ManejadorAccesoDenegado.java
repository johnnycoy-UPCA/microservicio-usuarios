package co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad;

import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
// import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ManejadorAccesoDenegado
        implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public ManejadorAccesoDenegado(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            AccessDeniedException excepcion)
            throws IOException {

        respuesta.setStatus(
                HttpServletResponse.SC_FORBIDDEN
        );
        respuesta.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );
        respuesta.setCharacterEncoding("UTF-8");

        Map<String, Object> cuerpo = Map.of(
                "fechaHora", LocalDateTime.now().toString(),
                "estado", HttpServletResponse.SC_FORBIDDEN,
                "codigo", CodigosError.ACCESO_DENEGADO,
                "mensaje", MensajesError.ACCESO_DENEGADO,
                "ruta", solicitud.getRequestURI(),
                "detalles", Map.of()
        );

        objectMapper.writeValue(
                respuesta.getOutputStream(),
                cuerpo
        );
    }
}