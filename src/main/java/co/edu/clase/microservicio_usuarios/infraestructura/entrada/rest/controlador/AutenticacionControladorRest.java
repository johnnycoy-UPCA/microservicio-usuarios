package co.edu.clase.microservicio_usuarios.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.AutenticacionRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.AutenticacionSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.handler.AutenticarUsuarioHandler;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Autenticacion",
        description = "Operaciones para la autenticacion de usuarios"
)
@RestController
public class AutenticacionControladorRest {

    private final AutenticarUsuarioHandler
            autenticarUsuarioHandler;

    public AutenticacionControladorRest(
            AutenticarUsuarioHandler
                    autenticarUsuarioHandler) {

        this.autenticarUsuarioHandler =
                autenticarUsuarioHandler;
    }

    @Operation(
            summary = "Autenticar usuario",
            description =
                    "Valida el correo y la clave del usuario "
                            + "y genera un token JWT"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario autenticado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas o usuario inactivo"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del microservicio"
            )
    })
    @PostMapping(ConstantesApi.AUTENTICACION)
    public ResponseEntity<AutenticacionRespuestaDto>
            autenticar(
                    @Valid @RequestBody
                    AutenticacionSolicitudDto solicitud) {

        return ResponseEntity.ok(
                autenticarUsuarioHandler.ejecutar(solicitud)
        );
    }
}