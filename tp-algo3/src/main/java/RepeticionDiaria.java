import java.time.*;
import java.util.ArrayList;

public class RepeticionDiaria implements ReglaRepeticion {
    @Override
    public LocalDateTime proximaRepeticion(LocalDateTime inicioEvento, int interval, ArrayList<DayOfWeek> dias) {
        if (interval == 0) {
            return inicioEvento.plusDays(1);
        }
        return inicioEvento.plusDays(interval);
    }

    @Override
    public String mostrarRepeticion(int interval, ArrayList<DayOfWeek> dias) {
        return "Repeticion Diaria, intervalo: " + interval;
    }
}
