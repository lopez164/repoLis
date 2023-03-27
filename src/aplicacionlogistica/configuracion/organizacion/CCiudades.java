/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion.organizacion;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class CCiudades extends CDepartamentos {

    int idCiudades = 0;
    int idDepartamentos = 0;
    String NombreCiudad = null;
    int activoCiudad = 0;
    private boolean grabarCiudades;

    public CCiudades() {

    }

    public CCiudades(Inicio ini) throws Exception {
        super(ini);
    }

    public CCiudades(Inicio ini, int ciudad) throws Exception {
        super(ini);

        String sql;
        Statement st;
        ResultSet rst;
        Connection con;

        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "select * from ciudades where idciudades=" + ciudad + " and activo=1 order by nombreCiudad asc;";

                rst = st.executeQuery(sql);
                if (rst != null) {
                    while (rst.next()) {
                        this.idCiudades = rst.getInt(1);
                        this.idDepartamentos = rst.getInt(2);
                        this.NombreCiudad = rst.getString(3);
                        this.activoCiudad = rst.getInt(4);
                    }
                    rst.close();
                    st.close();

                }

                st = con.createStatement();
                sql = "SELECT * FROM departamentos where iddepartamento=" + this.idDepartamentos + ";";
                rst = st.executeQuery(sql);
                if (rst != null) {
                    while (rst.next()) {
                        this.idDepartamentos = rst.getInt(1);
                        this.nombreDepartamento = rst.getString(3);
                        this.activoDepartamento = rst.getInt(4);
                    }
                    rst.close();
                    st.close();
                    con.close();

                }
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar ciudades consulta sql " + ex.getMessage().toString());
            //JOptionPane.showMessageDialog(null, ex.getMessage().toString(), "No Existe", 1);
        } catch (Exception ex) {
            System.out.println("Error en consultar ciudades consulta sql " + ex.getMessage().toString());
            // JOptionPane.showMessageDialog(null, "No existe la ciudad No existe", "No Existe", 1);
        }

    }

    public CCiudades(Inicio ini, String nombreCiudad) throws Exception {
        super(ini);
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;

        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "select * from ciudades where nombreCiudad='" + nombreCiudad + "' and activo=1 order by nombreCiudad asc;";

                rst = st.executeQuery(sql);

                if (rst.next()) {
                    this.idCiudades = rst.getInt(1);
                    this.idDepartamentos = rst.getInt(2);
                    this.NombreCiudad = rst.getString(3);
                    this.activoCiudad = rst.getInt(4);

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (Exception ex) {
            System.out.println("Error en consultar ciudades consulta sql " + ex.getMessage());
            //JOptionPane.showMessageDialog(null, "No existe la ciudad No existe", "No Existe", 1);
        }

    }

    public String getNombreCiudad() {
        return NombreCiudad;
    }

    public void setNombreCiudad(String NombreCiudad) {
        this.NombreCiudad = NombreCiudad;
    }

    public int getIdCiudad() {
        return idCiudades;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudades = idCiudad;
    }

    public int getIdDepartamento() {
        return idDepartamentos;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamentos = idDepartamento;
    }

    public int getActivoCiudad() {
        return activoCiudad;
    }

    public void setActivoCiudad(int activoCiudad) {
        this.activoCiudad = activoCiudad;
    }

    public ResultSet ciudades_() {
        ResultSet rst = null;
        ResultSet ciu = null;
        String sql;

        sql = "select * from ciudades where activo=1 order by nombreCiudad asc;";
        //rst = ini.getResultset(sql);

        return rst;

    }

    public void ciudad(int ciudad) {
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;

        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "select * from ciudades where idciudades=" + ciudad + " and activo=1 order by nombreCiudad asc;";
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idCiudades = rst.getInt(1);
                    this.idDepartamentos = rst.getInt(2);
                    this.NombreCiudad = rst.getString(3);

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (Exception ex) {
            System.out.println("Error en consultar ciudades consulta sql " + ex.getMessage().toString());
            //JOptionPane.showMessageDialog(null, "No existe la ciudad No existe", "No Existe", 1);
        }

    }

    public boolean grabarCiudades() throws SQLException {
        grabarCiudades = false;
        try {

            String sql = null;
            ResultSet rst = null;
            sql = "INSERT INTO `ciudades` (`idciudades`, `idDepartamento`, `nombreCiudad`, `activo`, `usuario`, `flag`)"
                    + " VALUES ("
                    + this.idCiudades + ","
                    + this.idDepartamentos + ","
                    + this.NombreCiudad + ",1,'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',0) ON DUPLICATE KEY UPDATE "
                    + "idDepartamento=" + this.idDepartamentos + ","
                    + "nombreCiudad='" + this.NombreCiudad + "',"
                    + "activo=" + this.activoCiudad + ","
                    + "flag=1";
            grabarCiudades = ini.insertarDatosLocalmente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CCiudades.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabarCiudades;
    }

    public String rstListadoDeCiudades() {

        // idciudades, idDepartamento, nombreCiudad, activo, fechaIng, usuario, flag
        String sql = "SELECT  ciudades.idciudades,ciudades. idDepartamento, departamentos.nombreDepartamento,"
                + "ciudades.nombreCiudad, ciudades.activo, ciudades.fechaIng, ciudades.usuario, ciudades.flag "
                + "FROM ciudades, departamentos "
                + "WHERE "
                + "ciudades.idDepartamento=departamentos.idDepartamento "
                + "and ciudades.activo = 1 "
                + "ORDER BY ciudades.nombreCiudad asc;";

        return sql;
    }

}
