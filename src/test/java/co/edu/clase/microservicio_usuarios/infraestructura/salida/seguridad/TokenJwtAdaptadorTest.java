package co.edu.clase.microservicio_usuarios.infraestructura.salida.seguridad;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.infraestructura.configuracion.JwtPropiedades;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.EstadoToken;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenJwtAdaptadorTest {

    private static final String SECRETO_PRUEBA =
		"MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDE=";

    private static final Instant FECHA_PRUEBA =
		Instant.parse("2026-11-09T15:00:00Z");

    private TokenJwtAdaptador tokenJwtAdaptador;

    @BeforeEach
    void configurar() {
        JwtPropiedades propiedades = new JwtPropiedades();

        propiedades.setSecreto(SECRETO_PRUEBA);
        propiedades.setDuracionMilisegundos(3_600_000L);

        Clock reloj = Clock.fixed(
			FECHA_PRUEBA,
			ZoneOffset.UTC
        );

        tokenJwtAdaptador = new TokenJwtAdaptador(
			propiedades,
			reloj
        );
    }

    @Test
    void debeGenerarYLeerTokenCorrectamente() {
        Usuario usuario = crearUsuarioAdministrador();

        String token = tokenJwtAdaptador.generar(usuario);

        // assertTrue(tokenJwtAdaptador.esValido(token));
        assertEquals(
			EstadoToken.VALIDO,
			tokenJwtAdaptador.validar(token)
        );
        assertEquals(
			"admin@plazoleta.com",
			tokenJwtAdaptador.extraerCorreo(token)
        );
        assertEquals(
			1L,
			tokenJwtAdaptador.extraerUsuarioId(token)
        );
        assertEquals(
			PerfilUsuario.ADMINISTRADOR,
			tokenJwtAdaptador.extraerPerfil(token)
        );
        assertEquals(
			3600L,
			tokenJwtAdaptador.obtenerDuracionSegundos()
        );
    }

    @Test
    void debeRechazarTokenAlterado() {
        String token = tokenJwtAdaptador.generar(
			crearUsuarioAdministrador()
        );

        String tokenAlterado = token + "alterado";

        // assertFalse(
		// 	tokenJwtAdaptador.esValido(tokenAlterado)
        // );
		assertEquals(
				EstadoToken.INVALIDO,
				tokenJwtAdaptador.validar(tokenAlterado)
		);
    }

    @Test
    void debeRechazarTextoQueNoEsToken() {
        // assertFalse(
		// 	tokenJwtAdaptador.esValido(
		// 			"contenido-invalido"
        //         )
        // );
		assertEquals(
			EstadoToken.INVALIDO,
			tokenJwtAdaptador.validar(
				"contenido-invalido"
			)
		);
    }

    @Test
    void debeIdentificarTokenExpirado() {
        JwtPropiedades propiedades = new JwtPropiedades();

        propiedades.setSecreto(SECRETO_PRUEBA);
        propiedades.setDuracionMilisegundos(1_000L);

        Clock relojEmision = Clock.fixed(
                FECHA_PRUEBA,
                ZoneOffset.UTC
        );

        TokenJwtAdaptador adaptadorEmision =
                new TokenJwtAdaptador(
                        propiedades,
                        relojEmision
                );

        String token = adaptadorEmision.generar(
                crearUsuarioAdministrador()
        );

        Clock relojPosterior = Clock.fixed(
                FECHA_PRUEBA.plusSeconds(2),
                ZoneOffset.UTC
        );

        TokenJwtAdaptador adaptadorValidacion =
                new TokenJwtAdaptador(
                        propiedades,
                        relojPosterior
                );

        assertEquals(
                EstadoToken.EXPIRADO,
                adaptadorValidacion.validar(token)
        );
    }

    private Usuario crearUsuarioAdministrador() {
        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setNombre("Administrador");
        usuario.setApellido("Plazoleta");
        usuario.setCorreo("admin@plazoleta.com");
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
