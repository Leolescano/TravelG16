package dao;

import static connect.Conexion.getConexion;
import java.sql.*;
import java.time.temporal.*;
import java.util.*;
import javax.swing.*;
import models.*;
import static utils.ManejoRecursos.closeConnection;
import static utils.ManejoRecursos.closePreparedStatement;
import static utils.ManejoRecursos.closeResultSet;

public class PaqueteData {

    public static void crearPaquete(PaqueteTuristico paquete) {        
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        if (paquete.getEstadia() != null) {
            try {
                conexion = getConexion();
                String sql = "INSERT INTO paquete (origen, destino, estadia, pasaje, " +
                                "valorTotal, estado)"
                                + "VALUES (?, ?, ?, ?, ?, ?)";
                ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                Ciudad ciudadAux = CiudadData.buscarCiudadPorNombre(paquete.getCiudadOrigen().getNombre());
//                ps.setInt(1, ciudadAux.getIdCiudad());
                
                ps.setInt(1, paquete.getCiudadOrigen().getIdCiudad());

                ciudadAux = CiudadData.buscarCiudadPorNombre(paquete.getCiudadDestino().getNombre());
//                ps.setInt(2, ciudadAux.getIdCiudad());
                
                ps.setInt(2, paquete.getCiudadDestino().getIdCiudad());

                Estadia estadiaAux = EstadiaData.buscarEstadiaPorId(paquete.getEstadia().getIdEstadia());
//                ps.setInt(3, alojamientoAux.getIdAlojamiento());
                
                ps.setInt(3, paquete.getEstadia().getIdEstadia());
                
                long cantidadDias = ChronoUnit.DAYS.between(estadiaAux.getCheckIn(), estadiaAux.getCheckOut());

                Pasaje pasajeAux = PasajeData.buscarPasajePorOrigen(paquete.getPasaje().getCiudadOrigen().getNombre());
//                ps.setInt(4, pasajeAux.getIdPasaje());
                
                ps.setInt(4, paquete.getPasaje().getIdPasaje());
   
                double valorTotal = pasajeAux.getImporte() + (estadiaAux.getImporteDiario() * estadiaAux.getServicio().getPorcentaje() * cantidadDias * estadiaAux.getTemporada().getPorcentaje());
                System.out.println("Calculo en PaqueteData: ");
                System.out.println(pasajeAux.getImporte() + " + ( " + estadiaAux.getImporteDiario() + " * " + estadiaAux.getServicio().getPorcentaje() + " * " + cantidadDias + " * " + estadiaAux.getTemporada().getPorcentaje() + ") = " + valorTotal);
                ps.setDouble(5, paquete.getValorTotal());
              
                ps.setBoolean(6, paquete.getEstado());
                ps.executeUpdate();
                rs =  ps.getGeneratedKeys();
                if (rs.next()) {
                    paquete.setIdPaquete(rs.getInt(1));
                    JOptionPane.showMessageDialog (null, "Paquete creado.");
                }
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al crear  el paquete.");
            } finally {
                closeResultSet(rs);
                closePreparedStatement(ps);
                closeConnection(conexion);
            }
        }
    }
    
    public static TreeSet<PaqueteTuristico> buscarPaquetesDinamico(PaqueteTuristico paquete) {       
        Connection conexion = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        TreeSet<PaqueteTuristico> paquetes = new java.util.TreeSet<>();
        try {
            conexion = getConexion();
            StringBuilder sql = new StringBuilder("SELECT * FROM paquete WHERE 1=1");
            if (paquete.getCiudadOrigen() != null) {
                    sql.append(" AND origen = ?");
            }
            if (paquete.getCiudadDestino() != null) {
                    sql.append(" AND destino = ?");
            }
            if (paquete.getEstadia() != null) {
                    sql.append(" AND estadia = ?");
            }
            if (paquete.getPasaje() != null) {
                    sql.append(" AND pasaje = ?");
            }

            if (paquete.getValorTotal() != null && paquete.getValorTotal() > 0) {
                    sql.append(" AND valorTotal = ?");
            }
            ps = conexion.prepareStatement(sql.toString());
            int index = 1;
            if (paquete.getCiudadOrigen() != null) {
                    Ciudad ciudadAux = CiudadData.buscarCiudadPorNombre(paquete.getCiudadOrigen().getNombre());
                    ps.setInt(index++, ciudadAux.getIdCiudad());
            }
            if (paquete.getCiudadDestino() != null) {
                    Ciudad ciudadAux = CiudadData.buscarCiudadPorNombre(paquete.getCiudadDestino().getNombre());
                    ps.setInt(index++, ciudadAux.getIdCiudad());
            }
            if (paquete.getEstadia() != null) {
                    Estadia estadiaAux = EstadiaData.buscarEstadiaPorId(paquete.getEstadia().getIdEstadia());
                    ps.setInt(index++, estadiaAux.getIdEstadia());
            }
            if (paquete.getPasaje() != null) {
                    Pasaje pasajeAux = PasajeData.buscarPasajePorOrigen(paquete.getPasaje().getCiudadOrigen().getNombre());
                    ps.setInt(index++, pasajeAux.getIdPasaje());
            }

            if (paquete.getValorTotal() != null) {
                    ps.setDouble(index++, paquete.getValorTotal());
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                int idPaquete = rs.getInt("idPaquete");
                int idCiudadOrigen = rs.getInt("origen");
                int idCiudadDestino = rs.getInt("destino");
                int idEstadia = rs.getInt("estadia");
                int idPasaje = rs.getInt("pasaje");

                double valorTotal = rs.getDouble("valorTotal");
                boolean estado = rs.getBoolean("estado");

                Ciudad ciudadOrigen = CiudadData.buscarCiudadPorId(idCiudadOrigen);
                Ciudad ciudadDestino = CiudadData.buscarCiudadPorId(idCiudadDestino);
                Estadia estadia = EstadiaData.buscarEstadiaPorId(idEstadia);
                Pasaje pasaje = PasajeData.buscarPasajePorId(idPasaje);

                PaqueteTuristico paqueteEncontrado = new PaqueteTuristico();
                paqueteEncontrado.setIdPaquete(idPaquete);
                paqueteEncontrado.setCiudadOrigen(ciudadOrigen);
                paqueteEncontrado.setCiudadDestino(ciudadDestino);
                paqueteEncontrado.setEstadia(estadia);
                paqueteEncontrado.setPasaje(pasaje);

                paqueteEncontrado.setValorTotal(valorTotal);
                paqueteEncontrado.setEstado(estado);

                paquetes.add(paqueteEncontrado);
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el paquetes.");
        } finally {
            closeResultSet(rs);
            closePreparedStatement(ps);
            closeConnection(conexion);
        }
        return paquetes;
    }
}
