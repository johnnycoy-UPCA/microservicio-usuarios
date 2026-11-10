package co.edu.clase.microservicio_usuarios.dominio.spi;

import java.util.Optional;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;

public interface UsuarioPersistenciaPuerto {

    boolean existePorCorreo(String correo);

    boolean existePorDocumentoIdentidad(String documentoIdentidad);

    Optional<Usuario> buscarPorCorreo(String correo);

    Usuario guardar(Usuario usuario);
}