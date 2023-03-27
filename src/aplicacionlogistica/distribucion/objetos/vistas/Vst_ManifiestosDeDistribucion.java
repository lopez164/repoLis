/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.vistas;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloGuardarBBDDRemota;
import java.util.Date;

/**
 *
 * @author lelopez
 */
public class Vst_ManifiestosDeDistribucion {

    Inicio ini;
   

    String numeroManifiesto, vehiculo,tipoContrato, conductor, nombreConductor, despachador, nombreDespachador,  nombreCanal, nombreDeRuta, tipoRuta, usuarioManifiesto, tipoVehiculo;
    int idCanal, idRuta, estadoManifiesto, kmSalida, kmEntrada, kmRecorrido, isFree, cantidadPedidos, zona, regional, agencia,activo, trazabilidad;
    Double valorTotalManifiesto, valorRecaudado,valorConsignado;
    Date fechaDistribucion,horaDeDespacho , horaDeLiquidacion, fechaReal ;
    

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    
    
    public Date getFechaDistribucion() {
        return fechaDistribucion;
    }

    public void setFechaDistribucion(Date fechaDistribucion) {
        this.fechaDistribucion = fechaDistribucion;
    }

    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(String numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
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
        return nombreDespachador;
    }

    public void setNombreDespachador(String nombreDespachador) {
        this.nombreDespachador = nombreDespachador;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
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

    public String getUsuarioManifiesto() {
        return usuarioManifiesto;
    }

    public void setUsuarioManifiesto(String usuarioManifiesto) {
        this.usuarioManifiesto = usuarioManifiesto;
    }

    public int getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(int idCanal) {
        this.idCanal = idCanal;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public int getEstadoManifiesto() {
        return estadoManifiesto;
    }

    public void setEstadoManifiesto(int estadoManifiesto) {
        this.estadoManifiesto = estadoManifiesto;
    }

    public int getKmSalida() {
        return kmSalida;
    }

    public void setKmSalida(int kmSalida) {
        this.kmSalida = kmSalida;
    }

    public int getKmEntrada() {
        return kmEntrada;
    }

    public void setKmEntrada(int kmEntrada) {
        this.kmEntrada = kmEntrada;
    }

    public int getKmRecorrido() {
        return kmRecorrido;
    }

    public void setKmRecorrido(int kmRecorrido) {
        this.kmRecorrido = kmRecorrido;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public void setCantidadPedidos(int cantidadPedidos) {
        this.cantidadPedidos = cantidadPedidos;
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

    public Double getValorTotalManifiesto() {
        return valorTotalManifiesto;
    }

    public void setValorTotalManifiesto(Double valorTotalManifiesto) {
        this.valorTotalManifiesto = valorTotalManifiesto;
    }

    public Double getValorRecaudado() {
        return valorRecaudado;
    }

    public void setValorRecaudado(Double valorRecaudado) {
        this.valorRecaudado = valorRecaudado;
    }

    public Date getHoraDeDespacho() {
        return horaDeDespacho;
    }

    public void setHoraDeDespacho(Date horaDeDespacho) {
        this.horaDeDespacho = horaDeDespacho;
    }

    public Date getHoraDeLiquidacion() {
        return horaDeLiquidacion;
    }

    public void setHoraDeLiquidacion(Date horaDeLiquidacion) {
        this.horaDeLiquidacion = horaDeLiquidacion;
    }

    public Date getFechaReal() {
        return fechaReal;
    }

    public void setFechaReal(Date fechaReal) {
        this.fechaReal = fechaReal;
    }

    public int getTrazabilidad() {
        return trazabilidad;
    }

    public void setTrazabilidad(int trazabilidad) {
        this.trazabilidad = trazabilidad;
    }

    public Double getValorConsignado() {
        return valorConsignado;
    }

    public void setValorConsignado(Double valorConsignado) {
        this.valorConsignado = valorConsignado;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Vst_ManifiestosDeDistribucion(Inicio ini, String numeroManifiesto) {
        this.ini = ini;
        this.numeroManifiesto = numeroManifiesto;
    }
    
     public Vst_ManifiestosDeDistribucion(Inicio ini) {
        this.ini = ini;
       
    }
     
     public Vst_ManifiestosDeDistribucion() {
              
    }
    
    

     /**
     * Método que permite bloquear ó liberar de tal modo que otro
     * usuarioManifiesto pueda usar ó no dicho manifiesto
     *
     * @param liberar true para liberar y false para bloquear manifiesto
     * @return devuelve verdadero si manifiesto es liberado y false para
     * bloquear manifiesto.
     */
    public boolean liberarManifiesto(boolean liberar) {
        boolean liberado = false;

        String sql;
        if (liberar) {
            sql = "UPDATE  manifiestosdedistribucion set isFree=1  WHERE consecutivo='" + this.numeroManifiesto + "';";
            this.isFree = 1;
            liberado = true;
        } else {
            sql = "UPDATE manifiestosdedistribucion set isFree=0 WHERE consecutivo='" + this.numeroManifiesto + "';";
            this.isFree = 0;

        }

        //new Thread(new HiloGuardarBBDDRemota(ini, sql, "actualizando manifiesto " + this.numeroManifiesto + " -> " + liberado)).start();

        liberado = ini.insertarDatosRemotamente(sql, "Vst_ManifiestosDeDistribucion.liberarManifiesto");
        
        return liberado;
    }
    
     /**
     * Método que devuelve el código del manifiesto para imprimirlo en el label
     * de la vista
     *
     * @return Una cadena con el código del manifiesto
     */
    public String codificarManifiesto() {
        String numeroDeManifiesto = String.valueOf(this.numeroManifiesto);
        String codruta = String.valueOf(this.idRuta);
        String fecha = this.fechaDistribucion.toString().replace("-", "");
        String codagencia = String.valueOf(this.agencia);
        switch (numeroDeManifiesto.length()) {
            case 1:
                numeroDeManifiesto = "000" + numeroDeManifiesto;
                break;
            case 2:
                numeroDeManifiesto = "00" + numeroDeManifiesto;
                break;
            case 3:
                numeroDeManifiesto = "0" + numeroDeManifiesto;
                break;

        }
        switch (codruta.length()) {
            case 1:
                codruta = "00" + codruta;
                break;
            case 2:
                codruta = "0" + codruta;
                break;

        }
        switch (codagencia.length()) {
            case 1:
                codagencia = "00" + codagencia;
                break;
            case 2:
                codagencia = "0" + codagencia;
                break;
        }
        String textoManifiesto = this.vehiculo + "-" + fecha + "-" + codagencia + "-" + codruta + "-" + numeroDeManifiesto;
        return textoManifiesto;
    }
    
  
}
