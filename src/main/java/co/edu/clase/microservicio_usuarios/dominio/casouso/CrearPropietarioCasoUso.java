package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.api.CrearPropietarioServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.CifradorClavePuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.RolPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class CrearPropietarioCasoUso
        implements CrearPropietarioServicioPuerto {

    private final UsuarioPersistenciaPuerto usuarioPersistenciaPuerto;
    private final RolPersistenciaPuerto rolPersistenciaPuerto;
    private final CifradorClavePuerto cifradorClavePuerto;
    private final Clock reloj;

    public CrearPropietarioCasoUso(
            UsuarioPersistenciaPuerto usuarioPersistenciaPuerto,
            RolPersistenciaPuerto rolPersistenciaPuerto,
            CifradorClavePuerto cifradorClavePuerto,
            Clock reloj) {

        this.usuarioPersistenciaPuerto = usuarioPersistenciaPuerto;
        this.rolPersistenciaPuerto = rolPersistenciaPuerto;
        this.cifradorClavePuerto = cifradorClavePuerto;
        this.reloj = reloj;
    }

    @Override
    public Usuario crear(Usuario usuario) {
        normalizarCorreo(usuario);
        validarFechaNacimiento(usuario.getFechaNacimiento());
        validarMayoriaEdad(usuario.getFechaNacimiento());
        validarCorreoDuplicado(usuario.getCorreo());
        validarDocumentoDuplicado(usuario.getDocumentoIdentidad());
        validarClave(usuario.getClave());

        Rol rolPropietario = obtenerRolPropietario();
        LocalDateTime fechaActual = LocalDateTime.now(reloj);

        usuario.setRol(rolPropietario);
        usuario.setClave(cifradorClavePuerto.cifrar(usuario.getClave()));
        usuario.setRestauranteId(null);
        usuario.setActivo(Boolean.TRUE);
        usuario.setFechaCreacion(fechaActual);
        usuario.setFechaActualizacion(fechaActual);

        return usuarioPersistenciaPuerto.guardar(usuario);
    }

    private void validarFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new ExcepcionUsuarios(
                    CodigosError.FECHA_NACIMIENTO_OBLIGATORIA,
                    MensajesError.FECHA_NACIMIENTO_OBLIGATORIA,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarMayoriaEdad(LocalDate fechaNacimiento) {
        LocalDate fechaActual = LocalDate.now(reloj);
        int edad = Period.between(fechaNacimiento, fechaActual).getYears();

        if (edad < ConstantesValidacion.EDAD_MINIMA_PROPIETARIO) {
            throw new ExcepcionUsuarios(
                    CodigosError.USUARIO_MENOR_EDAD,
                    MensajesError.USUARIO_MENOR_EDAD,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarCorreoDuplicado(String correo) {
        if (usuarioPersistenciaPuerto.existePorCorreo(correo)) {
            throw new ExcepcionUsuarios(
                    CodigosError.CORREO_YA_REGISTRADO,
                    MensajesError.CORREO_YA_REGISTRADO,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private void validarDocumentoDuplicado(String documentoIdentidad) {
        if (usuarioPersistenciaPuerto
                .existePorDocumentoIdentidad(documentoIdentidad)) {

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

    private void normalizarCorreo(Usuario usuario) {
        if (usuario.getCorreo() != null) {
            usuario.setCorreo(
                    usuario.getCorreo()
                            .trim()
                            .toLowerCase(java.util.Locale.ROOT)
            );
        }
    }

    private Rol obtenerRolPropietario() {
        return rolPersistenciaPuerto
                .buscarPorNombre(PerfilUsuario.PROPIETARIO)
                .orElseThrow(() -> new ExcepcionUsuarios(
                        CodigosError.ROL_PROPIETARIO_NO_CONFIGURADO,
                        MensajesError.ROL_PROPIETARIO_NO_CONFIGURADO,
                        CategoriaError.ERROR_INTERNO
                ));
    }
}