package co.edu.clase.microservicio_usuarios.aplicacion.mapeador;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.UsuarioRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.CrearPropietarioSolicitudDto;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDtoMapeador {

    public Usuario aModelo(
            CrearPropietarioSolicitudDto solicitud) {

        Usuario usuario = new Usuario();

        usuario.setNombre(solicitud.getNombre());
        usuario.setApellido(solicitud.getApellido());
        usuario.setDocumentoIdentidad(
                solicitud.getDocumentoIdentidad()
        );
        usuario.setCelular(solicitud.getCelular());
        usuario.setFechaNacimiento(solicitud.getFechaNacimiento());
        usuario.setCorreo(solicitud.getCorreo());
        usuario.setClave(solicitud.getClave());

        return usuario;
    }

    public UsuarioRespuestaDto aRespuesta(Usuario usuario) {
        UsuarioRespuestaDto respuesta =
                new UsuarioRespuestaDto();

        respuesta.setId(usuario.getId());
        respuesta.setNombre(usuario.getNombre());
        respuesta.setApellido(usuario.getApellido());
        respuesta.setDocumentoIdentidad(
                usuario.getDocumentoIdentidad()
        );
        respuesta.setCelular(usuario.getCelular());
        respuesta.setFechaNacimiento(usuario.getFechaNacimiento());
        respuesta.setCorreo(usuario.getCorreo());
        respuesta.setPerfil(usuario.getRol().getNombre());
        respuesta.setActivo(usuario.getActivo());
        respuesta.setFechaCreacion(usuario.getFechaCreacion());

        return respuesta;
    }
}