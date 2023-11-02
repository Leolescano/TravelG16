package models;

public class Pasaje {
	
    private Integer idPasaje;
    private TipoTransporte tipoTransporte;
    private Double importe;
    private Ciudad ciudadOrigen;
    private Ciudad ciudadDestino;
    private Boolean estado;

    public Pasaje() {}

    public Pasaje(Integer idPasaje, TipoTransporte tipoTransporte, Double importe,
                              Ciudad ciudadOrigen, Ciudad ciudadDestino) {
        this.idPasaje = idPasaje;
        this.tipoTransporte = tipoTransporte;
        this.importe = importe;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.estado = true;
    }

    public Pasaje(TipoTransporte tipoTransporte, Double importe,
                              Ciudad ciudadOrigen, Ciudad ciudadDestino) {
        this.tipoTransporte = tipoTransporte;
        this.importe = importe;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.estado = true;
    }

    public Integer getIdPasaje() {
        return idPasaje;
    }

    public void setIdPasaje(Integer idPasaje) {
        this.idPasaje = idPasaje;
    }

    public TipoTransporte getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoTransporte tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Ciudad getCiudadOrigen() {
        return ciudadOrigen;
    }

    public void setCiudadOrigen(Ciudad ciudadOrigen) {
        this.ciudadOrigen = ciudadOrigen;
    }

    public Ciudad getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(Ciudad ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public Boolean getEstado() {
        return true;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return """
            ID: %d
            Tipo Transporte: %s
            Importe: %,.2f
            Ciudad de Origen: %s
            Ciudad de Destino: %s
            Aplicable: %s
            """.formatted(idPasaje, tipoTransporte.name(), importe,
            ciudadOrigen.getNombre(), ciudadDestino.getNombre(), estado);
    }
}
