package co.edu.clase.microservicio_usuarios.dominio.spi;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.EstadoToken;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;

public interface TokenSeguridadPuerto {

    String generar(Usuario usuario);

    String extraerCorreo(String token);

    Long extraerUsuarioId(String token);

    PerfilUsuario extraerPerfil(String token);

    // boolean esValido(String token);
    EstadoToken validar(String token);

    long obtenerDuracionSegundos();
}