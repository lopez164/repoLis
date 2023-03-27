/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.administracion.TalentoHUmano;

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
public class CCargos { //extends Inicio {

    int idCargo;
    String nombreCargo;
    int activoCargo;
   
    String usuario;
    Inicio ini;

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public int getActivoCargo() {
        return activoCargo;
    }

    public void setActivoCargo(int activoCargo) {
        this.activoCargo = activoCargo;
    }

    public int getIdCargo(String nombreCargo) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
       con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
         sql = "SELECT idCargo FROM cargos where nombreCargo='" + nombreCargo + "' and activo=1;";
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                     id = rst.getInt(1);
                } else {
                    id = 0;
                }
                 rst.close();
                 st.close();
                 con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CCargos.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CCargos() {
    }

    public CCargos(Inicio ini) throws Exception {
    this.ini=ini;
    }

    public CCargos(Inicio ini, int idCargo) throws Exception {
     
         this.ini=ini;
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
        this.idCargo = idCargo;
         sql = "SELECT * FROM cargos where idCArgo=" + idCargo
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idCargo = rst.getInt(1);
                this.nombreCargo = rst.getString(2);
                this.activoCargo=rst.getInt(3);;
            } else {
                this.idCargo = idCargo;
                this.nombreCargo = null;
                this.activoCargo=0;
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar cargos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CCargos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CCargos(Inicio ini, String nombreCargo) throws Exception {
       this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
        sql = "SELECT * FROM cargos where nombreCargo='" + nombreCargo
                    + "' and activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idCargo = rst.getInt(1);
                this.nombreCargo = rst.getString(2);
                this.activoCargo=rst.getInt(3);;
            } else {
                this.idCargo = 0;
                this.nombreCargo = null;
                this.activoCargo=0;
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar cargos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CCargos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarCargos() {
        boolean grabado = false;
        try {
           
          
            String sql = "INSERT INTO cargos("
                    + "nombreCargo,activo,usuario,flag) VALUES('"
                    + this.nombreCargo + "',"
                    + this.activoCargo + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreCargo='" + this.nombreCargo + "',"
                    + "activo=" + this.activoCargo + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);
           
            
        } catch (Exception ex) {
            Logger.getLogger(CCargos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarCargos() throws SQLException {
        boolean grabado = false;
       
        String sql = "UPDATE  cargos SET "
                + "nombreCargo='" + this.nombreCargo + "',"
                + "activo=" + this.activoCargo + ","
                + "flag=-1 WHERE "
                + "idCargo=" + this.idCargo + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }


     public String arrListadoDeCargos(){
          //idCargo, nombreCargo, activo, fechaIng, usuario, flag
         String sql= "SELECT idCargo, nombreCargo, activo, fechaIng, usuario, flag "
                 + "FROM cargos "
                 + "order by nombreCargo asc;";
         return sql;
   }

   
}
