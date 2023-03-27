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
 * @author lopez164
 */
public class CRecogidasPorManifiesto_1 {

    

    int idRecogidasPorManifiesto;
    int idNumeroManifiesto;
    String numeroFactura;
    String facturaAfectada;
    String numeroDeSoporte;
    double valorRecogida;
    double valorRecaudadoRecogida;
    
    Inicio ini;

    public int getIdRecogidasPorManifiesto() {
        return idRecogidasPorManifiesto;
    }

    public void setIdRecogidasPorManifiesto(int idRecogidasPorManifiesto) {
        this.idRecogidasPorManifiesto = idRecogidasPorManifiesto;
    }

    public int getIdNumeroManifiesto() {
        return idNumeroManifiesto;
    }

    public void setIdNumeroManifiesto(int idNumeroManifiesto) {
        this.idNumeroManifiesto = idNumeroManifiesto;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFacturaAfectada() {
        return facturaAfectada;
    }

    public void setFacturaAfectada(String facturaAfectada) {
        this.facturaAfectada = facturaAfectada;
    }

    public String getNumeroDeSoporte() {
        return numeroDeSoporte;
    }

    public void setNumeroDeSoporte(String numeroDeSoporte) {
        this.numeroDeSoporte = numeroDeSoporte;
    }

    public double getValorRecogida() {
        return valorRecogida;
    }

    public void setValorRecogida(double valorRecogida) {
        this.valorRecogida = valorRecogida;
    }

       

    public CRecogidasPorManifiesto_1() {
        
    }

    public CRecogidasPorManifiesto_1(Inicio ini) {
         this.ini = ini;
    }

    public double getValorRecaudadoRecogida() {
        return valorRecaudadoRecogida;
    }

    public void setValorRecaudadoRecogida(double valorRecaudado) {
        this.valorRecaudadoRecogida = valorRecaudado;
    }
    
    
    public CRecogidasPorManifiesto_1(Inicio ini, int numeroManifiesto, String numeroFactura) {
        this.ini = ini;
        this.idNumeroManifiesto = numeroManifiesto;
        this.numeroFactura = numeroFactura;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
         con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT idRecogidasPorManifiesto, idNumeroManifiesto, numeroFactura,"
                + "facturaAfectada, numeroDeSoporte, valorRecogida,valorRecaudadoRecogida, activo "
                + "FROM recogidaspormanifiesto "
                + " WHERE "
                + " idNumeroManifiesto='" + numeroManifiesto + "' AND "
                + "numeroFactura='" + numeroFactura + "' ";
        
        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                     this.idRecogidasPorManifiesto = rst.getInt("idRecogidasPorManifiesto");
                    this.idNumeroManifiesto = rst.getInt("idNumeroManifiesto");
                    this.numeroFactura = rst.getString("numeroFactura");
                    this.facturaAfectada = rst.getString("facturaAfectada");
                    this.numeroDeSoporte = rst.getString("numeroDeSoporte");
                    this.valorRecogida = rst.getDouble("valorRecogida");
                    this.valorRecaudadoRecogida = rst.getDouble("valorRecaudadoRecogida");
                }
            } catch (SQLException ex) {
                Logger.getLogger(CRecogidasPorManifiesto_1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean grabarDevolucionesPorManifiesto(Inicio ini) {
        boolean grabado = false;
        try {

             String sql = "INSERT INTO `recogidaspormanifiesto` ("
                     + "`idNumeroManifiesto`, `numeroFactura`, `facturaAfectada`, "
                     + "`numeroDeSoporte`, `valorRecogida`,`valorRecaudadoRecogida`, `activo`, `usuario`, `flag`) "
                     + "VALUES('"
                    + this.idNumeroManifiesto + "','"
                    + this.numeroFactura + "','"
                    + this.facturaAfectada + "','"
                    + this.numeroDeSoporte + "','"
                    + this.valorRecogida + "','"
                    + this.valorRecaudadoRecogida + "','"
                    + "1','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    +  "1') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "idNumeroManifiesto='" + this.idNumeroManifiesto + "','"
                    + "numeroFactura='" + this.numeroFactura + "','" 
                    + "facturaAfectada='" + this.facturaAfectada + "','"
                    + "numeroDeSoporte='" + this.numeroDeSoporte + "','"
                    + "valorRecogida='" + this.valorRecogida + "','"
                    + "valorRecaudadoRecogida='" + this.valorRecaudadoRecogida + "','"
                    + "activo='1','"
                    + " usuario='" + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + "flag='-1'); ";

            grabado = ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(CRecogidasPorManifiesto_1.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarDevolucionesPorManifiesto 1 sql " + ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(CRecogidasPorManifiesto_1.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarDevolucionesPorManifiesto 2 sql " + ex.getMessage());
        }
        return grabado;

    }
            //numeroManifiesto, numeroDeFactura, numeroDeSoporte, valorRecogida, zona, regional, agencia, activo, fechaIng, usuario, flag, consecutivo, id

     public String getCadenaConLosCampos(){
        
        String cadena;
        
        cadena=  this.idRecogidasPorManifiesto + ","
                 + this.idNumeroManifiesto + ","
                 + this.numeroFactura + ","
                 + this.facturaAfectada + ","
                 + this.numeroDeSoporte + ","
                 + this.valorRecogida + ","
                 + this.valorRecaudadoRecogida;
                     
         
        return cadena;
    }
}
