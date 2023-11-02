package connect;

import java.sql.*;
import javax.swing.*;

public class Conexion {
    
    private static final String URL = "jdbc:mariadb://localhost/";
    private static final String DB = "paqueteturistico";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    private Conexion() {}

    public static Connection getConexion(){
            Connection conexion = null;
            try {
                    conexion = DriverManager.getConnection(URL+DB, USUARIO, PASSWORD);			
            } catch(SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectarse a la DB " + ex.getMessage());
            }
            return conexion;
    }
}
