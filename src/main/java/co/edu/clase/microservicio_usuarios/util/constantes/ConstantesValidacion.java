package co.edu.clase.microservicio_usuarios.util.constantes;

public final class ConstantesValidacion {

    public static final int EDAD_MINIMA_PROPIETARIO = 18;

    public static final int LONGITUD_MAXIMA_NOMBRE = 50;
    public static final int LONGITUD_MAXIMA_APELLIDO = 50;
    public static final int LONGITUD_MAXIMA_DOCUMENTO = 20;
    public static final int LONGITUD_MAXIMA_CELULAR = 13;
    public static final int LONGITUD_MAXIMA_CORREO = 100;
    public static final int LONGITUD_MAXIMA_CLAVE_CIFRADA = 100;

    public static final String EXPRESION_DOCUMENTO =
            "^[0-9]+$";

    public static final String EXPRESION_CELULAR =
            "^\\+?[0-9]{1,12}$";

    private ConstantesValidacion() {
        throw new IllegalStateException(
                "No se permite crear instancias de una clase de constantes"
        );
    }
}