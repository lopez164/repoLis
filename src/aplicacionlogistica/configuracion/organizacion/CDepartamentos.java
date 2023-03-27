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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author VLI_488
 */
public class CDepartamentos {//extends Inicio {

    int idDepartamento;
    String nombreDepartamento;
    int activoDepartamento;
    List<CCiudades> listaDeCiudades ;
    Inicio ini;

    public List<CCiudades> getListaDeCiudades() {
        return listaDeCiudades;
    }

    public void setListaDeCiudades(List<CCiudades> ciudades) {
        this.listaDeCiudades = ciudades;
    }
    
     public void setListaDeCiudades() {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;
        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeCiudades");

            CCiudades ciu = new CCiudades(ini);
            listaDeCiudades = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                
                String sql = "SELECT  c.idciudades,c. idDepartamento, d.nombreDepartamento,"
                       + "c.nombreCiudad, c.activo, c.fechaIng, c.usuario, c.flag "
                       + "FROM ciudades c "
                       + "join departamentos d on d.idDepartamento=c.idDepartamento "
                       + "where c.idDepartamento='" + this.idDepartamento + "' "
                       + "ORDER BY c.nombreCiudad asc;";
                
                 rst = st.executeQuery(sql);
                while (rst.next()) {
                    ciu = new CCiudades(ini);
                    ciu.setIdCiudad(rst.getInt("idciudades"));
                    ciu.setIdDepartamento(rst.getInt("idDepartamento"));
                    ciu.setNombreDepartamento(rst.getString("nombreDepartamento"));
                    ciu.setNombreCiudad(rst.getString("nombreCiudad"));
                    ciu.setActivoCiudad(rst.getInt("activo"));
                    System.out.println("Cargando Ciudades -> " + ciu.getNombreCiudad());

                    listaDeCiudades.add(ciu);
                   Thread.sleep(1);
                }
                if(listaDeCiudades.size()==0){
                    listaDeCiudades = null;
                }
                rst.close();
                st.close();
                con.close();

               
            }
        } 
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

            } catch (SQLException ex) {
                Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public CDepartamentos() {
    }

    public CDepartamentos(Inicio ini) throws Exception {
        this.ini = ini;

    }

    public CDepartamentos(Inicio ini, int idDepartamento) throws Exception {
        this.ini = ini;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "SELECT * FROM departamentos where idDepartamento=" + idDepartamento
                        + " and activo=1;";
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idDepartamento = rst.getInt(1);
                    this.nombreDepartamento = rst.getString(3);
                    this.activoDepartamento = rst.getInt(4);
                } else {
                    this.idDepartamento = 0;
                    this.nombreDepartamento = null;
                    this.activoDepartamento = 0;
                }
                rst.close();
                st.close();
                con.close();

            }

        } catch (SQLException ex) {
            Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CDepartamentos(Inicio ini, String nombreDepartamento) throws Exception {
        this.ini = ini;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();

                sql = "SELECT * FROM departamentos where nombreDepartamento='" + nombreDepartamento
                        + "' and activo=1;";
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.idDepartamento = rst.getInt(1);
                    this.nombreDepartamento = rst.getString(3);
                    this.activoDepartamento = rst.getInt(4);
                } else {
                    this.idDepartamento = 0;
                    this.nombreDepartamento = null;
                    this.activoDepartamento = 0;
                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public int getActivoDepartamento() {
        return activoDepartamento;
    }

    public void setActivoDepartamento(int activoDepartamento) {
        this.activoDepartamento = activoDepartamento;
    }

    public int getIdDepartamento(String nombreDepartamento) {
        int id = 0;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();

                sql = "SELECT idDepartamento FROM departamentos where nombreDepartamento='" + nombreDepartamento
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
            Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public boolean grabarDepartamentos() throws SQLException {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO departamentos(idDepartamento,"
                    + "nombreDepartamento,activo,usuario,) VALUES("
                    + this.idDepartamento + ",'"
                    + this.nombreDepartamento + "',"
                    + this.activoDepartamento + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreDepartamento='" + this.nombreDepartamento + "',"
                    + "activo=" + this.activoDepartamento + ","
                    + "flag=1";
            grabado = ini.insertarDatosLocalmente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public String rstListadoDeDpartamentos() {

        String sql = "SELECT idDepartamento, idPais, nombreDepartamento, activo, fechaIng, usuario, flag "
                + "FROM departamentos where activo=1;";
               

        return sql;
    }

}
