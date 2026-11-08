package co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.mapeador;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Rol;
import co.edu.clase.microservicio_usuarios.infraestructura.salida.persistencia.entidad.RolEntidad;
import org.springframework.stereotype.Component;

@Component
public class RolPersistenciaMapeador {

    public Rol aModelo(RolEntidad entidad) {
        if (entidad == null) {
            return null;
        }

        return new Rol(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.getActivo()
        );
    }

    public RolEntidad aEntidad(Rol modelo) {
        if (modelo == null) {
            return null;
        }

        return new RolEntidad(
                modelo.getId(),
                modelo.getNombre(),
                modelo.getDescripcion(),
                modelo.getActivo()
        );
    }
}