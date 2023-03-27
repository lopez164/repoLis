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
public class CTiposDeEmpresas {//extends Inicio {

    int idTipoEmpresa;
    String nombreTipoEmpresa;
    int activoTipoEmpresa;
    Inicio ini;

    public int getIdTipoEmpresa() {
        return idTipoEmpresa;
    }

    public void setIdTipoEmpresa(int idTipoEmpresa) {
        this.idTipoEmpresa = idTipoEmpresa;
    }

    public String getNombreTipoEmpresa() {
        return nombreTipoEmpresa;
    }

    public void setNombreTipoEmpresa(String nombreTipoEmpresa) {
        this.nombreTipoEmpresa = nombreTipoEmpresa;
    }

    public int getActivoTipoEmpresa() {
        return activoTipoEmpresa;
    }

    public void setActivoTipoEmpresa(int activoTipoEmpresa) {
        this.activoTipoEmpresa = activoTipoEmpresa;
    }

    public CTiposDeEmpresas() {
    }

    public CTiposDeEmpresas(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public CTiposDeEmpresas(Inicio ini, int idTipoEmpresa) throws Exception {
        this.ini = ini;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;
        this.idTipoEmpresa = idTipoEmpresa;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "SELECT * FROM tiposdeempresa "
                        + "WHERE idTipoEmpresa=" + idTipoEmpresa
                        + " AND activo=1;";
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idTipoEmpresa = rst.getInt("idTipoEmpresa");
                    this.nombreTipoEmpresa = rst.getString("nombreTipoEmpresa");
                    this.activoTipoEmpresa = rst.getInt("activo");
                } else {
                    this.idTipoEmpresa = 0;
                    this.nombreTipoEmpresa = null;
                    this.activoTipoEmpresa = 0;
                }
                rst.close();
                st.close();
                con.close();
            }

            //idtipoDeSangre, nombreTipoEmpresa, activo, fechaIng, usuario, flag
        } catch (SQLException ex) {
            System.out.println("Error en consultar tipos de Sangre consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CTiposDeEmpresas(Inicio ini, String nombreTipoEmpresa) throws Exception {
        this.ini = ini;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "SELECT * FROM tiposdeempresa "
                        + "WHERE nombreTipoEmpresa='" + nombreTipoEmpresa
                        + "' AND activo=1;";
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idTipoEmpresa = rst.getInt("IdtipoDeSangre");
                    this.nombreTipoEmpresa = rst.getString("nombreTipoEmpresa");
                    this.activoTipoEmpresa = rst.getInt("activo");;
                } else {
                    this.idTipoEmpresa = 0;
                    this.nombreTipoEmpresa = null;
                    this.activoTipoEmpresa = 0;
                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar tipos de Sangre consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getIdTipoEmpresa(String nombreTipoEmpresa) {
        int id = 0;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;
        try {
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "SELECT * FROM tiposdeempresa "
                        + "where nombreTipoEmpresa='" + nombreTipoEmpresa
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
            Logger.getLogger(CTiposDeEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public boolean grabarTipoEmpresa() throws SQLException {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO `tiposdeempresa`(`idTipoEmpresa`,"
                    + "`nombreTipoEmpresa`,`activo`,`usuario`,) VALUES("
                    + this.idTipoEmpresa + ",'"
                    + this.nombreTipoEmpresa + "',"
                    + this.activoTipoEmpresa + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoEmpresa='" + this.nombreTipoEmpresa + "',"
                    + "activo=" + this.activoTipoEmpresa + ","
                    + "flag=1";
            grabado = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CTiposDeEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public String strListadoDeTiposDeempresas() {

        String sql = "SELECT * "
                + "FROM tiposdeempresa "
                + "where "
                + "activo ='1'";

        return sql;
    }

}
