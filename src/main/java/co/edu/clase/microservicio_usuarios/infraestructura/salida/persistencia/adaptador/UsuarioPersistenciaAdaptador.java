package co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.entidad.UsuarioEntidad;
import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.mapeador.UsuarioPersistenciaMapeador;
import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.repositorio.UsuarioJpaRepositorio;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UsuarioPersistenciaAdaptador
        implements UsuarioPersistenciaPuerto {

    private final UsuarioJpaRepositorio usuarioJpaRepositorio;
    private final UsuarioPersistenciaMapeador usuarioPersistenciaMapeador;

    public UsuarioPersistenciaAdaptador(
            UsuarioJpaRepositorio usuarioJpaRepositorio,
            UsuarioPersistenciaMapeador usuarioPersistenciaMapeador) {

        this.usuarioJpaRepositorio = usuarioJpaRepositorio;
        this.usuarioPersistenciaMapeador =
                usuarioPersistenciaMapeador;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCorreo(String correo) {
        return usuarioJpaRepositorio.existsByCorreoIgnoreCase(correo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDocumentoIdentidad(
            String documentoIdentidad) {

        return usuarioJpaRepositorio
                .existsByDocumentoIdentidad(documentoIdentidad);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        UsuarioEntidad entidad =
                usuarioPersistenciaMapeador.aEntidad(usuario);

        UsuarioEntidad entidadGuardada =
                usuarioJpaRepositorio.save(entidad);

        return usuarioPersistenciaMapeador.aModelo(entidadGuardada);
    }
}