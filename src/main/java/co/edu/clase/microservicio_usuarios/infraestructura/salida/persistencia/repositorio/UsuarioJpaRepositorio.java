package co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.repositorio;

import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.entidad.UsuarioEntidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioJpaRepositorio
        extends JpaRepository<UsuarioEntidad, Long> {

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByDocumentoIdentidad(String documentoIdentidad);
}