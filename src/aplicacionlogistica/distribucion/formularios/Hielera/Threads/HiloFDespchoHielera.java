/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloGuardarFacturasPorManifiesto_2;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHielera;
import static aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHielera.band;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHieleraConIntegracion;
import java.awt.HeadlessException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloFDespchoHielera implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FDespachoHieleraConIntegracion fDespachoHielera = null;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFDespchoHielera(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fDespachoHielera
     * @param comando
     */
    public HiloFDespchoHielera(Inicio ini, FDespachoHieleraConIntegracion fDespachoHielera, String comando) {
        this.ini = ini;
        this.fDespachoHielera = fDespachoHielera;
        this.caso = comando;
    }

    @Override
    public void run() {
try{
        if (caso != null) {
            switch (caso) {
                case "descargarFactura":
                    grabar();
                break;
                case "asignarTipoDescargue":
                    //llenarjTableFacturasPorVehiculo();
                   // asignarTipoDescargue();
                break;
                case "buscarManifiesto": 
                    
                break;
                case "salir":
                   // salir();
                break;
                case "nuevo":
                   // nuevo();
                break;
                case "grabar":
                    grabar();
                break;
                case "cancelar":
                    //cancelar();
                break;
                case "agregarLafactura":
                    //consultarLafactura();
                   // descargarFactura();
                break;
                case "imprimir":
                   // imprimir();
                break;
                case "gestionarRecogida":
                  // gestionarRecogida();
                break;
                case "gestionarRechazoTotal": 
                   // descargarRechazosTotales();   
                break;
                case "borrarTodasLasFacturas":
                   // borrarTodasLasFacturas();
                    break;
                case "borrarUnaFactura":
                   // borrarUnaFactura();
                break;
                case "getionarSoporteConsignacion":
                    //getionarSoporteConsignacion();
                break;
                
               default:
                    JOptionPane.showInternalMessageDialog(fDespachoHielera, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);


            }
        }
}catch (Exception ex) {
                    Logger.getLogger(HiloFDespchoHielera.class.getName()).log(Level.SEVERE, null, ex);
                }
    }

 public boolean grabar() throws HeadlessException {
        int deseaGrabarRegistro;

        // String a = manifiestoActual.getObservaciones();
        /*valida sin pendientes del conductor */
        if (fDespachoHielera.manifiestoActual.getObservaciones().length() <= 2) {
            // if (manifiestoActual.getCantidadManifiestosPendientesConductor() > 0) {
            fDespachoHielera.fManifestarPedidosHielera_2.msgCantidadDeCanastas();
           // JOptionPane.showMessageDialog(this, "El manifiesto no tiene la cantidad de Canastas en ruta", "Manifiesto incompleto", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        if (fDespachoHielera.manifiestoActual.getEstadoManifiesto() == 2 || fDespachoHielera.manifiestoActual.getEstadoManifiesto() == 3) {  // IINICIO DEL IF-> ESTADO DEL MANIFIESTO

            //deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);
            //if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {
            if (true) {

                fDespachoHielera.manifiestoActual.setCantidadPedidos(fDespachoHielera.manifiestoActual.getListaFacturasPorManifiesto().size());


                /* Se valida la conexión a internet para grabar los datos en la BBDD */
                if (ini.verificarConexion()) {

                    band = true;

                   // new Thread(new HiloGuardarFacturasPorManifiesto_2(ini, fDespachoHielera, fDespachoHielera.fManifestarPedidosHielera_2)).start();
//                    new Thread(new JcProgressBar(brrProgreso, 100)).start();
                      grabarSalidasDomicilios();

                } else {
                    fDespachoHielera.fManifestarPedidosHielera_2.msgSinInternet();
                    //JOptionPane.showMessageDialog(null, "No hay conexión a internet", "Error al guardar Datos", JOptionPane.WARNING_MESSAGE, null);
                }

            }
        } else {

            fDespachoHielera.btnGrabar.setEnabled(false);
            fDespachoHielera.jBtnGrabar.setEnabled(false);
            fDespachoHielera.jBtnObservaciones.setEnabled(false);

            fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
            fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
            fDespachoHielera.fManifestarPedidosHielera_2.msgErrorAlguardar("");
// JOptionPane.showMessageDialog(null, "Error al guardar los datos, manifiesto  ya está en distribución", "manifiesto ya fue guardado", JOptionPane.ERROR_MESSAGE, null);

        }  // FIN DEL IF-> ESTADO DEL MANIFIESTO
        return false;
    } 
  public void grabarSalidasDomicilios() throws HeadlessException {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
               
        this.fDespachoHielera.btnImprimir.setEnabled(false);
        this.fDespachoHielera.jBtnImprimir.setEnabled(false);

        this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        this.fDespachoHielera.repaint();

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto_2.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fDespachoHielera.btnGrabar.setEnabled(false);
        this.fDespachoHielera.jBtnGrabar.setEnabled(false);
        this.fDespachoHielera.jBtnObservaciones.setEnabled(false);
        
        //this.fDespachoHielera.txtNumeroDeFactura.setEnabled(false);
        //this.fDespachoHielera.pnlAgregarLista.setEnabled(false);

        this.fDespachoHielera.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;
        
        
        /* Se cambia el estado de manifiesto a manifiesto cerrado*/
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (this.fDespachoHielera.manifiestoActual.grabarFacturasPorManifiesto()) {
                this.fDespachoHielera.fManifestarPedidosHielera_2.jBtnNuevo.setEnabled(true);
                
               new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3, true)).start();
          
                this.fDespachoHielera.llenarJtableFacturasXmanifiesto();
                
                this.fDespachoHielera.cantDeSalidas++;
                                             
                //fDespachoHielera.manifiestoActual.setEstadoManifiesto(3);
               // fDespachoHielera.manifiestoActual.setIsFree(1);
                 fDespachoHielera.manifiestoActual.setCantDeSalidas( this.fDespachoHielera.cantDeSalidas);
               // fDespachoHielera.manifiestoActual.actualizarManifiestoDeDistribucions();
                
               // fDespachoHielera.manifiestoActual.setHoraDeDespacho("" + new Date());
               // fDespachoHielera.manifiestoActual.setPesoKgManifiesto(fDespachoHielera.sumadorPesosFacturas);
                
                this.fDespachoHielera.estaOcupadoGrabando = false;
                this.fDespachoHielera.btnImprimir.setEnabled(true);
                this.fDespachoHielera.btnGrabar.setEnabled(false);
                
                
                this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                                
                //JOptionPane.showMessageDialog(this.fDespachoHielera, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                this.fDespachoHielera.jBtnMinuta.setEnabled(true);
                
                this.fDespachoHielera.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {

                this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                
                
                this.fDespachoHielera.btnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fDespachoHielera, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fDespachoHielera, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fDespachoHielera.btnGrabar.setEnabled(true);
            this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            
            
        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto_2.class.getName()).log(Level.SEVERE, null, ex);
            this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            
        }
        
        this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
    }

      
}
