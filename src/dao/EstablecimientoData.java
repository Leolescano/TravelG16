

package dao;

import static connect.Conexion.getConexion;
import java.sql.*;
import javax.swing.*;
import models.*;
import static utils.ManejoRecursos.closeConnection;
import static utils.ManejoRecursos.closePreparedStatement;
import static utils.ManejoRecursos.closeResultSet;

public class EstablecimientoData {


    public static void cargarEstablecimiento(Establecimiento establecimiento) {
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try{
            conexion = getConexion();
            String sql = "INSERT INTO establecimiento (nombre, direccion, telefono) VALUES (?, ?, ?)";
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, establecimiento.getNombre());
            ps.setString(2, establecimiento.getDireccion());
            ps.setString(3, establecimiento.getTelefono());
            ps.executeUpdate();
            rs =  ps.getGeneratedKeys();
            if (rs.next()) {
                establecimiento.setIdEstablecimiento(rs.getInt(1));
                JOptionPane.showMessageDialog (null, "Establecimiento guardado");
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Eror al guardar el establecimiento.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }

    public static Establecimiento buscarEstablecimientoPorId(int id) {        
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Establecimiento establecimiento = null;
        try {
            conexion = getConexion();
            String sql = "SELECT * FROM establecimiento WHERE idEstablecimiento = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                establecimiento = new Establecimiento();
                establecimiento.setIdEstablecimiento(rs.getInt("idEstablecimiento"));
                establecimiento.setNombre(rs.getString("nombre"));
                establecimiento.setDireccion(rs.getString("direccion"));
                establecimiento.setTelefono(rs.getString("telefono"));
            }
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog (null, "Error al buscar el establecimiento por el id.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return establecimiento;
    }
 
    public static Establecimiento buscarEstablecimientoPorNombre(String nombreBuscado) {        
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Establecimiento establecimiento = null;
        try {
            conexion = getConexion();
            String sql = "SELECT * FROM establecimiento WHERE nombre = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreBuscado);
            rs = ps.executeQuery();
            if (rs.next()) {
                establecimiento = new Establecimiento();
                establecimiento.setIdEstablecimiento(rs.getInt("idEstablecimiento"));
                establecimiento.setNombre(rs.getString("nombre"));
                establecimiento.setDireccion(rs.getString("direccion"));
                establecimiento.setTelefono(rs.getString("telefono"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al buscar el establecimiento por el nombre.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return establecimiento;
    }
   
    public static void eliminar(int idEstablecimiento) {      
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = getConexion();
            String sql = "DELETE FROM establecimiento WHERE idEstablecimiento = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idEstablecimiento);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Establecimiento eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Establecimiento no encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar establecimiento.");           
        } finally {
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }   
}