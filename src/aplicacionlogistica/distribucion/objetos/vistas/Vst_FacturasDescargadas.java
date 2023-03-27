/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.vistas;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class Vst_FacturasDescargadas {

    int consecutivo;
    int idCanal;
    String nombreCanal;
    String numeroManifiesto;
    int adherencia;
    String numeroFactura;
    Date fechaDistribucion, fechaDeVenta;
    String vehiculo;
    String tipoContrato;
    String conductor;
    String nombreConductor;
    String nombreDespachador;
    String nombreDeRuta;
    String tipoRuta;
    String idCliente;
    String nombreDeCliente;
    String direccion;
    Double valorFacturaSinIva;
    Double valorIvaFactura;
    Double valorTotalFactura;
    Double valorRechazo;
    Double valorDescuento;
    Double valorRecaudado;
    int formaDePago;
    Double pesofactura;
    int idTipoDeMovimiento;
    String nombreTipoDeMovimiento;
    int causalDeRechazo;
    String nombreCausalDeRechazo;
    String competencia;
    String vendedor;
    int salidasDistribucion;
    int control;
    String tipoDeDEscargue;
    Date fechaReal;
    String usuario;
    int activo=1;
    
    Inicio ini;
    
   List<CProductosPorFactura> listaCProductosPorFactura;

    //idCanal, nombreCanal, numeroManifiesto, adherencia, numeroFactura, fechaDistribucion, fechaDeVenta, 
    //vehiculo, tipoContrato, conductor, nombreConductor, nombreDeRuta, tipoRuta, nombreDeCliente, direccion, 
    //valorFacturaSinIva, valorTotalFactura, valorRechazo, valorDescuento, valorRecaudado, idTipoDeMovimiento, nombreTipoDeMovimiento, causalDeRechazo, nombreCausalDeRechazo, vendedor, salidasDistribucion
public Vst_FacturasDescargadas(){
    
}

public Vst_FacturasDescargadas(Inicio ini){
   this.ini=ini; 
}


    public Vst_FacturasDescargadas(int consecutivo,int idCanal, String nombreCanal, String numeroManifiesto, 
            int adherencia, String numeroFactura, Date fechaDistribucion, Date fechaDeVenta, 
            String vehiculo, String tipoContrato, String conductor, String nombreConductor, 
            String nombreDeRuta, String tipoRuta, String nombreDeCliente, String direccion, 
            Double valorFacturaSinIva, Double valorTotalFactura, Double valorRechazo, 
            Double valorDescuento, Double valorRecaudado, int idTipoDeMovimiento, 
            String nombreTipoDeMovimiento, int causalDeRechazo, String nombreCausalDeRechazo,
            String competencia, String vendedor, int salidasDistribucion,String usuario) {
       this.consecutivo=consecutivo;
        this.idCanal = idCanal;
        this.nombreCanal = nombreCanal;
        this.numeroManifiesto = numeroManifiesto;
        this.adherencia = adherencia;
        this.numeroFactura = numeroFactura;
        this.fechaDistribucion = fechaDistribucion;
        this.fechaDeVenta = fechaDeVenta;
        this.vehiculo = vehiculo;
        this.tipoContrato = tipoContrato;
        this.conductor = conductor;
        this.nombreConductor = nombreConductor;
        this.nombreDeRuta = nombreDeRuta;
        this.tipoRuta = tipoRuta;
        this.nombreDeCliente = nombreDeCliente;
        this.direccion = direccion;
        this.valorFacturaSinIva = valorFacturaSinIva;
        this.valorTotalFactura = valorTotalFactura;
        this.valorRechazo = valorRechazo;
        this.valorDescuento = valorDescuento;
        this.valorRecaudado = valorRecaudado;
        this.idTipoDeMovimiento = idTipoDeMovimiento;
        this.nombreTipoDeMovimiento = nombreTipoDeMovimiento;
        this.causalDeRechazo = causalDeRechazo;
        this.competencia = competencia;
        this.nombreCausalDeRechazo = nombreCausalDeRechazo;
        this.vendedor = vendedor;
        this.salidasDistribucion = salidasDistribucion;
        this.usuario = usuario;
    }

    public String getNombreDespachador() {
        return nombreDespachador;
    }

    public void setNombreDespachador(String nombreDespachador) {
        this.nombreDespachador = nombreDespachador;
    }

    
    
    public Date getFechaReal() {
        return fechaReal;
    }

    public void setFechaReal(Date fechaReal) {
        this.fechaReal = fechaReal;
    }

    
    public String getTipoDeDEscargue() {
        return tipoDeDEscargue;
    }

    public void setTipoDeDEscargue(String tipoDeDEscargue) {
        this.tipoDeDEscargue = tipoDeDEscargue;
    }

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

    
    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    
    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }
    
   
   
    public int getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(int idCanal) {
        this.idCanal = idCanal;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public int getAdherencia() {
        return adherencia;
    }

    public void setAdherencia(int adherencia) {
        this.adherencia = adherencia;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFechaDistribucion() {
        return fechaDistribucion;
    }

    public void setFechaDistribucion(Date fechaDistribucion) {
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

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    
    
    public String getNombreDeCliente() {
        return nombreDeCliente;
    }

    public void setNombreDeCliente(String nombreDeCliente) {
        this.nombreDeCliente = nombreDeCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getValorFacturaSinIva() {
        return valorFacturaSinIva;
    }

    public void setValorFacturaSinIva(Double valorFacturaSinIva) {
        this.valorFacturaSinIva = valorFacturaSinIva;
    }

    public Double getValorIvaFactura() {
        return valorIvaFactura;
    }

    public void setValorIvaFactura(Double valorIvaFactura) {
        this.valorIvaFactura = valorIvaFactura;
    }
    
    

    public Double getValorTotalFactura() {
        return valorTotalFactura;
    }

    public void setValorTotalFactura(Double valorTotalFactura) {
        this.valorTotalFactura = valorTotalFactura;
    }

    public Double getValorRechazo() {
        return valorRechazo;
    }

    public void setValorRechazo(Double valorRechazo) {
        this.valorRechazo = valorRechazo;
    }

    public Double getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(Double valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public Double getValorRecaudado() {
        return valorRecaudado;
    }

    public void setValorRecaudado(Double valorRecaudado) {
        this.valorRecaudado = valorRecaudado;
    }

    public int getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(int formaDePago) {
        this.formaDePago = formaDePago;
    }

    public Double getPesofactura() {
        return pesofactura;
    }

    public void setPesofactura(Double pesofactura) {
        this.pesofactura = pesofactura;
    }
    
    
    

    public int getIdTipoDeMovimiento() {
        return idTipoDeMovimiento;
    }

    public void setIdTipoDeMovimiento(int idTipoDeMovimiento) {
        this.idTipoDeMovimiento = idTipoDeMovimiento;
    }

    public String getNombreTipoDeMovimiento() {
        return nombreTipoDeMovimiento;
    }

    public void setNombreTipoDeMovimiento(String nombreTipoDeMovimiento) {
        this.nombreTipoDeMovimiento = nombreTipoDeMovimiento;
    }

    public int getCausalDeRechazo() {
        return causalDeRechazo;
    }

    public void setCausalDeRechazo(int causalDeRechazo) {
        this.causalDeRechazo = causalDeRechazo;
    }

    public String getNombreCausalDeRechazo() {
        return nombreCausalDeRechazo;
    }

    public void setNombreCausalDeRechazo(String nombreCausalDeRechazo) {
        this.nombreCausalDeRechazo = nombreCausalDeRechazo;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public Date getFechaDeVenta() {
        return fechaDeVenta;
    }

    public void setFechaDeVenta(Date fechaDeVenta) {
        this.fechaDeVenta = fechaDeVenta;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getNombreDeRuta() {
        return nombreDeRuta;
    }

    public void setNombreDeRuta(String nombreDeRuta) {
        this.nombreDeRuta = nombreDeRuta;
    }

    public String getTipoRuta() {
        return tipoRuta;
    }

    public void setTipoRuta(String tipoRuta) {
        this.tipoRuta = tipoRuta;
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

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
 /**
     * Método que devuelve una cadena con sentencia SQL para insertar Datos en
     * la BBDD ","
     *
     * @return una cadena con la sentencia SQL para isertar Datos
     */
//    @Override
    public String getSentenciaInsertSQL() {
        String sql = null;
        try {

            sql = "INSERT INTO  facturasdescargadas(`consecutivo`,`numeroManifiesto`,`adherencia`,`numeroFactura`,`valorRechazo`,`valorDescuento`,`valorRecaudado`,"
                    + "`movimientoFactura`,`motivoRechazo`,`activo`,`usuario`,`flag`) "
                    + "VALUES ('"
                    + this.consecutivo + "','"
                    + this.numeroManifiesto + "','"
                    + this.adherencia + "','"
                    + this.numeroFactura + "','"
                    + this.valorRechazo + "','"
                    + this.valorDescuento + "','"
                    + this.valorRecaudado + "','"
                    + this.idTipoDeMovimiento + "','"
                    + this.getCausalDeRechazo()+ "','"
                    + this.activo + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + "1') ON DUPLICATE KEY UPDATE "
                    + " `consecutivo`='" + this.consecutivo + "',"
                    + "`numeroManifiesto`='" + this.numeroManifiesto + "',"
                    + "`adherencia`='" + this.adherencia + "',"
                    + "`numeroFactura`='" + this.numeroFactura + "',"
                    + "`valorRechazo`='" + this.valorRechazo + "',"
                    + "`valorDescuento`='" + this.valorDescuento + "',"
                    + "`valorRecaudado`='" + this.valorRecaudado + "',"
                    + "`movimientoFactura`='" + this.idTipoDeMovimiento + "',"
                    + "`motivoRechazo`='" + this.causalDeRechazo + "',"
                    + "`activo`='" + this.activo + "',"
                    + "`usuario`='" + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "',"
                    + "`flag`=-1; ";

            /* si la factura es un rechazo total, se libera para tener la
             posibilidad de sacarla a ruta nuevamente 
            if (this.idMovimientoFactura == 3) {
                sql += "update facturascamdun set isFree=1 where numeroFactura='" + this.numeroFactura + "';";
            }
             */
        } catch (Exception ex) {
            Logger.getLogger(CFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }
    
    /**
     * Método actualiza una factura dando al campo isFree= 1 par poder con el
     * fin de poder utilizar el documento en un nuevo movimiento de salida a
     * distribución.
     *
     * @return devueleve una cabena con la sentencia SQL de actualizar, para
     * actualizar la BBDD remota
     */
    public String liberFacturaDescargada(String numeroFactura) {

        String sql = "";
        try {
            sql = "UPDATE facturascamdun "
                    + "set isFree=1 "
                    + "WHERE "
                    + "numeroFactura='" + numeroFactura + "' ;";
            ini.insertarDatosLocalmente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CFacturasDescargadas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sql;
    }
    
     public List<CProductosPorFactura> getListaProductosPorFactura() {
        return listaCProductosPorFactura;
    }

    public void setListaProductosPorFactura(List<CProductosPorFactura> listaCProductosPorFactura) {
        this.listaCProductosPorFactura = listaCProductosPorFactura;
    }
    
     /**
     * Método que devuelve una cadena con la lista de todos los campos de la
     * tabla.
     *
     * @return una cadena con la lista de los campos
     */
    //@Override
    public String getCadenaConLosCampos() {
        String cadena;

        cadena = this.consecutivo + ","
                + this.numeroManifiesto + ","
                + this.adherencia + ","
                + this.numeroFactura + ","
                + this.valorRechazo + ","
                + this.valorDescuento + ","
                + this.valorRecaudado + ","
                + this.idTipoDeMovimiento + ","
                + this.causalDeRechazo + ","
                + this.activo;
        return cadena;
    }
    
}
