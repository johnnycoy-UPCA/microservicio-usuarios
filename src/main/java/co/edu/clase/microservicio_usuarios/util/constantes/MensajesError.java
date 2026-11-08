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

	public static final String NOMBRE_OBLIGATORIO =
		"El nombre es obligatorio";

	public static final String NOMBRE_LONGITUD_INVALIDA =
		"El nombre no puede superar los 50 caracteres";

	public static final String APELLIDO_OBLIGATORIO =
		"El apellido es obligatorio";

	public static final String APELLIDO_LONGITUD_INVALIDA =
		"El apellido no puede superar los 50 caracteres";

	public static final String DOCUMENTO_OBLIGATORIO =
		"El documento de identidad es obligatorio";

	public static final String DOCUMENTO_FORMATO_INVALIDO =
		"El documento de identidad debe contener únicamente números";

	public static final String DOCUMENTO_LONGITUD_INVALIDA =
		"El documento de identidad no puede superar los 20 caracteres";

	public static final String CELULAR_OBLIGATORIO =
		"El celular es obligatorio";

	public static final String CELULAR_FORMATO_INVALIDO =
		"El celular debe contener únicamente números y puede iniciar con el signo más";

	public static final String FECHA_NACIMIENTO_INVALIDA =
		"La fecha de nacimiento debe ser anterior a la fecha actual";

	public static final String CORREO_OBLIGATORIO =
		"El correo es obligatorio";

	public static final String CORREO_FORMATO_INVALIDO =
		"El correo ingresado no tiene un formato válido";

	public static final String CORREO_LONGITUD_INVALIDA =
		"El correo no puede superar los 100 caracteres";

	public static final String CLAVE_LONGITUD_INVALIDA =
		"La clave debe contener entre 8 y 50 caracteres";

	public static final String SOLICITUD_INVALIDA =
		"La solicitud contiene campos inválidos";

	public static final String CUERPO_SOLICITUD_INVALIDO =
		"El cuerpo de la solicitud no tiene un formato válido";
			
	private MensajesError() {
		throw new IllegalStateException(
				"No se permite crear instancias de una clase de constantes");
	}
}