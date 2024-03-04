import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class RepeticionDiariaTest {
    @Test
    public void proximaRepeticionDevuelveIntervaloCorrecto () {
        var repeticionDiaria = new RepeticionDiaria();
        var inicio = LocalDateTime.of(2023, 4, 20, 18, 30);
        var esperada = LocalDateTime.of(2023, 4, 25, 18, 30);
        var actualizada = repeticionDiaria.proximaRepeticion(inicio, 5, null);
        assertEquals(esperada, actualizada);
    }

    @Test
    public void proximaRepeticionDevuelveCorrectoConDias () {
        var repeticionDiaria = new RepeticionDiaria();
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.FRIDAY);
        dias.add(DayOfWeek.MONDAY);
        var inicio = LocalDateTime.of(2023, 4, 20, 20, 45);
        var esperada = LocalDateTime.of(2023, 4, 21, 20, 45);

        var actualizada = repeticionDiaria.proximaRepeticion(inicio, 1, dias);

        assertEquals(esperada, actualizada);
    }
}