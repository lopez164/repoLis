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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CCanalesDeVenta { //extends Inicio {

    int idCanalDeVenta;
    String nombreCanal;
    int activoCanal;
    int caso;
    String usuario;
    Inicio ini;

    public int getIdCanalDeVenta() {
        return idCanalDeVenta;
    }

    public void setIdCanalDeVenta(int idCanal) {
        this.idCanalDeVenta = idCanal;
    }

    public String getNombreCanalDeVenta() {
        return nombreCanal;
    }

    public void setNombreCanalDeVenta(String nombreCanal) {
        this.nombreCanal = nombreCanal;
    }

    public int getActivoCanal() {
        return activoCanal;
    }

    public void setActivoCanal(int activoCanal) {
        this.activoCanal = activoCanal;
    }

   
    
    public int getIdCanalDeVenta(String nombreCanal) {
        int id = 0;
        
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
  
        sql = "SELECT idCanalDeVenta "
                + "FROM tiposcanaldeventas "
                + "where nombreCanal='" + nombreCanal + "' and "
              // + "aliado='" + ini.getIdAliado() + "' and "
                + " activo=1;";
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
                Logger.getLogger(CCanalesDeVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        return id;
    }

    
    public CCanalesDeVenta() {
    }

    public CCanalesDeVenta(Inicio ini) throws Exception {
    this.ini=ini;
    }

    public CCanalesDeVenta(Inicio ini, int idCanal) throws Exception {
     
         this.ini=ini;
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
 
        this.idCanalDeVenta = idCanal;
            sql = "SELECT * "
                    + "FROM tiposcanaldeventas "
                    + "where idCanalDeVenta=" + idCanal + "' and  "
                   // + "aliado='" + ini.getIdAliado() + "' and "
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
                this.idCanalDeVenta = rst.getInt("idCanal");
                this.nombreCanal = rst.getString("nombreCanal");
                this.activoCanal=rst.getInt("activo");
               
            } else {
                this.idCanalDeVenta = 0;
                
                   }
            rst.close();
            st.close();
            con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposCanalDeVentas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CCanalesDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public CCanalesDeVenta(Inicio ini, String nombreCanal) throws Exception {
       this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
  
        sql = "SELECT * "
                + "FROM tiposcanaldeventas "
                + "where nombreCanalDeVenta='" + nombreCanal +"' and  "
              //  + "aliado='" + ini.getIdAliado() + "' and "
                + " activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
                this.idCanalDeVenta = rst.getInt("idCanal");
                this.nombreCanal = rst.getString("nombreCanal");
                this.activoCanal=rst.getInt("activo");
               
            } else {
                this.idCanalDeVenta = 0;
                
            }
            rst.close();
            st.close();
            con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposCanalDeVentas consulta sql " + ex.getMessage());
            Logger.getLogger(CCanalesDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabarCanals(int caso) {
        boolean grabado = false;
        try {
           
            this.caso=caso;
            String sql = "INSERT INTO tiposcanaldeventas("
                    + "nombreCanalDeVenta,activo,usuario,flag) VALUES('"
                    + this.nombreCanal + "',"
                    + this.activoCanal + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',-1,') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "nombreCanal='" + this.nombreCanal + "',"
                    + "activo=" + this.activoCanal + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);
            if (grabado) {
                Connection con;
                Statement st;
                ResultSet rst;
                
                con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
                
                sql = "SELECT * "
                        + "FROM tiposcanaldeventas "
                        + "where "
                        + "nombreCanalDeVenta='" + this.nombreCanal + "' and "
                       // + "aliado='" + ini.getIdAliado() + "' and "
                        + " activo=1;";
                if (con != null) {
                    try {
                        st = con.createStatement();
                        rst = st.executeQuery(sql);
                        if (rst.next()) {
                            this.idCanalDeVenta = rst.getInt("idCanal");
                            this.nombreCanal = rst.getString("nombreCanal");
                            this.activoCanal = rst.getInt("activo");
                            
                        } else {
                            this.idCanalDeVenta = 0;
                           
                        }
                        rst.close();
                        st.close();
                        con.close();
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(CCanalesDeVenta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            
        } catch (Exception ex) {
            Logger.getLogger(CCanalesDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
     public boolean grabarCanals() {
        boolean grabado = false;
        try {
           
         
            String sql = "INSERT INTO tiposcanaldeventas("
                    + "nombreCanalDeVenta,activo,usuario,flag) VALUES('"
                    + this.nombreCanal + "',"
                    + this.activoCanal + ",'"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','-1') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "nombreCanalDeVenta='" + this.nombreCanal + "',"
                    + "activo=" + this.activoCanal + ","
                    + "flag=-1";
            grabado = ini.insertarDatosRemotamente(sql);

            
        } catch (Exception ex) {
            Logger.getLogger(CCanalesDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
    
     public boolean actualizarCanals() throws SQLException {
        boolean grabado = false;
         String sql = "UPDATE  tiposcanaldeventas SET "
                + "nombreCanalDeVenta='" + this.nombreCanal + "',"
                + "activo='" + this.activoCanal + "',"
                + "flag='-1' WHERE "
                + "idCanalDeVenta='" + this.idCanalDeVenta + "';" ;
        
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }
    
    public boolean actualizarCanals(int caso) throws SQLException {
        boolean grabado = false;
        this.caso=caso;
        String sql = "UPDATE  tiposcanaldeventas SET "
                + "nombreCanalDeVenta='" + this.nombreCanal + "',"
                + "activo=" + this.activoCanal + ","
                + "flag=-1 WHERE "
                + "idCanalDeVenta='" + this.idCanalDeVenta + "'; ";
               // + "aliado='" + ini.getIdAliado() + "'; " ;
        
        grabado = ini.insertarDatosRemotamente(sql);
       
        return grabado;
    }


//    public ArrayList<IdCanales> listadoDeCanals() {
//        ArrayList<IdCanales> lista = new ArrayList();
//        try {
//
//            Connection con;
//        Statement st;
//        String sql;
//        ResultSet rst;
//        con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
//       sql = "select idCanalDeVenta"
//               + " from tiposcanaldeventas "
//               + "where "
//               + "activo=1 and "
//               + "aliado='" + ini.getIdAliado() + "';  ";
//          
//         if (con != null) {
//            st = con.createStatement();
//            rst = st.executeQuery(sql);
//             while (rst.next()) {
//                IdCanales canal = new IdCanales(rst.getInt(1));
//                lista.add(canal);
//            }
//            rst.close();
//            st.close();
//            con.close();
//         }
//            
//            
//           
//            
//        } catch (SQLException ex) {
//            System.out.println("Error en consultar tiposCanalDeVentas consulta sql " + ex.getMessage().toString());
//            Logger.getLogger(CCanalesDeVenta.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return lista;
//    }
//    
     public String arrListadoDeCanaleDeVenta(){

        // idcausalesDeRechazo, nombreCausalDeRechazo, activo, fechaIng, usuario, flag
        String sql = "SELECT idCanalDeVenta, nombreCanalDeVenta, activo, fechaIng, usuario, flag "
                + "FROM tiposcanaldeventas ;";
        return sql;
    }

}
