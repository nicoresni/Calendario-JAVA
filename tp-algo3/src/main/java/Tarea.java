import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tarea extends Actividad {
    private boolean completada;



    // Constructor
    public Tarea(String titulo, String descripcion, LocalDateTime vencimiento,
                 String idActividad, ArrayList<Alarma> alarmas, boolean completada, boolean diaCompleto) {
        super(titulo, descripcion, vencimiento, idActividad, alarmas, diaCompleto);
        this.completada = completada;
    }

    // Getters  -----------------------------------------------------------------

    public boolean getCompletada() {
        return this.completada;
    }

    // Setters  -----------------------------------------------------------------

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    // Metodos  -----------------------------------------------------------------

    @Override
    public void modificarActividad(String titulo, String descripcion, LocalDateTime tiempo,
                                   ArrayList<Alarma> alarmas, LocalDateTime fin,
                                   Repeticion repeticion, boolean completada) {
        this.setTitulo(titulo);
        this.setDescripcion(descripcion);
        this.setTiempo(tiempo);
        this.setAlarmas(alarmas);
        this.completada = completada;
    }

    public String mostrarActividadParcial() {
        var tareaParcial = "";
        var espacio = " | ";
        tareaParcial += this.getTitulo() + espacio;
        tareaParcial += "Ini: " + this.getTiempo() + espacio;
        if (this.esDiaCompleto()){
            tareaParcial += " DIA COMPLETO" + espacio;
        }
        if (completada){
            tareaParcial += "COMPLETADA";
        }
        else {
            tareaParcial += "SIN COMPLETAR";
        }
        return tareaParcial;
    }

    public String mostrarActividadCompleta() {
        var tareaCompleta = "";
        var espacio = " | ";
        tareaCompleta += this.getDescripcion() + espacio;
        tareaCompleta += this.getAlarmas() + espacio;
        return tareaCompleta;
    }

    public Duration tiempoRestanteParaCompletar(LocalDateTime ahora) {
        Duration tiempoRestante = Duration.between(ahora, this.getTiempo());
        if (tiempoRestante.isNegative()) {
            return null;
        }
        return tiempoRestante;
    }
    public void aceptar(VisitanteActividad visitante){
        visitante.visitar(this);
    }
}