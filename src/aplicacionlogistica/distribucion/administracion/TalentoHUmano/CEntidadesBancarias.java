/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.administracion.TalentoHUmano;

import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
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
public class CEntidadesBancarias { //extends Inicio {

    int idEntidadBancaria;
    String nombreEntidadBancaria;
    int activoEntidadBancaria;
    Inicio ini;

    
    public CEntidadesBancarias() {
    }

    public CEntidadesBancarias(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public CEntidadesBancarias(Inicio ini, int idEntidadBancaria) throws Exception {
       this.ini = ini;
        String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
          
             sql = "SELECT * FROM bancos where idBanco=" + idEntidadBancaria
                    + " and activo=1;";
             rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idEntidadBancaria = rst.getInt(1);
                this.nombreEntidadBancaria = rst.getString(2);
                this.activoEntidadBancaria=rst.getInt(3);
            } else {
                this.idEntidadBancaria = 0;
                this.nombreEntidadBancaria = null;
                 this.activoEntidadBancaria=0;
            }
            rst.close();
            st.close();
            con.close();
             }
            
        } catch (SQLException ex) {
            Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CEntidadesBancarias(Inicio ini, String nombreEntidadBancaria) throws Exception {
        this.ini = ini;
         String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

              con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
            
             sql = "SELECT * FROM entidadesBancarias where nombreEntidadBancaria='" + nombreEntidadBancaria
                    + "' and activo=1;";
            rst = st.executeQuery(sql);
            
            if (rst.next()) {
                this.idEntidadBancaria = rst.getInt(1);
                this.nombreEntidadBancaria = rst.getString(3);
                this.activoEntidadBancaria=rst.getInt(4);
            } else {
                this.idEntidadBancaria = 0;
                this.nombreEntidadBancaria = null;
                this.activoEntidadBancaria=0;
            }
            rst.close();
             }
           
        } catch (SQLException ex) {
            Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int getIdEntidadBancaria() {
        return idEntidadBancaria;
    }

    public void setIdEntidadBancaria(int idEntidadBancaria) {
        this.idEntidadBancaria= idEntidadBancaria;
    }

    public String getNombreEntidadBancaria() {
        return nombreEntidadBancaria;
    }

    public void setNombreEntidadBancaria(String nombreEntidadBancaria) {
        this.nombreEntidadBancaria = nombreEntidadBancaria;
    }

    public int getActivoEntidadBancaria() {
        return activoEntidadBancaria;
    }

    public void setActivoEntidadBancaria(int activoEntidadBancaria) {
        this.activoEntidadBancaria = activoEntidadBancaria;
    }

    public int getIdEntidadBancaria(String nombreEntidadBancaria) {
        int id = 0;
         String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

             con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
            
             sql = "SELECT idEntidadBancaria FROM entidadesBancarias where nombreEntidadBancaria='" + nombreEntidadBancaria
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
            Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public boolean grabarEntidadesBancarias() throws SQLException {
        boolean grabado = false;
        try {
           
            String sql = "INSERT INTO bancos("
                    + "nombreBanco,activo,usuario,flag) VALUES('"
                    + this.nombreEntidadBancaria + "','"
                    + this.activoEntidadBancaria + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','1'"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreBanco='" + this.nombreEntidadBancaria + "',"
                    + "activo='" + this.activoEntidadBancaria + "',"
                    + "flag='-1'";
            grabado = ini.insertarDatosLocalmente(sql);
            
        } catch (Exception ex) {
            Logger.getLogger(CEntidadesBancarias.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarEntidadesBancarias() throws SQLException {
        boolean grabado = false;
       
        String sql = "UPDATE  bancos SET "
                + "nombreBanco='" + this.nombreEntidadBancaria + "',"
                + "activo=" + this.activoEntidadBancaria + ","
                + "flag='-1' WHERE "
                + "idBanco=" + this.idEntidadBancaria + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }

     public String arrListadoDeEntidadesBancarias(){
          //idCargo, nombreCargo, activo, fechaIng, usuario, flag
         String sql= "SELECT idBanco, nombreBanco, activo, fechaIng, usuario, flag "
                 + "FROM bancos ";
         return sql;
   }

        
   
}
