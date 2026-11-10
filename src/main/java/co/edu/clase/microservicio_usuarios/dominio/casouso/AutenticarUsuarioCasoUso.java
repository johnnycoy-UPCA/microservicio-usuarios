package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.api.AutenticarUsuarioServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Autenticacion;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.CifradorClavePuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.TokenSeguridadPuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.CategoriaError;

import java.util.Locale;

public class AutenticarUsuarioCasoUso
        implements AutenticarUsuarioServicioPuerto {

    private final UsuarioPersistenciaPuerto usuarioPersistenciaPuerto;
    private final CifradorClavePuerto cifradorClavePuerto;
    private final TokenSeguridadPuerto tokenSeguridadPuerto;

    public AutenticarUsuarioCasoUso(
            UsuarioPersistenciaPuerto usuarioPersistenciaPuerto,
            CifradorClavePuerto cifradorClavePuerto,
            TokenSeguridadPuerto tokenSeguridadPuerto) {

        this.usuarioPersistenciaPuerto = usuarioPersistenciaPuerto;
        this.cifradorClavePuerto = cifradorClavePuerto;
        this.tokenSeguridadPuerto = tokenSeguridadPuerto;
    }

    @Override
    public Autenticacion autenticar(
            String correo,
            String clave) {

        String correoNormalizado = normalizarCorreo(correo);

        Usuario usuario = usuarioPersistenciaPuerto
            .buscarPorCorreo(correoNormalizado)
            .orElseThrow(this::crearExcepcionCredenciales);

        validarUsuarioActivo(usuario);
        validarClave(clave, usuario.getClave());

        String token = tokenSeguridadPuerto.generar(usuario);

        return construirAutenticacion(usuario, token);
    }

    private String normalizarCorreo(String correo) {
        if (correo == null) {
            return null;
        }

        return correo
            .trim()
            .toLowerCase(Locale.ROOT);
    }

    private void validarUsuarioActivo(Usuario usuario) {
        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ExcepcionUsuarios(
                CodigosError.USUARIO_INACTIVO,
                MensajesError.USUARIO_INACTIVO,
                CategoriaError.NO_AUTORIZADO
            );
        }
    }

    private void validarClave(
            String claveSinCifrar,
            String claveCifrada) {

        if (!cifradorClavePuerto.coincide(
            claveSinCifrar,
            claveCifrada
        )) {
            throw crearExcepcionCredenciales();
        }
    }

    private ExcepcionUsuarios crearExcepcionCredenciales() {
        return new ExcepcionUsuarios(
            CodigosError.CREDENCIALES_INVALIDAS,
            MensajesError.CREDENCIALES_INVALIDAS,
            CategoriaError.NO_AUTORIZADO
        );
    }

    private Autenticacion construirAutenticacion(
            Usuario usuario,
            String token) {

        Autenticacion autenticacion = new Autenticacion();

        // autenticacion.setToken(token);
        // autenticacion.setTipo("Bearer");
        // autenticacion.setDuracionSegundos(
        //     ConstantesSeguridad
        //         .DURACION_TOKEN_MILISEGUNDOS / 1000
        // );

        autenticacion.setToken(token);
        autenticacion.setTipo(
                ConstantesSeguridad.TIPO_TOKEN
        );

        autenticacion.setDuracionSegundos(
                tokenSeguridadPuerto.obtenerDuracionSegundos()
        );

        autenticacion.setUsuarioId(usuario.getId());
        autenticacion.setNombre(
            usuario.getNombre() + " " + usuario.getApellido()
        );
        autenticacion.setCorreo(usuario.getCorreo());
        autenticacion.setPerfil(usuario.getRol().getNombre());

        return autenticacion;
    }
}