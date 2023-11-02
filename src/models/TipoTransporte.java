
package models;

public enum TipoTransporte {
    
    TAXI("Taxi"),
    AVION("Avion"),
    MICRO_LARGA_DISTANCIA("Micro larga distancia");

    private String valor;

    private TipoTransporte(String valor) {
        this.valor = valor;
    }

    public String getValorStr() {
        return valor;
    }
    
    
}
