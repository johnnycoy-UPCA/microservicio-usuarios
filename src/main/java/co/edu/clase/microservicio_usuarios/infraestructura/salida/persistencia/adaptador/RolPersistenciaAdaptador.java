package co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.dominio.spi.RolPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.mapeador.RolPersistenciaMapeador;
import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.repositorio.RolJpaRepositorio;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class RolPersistenciaAdaptador
        implements RolPersistenciaPuerto {

    private final RolJpaRepositorio rolJpaRepositorio;
    private final RolPersistenciaMapeador rolPersistenciaMapeador;

    public RolPersistenciaAdaptador(
            RolJpaRepositorio rolJpaRepositorio,
            RolPersistenciaMapeador rolPersistenciaMapeador) {

        this.rolJpaRepositorio = rolJpaRepositorio;
        this.rolPersistenciaMapeador = rolPersistenciaMapeador;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> buscarPorNombre(
            PerfilUsuario perfilUsuario) {

        return rolJpaRepositorio
                .findByNombreAndActivoTrue(perfilUsuario)
                .map(rolPersistenciaMapeador::aModelo);
    }
}