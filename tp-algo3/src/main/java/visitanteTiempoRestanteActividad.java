import java.time.Duration;
import java.time.LocalDateTime;

public class visitanteTiempoRestanteActividad implements VisitanteActividad {
    private Duration tiempoRestante = null;
    private LocalDateTime ahora;
    public visitanteTiempoRestanteActividad(LocalDateTime ahora) {
        this.ahora = ahora;
    }
    public Duration tiempoRestante(){
        return tiempoRestante;
    }
    public void visitar(Evento evento) {
        if (evento.tieneRepeticion()) {
            evento = evento.siguienteRepeticion(ahora);
        }
        tiempoRestante = evento.tiempoRestanteParaCompletar(ahora);
    }
    public void visitar(Tarea tarea) {
        tiempoRestante = tarea.tiempoRestanteParaCompletar(ahora);
    }
}
