package models;

import javax.swing.*;

public class PaqueteTuristico implements Comparable<PaqueteTuristico> {

    private Integer idPaquete;
    private Ciudad ciudadOrigen;
    private Ciudad ciudadDestino;
    private Estadia estadia;
    private Pasaje pasaje;
    private Double valorTotal = 0.0;
    private Boolean estado;

    public PaqueteTuristico() {}

    public PaqueteTuristico(Integer idPaquete, Estadia estadia, Pasaje pasaje, Integer cantidadPasajeros, Double valorTotal) {
            if (estadia.getCiudadDestino().equals(pasaje.getCiudadDestino())) {
                this.estadia = estadia;
                this.idPaquete = idPaquete;
                this.pasaje = pasaje;
                this.ciudadOrigen = pasaje.getCiudadOrigen();
                this.ciudadDestino = pasaje.getCiudadDestino();
                this.estado = true;
            } else {
                JOptionPane.showMessageDialog(null, "La localizacion del alojamiento precisa ser igual que el destino del pasaje.");
            }
    }

    public PaqueteTuristico(Estadia estadia, Pasaje pasaje) {
        if (estadia.getCiudadDestino().equals(pasaje.getCiudadDestino())) {
            this.estadia = estadia;
            this.pasaje = pasaje;
            this.ciudadOrigen = pasaje.getCiudadOrigen();
            this.ciudadDestino = pasaje.getCiudadDestino();
            this.estado = true;
        } else {
            JOptionPane.showMessageDialog(null, "La localizacion del alojamiento precisa ser igual que el destino del pasaje.");
        }
    }

    public Integer getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(Integer idPaquete) {
        this.idPaquete = idPaquete;
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

    public Estadia getEstadia() {
        return estadia;
    }

    public void setEstadia(Estadia estadia) {
        this.estadia = estadia;
    }

    public Pasaje getPasaje() {
        return pasaje;
    }

    public void setPasaje(Pasaje pasaje) {
        this.pasaje = pasaje;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }


    @Override
    public int compareTo(PaqueteTuristico paqueteTuristico) {
        return this.estadia.getCheckIn().compareTo(paqueteTuristico.getEstadia().getCheckIn());
    }

    @Override
    public String toString() {
        return "Valor Total: " + this.valorTotal + "\n" +
                "Ciudad Destino: " + this.ciudadDestino + "\n";
    }
}
