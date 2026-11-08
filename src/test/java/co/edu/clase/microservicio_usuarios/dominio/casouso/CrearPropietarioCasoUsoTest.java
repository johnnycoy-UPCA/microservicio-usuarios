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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearPropietarioCasoUsoTest {

	private static final Instant FECHA_PRUEBA = Instant.parse("2026-11-07T12:00:00Z");

	private static final ZoneId ZONA_HORARIA = ZoneId.of("America/Bogota");

	@Mock
	private UsuarioPersistenciaPuerto usuarioPersistenciaPuerto;

	@Mock
	private RolPersistenciaPuerto rolPersistenciaPuerto;

	@Mock
	private CifradorClavePuerto cifradorClavePuerto;

	private CrearPropietarioCasoUso crearPropietarioCasoUso;

	@BeforeEach
	void configurar() {
		Clock relojFijo = Clock.fixed(FECHA_PRUEBA, ZONA_HORARIA);

		crearPropietarioCasoUso = new CrearPropietarioCasoUso(
				usuarioPersistenciaPuerto,
				rolPersistenciaPuerto,
				cifradorClavePuerto,
				relojFijo);
	}

	@Test
	void debeCrearPropietarioCorrectamente() {
		Usuario usuario = crearUsuarioValido();
		Rol rolPropietario = crearRolPropietario();

		when(usuarioPersistenciaPuerto
				.existePorCorreo(usuario.getCorreo()))
				.thenReturn(false);

		when(usuarioPersistenciaPuerto
				.existePorDocumentoIdentidad(
						usuario.getDocumentoIdentidad()))
				.thenReturn(false);

		when(rolPersistenciaPuerto
				.buscarPorNombre(PerfilUsuario.PROPIETARIO))
				.thenReturn(Optional.of(rolPropietario));

		when(cifradorClavePuerto.cifrar("ClaveSegura123"))
				.thenReturn("clave-cifrada");

		when(usuarioPersistenciaPuerto.guardar(any(Usuario.class)))
				.thenAnswer(invocacion -> {
					Usuario usuarioGuardado = invocacion.getArgument(0);
					usuarioGuardado.setId(1L);
					return usuarioGuardado;
				});

		Usuario resultado = crearPropietarioCasoUso.crear(usuario);

		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
		assertEquals(PerfilUsuario.PROPIETARIO,
				resultado.getRol().getNombre());
		assertEquals("clave-cifrada", resultado.getClave());
		assertNotEquals("ClaveSegura123", resultado.getClave());
		assertTrue(resultado.getActivo());
		assertNotNull(resultado.getFechaCreacion());
		assertNotNull(resultado.getFechaActualizacion());

		verify(usuarioPersistenciaPuerto).guardar(usuario);
	}

	@Test
	void debeRechazarCorreoDuplicado() {
		Usuario usuario = crearUsuarioValido();

		when(usuarioPersistenciaPuerto
				.existePorCorreo(usuario.getCorreo()))
				.thenReturn(true);

		ExcepcionUsuarios excepcion = assertThrows(
				ExcepcionUsuarios.class,
				() -> crearPropietarioCasoUso.crear(usuario));

		assertEquals(
				CodigosError.CORREO_YA_REGISTRADO,
				excepcion.getCodigo());

		verify(usuarioPersistenciaPuerto, never())
				.guardar(any(Usuario.class));
	}

	@Test
	void debeRechazarDocumentoDuplicado() {
		Usuario usuario = crearUsuarioValido();

		when(usuarioPersistenciaPuerto
				.existePorCorreo(usuario.getCorreo()))
				.thenReturn(false);

		when(usuarioPersistenciaPuerto
				.existePorDocumentoIdentidad(
						usuario.getDocumentoIdentidad()))
				.thenReturn(true);

		ExcepcionUsuarios excepcion = assertThrows(
				ExcepcionUsuarios.class,
				() -> crearPropietarioCasoUso.crear(usuario));

		assertEquals(
				CodigosError.DOCUMENTO_YA_REGISTRADO,
				excepcion.getCodigo());

		verify(usuarioPersistenciaPuerto, never())
				.guardar(any(Usuario.class));
	}

	@Test
	void debeRechazarPropietarioMenorDeEdad() {
		Usuario usuario = crearUsuarioValido();
		usuario.setFechaNacimiento(LocalDate.of(2009, 11, 8));

		ExcepcionUsuarios excepcion = assertThrows(
				ExcepcionUsuarios.class,
				() -> crearPropietarioCasoUso.crear(usuario));

		assertEquals(
				CodigosError.USUARIO_MENOR_EDAD,
				excepcion.getCodigo());

		verify(usuarioPersistenciaPuerto, never())
				.guardar(any(Usuario.class));
	}

	@Test
	void debeAceptarPropietarioQueCumpleDieciochoAniosHoy() {
		Usuario usuario = crearUsuarioValido();
		usuario.setFechaNacimiento(LocalDate.of(2008, 11, 7));

		Rol rolPropietario = crearRolPropietario();

		when(usuarioPersistenciaPuerto
				.existePorCorreo(usuario.getCorreo()))
				.thenReturn(false);

		when(usuarioPersistenciaPuerto
				.existePorDocumentoIdentidad(
						usuario.getDocumentoIdentidad()))
				.thenReturn(false);

		when(rolPersistenciaPuerto
				.buscarPorNombre(PerfilUsuario.PROPIETARIO))
				.thenReturn(Optional.of(rolPropietario));

		when(cifradorClavePuerto.cifrar(any()))
				.thenReturn("clave-cifrada");

		when(usuarioPersistenciaPuerto.guardar(any()))
				.thenAnswer(invocacion -> invocacion.getArgument(0));

		Usuario resultado = crearPropietarioCasoUso.crear(usuario);

		assertNotNull(resultado);
		verify(usuarioPersistenciaPuerto).guardar(usuario);
	}

	@Test
	void debeRechazarFechaNacimientoNula() {
		Usuario usuario = crearUsuarioValido();
		usuario.setFechaNacimiento(null);

		ExcepcionUsuarios excepcion = assertThrows(
				ExcepcionUsuarios.class,
				() -> crearPropietarioCasoUso.crear(usuario));

		assertEquals(
				CodigosError.FECHA_NACIMIENTO_OBLIGATORIA,
				excepcion.getCodigo());

		verify(usuarioPersistenciaPuerto, never())
				.guardar(any(Usuario.class));
	}

	@Test
	void debeRechazarRolPropietarioNoConfigurado() {
		Usuario usuario = crearUsuarioValido();

		when(usuarioPersistenciaPuerto
				.existePorCorreo(usuario.getCorreo()))
				.thenReturn(false);

		when(usuarioPersistenciaPuerto
				.existePorDocumentoIdentidad(
						usuario.getDocumentoIdentidad()))
				.thenReturn(false);

		when(rolPersistenciaPuerto
				.buscarPorNombre(PerfilUsuario.PROPIETARIO))
				.thenReturn(Optional.empty());

		ExcepcionUsuarios excepcion = assertThrows(
				ExcepcionUsuarios.class,
				() -> crearPropietarioCasoUso.crear(usuario));

		assertEquals(
				CodigosError.ROL_PROPIETARIO_NO_CONFIGURADO,
				excepcion.getCodigo());

		verify(usuarioPersistenciaPuerto, never())
				.guardar(any(Usuario.class));
	}

	@Test
	void debeNormalizarCorreoAntesDeValidarYGuardar() {
		Usuario usuario = crearUsuarioValido();
		usuario.setCorreo("  Carlos.Ramirez@Correo.com  ");

		Rol rolPropietario = crearRolPropietario();

		when(usuarioPersistenciaPuerto
				.existePorCorreo("carlos.ramirez@correo.com"))
				.thenReturn(false);

		when(usuarioPersistenciaPuerto
				.existePorDocumentoIdentidad(
						usuario.getDocumentoIdentidad()))
				.thenReturn(false);

		when(rolPersistenciaPuerto
				.buscarPorNombre(PerfilUsuario.PROPIETARIO))
				.thenReturn(Optional.of(rolPropietario));

		when(cifradorClavePuerto.cifrar(usuario.getClave()))
				.thenReturn("clave-cifrada");

		when(usuarioPersistenciaPuerto.guardar(any(Usuario.class)))
				.thenAnswer(invocacion -> {
					Usuario usuarioGuardado = invocacion.getArgument(0);
					usuarioGuardado.setId(1L);
					return usuarioGuardado;
				});

		Usuario resultado = crearPropietarioCasoUso.crear(usuario);

		assertEquals(
				"carlos.ramirez@correo.com",
				resultado.getCorreo());

		verify(usuarioPersistenciaPuerto)
				.existePorCorreo("carlos.ramirez@correo.com");

		verify(usuarioPersistenciaPuerto)
				.guardar(usuario);
	}

	private Usuario crearUsuarioValido() {
		Usuario usuario = new Usuario();

		usuario.setNombre("Carlos");
		usuario.setApellido("Ramirez");
		usuario.setDocumentoIdentidad("123456789");
		usuario.setCelular("+573001234567");
		usuario.setFechaNacimiento(LocalDate.of(1990, 5, 20));
		usuario.setCorreo("carlos.ramirez@correo.com");
		usuario.setClave("ClaveSegura123");

		return usuario;
	}

	private Rol crearRolPropietario() {
		return new Rol(
				2L,
				PerfilUsuario.PROPIETARIO,
				"Usuario propietario de restaurantes",
				Boolean.TRUE);
	}
}