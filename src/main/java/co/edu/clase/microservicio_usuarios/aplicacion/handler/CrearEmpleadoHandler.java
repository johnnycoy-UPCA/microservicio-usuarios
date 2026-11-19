package co.edu.clase.microservicio_usuarios.aplicacion.handler;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.UsuarioRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.CrearEmpleadoSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.mapeador.UsuarioDtoMapeador;
import co.edu.clase.microservicio_usuarios.dominio.api.CrearEmpleadoServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import org.springframework.stereotype.Component;

@Component
public class CrearEmpleadoHandler {

    private final CrearEmpleadoServicioPuerto
            crearEmpleadoServicioPuerto;

    private final UsuarioDtoMapeador usuarioDtoMapeador;

    public CrearEmpleadoHandler(
            CrearEmpleadoServicioPuerto
                    crearEmpleadoServicioPuerto,
            UsuarioDtoMapeador usuarioDtoMapeador) {

        this.crearEmpleadoServicioPuerto =
                crearEmpleadoServicioPuerto;
        this.usuarioDtoMapeador = usuarioDtoMapeador;
    }

    public UsuarioRespuestaDto ejecutar(
            CrearEmpleadoSolicitudDto solicitud,
            String token) {

        Usuario empleado =
                usuarioDtoMapeador.aModelo(solicitud);

        Usuario creado =
                crearEmpleadoServicioPuerto.crear(
                        empleado,
                        token
                );

        return usuarioDtoMapeador.aRespuesta(creado);
    }
}