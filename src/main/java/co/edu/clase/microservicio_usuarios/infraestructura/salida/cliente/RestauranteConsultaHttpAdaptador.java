package co.edu.clase.microservicio_usuarios.infraestructura.salida.cliente;

import co.edu.clase.microservicio_usuarios.dominio.excepcion.ExcepcionUsuarios;
import co.edu.clase.microservicio_usuarios.dominio.modelo.RestauranteValidacion;
import co.edu.clase.microservicio_usuarios.dominio.spi.RestauranteConsultaPuerto;
import co.edu.clase.microservicio_usuarios.util.constantes.CodigosError;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.CategoriaError;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class RestauranteConsultaHttpAdaptador
        implements RestauranteConsultaPuerto {

    private final RestClient clientePlazoleta;

    public RestauranteConsultaHttpAdaptador(
            RestClient clientePlazoleta) {

        this.clientePlazoleta = clientePlazoleta;
    }

    @Override
    public RestauranteValidacion validarPropiedad(
            Long restauranteId,
            String token) {

        try {
            return clientePlazoleta.get()
                    .uri(
                            ConstantesApi
                                    .VALIDACION_RESTAURANTE_PROPIETARIO,
                            restauranteId
                    )
                    .header(
                            ConstantesSeguridad
                                    .ENCABEZADO_AUTORIZACION,
                            ConstantesSeguridad.PREFIJO_TOKEN
                                    + token
                    )
                    .exchange((solicitud, respuesta) -> {
                        if (respuesta.getStatusCode()
                                .is2xxSuccessful()) {

                            RestauranteValidacionRespuesta
                                    resultado =
                                    respuesta.bodyTo(
                                            RestauranteValidacionRespuesta
                                                    .class
                                    );

                            if (resultado == null) {
                                throw crearExcepcionRestaurante();
                            }

                            return new RestauranteValidacion(
                                    resultado.getId(),
                                    resultado.getPropietarioId(),
                                    resultado.getActivo()
                            );
                        }

                        if (respuesta.getStatusCode().value()
                                == 403
                                || respuesta.getStatusCode()
                                .value() == 404) {

                            throw crearExcepcionRestaurante();
                        }

                        throw new ExcepcionUsuarios(
                                CodigosError
                                        .SERVICIO_PLAZOLETA_NO_DISPONIBLE,
                                MensajesError
                                        .SERVICIO_PLAZOLETA_NO_DISPONIBLE,
                                CategoriaError
                                        .DEPENDENCIA_EXTERNA
                        );
                    });
        } catch (ExcepcionUsuarios excepcion) {
            throw excepcion;
        } catch (RestClientException excepcion) {
            throw new ExcepcionUsuarios(
                    CodigosError
                            .SERVICIO_PLAZOLETA_NO_DISPONIBLE,
                    MensajesError
                            .SERVICIO_PLAZOLETA_NO_DISPONIBLE,
                    CategoriaError.DEPENDENCIA_EXTERNA,
                    excepcion
            );
        }
    }

    private ExcepcionUsuarios
            crearExcepcionRestaurante() {

        return new ExcepcionUsuarios(
                CodigosError.RESTAURANTE_NO_VALIDO,
                MensajesError.RESTAURANTE_NO_VALIDO,
                CategoriaError.VALIDACION
        );
    }
}