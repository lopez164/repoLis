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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CRegionales extends CZonas {//extends CZonas {

    int idRegional;
    String nombreRegional;
    int activoRegional;
    Inicio ini;
    
public int getIdRegional() {
        return idRegional;
    }

    public void setIdRegional(int idRegional) {
        this.idRegional = idRegional;
    }

    public String getNombreRegional() {
        return nombreRegional;
    }

    public void setNombreRegional(String nombreRegional) {
        this.nombreRegional = nombreRegional;
    }

    public int getActivoRegional() {
        return activoRegional;
    }

    public void setActivoRegional(int activoRegional) {
        this.activoRegional = activoRegional;
    }

    public int getIdRegional(String nombreRegional) {
        int id = 0;
          String sql;
        Statement st;
        ResultSet rst;
        Connection  con;
        try {

             con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
           
             if (con != null) {
                 st = con.createStatement();
                  sql = "SELECT idRegional FROM `regionales` where nombreRegional='" + nombreRegional
                    + "' and activo=1 "
                    + "and aliado='" + ini.getIdAliado() + "';";
           rst = st.executeQuery(sql);
           
            if (rst.next()) {
                id = rst.getInt("idRegional");
            } else {
                id = 0;
            }
            rst.close();
            st.close();
            con.close();
             }
            
          
        } catch (SQLException ex) {
            System.out.println("Error en consultar regionales consulta sql " + ex.getMessage());
            Logger.getLogger(CRegionales.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    
    public CRegionales() {
    }

    public CRegionales(Inicio ini) throws Exception {
        this.ini=ini;

    }

    public CRegionales(Inicio ini, int idRegional) throws Exception {
       this.ini=ini;
        Connection con;
        Statement st;
        ResultSet rst;
        try {

           con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
             //idRegional, idZona, nombreRegional, activo, fechaIng, usuario, flag
            // idZona, nombreZona, activo, fechaIng, usuario, flag
           String sql="SELECT regionales.idRegional, regionales.idZona, regionales.nombreRegional,"
                   + " regionales.activo,"
            +"zonas.nombreZona "
            + "FROM regionales,zonas  "
            +"WHERE "
            +"regionales.idZona=zonas.idZona AND "
            +"regionales.idRegional= '" + idRegional + "' AND "
            + "regionales.activo=1 "
            + "ORDER BY regionales.nombreRegional ASC";
            
           if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idRegional = rst.getInt("idRegional");
                this.idZona=rst.getInt("idZona");
                this.nombreZona=rst.getString("nombreZona");
                this.nombreRegional = rst.getString("nombreRegional");
                this.nombreZona=rst.getString("nombreZona");
                this.activoRegional = rst.getInt("activo");
                
            } else {
               this.idRegional = 0;
               this.idZona=0;
               this.nombreRegional = null;
                this.nombreZona=null;
               this.activoRegional = 0; 
              
            }
            rst.close();
            st.close();
            con.close();
            }
            //con.close();
        } catch (SQLException ex) {
            System.out.println("Error en consultar regionales consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CRegionales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CRegionales(Inicio ini, String nombreRegional) throws Exception {
        super(ini);
        Connection con;
        Statement st;
        ResultSet rst;
        try {
          con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());

       
             String sql="SELECT idRegional, idZona, nombreRegional, activo, fechaIng, usuario, flag,"
            +"zonas.nombreZona, "
            + "FROM regionales,zonas  "
            +"WHERE "
            +"regionales.idZona=zonas.idZona AND "
            +"regionales.nombreRegional=" + nombreRegional + "AND "
            + "regionales.activo=1 "
           + "ORDER BY regionales.nombreRegional ASC";
            
           if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idRegional = rst.getInt("idRegional");
                this.idZona=rst.getInt("idZona");
                this.nombreRegional = rst.getString("nombreRegional");
                this.nombreZona=rst.getString("nombreZona");
                this.activoRegional = rst.getInt("activo"); 
               
            } else {
               this.idRegional = 0;
               this.idZona=0;
               this.nombreRegional = null;
                this.nombreZona=null;
               this.activoRegional = 0; 
              
            }
            rst.close();
            st.close();
            con.close();
           }
          
            
        } catch (SQLException ex) {
            System.out.println("Error en consultar regionales consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CRegionales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarRegionales() throws SQLException {
         boolean grabado = false;
        try {
           
            String sql = "INSERT INTO `regionales`("
                    + "`nombreRegional`,`activo`,`usuario`, `aliado`) VALUES("
                    + this.nombreRegional + "','"
                    + this.activoRegional + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario())+ "')"
                    + " ON DUPLICATE KEY UPDATE"
                    + "nombreRegional='" + this.nombreRegional + "',"
                    + "activo=" + this.activoRegional + ","
                    + "flag=1";
            grabado = ini.insertarDatosRemotamente(sql);
            
        } catch (Exception ex) {
            Logger.getLogger(CRegionales.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

  
    
  
public String arrListadoDeRegionales(){
   
    //idRegional, idZona, nombreRegional, activo, fechaIng, usuario, flag
    // idZona, nombreZona, activo, fechaIng, usuario, flag
    String sql="SELECT regionales.idRegional, regionales.idZona, regionales.nombreRegional, regionales.activo, regionales.fechaIng, regionales.usuario, regionales.flag,"
            +"zonas.nombreZona "
            + "FROM regionales,zonas  "
            +"WHERE "
            +"regionales.idZona=zonas.idZona ;";
            
    return sql;
}
    
}
