import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Evento extends Actividad {

    private LocalDateTime fin;

    private Repeticion repeticion;

    // Constructor
    public Evento(String titulo, String descripcion, LocalDateTime inicio,
                   String idActividad,ArrayList<Alarma> alarmas,LocalDateTime fin,Repeticion repeticion, boolean diaCompleto) {
        super(titulo,descripcion,inicio,idActividad,alarmas,diaCompleto);
        this.fin = fin;
        this.repeticion = repeticion;
    }

    // Getters  -----------------------------------------------------------------
    public LocalDateTime getFin() {
        return this.fin;
    }

    public Repeticion getRepeticion() {return this.repeticion;}


    // Setters  -----------------------------------------------------------------
    public void setFin(LocalDateTime fin) {this.fin = fin;}

    public void setRepeticion(Repeticion repeticion){ this.repeticion = repeticion;}


    // Funciones Privadas -----------------------------------------------------------------

    private LocalDateTime calcularNuevaFechaFin(Duration diferencia, LocalDateTime nuevo_inicio){
        LocalDateTime nuevoFin;
        nuevoFin = nuevo_inicio.plusSeconds(diferencia.toSeconds());
        return  nuevoFin;
    }

    private ArrayList<Alarma> crearNuevasAlarmas(Duration diferenciaInicios){
        ArrayList<Alarma> nuevasAlar = new ArrayList<>();

        for (Alarma a : this.getAlarmas()){
            LocalDateTime nuevaHoraAbsoluta = a.getHoraAbsoluta().plusSeconds(diferenciaInicios.toSeconds());
            Alarma nuevaA = new Alarma(nuevaHoraAbsoluta,a.getEfecto());
            nuevaA.setIdActividad(a.getIdActividad());
            nuevaA.setIdAlarma(a.getIdAlarma());
            nuevasAlar.add(nuevaA);
        }
        return  nuevasAlar;
    }

    private Repeticion crearNuevaRepeticion(int repeticionesRestantes){
        Repeticion nuevaRepeticion = new Repeticion(this.repeticion.getRepeticion(),this.repeticion.getInterval(),
                this.repeticion.getHasta(),this.repeticion.getOcurrencias(),
                this.repeticion.getDias(),this.repeticion.getReglaRepeticion());
        nuevaRepeticion.setOcurrencias(repeticionesRestantes);
        return  nuevaRepeticion;
    }

    // Metodos Publicos -----------------------------------------------------------------

    public void modificarActividad(String titulo, String descripcion, LocalDateTime tiempo,
                                   ArrayList<Alarma> alarmas, LocalDateTime fin,
                                   Repeticion repeticion, boolean completado){
        this.setTitulo(titulo);
        this.setDescripcion(descripcion);
        this.setTiempo(tiempo);
        this.setAlarmas(alarmas);
        this.fin = fin;
        this.repeticion = repeticion;

    }
    public String mostrarActividadParcial(){
        var eventoParcial = "";
        var espacio = " | ";
        eventoParcial += "Titulo: " + this.getTitulo() + espacio;
        eventoParcial += "Inicio: " + this.getTiempo() + espacio;
        if (this.esDiaCompleto()){
            eventoParcial += " DIA COMPLETO" + espacio;
        } else {
            eventoParcial += "Fin: ";
            eventoParcial += this.fin + espacio;
        }
        return eventoParcial;
    }

    public String mostrarActividadCompleta(){
        var eventoCompleto = mostrarActividadParcial();
        var espacio = " | ";
        eventoCompleto += "Descripcion: " + this.getDescripcion() + espacio;
        eventoCompleto += "Alarmas: " + this.getAlarmas() + espacio;
        eventoCompleto += repeticion.mostrarRepeticion();
        return eventoCompleto;
    }

    public boolean tieneRepeticion(){
        // Debe revisra la repeticion de este evento, y ver si me deja repetir(si hay, si tiene cantidad de repeticiones a hacer)
        return repeticion.getRepeticion();
    }


    public Evento siguienteRepeticion(LocalDateTime ahora){
        //  *Debe, teniendo el tiempo actual, darme la siguiente repeticion
        //  *Teniendo el tiempo actual, podes saber si podes saber si el "repeticiones.hasta" te deja devolver un
        // nuevo evento, y con el "repeticiones.ocurrencias" podes usar un contador para saber si podes devolver
        ArrayList<String> tiempoActual = repeticion.primeraRepeticion(this.getTiempo(), ahora);
        if (tiempoActual == null) {
            return null;
        }
        // Nuevas Fechas de Evento
        Duration diferenciaIniFin = Duration.between(this.getTiempo(),this.fin);
        LocalDateTime nuevaFechaInicio = LocalDateTime.parse(tiempoActual.get(0));
        LocalDateTime nuevaFechaFin = calcularNuevaFechaFin(diferenciaIniFin,nuevaFechaInicio);

        // Nuevas Repeticiones
        Repeticion nuevaRepeticion = this.repeticion;
        if (tiempoActual.get(1) != null) {
            int repeticionesHechas = Integer.parseInt(tiempoActual.get(1));
            nuevaRepeticion = crearNuevaRepeticion(repeticionesHechas);
        }

        // Nuevas Alarmas
        Duration diferenciaInicios = Duration.between(this.getTiempo(),nuevaFechaInicio);
        ArrayList<Alarma> nuevasAlarmas = crearNuevasAlarmas(diferenciaInicios);

        //Proximamente

        Evento copia_evento;
        copia_evento = new Evento(this.getTitulo(),this.getDescripcion(),
                nuevaFechaInicio,this.getIdActividad(),nuevasAlarmas,nuevaFechaFin,nuevaRepeticion,this.getDiaCompleto());

        return copia_evento;

    }

    public Duration tiempoRestanteParaCompletar(LocalDateTime ahora){
        Duration tiempoRestante = Duration.between(ahora, this.fin);
        if (tiempoRestante.isNegative()) {
            return null;

        }
        return tiempoRestante;
    }

    public boolean getCompletada(){
        return false;
    }

    public void aceptar(VisitanteActividad visitante){
        visitante.visitar(this);
    }

}