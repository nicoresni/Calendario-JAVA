import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RepeticionAnual implements ReglaRepeticion {
    @Override
    public LocalDateTime proximaRepeticion(LocalDateTime inicioEvento, int interval, ArrayList<DayOfWeek> dias) {
        return inicioEvento.plusYears(1);
    }

    @Override
    public String mostrarRepeticion(int interval, ArrayList<DayOfWeek> dias) {
        return "Repeticion Anual";
    }
}
