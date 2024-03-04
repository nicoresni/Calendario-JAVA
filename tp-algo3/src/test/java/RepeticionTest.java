import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class RepeticionTest {
    // SOLO METODO PRIMERA REPETICION PROBAR TODOS LOS TIPOS DE REPETICIONES
    @Test
    public void repeticionEsFalse() {
        var inicio = LocalDateTime.of(2023, 4, 20, 0, 0);
        var comparar = LocalDateTime.of(2023, 4, 25,0,0);
        var hasta = LocalDateTime.of(2023, 5, 1, 0, 0);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        var repeticion = new Repeticion(false, 2, hasta, 15, dias, new RepeticionDiaria());

        var resultado = repeticion.primeraRepeticion(inicio, comparar);

        assertNull(resultado);
    }
    @Test
    public void repeticionHastaMenorAlComparador() {
        var inicio = LocalDateTime.of(2023, 4, 20, 0, 0);
        var comparar = LocalDateTime.of(2023, 4, 25,0,0);
        var hasta = LocalDateTime.of(2023, 4, 24, 0, 0);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        var repeticion = new Repeticion(true, 2, hasta, 15, dias, new RepeticionDiaria());

        var resultado = repeticion.primeraRepeticion(inicio, comparar);

        assertNull(resultado);
    }

    @Test
    public void repeticionHastaMayorAlComparador() {
        var inicio = LocalDateTime.of(2023, 4, 20, 0, 0);
        var comparar = LocalDateTime.of(2023, 4, 25,0,0);
        var hasta = LocalDateTime.of(2023, 5, 1, 0, 0);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        var repeticion = new Repeticion(true, 2, hasta, 15, dias, new RepeticionDiaria());

        var fechaEsperada = LocalDateTime.of(2023, 4, 26, 0, 0).toString();

        var resultado = repeticion.primeraRepeticion(inicio, comparar);

        assertEquals(fechaEsperada, resultado.get(0));
    }

    // REPETICION DIARIA
    @Test
    public void repeticionDiariaInfinita() {
        var inicio = LocalDateTime.of(2020, 4, 20, 0, 0);
        var comparar = LocalDateTime.of(2023, 4, 25,0,0);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        var repeticion = new Repeticion(true, 1, null, 0, dias, new RepeticionDiaria());

        var fechaEsperada = LocalDateTime.of(2023, 4, 26, 0, 0).toString();

        var resultado = repeticion.primeraRepeticion(inicio, comparar);

        assertEquals(fechaEsperada, resultado.get(0));
    }
    @Test
    public void repeticionDiariaNoLLegaPorOcurrencias() {
        var inicio = LocalDateTime.of(2022, 4, 20, 0, 0);
        var comparar = LocalDateTime.of(2023, 4, 25,0,0);
        var hasta = LocalDateTime.of(2023, 5, 1, 0, 0);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        var repeticion = new Repeticion(true, 2, hasta, 15, dias, new RepeticionDiaria());

        var resultado = repeticion.primeraRepeticion(inicio, comparar);

        assertNull(resultado);
    }
    @Test
    public void repeticionDiariaTieneOcurrenciasYHasta() {
        var inicio = LocalDateTime.of(2023, 4, 20, 0, 0);
        var comparar = LocalDateTime.of(2023, 4, 25,0,0);
        var hasta = LocalDateTime.of(2023, 5, 1, 0, 0);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        var repeticion = new Repeticion(true, 2, hasta, 15, dias, new RepeticionDiaria());

        var fechaEsperada = LocalDateTime.of(2023, 4, 26, 0, 0).toString();

        var resultado = repeticion.primeraRepeticion(inicio, comparar);

        assertEquals(fechaEsperada, resultado.get(0));
    }

    // REPETICION SEMANAL
    @Test
    public void repeticionSemanalInfinita() {
        var inicio = LocalDateTime.of(2015, 12, 1, 0, 0);
        var comparar = LocalDateTime.of(2023, 4, 25,0,0);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        var repeticion = new Repeticion(true, 0, null, 0, dias, new RepeticionSemanal());

        var fechaEsperada = LocalDateTime.of(2023, 5, 2, 0, 0).toString();

        var resultado = repeticion.primeraRepeticion(inicio, comparar);

        assertEquals(fechaEsperada, resultado.get(0));
    }
    @Test
    public void repeticionSemanalConDiasInfinita() {
        var inicio = LocalDateTime.of(2015, 12, 1, 0, 0);
        var comparar = LocalDateTime.of(2023, 4, 25,0,0);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        dias.add(DayOfWeek.SUNDAY);
        dias.add(DayOfWeek.MONDAY);
        dias.add(DayOfWeek.WEDNESDAY);
        var repeticion = new Repeticion(true, 0, null, 0, dias, new RepeticionSemanal());

        var fechaEsperada = LocalDateTime.of(2023, 4, 26, 0, 0).toString();

        var resultado = repeticion.primeraRepeticion(inicio, comparar);

        assertEquals(fechaEsperada, resultado.get(0));
    }

    // REPETICION MENSUAL
    @Test
    public void repeticionMensualInfinita() {
        var inicio = LocalDateTime.of(2010, 12, 24, 0, 0);
        var comparar = LocalDateTime.of(2023, 4, 25,0,0);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        var repeticion = new Repeticion(true, 0, null, 0, dias, new RepeticionMensual());

        var fechaEsperada = LocalDateTime.of(2023, 5, 24, 0, 0).toString();

        var resultado = repeticion.primeraRepeticion(inicio, comparar);

        assertEquals(fechaEsperada, resultado.get(0));
    }

    // REPETICION ANUAL
    @Test
    public void repeticionAnualInfinita() {
        var inicio = LocalDateTime.of(2000, 12, 24, 0, 0);
        var comparar = LocalDateTime.of(2023, 4, 25,0,0);
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        var repeticion = new Repeticion(true, 0, null, 0, dias, new RepeticionAnual());

        var fechaEsperada = LocalDateTime.of(2023, 12, 24, 0, 0).toString();

        var resultado = repeticion.primeraRepeticion(inicio, comparar);

        assertEquals(fechaEsperada, resultado.get(0));
    }
}