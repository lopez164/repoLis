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
public class CTiposEstadosVehiculos {//extends Inicio {

    int idtipoDeEstadosVehiculo;
    String nombreEstadoVehiculo;
    int activoEstadoVehiculo;
    String usuario;
    String fechaIng;
    Inicio ini;

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

    public int getIdtipoDeEstadosVehiculo() {
        return idtipoDeEstadosVehiculo;
    }

    public void setIdtipoDeEstadosVehiculo(int idTipoDeVehiculo) {
        this.idtipoDeEstadosVehiculo = idTipoDeVehiculo;
    }

    public String getNombreEstadoVehiculo() {
        return nombreEstadoVehiculo;
    }

    public void setNombreEstadoVehiculo(String nombreEstadoVehiculo) {
        this.nombreEstadoVehiculo = nombreEstadoVehiculo;
    }

    public int getActivoEstadoVehiculo() {
        return activoEstadoVehiculo;
    }

    public void setActivoEstadoVehiculo(int activoEstadoVehiculo) {
        this.activoEstadoVehiculo = activoEstadoVehiculo;
    }

    public int getIdtipoDeEstadosVehiculo(String nombreTipoDeVehiculo) {
        int id = 0;

        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT idtipoDeEstadosVehiculo "
                + "FROM tipodeestadosvehiculo "
                + "where "
                + "nombreEstadoVehiculo='" + nombreEstadoVehiculo + "' and "
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
                Logger.getLogger(CTiposEstadosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return id;
    }

    public CTiposEstadosVehiculos() {
    }

    public CTiposEstadosVehiculos(Inicio ini) throws Exception {
        this.ini = ini;
    }

    public CTiposEstadosVehiculos(Inicio ini, int idTipoDeVehiculo) throws Exception {
        this.ini = ini;

        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            this.idtipoDeEstadosVehiculo = idTipoDeVehiculo;
            sql = "SELECT * "
                    + "FROM tipodeestadosvehiculo "
                    + "where "
                    + "idtipoDeEstadosVehiculo=" + idtipoDeEstadosVehiculo
                    + " and activo=1;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql); // idtipoDeEstadosVehiculo, nombreEstadoVehiculo, activo, fechaIng, usuario, flag
                if (rst.next()) {
                    this.idtipoDeEstadosVehiculo = rst.getInt("idtipoDeEstadosVehiculo");
                    this.nombreEstadoVehiculo = rst.getString("nombreEstadoVehiculo");
                    this.activoEstadoVehiculo = rst.getInt("activo");
                    this.usuario = rst.getString("usuario");
                    this.fechaIng = rst.getString("fechaIng");
                } else {
                    this.idtipoDeEstadosVehiculo = 0;

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar cargos consulta sql " + ex.getMessage());
            Logger.getLogger(CTiposEstadosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CTiposEstadosVehiculos(Inicio ini, String nombreTipoDeVehiculo) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            sql = "SELECT * "
                    + "FROM tipodeestadosvehiculo "
                    + "where nombreEstadoVehiculo='" + nombreEstadoVehiculo
                    + "' and activo=1;";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idtipoDeEstadosVehiculo = rst.getInt("idtipoDeEstadosVehiculo");
                    this.nombreEstadoVehiculo = rst.getString("nombreEstadoVehiculo");
                    this.activoEstadoVehiculo = rst.getInt("activo");
                    this.usuario = rst.getString("usuario");
                    this.fechaIng = rst.getString("fechaIng");
                } else {
                    this.idtipoDeEstadosVehiculo = 0;

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar cargos consulta sql " + ex.getMessage());
            Logger.getLogger(CTiposEstadosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarEstadoVehiculos() {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO tipodeestadosvehiculo("
                    + "nombreEstadoVehiculo,activo,usuario,flag) VALUES('"
                    + this.nombreEstadoVehiculo + "',"
                    + this.activoEstadoVehiculo + ",'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreEstadoVehiculo='" + this.nombreEstadoVehiculo + "',"
                    + "activo=" + this.activoEstadoVehiculo + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);
        
        } catch (Exception ex) {
            Logger.getLogger(CTiposEstadosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean actualizarEstadoVehiculos() throws SQLException {
        boolean grabado;

        String sql = "UPDATE  tipodeestadosvehiculo SET "
                + "nombreEstadoVehiculo='" + this.nombreEstadoVehiculo + "',"
                + "activo=" + this.activoEstadoVehiculo + ","
                + "flag=-1 WHERE "
                + "idtipoDeEstadosVehiculo=" + this.idtipoDeEstadosVehiculo + ";";
        grabado = ini.insertarDatosRemotamente(sql);

        return grabado;
    }

    public String arrListadoEstadosDeVehiculos() {

        String sql = "SELECT idtipoDeEstadosVehiculo, nombreEstadoVehiculo, activo, fechaIng, usuario, flag "
                + "FROM tipodeestadosvehiculo;";

        return sql;
    }

}
