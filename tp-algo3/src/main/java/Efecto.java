public enum Efecto{
    NOTIFICACION("Tiene una Notificacion"),
    SONIDO("Ring Ring"),
    EMAIL("Nuevo Email")
    ;

    private final String producir;

    Efecto(String producir) {
        this.producir = producir;
    }
    public String producir() {
        return producir;
    }
}