/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.objetos;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.CConexiones;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class CRecogidasPorFactura {

    int idrecogidaporfactura;
    String numeroDocumento;
    String numeroFactura;
    Date fechaDeVenta;
    String nombreCliente;
    String direccionCliente;
    Double valorTotalFactura;
    double valorRecogida;
    int activo;
    Date fechaIng;
    String usuario;
    int flag;

  
        
    Inicio ini;

    public int getIdRecogidaPorFactura() {
        return idrecogidaporfactura;
    }

    public void setIdRecogidaPorFactura(int idrecogidaporfactura) {
        this.idrecogidaporfactura = idrecogidaporfactura;
    }

    public double getValorRecogida() {
        return valorRecogida;
    }

    public void setValorRecogida(double valorRecogida) {
        this.valorRecogida = valorRecogida;
    }

   
    
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public int getIdrecogidaporfactura() {
        return idrecogidaporfactura;
    }

    public void setIdrecogidaporfactura(int idrecogidaporfactura) {
        this.idrecogidaporfactura = idrecogidaporfactura;
    }

    public Date getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(Date fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public Double getValorTotalFactura() {
        return valorTotalFactura;
    }

    public void setValorTotalFactura(Double valorTotalFactura) {
        this.valorTotalFactura = valorTotalFactura;
    }

    
   
    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Date getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(Date fechaIng) {
        this.fechaIng = fechaIng;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public CRecogidasPorFactura() {
        
    }
     public CRecogidasPorFactura(Inicio ini) {
        this.ini = ini;
     }

    public CRecogidasPorFactura(Inicio ini, String numeroFactura) {
        this.ini = ini;
        this.numeroFactura = numeroFactura;

        Connection con = null;
        Statement st = null;
        String sql;
        ResultSet rst = null;
        this.ini = ini;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "SELECT rf.idrecogidaporfactura, rf.numeroFactura as factura,f.fechaDeVenta as fechaVenta,"
                    + "c.nombreDeCliente as nombreCliente,f.direccion as direccion,f.valorTotalFactura, "
                    + "rf.numeroDocumento as documento,rf.valorDescuento as valor,"
                    + "rf.activo,rf.fechaIng,rf.usuario, rf.flag "
                    + "FROM recogidaporfactura df "
                    + "join facturascamdun f on f.numeroFactura=rf.numeroFactura "
                    + "join clientescamdun c on c.codigoInterno=f.cliente "
                    + "where f.numeroFactura='" + numeroFactura + "';";

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                llenarDatos(rst);

                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
            try {
                System.out.println("Error en consultar vst_facturaCamdun consulta sql " + ex.getMessage().toString());
                Logger.getLogger(CRecogidasPorFactura.class.getName()).log(Level.SEVERE, null, ex);
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex1) {
                Logger.getLogger(CRecogidasPorFactura.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    private void llenarDatos(ResultSet rst) throws SQLException {
        if (rst.next()) {

            this.idrecogidaporfactura = rst.getInt("idrecogidaporfactura");
            this.numeroDocumento = rst.getString("numeroDocumento");
            this.numeroFactura = rst.getString("numeroFactura");
            this.fechaDeVenta = rst.getDate("fechaDeVenta");
            this.nombreCliente = rst.getString("nombreCliente");
            this.direccionCliente = rst.getString("direccion");
            this.valorTotalFactura=rst.getDouble("valorTotalFactura");
            this.valorRecogida = rst.getDouble("valorDescuento");
            this.activo = rst.getInt("activo");
            this.fechaIng = rst.getDate("fechaIng");
            this.usuario = rst.getString("usuario");
            this.flag = rst.getInt("flag");
        }
    }

    public boolean  guardarRecogidaPorFactura() throws Exception {
        String sql = "INSERT INTO recogidaporfactura"
                + "(numeroDocumento,"
                + "numeroFactura,"
                + "valorRecogida,"
                + "usuario)"
                + "VALUES"
                + "('" + this.numeroDocumento + "','"
                + this.numeroFactura + "','"
                + this.valorRecogida + "','"
                + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "') "
                + "ON DUPLICATE KEY UPDATE flag=-1";
        
      return   ini.insertarDatosRemotamente(sql);

    }

}


