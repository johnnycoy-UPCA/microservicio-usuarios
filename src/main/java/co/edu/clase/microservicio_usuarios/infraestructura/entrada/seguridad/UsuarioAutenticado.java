package co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad;

import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

public class UsuarioAutenticado {

    private final Long usuarioId;
    private final String correo;
    private final PerfilUsuario perfil;

    public UsuarioAutenticado(
            Long usuarioId,
            String correo,
            PerfilUsuario perfil) {

        this.usuarioId = usuarioId;
        this.correo = correo;
        this.perfil = perfil;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getCorreo() {
        return correo;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }
}