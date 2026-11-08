package co.edu.clase.microservicio_usuarios.aplicacion.handler;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.UsuarioRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.CrearPropietarioSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.mapeador.UsuarioDtoMapeador;
import co.edu.clase.microservicio_usuarios.dominio.api.CrearPropietarioServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import org.springframework.stereotype.Component;

@Component
public class CrearPropietarioHandler {

    private final CrearPropietarioServicioPuerto crearPropietarioServicioPuerto;
    private final UsuarioDtoMapeador usuarioDtoMapeador;

    public CrearPropietarioHandler(
            CrearPropietarioServicioPuerto crearPropietarioServicioPuerto,
            UsuarioDtoMapeador usuarioDtoMapeador) {

        this.crearPropietarioServicioPuerto =
                crearPropietarioServicioPuerto;
        this.usuarioDtoMapeador = usuarioDtoMapeador;
    }

    public UsuarioRespuestaDto ejecutar(
            CrearPropietarioSolicitudDto solicitud) {

        Usuario usuario = usuarioDtoMapeador.aModelo(solicitud);

        Usuario usuarioCreado =
                crearPropietarioServicioPuerto.crear(usuario);

        return usuarioDtoMapeador.aRespuesta(usuarioCreado);
    }
}