package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.modelo.UsuarioConsulta;
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
class ConsultarUsuarioCasoUsoTest {

    @Mock
    private UsuarioPersistenciaPuerto
            usuarioPersistenciaPuerto;

    @Test
    void debeConsultarUsuarioPorId() {
        Usuario usuario = crearUsuario();

        when(usuarioPersistenciaPuerto.buscarPorId(10L))
                .thenReturn(Optional.of(usuario));

        ConsultarUsuarioCasoUso casoUso =
                new ConsultarUsuarioCasoUso(
                        usuarioPersistenciaPuerto
                );

        UsuarioConsulta resultado =
                casoUso.consultarPorId(10L);

        assertEquals(10L, resultado.getId());
        assertEquals(
                PerfilUsuario.PROPIETARIO,
                resultado.getPerfil()
        );
        assertEquals(
                Boolean.TRUE,
                resultado.getActivo()
        );
    }

    @Test
    void debeRechazarUsuarioInexistente() {
        when(usuarioPersistenciaPuerto.buscarPorId(99L))
                .thenReturn(Optional.empty());

        ConsultarUsuarioCasoUso casoUso =
                new ConsultarUsuarioCasoUso(
                        usuarioPersistenciaPuerto
                );

        ExcepcionUsuarios excepcion = assertThrows(
                ExcepcionUsuarios.class,
                () -> casoUso.consultarPorId(99L)
        );

        assertEquals(
                CodigosError.USUARIO_NO_ENCONTRADO,
                excepcion.getCodigo()
        );
    }

    private Usuario crearUsuario() {
        Usuario usuario = new Usuario();

        usuario.setId(10L);
        usuario.setActivo(Boolean.TRUE);
        usuario.setRol(new Rol(
                2L,
                PerfilUsuario.PROPIETARIO,
                "Propietario",
                Boolean.TRUE
        ));

        return usuario;
    }
}