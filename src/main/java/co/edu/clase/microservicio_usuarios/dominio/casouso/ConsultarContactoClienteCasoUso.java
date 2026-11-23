package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.api.ConsultarContactoClienteServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.ContactoCliente;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

public class ConsultarContactoClienteCasoUso
        implements ConsultarContactoClienteServicioPuerto {

    private final UsuarioPersistenciaPuerto
            usuarioPersistenciaPuerto;

    public ConsultarContactoClienteCasoUso(
            UsuarioPersistenciaPuerto
                    usuarioPersistenciaPuerto) {

        this.usuarioPersistenciaPuerto =
                usuarioPersistenciaPuerto;
    }

    @Override
    public ContactoCliente consultar(Long usuarioId) {
        Usuario usuario = usuarioPersistenciaPuerto
                .buscarPorId(usuarioId)
                .orElseThrow(() ->
                        new ExcepcionUsuarios(
                                CodigosError.CLIENTE_NO_ENCONTRADO,
                                MensajesError.CLIENTE_NO_ENCONTRADO,
                                CategoriaError.NO_ENCONTRADO
                        )
                );

        validarPerfil(usuario);
        validarActivo(usuario);
        validarCelular(usuario);

        return new ContactoCliente(
                usuario.getId(),
                usuario.getCelular(),
                usuario.getActivo()
        );
    }

    private void validarPerfil(Usuario usuario) {
        boolean perfilInvalido =
                usuario.getRol() == null
                        || !PerfilUsuario.CLIENTE.equals(
                        usuario.getRol().getNombre()
                );

        if (perfilInvalido) {
            throw new ExcepcionUsuarios(
                    CodigosError.USUARIO_NO_ES_CLIENTE,
                    MensajesError.USUARIO_NO_ES_CLIENTE,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarActivo(Usuario usuario) {
        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ExcepcionUsuarios(
                    CodigosError.CLIENTE_INACTIVO,
                    MensajesError.CLIENTE_INACTIVO,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarCelular(Usuario usuario) {
        if (usuario.getCelular() == null
                || usuario.getCelular().isBlank()) {

            throw new ExcepcionUsuarios(
                    CodigosError.CELULAR_CLIENTE_NO_DISPONIBLE,
                    MensajesError.CELULAR_CLIENTE_NO_DISPONIBLE,
                    CategoriaError.VALIDACION
            );
        }
    }
}