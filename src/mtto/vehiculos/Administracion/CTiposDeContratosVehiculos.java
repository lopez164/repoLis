/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos.Administracion;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeContratosVehiculos;
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
public class CTiposDeContratosVehiculos {//extends Inicio {

    int idTipoDeContrato;
    String nombreTipoDeContrato;
    int activoTipoDeContrato;
    Inicio ini;

    public int getIdTipoDeContrato() {
        return idTipoDeContrato;
    }

    public void setIdTipoDeContrato(int idTipoDeContrato) {
        this.idTipoDeContrato = idTipoDeContrato;
    }

    public String getNombreTipoDeContrato() {
        return nombreTipoDeContrato;
    }

    public void setNombreTipoDeContrato(String nombreTipoDeContrato) {
        this.nombreTipoDeContrato = nombreTipoDeContrato;
    }

    public int getActivoTipoDeContrato() {
        return activoTipoDeContrato;
    }

    public void setActivoTipoDeContrato(int activoTipoDeContrato) {
        this.activoTipoDeContrato = activoTipoDeContrato;
    }

    public int getIdTipoDeContrato(String nombreTipoDeContrato) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
         sql = "SELECT idTipoContrato "
                 + "FROM tiposcontratosvehiculos "
                 + "where nombreTipoContrato='" + nombreTipoDeContrato + "' and "
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
                Logger.getLogger(CTiposDeContratosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CTiposDeContratosVehiculos() {
    }

    public CTiposDeContratosVehiculos(Inicio ini) throws Exception {
        this.ini=ini;
    }

    public CTiposDeContratosVehiculos(Inicio ini, int idTipoDeContrato) throws Exception {
       this.ini=ini;
        
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        this.idTipoDeContrato = idTipoDeContrato;
         sql = "SELECT * "
                 + "FROM tiposcontratosvehiculos "
                 + "where idTipoContrato=" + idTipoDeContrato
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idTipoDeContrato = rst.getInt(1);
                this.nombreTipoDeContrato = rst.getString(2);
                this.activoTipoDeContrato=rst.getInt(3);;
            } else {
                this.idTipoDeContrato = idTipoDeContrato;
                this.nombreTipoDeContrato = null;
                this.activoTipoDeContrato=0;
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar contratos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeContratosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CTiposDeContratosVehiculos(Inicio ini, String nombreTipoDeContrato) throws Exception {
       this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT * "
                + "FROM tiposcontratosvehiculos "
                + "where nombreTipoContrato='" + nombreTipoDeContrato
                    + "' and activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idTipoDeContrato = rst.getInt(1);
                this.nombreTipoDeContrato = rst.getString(2);
                this.activoTipoDeContrato=rst.getInt(3);;
            } else {
                this.idTipoDeContrato = 0;
                this.nombreTipoDeContrato = null;
                this.activoTipoDeContrato=0;
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar contratos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeContratosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarTipoDeContratos() {
        boolean grabado = false;
        try {
           
            String sql = "INSERT INTO tiposcontratosvehiculos("
                    + "nombreTipoContrato,activo,usuario,flag) VALUES('"
                    + this.nombreTipoDeContrato + "',"
                    + this.activoTipoDeContrato + ",'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoContrato='" + this.nombreTipoDeContrato + "',"
                    + "activo=" + this.activoTipoDeContrato + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);

            
        } catch (Exception ex) {
            Logger.getLogger(CTiposDeContratosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarTipoDeContratos() throws SQLException {
        boolean grabado = false;
       
        String sql = "UPDATE  tiposcontratosvehiculos SET "
                + "nombreTipoContrato='" + this.nombreTipoDeContrato + "',"
                + "activo=" + this.activoTipoDeContrato + ","
                + "flag=-1 WHERE "
                + "idTipoContrato=" + this.idTipoDeContrato + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }


     public String arrListadoDeTiposContratosVehiculos() {
      
            String sql = "SELECT idTipoContrato, nombreTipoContrato, activo, fechaIng, usuario, flag  "
                    + "FROM tiposcontratosvehiculos;";
        
        return sql;
    }

   
}
