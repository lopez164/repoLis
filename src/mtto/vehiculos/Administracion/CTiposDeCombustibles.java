/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos.Administracion;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeCombustibles;
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
public class CTiposDeCombustibles  {

    int idCombustible;
    String nombreTipoCombustible;
    int activoCombustible;
    Inicio ini;

    public int getIdCombustible() {
        return idCombustible;
    }

    public void setIdCombustible(int idCombustible) {
        this.idCombustible = idCombustible;
    }

    public String getNombreCombustible() {
        return nombreTipoCombustible;
    }

    public void setNombreCombustible(String nombreTipoCombustible) {
        this.nombreTipoCombustible = nombreTipoCombustible;
    }

    public int getActivoCombustible() {
        return activoCombustible;
    }

    public void setActivoCombustible(int activoCombustible) {
        this.activoCombustible = activoCombustible;
    }

    public int getIdCombustible(String nombreCombustible) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

         sql = "SELECT idTipoCombustible "
                 + "FROM tiposdecombustible "
                 + "where nombreTipoCombustible='" + nombreCombustible + "' and "
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
                Logger.getLogger(CTiposDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CTiposDeCombustibles() {
        
    }

    public CTiposDeCombustibles(Inicio ini) throws Exception {
        this.ini=ini;

    }

    public CTiposDeCombustibles(Inicio ini, int idCombustible) throws Exception {
       this.ini=ini;
        
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        this.idCombustible = idCombustible;
         sql = "SELECT * "
                 + "FROM tiposdecombustible "
                 + "where "
                 + "idTipoCombustible=" + idCombustible
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idCombustible = rst.getInt(1);
                this.nombreTipoCombustible = rst.getString(2);
                this.activoCombustible=rst.getInt(3);;
            } else {
                this.idCombustible = idCombustible;
                
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposdecombustible consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CTiposDeCombustibles(Inicio ini, String nombreTipoCombustible) throws Exception {
        this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT * "
                + "FROM tiposdecombustible "
                + "where nombreTipoCombustible='" + nombreTipoCombustible
                    + "' and activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idCombustible = rst.getInt(1);
                this.nombreTipoCombustible = rst.getString(2);
                this.activoCombustible=rst.getInt(3);;
            } else {
                this.idCombustible = 0;
                
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposdecombustible consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarCombustibles() {
       boolean grabado = false;
        try {
           
            String sql = "INSERT INTO tiposdecombustible("
                    + "nombreTipoCombustible,activo,usuario,flag) VALUES('"
                    + this.nombreTipoCombustible + "',"
                    + this.activoCombustible + ",'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoCombustible='" + this.nombreTipoCombustible + "',"
                    + "activo=" + this.activoCombustible + ","
                    + "flag=-1";
            grabado = this.ini.insertarDatosRemotamente(sql);
            
          
        } catch (Exception ex) {
            Logger.getLogger(CTiposDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
        }
          return grabado;
    }
    
    public boolean actualizarCombustibles() throws SQLException {
        boolean grabado = false;
       
        String sql = "UPDATE  tiposdecombustible SET "
                + "nombreTipoCombustible='" + this.nombreTipoCombustible + "',"
                + "activo=" + this.activoCombustible + ","
                + "flag=-1 WHERE "
                + "idTipoCombustible=" + this.idCombustible + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }

 
     public String arrListadoDeCombustibles() {
      
            String sql = "SELECT idTipoCombustible, nombreTipoCombustible, activo, fechaIng, usuario, flag  "
                    + "FROM tiposdecombustible;";
        
        return sql;
    }

    
}
