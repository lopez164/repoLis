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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CDocumentos {//extends Inicio {

    int idtiposDocumentos;
    String nombreTipoDocumento;
    String formato;
    int tieneVencimiento;
    int activoDocumento;
    Inicio ini;

    public int getIdtiposDocumentos() {
        return idtiposDocumentos;
    }

    public void setIdtiposDocumentos(int idtiposDocumentos) {
        this.idtiposDocumentos = idtiposDocumentos;
    }

    public String getNombreTipoDocumento() {
        return nombreTipoDocumento;
    }

    public void setNombreTipoDocumento(String nombreTipoDocumento) {
        this.nombreTipoDocumento = nombreTipoDocumento;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public int getTieneVencimiento() {
        return tieneVencimiento;
    }

    public void setTieneVencimiento(int tieneVencimiento) {
        this.tieneVencimiento = tieneVencimiento;
    }

    public int getActivoDocumento() {
        return activoDocumento;
    }

    public void setActivoDocumento(int activoDocumento) {
        this.activoDocumento = activoDocumento;
    }

    public CDocumentos() {
    }

    public CDocumentos(Inicio ini) throws Exception {
      this.ini=ini;

    }

    public CDocumentos(Inicio ini, int idDoc) throws Exception {
        this.ini=ini;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

       con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
        sql = "SELECT * "
                + "FROM `tiposDocumentos`"
                + " where idtiposDocumentos=" + idDoc
                + " and activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            try {
                if (rst.next()) {
                    this.idtiposDocumentos = rst.getInt(1);
                    this.nombreTipoDocumento = rst.getString(2);
                    this.formato = rst.getString(3);
                    this.tieneVencimiento = rst.getInt(4);
                    this.activoDocumento = rst.getInt(5);
                } else {
                    this.idtiposDocumentos = idDoc;
                    this.nombreTipoDocumento = null;
                    this.formato = null;
                    this.tieneVencimiento = 0;
                    this.activoDocumento = 0;
                }
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println("Error en consultar documento consulta sql " + ex.getMessage().toString());
                Logger.getLogger(CDocumentos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public CDocumentos(Inicio ini, String nombreDocumento) throws Exception {
       this.ini=ini;
        String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
         try {
             
              con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();
          
             sql = "SELECT * FROM `tiposDocumentos` where nombreTipoDocumento='" + nombreDocumento
                    + "' and activo=1;";
             
             rst = st.executeQuery(sql);
             
            if (rst.next()) {
                this.idtiposDocumentos = rst.getInt(1);
                this.nombreTipoDocumento = rst.getString(2);
                this.formato = rst.getString(3);
                this.tieneVencimiento = rst.getInt(4);
                this.activoDocumento = rst.getInt(5);
            } else {
                this.idtiposDocumentos = 0;
                this.nombreTipoDocumento = null;
                this.formato = null;
                this.tieneVencimiento = 0;
                this.activoDocumento = 0;
            }
            rst.close();
                    st.close();
                    con.close();
             }

        } catch (SQLException ex) {
            System.out.println("Error en consultar documentos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getIdDocumento(String nombreDocumento) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
        sql =  "SELECT idtiposDocumentos FROM `tiposDocumentos` where nombreTipoDocumento='" + nombreDocumento
                    + "' and activo=1;";
        if (con != null) {
           
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                   id = rst.getInt(1); 
                }else{
                    id = 0;
                }
                rst.close();
                st.close();
                con.close();
            }catch (SQLException ex) {
            System.out.println("Error en consultar documentos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }   
        }
         return id;
    }

   

     public boolean grabarTiposDocumentos() throws SQLException {
        boolean grabado = false;
         try {
            
           
            String sql = "INSERT INTO `tiposDocumentos`("
                    + "`nombreTipoDocumento`,`formato`,`activo`,`usuario`,) VALUES("
                    + this.nombreTipoDocumento + "',"
                    + this.formato + "',"
                    + this.activoDocumento + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoDocumento='" + this.nombreTipoDocumento + "',"
                    + "activo=" + this.activoDocumento + ","
                    + "flag=1";
            grabado = ini.insertarDatosRemotamente(sql);
            if (grabado) {
                Connection con;
                Statement st;
                ResultSet rst;
                con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
                sql = "SELECT * FROM `tiposDocumentos` where nombreTipoDocumento='" + this.nombreTipoDocumento
                        + "' and activo=1;";
                if (con != null) {
                    try {
                        st = con.createStatement();
                        rst = st.executeQuery(sql);
                        if (rst.next()) {
                            this.idtiposDocumentos = rst.getInt(1);
                            this.nombreTipoDocumento = rst.getString(2);
                            this.activoDocumento = rst.getInt(3);;
                        } else {
                            this.idtiposDocumentos = 0;
                            this.nombreTipoDocumento = null;
                            this.activoDocumento = 0;
                        }
                        rst.close();
                        st.close();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(CDocumentos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            
        } catch (Exception ex) {
            Logger.getLogger(CDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }
         return grabado;  
        }
       
      public boolean actualizarDocumentos(int caso) throws SQLException {
        boolean grabado = false;
        
        String sql = "UPDATE  `tiposDocumentos` SET "
                + "nombreTipoDocumento='" + this.nombreTipoDocumento + "',"
                + "activo=" + this.activoDocumento + ","
                + "flag=-1 WHERE "
                + "idtiposDocumentos=" + this.idtiposDocumentos + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }  
        
     
    
  
  
}
