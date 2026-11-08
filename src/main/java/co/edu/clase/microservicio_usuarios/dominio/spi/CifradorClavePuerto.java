package co.edu.clase.microservicio_usuarios.dominio.spi;

public interface CifradorClavePuerto {

    String cifrar(String claveSinCifrar);

    boolean coincide(String claveSinCifrar, String claveCifrada);
}