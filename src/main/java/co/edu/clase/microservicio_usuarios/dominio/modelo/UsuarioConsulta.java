package co.edu.clase.microservicio_usuarios.dominio.modelo;

import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

public class UsuarioConsulta {

    private Long id;
    private PerfilUsuario perfil;
    private Boolean activo;

    public UsuarioConsulta() {
    }

    public UsuarioConsulta(
            Long id,
            PerfilUsuario perfil,
            Boolean activo) {

        this.id = id;
        this.perfil = perfil;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public Boolean getActivo() {
        return activo;
    }
}