/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion.organizacion;

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
public class CZonas {//extends Inicio {

    int idZona;
    String nombreZona;
    int activoZona;
    Inicio ini;

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public int getActivoZona() {
        return activoZona;
    }

    public void setActivoZona(int activoZona) {
        this.activoZona = activoZona;
    }

    public int getIdZona(String nombreZona) {
        int id = 0;
         String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

         
             con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                st = con.createStatement();

                sql = "SELECT idZona FROM `zonas` where nombreZona='" + nombreZona
                        + "' and activo=1 "
                        + "and aliado='" + ini.getIdAliado() + "';";
                
               rst = st.executeQuery(sql);
               
                if (rst.next()) {
                    id = rst.getInt("idZona");
                } else {
                    id = 0;
                }
                
                rst.close();
                st.close();
                con.close();

            }    
           
            
        } catch (SQLException ex) {
            System.out.println("Error en consultar regionales consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CZonas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

   
    public CZonas() {
    }

    public CZonas(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public CZonas(Inicio ini, int idZona) throws Exception {
        this.ini = ini;
        Connection con;
        Statement st;
        ResultSet rst;
        try {

           con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           String sql = "SELECT idZona, nombreZona, activo, fechaIng, usuario, flag "
                    + "FROM zonas  "
                    + "WHERE "
                    + "zonas.idzona=" + idZona + " AND "
                    + "zonas.activo=1  ORDER BY zonas.nombreZona ASC";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idZona = rst.getInt("idZona");
                    this.nombreZona = rst.getString("nombreZona");
                    this.activoZona = rst.getInt("activo");
                   
                } else {
                    this.idZona = 0;
                    this.nombreZona = null;
                    this.activoZona = 0;
                    
                }
                rst.close();
                st.close();
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error en consultar regionales consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CZonas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CZonas(Inicio ini, String nombreZona) throws Exception {
        this.ini = ini;
        Connection con;
        Statement st;
        ResultSet rst;
        try {
          
            con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
            
           String sql = "SELECT idZona, nombreZona, activo, fechaIng, usuario, flag "
                    + "FROM zonas  "
                    + "WHERE "
                    + "zonas.nombreZona=" + nombreZona + " AND "
                    + "zonas.activo=1  ORDER BY zonas.nombreZona ASC";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idZona = rst.getInt("idZona");
                    this.nombreZona = rst.getString("nombreZona");
                    this.activoZona = rst.getInt("activo");
                   
                } else {
                    this.idZona = 0;
                    this.nombreZona = null;
                    this.activoZona = 0;
                    
                }
                rst.close();
                st.close();
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error en consultar regionales consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CZonas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarZonas() throws SQLException {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO `zonas`(`idZona`,"
                    + "`nombreZona`,`activo`,`usuario`) VALUES("
                    + this.idZona + ",'"
                    + this.nombreZona + "',"
                    + this.activoZona + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreZona='" + this.nombreZona + "',"
                    + "activo=" + this.activoZona + ","
                    + "flag=1";
            grabado = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CZonas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }


    public String arrListadoDeZonas() {

        // idZona, nombreZona, activo, fechaIng, usuario, flag
        String sql = "SELECT idZona, nombreZona, activo, fechaIng, usuario, flag "
                + "FROM zonas;  ";
                
        return sql;
    }

   
}
