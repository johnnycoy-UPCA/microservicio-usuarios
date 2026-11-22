package co.edu.clase.microservicio_usuarios.dominio.api;

import co.edu.clase.microservicio_usuarios.dominio.modelo.EmpleadoValidacion;

public interface ValidarEmpleadoServicioPuerto {

    EmpleadoValidacion validar(
            Long usuarioId,
            Long usuarioAutenticadoId
    );
}