package co.edu.clase.microservicio_usuarios.dominio.spi;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;

public interface UsuarioPersistenciaPuerto {

    boolean existePorCorreo(String correo);

    boolean existePorDocumentoIdentidad(String documentoIdentidad);

    Usuario guardar(Usuario usuario);
}