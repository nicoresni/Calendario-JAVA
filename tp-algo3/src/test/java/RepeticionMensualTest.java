import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class RepeticionMensualTest {
    @Test
    public void proximaRepeticionDevuelveCorrecto () {
        var repeticionMensual = new RepeticionMensual();
        var inicio = LocalDateTime.of(2023, 3, 20, 18, 30);
        var esperada = LocalDateTime.of(2023, 4, 20, 18, 30);
        var actualizada = repeticionMensual.proximaRepeticion(inicio, 0, null);
        assertEquals(esperada, actualizada);
    }

    @Test
    public void proximaRepeticionDevuelveCorrectoConDiasEIntervalo () {
        var repeticionMensual = new RepeticionMensual();
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.FRIDAY);
        dias.add(DayOfWeek.MONDAY);
        var inicio = LocalDateTime.of(2023, 3, 20, 20, 45);
        var esperada = LocalDateTime.of(2023, 4, 20, 20, 45);

        var actualizada = repeticionMensual.proximaRepeticion(inicio, 2, dias);

        assertEquals(esperada, actualizada);
    }
}