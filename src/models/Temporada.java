
package models;


public enum Temporada {
    
    ALTA(1.3, "Alta + 30"),
    MEDIA(1.2, "Media + 20%"),
    BAJA(1, "Baja sin recargo");
  
    private double porcentaje;
    private String valor;

    private Temporada(double porcentaje, String valor) {
        this.porcentaje = porcentaje;
        this.valor = valor;
    }

    public double getPorcentaje() {
        return porcentaje;
    }
  
    public String getValorStr() {
        return valor;
    }
}
