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
public class CEstadosManifiestos { //extends Inicio { idEstadoManifiesto, nombreDeEstadoManifiesto, activo, fechaIng, usuario, flag

    int idEstadoManifiesto;
    String nombreDeEstadoManifiesto;
    int activoEstadoManifiesto;
    int flag;
    String usuario;
    String fechaIng;
    Inicio ini;

    public int getIdEstadoManifiesto() {
        return idEstadoManifiesto;
    }

    public void setIdEstadoManifiesto(int idEstadoManifiesto) {
        this.idEstadoManifiesto = idEstadoManifiesto;
    }

    public String getNombreDeEstadoManifiesto() {
        return nombreDeEstadoManifiesto;
    }

    public void setNombreDeEstadoManifiesto(String nombreDeEstadoManifiesto) {
        this.nombreDeEstadoManifiesto = nombreDeEstadoManifiesto;
    }

    public int getActivoEstadoManifiesto() {
        return activoEstadoManifiesto;
    }

    public void setActivoEstadoManifiesto(int activoEstadoManifiesto) {
        this.activoEstadoManifiesto = activoEstadoManifiesto;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
        this.fechaIng = fechaIng;
    }

    public int getIdCausalesDeDevolucion(String nombreCausalesDeRechazol) {
        int id = 0;

        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        // , , activo, fechaIng, usuario, flag
        sql = "SELECT idEstadoManifiesto "
                + "FROM "
                + "estadosDeManifiesto "
                + "WHERE nombreDeEstadoManifiesto='" + nombreCausalesDeRechazol + "' and activo=1;";
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
                Logger.getLogger(CEstadosManifiestos.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return id;
    }

    public CEstadosManifiestos() {
    }

    public CEstadosManifiestos(Inicio ini) throws Exception {
        this.ini = ini;
    }

    public CEstadosManifiestos(Inicio ini, int idEstadoManifiesto) throws Exception {

        this.ini = ini;

        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "SELECT idEstadoManifiesto, nombreDeEstadoManifiesto, activo, usuario, flag "
                    + "FROM estadosDeManifiesto "
                    + "WHERE idEstadoManifiesto=" + idEstadoManifiesto
                    + " and activo=1;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idEstadoManifiesto = rst.getInt("idEstadoManifiesto");
                    this.nombreDeEstadoManifiesto = rst.getString("nombreDeEstadoManifiesto");
                    this.activoEstadoManifiesto = rst.getInt("activo");
                    this.usuario = rst.getString("usuario");
                    this.fechaIng = rst.getString("fechaIng");

                } else {
                    this.idEstadoManifiesto = 0;

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar tiposCausalesDeRechazolDeVentas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CEstadosManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CEstadosManifiestos(Inicio ini, String nombreCausalesDeRechazol) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "SELECT idEstadoManifiesto, nombreDeEstadoManifiesto, activo, usuario, flag "
                    + "FROM estadosDeManifiesto "
                    + "WHERE nombreDeEstadoManifiesto='" + nombreCausalesDeRechazol
                    + "' AND activo=1;";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idEstadoManifiesto = rst.getInt("idEstadoManifiesto");
                    this.nombreDeEstadoManifiesto = rst.getString("nombreDeEstadoManifiesto");
                    this.activoEstadoManifiesto = rst.getInt("activo");
                    this.usuario = rst.getString("usuario");
                    this.fechaIng = rst.getString("fechaIng");
                } else {
                    this.idEstadoManifiesto = 0;

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar tiposCausalesDeRechazolDeVentas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CEstadosManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarEstadosManifiestos(int caso) {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO estadosDeManifiesto("
                    + " nombreDeEstadoManifiesto, activo,  usuario, flag) VALUES('"
                    + this.nombreDeEstadoManifiesto + "',"
                    + this.activoEstadoManifiesto + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreDeEstadoManifiesto='" + this.nombreDeEstadoManifiesto + "',"
                    + "activo=" + this.activoEstadoManifiesto + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);


        } catch (Exception ex) {
            Logger.getLogger(CEstadosManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean grabarEstadosManifiestos() {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO estadosDeManifiesto("
                    + " nombreDeEstadoManifiesto, activo,  usuario, flag) VALUES('"
                    + this.nombreDeEstadoManifiesto + "',"
                    + this.activoEstadoManifiesto + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreDeEstadoManifiesto='" + this.nombreDeEstadoManifiesto + "',"
                    + "activo=" + this.activoEstadoManifiesto + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CEstadosManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean actualizarEstadosManifiestos(int caso) throws SQLException {
        boolean grabado = false;

        String sql = "UPDATE  estadosDeManifiesto SET "
                + "nombreDeEstadoManifiesto='" + this.nombreDeEstadoManifiesto + "',"
                + "activo=" + this.activoEstadoManifiesto + ","
                + "flag=-1 WHERE "
                + "idEstadoManifiesto=" + this.idEstadoManifiesto + ";";
        grabado = ini.insertarDatosRemotamente(sql);

        return grabado;
    }

    public boolean actualizarEstadosManifiestos() throws SQLException {
        boolean grabado = false;

        String sql = "UPDATE  estadosDeManifiesto SET "
                + "nombreDeEstadoManifiesto='" + this.nombreDeEstadoManifiesto + "',"
                + "activo=" + this.activoEstadoManifiesto + ","
                + "flag=-1 WHERE "
                + "idEstadoManifiesto=" + this.idEstadoManifiesto + ";";
        grabado = ini.insertarDatosRemotamente(sql);

        return grabado;
    }

    public String arrListadoDeCCausalesDeRechazo() {

        // idEstadoManifiesto, nombreDeEstadoManifiesto, activo, fechaIng, usuario, flag
        String sql = "SELECT * "
                + "FROM estadosDeManifiesto  "
                + "ORDER BY nombreDeEstadoManifiesto ASC";
        return sql;
    }

}
