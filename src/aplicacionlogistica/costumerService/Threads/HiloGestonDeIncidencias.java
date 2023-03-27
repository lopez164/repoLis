/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FGestionarIncidencias;
import aplicacionlogistica.costumerService.objetos.CSvcIncidencias;
import aplicacionlogistica.costumerService.objetos.CTiposDePeticion;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CMovimientosManifiestosfacturas;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasPorManifiesto;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloGestonDeIncidencias implements Runnable {

    List<CTiposDePeticion> ListadoDeTiposDePeticones = null;

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    JProgressBar barraInf = null;
    JProgressBar barraSup = null;
    int totalFilasDeConsultas;
    int contadorDeRegistros;
    FGestionarIncidencias fGestinarIncidencias;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloGestonDeIncidencias(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fGestinarIncidencias
     */
    public HiloGestonDeIncidencias(Inicio ini, FGestionarIncidencias fGestinarIncidencias, String comando) {
        this.ini = ini;
        this.fGestinarIncidencias = fGestinarIncidencias;
        this.caso = comando;
    }

    @Override
    public void run() {

        if (caso != null) {
            switch (caso) {
                case "GrabarIncidencia":
                    crearIncidencia();
                    break;
                case "buscarFactura": {
                    try {
                         fGestinarIncidencias.lblCirculoDeProgreso.setVisible(true);
                         fGestinarIncidencias.txtNumeroFactura.setEditable(false);
                        fGestinarIncidencias.incidenciaSvc = new CSvcIncidencias(ini, fGestinarIncidencias.txtNumeroFactura.getText().trim());

//                      if (incidenciaSvc.getFactura() != null) {
                        fGestinarIncidencias.lblCirculoDeProgreso.setVisible(true);

                        if (fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getNumeroDeFactura() != null) {
                           

                            llenarDatosDeLaVista();

                           
                            
                            // fGestinarIncidencias. btnHabilitarGestion.setEnabled(true);
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(HiloGestonDeIncidencias.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            }
        }
    }

    private void llenarDatosDeLaVista() throws Exception {

        try {
           
            fGestinarIncidencias.lblCirculoDeProgreso.setVisible(true);
            fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().setListaDetalleFactura(true);

            fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().setListaDetalleFactura(true);
            fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().setListaDeMovimientosfactura();
            fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().setListaDeProductosRechazados();
            fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().setListaDeMovimientosBitacora();

            String numeroManifiesto = null;
            for (CFacturasPorManifiesto fxm : fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getListaDeMovimientosfactura()) {
                numeroManifiesto = fxm.getNumeroManifiesto();
            }

            fGestinarIncidencias.txtVehiculo.setText(fGestinarIncidencias.incidenciaSvc.getObjManifiesto().getVehiculo());
            fGestinarIncidencias.txtConductor.setText(fGestinarIncidencias.incidenciaSvc.getObjManifiesto().getNombreConductor() + " " + fGestinarIncidencias.incidenciaSvc.getObjManifiesto().getApellidosConductor());
            fGestinarIncidencias.txtCelularConductor.setText(fGestinarIncidencias.incidenciaSvc.getObjManifiesto().getTelefonoConductor());
            fGestinarIncidencias.txtNombreRuta.setText(fGestinarIncidencias.incidenciaSvc.getObjManifiesto().getNombreDeRuta());
            fGestinarIncidencias.txtNombreCanal.setText(fGestinarIncidencias.incidenciaSvc.getObjManifiesto().getNombreCanal());
            fGestinarIncidencias.txtNumeroManifiesto.setText(fGestinarIncidencias.incidenciaSvc.getObjManifiesto().getNumeroManifiesto());


            /*Hilo que recupera los movimientos de la facturaActual */
            // new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this.facturaActual)).start();
            fGestinarIncidencias.txtNumeroFactura.setText(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getNumeroDeFactura());
            fGestinarIncidencias.txtDireccionDelCliente.setText(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getDireccionDeCliente());
            fGestinarIncidencias.txtBarrioCliente.setText(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getBarrio());
            fGestinarIncidencias.txtFechaVenta.setText("" + fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getFechaDeVenta());
            fGestinarIncidencias.txtCelularCliente.setText(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getTelefonoCliente());
            fGestinarIncidencias.txtTelefonoDelCliente.setText(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getTelefonoCliente());
            fGestinarIncidencias.txtCelularVendedor.setText(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getTelefonoVendedor());
            fGestinarIncidencias.txtNombreCiudad.setText(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getCiudad());

            /* nombre del cliente y el código entre parétesis*/
            fGestinarIncidencias.txtNombreDelCliente.setText(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getNombreDeCliente() + " (" + fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getCodigoDeCliente()+ ")");
            fGestinarIncidencias.txtNombreDelVendedor.setText(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getVendedor());

            fGestinarIncidencias.txtValorFactura.setText(nf.format(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getValorTotalFactura()));
            fGestinarIncidencias.txtPesoFactura.setText((fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getPesofactura() / 1000) + " Kg");
  
            fGestinarIncidencias.lblCirculoDeProgreso.setVisible(false);
            fGestinarIncidencias.llenarTablaProductosPorFactura();
            fGestinarIncidencias.llenarTablaProductosPorFacturaParciales();
            fGestinarIncidencias.llenarTablaDistribucion();
            fGestinarIncidencias.llenarTablaProductosPorFacturaRechazados();
            fGestinarIncidencias.llenarTablabitacora();

            /*LLena los datos de la pestaña 2 */
            fGestinarIncidencias.txtNumeroFactura1.setText(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getNumeroDeFactura());

            fGestinarIncidencias.cbxTipoMovimiento.setSelectedItem(fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getNombreMovimiento());

            fGestinarIncidencias.txtObservacion.setText(fGestinarIncidencias.incidenciaSvc.getObservaciones());
            System.out.println("Aca verifica que hayan movimientos en la factura " + fGestinarIncidencias.incidenciaSvc.getObjFacturaCamdun().getNumeroDeFactura());

            fGestinarIncidencias.completado = false;

           fGestinarIncidencias.btnHabilitarGestion1.setEnabled(true);
           
           

        } catch (Exception ex) {

        }

    }
    
      private void crearIncidencia() {
       try {
            //incidenciaSvc.setConsecutivo(mensaje);
            for(CMovimientosManifiestosfacturas mov : ini.getListaDeMovimientosFacturas()){
                if(fGestinarIncidencias.cbxTipoMovimiento.getSelectedItem().toString().equals(mov.getNombreMovimientosManifiestosfacturas())){
                    fGestinarIncidencias.incidenciaSvc.setIdtipoDeMovimiento(mov.getIdMovimientosManifiestosfacturas());
                    break;
                }
            }
            
            
            
           
            fGestinarIncidencias.incidenciaSvc.setResponsable("0");
            fGestinarIncidencias.incidenciaSvc.setObservaciones(fGestinarIncidencias.txtObservacion.getText().trim());
            fGestinarIncidencias.incidenciaSvc.setIdEstado(1);
            
            fGestinarIncidencias.incidenciaSvc.setCantidadGestiones(0);
            // incidenciaSvc.setFechaDeCierre(mensaje);
            for(CTiposDePeticion pet : ini.getListaDeTiposDePeticion()){
                if(fGestinarIncidencias.cbxTiposDePeticion.getSelectedItem().toString().equals(pet.getNombreTiposDePeticones())){
                    fGestinarIncidencias.incidenciaSvc.setMedioDePeticion(pet.getIdTiposDePeticones());
                    break;
                }
            }
            
            
            fGestinarIncidencias.incidenciaSvc.setIdMovimientoFinal(0);
            
            fGestinarIncidencias.incidenciaSvc.setActivo(1);
           
            fGestinarIncidencias.incidenciaSvc.setUsuario(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            fGestinarIncidencias.incidenciaSvc.setFlag(1);
            fGestinarIncidencias.incidenciaSvc.grabarIncidencia();
            
            fGestinarIncidencias.btnGrabarIncidencia.setEnabled(true);
        } catch (Exception ex) {
            Logger.getLogger(FGestionarIncidencias.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
