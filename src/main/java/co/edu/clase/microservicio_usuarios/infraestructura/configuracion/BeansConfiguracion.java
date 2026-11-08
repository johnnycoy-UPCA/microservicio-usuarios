package co.edu.clase.microservicio_usuarios.infraestructura.configuracion;

import co.edu.clase.microservicio_usuarios.dominio.api.CrearPropietarioServicioPuerto;
import co.edu.clase.microservicio_usuarios.dominio.casouso.CrearPropietarioCasoUso;
import co.edu.clase.microservicio_usuarios.dominio.spi.CifradorClavePuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.RolPersistenciaPuerto;
import co.edu.clase.microservicio_usuarios.dominio.spi.UsuarioPersistenciaPuerto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

@Configuration
public class BeansConfiguracion {

    @Bean
    public Clock relojAplicacion() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public PasswordEncoder codificadorClave() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CrearPropietarioServicioPuerto crearPropietarioServicioPuerto(
            UsuarioPersistenciaPuerto usuarioPersistenciaPuerto,
            RolPersistenciaPuerto rolPersistenciaPuerto,
            CifradorClavePuerto cifradorClavePuerto,
            Clock relojAplicacion) {

        return new CrearPropietarioCasoUso(
                usuarioPersistenciaPuerto,
                rolPersistenciaPuerto,
                cifradorClavePuerto,
                relojAplicacion
        );
    }
}