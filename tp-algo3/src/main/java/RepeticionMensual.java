import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RepeticionMensual implements ReglaRepeticion {
    public LocalDateTime proximaRepeticion(LocalDateTime inicioEvento, int interval, ArrayList<DayOfWeek> dias) {
        return inicioEvento.plusMonths(1);
    }

    @Override
    public String mostrarRepeticion(int interval, ArrayList<DayOfWeek> dias) {
        return "Repeticion Mensual";
    }
}
