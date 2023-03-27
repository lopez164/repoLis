/*
 * To change this license headerString  choose License Headers in Project Properties.
 * To change this template fileString  choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.vistas;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lopez164
 */
public class Vst_ProductosPorFactura {

    String numeroFactura;
    String idCliente;
    int zona;
    int regional;
    int agencia;
    String nombreDeCliente;
    String nombreVendedor;
    Date fechaDeVenta;
    String FormaDePago;
    int canal;
    double valorFacturaSinIva;
    double valorIvaFactura;
    double valorTotalFactura;
    double valorRechazo;
    double valorDescuento;
    double valorTotalRecaudado;
    int consecutivo;
    String codigoProducto;
    String descripcionProducto;
    double cantidad;
    double valorUnitarioSinIva;
    double valorTotalItem;
    double valorUnitarioConIva;
    double valorTotalConIva;

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public int getRegional() {
        return regional;
    }

    public void setRegional(int regional) {
        this.regional = regional;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public String getNombreDeCliente() {
        return nombreDeCliente;
    }

    public void setNombreDeCliente(String nombreDeCliente) {
        this.nombreDeCliente = nombreDeCliente;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public Date getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(Date fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public String getFormaDePago() {
        return FormaDePago;
    }

    public void setFormaDePago(String FormaDePago) {
        this.FormaDePago = FormaDePago;
    }

    public int getCanal() {
        return canal;
    }

    public void setCanal(int canal) {
        this.canal = canal;
    }

    public double getValorFacturaSinIva() {
        return valorFacturaSinIva;
    }

    public void setValorFacturaSinIva(double valorFacturaSinIva) {
        this.valorFacturaSinIva = valorFacturaSinIva;
    }

    public double getValorIvaFactura() {
        return valorIvaFactura;
    }

    public void setValorIvaFactura(double valorIvaFactura) {
        this.valorIvaFactura = valorIvaFactura;
    }

    public double getValorTotalFactura() {
        return valorTotalFactura;
    }

    public void setValorTotalFactura(double valorTotalFactura) {
        this.valorTotalFactura = valorTotalFactura;
    }
    
    public double getValorRechazo() {
        return valorRechazo;
    }

    public void setValorRechazo(double valorRechazo) {
        this.valorRechazo = valorRechazo;
    }

    public double getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(double valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public double getValorTotalRecaudado() {
        return valorTotalRecaudado;
    }

    public void setValorTotalRecaudado(double valorTotalRecaudado) {
        this.valorTotalRecaudado = valorTotalRecaudado;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
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
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getValorUnitarioSinIva() {
        return valorUnitarioSinIva;
    }

    public void setValorUnitarioSinIva(double valorUnitario) {
        this.valorUnitarioSinIva = valorUnitario;
    }

    public double getValorTotalItem() {
        return valorTotalItem;
    }

    public void setValorTotalItem(double valorTotal) {
        this.valorTotalItem = valorTotal;
    }

    public double getValorUnitarioConIva() {
        return valorUnitarioConIva;
    }

    public void setValorUnitarioConIva(double valorUnitarioConIva) {
        this.valorUnitarioConIva = valorUnitarioConIva;
    }

    /**
     * Método que devuelve el valor total del producto con el iva incluidos
     *
     * @return devuelve el valor total del producto de la factura con iva
     * incluido
     */
    public double getValorTotalConIva() {
        return valorTotalConIva;
    }

    /**
     * Método que asigna el valor total al producto con el iva incluidos
     *
     * @param valorTotalConIva corresponde al valor del produco con el iva
     * incluido
     */
    public void setValorTotalConIva(double valorTotalConIva) {
        this.valorTotalConIva = valorTotalConIva;
    }

    public Vst_ProductosPorFactura() {
    }

    public Vst_ProductosPorFactura(Inicio ini, String numeroDeFactura) {

         Connection con = null;
            Statement st = null;
            String sql;
            ResultSet rst = null;
        try {
           
            con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //  con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            sql = "SELECT * FROM `vst_productosporfactura` "
                    + "WHERE numeroFactura='" + numeroDeFactura
                    + "' and activo=1;";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                if (rst.next()) {
                    this.numeroFactura = rst.getString("numeroFactura");
                    this.idCliente = rst.getString("idCliente");
                    this.zona = rst.getInt("zona");
                    this.regional = rst.getInt("regional");
                    this.agencia = rst.getInt("agencia");
                    this.nombreDeCliente = rst.getString("nombreDeCliente");
                    this.nombreVendedor = rst.getString("nombreVendedor");
                    this.fechaDeVenta = rst.getDate("fechaDeVenta");
                    this.FormaDePago = rst.getString("FormaDePago");
                    this.canal = rst.getInt("canal");
                    this.valorFacturaSinIva = rst.getDouble("valorFacturaSinIva");
                    this.valorIvaFactura = rst.getDouble("valorIvaFactura");
                    this.valorTotalFactura = rst.getDouble("valorTotalFactura");
                    this.valorRechazo = rst.getDouble("valorRechazo");
                    this.valorDescuento = rst.getDouble("valorDescuento ");
                    this.valorTotalRecaudado = rst.getDouble("valorTotalRecaudado");
                    this.consecutivo = rst.getInt("consecutivo");
                    this.codigoProducto = rst.getString("codigoProducto");
                    this.descripcionProducto = rst.getString("descripcionProducto");
                    this.cantidad = rst.getDouble("cantidad");
                    this.valorUnitarioSinIva = rst.getDouble("valorUnitario");
                    this.valorTotalItem = rst.getDouble("valorTotal");
                    //this.valorUnitarioConIva= rst.getDouble( "valorUnitarioConIva"); 
                    this.valorTotalConIva = rst.getDouble("valorTotalConIva");
                    this.valorUnitarioConIva = valorTotalItem / this.cantidad;

                } else {
                    this.numeroFactura = null;
                    

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException ex) {
             try {
                 System.out.println("Error en consultar tiposCanalDeVentas consulta sql " + ex.getMessage().toString());
                 Logger.getLogger(CCanalesDeVenta.class.getName()).log(Level.SEVERE, null, ex);
                 rst.close();
                 st.close();
                 con.close();
             } catch (SQLException ex1) {
                 Logger.getLogger(Vst_ProductosPorFactura.class.getName()).log(Level.SEVERE, null, ex1);
             }
        }

    }
   

}
