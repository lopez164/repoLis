/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author root
 */
public class CFacturasParaAnular{
    String numeroFactura;
    String causalDeRechazo;
    Inicio ini;

    public CFacturasParaAnular() {

    }
   
    
    public CFacturasParaAnular(Inicio ini) {
        this.ini = ini;
    }

    public CFacturasParaAnular(String numeroFactura, Inicio ini) {
        this.numeroFactura = numeroFactura;
        this.ini = ini;
        
        String sql = "SELECT * "
                + "FROM facturasparaanular "
                + "WHERE "
                + "numeroFactura='" + numeroFactura + "';";
        
        traerDatosBBDD(sql);
    }
    
    
    private void traerDatosBBDD(String sql){
        
        Connection con;
        Statement st;
        ResultSet rst;
        
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        if (con != null) {
            
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                
                if (rst.next()) {
                    this.numeroFactura=(rst.getString("numeroFactura"));
                    this.causalDeRechazo=(rst.getString("causalDeRechazo"));
                    
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(CControladorReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
       
    }
   
    
    public String getCadenaConLosCampos(){
      String  cadena = 
                 this.numeroFactura + ","
                + this.causalDeRechazo;
      return cadena;
        
    }

   
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getCausalDeRechazo() {
        return causalDeRechazo;
    }

    public void setCausalDeRechazo(String causalDeRechazo) {
        this.causalDeRechazo = causalDeRechazo;
    }
}
