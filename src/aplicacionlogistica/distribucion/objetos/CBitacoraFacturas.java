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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class CBitacoraFacturas {
    int idbitacorafacturas;
     String documento;
    String numeroFactura;
    String observacion;
    String fechaIng;
    Date fecha;
    
    
    String usuario;
    Inicio ini;

    public CBitacoraFacturas() {
    }

    public CBitacoraFacturas(Inicio ini) {
        this.ini = ini;
    }

    public CBitacoraFacturas(int idbitacorafacturas, String numeroFactura, String observacion, String fechaIng, String usuario) {
        this.idbitacorafacturas = idbitacorafacturas;
        this.numeroFactura = numeroFactura;
        this.observacion = observacion;
        this.fechaIng = fechaIng;
        this.usuario = usuario;
    }
    

    
    public CBitacoraFacturas(String numeroFactura, Inicio ini) {
        this.numeroFactura = numeroFactura;
        this.ini = ini;
         this.numeroFactura = numeroFactura;
        this.ini = ini;
        
        String sql = "SELECT * "
                + "FROM bitacorafacturas "
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
                   
                    this.idbitacorafacturas = (rst.getInt("idbitacorafacturas"));
                    this.documento = (rst.getString("documento"));
                    this.numeroFactura = (rst.getString("numeroFactura"));
                    this.observacion = (rst.getString("observacion"));
                    this.fechaIng = (rst.getString("fechaIng"));
                    this.usuario = (rst.getString("usuario"));
                    this.fecha = (rst.getDate("fecha"));
                    
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(CControladorReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
       
    }


    public int getIdbitacorafacturas() {
        return idbitacorafacturas;
    }

    public void setIdbitacorafacturas(int idbitacorafacturas) {
        this.idbitacorafacturas = idbitacorafacturas;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
        this.fechaIng = fechaIng;
    }
    
     public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNumeroDocumento() {
        return documento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.documento = numeroDocumento;
    }
    
    
}
