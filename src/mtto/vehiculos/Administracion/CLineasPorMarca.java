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
public class CLineasPorMarca  {

int idlineasVehiculos;
int idMarcaDeVehiculo;
String nombreMarcaDeVehiculos;
String nombreLineaVehiculo;
Inicio ini;
int activoLinea;

    public int getIdMarcaDeVehiculo() {
        return idMarcaDeVehiculo;
    }

    public void setIdMarcaDeVehiculo(int idMarcaDeVehiculo) {
        this.idMarcaDeVehiculo = idMarcaDeVehiculo;
    }

    public String getNombreMarcaDeVehiculos() {
        return nombreMarcaDeVehiculos;
    }

    public void setNombreMarcaDeVehiculos(String nombreMarcaDeVehiculos) {
        this.nombreMarcaDeVehiculos = nombreMarcaDeVehiculos;
    }



    public int getIdlineasVehiculos() {
        return idlineasVehiculos;
    }

    public void setIdlineasVehiculos(int idlineasVehiculos) {
        this.idlineasVehiculos = idlineasVehiculos;
    }

   

    public String getNombreLineaVehiculo() {
        return nombreLineaVehiculo;
    }

    public void setNombreLineaVehiculo(String nombreLineaVehiculo) {
        this.nombreLineaVehiculo = nombreLineaVehiculo;
    }

    public int getActivoLinea() {
        return activoLinea;
    }

    public void setActivoLinea(int activoLinea) {
        this.activoLinea = activoLinea;
    }

    public CLineasPorMarca() {
    }

    public CLineasPorMarca(Inicio ini) throws Exception {
        this.ini= ini;
    }

