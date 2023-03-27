/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author VLI_488
 */
public abstract class CConexionenBurbuja {

Inicio ini;
    public  CConexionenBurbuja() {
   
    }


     public static Connection GetConnection(){
       Connection conexion=null;
       try
        {
             String cadena = "jdbc:mysql" + "://" + "localhost" + ":" + "3306" + "/" + "prueba1";
             String usuario="lopez164";
             String clave="drdndrslpzmjc";
           
             Class.forName("com.mysql.jdbc.Driver").newInstance();
             conexion = DriverManager.getConnection(cadena,usuario , clave);
        }
        catch(ClassNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error1 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error2 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error3 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        finally
        {
            return conexion;
        }
     }
}
