import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class RepeticionAnualTest {
    @Test
    public void proximaRepeticionDevuelveCorrecto () {
        var repeticionAnual = new RepeticionAnual();
        var inicio = LocalDateTime.of(2022, 4, 20, 18, 30);
        var esperada = LocalDateTime.of(2023, 4, 20, 18, 30);
        var actualizada = repeticionAnual.proximaRepeticion(inicio, 0, null);
        assertEquals(esperada, actualizada);
    }
    @Test
    public void proximaRepeticionDevuelveCorrectoConDiasEIntervalo () {
        var repeticionAnual = new RepeticionAnual();
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.FRIDAY);
        dias.add(DayOfWeek.MONDAY);
        var inicio = LocalDateTime.of(2022, 4, 20, 20, 45);
        var esperada = LocalDateTime.of(2023, 4, 20, 20, 45);

        var actualizada = repeticionAnual.proximaRepeticion(inicio, 2, dias);

        assertEquals(esperada, actualizada);
    }
}