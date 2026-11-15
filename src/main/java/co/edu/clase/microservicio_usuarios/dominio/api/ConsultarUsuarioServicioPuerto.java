package co.edu.clase.microservicio_usuarios.dominio.api;

import co.edu.clase.microservicio_usuarios.dominio.modelo.UsuarioConsulta;

public interface ConsultarUsuarioServicioPuerto {
        
    UsuarioConsulta consultarPorId(Long usuarioId);
}
