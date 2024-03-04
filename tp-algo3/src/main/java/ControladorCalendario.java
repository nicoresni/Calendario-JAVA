import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.time.temporal.ChronoUnit;

import java.io.*;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.*;

public class ControladorCalendario {
    private Calendario modelo;
    private ArrayList<String> idsEventosMostrados;
    private ArrayList<String> EventosMostrados;
    private ArrayList<String> idsTareasMostradas;
    private ArrayList<String> TareasMostradas;
    @FXML
    private Button SiguienteFecha;
    @FXML
    private Button AnteriorFecha;
    @FXML
    private ListView<String> ListaEventos;
    @FXML
    private ListView<String> ListaTareas;
    @FXML
    private Button NuevoEvento;
    @FXML
    private Button EliminarEvento;
    @FXML
    private Button EliminarTarea;
    @FXML
    private Button NuevaTarea;
    @FXML
    private ChoiceBox<String> FormatoFechas;
    @FXML
    private Label FechaActual;
    @FXML
    private Label ActividadSeleccionada;
    private final String[] formatos = {"Diario", "Semanal", "Mensual"};
    private LocalDateTime tiempoCalendario;

    public void setModelo(Calendario modelo) {
        this.modelo = modelo;
    }

    private void LoadCalendar() {
        try {
            FileInputStream fileInputStream = new FileInputStream("actividades.json");
            var calendario = Calendario.deserializar(fileInputStream);
            setModelo(calendario);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SaveCalendar(Calendario calendario) throws FileNotFoundException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("actividades.json");
            calendario.serializar(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<ArrayList<String>> mostrarActividades(String tipoActividad, LocalDateTime tiempoCalendario) {
        var actividades = modelo.obtenerListadoAMostrar(FormatoFechas.getValue(), tiempoCalendario, tipoActividad);
        var actividadesMostrables = new ArrayList<String>();
        var idsActividadesMostradas = new ArrayList<String>();
        for (Actividad ac: actividades){
            idsActividadesMostradas.add(ac.getIdActividad());
            actividadesMostrables.add(modelo.mostrarActividadParcial(ac));
        }
        var listadosNecesarios = new ArrayList<ArrayList<String>>();
        listadosNecesarios.add(idsActividadesMostradas);
        listadosNecesarios.add(actividadesMostrables);
        return listadosNecesarios;
    }


    private void actualizarActividadesMostrables(){
        var actualizacionEvento = mostrarActividades("EVENTO", tiempoCalendario);
        var actualizacionTarea = mostrarActividades("TAREA", tiempoCalendario);
        this.idsEventosMostrados = actualizacionEvento.get(0);
        this.EventosMostrados = actualizacionEvento.get(1);
        this.idsTareasMostradas = actualizacionTarea.get(0);
        this.TareasMostradas = actualizacionTarea.get(1);
    }

    private LocalDateTime actualizarTiempoCalendario(String rango, int aux) {
        if (aux == 1) {
            return switch (rango) {
                case "Semanal" -> tiempoCalendario.plusWeeks(1);
                case "Mensual" -> tiempoCalendario.plusMonths(1);
                default -> tiempoCalendario.plusDays(1);
            };
        } else {
            return switch (rango) {
                case "Semanal" -> tiempoCalendario.minusWeeks(1);
                case "Mensual" -> tiempoCalendario.minusMonths(1);
                default -> tiempoCalendario.minusDays(1);
            };
        }
    }

    private String diaFormateado(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return tiempoCalendario.format(dateTimeFormatter);
    }

    @FXML
    public void initialize() {
        LoadCalendar();
        tiempoCalendario = LocalDateTime.now();
        FechaActual.setText(diaFormateado());
        FormatoFechas.getItems().addAll(formatos);
        FormatoFechas.setValue("Diario");
        ListaEventos.getItems().clear();
        ListaTareas.getItems().clear();

        actualizarActividadesMostrables();

        ListaEventos.getItems().addAll(this.EventosMostrados);
        ListaTareas.getItems().addAll(this.TareasMostradas);

        AnimationTimer timer = new AnimationTimer() {
            private final Set<Alarma> alarmasMostradas = new HashSet<>();

            @Override
            public void handle(long now) {
                LocalDateTime ahora = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                var siguienteAlarmas = modelo.obtenerSiguienteAlarma(ahora);

                siguienteAlarmas.removeAll(alarmasMostradas);

                for (Alarma alarma : siguienteAlarmas) {
                    alarmasMostradas.add(alarma);
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        Actividad act = modelo.obtenerActividad(alarma.getIdActividad());
                        alert.setTitle(act.getTitulo());
                        alert.setHeaderText(act.getDescripcion());
                        alert.setContentText(alarma.producir());
                        alert.showAndWait();
                    });
                }
            }
        };
        timer.start();
    }

    @FXML
    private void handleFormatoFechas(ActionEvent event) {
        tiempoCalendario = LocalDateTime.now();
        FechaActual.setText(diaFormateado());
        ListaEventos.getItems().clear();
        ListaTareas.getItems().clear();

        actualizarActividadesMostrables();

        ListaEventos.getItems().addAll(this.EventosMostrados);
        ListaTareas.getItems().addAll(this.TareasMostradas);
    }

    @FXML
    private void handleSiguienteFecha(ActionEvent event) {
        ListaEventos.getItems().clear();
        ListaTareas.getItems().clear();
        tiempoCalendario = actualizarTiempoCalendario(FormatoFechas.getValue(), 1);
        FechaActual.setText(diaFormateado());

        actualizarActividadesMostrables();

        ListaEventos.getItems().addAll(this.EventosMostrados);
        ListaTareas.getItems().addAll(this.TareasMostradas);
    }

    @FXML
    private void handleAnteriorFecha(ActionEvent event) {
        ListaEventos.getItems().clear();
        ListaTareas.getItems().clear();
        tiempoCalendario = actualizarTiempoCalendario(FormatoFechas.getValue(), -1);
        FechaActual.setText(diaFormateado());

        actualizarActividadesMostrables();


        ListaEventos.getItems().addAll(this.EventosMostrados);
        ListaTareas.getItems().addAll(this.TareasMostradas);
    }

    @FXML
    private void handleDetallesEvento(MouseEvent event) {
        String seleccionado = ListaEventos.getSelectionModel().getSelectedItems().get(0);
        var indiceIDEvento = this.EventosMostrados.indexOf(seleccionado);
        var iDEvento = this.idsEventosMostrados.get(indiceIDEvento);
        var actividadElegida = modelo.obtenerActividad(iDEvento);
        ActividadSeleccionada.setText(modelo.mostrarActividadCompleta(actividadElegida));
    }

    @FXML
    private void handleEliminarEvento(ActionEvent event) throws FileNotFoundException {
        String seleccionado = ListaEventos.getSelectionModel().getSelectedItems().get(0);
        var indiceIDEvento = this.EventosMostrados.indexOf(seleccionado);
        var iDEvento = this.idsEventosMostrados.get(indiceIDEvento);
        modelo.eliminarActividad(iDEvento);

        SaveCalendar(modelo);

        ListaEventos.getItems().clear();
        actualizarActividadesMostrables();
        ListaEventos.getItems().addAll(this.EventosMostrados);
    }

    @FXML
    private void handleEliminarTarea(ActionEvent event) throws FileNotFoundException {
        String seleccionado = ListaTareas.getSelectionModel().getSelectedItems().get(0);
        var indiceIDTarea = this.TareasMostradas.indexOf(seleccionado);
        var iDTarea = this.idsTareasMostradas.get(indiceIDTarea);
        modelo.eliminarActividad(iDTarea);

        SaveCalendar(modelo);

        ListaTareas.getItems().clear();
        actualizarActividadesMostrables();
        ListaTareas.getItems().addAll(this.TareasMostradas);
    }

    @FXML
    private void switchToCrearEvento() throws IOException {
        CalendarioApp.setRoot("crearEvento");
    }
    @FXML
    private void switchToCrearTarea() throws IOException {
        CalendarioApp.setRoot("crearTarea");
    }

}
