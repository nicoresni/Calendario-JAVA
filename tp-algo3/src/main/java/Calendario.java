
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.time.*;
import java.util.*;

public class Calendario {
    // Acumulador de ACTIVIDADES
    private HashMap<String, Actividad> actividades;
    private int idCantActividades;

    // IDs de EVENTOS
    private ArrayList<String> idsEventos;

    // IDs de TAREAS
    private ArrayList<String> idsTareas;

    // Acumulador de ALARMAS
    private HashMap<String, Alarma> alarmas;
    private int idCantAlarmas;


    public Calendario() {
        this.actividades = new HashMap<>();
        this.idCantActividades = 0;
        this.idsTareas = new ArrayList<>();
        this.idsEventos = new ArrayList<>();
        this.alarmas = new HashMap<>();
        this.idCantAlarmas = 0;
    }

    public void setActividades(HashMap<String, Actividad> actividades) {
        this.actividades = actividades;
    }

    public int getIdCantActividades() {
        return idCantActividades;
    }

    public void setIdCantActividades(int idCantActividades) {
        this.idCantActividades = idCantActividades;
    }

    public ArrayList<String> getIdsEventos() {
        return idsEventos;
    }

    public void setIdsEventos(ArrayList<String> idsEventos) {
        this.idsEventos = idsEventos;
    }

    public ArrayList<String> getIdsTareas() {
        return idsTareas;
    }

    public void setIdsTareas(ArrayList<String> idsTareas) {
        this.idsTareas = idsTareas;
    }

    public void setAlarmas(HashMap<String, Alarma> alarmas) {
        this.alarmas = alarmas;
    }

    public int getIdCantAlarmas() {
        return idCantAlarmas;
    }

    public void setIdCantAlarmas(int idCantAlarmas) {
        this.idCantAlarmas = idCantAlarmas;
    }

    // FUNCIONES ADICIONALES ------------------------------------------------------------------------------------------------------------------

    private void ordenarListaActividades(ArrayList<Actividad> actividades) {
        actividades.sort(new Comparator<>() {
            @Override
            public int compare(Actividad ac1, Actividad ac2) {
                return ac1.getTiempo().compareTo(ac2.getTiempo());
            }
        });
    }

    private void ordenarListaAlarmas(ArrayList<Alarma> alarmas) {
        alarmas.sort(new Comparator<>() {
            @Override
            public int compare(Alarma a1, Alarma a2) {
                return a1.getHoraAbsoluta().compareTo(a2.getHoraAbsoluta());
            }
        });
    }

    private ArrayList<Actividad> crearArregloActividad(ArrayList<String> idsParaUtilizar) {
        ArrayList<Actividad> listadoDevolver = new ArrayList<>();
        for (String id: idsParaUtilizar){
            listadoDevolver.add(this.actividades.get(id));
        }
        ordenarListaActividades(listadoDevolver);
        return listadoDevolver;
    }


    private ArrayList<Alarma> crearArregloAlarmas() {
        ArrayList<Alarma> listaAlarmas = new ArrayList<>();
        for (Map.Entry<String, Alarma> entry : this.alarmas.entrySet()) {
            listaAlarmas.add(entry.getValue());
        }
        ordenarListaAlarmas(listaAlarmas);
        return listaAlarmas;
    }

    // ALARMAS ----------------------------------------------------------------------------------------------------------------------------------------------------------

    public HashMap<String, Alarma> getAlarmas() {
        return alarmas;
    }

    public String agregarAlarma(String idActividad, Alarma alarmaUnica) {
        alarmaUnica.setIdActividad(idActividad);
        String idAlarma;

        if (this.actividades.containsKey(idActividad)) {
            // Es un Evento
            Actividad ac = this.actividades.get(idActividad);
            ArrayList<Alarma> alarmasActividad = ac.getAlarmas();
            idAlarma = Integer.toString(this.idCantAlarmas);
            this.idCantAlarmas++;
            alarmaUnica.setIdAlarma(idAlarma);
            alarmasActividad.add(alarmaUnica);
        } else {
            // Dio un ID de nada
            return null;
        }
        this.alarmas.put(idAlarma, alarmaUnica);
        return idAlarma;
    }

    public void modificarAlarma(String idAlarma, Alarma nuevaAlarma) {
        if (this.alarmas.containsKey(idAlarma)) {
            Alarma anterior = this.alarmas.get(idAlarma);
            nuevaAlarma.setIdAlarma(idAlarma);
            String idActividad = anterior.getIdActividad();
            nuevaAlarma.setIdActividad(idActividad);

            alarmas.put(idAlarma, nuevaAlarma);
            Actividad ac = this.actividades.get(idActividad);
            ArrayList<Alarma> alarmasActividad = ac.getAlarmas();
            alarmasActividad.remove(anterior);
            alarmasActividad.add(nuevaAlarma);
            ac.setAlarmas(alarmasActividad);
        }

    }

