import java.time.*;
import java.util.ArrayList;
public interface ReglaRepeticion {
    LocalDateTime proximaRepeticion(LocalDateTime inicioEvento, int interval, ArrayList<DayOfWeek> dias);

    String mostrarRepeticion(int interval, ArrayList<DayOfWeek> dias);
}

