package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidarEmpleadoCasoUsoTest {

    @Mock
    private UsuarioPersistenciaPuerto
            usuarioPersistenciaPuerto;

    @Test
    void debeValidarEmpleadoCorrectamente() {
        Usuario empleado = crearEmpleado();

        when(usuarioPersistenciaPuerto
                .buscarPorId(30L))
                .thenReturn(Optional.of(empleado));

        ValidarEmpleadoCasoUso casoUso =
                new ValidarEmpleadoCasoUso(
                        usuarioPersistenciaPuerto
                );

        var resultado = casoUso.validar(
                30L,
                30L
        );

        assertEquals(30L, resultado.getUsuarioId());
        assertEquals(
                PerfilUsuario.EMPLEADO,
                resultado.getPerfil()
        );
        assertEquals(
                1L,
                resultado.getRestauranteId()
        );
        assertEquals(
                Boolean.TRUE,
                resultado.getActivo()
        );
    }

    @Test
    void debeRechazarIdentidadDiferente() {
        ValidarEmpleadoCasoUso casoUso =
                new ValidarEmpleadoCasoUso(
                        usuarioPersistenciaPuerto
                );

        ExcepcionUsuarios excepcion =
                assertThrows(
                        ExcepcionUsuarios.class,
                        () -> casoUso.validar(
                                30L,
                                99L
                        )
                );

        assertEquals(
                CodigosError.EMPLEADO_NO_CORRESPONDE,
                excepcion.getCodigo()
        );
    }

    @Test
    void debeRechazarEmpleadoInactivo() {
        Usuario empleado = crearEmpleado();
        empleado.setActivo(Boolean.FALSE);

        when(usuarioPersistenciaPuerto
                .buscarPorId(30L))
                .thenReturn(Optional.of(empleado));

        ValidarEmpleadoCasoUso casoUso =
                new ValidarEmpleadoCasoUso(
                        usuarioPersistenciaPuerto
                );

        ExcepcionUsuarios excepcion =
                assertThrows(
                        ExcepcionUsuarios.class,
                        () -> casoUso.validar(
                                30L,
                                30L
                        )
                );

        assertEquals(
                CodigosError.EMPLEADO_INACTIVO,
                excepcion.getCodigo()
        );
    }

    private Usuario crearEmpleado() {
        Usuario empleado = new Usuario();

        empleado.setId(30L);
        empleado.setRestauranteId(1L);
        empleado.setActivo(Boolean.TRUE);
        empleado.setRol(new Rol(
                3L,
                PerfilUsuario.EMPLEADO,
                "Empleado",
                Boolean.TRUE
        ));

        return empleado;
    }
}