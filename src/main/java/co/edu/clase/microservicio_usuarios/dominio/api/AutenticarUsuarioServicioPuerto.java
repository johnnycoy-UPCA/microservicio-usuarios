package co.edu.clase.microservicio_usuarios.dominio.api;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Autenticacion;

public interface AutenticarUsuarioServicioPuerto {

    Autenticacion autenticar(String correo, String clave);
}