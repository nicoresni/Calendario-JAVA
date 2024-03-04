import java.time.*;
import java.util.ArrayList;

public class Repeticion {
    private Boolean repeticion;
    private int interval;
    private LocalDateTime hasta;
    private int ocurrencias;
    private ArrayList<DayOfWeek> dias;
    private ReglaRepeticion reglaRepeticion;

    public Repeticion(Boolean repeticion, int interval, LocalDateTime hasta,
                      int ocurrencias, ArrayList<DayOfWeek> dias, ReglaRepeticion reglaRepeticion) {
        this.repeticion = repeticion;
        this.interval = interval;
        this.hasta = hasta;
        this.ocurrencias = ocurrencias;
        this.dias = dias;
        this.reglaRepeticion = reglaRepeticion;
    }

    // GETTERS
    public Boolean getRepeticion() {
        return repeticion;
    }

    public int getInterval() {
        return interval;
    }

    public LocalDateTime getHasta() {
        return hasta;
    }

    public ArrayList<DayOfWeek> getDias() {
        return dias;
    }

    public int getOcurrencias() {
        return ocurrencias;
    }

    public ReglaRepeticion getReglaRepeticion() {
        return reglaRepeticion;
    }

    // SETTERS
    public void setRepeticion(Boolean repeticion) {
        this.repeticion = repeticion;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setHasta(LocalDateTime hasta) {
        this.hasta = hasta;
    }

    public void setOcurrencias(int ocurrencias) {
        this.ocurrencias = ocurrencias;
    }

    public void setDias(ArrayList<DayOfWeek> dias) {
        this.dias = dias;
    }

    public void setReglaRepeticion(ReglaRepeticion reglaRepeticion) {
        this.reglaRepeticion = reglaRepeticion;
    }

    // METODOS
    public ArrayList<String> primeraRepeticion (LocalDateTime inicioEvento, LocalDateTime fechaAComparar) {
        if (!repeticion || (this.hasta != null && fechaAComparar.isAfter(this.hasta)) ||
                (this.ocurrencias != 0 && this.ocurrencias <= 0)) {
            return null;
        }

        ArrayList<String> resultado = new ArrayList<>();
        int i = 0;
        LocalDateTime fechaActual = inicioEvento;
        while (true) {
            i++;
            fechaActual = reglaRepeticion.proximaRepeticion(fechaActual, this.interval, this.dias);

            if (fechaAComparar.isBefore(fechaActual)) {
                break;
            }
            if (ocurrencias != 0 && i >= ocurrencias) {
                return null;
            }

        }
        resultado.add(fechaActual.toString());
        Integer ocurrenciasRestantes;
        if (this.ocurrencias == 0) {
            resultado.add(null);
        } else {
            ocurrenciasRestantes = this.ocurrencias - i;
            String restantes = String.valueOf(ocurrenciasRestantes);
            resultado.add(restantes);
        }
        return resultado;
    }

    public String mostrarRepeticion() {
        var textRepeticion = "";
        var espacio = ", ";
        if (repeticion) {
            textRepeticion += reglaRepeticion.mostrarRepeticion(interval, dias) + espacio;
            textRepeticion += "ocurrencias: " + ocurrencias + espacio;
            textRepeticion += "hasta: " + hasta;
        } else {
            textRepeticion += "SIN REPETICION";
        }
        return textRepeticion;
    }
}
