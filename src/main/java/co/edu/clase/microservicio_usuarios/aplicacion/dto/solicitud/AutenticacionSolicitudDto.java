package co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud;

import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AutenticacionSolicitudDto {

    @NotBlank(message = MensajesError.CORREO_OBLIGATORIO)
    @Email(message = MensajesError.CORREO_FORMATO_INVALIDO)
    private String correo;

    @NotBlank(message = MensajesError.CLAVE_OBLIGATORIA)
    private String clave;

    public AutenticacionSolicitudDto() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}