    public void eliminarAlarma(String idAlarma) {
        if (!this.alarmas.containsKey(idAlarma)) {
            return;
        }
        this.alarmas.remove(idAlarma);
    }

    private void agregarNuevasAlarmasdeActividad(ArrayList<Alarma> alarmas, String idActividad) {
        for (Alarma a : alarmas) {
            a.setIdActividad(idActividad);
            String idAlarma = Integer.toString(this.idCantAlarmas);
            a.setIdAlarma(idAlarma);
            this.idCantAlarmas++;
            this.alarmas.put(idAlarma, a);
        }
    }

    private void actualizarNuevasAlarmas(ArrayList<Alarma> alarmas, String idActividad) {

        // Borrar las viejas alarmar del Hash de Alarmas
        ArrayList<Alarma> viejasAlarmas = this.actividades.get(idActividad).getAlarmas();
        for (Alarma a : viejasAlarmas) {
            this.alarmas.remove(a.getIdAlarma());
        }

        // Guardar nuevas Alarmas en Hash de Alarmas
        for (Alarma a : alarmas) {
            a.setIdActividad(idActividad);
            String idAlarma = Integer.toString(this.idCantAlarmas);
            a.setIdAlarma(idAlarma);
            this.idCantAlarmas++;
            this.alarmas.put(idAlarma, a);
        }
    }


