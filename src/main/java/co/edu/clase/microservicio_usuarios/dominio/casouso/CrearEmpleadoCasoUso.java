package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.api.CrearEmpleadoServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.RestauranteValidacion;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.CifradorClavePuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.RestauranteConsultaPuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.RolPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

public class CrearEmpleadoCasoUso
        implements CrearEmpleadoServicioPuerto {

    private final UsuarioPersistenciaPuerto
            usuarioPersistenciaPuerto;

    private final RolPersistenciaPuerto
            rolPersistenciaPuerto;

    private final CifradorClavePuerto
            cifradorClavePuerto;

    private final RestauranteConsultaPuerto
            restauranteConsultaPuerto;

    private final Clock reloj;

    public CrearEmpleadoCasoUso(
            UsuarioPersistenciaPuerto usuarioPersistenciaPuerto,
            RolPersistenciaPuerto rolPersistenciaPuerto,
            CifradorClavePuerto cifradorClavePuerto,
            RestauranteConsultaPuerto restauranteConsultaPuerto,
            Clock reloj) {

        this.usuarioPersistenciaPuerto =
                usuarioPersistenciaPuerto;
        this.rolPersistenciaPuerto =
                rolPersistenciaPuerto;
        this.cifradorClavePuerto =
                cifradorClavePuerto;
        this.restauranteConsultaPuerto =
                restauranteConsultaPuerto;
        this.reloj = reloj;
    }

    @Override
    public Usuario crear(
            Usuario empleado,
            String token) {

        normalizarDatos(empleado);
        validarCorreoDuplicado(empleado.getCorreo());
        validarDocumentoDuplicado(
                empleado.getDocumentoIdentidad()
        );
        validarClave(empleado.getClave());

        RestauranteValidacion restaurante =
                restauranteConsultaPuerto.validarPropiedad(
                        empleado.getRestauranteId(),
                        token
                );

        validarRestaurante(restaurante);

        Rol rolEmpleado = obtenerRolEmpleado();

        LocalDateTime fechaActual =
                LocalDateTime.now(reloj);

        empleado.setRol(rolEmpleado);
        empleado.setClave(
                cifradorClavePuerto.cifrar(
                        empleado.getClave()
                )
        );
        empleado.setActivo(Boolean.TRUE);
        empleado.setFechaCreacion(fechaActual);
        empleado.setFechaActualizacion(fechaActual);

        return usuarioPersistenciaPuerto.guardar(
                empleado
        );
    }

    private void normalizarDatos(Usuario empleado) {
        if (empleado.getNombre() != null) {
            empleado.setNombre(
                    empleado.getNombre().trim()
            );
        }

        if (empleado.getApellido() != null) {
            empleado.setApellido(
                    empleado.getApellido().trim()
            );
        }

        if (empleado.getDocumentoIdentidad() != null) {
            empleado.setDocumentoIdentidad(
                    empleado.getDocumentoIdentidad().trim()
            );
        }

        if (empleado.getCelular() != null) {
            empleado.setCelular(
                    empleado.getCelular().trim()
            );
        }

        if (empleado.getCorreo() != null) {
            empleado.setCorreo(
                    empleado.getCorreo()
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
        boolean claveInvalida = clave == null
                || !clave.matches(
                ConstantesValidacion
                        .EXPRESION_CLAVE_SEGURA
        );

        if (claveInvalida) {
            throw new ExcepcionUsuarios(
                    CodigosError.CLAVE_OBLIGATORIA,
                    MensajesError.CLAVE_FORMATO_INVALIDO,
                    CategoriaError.VALIDACION
            );
        }
    }

    private void validarRestaurante(
            RestauranteValidacion restaurante) {

        if (restaurante.getId() == null
                || !Boolean.TRUE.equals(
                restaurante.getActivo()
        )) {
            throw new ExcepcionUsuarios(
                    CodigosError.RESTAURANTE_NO_VALIDO,
                    MensajesError.RESTAURANTE_NO_VALIDO,
                    CategoriaError.VALIDACION
            );
        }
    }

    private Rol obtenerRolEmpleado() {
        return rolPersistenciaPuerto
                .buscarPorNombre(PerfilUsuario.EMPLEADO)
                .orElseThrow(() -> new ExcepcionUsuarios(
                        CodigosError
                                .ROL_EMPLEADO_NO_CONFIGURADO,
                        MensajesError
                                .ROL_EMPLEADO_NO_CONFIGURADO,
                        CategoriaError.ERROR_INTERNO
                ));
    }
}