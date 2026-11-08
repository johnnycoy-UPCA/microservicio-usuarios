package co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.repositorio;

import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.entidad.RolEntidad;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolJpaRepositorio
        extends JpaRepository<RolEntidad, Long> {

    Optional<RolEntidad> findByNombreAndActivoTrue(
            PerfilUsuario nombre);
}