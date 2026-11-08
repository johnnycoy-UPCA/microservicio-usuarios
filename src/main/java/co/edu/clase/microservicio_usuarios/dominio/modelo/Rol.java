package co.edu.clase.microservicio_usuarios.dominio.modelo;

import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

public class Rol {

    private Long id;
    private PerfilUsuario nombre;
    private String descripcion;
    private Boolean activo;

    public Rol() {
    }

    public Rol(
            Long id,
            PerfilUsuario nombre,
            String descripcion,
            Boolean activo) {

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PerfilUsuario getNombre() {
        return nombre;
    }

    public void setNombre(PerfilUsuario nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}