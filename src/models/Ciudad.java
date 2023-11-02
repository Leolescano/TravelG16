package models;

import java.time.*;
import java.util.*;

public class Ciudad {
	
    private Integer idCiudad;
    private String nombre;
    private Provincia provincia; 
    private Pais pais;
    private Boolean estado;
    private LocalDate fechaDeTemporadaAlta;

    public Ciudad(){}

    public Ciudad(Integer idCiudad, String nombre, Provincia provincia, Pais pais, LocalDate fechaDeTemporadaAlta ) {
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.provincia = provincia;
        this.pais = pais;
        this.fechaDeTemporadaAlta = fechaDeTemporadaAlta;
        this.estado = true;
    }

    public Ciudad(String nombre, Provincia provincia, Pais pais, LocalDate fechaDeTemporadaAlta) {
        this.nombre = nombre;
        this.provincia = provincia;
        this.pais = pais;
        this.fechaDeTemporadaAlta = fechaDeTemporadaAlta;
        this.estado = true;
    }

    public Integer getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Integer idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public LocalDate getFechaDeTemporadaAlta() {
        return fechaDeTemporadaAlta;
    }

    public void setFechaDeTemporadaAlta(LocalDate fechaDeTemporadaAlta) {
        this.fechaDeTemporadaAlta = fechaDeTemporadaAlta;
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
            Nombre: %s
            Provincia: %s
            Pais: %s
            Estado: %s
            """.formatted(idCiudad, nombre, provincia, pais, estado);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciudad ciudad)) return false;
        if (!Objects.equals(idCiudad, ciudad.idCiudad)) return false;
        if (!Objects.equals(nombre, ciudad.nombre)) return false;
        if (!Objects.equals(provincia, ciudad.provincia)) return false;
        if (!Objects.equals(pais, ciudad.pais)) return false;
        return Objects.equals(estado, ciudad.estado);
    }

    @Override
    public int hashCode() {
        int result = idCiudad != null ? idCiudad.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (provincia != null ? provincia.hashCode() : 0);
        result = 31 * result + (pais != null ? pais.hashCode() : 0);
        result = 31 * result + (estado != null ? estado.hashCode() : 0);
        return result;
    }
}
