package co.edu.clase.microservicio_usuarios.dominio.spi;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

import java.util.Optional;

public interface RolPersistenciaPuerto {

    Optional<Rol> buscarPorNombre(PerfilUsuario perfilUsuario);
}