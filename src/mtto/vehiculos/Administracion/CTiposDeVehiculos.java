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
public class CTiposDeVehiculos {//extends Inicio {

    int idTipoDeVehiculo;
    String nombreTipoDeVehiculo;
    int activoTipoDeVehiculo;
    Inicio ini;

    public int getIdTipoDeVehiculo() {
        return idTipoDeVehiculo;
    }

    public void setIdTipoDeVehiculo(int idTipoDeVehiculo) {
        this.idTipoDeVehiculo = idTipoDeVehiculo;
    }

    public String getNombreTipoDeVehiculo() {
        return nombreTipoDeVehiculo;
    }

    public void setNombreTipoDeVehiculo(String nombreTipoDeVehiculo) {
        this.nombreTipoDeVehiculo = nombreTipoDeVehiculo;
    }

    public int getActivoTipoDeVehiculo() {
        return activoTipoDeVehiculo;
    }

    public void setActivoTipoDeVehiculo(int activoTipoDeVehiculo) {
        this.activoTipoDeVehiculo = activoTipoDeVehiculo;
    }

    public int getIdTipoDeVehiculo(String nombreTipoDeVehiculo) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
         sql = "SELECT idTipoVehiculo "
                 + "FROM tiposdevehiculos "
                 + "where "
                 + "nombreTipoVehiculo='" + nombreTipoDeVehiculo + "' and "
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
                Logger.getLogger(CTiposDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CTiposDeVehiculos() {
    }

    public CTiposDeVehiculos(Inicio ini) throws Exception {
       this.ini=ini;
    }

    public CTiposDeVehiculos(Inicio ini, int idTipoDeVehiculo) throws Exception {
        this.ini=ini;
        
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
       
         sql = "SELECT * "
                 + "FROM tiposdevehiculos "
                 + "where "
                 + "idTipoVehiculo=" + idTipoDeVehiculo
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idTipoDeVehiculo = rst.getInt(1);
                this.nombreTipoDeVehiculo = rst.getString(2);
                this.activoTipoDeVehiculo=rst.getInt(3);
            } else {
                this.idTipoDeVehiculo = 0;
                
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar cargos consulta sql " + ex.getMessage());
            Logger.getLogger(CTiposDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CTiposDeVehiculos(Inicio ini, String nombreTipoDeVehiculo) throws Exception {
       this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT * "
                + "FROM tiposdevehiculos "
                + "where nombreTipoVehiculo='" + nombreTipoDeVehiculo
                    + "' and activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idTipoDeVehiculo = rst.getInt(1);
                this.nombreTipoDeVehiculo = rst.getString(2);
                this.activoTipoDeVehiculo=rst.getInt(3);
            } else {
                this.idTipoDeVehiculo = 0;
               
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar cargos consulta sql " + ex.getMessage());
            Logger.getLogger(CTiposDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarTipoDeVehiculos() {
        boolean grabado = false;
        try {
           
                    String sql = "INSERT INTO tiposdevehiculos("
                    + "nombreTipoVehiculo,activo,usuario,flag) VALUES('"
                    + this.nombreTipoDeVehiculo + "',"
                    + this.activoTipoDeVehiculo + ",'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoVehiculo='" + this.nombreTipoDeVehiculo + "',"
                    + "activo=" + this.activoTipoDeVehiculo + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);

            
        } catch (Exception ex) {
            Logger.getLogger(CTiposDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarTipoDeVehiculos() throws SQLException {
        boolean grabado;
        
        String sql = "UPDATE  tiposdevehiculos SET "
                + "nombreTipoVehiculo='" + this.nombreTipoDeVehiculo + "',"
                + "activo=" + this.activoTipoDeVehiculo + ","
                + "flag=-1 WHERE "
                + "idTipoVehiculo=" + this.idTipoDeVehiculo + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }


    
public String arrListadoDeTiposDeVehiculos() {
      
            String sql = "SELECT idTipoVehiculo, nombreTipoVehiculo, activo, fechaIng, usuario, flag "
                    + "FROM tiposdevehiculos;";
        
        return sql;
    }
   
}
