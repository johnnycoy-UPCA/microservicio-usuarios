package co.edu.clase.microservicio_usuarios.util.constantes;

public final class ConstantesSeguridad {

    public static final String PREFIJO_TOKEN = "Bearer ";
    public static final String ENCABEZADO_AUTORIZACION = "Authorization";
    public static final String RECLAMO_USUARIO_ID = "usuarioId";
    public static final String RECLAMO_PERFIL = "perfil";
    public static final long DURACION_TOKEN_MILISEGUNDOS = 3_600_000L;
    public static final String TIPO_TOKEN = "Bearer";
    public static final String EMISOR_TOKEN = "microservicio-usuarios";
    public static final String ATRIBUTO_CODIGO_ERROR = "codigoErrorAutenticacion";
    public static final String ATRIBUTO_MENSAJE_ERROR = "mensajeErrorAutenticacion";
    
    private ConstantesSeguridad() {
        throw new IllegalStateException(
            "No se permite crear instancias de una clase de constantes"
        );
    }
}