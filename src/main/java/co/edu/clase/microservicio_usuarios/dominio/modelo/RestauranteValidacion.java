package co.edu.clase.microservicio_usuarios.dominio.modelo;

public class RestauranteValidacion {

    private Long id;
    private Long propietarioId;
    private Boolean activo;

    public RestauranteValidacion() {
    }

    public RestauranteValidacion(
            Long id,
            Long propietarioId,
            Boolean activo) {

        this.id = id;
        this.propietarioId = propietarioId;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public Long getPropietarioId() {
        return propietarioId;
    }

    public Boolean getActivo() {
        return activo;
    }
}
