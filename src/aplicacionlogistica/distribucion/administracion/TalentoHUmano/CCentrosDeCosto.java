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
public class CCentrosDeCosto {//extends Inicio {

    int idCentroDeCosto;
    String nombreCentroDeCosto;
    int activoCentroDeCosto;
    Inicio ini;
    
    public CCentrosDeCosto() {
    }

    public CCentrosDeCosto( Inicio ini) throws Exception {
        this.ini=ini;

    }

    public CCentrosDeCosto( Inicio ini, int idCentroDeCosto) throws Exception {
        this.ini=ini;
          String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
            if (con != null) {
                st = con.createStatement();
                 sql = "SELECT * FROM centrosdecosto where idCentroDeCosto=" + idCentroDeCosto
                    + " and activo=1 "
                    + "and aliado='" + ini.getIdAliado() + "';";
                 
                 rst = st.executeQuery(sql);
                  
                if (rst.next()) {
                    this.idCentroDeCosto = rst.getInt("idCentroDeCosto");
                    this.nombreCentroDeCosto = rst.getString("nombreCentroDeCosto");
                    this.activoCentroDeCosto = rst.getInt("activo");
                   
                } else {
                    this.idCentroDeCosto = 0;
                   
                }
                
            rst.close();
            st.close();
            con.close();
            }

            
           
           
        } catch (SQLException ex) {
            Logger.getLogger(CCentrosDeCosto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CCentrosDeCosto( Inicio ini, String nombreCentroDeCosto) throws Exception {
        this.ini=ini;
          String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
                sql = "SELECT * FROM centrosdecosto where nombreCentroDeCosto='" + nombreCentroDeCosto
                    + "' and activo=1 "
                    + "and aliado='" + ini.getIdAliado() + "';";
                
                 rst = st.executeQuery(sql);
                  if (rst.next()) {
               this.idCentroDeCosto = rst.getInt("idCentroDeCosto");
                    this.nombreCentroDeCosto = rst.getString("nombreCentroDeCosto");
                    this.activoCentroDeCosto = rst.getInt("activo");
                   
            } else {
                this.idCentroDeCosto = 0;
                    this.nombreCentroDeCosto = null;
                    this.activoCentroDeCosto = 0;
                 
            }
            rst.close();
            st.close();
            con.close();
             }
             
            
           
        } catch (SQLException ex) {
            Logger.getLogger(CCentrosDeCosto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int getIdCentroDeCosto() {
        return idCentroDeCosto;
    }

    public void setIdCentroDeCosto(int idCentroDeCosto) {
        this.idCentroDeCosto = idCentroDeCosto;
    }

    public String getNombreCentroDeCosto() {
        return nombreCentroDeCosto;
    }

    public void setNombreCentroDeCosto(String nombreCentroDeCosto) {
        this.nombreCentroDeCosto = nombreCentroDeCosto;
    }

    public int getactivoCentroDeCosto() {
        return activoCentroDeCosto;
    }

    public void setactivoCentroDeCosto(int activoCentroDeCosto) {
        this.activoCentroDeCosto = activoCentroDeCosto;
    }


    public int getIdCentroDeCosto(String nombreCentroDeCosto) {
        int id = 0;
         String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

              con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
                sql = "SELECT idCentroDeCosto FROM centrosdecosto where nombreCentroDeCosto='" + nombreCentroDeCosto
                    + "' and activo=1 "
                       + "and aliado='" + ini.getIdAliado() + "';";
                  rst = st.executeQuery(sql);
                 if (rst.next()) {
                 this.idCentroDeCosto = rst.getInt("idCentroDeCosto");
            } else {
                id = 0;
            }
            rst.close();
            st.close();
            con.close();
             }
             
           
           
        } catch (SQLException ex) {
            Logger.getLogger(CCentrosDeCosto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public boolean grabarCentrosDeCosto() throws SQLException {
        boolean grabado = false;
        try {
            
            String sql = "INSERT INTO centrosdecosto(idCentroDeCosto,"
                    + "nombreCentroDeCosto,activo,usuario) VALUES("
                    + this.idCentroDeCosto + ",'"
                    + this.nombreCentroDeCosto + "',"
                    + this.activoCentroDeCosto + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "nombreCentroDeCosto='" + this.nombreCentroDeCosto + "',"
                    + "activo=" + this.activoCentroDeCosto + ","
                    + "flag=1";
            grabado = ini.insertarDatosRemotamente(sql);
            
        } catch (Exception ex) {
            Logger.getLogger(CCentrosDeCosto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
     public boolean actualizarCentrosDeCosto() throws SQLException {
        boolean grabado = false;
        
        String sql = "UPDATE  centrosdecosto SET "
                + "nombreCentroDeCosto='" + this.nombreCentroDeCosto + "',"
                + "activo=" + this.activoCentroDeCosto + ","
                + "flag=-1 WHERE "
                + "idCentroDeCosto=" + this.idCentroDeCosto + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }

    public String arrListadoDeCentrosDeCosto() {

        // idCentroDeCosto, nombreCentroDeCosto, activo, fechaIng, usuario, flag
        String sql = "SELECT idCentroDeCosto, nombreCentroDeCosto, activo, fechaIng, usuario, flag "
                + "FROM centrosdecosto  "
                + "ORDER BY nombreCentroDeCosto ASC";
        return sql;
    }
  
    
   
}
