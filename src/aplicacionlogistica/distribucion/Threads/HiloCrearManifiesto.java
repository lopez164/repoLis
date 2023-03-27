/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRutaConIntegrador;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHieleraConIntegracion;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera_2;
import aplicacionlogistica.distribucion.formularios.poblaciones.FManifestarPedidosPoblaciones;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloCrearManifiesto implements Runnable {

    Inicio ini;
    FManifestarPedidosEnRuta fManifestarPedidosEnRuta = null;
    FManifestarPedidosPoblaciones fManifestarPedidosPoblaciones = null;
    FManifestarPedidosHielera fManifestarPedidosHielera = null;
    FDespachoHieleraConIntegracion fDespachoHieleraConIntegracion = null;
    FManifestarPedidosHielera_2 fManifestarPedidosHielera_2 = null;
    FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloCrearManifiesto(Inicio ini, FManifestarPedidosEnRuta form) {

        this.fManifestarPedidosEnRuta = form;
        this.ini = ini;

    } // 

    public HiloCrearManifiesto(Inicio ini, FManifestarPedidosPoblaciones fManifestarPedidosPoblaciones) {

        this.fManifestarPedidosPoblaciones = fManifestarPedidosPoblaciones;
        this.ini = ini;

    } // 
    
     public HiloCrearManifiesto(Inicio ini,FManifestarPedidosHielera fManifestarPedidosHielera) {

        this.fManifestarPedidosHielera = fManifestarPedidosHielera;
        this.ini = ini;

    } // 
     
      public HiloCrearManifiesto(Inicio ini,FDespachoHieleraConIntegracion fDespachoHielera, FManifestarPedidosHielera_2 fDespachoHielera_2) {
        this.ini = ini;
        this.fDespachoHieleraConIntegracion = fDespachoHielera;
        this.fManifestarPedidosHielera_2 = fDespachoHielera_2;
        

    } // 
      
       public HiloCrearManifiesto(Inicio ini, FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador) {

        this.fManifestarPedidosEnRutaConIntegrador = fManifestarPedidosEnRutaConIntegrador;
        this.ini = ini;

    } //

    @Override
    public void run() {

        if (fManifestarPedidosEnRuta != null) {
            manifestarRutasLocales();
        }

        if (fManifestarPedidosPoblaciones != null) {
            manifestarRutasPoblaciones();
        }
        
         if (fDespachoHieleraConIntegracion != null) {
            manifestarRutasHieleraConIntegracion();
        }
          if  (fManifestarPedidosEnRutaConIntegrador != null) {
              manifestarRutasLocalesConIntegrador();
        }

    }

    public void manifestarRutasLocales() {
        try {
            /* S hace visible el gif que indica que hay un proceso en ejecución */
            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(true);

            this.fManifestarPedidosEnRuta.modelo2 = (DefaultTableModel) this.fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.getModel();

            this.fManifestarPedidosEnRuta.btnCrearManifiesto.setEnabled(false);

            this.fManifestarPedidosEnRuta.txtKmDeSalida.setEditable(false);
            this.fManifestarPedidosEnRuta.txtPlaca.setEnabled(false);
            this.fManifestarPedidosEnRuta.txtPlaca.setEditable(false);

            /*  SE LLENAN LAS PROPIEDADES DE  EL MANIFIESTO */
            Date dt = new Date();
            dt = ini.getFechaSql(this.fManifestarPedidosEnRuta.dateManifiesto);
            this.fManifestarPedidosEnRuta.manifiestoActual.setFechaDistribucion("" + dt);

            this.fManifestarPedidosEnRuta.manifiestoActual.setVehiculo(this.fManifestarPedidosEnRuta.txtPlaca.getText().trim());
            this.fManifestarPedidosEnRuta.manifiestoActual.setConductor(this.fManifestarPedidosEnRuta.conductor.getCedula());
            this.fManifestarPedidosEnRuta.manifiestoActual.setNombreConductor(this.fManifestarPedidosEnRuta.conductor.getNombres());
            this.fManifestarPedidosEnRuta.manifiestoActual.setApellidosConductor(this.fManifestarPedidosEnRuta.conductor.getApellidos());

            this.fManifestarPedidosEnRuta.manifiestoActual.setDespachador(this.fManifestarPedidosEnRuta.despachador.getCedula());
            this.fManifestarPedidosEnRuta.manifiestoActual.setNombreDespachador(this.fManifestarPedidosEnRuta.despachador.getNombres());
            this.fManifestarPedidosEnRuta.manifiestoActual.setApellidosDespachador(this.fManifestarPedidosEnRuta.despachador.getApellidos());

            this.fManifestarPedidosEnRuta.manifiestoActual.setIdCanal(this.fManifestarPedidosEnRuta.canalDeVenta.getIdCanalDeVenta());
            this.fManifestarPedidosEnRuta.manifiestoActual.setNombreCanal(this.fManifestarPedidosEnRuta.cbxCanales.getSelectedItem().toString());

            this.fManifestarPedidosEnRuta.manifiestoActual.setIdRuta(this.fManifestarPedidosEnRuta.ruta.getIdRutasDeDistribucion());
            this.fManifestarPedidosEnRuta.manifiestoActual.setNombreDeRuta(this.fManifestarPedidosEnRuta.cbxRutaDeDistribucion.getSelectedItem().toString());

            this.fManifestarPedidosEnRuta.manifiestoActual.setZona(ini.getUser().getZona());
            this.fManifestarPedidosEnRuta.manifiestoActual.setRegional(ini.getUser().getRegional());
            this.fManifestarPedidosEnRuta.manifiestoActual.setAgencia(ini.getUser().getAgencia());

            this.fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(2);
            this.fManifestarPedidosEnRuta.manifiestoActual.setKmSalida(Integer.parseInt(this.fManifestarPedidosEnRuta.txtKmDeSalida.getText().trim()));
            this.fManifestarPedidosEnRuta.manifiestoActual.setKmEntrada(Integer.parseInt(this.fManifestarPedidosEnRuta.txtKmDeSalida.getText().trim()));
            this.fManifestarPedidosEnRuta.manifiestoActual.setKmRecorrido(0);

            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechax;
            fechax = fecha.format(new Date());

            this.fManifestarPedidosEnRuta.manifiestoActual.setHoraDeDespacho("CURRENT_TIMESTAMP");
            this.fManifestarPedidosEnRuta.manifiestoActual.setHoraDeLiquidacion("CURRENT_TIMESTAMP");
            this.fManifestarPedidosEnRuta.manifiestoActual.setIsFree(0);
            this.fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(0.0);
            this.fManifestarPedidosEnRuta.manifiestoActual.setValorRecaudado(0.0);
            this.fManifestarPedidosEnRuta.manifiestoActual.setActivo(1);
            this.fManifestarPedidosEnRuta.manifiestoActual.setUsuarioManifiesto(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            this.fManifestarPedidosEnRuta.manifiestoActual.setObservaciones("NA");
             this.fManifestarPedidosEnRuta.manifiestoActual.setCantDeSalidas(1);

            /*SE ASIGNAN LOS AUXILIARES */
            this.fManifestarPedidosEnRuta.manifiestoActual.setListaDeAuxiliares(this.fManifestarPedidosEnRuta.listaDeAuxiliares);

            // GRABA EL MANIFIESTO DE DISTRIBUCION
            if (this.fManifestarPedidosEnRuta.manifiestoActual.grabarManifiestoDeDistribucion()) {

                this.fManifestarPedidosEnRuta.manifiestoActual.crearRutasDeArchivos();

                /*Se crea la ruta donde se guardarán temporalmente las facturas del manfiesto */
                fManifestarPedidosEnRuta.archivoConListaDeFacturas = new File(this.fManifestarPedidosEnRuta.manifiestoActual.getRutArchivofacturasporManifiesto());

                // HABILITA CAMPO DE TEXTO PARA EMPEZAR A INGRESAR LAS FACTURAS
                this.fManifestarPedidosEnRuta.cbxPrefijos.setEnabled(true);
                this.fManifestarPedidosEnRuta.txtNumeroDeFactura.setEnabled(true);
                this.fManifestarPedidosEnRuta.txtNumeroDeFactura.setEditable(true);

                // SE CODIFICA EL MANIFIESTO Y SE LLENA EL CAMPO DE TEXTO RESPECTIVO
                String cadenaManifiestoCodificado = this.fManifestarPedidosEnRuta.manifiestoActual.codificarManifiesto();
                this.fManifestarPedidosEnRuta.txtNumeroDeManifiesto.setText(cadenaManifiestoCodificado);

                // SE DESHABILITAN LOS COMPONENTES PARA EVITAR CAMBIO DE DATOS
                this.fManifestarPedidosEnRuta.txtKmDeSalida.setEditable(false);

                this.fManifestarPedidosEnRuta.dateManifiesto.setEnabled(false);
                this.fManifestarPedidosEnRuta.cbxCanales.setEnabled(false);
                this.fManifestarPedidosEnRuta.cbxRutaDeDistribucion.setEnabled(false);

                //SE CREAN LOS ARRAY PARA GUARDAR LAS FACTURAS
                List<CFacturasPorManifiesto> lista = new ArrayList();
                this.fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto(lista);
                //this.fManifestarPedidosEnRuta.listaDeCFacturasCamdunEnElManifiesto = new ArrayList<>();//CFacturas

//              this.fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto(this.fManifestarPedidosEnRuta.listaDeCFacturasPorManifiesto);
//              this.fManifestarPedidosEnRuta.manifiestoActual.setListaCFacturasCamdun(this.fManifestarPedidosEnRuta.listaDeCFacturasCamdunEnElManifiesto);
                this.fManifestarPedidosEnRuta.jTabbedPane1.setEnabled(true);
                this.fManifestarPedidosEnRuta.jTabbedPane1.setEnabledAt(this.fManifestarPedidosEnRuta.jTabbedPane1.indexOfComponent(this.fManifestarPedidosEnRuta.pnlAgregarFactura), true);
                this.fManifestarPedidosEnRuta.btnFile.setEnabled(true);
                this.fManifestarPedidosEnRuta.btnAgregarFacturas.setEnabled(true);

                // DE DESHABILITA EL MANIFIESTO PARA EVITAR QUE OTRO USUARIO LO MODIFIQUE.
                this.fManifestarPedidosEnRuta.manifiestoActual.setIsFree(0);
                this.fManifestarPedidosEnRuta.manifiestoActual.liberarManifiesto(false);

                this.fManifestarPedidosEnRuta.lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + this.fManifestarPedidosEnRuta.txtNumeroDeManifiesto.getText().trim());

                this.fManifestarPedidosEnRuta.txtnombreDeConductor.setEditable(false);
                this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setEditable(false);
                this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setEditable(false);
                this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setEditable(false);
                this.fManifestarPedidosEnRuta.txtNombreDedespachador.setEditable(false);

                this.fManifestarPedidosEnRuta.txtBarroCliente.setEditable(false);
                this.fManifestarPedidosEnRuta.txtDireccionCliente.setEditable(false);
                this.fManifestarPedidosEnRuta.txtNombreDeCliente.setEditable(false);
                this.fManifestarPedidosEnRuta.txtTelefonoCliente.setEditable(false);
                this.fManifestarPedidosEnRuta.txtNumeroDeManifiesto.setEditable(false);
                this.fManifestarPedidosEnRuta.txtNombreVendedor.setEditable(false);
                
                this.fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(false);

                JOptionPane.showInternalMessageDialog(this.fManifestarPedidosEnRuta, "Manifiesto guardado correctamente ", "Manifiesto guardado correctamente ", JOptionPane.INFORMATION_MESSAGE);

                /*Se anexa el manifiesto actual a la lista de manifiestos sin descargar*/
                this.ini.getListaDeManifiestossinDescargar().add(this.fManifestarPedidosEnRuta.manifiestoActual);

                this.fManifestarPedidosEnRuta.formaDePago = 1; // contado
               
                this.fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(false);

                this.fManifestarPedidosEnRuta.cbxPrefijos.setEnabled(true);
                this.fManifestarPedidosEnRuta.txtNumeroDeFactura.requestFocus();
                this.fManifestarPedidosEnRuta.txtNumeroDeFactura.requestFocus();
            } else {
                JOptionPane.showInternalMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar manifiesto", "Error ", JOptionPane.ERROR_MESSAGE);
                this.fManifestarPedidosEnRuta.manifiestoActual.liberarManifiesto(true);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloCrearManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosEnRuta.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }
    }
    
    public void manifestarRutasPoblaciones() {
        try {
            /* S hace visible el gif que indica que hay un proceso en ejecución */
            this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso2.setVisible(true);

            this.fManifestarPedidosPoblaciones.modelo2 = (DefaultTableModel) this.fManifestarPedidosPoblaciones.jTableFacturasPorManifiesto.getModel();

            this.fManifestarPedidosPoblaciones.btnCrearManifiesto.setEnabled(false);

            this.fManifestarPedidosPoblaciones.txtKmDeSalida.setEditable(false);
            this.fManifestarPedidosPoblaciones.txtPlaca.setEnabled(false);
            this.fManifestarPedidosPoblaciones.txtPlaca.setEditable(false);

            /*  SE LLENAN LAS PROPIEDADES DE  EL MANIFIESTO */
            this.fManifestarPedidosPoblaciones.manifiestoActual.setFechaDistribucion("CURRENT_TIMESTAMP()");

            Date dt = new Date();
            dt = ini.getFechaSql(this.fManifestarPedidosPoblaciones.dateManifiesto);
            this.fManifestarPedidosPoblaciones.manifiestoActual.setFechaDistribucion("CURRENT_TIMESTAMP()");

            this.fManifestarPedidosPoblaciones.manifiestoActual.setVehiculo(this.fManifestarPedidosPoblaciones.txtPlaca.getText().trim());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setConductor(this.fManifestarPedidosPoblaciones.conductor.getCedula());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setNombreConductor(this.fManifestarPedidosPoblaciones.conductor.getNombres());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setApellidosConductor(this.fManifestarPedidosPoblaciones.conductor.getApellidos());

            this.fManifestarPedidosPoblaciones.manifiestoActual.setDespachador(this.fManifestarPedidosPoblaciones.despachador.getCedula());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setNombreDespachador(this.fManifestarPedidosPoblaciones.despachador.getNombres());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setApellidosDespachador(this.fManifestarPedidosPoblaciones.despachador.getApellidos());

            this.fManifestarPedidosPoblaciones.manifiestoActual.setIdCanal(this.fManifestarPedidosPoblaciones.canalDeVenta.getIdCanalDeVenta());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setNombreCanal(this.fManifestarPedidosPoblaciones.cbxCanales.getSelectedItem().toString());

            this.fManifestarPedidosPoblaciones.manifiestoActual.setIdRuta(this.fManifestarPedidosPoblaciones.ruta.getIdRutasDeDistribucion());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setNombreDeRuta(this.fManifestarPedidosPoblaciones.cbxRutaDeDistribucion.getSelectedItem().toString());

            this.fManifestarPedidosPoblaciones.manifiestoActual.setZona(ini.getUser().getZona());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setRegional(ini.getUser().getRegional());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setAgencia(ini.getUser().getAgencia());

            this.fManifestarPedidosPoblaciones.manifiestoActual.setEstadoManifiesto(2);
            this.fManifestarPedidosPoblaciones.manifiestoActual.setKmSalida(Integer.parseInt(this.fManifestarPedidosPoblaciones.txtKmDeSalida.getText().trim()));
            this.fManifestarPedidosPoblaciones.manifiestoActual.setKmEntrada(Integer.parseInt(this.fManifestarPedidosPoblaciones.txtKmDeSalida.getText().trim()));
            this.fManifestarPedidosPoblaciones.manifiestoActual.setKmRecorrido(0);

            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechax;
            fechax = fecha.format(new Date());

            this.fManifestarPedidosPoblaciones.manifiestoActual.setHoraDeDespacho("" + ini.getFechaSql());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setHoraDeLiquidacion("" + ini.getFechaSql());
            this.fManifestarPedidosPoblaciones.manifiestoActual.setIsFree(0);
            this.fManifestarPedidosPoblaciones.manifiestoActual.setValorTotalManifiesto(0.0);
            this.fManifestarPedidosPoblaciones.manifiestoActual.setValorRecaudado(0.0);
            this.fManifestarPedidosPoblaciones.manifiestoActual.setActivo(1);
            this.fManifestarPedidosPoblaciones.manifiestoActual.setUsuarioManifiesto(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));

            /*SE ASIGNAN LOS AUXILIARES */
            this.fManifestarPedidosPoblaciones.manifiestoActual.setListaDeAuxiliares(this.fManifestarPedidosPoblaciones.listaDeAuxiliares);

            // GRABA EL MANIFIESTO DE DISTRIBUCION
            if (this.fManifestarPedidosPoblaciones.manifiestoActual.grabarManifiestoDeDistribucion()) {
              

                this.fManifestarPedidosPoblaciones.manifiestoActual.crearRutasDeArchivos();

                /*Se crea la ruta donde se guardarán temporalmente las facturas del manfiesto */
                fManifestarPedidosPoblaciones.archivoConListaDeFacturas = new File(this.fManifestarPedidosPoblaciones.manifiestoActual.getRutArchivofacturasporManifiesto());

                // HABILITA CAMPO DE TEXTO PARA EMPEZAR A INGRESAR LAS FACTURAS
                this.fManifestarPedidosEnRuta.cbxPrefijos.setEnabled(true);
                this.fManifestarPedidosPoblaciones.txtNumeroDeFactura.setEnabled(true);
                this.fManifestarPedidosPoblaciones.txtNumeroDeFactura.setEditable(true);

                // SE CODIFICA EL MANIFIESTO Y SE LLENA EL CAMPO DE TEXTO RESPECTIVO
                String cadenaManifiestoCodificado = this.fManifestarPedidosPoblaciones.manifiestoActual.codificarManifiesto();
                this.fManifestarPedidosPoblaciones.txtNumeroDeManifiesto.setText(cadenaManifiestoCodificado);

                // SE DESHABILITAN LOS COMPONENTES PARA EVITAR CAMBIO DE DATOS
                this.fManifestarPedidosPoblaciones.txtKmDeSalida.setEditable(false);

                this.fManifestarPedidosPoblaciones.dateManifiesto.setEnabled(false);
                this.fManifestarPedidosPoblaciones.cbxCanales.setEnabled(false);
                this.fManifestarPedidosPoblaciones.cbxRutaDeDistribucion.setEnabled(false);

                //SE CREAN LOS ARRAY PARA GUARDAR LAS FACTURAS
                List<CFacturasPorManifiesto> lista = new ArrayList();
                this.fManifestarPedidosPoblaciones.manifiestoActual.setListaFacturasPorManifiesto(lista);
                //this.fManifestarPedidosPoblaciones.listaDeCFacturasCamdunEnElManifiesto = new ArrayList<>();//CFacturas

//                this.fManifestarPedidosPoblaciones.manifiestoActual.setListaFacturasPorManifiesto(this.fManifestarPedidosPoblaciones.listaDeCFacturasPorManifiesto);
//                this.fManifestarPedidosPoblaciones.manifiestoActual.setListaCFacturasCamdun(this.fManifestarPedidosPoblaciones.listaDeCFacturasCamdunEnElManifiesto);
                this.fManifestarPedidosPoblaciones.jTabbedPane1.setEnabled(true);
                this.fManifestarPedidosPoblaciones.jTabbedPane1.setEnabledAt(this.fManifestarPedidosPoblaciones.jTabbedPane1.indexOfComponent(this.fManifestarPedidosPoblaciones.pnlAgregarFactura), true);
                 //this.fManifestarPedidosPoblaciones.btnFile.setEnabled(true);
                 //this.fManifestarPedidosPoblaciones.btnAgregarFacturas.setEnabled(true);

                 // DE DESHABILITA EL MANIFIESTO PARA EVITAR QUE OTRO USUARIO LO MODIFIQUE.
                this.fManifestarPedidosPoblaciones.manifiestoActual.setIsFree(0);
                this.fManifestarPedidosPoblaciones.manifiestoActual.liberarManifiesto(false);

                this.fManifestarPedidosPoblaciones.lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + this.fManifestarPedidosPoblaciones.txtNumeroDeManifiesto.getText().trim());

                this.fManifestarPedidosPoblaciones.txtnombreDeConductor.setEditable(false);
                this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar1.setEditable(false);
                this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar2.setEditable(false);
                this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar3.setEditable(false);
                this.fManifestarPedidosPoblaciones.txtNombreDedespachador.setEditable(false);
                this.fManifestarPedidosPoblaciones.txtNumeroDeManifiesto.setEditable(false);

                
                this.fManifestarPedidosPoblaciones.habilitarCajasDeTexto(true);
                
              
                
                this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso2.setVisible(false);

                JOptionPane.showInternalMessageDialog(this.fManifestarPedidosPoblaciones, "Manifiesto guardado correctamente ", "Manifiesto guardado correctamente ", JOptionPane.INFORMATION_MESSAGE);

                /*Se anexa el manifiesto actual a la lista de manifiestos sin descargar*/
                this.ini.getListaDeManifiestossinDescargar().add(this.fManifestarPedidosPoblaciones.manifiestoActual);

                this.fManifestarPedidosPoblaciones.formaDePago = 1; // contado

                this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso2.setVisible(false);

                this.fManifestarPedidosPoblaciones.txtNumeroDeFactura.requestFocus();
                this.fManifestarPedidosPoblaciones.txtNumeroDeFactura.requestFocus();
                
            } else {
                JOptionPane.showInternalMessageDialog(this.fManifestarPedidosPoblaciones, "Error al guardar manifiesto", "Error ", JOptionPane.ERROR_MESSAGE);
                this.fManifestarPedidosPoblaciones.manifiestoActual.liberarManifiesto(true);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloCrearManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosPoblaciones.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }
    }
    
    public void manifestarRutasHieleraConIntegracion() {
        try {
            /* S hace visible el gif que indica que hay un proceso en ejecución */
            this.fDespachoHieleraConIntegracion.lblCirculoDeProgreso2.setVisible(true);

            this.fDespachoHieleraConIntegracion.modelo2 = (DefaultTableModel) this.fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getModel();

            this.fDespachoHieleraConIntegracion.btnCrearManifiesto.setEnabled(false);

            this.fDespachoHieleraConIntegracion.txtKmDeSalida.setEditable(false);
            this.fDespachoHieleraConIntegracion.txtPlaca.setEnabled(false);
            this.fDespachoHieleraConIntegracion.txtPlaca.setEditable(false);

            /*  SE LLENAN LAS PROPIEDADES DE  EL MANIFIESTO */
            Date dt = new Date();
            dt = Inicio.getFechaSql(this.fDespachoHieleraConIntegracion.dateManifiesto);
            this.fDespachoHieleraConIntegracion.manifiestoActual.setFechaDistribucion("" + dt);

            this.fDespachoHieleraConIntegracion.manifiestoActual.setVehiculo(this.fDespachoHieleraConIntegracion.txtPlaca.getText().trim());
            this.fDespachoHieleraConIntegracion.manifiestoActual.setConductor(this.fDespachoHieleraConIntegracion.conductor.getCedula());
            this.fDespachoHieleraConIntegracion.manifiestoActual.setNombreConductor(this.fDespachoHieleraConIntegracion.conductor.getNombres());
            this.fDespachoHieleraConIntegracion.manifiestoActual.setApellidosConductor(this.fDespachoHieleraConIntegracion.conductor.getApellidos());

            this.fDespachoHieleraConIntegracion.manifiestoActual.setDespachador(this.fDespachoHieleraConIntegracion.despachador.getCedula());
            this.fDespachoHieleraConIntegracion.manifiestoActual.setNombreDespachador(this.fDespachoHieleraConIntegracion.despachador.getNombres());
            this.fDespachoHieleraConIntegracion.manifiestoActual.setApellidosDespachador(this.fDespachoHieleraConIntegracion.despachador.getApellidos());

            this.fDespachoHieleraConIntegracion.manifiestoActual.setIdCanal(this.fDespachoHieleraConIntegracion.canalDeVenta.getIdCanalDeVenta());
            this.fDespachoHieleraConIntegracion.manifiestoActual.setNombreCanal(this.fDespachoHieleraConIntegracion.cbxCanales.getSelectedItem().toString());

            this.fDespachoHieleraConIntegracion.manifiestoActual.setIdRuta(this.fDespachoHieleraConIntegracion.ruta.getIdRutasDeDistribucion());
            this.fDespachoHieleraConIntegracion.manifiestoActual.setNombreDeRuta(this.fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.getSelectedItem().toString());

            this.fDespachoHieleraConIntegracion.manifiestoActual.setZona(ini.getUser().getZona());
            this.fDespachoHieleraConIntegracion.manifiestoActual.setRegional(ini.getUser().getRegional());
            this.fDespachoHieleraConIntegracion.manifiestoActual.setAgencia(ini.getUser().getAgencia());

            this.fDespachoHieleraConIntegracion.manifiestoActual.setEstadoManifiesto(2);
            this.fDespachoHieleraConIntegracion.manifiestoActual.setKmSalida(Integer.parseInt(this.fDespachoHieleraConIntegracion.txtKmDeSalida.getText().trim()));
            this.fDespachoHieleraConIntegracion.manifiestoActual.setKmEntrada(Integer.parseInt(this.fDespachoHieleraConIntegracion.txtKmDeSalida.getText().trim()));
            this.fDespachoHieleraConIntegracion.manifiestoActual.setKmRecorrido(0);

            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechax;
            fechax = fecha.format(new Date());

            this.fDespachoHieleraConIntegracion.manifiestoActual.setHoraDeDespacho("CURRENT_TIMESTAMP()");
            this.fDespachoHieleraConIntegracion.manifiestoActual.setHoraDeLiquidacion("CURRENT_TIMESTAMP()");
            this.fDespachoHieleraConIntegracion.manifiestoActual.setIsFree(1);
            this.fDespachoHieleraConIntegracion.manifiestoActual.setValorTotalManifiesto(0.0);
            this.fDespachoHieleraConIntegracion.manifiestoActual.setValorRecaudado(0.0);
            this.fDespachoHieleraConIntegracion.manifiestoActual.setActivo(1);
            this.fDespachoHieleraConIntegracion.manifiestoActual.setUsuarioManifiesto(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            this.fDespachoHieleraConIntegracion.manifiestoActual.setObservaciones("NA  .");

            /*SE ASIGNAN LOS AUXILIARES */
            this.fDespachoHieleraConIntegracion.manifiestoActual.setListaDeAuxiliares(this.fDespachoHieleraConIntegracion.listaDeAuxiliares);

            // GRABA EL MANIFIESTO DE DISTRIBUCION
            if (this.fDespachoHieleraConIntegracion.manifiestoActual.grabarManifiestoDeDistribucion()) {
             // if (true) {
                this.fDespachoHieleraConIntegracion.setTitle(this.fDespachoHieleraConIntegracion.conductor. getNombres() + " " + this.fDespachoHieleraConIntegracion.conductor. getApellidos());
                
                this.fDespachoHieleraConIntegracion.manifiestoActual.crearRutasDeArchivos();
                
                
                  this.fManifestarPedidosHielera_2.listaDeConductores.add(this.fDespachoHieleraConIntegracion.conductor.getNombres() + " " 
                          + this.fDespachoHieleraConIntegracion.conductor.getApellidos() + "-" + this.fDespachoHieleraConIntegracion.carro.getPlaca() + "-" 
                          + this.fDespachoHieleraConIntegracion.manifiestoActual.getNumeroManifiesto());
                  
                  this.fManifestarPedidosHielera_2.listaDeFormulariosManifiestos.add(this.fDespachoHieleraConIntegracion);
                  
                 this.fDespachoHieleraConIntegracion.anexarElementoALalista();
                 
                 new Thread(new HiloListadoDeManifiestosSinDescargar(ini,3,true)).start();


                /*Se crea la ruta donde se guardarán temporalmente las facturas del manfiesto */
               // fDespachoHieleraConIntegracion.archivoConListaDeFacturas = new File(this.fDespachoHieleraConIntegracion.manifiestoActual.getRutArchivofacturasporManifiesto());

                // HABILITA CAMPO DE TEXTO PARA EMPEZAR A INGRESAR LAS FACTURAS
                this.fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(true);
                this.fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                this.fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(true);

                // SE CODIFICA EL MANIFIESTO Y SE LLENA EL CAMPO DE TEXTO RESPECTIVO
                String cadenaManifiestoCodificado = this.fDespachoHieleraConIntegracion.manifiestoActual.codificarManifiesto();
                this.fDespachoHieleraConIntegracion.txtNumeroDeManifiesto.setText(cadenaManifiestoCodificado);

                // SE DESHABILITAN LOS COMPONENTES PARA EVITAR CAMBIO DE DATOS
                this.fDespachoHieleraConIntegracion.txtKmDeSalida.setEditable(false);

                this.fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);
                this.fDespachoHieleraConIntegracion.cbxCanales.setEnabled(false);
                this.fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setEnabled(false);

                //SE CREAN LOS ARRAY PARA GUARDAR LAS FACTURAS
                List<CFacturasPorManifiesto> lista = new ArrayList();
                this.fDespachoHieleraConIntegracion.manifiestoActual.setListaFacturasPorManifiesto(lista);
                //this.fDespachoHieleraConIntegracion.listaDeCFacturasCamdunEnElManifiesto = new ArrayList<>();//CFacturas

//              this.fDespachoHieleraConIntegracion.manifiestoActual.setListaFacturasPorManifiesto(this.fDespachoHieleraConIntegracion.listaDeCFacturasPorManifiesto);
//              this.fDespachoHieleraConIntegracion.manifiestoActual.setListaCFacturasCamdun(this.fDespachoHieleraConIntegracion.listaDeCFacturasCamdunEnElManifiesto);
                this.fDespachoHieleraConIntegracion.jTabbedPane1.setEnabled(true);
                this.fDespachoHieleraConIntegracion.jTabbedPane1.setEnabledAt(this.fDespachoHieleraConIntegracion.jTabbedPane1.indexOfComponent(this.fDespachoHieleraConIntegracion.pnlAgregarFactura), true);
               

                // DE DESHABILITA EL MANIFIESTO PARA EVITAR QUE OTRO USUARIO LO MODIFIQUE.
                this.fDespachoHieleraConIntegracion.manifiestoActual.setIsFree(0);
                this.fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(false);

               // this.fDespachoHieleraConIntegracion.lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + this.fDespachoHieleraConIntegracion.txtNumeroDeManifiesto.getText().trim());

                this.fDespachoHieleraConIntegracion.txtnombreDeConductor.setEditable(false);
                this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setEditable(false);
                this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setEditable(false);
                this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setEditable(false);
                this.fDespachoHieleraConIntegracion.txtNombreDedespachador.setEditable(false);

                this.fDespachoHieleraConIntegracion.txtBarroCliente.setEditable(false);
                this.fDespachoHieleraConIntegracion.txtDireccionCliente.setEditable(false);
                this.fDespachoHieleraConIntegracion.txtNombreDeCliente.setEditable(false);
                this.fDespachoHieleraConIntegracion.txtTelefonoCliente.setEditable(false);
                this.fDespachoHieleraConIntegracion.txtNumeroDeManifiesto.setEditable(false);
                this.fDespachoHieleraConIntegracion.txtNombreVendedor.setEditable(false);
                
                this.fDespachoHieleraConIntegracion.lblCirculoDeProgreso2.setVisible(false);

                JOptionPane.showInternalMessageDialog(this.fDespachoHieleraConIntegracion, "Manifiesto guardado correctamente ", "Manifiesto guardado correctamente ", JOptionPane.INFORMATION_MESSAGE);

                /*Se anexa el manifiesto actual a la lista de manifiestos sin descargar*/
                this.ini.getListaDeManifiestossinDescargar().add(this.fDespachoHieleraConIntegracion.manifiestoActual);

                this.fDespachoHieleraConIntegracion.formaDePago =""; // contado
               
                this.fDespachoHieleraConIntegracion.lblCirculoDeProgreso2.setVisible(false);

                this.fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(true);
                this.fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                this.fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
            } else {
                JOptionPane.showInternalMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar manifiesto", "Error ", JOptionPane.ERROR_MESSAGE);
                this.fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(true);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloCrearManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            this.fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }
    }
     
    public void manifestarRutasLocalesConIntegrador() {
        try {
            /* S hace visible el gif que indica que hay un proceso en ejecución */
            this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso2.setVisible(true);

            this.fManifestarPedidosEnRutaConIntegrador.modelo2 = (DefaultTableModel) this.fManifestarPedidosEnRutaConIntegrador.jTableFacturasPorManifiesto.getModel();

            this.fManifestarPedidosEnRutaConIntegrador.btnCrearManifiesto.setEnabled(false);

            this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.setEditable(false);
            this.fManifestarPedidosEnRutaConIntegrador.txtPlaca.setEnabled(false);
            this.fManifestarPedidosEnRutaConIntegrador.txtPlaca.setEditable(false);

            /*  SE LLENAN LAS PROPIEDADES DE  EL MANIFIESTO */
            Date dt = new Date();
            dt = ini.getFechaSql(this.fManifestarPedidosEnRutaConIntegrador.dateManifiesto);
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setFechaDistribucion("" + dt);

            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setVehiculo(this.fManifestarPedidosEnRutaConIntegrador.txtPlaca.getText().trim());
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setConductor(this.fManifestarPedidosEnRutaConIntegrador.conductor.getCedula());
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setNombreConductor(this.fManifestarPedidosEnRutaConIntegrador.conductor.getNombres());
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setApellidosConductor(this.fManifestarPedidosEnRutaConIntegrador.conductor.getApellidos());

            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setDespachador(this.fManifestarPedidosEnRutaConIntegrador.despachador.getCedula());
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setNombreDespachador(this.fManifestarPedidosEnRutaConIntegrador.despachador.getNombres());
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setApellidosDespachador(this.fManifestarPedidosEnRutaConIntegrador.despachador.getApellidos());

            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setIdCanal(this.fManifestarPedidosEnRutaConIntegrador.canalDeVenta.getIdCanalDeVenta());
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setNombreCanal(this.fManifestarPedidosEnRutaConIntegrador.cbxCanales.getSelectedItem().toString());

            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setIdRuta(this.fManifestarPedidosEnRutaConIntegrador.ruta.getIdRutasDeDistribucion());
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setNombreDeRuta(this.fManifestarPedidosEnRutaConIntegrador.cbxRutaDeDistribucion.getSelectedItem().toString());

            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setZona(ini.getUser().getZona());
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setRegional(ini.getUser().getRegional());
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setAgencia(ini.getUser().getAgencia());

            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setEstadoManifiesto(2);
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setKmSalida(Integer.parseInt(this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.getText().trim()));
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setKmEntrada(Integer.parseInt(this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.getText().trim()));
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setKmRecorrido(0);

            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechax;
            fechax = fecha.format(new Date());

            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setHoraDeDespacho("CURRENT_TIMESTAMP");
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setHoraDeLiquidacion("CURRENT_TIMESTAMP");
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setIsFree(0);
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setValorTotalManifiesto(0.0);
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setValorRecaudado(0.0);
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setActivo(1);
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setUsuarioManifiesto(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setObservaciones("NA");
             this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setCantDeSalidas(1);

            /*SE ASIGNAN LOS AUXILIARES */
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setListaDeAuxiliares(this.fManifestarPedidosEnRutaConIntegrador.listaDeAuxiliares);

            // GRABA EL MANIFIESTO DE DISTRIBUCION
            if (this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.grabarManifiestoDeDistribucion()) {

                this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.crearRutasDeArchivos();

                /*Se crea la ruta donde se guardarán temporalmente las facturas del manfiesto */
                fManifestarPedidosEnRutaConIntegrador.archivoConListaDeFacturas = new File(this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getRutArchivofacturasporManifiesto());

                // HABILITA CAMPO DE TEXTO PARA EMPEZAR A INGRESAR LAS FACTURAS
                this.fManifestarPedidosEnRutaConIntegrador.cbxPrefijos.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.txtNumeroDeFactura.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.txtNumeroDeFactura.setEditable(true);

                // SE CODIFICA EL MANIFIESTO Y SE LLENA EL CAMPO DE TEXTO RESPECTIVO
                String cadenaManifiestoCodificado = this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.codificarManifiesto();
                this.fManifestarPedidosEnRutaConIntegrador.txtNumeroDeManifiesto.setText(cadenaManifiestoCodificado);

                // SE DESHABILITAN LOS COMPONENTES PARA EVITAR CAMBIO DE DATOS
                this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.setEditable(false);

                this.fManifestarPedidosEnRutaConIntegrador.dateManifiesto.setEnabled(false);
                this.fManifestarPedidosEnRutaConIntegrador.cbxCanales.setEnabled(false);
                this.fManifestarPedidosEnRutaConIntegrador.cbxRutaDeDistribucion.setEnabled(false);

                //SE CREAN LOS ARRAY PARA GUARDAR LAS FACTURAS
                List<CFacturasPorManifiesto> lista = new ArrayList();
                this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setListaFacturasPorManifiesto(lista);
                //this.fManifestarPedidosEnRutaConIntegrador.listaDeCFacturasCamdunEnElManifiesto = new ArrayList<>();//CFacturas

//              this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setListaFacturasPorManifiesto(this.fManifestarPedidosEnRutaConIntegrador.listaDeCFacturasPorManifiesto);
//              this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setListaCFacturasCamdun(this.fManifestarPedidosEnRutaConIntegrador.listaDeCFacturasCamdunEnElManifiesto);
                this.fManifestarPedidosEnRutaConIntegrador.jTabbedPane1.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.jTabbedPane1.setEnabledAt(this.fManifestarPedidosEnRutaConIntegrador.jTabbedPane1.indexOfComponent(this.fManifestarPedidosEnRutaConIntegrador.pnlAgregarFactura), true);
                this.fManifestarPedidosEnRutaConIntegrador.btnFile.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.btnAgregarFacturas.setEnabled(true);

                // DE DESHABILITA EL MANIFIESTO PARA EVITAR QUE OTRO USUARIO LO MODIFIQUE.
                this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setIsFree(0);
                this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.liberarManifiesto(false);

                this.fManifestarPedidosEnRutaConIntegrador.lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + this.fManifestarPedidosEnRutaConIntegrador.txtNumeroDeManifiesto.getText().trim());

                this.fManifestarPedidosEnRutaConIntegrador.txtnombreDeConductor.setEditable(false);
                this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar1.setEditable(false);
                this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar2.setEditable(false);
                this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar3.setEditable(false);
                this.fManifestarPedidosEnRutaConIntegrador.txtNombreDedespachador.setEditable(false);

                this.fManifestarPedidosEnRutaConIntegrador.txtBarroCliente.setEditable(false);
                this.fManifestarPedidosEnRutaConIntegrador.txtDireccionCliente.setEditable(false);
                this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeCliente.setEditable(false);
                this.fManifestarPedidosEnRutaConIntegrador.txtTelefonoCliente.setEditable(false);
                this.fManifestarPedidosEnRutaConIntegrador.txtNumeroDeManifiesto.setEditable(false);
                this.fManifestarPedidosEnRutaConIntegrador.txtNombreVendedor.setEditable(false);
                
                this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso2.setVisible(false);

                JOptionPane.showInternalMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Manifiesto guardado correctamente ", "Manifiesto guardado correctamente ", JOptionPane.INFORMATION_MESSAGE);

                /*Se anexa el manifiesto actual a la lista de manifiestos sin descargar*/
                this.ini.getListaDeManifiestossinDescargar().add(this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual);

                this.fManifestarPedidosEnRutaConIntegrador.formaDePago ="CONTADO"; // contado
               
                this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso2.setVisible(false);

                this.fManifestarPedidosEnRutaConIntegrador.cbxPrefijos.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.txtNumeroDeFactura.requestFocus();
                this.fManifestarPedidosEnRutaConIntegrador.txtNumeroDeFactura.requestFocus();
            } else {
                JOptionPane.showInternalMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Error al guardar manifiesto", "Error ", JOptionPane.ERROR_MESSAGE);
                this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.liberarManifiesto(true);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloCrearManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }
    }
   
     
       
}
