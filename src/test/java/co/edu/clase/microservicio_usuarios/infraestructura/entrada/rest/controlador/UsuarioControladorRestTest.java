package co.edu.clase.microservicio_usuarios.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.UsuarioRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.CrearPropietarioSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.handler.CrearPropietarioHandler;
import co.edu.clase.microservicio_usuarios.infraestructura.entrada.rest.manejador.ManejadorGlobalExcepciones;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
// import jakarta.validation.Validation;
// import jakarta.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UsuarioControladorRestTest {

    @Mock
    private CrearPropietarioHandler crearPropietarioHandler;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void configurar() {
        UsuarioControladorRest controlador =
                new UsuarioControladorRest(crearPropietarioHandler);

        // Validator validador = Validation
        //         .buildDefaultValidatorFactory()
        //         .getValidator();

        LocalValidatorFactoryBean validador =
                new LocalValidatorFactoryBean();

        validador.afterPropertiesSet();
        
        mockMvc = MockMvcBuilders
                .standaloneSetup(controlador)
                .setControllerAdvice(
                        new ManejadorGlobalExcepciones()
                )
                .setValidator(validador)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void debeCrearPropietarioYResponderCreado() throws Exception {
        CrearPropietarioSolicitudDto solicitud =
                crearSolicitudValida();

        UsuarioRespuestaDto respuesta =
                crearRespuestaValida();

        when(crearPropietarioHandler.ejecutar(
                any(CrearPropietarioSolicitudDto.class)
        )).thenReturn(respuesta);

        mockMvc.perform(post(
                        ConstantesApi.API_USUARIOS
                                + ConstantesApi.PROPIETARIOS
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                solicitud
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.perfil")
                        .value("PROPIETARIO"))
                .andExpect(jsonPath("$.activo").value(true))
                .andExpect(jsonPath("$.clave").doesNotExist());

        verify(crearPropietarioHandler)
                .ejecutar(any(
                        CrearPropietarioSolicitudDto.class
                ));
    }

    @Test
    void debeRechazarSolicitudConDocumentoInvalido()
            throws Exception {

        CrearPropietarioSolicitudDto solicitud =
                crearSolicitudValida();

        solicitud.setDocumentoIdentidad("ABC123");

        mockMvc.perform(post(
                        ConstantesApi.API_USUARIOS
                                + ConstantesApi.PROPIETARIOS
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                solicitud
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo")
                        .value("VAL-001"))
                .andExpect(jsonPath(
                        "$.detalles.documentoIdentidad"
                ).exists());
    }

    @Test
    void debeRechazarCuerpoJsonInvalido() throws Exception {
        String cuerpoInvalido = """
                {
                  "nombre": "Ana",
                  "apellido": "Martinez",
                }
                """;

        mockMvc.perform(post(
                        ConstantesApi.API_USUARIOS
                                + ConstantesApi.PROPIETARIOS
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo")
                        .value("VAL-002"));
    }

    private CrearPropietarioSolicitudDto crearSolicitudValida() {
        CrearPropietarioSolicitudDto solicitud =
                new CrearPropietarioSolicitudDto();

        solicitud.setNombre("Ana");
        solicitud.setApellido("Martinez");
        solicitud.setDocumentoIdentidad("1020304050");
        solicitud.setCelular("+573001234567");
        solicitud.setFechaNacimiento(
                LocalDate.of(1990, 5, 20)
        );
        solicitud.setCorreo("ana.martinez@correo.com");
        solicitud.setClave("ClaveSegura123");

        return solicitud;
    }

    private UsuarioRespuestaDto crearRespuestaValida() {
        UsuarioRespuestaDto respuesta =
                new UsuarioRespuestaDto();

        respuesta.setId(1L);
        respuesta.setNombre("Ana");
        respuesta.setApellido("Martinez");
        respuesta.setDocumentoIdentidad("1020304050");
        respuesta.setCelular("+573001234567");
        respuesta.setFechaNacimiento(
                LocalDate.of(1990, 5, 20)
        );
        respuesta.setCorreo("ana.martinez@correo.com");
        respuesta.setPerfil(PerfilUsuario.PROPIETARIO);
        respuesta.setActivo(Boolean.TRUE);
        respuesta.setFechaCreacion(
                LocalDateTime.of(2026, 11, 8, 11, 21)
        );

        return respuesta;
    }
}