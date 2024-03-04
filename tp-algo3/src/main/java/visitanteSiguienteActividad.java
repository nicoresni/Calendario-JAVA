import java.time.LocalDateTime;

public class visitanteSiguienteActividad implements VisitanteActividad {
    private Actividad siguienteActividad;
    private LocalDateTime ahora;

    public visitanteSiguienteActividad(LocalDateTime ahora){
        this.ahora = ahora;
    }

    public Actividad siguienteActividad() {
        return siguienteActividad;
    }
    @Override
    public void visitar(Evento evento) {
        if ((siguienteActividad == null && evento.getTiempo().isAfter(ahora)) ||
                (siguienteActividad != null && evento.getTiempo().isBefore(siguienteActividad.getTiempo()))) {
            siguienteActividad = evento;
        }
        if (evento.tieneRepeticion()) {
            Evento repeticionProxima = evento.siguienteRepeticion(ahora);
            if (repeticionProxima == null) {
                return;
            }

            if (siguienteActividad == null || repeticionProxima.getTiempo().isBefore(siguienteActividad.getTiempo())) {
                // Si no tenia un proximo, y la repeticion es DESPUES A AHORA--> lo guardo
                // Si tenia un proximo, tiene que ser ANTES que PROXIMO
                siguienteActividad = repeticionProxima; // Si ya defino que esta repeticion es la correcta, al pedo ver las siguente
            }
        }
    }

    @Override
    public void visitar(Tarea tarea) {
        if (!tarea.getCompletada() && tarea.getTiempo().isAfter(ahora)) {
            siguienteActividad = tarea;
        }
    }
}
