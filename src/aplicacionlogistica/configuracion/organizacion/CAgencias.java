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
public class CAgencias extends CRegionales {

    int idAgencia;
    int ciudad;
    String nombreAgencia;
    int activoAgencia;
    

    
    public int getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(int idAgencia) {
        this.idAgencia = idAgencia;
    }

    
    public int getCiudad() {
        return ciudad;
    }

    public void setCiudad(int ciudad) {
        this.ciudad = ciudad;
    }

    public String getNombreAgencia() {
        return nombreAgencia;
    }

    public void setNombreAgencia(String nombreAgencia) {
        this.nombreAgencia = nombreAgencia;
    }

    public int getActivoAgencia() {
        return activoAgencia;
    }

    public void setActivoAgencia(int activoAgencia) {
        this.activoAgencia = activoAgencia;
    }

    
    public CAgencias() {
    }

    public CAgencias(Inicio ini) throws Exception {
       this.ini=ini;
    }
    
    public CAgencias(Inicio ini, int regional, int agencia) throws Exception {
      super(ini);
       Connection con;
        Statement st;
        ResultSet rst;
        ;
        try {
          //con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
   con= this.ini.getConnRemota();
            
     //idagencias, idRegional, idZona, nombreAgencia, idCiudad, activo, fechaIng, usuario, flag
    //idRegional, idZona, nombreRegional, activo, fechaIng, usuario, flag
    // idZona, nombreZona, activo, fechaIng, usuario, flag
    String sql="SELECT agencias.idagencias,agencias.idRegional, agencias.idZona,"
            + " agencias.nombreAgencia,agencias.idCiudad, agencias.activo, agencias.fechaIng, agencias.usuario, agencias.flag,"
            +"regionales.nombreRegional,"
            +"zonas.nombreZona "
            + "FROM agencias,regionales,zonas  "
            +"WHERE "
            +"agencias.idRegional=regionales.idRegional AND "
            +"regionales.idZona=zonas.idZona AND"
            +" agencias.idRegional=" + regional +" AND "
            + "agencias.idagencias=" + agencia + " AND  "
            + "agencias.activo=1 ORDER BY agencias.nombreAgencia ASC";
     if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                    this.idAgencia=rst.getInt("idagencias");
                    this.idRegional=rst.getInt("idRegional");
                    this.idZona=rst.getInt("idZona");
                    this.nombreAgencia=rst.getString("nombreAgencia");
                    this.ciudad=rst.getInt("idCiudad");
                    this.nombreRegional=rst.getString("nombreRegional");
                    this.nombreZona=rst.getString("nombreZona");
                    this.activoAgencia=rst.getInt("activo");
                   
                
            } else {
                this.idAgencia=0;
                    this.idRegional=0;
                    this.idZona=0;
                    this.nombreAgencia=null;
                    this.ciudad=0;
                    this.nombreRegional=null;
                    this.nombreZona=null;
                    this.activoAgencia=0;
                   
            }
            rst.close();
            st.close();
           //con.close();
     }
        } catch (SQLException ex) {
             System.out.println("Error en consultar agencias consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CAgencias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CAgencias(Inicio ini, int agencia) throws Exception {
      super(ini);
       Connection con;
        Statement st;
       
        ResultSet rst;
       
        try {
             // con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
              con= this.ini.getConnRemota();
          
             String sql="SELECT agencias.idagencias,agencias.idRegional, agencias.idZona, "
                     + "agencias.nombreAgencia,agencias.idCiudad, agencias.activo, "
                     + "agencias.fechaIng, agencias.usuario, agencias.flag,"
            +"regionales.nombreRegional,"
            +"zonas.nombreZona "
            + "FROM agencias,regionales,zonas  "
            +"WHERE "
              +"agencias.idRegional=regionales.idRegional AND "
            +"regionales.idZona=zonas.idZona AND "
            + "agencias.idagencias=" + agencia + " AND  "
            + "agencias.activo=1 ORDER BY agencias.nombreAgencia ASC";     
                   
             if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                    this.idAgencia=rst.getInt("idagencias");
                    this.idRegional=rst.getInt("idRegional");
                    this.idZona=rst.getInt("idZona");
                    this.nombreAgencia=rst.getString("nombreAgencia");
                    this.ciudad=rst.getInt("idCiudad");
                    this.nombreRegional=rst.getString("nombreRegional");
                    this.nombreZona=rst.getString("nombreZona");
                    this.activoAgencia=rst.getInt("activo");
                    
              
                
            } else {
                this.idAgencia=0;
                    this.idRegional=0;
                    this.idZona=0;
                    this.nombreAgencia=null;
                    this.ciudad=0;
                    this.nombreRegional=null;
                    this.nombreZona=null;
                    this.activoAgencia=0;
                   
            }
           
            rst.close();
            st.close();
            //con.close();
             }
        } catch (SQLException ex) {
             System.out.println("Error en consultar agencias consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CAgencias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
      public CAgencias(Inicio ini, String  nombreAgencia) throws Exception {
      super(ini);
       Connection con;
        Statement st;
       
        ResultSet rst;
         try {
                 //con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 con= this.ini.getConnRemota();
         
            
             String sql = "SELECT agencias.idagencias,agencias.idRegional, agencias.idZona, "
                     + "agencias.nombreAgencia,agencias.idCiudad, agencias.activo, "
                     + "agencias.fechaIng, agencias.usuario, agencias.flag,"
                     + "regionales.nombreRegional,"
                     + "zonas.nombreZona "
                     + "FROM agencias,regionales,zonas  "
                     + "WHERE "
                     + "agencias.idRegional=regionales.idRegional AND "
                     + "regionales.idZona=zonas.idZona AND "
                     + "agencias.nombreAgencia='" + nombreAgencia + "' AND  "
                     + "agencias.activo=1 ORDER BY agencias.nombreAgencia ASC"; 
         
             if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                    this.idAgencia=rst.getInt("idagencias");
                    this.idRegional=rst.getInt("idRegional");
                    this.idZona=rst.getInt("idZona");
                    this.nombreAgencia=rst.getString("nombreAgencia");
                    this.ciudad=rst.getInt("idCiudad");
                    this.nombreRegional=rst.getString("nombreRegional");
                    this.nombreZona=rst.getString("nombreZona");
                    this.activoAgencia=rst.getInt("activo");
                  
                
            } else {
                this.idAgencia=0;
                    this.idRegional=0;
                    this.idZona=0;
                    this.nombreAgencia=null;
                    this.ciudad=0;
                    this.nombreRegional=null;
                    this.nombreZona=null;
                    this.activoAgencia=0;
                  
            }
           
            rst.close();
            st.close();
            //con.close();
             }
           
        } catch (SQLException ex) {
             System.out.println("Error en consultar agencias consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CAgencias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public int getIdAgencia(String nombreAgencia) {
        int id = 0;
        ResultSet rst = null;
        Statement st = null;

        Connection con;
        try {

            //con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con= this.ini.getConnLocal();
            
            String sql = "SELECT idagencias FROM `agencias` where nombreAgencia='" + nombreAgencia
                    + " AND  "
                    + "' activo=1;";
            
            if (con != null) {
                
            rst = st.executeQuery(sql);
            
            if (rst.next()) {
                id = rst.getInt(1);
            } else {
                id = 0;
            }
            }
            st.close();
            rst.close();
            //con.close();
        } catch (SQLException ex) {
            System.out.println("Error en consultar agencias consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CAgencias.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public boolean grabarAgencias() throws SQLException {
        boolean grabado = false;
        try {
            
            String sql = "INSERT INTO `agencias`(`idRegional`,`idZona`,"
                    + "`nombreAgencia`,`idCiudad`,`activo`,`usuario`,`aliado`) VALUES("
                    + this.idRegional + ","
                    + this.idZona + ","
                    + this.nombreAgencia + "',"
                    + this.ciudad + "',"
                    + this.activoAgencia + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "idagencias=" + this.idAgencia + ","
                    + "idRegional=" + this.idRegional + ","
                    + "idZona=" + this.idZona + ","
                    + "nombreAgencia='" + this.nombreAgencia + "',"
                    + "activo=" + this.activoAgencia + ","
                    + "flag=1";
            grabado = ini.insertarDatosRemotamente(sql);
            
        } catch (Exception ex) {
            Logger.getLogger(CAgencias.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

         
  
public String arrListdoDeAgencias(){
    //idagencias, idRegional, idZona, nombreAgencia, idCiudad, activo, fechaIng, usuario, flag
    //idRegional, idZona, nombreRegional, activo, fechaIng, usuario, flag
    // idZona, nombreZona, activo, fechaIng, usuario, flag
    String sql="SELECT agencias.idagencias,agencias.idRegional, agencias.idZona,"
            + " agencias.nombreAgencia,agencias.idCiudad, agencias.activo, "
            + "agencias.fechaIng, agencias.usuario, agencias.flag,"
            +"regionales.nombreRegional,"
            +"zonas.nombreZona "
            + "FROM agencias,regionales,zonas  "
            +"WHERE "
            +"agencias.idRegional=regionales.idRegional AND "
            +"regionales.idZona=zonas.idZona "
            + "ORDER BY agencias.nombreAgencia ASC";
    return sql;
}
   
}
