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
public class CProductosPorMinuta { //extends Inicio {

 
    String codigoProducto;
    String descripcionProducto;
    double Cantidad;
    double peso ;
    double valor;
    Inicio ini;

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    
    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double Cantidad) {
        this.Cantidad = Cantidad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
    
    public CProductosPorMinuta() {
    }

    public CProductosPorMinuta(Inicio ini) throws Exception {
    this.ini=ini;
    }

    public CProductosPorMinuta(Inicio ini, int idCanal) throws Exception {
     
         this.ini=ini;
        
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        
        //con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
                     con = this.ini.getConnRemota();

 
        
            sql = "SELECT * "
                    + "FROM tiposcanaldeventas "
                    + "where idCanalDeVenta=" + idCanal + "' and  "
                    + "aliado='" + ini.getIdAliado() + "' and "
                    + " and activo=1;";
         
         if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            if (rst.next()) {
//                this.idCanal = rst.getInt("idCanal");
//                this.nombreCanal = rst.getString("nombreCanal");
//                this.activoCanal=rst.getInt("activo");
//                this.aliado=rst.getInt("aliado");
            } else {
//                this.idCanal = 0;
//                this.nombreCanal = null;
//                this.activoCanal=0;
//                 this.aliado=0;
            }
            rst.close();
            st.close();
           // con.close();
         }
            
            
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposCanalDeVentas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CProductosPorMinuta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CProductosPorMinuta(Inicio ini, String nombreCanal) throws Exception {
       this.ini=ini;
        try {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        
        // con = CConexiones.GetConnection(ini.getCadenaRemota(),ini.getUsuarioBDRemota(),ini.getClaveBDRemota());
                     con = this.ini.getConnRemota();

  
        sql = "SELECT * "
                + "FROM tiposcanaldeventas "
                + "where nombreCanalDeVenta='" + nombreCanal +"' and  "
                + "aliado='" + ini.getIdAliado() + "' and "
                + " activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
         if (rst.next()) {
//                this.idCanal = rst.getInt("idCanal");
//                this.nombreCanal = rst.getString("nombreCanal");
//                this.activoCanal=rst.getInt("activo");
//                this.aliado=rst.getInt("aliado");
            } else {
//                this.idCanal = 0;
//                this.nombreCanal = null;
//                this.activoCanal=0;
//                 this.aliado=0;
            }
            rst.close();
            st.close();
            // con.close();
        }
        
            
           
            
        } catch (SQLException ex) {
             System.out.println("Error en consultar tiposCanalDeVentas consulta sql " + ex.getMessage().toString());
            Logger.getLogger(CProductosPorMinuta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    


}
