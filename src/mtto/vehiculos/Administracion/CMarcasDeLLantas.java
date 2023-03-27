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
public class CMarcasDeLLantas {//extends Inicio {

    int idMarcaDeLLanta;
    String nombreMarcaDeLLantas;
    int activoMarcaDeLLantas;

    Inicio ini;

    public int getIdMarcaDeLLantas() {
        return idMarcaDeLLanta;
    }

    public void setIdMarcaDeLLantas(int idMarcaDeLLanta) {
        this.idMarcaDeLLanta = idMarcaDeLLanta;
    }

    public String getNombreMarcaDeLLantas() {
        return nombreMarcaDeLLantas;
    }

    public void setNombreMarcaDeLLantas(String nombreMarcaDeLLantas) {
        this.nombreMarcaDeLLantas = nombreMarcaDeLLantas;
    }

    public int getActivoMarcaDeLLantas() {
        return activoMarcaDeLLantas;
    }

    public void setActivoMarcaDeLLantas(int activoMarcaDeLLantas) {
        this.activoMarcaDeLLantas = activoMarcaDeLLantas;
    }

    public int getIdMarcaDeLLantas(String nombreMarcaDeLLantas) {
        int id = 0;

        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT idMarcaDeLlanta "
                + "FROM marcasdellantas "
                + "where nombreMarcaDeLlanta='" + nombreMarcaDeLLantas + "' and activo=1;";
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
                Logger.getLogger(CMarcasDeLLantas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return id;
    }

    public CMarcasDeLLantas() {
    }

    public CMarcasDeLLantas(Inicio ini) throws Exception {
        this.ini = ini;
        this.idMarcaDeLLanta = 0;
        this.nombreMarcaDeLLantas = "";

    }

    public CMarcasDeLLantas(Inicio ini, int idMarcaDeLLanta) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "SELECT * "
                    + "FROM marcasdellantas "
                    + "where idMarcaDeLlanta=" + idMarcaDeLLanta
                    + " and activo=1;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idMarcaDeLLanta = rst.getInt(1);
                    this.nombreMarcaDeLLantas = rst.getString(2);
                    this.activoMarcaDeLLantas = rst.getInt(3);;
                } else {
                    this.idMarcaDeLLanta = idMarcaDeLLanta;
                    this.nombreMarcaDeLLantas = null;
                    this.activoMarcaDeLLantas = 0;
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

    public CMarcasDeLLantas(Inicio ini, String nombreMarcaDeLLantas) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            sql = "SELECT * "
                    + "FROM marcasdellantas "
                    + "where nombreMarcaDeLlanta='" + nombreMarcaDeLLantas
                    + "' and activo=1;";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idMarcaDeLLanta = rst.getInt(1);
                    this.nombreMarcaDeLLantas = rst.getString(2);
                    this.activoMarcaDeLLantas = rst.getInt(3);;
                } else {
                    this.idMarcaDeLLanta = 0;
                    this.nombreMarcaDeLLantas = null;
                    this.activoMarcaDeLLantas = 0;
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

    public boolean grabarMarcaDeLLantass() {
        boolean grabado = false;
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;
        try {

            String sql = "INSERT INTO marcasdellantas("
                    + "nombreMarcaDeLlanta,activo,usuario,flag) VALUES('"
                    + this.nombreMarcaDeLLantas + "',"
                    + this.activoMarcaDeLLantas + ",'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreMarcaDeLlanta='" + this.nombreMarcaDeLLantas + "',"
                    + "activo=" + this.activoMarcaDeLLantas + ","
                    + "flag=-1";
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

    public boolean actualizarMarcaDeLLantass() throws SQLException {
        boolean grabado = false;

        String sql = "UPDATE  marcasdellantas SET "
                + "nombreMarcaDeLlanta='" + this.nombreMarcaDeLLantas + "',"
                + "activo=" + this.activoMarcaDeLLantas + ","
                + "flag=-1 WHERE "
                + "idMarcaDeLlanta=" + this.idMarcaDeLLanta + ";";
       
        grabado = ini.insertarDatosRemotamente(sql);

        return grabado;
    }

    public String arrListadoDeMarcasDeLLantas() {

        String sql = "SELECT idMarcaDeLlanta, nombreMarcaDeLlanta, activo, fechaIng, usuario, flag "
                + "FROM marcasdellantas"
                + " order by nombreMarcaDeLlanta;";

        return sql;
    }

}
