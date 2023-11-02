

package dao;

import static connect.Conexion.getConexion;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import models.*;
import static utils.ManejoRecursos.closeConnection;
import static utils.ManejoRecursos.closePreparedStatement;
import static utils.ManejoRecursos.closeResultSet;


public class ProvinciaData {
    
    public static void cargarProvincia(Provincia provincia) {        
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conexion = getConexion();
            String sql = "INSERT INTO provincia (nombre, pais) VALUES (?, ?)";
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, provincia.getNombre());
            Pais paisAux = PaisData.buscarPaisPorNombre(provincia.getPais().getNombre());
            ps.setInt(2, paisAux.getIdPais()); 
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                provincia.setIdProvincia(rs.getInt(1));
                JOptionPane.showMessageDialog(null, "Provincia guardada");              
            }            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar la provincia.");
        } finally {
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }

    public static Provincia buscarProvinciaPorId(int idProvincia) {        
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Provincia provincia = null;
        try {
            conexion = getConexion();
            String sql = "SELECT * FROM provincia WHERE idProvincia = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProvincia);
            rs = ps.executeQuery();
            if (rs.next()) {
                provincia = new Provincia();
                provincia.setIdProvincia(rs.getInt("idProvincia"));
                provincia.setNombre(rs.getString("nombre"));
                Pais paisAux = PaisData.buscarPaisPorId(rs.getInt("pais"));               
                provincia.setPais(paisAux);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar la provincia por el id.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return provincia;
    }

    public static Provincia buscarProvinciaPorNombre(String nombre) {        
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Provincia provincia = null;
        try {
            conexion = getConexion();
            String sql = "SELECT * FROM provincia WHERE nombre = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) {
                provincia = new Provincia();
                provincia.setIdProvincia(rs.getInt("idProvincia"));
                provincia.setNombre(rs.getString("nombre"));
                Pais paisAux = PaisData.buscarPaisPorId(rs.getInt("pais"));               
                provincia.setPais(paisAux);         
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar la provincia por el nombre.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return provincia;
    }

    public static void actualizarProvincia(Provincia provincia) {       
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = getConexion();
            String sql = "UPDATE provincia SET nombre = ?, pais = ? WHERE idProvincia = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, provincia.getNombre());
            Pais paisAux = PaisData.buscarPaisPorNombre(provincia.getPais().getNombre());
            ps.setInt(2, paisAux.getIdPais());   
            ps.setInt(3, provincia.getIdProvincia());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Provincia actualizada");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar la provincia.");
        } finally {
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }

    public static Set<Provincia> listarProvincias() {        
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        Set<Provincia> provincias = new HashSet<>();
        try {
            conexion = getConexion();
            stmt = conexion.createStatement();
            String sql = "SELECT * FROM provincia";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Provincia provincia = new Provincia();
                provincia.setIdProvincia(rs.getInt("idProvincia"));
                provincia.setNombre(rs.getString("nombre"));
                
                Pais paisAux = PaisData.buscarPaisPorId(rs.getInt("pais"));               
                provincia.setPais(paisAux); 
                
                provincias.add(provincia);
            }
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog (null, "Error al listar las provincias.");
        } finally {
            closeResultSet(rs);           
            closeConnection(conexion);
        }
        return provincias;
    }
}
