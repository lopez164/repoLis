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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CMovimientosManifiestosfacturas { //extends Inicio {

    int idtipoDeMovimiento;
    String nombreTipoDeMovimiento;
    int activoMovimiento;
    String usuario;
    Inicio ini;

    public int getIdMovimientosManifiestosfacturas() {
        return idtipoDeMovimiento;
    }

    public void setIdMovimientosManifiestosfacturas(int idtipoDeMovimiento) {
        this.idtipoDeMovimiento = idtipoDeMovimiento;
    }

    public String getNombreMovimientosManifiestosfacturas() {
        return nombreTipoDeMovimiento;
    }

    public void setNombreMovimientosManifiestosfacturas(String nombreMovimientosManifiestosfacturas) {
        this.nombreTipoDeMovimiento = nombreMovimientosManifiestosfacturas;
    }

    public int getActivoMovimientosManifiestosfacturas() {
        return activoMovimiento;
    }

    public void setActivoMovimientosManifiestosfacturas(int activoMovimientosManifiestosfacturas) {
        this.activoMovimiento = activoMovimientosManifiestosfacturas;
    }

    public int getIdMovimientosManifiestosfacturas(String nombreMovimientosManifiestosfacturas) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
         sql = "SELECT idtipoDeMovimiento "
                 + "FROM tiposdemovimientosmanifiestosfacturas "
                 + "WHERE nombreTipoDeMovimiento='" + nombreMovimientosManifiestosfacturas + "' AND  activo=1;";
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
                Logger.getLogger(CMovimientosManifiestosfacturas.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CMovimientosManifiestosfacturas() {
    }

    public CMovimientosManifiestosfacturas(Inicio ini) throws Exception {
    this.ini=ini;
    }

    public CMovimientosManifiestosfacturas(Inicio ini, int idtipoDeMovimiento) throws Exception {
     
         this.ini=ini;
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        this.idtipoDeMovimiento = idtipoDeMovimiento;
         sql = "SELECT  idtipoDeMovimiento, nombreTipoDeMovimiento, activo, fechaIng, usuario, flag "
                 + "FROM tiposdemovimientosmanifiestosfacturas "
                 + "WHERE idtipoDeMovimiento=" + idtipoDeMovimiento
                 + " AND activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idtipoDeMovimiento = rst.getInt(1);
                this.nombreTipoDeMovimiento = rst.getString(2);
                this.activoMovimiento=rst.getInt(3);;
            } else {
                this.idtipoDeMovimiento = 0;
                this.nombreTipoDeMovimiento = null;
                this.activoMovimiento=0;
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposDeMovimientosManifiestosfacturas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CMovimientosManifiestosfacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CMovimientosManifiestosfacturas(Inicio ini, String nombreMovimientosManifiestosfacturas) throws Exception {
       this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        sql = "SELECT * "
                + "FROM tiposdemovimientosmanifiestosfacturas "
                + "WHERE nombreMovimientosManifiestosfacturasDeVenta='" + nombreMovimientosManifiestosfacturas
                + "' AND activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idtipoDeMovimiento = rst.getInt(1);
                this.nombreTipoDeMovimiento = rst.getString(2);
                this.activoMovimiento=rst.getInt(3);;
            } else {
                this.idtipoDeMovimiento = 0;
                this.nombreTipoDeMovimiento = null;
                this.activoMovimiento=0;
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposDeMovimientosManifiestosfacturas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CMovimientosManifiestosfacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarMovimientosManifiestosfacturass() {
        boolean grabado = false;
        try {
           
          
            String sql = "INSERT INTO tiposdemovimientosmanifiestosfacturas("
                    + "nombreTipoDeMovimiento,activo,usuario,flag) VALUES('"
                    + this.nombreTipoDeMovimiento + "',"
                    + this.activoMovimiento + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreTipoDeMovimiento='" + this.nombreTipoDeMovimiento + "',"
                    + "activo=" + this.activoMovimiento + ","
                    + "flag=-1";
            grabado = ini.insertarDatosLocalmente(sql);
            if (grabado) {
                Connection con;
                Statement st;
                ResultSet rst;
                con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                sql = "SELECT * "
                        + "FROM tiposdemovimientosmanifiestosfacturas "
                        + "WHERE nombreTipoDeMovimiento='" + this.nombreTipoDeMovimiento
                        + "' AND  activo=1;";
                if (con != null) {
                    try {
                        st = con.createStatement();
                        rst = st.executeQuery(sql);
                        if (rst.next()) {
                            this.idtipoDeMovimiento = rst.getInt(1);
                            this.nombreTipoDeMovimiento = rst.getString(2);
                            this.activoMovimiento = rst.getInt(3);;
                        } else {
                            this.idtipoDeMovimiento = 0;
                            this.nombreTipoDeMovimiento = null;
                            this.activoMovimiento = 0;
                        }
                        rst.close();
                        st.close();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(CMovimientosManifiestosfacturas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            
        } catch (Exception ex) {
            Logger.getLogger(CMovimientosManifiestosfacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarMovimientosManifiestosfacturass() {
        boolean grabado = false;
        try {
           
            
            String sql = "UPDATE  tiposdemovimientosmanifiestosfacturas SET "
                    + "nombreTipoDeMovimiento='" + this.nombreTipoDeMovimiento + "',"
                    + "activo=" + this.activoMovimiento + ","
                    + "flag=-1 WHERE "
                    + "idtipoDeMovimiento=" + this.idtipoDeMovimiento + ";" ;
            grabado = ini.insertarDatosLocalmente(sql);
            
           
        } catch (SQLException ex) {
            Logger.getLogger(CMovimientosManifiestosfacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
         return grabado;
    }


    public ArrayList<IdMovimientosManifiestosfacturases> listadoDeMovimientosManifiestosfacturass() {
        ArrayList<IdMovimientosManifiestosfacturases> lista = new ArrayList();
        try {

            Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
       sql = "SELECT idtipoDeMovimiento "
               + "FROM tiposDeMovimientosManifiestosfacturas "
               + "WHERE activo=1";
         
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
             while (rst.next()) {
                IdMovimientosManifiestosfacturases canal = new IdMovimientosManifiestosfacturases(rst.getInt(1));
                lista.add(canal);
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
           
            
        } catch (SQLException ex) {
            System.out.println("Error en consultar tiposDeMovimientosManifiestosfacturas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CMovimientosManifiestosfacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
     public String arrListadoDeCMovimientosManifiestosfacturas(){
        //  idtipoDeMovimiento, nombreTipoDeMovimiento, activo, fechaIng, usuario, flag
         String sql= "SELECT idtipoDeMovimiento, nombreTipoDeMovimiento, activo, fechaIng, usuario, flag "
                 + "FROM tiposdemovimientosmanifiestosfacturas "
                 + "where activo=1;";
         return sql;
    
}
    public static class IdMovimientosManifiestosfacturases {

        int movimiento;

        public int getMovimientosManifiestosfacturas() {
            return movimiento;
        }

        public IdMovimientosManifiestosfacturases(int movimiento) {
            this.movimiento = movimiento;
        }
    }
}
