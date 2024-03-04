import java.time.LocalDateTime;
import java.util.ArrayList;

public class visitanteListadoActividad implements VisitanteActividad {
    private LocalDateTime hasta;
    private LocalDateTime desde;
    private ArrayList<Actividad> listado;

    public visitanteListadoActividad(LocalDateTime hasta, LocalDateTime desde, ArrayList<Actividad> listado){
        this.hasta = hasta;
        this.desde = desde;
        this.listado = listado;
    }

    public ArrayList<Actividad> listado(){return this.listado;}

    @Override
    public void visitar(Evento evento) {
        LocalDateTime tiempoEvento = evento.getTiempo();
        if (tiempoEvento.isBefore(hasta) && tiempoEvento.isAfter(desde)) {
            this.listado.add(evento);
        }if (evento.tieneRepeticion()) {
            Evento revisarRepeticion = evento.siguienteRepeticion(desde);
            while (revisarRepeticion != null){
                LocalDateTime tiempoRepeticion = revisarRepeticion.getTiempo();
                if (tiempoRepeticion.isAfter(hasta)){
                    break;
                }
                if (tiempoRepeticion.isBefore(hasta) && tiempoRepeticion.isAfter(desde)){
                    this.listado.add(revisarRepeticion);
                }

                revisarRepeticion = revisarRepeticion.siguienteRepeticion(desde);
            }
        }

    }

    @Override
    public void visitar(Tarea tarea) {
        if (tarea.getTiempo().isAfter(desde) && tarea.getTiempo().isBefore(hasta)) {
            this.listado.add(tarea);
        }
    }
}
