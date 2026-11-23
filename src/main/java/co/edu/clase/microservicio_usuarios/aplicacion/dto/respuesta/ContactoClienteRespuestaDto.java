package co.edu.clase.microservicio_usuarios.aplicacion.dto.respuesta;

public class ContactoClienteRespuestaDto {

    private Long usuarioId;
    private String celular;
    private Boolean activo;

    public ContactoClienteRespuestaDto() {
    }

    public ContactoClienteRespuestaDto(
            Long usuarioId,
            String celular,
            Boolean activo) {

        this.usuarioId = usuarioId;
        this.celular = celular;
        this.activo = activo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getCelular() {
        return celular;
    }

    public Boolean getActivo() {
        return activo;
    }
}