package co.edu.clase.microservicio_usuarios.infraestructura.configuracion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "seguridad.interna")
public class SeguridadInternaPropiedades {

    @NotBlank(
            message = "USUARIOS_API_KEY es obligatoria"
    )
    @Size(
            min = 24,
            message =
                    "USUARIOS_API_KEY debe tener "
                            + "al menos 24 caracteres"
    )
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}