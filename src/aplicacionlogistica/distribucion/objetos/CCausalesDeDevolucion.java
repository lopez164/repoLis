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
public class CCausalesDeDevolucion { //extends Inicio {
    int idcausalesDeDEvolucion;
    String nombreCausalDeDevolucion;
    int activoCausalesDeDevolucion;
    int caso;
    String usuario;
    Inicio ini;

    public int getIdCausalesDeDevolucion() {
        return idcausalesDeDEvolucion;
    }

    public void setIdCausalesDeDevolucion(int idCausalesDeRechazol) {
        this.idcausalesDeDEvolucion = idCausalesDeRechazol;
    }

    public String getNombreCausalesDeDevolucion() {
        return nombreCausalDeDevolucion;
    }

    public void setNombreCausalesDeDEvolucion(String nombreCausalesDeRechazol) {
        this.nombreCausalDeDevolucion = nombreCausalesDeRechazol;
    }

    public int getActivoCausalesDeDevolucion() {
        return activoCausalesDeDevolucion;
    }

    public void setActivoCausalesDeDEvolucion(int activoCausalesDeRechazol) {
        this.activoCausalesDeDevolucion = activoCausalesDeRechazol;
    }

    public int getIdCausalesDeDevolucion(String nombreCausalesDeRechazol) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        
         con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());

        // , , activo, fechaIng, usuario, flag
         sql = "SELECT idcausalesderechazo "
                 + "FROM "
                 + "causalesderechazo "
                 + "WHERE nombreCausalDeRechazo='" + nombreCausalesDeRechazol + "' and activo=1;";
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
                Logger.getLogger(CCausalesDeDevolucion.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CCausalesDeDevolucion() {
    }

    public CCausalesDeDevolucion(Inicio ini) throws Exception {
    this.ini=ini;
    }

    public CCausalesDeDevolucion(Inicio ini, int idCausalesDeRechazol) throws Exception {
     
         this.ini=ini;
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
       
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());

        this.idcausalesDeDEvolucion = idCausalesDeRechazol;
         sql = "SELECT idcausalesderechazo, nombreCausalDeRechazo, activo, usuario, flag "
                 + "FROM causalesderechazo "
                 + "WHERE idcausalesderechazo=" + idCausalesDeRechazol
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idcausalesDeDEvolucion = rst.getInt("idcausalesderechazo");
                this.nombreCausalDeDevolucion = rst.getString("nombreCausalDeRechazo");
                this.activoCausalesDeDevolucion=rst.getInt("activo");;
            } else {
                this.idcausalesDeDEvolucion = 0;
                this.nombreCausalDeDevolucion = null;
                this.activoCausalesDeDevolucion=0;
            }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposCausalesDeRechazolDeVentas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CCausalesDeDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CCausalesDeDevolucion(Inicio ini, String nombreCausalesDeRechazol) throws Exception {
       this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
        sql = "SELECT idcausalesderechazo, nombreCausalDeRechazo, activo, usuario, flag "
                + "FROM causalesderechazo "
                + "WHERE nombreCausalDeRechazo='" + nombreCausalesDeRechazol
                    + "' AND activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
          if (rst.next()) {
                this.idcausalesDeDEvolucion = rst.getInt("idcausalesderechazo");
                this.nombreCausalDeDevolucion = rst.getString("nombreCausalDeRechazo");
                this.activoCausalesDeDevolucion=rst.getInt("activo");;
            } else {
                this.idcausalesDeDEvolucion = 0;
                this.nombreCausalDeDevolucion = null;
                this.activoCausalesDeDevolucion=0;
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposCausalesDeRechazolDeVentas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CCausalesDeDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarCausalesDeRechazols(int caso) {
        boolean grabado = false;
        try {
           
            this.caso=caso;
            String sql = "INSERT INTO causalesderechazo("
                    + " nombreCausalDeRechazo, activo,  usuario, flag) VALUES('"
                    + this.nombreCausalDeDevolucion + "',"
                    + this.activoCausalesDeDevolucion + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreCausalDeRechazo='" + this.nombreCausalDeDevolucion + "',"
                    + "activo=" + this.activoCausalesDeDevolucion + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);
            if (grabado) {
                Connection con;
                Statement st;
                ResultSet rst;
                con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
              
                sql = "SELECT causalesderechazo, nombreCausalDeRechazo, activo, usuario, flag "
                        + "FROM causalesderechazo "
                        + "WHERE nombreCausalDeRechazo='" + this.nombreCausalDeDevolucion
                        + "' and activo=1;";
                if (con != null) {
                    try {
                        st = con.createStatement();
                        rst = st.executeQuery(sql);
                        if (rst.next()) {
                           this.idcausalesDeDEvolucion = rst.getInt("idcausalesderechazo");
                this.nombreCausalDeDevolucion = rst.getString("nombreCausalDeRechazo");
                this.activoCausalesDeDevolucion=rst.getInt("activo");
                        } else {
                            this.idcausalesDeDEvolucion = 0;
                            this.nombreCausalDeDevolucion = null;
                            this.activoCausalesDeDevolucion = 0;
                        }
                        rst.close();
                        st.close();
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(CCausalesDeDevolucion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            
        } catch (Exception ex) {
            Logger.getLogger(CCausalesDeDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
      public boolean grabarCausalDeDevolcuion() {
        boolean grabado = false;
        try {
           
            String sql = "INSERT INTO causalesderechazo("
                    + " nombreCausalDeRechazo, activo,  usuario, flag) VALUES('"
                    + this.nombreCausalDeDevolucion + "',"
                    + this.activoCausalesDeDevolucion + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',-1"
                    + ") ON DUPLICATE KEY UPDATE "
                    + "nombreCausalDeRechazo='" + this.nombreCausalDeDevolucion + "',"
                    + "activo=" + this.activoCausalesDeDevolucion + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);

            
        } catch (Exception ex) {
            Logger.getLogger(CCausalesDeDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
    public boolean actualizarCausalDeDevolucion(int caso) throws SQLException {
        boolean grabado = false;
        this.caso=caso;
        String sql = "UPDATE  causalesderechazo SET "
                + "nombreCausalDeRechazo='" + this.nombreCausalDeDevolucion + "',"
                + "activo=" + this.activoCausalesDeDevolucion + ","
                + "flag=-1 WHERE "
                + "idcausalesderechazo=" + this.idcausalesDeDEvolucion + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }

    
     public boolean actualizarCausalDeDevolucion() throws SQLException {
        boolean grabado = false;
        
        String sql = "UPDATE  causalesderechazo SET "
                + "nombreCausalDeRechazo='" + this.nombreCausalDeDevolucion + "',"
                + "activo=" + this.activoCausalesDeDevolucion + ","
                + "flag=-1 WHERE "
                + "idcausalesderechazo=" + this.idcausalesDeDEvolucion + ";" ;
        grabado = ini.insertarDatosRemotamente(sql);
      
        return grabado;
    }

   
    public String arrListadoDeCCausalesDeRechazo() {

        // idcausalesDeDEvolucion, nombreCausalDeDevolucion, activo, fechaIng, usuario, flag
        String sql = "SELECT * "
                + "FROM causalesderechazo  "
                + "ORDER BY nombreCausalDeRechazo ASC";
        return sql;
    }

   
}
