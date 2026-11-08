package co.edu.clase.microservicio_usuarios.aplicacion.dto.solicitud;

import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesValidacion;
import co.edu.clase.microservicio_usuarios.util.constantes.MensajesError;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CrearPropietarioSolicitudDto {

    @NotBlank(message = MensajesError.NOMBRE_OBLIGATORIO)
    @Size(
            max = ConstantesValidacion.LONGITUD_MAXIMA_NOMBRE,
            message = MensajesError.NOMBRE_LONGITUD_INVALIDA
    )
    private String nombre;

    @NotBlank(message = MensajesError.APELLIDO_OBLIGATORIO)
    @Size(
            max = ConstantesValidacion.LONGITUD_MAXIMA_APELLIDO,
            message = MensajesError.APELLIDO_LONGITUD_INVALIDA
    )
    private String apellido;

    @NotBlank(message = MensajesError.DOCUMENTO_OBLIGATORIO)
    @Size(
            max = ConstantesValidacion.LONGITUD_MAXIMA_DOCUMENTO,
            message = MensajesError.DOCUMENTO_LONGITUD_INVALIDA
    )
    @Pattern(
            regexp = ConstantesValidacion.EXPRESION_DOCUMENTO,
            message = MensajesError.DOCUMENTO_FORMATO_INVALIDO
    )
    private String documentoIdentidad;

    @NotBlank(message = MensajesError.CELULAR_OBLIGATORIO)
    @Pattern(
            regexp = ConstantesValidacion.EXPRESION_CELULAR,
            message = MensajesError.CELULAR_FORMATO_INVALIDO
    )
    private String celular;

    @NotNull(message = MensajesError.FECHA_NACIMIENTO_OBLIGATORIA)
    @Past(message = MensajesError.FECHA_NACIMIENTO_INVALIDA)
    private LocalDate fechaNacimiento;

    @NotBlank(message = MensajesError.CORREO_OBLIGATORIO)
    @Email(message = MensajesError.CORREO_FORMATO_INVALIDO)
    @Size(
            max = ConstantesValidacion.LONGITUD_MAXIMA_CORREO,
            message = MensajesError.CORREO_LONGITUD_INVALIDA
    )
    private String correo;

    @NotBlank(message = MensajesError.CLAVE_OBLIGATORIA)
    @Size(
            min = ConstantesValidacion.LONGITUD_MINIMA_CLAVE,
            max = ConstantesValidacion.LONGITUD_MAXIMA_CLAVE,
            message = MensajesError.CLAVE_LONGITUD_INVALIDA
    )
    private String clave;

    public CrearPropietarioSolicitudDto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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