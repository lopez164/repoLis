/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Threads;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import com.mxrck.autocompleter.TextAutoCompleter;
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
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.table.DefaultTableModel;
import mtto.documentos.objetos.DocumentosPorVehiculo;
import mtto.vehiculos.CCarros;
import mtto.vehiculos.CVehiculos;

/**
 *
 * @author Usuario
 */
public class HiloFmanifestarPedidosEnRuta implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FManifestarPedidosEnRuta fManifestarPedidosEnRuta = null;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFmanifestarPedidosEnRuta(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fManifestarPedidosEnRuta
     * @param comando
     */
    public HiloFmanifestarPedidosEnRuta(Inicio ini, FManifestarPedidosEnRuta fManifestarPedidosEnRuta, String comando) {
        this.ini = ini;
        this.fManifestarPedidosEnRuta = fManifestarPedidosEnRuta;
        this.caso = comando;
    }

    @Override
    public void run() {

        if (caso != null) {
            switch (caso) {
                case "consultarManifiesto":
                    consultarManifiesto();
                    break;
                case "crearManifiesto":
                    crearManifiesto();
                    break;

                case "funcionAgregarFactura":
                    funcionAgregarFactura();
                    break;

                case "agregarFacturasDesdeJList": {
                           
                    try {
                        agregarFacturasDesdeJList();
                    } catch (HeadlessException ex) {
                        Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                case "sacarMinuta":
                    sacarMinuta();
                    break;
                case "cargarVista":
                    cargarVista();
                    break;

                case "refrescar":
                    refrescar();
                    break;

                case "grabarFacturasPorManifiesto":
                    grabarFacturasPorManifiesto();
                    break;

                default:
                    JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    public void funcionAgregarFactura() {
        fManifestarPedidosEnRuta.jBtnBorrarFIla.setEnabled(false);

        if (fManifestarPedidosEnRuta.formaDePago < 1) {

            JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, " ¡ Debe especificar la forma de pago de la factura !!!" + "", "Error !!!", JOptionPane.WARNING_MESSAGE);

        } else {
            try {
                String numeroFactura = fManifestarPedidosEnRuta.cbxPrefijos.getSelectedItem().toString() + (fManifestarPedidosEnRuta.txtNumeroDeFactura.getText().trim());
                //agregarFactura("" + Integer.parseInt(txtNumeroDeFactura.getText().trim()), false, jTableFacturasPorManifiesto.getRowCount() + 1);
                agregarFactura(numeroFactura, false, fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.getRowCount() + 1);

                fManifestarPedidosEnRuta.lblCantidadFacturas.setText("" + fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto().size());

                /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                if (fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto().size() > 1) {
                    fManifestarPedidosEnRuta.btnImprimir.setEnabled(true);
                    fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(true);
                }

            } catch (Exception ex) {
                Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void consultarManifiesto() {
        if (fManifestarPedidosEnRuta.manifiestoActual.getEstadoManifiesto() <= 2) {
            System.out.println("Cargando datos despues del enter -> \n\n");

            fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(true);
            fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(false);
            fManifestarPedidosEnRuta.btnImprimir.setEnabled(false);
            fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(false);

            try {
                fManifestarPedidosEnRuta.carro = null;

                fManifestarPedidosEnRuta.btnNuevo.setEnabled(false);
                fManifestarPedidosEnRuta.jBtnNuevo.setEnabled(false);

                /* SE BUSCA EN VEHICULO EN EL ARRAY DEL SISTEMA */
                for (CCarros carro : ini.getListaDeVehiculos()) {
                    if (carro.getPlaca().equals(fManifestarPedidosEnRuta.txtPlaca.getText().trim())) {
                        fManifestarPedidosEnRuta.carro = carro;
                        break;
                    }
                    Thread.sleep(2);
                }

                System.out.println("busca el carro con el manifiesto -> \n\n");

                /* Se valid que el vehículo exista en el sistema*/
                if (fManifestarPedidosEnRuta.carro == null) {
                    fManifestarPedidosEnRuta.txtPlaca.requestFocus();
                    JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "ESE VEHICULO NO EXISTE EN LA BASE DE DATOS ", "Error", 0);
                    fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                } else {
                    fManifestarPedidosEnRuta.carro.setListaDeDocumentosPorVehiculoVencidos();

                    if (fManifestarPedidosEnRuta.carro.getListaDeDocumentosPorVehiculoVencidos() != null) {
                       fManifestarPedidosEnRuta.jBtnDocVencidos.setEnabled(false);
                        validarDocumentosVehiculo();
                        if (fManifestarPedidosEnRuta.carro.isVehiculoConDocumentosVencidos()) {
                            JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "Vehiculo tiene documentos vencidos", "Error", JOptionPane.ERROR_MESSAGE);
                            fManifestarPedidosEnRuta.jBtnDocVencidos.setEnabled(true);
                        }else{
                             JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "Vehiculo tiene documentos a punto de vencer \n en estos dias ", "Alerta", JOptionPane.WARNING_MESSAGE);
                            fManifestarPedidosEnRuta.jBtnDocVencidos.setEnabled(true);
                        }
                    }
                                       DecimalFormat df = new DecimalFormat("#,###.0");
                    fManifestarPedidosEnRuta.txtPesoMAximoAutorizado.setText("Peso Maximo Autorizado : " + df.format(fManifestarPedidosEnRuta.carro.getPesoTotalAutorizado()) + " Kgs.");
                    fManifestarPedidosEnRuta.txtKmDeSalida.setText(fManifestarPedidosEnRuta.carro.getKilometrajeActual() + "");
                    fManifestarPedidosEnRuta.txtnombreDeConductor.setText(fManifestarPedidosEnRuta.carro.getNombreConductor() + " " + fManifestarPedidosEnRuta.carro.getApellidosConductor());
                    fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
             
                    //new Thread(new HiloConsultarManifiesto(ini, fManifestarPedidosEnRuta)).start();
                    consultarManifiestoPedidosEnRuta();
                }
            } catch (Exception ex) {
                JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "Error en la consulta del vehiculo", "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
                fManifestarPedidosEnRuta.cancelar();
            }
        }
    }

    private void consultarManifiestoPedidosEnRuta() {

        System.out.println("Trae los datos del manifiesto -> \n\n");
        try {

            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(true);
            fManifestarPedidosEnRuta.manifiestoActual = new CManifiestosDeDistribucion(ini);

            if (ini.getListaDeManifiestossinDescargar() != null) {
                for (CManifiestosDeDistribucion mfto : ini.getListaDeManifiestossinDescargar()) {
                    if (mfto.getVehiculo().equals(fManifestarPedidosEnRuta.txtPlaca.getText().trim())) {
                        fManifestarPedidosEnRuta.manifiestoActual = mfto;

                    }
                    Thread.sleep(10);
                }
            }

            if (fManifestarPedidosEnRuta.manifiestoActual.getNumeroManifiesto() == null) {

                fManifestarPedidosEnRuta.manifiestoActual = new CManifiestosDeDistribucion(ini, fManifestarPedidosEnRuta.txtPlaca.getText().trim());
            }

            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(false);

            /*Se crea una lista provisionnal de acuerdo a reqerimiento*/
            List<CFacturasPorManifiesto> listaFacturasxmanifiesto = new ArrayList();

            this.fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto();

            if (fManifestarPedidosEnRuta.manifiestoActual.getEstadoManifiesto() == 0) {
                fManifestarPedidosEnRuta.manifiestoActual.setIsFree(1);

            }

            /* ENTRA AL CASO CORRESPONDIENTE DE ACUERDO AL ESTADO DEL MANIFIESTO */
            switch (this.fManifestarPedidosEnRuta.manifiestoActual.getEstadoManifiesto()) {

                case 0:
                    /* El caso cuando por primera vez se le crea a un vehículo
                     un manifiesto de distribución 
                     */
                    // fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto=new ArrayList();
                    crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);

                    listaFacturasxmanifiesto = new ArrayList();
                    this.fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto(listaFacturasxmanifiesto);
                    break;

                case 1:// CASO CUANDO NO HAY MANIFIESTOS PENDIENTES
                    // fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto=new ArrayList();
                    crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                    //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                    this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometrajeActual());
                    this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosEnRuta.txtnombreDeConductor.setEditable(false);

                    break;

                case 2:// TIENE MANIFIESTO ASIGANDO PERO NO SE HA CERRADO;
                    //this.fManifestarPedidosEnRuta.manifiestoActual.setRutArchivofacturasporManifiesto();
                    //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                    this.fManifestarPedidosEnRuta.modificarManifiesto();

                    this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                    //this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometraje());
                    break;

                case 3:// VEHICULO EN DISTRIBUCION,  NO SE PUEDE ASIGNAR OTRO MANIFIESTO

                    if (this.ini.getPropiedades().getProperty("permitirVariosManifiestos").equals("false")) {
                        JOptionPane.showInternalMessageDialog(this.fManifestarPedidosEnRuta, "Ese vehiculo ya tiene asignado un manifiesto de distribucion,\n"
                                + "debe liquidar y cerrarlo para crear uno nuevo", "Error", 0);
                        this.fManifestarPedidosEnRuta.cancelar();
                        break;
                    }

                    int reply = JOptionPane.showConfirmDialog(this.fManifestarPedidosEnRuta, "Desea Crear otro manifiesto de Distribucion?", "Vehiculo ya aparece en Ruta", YES_NO_OPTION, WARNING_MESSAGE);
                    this.fManifestarPedidosEnRuta.nuevo = false;

                    if (reply == JOptionPane.YES_OPTION) {

                        crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                        this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometrajeActual());
                        this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                        //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                    } else {

                        //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                        llenarDatosManifiestoCerrado();
                        this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                    }

                    /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                    if (this.fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                        fManifestarPedidosEnRuta.btnImprimir.setEnabled(true);
                    }

                    // JOptionPane.showInternalMessageDialog(this.fManifestarPedidosEnRuta, "ESE VEHICULO ESTA EN DISTRIBUCION EN ESTOS MOMENTOS,\n"
                    //       + "NO SE PUEDE CREAR UN NUEVO MANIFIESTO", "Error", 0);
                    break;

                case 4: // NUMERO DE MANIFIESTO YA DESCARGADO Y SE PUEDE CREAR OTRO
                    //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();

                    this.fManifestarPedidosEnRuta.jTabbedPane1.setEnabledAt(this.fManifestarPedidosEnRuta.jTabbedPane1.indexOfComponent(this.fManifestarPedidosEnRuta.pnlAgregarLista), true);
                    // jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlAgregarLista));
                    crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                    this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometrajeActual());
                    this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosEnRuta.txtKmDeSalida.requestFocus();

                    
                    
                    /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                    if (this.fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                        fManifestarPedidosEnRuta.btnImprimir.setEnabled(true);
                    }
                    break;

                case 5: // MANIFIESTO ANULADO

                    this.fManifestarPedidosEnRuta.jTabbedPane1.setEnabledAt(this.fManifestarPedidosEnRuta.jTabbedPane1.indexOfComponent(this.fManifestarPedidosEnRuta.pnlAgregarLista), true);
                    // jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlAgregarLista));
                    crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                    this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometrajeActual());
                    this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosEnRuta.txtKmDeSalida.requestFocus();
                    break;

            }

            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(false);
            this.fManifestarPedidosEnRuta.txtKmDeSalida.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(false);
        }
    }

    public void crearManifiesto() {
        try {

            fManifestarPedidosEnRuta.canalDeVenta = null;
            fManifestarPedidosEnRuta.ruta = null;

            /*Se crea un objeto ruta de distribucion*/
            for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                if (obj.getNombreRutasDeDistribucion().equals(fManifestarPedidosEnRuta.cbxRutaDeDistribucion.getSelectedItem().toString())) {
                    fManifestarPedidosEnRuta.ruta = new CRutasDeDistribucion(ini);
                    fManifestarPedidosEnRuta.ruta = obj;
                    break;
                }
                Thread.sleep(1);
            }

            /*Se crea un objeto canal de distribucion*/
            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                if (obj.getNombreCanalDeVenta().equals(fManifestarPedidosEnRuta.cbxCanales.getSelectedItem().toString())) {
                    fManifestarPedidosEnRuta.canalDeVenta = new CCanalesDeVenta(ini);
                    fManifestarPedidosEnRuta.canalDeVenta = obj;
                    break;
                }
                Thread.sleep(1);
            }

            fManifestarPedidosEnRuta.sumadorPesosFacturas = 0.0;
            /* Se validan todos los datos del manifiesdo conductor, carro, ruta, canal...*/
            if (validarManifiesto()) {

                //new Thread(new HiloCrearManifiesto(ini, fManifestarPedidosEnRuta)).start();
                manifestarRutasLocales();

            } else {
                JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, fManifestarPedidosEnRuta.mensaje, "Error al guardar El Manifiesto de salida a Ruta", 0);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
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

            this.fManifestarPedidosEnRuta.manifiestoActual.setVehiculo(this.fManifestarPedidosEnRuta.carro.getPlaca());
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
            Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosEnRuta.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }
    }
