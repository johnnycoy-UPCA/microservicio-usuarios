package co.edu.clase.microservicio_usuarios.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.UsuarioRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.CrearPropietarioSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.handler.CrearPropietarioHandler;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public UsuarioControladorRest(
            CrearPropietarioHandler crearPropietarioHandler) {

        this.crearPropietarioHandler = crearPropietarioHandler;
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
}