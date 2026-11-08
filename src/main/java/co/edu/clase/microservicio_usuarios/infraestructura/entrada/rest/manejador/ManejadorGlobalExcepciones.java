package co.edu.clase.microservicio_usuarios.infraestructura.entrada.rest.manejador;

import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.CategoriaError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(ExcepcionUsuarios.class)
    public ResponseEntity<ErrorRespuesta> manejarExcepcionUsuarios(
            ExcepcionUsuarios excepcion,
            HttpServletRequest solicitud) {

        HttpStatus estadoHttp =
                obtenerEstadoHttp(excepcion.getCategoria());

        ErrorRespuesta respuesta = construirRespuesta(
                estadoHttp,
                excepcion.getCodigo(),
                excepcion.getMessage(),
                solicitud.getRequestURI(),
                excepcion.getDetalles()
        );

        return ResponseEntity
                .status(estadoHttp)
                .body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> manejarValidacionDto(
            MethodArgumentNotValidException excepcion,
            HttpServletRequest solicitud) {

        Map<String, Object> detalles = new LinkedHashMap<>();

        excepcion.getBindingResult()
                .getFieldErrors()
                .forEach(error -> detalles.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                ));

        ErrorRespuesta respuesta = construirRespuesta(
                HttpStatus.BAD_REQUEST,
                CodigosError.SOLICITUD_INVALIDA,
                MensajesError.SOLICITUD_INVALIDA,
                solicitud.getRequestURI(),
                detalles
        );

        return ResponseEntity
                .badRequest()
                .body(respuesta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> manejarCuerpoIlegible(
            HttpMessageNotReadableException excepcion,
            HttpServletRequest solicitud) {

        ErrorRespuesta respuesta = construirRespuesta(
                HttpStatus.BAD_REQUEST,
                CodigosError.CUERPO_SOLICITUD_INVALIDO,
                MensajesError.CUERPO_SOLICITUD_INVALIDO,
                solicitud.getRequestURI(),
                Map.of()
        );

        return ResponseEntity
                .badRequest()
                .body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarErrorInesperado(
            Exception excepcion,
            HttpServletRequest solicitud) {

        ErrorRespuesta respuesta = construirRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                CodigosError.ERROR_INTERNO,
                MensajesError.ERROR_INTERNO,
                solicitud.getRequestURI(),
                Map.of()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(respuesta);
    }

    private ErrorRespuesta construirRespuesta(
            HttpStatus estadoHttp,
            String codigo,
            String mensaje,
            String ruta,
            Map<String, Object> detalles) {

        return new ErrorRespuesta(
                LocalDateTime.now(),
                estadoHttp.value(),
                codigo,
                mensaje,
                ruta,
                detalles
        );
    }

    private HttpStatus obtenerEstadoHttp(
            CategoriaError categoria) {

        return switch (categoria) {
            case VALIDACION -> HttpStatus.BAD_REQUEST;
            case NO_ENCONTRADO -> HttpStatus.NOT_FOUND;
            case CONFLICTO -> HttpStatus.CONFLICT;
            case NO_AUTORIZADO -> HttpStatus.UNAUTHORIZED;
            case ACCESO_DENEGADO -> HttpStatus.FORBIDDEN;
            case DEPENDENCIA_EXTERNA -> HttpStatus.BAD_GATEWAY;
            case ERROR_INTERNO -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}