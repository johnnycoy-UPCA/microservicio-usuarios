package co.edu.clase.microservicio_usuarios.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.UsuarioConsultaRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.UsuarioRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.CrearEmpleadoSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.CrearPropietarioSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.RegistrarClienteSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.handler.ConsultarUsuarioHandler;
import co.edu.clase.microservicio_usuarios.aplicacion.handler.CrearEmpleadoHandler;
import co.edu.clase.microservicio_usuarios.aplicacion.handler.CrearPropietarioHandler;
import co.edu.clase.microservicio_usuarios.aplicacion.handler.RegistrarClienteHandler;
import co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad.UsuarioAutenticado;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(
	name = "Usuarios",
	description = "Operaciones para la gestión de usuarios"
)

@RestController
@RequestMapping(ConstantesApi.API_USUARIOS)
public class UsuarioControladorRest {

    private final CrearPropietarioHandler crearPropietarioHandler;
	private final ConsultarUsuarioHandler consultarUsuarioHandler;
	private final RegistrarClienteHandler registrarClienteHandler;
	private final CrearEmpleadoHandler crearEmpleadoHandler;

    public UsuarioControladorRest(
            CrearPropietarioHandler crearPropietarioHandler,
            ConsultarUsuarioHandler consultarUsuarioHandler,
            RegistrarClienteHandler registrarClienteHandler,
			CrearEmpleadoHandler crearEmpleadoHandler) {

        this.crearPropietarioHandler = crearPropietarioHandler;
        this.consultarUsuarioHandler = consultarUsuarioHandler;
        this.registrarClienteHandler = registrarClienteHandler;
		this.crearEmpleadoHandler = crearEmpleadoHandler;
    }

    @Operation(
		summary = "Crear propietario",
		description =
				"Crea una cuenta con perfil PROPIETARIO. "
				+ "El correo y el documento deben ser únicos, "
				+ "y la persona debe ser mayor de edad."
    )

	@SecurityRequirement(name = "autenticacionBearer")
    @ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "Propietario creado correctamente"
		),
		@ApiResponse(
			responseCode = "400",
			description = "Solicitud inválida o propietario menor de edad"
		),
		@ApiResponse(
			responseCode = "401",
			description = "Token ausente o inválido"
		),
		@ApiResponse(
			responseCode = "403",
			description = "El usuario no tiene perfil administrador"
		),
		@ApiResponse(
			responseCode = "409",
			description = "Correo o documento ya registrado"
		),
		@ApiResponse(
			responseCode = "500",
			description = "Error interno del microservicio"
		)
    })
    @PostMapping(ConstantesApi.PROPIETARIOS)
    public ResponseEntity<UsuarioRespuestaDto> crearPropietario(
            @Valid @RequestBody
            CrearPropietarioSolicitudDto solicitud) {

        UsuarioRespuestaDto respuesta =
			crearPropietarioHandler.ejecutar(solicitud);

        return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(respuesta);
    }

	@Operation(
		summary = "Consultar usuario para validación interna",
		description =
			"Consulta el perfil y estado de un usuario. "
			+ "La operación está destinada a la comunicación "
			+ "entre microservicios y requiere perfil ADMINISTRADOR."
	)
	@SecurityRequirement(name = "autenticacionBearer")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Usuario consultado correctamente"
		),
		@ApiResponse(
			responseCode = "401",
			description = "Token ausente, inválido o expirado"
		),
		@ApiResponse(
			responseCode = "403",
			description = "El usuario autenticado no tiene perfil ADMINISTRADOR"
		),
		@ApiResponse(
			responseCode = "404",
			description = "Usuario no encontrado"
		),
		@ApiResponse(
			responseCode = "500",
			description = "Error interno del microservicio"
		)
	})	
	@GetMapping(ConstantesApi.VALIDACION_USUARIO)
	public ResponseEntity<UsuarioConsultaRespuestaDto>
			consultarUsuario(
					@PathVariable Long usuarioId) {

		return ResponseEntity.ok(
				consultarUsuarioHandler.ejecutar(usuarioId)
		);
	}

	@Operation(
		summary = "Registrar cliente",
		description =
				"Registra públicamente un usuario con perfil CLIENTE"
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "Cliente registrado correctamente"
		),
		@ApiResponse(
			responseCode = "400",
			description = "Solicitud inválida"
		),
		@ApiResponse(
			responseCode = "409",
			description = "Correo o documento ya registrado"
		),
		@ApiResponse(
			responseCode = "500",
			description = "Error interno del microservicio"
		)
	})
	@PostMapping(ConstantesApi.CLIENTES)
	public ResponseEntity<UsuarioRespuestaDto>
			registrarCliente(
					@Valid @RequestBody
					RegistrarClienteSolicitudDto solicitud) {

		UsuarioRespuestaDto respuesta =
				registrarClienteHandler.ejecutar(solicitud);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(respuesta);
	}

	@Operation(
		summary = "Crear empleado",
		description =
			"Crea un empleado asociado a un restaurante "
			+ "del propietario autenticado"
	)
	@SecurityRequirement(name = "autenticacionBearer")
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "Empleado creado correctamente"
		),
		@ApiResponse(
			responseCode = "400",
			description = "Solicitud o restaurante inválido"
		),
		@ApiResponse(
			responseCode = "401",
			description = "Token ausente, inválido o expirado"
		),
		@ApiResponse(
			responseCode = "403",
			description = "Perfil no autorizado o restaurante ajeno"
		),
		@ApiResponse(
			responseCode = "409",
			description = "Correo o documento ya registrado"
		),
		@ApiResponse(
			responseCode = "502",
			description = "Microservicio Plazoleta no disponible"
		)
	})
	@PostMapping(ConstantesApi.EMPLEADOS)
	public ResponseEntity<UsuarioRespuestaDto>
			crearEmpleado(
					@Valid @RequestBody
					CrearEmpleadoSolicitudDto solicitud,
					@AuthenticationPrincipal
					UsuarioAutenticado usuarioAutenticado) {

		UsuarioRespuestaDto respuesta =
				crearEmpleadoHandler.ejecutar(
						solicitud,
						usuarioAutenticado.getToken()
				);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(respuesta);
	}
}