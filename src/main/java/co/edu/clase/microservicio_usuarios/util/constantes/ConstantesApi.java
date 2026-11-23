package co.edu.clase.microservicio_usuarios.util.constantes;

public final class ConstantesApi {

    public static final String API_USUARIOS = "/api/v1/usuarios";
    public static final String PROPIETARIOS = "/propietarios";
    public static final String AUTENTICACION = "/api/v1/autenticacion";
    public static final String VALIDACION_USUARIO = "/{usuarioId}/validacion";
    public static final String CLIENTES = "/clientes";
    public static final String EMPLEADOS = "/empleados";
    public static final String VALIDACION_RESTAURANTE_PROPIETARIO = "/api/v1/restaurantes/{restauranteId}"
                                                                    + "/validacion-propietario";
    public static final String VALIDACION_EMPLEADO = "/{usuarioId}/validacion-empleado";
    public static final String CONTACTO_USUARIO = "/{usuarioId}/contacto";

    private ConstantesApi() {
        throw new IllegalStateException(
            "No se permite crear instancias de una clase de constantes"
        );
    }
}