package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.api.ValidarEmpleadoServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.EmpleadoValidacion;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

public class ValidarEmpleadoCasoUso
        implements ValidarEmpleadoServicioPuerto {

    private final UsuarioPersistenciaPuerto
            usuarioPersistenciaPuerto;

    public ValidarEmpleadoCasoUso(
            UsuarioPersistenciaPuerto
                    usuarioPersistenciaPuerto) {

        this.usuarioPersistenciaPuerto =
                usuarioPersistenciaPuerto;
    }

    @Override
    public EmpleadoValidacion validar(
            Long usuarioId,
            Long usuarioAutenticadoId) {

        validarCorrespondencia(
                usuarioId,
                usuarioAutenticadoId
        );

        Usuario usuario = usuarioPersistenciaPuerto
                .buscarPorId(usuarioId)
                .orElseThrow(() ->
                        new ExcepcionUsuarios(
                                CodigosError.EMPLEADO_NO_ENCONTRADO,
                                MensajesError.EMPLEADO_NO_ENCONTRADO,
                                CategoriaError.NO_ENCONTRADO
                        )
                );

        validarPerfil(usuario);
        validarActivo(usuario);
        validarRestaurante(usuario);

        return new EmpleadoValidacion(
                usuario.getId(),
                usuario.getRol().getNombre(),
                usuario.getRestauranteId(),
                usuario.getActivo()
        );
    }

    private void validarCorrespondencia(
            Long usuarioId,
            Long usuarioAutenticadoId) {

        if (usuarioId == null
                || !usuarioId.equals(
                usuarioAutenticadoId
        )) {
            throw new ExcepcionUsuarios(
                    CodigosError.EMPLEADO_NO_CORRESPONDE,
                    MensajesError.EMPLEADO_NO_CORRESPONDE,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarPerfil(Usuario usuario) {
        if (usuario.getRol() == null
                || !PerfilUsuario.EMPLEADO.equals(
                usuario.getRol().getNombre()
        )) {
            throw new ExcepcionUsuarios(
                    CodigosError.EMPLEADO_NO_CORRESPONDE,
                    MensajesError.EMPLEADO_NO_CORRESPONDE,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarActivo(Usuario usuario) {
        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ExcepcionUsuarios(
                    CodigosError.EMPLEADO_INACTIVO,
                    MensajesError.EMPLEADO_INACTIVO,
                    CategoriaError.ACCESO_DENEGADO
            );
        }
    }

    private void validarRestaurante(
            Usuario usuario) {

        if (usuario.getRestauranteId() == null) {
            throw new ExcepcionUsuarios(
                    CodigosError.EMPLEADO_SIN_RESTAURANTE,
                    MensajesError.EMPLEADO_SIN_RESTAURANTE,
                    CategoriaError.VALIDACION
            );
        }
    }
}