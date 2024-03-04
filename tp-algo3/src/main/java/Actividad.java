import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Actividad {
    private String titulo;

    private String descripcion;

    private LocalDateTime tiempo;

    private String idActividad;

    private ArrayList<Alarma> alarmas;

    private boolean diaCompleto;


    // Constructor
    public Actividad(String titulo, String descripcion, LocalDateTime tiempo,
                     String idActividad,ArrayList<Alarma> alarmas,boolean diaCompleto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.idActividad = idActividad;
        this.alarmas = alarmas;
        this.diaCompleto = diaCompleto;
    }

    // Getters  -----------------------------------------------------------------

    public String getTitulo() {
        return this.titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public LocalDateTime getTiempo() {
        return this.tiempo;
    }

    public  String getIdActividad() { return  this.idActividad;}

    public ArrayList<Alarma> getAlarmas() {return this.alarmas;}

    public boolean getDiaCompleto(){ return this.diaCompleto;}


    // Setters  -----------------------------------------------------------------
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTiempo(LocalDateTime tiempo) {this.tiempo = tiempo;}

    public void setIdActividad(String idActividad) { this.idActividad = idActividad;}

    public void setAlarmas(ArrayList<Alarma> alarmas){this.alarmas = alarmas;}

    public void  setDiaCompleto(boolean diaCompleto){this.diaCompleto = diaCompleto;}

    public abstract void modificarActividad(String titulo, String descripcion, LocalDateTime tiempo,
                                            ArrayList<Alarma> alarmas, LocalDateTime fin,
                                            Repeticion repeticio, boolean completado);
    public abstract String mostrarActividadCompleta();
    public abstract String mostrarActividadParcial();

    // Adicionales

    public boolean esDiaCompleto() {return this.diaCompleto;}

    public abstract void aceptar(VisitanteActividad visitante);
}
