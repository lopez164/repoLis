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
public class CTiposDeServicio {//extends Inicio {

    int idTipoDeServicio;
    String nombreTipoDeServicio;
    int activoTipoDeServicio;
    Inicio ini;

    public int getIdTipoDeServicio() {
        return idTipoDeServicio;
    }

    public void setIdTipoDeServicio(int idTipoDeServicio) {
        this.idTipoDeServicio = idTipoDeServicio;
    }

    public String getNombreTipoDeServicio() {
        return nombreTipoDeServicio;
    }

    public void setNombreTipoDeServicio(String nombreTipoDeServicio) {
        this.nombreTipoDeServicio = nombreTipoDeServicio;
    }

    public int getActivoTipoDeServicio() {
        return activoTipoDeServicio;
    }

    public void setActivoTipoDeServicio(int activoTipoDeServicio) {
        this.activoTipoDeServicio = activoTipoDeServicio;
    }

    public int getIdTipoDeServicio(String nombreTipoDeServicio) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
         sql = "SELECT idtiposDeServicio "
                 + "FROM tiposdeservicio "
                 + "where nombreTipoServicio='" + nombreTipoDeServicio + "' and "
                 + "activo=1;";
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
                Logger.getLogger(CTiposDeServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CTiposDeServicio() {
    }

    public CTiposDeServicio(Inicio ini) throws Exception {
        this.ini=ini;
    }

    public CTiposDeServicio(Inicio ini, int idTipoDeServicio) throws Exception {
        this.ini=ini;
        
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        this.idTipoDeServicio = idTipoDeServicio;
         sql = "SELECT * "
                 + "FROM tiposdeservicio "
                 + "where "
                 + "idtiposDeServicio=" + idTipoDeServicio
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idTipoDeServicio = rst.getInt(1);
                this.nombreTipoDeServicio = rst.getString(2);
                this.activoTipoDeServicio=rst.getInt(3);;
            } else {
                this.idTipoDeServicio = idTipoDeServicio;
               
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar servicios consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CTiposDeServicio(Inicio ini, String nombreTipoDeServicio) throws Exception {
        this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT * "
                + "FROM tiposdeservicio "
                + "where "
                + "nombreTipoServicio='" + nombreTipoDeServicio
                    + "' and activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idTipoDeServicio = rst.getInt(1);
                this.nombreTipoDeServicio = rst.getString(2);
                this.activoTipoDeServicio=rst.getInt(3);;
            } else {
                this.idTipoDeServicio = 0;
               
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar servicios consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarTipoDeServicios() {
        boolean grabado = false;
        try {
           
                    String sql = "INSERT INTO tiposdeservicio("
                    + "nombreTipoServicio,activo,usuario,flag) VALUES('"
                    + this.nombreTipoDeServicio + "',"
                    + this.activoTipoDeServicio + ",'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoServicio='" + this.nombreTipoDeServicio + "',"
                    + "activo=" + this.activoTipoDeServicio + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);
          
            
        } catch (Exception ex) {
            Logger.getLogger(CTiposDeServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarTipoDeServicios() throws SQLException {
        boolean grabado = false;
        
        String sql = "UPDATE  tiposdeservicio SET "
                + "nombreTipoServicio='" + this.nombreTipoDeServicio + "',"
                + "activo=" + this.activoTipoDeServicio + ","
                + "flag=-1 WHERE "
                + "idtiposDeServicio=" + this.idTipoDeServicio + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }


 
 public String arrListadoDeServicios() {
      
            String sql = "SELECT idtiposDeServicio, nombreTipoServicio, activo, fechaIng, usuario, flag  "
                    + "FROM tiposdeservicio;";
        
        return sql;
    }
    
   
}
