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
public class CNivelesDeAcceso {//extends Inicio {

    int idNivelDeAcceso;
    String nombreNivelDeAcceso;
    int activoNivelDeAcceso;
    Inicio ini;

    public int getIdNivelDeAcceso() {
        return idNivelDeAcceso;
    }

    public void setIdNivelDeAcceso(int idNivelDeAcceso) {
        this.idNivelDeAcceso = idNivelDeAcceso;
    }

    public String getNombreNivelDeAcceso() {
        return nombreNivelDeAcceso;
    }

    public void setNombreNivelDeAcceso(String nombreNivelDeAcceso) {
        this.nombreNivelDeAcceso = nombreNivelDeAcceso;
    }

    public int getActivoNivelDeAcceso() {
        return activoNivelDeAcceso;
    }

    public void setActivoNivelDeAcceso(int activoNivelDeAcceso) {
        this.activoNivelDeAcceso = activoNivelDeAcceso;
    }

    public int getIdNivelDeAcceso(String nombreNivelDeAcceso) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
         sql = "SELECT idNivelDeAcceso FROM nivelesdeacceso where nombreNivelDeAcceso='" + nombreNivelDeAcceso + "' and activo=1;";
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
                Logger.getLogger(CNivelesDeAcceso.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CNivelesDeAcceso() {
    }

    public CNivelesDeAcceso(Inicio ini) throws Exception {
       this.ini=ini;

    }

    public CNivelesDeAcceso(Inicio ini, int idNivelDeAcceso) throws Exception {
       this.ini=ini;
        
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        this.idNivelDeAcceso = idNivelDeAcceso;
         sql = "SELECT * FROM nivelesdeacceso where idNivelDeAcceso=" + idNivelDeAcceso
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idNivelDeAcceso = rst.getInt(1);
                this.nombreNivelDeAcceso = rst.getString(2);
                this.activoNivelDeAcceso=rst.getInt(3);;
            } else {
                this.idNivelDeAcceso = idNivelDeAcceso;
                this.nombreNivelDeAcceso = null;
                this.activoNivelDeAcceso=0;
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar cargos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CNivelesDeAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CNivelesDeAcceso(Inicio ini, String nombreNivelDeAcceso) throws Exception {
       this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        sql = "SELECT * FROM nivelesdeacceso where nombreNivelDeAcceso='" + nombreNivelDeAcceso
                    + "' and activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idNivelDeAcceso = rst.getInt(1);
                this.nombreNivelDeAcceso = rst.getString(2);
                this.activoNivelDeAcceso=rst.getInt(3);;
            } else {
                this.idNivelDeAcceso = 0;
                this.nombreNivelDeAcceso = null;
                this.activoNivelDeAcceso=0;
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar cargos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CNivelesDeAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarNivelDeAcceso() throws SQLException {
        boolean grabado = false;
        String sql = "INSERT INTO nivelesdeacceso(idNivelDeAcceso,"
                + "nombreNivelDeAcceso,activo,usuario,) VALUES("
                + this.idNivelDeAcceso + ",'"
                + this.nombreNivelDeAcceso + "',"
                + this.activoNivelDeAcceso + ",'"
                + ini.getUser().getNombreUsuario() + "',1"
                + ") ON DUPLICATE KEY UPDATE"
                + "nombreNivelDeAcceso='" + this.nombreNivelDeAcceso + "',"
                + "activo=" + this.activoNivelDeAcceso + ","
                + "flag=1";
        grabado = ini.insertarDatosLocalmente(sql);
        return grabado;
    }

    public ArrayList<IdNivelesDeAcceso> listadoDeNivelesDeAcceso() {
        ArrayList<IdNivelesDeAcceso> lista = new ArrayList();
        try {

        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
       sql = "select idNivelDeAcceso from nivelesdeacceso where activo=1 ";
         
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
             while (rst.next()) {
                IdNivelesDeAcceso cargo = new IdNivelesDeAcceso(rst.getInt(1));
                lista.add(cargo);
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
           
            
        } catch (SQLException ex) {
            System.out.println("Error en consultar cargos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CNivelesDeAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
 public String rstListadoDeNivelesDeAcceso() {
      
            String sql = "SELECT idNivelDeAcceso, nombreNivelDeAcceso, activo, fechaIng, usuario, flag   "
                    + "FROM nivelesdeacceso ";
        
        return sql;
    }
    public static class IdNivelesDeAcceso {

        int nivelDeAcceso;

        public int getNivelesDeAcceso() {
            return nivelDeAcceso;
        }

        public IdNivelesDeAcceso(int nivelDeAcceso) {
            this.nivelDeAcceso = nivelDeAcceso;
        }
    }
}
