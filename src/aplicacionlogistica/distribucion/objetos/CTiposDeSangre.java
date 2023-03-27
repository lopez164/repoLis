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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CTiposDeSangre {//extends Inicio {

    int idTipoDeSangre;
    String nombreTipoDeSangre;
    int activoTipoDeSAngre;
    Inicio ini;

    public int getIdTipoDeSAngre() {
        return idTipoDeSangre;
    }

    public void setIdTipoDeSaangre(int idTipoDeSangre) {
        this.idTipoDeSangre = idTipoDeSangre;
    }

    public String getNombreTipoDeSAngre() {
        return nombreTipoDeSangre;
    }

    public void setNombreTipoDeSangre(String nombreTipoDeSangre) {
        this.nombreTipoDeSangre = nombreTipoDeSangre;
    }

    public int getActivoTipoDeSangre() {
        return activoTipoDeSAngre;
    }

    public void setActivoTipoDeSangre(int activoTipoSangre) {
        this.activoTipoDeSAngre = activoTipoSangre;
    }

    public CTiposDeSangre() {
    }

    public CTiposDeSangre(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public CTiposDeSangre(Inicio ini, int idTipoDeSangre) throws Exception {
        this.ini = ini;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;
        this.idTipoDeSangre = idTipoDeSangre;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "SELECT IdtipoDeSangre, nombreTipoDeSangre, activo, fechaIng, usuario, flag "
                        + "FROM `tiposdesangre` "
                        + "WHERE idtipoDeSangre=" + idTipoDeSangre
                        + " AND activo=1;";
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idTipoDeSangre = rst.getInt("IdtipoDeSangre");
                    this.nombreTipoDeSangre = rst.getString("nombreTipoDeSangre");
                    this.activoTipoDeSAngre = rst.getInt("activo");;
                } else {
                    this.idTipoDeSangre = 0;
                    this.nombreTipoDeSangre = null;
                    this.activoTipoDeSAngre = 0;
                }
                rst.close();
                st.close();
                con.close();
            }

            //idtipoDeSangre, nombreTipoDeSangre, activo, fechaIng, usuario, flag
        } catch (SQLException ex) {
            System.out.println("Error en consultar tipos de Sangre consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeSangre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CTiposDeSangre(Inicio ini, String nombreTipoDeSangre) throws Exception {
        this.ini = ini;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "SELECT IdtipoDeSangre, nombreTipoDeSangre, activo, fechaIng, usuario, flag "
                        + "FROM `tiposdesangre` "
                        + "WHERE nombreTipoDeSangre='" + nombreTipoDeSangre
                        + "' AND activo=1;";
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idTipoDeSangre = rst.getInt("IdtipoDeSangre");
                    this.nombreTipoDeSangre = rst.getString("nombreTipoDeSangre");
                    this.activoTipoDeSAngre = rst.getInt("activo");;
                } else {
                    this.idTipoDeSangre = 0;
                    this.nombreTipoDeSangre = null;
                    this.activoTipoDeSAngre = 0;
                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar tipos de Sangre consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDeSangre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getIdTipoDeSangre(String nombreTipoDeSangre) {
        int id = 0;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;
        try {
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "SELECT idtipoDeSangre FROM `tiposdesangre` where nombreTipoDeSangre='" + nombreTipoDeSangre
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
            Logger.getLogger(CTiposDeSangre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public boolean grabarTiposDeSangre() throws SQLException {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO `tiposdesangre`(`idtipoDeSangre`,"
                    + "`nombreTipoDeSangre`,`activo`,`usuario`,) VALUES("
                    + this.idTipoDeSangre + ",'"
                    + this.nombreTipoDeSangre + "',"
                    + this.activoTipoDeSAngre + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoDeSangre='" + this.nombreTipoDeSangre + "',"
                    + "activo=" + this.activoTipoDeSAngre + ","
                    + "flag=1";
            grabado = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CTiposDeSangre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public String rstListadoDeTiposDeSangres() {

        String sql = "SELECT IdtipoDeSangre, nombreTipoDeSangre, activo, fechaIng, usuario, flag "
                + "FROM `tiposdesangre` ";

        return sql;
    }

}
