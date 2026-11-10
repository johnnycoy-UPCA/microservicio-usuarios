package co.edu.clase.microservicio_usuarios.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta.AutenticacionRespuestaDto;
import co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud.AutenticacionSolicitudDto;
import co.edu.clase.microservicio_usuarios.aplicacion.handler.AutenticarUsuarioHandler;
import co.edu.clase.microservicio_usuarios.infraestructura.entrada.rest.manejador.ManejadorGlobalExcepciones;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AutenticacionControladorRestTest {

    @Mock
    private AutenticarUsuarioHandler autenticarUsuarioHandler;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void configurar() {
        AutenticacionControladorRest controlador =
                new AutenticacionControladorRest(
                        autenticarUsuarioHandler
                );

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
    void debeAutenticarUsuarioYResponderToken()
            throws Exception {

        AutenticacionRespuestaDto respuesta =
                crearRespuesta();

        when(autenticarUsuarioHandler.ejecutar(
                any(AutenticacionSolicitudDto.class)
        )).thenReturn(respuesta);

        AutenticacionSolicitudDto solicitud =
                new AutenticacionSolicitudDto();

        solicitud.setCorreo("admin@plazoleta.com");
        solicitud.setClave("ClaveSegura123");

        mockMvc.perform(post(ConstantesApi.AUTENTICACION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                solicitud
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("token-generado"))
                .andExpect(jsonPath("$.tipo")
                        .value("Bearer"))
                .andExpect(jsonPath("$.duracionSegundos")
                        .value(3600))
                .andExpect(jsonPath("$.perfil")
                        .value("ADMINISTRADOR"))
                .andExpect(jsonPath("$.clave").doesNotExist());

        verify(autenticarUsuarioHandler)
                .ejecutar(any(
                        AutenticacionSolicitudDto.class
                ));
    }

    @Test
    void debeRechazarCorreoConFormatoInvalido()
            throws Exception {

        AutenticacionSolicitudDto solicitud =
                new AutenticacionSolicitudDto();

        solicitud.setCorreo("correo-invalido");
        solicitud.setClave("ClaveSegura123");

        mockMvc.perform(post(ConstantesApi.AUTENTICACION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                solicitud
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo")
                        .value("VAL-001"))
                .andExpect(jsonPath("$.detalles.correo")
                        .exists());
    }

    private AutenticacionRespuestaDto crearRespuesta() {
        AutenticacionRespuestaDto respuesta =
                new AutenticacionRespuestaDto();

        respuesta.setToken("token-generado");
        respuesta.setTipo("Bearer");
        respuesta.setDuracionSegundos(3600L);
        respuesta.setUsuarioId(1L);
        respuesta.setNombre("Administrador Plazoleta");
        respuesta.setCorreo("admin@plazoleta.com");
        respuesta.setPerfil(PerfilUsuario.ADMINISTRADOR);

        return respuesta;
    }
}