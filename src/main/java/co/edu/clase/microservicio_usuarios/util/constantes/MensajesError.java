package co.edu.clase.microservicio_usuarios.util.constantes;

public final class MensajesError {

        public static final String USUARIO_NO_ENCONTRADO =
                "El usuario solicitado no fue encontrado";

        public static final String CORREO_YA_REGISTRADO =
                "El correo ingresado ya se encuentra registrado";

        public static final String DOCUMENTO_YA_REGISTRADO =
                "El documento de identidad ingresado ya se encuentra registrado";

        public static final String USUARIO_MENOR_EDAD =
                "El propietario debe ser mayor de edad";

        public static final String ROL_NO_ENCONTRADO =
                "El rol solicitado no se encuentra configurado";

        public static final String ROL_PROPIETARIO_NO_CONFIGURADO =
                "El rol propietario no se encuentra configurado";

        public static final String ERROR_CIFRANDO_CLAVE =
                "No fue posible proteger la clave del usuario";

        public static final String ERROR_INTERNO =
                "Ocurrió un error interno al procesar la solicitud";

        public static final String FECHA_NACIMIENTO_OBLIGATORIA =
                "La fecha de nacimiento es obligatoria para crear un propietario";

        public static final String CLAVE_OBLIGATORIA =
                "La clave es obligatoria";
                
                
        private MensajesError() {
                throw new IllegalStateException(
                        "No se permite crear instancias de una clase de constantes"
                );
        }
}