package co.edu.clase.microservicio_usuarios.aplicacion.handler;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.ContactoClienteRespuestaDto;
import co.edu.clase.microservicio_usuarios.dominio.api.ConsultarContactoClienteServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.modelo.ContactoCliente;
import org.springframework.stereotype.Component;

@Component
public class ConsultarContactoClienteHandler {

    private final ConsultarContactoClienteServicioPuerto
            consultarContactoClienteServicioPuerto;

    public ConsultarContactoClienteHandler(
            ConsultarContactoClienteServicioPuerto
                    consultarContactoClienteServicioPuerto) {

        this.consultarContactoClienteServicioPuerto =
                consultarContactoClienteServicioPuerto;
    }

    public ContactoClienteRespuestaDto ejecutar(
            Long usuarioId) {

        ContactoCliente contacto =
                consultarContactoClienteServicioPuerto
                        .consultar(usuarioId);

        return new ContactoClienteRespuestaDto(
                contacto.getUsuarioId(),
                contacto.getCelular(),
                contacto.getActivo()
        );
    }
}