import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RepeticionSemanal implements ReglaRepeticion {
    public LocalDateTime proximaRepeticion(LocalDateTime inicioEvento, int interval, ArrayList<DayOfWeek> dias) {
        if (dias != null && !dias.isEmpty()) {
            LocalDateTime proxDia = inicioEvento;
            for (int i=0; i<7; i++) {
                proxDia = proxDia.plusDays(1);
                if (dias.contains(proxDia.getDayOfWeek())){
                    return proxDia;
                }
            }
        }
        return inicioEvento.plusWeeks(1);
    }

    @Override
    public String mostrarRepeticion(int interval, ArrayList<DayOfWeek> dias) {
        return "Repeticion Semanal, " + dias;
    }
}
