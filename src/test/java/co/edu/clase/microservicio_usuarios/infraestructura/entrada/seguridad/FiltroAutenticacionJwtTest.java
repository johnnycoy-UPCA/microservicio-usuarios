package co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad;

import co.edu.clase.microservicio_usuarios.dominio.spi.TokenSeguridadPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.EstadoToken;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FiltroAutenticacionJwtTest {

    @Mock
    private TokenSeguridadPuerto tokenSeguridadPuerto;

    @Mock
    private FilterChain cadenaFiltros;

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void debeAutenticarSolicitudConTokenValido()
            throws Exception {

        String token = "token-valido";

        when(tokenSeguridadPuerto.validar(token))
                .thenReturn(EstadoToken.VALIDO);

        when(tokenSeguridadPuerto.extraerCorreo(token))
                .thenReturn("admin@plazoleta.com");

        when(tokenSeguridadPuerto.extraerUsuarioId(token))
                .thenReturn(1L);

        when(tokenSeguridadPuerto.extraerPerfil(token))
                .thenReturn(PerfilUsuario.ADMINISTRADOR);

        MockHttpServletRequest solicitud =
                new MockHttpServletRequest();

        solicitud.addHeader(
                ConstantesSeguridad.ENCABEZADO_AUTORIZACION,
                ConstantesSeguridad.PREFIJO_TOKEN + token
        );

        MockHttpServletResponse respuesta =
                new MockHttpServletResponse();

        FiltroAutenticacionJwt filtro =
                new FiltroAutenticacionJwt(
                        tokenSeguridadPuerto
                );

        filtro.doFilter(
                solicitud,
                respuesta,
                cadenaFiltros
        );

        assertTrue(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .isAuthenticated()
        );

        assertEquals(
                "ROLE_ADMINISTRADOR",
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
        );

        verify(cadenaFiltros).doFilter(
                solicitud,
                respuesta
        );
    }

    @Test
    void debeRegistrarTokenInvalidoSinAutenticar()
            throws Exception {

        String token = "token-invalido";

        when(tokenSeguridadPuerto.validar(token))
                .thenReturn(EstadoToken.INVALIDO);

        MockHttpServletRequest solicitud =
                new MockHttpServletRequest();

        solicitud.addHeader(
                ConstantesSeguridad.ENCABEZADO_AUTORIZACION,
                ConstantesSeguridad.PREFIJO_TOKEN + token
        );

        MockHttpServletResponse respuesta =
                new MockHttpServletResponse();

        FiltroAutenticacionJwt filtro =
                new FiltroAutenticacionJwt(
                        tokenSeguridadPuerto
                );

        filtro.doFilter(
                solicitud,
                respuesta,
                cadenaFiltros
        );

        assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );

        assertEquals(
                CodigosError.TOKEN_INVALIDO,
                solicitud.getAttribute(
                        ConstantesSeguridad
                                .ATRIBUTO_CODIGO_ERROR
                )
        );
    }

    @Test
    void debeContinuarSinAutenticarCuandoNoHayToken()
            throws Exception {

        MockHttpServletRequest solicitud =
                new MockHttpServletRequest();

        MockHttpServletResponse respuesta =
                new MockHttpServletResponse();

        FiltroAutenticacionJwt filtro =
                new FiltroAutenticacionJwt(
                        tokenSeguridadPuerto
                );

        filtro.doFilter(
                solicitud,
                respuesta,
                cadenaFiltros
        );

        assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );

        verify(cadenaFiltros).doFilter(
                solicitud,
                respuesta
        );
    }
}