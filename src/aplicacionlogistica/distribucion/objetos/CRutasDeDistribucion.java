/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

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
public class CRutasDeDistribucion {//extends Inicio {
// idRuta, nombreDeRuta, agencia, activo, fechaIng, usuario, flag, tipoRuta

    int idRutasDeDistribucion;
    String nombreDeRuta;
    int agencia;
    int activoRutasDeDistribucion;
    String fechaIng;
    String usuario;
    int flag;
    String tipoRuta;
    Inicio ini;

    public String getNombreDeRuta() {
        return nombreDeRuta;
    }

    public void setNombreDeRuta(String nombreDeRuta) {
        this.nombreDeRuta = nombreDeRuta;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
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

    public int getIdRutasDeDistribucion() {
        return idRutasDeDistribucion;
    }

    public void setIdRutasDeDistribucion(int idRutasDeDistribucion) {
        this.idRutasDeDistribucion = idRutasDeDistribucion;
    }

    public String getNombreRutasDeDistribucion() {
        return nombreDeRuta;
    }

    public void setNombreRutasDeDistribucion(String nombreRutasDeDistribucion) {
        this.nombreDeRuta = nombreRutasDeDistribucion;
    }

    public int getActivoRutasDeDistribucion() {
        return activoRutasDeDistribucion;
    }

    public void setActivoRutasDeDistribucion(int activoRutasDeDistribucion) {
        this.activoRutasDeDistribucion = activoRutasDeDistribucion;
    }

    public int getIdRutasDeDistribucion(String nombreRutasDeDistribucion) {
        int id = 0;

        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = ini.getConnRemota();

        sql = "SELECT idRuta FROM rutasdedistribucion where nombreDeRuta='" + nombreRutasDeDistribucion + "' and activo=1;";
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
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CRutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return id;
    }

    public String getTipoRuta() {
        return tipoRuta;
    }

    public void setTipoRuta(String tipoRutaado) {
        this.tipoRuta = tipoRutaado;
    }

    public CRutasDeDistribucion() {
    }

    public CRutasDeDistribucion(Inicio ini) throws Exception {
        this.ini = ini;
    }

    public CRutasDeDistribucion(Inicio ini, int idRutasDeDistribucion) throws Exception {
        this.ini = ini;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        this.idRutasDeDistribucion = idRutasDeDistribucion;

        try {

            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            con = this.ini.getConnRemota();
            sql = "SELECT * "
                    + "FROM rutasdedistribucion "
                    + "where idRuta=" + idRutasDeDistribucion + "' and "
                    // + "aliado='" + ini.getIdAliado() + "' and "
                    + " activo=1;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idRutasDeDistribucion = rst.getInt("idRuta");
                    this.nombreDeRuta = rst.getString("nombreDeRuta");
                    this.activoRutasDeDistribucion = rst.getInt("activo");
                    this.tipoRuta = rst.getString("tipoRuta");
                } else {
                    this.idRutasDeDistribucion = 0;
                    this.nombreDeRuta = null;
                    this.activoRutasDeDistribucion = 0;
                    this.tipoRuta = null;
                }
                rst.close();
                st.close();
                //con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar uutasDeDistribucion consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CRutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CRutasDeDistribucion(Inicio ini, String nombreRutasDeDistribucion) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            con = this.ini.getConnRemota();

            sql = "SELECT * "
                    + "FROM rutasdedistribucion "
                    + "where nombreDeRuta='" + nombreRutasDeDistribucion + "' and "
                    + "' and activo=1;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idRutasDeDistribucion = rst.getInt("idRuta");
                    this.nombreDeRuta = rst.getString("nombreDeRuta");
                    this.activoRutasDeDistribucion = rst.getInt("activo");
                    this.tipoRuta = rst.getString("tipoRuta");
                } else {
                    this.idRutasDeDistribucion = 0;
                    this.nombreDeRuta = null;
                    this.activoRutasDeDistribucion = 0;
                    this.tipoRuta = null;
                }
                rst.close();
                st.close();
                //con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error en consultar rutasDeDistribucion consulta sql " + ex.getMessage());
            Logger.getLogger(CRutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarRutasDeDistribucions() {
        boolean grabado = false;
        String sql = null;
        try {

            sql = "INSERT INTO rutasdedistribucion("
                    + "nombreDeRuta,agencia,activo,usuario,fechaIng,flag,tipoRuta) VALUES('"
                    + this.nombreDeRuta + "','"
                    + ini.getUser().getAgencia() + "','"
                    + this.activoRutasDeDistribucion + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',CURRENT_TIMESTAMP,'-1','"
                    + this.tipoRuta + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "nombreDeRuta='" + this.nombreDeRuta + "',"
                    + "activo=" + this.activoRutasDeDistribucion + ","
                    + "flag=-1 ,"
                    + "tipoRuta='" + this.tipoRuta + "';";

            grabado = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CRutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public boolean actualizarRutasDeDistribucions(int caso) throws SQLException {
        boolean grabado = false;

        String sql = "UPDATE  rutasdedistribucion SET "
                + "nombreDeRuta='" + this.nombreDeRuta + "',"
                + "activo=" + this.activoRutasDeDistribucion + ","
                + "flag=-1 "
                + "WHERE "
                // + "aliado='" + ini.getIdAliado() + "' and "
                + "idRuta=" + this.idRutasDeDistribucion + ";";

        grabado = ini.insertarDatosRemotamente(sql);

        return grabado;
    }

    public boolean actualizarRutasDeDistribucions() throws SQLException {
        boolean grabado = false; // idRuta, nombreDeRuta, agencia, activo, fechaIng, usuario, flag, tipoRuta
        try {

            String sql = "UPDATE  rutasdedistribucion SET "
                    + "nombreDeRuta='" + this.nombreDeRuta + "',"
                    + "agencia='" + ini.getUser().getAgencia() + "',"
                    + "activo='" + this.activoRutasDeDistribucion + "',"
                    + "usuario='" + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',"
                    + "flag='-1', "
                    + "tipoRuta='" + this.tipoRuta + "' "
                    + "WHERE "
                    //  + "aliado='" + ini.getIdAliado() + "' and "
                    + "idRuta=" + this.idRutasDeDistribucion + ";";

            grabado = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CRutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public String arrListadoDeRutasDeDistribucion() {

        String sql = "SELECT * "
                + "FROM rutasdedistribucion ;";

        return sql;
    }

}
