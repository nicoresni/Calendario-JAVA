import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class RepeticionSemanalTest {
    @Test
    public void proximaRepeticionDevuelveIntervaloCorrectoSinDias () {
        var repeticionSemanal = new RepeticionSemanal();
        var inicio = LocalDateTime.of(2023, 4, 20, 18, 30);
        var esperada = LocalDateTime.of(2023, 4, 27, 18, 30);

        var actualizada = repeticionSemanal.proximaRepeticion(inicio, 5, null);

        assertEquals(esperada, actualizada);
    }

    @Test
    public void proximaRepeticionSoloConMismoDiaRepetido () {
        var repeticionSemanal = new RepeticionSemanal();
        var inicio = LocalDateTime.of(2023, 4, 13, 18, 30);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.THURSDAY);
        var esperada1 = LocalDateTime.of(2023, 4, 20, 18, 30);
        var esperada2 = LocalDateTime.of(2023, 4, 27, 18, 30);

        var actualizada1 = repeticionSemanal.proximaRepeticion(inicio, 0, dias);
        var actualizada2 = repeticionSemanal.proximaRepeticion(actualizada1, 0, dias);

        assertEquals(esperada1, actualizada1);
        assertEquals(esperada2, actualizada2);
    }

    @Test
    public void proximaRepeticionConDiaRepetidoPosterioresAlComienzo () {
        var repeticionSemanal = new RepeticionSemanal();
        var inicio = LocalDateTime.of(2023, 4, 20, 18, 30);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.SATURDAY);

        var esperada1 = LocalDateTime.of(2023, 4, 22, 18, 30);
        var esperada2 = LocalDateTime.of(2023, 4, 29, 18, 30);

        var actualizada1 = repeticionSemanal.proximaRepeticion(inicio, 0, dias);
        var actualizada2 = repeticionSemanal.proximaRepeticion(actualizada1, 0, dias);

        assertEquals(esperada1, actualizada1);
        assertEquals(esperada2, actualizada2);
    }

    @Test
    public void proximaRepeticionConDiaRepetidoPrevioAlComienzo () {
        var repeticionSemanal = new RepeticionSemanal();
        var inicio = LocalDateTime.of(2023, 4, 20, 18, 30);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.MONDAY);
        var esperada1 = LocalDateTime.of(2023, 4, 24, 18, 30);
        var esperada2 = LocalDateTime.of(2023, 5, 1, 18, 30);

        var actualizada1 = repeticionSemanal.proximaRepeticion(inicio, 0, dias);
        var actualizada2 = repeticionSemanal.proximaRepeticion(actualizada1, 0, dias);

        assertEquals(esperada1, actualizada1);
        assertEquals(esperada2, actualizada2);
    }

    @Test
    public void proximaRepeticionConDiaPrevioYPosteriorAlComienzo () {
            var repeticionSemanal = new RepeticionSemanal();
            var inicio = LocalDateTime.of(2023, 4, 20, 18, 30);
            ArrayList<DayOfWeek> dias = new ArrayList<>();
            dias.add(DayOfWeek.SATURDAY);
            dias.add(DayOfWeek.MONDAY);
            var esperada1 = LocalDateTime.of(2023, 4, 22, 18, 30);
            var esperada2 = LocalDateTime.of(2023, 4, 24, 18, 30);
            var esperada3 = LocalDateTime.of(2023, 4, 29, 18, 30);
            var esperada4 = LocalDateTime.of(2023, 5, 1, 18, 30);

            var actualizada1 = repeticionSemanal.proximaRepeticion(inicio, 0, dias);
            var actualizada2 = repeticionSemanal.proximaRepeticion(actualizada1, 0, dias);
            var actualizada3 = repeticionSemanal.proximaRepeticion(actualizada2, 0, dias);
            var actualizada4 = repeticionSemanal.proximaRepeticion(actualizada3, 0, dias);

            assertEquals(esperada1, actualizada1);
            assertEquals(esperada2, actualizada2);
            assertEquals(esperada3, actualizada3);
            assertEquals(esperada4, actualizada4);
        }
    }
