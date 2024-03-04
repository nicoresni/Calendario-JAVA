import java.util.ArrayList;

public class visitanteActividadParcial implements VisitanteActividad {
    private String actividadParcial;

    public String getActividadParcial(){return this.actividadParcial;}
    @Override
    public void visitar(Evento evento) {
        actividadParcial = evento.mostrarActividadParcial();
    }

    @Override
    public void visitar(Tarea tarea) {
        actividadParcial = tarea.mostrarActividadParcial();
    }
}
