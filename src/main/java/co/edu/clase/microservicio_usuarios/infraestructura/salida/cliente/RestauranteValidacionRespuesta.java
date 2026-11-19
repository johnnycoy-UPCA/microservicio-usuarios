package co.edu.clase.microservicio_usuarios.infraestructura.salida.cliente;

public class RestauranteValidacionRespuesta {

    private Long id;
    private Long propietarioId;
    private Boolean activo;

    public RestauranteValidacionRespuesta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(Long propietarioId) {
        this.propietarioId = propietarioId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}