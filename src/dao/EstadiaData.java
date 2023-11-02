package dao;

import static connect.Conexion.getConexion;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import models.*;
import static utils.ManejoRecursos.closeConnection;
import static utils.ManejoRecursos.closePreparedStatement;
import static utils.ManejoRecursos.closeResultSet;

public class EstadiaData {

    public EstadiaData(){}

    public static void cargarEstadia(Estadia estadia) {
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conexion = getConexion();
            String sql = "INSERT INTO estadia (establecimiento, checkIn, checkOut, servicio, " +
                            "importeDiario, ciudadDestino, temporada, estado)"
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            Establecimiento establecimiento = EstablecimientoData.buscarEstablecimientoPorNombre(estadia.getEstablecimiento().getNombre());
            ps.setInt(1, establecimiento.getIdEstablecimiento());
            ps.setDate(2, java.sql.Date.valueOf(estadia.getCheckIn()));
            ps.setDate(3, java.sql.Date.valueOf(estadia.getCheckOut()));
            ps.setString(4, estadia.getServicio().toString());			
            ps.setDouble(5, estadia.getImporteDiario());
            Ciudad ciudad = CiudadData.buscarCiudadPorNombre(estadia.getCiudadDestino().getNombre());
            ps.setInt(6, ciudad.getIdCiudad());
            ps.setString(7, estadia.getTemporada().toString());
            ps.setBoolean(8, estadia.getEstado());
            ps.executeUpdate();
            rs =  ps.getGeneratedKeys();
            if (rs.next()) {
                estadia.setIdEstadia(rs.getInt(1));
                JOptionPane.showMessageDialog (null, "Estadia guardada.");
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al aguardar la estadia.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
    }

    public static Estadia buscarEstadiaPorId(int idEstadia) {
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Estadia estadia = null;
        try {
            conexion = getConexion();
            String sql = "SELECT * FROM estadia WHERE idEstadia = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idEstadia);
            rs = ps.executeQuery();
            if (rs.next()) {
                estadia = new Estadia();
                estadia.setIdEstadia(rs.getInt("idEstadia"));

                Establecimiento establecimiento = EstablecimientoData.buscarEstablecimientoPorId(rs.getInt("establecimiento"));
                estadia.setEstablecimiento(establecimiento);
                estadia.setCheckIn(rs.getDate("checkIn").toLocalDate());
                estadia.setCheckOut(rs.getDate("checkOut").toLocalDate());
                estadia.setServicio(Servicio.valueOf(rs.getString("servicio")));
                estadia.setImporteDiario(rs.getDouble("importeDiario"));
                Ciudad ciudad = CiudadData.buscarCiudadPorId(rs.getInt("ciudadDestino"));
                estadia.setTemporada(Temporada.valueOf(rs.getString("temporada")));
                estadia.setCiudadDestino(ciudad);
                estadia.setEstado(rs.getBoolean("estado"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar la estadia por el id.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return estadia;
    }

    public static Estadia buscarEstadiaPorEstablecimiento(Establecimiento establecimiento) {
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Estadia estadia = null;
        try {
            conexion = getConexion();
            String sql = "SELECT * FROM estadia WHERE establecimiento = ?";
            ps = conexion.prepareStatement(sql);
            System.out.println(establecimiento);
            ps.setInt(1, establecimiento.getIdEstablecimiento());
            rs = ps.executeQuery();
            if (rs.next()) {
                estadia = new Estadia();
                estadia.setIdEstadia(rs.getInt("idEstadia"));

                Establecimiento establecimientoAux = EstablecimientoData.buscarEstablecimientoPorId(rs.getInt("establecimiento"));

                estadia.setEstablecimiento(establecimientoAux);
                estadia.setCheckIn(rs.getDate("checkIn").toLocalDate());
                estadia.setCheckOut(rs.getDate("checkOut").toLocalDate());
                estadia.setServicio(Servicio.valueOf(rs.getString("servicio")));
                estadia.setImporteDiario(rs.getDouble("importeDiario"));
                Ciudad ciudad = CiudadData.buscarCiudadPorId(rs.getInt("ciudadDestino"));                              
                estadia.setCiudadDestino(ciudad);
                estadia.setTemporada(Temporada.valueOf(rs.getString("temporada")));
                estadia.setEstado(rs.getBoolean("estado"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar la estadia por el nombre del establecimiento.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return estadia;
    }

    public static Set<Estadia> listarEstadias() {
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Set <Estadia> estadias = new HashSet<>();
        try {
            conexion = getConexion();
            String sql = "SELECT idEstadia, establecimiento, ciudadDestino FROM estadia";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Estadia alojamiento = new Estadia();
                alojamiento.setIdEstadia(rs.getInt("idEstadia"));

                Establecimiento establecimiento = EstablecimientoData.buscarEstablecimientoPorId(rs.getInt("establecimiento"));

                alojamiento.setEstablecimiento(establecimiento);

                Ciudad ciudadAux = CiudadData.buscarCiudadPorId(rs.getInt("ciudadDestino"));
                alojamiento.setCiudadDestino(ciudadAux);

                estadias.add(alojamiento);
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog (null, "Error al listar las estadias.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return estadias;
    }
}