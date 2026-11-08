package co.edu.clase.microservicio_usuarios.infraestructura.salida.seguridad;

import co.edu.clase.microservicio_usuarios.dominio.spi.CifradorClavePuerto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CifradorClaveBCryptAdaptador
        implements CifradorClavePuerto {

    private final PasswordEncoder codificadorClave;

    public CifradorClaveBCryptAdaptador(
            PasswordEncoder codificadorClave) {

        this.codificadorClave = codificadorClave;
    }

    @Override
    public String cifrar(String claveSinCifrar) {
        return codificadorClave.encode(claveSinCifrar);
    }

    @Override
    public boolean coincide(
            String claveSinCifrar,
            String claveCifrada) {

        return codificadorClave.matches(
                claveSinCifrar,
                claveCifrada
        );
    }
}