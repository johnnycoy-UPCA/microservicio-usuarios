package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.CifradorClavePuerto;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarClienteCasoUsoTest {

    @Mock
    private UsuarioPersistenciaPuerto
            usuarioPersistenciaPuerto;

    @Mock
    private RolPersistenciaPuerto
            rolPersistenciaPuerto;

    @Mock
    private CifradorClavePuerto
            cifradorClavePuerto;

    private RegistrarClienteCasoUso
            registrarClienteCasoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                Instant.parse("2026-11-17T15:00:00Z"),
                ZoneId.of("America/Bogota")
        );

        registrarClienteCasoUso =
                new RegistrarClienteCasoUso(
                        usuarioPersistenciaPuerto,
                        rolPersistenciaPuerto,
                        cifradorClavePuerto,
                        reloj
                );
    }

    @Test
    void debeRegistrarClienteCorrectamente() {
        Usuario usuario = crearUsuarioValido();
        Rol rolCliente = crearRolCliente();

        when(usuarioPersistenciaPuerto
                .existePorCorreo(
                        "cliente@correo.com"
                ))
                .thenReturn(false);

        when(usuarioPersistenciaPuerto
                .existePorDocumentoIdentidad(
                        "123456789"
                ))
                .thenReturn(false);

        when(rolPersistenciaPuerto
                .buscarPorNombre(PerfilUsuario.CLIENTE))
                .thenReturn(Optional.of(rolCliente));

        when(cifradorClavePuerto.cifrar(
                "ClaveCliente123"
        )).thenReturn("clave-cifrada");

        when(usuarioPersistenciaPuerto.guardar(
                any(Usuario.class)
        )).thenAnswer(invocacion -> {
            Usuario guardado =
                    invocacion.getArgument(0);

            guardado.setId(20L);
            return guardado;
        });

        Usuario resultado =
                registrarClienteCasoUso.registrar(usuario);

        assertEquals(20L, resultado.getId());
        assertEquals(
                "cliente@correo.com",
                resultado.getCorreo()
        );
        assertEquals(
                PerfilUsuario.CLIENTE,
                resultado.getRol().getNombre()
        );
        assertEquals(
                "clave-cifrada",
                resultado.getClave()
        );
        assertTrue(resultado.getActivo());
        assertNotNull(resultado.getFechaCreacion());
        assertNotNull(
                resultado.getFechaActualizacion()
        );

        verify(usuarioPersistenciaPuerto)
                .guardar(usuario);
    }

    @Test
    void debeRechazarCorreoDuplicado() {
        Usuario usuario = crearUsuarioValido();

        when(usuarioPersistenciaPuerto
                .existePorCorreo(
                        "cliente@correo.com"
                ))
                .thenReturn(true);

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> registrarClienteCasoUso
                        .registrar(usuario)
        );

        assertEquals(
                CodigosError.CORREO_YA_REGISTRADO,
                excepcion.getCodigo()
        );

        verify(usuarioPersistenciaPuerto, never())
                .guardar(any());
    }

    @Test
    void debeRechazarDocumentoDuplicado() {
        Usuario usuario = crearUsuarioValido();

        when(usuarioPersistenciaPuerto
                .existePorCorreo(
                        "cliente@correo.com"
                ))
                .thenReturn(false);

        when(usuarioPersistenciaPuerto
                .existePorDocumentoIdentidad(
                        "123456789"
                ))
                .thenReturn(true);

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> registrarClienteCasoUso
                        .registrar(usuario)
        );

        assertEquals(
                CodigosError.DOCUMENTO_YA_REGISTRADO,
                excepcion.getCodigo()
        );
    }

    @Test
    void debeRechazarRolClienteNoConfigurado() {
        Usuario usuario = crearUsuarioValido();

        when(usuarioPersistenciaPuerto
                .existePorCorreo(
                        "cliente@correo.com"
                ))
                .thenReturn(false);

        when(usuarioPersistenciaPuerto
                .existePorDocumentoIdentidad(
                        "123456789"
                ))
                .thenReturn(false);

        when(rolPersistenciaPuerto
                .buscarPorNombre(PerfilUsuario.CLIENTE))
                .thenReturn(Optional.empty());

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> registrarClienteCasoUso
                        .registrar(usuario)
        );

        assertEquals(
                CodigosError.ROL_CLIENTE_NO_CONFIGURADO,
                excepcion.getCodigo()
        );

        verify(usuarioPersistenciaPuerto, never())
                .guardar(any());
    }

    private Usuario crearUsuarioValido() {
        Usuario usuario = new Usuario();

        usuario.setNombre("Cliente");
        usuario.setApellido("Prueba");
        usuario.setDocumentoIdentidad("123456789");
        usuario.setCelular("+573001234567");
        usuario.setFechaNacimiento(
                LocalDate.of(2008, 5, 20)
        );
        usuario.setCorreo(
                "  CLIENTE@CORREO.COM  "
        );
        usuario.setClave("ClaveCliente123");

        return usuario;
    }

    private Rol crearRolCliente() {
        return new Rol(
                4L,
                PerfilUsuario.CLIENTE,
                "Cliente",
                Boolean.TRUE
        );
    }
}