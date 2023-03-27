/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos.Administracion;

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
public class CTiposDeCarrocerias { //extends Inicio {

    int idCarroceria;
    String nombreCarroceria;
    int activoCarroceria;
    Inicio ini;

    public int getIdCarroceria() {
        return idCarroceria;
    }

    public void setIdCarroceria(int idCarroceria) {
        this.idCarroceria = idCarroceria;
    }

    public String getNombreCarroceria() {
        return nombreCarroceria;
    }

    public void setNombreCarroceria(String nombreCarroceria) {
        this.nombreCarroceria = nombreCarroceria;
    }

    public int getActivoCarroceria() {
        return activoCarroceria;
    }

    public void setActivoCarroceria(int activoCarroceria) {
        this.activoCarroceria = activoCarroceria;
    }

    public int getIdCarroceria(String nombreCarroceria) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
         sql = "SELECT idTipoCarroceria "
                 + "FROM tiposdecarrocerias  "
                 + "where "
                 + "nombreTipoCarroceria='" + nombreCarroceria + "' "
                 + "and activo=1;";
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
                Logger.getLogger(CTiposDeCarrocerias.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CTiposDeCarrocerias() {
    }

    public CTiposDeCarrocerias(Inicio ini) throws Exception {
        this.ini=ini;
    }

    public CTiposDeCarrocerias(Inicio ini, int idCarroceria) throws Exception {
        this.ini=ini;
        
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        this.idCarroceria = idCarroceria;
         sql = "SELECT * "
                 + "FROM tiposdecarrocerias "
                 + "where "
                 + "idTipoCarroceria=" + idCarroceria
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idCarroceria = rst.getInt(1);
                this.nombreCarroceria = rst.getString(2);
                this.activoCarroceria=rst.getInt(3);;
            } else {
                this.idCarroceria = idCarroceria;
                this.nombreCarroceria = null;
                this.activoCarroceria=0;
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposdecarrocerias consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeCarrocerias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CTiposDeCarrocerias(Inicio ini, String nombreCarroceria) throws Exception {
        this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT * "
                + "FROM tiposdecarrocerias "
                + "where "
                + "nombreTipoCarroceria='" + nombreCarroceria
                    + "' and activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idCarroceria = rst.getInt(1);
                this.nombreCarroceria = rst.getString(2);
                this.activoCarroceria=rst.getInt(3);;
            } else {
                this.idCarroceria = 0;
                this.nombreCarroceria = null;
                this.activoCarroceria=0;
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposdecarrocerias consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeCarrocerias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarCarrocerias() {
        boolean grabado = false;
        try {
           
            
            String sql = "INSERT INTO tiposdecarrocerias("
                    + "nombreTipoCarroceria,activo,usuario,flag) VALUES('"
                    + this.nombreCarroceria + "',"
                    + this.activoCarroceria + ",'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoCarroceria='" + this.nombreCarroceria + "',"
                    + "activo=" + this.activoCarroceria + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CTiposDeCarrocerias.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarCarrocerias(int caso) throws SQLException {
        boolean grabado = false;
       
        String sql = "UPDATE  tiposdecarrocerias SET "
                + "nombreTipoCarroceria='" + this.nombreCarroceria + "',"
                + "activo=" + this.activoCarroceria + ","
                + "flag=-1 WHERE "
                + "idTipoCarroceria=" + this.idCarroceria + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }


 
    public String arrListadoDeTiposDeCarrocerias() {
      
            String sql = "SELECT idTipoCarroceria, nombreTipoCarroceria, activo, fechaIng, usuario, flag  "
                    + "FROM tiposdecarrocerias;";
        
        return sql;
    }

  
}
