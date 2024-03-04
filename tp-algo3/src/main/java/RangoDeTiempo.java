public enum RangoDeTiempo {
    DIARIO("DIARIO"),
    SEMANAL("SEMANAL"),
    MENSUAL("MENSUAL");

    private final String adicional;

    RangoDeTiempo(String adicional){this.adicional = adicional;}
    public String rangoTemporal(){return  adicional;}

}
