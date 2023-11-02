

package models;


public enum Servicio {
    
    SIN_SERVICIO(1, "Sin servicio"),
    MEDIA_PENSION(1.10, "Media pension + 10%"),
    PENSION_COMPLETA(1.20, "Pension Completa + 20%");
    
    private double porcentaje;
    private String valor;

    private Servicio(double porcentaje, String valor) {
        this.porcentaje = porcentaje;
        this.valor = valor;
    }

    public double getPorcentaje() {
        return porcentaje;
    }   

    public String getValorStr(){
        return valor;
    }
}
