import org.junit.Test;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class CalendarioTest {
    // BASICO EVENTO
    @Test
    public void crearEventoAgregaAlHash() {
        //arrange
        var calendario = new Calendario();
        var inicio = LocalDateTime.of(2023, 4, 20, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 21, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        //act
        calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);
        //assert
        assertTrue(calendario.getActividades().containsKey("0"));
    }

    @Test
    public void modificarEventoModificaTodo() {
        var calendario = new Calendario();
        var inicio = LocalDateTime.of(2023, 4, 20, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 21, 0, 0);
        var nuevoInicio = inicio.plusSeconds(55);
        var nuevoFin = fin.plusDays(20);
        var alarma1 = new Alarma(fin, Efecto.EMAIL);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(true, 1, null, 0, null, new RepeticionDiaria());

        var id = calendario.crearEvento("", "", inicio, fin, alarmas, null,false);

        alarmas.add(alarma1);
        calendario.modificarEvento(id,"Evento", "Modificado", nuevoInicio, nuevoFin, alarmas, repeticion);

        assertEquals("Evento", calendario.getActividades().get(id).getTitulo());
        assertEquals("Modificado", calendario.getActividades().get(id).getDescripcion());
        assertEquals(nuevoInicio, calendario.getActividades().get(id).getTiempo());
        assertEquals(nuevoFin, ((Evento) calendario.getActividades().get(id)).getFin());
        assertEquals(alarmas, calendario.getActividades().get(id).getAlarmas());
        assertEquals(repeticion, ((Evento) calendario.getActividades().get(id)).getRepeticion());
    }

    @Test
    public void eliminarEventoLoQuitaDelHash() {
        var calendario = new Calendario();
        var inicio = LocalDateTime.of(2023, 4, 20, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 21, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(false, 0, null, 0, null, null);

        String id = calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        calendario.eliminarActividad(id);

        assertTrue(calendario.getActividades().isEmpty());
    }

    // BASICO TAREA
    @Test
    public void crearTareaAgregaAlHash() {
        var calendario = new Calendario();
        var fin = LocalDateTime.of(2023, 4, 21, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        //act
        calendario.crearTarea("", "", false, fin, alarmas,false);
        //assert
        assertTrue(calendario.getActividades().containsKey("0"));
    }

    @Test
    public void modificarTareaModificaTodo() {
        var calendario = new Calendario();
        var fin = LocalDateTime.of(2023, 4, 21, 0, 0);
        var nuevoFin = fin.plusDays(26);
        var alarma1 = new Alarma(fin, Efecto.EMAIL);
        ArrayList<Alarma> alarmas = new ArrayList<>();

        var id = calendario.crearTarea("", "", false, fin, alarmas,false);

        alarmas.add(alarma1);
        calendario.modificarTarea(id,"Tarea", "Modificada", true, nuevoFin, alarmas);

        assertEquals("Tarea", calendario.getActividades().get(id).getTitulo());
        assertEquals("Modificada", calendario.getActividades().get(id).getDescripcion());
        assertTrue(((Tarea) calendario.getActividades().get(id)).getCompletada());
        assertEquals(nuevoFin, calendario.getActividades().get(id).getTiempo());
        assertEquals(alarmas, calendario.getActividades().get(id).getAlarmas());
    }

    @Test
    public void eliminarTareaLoQuitaDelHash() {
        var calendario = new Calendario();
        var fin = LocalDateTime.of(2023, 4, 21, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        String id = calendario.crearTarea("", "", false, fin, alarmas,false);

        calendario.eliminarActividad(id);

        assertTrue(calendario.getActividades().isEmpty());
    }

    // MOSTRAR SIGUIENTES TAREAS

    @Test
    public void mostrarSiguienteTareaSinTareas() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 21, 0, 0);

        var resultado = calendario.mostrarSiguienteTarea(ahora);

        assertNull(resultado);
    }

    @Test
    public void mostrarSiguienteTareaUnaTareaPrevia() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 22, 0, 0);

        var fin = LocalDateTime.of(2023, 4, 21, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        calendario.crearTarea("", "", false, fin, alarmas,false);

        var resultado = calendario.mostrarSiguienteTarea(ahora);

        assertNull(resultado);
    }

    @Test
    public void mostrarSiguienteTareaUnaTareaPosterior() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 22, 0, 0);

        var fin = LocalDateTime.of(2023, 4, 23, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        String id = calendario.crearTarea("", "", false, fin, alarmas,false);

        var resultado = calendario.mostrarSiguienteTarea(ahora);

        assertEquals(calendario.getActividades().get(id), resultado);
    }

    @Test
    public void mostrarSiguienteTareaNoMuestraCompletadaPosterior() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 22, 0, 0);

        var fin = LocalDateTime.of(2023, 4, 23, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        calendario.crearTarea("", "", true, fin, alarmas,false);

        var resultado = calendario.mostrarSiguienteTarea(ahora);

        assertNull(resultado);
    }

    // MOSTRAR SIGUIENTES EVENTOS
    @Test
    public void mostrarSiguienteEventoSinEventos() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 21, 0, 0);

        var resultado = calendario.mostrarSiguienteEvento(ahora);

        assertNull(resultado);
    }

    @Test
    public void mostrarSiguienteEventoSinRepeticionInicioYFinPrevio() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 22, 0, 0);

        var inicio = LocalDateTime.of(2023, 4, 20, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 21, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        var resultado = calendario.mostrarSiguienteEvento(ahora);

        assertNull(resultado);
    }

    @Test
    public void mostrarSiguienteEventoSinRepeticionInicioPrevioYFinPosterior() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 22, 0, 0);

        var inicio = LocalDateTime.of(2023, 4, 20, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 23, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        var resultado = calendario.mostrarSiguienteEvento(ahora);

        assertNull(resultado);
    }

    @Test
    public void mostrarSiguienteEventoSinRepeticionInicioYFinPosterior() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 22, 0, 0);

        var inicio = LocalDateTime.of(2023, 4, 23, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 24, 0, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        String id = calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        var resultado = calendario.mostrarSiguienteEvento(ahora);

        assertEquals(calendario.getActividades().get(id), resultado);
    }

    @Test
    public void mostrarSiguienteEventoRepeticion() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 22, 0, 0);
        var inicio = LocalDateTime.of(2023, 4, 9, 12, 30);
        var fin = LocalDateTime.of(2023, 4, 10, 12, 30);
        ArrayList<Alarma> alarmas = new ArrayList<>();

        var repeticion = new Repeticion(true, 3, null, 0, null, new RepeticionDiaria());
        String id = calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        var resultado = calendario.mostrarSiguienteEvento(ahora);
        assertEquals(id, resultado.getIdActividad());
    }

    @Test
    public void mostrarSiguienteEventoRepeticionNollegaPorOcurrencias() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 22, 0, 0);

        var inicio = LocalDateTime.of(2023, 4, 9, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 9, 12, 30);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(true, 3, null, 4, null, new RepeticionDiaria());
        calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        var resultado = calendario.mostrarSiguienteEvento(ahora);

        assertNull(resultado);
    }

    // TIEMPO RESTANTE ACTIVIDAD
    @Test
    public void tiempoRestanteActividadEventoTerminado(){
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 24, 0, 0);

        var inicio = LocalDateTime.of(2023, 4, 9, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 23, 12, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        var id = calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        var resulatado = calendario.tiempoRestanteParaCompletarActividad(id, ahora);

        assertNull(resulatado);
    }

    @Test
    public void tiempoRestanteActividadEventoOriginalTerminadoPeroTieneRepeticion(){
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 28, 0, 0);

        var inicio = LocalDateTime.of(2023, 4, 25, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 25, 12, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(true, 4, null, 0, null, new RepeticionDiaria());
        var id = calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        var esperado = Duration.between(ahora, fin.plusDays(4));
        var resulatado = calendario.tiempoRestanteParaCompletarActividad(id, ahora);

        assertEquals(esperado, resulatado);
    }

    @Test
    public void tiempoRestanteActividadEventoNoTerminado(){
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 22, 0, 0);

        var inicio = LocalDateTime.of(2023, 4, 9, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 23, 12, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        var id = calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        var esperado = Duration.between(ahora, fin);
        var resulatado = calendario.tiempoRestanteParaCompletarActividad(id, ahora);

        assertEquals(esperado, resulatado);
    }

    @Test
    public void tiempoRestanteActividadTareaVencida(){
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 24, 0, 0);

        var fin = LocalDateTime.of(2023, 4, 23, 12, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var id = calendario.crearTarea("", "", true, fin, alarmas,false);

        var resulatado = calendario.tiempoRestanteParaCompletarActividad(id, ahora);

        assertNull(resulatado);
    }

    @Test
    public void tiempoRestanteActividadTareaNoVencida(){
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 22, 0, 0);

        var fin = LocalDateTime.of(2023, 4, 23, 12, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var id = calendario.crearTarea("", "", true, fin, alarmas,false);

        var esperado = Duration.between(ahora, fin);
        var resulatado = calendario.tiempoRestanteParaCompletarActividad(id, ahora);

        assertEquals(esperado, resulatado);
    }

    // ALARMAS

    @Test
    public void pruebasAlarmaSiguienteCorrectaConFechaAbsoluta() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 24, 19, 0);
        var inicio = LocalDateTime.of(2023, 4, 24, 20, 0);
        var fin = LocalDateTime.of(2023, 4, 24, 20, 45);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        Alarma alarmaUnica = new Alarma(inicio, Efecto.SONIDO);
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        alarmas.add(alarmaUnica);

        calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        assertEquals(alarmaUnica, calendario.obtenerSiguienteAlarma(ahora));
    }

    @Test
    public void pruebasAlarmaSiguienteCorrectaConFechaRelativa() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 24, 19, 0);
        var inicio = LocalDateTime.of(2023, 4, 24, 20, 0);
        var fin = LocalDateTime.of(2023, 4, 24, 20, 45);
        ArrayList<Alarma> alarmas = new ArrayList<>();

        Duration tiempo_anterior = Duration.between(fin, inicio); // 45 minutos antes

        Alarma alarmaUnica = new Alarma(inicio, tiempo_anterior, Efecto.SONIDO);
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        alarmas.add(alarmaUnica);

        calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        assertEquals(alarmaUnica, calendario.obtenerSiguienteAlarma(ahora));

    }

    @Test
    public void pruebasAlarmaNoDeberiaAparecer() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 24, 21, 0);
        var inicio = LocalDateTime.of(2023, 4, 24, 20, 0);
        var fin = LocalDateTime.of(2023, 4, 24, 20, 45);
        ArrayList<Alarma> alarmas = new ArrayList<>();

        Duration tiempo_anterior = Duration.between(fin, inicio); // 45 minutos antes

        Alarma alarmaUnica = new Alarma(inicio, tiempo_anterior, Efecto.SONIDO);
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        alarmas.add(alarmaUnica);

        calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        assertNull(calendario.obtenerSiguienteAlarma(ahora));
    }

    @Test
    public void pruebasAlarmaDebeAparecerLaCorrecta() {
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 24, 20, 0);
        var inicio = LocalDateTime.of(2023, 4, 24, 19, 0);
        var fin = LocalDateTime.of(2023, 4, 24, 20, 45);
        ArrayList<Alarma> alarmas1 = new ArrayList<>();
        ArrayList<Alarma> alarmas2 = new ArrayList<>();

        Alarma alarmaUnica1 = new Alarma(inicio, Efecto.SONIDO);
        Alarma alarmaUnica2 = new Alarma(fin, Efecto.SONIDO);

        alarmas1.add(alarmaUnica1);
        alarmas2.add(alarmaUnica2);

        calendario.crearTarea("2", "", false, fin, alarmas1,false);
        calendario.crearTarea("1", "", false, inicio, alarmas2,false);

        assertEquals("2", calendario.mostrarSiguienteTarea(ahora).getTitulo());
        assertEquals(2,calendario.getAlarmas().size());
        assertEquals(alarmaUnica2, calendario.obtenerSiguienteAlarma(ahora));
    }

    @Test
    public void agregarAlarmaAEventoCreado(){
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 24, 18, 0);
        var inicio = LocalDateTime.of(2023, 4, 24, 19, 0);
        var fin = LocalDateTime.of(2023, 4, 24, 20, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();

        var repeticion = new Repeticion(false, 0, null, 0, null, null);

        calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        assertNull(calendario.obtenerSiguienteAlarma(ahora));
        Alarma alarmaUnica = new Alarma(inicio, Efecto.SONIDO);

        calendario.agregarAlarma("0",alarmaUnica); // Como es la actividad 0, tendra ese ID

        assertEquals(alarmaUnica,calendario.obtenerSiguienteAlarma(ahora));

    }

    @Test
    public void agregarMultiplesAlarmasAActividadCreada(){
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 24, 18, 0);
        var inicio = LocalDateTime.of(2023, 4, 24, 19, 0);
        var fin = LocalDateTime.of(2023, 4, 24, 20, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();

        var repeticion = new Repeticion(false, 0, null, 0, null, null);

        var id = calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        assertNull(calendario.obtenerSiguienteAlarma(ahora));
        Alarma alarma1 = new Alarma(inicio, Efecto.SONIDO);
        Alarma alarma2 = new Alarma(inicio.plusMinutes(10), Efecto.EMAIL);
        Alarma alarma3 = new Alarma(ahora.minusMinutes(30), Efecto.NOTIFICACION);

        calendario.agregarAlarma(id,alarma1);
        calendario.agregarAlarma(id,alarma2);
        calendario.agregarAlarma(id,alarma3);


        assertEquals(alarma1.producir(),calendario.obtenerSiguienteAlarma(ahora).get(0).producir());
        assertEquals(alarma2.producir(),calendario.obtenerSiguienteAlarma(ahora.plusHours(1)).get(0).producir());
        assertEquals(alarma3.producir(),calendario.obtenerSiguienteAlarma(ahora.minusHours(1)).get(0).producir());
    }


    @Test
    public void modificarAlarmaAEventoCreado(){
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 24, 18, 0);
        var inicio = LocalDateTime.of(2023, 4, 24, 19, 0);
        var fin = LocalDateTime.of(2023, 4, 24, 20, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        Alarma alarmaUnica = new Alarma(inicio, Efecto.SONIDO);
        alarmas.add(alarmaUnica);
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);

        assertEquals(alarmaUnica,calendario.obtenerSiguienteAlarma(ahora));
        Alarma alarmaNueva = new Alarma(inicio.plusDays(10), Efecto.SONIDO);

        calendario.modificarAlarma("0",alarmaNueva);
        assertEquals(alarmaNueva,calendario.obtenerSiguienteAlarma(ahora));

    }

    @Test
    public void eliminarAlarmaAEventoCreado(){
        var calendario = new Calendario();
        var ahora = LocalDateTime.of(2023, 4, 24, 18, 0);
        var inicio = LocalDateTime.of(2023, 4, 24, 19, 0);
        var fin = LocalDateTime.of(2023, 4, 24, 20, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        Alarma alarmaUnica = new Alarma(inicio, Efecto.SONIDO);
        alarmas.add(alarmaUnica);
        var repeticion = new Repeticion(false, 0, null, 0, null, null);

        calendario.crearEvento("", "", inicio, fin, alarmas, repeticion,false);


        assertEquals(alarmaUnica,calendario.obtenerSiguienteAlarma(ahora));
        calendario.eliminarAlarma("0");
        assertNull(calendario.obtenerSiguienteAlarma(ahora));
    }

    @Test
    public void alarmaReproduceEfecto(){
        var tiempo = LocalDateTime.of(2023, 4, 24, 20, 0);
        Alarma alarma1 = new Alarma(tiempo, Efecto.SONIDO);
        Alarma alarma2 = new Alarma(tiempo, Efecto.NOTIFICACION);
        Alarma alarma3 = new Alarma(tiempo, Efecto.EMAIL);

        assertEquals(alarma1.producir(), Efecto.SONIDO.producir());
        assertEquals(alarma2.producir(), Efecto.NOTIFICACION.producir());
        assertEquals(alarma3.producir(), Efecto.EMAIL.producir());
    }

    @Test
    public void PersistenciaTest() {
        Calendario calendario = new Calendario();
        var inicio = LocalDateTime.of(2023, 4, 25, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 25, 12, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(true, 4, null, 0, null, new RepeticionDiaria());
        calendario.crearEvento("EVENTO", "", inicio, fin, alarmas, repeticion,false);

        var ahora = LocalDateTime.of(2023, 4, 26, 0, 0);

        // serializar el objeto
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        calendario.serializar(bytes);

        // deserializar
        Calendario calendarioDeserializado = Calendario.deserializar(new ByteArrayInputStream(bytes.toByteArray()));

        assertNotNull(calendarioDeserializado);
        assertEquals(calendario.getActividades().get("0").getTitulo(), calendarioDeserializado.getActividades().get("0").getTitulo());
        assertEquals(calendario.mostrarSiguienteEvento(ahora).getTiempo(), calendarioDeserializado.mostrarSiguienteEvento(ahora).getTiempo());
    }


    @Test
    public void calendarioEntregaListadoEventosCONRepeticion(){
        var calendario = new Calendario();
        var inicio = LocalDateTime.of(2023, 4, 25, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 25, 12, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(true, 0, null, 0, null, new RepeticionDiaria());
        calendario.crearEvento("EventoX", "", inicio, fin, alarmas, repeticion,false);

        // Tengo un calendario, con 1 solo evento, pero de repecion DIARIA, y quiero ver las 6 primeras repeticiones
        // Osea busco tener eventosX y que tenga 6 eventos con el inicio y fin correcto


        var listado = calendario.obtenerListadoAMostrar("Semanal",inicio,"EVENTO");

        int aumento = 1;

        for (Actividad act: listado){
            assertEquals("EventoX",act.getTitulo());
            assertEquals(inicio.plusDays(aumento),act.getTiempo());
            aumento++;
        }
    }

    @Test
    public void calendarioEntregaListadoEventosSINRepeticion(){
        var calendario = new Calendario();
        var inicio = LocalDateTime.of(2023, 4, 25, 0, 0);
        var fin = LocalDateTime.of(2023, 4, 25, 12, 0);
        ArrayList<Alarma> alarmas = new ArrayList<>();
        var repeticion = new Repeticion(false, 0, null, 0, null, null);
        calendario.crearEvento("0", "", inicio, fin, alarmas, repeticion,false);
        for (int i = 1; i< 10;i++){
            String nombreEvento = String.valueOf(i);
            calendario.crearEvento(nombreEvento, "", inicio.plusDays(i), fin.plusDays(i), alarmas, repeticion,false);
        }
        // Tengo un calendario, con 10 eventos en 10 dias distinto, y quiero desde el dia 1, hasta el 7(de forma semanal)
        // Osea busco tener evento 1,2,3,4,5,6

        var listado = calendario.obtenerListadoAMostrar("Semanal",inicio,"EVENTO");
        var enteroNombre = 1;
        for (Actividad act: listado){

            String nombreActividad = String.valueOf(enteroNombre);
            enteroNombre++;
            assertEquals(nombreActividad,act.getTitulo());
        }
    }

    @Test
    public void  calendarioEntregaListadoTareas(){
        var calendario = new Calendario();
        var fin1 = LocalDateTime.of(2023, 4, 12, 21, 0);
        ArrayList<Alarma> alarmas1 = new ArrayList<>();
        calendario.crearTarea("TAREA1", "", false, fin1, alarmas1,false);


        var fin2 = LocalDateTime.of(2023, 4, 15, 21, 0);
        ArrayList<Alarma> alarmas2 = new ArrayList<>();
        calendario.crearTarea("TAREA2", "", false, fin2, alarmas2,false);

        // EXISTEN LAS 2 TAREAS

        var listado = calendario.obtenerListadoAMostrar("Semanal",fin1.minusDays(3),"TAREA");
        assertEquals(listado.size(),2);
    }

}