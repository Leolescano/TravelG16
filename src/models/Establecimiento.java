package models;

public class Establecimiento {

    private Integer idEstablecimiento;
    private String nombre;
    private String direccion;
    private String telefono;

    public Establecimiento() {
    }

    public Establecimiento(Integer idAlojamiento, String nombre, String direccion, String telefono) {
        this.idEstablecimiento = idAlojamiento;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Establecimiento(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Integer getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(Integer idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }  
}
