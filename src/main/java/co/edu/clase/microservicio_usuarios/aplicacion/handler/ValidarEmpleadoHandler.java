package co.edu.clase.microservicio_usuarios.aplicacion.handler;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.EmpleadoValidacionRespuestaDto;
import co.edu.clase.microservicio_usuarios.dominio.api.ValidarEmpleadoServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.modelo.EmpleadoValidacion;
import org.springframework.stereotype.Component;

@Component
public class ValidarEmpleadoHandler {

    private final ValidarEmpleadoServicioPuerto
            validarEmpleadoServicioPuerto;

    public ValidarEmpleadoHandler(
            ValidarEmpleadoServicioPuerto
                    validarEmpleadoServicioPuerto) {

        this.validarEmpleadoServicioPuerto =
                validarEmpleadoServicioPuerto;
    }

    public EmpleadoValidacionRespuestaDto ejecutar(
            Long usuarioId,
            Long usuarioAutenticadoId) {

        EmpleadoValidacion resultado =
                validarEmpleadoServicioPuerto.validar(
                        usuarioId,
                        usuarioAutenticadoId
                );

        return new EmpleadoValidacionRespuestaDto(
                resultado.getUsuarioId(),
                resultado.getPerfil(),
                resultado.getRestauranteId(),
                resultado.getActivo()
        );
    }
}