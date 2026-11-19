package co.edu.clase.microservicio_usuarios.dominio.spi;

import co.edu.clase.microservicio_usuarios.dominio.modelo.RestauranteValidacion;

public interface RestauranteConsultaPuerto {
    
    RestauranteValidacion validarPropiedad(
        Long restauranteId,
        String token
    );
}
