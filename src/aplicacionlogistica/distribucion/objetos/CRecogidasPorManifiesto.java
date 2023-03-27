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
 * @author Luis Eduardo López Casanova
 */
public class CRecogidasPorManifiesto {

    private int idRecogidasPorManifiesto;
    private String idNumeroManifiesto;
    private String numeroFactura;
    private String facturaAfectada;
    private String numeroDeSoporte;
    private double valorRecogida;
    private double valorRecaudadoRecogida;
    Inicio ini;

    public CRecogidasPorManifiesto() {
        
    }
    
     public CRecogidasPorManifiesto(Inicio ini) {
         this.ini=ini;
    }
    
    
    /**
     * Método constructor el cual recibe tres (3) parametros
     *
     * @param ini correspondiente a la clase que configura el sistema
     * @param numeroManifiesto es el numero de manifiesto donde se encuentra la factura que 
     * se va a descargar
     * @param numeroFactura es el número de la factura a la cual se le va a aplicar
     * el descuento respectivo
     */
    public CRecogidasPorManifiesto(Inicio ini, String numeroManifiesto, String numeroFactura) {
        this.idNumeroManifiesto = numeroManifiesto;
        this.numeroFactura = numeroFactura;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql =   "SELECT idRecogidasPorManifiesto, idNumeroManifiesto, numeroFactura,"
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
                    this.idNumeroManifiesto = rst.getString("idNumeroManifiesto");
                    this.numeroFactura = rst.getString("numeroFactura");
                    this.facturaAfectada = rst.getString("facturaAfectada");
                    this.numeroDeSoporte = rst.getString("numeroDeSoporte");
                    this.valorRecogida = rst.getDouble("valorRecogida");
                    this.valorRecaudadoRecogida = rst.getDouble("valorRecaudadoRecogida");

                }
            } catch (SQLException ex) {
                Logger.getLogger(CRecogidasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean grabarRecogidasPorManifiesto(Inicio ini) {
        boolean grabado = false;
        try {

             String sql = "INSERT INTO recogidaspormanifiesto ("
                     + "idNumeroManifiesto, numeroFactura, facturaAfectada, "
                     + "numeroDeSoporte, valorRecogida,valorRecaudadoRecogida, activo, usuario, flag) "
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
                    + "idNumeroManifiesto='" + this.idNumeroManifiesto + "',"
                    + "numeroFactura='" + this.numeroFactura + "'," 
                    + "facturaAfectada='" + this.facturaAfectada + "',"
                    + "numeroDeSoporte='" + this.numeroDeSoporte + "',"
                    + "valorRecogida='" + this.valorRecogida + "',"
                    + "valorRecaudadoRecogida='" + this.valorRecaudadoRecogida + "',"
                    + "activo='1',"
                    + " usuario='" + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',"
                    + "flag='-1'; ";

            grabado = ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(CRecogidasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarDevolucionesPorManifiesto 1 sql " + ex.getMessage().toString());
        } catch (Exception ex) {
            Logger.getLogger(CRecogidasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarDevolucionesPorManifiesto 2 sql " + ex.getMessage().toString());
        }
        return grabado;

    }

    public int getIdRecogidasPorManifiesto() {
        return idRecogidasPorManifiesto;
    }

    public void setIdRecogidasPorManifiesto(int idRecogidasPorManifiesto) {
        this.idRecogidasPorManifiesto = idRecogidasPorManifiesto;
    }

    public String getIdNumeroManifiesto() {
        return idNumeroManifiesto;
    }

    public void setIdNumeroManifiesto(String idNumeroManifiesto) {
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

    public double getValorRecaudadoRecogida() {
        return valorRecaudadoRecogida;
    }

    public void setValorRecaudadoRecogida(double valorRecaudadoRecogida) {
        this.valorRecaudadoRecogida = valorRecaudadoRecogida;
    }
    
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
    
      /**
     * Método que devuelve una cadena con sentencia SQL para insertar Datos en la BBDD ","
     * 
     * @return una cadena con la sentencia SQL para insertar Datos
     */
         
     public String getSentenciaInsertSQL() {
         String sql = null;
         try {
            
             sql = "INSERT INTO recogidaspormanifiesto ("
                     + "idNumeroManifiesto, numeroFactura, facturaAfectada, "
                     + "numeroDeSoporte, valorRecogida,valorRecaudadoRecogida, zona,regional,agencia,activo, usuario, flag) "
                     + "VALUES('"
                     + this.idNumeroManifiesto + "','"
                     + this.numeroFactura + "','"
                     + this.facturaAfectada + "','"
                     + this.numeroDeSoporte + "','"
                     + this.valorRecogida + "','"
                     + this.valorRecaudadoRecogida + "','"
                     + ini.getUser().getZona() + "','"
                     + ini.getUser().getRegional() + "','"
                     + ini.getUser().getAgencia() + "','"
                     + "1','"
                     + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                     + "1') "
                     + " ON DUPLICATE KEY UPDATE "
                     + "idNumeroManifiesto='" + this.idNumeroManifiesto + "', "
                     + "numeroFactura='" + this.numeroFactura + "', "
                     + "facturaAfectada='" + this.facturaAfectada + "', "
                     + "numeroDeSoporte='" + this.numeroDeSoporte + "', "
                     + "valorRecogida='" + this.valorRecogida + "', "
                     + "valorRecaudadoRecogida='" + this.valorRecaudadoRecogida + "', "
                     + "activo='1', "
                     + " usuario='" + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "', "
                     + "flag='-1'; ";
         
        } catch (Exception ex) {
            Logger.getLogger(CRecogidasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
         return sql;
    }

}
