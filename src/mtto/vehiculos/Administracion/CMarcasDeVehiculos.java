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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CMarcasDeVehiculos {//extends Inicio {

    int idMarcaDeVehiculo;
    String nombreMarcaDeVehiculos;
    int activoMarcaDeVehiculos;
    List<CLineasPorMarca> listaDeLineasDelVehiculo;

    Inicio ini;

    public int getIdMarcaDeVehiculo() {
        return idMarcaDeVehiculo;
    }

    public void setIdMarcaDeVehiculo(int idMarcaDeVehiculo) {
        this.idMarcaDeVehiculo = idMarcaDeVehiculo;
    }

    public List<CLineasPorMarca> getListaDeLineasDelVehiculo() {
        return listaDeLineasDelVehiculo;
    }

    public void setListaDeLineasDelVehiculo() {
        ResultSet rst = null;
        Statement st = null;
        String sql = null;

        Connection con = null;
        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            CLineasPorMarca lineaVeh = new CLineasPorMarca(ini);
            listaDeLineasDelVehiculo = new ArrayList();
            sql = "SELECT l.marcaVehiculo, l.idlineasVehiculos, m.nombreMarcaDeVehiculo,l.nombreLineaVehiculo, l.activo "
                    + "FROM lineasvehiculos l "
                    + "join marcasdevehiculos m on m.idMarcaDeVehiculo=l.marcaVehiculo "
                    + "where l.marcaVehiculo='" + idMarcaDeVehiculo + "' "
                    + " order by l.idlineasVehiculos;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());

                    lineaVeh = new CLineasPorMarca(ini);
                    lineaVeh.setIdMarcaDeVehiculo(rst.getInt("marcaVehiculo"));
                    lineaVeh.setIdlineasVehiculos(rst.getInt("idlineasVehiculos"));
                    lineaVeh.setNombreMarcaDeVehiculos(rst.getString("nombreMarcaDeVehiculo"));
                    lineaVeh.setNombreLineaVehiculo(rst.getString("nombreLineaVehiculo"));
                    lineaVeh.setActivoLinea(rst.getInt("activo"));
                    System.out.println("Cargando Lineas por Marca de vehiculo -> " + lineaVeh.getNombreLineaVehiculo());

                    listaDeLineasDelVehiculo.add(lineaVeh);

                    System.out.println("tiempo 2 " + new Date());
                    Thread.sleep(1);
                }
                rst.close();
                st.close();
                con.close();

            }
        } // fin try // fin try // fin try // fin try
         catch (Exception ex) {
            Logger.getLogger(CMarcasDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setListaDeLineasDelVehiculo(List<CLineasPorMarca> listaDeLineasDelVehiculo) {
        this.listaDeLineasDelVehiculo = listaDeLineasDelVehiculo;
    }

    public int getIdMarcaDeVehiculos() {
        return idMarcaDeVehiculo;
    }

    public void setIdMarcaDeVehiculos(int idMarcaDeVehiculo) {
        this.idMarcaDeVehiculo = idMarcaDeVehiculo;
    }

    public String getNombreMarcaDeVehiculos() {
        return nombreMarcaDeVehiculos;
    }

    public void setNombreMarcaDeVehiculos(String nombreMarcaDeVehiculos) {
        this.nombreMarcaDeVehiculos = nombreMarcaDeVehiculos;
    }

    public int getActivoMarcaDeVehiculos() {
        return activoMarcaDeVehiculos;
    }

    public void setActivoMarcaDeVehiculos(int activoMarcaDeVehiculos) {
        this.activoMarcaDeVehiculos = activoMarcaDeVehiculos;
    }

    public int getIdMarcaDeVehiculos(String nombreMarcaDeVehiculos) {
        int id = 0;

        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT idMarcaDeVehiculo "
                + "FROM marcasdevehiculos "
                + "where nombreMarcaDeVehiculo='" + nombreMarcaDeVehiculos + "' and activo=1;";
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
                Logger.getLogger(CMarcasDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return id;
    }

    public CMarcasDeVehiculos() {
    }

    public CMarcasDeVehiculos(Inicio ini) throws Exception {
        this.ini = ini;
        this.idMarcaDeVehiculo = 0;
        this.nombreMarcaDeVehiculos = "";

    }

    public CMarcasDeVehiculos(Inicio ini, int idMarcaDeVehiculo) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "SELECT * "
                    + "FROM marcasdevehiculos "
                    + "where idMarcaDeVehiculo=" + idMarcaDeVehiculo
                    + " and activo=1;";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idMarcaDeVehiculo = rst.getInt(1);
                    this.nombreMarcaDeVehiculos = rst.getString(2);
                    this.activoMarcaDeVehiculos = rst.getInt(3);
                } else {
                    this.idMarcaDeVehiculo = idMarcaDeVehiculo;
                    this.nombreMarcaDeVehiculos = null;
                    this.activoMarcaDeVehiculos = 0;
                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println("Error en consultar marcasdevehiculos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CMarcasDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CMarcasDeVehiculos(Inicio ini, String nombreMarcaDeVehiculos) throws Exception {
        this.ini = ini;
        try {
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            sql = "SELECT * "
                    + "FROM marcasdevehiculos "
                    + "where nombreMarcaDeVehiculo='" + nombreMarcaDeVehiculos
                    + "' and activo=1;";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idMarcaDeVehiculo = rst.getInt(1);
                    this.nombreMarcaDeVehiculos = rst.getString(2);
                    this.activoMarcaDeVehiculos = rst.getInt(3);;
                } else {
                    this.idMarcaDeVehiculo = 0;
                   
                }
                rst.close();
                st.close();
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error en consultar marcasdevehiculos consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CMarcasDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabarMarcaDeVehiculoss() {
      boolean grabado= false;
        try {
            
            
            String sql = "INSERT INTO marcasdevehiculos("
                    + "nombreMarcaDeVehiculo,activo,usuario,flag) VALUES('"
                    + this.nombreMarcaDeVehiculos + "',"
                    + this.activoMarcaDeVehiculos + ",'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreMarcaDeVehiculo='" + this.nombreMarcaDeVehiculos + "',"
                    + "activo=" + this.activoMarcaDeVehiculos + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);
            
            
            
           
        } catch (Exception ex) {
            Logger.getLogger(CMarcasDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
         return grabado;
    }

    public boolean actualizarMarcaDeVehiculoss() throws SQLException {
        boolean grabado = false;

        String sql = "UPDATE  marcasdevehiculos SET "
                + "nombreMarcaDeVehiculo='" + this.nombreMarcaDeVehiculos + "',"
                + "activo=" + this.activoMarcaDeVehiculos + ","
                + "flag=-1 WHERE "
                + "idMarcaDeVehiculo=" + this.idMarcaDeVehiculo + ";";
        grabado = ini.insertarDatosRemotamente(sql);

        return grabado;
    }

    public String arrListadoDeMarcasDeVehiculos() {

        String sql = "SELECT idMarcaDeVehiculo, nombreMarcaDeVehiculo, activo, fechaIng, usuario, flag "
                + "FROM marcasdevehiculos"
                + " order by nombreMarcaDeVehiculo;";

        return sql;
    }

}
