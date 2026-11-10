package co.edu.clase.microservicio_usuarios.aplicacion.handler;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.AutenticacionRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.AutenticacionSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.mapeador.AutenticacionDtoMapeador;
import co.edu.clase.microservicio_usuarios.dominio.api.AutenticarUsuarioServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Autenticacion;
import org.springframework.stereotype.Component;

@Component
public class AutenticarUsuarioHandler {

    private final AutenticarUsuarioServicioPuerto
            autenticarUsuarioServicioPuerto;

    private final AutenticacionDtoMapeador
            autenticacionDtoMapeador;

    public AutenticarUsuarioHandler(
            AutenticarUsuarioServicioPuerto
                    autenticarUsuarioServicioPuerto,
            AutenticacionDtoMapeador
                    autenticacionDtoMapeador) {

        this.autenticarUsuarioServicioPuerto =
                autenticarUsuarioServicioPuerto;

        this.autenticacionDtoMapeador =
                autenticacionDtoMapeador;
    }

    public AutenticacionRespuestaDto ejecutar(
            AutenticacionSolicitudDto solicitud) {

        Autenticacion autenticacion =
                autenticarUsuarioServicioPuerto.autenticar(
                        solicitud.getCorreo(),
                        solicitud.getClave()
                );

        return autenticacionDtoMapeador.aRespuesta(
                autenticacion
        );
    }
}