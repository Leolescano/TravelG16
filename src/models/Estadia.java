package models;

import java.time.*;
import java.time.temporal.*;
import java.util.*;

public class Estadia {
	
    private Integer idAlojamiento;
    private Establecimiento establecimiento;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Servicio servicio;
    private Temporada temporada;
    private Double importeDiario;
    private Ciudad ciudadDestino;
    private Boolean estado;

    public Estadia() {}

    public Estadia(Integer idAlojamiento, Establecimiento establecimiento, LocalDate checkIn, LocalDate checkOut,
                                       Servicio servicio, Double importeDiario, Ciudad ciudadDestino) {
        this.idAlojamiento = idAlojamiento;
        this.establecimiento = establecimiento;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.servicio = servicio;
        this.ciudadDestino = ciudadDestino;
        this.importeDiario = importeDiario;
        this.estado = true;

        LocalDate fechaTemporadaAltaAjustada = ciudadDestino.getFechaDeTemporadaAlta().withYear(checkIn.getYear());
        long diasDiferencia = ChronoUnit.DAYS.between(fechaTemporadaAltaAjustada, checkIn);

        if (Math.abs(diasDiferencia) <= 45) {
             this.temporada = Temporada.ALTA;                  
        } else if (Math.abs(diasDiferencia) > 45 && (Math.abs(diasDiferencia) <= 90)) {
            this.temporada = Temporada.MEDIA;
        } else {
            this.temporada = Temporada.BAJA;                           
        }
    }

    public Estadia(Establecimiento establecimiento, LocalDate checkIn, LocalDate checkOut,
                                       Servicio servicio, Double importeDiario, Ciudad ciudadDestino) {
        this.establecimiento = establecimiento;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.servicio = servicio;
        this.ciudadDestino = ciudadDestino;
        this.importeDiario = importeDiario;
        this.estado = true;

        LocalDate fechaTemporadaAltaAjustada = ciudadDestino.getFechaDeTemporadaAlta().withYear(checkIn.getYear());
        long diasDiferencia = ChronoUnit.DAYS.between(fechaTemporadaAltaAjustada, checkIn);

        if (Math.abs(diasDiferencia) <= 45) {
             this.temporada = Temporada.ALTA;                  
        } else if (Math.abs(diasDiferencia) > 45 && (Math.abs(diasDiferencia) <= 90)) {
            this.temporada = Temporada.MEDIA;
        } else {
            this.temporada = Temporada.BAJA;                           
        }

    }

    public Integer getIdEstadia() {
        return idAlojamiento;
    }

    public void setIdEstadia(Integer idAlojamiento) {
        this.idAlojamiento = idAlojamiento;
    }

    public Establecimiento getEstablecimiento() {
            return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        this.temporada = temporada;
    }


    public Double getImporteDiario() {
        return importeDiario;
    }

    public void setImporteDiario(Double importeDiario) {
        this.importeDiario = importeDiario;
    }

    public Ciudad getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(Ciudad ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
    return """
            ID: %d
            Establecimiento: %s
            Checking: %s
            Checkout: %s
            Servicio: %s
            Diaria: %,.2f
            Ciudad de Destino: %s
            """.formatted(idAlojamiento, establecimiento.getNombre(), checkIn, checkOut,
                servicio, importeDiario, ciudadDestino.getNombre());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.idAlojamiento);
        hash = 67 * hash + Objects.hashCode(this.establecimiento);
        hash = 67 * hash + Objects.hashCode(this.checkIn);
        hash = 67 * hash + Objects.hashCode(this.checkOut);
        hash = 67 * hash + Objects.hashCode(this.servicio);
        hash = 67 * hash + Objects.hashCode(this.temporada);
        hash = 67 * hash + Objects.hashCode(this.importeDiario);
        hash = 67 * hash + Objects.hashCode(this.ciudadDestino);
        hash = 67 * hash + Objects.hashCode(this.estado);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estadia other = (Estadia) obj;
        if (!Objects.equals(this.idAlojamiento, other.idAlojamiento)) {
            return false;
        }
        if (!Objects.equals(this.establecimiento, other.establecimiento)) {
            return false;
        }
        if (!Objects.equals(this.checkIn, other.checkIn)) {
            return false;
        }
        if (!Objects.equals(this.checkOut, other.checkOut)) {
            return false;
        }
        if (this.servicio != other.servicio) {
            return false;
        }
        if (this.temporada != other.temporada) {
            return false;
        }
        if (!Objects.equals(this.importeDiario, other.importeDiario)) {
            return false;
        }
        if (!Objects.equals(this.ciudadDestino, other.ciudadDestino)) {
            return false;
        }
        return Objects.equals(this.estado, other.estado);
    }
}
