package co.edu.clase.microservicio_usuarios.dominio.modelo;

import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

public class EmpleadoValidacion {

    private Long usuarioId;
    private PerfilUsuario perfil;
    private Long restauranteId;
    private Boolean activo;

    public EmpleadoValidacion() {
    }

    public EmpleadoValidacion(
            Long usuarioId,
            PerfilUsuario perfil,
            Long restauranteId,
            Boolean activo) {

        this.usuarioId = usuarioId;
        this.perfil = perfil;
        this.restauranteId = restauranteId;
        this.activo = activo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public Boolean getActivo() {
        return activo;
    }
}