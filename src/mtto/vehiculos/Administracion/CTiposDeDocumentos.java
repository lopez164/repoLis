/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos.Administracion;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class CTiposDeDocumentos {
    int idtiposDocumentos;
    String nombreTipoDocumento;
    String formato;
    int tieneVencimiento;
    int activo;
    Date fechaIng;
    String usuario;
    int flag;
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

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Date getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(Date fechaIng) {
        this.fechaIng = fechaIng;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public CTiposDeDocumentos() {
    }

    public CTiposDeDocumentos(Inicio ini) {
        this.ini = ini;
    }

    public CTiposDeDocumentos(Inicio ini, int idtipoDocumento) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "SELECT * "
                    + "FROM idtiposDocumentos "
                    + "where idtiposDocumentos=" + idtipoDocumento
                    + ";";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idtiposDocumentos = rst.getInt("idtiposDocumentos");
                    this.nombreTipoDocumento = rst.getString("nombreTipoDocumento");
                    this.formato=  rst.getString("formato");
                    this.tieneVencimiento = rst.getInt("tieneVencimiento");
                    this.activo = rst.getInt("activo");
                } else {
                    this.idtiposDocumentos = 0;
                    this.nombreTipoDocumento = null;
                    this.activo = 0;
                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar marcasdellantass consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CMarcasDeLLantas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CTiposDeDocumentos(Inicio ini, String nombreTipoDocumento) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            sql = "SELECT * "
                    + "FROM tiposdocumentos "
                    + "where nombreTipoDocumento='" + nombreTipoDocumento
                    + "'";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idtiposDocumentos = rst.getInt("idtiposDocumentos");
                    this.nombreTipoDocumento = rst.getString("nombreTipoDocumento");
                    this.formato=  rst.getString("formato");
                    this.tieneVencimiento = rst.getInt("tieneVencimiento");
                    this.activo = rst.getInt("activo");
                } else {
                    this.idtiposDocumentos = 0;
                   
                }
                rst.close();
                st.close();
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error en consultar marcasdellantass consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CMarcasDeLLantas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarTipoDeDocumento() {
        boolean grabado = false;
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        try {

            String sql = "INSERT INTO tiposdocumentos("
                    + "nombreTipoDocumento,formato,tieneVencimiento,activo,usuario,flag) VALUES('"
                   + this.nombreTipoDocumento + "','"
                   + this.formato + "','"
                   + this.tieneVencimiento + "','"
                   + this.activo + "','"                    
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','1'"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoDocumento='" + this.nombreTipoDocumento + "',"
                    + "activo='" + this.activo + "',"
                    + "flag='-1'";
            grabado = ini.insertarDatosRemotamente(sql);


        } catch (Exception ex) {
            try {
                Logger.getLogger(CMarcasDeLLantas.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CMarcasDeLLantas.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return grabado;
    }

    public boolean actualizarTipoDeDocumento() throws SQLException {
        boolean grabado = false;

        String sql = "UPDATE  tiposdocumentos SET "
                + "nombreTipoDocumento='" + this.nombreTipoDocumento + "',"
                + "formato='" + this.formato + "',"
                + "tieneVencimiento='" + this.tieneVencimiento + "',"
                 + "activo='" + this.activo + "',"
                 + "flag=-1 WHERE "
                + "idtiposDocumentos=" + this.idtiposDocumentos + ";";
        
        grabado = ini.insertarDatosRemotamente(sql);

        return grabado;
    }


    public String toString(){
          //idCargo, nombreCargo, activo, fechaIng, usuario, flag
         String sql= "SELECT * "
                 + "FROM tiposdocumentos;";
         return sql;
   }
}
