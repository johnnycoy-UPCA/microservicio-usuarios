package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Autenticacion;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.CifradorClavePuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.TokenSeguridadPuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticarUsuarioCasoUsoTest {

    @Mock
    private UsuarioPersistenciaPuerto usuarioPersistenciaPuerto;

    @Mock
    private CifradorClavePuerto cifradorClavePuerto;

    @Mock
    private TokenSeguridadPuerto tokenSeguridadPuerto;

    private AutenticarUsuarioCasoUso autenticarUsuarioCasoUso;

    @BeforeEach
    void configurar() {
        autenticarUsuarioCasoUso =
                new AutenticarUsuarioCasoUso(
                        usuarioPersistenciaPuerto,
                        cifradorClavePuerto,
                        tokenSeguridadPuerto
                );
    }

    @Test
    void debeAutenticarUsuarioCorrectamente() {
        Usuario usuario = crearUsuarioActivo();

        when(usuarioPersistenciaPuerto.buscarPorCorreo(
                "admin@plazoleta.com"
        )).thenReturn(Optional.of(usuario));

        when(cifradorClavePuerto.coincide(
                "ClaveSegura123",
                "clave-cifrada"
        )).thenReturn(true);

        // when(tokenSeguridadPuerto.generar(usuario))
        when(tokenSeguridadPuerto.generar(any(Usuario.class)))
                .thenReturn("token-generado");
        when(tokenSeguridadPuerto.obtenerDuracionSegundos())
                .thenReturn(3600L);

        Autenticacion resultado =
                autenticarUsuarioCasoUso.autenticar(
                        "  ADMIN@PLAZOLETA.COM  ",
                        "ClaveSegura123"
                );

        assertEquals("token-generado", resultado.getToken());
        assertEquals(
                ConstantesSeguridad.TIPO_TOKEN,
                resultado.getTipo()
        );
        // assertEquals("Bearer", resultado.getTipo());
        assertEquals(3600L, resultado.getDuracionSegundos());
        assertEquals(1L, resultado.getUsuarioId());
        assertEquals(
                PerfilUsuario.ADMINISTRADOR,
                resultado.getPerfil()
        );

        verify(usuarioPersistenciaPuerto)
                .buscarPorCorreo("admin@plazoleta.com");
    }

    @Test
    void debeRechazarCorreoNoRegistrado() {
        when(usuarioPersistenciaPuerto.buscarPorCorreo(
                "inexistente@plazoleta.com"
        )).thenReturn(Optional.empty());

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> autenticarUsuarioCasoUso.autenticar(
                        "inexistente@plazoleta.com",
                        "ClaveSegura123"
                )
        );

        assertEquals(
                CodigosError.CREDENCIALES_INVALIDAS,
                excepcion.getCodigo()
        );

        verify(tokenSeguridadPuerto, never())
                .generar(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void debeRechazarClaveIncorrecta() {
        Usuario usuario = crearUsuarioActivo();

        when(usuarioPersistenciaPuerto.buscarPorCorreo(
                "admin@plazoleta.com"
        )).thenReturn(Optional.of(usuario));

        when(cifradorClavePuerto.coincide(
                "ClaveIncorrecta",
                "clave-cifrada"
        )).thenReturn(false);

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> autenticarUsuarioCasoUso.autenticar(
                        "admin@plazoleta.com",
                        "ClaveIncorrecta"
                )
        );

        assertEquals(
                CodigosError.CREDENCIALES_INVALIDAS,
                excepcion.getCodigo()
        );

        verify(tokenSeguridadPuerto, never())
                .generar(usuario);
    }

    @Test
    void debeRechazarUsuarioInactivo() {
        Usuario usuario = crearUsuarioActivo();
        usuario.setActivo(Boolean.FALSE);

        when(usuarioPersistenciaPuerto.buscarPorCorreo(
                "admin@plazoleta.com"
        )).thenReturn(Optional.of(usuario));

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> autenticarUsuarioCasoUso.autenticar(
                        "admin@plazoleta.com",
                        "ClaveSegura123"
                )
        );

        assertEquals(
                CodigosError.USUARIO_INACTIVO,
                excepcion.getCodigo()
        );

        verify(cifradorClavePuerto, never())
                .coincide(
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any()
                );

        verify(tokenSeguridadPuerto, never())
                .generar(usuario);
    }

    private Usuario crearUsuarioActivo() {
        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setNombre("Administrador");
        usuario.setApellido("Plazoleta");
        usuario.setCorreo("admin@plazoleta.com");
        usuario.setClave("clave-cifrada");
        usuario.setActivo(Boolean.TRUE);
        usuario.setRol(new Rol(
                1L,
                PerfilUsuario.ADMINISTRADOR,
                "Administrador del sistema",
                Boolean.TRUE
        ));

        return usuario;
    }
}