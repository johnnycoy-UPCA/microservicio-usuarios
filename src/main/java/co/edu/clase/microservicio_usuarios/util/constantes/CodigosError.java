package co.edu.clase.microservicio_usuarios.util.constantes;

public final class CodigosError {
    public static final String USUARIO_NO_ENCONTRADO = "USR-001";
    public static final String CORREO_YA_REGISTRADO = "USR-002";
    public static final String DOCUMENTO_YA_REGISTRADO = "USR-003";
    public static final String USUARIO_MENOR_EDAD = "USR-004";
    public static final String ROL_NO_ENCONTRADO = "ROL-001";
    public static final String ROL_PROPIETARIO_NO_CONFIGURADO = "ROL-002";
    public static final String ERROR_CIFRANDO_CLAVE = "SEG-001";
    public static final String ERROR_INTERNO = "GEN-001";
    public static final String FECHA_NACIMIENTO_OBLIGATORIA = "USR-005";
    public static final String CLAVE_OBLIGATORIA = "USR-006";
    public static final String SOLICITUD_INVALIDA = "VAL-001";
    public static final String CUERPO_SOLICITUD_INVALIDO = "VAL-002";

    private CodigosError() {
        throw new IllegalStateException(
                "No se permite crear instancias de una clase de constantes"
        );
    }    
}