//
//    public void llenarDatosManifiestoCerrado() throws Exception {
//
//        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
//        // SE DESHABILITAN LOS COMPONENETES
//
//        fManifestarPedidosEnRuta.txtnombreDeConductor.setEditable(false);
//
//        fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setEditable(false);
//        fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setEditable(false);
//        fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setEditable(false);
//        fManifestarPedidosEnRuta.txtNombreDedespachador.setEditable(false);
//        fManifestarPedidosEnRuta.txtKmDeSalida.setEditable(false);
//        fManifestarPedidosEnRuta.txtPlaca.setEditable(false);
//        fManifestarPedidosEnRuta.txtKmDeSalida.setEditable(false);
//        fManifestarPedidosEnRuta.cbxCanales.setEnabled(false);
//        fManifestarPedidosEnRuta.cbxRutaDeDistribucion.setEnabled(false);
//        fManifestarPedidosEnRuta.dateManifiesto.setEnabled(false);
//        fManifestarPedidosEnRuta.btnCrearManifiesto.setEnabled(false);
//        fManifestarPedidosEnRuta.jTabbedPane1.setEnabled(false);
//        fManifestarPedidosEnRuta.btnNuevo.setEnabled(false);
//        fManifestarPedidosEnRuta.jBtnNuevo.setEnabled(false);
//
//        fManifestarPedidosEnRuta.btnImprimir.setEnabled(false);
//        fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(false);
//
//        fManifestarPedidosEnRuta.btnGrabar.setEnabled(false);
//        fManifestarPedidosEnRuta.jBtnGrabar.setEnabled(false);
//        fManifestarPedidosEnRuta.jBtnObservaciones.setEnabled(false);
//
//        fManifestarPedidosEnRuta.dateManifiesto.setEnabled(false);
//
//        fManifestarPedidosEnRuta.txtPlaca.setEnabled(false);
//        fManifestarPedidosEnRuta.txtnombreDeConductor.setEnabled(false);
//        fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setEnabled(false);
//        fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setEnabled(false);
//        fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setEnabled(false);
//        fManifestarPedidosEnRuta.txtNombreDedespachador.setEnabled(false);
//
//        fManifestarPedidosEnRuta.jTabbedPane1.setEnabledAt(fManifestarPedidosEnRuta.jTabbedPane1.indexOfComponent(fManifestarPedidosEnRuta.pnlAgregarLista), false);
//        fManifestarPedidosEnRuta.btnFile.setEnabled(false);
//        fManifestarPedidosEnRuta.btnAgregarFacturas.setEnabled(false);
//        fManifestarPedidosEnRuta.txtFile.setEnabled(false);
//        fManifestarPedidosEnRuta.listaDeFacturas1.setEnabled(false);
//        fManifestarPedidosEnRuta.jListListaDeFacturasErradas.setEnabled(false);
//
//        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
//        System.out.println("trae las facturas del manifiesto -> ");
//        //manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
//        System.out.println("trae la listade facturaspor manifiesto -> ");
//        // manifiestoActual.setListaFacturasPorManifiesto();
//        System.out.println("trae los datos de la vista facturas en manifiesto -> ");
//
//        fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto();
//        //listaDeCFacturasCamdunEnElManifiesto = manifiestoActual.getVstListaFacturasEnDistribucion(); // CfacturasCamdun
//        //listaDeFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto(); //CFacturasPorManifiesto
//
//        // manifiestoActual.setListaDeAuxiliares();
//        fManifestarPedidosEnRuta.listaDeAuxiliares = fManifestarPedidosEnRuta.manifiestoActual.getListaDeAuxiliares("" + fManifestarPedidosEnRuta.manifiestoActual.getNumeroManifiesto());
//
//        fManifestarPedidosEnRuta.manifiestoActual.setListaDeDescuentos();
//        fManifestarPedidosEnRuta.manifiestoActual.setListaDeRecogidas();
//
//        // SE BUSCA EL CONDUCTOR Y SE LLENA EL CAMPO
//        fManifestarPedidosEnRuta.txtnombreDeConductor.setText(fManifestarPedidosEnRuta.manifiestoActual.getNombreConductor() + " " + fManifestarPedidosEnRuta.manifiestoActual.getApellidosConductor());
//
//
//        /* Se llenan os campos de texto de los auiliares*/
//        llenarTxtAuxiliares();
//
//        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
//        if (fManifestarPedidosEnRuta.manifiestoActual.getDespachador().equals("0")) {
//            fManifestarPedidosEnRuta.txtNombreDedespachador.setText("");
//
//        } else {
//            fManifestarPedidosEnRuta.txtNombreDedespachador.setText(fManifestarPedidosEnRuta.manifiestoActual.getNombreDespachador() + " " + fManifestarPedidosEnRuta.manifiestoActual.getApellidosDespachador());
//
//        }
//
//        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
//        fManifestarPedidosEnRuta.cbxCanales.setSelectedItem(fManifestarPedidosEnRuta.manifiestoActual.getNombreCanal());
//
//        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
//        fManifestarPedidosEnRuta.cbxRutaDeDistribucion.setSelectedItem(fManifestarPedidosEnRuta.manifiestoActual.getNombreDeRuta());
//
//        fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + fManifestarPedidosEnRuta.manifiestoActual.getKmSalida());
//        fManifestarPedidosEnRuta.txtNumeroDeManifiesto.setText(fManifestarPedidosEnRuta.manifiestoActual.codificarManifiesto());
//
//        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
//        String strFecha = fManifestarPedidosEnRuta.manifiestoActual.getFechaDistribucion();
//        Date fecha = null;
//        fecha = formatoDelTexto.parse(strFecha);
//        //dateManifiesto.setDate(manifiestoActual.getFechaDistribucion());
//        Date dt = new Date();
//        fManifestarPedidosEnRuta.dateManifiesto.setDate(fecha);
//
//        int cantidadFacturasEnManifiesto = 0;
//        fManifestarPedidosEnRuta.sumadorPesosFacturas = 0.0;
//
//        fManifestarPedidosEnRuta.modelo2 = (DefaultTableModel) fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.getModel();
//
//        double valor = 0.0;
//        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
//        for (CFacturasPorManifiesto obj : fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto()) {
//
//            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
//            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
//            fManifestarPedidosEnRuta.filaTabla2 = fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.getRowCount();
//            fManifestarPedidosEnRuta.modelo2.addRow(new Object[fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.getRowCount()]);
//
//            fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt("" + (fManifestarPedidosEnRuta.filaTabla2 + 1), fManifestarPedidosEnRuta.filaTabla2, 0); // item 
//            fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), fManifestarPedidosEnRuta.filaTabla2, 1); // numero de la factura
//
//            fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), fManifestarPedidosEnRuta.filaTabla2, 2); // cliente
//
//            fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), fManifestarPedidosEnRuta.filaTabla2, 3); // valor a recaudar  de la factura   
//            fManifestarPedidosEnRuta.valorTotalManifiesto += obj.getValorARecaudarFactura();
//
//            // se ubica en la fila insertada
//            fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.changeSelection(fManifestarPedidosEnRuta.filaTabla2, 0, false, false);
//
//            cantidadFacturasEnManifiesto++;
//            valor += obj.getValorARecaudarFactura();
//            fManifestarPedidosEnRuta.sumadorPesosFacturas += obj.getPesoFactura();
//
//            // }
//            // }
//          Thread.sleep(10);
//        }
//        fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(valor);
//
//        fManifestarPedidosEnRuta.lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + fManifestarPedidosEnRuta.txtNumeroDeManifiesto.getText().trim());
//        DecimalFormat df = new DecimalFormat("#,###.0");
//        fManifestarPedidosEnRuta.lblPesoManifiesto.setText(df.format(fManifestarPedidosEnRuta.manifiestoActual.getPesoKgManifiesto() / 1000) + " Kg");
//        fManifestarPedidosEnRuta.lblValorRecaudoManifiesto.setText(nf.format(fManifestarPedidosEnRuta.manifiestoActual.getValorTotalManifiesto()));
//        fManifestarPedidosEnRuta.lblCantidadFacturas.setText("" + cantidadFacturasEnManifiesto);
//
//        fManifestarPedidosEnRuta.btnImprimir.setEnabled(true);
//        fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(true);
//
//        fManifestarPedidosEnRuta.btnImprimir.requestFocus();
//        fManifestarPedidosEnRuta.manifiestoActual.getPesoKgManifiesto();
//    }

    private void llenarTxtAuxiliares() throws InterruptedException {
        /*se llenan los campos de texto de los nombres de los auxiliares*/
        int indice = 1;

        fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
        fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
        fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setText("SALE A DISTRIBUCION SIN AUXILIAR3");
        fManifestarPedidosEnRuta.listaDeAuxiliares = fManifestarPedidosEnRuta.manifiestoActual.getListaDeAuxiliares("" + fManifestarPedidosEnRuta.manifiestoActual.getNumeroManifiesto());

        if (fManifestarPedidosEnRuta.listaDeAuxiliares.size() > 0) {
            for (CEmpleados aux : fManifestarPedidosEnRuta.listaDeAuxiliares) {
                switch (indice) {
                    case 1:
                        if (aux.getCedula().equals("0")) {
                            fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setText("");
                        } else {
                            fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;
                        break;
                    case 2:
                        if (aux.getCedula().equals("0")) {
                            fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setText("");
                        } else {
                            fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;
                        break;
                    case 3:
                        if (aux.getCedula().equals("0")) {
                            fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setText("");
                        } else {
                            fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;

                        break;

                }
                /* fin switch */
                Thread.sleep(10);

            }
        }

    }

    public void agregarFactura(String numeroDeFactura, boolean desdeArchivo, int adherencia) throws Exception {

        fManifestarPedidosEnRuta.facturaActual = null;

        try {

            /* se crea el objeto factura, digitado en el campo de texto */
            fManifestarPedidosEnRuta.facturaActual = new CFacturas(ini, numeroDeFactura);

            if (fManifestarPedidosEnRuta.facturaActual.getNumeroDeFactura() == null) {
                JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "La Factura  # " + numeroDeFactura + " no existe en servidor local", "Error, factura no existe", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (fManifestarPedidosEnRuta.facturaActual.getNombreDeCliente().contains("A N U L A D O")) {
                JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "La Factura  # " + numeroDeFactura + "s e encuentra anulada", "Error, factura Anulada", JOptionPane.WARNING_MESSAGE);
                fManifestarPedidosEnRuta.facturaActual = null;
                return;
            }

            /* SE VALIDA QUE LA FACTURA EXISTA  */
            if (fManifestarPedidosEnRuta.facturaActual.getNumeroDeFactura() != null) {
                //factura = new CFacturas(ini, numeroDeFactura);

                /*Se valida el tipo de movimiento de la factura*/
                switch (fManifestarPedidosEnRuta.facturaActual.getEstadoFactura()) {

                    case 2:
                        /*Entrega total*/
                        JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);
                        return;

                    case 3:
                        /*Devolucion total*/
                        JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "La Factura  # " + numeroDeFactura + " fue devuelta de ruta", "Error, factura Devuelta", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 4:/*Entrega con novedad*/
                        JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 5:/*Entrega total con recogida*/
                        JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 6:
                        /*Re Envio*/
                        break;

                    case 8:
                        /*ANULADA*/
                        JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "La Factura  # " + numeroDeFactura + "  fue ANULADA", "Error, factura ANULADA", JOptionPane.ERROR_MESSAGE);
                        return;

                }

                CFacturasPorManifiesto facXMfto = new CFacturasPorManifiesto(ini);

                if (fManifestarPedidosEnRuta.facturaActual.getIsFree() == 0) {
                    JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "La Factura  # " + numeroDeFactura
                            + " ya se encuentra en Distribución "
                            + "", "Error", JOptionPane.WARNING_MESSAGE);

                    /* =1  la factura está disponible para agregarla al manifiesto 
                           SE registran las facturas que presentan inconvenientes en el Jlist*/
                    //modelo3.addElement(numeroDeFactura);
                    //jListListaDeFacturasErradas.setModel(modelo3);
                    // return;
                }


                /* SE valida que la factura no esté en el manifiesto */
                if (!fManifestarPedidosEnRuta.estaLaFacturaEnElManifiesto()) {
                    fManifestarPedidosEnRuta.jBtnMinuta.setEnabled(true);
                    
                    if(fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto() != null){
                         facXMfto.setAdherencia(fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto().size() + 1);
                    }else{
                         facXMfto.setAdherencia(1);
                         List<CFacturasPorManifiesto> lista = new ArrayList<>();
                         fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto(lista);
                          
                    }

                    /* Se agregan las propiedades a l objeto vista facturas por manifiesto */
                    facXMfto.setAdherencia(fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto().size() + 1);
                    facXMfto.setNumeroManifiesto(fManifestarPedidosEnRuta.manifiestoActual.getNumeroManifiesto());
                    facXMfto.setNumeroFactura(fManifestarPedidosEnRuta.facturaActual.getNumeroDeFactura());
                    facXMfto.setNombreDeCliente(fManifestarPedidosEnRuta.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fManifestarPedidosEnRuta.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorTotalFactura(fManifestarPedidosEnRuta.facturaActual.getValorTotalFactura());
                    facXMfto.setPesoFactura(fManifestarPedidosEnRuta.facturaActual.getPesofactura());
                    facXMfto.setConsecutivo(0);
                    facXMfto.setIdCanal(fManifestarPedidosEnRuta.manifiestoActual.getIdCanal());
                    facXMfto.setNombreCanal(fManifestarPedidosEnRuta.manifiestoActual.getNombreCanal());
                    facXMfto.setValorARecaudarFactura(fManifestarPedidosEnRuta.facturaActual.getValorTotalFactura());
                    facXMfto.setFechaDistribucion(fManifestarPedidosEnRuta.manifiestoActual.getFechaDistribucion());
                    facXMfto.setVehiculo(fManifestarPedidosEnRuta.manifiestoActual.getVehiculo());
                    facXMfto.setConductor(fManifestarPedidosEnRuta.manifiestoActual.getConductor());
                    facXMfto.setNombreConductor(fManifestarPedidosEnRuta.manifiestoActual.getNombreConductor());
                    facXMfto.setNombreDeCliente(fManifestarPedidosEnRuta.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fManifestarPedidosEnRuta.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorFacturaSinIva(fManifestarPedidosEnRuta.facturaActual.getValorFacturaSinIva());
                    facXMfto.setValorTotalFactura(fManifestarPedidosEnRuta.facturaActual.getValorTotalFactura());
                    facXMfto.setValorRechazo(0.0);
                    facXMfto.setValorDescuento(0.0);
                    facXMfto.setValorRecaudado(0.0);
                    facXMfto.setVendedor(fManifestarPedidosEnRuta.facturaActual.getVendedor());


                    /* se actualiza el canal  y la forma de pago de distribución de la factura */
                    // facturaActual.setCanal(canalDeVenta.getIdCanalDeVenta());
                    //fManifestarPedidosEnRuta.facturaActual.setFormaDePago(fManifestarPedidosEnRuta.formaDePago);
                    fManifestarPedidosEnRuta.sumadorPesosFacturas += facXMfto.getPesoFactura();
                    fManifestarPedidosEnRuta.valorTotalManifiesto += facXMfto.getValorTotalFactura();
                    fManifestarPedidosEnRuta.manifiestoActual.setPesoKgManifiesto(fManifestarPedidosEnRuta.sumadorPesosFacturas);
                    fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(fManifestarPedidosEnRuta.valorTotalManifiesto);

                    /* se verifica que la factura esté libre, sino arroja el siguiente mensaje... 
                     =0 indica que la factura esta siendo ocupada por alguien  */
 /* Se graba el registro temporal de factura por manifiesto en el archivo temporal*/
                    List<CProductosPorFactura> listaProduproductosEnLaFactura = new ArrayList();
                    CFacturas factura = new CFacturas(ini);
                    factura.setNumeroDeFactura(fManifestarPedidosEnRuta.facturaActual.getNumeroDeFactura());

                    factura.setListaCProductosPorFactura(false);
                    // Vst_ProductosPorFactura
                    listaProduproductosEnLaFactura = factura.getListaCProductosPorFactura();

                    if (listaProduproductosEnLaFactura == null || listaProduproductosEnLaFactura.size() < 1) {
                        JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "Factura sin productos ", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String listaDeCampos = facXMfto.getCadenaConLosCampos();

                    ArchivosDeTexto archivo = new ArchivosDeTexto("" + (new File(".").getAbsolutePath()).replace(".", "") + fManifestarPedidosEnRuta.manifiestoActual.getRutArchivofacturasporManifiesto());

                    if (archivo.insertarLineaEnFichero(listaDeCampos, fManifestarPedidosEnRuta.manifiestoActual.getRutArchivofacturasporManifiesto())) {

                        /* se agrega el registro al array  de facturas por manifiesto */
                        fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto().add(facXMfto);//CfacturasPorManifiesto
                        //  manifiestoActual.setListaFacturasPorManifiesto(listaFacturasPorManifiesto);

                        /*Validamos de donde proviene el dato: bien sea del campo 
                            de texto ó de un archivo plano */
                        if (!desdeArchivo) {
                            /* sí hay datos en la tabla de los productos  de la factura se limpia la tabla */
                            fManifestarPedidosEnRuta.limpiarDatodsDePanelProductosPorFactura();

                            fManifestarPedidosEnRuta.txtNombreDeCliente.setText(fManifestarPedidosEnRuta.facturaActual.getNombreDeCliente());
                            fManifestarPedidosEnRuta.txtDireccionCliente.setText(fManifestarPedidosEnRuta.facturaActual.getDireccionDeCliente());
                            fManifestarPedidosEnRuta.txtTelefonoCliente.setText(fManifestarPedidosEnRuta.facturaActual.getTelefonoCliente());
                            fManifestarPedidosEnRuta.txtBarroCliente.setText(fManifestarPedidosEnRuta.facturaActual.getBarrio());
                            fManifestarPedidosEnRuta.txtNombreVendedor.setText(fManifestarPedidosEnRuta.facturaActual.getVendedor());
                            fManifestarPedidosEnRuta.lblValorFactura.setText(nf.format(fManifestarPedidosEnRuta.facturaActual.getValorTotalFactura()));

                            DecimalFormat df = new DecimalFormat("#,###.0");
                            fManifestarPedidosEnRuta.lblPesoDeLaFactura.setText(df.format((fManifestarPedidosEnRuta.facturaActual.getPesofactura()) / 1000) + " Kg");

//                            /* Se anexa la factura al array.
//                                 Se crea un array temporal de los productos que tiene la factura.. */
//                            List<CProductosPorFactura> listaProduproductosEnLaFactura = new ArrayList();
//
//                            CFacturas factura = new CFacturas(ini);
//                            factura.setNumeroFactura(facturaActual.getNumeroFactura());
//
//                            factura.setListaProductosPorFactura(false);
//                            // Vst_ProductosPorFactura
//                            listaProduproductosEnLaFactura = factura.getListaProductosPorFactura();
//                           
//                            if(listaProduproductosEnLaFactura == null || listaProduproductosEnLaFactura.size()< 1){
//                                JOptionPane.showInternalMessageDialog(this, "Factura sin productos ", "Error", JOptionPane.ERROR_MESSAGE);
//                                return;
//                            }

                            /* se llena la tabla de productos por Factura*/
                            llenarJtableProductosPorFactura(listaProduproductosEnLaFactura);

                        }
                        fManifestarPedidosEnRuta.sumadorPesosFacturas += fManifestarPedidosEnRuta.facturaActual.getPesofactura();

                        /*Se llena el Jtable correspondiente*/
                        llenarJtableFacturasPorManifiesto();

                        /* se ubica el cursor en la fila insertada */
                        fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.changeSelection(fManifestarPedidosEnRuta.filaTabla2, 0, false, false);

                        /* se imprime el dato en la respectiva etiqueta */
                        DecimalFormat df = new DecimalFormat("#,###.0");
                        fManifestarPedidosEnRuta.lblPesoManifiesto.setText(df.format(fManifestarPedidosEnRuta.manifiestoActual.getPesoKgManifiesto() / 1000) + " Kg");
                        fManifestarPedidosEnRuta.lblValorRecaudoManifiesto.setText(nf.format(fManifestarPedidosEnRuta.manifiestoActual.getValorTotalManifiesto()));
                        fManifestarPedidosEnRuta.btnGrabar.setEnabled(!fManifestarPedidosEnRuta.grabar);
                        fManifestarPedidosEnRuta.jBtnGrabar.setEnabled(!fManifestarPedidosEnRuta.grabar);
                        fManifestarPedidosEnRuta.jBtnObservaciones.setEnabled(!fManifestarPedidosEnRuta.grabar);

                    } else {
                        JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "Error al grabar en el archivo temporal, la  FACTURA # " + numeroDeFactura + "", "Error", JOptionPane.ERROR_MESSAGE);

                    }

                    /* si el registro existe en la tabla TMPfacturasPorManifiesto, hace nada */
                    fManifestarPedidosEnRuta.txtNombreDeCliente.requestFocus();

                }

                /* si no   existe la factura, arroja un mensaje de error. */
            } else {

                JOptionPane.showInternalMessageDialog(fManifestarPedidosEnRuta, "La factura # " + numeroDeFactura
                        + "  no existe en el sistema "
                        + "", "Error", JOptionPane.WARNING_MESSAGE);
            }

            fManifestarPedidosEnRuta.txtNombreDeCliente.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * Método que asigna las factura leidas desde un archivo de texto, al
     * manifiesto actual
     *
     * @param lista corresponde a un listado de numero de facturas tomados desde
     * un archivo de texto
     */
    private void agregarFacturasDesdeArchivo(ArrayList<String> lista) {

        String numeroDeFactura;
        fManifestarPedidosEnRuta.contadorDeFacturas = 0;

        try {

            int adherencia = 1;

            /* Se hace el recorrido por el array para crear los objetos*/
            for (String obj : lista) {
                numeroDeFactura = obj;
                agregarFactura(numeroDeFactura, true, adherencia);
                adherencia++;
                Thread.sleep(2);
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void agregarFacturasDesdeJList() throws HeadlessException, InterruptedException {
        /* 
        ***********************************************************************************************************/
        int deseaMontarFacturas = JOptionPane.showConfirmDialog(fManifestarPedidosEnRuta, "Desea adicionar las facturas?", "Sgregar facturas", JOptionPane.YES_NO_OPTION);

        if (deseaMontarFacturas == JOptionPane.YES_OPTION) {
            fManifestarPedidosEnRuta.btnAgregarFacturas.setEnabled(false);
            fManifestarPedidosEnRuta.btnFile.setEnabled(false);
            
            /*Modelo donde se guardaran las facturas con errores en el sistema*/
            fManifestarPedidosEnRuta.modelo3 = new DefaultListModel();

            // new Thread(new HiloCopiarfacturasEnJlist(fManifestarPedidosEnRuta)).start();
            ArrayList<String> lista = new ArrayList();

            if (fManifestarPedidosEnRuta != null) {

                fManifestarPedidosEnRuta.lblBarraDeProgreso.setVisible(true);

                /* Se realiza el proceso para verificar que no hayan facturas repetidas*/
                lista = listaDeFacturasNoRepetidas();

                /* Se invoca éste método de tal manera que ya teniendo la lista de las facturas, se le asignan al
           manifiesto actual */
                agregarFacturasDesdeArchivo(lista);
            }

        }
    }

    private ArrayList<String> listaDeFacturasNoRepetidas() throws InterruptedException {
        ArrayList<String> cadena = new ArrayList<>();
        boolean aparece = false;
        /* Se recorren todos los elementos del listado de  las facturas en el archivo */
        for (String obj : fManifestarPedidosEnRuta.listaDeFacturasEnElArchivo) {
            aparece = false;

            /* se hace la primera validacion en el que la cadena se encuentra vacía*/
            if (cadena.isEmpty()) {
                cadena.add(obj);

                /* En el caso cuando tiene la cadena mas de un elemento*/
            } else {
                /* Se recorre la segunda lista para validar sí el numero de factura existe */
                for (String obj2 : cadena) {

                    /* Si la factura ya existe en la lista devuelve true para indicar que ya existe el numero de la factura en el array 
                     y sale del bucle para continuar con el siguiente elemento */
                    if (obj.equals(obj2)) {
                        aparece = true;
                        break;
                    }
                    Thread.sleep(10);
                }
                /* al salir del bucle se valida que no existe la facura en el segundo array y se agrega el elmento.  */
                if (!aparece) {
                    cadena.add(obj);
                }
            }

            Thread.sleep(10);
        }

        return cadena;

    }

    public void llenarJtableFacturasPorManifiesto() {

        /* se anexa el registro a la Jtable de facturas por manifiesto */
        fManifestarPedidosEnRuta.filaTabla2 = fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.getRowCount();

        fManifestarPedidosEnRuta.modelo2.addRow(new Object[fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.getRowCount()]);

        fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt("" + (fManifestarPedidosEnRuta.filaTabla2 + 1), fManifestarPedidosEnRuta.filaTabla2, 0);
        fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt(fManifestarPedidosEnRuta.facturaActual.getNumeroDeFactura(), fManifestarPedidosEnRuta.filaTabla2, 1);
        fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt(fManifestarPedidosEnRuta.facturaActual.getNombreDeCliente(), fManifestarPedidosEnRuta.filaTabla2, 2);
        fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt(nf.format(fManifestarPedidosEnRuta.facturaActual.getValorTotalFactura()), fManifestarPedidosEnRuta.filaTabla2, 3);

        fManifestarPedidosEnRuta.valorTotalManifiesto += fManifestarPedidosEnRuta.facturaActual.getValorTotalFactura();

    }

    public void llenarJtableProductosPorFactura(List<CProductosPorFactura> arrProduproductosEnLaFactura) throws InterruptedException {
        fManifestarPedidosEnRuta.modelo1 = (DefaultTableModel) fManifestarPedidosEnRuta.jTableProductosPorFactura.getModel();

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura */
        for (CProductosPorFactura obj : arrProduproductosEnLaFactura) {

            Vst_ProductosPorFactura pxf = new Vst_ProductosPorFactura();

            int fila = fManifestarPedidosEnRuta.jTableProductosPorFactura.getRowCount();

            fManifestarPedidosEnRuta.modelo1.addRow(new Object[fManifestarPedidosEnRuta.jTableProductosPorFactura.getRowCount()]);

            fManifestarPedidosEnRuta.jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            fManifestarPedidosEnRuta.jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            fManifestarPedidosEnRuta.jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            fManifestarPedidosEnRuta.jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            fManifestarPedidosEnRuta.jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            fManifestarPedidosEnRuta.jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva() * obj.getCantidad()), fila, 5);
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
            fManifestarPedidosEnRuta.manifiestoActual.sacarMinutaManifiesto(seleccion);
        }
    }

    private void validarDocumentosVehiculo() {
        for (DocumentosPorVehiculo dxv : fManifestarPedidosEnRuta.carro.getListaDeDocumentosPorVehiculoVencidos()) {
            if (dxv.isDocumentoVencido()) {
                fManifestarPedidosEnRuta.carro.setVehiculoConDocumentosVencidos(true);
                break;
            }else{
                fManifestarPedidosEnRuta.carro.setVehiculoConDocumentosVencidos(false);
            }
        }

    }

    private void crearNuevoManifiesto(CCarros carro) {
        try {
            //limpiar();
            fManifestarPedidosEnRuta.manifiestoActual = new CManifiestosDeDistribucion(ini);

            //modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
            //txtPlaca.setText(carro.getPlaca());
            fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + carro.getKilometrajeActual());

            /* Se verifica que el carro tenga asignado un conductor */
            if (carro.getConductor().length() > 1) {
                try {
                    fManifestarPedidosEnRuta.conductor = new CEmpleados(ini);
                    for (CEmpleados obj : ini.getListaDeEmpleados()) {
                        if (obj.getCedula().equals(carro.getConductor())) {
                            fManifestarPedidosEnRuta.conductor = obj;
                        }

                    }
                    fManifestarPedidosEnRuta.txtnombreDeConductor.setText(fManifestarPedidosEnRuta.conductor.getNombres() + " " + fManifestarPedidosEnRuta.conductor.getApellidos());

                } catch (Exception ex) {
                    Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            /* SE DesHABILITA BOTON DE IMPRIMIR */
            fManifestarPedidosEnRuta.btnImprimir.setEnabled(false);
            fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(false);
            fManifestarPedidosEnRuta.jBtnMinuta.setEnabled(false);

            fManifestarPedidosEnRuta.txtnombreDeConductor.setEditable(false);

            fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setEditable(false);
            fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setEditable(false);
            fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setEditable(false);

            fManifestarPedidosEnRuta.txtKmDeSalida.setEditable(true);
            fManifestarPedidosEnRuta.txtPlaca.setEnabled(true);
            fManifestarPedidosEnRuta.txtPlaca.setEditable(true);
            fManifestarPedidosEnRuta.cbxCanales.setEnabled(true);
            fManifestarPedidosEnRuta.cbxRutaDeDistribucion.setEnabled(true);
            fManifestarPedidosEnRuta.dateManifiesto.setEnabled(true);
            fManifestarPedidosEnRuta.jTabbedPane1.setEnabled(true);
            fManifestarPedidosEnRuta.jTabbedPane1.setEnabledAt(fManifestarPedidosEnRuta.jTabbedPane1.indexOfComponent(fManifestarPedidosEnRuta.pnlAgregarFactura), true);
            fManifestarPedidosEnRuta.btnCrearManifiesto.setEnabled(true);

            fManifestarPedidosEnRuta.txtNombreDedespachador.setEnabled(true);

            fManifestarPedidosEnRuta.txtnombreDeConductor.setEnabled(true);
            fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setEnabled(true);
            fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setEnabled(true);
            fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setEnabled(true);
            fManifestarPedidosEnRuta.txtNombreDedespachador.setEnabled(true);

            if (fManifestarPedidosEnRuta.manifiestoActual != null) {
                fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(1);
            }

            fManifestarPedidosEnRuta.txtKmDeSalida.requestFocus();
            fManifestarPedidosEnRuta.txtKmDeSalida.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean validarManifiesto() {
        boolean verificado = true;
        try {

            fManifestarPedidosEnRuta.mensaje = "";

            if (fManifestarPedidosEnRuta.carro == null) {
                fManifestarPedidosEnRuta.mensaje += "La Ruta no tiene vehiculo asignado" + "  \n";
                verificado = false;
            }

            if (fManifestarPedidosEnRuta.carro.isVehiculoConDocumentosVencidos()) {
                fManifestarPedidosEnRuta.mensaje += "El vehiculo tiene documentos vencidos" + "  \n";
                verificado = false;
            }

            if (fManifestarPedidosEnRuta.cbxCanales.getSelectedIndex() == 0) {
                fManifestarPedidosEnRuta.mensaje += "No ha seleccionado el canal de distribución " + "  \n";
                verificado = false;
            }

            if (fManifestarPedidosEnRuta.cbxRutaDeDistribucion.getSelectedIndex() == 0) {
                fManifestarPedidosEnRuta.mensaje += "No ha seleccionado la Ruta de Distribución " + "  \n";
                verificado = false;
            }

//            if (!fi.equals(ff)) {
//                mensaje += "La fecha del Servidor y el Sistema no coinciden, verificar configuración del sistema " + "  \n";
//                verificado = false;
//            }
            if (fManifestarPedidosEnRuta.conductor == null) {
                fManifestarPedidosEnRuta.mensaje += "No ha asigando el conductor de la ruta" + "  \n";
                verificado = false;

            }
//            if (auxiliar1 == null) {
//                mensaje += "No ha asigando el Auxiliar de la ruta " + "  \n";
//                verificado = false;
//            } else if (auxiliar1.getCedula().equals(conductor.getCedula())) {
//                mensaje += "La cédula del conductor y del auxiliar son la misma  " + "  \n";
//                verificado = false;
//            }
//            
            if (fManifestarPedidosEnRuta.despachador == null) {
                fManifestarPedidosEnRuta.mensaje += "No ha asigando un despachador de ruta.El Campo es obligatorio " + "  \n";
                verificado = false;
            }

            if (fManifestarPedidosEnRuta.txtKmDeSalida.getText().isEmpty()) {
                fManifestarPedidosEnRuta.mensaje += "No ha colocado el kilometraje de salidad  del vehículo" + "  \n";
                verificado = false;
            }

            if (fManifestarPedidosEnRuta.canalDeVenta == null) {
                fManifestarPedidosEnRuta.mensaje += "No ha selecccionado El canal de venta " + "  \n";
                verificado = false;
            }
            if (fManifestarPedidosEnRuta.ruta == null) {
                fManifestarPedidosEnRuta.mensaje += "No ha selecccionado la Ruta a distribuir " + "  \n";
                verificado = false;
            }
            if (fManifestarPedidosEnRuta.manifiestoActual == null) {
                fManifestarPedidosEnRuta.mensaje += "Vehiculo ya tiene asignado un manifiesto de Distribución sin cerrar" + "  \n";
                verificado = false;
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificado;
    }

    public void llenarDatosManifiestoCerrado() throws Exception {

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        fManifestarPedidosEnRuta.txtnombreDeConductor.setEditable(false);

        fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setEditable(false);
        fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setEditable(false);
        fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setEditable(false);
        fManifestarPedidosEnRuta.txtNombreDedespachador.setEditable(false);
        fManifestarPedidosEnRuta.txtKmDeSalida.setEditable(false);
        fManifestarPedidosEnRuta.txtPlaca.setEditable(false);
        fManifestarPedidosEnRuta.txtKmDeSalida.setEditable(false);
        fManifestarPedidosEnRuta.cbxCanales.setEnabled(false);
        fManifestarPedidosEnRuta.cbxRutaDeDistribucion.setEnabled(false);
        fManifestarPedidosEnRuta.dateManifiesto.setEnabled(false);
        fManifestarPedidosEnRuta.btnCrearManifiesto.setEnabled(false);
        fManifestarPedidosEnRuta.jTabbedPane1.setEnabled(false);
        fManifestarPedidosEnRuta.btnNuevo.setEnabled(false);
        fManifestarPedidosEnRuta.jBtnNuevo.setEnabled(false);

        fManifestarPedidosEnRuta.btnImprimir.setEnabled(false);
        fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(false);

        fManifestarPedidosEnRuta.btnGrabar.setEnabled(false);
        fManifestarPedidosEnRuta.jBtnGrabar.setEnabled(false);
        fManifestarPedidosEnRuta.jBtnObservaciones.setEnabled(false);

        fManifestarPedidosEnRuta.dateManifiesto.setEnabled(false);

        fManifestarPedidosEnRuta.txtPlaca.setEnabled(false);
        fManifestarPedidosEnRuta.txtnombreDeConductor.setEnabled(false);
        fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setEnabled(false);
        fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setEnabled(false);
        fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setEnabled(false);
        fManifestarPedidosEnRuta.txtNombreDedespachador.setEnabled(false);

        fManifestarPedidosEnRuta.jTabbedPane1.setEnabledAt(fManifestarPedidosEnRuta.jTabbedPane1.indexOfComponent(fManifestarPedidosEnRuta.pnlAgregarLista), false);
        fManifestarPedidosEnRuta.btnFile.setEnabled(false);
        fManifestarPedidosEnRuta.btnAgregarFacturas.setEnabled(false);
        fManifestarPedidosEnRuta.txtFile.setEnabled(false);
        fManifestarPedidosEnRuta.listaDeFacturas1.setEnabled(false);
        fManifestarPedidosEnRuta.jListListaDeFacturasErradas.setEnabled(false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
        //manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        // manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto();
        //listaDeCFacturasCamdunEnElManifiesto = manifiestoActual.getVstListaFacturasEnDistribucion(); // CfacturasCamdun
        //listaDeFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto(); //CFacturasPorManifiesto

        // manifiestoActual.setListaDeAuxiliares();
        fManifestarPedidosEnRuta.listaDeAuxiliares = fManifestarPedidosEnRuta.manifiestoActual.getListaDeAuxiliares("" + fManifestarPedidosEnRuta.manifiestoActual.getNumeroManifiesto());

        fManifestarPedidosEnRuta.manifiestoActual.setListaDeDescuentos();
        fManifestarPedidosEnRuta.manifiestoActual.setListaDeRecogidas();

        // SE BUSCA EL CONDUCTOR Y SE LLENA EL CAMPO
        fManifestarPedidosEnRuta.txtnombreDeConductor.setText(fManifestarPedidosEnRuta.manifiestoActual.getNombreConductor() + " " + fManifestarPedidosEnRuta.manifiestoActual.getApellidosConductor());


        /* Se llenan os campos de texto de los auiliares*/
        llenarTxtAuxiliares();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (fManifestarPedidosEnRuta.manifiestoActual.getDespachador().equals("0")) {
            fManifestarPedidosEnRuta.txtNombreDedespachador.setText("");

        } else {
            fManifestarPedidosEnRuta.txtNombreDedespachador.setText(fManifestarPedidosEnRuta.manifiestoActual.getNombreDespachador() + " " + fManifestarPedidosEnRuta.manifiestoActual.getApellidosDespachador());

        }

        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
        fManifestarPedidosEnRuta.cbxCanales.setSelectedItem(fManifestarPedidosEnRuta.manifiestoActual.getNombreCanal());

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        fManifestarPedidosEnRuta.cbxRutaDeDistribucion.setSelectedItem(fManifestarPedidosEnRuta.manifiestoActual.getNombreDeRuta());

        fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + fManifestarPedidosEnRuta.manifiestoActual.getKmSalida());
        fManifestarPedidosEnRuta.txtNumeroDeManifiesto.setText(fManifestarPedidosEnRuta.manifiestoActual.codificarManifiesto());

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
        String strFecha = fManifestarPedidosEnRuta.manifiestoActual.getFechaDistribucion();
        Date fecha = null;
        fecha = formatoDelTexto.parse(strFecha);
        //dateManifiesto.setDate(manifiestoActual.getFechaDistribucion());
        Date dt = new Date();
        fManifestarPedidosEnRuta.dateManifiesto.setDate(fecha);

        int cantidadFacturasEnManifiesto = 0;
        fManifestarPedidosEnRuta.sumadorPesosFacturas = 0.0;

        fManifestarPedidosEnRuta.modelo2 = (DefaultTableModel) fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.getModel();

        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto()) {

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            fManifestarPedidosEnRuta.filaTabla2 = fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.getRowCount();
            fManifestarPedidosEnRuta.modelo2.addRow(new Object[fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.getRowCount()]);

            fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt("" + (fManifestarPedidosEnRuta.filaTabla2 + 1), fManifestarPedidosEnRuta.filaTabla2, 0); // item 
            fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), fManifestarPedidosEnRuta.filaTabla2, 1); // numero de la factura

            fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), fManifestarPedidosEnRuta.filaTabla2, 2); // cliente

            fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), fManifestarPedidosEnRuta.filaTabla2, 3); // valor a recaudar  de la factura   
            fManifestarPedidosEnRuta.valorTotalManifiesto += obj.getValorARecaudarFactura();

            // se ubica en la fila insertada
            fManifestarPedidosEnRuta.jTableFacturasPorManifiesto.changeSelection(fManifestarPedidosEnRuta.filaTabla2, 0, false, false);

            cantidadFacturasEnManifiesto++;
            valor += obj.getValorARecaudarFactura();
            fManifestarPedidosEnRuta.sumadorPesosFacturas += obj.getPesoFactura();

            // }
            // }
            //this.repaint();
        }
        valor = fManifestarPedidosEnRuta.manifiestoActual.getValorTotalManifiesto(false);
        fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(valor);

        fManifestarPedidosEnRuta.lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + fManifestarPedidosEnRuta.txtNumeroDeManifiesto.getText().trim());
        DecimalFormat df = new DecimalFormat("#,###.0");
        fManifestarPedidosEnRuta.lblPesoManifiesto.setText(df.format(fManifestarPedidosEnRuta.manifiestoActual.getPesoKgManifiesto() / 1000) + " Kg");
        fManifestarPedidosEnRuta.lblValorRecaudoManifiesto.setText(nf.format(fManifestarPedidosEnRuta.manifiestoActual.getValorTotalManifiesto()));
        fManifestarPedidosEnRuta.lblCantidadFacturas.setText("" + cantidadFacturasEnManifiesto);

        fManifestarPedidosEnRuta.btnImprimir.setEnabled(true);
        fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(true);
        
        fManifestarPedidosEnRuta.jBtnMinuta.setEnabled(true);

        fManifestarPedidosEnRuta.btnImprimir.requestFocus();
        fManifestarPedidosEnRuta.manifiestoActual.getPesoKgManifiesto();
    }

    public void cargarVista() {
        // SE LLENAN LAS LISTAS DESPLEGABLES
        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            if (obj.getActivoCanal() == 1) {
                fManifestarPedidosEnRuta.cbxCanales.addItem(obj.getNombreCanalDeVenta());
            }
        }
        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            if (obj.getActivoRutasDeDistribucion() == 1) {
                fManifestarPedidosEnRuta.cbxRutaDeDistribucion.addItem(obj.getNombreRutasDeDistribucion());
            }
        }
        fManifestarPedidosEnRuta.autoTxtVehiculos = new TextAutoCompleter(fManifestarPedidosEnRuta.txtPlaca);
        for (CVehiculos car : this.ini.getListaDeVehiculos()) {
            if (car.getActivoVehiculo() == 1) {
                fManifestarPedidosEnRuta.autoTxtVehiculos.addItem(car.getPlaca() + " ");
            }
        }
        String strMain = this.ini.getPrefijos();
        String[] arrSplit = strMain.split(",");
        for (int i = 0; i < arrSplit.length; i++) {
            fManifestarPedidosEnRuta.cbxPrefijos.addItem(arrSplit[i].replace("'", ""));
        }
        fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
        fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(false);
        fManifestarPedidosEnRuta.cargado = true;
    }

    private void refrescar() {

        fManifestarPedidosEnRuta.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        fManifestarPedidosEnRuta.habilitar(false);
        ini.setListaDeVehiculos(1);
        ini.setListaDeEmpleados();
        ini.setListaDeManifiestossinDescargar(3, false);
        fManifestarPedidosEnRuta.habilitar(true);
        fManifestarPedidosEnRuta.jBtnDocVencidos.setEnabled(false);
        fManifestarPedidosEnRuta.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N

    }

    public void grabarFacturasPorManifiesto() throws HeadlessException {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

        this.fManifestarPedidosEnRuta.btnImprimir.setEnabled(false);
        this.fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(false);

        this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        this.fManifestarPedidosEnRuta.repaint();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(false);
        this.fManifestarPedidosEnRuta.jBtnGrabar.setEnabled(false);
        this.fManifestarPedidosEnRuta.jBtnObservaciones.setEnabled(false);

        this.fManifestarPedidosEnRuta.txtNumeroDeFactura.setEnabled(false);
        this.fManifestarPedidosEnRuta.pnlAgregarLista.setEnabled(false);

        this.fManifestarPedidosEnRuta.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;

        /* Se cambia el estado de manifiesto a manifiesto cerrado*/
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (this.fManifestarPedidosEnRuta.manifiestoActual.grabarFacturasPorManifiesto(true)) {

                fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(3);
                fManifestarPedidosEnRuta.manifiestoActual.setIsFree(1);
                fManifestarPedidosEnRuta.manifiestoActual.actualizarManifiestoDeDistribucions();

                fManifestarPedidosEnRuta.manifiestoActual.setHoraDeDespacho("" + new Date());
                fManifestarPedidosEnRuta.manifiestoActual.setPesoKgManifiesto(fManifestarPedidosEnRuta.sumadorPesosFacturas);

                this.fManifestarPedidosEnRuta.estaOcupadoGrabando = false;
                this.fManifestarPedidosEnRuta.btnImprimir.setEnabled(true);
                this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(false);

                this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                this.fManifestarPedidosEnRuta.jBtnMinuta.setEnabled(true);

                this.fManifestarPedidosEnRuta.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {

                this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(true);
            this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        } catch (Exception ex) {
            Logger.getLogger(HiloFmanifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        }

        this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
    }

}
