/*
 * To change this license headerString  choose License Headers in Project Properties.
 * To change this template fileString  choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.vistas;

/**
 *
 * @author lopez164
 */
public class Vst_ProductosPorFacturaDescargados {

    int consecutivoFacturasDescargadas;
    String numeroManifiesto;
    int consecutivoProductosPorFactura;
    String numeroFactura;
    double porcentajeDescuento;
    double descuento;
    double cantidadRechazada;
    double valorRechazo;
    double cantidadEntregada;
    double cantidadDespachada;
    double valorTotalRecaudadoItem;
    int entregado;
    int causalDerechazo;
    String codigoProducto;
    String descripcionProducto;
    String nombreCausalDeRechazo;
    
    

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
   


    public int getConsecutivoFacturasDescargadas() {
        return consecutivoFacturasDescargadas;
    }

    public void setConsecutivoFacturasDescargadas(int consecutivoFacturasDescargadas) {
        this.consecutivoFacturasDescargadas = consecutivoFacturasDescargadas;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public int getConsecutivoProductosPorFactura() {
        return consecutivoProductosPorFactura;
    }

    public void setConsecutivoProductosPorFactura(int consecutivoProductosPorFactura) {
        this.consecutivoProductosPorFactura = consecutivoProductosPorFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public double getValorDescuento() {
        return descuento;
    }

    public void setValorDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getCantidadRechazada() {
        return cantidadRechazada;
    }

    public void setCantidadRechazada(double cantidadRechazada) {
        this.cantidadRechazada = cantidadRechazada;
    }

    public double getValorRechazo() {
        return valorRechazo;
    }

    public void setValorRechazo(double valorRechazo) {
        this.valorRechazo = valorRechazo;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getCantidadDespachada() {
        return cantidadDespachada;
    }

    public void setCantidadDespachada(double cantidadDespachada) {
        this.cantidadDespachada = cantidadDespachada;
    }

    
    
    public double getCantidadEntregada() {
        return cantidadEntregada;
    }

    public void setCantidadEntregada(double cantidadEntregada) {
        this.cantidadEntregada = cantidadEntregada;
    }

    public double getValorTotalRecaudadoItem() {
        return valorTotalRecaudadoItem;
    }

    public void setValoTotalRecaudadoItem(double totalRecaudado) {
        this.valorTotalRecaudadoItem = totalRecaudado;
    }

    public int getEntregado() {
        return entregado;
    }

    public void setEntregado(int entregado) {
        this.entregado = entregado;
    }

    public int getCausalDerechazo() {
        return causalDerechazo;
    }

    public void setCausalDerechazo(int causalDerechazo) {
        this.causalDerechazo = causalDerechazo;
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

    public String getNombreCausalDeRechazo() {
        return nombreCausalDeRechazo;
    }

    public void setNombreCausalDeRechazo(String nombreCausalDeRechazo) {
        this.nombreCausalDeRechazo = nombreCausalDeRechazo;
    }
}
