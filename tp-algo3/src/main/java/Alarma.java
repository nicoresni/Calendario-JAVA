import java.time.Duration;
import java.time.LocalDateTime;

public class Alarma {
    private LocalDateTime horaAbsoluta;
    private Efecto efecto;
    private String IdActividad;
    private String idAlarma;

    // CONSTRUCTOR con Hora Absoluta
    public Alarma(LocalDateTime horaAbsoluta, Efecto efecto) {
        this.horaAbsoluta = horaAbsoluta;
        this.efecto = efecto;
        this.IdActividad = null;
        this.idAlarma = null;
    }
    // CONSTRUCTOR con Duracion Relativa
    public Alarma(LocalDateTime horaActividad, Duration relativa, Efecto efecto){
        this.horaAbsoluta = horaActividad.minusSeconds(relativa.toSeconds());
        this.efecto = efecto;
        this.IdActividad = null;
        this.idAlarma = null;
    }
    // GETTERS

    public LocalDateTime getHoraAbsoluta() {
        return horaAbsoluta;
    }

    public Efecto getEfecto() {
        return efecto;
    }

    public String getIdActividad() {
        return IdActividad;
    }

    public String getIdAlarma() {
        return idAlarma;
    }


    // SETTERS


    public void setHoraAbsoluta(LocalDateTime horaAbsoluta) {
        this.horaAbsoluta = horaAbsoluta;
    }

    public void setEfecto(Efecto efecto) {
        this.efecto = efecto;
    }

    public void setIdActividad(String idActividad) {
        IdActividad = idActividad;
    }

    public void setIdAlarma(String idAlarma) {
        this.idAlarma = idAlarma;
    }

    // METODOS
    public String producir() {
        return this.efecto.producir();
    }
}
