/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.objetos;

import aplicacionlogistica.distribucion.objetos.*;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CTiposDePeticion {//extends Inicio {

    int idtiposDePeticones;
    String nombreTipoDePeticion;
    int activo;
   
    Inicio ini;

    public int getIdTiposDePeticones() {
        return idtiposDePeticones;
    }

    public void setIdTiposDePeticones(int idRutasDeDistribucion) {
        this.idtiposDePeticones = idRutasDeDistribucion;
    }

    public String getNombreTiposDePeticones() {
        return nombreTipoDePeticion;
    }

    public void setNombreTiposDePeticones(String tiposDePeticones) {
        this.nombreTipoDePeticion = tiposDePeticones;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getIdTiposDePeticones(String nombreTipoDePeticion) {
        int id = 0;

        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT idtiposDePeticones FROM tiposDePeticones where nombreDeRuta='" + nombreTipoDePeticion + "' and activo=1;";
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
                Logger.getLogger(CTiposDePeticion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return id;
    }

   

    public CTiposDePeticion() {
    }

    public CTiposDePeticion(Inicio ini) throws Exception {
        this.ini = ini;
    }

    public CTiposDePeticion(Inicio ini, int idtiposDePeticones) throws Exception {
        this.ini = ini;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        this.idtiposDePeticones = idtiposDePeticones;

        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "SELECT * "
                    + "FROM tiposDePeticones "
                    + "where idtiposDePeticones=" + idtiposDePeticones + "' and "
                    + " activo=1;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idtiposDePeticones = rst.getInt("idtiposDePeticones");
                    this.nombreTipoDePeticion = rst.getString("nombreTipoDePeticion");
                    this.activo = rst.getInt("activo");
                    
                } else {
                    this.idtiposDePeticones = 0;
                    this.nombreTipoDePeticion = null;
                    this.activo = 0;
                    
                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar uutasDeDistribucion consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDePeticion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CTiposDePeticion(Inicio ini, String nombreTipoDePeticion) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

                        
            sql = "SELECT * "
                    + "FROM tiposDePeticones "
                    + "where nombreTipoDePeticion='" + nombreTipoDePeticion + "' and "
                    + "' and activo=1;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idtiposDePeticones = rst.getInt("v");
                    this.nombreTipoDePeticion = rst.getString("nombreTipoDePeticion");
                    this.activo = rst.getInt("activo");
                   
                } else {
                    this.idtiposDePeticones = 0;
                    this.nombreTipoDePeticion = null;
                    this.activo = 0;
                   
                }
                rst.close();
                st.close();
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error en consultar uutasDeDistribucion consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CTiposDePeticion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarTiposDePeticones() {
        boolean grabado = false;
        String sql = null;
        try {
            /*Registro nuevo*/
            if (this.idtiposDePeticones == 0) {
                sql = "INSERT INTO tiposDePeticones("
                        + "nombreTipoDePeticion,activo,usuario,flag) VALUES('"
                        + this.nombreTipoDePeticion + "',"
                        + this.activo + ",'"
                        + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',-1) "   
                        + " ON DUPLICATE KEY UPDATE "
                        + "nombreTipoDePeticion='" + this.nombreTipoDePeticion + "',"
                        + "activo=" + this.activo + ","
                        + "flag=-1;";
                      

                Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("No se pudo guardar");
                }

                ResultSet rst = ps.getGeneratedKeys();
                if (rst.next()) {
                    this.idtiposDePeticones = rst.getInt(1);
                }
                rst.close();
                ps.close();
                con.close();
                
            } else {
                /*Registro para actualizar*/
                sql = "INSERT INTO tiposDePeticones(idtiposDePeticones,"
                        + "nombreTipoDePeticion,activo,usuario,flag) VALUES('"
                        + this.idtiposDePeticones + "',"
                        + this.nombreTipoDePeticion + "',"
                        + this.activo + ",'"
                        + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',-1,)"
                        + " ON DUPLICATE KEY UPDATE "
                        + "idtiposDePeticones='" + this.idtiposDePeticones + "',"
                        + "nombreTipoDePeticion='" + this.nombreTipoDePeticion + "',"
                        + "activo=" + this.activo + ","
                        + "flag=-1 );"
                      ;
                grabado = ini.insertarDatosRemotamente(sql);
            }

           

        } catch (Exception ex) {
            Logger.getLogger(CTiposDePeticion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }


    public String arrListadoDeTiposDePeticones() {

        String sql = "SELECT * "
                + "FROM tiposDePeticones where activo=1;";

        return sql;
    }

}

 