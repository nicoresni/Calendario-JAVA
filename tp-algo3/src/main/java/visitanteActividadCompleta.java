public class visitanteActividadCompleta implements VisitanteActividad {

    private String actividadCompleta;

    public String getActividadCompleta(){return this.actividadCompleta;}
    @Override
    public void visitar(Evento evento) {
        actividadCompleta = evento.mostrarActividadCompleta();
    }

    @Override
    public void visitar(Tarea tarea) {
        actividadCompleta = tarea.mostrarActividadCompleta();
    }
}
