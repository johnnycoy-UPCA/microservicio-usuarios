package co.edu.clase.microservicio_usuarios.aplicacion.mapeador;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.AutenticacionRespuestaDto;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Autenticacion;
import org.springframework.stereotype.Component;

@Component
public class AutenticacionDtoMapeador {

    public AutenticacionRespuestaDto aRespuesta(
            Autenticacion autenticacion) {

        AutenticacionRespuestaDto respuesta =
                new AutenticacionRespuestaDto();

        respuesta.setToken(autenticacion.getToken());
        respuesta.setTipo(autenticacion.getTipo());
        respuesta.setDuracionSegundos(
                autenticacion.getDuracionSegundos()
        );
        respuesta.setUsuarioId(autenticacion.getUsuarioId());
        respuesta.setNombre(autenticacion.getNombre());
        respuesta.setCorreo(autenticacion.getCorreo());
        respuesta.setPerfil(autenticacion.getPerfil());

        return respuesta;
    }
}