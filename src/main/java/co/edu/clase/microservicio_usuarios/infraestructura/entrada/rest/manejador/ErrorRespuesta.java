package co.edu.clase.microservicio_usuarios.infraestructura.entrada.rest.manejador;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorRespuesta {

    private LocalDateTime fechaHora;
    private int estado;
    private String codigo;
    private String mensaje;
    private String ruta;
    private Map<String, Object> detalles;

    public ErrorRespuesta() {
    }

    public ErrorRespuesta(
            LocalDateTime fechaHora,
            int estado,
            String codigo,
            String mensaje,
            String ruta,
            Map<String, Object> detalles) {

        this.fechaHora = fechaHora;
        this.estado = estado;
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.ruta = ruta;
        this.detalles = detalles;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Map<String, Object> getDetalles() {
        return detalles;
    }

    public void setDetalles(Map<String, Object> detalles) {
        this.detalles = detalles;
    }
}