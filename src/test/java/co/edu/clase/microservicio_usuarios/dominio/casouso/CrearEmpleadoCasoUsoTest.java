package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.RestauranteValidacion;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.CifradorClavePuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.RestauranteConsultaPuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.RolPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearEmpleadoCasoUsoTest {

    private static final Long RESTAURANTE_ID = 1L;
    private static final Long PROPIETARIO_ID = 10L;
    private static final String TOKEN = "token-propietario";

    @Mock
    private UsuarioPersistenciaPuerto
            usuarioPersistenciaPuerto;

    @Mock
    private RolPersistenciaPuerto
            rolPersistenciaPuerto;

    @Mock
    private CifradorClavePuerto cifradorClavePuerto;

    @Mock
    private RestauranteConsultaPuerto
            restauranteConsultaPuerto;

    private CrearEmpleadoCasoUso crearEmpleadoCasoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                Instant.parse("2026-11-18T20:00:00Z"),
                ZoneId.of("America/Bogota")
        );

        crearEmpleadoCasoUso = new CrearEmpleadoCasoUso(
                usuarioPersistenciaPuerto,
                rolPersistenciaPuerto,
                cifradorClavePuerto,
                restauranteConsultaPuerto,
                reloj
        );
    }

    @Test
    void debeCrearEmpleadoCorrectamente() {
        Usuario empleado = crearEmpleadoValido();

        when(usuarioPersistenciaPuerto.existePorCorreo(
                "empleado@correo.com"
        )).thenReturn(false);

        when(usuarioPersistenciaPuerto
                .existePorDocumentoIdentidad(
                        "1099001122"
                ))
                .thenReturn(false);

        when(restauranteConsultaPuerto.validarPropiedad(
                RESTAURANTE_ID,
                TOKEN
        )).thenReturn(new RestauranteValidacion(
                RESTAURANTE_ID,
                PROPIETARIO_ID,
                Boolean.TRUE
        ));

        when(rolPersistenciaPuerto.buscarPorNombre(
                PerfilUsuario.EMPLEADO
        )).thenReturn(Optional.of(crearRolEmpleado()));

        when(cifradorClavePuerto.cifrar(
                "ClaveEmpleado#123"
        )).thenReturn("clave-cifrada");

        when(usuarioPersistenciaPuerto.guardar(
                any(Usuario.class)
        )).thenAnswer(invocacion -> {
            Usuario guardado = invocacion.getArgument(0);
            guardado.setId(30L);
            return guardado;
        });

        Usuario resultado = crearEmpleadoCasoUso.crear(
                empleado,
                TOKEN
        );

        assertEquals(30L, resultado.getId());
        assertEquals(
                "empleado@correo.com",
                resultado.getCorreo()
        );
        assertEquals(
                PerfilUsuario.EMPLEADO,
                resultado.getRol().getNombre()
        );
        assertEquals(
                RESTAURANTE_ID,
                resultado.getRestauranteId()
        );
        assertEquals(
                "clave-cifrada",
                resultado.getClave()
        );
        assertTrue(resultado.getActivo());
        assertNotNull(resultado.getFechaCreacion());
        assertNotNull(resultado.getFechaActualizacion());

        verify(restauranteConsultaPuerto)
                .validarPropiedad(
                        RESTAURANTE_ID,
                        TOKEN
                );

        verify(usuarioPersistenciaPuerto)
                .guardar(empleado);
    }

    @Test
    void debeRechazarClaveDebil() {
        Usuario empleado = crearEmpleadoValido();
        empleado.setClave("clave123");

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> crearEmpleadoCasoUso.crear(
                        empleado,
                        TOKEN
                )
        );

        assertEquals(
                CodigosError.CLAVE_OBLIGATORIA,
                excepcion.getCodigo()
        );

        verify(restauranteConsultaPuerto, never())
                .validarPropiedad(any(), any());
    }

    @Test
    void debeRechazarCorreoDuplicado() {
        Usuario empleado = crearEmpleadoValido();

        when(usuarioPersistenciaPuerto.existePorCorreo(
                "empleado@correo.com"
        )).thenReturn(true);

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> crearEmpleadoCasoUso.crear(
                        empleado,
                        TOKEN
                )
        );

        assertEquals(
                CodigosError.CORREO_YA_REGISTRADO,
                excepcion.getCodigo()
        );

        verify(restauranteConsultaPuerto, never())
                .validarPropiedad(any(), any());
    }

    @Test
    void debeRechazarRestauranteInvalido() {
        Usuario empleado = crearEmpleadoValido();

        when(usuarioPersistenciaPuerto.existePorCorreo(
                "empleado@correo.com"
        )).thenReturn(false);

        when(usuarioPersistenciaPuerto
                .existePorDocumentoIdentidad(
                        "1099001122"
                ))
                .thenReturn(false);

        when(restauranteConsultaPuerto.validarPropiedad(
                RESTAURANTE_ID,
                TOKEN
        )).thenReturn(new RestauranteValidacion(
                RESTAURANTE_ID,
                PROPIETARIO_ID,
                Boolean.FALSE
        ));

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> crearEmpleadoCasoUso.crear(
                        empleado,
                        TOKEN
                )
        );

        assertEquals(
                CodigosError.RESTAURANTE_NO_VALIDO,
                excepcion.getCodigo()
        );

        verify(usuarioPersistenciaPuerto, never())
                .guardar(any());
    }

    @Test
    void debeRechazarRolEmpleadoNoConfigurado() {
        Usuario empleado = crearEmpleadoValido();

        when(usuarioPersistenciaPuerto.existePorCorreo(
                "empleado@correo.com"
        )).thenReturn(false);

        when(usuarioPersistenciaPuerto
                .existePorDocumentoIdentidad(
                        "1099001122"
                ))
                .thenReturn(false);

        when(restauranteConsultaPuerto.validarPropiedad(
                RESTAURANTE_ID,
                TOKEN
        )).thenReturn(new RestauranteValidacion(
                RESTAURANTE_ID,
                PROPIETARIO_ID,
                Boolean.TRUE
        ));

        when(rolPersistenciaPuerto.buscarPorNombre(
                PerfilUsuario.EMPLEADO
        )).thenReturn(Optional.empty());

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> crearEmpleadoCasoUso.crear(
                        empleado,
                        TOKEN
                )
        );

        assertEquals(
                CodigosError.ROL_EMPLEADO_NO_CONFIGURADO,
                excepcion.getCodigo()
        );

        verify(usuarioPersistenciaPuerto, never())
                .guardar(any());
    }

    private Usuario crearEmpleadoValido() {
        Usuario empleado = new Usuario();

        empleado.setNombre("  Empleado  ");
        empleado.setApellido("  Prueba  ");
        empleado.setDocumentoIdentidad("1099001122");
        empleado.setCelular("+573001234599");
        empleado.setFechaNacimiento(
                LocalDate.of(1995, 4, 12)
        );
        empleado.setCorreo(
                "  EMPLEADO@CORREO.COM  "
        );
        empleado.setClave("ClaveEmpleado#123");
        empleado.setRestauranteId(RESTAURANTE_ID);

        return empleado;
    }

    private Rol crearRolEmpleado() {
        return new Rol(
                3L,
                PerfilUsuario.EMPLEADO,
                "Empleado",
                Boolean.TRUE
        );
    }
}