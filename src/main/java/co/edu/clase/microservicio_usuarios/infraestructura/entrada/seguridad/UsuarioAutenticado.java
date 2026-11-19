package co.edu.clase.microservicio_usuarios.infraestructura.entrada.seguridad;

import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

public class UsuarioAutenticado {

    private final Long usuarioId;
    private final String correo;
    private final PerfilUsuario perfil;
    private final String token;

    public UsuarioAutenticado(
            Long usuarioId,
            String correo,
            PerfilUsuario perfil,
            String token) {

        this.usuarioId = usuarioId;
        this.correo = correo;
        this.perfil = perfil;
        this.token = token;
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

    public String getToken() {
        return token;
    }
}