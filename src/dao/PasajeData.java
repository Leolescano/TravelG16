
package dao;

import static connect.Conexion.getConexion;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import models.*;
import static utils.ManejoRecursos.closeConnection;
import static utils.ManejoRecursos.closePreparedStatement;
import static utils.ManejoRecursos.closeResultSet;

public class PasajeData {

    public static void crearPasaje(Pasaje pasaje) {        
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conexion = getConexion();
            String sql = "INSERT INTO pasaje (tipoTransporte, importe, nombreCiudadOrigen, nombreCiudadDestino, estado)"
                            + "VALUES (?, ?, ?, ?, ?)";
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pasaje.getTipoTransporte().toString());
            ps.setDouble(2, pasaje.getImporte());
            ps.setString(3, pasaje.getCiudadOrigen().getNombre());
            ps.setString(4, pasaje.getCiudadDestino().getNombre());
            ps.setBoolean(5, pasaje.getEstado());
            ps.executeUpdate();
            rs =  ps.getGeneratedKeys();
            if (rs.next()) {
                    pasaje.setIdPasaje(rs.getInt(1));
                    JOptionPane.showMessageDialog (null, "Pasaje guardado");
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el pasaje.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }

    public static void desactivarPasaje(Integer idPasaje) {        
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = getConexion();
            String sql = "UPDATE pasaje SET estado = 0 WHERE idPasaje = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPasaje);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                JOptionPane.showMessageDialog (null, "Pasaje desactivado");
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al querer desactivar el pasaje.");
        } finally {
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }

    public static void modificarImporte(Double importe, Integer idPasaje) {        
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = getConexion();
            String sql = "UPDATE  pasaje  set importe = ? WHERE idPasaje = ?" ;
            ps = conexion.prepareStatement(sql);
            ps.setDouble(1, importe);
            ps.setInt(2, idPasaje);
            int exito = ps.executeUpdate();
            if(exito == 1) {
                JOptionPane.showMessageDialog(null, "Valor del pasaje fue modificado.");
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al modificar el valor del importe del pasaje.");
        } finally {
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }

    public static Pasaje buscarPasajePorId(Integer idPasaje) {       
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Pasaje pasaje = null;
        try {
            conexion = getConexion();
            String sql = "SELECT tipoTransporte, importe, nombreCiudadOrigen, nombreCiudadDestino FROM pasaje WHERE idPasaje = ? AND estado = 1";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPasaje);
            rs = ps.executeQuery();
            if(rs.next()){
                pasaje = new Pasaje();
                pasaje.setIdPasaje(idPasaje);
                pasaje.setTipoTransporte(TipoTransporte.valueOf(rs.getString("tipoTransporte")));
                pasaje.setImporte(rs.getDouble("importe"));

                Ciudad ciudadAux = new Ciudad();
                ciudadAux.setNombre(rs.getString("nombreCiudadOrigen"));
                pasaje.setCiudadOrigen(ciudadAux);

                Ciudad ciudadAux2 = new Ciudad();
                ciudadAux2.setNombre(rs.getString("nombreCiudadDestino"));
                pasaje.setCiudadDestino(ciudadAux2);

                pasaje.setEstado(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al buscar el pasaje por el id.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return pasaje;
    }

    public static Pasaje buscarPasajePorOrigen(String ciudadOrigen) {        
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Pasaje pasaje = null;
        try {
            conexion = getConexion();
            String sql = "SELECT idPasaje, tipoTransporte, importe, nombreCiudadOrigen, nombreCiudadDestino FROM pasaje WHERE nombreCiudadOrigen = ? AND estado = 1";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, ciudadOrigen);
            rs = ps.executeQuery();
            if(rs.next()){
                pasaje = new Pasaje();
                pasaje.setIdPasaje(rs.getInt("idPasaje"));
                pasaje.setTipoTransporte(TipoTransporte.valueOf(rs.getString("tipoTransporte")));
                pasaje.setImporte(rs.getDouble("importe"));

                Ciudad ciudadAux = CiudadData.buscarCiudadPorNombre(ciudadOrigen);
                pasaje.setCiudadOrigen(ciudadAux);

                ciudadAux = CiudadData.buscarCiudadPorNombre(rs.getString("nombreCiudadDestino"));
                pasaje.setCiudadDestino(ciudadAux);

                pasaje.setEstado(true);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al buscar el pasaje por la ciudad de origen.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return pasaje;
    }

    public static Set<Pasaje> listarPasajes() {        
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Set <Pasaje> pasajes = new HashSet<>();
        try {
            conexion = getConexion();
            String sql = "SELECT idPasaje, tipoTransporte, importe, nombreCiudadOrigen, nombreCiudadDestino FROM pasaje WHERE estado = 1";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Pasaje pasaje = new Pasaje();
                pasaje.setIdPasaje(rs.getInt("idPasaje"));
                pasaje.setTipoTransporte(TipoTransporte.valueOf(rs.getString("tipoTransporte")));
                pasaje.setImporte(rs.getDouble("importe"));

                Ciudad ciudadAux = new Ciudad();
                ciudadAux.setNombre(rs.getString("nombreCiudadOrigen"));
                pasaje.setCiudadOrigen(ciudadAux);

                Ciudad ciudadAux2 = new Ciudad();
                ciudadAux2.setNombre(rs.getString("nombreCiudadDestino"));
                pasaje.setCiudadDestino(ciudadAux2);

                pasaje.setEstado(true);
                pasajes.add(pasaje);
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al listar los pasajes.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return pasajes;
    }
}
