/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CEstadosCiviles {//extends Inicio {

    int idEstadoCivil;
    String nombreEstadoCivil;
    int activoEstadoCivil;
Inicio ini;
    
    public CEstadosCiviles() {
    }

    public CEstadosCiviles(Inicio ini) throws Exception {
        this.ini = ini;;

    }

    public CEstadosCiviles(Inicio ini, int idEstadoCivil) throws Exception {
        this.ini = ini;
          String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {
            
              con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();           
                sql = "SELECT * FROM `estadosciviles` where idEstadoCivil=" + idEstadoCivil
                    + " and activo=1;";
             rst = st.executeQuery(sql);
              if (rst.next()) {
                this.idEstadoCivil = rst.getInt(1);
                this.nombreEstadoCivil = rst.getString(2);
                this.activoEstadoCivil=rst.getInt(3);
            } else {
                this.idEstadoCivil = 0;
                this.nombreEstadoCivil = null;
            }
            rst.close();
            st.close();
            con.close();
             }
           
        } catch (SQLException ex) {
             System.out.println("Error en consultar estados civiless consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CEstadosCiviles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public CEstadosCiviles(Inicio ini, String nombreEstadoCivil) throws Exception {
       this.ini = ini;
         String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
                
             sql = "SELECT * FROM `estadosciviles` where nombreEstadoCivil='" + nombreEstadoCivil
                    + "' and activo=1;";
             rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idEstadoCivil = rst.getInt(1);
                this.nombreEstadoCivil = rst.getString(2);
                this.activoEstadoCivil=rst.getInt(3);
            } else {
                this.idEstadoCivil = 0;
                this.nombreEstadoCivil = null;
                 this.activoEstadoCivil=0;
            }
            rst.close();
            st.close();
            con.close();
             }
           
        } catch (SQLException ex) {
             System.out.println("Error en consultar estados civiles consulta sql " + ex.getMessage());
            Logger.getLogger(CEstadosCiviles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getIdEstadoCivil() {
        return idEstadoCivil;
    }

    public void setIdEstadiCivil(int idEstadoCivil) {
        this.idEstadoCivil = idEstadoCivil;
    }

    public String getNombreEstadoCivil() {
        return nombreEstadoCivil;
    }

    public void setNombreEStadoCivil(String nombreEstadoCivil) {
        this.nombreEstadoCivil = nombreEstadoCivil;
    }

    public int getActivoEstadoCivil() {
        return activoEstadoCivil;
    }

    public void setActivoEstadoCivil(int activoEstadoCivil) {
        this.activoEstadoCivil = activoEstadoCivil;
    }

    public int getIdEstadoCivil(String nombreEstadoCivil) {
        int id = 0;
          String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {
             con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
                
             sql = "SELECT idEstadoCivil FROM `estadosciviles` where nombreEstadoCivil='" + nombreEstadoCivil
                    + "' and activo=1;";
           rst = st.executeQuery(sql);
            if (rst.next()) {
                id = rst.getInt(1);
            } else {
                id = 0;
            }
            rst.close();
            st.close();
            con.close();
             }

           
        } catch (SQLException ex) {
            System.out.println("Error en consultar estados civiles consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CEstadosCiviles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public boolean grabarEstadosCiviles() throws SQLException {
        boolean grabado = false;
        try {
            
            String sql = "INSERT INTO `estadosciviles`(`idEstadoCivil`,"
                    + "`nombreEstadoCivil`,`activo`,`usuario`,) VALUES("
                    + this.idEstadoCivil + ",'"
                    + this.nombreEstadoCivil + "',"
                    + this.activoEstadoCivil + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',1"
                    + ") ON DUPLICATE KEY UPDATE"
                    + "nombreEstadoCivil=" + this.nombreEstadoCivil + "',"
                    + "activo=" + this.activoEstadoCivil + ","
                    + "flag=1";
            grabado = ini.insertarDatosLocalmente(sql);
            
        } catch (Exception ex) {
            Logger.getLogger(CEstadosCiviles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

 
   public String rstListadoDeEstadosCiviles() {
         
           String sql = "SELECT idEstadoCivil, nombreEstadoCivil, activo, fechaIng, usuario, flag "
                    + "FROM `estadosciviles` "
                    + "ORDER BY nombreEstadoCivil ASC;";
    
        return sql;
    }
 
}
