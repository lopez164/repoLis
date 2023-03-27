/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.ingresoDeRegistros.objetos;

import aplicacionlogistica.distribucion.administracion.TalentoHUmano.*;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtto.documentos.Threads.HiloListadoDeCuentasSecundarias;

/**
 *
 * @author VLI_488
 */
public class CCuentasPrincipalesLogistica { //extends Inicio {

    int idCuentasPpal;
    String codigoCuenta;
    String nombreCuentasPpal;
    int activoCuentasPpal;
    String usuario;
    Inicio ini;
    List<CCuentaSecundariaLogistica> listaDeCuentasSecundarias;
    
    
    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    public void setCodigoCuenta(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public int getActivoCuentasPpal() {
        return activoCuentasPpal;
    }

    public void setActivoCuentasPpal(int activoCuentasPpal) {
        this.activoCuentasPpal = activoCuentasPpal;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    
    public int getIdCuentasPpal() {
        return idCuentasPpal;
    }

    public void setIdCuentasPpal(int idCuentasPpal) {
        this.idCuentasPpal = idCuentasPpal;
    }

    public String getNombreCuentasPpal() {
        return nombreCuentasPpal;
    }

    public void setNombreCuentaPpal(String nombreCuentasPpal) {
        this.nombreCuentasPpal = nombreCuentasPpal;
    }

   
    public int getIdCuentaPpal(String nombreCuentasPpal) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
       con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
         sql = "SELECT idCuentaPrincipal "
                 + "FROM cuentasprincipaleslogistica "
                 + "where nombreCuentaPrincipal='" + nombreCuentasPpal + "' and activo=1;";
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
                Logger.getLogger(CCuentasPrincipalesLogistica.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    public List<CCuentaSecundariaLogistica> getListaDeCuentasSecundarias() {
        return listaDeCuentasSecundarias;
    }

    public void setListaDeCuentasSecundarias(List<CCuentaSecundariaLogistica> listaDeCuentasSecundarias) {
        this.listaDeCuentasSecundarias = listaDeCuentasSecundarias;
    }

  
    public void setListaDeCuentasSecundarias() {
        ResultSet rst = null;
        Statement st;
        Connection con;
        try {
            listaDeCuentasSecundarias = new ArrayList<>();
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            
            String sql = null;
            
                 sql = "SELECT "
                        + "cp.idCuentaPrincipal, "
                        + "cp.nombreCuentaPrincipal, "
                        + "cs.idCuentaSecundaria, "
                        + "cs.nombreCuentaSecundaria, "
                        + "cs.codigoCuentaSecundaria, "
                        + "cs.activo "
                        + "FROM cuentassecundariaslogistica cs "
                        + "join cuentasprincipaleslogistica cp on cs.idcuentaPrincipal=cp.idCuentaPrincipal "
                        + "where cs.idCuentaPrincipal='"+ this.idCuentasPpal + "' "
                        + "order by cs.idCuentaPrincipal,cs.nombreCuentaSecundaria;";
            
            if (con != null) {
                st = con.createStatement();
                 rst = st.executeQuery(sql);
                while (rst.next()) {
                    CCuentaSecundariaLogistica cuenta = new CCuentaSecundariaLogistica();
                   
                    cuenta.setIdCuentaPrincipal(rst.getInt("idCuentaPrincipal"));
                    cuenta.setNombreCuentaPrincipal(rst.getString("nombreCuentaPrincipal"));
                    cuenta.setIdCuentaSecundaria(rst.getInt("idCuentaSecundaria"));
                    cuenta.setNombreCuentaSecundaria(rst.getString("nombreCuentaSecundaria"));
                    cuenta.setCodigoCuentaSecundaria(rst.getString("codigoCuentaSecundaria"));
                    cuenta.setActivo(rst.getInt("activo"));
                    

                    listaDeCuentasSecundarias.add(cuenta);

                }
                rst.close();
                st.close();
                con.close();

                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
                     
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCuentasSecundarias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public CCuentasPrincipalesLogistica() {
    }

    public CCuentasPrincipalesLogistica(Inicio ini) throws Exception {
    this.ini=ini;
    }

    public CCuentasPrincipalesLogistica(Inicio ini, int idCuentasPpal) throws Exception {
     this.ini=ini;
     this.idCuentasPpal =idCuentasPpal;   
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
        this.idCuentasPpal = idCuentasPpal;
         sql = "SELECT * "
                 + "FROM cuentasprincipaleslogistica "
                 + "where idCuentaPrincipal=" + idCuentasPpal
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
               this.idCuentasPpal = rst.getInt("idCuentaPrincipal");
                this.nombreCuentasPpal = rst.getString("nombreCuentaPrincipal");
                this.codigoCuenta = rst.getString("codigoCuenta");
                this.activoCuentasPpal=rst.getInt("activo");
            } else {
                this.idCuentasPpal = 0;
                
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar cuentasprincipaleslogistica consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CCuentasPrincipalesLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CCuentasPrincipalesLogistica(Inicio ini, String nombreCuentasPpal) throws Exception {
       this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
        sql = "SELECT * "
                + "FROM cuentasprincipaleslogistica "
                + "where nombreCuentaPrincipal='" + nombreCuentasPpal
                    + "' and activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idCuentasPpal = rst.getInt("idCuentaPrincipal");
                this.nombreCuentasPpal = rst.getString("nombreCuentaPrincipal");
                this.codigoCuenta = rst.getString("codigoCuenta");
                this.activoCuentasPpal=rst.getInt("activo");
            } else {
                this.idCuentasPpal = 0;
               
            } // idCuentaPrincipal, nombreCuentaPrincipal, codigoCuenta, activo, fechaIng, usuario, flag
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar cuentasprincipaleslogistica consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CCuentasPrincipalesLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarCuentasPpals() {
        boolean grabado = false;
        try {
           
          
            String sql = "INSERT INTO cuentasprincipaleslogistica("
                    + "nombreCuentaPrincipal,codigoCuenta,activo,usuario,flag) VALUES('"
                    + this.nombreCuentasPpal + "','"
                    + this.codigoCuenta + "','"
                    + this.activoCuentasPpal + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','-1'"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreCuentaPrincipal='" + this.nombreCuentasPpal + "',"
                    + "codigoCuenta='" + this.codigoCuenta + "',"  
                    + "activo=" + this.activoCuentasPpal + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);
           
            
        } catch (Exception ex) {
            Logger.getLogger(CCuentasPrincipalesLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarCuentasPpals() throws SQLException {
        boolean grabado = false;
       
        String sql = "UPDATE  cuentasprincipaleslogistica SET "
                + "nombreCuentaPrincipal='" + this.nombreCuentasPpal + "'," 
                + "codigoCuenta='" + this.codigoCuenta + "',"  
                + "activo=" + this.activoCuentasPpal + ","
                + "flag=-1 WHERE "
                + "idCuentaPrincipal=" + this.idCuentasPpal + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }


     public String arrListadoDeCuentasPpals(){
          
         String sql= "SELECT idCuentaPrincipal, nombreCuentaPrincipal, codigoCuenta, activo, fechaIng, usuario, flag "
                 + "FROM cuentasprincipaleslogistica "
                 + "order by nombreCuentaPrincipal asc;";
         return sql;
   }

   
}
