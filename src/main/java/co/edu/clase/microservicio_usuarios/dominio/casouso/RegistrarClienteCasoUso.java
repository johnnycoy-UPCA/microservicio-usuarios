package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.api.RegistrarClienteServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.CifradorClavePuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.RolPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

public class RegistrarClienteCasoUso
        implements RegistrarClienteServicioPuerto {

    private final UsuarioPersistenciaPuerto
            usuarioPersistenciaPuerto;

    private final RolPersistenciaPuerto
            rolPersistenciaPuerto;

    private final CifradorClavePuerto
            cifradorClavePuerto;

    private final Clock reloj;

    public RegistrarClienteCasoUso(
            UsuarioPersistenciaPuerto
                    usuarioPersistenciaPuerto,
            RolPersistenciaPuerto
                    rolPersistenciaPuerto,
            CifradorClavePuerto
                    cifradorClavePuerto,
            Clock reloj) {

        this.usuarioPersistenciaPuerto =
                usuarioPersistenciaPuerto;

        this.rolPersistenciaPuerto =
                rolPersistenciaPuerto;

        this.cifradorClavePuerto =
                cifradorClavePuerto;

        this.reloj = reloj;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        normalizarCorreo(usuario);
        validarCorreoDuplicado(usuario.getCorreo());
        validarDocumentoDuplicado(
                usuario.getDocumentoIdentidad()
        );
        validarClave(usuario.getClave());

        Rol rolCliente = obtenerRolCliente();
        LocalDateTime fechaActual =
                LocalDateTime.now(reloj);

        usuario.setRol(rolCliente);
        usuario.setClave(
                cifradorClavePuerto.cifrar(
                        usuario.getClave()
                )
        );
        usuario.setRestauranteId(null);
        usuario.setActivo(Boolean.TRUE);
        usuario.setFechaCreacion(fechaActual);
        usuario.setFechaActualizacion(fechaActual);

        return usuarioPersistenciaPuerto.guardar(usuario);
    }

    private void normalizarCorreo(Usuario usuario) {
        if (usuario.getCorreo() != null) {
            usuario.setCorreo(
                    usuario.getCorreo()
                            .trim()
                            .toLowerCase(Locale.ROOT)
            );
        }
    }

    private void validarCorreoDuplicado(String correo) {
        if (usuarioPersistenciaPuerto
                .existePorCorreo(correo)) {

            throw new ExcepcionUsuarios(
                    CodigosError.CORREO_YA_REGISTRADO,
                    MensajesError.CORREO_YA_REGISTRADO,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void validarDocumentoDuplicado(
            String documentoIdentidad) {

        if (usuarioPersistenciaPuerto
                .existePorDocumentoIdentidad(
                        documentoIdentidad
                )) {

            throw new ExcepcionUsuarios(
                    CodigosError.DOCUMENTO_YA_REGISTRADO,
                    MensajesError.DOCUMENTO_YA_REGISTRADO,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void validarClave(String clave) {
        if (clave == null || clave.isBlank()) {
            throw new ExcepcionUsuarios(
                    CodigosError.CLAVE_OBLIGATORIA,
                    MensajesError.CLAVE_OBLIGATORIA,
                    CategoriaError.VALIDACION
            );
        }
    }

    private Rol obtenerRolCliente() {
        return rolPersistenciaPuerto
                .buscarPorNombre(PerfilUsuario.CLIENTE)
                .orElseThrow(() -> new ExcepcionUsuarios(
                        CodigosError.ROL_CLIENTE_NO_CONFIGURADO,
                        MensajesError.ROL_CLIENTE_NO_CONFIGURADO,
                        CategoriaError.ERROR_INTERNO
                ));
    }
}