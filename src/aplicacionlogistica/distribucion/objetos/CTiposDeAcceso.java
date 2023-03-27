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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CTiposDeAcceso {//extends Inicio {

    int idTipoDeAcceso;
    String nombreTipoDeAcceso;
    int activoTipoDeAcceso;
    Inicio ini;

    public int getIdTipoDeAcceso() {
        return idTipoDeAcceso;
    }

    public void setIdTipoDeAcceso(int idTipoDeAcceso) {
        this.idTipoDeAcceso = idTipoDeAcceso;
    }

    public String getNombreTipoDeAcceso() {
        return nombreTipoDeAcceso;
    }

    public void setNombreTipoDeAcceso(String nombreTipoDeAcceso) {
        this.nombreTipoDeAcceso = nombreTipoDeAcceso;
    }

    public int getActivoTipoDeAcceso() {
        return activoTipoDeAcceso;
    }

    public void setActivoTipoDeAcceso(int activoTipoDeAcceso) {
        this.activoTipoDeAcceso = activoTipoDeAcceso;
    }

    public int getIdTipoDeAcceso(String nombreTipoAcceso) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
         sql = "SELECT idTipoDeAcceso FROM `tiposdeacceso` where nombreTipoDeAcceso='" + nombreTipoAcceso + "' and activo=1;";
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
                Logger.getLogger(CTiposDeAcceso.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CTiposDeAcceso() {
    }

    public CTiposDeAcceso(Inicio ini) throws Exception {
       this.ini=ini;

    }

    public CTiposDeAcceso(Inicio ini, int idCargo) throws Exception {
       this.ini=ini;
        
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        this.idTipoDeAcceso = idCargo;
         sql = "SELECT * FROM `tiposdeacceso` where idTipoDeAcceso=" + idCargo
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idTipoDeAcceso = rst.getInt(1);
                this.nombreTipoDeAcceso = rst.getString(2);
                this.activoTipoDeAcceso=rst.getInt(3);;
            } else {
                this.idTipoDeAcceso = idCargo;
                this.nombreTipoDeAcceso = null;
                this.activoTipoDeAcceso=0;
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar cargos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CTiposDeAcceso(Inicio ini, String nombreCargo) throws Exception {
        this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        sql = "SELECT * FROM `tiposdeacceso` where nombreTipoDeAcceso='" + nombreCargo
                    + "' and activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idTipoDeAcceso = rst.getInt(1);
                this.nombreTipoDeAcceso = rst.getString(2);
                this.activoTipoDeAcceso=rst.getInt(3);;
            } else {
                this.idTipoDeAcceso = 0;
                this.nombreTipoDeAcceso = null;
                this.activoTipoDeAcceso=0;
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar cargos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarTipoDeAcceso() throws SQLException {
        boolean grabado = false;
        try {
            
            String sql = "INSERT INTO `tiposdeacceso`(`idTipoDeAcceso`,"
                    + "`nombreTipoDeAcceso`,`activo`,`usuario`,) VALUES("
                    + this.idTipoDeAcceso + ",'"
                    + this.nombreTipoDeAcceso + "',"
                    + this.activoTipoDeAcceso + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',1"
                    + ") ON DUPLICATE KEY UPDATE"
                    + "nombreTipoDeAcceso='" + this.nombreTipoDeAcceso + "',"
                    + "activo=" + this.activoTipoDeAcceso + ","
                    + "flag=1";
            grabado = ini.insertarDatosLocalmente(sql);
           
        } catch (Exception ex) {
            Logger.getLogger(CTiposDeAcceso.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return grabado;
    }

    public ArrayList<IdTiposDeAcceso> listadoDeTiposDeAcceso() {
        ArrayList<IdTiposDeAcceso> lista = new ArrayList();
        try {

            Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
       sql = "select idTipoDeAcceso from tiposdeacceso where activo=1 ";
         
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
             while (rst.next()) {
                IdTiposDeAcceso cargo = new IdTiposDeAcceso(rst.getInt(1));
                lista.add(cargo);
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
           
            
        } catch (SQLException ex) {
            System.out.println("Error en consultar cargos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
     public String rstListadoDeTiposDeAcceso() {
      
            String sql = "SELECT idTipoDeAcceso, nombreTipoDeAcceso, activo, fechaIng, usuario, flag  "
                    + "FROM `tiposdeacceso` ";
        
        return sql;
    }

    public static class IdTiposDeAcceso {

        int tiposDeAcceso;

        public int getTiposDeAcceso() {
            return tiposDeAcceso;
        }

        public IdTiposDeAcceso(int tiposDeAcceso) {
            this.tiposDeAcceso = tiposDeAcceso;
        }
    }
}
