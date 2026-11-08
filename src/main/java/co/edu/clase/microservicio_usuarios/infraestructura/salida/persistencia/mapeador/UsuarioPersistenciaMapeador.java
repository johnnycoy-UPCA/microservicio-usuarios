package co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.mapeador;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.entidad.UsuarioEntidad;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenciaMapeador {

    private final RolPersistenciaMapeador rolPersistenciaMapeador;

    public UsuarioPersistenciaMapeador(
            RolPersistenciaMapeador rolPersistenciaMapeador) {

        this.rolPersistenciaMapeador = rolPersistenciaMapeador;
    }

    public UsuarioEntidad aEntidad(Usuario modelo) {
        if (modelo == null) {
            return null;
        }

        UsuarioEntidad entidad = new UsuarioEntidad();

        entidad.setId(modelo.getId());
        entidad.setNombre(modelo.getNombre());
        entidad.setApellido(modelo.getApellido());
        entidad.setDocumentoIdentidad(
                modelo.getDocumentoIdentidad()
        );
        entidad.setCelular(modelo.getCelular());
        entidad.setFechaNacimiento(modelo.getFechaNacimiento());
        entidad.setCorreo(modelo.getCorreo());
        entidad.setClave(modelo.getClave());
        entidad.setRol(
                rolPersistenciaMapeador.aEntidad(modelo.getRol())
        );
        entidad.setRestauranteId(modelo.getRestauranteId());
        entidad.setActivo(modelo.getActivo());
        entidad.setFechaCreacion(modelo.getFechaCreacion());
        entidad.setFechaActualizacion(
                modelo.getFechaActualizacion()
        );

        return entidad;
    }

    public Usuario aModelo(UsuarioEntidad entidad) {
        if (entidad == null) {
            return null;
        }

        return new Usuario(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getApellido(),
                entidad.getDocumentoIdentidad(),
                entidad.getCelular(),
                entidad.getFechaNacimiento(),
                entidad.getCorreo(),
                entidad.getClave(),
                rolPersistenciaMapeador.aModelo(entidad.getRol()),
                entidad.getRestauranteId(),
                entidad.getActivo(),
                entidad.getFechaCreacion(),
                entidad.getFechaActualizacion()
        );
    }
}