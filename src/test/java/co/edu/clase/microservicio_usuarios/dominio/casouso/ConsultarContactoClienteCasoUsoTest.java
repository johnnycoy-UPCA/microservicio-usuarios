package co.edu.clase.microservicio_usuarios.dominio.casouso;

import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.ContactoCliente;
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
class ConsultarContactoClienteCasoUsoTest {

    private static final Long CLIENTE_ID = 20L;

    @Mock
    private UsuarioPersistenciaPuerto
            usuarioPersistenciaPuerto;

    @Test
    void debeConsultarContactoClienteActivo() {
        Usuario cliente = crearCliente();

        when(usuarioPersistenciaPuerto
                .buscarPorId(CLIENTE_ID))
                .thenReturn(Optional.of(cliente));

        ConsultarContactoClienteCasoUso casoUso =
                new ConsultarContactoClienteCasoUso(
                        usuarioPersistenciaPuerto
                );

        ContactoCliente resultado =
                casoUso.consultar(CLIENTE_ID);

        assertEquals(
                CLIENTE_ID,
                resultado.getUsuarioId()
        );

        assertEquals(
                "+573001234567",
                resultado.getCelular()
        );

        assertEquals(
                Boolean.TRUE,
                resultado.getActivo()
        );
    }

    @Test
    void debeRechazarClienteInexistente() {
        when(usuarioPersistenciaPuerto
                .buscarPorId(CLIENTE_ID))
                .thenReturn(Optional.empty());

        ConsultarContactoClienteCasoUso casoUso =
                new ConsultarContactoClienteCasoUso(
                        usuarioPersistenciaPuerto
                );

        ExcepcionUsuarios excepcion =
                assertThrows(
                        ExcepcionUsuarios.class,
                        () -> casoUso.consultar(
                                CLIENTE_ID
                        )
                );

        assertEquals(
                CodigosError.CLIENTE_NO_ENCONTRADO,
                excepcion.getCodigo()
        );
    }

    @Test
    void debeRechazarUsuarioNoCliente() {
        Usuario usuario = crearCliente();

        usuario.setRol(
                new Rol(
                        2L,
                        PerfilUsuario.PROPIETARIO,
                        "Propietario",
                        Boolean.TRUE
                )
        );

        when(usuarioPersistenciaPuerto
                .buscarPorId(CLIENTE_ID))
                .thenReturn(Optional.of(usuario));

        ConsultarContactoClienteCasoUso casoUso =
                new ConsultarContactoClienteCasoUso(
                        usuarioPersistenciaPuerto
                );

        ExcepcionUsuarios excepcion =
                assertThrows(
                        ExcepcionUsuarios.class,
                        () -> casoUso.consultar(
                                CLIENTE_ID
                        )
                );

        assertEquals(
                CodigosError.USUARIO_NO_ES_CLIENTE,
                excepcion.getCodigo()
        );
    }

    @Test
    void debeRechazarClienteInactivo() {
        Usuario cliente = crearCliente();
        cliente.setActivo(Boolean.FALSE);

        when(usuarioPersistenciaPuerto
                .buscarPorId(CLIENTE_ID))
                .thenReturn(Optional.of(cliente));

        ConsultarContactoClienteCasoUso casoUso =
                new ConsultarContactoClienteCasoUso(
                        usuarioPersistenciaPuerto
                );

        ExcepcionUsuarios excepcion =
                assertThrows(
                        ExcepcionUsuarios.class,
                        () -> casoUso.consultar(
                                CLIENTE_ID
                        )
                );

        assertEquals(
                CodigosError.CLIENTE_INACTIVO,
                excepcion.getCodigo()
        );
    }

    private Usuario crearCliente() {
        Usuario cliente = new Usuario();

        cliente.setId(CLIENTE_ID);
        cliente.setCelular("+573001234567");
        cliente.setActivo(Boolean.TRUE);

        cliente.setRol(
                new Rol(
                        4L,
                        PerfilUsuario.CLIENTE,
                        "Cliente",
                        Boolean.TRUE
                )
        );

        return cliente;
    }
}