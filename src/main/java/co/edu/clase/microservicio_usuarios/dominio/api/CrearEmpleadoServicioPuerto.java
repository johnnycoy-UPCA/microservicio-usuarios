package co.edu.clase.microservicio_usuarios.dominio.api;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;

public interface CrearEmpleadoServicioPuerto {
    
    Usuario crear(
        Usuario empleado,
        String token
    );
}
