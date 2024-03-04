import org.junit.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class EventoTest {
    @Test
    public void siguienteEventoModificaLaHoraInicial() {
        var inicio = LocalDateTime.of(2023, 4, 20, 5, 0);
        var fin = LocalDateTime.of(2023, 4, 21, 9, 30);
        var ahora = LocalDateTime.of(2023, 5, 1, 0, 0);
        var esperado = LocalDateTime.of(2023, 5, 1, 5, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        Repeticion repeticion = new Repeticion(true, 0,null,20, dias, new RepeticionDiaria());
        Evento evento = new Evento("", "", inicio,"0",alarmas, fin,  repeticion,false);

        var copiaEvento = evento.siguienteRepeticion(ahora);

        assertEquals(esperado, copiaEvento.getTiempo());
    }

    @Test
    public void siguienteEventoModificaLaHoraFinal() {
        var inicio = LocalDateTime.of(2023, 4, 20, 5, 0);
        var fin = LocalDateTime.of(2023, 4, 21, 9, 30);
        var ahora = LocalDateTime.of(2023, 5, 1, 0, 0);
        var esperado = LocalDateTime.of(2023, 5, 2, 9, 30);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        Repeticion repeticion = new Repeticion(true, 0,null,20, dias, new RepeticionDiaria());
        Evento evento = new Evento("", "", inicio,"0",alarmas, fin,  repeticion,false);

        var copiaEvento = evento.siguienteRepeticion(ahora);

        assertEquals(esperado, copiaEvento.getFin());
    }

    @Test
    public void siguienteEventoModificaLasAlarmas() {
        var inicio = LocalDateTime.of(2023, 4, 20, 5, 0);
        var fin = LocalDateTime.of(2023, 4, 20, 6, 30);
        var ahora = LocalDateTime.of(2023, 5, 1, 0, 0);

        var tiempo_anterior = Duration.between(fin, inicio); // 1 Hora y media
        ArrayList<Alarma> alarmas = new ArrayList<>();
        Alarma alarma1 = new Alarma(inicio, Efecto.NOTIFICACION);
        Alarma alarma2 = new Alarma(inicio, tiempo_anterior, Efecto.SONIDO);
        alarmas.add(alarma1);
        alarmas.add(alarma2);
        ArrayList<DayOfWeek> dias = new ArrayList<>();

        Repeticion repeticion = new Repeticion(true, 0,null,20, dias, new RepeticionDiaria());
        Evento evento = new Evento("", "", inicio,"0",alarmas, fin,  repeticion,false);

        var esperado1 = LocalDateTime.of(2023, 5, 1, 5, 0);
        var esperado2 = LocalDateTime.of(2023, 5, 1, 3, 30);

        var copiaEvento = evento.siguienteRepeticion(ahora);

        assertEquals(esperado1, copiaEvento.getAlarmas().get(0).getHoraAbsoluta());
        assertEquals(esperado2, copiaEvento.getAlarmas().get(1).getHoraAbsoluta());
    }

    @Test
    public void siguienteEventoModificaLasRepeticiones() {
        var inicio = LocalDateTime.of(2023, 4, 20, 5, 0);
        var fin = LocalDateTime.of(2023, 4, 21, 9, 30);
        var ahora = LocalDateTime.of(2023, 4, 23, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        ArrayList<DayOfWeek> dias = new ArrayList<>();
        Repeticion repeticion = new Repeticion(true, 0,null,5, dias, new RepeticionDiaria());
        Evento evento = new Evento("", "", inicio,"0",alarmas, fin,  repeticion,false);
        int esperado = 2;

        var copiaEvento = evento.siguienteRepeticion(ahora);

        assertEquals(esperado, copiaEvento.getRepeticion().getOcurrencias());
    }
}