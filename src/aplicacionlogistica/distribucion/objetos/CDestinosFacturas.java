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
public class CDestinosFacturas { //extends Inicio {

    int iddestinosFacturas;
    String nombreDestino;
    int activoDestino;

    String usuario;
    Inicio ini;

    public int getIddestinosFacturas() {
        return iddestinosFacturas;
    }

    public void setIddestinosFacturas(int iddestinosfacturas) {
        this.iddestinosFacturas = iddestinosfacturas;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }

    public int getAcctivoDestino() {
        return activoDestino;
    }

    public void setActivoDestino(int activoDestino) {
        this.activoDestino = activoDestino;
    }

    public int getIddestinosfacturas(String nombreDestino) {
        int id = 0;

        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT iddestinosfacturas FROM `destinosfacturas` where nombreDestino='" + nombreDestino + "' and activo=1;";
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
                Logger.getLogger(CDestinosFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return id;
    }

    public CDestinosFacturas() {
    }

    public CDestinosFacturas(Inicio ini) throws Exception {
        this.ini = ini;
    }

    public CDestinosFacturas(Inicio ini, int iddestinosfacturas) throws Exception {

        this.ini = ini;

        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            this.iddestinosFacturas = iddestinosfacturas;
            sql = "SELECT * FROM `destinosfacturas` where iddestinosfacturas=" + iddestinosfacturas
                    + " and activo=1;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.iddestinosFacturas = rst.getInt(1);
                    this.nombreDestino = rst.getString(2);
                    this.activoDestino = rst.getInt(3);;
                } else {
                    this.iddestinosFacturas = 0;
                    this.nombreDestino = null;
                    this.activoDestino = 0;
                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar destinosfacturas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CDestinosFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CDestinosFacturas(Inicio ini, String nombreDestino) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            sql = "SELECT * FROM `destinosfacturas` where nombreDestino='" + nombreDestino
                    + "' and activo=1;";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.iddestinosFacturas = rst.getInt(1);
                    this.nombreDestino = rst.getString(2);
                    this.activoDestino = rst.getInt(3);
                } else {
                    this.iddestinosFacturas = 0;
                    this.nombreDestino = null;
                    this.activoDestino = 0;
                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar destinosfacturas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CDestinosFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarDestinosFActuras() {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO `destinosfacturas`("
                    + "`nombreDestino`,`activo`,`usuario`,`flag`) VALUES('"
                    + this.nombreDestino + "',"
                    + "1,'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreDestino='" + this.nombreDestino + "',"
                    + "activo=1,"
                    + "flag=-1";
            grabado = ini.insertarDatosLocalmente(sql);
            if (grabado) {
                Connection con;
                Statement st;
                ResultSet rst;
                con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
                sql = "SELECT * FROM `destinosfacturas` where nombreDestino='" + this.nombreDestino
                        + "' and activo=1;";
                if (con != null) {
                    try {
                        st = con.createStatement();
                        rst = st.executeQuery(sql);
                        if (rst.next()) {
                            this.iddestinosFacturas = rst.getInt(1);
                            this.nombreDestino = rst.getString(2);
                            this.activoDestino = rst.getInt(3);
                        } else {
                            this.iddestinosFacturas = 0;
                            this.nombreDestino = null;
                            this.activoDestino = 0;
                        }
                        rst.close();
                        st.close();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(CDestinosFacturas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(CDestinosFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean actualizarDestinosFacturas() throws SQLException {
        boolean grabado = false;

        String sql = "UPDATE  `destinosfacturas` SET "
                + "nombreDestino='" + this.nombreDestino + "',"
                + "activo=" + this.activoDestino + ","
                + "flag=-1 WHERE "
                + "iddestinosFacturas=" + this.iddestinosFacturas + ";";
        grabado = ini.insertarDatosLocalmente(sql);

        return grabado;
    }

    public String arrListadoDeDestinosFacturas() {
       
        String sql = "SELECT iddestinosFacturas, nombreDestino, activo, fechaIng, usuario, flag "
                + "FROM `destinosfacturas`;";
        return sql;
    }

}
