package co.edu.clase.microservicio_usuarios.aplicacion.handler;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.UsuarioRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.RegistrarClienteSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.mapeador.UsuarioDtoMapeador;
import co.edu.clase.microservicio_usuarios.dominio.api.RegistrarClienteServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import org.springframework.stereotype.Component;

@Component
public class RegistrarClienteHandler {

    private final RegistrarClienteServicioPuerto
            registrarClienteServicioPuerto;

    private final UsuarioDtoMapeador
            usuarioDtoMapeador;

    public RegistrarClienteHandler(
            RegistrarClienteServicioPuerto
                    registrarClienteServicioPuerto,
            UsuarioDtoMapeador usuarioDtoMapeador) {

        this.registrarClienteServicioPuerto =
                registrarClienteServicioPuerto;

        this.usuarioDtoMapeador = usuarioDtoMapeador;
    }

    public UsuarioRespuestaDto ejecutar(
            RegistrarClienteSolicitudDto solicitud) {

        Usuario usuario =
                usuarioDtoMapeador.aModelo(solicitud);

        Usuario clienteRegistrado =
                registrarClienteServicioPuerto.registrar(
                        usuario
                );

        return usuarioDtoMapeador.aRespuesta(
                clienteRegistrado
        );
    }
}