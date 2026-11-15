package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.api.ConsultarUsuarioServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.modelo.UsuarioConsulta;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.CategoriaError;

public class ConsultarUsuarioCasoUso
        implements ConsultarUsuarioServicioPuerto {

    private final UsuarioPersistenciaPuerto
        usuarioPersistenciaPuerto;

    public ConsultarUsuarioCasoUso(
        UsuarioPersistenciaPuerto usuarioPersistenciaPuerto) {

        this.usuarioPersistenciaPuerto =
            usuarioPersistenciaPuerto;
    }

    @Override
    public UsuarioConsulta consultarPorId(Long usuarioId) {
        Usuario usuario = usuarioPersistenciaPuerto
            .buscarPorId(usuarioId)
            .orElseThrow(() -> new ExcepcionUsuarios(
                CodigosError.USUARIO_NO_ENCONTRADO,
                MensajesError.USUARIO_NO_ENCONTRADO,
                CategoriaError.NO_ENCONTRADO
            ));

        return new UsuarioConsulta(
            usuario.getId(),
            usuario.getRol().getNombre(),
            usuario.getActivo()
        );
    }
}