

package dao;

import static connect.Conexion.getConexion;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import models.*;
import static utils.ManejoRecursos.closeConnection;
import static utils.ManejoRecursos.closePreparedStatement;
import static utils.ManejoRecursos.closeResultSet;


public class PaisData {
    
    public static void cargarPais(Pais pais) {       
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conexion = getConexion();
            String sql = "INSERT INTO pais (nombre) VALUES (?)";
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pais.getNombre());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();          
            if (rs.next()) {
                pais.setIdPais(rs.getInt(1));
                JOptionPane.showMessageDialog(null, "País guardado");                  
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el País.");
        } finally {
             closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }

    public static Pais buscarPaisPorId(int idPais) {   
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Pais pais = null;
        try {
            conexion = getConexion();
            String sql = "SELECT * FROM pais WHERE idPais = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPais);
            rs = ps.executeQuery();
            if (rs.next()) {
                pais = new Pais();
                pais.setIdPais(rs.getInt("idPais"));
                pais.setNombre(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al buscar el pais por el id.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return pais;
    }

    public static Pais buscarPaisPorNombre(String nombre) {       
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Pais pais = null;
        try {
            conexion = getConexion();
            String sql = "SELECT * FROM pais WHERE nombre = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) {
                pais = new Pais();
                pais.setIdPais(rs.getInt("idPais"));
                pais.setNombre(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al buscar el pais por el nombre.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return pais;
    }

    public static void actualizarPais(Pais pais) {        
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = getConexion();
            String sql = "UPDATE pais SET nombre = ? WHERE idPais = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, pais.getNombre());
            ps.setInt(2, pais.getIdPais());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "País actualizado");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el País.");
        } finally {
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }

    public static Set<Pais> listarPaises() {       
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        Set<Pais> paises = new HashSet<>();
        try {
            conexion = getConexion();
            stmt = conexion.createStatement();
            String sql = "SELECT * FROM pais";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Pais pais = new Pais();
                pais.setIdPais(rs.getInt("idPais"));
                pais.setNombre(rs.getString("nombre"));
                paises.add(pais);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al listar los paises.");
        } finally {
            closeResultSet(rs);       
            closeConnection(conexion);
        }
        return paises;
    }
}
