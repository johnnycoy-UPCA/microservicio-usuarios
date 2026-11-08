package co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.adaptador;

// import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
// import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.mapeador.RolPersistenciaMapeador;
import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.mapeador.UsuarioPersistenciaMapeador;
import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.repositorio.UsuarioJpaRepositorio;
// import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.LocalDate;
// import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioPersistenciaAdaptadorTest {

    @Mock
    private UsuarioJpaRepositorio usuarioJpaRepositorio;

    // @Spy
    // private RolPersistenciaMapeador rolPersistenciaMapeador;

    @Spy
    private UsuarioPersistenciaMapeador
            usuarioPersistenciaMapeador =
            new UsuarioPersistenciaMapeador(
                    new RolPersistenciaMapeador()
            );

    @InjectMocks
    private UsuarioPersistenciaAdaptador
            usuarioPersistenciaAdaptador;

    @Test
    void debeDetectarCorreoRegistrado() {
        when(usuarioJpaRepositorio.existsByCorreoIgnoreCase(
                "ana.martinez@correo.com"
        )).thenReturn(true);

        boolean resultado =
                usuarioPersistenciaAdaptador.existePorCorreo(
                        "ana.martinez@correo.com"
                );

        assertTrue(resultado);
    }

    @Test
    void debeDetectarDocumentoNoRegistrado() {
        when(usuarioJpaRepositorio
                .existsByDocumentoIdentidad("1020304050"))
                .thenReturn(false);

        boolean resultado =
                usuarioPersistenciaAdaptador
                        .existePorDocumentoIdentidad(
                                "1020304050"
                        );

        assertFalse(resultado);
    }
}