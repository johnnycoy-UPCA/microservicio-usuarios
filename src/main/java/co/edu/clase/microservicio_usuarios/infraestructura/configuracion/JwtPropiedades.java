package co.edu.clase.microservicio_usuarios.infraestructura.configuracion;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "seguridad.jwt")
public class JwtPropiedades {

    private String secreto;
    private long duracionMilisegundos;

    public String getSecreto() {
        return secreto;
    }

    public void setSecreto(String secreto) {
        this.secreto = secreto;
    }

    public long getDuracionMilisegundos() {
        return duracionMilisegundos;
    }

    public void setDuracionMilisegundos(
            long duracionMilisegundos) {

        this.duracionMilisegundos = duracionMilisegundos;
    }
}