    // ACTIVIDADES -------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public HashMap<String, Actividad> getActividades() {
        return actividades;
    }


    public void eliminarActividad(String id) {
        if (this.actividades.containsKey(id)) {
            Actividad acABorrar = actividades.get(id);
            this.actividades.remove(id);
            for (Alarma a : acABorrar.getAlarmas()) {
                eliminarAlarma(a.getIdAlarma());
            }
        }
        if (this.idsEventos.contains(id)) {
            this.idsEventos.remove(id);
        } else {
            this.idsTareas.remove(id);
        }

    }

    // EVENTOS -------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public String crearEvento(String titulo, String descripcion, LocalDateTime inicio,
                              LocalDateTime fin, ArrayList<Alarma> alarmas, Repeticion repeticion, boolean diaCompleto) {
        // CREAR EVENTO
        String idEvento = Integer.toString(this.idCantActividades);
        this.idCantActividades++;
        this.idsEventos.add(idEvento);
        Evento nuevo_evento;
        if (diaCompleto){
            nuevo_evento = new Evento(titulo, descripcion, inicio, idEvento, alarmas, inicio.plusDays(1), repeticion, diaCompleto);
        }else{
            nuevo_evento = new Evento(titulo, descripcion, inicio, idEvento, alarmas, fin, repeticion, diaCompleto);
        }
        // Agregar Alarmas
        agregarNuevasAlarmasdeActividad(alarmas, idEvento);

        // AGREGARLO en EVENTOS
        this.actividades.put(idEvento, nuevo_evento);

        return idEvento;
    }

    public void modificarEvento(String id, String titulo, String descripcion, LocalDateTime inicio,
                                LocalDateTime fin, ArrayList<Alarma> alarmas, Repeticion repeticion) {
        // Supongo que modificas todo, si solo modificas 1 sola cosa,
        // entonces esto tiene que recibir lo anterior
        // igual eso es mas labor de la interfaz
        Actividad modificar = this.actividades.get(id);
        if (modificar == null) {
            return;
        }

        actualizarNuevasAlarmas(alarmas, id);
        modificar.modificarActividad(titulo, descripcion, inicio, alarmas, fin, repeticion, false);
        this.actividades.put(id, modificar);
    }


    // TAREAS -------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public String crearTarea(String titulo, String descripcion, boolean completada,
                             LocalDateTime vencimiento, ArrayList<Alarma> alarmas,boolean diaCompleto) {
        // Crear TAREA
        String idTarea = Integer.toString(this.idCantActividades);
        this.idCantActividades++;
        this.idsTareas.add(idTarea);
        Tarea nueva_tarea = new Tarea(titulo, descripcion, vencimiento, idTarea, alarmas, completada,diaCompleto);

        // AGREGARLA en TAREAS
        this.actividades.put(idTarea, nueva_tarea);
        // Agregar las Alarmas
        agregarNuevasAlarmasdeActividad(alarmas, idTarea);

        return idTarea;
    }

    public void modificarTarea(String id, String titulo, String descripcion, boolean completada,
                               LocalDateTime vencimiento, ArrayList<Alarma> alarmas) {
        Actividad modificar = this.actividades.get(id);
        if (modificar == null) {
            return;
        }
        actualizarNuevasAlarmas(alarmas, id);
        modificar.modificarActividad(titulo, descripcion, vencimiento, alarmas, null, null, completada);
        this.actividades.put(id, modificar);
    }

    // PERSISTENCIA -----------------------------------------------------------------------------------------------------------------------------------------------------------

    public void serializar(OutputStream os) {
        GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.registerTypeAdapter(LocalDateTime.class, new AdapterLocalDateTime());
        gsonbuilder.registerTypeAdapter(Actividad.class, new AdapterActividad());
        gsonbuilder.registerTypeAdapter(ReglaRepeticion.class, new AdapterRepeticion());
        Gson gson = gsonbuilder.setPrettyPrinting().create();

        try (Writer writer = new OutputStreamWriter(os)){
            gson.toJson(this, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Calendario deserializar(InputStream is){
        GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.registerTypeAdapter(LocalDateTime.class, new AdapterLocalDateTime());
        gsonbuilder.registerTypeAdapter(Actividad.class, new AdapterActividad());
        gsonbuilder.registerTypeAdapter(ReglaRepeticion.class, new AdapterRepeticion());
        Gson gson = gsonbuilder.setPrettyPrinting().create();

        try (Reader reader = new InputStreamReader(is)) {
            return gson.fromJson(reader, Calendario.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* MOSTRAR TAREAS ORDENADAS SEGUN RANGO TIEMPO */
    // MOSTRAS SIGUIENTE TAREA-EVENTO-ALARMA ----------------------------------------------------------------------------------------------------------------------------------



    public Actividad obtenerActividad(String id){
        return this.actividades.get(id);
    }

    public ArrayList<Actividad> obtenerListadoAMostrar(String rango, LocalDateTime desde,String tipoActividad){
        ArrayList<String> idsAUtilizar;
        if (tipoActividad == "EVENTO"){
            idsAUtilizar = this.idsEventos;
        } else if( tipoActividad == "TAREA"){
            idsAUtilizar = this.idsTareas;
        }else{ return null;}

        ArrayList<Actividad> listadoBuscado = new ArrayList<>();
        ArrayList<Actividad> listadoOrdenado = crearArregloActividad(idsAUtilizar);


        LocalDateTime hasta;
        switch (rango) {
            case "Semanal" -> hasta = desde.plusWeeks(1);
            case "Mensual" -> hasta = desde.plusMonths(1);
            default -> hasta = desde.plusDays(1);
        }

        var visitante = new visitanteListadoActividad(hasta,desde,listadoBuscado);
        for (Actividad act: listadoOrdenado){
            act.aceptar(visitante);
        }
        ordenarListaActividades(visitante.listado());

        return visitante.listado();

    }

    public String mostrarActividadParcial(Actividad act){
        var visitante = new visitanteActividadParcial();
        act.aceptar(visitante);
        return visitante.getActividadParcial();
    }

    public String mostrarActividadCompleta(Actividad act){
        var visitante = new visitanteActividadCompleta();
        act.aceptar(visitante);
        return visitante.getActividadCompleta();
    }

    public Actividad mostrarSiguienteTarea(LocalDateTime ahora) {
        ArrayList<Actividad> listado_tareas = crearArregloActividad(idsTareas);
        var visitante = new visitanteSiguienteActividad(ahora);
        for (Actividad tarea : listado_tareas) {
            tarea.aceptar(visitante);
        }
        return visitante.siguienteActividad();
    }

    public Actividad mostrarSiguienteEvento(LocalDateTime ahora) {
        ArrayList<Actividad> listado_eventos = crearArregloActividad(idsEventos);
        var visitante = new visitanteSiguienteActividad(ahora);
        for (Actividad evento : listado_eventos) {
            evento.aceptar(visitante);
        }
        return visitante.siguienteActividad();
    }

    public ArrayList<Alarma> obtenerSiguienteAlarma(LocalDateTime ahora) {
        ArrayList<Alarma> proximas = new ArrayList<>();


        ArrayList<Alarma> listadoAlarma = crearArregloAlarmas(); // Esta ordenado
        for (Alarma a : listadoAlarma) {
            var horaAlarma = a.getHoraAbsoluta();
            if (horaAlarma.equals(ahora)){
                proximas.add(a);
            }
        }
        return proximas;
    }


    // TIEMPO RESTANTE COMPLETAR ACTIVIDAD ----------------------------------------------------------------------------------------------------------------------------------------
    public Duration tiempoRestanteParaCompletarActividad(String idActividad, LocalDateTime ahora) {
        Actividad actividad = this.actividades.get(idActividad);
        var visitante = new visitanteTiempoRestanteActividad(ahora);
        actividad.aceptar(visitante);
        return visitante.tiempoRestante();
    }
}