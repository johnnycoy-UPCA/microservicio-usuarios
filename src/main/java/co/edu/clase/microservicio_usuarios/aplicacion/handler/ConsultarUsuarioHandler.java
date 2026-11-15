package co.edu.clase.microservicio_usuarios.aplicacion.handler;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.UsuarioConsultaRespuestaDto;
import co.edu.clase.microservicio_usuarios.dominio.api.ConsultarUsuarioServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.modelo.UsuarioConsulta;
import org.springframework.stereotype.Component;

@Component
public class ConsultarUsuarioHandler {

    private final ConsultarUsuarioServicioPuerto
        consultarUsuarioServicioPuerto;

    public ConsultarUsuarioHandler(
        ConsultarUsuarioServicioPuerto
            consultarUsuarioServicioPuerto) {

        this.consultarUsuarioServicioPuerto =
            consultarUsuarioServicioPuerto;
    }

    public UsuarioConsultaRespuestaDto ejecutar(
            Long usuarioId) {

        UsuarioConsulta usuario =
            consultarUsuarioServicioPuerto
                .consultarPorId(usuarioId);

        return new UsuarioConsultaRespuestaDto(
            usuario.getId(),
            usuario.getPerfil(),
            usuario.getActivo()
        );
    }
}