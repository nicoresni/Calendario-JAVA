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

public class ControladorNuevaTarea {
    private Calendario modelo;
    private ArrayList<Alarma> alarmas;
    @FXML
    private TextField Nombre;
    @FXML
    private TextArea Descripcion;
    @FXML
    private DatePicker DiaVencimiento;
    @FXML
    private Spinner<Integer> HoraVencimiento;
    @FXML
    private Spinner<Integer> MinutoVencimiento;
    @FXML
    private ToggleButton Completo;
    @FXML
    private ToggleButton Completada;
    @FXML
    private Button CrearTarea;
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

    private void SaveCalendar(Calendario calendario) throws FileNotFoundException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("actividades.json");
            calendario.serializar(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        LoadCalendar();
        alarmas = new ArrayList<>();
        SpinnerValueFactory<Integer> valueFactoryHoraVencimiento = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalDateTime.now().getHour());
        HoraVencimiento.setValueFactory(valueFactoryHoraVencimiento);
        SpinnerValueFactory<Integer> valueFactoryMinutoVencimiento = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalDateTime.now().getMinute());
        MinutoVencimiento.setValueFactory(valueFactoryMinutoVencimiento);
        SpinnerValueFactory<Integer> valueFactoryTiempoPosterior = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000000);
        TiempoPosterior.setValueFactory(valueFactoryTiempoPosterior);
        DiaVencimiento = new DatePicker(LocalDate.now());
    }

    @FXML
    private void handleCrearTarea(ActionEvent event) throws IOException {
        LocalDateTime vencimiento = DiaVencimiento.getValue().atStartOfDay().plusHours(HoraVencimiento.getValue()).plusMinutes(MinutoVencimiento.getValue());
        modelo.crearTarea(Nombre.getText(), Descripcion.getText(), Completada.isSelected(), vencimiento, alarmas, Completo.isSelected());
        SaveCalendar(modelo);
        salir();
    }

    @FXML
    private void handleCrearAlarma(ActionEvent event) {
        LocalDateTime inicio = DiaVencimiento.getValue().atStartOfDay().plusHours(HoraVencimiento.getValue()).plusMinutes(MinutoVencimiento.getValue());
        var efecto = devolverEfecto(Efectos);
        Alarma alarma = new Alarma(inicio, Duration.ofMinutes(TiempoPosterior.getValue()), efecto);
        alarmas.add(alarma);
        CantidadAlarmas.setText(String.valueOf(Integer.parseInt(CantidadAlarmas.getText())+1));
    }
}
