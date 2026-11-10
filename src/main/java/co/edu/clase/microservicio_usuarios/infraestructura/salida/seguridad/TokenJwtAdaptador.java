package co.edu.clase.microservicio_usuarios.infraestructura.salida.seguridad;

import co.edu.clase.microservicio_usuarios.dominio.modelo.Usuario;
import co.edu.clase.microservicio_usuarios.dominio.spi.TokenSeguridadPuerto;
import co.edu.clase.microservicio_usuarios.infraestructura.configuracion.JwtPropiedades;
import co.edu.clase.microservicio_usuarios.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.EstadoToken;
import co.edu.clase.microservicio_usuarios.util.enumeraciones.PerfilUsuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Component
public class TokenJwtAdaptador
        implements TokenSeguridadPuerto {

    private final JwtPropiedades jwtPropiedades;
    private final Clock reloj;

    public TokenJwtAdaptador(
            JwtPropiedades jwtPropiedades,
            Clock reloj) {

        this.jwtPropiedades = jwtPropiedades;
        this.reloj = reloj;
    }

    @Override
    public String generar(Usuario usuario) {
        Instant fechaEmision = reloj.instant();

        Instant fechaExpiracion = fechaEmision.plusMillis(
                jwtPropiedades.getDuracionMilisegundos()
        );

        return Jwts.builder()
                .issuer(ConstantesSeguridad.EMISOR_TOKEN)
                .subject(usuario.getCorreo())
                .claim(
                        ConstantesSeguridad.RECLAMO_USUARIO_ID,
                        usuario.getId()
                )
                .claim(
                        ConstantesSeguridad.RECLAMO_PERFIL,
                        usuario.getRol().getNombre().name()
                )
                .issuedAt(Date.from(fechaEmision))
                .expiration(Date.from(fechaExpiracion))
                .signWith(obtenerClaveFirma())
                .compact();
    }

    @Override
    public String extraerCorreo(String token) {
        return extraerReclamos(token).getSubject();
    }

    @Override
    public Long extraerUsuarioId(String token) {
        Number usuarioId = extraerReclamos(token).get(
                ConstantesSeguridad.RECLAMO_USUARIO_ID,
                Number.class
        );

        return usuarioId.longValue();
    }

    @Override
    public PerfilUsuario extraerPerfil(String token) {
        String perfil = extraerReclamos(token).get(
                ConstantesSeguridad.RECLAMO_PERFIL,
                String.class
        );

        return PerfilUsuario.valueOf(perfil);
    }

    @Override
    public EstadoToken validar(String token) {
        try {
            Claims reclamos = extraerReclamos(token);

            boolean emisorCorrecto =
                    ConstantesSeguridad.EMISOR_TOKEN.equals(
                            reclamos.getIssuer()
                    );

            if (!emisorCorrecto) {
                return EstadoToken.INVALIDO;
            }

            return EstadoToken.VALIDO;
        } catch (ExpiredJwtException excepcion) {
            return EstadoToken.EXPIRADO;
        } catch (JwtException
                | IllegalArgumentException excepcion) {

            return EstadoToken.INVALIDO;
        }
    }

    @Override
    public long obtenerDuracionSegundos() {
        return jwtPropiedades.getDuracionMilisegundos() / 1000;
    }

    private Claims extraerReclamos(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .clock(() -> Date.from(reloj.instant()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerClaveFirma() {
        byte[] secretoDecodificado = Decoders.BASE64.decode(
                jwtPropiedades.getSecreto()
        );

        return Keys.hmacShaKeyFor(secretoDecodificado);
    }
}