

package dao;


import static connect.Conexion.getConexion;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import models.*;
import static utils.ManejoRecursos.closeConnection;
import static utils.ManejoRecursos.closePreparedStatement;
import static utils.ManejoRecursos.closeResultSet;

public class CiudadData {
	

    public static void cargarCiudad(Ciudad ciudad) {
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conexion = getConexion();
            String sql = "INSERT INTO ciudad (nombre, provincia, pais, fechaDeTemporadaAlta, estado)"
                            + "VALUES (?, ?, ?, ?, ?)";
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ciudad.getNombre());
            Provincia provinciaAux = ProvinciaData.buscarProvinciaPorNombre(ciudad.getProvincia().getNombre());
            ps.setInt(2, provinciaAux.getIdProvincia());
            Pais paisAux = PaisData.buscarPaisPorNombre(ciudad.getPais().getNombre());
            ps.setInt(3, paisAux.getIdPais());
            ps.setDate(4, java.sql.Date.valueOf(ciudad.getFechaDeTemporadaAlta()));                  
            ps.setBoolean(5, ciudad.getEstado());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                    ciudad.setIdCiudad(rs.getInt(1));
                    JOptionPane.showMessageDialog (null, "Ciudad guardada");
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se permiten ciudades repetidas.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }

    public static Ciudad buscarCiudadPorId(Integer id) {
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Ciudad ciudad = null;
        try {
            conexion = getConexion();
            String sql = "SELECT idCiudad, nombre, provincia, pais, fechaDeTemporadaAlta, estado FROM ciudad WHERE idCiudad = ? ";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                    ciudad = new Ciudad();
                    ciudad.setIdCiudad(id);
                    ciudad.setNombre(rs.getString("nombre"));
                    
                    Provincia provinciaAux = ProvinciaData.buscarProvinciaPorId(rs.getInt("provincia"));
                    ciudad.setProvincia(provinciaAux);
                    
                    Pais paisAux = PaisData.buscarPaisPorId(rs.getInt("pais"));
                    ciudad.setPais(paisAux);
                    
                    ciudad.setFechaDeTemporadaAlta(rs.getDate("fechaDeTemporadaAlta").toLocalDate());                                
                    ciudad.setEstado(rs.getBoolean("estado"));
                }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al buscar la ciudad por el id.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return ciudad;
    }

    public static Ciudad buscarCiudadPorNombre(String nombre) {
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Ciudad ciudad = null;
        try {
            conexion = getConexion();
            String sql = "SELECT idCiudad, nombre, provincia, pais, fechaDeTemporadaAlta, estado FROM ciudad WHERE nombre = ? ";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if(rs.next()) {
                ciudad = new Ciudad();
                ciudad.setIdCiudad(rs.getInt("idCiudad"));
                ciudad.setNombre(nombre);
                Provincia provinciaAux = ProvinciaData.buscarProvinciaPorId(rs.getInt("provincia"));
                ciudad.setProvincia(provinciaAux);

                Pais paisAux = PaisData.buscarPaisPorId(rs.getInt("pais"));                   
                ciudad.setPais(paisAux);

                ciudad.setFechaDeTemporadaAlta(rs.getDate("fechaDeTemporadaAlta").toLocalDate()); 
                ciudad.setEstado(rs.getBoolean("estado"));
          
                return ciudad;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al buscar la ciudad por el nombre");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return null;
    }

    public static Set<Ciudad> listarCiudades() {
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Set <Ciudad> ciudades = new HashSet<>();
        try {
            conexion = getConexion();
            String sql = "SELECT idCiudad, nombre, provincia, pais, fechaDeTemporadaAlta, estado FROM ciudad";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Ciudad ciudad = new Ciudad();
                ciudad.setIdCiudad(rs.getInt("idCiudad"));
                ciudad.setNombre(rs.getString("nombre"));

                Provincia provinciaAux = ProvinciaData.buscarProvinciaPorId(rs.getInt("provincia"));
                ciudad.setProvincia(provinciaAux);

                Pais paisAux = PaisData.buscarPaisPorId(rs.getInt("pais"));
                ciudad.setPais(paisAux);

                ciudad.setFechaDeTemporadaAlta(rs.getDate("fechaDeTemporadaAlta").toLocalDate()); 
                ciudad.setEstado(rs.getBoolean("estado"));
                ciudades.add(ciudad);
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al listar las ciudades.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return ciudades;
    }
}
