package co.edu.clase.microservicio_usuarios.dominio.api;

import co.edu.clase.microservicio_usuarios.dominio.modelo.ContactoCliente;

public interface ConsultarContactoClienteServicioPuerto {

    ContactoCliente consultar(Long usuarioId);
}