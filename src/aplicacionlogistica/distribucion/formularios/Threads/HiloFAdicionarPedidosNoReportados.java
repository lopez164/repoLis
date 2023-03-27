/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Threads;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.FAdicionarPedidosNoReportados;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import java.awt.HeadlessException;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
public class HiloFAdicionarPedidosNoReportados implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FAdicionarPedidosNoReportados fAdicionarPedidosNoReportados = null;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFAdicionarPedidosNoReportados(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fManifestarPedidosEnRuta
     * @param comando
     */
    public HiloFAdicionarPedidosNoReportados(Inicio ini, FAdicionarPedidosNoReportados fManifestarPedidosEnRuta, String comando) {
        this.ini = ini;
        this.fAdicionarPedidosNoReportados = fManifestarPedidosEnRuta;
        this.caso = comando;
    }

    @Override
    public void run() {

        if (caso != null) {
            switch (caso) {
                case "consultarManifiesto":
                    consultarManifiesto();
                    break;

                case "funcionAgregarFactura":
                    funcionAgregarFactura();
                    break;

                case "sacarMinuta":
                    sacarMinuta();
                    break;
                case "cargarVista":
                    cargarVista();
                    break;

                case "grabarFacturasPorManifiesto":
                    grabarFacturasPorManifiesto();
                    break;

                default:
                    JOptionPane.showInternalMessageDialog(fAdicionarPedidosNoReportados, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    public void funcionAgregarFactura() {
        fAdicionarPedidosNoReportados.facturaActual = null;
        String numeroDeFactura = fAdicionarPedidosNoReportados.txtNumeroDeFactura.getText().trim();
        //CFacturasCamdun factura;

        try {

            /* se crea el objeto factura, digitado en el campo de texto */
            fAdicionarPedidosNoReportados.facturaActual = new CFacturas(ini, numeroDeFactura);
            // Vst_FacturasPorManifiesto vfxm = new Vst_FacturasPorManifiesto();

            /* SE VALIDA QUE LA FACTURA EXISTA  */
            if (fAdicionarPedidosNoReportados.facturaActual.getNumeroDeFactura() != null) {
                //factura = new CFacturas(ini, numeroDeFactura);

                /*Se valida el tipo de movimiento de la factura*/
                switch (fAdicionarPedidosNoReportados.facturaActual.getEstadoFactura()) {

                    case 2:
                        /*Entrega total*/
                        JOptionPane.showInternalMessageDialog(fAdicionarPedidosNoReportados, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);
                        return;

                    case 3:
                        /*Devolucion total*/
                        JOptionPane.showInternalMessageDialog(fAdicionarPedidosNoReportados, "La Factura  # " + numeroDeFactura + " fue devuelta de ruta", "Error, factura Devuelta", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 4:/*Entrega con novedad*/
                        JOptionPane.showInternalMessageDialog(fAdicionarPedidosNoReportados, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 5:/*Entrega total con recogida*/
                        JOptionPane.showInternalMessageDialog(fAdicionarPedidosNoReportados, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 6:
                        /*Re Envio*/
                        break;

                }

                /* se verifica que la factura esté libre, sino arroja el siguiente mensaje... 
                     =0 indica que la factura esta siendo ocupada por alguien  */
                if (fAdicionarPedidosNoReportados.facturaActual.getIsFree() == 0) {
                    JOptionPane.showInternalMessageDialog(fAdicionarPedidosNoReportados, "La Factura  # " + numeroDeFactura
                            + " ya se encuentra en Distribución "
                            + "", "Error", JOptionPane.WARNING_MESSAGE);

                    /* =1  la factura está disponible para agregarla al manifiesto 
                           SE registran las facturas que presentan inconvenientes en el Jlist*/
                    //modelo3.addElement(numeroDeFactura);
                    //jListListaDeFacturasErradas.setModel(modelo3);
                    return;
                }

                /* SE valida que la factura no esté en el manifiesto */
                if (!fAdicionarPedidosNoReportados.estaLaFacturaEnElManifiesto()) {
                    if (fAdicionarPedidosNoReportados.facturaActual.getFormaDePago().equals("CONTADO")) {
                        fAdicionarPedidosNoReportados.btnContado.setVisible(true);
                        fAdicionarPedidosNoReportados.btnContado.setSelected(true);
                    }
                    if (fAdicionarPedidosNoReportados.facturaActual.getFormaDePago().equals("CREDITO")) {
                        fAdicionarPedidosNoReportados.btnCredito.setVisible(true);
                        fAdicionarPedidosNoReportados.btnCredito.setSelected(true);
                    }
                  
                    /* se crea un objeto temporal de facturas por manifiesto */
                    CFacturasPorManifiesto facturaPorManifiesto = new CFacturasPorManifiesto(ini);

                    /* Se agregan las propiedades a l objeto vista facturas por manifiesto */
                    facturaPorManifiesto.setNumeroManifiesto(fAdicionarPedidosNoReportados.manifiestoActual.getNumeroManifiesto());
                    facturaPorManifiesto.setNumeroFactura(fAdicionarPedidosNoReportados.facturaActual.getNumeroDeFactura());
                    facturaPorManifiesto.setNombreDeCliente(fAdicionarPedidosNoReportados.facturaActual.getNombreDeCliente());
                    facturaPorManifiesto.setDireccionDeCliente(fAdicionarPedidosNoReportados.facturaActual.getDireccionDeCliente());
                    facturaPorManifiesto.setValorTotalFactura(fAdicionarPedidosNoReportados.facturaActual.getValorTotalFactura());
                    facturaPorManifiesto.setAdherencia(fAdicionarPedidosNoReportados.listaDeFacturasPorManifiesto.size() + 1);
                    facturaPorManifiesto.setValorARecaudarFactura(fAdicionarPedidosNoReportados.facturaActual.getValorTotalFactura());

                    if (true) {

                        /* se agrega el registro al array  de facturas por manifiesto */
                        fAdicionarPedidosNoReportados.listaDeFacturasPorManifiesto.add(facturaPorManifiesto); //CfacturasPorManifiesto
                        fAdicionarPedidosNoReportados.manifiestoActual.setListaFacturasPorManifiesto(fAdicionarPedidosNoReportados.listaDeFacturasPorManifiesto);

                        /*Validamos de donde proviene el dato: bien sea del campo 
                            de texto ó de un archivo plano */
 /* sí hay datos en la tabla de los productos  de la factura se limpia la tabla */
                        fAdicionarPedidosNoReportados.limpiarDatodsDePanelProductosPorFactura();

                        fAdicionarPedidosNoReportados.txtNombreDeCliente.setText(fAdicionarPedidosNoReportados.facturaActual.getNombreDeCliente());
                        fAdicionarPedidosNoReportados.txtDireccionCliente.setText(fAdicionarPedidosNoReportados.facturaActual.getDireccionDeCliente());
                        fAdicionarPedidosNoReportados.txtTelefonoCliente.setText(fAdicionarPedidosNoReportados.facturaActual.getTelefonoCliente());
                        fAdicionarPedidosNoReportados.txtBarroCliente.setText(fAdicionarPedidosNoReportados.facturaActual.getBarrio());
                        fAdicionarPedidosNoReportados.txtNombreVendedor.setText(fAdicionarPedidosNoReportados.facturaActual.getVendedor());
                        fAdicionarPedidosNoReportados.lblValorFactura.setText(nf.format(fAdicionarPedidosNoReportados.facturaActual.getValorTotalFactura()));

                        fAdicionarPedidosNoReportados.facturaActual.setListaCProductosPorFactura(false);

                        /* se llena la tabla de productos por Factura*/
                        llenarJtableProductosPorFactura(fAdicionarPedidosNoReportados.facturaActual.getListaCProductosPorFactura());

                        if (facturaPorManifiesto.grabarFacturasPoManifiesto()) {
                            /*Se llena el Jtable correspondiente*/
                            llenarJtableFacturasPorManifiesto();
                            /* se ubica el cursor en la fila insertada */
                            fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.changeSelection(fAdicionarPedidosNoReportados.filaTabla2, 0, false, false);

                            /* se imprime el dato en la respectiva etiqueta */
                            fAdicionarPedidosNoReportados.lblValorRecaudoManifiesto.setText(nf.format(fAdicionarPedidosNoReportados.valorTotalManifiesto));
                            fAdicionarPedidosNoReportados.btnGrabar.setEnabled(false);
                            fAdicionarPedidosNoReportados.jBtnGrabar.setEnabled(false);
                        }

                    } else {
                        JOptionPane.showInternalMessageDialog(fAdicionarPedidosNoReportados, "Error al grabar en el archivo temporal, la  FACTURA # " + numeroDeFactura + "", "Error", JOptionPane.ERROR_MESSAGE);

                    }

                    /* si el registro existe en la tabla TMPfacturasPorManifiesto, hace nada */
                    fAdicionarPedidosNoReportados.txtNombreDeCliente.requestFocus();

                }

                /* si no   existe la factura, arroja un mensaje de error. */
            } else {

                JOptionPane.showInternalMessageDialog(fAdicionarPedidosNoReportados, "La factura # " + numeroDeFactura
                        + "  no existe en el sistema "
                        + "", "Error", JOptionPane.WARNING_MESSAGE);
            }

            fAdicionarPedidosNoReportados.txtNombreDeCliente.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarManifiesto() {
        System.out.println("Cargando datos despues del enter -> \n\n");

        fAdicionarPedidosNoReportados.lblCirculoDeProgreso1.setVisible(true);
        fAdicionarPedidosNoReportados.btnImprimir.setEnabled(false);
        fAdicionarPedidosNoReportados.jBtnImprimir.setEnabled(false);

        try {
            fAdicionarPedidosNoReportados.manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(fAdicionarPedidosNoReportados.txtNumeroManifiesto.getText().trim()));
            fAdicionarPedidosNoReportados.lblCirculoDeProgreso1.setVisible(true);
            consultarManifiestoIncompleto();
        } catch (Exception ex) {
            JOptionPane.showInternalMessageDialog(fAdicionarPedidosNoReportados, "Error en la consulta del vehiculo", "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(HiloFAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public void manifestarRutasLocales() {
        try {
            /* S hace visible el gif que indica que hay un proceso en ejecución */
            this.fAdicionarPedidosNoReportados.lblCirculoDeProgreso1.setVisible(true);

            this.fAdicionarPedidosNoReportados.modelo2 = (DefaultTableModel) this.fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.getModel();

            this.fAdicionarPedidosNoReportados.txtKmDeSalida.setEditable(false);
            this.fAdicionarPedidosNoReportados.txtPlaca.setEnabled(false);
            this.fAdicionarPedidosNoReportados.txtPlaca.setEditable(false);

            /*  SE LLENAN LAS PROPIEDADES DE  EL MANIFIESTO */
            Date dt = new Date();
            dt = ini.getFechaSql(this.fAdicionarPedidosNoReportados.dateManifiesto);
            this.fAdicionarPedidosNoReportados.manifiestoActual.setFechaDistribucion("" + dt);

            this.fAdicionarPedidosNoReportados.manifiestoActual.setVehiculo(this.fAdicionarPedidosNoReportados.carro.getPlaca());
            this.fAdicionarPedidosNoReportados.manifiestoActual.setConductor(this.fAdicionarPedidosNoReportados.conductor.getCedula());
            this.fAdicionarPedidosNoReportados.manifiestoActual.setNombreConductor(this.fAdicionarPedidosNoReportados.conductor.getNombres());
            this.fAdicionarPedidosNoReportados.manifiestoActual.setApellidosConductor(this.fAdicionarPedidosNoReportados.conductor.getApellidos());

            this.fAdicionarPedidosNoReportados.manifiestoActual.setDespachador(this.fAdicionarPedidosNoReportados.despachador.getCedula());
            this.fAdicionarPedidosNoReportados.manifiestoActual.setNombreDespachador(this.fAdicionarPedidosNoReportados.despachador.getNombres());
            this.fAdicionarPedidosNoReportados.manifiestoActual.setApellidosDespachador(this.fAdicionarPedidosNoReportados.despachador.getApellidos());

            this.fAdicionarPedidosNoReportados.manifiestoActual.setIdCanal(this.fAdicionarPedidosNoReportados.canalDeVenta.getIdCanalDeVenta());
            //    this.fAdicionarPedidosNoReportados.manifiestoActual.setNombreCanal(this.fAdicionarPedidosNoReportados.cbxCanales.getSelectedItem().toString());

            this.fAdicionarPedidosNoReportados.manifiestoActual.setIdRuta(this.fAdicionarPedidosNoReportados.ruta.getIdRutasDeDistribucion());
//            this.fAdicionarPedidosNoReportados.manifiestoActual.setNombreDeRuta(this.fAdicionarPedidosNoReportados.cbxRutaDeDistribucion.getSelectedItem().toString());

            this.fAdicionarPedidosNoReportados.manifiestoActual.setZona(ini.getUser().getZona());
            this.fAdicionarPedidosNoReportados.manifiestoActual.setRegional(ini.getUser().getRegional());
            this.fAdicionarPedidosNoReportados.manifiestoActual.setAgencia(ini.getUser().getAgencia());

            this.fAdicionarPedidosNoReportados.manifiestoActual.setEstadoManifiesto(2);
            this.fAdicionarPedidosNoReportados.manifiestoActual.setKmSalida(Integer.parseInt(this.fAdicionarPedidosNoReportados.txtKmDeSalida.getText().trim()));
            this.fAdicionarPedidosNoReportados.manifiestoActual.setKmEntrada(Integer.parseInt(this.fAdicionarPedidosNoReportados.txtKmDeSalida.getText().trim()));
            this.fAdicionarPedidosNoReportados.manifiestoActual.setKmRecorrido(0);

            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechax;
            fechax = fecha.format(new Date());

            this.fAdicionarPedidosNoReportados.manifiestoActual.setHoraDeDespacho("CURRENT_TIMESTAMP");
            this.fAdicionarPedidosNoReportados.manifiestoActual.setHoraDeLiquidacion("CURRENT_TIMESTAMP");
            this.fAdicionarPedidosNoReportados.manifiestoActual.setIsFree(0);
            this.fAdicionarPedidosNoReportados.manifiestoActual.setValorTotalManifiesto(0.0);
            this.fAdicionarPedidosNoReportados.manifiestoActual.setValorRecaudado(0.0);
            this.fAdicionarPedidosNoReportados.manifiestoActual.setActivo(1);
            this.fAdicionarPedidosNoReportados.manifiestoActual.setUsuarioManifiesto(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            this.fAdicionarPedidosNoReportados.manifiestoActual.setObservaciones("NA");
            this.fAdicionarPedidosNoReportados.manifiestoActual.setCantDeSalidas(1);

            /*SE ASIGNAN LOS AUXILIARES */
            this.fAdicionarPedidosNoReportados.manifiestoActual.setListaDeAuxiliares(this.fAdicionarPedidosNoReportados.listaDeAuxiliares);

            // GRABA EL MANIFIESTO DE DISTRIBUCION
            if (this.fAdicionarPedidosNoReportados.manifiestoActual.grabarManifiestoDeDistribucion()) {

                this.fAdicionarPedidosNoReportados.manifiestoActual.crearRutasDeArchivos();

                /*Se crea la ruta donde se guardarán temporalmente las facturas del manfiesto */
                fAdicionarPedidosNoReportados.archivoConListaDeFacturas = new File(this.fAdicionarPedidosNoReportados.manifiestoActual.getRutArchivofacturasporManifiesto());

                // HABILITA CAMPO DE TEXTO PARA EMPEZAR A INGRESAR LAS FACTURAS
//                this.fAdicionarPedidosNoReportados.cbxPrefijos.setEnabled(true);
                this.fAdicionarPedidosNoReportados.txtNumeroDeFactura.setEnabled(true);
                this.fAdicionarPedidosNoReportados.txtNumeroDeFactura.setEditable(true);

                // SE CODIFICA EL MANIFIESTO Y SE LLENA EL CAMPO DE TEXTO RESPECTIVO
                String cadenaManifiestoCodificado = this.fAdicionarPedidosNoReportados.manifiestoActual.codificarManifiesto();
                this.fAdicionarPedidosNoReportados.txtNumeroManifiesto.setText(cadenaManifiestoCodificado);

                // SE DESHABILITAN LOS COMPONENTES PARA EVITAR CAMBIO DE DATOS
                this.fAdicionarPedidosNoReportados.txtKmDeSalida.setEditable(false);

                this.fAdicionarPedidosNoReportados.dateManifiesto.setEnabled(false);
//                this.fAdicionarPedidosNoReportados.cbxCanales.setEnabled(false);
//                this.fAdicionarPedidosNoReportados.cbxRutaDeDistribucion.setEnabled(false);

                //SE CREAN LOS ARRAY PARA GUARDAR LAS FACTURAS
                List<CFacturasPorManifiesto> lista = new ArrayList();
                this.fAdicionarPedidosNoReportados.manifiestoActual.setListaFacturasPorManifiesto(lista);
                //this.fAdicionarPedidosNoReportados.listaDeCFacturasCamdunEnElManifiesto = new ArrayList<>();//CFacturas

//              this.fAdicionarPedidosNoReportados.manifiestoActual.setListaFacturasPorManifiesto(this.fAdicionarPedidosNoReportados.listaDeCFacturasPorManifiesto);
//              this.fAdicionarPedidosNoReportados.manifiestoActual.setListaCFacturasCamdun(this.fAdicionarPedidosNoReportados.listaDeCFacturasCamdunEnElManifiesto);
                this.fAdicionarPedidosNoReportados.jTabbedPane1.setEnabled(true);
//                this.fAdicionarPedidosNoReportados.jTabbedPane1.setEnabledAt(this.fAdicionarPedidosNoReportados.jTabbedPane1.indexOfComponent(this.fAdicionarPedidosNoReportados.pnlAgregarFactura), true);

                // DE DESHABILITA EL MANIFIESTO PARA EVITAR QUE OTRO USUARIO LO MODIFIQUE.
                this.fAdicionarPedidosNoReportados.manifiestoActual.setIsFree(0);
                this.fAdicionarPedidosNoReportados.manifiestoActual.liberarManifiesto(false);

                // this.fAdicionarPedidosNoReportados.lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + this.fAdicionarPedidosNoReportados.txtNumeroDeManifiesto.getText().trim());
                this.fAdicionarPedidosNoReportados.txtnombreDeConductor.setEditable(false);

                this.fAdicionarPedidosNoReportados.txtBarroCliente.setEditable(false);
                this.fAdicionarPedidosNoReportados.txtDireccionCliente.setEditable(false);
                this.fAdicionarPedidosNoReportados.txtNombreDeCliente.setEditable(false);
                this.fAdicionarPedidosNoReportados.txtTelefonoCliente.setEditable(false);
                this.fAdicionarPedidosNoReportados.txtNumeroManifiesto.setEditable(false);
                this.fAdicionarPedidosNoReportados.txtNombreVendedor.setEditable(false);

                this.fAdicionarPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);

                JOptionPane.showInternalMessageDialog(this.fAdicionarPedidosNoReportados, "Manifiesto guardado correctamente ", "Manifiesto guardado correctamente ", JOptionPane.INFORMATION_MESSAGE);

                /*Se anexa el manifiesto actual a la lista de manifiestos sin descargar*/
                this.ini.getListaDeManifiestossinDescargar().add(this.fAdicionarPedidosNoReportados.manifiestoActual);

                this.fAdicionarPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);

                //  this.fAdicionarPedidosNoReportados.cbxPrefijos.setEnabled(true);
                this.fAdicionarPedidosNoReportados.txtNumeroDeFactura.requestFocus();
                this.fAdicionarPedidosNoReportados.txtNumeroDeFactura.requestFocus();
            } else {
                JOptionPane.showInternalMessageDialog(this.fAdicionarPedidosNoReportados, "Error al guardar manifiesto", "Error ", JOptionPane.ERROR_MESSAGE);
                this.fAdicionarPedidosNoReportados.manifiestoActual.liberarManifiesto(true);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloFAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
            this.fAdicionarPedidosNoReportados.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }
    }

    public void llenarJtableFacturasPorManifiesto() {

        /* se anexa el registro a la Jtable de facturas por manifiesto */
//        fAdicionarPedidosNoReportados.filaTabla2 = fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.getRowCount();
        fAdicionarPedidosNoReportados.modelo2.addRow(new Object[fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.getRowCount()]);

//        fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.setValueAt("" + (fAdicionarPedidosNoReportados.filaTabla2 + 1), fAdicionarPedidosNoReportados.filaTabla2, 0);
//        fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.setValueAt(fAdicionarPedidosNoReportados.facturaActual.getNumeroFactura(), fAdicionarPedidosNoReportados.filaTabla2, 1);
//        fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.setValueAt(fAdicionarPedidosNoReportados.facturaActual.getNombreDeCliente(), fAdicionarPedidosNoReportados.filaTabla2, 2);
//        fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.setValueAt(nf.format(fAdicionarPedidosNoReportados.facturaActual.getValorTotalFactura()), fAdicionarPedidosNoReportados.filaTabla2, 3);
        fAdicionarPedidosNoReportados.valorTotalManifiesto += fAdicionarPedidosNoReportados.facturaActual.getValorTotalFactura();

    }

    public void llenarJtableProductosPorFactura(List<CProductosPorFactura> arrProduproductosEnLaFactura) throws InterruptedException {
        fAdicionarPedidosNoReportados.modelo1 = (DefaultTableModel) fAdicionarPedidosNoReportados.jTableProductosPorFactura.getModel();

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura */
        for (CProductosPorFactura obj : arrProduproductosEnLaFactura) {

            Vst_ProductosPorFactura pxf = new Vst_ProductosPorFactura();

            int fila = fAdicionarPedidosNoReportados.jTableProductosPorFactura.getRowCount();

            fAdicionarPedidosNoReportados.modelo1.addRow(new Object[fAdicionarPedidosNoReportados.jTableProductosPorFactura.getRowCount()]);

            fAdicionarPedidosNoReportados.jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            fAdicionarPedidosNoReportados.jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            fAdicionarPedidosNoReportados.jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            fAdicionarPedidosNoReportados.jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            fAdicionarPedidosNoReportados.jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            fAdicionarPedidosNoReportados.jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva() * obj.getCantidad()), fila, 5);
            Thread.sleep(10);
        }
    }

    public void sacarMinuta() throws HeadlessException {
        String listadeproductos = null;
        int seleccion = JOptionPane.showOptionDialog(null, "Seleccione Tipo de Minuta",
                "Selector de opciones", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,// null para icono por defecto.
                new Object[]{"Completa", "Por Codigos", "Cancelar"}, "opcion 1");

        if (seleccion != -1) {
            System.out.println("seleccionada opcion " + (seleccion + 1));
            fAdicionarPedidosNoReportados.manifiestoActual.sacarMinutaManifiesto(seleccion);
        }
    }

    public boolean validarManifiesto() {
        boolean verificado = true;
        try {

            fAdicionarPedidosNoReportados.mensaje = "";

        } catch (Exception ex) {
            Logger.getLogger(HiloFAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificado;
    }

    public void cargarVista() {
        fAdicionarPedidosNoReportados.btnContado.setVisible(false);
        fAdicionarPedidosNoReportados.btnCredito.setVisible(false);

        if (ini.getListaDeVehiculos() == null) {
            ini.setListaDeVehiculos(1);
        }
        fAdicionarPedidosNoReportados.dateManifiesto.setDate(new Date());
        fAdicionarPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);

        fAdicionarPedidosNoReportados.cargado = true;
    }

    public void grabarFacturasPorManifiesto() throws HeadlessException {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

        this.fAdicionarPedidosNoReportados.btnImprimir.setEnabled(false);
        this.fAdicionarPedidosNoReportados.jBtnImprimir.setEnabled(false);

        this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        this.fAdicionarPedidosNoReportados.repaint();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloFAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fAdicionarPedidosNoReportados.btnGrabar.setEnabled(false);
        this.fAdicionarPedidosNoReportados.jBtnGrabar.setEnabled(false);

        this.fAdicionarPedidosNoReportados.txtNumeroDeFactura.setEnabled(false);
        //this.fAdicionarPedidosNoReportados.pnlAgregarLista.setEnabled(false);

        this.fAdicionarPedidosNoReportados.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;

        /* Se cambia el estado de manifiesto a manifiesto cerrado*/
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (this.fAdicionarPedidosNoReportados.manifiestoActual.grabarFacturasPorManifiesto(true)) {

                fAdicionarPedidosNoReportados.manifiestoActual.setEstadoManifiesto(3);
                fAdicionarPedidosNoReportados.manifiestoActual.setIsFree(1);
                fAdicionarPedidosNoReportados.manifiestoActual.actualizarManifiestoDeDistribucions();

                fAdicionarPedidosNoReportados.manifiestoActual.setHoraDeDespacho("" + new Date());
                // fAdicionarPedidosNoReportados.manifiestoActual.setPesoKgManifiesto(fAdicionarPedidosNoReportados.sumadorPesosFacturas);

                this.fAdicionarPedidosNoReportados.estaOcupadoGrabando = false;
                this.fAdicionarPedidosNoReportados.btnImprimir.setEnabled(true);
                this.fAdicionarPedidosNoReportados.btnGrabar.setEnabled(false);

                this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                JOptionPane.showMessageDialog(this.fAdicionarPedidosNoReportados, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                // this.fAdicionarPedidosNoReportados.jBtnMinuta.setEnabled(true);

                this.fAdicionarPedidosNoReportados.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {

                this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                this.fAdicionarPedidosNoReportados.btnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fAdicionarPedidosNoReportados, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fAdicionarPedidosNoReportados, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fAdicionarPedidosNoReportados.btnGrabar.setEnabled(true);
            this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        } catch (Exception ex) {
            Logger.getLogger(HiloFAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
            this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        }

        this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
    }

    private void consultarManifiestoIncompleto() {

        System.out.println("Trae los datos del manifiesto -> \n\n");
        try {

            this.fAdicionarPedidosNoReportados.lblCirculoDeProgreso1.setVisible(true);
//            this.fPedidosNoReportados.lblCirculoDeProgreso2.setVisible(false);
            this.fAdicionarPedidosNoReportados.listaDeFacturasPorManifiesto = new ArrayList();

            /* ENTRA AL CASO CORRESPONDIENTE DE ACUERDO AL ESTADO DEL MANIFIESTO */
            switch (this.fAdicionarPedidosNoReportados.manifiestoActual.getEstadoManifiesto()) {

                case 0:
                    /* El caso cuando por primera vez se le crea a un vehículo
                     un manifiesto de distribución 
                     */
                    // fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto=new ArrayList();
                    //this.fPedidosNoReportados.crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                    this.fAdicionarPedidosNoReportados.listaDeFacturasPorManifiesto = new ArrayList();
                    break;

                case 1:// CASO CUANDO NO HAY MANIFIESTOS PENDIENTES
                    // fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto=new ArrayList();
                    this.fAdicionarPedidosNoReportados.crearNuevoManifiesto(this.fAdicionarPedidosNoReportados.carro);
                    //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                    this.fAdicionarPedidosNoReportados.txtKmDeSalida.setText("" + this.fAdicionarPedidosNoReportados.carro.getKilometrajeActual());
                    // this.fPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);
                    this.fAdicionarPedidosNoReportados.txtnombreDeConductor.setEditable(false);
                    JOptionPane.showInternalMessageDialog(this.fAdicionarPedidosNoReportados, "Ese manifiesto no se ha creado ", "Error", JOptionPane.WARNING_MESSAGE);

                    break;

                case 2:// TIENE MANIFIESTO ASIGANDO PERO NO SE HA CERRADO;

                    JOptionPane.showInternalMessageDialog(this.fAdicionarPedidosNoReportados, "Ese manifiesto esta creado, pero no cerrado ", "No Cerrado", JOptionPane.WARNING_MESSAGE);
                    //this.fManifestarPedidosEnRuta.manifiestoActual.setRutArchivofacturasporManifiesto();
                    //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                    // this.fPedidosNoReportados.modificarManifiesto();
                    // this.fPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);
                    //this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometraje());
                    break;

                case 3:// VEHICULO EN DISTRIBUCION,  NO SE PUEDE ASIGNAR OTRO MANIFIESTO

                    llenarDatosManifiestoCerrado();

                    break;

                case 4: // NUMERO DE MANIFIESTO YA DESCARGADO
                    JOptionPane.showInternalMessageDialog(this.fAdicionarPedidosNoReportados, "Ese manifiesto esta descargado del sistema ", "No Cerrado", JOptionPane.WARNING_MESSAGE);

                    break;

                case 5: // MANIFIESTO ANULADO
                    JOptionPane.showInternalMessageDialog(this.fAdicionarPedidosNoReportados, "Ese manifiesto anulado del sistema ", "Anulado", JOptionPane.WARNING_MESSAGE);

                    //this.fPedidosNoReportados.jTabbedPane1.setEnabledAt(this.fPedidosNoReportados.jTabbedPane1.indexOfComponent(this.fPedidosNoReportados.pnlAgregarLista), true);
                    // jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlAgregarLista));
                    //this.fPedidosNoReportados.crearNuevoManifiesto(this.fPedidosNoReportados.carro);
                    this.fAdicionarPedidosNoReportados.txtKmDeSalida.setText("" + this.fAdicionarPedidosNoReportados.carro.getKilometrajeActual());
                    // this.fPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);

                    break;

            }

            try {

                /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                if (this.fAdicionarPedidosNoReportados.listaDeFacturasPorManifiesto != null) {
                    fAdicionarPedidosNoReportados.btnImprimir.setEnabled(true);
                }

            } catch (Exception ex) {
                Logger.getLogger(HiloFAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.fAdicionarPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);
//            this.fPedidosNoReportados.lblCirculoDeProgreso2.setVisible(false);
            this.fAdicionarPedidosNoReportados.txtKmDeSalida.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(HiloFAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void llenarDatosManifiestoCerrado() throws Exception {

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        fAdicionarPedidosNoReportados.txtnombreDeConductor.setEditable(false);

        fAdicionarPedidosNoReportados.txtKmDeSalida.setEditable(false);
        fAdicionarPedidosNoReportados.txtPlaca.setEditable(false);
        fAdicionarPedidosNoReportados.txtKmDeSalida.setEditable(false);

        fAdicionarPedidosNoReportados.dateManifiesto.setEnabled(false);

        fAdicionarPedidosNoReportados.jTabbedPane1.setEnabled(false);
        fAdicionarPedidosNoReportados.btnNuevo.setEnabled(false);
        fAdicionarPedidosNoReportados.jBtnNuevo.setEnabled(false);

        fAdicionarPedidosNoReportados.btnImprimir.setEnabled(false);
        fAdicionarPedidosNoReportados.jBtnImprimir.setEnabled(false);

        fAdicionarPedidosNoReportados.btnGrabar.setEnabled(false);
        fAdicionarPedidosNoReportados.jBtnGrabar.setEnabled(false);

        fAdicionarPedidosNoReportados.dateManifiesto.setEnabled(false);

        fAdicionarPedidosNoReportados.txtPlaca.setEnabled(false);
        fAdicionarPedidosNoReportados.txtnombreDeConductor.setEnabled(false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
        fAdicionarPedidosNoReportados.manifiestoActual.setListaFacturasPorManifiesto();// CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        fAdicionarPedidosNoReportados.manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        //manifiestoActual.setVstListaFacturasEnDistribucion();
        //listaDeCFacturasCamdunEnElManifiesto = manifiestoActual.getVstListaFacturasEnDistribucion(); // CfacturasCamdun
        fAdicionarPedidosNoReportados.listaDeFacturasPorManifiesto = fAdicionarPedidosNoReportados.manifiestoActual.getListaFacturasPorManifiesto(); //CFacturasPorManifiesto
        fAdicionarPedidosNoReportados.listaDeAuxiliares = fAdicionarPedidosNoReportados.manifiestoActual.getListaDeAuxiliares("" + fAdicionarPedidosNoReportados.manifiestoActual.getNumeroManifiesto());

        int cantidadFacturasEnManifiesto = 0;

        fAdicionarPedidosNoReportados.modelo2 = (DefaultTableModel) fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.getModel();

        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : fAdicionarPedidosNoReportados.manifiestoActual.getListaFacturasPorManifiesto()) {

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            fAdicionarPedidosNoReportados.filaTabla2 = fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.getRowCount();
            fAdicionarPedidosNoReportados.modelo2.addRow(new Object[fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.getRowCount()]);

            fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.setValueAt("" + (fAdicionarPedidosNoReportados.filaTabla2 + 1), fAdicionarPedidosNoReportados.filaTabla2, 0); // item 
            fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), fAdicionarPedidosNoReportados.filaTabla2, 1); // numero de la factura

            fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), fAdicionarPedidosNoReportados.filaTabla2, 2); // cliente

            fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), fAdicionarPedidosNoReportados.filaTabla2, 3); // valor a recaudar  de la factura   
            fAdicionarPedidosNoReportados.valorTotalManifiesto += obj.getValorARecaudarFactura();

            // se ubica en la fila insertada
            fAdicionarPedidosNoReportados.jTableFacturasPorManifiesto.changeSelection(fAdicionarPedidosNoReportados.filaTabla2, 0, false, false);

            cantidadFacturasEnManifiesto++;
            valor += obj.getValorARecaudarFactura();

            // }
            // }
            //this.repaint();
        }

        fAdicionarPedidosNoReportados.manifiestoActual.setValorTotalManifiesto(valor);

        fAdicionarPedidosNoReportados.txtPlaca.setText(fAdicionarPedidosNoReportados.manifiestoActual.getVehiculo());

        // SE BUSCA EL CONDUCTOR Y SE LLENA EL CAMPO
        for (CEmpleados obj : ini.getListaDeEmpleados()) {
            if (fAdicionarPedidosNoReportados.manifiestoActual.getConductor().equals(obj.getCedula())) {

                fAdicionarPedidosNoReportados.txtnombreDeConductor.setText(obj.getNombres() + " " + obj.getApellidos());
            }
        }

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            if (obj.getIdCanalDeVenta() == fAdicionarPedidosNoReportados.manifiestoActual.getIdCanal()) {
                fAdicionarPedidosNoReportados.txtCanal.setText(obj.getNombreCanalDeVenta());
            }
        }

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            if (obj.getIdRutasDeDistribucion() == fAdicionarPedidosNoReportados.manifiestoActual.getIdRuta()) {
                fAdicionarPedidosNoReportados.txtRuta.setText(obj.getNombreDeRuta());
            }
        }

        fAdicionarPedidosNoReportados.txtKmDeSalida.setText("" + fAdicionarPedidosNoReportados.manifiestoActual.getKmSalida());

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
        String strFecha = fAdicionarPedidosNoReportados.manifiestoActual.getFechaDistribucion();
        Date fecha = null;
        fecha = formatoDelTexto.parse(strFecha);
        //dateManifiesto.setDate(manifiestoActual.getFechaDistribucion());
        // Date dt = new Date();
        // fManifestarPedidosEnRuta.dateManifiesto.setDate(fecha);

        fAdicionarPedidosNoReportados.dateManifiesto.setDate(fecha);
        if (fAdicionarPedidosNoReportados.manifiestoActual.getEstadoManifiesto() == 3) {
            fAdicionarPedidosNoReportados.txtNumeroDeFactura.setEnabled(true);
            fAdicionarPedidosNoReportados.txtNumeroDeFactura.setEditable(true);
        }

        fAdicionarPedidosNoReportados.lblValorRecaudoManifiesto.setText(nf.format(fAdicionarPedidosNoReportados.manifiestoActual.getValorTotalManifiesto()));

        fAdicionarPedidosNoReportados.lblValorRecaudoManifiesto.setText(nf.format(fAdicionarPedidosNoReportados.valorTotalManifiesto));
        fAdicionarPedidosNoReportados.lblCantidadFacturas.setText("" + cantidadFacturasEnManifiesto);
        fAdicionarPedidosNoReportados.btnImprimir.setEnabled(true);
        fAdicionarPedidosNoReportados.jBtnImprimir.setEnabled(true);

        fAdicionarPedidosNoReportados.btnImprimir.requestFocus();

    }

}
