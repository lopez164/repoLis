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
public class CTiposDeContratosPersonas {//extends Inicio {

    int idTipoContrato;
    String nombreTipoDeContrato;
    int activoTipoDeContrato;
    Inicio ini;

    public int getIdTipoDeContrato() {
        return idTipoContrato;
    }

    public void setIdTipoDeContrato(int idTipocontrato) {
        this.idTipoContrato = idTipocontrato;
    }

    public String getNombreTipoDeContrato() {
        return nombreTipoDeContrato;
    }

    public void setNombreTipoDeContrato(String nombreTipoDeContrato) {
        this.nombreTipoDeContrato = nombreTipoDeContrato;
    }

    public int getActivoTipoDeContrato(){
       return activoTipoDeContrato;
    }

    public void setActivoTipoDeContrato(int activoTipoContrato) {
        this.activoTipoDeContrato = activoTipoContrato;
    }

    public int getIdTipoDeContrato(String nombreTipoDeContrato) {
        int id = 0;
         String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

             con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
                
           
             sql = "SELECT idtipoContrato FROM tiposdecontratospersonas where nombreTipoDeContrato='" + nombreTipoDeContrato
                    + "' and activo=1;";
             
             rst = st.executeQuery(sql);
            
            if (rst.next()) {
                id = rst.getInt(1);
            } else {
                id = 0;
            }
            rst.close();
            st.close();
            con.close();
             }
        } catch (SQLException ex) {
             System.out.println("Error en consultar tingres consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeContratosPersonas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    public CTiposDeContratosPersonas() {
    }

    public CTiposDeContratosPersonas(Inicio ini) throws Exception {
       this.ini=ini;

    }

    public CTiposDeContratosPersonas(Inicio ini, int idTipoDeContrato) throws Exception {
        this.ini=ini;
        String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
         this.idTipoContrato = idTipoDeContrato;
        try {

             con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
              
             sql = "SELECT * FROM tiposcontratospersonas where idtipoContrato=" + idTipoDeContrato
                    + " and activo=1;";
            
            rst = st.executeQuery(sql);
            
            if (rst.next()) {
                this.idTipoContrato = rst.getInt(1);
                this.nombreTipoDeContrato = rst.getString(2);
                this.activoTipoDeContrato=rst.getInt(3);;
            } else {
                this.idTipoContrato = 0;
                this.nombreTipoDeContrato = null;
                this.activoTipoDeContrato=0;
            }
            rst.close();
            //con.close();
             }
        } catch (SQLException ex) {
             System.out.println("Error en consultar tipos de Contrato consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeContratosPersonas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CTiposDeContratosPersonas(Inicio ini, String nombreTipoDeContrato) throws Exception {
        this.ini=ini;
          String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

             con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
           
          
             sql = "SELECT * FROM tiposcontratospersonas where nombreTipoDeContrato='" + nombreTipoDeContrato
                    + "' and activo=1;";
             rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idTipoContrato = rst.getInt(1);
                this.nombreTipoDeContrato = rst.getString(2);
                this.activoTipoDeContrato=rst.getInt(3);;
            } else {
                this.idTipoContrato = 0;
                this.nombreTipoDeContrato = null;
                this.activoTipoDeContrato=0;
            }
            rst.close();
            st.close();
            con.close();
             }
        } catch (SQLException ex) {
             System.out.println("Error en consultar tipos de Contrato consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeContratosPersonas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public boolean grabarTiposDeContrato() throws SQLException {
       boolean grabado = false;
        try {
            
            String sql = "INSERT INTO  tiposcontratospersonas("
                    + "nombreTipoDeContrato,activo,usuario,flag) VALUES('"
                    + this.nombreTipoDeContrato + "',"
                    + this.activoTipoDeContrato + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','1'"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoDeContrato='" + this.nombreTipoDeContrato + "',"
                    + "activo=" + this.activoTipoDeContrato + ","
                    + "flag=1";
            grabado = ini.insertarDatosRemotamente(sql);
            
        } catch (Exception ex) {
            Logger.getLogger(CTiposDeContratosPersonas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarTipoContratoPer() throws SQLException {
        boolean grabado = false;
       
        String sql = "UPDATE  tiposcontratospersonas SET "
                + "nombreTipoDeContrato='" + this.nombreTipoDeContrato + "',"
                + "activo=" + this.activoTipoDeContrato + ","
                + "flag=-1 WHERE "
                + "idTipoContrato=" + this.idTipoContrato + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }

 
 public String arrListadoDeContratosPersonas() {

        // iidTipoContrato, nombreTipoDeContrato, activo, fechaIng, usuario, flag
        String sql = "SELECT idTipoContrato, nombreTipoDeContrato, activo, fechaIng, usuario, flag "
                + "from tiposcontratospersonas "
                + " ORDER BY tiposcontratospersonas.nombreTipoDeContrato ASC";
        return sql;
    }
  
}