     public CLineasPorMarca(Inicio ini,int idlineasVehiculos) throws Exception {
       
       try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql="SELECT marcasdevehiculos.idMarcaDeVehiculo, marcasdevehiculos.nombreMarcaDeVehiculo,"
                + "lineasvehiculos.idlineasVehiculos, lineasvehiculos.nombreLineaVehiculo "
                + "FROM marcasdevehiculos,lineasvehiculos "
                + "where "
                + "marcasdevehiculos.idMarcaDeVehiculo=lineasvehiculos.marcaVehiculo and "
                + "lineasvehiculos.idlineasVehiculos=" + idlineasVehiculos 
                + " and lineasvehiculos.activo=1;";
             if (con != null) {
                 st = con.createStatement();
                 rst = st.executeQuery(sql);
                     if (rst.next()) {
                     this.idMarcaDeVehiculo=rst.getInt(1);
                     this.nombreMarcaDeVehiculos=rst.getString(2);
                     this.idlineasVehiculos = rst.getInt(3);
                     this.nombreLineaVehiculo = rst.getString(4);

                 } else {
                      this.idlineasVehiculos = 0;    
                    
                 }
                 rst.close();
                 st.close();
                 con.close();
             }
       
    } catch (SQLException ex) {
        Logger.getLogger(CLineasPorMarca.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
     
      public CLineasPorMarca(Inicio ini, String nombreLinea) throws Exception {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT marcasdevehiculos.idMarcaDeVehiculo, marcasdevehiculos.nombreMarcaDeVehiculo,"
                + "lineasvehiculos.idlineasVehiculos, lineasvehiculos.nombreLineaVehiculo,lineasvehiculos.activo"
                + " FROM marcasdevehiculos,lineasvehiculos "
                + "where "
                + "marcasdevehiculos.idMarcaDeVehiculo=lineasvehiculos.marcaVehiculo and "
                + "lineasvehiculos.nombreLineaVehiculo='" + nombreLinea + "' and "
                + "lineasvehiculos.activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            try {
                if (rst.next()) {
                    this.idMarcaDeVehiculo = rst.getInt(1);
                    this.nombreMarcaDeVehiculos = rst.getString(2);
                    this.idlineasVehiculos = rst.getInt(3);
                    this.nombreLineaVehiculo = rst.getString(4);
                    this.activoLinea=rst.getInt(5);
                    

                } else {
                   this.idlineasVehiculos = 0;
                    
                }
                rst.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CLineasPorMarca.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public CLineasPorMarca(Inicio ini, int idlineasVehiculos, int idmarcasDeVehiculos) throws Exception {
    
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT marcasdevehiculos.idMarcaDeVehiculo, marcasdevehiculos.nombreMarcaDeVehiculo,"
                + "lineasvehiculos.idlineasVehiculos, lineasvehiculos.nombreLineaVehiculo,lineasvehiculos.activo"
                + " FROM marcasdevehiculos,lineasvehiculos "
                + "where "
                + "marcasdevehiculos.idMarcaDeVehiculo=lineasvehiculos.marcaVehiculo and "
                + "lineasvehiculos.idlineasVehiculos=" + idlineasVehiculos + " and "
                + "marcasdevehiculos.idMarcaDeVehiculo=" + idmarcasDeVehiculos + " and "
                + "lineasvehiculos.activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            try {
                if (rst.next()) {
                    this.idMarcaDeVehiculo = rst.getInt(1);
                    this.nombreMarcaDeVehiculos = rst.getString(2);
                    this.idlineasVehiculos = rst.getInt(3);
                    this.nombreLineaVehiculo = rst.getString(4);
                    this.activoLinea=rst.getInt(5);
                    

                } else {
                     this.idlineasVehiculos = 0;
                    
                }
                rst.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CLineasPorMarca.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     
    public boolean grabarLineassVehiculos() throws SQLException {
    boolean grabado = false;
        try {
        
        
        String sql = "INSERT INTO lineasvehiculos(marcaVehiculo,"
                + "nombreLineaVehiculo,activo,usuario,flag) VALUES('"
                + this.idMarcaDeVehiculo + "','"
                + this.nombreLineaVehiculo + "','"
                + this.activoLinea + "','"
                + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','1')"
                + " ON DUPLICATE KEY UPDATE "
                + "marcaVehiculo='" + this.idMarcaDeVehiculo + "',"
                + "nombreLineaVehiculo='" + this.nombreLineaVehiculo + "',"
                + "activo='" + this.activoLinea + "',"
                + "flag='1'";
        
        grabado=ini.insertarDatosRemotamente(sql);
        
        
        
        
    } catch (Exception ex) {
        Logger.getLogger(CLineasPorMarca.class.getName()).log(Level.SEVERE, null, ex);
    }
     return grabado;   
    }
    
     public boolean actualizarLineassVehiculos(int linea) throws SQLException {
        boolean grabado = false;
        
         String sql = "Update lineasvehiculos set "
                 + "nombreLineaVehiculo=" + this.nombreLineaVehiculo + "',"
                 + "idmarcasDeVehiculos=" + this.idMarcaDeVehiculo + ","
                 + "activo=" + this.activoLinea + ","
                 + "flag=1 where idlineasVehiculos=" + linea;

         grabado = ini.insertarDatosRemotamente(sql);
         return grabado;
    }
    
      public boolean actualizarLineassVehiculos() throws SQLException {
        boolean grabado = false;
        
         String sql = "Update lineasvehiculos set "
                 + "nombreLineaVehiculo='" + this.nombreLineaVehiculo + "',"
                 + "marcaVehiculo='" + this.idMarcaDeVehiculo + "',"
                 + "activo='" + this.activoLinea + "',"
                 + "flag=1 where idlineasVehiculos='" + this.idlineasVehiculos + "';";

         grabado = ini.insertarDatosRemotamente(sql);
         return grabado;
    }
 
      public String arrListadoDeLineasPorMarca() {
       
             
       String sql = "SELECT  marcasdevehiculos.idMarcaDeVehiculo, marcasdevehiculos.nombreMarcaDeVehiculo,"
               + "lineasvehiculos.idlineasVehiculos, lineasvehiculos.nombreLineaVehiculo, lineasvehiculos.rendimientoCombustible,lineasvehiculos.activo "
                + "FROM marcasdevehiculos, lineasvehiculos "
                + "WHERE "
                + "marcasdevehiculos.idMarcaDeVehiculo = lineasvehiculos.marcaVehiculo "
               + " ORDER BY lineasvehiculos.nombreLineaVehiculo asc;";
     
        return sql;
    }
    
   


}
