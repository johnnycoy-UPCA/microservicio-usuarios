package co.edu.clase.microservicio_usuarios.dominio.excepcion;

import co.edu.clase.microservicio_usuarios.util.enumeraciones.CategoriaError;

import java.util.Collections;
import java.util.Map;

public class ExcepcionUsuarios extends RuntimeException {

    private final String codigo;
    private final CategoriaError categoria;
    private final Map<String, Object> detalles;

    public ExcepcionUsuarios(
            String codigo,
            String mensaje,
            CategoriaError categoria) {

        super(mensaje);
        this.codigo = codigo;
        this.categoria = categoria;
        this.detalles = Collections.emptyMap();
    }

    public ExcepcionUsuarios(
            String codigo,
            String mensaje,
            CategoriaError categoria,
            Map<String, Object> detalles) {

        super(mensaje);
        this.codigo = codigo;
        this.categoria = categoria;
        this.detalles = detalles == null
                ? Collections.emptyMap()
                : Map.copyOf(detalles);
    }

    public ExcepcionUsuarios(
            String codigo,
            String mensaje,
            CategoriaError categoria,
            Throwable causa) {

        super(mensaje, causa);
        this.codigo = codigo;
        this.categoria = categoria;
        this.detalles = Collections.emptyMap();
    }

    public String getCodigo() {
        return codigo;
    }

    public CategoriaError getCategoria() {
        return categoria;
    }

    public Map<String, Object> getDetalles() {
        return detalles;
    }
}