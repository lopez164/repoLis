/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author VLI_488
 */
public abstract class CConexiones {

    String cadena;
    Inicio ini;

    public CConexiones() {

    }

    public static Connection GetConnection(String cadena, String usuario, String clave) {
        Connection conexion = null;
        try {
//            if (!Inicio.verificarConexion()) {
//                JOptionPane.showMessageDialog(null, "Error en la conexion a Internet ", " Alerta", JOptionPane.ERROR_MESSAGE);
//                return null;
//            }
            conexion = DriverManager.getConnection(cadena, usuario, clave);

        } catch (SQLException ex) {
            Logger.getLogger(CConexiones.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error2 en la Conexión con la BD " + ex.getMessage(), "Error en la conexión ", JOptionPane.ERROR_MESSAGE);
            conexion = null;
        } finally {
            return conexion;
        }
    }

    public static Connection GetConnection(String cadena, String usuario, String clave, String claseOrigen) {
        Connection conexion = null;
        try {
//            if (!Inicio.verificarConexion()) { 
//                JOptionPane.showMessageDialog(null, "Error en la conexion a Internet en la clase " + claseOrigen   , " Alerta", JOptionPane.ERROR_MESSAGE);
//                return null;
//            }
            conexion = DriverManager.getConnection(cadena, usuario, clave);

        } catch (SQLException ex) {
           Logger.getLogger(CConexiones.class.getName()).log(Level.SEVERE, null, ex);

            JOptionPane.showMessageDialog(null, "Errorde Conexión con la BD en la clase  " + claseOrigen + "\n " + ex.getMessage(), "Error en la conexión ", JOptionPane.ERROR_MESSAGE);
            conexion = null;
        } finally {
            return conexion;
        }
    }

    public static boolean hayConexion(String cadena, String usuario, String clave) {
        boolean conx = true;
        Connection conexion = null;
        String sql = "";
        try {
//            if (!Inicio.verificarConexion()) {
//                JOptionPane.showMessageDialog(null, "Error en la conexion a Internet ", " Alerta", JOptionPane.ERROR_MESSAGE);
//                return false;
//            }

            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection(cadena, usuario, clave);
            conx = true;

        } catch (SQLException ex) {
                       Logger.getLogger(CConexiones.class.getName()).log(Level.SEVERE, null, ex);

             JOptionPane.showMessageDialog(null,  "Error 2 en la Conexión con servidor ","Error en la conexión ", JOptionPane.ERROR_MESSAGE);
            conexion = null;
            conx = false;
        } finally {
            return conx;
        }
    }

    public static Connection GetConnectionDeConfiguracion(String host, String usuario, String clave, String objeto) {
        Connection conexion = null;
       
        try {
//            if (!Inicio.verificarConexion()) { 
//                JOptionPane.showMessageDialog(null, "Error en la conexion a Internet en la clase " + claseOrigen   , " Alerta", JOptionPane.ERROR_MESSAGE);
//                return null;
//            }
            conexion = DriverManager.getConnection(host, usuario, clave);

        } catch (SQLException ex) {
           Logger.getLogger(CConexiones.class.getName()).log(Level.SEVERE, null, ex);

            //JOptionPane.showMessageDialog(null, "Errorde Conexión con la BD en la clase MisClientes \n " + ex.getMessage(), "Error en la conexión ", JOptionPane.ERROR_MESSAGE);
            conexion = null;
        } finally {
            return conexion;
        }
    }

}
