/*
 * To change this license header;choose License Headers in Project Properties.
 * To change this template file;choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.vistas;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lopez164
 */
public class Vst_FacturasPorManifiesto {

    Inicio ini;
    
    int consecutivo;
    int adherencia;
    String numeroManifiesto;
    String fechaDistribucion;
    String vehiculo;
    String conductor;
    String nombreConductor;
    String despachador;
    String NombreDespachador;
    int ruta;
    String nombreRuta;
    String numeroFactura;
    double valorARecaudarFactura;
    String fechaIng;
    Date fechaDeVenta;
    String cliente;
    String nombreDeCliente;
    String direccionCliente;
     String barrio;
    String vendedor;
    String formaDePago;
    int canal;
    String nombreCanal;
    double valorFacturaSinIva;
    double valorIvaFactura;
    double valorFactura;
    double valorRechazo;
    double valorDescuento;
    double valorTotalRecaudado;
    int salidasDistribucion;
    String usuario;
    double pesofactura;
    int isFree;
    String nombreEstadoFactura;
    String telefonoCliente;
    String ciudad;
    int estadoFactura;
    String observaciones;

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    

    public int getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(int estadoFactura) {
        this.estadoFactura = estadoFactura;
    }
    

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }
    
    

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }
    
    

    public String getNombreEstadoFactura() {
        return nombreEstadoFactura;
    }

    public void setNombreEstadoFactura(String nombreEstadoFactura) {
        this.nombreEstadoFactura = nombreEstadoFactura;
    }
    
    

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }
    
    
    

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }
    
    

    public int getAdherencia() {
        return adherencia;
    }

    public void setAdherencia(int adherencia) {
        this.adherencia = adherencia;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getFechaDistribucion() {
        return fechaDistribucion;
    }

    public void setFechaDistribucion(String fechaDistribucion) {
        this.fechaDistribucion = fechaDistribucion;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getDespachador() {
        return despachador;
    }

    public void setDespachador(String despachador) {
        this.despachador = despachador;
    }

    public String getNombreDespachador() {
        return NombreDespachador;
    }

    public void setNombreDespachador(String NombreDespachador) {
        this.NombreDespachador = NombreDespachador;
    }

    
    
    public int getRuta() {
        return ruta;
    }

    public void setRuta(int ruta) {
        this.ruta = ruta;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public double getValorARecaudarFactura() {
        return valorARecaudarFactura;
    }

    public void setValorARecaudarFactura(double valorARecaudarFactura) {
        this.valorARecaudarFactura = valorARecaudarFactura;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
        this.fechaIng = fechaIng;
    }

    public Date getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(Date fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNombreDeCliente() {
        return nombreDeCliente;
    }

    public void setNombreDeCliente(String nombreDeCliente) {
        this.nombreDeCliente = nombreDeCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }
    
    

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(String formaDePago) {
        this.formaDePago = formaDePago;
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
        return valorFactura;
    }

    public void setValorTotalFactura(double valorTotalFactura) {
        this.valorFactura = valorTotalFactura;
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

    public double getValorRecaudado() {
        return valorTotalRecaudado;
    }

    public void setValorRecaudado(double valorTotalRecaudado) {
        this.valorTotalRecaudado = valorTotalRecaudado;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
    }

    public int getSalidasDistribucion() {
        return salidasDistribucion;
    }

    public void setSalidasDistribucion(int salidasDistribucion) {
        this.salidasDistribucion = salidasDistribucion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public double getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(double valorFactura) {
        this.valorFactura = valorFactura;
    }

    public double getValorTotalRecaudado() {
        return valorTotalRecaudado;
    }

    public void setValorTotalRecaudado(double valorTotalRecaudado) {
        this.valorTotalRecaudado = valorTotalRecaudado;
    }

    public double getPesofactura() {
        return pesofactura;
    }

    public void setPesofactura(double pesofactura) {
        this.pesofactura = pesofactura;
    }

    public Vst_FacturasPorManifiesto() {
    }

    public Vst_FacturasPorManifiesto(Inicio ini) {
        this.ini = ini;
    }
    
     /**
     * Método que devuelve una cadena con l lista de los campos de la tabla
     * facturas por manifiesto
     *
     * @return una cadena con la lista de los campos
     */
    public String getCadenaConLosCampos() {
        String cadena = null;

        cadena = this.adherencia + ","
                + this.numeroManifiesto + ","
                + this.numeroFactura + ","
                + this.valorARecaudarFactura + ","
                + this.pesofactura;

        return cadena;
    }
    
      /**
     * Método que devuelve una cadena con sentencia SQL para
     * insertarDatosLocalmente datos en la BBDD ","
     *
     * @return una cadena con la sentencia SQL para isertar Datos
     */
    public String getSentenciaInsertSQL() {
        String sql = null;
        try {

            sql = "INSERT INTO facturaspormanifiesto (numeroManifiesto,"
                    + " numeroFactura, valorARecaudarFactura,pesoFactura,adherencia,zona, regional, agencia, activo, usuario,"
                    + " flag) VALUES ('"
                    + this.numeroManifiesto + "','"
                    + this.numeroFactura + "','"
                    + this.valorARecaudarFactura + "','"
                    + this.pesofactura + "','"
                    + this.adherencia + "','"
                    + ini.getUser().getZona() + "','"
                    + ini.getUser().getRegional() + "','"
                    + ini.getUser().getAgencia() + "','1','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','-1') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "numeroManifiesto='" + this.numeroManifiesto + "',"
                    + "numeroFactura='" + this.numeroFactura + "',"
                    + "adherencia='" + this.adherencia + "',"
                    + "zona='" + ini.getUser().getZona() + "',"
                    + "regional='" + ini.getUser().getRegional() + "',"
                    + "agencia='" + ini.getUser().getAgencia() + "',"
                    + " usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + " flag='-1';";

        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }
}
