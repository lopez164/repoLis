/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.objetos;

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
public class CtiposGestiones { //extends Inicio {

   int idtiposDeGestion;
   String nombreTipoDeGestion;
   int activo;
   String usuario;
   
   Inicio ini;

    public int getIdtiposDeGestion() {
        return idtiposDeGestion;
    }

    public void setIdtiposDeGestion(int idtiposDeGestion) {
        this.idtiposDeGestion = idtiposDeGestion;
    }

    public String getNombreTipoDeGestion() {
        return nombreTipoDeGestion;
    }

    public void setNombreTipoDeGestion(String nombreTipoDeGestion) {
        this.nombreTipoDeGestion = nombreTipoDeGestion;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

   

   

    
    public int getIdCanal(String nombreTipoDeGestion) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
  
        sql = "SELECT idtiposDeGestion "
                + "FROM `tiposDeGestion` "
                + "where nombreTipoDeGestion='" + nombreTipoDeGestion + "' and "
                + " activo=1;";
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
                Logger.getLogger(CtiposGestiones.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CtiposGestiones() {
    }

    public CtiposGestiones(Inicio ini) throws Exception {
    this.ini=ini;
    }

    
            
    
     public CtiposGestiones(Inicio ini, String nombreTipoDeGestion) throws Exception {
       this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
  
        sql =  sql = "SELECT idtiposDeGestion "
                + "FROM `tiposDeGestion` "
                + "where nombreTipoDeGestion='" + nombreTipoDeGestion + "' and "
                + " activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idtiposDeGestion = rst.getInt("idtiposDeGestion");
                this.nombreTipoDeGestion = rst.getString("nombreTipoDeGestion");
                this.activo=rst.getInt("activo");
               
            } else {
                this.idtiposDeGestion = 0;
                this.nombreTipoDeGestion = null;
                this.activo=0;
                
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposCanalDeVentas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CtiposGestiones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarTiposDeGestion(int caso) {
        boolean grabado = false;
        try {
           
            
            String sql = "INSERT INTO `tiposDeGestion`("
                    + "`nombreTipoDeGestion`,`activo`,`usuario`,`flag`) VALUES('"
                    + this.nombreTipoDeGestion + "',"
                    +  "1,'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',-1) "
                   
                    + " ON DUPLICATE KEY UPDATE "
                    + "nombreCanal='" + this.nombreTipoDeGestion + "',"
                    + "activo= 1,"
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);
            if (grabado) {
                Connection con;
                Statement st;
                ResultSet rst;
                
                con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
                
                sql = "SELECT * "
                        + "FROM `tiposDeGestion` "
                        + "where "
                        + "nombreTipoDeGestion='" + this.nombreTipoDeGestion + "' and "
                        + " activo=1;";
                if (con != null) {
                    try {
                        st = con.createStatement();
                        rst = st.executeQuery(sql);
                        if (rst.next()) {
                            this.idtiposDeGestion = rst.getInt("idtiposDeGestion");
                            this.nombreTipoDeGestion = rst.getString("nombreTipoDeGestion");
                            this.activo = rst.getInt("activo");
                           
                        } else {
                            this.idtiposDeGestion = 0;
                            this.nombreTipoDeGestion = null;
                            this.activo = 0;
                           
                        }
                        rst.close();
                        st.close();
                        con.close();
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(CtiposGestiones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            
        } catch (Exception ex) {
            Logger.getLogger(CtiposGestiones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarTiposDeGestion(int caso) throws SQLException {
        boolean grabado = false;
       
        String sql = "UPDATE  `tiposDeGestion` SET "
                + "nombreTipoDeGestion='" + this.nombreTipoDeGestion + "',"
                + "activo=" + this.activo + ","
                + "flag=-1 WHERE "
                + "idtiposDeGestion='" + this.idtiposDeGestion + "'; " ;
        
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }


   
     public String arrListadoDeTiposDeGestion(){

        // idcausalesDeRechazo, nombreCausalDeRechazo, activo, fechaIng, usuario, flag
        String sql = "SELECT idtiposDeGestion, nombreTipoDeGestion, activo, fechaIng, usuario, flag "
                + "FROM tiposDeGestion  "
                + "WHERE "
                + "tiposDeGestion.activo=1;  ";
        return sql;
    }

}
