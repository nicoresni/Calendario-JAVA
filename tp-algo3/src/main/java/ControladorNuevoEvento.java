import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ControladorNuevoEvento {
    private Calendario modelo;
    private ArrayList<Alarma> alarmas;
    @FXML
    private TextField Nombre;
    @FXML
    private TextArea Descripcion;
    @FXML
    private DatePicker DiaInicio;
    @FXML
    private Spinner<Integer> HoraInicio;
    @FXML
    private Spinner<Integer> MinutoInicio;
    @FXML
    private ToggleButton Completo;
    @FXML
    private DatePicker DiaFin;
    @FXML
    private Spinner<Integer> HoraFin;
    @FXML
    private Spinner<Integer> MinutoFin;
    @FXML
    private ToggleButton HayRepeticion;
    @FXML
    private RadioButton RepeticionDiaria;
    @FXML
    private RadioButton RepeticionSemanal;
    @FXML
    private RadioButton RepeticionMensual;
    @FXML
    private RadioButton RepeticionAnual;
    @FXML
    private Spinner<Integer> IntervaloRepeticion;
    @FXML
    private Spinner<Integer> OcurrenciasRepeticion;
    @FXML
    private DatePicker HastaRepeticion;
    @FXML
    private Button CrearEvento;
    @FXML
    private Button Salir;
    @FXML
    private Spinner<Integer> TiempoPosterior;
    @FXML
    private RadioButton Notificacion;
    @FXML
    private RadioButton Email;
    @FXML
    private RadioButton Sonido;
    @FXML
    private Button CrearAlarma;
    @FXML
    private Label CantidadAlarmas;
    @FXML
    private ToggleGroup ReglaRepeticion;
    @FXML
    private ToggleGroup Efectos;
    @FXML
    private void salir() throws IOException {
        CalendarioApp.setRoot("calendarioApp");
    }
    private void setModelo(Calendario modelo){
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

    private ReglaRepeticion devolverReglaRepeticion(ToggleGroup reglaRepeticion){
        Toggle elegido = reglaRepeticion.getSelectedToggle();
        ReglaRepeticion repeticionPedida;
        if (elegido == RepeticionAnual){
            repeticionPedida = new RepeticionAnual();
        } else if (elegido == RepeticionSemanal) {
            repeticionPedida = new RepeticionSemanal();
        } else if (elegido == RepeticionMensual) {
            repeticionPedida = new RepeticionMensual();
        } else{
            repeticionPedida = new RepeticionDiaria();
        }
        return  repeticionPedida;
    }

    private Efecto devolverEfecto(ToggleGroup efecto) {
        Toggle elegido = efecto.getSelectedToggle();
        Efecto efectoPedido;
        if (elegido == Notificacion){
            efectoPedido = Efecto.NOTIFICACION;
        } else if (elegido == Email) {
            efectoPedido = Efecto.EMAIL;
        } else {
            efectoPedido = Efecto.SONIDO;
        }
        return efectoPedido;
    }
    @FXML
    public void initialize() {
        LoadCalendar();
        alarmas = new ArrayList<>();
        SpinnerValueFactory<Integer> valueFactoryHoraInicio = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalDateTime.now().getHour());
        HoraInicio.setValueFactory(valueFactoryHoraInicio);
        SpinnerValueFactory<Integer> valueFactoryHoraFin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalDateTime.now().getHour());
        HoraFin.setValueFactory(valueFactoryHoraFin);
        SpinnerValueFactory<Integer> valueFactoryMinutoInicio = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalDateTime.now().getMinute());
        MinutoInicio.setValueFactory(valueFactoryMinutoInicio);
        SpinnerValueFactory<Integer> valueFactoryMinutoFin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalDateTime.now().getMinute());
        MinutoFin.setValueFactory(valueFactoryMinutoFin);
        SpinnerValueFactory<Integer> valueFactoryIntervalo = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,7);
        IntervaloRepeticion.setValueFactory(valueFactoryIntervalo);
        SpinnerValueFactory<Integer> valueFactoryOcurrencias = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100000);
        OcurrenciasRepeticion.setValueFactory(valueFactoryOcurrencias);
        SpinnerValueFactory<Integer> valueFactoryTiempoPosterior = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000000);
        TiempoPosterior.setValueFactory(valueFactoryTiempoPosterior);
        DiaInicio = new DatePicker(LocalDate.now());
        DiaFin = new DatePicker(LocalDate.now());
    }

    @FXML
    private void handleCrearEvento(ActionEvent event) throws IOException {
        LocalDateTime hasta;

        if (HastaRepeticion.getValue() == null){
            hasta = null;
        } else {
            hasta = HastaRepeticion.getValue().atStartOfDay();
        }

        ReglaRepeticion tipoRepeticion = devolverReglaRepeticion(ReglaRepeticion);
        Repeticion repeticion = new Repeticion(HayRepeticion.isSelected(), IntervaloRepeticion.getValue(), hasta, OcurrenciasRepeticion.getValue(), null, tipoRepeticion );
        LocalDateTime inicio = DiaInicio.getValue().atStartOfDay().plusHours(HoraInicio.getValue()).plusMinutes(MinutoInicio.getValue());
        LocalDateTime fin = DiaFin.getValue().atStartOfDay().plusHours(HoraFin.getValue()).plusMinutes(MinutoFin.getValue());
        modelo.crearEvento(Nombre.getText(), Descripcion.getText(), inicio, fin, alarmas, repeticion,Completo.isSelected());
        SaveCalendar(modelo);
        salir();
    }

    @FXML
    private void handleCrearAlarma(ActionEvent event) {
        LocalDateTime inicio = DiaInicio.getValue().atStartOfDay().plusHours(HoraInicio.getValue()).plusMinutes(MinutoInicio.getValue());
        var efecto = devolverEfecto(Efectos);
        Alarma alarma = new Alarma(inicio, Duration.ofMinutes(TiempoPosterior.getValue()), efecto);
        alarmas.add(alarma);
        CantidadAlarmas.setText(String.valueOf(Integer.parseInt(CantidadAlarmas.getText())+1));
    }
}
