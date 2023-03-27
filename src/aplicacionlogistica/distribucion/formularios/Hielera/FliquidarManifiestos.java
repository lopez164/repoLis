/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.PrincipalLogistica;
import aplicacionlogistica.distribucion.consultas.FBuscarManifiestos;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CMovimientosManifiestosfacturas;
import aplicacionlogistica.distribucion.formularios.FDescargarEntregasTotalesConDescuento;
import aplicacionlogistica.distribucion.formularios.FDescargarRechazosParciales;
import aplicacionlogistica.distribucion.formularios.Hielera.Threads.HiloFLiquidarManifiestos;
import aplicacionlogistica.distribucion.objetos.CCuentasBancarias;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CEntidadesBancarias;
import aplicacionlogistica.distribucion.integrador.HiloIntegradorTNSLaHielera;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import mtto.vehiculos.CCarros;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Eduardo López Casanova
 */
public final class FliquidarManifiestos extends javax.swing.JInternalFrame {

    //public Vst_empleados conductor = null;
    //public CManifiestosDeDistribucion manifiestoActual = null;
    //  public CEmpleados conductorActual = null;
    public CManifiestosDeDistribucion manifiesto = null;
    public CCarros carro = null;
    public CFacturasPorManifiesto facturaActual = null;
    public boolean conductorLiquidado = false;
    public Double valorTotalRecaudado = 0.0;
    public Double valorTotalRecogidas = 0.0;
    public Double valorTotalAConsignar = 0.0;
    public double valorRecogida;
    public double valorDescontado;
    public Double valorConsignado = 0.0;
    public boolean isFormularioCompleto = false;
    public boolean cargado = false;
    public boolean nuevo = false;
    public boolean actualizar = false;
    public boolean liberado = false;
    public boolean grabar = false;
    public boolean estaOcupadoGrabando = false;
    public boolean isFacturaCredito = false;
    public boolean esteManifiestoEsMio = false;
    public int kilometrosRecorridos;
    public int filaSeleccionada = -1;
    public int indiceLista = 0;
    public int columna = 0;
    public String mensaje = null;
    public int causalrechazo = 0;
    public String rutaDeArchivoFacturasDescargadas;
    public String rutaDeArchivoProductosDescargados;
    public String rutaDeArchivoRecogidasDescargado;
    public String rutaDeArchivoFacturasParaVolverAZonificar;
    public String rutaDeArchivoSoporteConsignaciones;
    public int tipoMovimientoFactura = 0;
    public List<String> listaDeFacturasNoManifestadas;
    String senteciaSqlFacturasDescargadas = null;
    String senteciaSqlProductosPorFacturaDescargados = null;
    String senteciaSqlSoportesConsignaciones = null;
    public DefaultTableModel modelo1, modelo2, modelo3;

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    public Inicio ini = null;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    PrincipalLogistica form;
    FDescargarRechazosParciales fDescargueRechazosParciales;
    FDescargarEntregasTotalesConDescuento fDescargueEntregaTotalConDescuento;
    FBuscarManifiestos formulario;

    public TextAutoCompleter autoTxtNumeroManifiesto;
    /// public String fechaDistribucion;
    public int numeroManifiesto;

    public DescargarFacturas_2 descargarFacturas_2 = null;

//int numeroDeFacturasEnManifiesto = 0;
    //int cantidadFacturasDescargadasManifiesto = 0;
    //int cantidadFacturasPendientesPorDescargar = 0;
    //int kilometrosEntrada;
    // public List<Vst_FacturasDescargadas> listaFacturasPorManifiesto;
    //public List<Vst_FacturasDescargadas> listaFacturasDescargadas;
    // public List<CFacturasPorManifiesto> listaFacturasPendientesPorDescargar;
    //public List<CRecogidasPorManifiesto> listaDeRecogidasPorManifiesto;
    //public List<CProductosPorFactura> listaDeCProductosPorFacturaDescargados;
    //public List<CSoportesConsignaciones> listaDeSoportesDeConsignaciones;
    // public List<CFacturasCamdun> listaFacturaTemporal;
    //public Vst_FacturasPorManifiesto facturaDescargada = null;
    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param form
     */
    public FliquidarManifiestos(PrincipalLogistica form) {
        this.form = form;
        this.ini = form.ini;
        //CargarVista();
    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param ini
     */
    public FliquidarManifiestos(Inicio ini) {
        this.ini = ini;

        initComponents();
        cbxMovimientoFactura.removeAllItems();
        cbxCausalDeRechazoFactura.removeAllItems();
        cbxEntidadesBancarias.removeAllItems();
        cbxNumeroDeCuenta.removeAllItems();
        cbxPrefijo.removeAllItems();

        for (CMovimientosManifiestosfacturas obj : this.ini.getListaDeMovimientosFacturas()) {
            cbxMovimientoFactura.addItem(obj.getNombreMovimientosManifiestosfacturas());
        }
        //CausalesDeRechazo
        for (CCausalesDeDevolucion obj : this.ini.getListaDeCausalesDeDevolucion()) {
            cbxCausalDeRechazoFactura.addItem(obj.getNombreCausalesDeDevolucion());
        }

        String strMain = this.ini.getPrefijos();
        String[] arrSplit = strMain.split(",");
        for (int i = 0; i < arrSplit.length; i++) {
            cbxPrefijo.addItem(arrSplit[i].replace("'", ""));
        }
 this.moveToFront();
        // CargarVista();
    }

    public FliquidarManifiestos(Inicio ini, DescargarFacturas_2 descargarFacturas_2, CManifiestosDeDistribucion manifiesto) throws InterruptedException {
        initComponents();

        

        this.ini = ini;
        this.manifiesto = manifiesto;
        //this.conductorActual = null;
        this.descargarFacturas_2 = descargarFacturas_2;
//        this.fechaDistribucion = fechaDistribucion;

        cbxMovimientoFactura.removeAllItems();
        cbxCausalDeRechazoFactura.removeAllItems();
        cbxEntidadesBancarias.removeAllItems();
        cbxNumeroDeCuenta.removeAllItems();
        cbxPrefijo.removeAllItems();

        for (CMovimientosManifiestosfacturas obj : this.ini.getListaDeMovimientosFacturas()) {
            cbxMovimientoFactura.addItem(obj.getNombreMovimientosManifiestosfacturas());
        }
        //CausalesDeRechazo
        for (CCausalesDeDevolucion obj : this.ini.getListaDeCausalesDeDevolucion()) {
            cbxCausalDeRechazoFactura.addItem(obj.getNombreCausalesDeDevolucion());
        }

        String strMain = this.ini.getPrefijos();
        String[] arrSplit = strMain.split(",");
        for (int i = 0; i < arrSplit.length; i++) {
            cbxPrefijo.addItem(arrSplit[i].replace("'", ""));
        }

        this.manifiesto.setListaFacturasPorManifiesto();
        this.manifiesto.setListaFacturasDescargadas();
        this.manifiesto.setListaFacturasPendientesPorDescargar();

        this.btnGrabar100.setEnabled(true);

        this.moveToFront();
//        conductorActual.setListaDeManifiestosPorConductor(fechaDistribucion);
//        conductorActual.setListaDeFacturaPorConductor(arrSplit[3]);
//        conductorActual.setListaFacturasDescargadas(arrSplit[3], false);
//        conductorActual.setListaFacturasPendientesPorDescargar();
        //carro = new CCarros(ini, conductorActual.getVehiculo());
        CargarVistaPorConductor();
    }

//    public FliquidarManifiestos(Inicio ini, DescargarFacturas_2 descargarFacturas_2, CManifiestosDeDistribucion manifiesto) {
//        this.ini = ini;
//        //this.manifiestoActual = manifiesto;
//        this.descargarFacturas_2 = descargarFacturas_2;
//
//        initComponents();
//
//        cbxMovimientoFactura.removeAllItems();
//        cbxCausalDeRechazoFactura.removeAllItems();
//        cbxEntidadesBancarias.removeAllItems();
//        cbxNumeroDeCuenta.removeAllItems();
//        cbxPrefijo.removeAllItems();
//
//        for (CMovimientosManifiestosfacturas obj : this.ini.getListaDeMovimientosFacturas()) {
//            cbxMovimientoFactura.addItem(obj.getNombreMovimientosManifiestosfacturas());
//        }
//        //CausalesDeRechazo
//        for (CCausalesDeDevolucion obj : this.ini.getListaDeCausalesDeDevolucion()) {
//            cbxCausalDeRechazoFactura.addItem(obj.getNombreCausalesDeDevolucion());
//        }
//
//        String strMain = this.ini.getPrefijos();
//        String[] arrSplit = strMain.split(",");
//        for (int i = 0; i < arrSplit.length; i++) {
//            cbxPrefijo.addItem(arrSplit[i].replace("'", ""));
//        }
//
//        manifiestoActual.setListaFacturasPorManifiesto();
//        manifiestoActual.setListaFacturasDescargadas();
//        manifiestoActual.setListaFacturasPendientesPorDescargar();
//
//        carro = new CCarros(ini, manifiestoActual.getVehiculo());
//
//        CargarVistaPorManifiesto();
//    }
    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     */
    public void CargarVistaPorConductor() {
        try {
            try {
                //String fecha = null;
//                lblCirculoDeProgreso.setVisible(false);
                //asignarTipoDescargue();

                new Thread(new HiloFLiquidarManifiestos(ini, this, "llenarjTableFacturasPorVehiculo")).start();

                valorTotalRecaudado = 0.0;
                //llenarjTableFacturasPorVehiculo();
                if (manifiesto.getListaFacturasDescargadas() != null) {
                    for (CFacturasPorManifiesto fac : manifiesto.getListaFacturasDescargadas()) {
                        valorTotalRecaudado += fac.getValorRecaudado();

                    }
                    lblValorRecaudoManifiesto.setText(nf.format(valorTotalRecaudado));
                    lblFacturasPendientes.setText("" + manifiesto.getListaFacturasPendientesPorDescargar().size());

                    this.setTitle("Formulario para Descargar las facturas  # "
                            + manifiesto.getNombreConductor() + " "
                            + manifiesto.getApellidosConductor()
                            + "-"
                            + manifiesto.getNumeroManifiesto());

                }

                //llenarjTableFacturasPorVehiculo();
//                txtKilometrosEntrada.setText("" + carro.getKilometrajeActual());
//                if (carro.getKilometrajeActual() > conductorActual.getKmSalida()) {
//                    cbxMovimientoFactura.setEnabled(true);
//                    cbxPrefijo.setEnabled(true);
//                }
                cbxMovimientoFactura.setEnabled(true);
                cbxPrefijo.setEnabled(true);

                /*Actualizamos la lista de manifiestos sin descargar */
                // new Thread(new HiloListaDeNumeroDeManifiestosPendientes(ini,3)).start();
                dateOperacion.setDate(new Date());
                dateOperacion.setEnabled(true);

                cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
                cbxMovimientoFactura.setSelectedItem("NINGUNO");

                manager.addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        //como dije, solo las notificaciones del tipo "typed" son las que actualizan los componentes
                        if (e.getID() == KeyEvent.KEY_TYPED) {
                            //if(e.getKeyChar()>='0' && e.getKeyChar()<='9' ){
                            if (e.getSource() instanceof JComponent
                                    // si el textfield esta marcado en el nombre y si el nombre es igual a "ignore_upper_case"
                                    // entonces el campo puede tomar las minusculas
                                    && ((JComponent) e.getSource()).getName() != null
                                    && ((JComponent) e.getSource()).getName().startsWith("numerico")) {

                                if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
                                    return false;
                                } else {
                                    return true;
                                }

                            } else if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
                                if (e.getSource() instanceof JComponent
                                        // si el textfield esta marcado en el nombre y si el nombre es igual a "ignore_upper_case"
                                        // entonces el campo puede tomar las minusculas
                                        && ((JComponent) e.getSource()).getName() != null
                                        && ((JComponent) e.getSource()).getName().startsWith("minuscula")) {
                                    return false;
                                } else {
                                    //como vamos a convertir todo a mayúsculas, entonces solo checamos si los caracteres son
                                    //minusculas
                                    e.setKeyChar((char) (((int) e.getKeyChar()) - 32));
                                }
                            }
                        }

                        //y listo, regresamos siempre falso para que las demas notificaciones continuen, si regresamos true
                        // significa que el dispatcher consumio el evento
                        return false;
                    }
                });
            } catch (Exception ex) {
                System.out.println("Error en dispatchKeyEvent ");
                Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
            }
            cargado = true;
            for (CEntidadesBancarias obj : ini.getListaDeEntidadesBancarias()) {
                cbxEntidadesBancarias.addItem(obj.getNombreEntidadBancaria());
            }

            if (this.ini.getListaDeManifiestossinDescargar() != null) {
                this.autoTxtNumeroManifiesto = new TextAutoCompleter(txtNombreConductor);
                for (CManifiestosDeDistribucion manifiesto : this.ini.getListaDeManifiestossinDescargar()) {

                    if (manifiesto.getEstadoManifiesto() != 4) {
                        autoTxtNumeroManifiesto.addItem(manifiesto.getNumeroManifiesto() + "");
                    }
                }
            }

//            for (Vst_empleados cond : ini.getListaDeEmpleados()) {
//                if (cond.getCedula().equals(conductorActual.getConductor())) {
//                    this.conductor = cond;
//                }
//            }
//            for (CCarros car : ini.getListaDeVehiculos()) {
//                if (car.getPlaca().equals(conductorActual.getVehiculo())) {
//                    this.carro = car;
//                }
//            }
            txtNombreConductor.setText(manifiesto.getNombreConductor() + " " + manifiesto.getApellidosConductor());
            // txtPlaca.setText(conductorActual.getVehiculo());
            // txtKilometrosSalida.setText("" + conductorActual.getKmSalida());
//            if (conductorActual.getKmEntrada() == conductorActual.getKmSalida()) {
//                txtKilometrosEntrada.setEnabled(true);
//                txtKilometrosEntrada.setEditable(true);
//                txtNumeroDeFactura.setEnabled(false);
//                txtNumeroDeFactura.setEditable(true);
//            }
            if (manifiesto.getListaFacturasPendientesPorDescargar().size() > 1) {
                btnGrabar100.setEnabled(true);
                jBtnGrabar100.setEnabled(true);
            }
            lblFacturasPendientes.setText("" + manifiesto.getListaFacturasPendientesPorDescargar().size());
            lblIdentificadorManifiesto.setText("Total a recaudar en Manifiesto # ");

        } catch (Exception ex) {
            Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }

        fDescargueEntregaTotalConDescuento = new FDescargarEntregasTotalesConDescuento();
        fDescargueRechazosParciales = new FDescargarRechazosParciales();
        formulario = new FBuscarManifiestos(ini);
        jBtnMinuta.setEnabled(false);

        //this.autoTxtNumeroManifiesto = new TextAutoCompleter(txtNumeroManifiesto);
    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     */
//    public void CargarVistaPorManifiesto() {
//        try {
//            try {
//
////                lblCirculoDeProgreso.setVisible(false);
//                //asignarTipoDescargue();
//                new Thread(new HiloFLiquidarManifiestos(ini, this, "asignarTipoDescargue")).start();
//                //llenarjTableFacturasPorVehiculo();
//                lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());
//                valorTotalRecaudado = 0.0;
//
//                this.setTitle("Formulario para Descargar las facturas del  manifiestos de Distribución # " + manifiestoActual.getNumeroManifiesto());
//                for (CFacturasPorManifiesto fac : manifiestoActual.getListaFacturasDescargadas()) {
//                    valorTotalRecaudado += fac.getValorRecaudado();
//                }
//
//                lblValorRecaudoManifiesto.setText(nf.format(valorTotalRecaudado));
//
//                //llenarjTableFacturasPorVehiculo();
////                txtKilometrosEntrada.setText("" + carro.getKilometrajeActual());
//                if (carro.getKilometrajeActual() > manifiestoActual.getKmSalida()) {
//                    cbxMovimientoFactura.setEnabled(true);
//                    cbxPrefijo.setEnabled(true);
//                }
//
//
//                /*Actualizamos la lista de manifiestos sin descargar */
//                // new Thread(new HiloListaDeNumeroDeManifiestosPendientes(ini,3)).start();
//                dateOperacion.setDate(new Date());
//                dateOperacion.setEnabled(true);
//
//                cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
//                cbxMovimientoFactura.setSelectedItem("NINGUNO");
//
//                manager.addKeyEventDispatcher(new KeyEventDispatcher() {
//                    @Override
//                    public boolean dispatchKeyEvent(KeyEvent e) {
//                        //como dije, solo las notificaciones del tipo "typed" son las que actualizan los componentes
//                        if (e.getID() == KeyEvent.KEY_TYPED) {
//                            //if(e.getKeyChar()>='0' && e.getKeyChar()<='9' ){
//                            if (e.getSource() instanceof JComponent
//                                    // si el textfield esta marcado en el nombre y si el nombre es igual a "ignore_upper_case"
//                                    // entonces el campo puede tomar las minusculas
//                                    && ((JComponent) e.getSource()).getName() != null
//                                    && ((JComponent) e.getSource()).getName().startsWith("numerico")) {
//
//                                if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
//                                    return false;
//                                } else {
//                                    return true;
//                                }
//
//                            } else if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
//                                if (e.getSource() instanceof JComponent
//                                        // si el textfield esta marcado en el nombre y si el nombre es igual a "ignore_upper_case"
//                                        // entonces el campo puede tomar las minusculas
//                                        && ((JComponent) e.getSource()).getName() != null
//                                        && ((JComponent) e.getSource()).getName().startsWith("minuscula")) {
//                                    return false;
//                                } else {
//                                    //como vamos a convertir todo a mayúsculas, entonces solo checamos si los caracteres son
//                                    //minusculas
//                                    e.setKeyChar((char) (((int) e.getKeyChar()) - 32));
//                                }
//                            }
//                        }
//
//                        //y listo, regresamos siempre falso para que las demas notificaciones continuen, si regresamos true
//                        // significa que el dispatcher consumio el evento
//                        return false;
//                    }
//                });
//            } catch (Exception ex) {
//                System.out.println("Error en dispatchKeyEvent ");
//                Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            cargado = true;
//            for (CEntidadesBancarias obj : ini.getListaDeEntidadesBancarias()) {
//                cbxEntidadesBancarias.addItem(obj.getNombreEntidadBancaria());
//            }
//
//            if (this.ini.getListaDeManifiestossinDescargar() != null) {
//                this.autoTxtNumeroManifiesto = new TextAutoCompleter(txtNombreConductor);
//                for (CManifiestosDeDistribucion manifiesto : this.ini.getListaDeManifiestossinDescargar()) {
//
//                    if (manifiesto.getEstadoManifiesto() != 4) {
//                        autoTxtNumeroManifiesto.addItem(manifiesto.getNumeroManifiesto() + "");
//                    }
//                }
//            }
//
////            for (Vst_empleados cond : ini.getListaDeEmpleados()) {
////                if (cond.getCedula().equals(manifiestoActual.getConductor())) {
////                    this.conductorActual = cond;
////                }
////            }
//            for (CCarros car : ini.getListaDeVehiculos()) {
//                if (car.getPlaca().equals(manifiestoActual.getVehiculo())) {
//                    this.carro = car;
//                }
//            }
//
//            txtNombreConductor.setText(manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor());
////            txtPlaca.setText(manifiestoActual.getVehiculo());
////            txtKilometrosSalida.setText("" + manifiestoActual.getKmSalida());
//            if (manifiestoActual.getKmEntrada() == manifiestoActual.getKmSalida()) {
////                txtKilometrosEntrada.setEnabled(true);
////                txtKilometrosEntrada.setEditable(true);
//                txtNumeroDeFactura.setEnabled(false);
//                txtNumeroDeFactura.setEditable(true);
//            }
//            if (conductorActual.getListaFacturasPendientesPorDescargar().size() > 1) {
//                btnGrabar100.setEnabled(true);
//            }
//            lblFacturasPendientes.setText("" + conductorActual.getListaFacturasPendientesPorDescargar().size());
//            lblIdentificadorManifiesto.setText("Total a recaudar en Manifiesto # " + manifiestoActual.codificarManifiesto());
//
//        } catch (Exception ex) {
//            Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        fDescargueEntregaTotalConDescuento = new FDescargarEntregasTotalesConDescuento();
//        fDescargueRechazosParciales = new FDescargarRechazosParciales();
//        formulario = new FBuscarManifiestos(ini);
//        jBtnMinuta.setEnabled(false);
//
//        //this.autoTxtNumeroManifiesto = new TextAutoCompleter(txtNumeroManifiesto);
//    }
    /**
     * This method is called from within the constructor to initialize the
     * fCambiarClave. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnGrabar100 = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();
        chkCerrarManifiesto = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtNombreConductor = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbxMovimientoFactura = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        txtNumeroDeFactura = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        rBtnCredito = new javax.swing.JRadioButton();
        rBtnContado = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListFacturasNoManifestadas = new javax.swing.JList();
        jProgressBar1 = new javax.swing.JProgressBar();
        cbxPrefijo = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        lblFacturasPendientes = new javax.swing.JLabel();
        chkConDescuento = new javax.swing.JCheckBox();
        lblValorFactura = new org.edisoncor.gui.label.LabelCustom();
        txtCedulaConductor = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlEntregas = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableProductosPorFactura = new javax.swing.JTable();
        pnlRechazosTotales = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        cbxCausalDeRechazoFactura = new javax.swing.JComboBox();
        btnDescargarRechazoTotal = new javax.swing.JButton();
        pnlRechazosParciales = new javax.swing.JPanel();
        pnlRecogidas = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableRecogidas = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        txtNumeroDeSoporteRecogida = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtFacturaAfectada = new javax.swing.JTextField();
        btnDescargarRechazoTotal1 = new javax.swing.JButton();
        btnAgregarRecogida = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        txtValorRecogida = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtValorDescontado = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jBtnConsignaciones = new javax.swing.JButton();
        txtValorConsignacion = new javax.swing.JTextField();
        txtMedioDePago = new javax.swing.JTextField();
        cbxEntidadesBancarias = new javax.swing.JComboBox<>();
        txtNumeroSoporteConsignacion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dateOperacion = new com.toedter.calendar.JDateChooser();
        cbxNumeroDeCuenta = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        lblValorConsignado = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableListaDeConsignaciones = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnGrabar100 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblFacturasDescargadasPorVehiculo = new javax.swing.JTable();
        lblIdentificadorManifiesto = new javax.swing.JLabel();
        lblValorRecaudoManifiesto = new org.edisoncor.gui.label.LabelCustom();

        setIconifiable(true);
        setTitle("Formulario para Descargar las facturas del  manifiestos de Distribución");
        setFocusTraversalPolicyProvider(true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(1141, 709));
        setMinimumSize(new java.awt.Dimension(1141, 709));
        setVerifyInputWhenFocusTarget(false);
        setVisible(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo.setEnabled(false);
        jBtnNuevo.setFocusable(false);
        jBtnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnNuevo);

        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jBtnGrabar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnGrabar.setEnabled(false);
        jBtnGrabar.setFocusable(false);
        jBtnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGrabarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGrabar);

        jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N
        jBtnGrabar100.setToolTipText("100 %");
        jBtnGrabar100.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnGrabar100.setEnabled(false);
        jBtnGrabar100.setFocusable(false);
        jBtnGrabar100.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGrabar100.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGrabar100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGrabar100ActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGrabar100);

        jBtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jBtnImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnImprimir.setEnabled(false);
        jBtnImprimir.setFocusable(false);
        jBtnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnImprimirActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnImprimir);

        jBtnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnCancel.setEnabled(false);
        jBtnCancel.setFocusable(false);
        jBtnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnCancel);

        jBtnMinuta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Table.png"))); // NOI18N
        jBtnMinuta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnMinuta.setEnabled(false);
        jBtnMinuta.setFocusable(false);
        jBtnMinuta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnMinuta.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnMinuta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnMinutaActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnMinuta);

        jBtnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jBtnExit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExit.setFocusable(false);
        jBtnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExitActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExit);

        chkCerrarManifiesto.setText("Cerrar Manifiesto");
        chkCerrarManifiesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCerrarManifiestoActionPerformed(evt);
            }
        });
        jToolBar1.add(chkCerrarManifiesto);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Descargar facturas"));

        txtNombreConductor.setEnabled(false);
        txtNombreConductor.setInheritsPopupMenu(true);
        txtNombreConductor.setName("numerico"); // NOI18N
        txtNombreConductor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreConductorFocusGained(evt);
            }
        });
        txtNombreConductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreConductorActionPerformed(evt);
            }
        });
        txtNombreConductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreConductorKeyPressed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText(" Movimiento");

        cbxMovimientoFactura.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxMovimientoFacturaItemStateChanged(evt);
            }
        });
        cbxMovimientoFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbxMovimientoFacturaFocusLost(evt);
            }
        });
        cbxMovimientoFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMovimientoFacturaActionPerformed(evt);
            }
        });
        cbxMovimientoFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxMovimientoFacturaKeyPressed(evt);
            }
        });

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("# de Factura");

        txtNumeroDeFactura.setEditable(false);
        txtNumeroDeFactura.setEnabled(false);
        txtNumeroDeFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeFacturaFocusGained(evt);
            }
        });
        txtNumeroDeFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroDeFacturaActionPerformed(evt);
            }
        });
        txtNumeroDeFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeFacturaKeyPressed(evt);
            }
        });

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Conductor");

        buttonGroup1.add(rBtnCredito);
        rBtnCredito.setText("Crédito");
        rBtnCredito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rBtnCreditoMouseClicked(evt);
            }
        });
        rBtnCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rBtnCreditoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rBtnContado);
        rBtnContado.setSelected(true);
        rBtnContado.setText("Contado");
        rBtnContado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rBtnContadoMouseClicked(evt);
            }
        });
        rBtnContado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rBtnContadoActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Facturas sin Manifestar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 8))); // NOI18N

        jListFacturasNoManifestadas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jListFacturasNoManifestadas);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pendientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 9))); // NOI18N

        lblFacturasPendientes.setFont(new java.awt.Font("Dialog", 1, 40)); // NOI18N
        lblFacturasPendientes.setText(".");
        lblFacturasPendientes.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblFacturasPendientesMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(lblFacturasPendientes, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(lblFacturasPendientes)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        chkConDescuento.setText("Con Descuento");
        chkConDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkConDescuentoActionPerformed(evt);
            }
        });

        lblValorFactura.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValorFactura.setText("$ 0.0");
        lblValorFactura.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lblValorFactura.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtCedulaConductor.setEnabled(false);
        txtCedulaConductor.setInheritsPopupMenu(true);
        txtCedulaConductor.setName("numerico"); // NOI18N
        txtCedulaConductor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCedulaConductorFocusGained(evt);
            }
        });
        txtCedulaConductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaConductorActionPerformed(evt);
            }
        });
        txtCedulaConductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCedulaConductorKeyPressed(evt);
            }
        });

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Cedula");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(cbxMovimientoFactura, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(lblValorFactura, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCedulaConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(chkConDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rBtnContado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rBtnCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(176, 176, 176))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxPrefijo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(150, 150, 150)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCedulaConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxMovimientoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(38, 38, 38)
                        .addComponent(lblValorFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rBtnCredito)
                            .addComponent(rBtnContado)
                            .addComponent(chkConDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxPrefijo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pnlEntregas.setAutoscrolls(true);
        pnlEntregas.setDoubleBuffered(false);
        pnlEntregas.setEnabled(false);

        jTableProductosPorFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Ref", "Producto", "cantidad", "V. Unit", "Valor Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableProductosPorFactura.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableProductosPorFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableProductosPorFacturaMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTableProductosPorFactura);
        if (jTableProductosPorFactura.getColumnModel().getColumnCount() > 0) {
            jTableProductosPorFactura.getColumnModel().getColumn(0).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTableProductosPorFactura.getColumnModel().getColumn(1).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTableProductosPorFactura.getColumnModel().getColumn(1).setHeaderValue("Ref");
            jTableProductosPorFactura.getColumnModel().getColumn(2).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(2).setPreferredWidth(270);
            jTableProductosPorFactura.getColumnModel().getColumn(3).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTableProductosPorFactura.getColumnModel().getColumn(4).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(100);
            jTableProductosPorFactura.getColumnModel().getColumn(5).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        javax.swing.GroupLayout pnlEntregasLayout = new javax.swing.GroupLayout(pnlEntregas);
        pnlEntregas.setLayout(pnlEntregasLayout);
        pnlEntregasLayout.setHorizontalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addComponent(jScrollPane7)
                .addContainerGap())
        );
        pnlEntregasLayout.setVerticalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Entregas Totales", pnlEntregas);

        pnlRechazosTotales.setAutoscrolls(true);
        pnlRechazosTotales.setDoubleBuffered(false);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Causal de Devolucion");

        cbxCausalDeRechazoFactura.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxCausalDeRechazoFactura.setEnabled(false);
        cbxCausalDeRechazoFactura.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCausalDeRechazoFacturaItemStateChanged(evt);
            }
        });

        btnDescargarRechazoTotal.setText("Descargar");
        btnDescargarRechazoTotal.setEnabled(false);
        btnDescargarRechazoTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarRechazoTotalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRechazosTotalesLayout = new javax.swing.GroupLayout(pnlRechazosTotales);
        pnlRechazosTotales.setLayout(pnlRechazosTotalesLayout);
        pnlRechazosTotalesLayout.setHorizontalGroup(
            pnlRechazosTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRechazosTotalesLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(pnlRechazosTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDescargarRechazoTotal)
                    .addGroup(pnlRechazosTotalesLayout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addGap(18, 18, 18)
                        .addComponent(cbxCausalDeRechazoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(106, 106, 106))
        );
        pnlRechazosTotalesLayout.setVerticalGroup(
            pnlRechazosTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRechazosTotalesLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlRechazosTotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxCausalDeRechazoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDescargarRechazoTotal)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Devolucion Total", pnlRechazosTotales);

        pnlRechazosParciales.setBorder(javax.swing.BorderFactory.createTitledBorder("Manifiesto de Reparto"));
        pnlRechazosParciales.setEnabled(false);

        javax.swing.GroupLayout pnlRechazosParcialesLayout = new javax.swing.GroupLayout(pnlRechazosParciales);
        pnlRechazosParciales.setLayout(pnlRechazosParcialesLayout);
        pnlRechazosParcialesLayout.setHorizontalGroup(
            pnlRechazosParcialesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlRechazosParcialesLayout.setVerticalGroup(
            pnlRechazosParcialesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Rechazos Parciales", pnlRechazosParciales);

        jTableRecogidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Soporte", "Fact. Rec.", "Cliente.", "valor Descto."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableRecogidas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableRecogidas.setColumnSelectionAllowed(true);
        jTableRecogidas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRecogidasMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableRecogidas);
        jTableRecogidas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTableRecogidas.getColumnModel().getColumnCount() > 0) {
            jTableRecogidas.getColumnModel().getColumn(0).setResizable(false);
            jTableRecogidas.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTableRecogidas.getColumnModel().getColumn(1).setResizable(false);
            jTableRecogidas.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTableRecogidas.getColumnModel().getColumn(2).setResizable(false);
            jTableRecogidas.getColumnModel().getColumn(3).setResizable(false);
            jTableRecogidas.getColumnModel().getColumn(4).setResizable(false);
            jTableRecogidas.getColumnModel().getColumn(4).setPreferredWidth(250);
            jTableRecogidas.getColumnModel().getColumn(5).setResizable(false);
            jTableRecogidas.getColumnModel().getColumn(5).setPreferredWidth(120);
        }

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Numero Soporte");

        txtNumeroDeSoporteRecogida.setEditable(false);
        txtNumeroDeSoporteRecogida.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeSoporteRecogidaFocusGained(evt);
            }
        });
        txtNumeroDeSoporteRecogida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroDeSoporteRecogidaActionPerformed(evt);
            }
        });
        txtNumeroDeSoporteRecogida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeSoporteRecogidaKeyPressed(evt);
            }
        });

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Factura Afectada");

        txtFacturaAfectada.setEditable(false);
        txtFacturaAfectada.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFacturaAfectadaFocusGained(evt);
            }
        });
        txtFacturaAfectada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFacturaAfectadaActionPerformed(evt);
            }
        });
        txtFacturaAfectada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFacturaAfectadaKeyPressed(evt);
            }
        });

        btnDescargarRechazoTotal1.setText("Descargar");
        btnDescargarRechazoTotal1.setEnabled(false);
        btnDescargarRechazoTotal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarRechazoTotal1ActionPerformed(evt);
            }
        });

        btnAgregarRecogida.setText("Agregar");
        btnAgregarRecogida.setEnabled(false);
        btnAgregarRecogida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarRecogidaActionPerformed(evt);
            }
        });

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Valor Recogida");

        txtValorRecogida.setEditable(false);
        txtValorRecogida.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorRecogidaFocusGained(evt);
            }
        });
        txtValorRecogida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorRecogidaActionPerformed(evt);
            }
        });
        txtValorRecogida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorRecogidaKeyPressed(evt);
            }
        });

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Valor Descontado");

        txtValorDescontado.setEditable(false);
        txtValorDescontado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorDescontadoFocusGained(evt);
            }
        });
        txtValorDescontado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorDescontadoActionPerformed(evt);
            }
        });
        txtValorDescontado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorDescontadoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlRecogidasLayout = new javax.swing.GroupLayout(pnlRecogidas);
        pnlRecogidas.setLayout(pnlRecogidasLayout);
        pnlRecogidasLayout.setHorizontalGroup(
            pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRecogidasLayout.createSequentialGroup()
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRecogidasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDescargarRechazoTotal1))
                    .addGroup(pnlRecogidasLayout.createSequentialGroup()
                        .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlRecogidasLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(pnlRecogidasLayout.createSequentialGroup()
                                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtFacturaAfectada, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnlRecogidasLayout.createSequentialGroup()
                                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtNumeroDeSoporteRecogida, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnlRecogidasLayout.createSequentialGroup()
                                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtValorRecogida, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(pnlRecogidasLayout.createSequentialGroup()
                                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtValorDescontado, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnAgregarRecogida))))
                            .addGroup(pnlRecogidasLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlRecogidasLayout.setVerticalGroup(
            pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRecogidasLayout.createSequentialGroup()
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtNumeroDeSoporteRecogida, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtFacturaAfectada, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtValorRecogida, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValorDescontado, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(btnAgregarRecogida))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128)
                .addComponent(btnDescargarRechazoTotal1)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Recogidas", pnlRecogidas);

        jBtnConsignaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/OK.png"))); // NOI18N
        jBtnConsignaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnConsignacionesActionPerformed(evt);
            }
        });

        txtValorConsignacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorConsignacionFocusGained(evt);
            }
        });
        txtValorConsignacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorConsignacionKeyPressed(evt);
            }
        });

        txtMedioDePago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMedioDePagoFocusGained(evt);
            }
        });
        txtMedioDePago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMedioDePagoKeyPressed(evt);
            }
        });

        cbxEntidadesBancarias.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxEntidadesBancarias.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxEntidadesBancariasItemStateChanged(evt);
            }
        });

        txtNumeroSoporteConsignacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroSoporteConsignacionFocusGained(evt);
            }
        });
        txtNumeroSoporteConsignacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroSoporteConsignacionKeyPressed(evt);
            }
        });

        jLabel3.setText("numero Soporte");

        jLabel4.setText("Entidad Bancaria");

        jLabel5.setText("numero Cuenta");

        jLabel6.setText("Medio de pago");

        jLabel7.setText("Valor");

        dateOperacion.setToolTipText("Seleccionar la fecha ");
        dateOperacion.setDateFormatString("yyyy/MM/dd");
        dateOperacion.setEnabled(false);
        dateOperacion.setFont(new java.awt.Font("Verdana", 0, 9)); // NOI18N

        cbxNumeroDeCuenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Fecha Operacion");

        lblValorConsignado.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        lblValorConsignado.setText(".");
        lblValorConsignado.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblValorConsignadoMouseMoved(evt);
            }
        });

        jTableListaDeConsignaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Soporte", "Banco", "Medio de Pago", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableListaDeConsignaciones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableListaDeConsignaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableListaDeConsignacionesMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableListaDeConsignaciones);
        if (jTableListaDeConsignaciones.getColumnModel().getColumnCount() > 0) {
            jTableListaDeConsignaciones.getColumnModel().getColumn(0).setResizable(false);
            jTableListaDeConsignaciones.getColumnModel().getColumn(0).setPreferredWidth(130);
            jTableListaDeConsignaciones.getColumnModel().getColumn(1).setResizable(false);
            jTableListaDeConsignaciones.getColumnModel().getColumn(1).setPreferredWidth(260);
            jTableListaDeConsignaciones.getColumnModel().getColumn(2).setResizable(false);
            jTableListaDeConsignaciones.getColumnModel().getColumn(2).setPreferredWidth(230);
            jTableListaDeConsignaciones.getColumnModel().getColumn(3).setResizable(false);
            jTableListaDeConsignaciones.getColumnModel().getColumn(3).setPreferredWidth(130);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblValorConsignado, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cbxNumeroDeCuenta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtNumeroSoporteConsignacion))
                                    .addComponent(txtValorConsignacion, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtMedioDePago, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dateOperacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbxEntidadesBancarias, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(59, 59, 59)
                        .addComponent(jBtnConsignaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cbxEntidadesBancarias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtNumeroSoporteConsignacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtMedioDePago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cbxNumeroDeCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(dateOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtValorConsignacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))
                            .addComponent(jBtnConsignaciones, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValorConsignado, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jTabbedPane1.addTab("Consignaciones", jPanel6);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/exit.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setToolTipText("Salir de ingresar empleado...");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setPreferredSize(new java.awt.Dimension(97, 97));
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/cancel-64x64.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setToolTipText("Limpia la ventana para ingreasr registro...");
        btnCancelar.setEnabled(false);
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setPreferredSize(new java.awt.Dimension(97, 97));
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setToolTipText("Agregar ó Modificar un registro");
        btnNuevo.setEnabled(false);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setPreferredSize(new java.awt.Dimension(97, 97));
        btnNuevo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        btnGrabar.setText("Grabar");
        btnGrabar.setToolTipText("Cerrar manifiestos");
        btnGrabar.setEnabled(false);
        btnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabar.setPreferredSize(new java.awt.Dimension(97, 97));
        btnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/printer64x64.png"))); // NOI18N
        btnImprimir.setText("imprimir");
        btnImprimir.setToolTipText("Limpia la ventana para ingreasr registro...");
        btnImprimir.setEnabled(false);
        btnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprimir.setPreferredSize(new java.awt.Dimension(97, 97));
        btnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
        btnGrabar100.setText("100 %");
        btnGrabar100.setToolTipText("Guardar registro nuevo ó modificado");
        btnGrabar100.setEnabled(false);
        btnGrabar100.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabar100.setPreferredSize(new java.awt.Dimension(97, 97));
        btnGrabar100.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabar100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabar100ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnGrabar100, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGrabar100, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblFacturasDescargadasPorVehiculo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Cliente.", "Mov.", "valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFacturasDescargadasPorVehiculo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblFacturasDescargadasPorVehiculo.setColumnSelectionAllowed(true);
        tblFacturasDescargadasPorVehiculo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFacturasDescargadasPorVehiculoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblFacturasDescargadasPorVehiculo);
        tblFacturasDescargadasPorVehiculo.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tblFacturasDescargadasPorVehiculo.getColumnModel().getColumnCount() > 0) {
            tblFacturasDescargadasPorVehiculo.getColumnModel().getColumn(0).setResizable(false);
            tblFacturasDescargadasPorVehiculo.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblFacturasDescargadasPorVehiculo.getColumnModel().getColumn(1).setResizable(false);
            tblFacturasDescargadasPorVehiculo.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblFacturasDescargadasPorVehiculo.getColumnModel().getColumn(2).setResizable(false);
            tblFacturasDescargadasPorVehiculo.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblFacturasDescargadasPorVehiculo.getColumnModel().getColumn(3).setResizable(false);
            tblFacturasDescargadasPorVehiculo.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblFacturasDescargadasPorVehiculo.getColumnModel().getColumn(4).setResizable(false);
            tblFacturasDescargadasPorVehiculo.getColumnModel().getColumn(4).setPreferredWidth(110);
        }

        lblIdentificadorManifiesto.setFont(new java.awt.Font("Cantarell", 0, 12)); // NOI18N
        lblIdentificadorManifiesto.setText("Total a recaudar en Manifiesto # ");

        lblValorRecaudoManifiesto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValorRecaudoManifiesto.setText("$ 0.0");
        lblValorRecaudoManifiesto.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lblValorRecaudoManifiesto.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                    .addComponent(lblIdentificadorManifiesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblValorRecaudoManifiesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIdentificadorManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValorRecaudoManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 1120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        //salir();
        new Thread(new HiloFLiquidarManifiestos(ini, this, "salir")).start();
    }//GEN-LAST:event_btnSalirActionPerformed


    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        this.filaSeleccionada = -1;
        // nuevo();
        new Thread(new HiloFLiquidarManifiestos(ini, this, "nuevo")).start();

    }//GEN-LAST:event_btnNuevoActionPerformed


    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed

        //cerrarManifiesto();
        new Thread(new HiloFLiquidarManifiestos(ini, this, "cerrarManifiesto")).start();


    }//GEN-LAST:event_btnGrabarActionPerformed


    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.filaSeleccionada = -1;
        // cancelar();
        new Thread(new HiloFLiquidarManifiestos(ini, this, "cancelar")).start();

    }//GEN-LAST:event_btnCancelarActionPerformed


    private void tblFacturasDescargadasPorVehiculoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFacturasDescargadasPorVehiculoMouseClicked

        if (tblFacturasDescargadasPorVehiculo.getRowCount() > 0) {
            modelo2 = (DefaultTableModel) tblFacturasDescargadasPorVehiculo.getModel();

            /* se identifica la fila seleccionada de la tabla*/
            filaSeleccionada = tblFacturasDescargadasPorVehiculo.getSelectedRow();

            /*Se identifica el numero de la factura contenida en la fila seleccionada*/
            String numFactura = modelo2.getValueAt(filaSeleccionada, 1).toString();

            for (CFacturasPorManifiesto fac : manifiesto.getListaFacturasDescargadas()) {
                if (fac.getNumeroFactura().equals(numFactura)) {
                    this.facturaActual = fac;
                    txtNumeroDeFactura.setText(fac.getNumeroFactura());
                    txtNumeroDeFactura.setEnabled(false);
                    facturaActual.setListaProductosPorFactura(false);
                    llenarTablaProductosPorFactura();

                }
            }
        }

    }//GEN-LAST:event_tblFacturasDescargadasPorVehiculoMouseClicked

    private void txtNumeroDeFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaFocusGained
        txtNumeroDeFactura.setSelectionStart(0);
        txtNumeroDeFactura.setSelectionEnd(txtNumeroDeFactura.getText().length());

    }//GEN-LAST:event_txtNumeroDeFacturaFocusGained

    private void txtNumeroDeFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaKeyPressed

        if ((evt.getKeyCode() == KeyEvent.VK_F2) && (txtNumeroDeFactura.getText().length() > 0)) {

//            if (verificarQueFacturaExiste()) {
//                consultarLafactura();
//            }
            //new Thread(new HiloFLiquidarManifiestos(ini, this, "agregarLafactura")).start();
        }

        if ((evt.getKeyCode() == KeyEvent.VK_ENTER) && (txtNumeroDeFactura.getText().length() > 0)) {
            this.filaSeleccionada = -1;
            new Thread(new HiloFLiquidarManifiestos(ini, this, "descargarFactura")).start();
        }

    }//GEN-LAST:event_txtNumeroDeFacturaKeyPressed


    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (!estaOcupadoGrabando) {
            if (manifiesto != null) {
                if (esteManifiestoEsMio) {
                    // new Thread(new HiloLiberarManifiesto(conductorActual, true)).start();//-->Error v 
                    // conductorActual.liberarManifiesto(true);
                    manifiesto = null;
                    esteManifiestoEsMio = false;
                }
            }
        } else {
            descargarFacturas_2.msgSistemaOcupado();
            // JOptionPane.showInternalMessageDialog(this, "El sistema está ocupado grabado los datos,\n no se puede cerrar el formulario", "Error al cerrar", JOptionPane.ERROR_MESSAGE);

        }


    }//GEN-LAST:event_formInternalFrameClosing

    private void txtNumeroDeFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        if (conductorLiquidado) {

            new Thread(new HiloFLiquidarManifiestos(ini, this, "imprimir")).start();
        } else {
            // imprimirConManifiestosAbiertos();
            new Thread(new HiloFLiquidarManifiestos(ini, this, "imprimirConManifiestosAbiertos")).start();
        }


    }//GEN-LAST:event_btnImprimirActionPerformed


    private void txtNombreConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreConductorFocusGained
//        txtNumeroManifiesto.setSelectionStart(0);
//        txtNumeroManifiesto.setSelectionEnd(txtNumeroManifiesto.getText().length());
    }//GEN-LAST:event_txtNombreConductorFocusGained

    private void txtNombreConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorActionPerformed

    private void txtNombreConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreConductorKeyPressed

        /*Evento al oprimir la tecla F2 */
        if (evt.getKeyCode() == KeyEvent.VK_F2) {

            formulario = new FBuscarManifiestos(ini, this);
            formulario.setClosable(true);
            formulario.setVisible(true);
            formulario.setTitle("Formulario para Buscar manifiestos pendientes de Descargue");
            //form.setSize(PrincipalLogistica.escritorio.getSize());
            formulario.setLocation((this.ini.getDimension().width - formulario.getSize().width) / 2, (this.ini.getDimension().height - formulario.getSize().height) / 2);

            this.getDesktopPane().add(formulario);
            formulario.show();
            formulario.toFront();

        }

        /*Evento al oprimir la tecla Enter */
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //manifiestoActual = null;

            /* Se realiza la consulta para buscar el manifiesto que se va a descargar*/
//            new Thread(new HiloConsultarManifiesto(ini, this)).start();
        }

    }//GEN-LAST:event_txtNombreConductorKeyPressed


    private void cbxMovimientoFacturaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxMovimientoFacturaItemStateChanged
        tipoMovimientoFactura = 0;

        for (CMovimientosManifiestosfacturas obj : ini.getListaDeMovimientosManifiestosfacturas()) {
            if (cbxMovimientoFactura.getSelectedItem().toString().equals(obj.getNombreMovimientosManifiestosfacturas())) {
                tipoMovimientoFactura = obj.getIdMovimientosManifiestosfacturas();
            }
        }

        jTabbedPane1.setEnabled(true);
        switch (tipoMovimientoFactura) {
            case 0:
                cbxPrefijo.setEnabled(false);
                txtNumeroDeFactura.setEnabled(false);
                txtNumeroDeFactura.setEnabled(false);
                chkConDescuento.setEnabled(false);
                break;
            // NO SE HA SELECCIONADO NINGUN TIPO DE MOVIMIENTO
            case 1:
                chkConDescuento.setEnabled(false);
                cbxPrefijo.setEnabled(false);
                txtNumeroDeFactura.setEnabled(false);
                txtNumeroDeFactura.setEnabled(false);
                break;
            // ENTREGA TOTAL
            case 2:

                cbxPrefijo.setEnabled(true);
                txtNumeroDeFactura.setEnabled(true);
                txtNumeroDeFactura.setEditable(true);
                jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlEntregas), true);
                jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlEntregas));
                chkConDescuento.setEnabled(true);
                txtNumeroDeFactura.requestFocus();
                break;
            // RECHAZO TOTAL
            case 3: //rechazos totales
                //chkDescuento.setEnabled(true);
                btnGrabar100.setEnabled(false);
                jBtnGrabar100.setEnabled(false);

                jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlRechazosTotales), true);
                jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlRechazosTotales));
                cbxCausalDeRechazoFactura.setEnabled(true);
                btnDescargarRechazoTotal.setEnabled(true);
                cbxPrefijo.setEnabled(true);
                txtNumeroDeFactura.setEnabled(true);
                chkConDescuento.setEnabled(false);
                txtNumeroDeFactura.requestFocus();
                break;
            // ENTREGA CON NOVEDAD
            case 4:
                btnGrabar100.setEnabled(false);
                jBtnGrabar100.setEnabled(false);

                chkConDescuento.setEnabled(true);
                jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlRechazosParciales), true);
                jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlRechazosParciales));
                cbxPrefijo.setEnabled(true);
                txtNumeroDeFactura.setEditable(true);
                txtNumeroDeFactura.setEnabled(true);

                txtNumeroDeFactura.requestFocus();
                break;
            //Recogidas
            case 5:
                btnGrabar100.setEnabled(false);
                jBtnGrabar100.setEnabled(false);
                chkConDescuento.setEnabled(false);
                jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlRecogidas), true);
                jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlRecogidas));

                txtNumeroDeFactura.requestFocus();
                break;

            // VOLVER A ZONIFICAR
            case 6:
                btnGrabar100.setEnabled(false);
                jBtnGrabar100.setEnabled(false);

                chkConDescuento.setEnabled(false);
                cbxPrefijo.setEnabled(true);
                txtNumeroDeFactura.setEditable(true);
                txtNumeroDeFactura.setEnabled(true);
                jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlEntregas), true);
                jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlEntregas));
                chkConDescuento.setEnabled(false);
                txtNumeroDeFactura.requestFocus();
                break;

        }
        if (filaSeleccionada > 0) {
            txtNumeroDeFactura.setEditable(false);
            txtNumeroDeFactura.setEnabled(false);
        }

    }//GEN-LAST:event_cbxMovimientoFacturaItemStateChanged

    private void cbxMovimientoFacturaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxMovimientoFacturaFocusLost
        cbxPrefijo.setEnabled(true);
        txtNumeroDeFactura.setEnabled(true);
        txtNumeroDeFactura.setEditable(true);
        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();
// TODO add your handling code here:
    }//GEN-LAST:event_cbxMovimientoFacturaFocusLost


    private void cbxMovimientoFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxMovimientoFacturaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxPrefijo.setEnabled(true);
            txtNumeroDeFactura.setEnabled(true);
            txtNumeroDeFactura.setEnabled(true);
            txtNumeroDeFactura.requestFocus();        // TODO add your handling code here:
        }
    }//GEN-LAST:event_cbxMovimientoFacturaKeyPressed

    private void lblFacturasPendientesMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFacturasPendientesMouseMoved

        String toolTipHTML = ""
                + "<html><body><pre>";

        try {
            if ((manifiesto.getListaFacturasPendientesPorDescargar() != null) || manifiesto.getListaFacturasPendientesPorDescargar().size() > 0) { // Vst_FacturasPorManifiesto
                int i = 1;
                for (CFacturasPorManifiesto obj : manifiesto.getListaFacturasPendientesPorDescargar()) {
                    if (i < 5) {
                        toolTipHTML += obj.getNumeroFactura() + ",";
                    } else {
                        toolTipHTML += obj.getNumeroFactura() + ",\n";
                        i = 0;
                    }

                    i++;
                }
                toolTipHTML += "</pre></body></html>";
                lblFacturasPendientes.setToolTipText(toolTipHTML);
            }

        } catch (Exception ex) {
            Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_lblFacturasPendientesMouseMoved

    private void txtValorRecogidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorRecogidaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String cadena = txtValorRecogida.getText().trim().replace("$", "");
            cadena = cadena.replace(",", ".");

            valorRecogida = Double.parseDouble(cadena);

            txtValorRecogida.setText(nf.format(valorRecogida));
            txtValorDescontado.requestFocus();
        }
    }//GEN-LAST:event_txtValorRecogidaKeyPressed

    private void txtValorRecogidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorRecogidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorRecogidaActionPerformed

    private void txtValorRecogidaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorRecogidaFocusGained
        txtValorRecogida.setSelectionStart(0);
        txtValorRecogida.setSelectionEnd(txtValorRecogida.getText().length());
    }//GEN-LAST:event_txtValorRecogidaFocusGained

    private void btnAgregarRecogidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarRecogidaActionPerformed
        int x = descargarFacturas_2.msgGuardarRecogida();

        /* Si elige adicionar la regogida */
        if (x == JOptionPane.YES_OPTION) {
            try {
                //gestionarRecogida();
                new Thread(new HiloFLiquidarManifiestos(ini, this, "gestionarRecogida")).start();

            } catch (Exception ex) {
                Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_btnAgregarRecogidaActionPerformed

    private void btnDescargarRechazoTotal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarRechazoTotal1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDescargarRechazoTotal1ActionPerformed

    private void txtFacturaAfectadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaAfectadaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValorRecogida.requestFocus();
        }
    }//GEN-LAST:event_txtFacturaAfectadaKeyPressed

    private void txtFacturaAfectadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFacturaAfectadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFacturaAfectadaActionPerformed

    private void txtFacturaAfectadaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFacturaAfectadaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFacturaAfectadaFocusGained

    private void txtNumeroDeSoporteRecogidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeSoporteRecogidaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtFacturaAfectada.requestFocus();
        }
    }//GEN-LAST:event_txtNumeroDeSoporteRecogidaKeyPressed

    private void txtNumeroDeSoporteRecogidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeSoporteRecogidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeSoporteRecogidaActionPerformed

    private void txtNumeroDeSoporteRecogidaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeSoporteRecogidaFocusGained
        txtNumeroDeSoporteRecogida.setSelectionStart(0);
        txtNumeroDeSoporteRecogida.setSelectionEnd(txtNumeroDeSoporteRecogida.getText().length());
    }//GEN-LAST:event_txtNumeroDeSoporteRecogidaFocusGained

    private void jTableRecogidasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRecogidasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableRecogidasMouseClicked

    private void btnDescargarRechazoTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarRechazoTotalActionPerformed
        if (cbxCausalDeRechazoFactura.getSelectedItem().equals("NINGUNO")) {
            descargarFacturas_2.msgCausalDevolucion();
//JOptionPane.showInternalMessageDialog(this, "No ha seleccionado una causal de Devolucion", "Alerta ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            //double valorFactura = llenarTablaProductosPorFactura();
            lblValorFactura.setText(nf.format(facturaActual.getValorTotalFactura()));

            // SE VALIDA QUE LA FACTURA NO ESTE DESCARGADA
            // gestionarRechazoTotal();
            new Thread(new HiloFLiquidarManifiestos(ini, this, "gestionarRechazoTotal")).start();

        } catch (Exception ex) {
            Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("\n\n Error en btnDescargarRechazoTotalActionPerformed \n");

            descargarFacturas_2.msgFacturaNoAparece();
            //JOptionPane.showInternalMessageDialog(this, "La factura no aparece en el manifiesto", "Error", 0);

        }
        txtNumeroDeFactura.requestFocus();
    }//GEN-LAST:event_btnDescargarRechazoTotalActionPerformed

    private void cbxCausalDeRechazoFacturaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCausalDeRechazoFacturaItemStateChanged

    }//GEN-LAST:event_cbxCausalDeRechazoFacturaItemStateChanged

    private void jTableProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductosPorFacturaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableProductosPorFacturaMouseClicked

    private void rBtnContadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rBtnContadoActionPerformed

    }//GEN-LAST:event_rBtnContadoActionPerformed

    private void rBtnCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rBtnCreditoActionPerformed

        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();
    }//GEN-LAST:event_rBtnCreditoActionPerformed

    private void rBtnCreditoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rBtnCreditoMouseClicked
        if (rBtnCredito.isSelected()) {
            isFacturaCredito = true;
            chkConDescuento.setSelected(false);
            //facturaActual.setFormaDePago( 2);// formaDePago = 2; // crédito
        } else {
            isFacturaCredito = false;
            //facturaActual.setFormaDePago(1);//formaDePago = 1; // contado
        }
        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();

    }//GEN-LAST:event_rBtnCreditoMouseClicked

    private void rBtnContadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rBtnContadoMouseClicked
        if (rBtnContado.isSelected()) {
            isFacturaCredito = false;
            //facturaActual.setFormaDePago(1); //formaDePago = 1;  contado
        } else {
            isFacturaCredito = true;
            //facturaActual.setFormaDePago(2);//  formaDePago = 2;  crédito
        }
        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();


    }//GEN-LAST:event_rBtnContadoMouseClicked

    private void txtValorDescontadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorDescontadoFocusGained
        txtValorDescontado.setSelectionStart(0);
        txtValorDescontado.setSelectionEnd(txtValorDescontado.getText().length());
    }//GEN-LAST:event_txtValorDescontadoFocusGained

    private void txtValorDescontadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorDescontadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorDescontadoActionPerformed

    private void txtValorDescontadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorDescontadoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String cadena;

            cadena = txtValorDescontado.getText().trim().replace("$", "");
            cadena = cadena.replace(",", ".");

            valorDescontado = Double.parseDouble(cadena);

            txtValorDescontado.setText(nf.format(valorDescontado));
            btnAgregarRecogida.setEnabled(true); // TODO add your handling code here:
            btnAgregarRecogida.requestFocus();
        }
    }//GEN-LAST:event_txtValorDescontadoKeyPressed

    private void chkConDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkConDescuentoActionPerformed

        rBtnContado.setSelected(true);
        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();
    }//GEN-LAST:event_chkConDescuentoActionPerformed


    private void jTableListaDeConsignacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableListaDeConsignacionesMouseClicked
        // TODO add your handling code here:

        int row = jTableListaDeConsignaciones.getSelectedRow();
        //txtNumeroManifiesto.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        //jLabel2.setText("Id. Documento :  " + String.valueOf(jTable1.getValueAt(row, 0)));
        //jLabel2.setVisible(true);
        txtNumeroSoporteConsignacion.setText(String.valueOf(jTableListaDeConsignaciones.getValueAt(row, 2)));
        if ((Boolean) jTableListaDeConsignaciones.getModel().getValueAt(row, 3)) {
            //chkActivo.setText("Documento Activo");
            // chkActivo.setSelected(true);
        } else {
            //chkActivo.setText("Documento no Activo");
            //chkActivo.setSelected(false);
        }
        actualizar = true;
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        btnNuevo.setText("Actualizar");
    }//GEN-LAST:event_jTableListaDeConsignacionesMouseClicked

    private void txtValorConsignacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorConsignacionFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorConsignacionFocusGained

    private void txtValorConsignacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorConsignacionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorConsignacionKeyPressed

    private void txtMedioDePagoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMedioDePagoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMedioDePagoFocusGained

    private void txtMedioDePagoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMedioDePagoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMedioDePagoKeyPressed

    private void txtNumeroSoporteConsignacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroSoporteConsignacionFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroSoporteConsignacionFocusGained

    private void txtNumeroSoporteConsignacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroSoporteConsignacionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroSoporteConsignacionKeyPressed


    private void cbxEntidadesBancariasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxEntidadesBancariasItemStateChanged

        if (cbxEntidadesBancarias.getItemCount() > 0 && ini.getListaDeCuentasBancarias() != null) {

            cbxNumeroDeCuenta.removeAllItems();
            for (CCuentasBancarias obj : ini.getListaDeCuentasBancarias()) {
                if (obj.getNombreDeBanco().equals(cbxEntidadesBancarias.getSelectedItem().toString())) {
                    cbxNumeroDeCuenta.addItem(obj.getNumeroDeCuenta());
                }

            }

        }

    }//GEN-LAST:event_cbxEntidadesBancariasItemStateChanged

    private void lblValorConsignadoMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValorConsignadoMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValorConsignadoMouseMoved

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        this.filaSeleccionada = -1;        // nuevo();
        new Thread(new HiloFLiquidarManifiestos(ini, this, "nuevo")).start();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
        this.filaSeleccionada = -1;        // cerrarManifiesto();
        new Thread(new HiloFLiquidarManifiestos(ini, this, "cerrarManifiesto")).start();

    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jBtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnImprimirActionPerformed
        // imprimir();
        new Thread(new HiloFLiquidarManifiestos(ini, this, "imprimir")).start();

    }//GEN-LAST:event_jBtnImprimirActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        this.filaSeleccionada = -1;        // cancelar();
        new Thread(new HiloFLiquidarManifiestos(ini, this, "cancelar")).start();

    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnMinutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnMinutaActionPerformed
//
//        if (conductorActual != null) {
//            try {
//                MinutasDeDistribucion sacarMinuta = new MinutasDeDistribucion(ini);
//
//                switch (conductorActual.getEstadoManifiesto()) {
//                    case 1:
//
//                    case 2:
//
//                    case 3:
//                        sacarMinuta.minutaPoManifiesto(conductorActual.getNumeroManifiesto());
//                        break;
//                    case 4:
//                        ReporteMinutaDeDescargueDeRuta demo = new ReporteMinutaDeDescargueDeRuta(ini, conductorActual);
//                        break;
//
//                }
//            } catch (Exception ex) {
//                Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            descargarFacturas_2.msgManifiestoNoSeleccionado();
//            //JOptionPane.showMessageDialog(this, "No hay manifiesto de ruta selecccionado ", "Error", 0);
//        }
    }//GEN-LAST:event_jBtnMinutaActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        this.filaSeleccionada = -1;        // salir(); 
        new Thread(new HiloFLiquidarManifiestos(ini, this, "salir")).start();

// TODO add your handling code here:
    }//GEN-LAST:event_jBtnExitActionPerformed

    private void btnGrabar100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabar100ActionPerformed

        // descargarFacturas_2.grabarcien();
        // llenarjTableFacturasDescargadasPorVehiculo
        new Thread(new HiloIntegradorTNSLaHielera(this.ini)).start();

        new Thread(new HiloFLiquidarManifiestos(ini, this, "grabarCien100")).start();

//        int deseaGrabar = JOptionPane.showConfirmDialog(this, "Desea guardar todos los registros de una sola vez ?", "Guardar registro", JOptionPane.YES_NO_OPTION);
//
//        /* Se valida el deseo de cerrarManifiesto los datos en la BBDD  */
//        if (deseaGrabar == JOptionPane.YES_OPTION) {
//
//            new Thread(new HiloGuardarManifiestoDescargado100(ini, this)).start();
//        }
    }//GEN-LAST:event_btnGrabar100ActionPerformed

    private void jBtnGrabar100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabar100ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnGrabar100ActionPerformed

    private void cbxMovimientoFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxMovimientoFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxMovimientoFacturaActionPerformed

    private void chkCerrarManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCerrarManifiestoActionPerformed
        if (chkCerrarManifiesto.isSelected()) {
            if ((manifiesto.getListaFacturasPendientesPorDescargar().size() == 0 && manifiesto.getListaFacturasPorManifiesto().size() > 0)) {
                btnGrabar.setEnabled(true);
                jBtnGrabar.setEnabled(true);
                btnGrabar100.setEnabled(false);
                jBtnGrabar100.setEnabled(false);
            }
        } else {

            btnGrabar.setEnabled(false);
            jBtnGrabar.setEnabled(false);
            btnGrabar100.setEnabled(true);
            jBtnGrabar100.setEnabled(true);
        }
    }//GEN-LAST:event_chkCerrarManifiestoActionPerformed

    private void jBtnConsignacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnConsignacionesActionPerformed

        // getionarSoporteConsignacion();
        new Thread(new HiloFLiquidarManifiestos(ini, this, "getionarSoporteConsignacion")).start();

    }//GEN-LAST:event_jBtnConsignacionesActionPerformed

    private void txtCedulaConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaConductorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorFocusGained

    private void txtCedulaConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorActionPerformed

    private void txtCedulaConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaConductorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!txtCedulaConductor.getText().trim().equals(manifiesto.getConductor())) {
                JOptionPane.showInternalMessageDialog(this, "El documento de identificacion no corresponde ,\n al conductor actual",
                        "Documento no valido", JOptionPane.ERROR_MESSAGE);

            } else {
                new Thread(new HiloFLiquidarManifiestos(ini, this, "grabarCien100")).start();
            }

        }
    }//GEN-LAST:event_txtCedulaConductorKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarRecogida;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnDescargarRechazoTotal;
    private javax.swing.JButton btnDescargarRechazoTotal1;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnGrabar100;
    public javax.swing.JButton btnImprimir;
    public javax.swing.JButton btnNuevo;
    public javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JComboBox cbxCausalDeRechazoFactura;
    public javax.swing.JComboBox<String> cbxEntidadesBancarias;
    public javax.swing.JComboBox cbxMovimientoFactura;
    public javax.swing.JComboBox<String> cbxNumeroDeCuenta;
    public javax.swing.JComboBox<String> cbxPrefijo;
    public javax.swing.JCheckBox chkCerrarManifiesto;
    public javax.swing.JCheckBox chkConDescuento;
    public com.toedter.calendar.JDateChooser dateOperacion;
    private javax.swing.JToggleButton jBtnCancel;
    public javax.swing.JButton jBtnConsignaciones;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnGrabar100;
    public javax.swing.JToggleButton jBtnImprimir;
    public javax.swing.JToggleButton jBtnMinuta;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    public javax.swing.JList jListFacturasNoManifestadas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    public javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableListaDeConsignaciones;
    public javax.swing.JTable jTableProductosPorFactura;
    public javax.swing.JTable jTableRecogidas;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblFacturasPendientes;
    public javax.swing.JLabel lblIdentificadorManifiesto;
    public javax.swing.JLabel lblValorConsignado;
    public org.edisoncor.gui.label.LabelCustom lblValorFactura;
    public org.edisoncor.gui.label.LabelCustom lblValorRecaudoManifiesto;
    private javax.swing.JPanel pnlEntregas;
    public javax.swing.JPanel pnlRechazosParciales;
    public javax.swing.JPanel pnlRechazosTotales;
    public javax.swing.JPanel pnlRecogidas;
    public javax.swing.JRadioButton rBtnContado;
    public javax.swing.JRadioButton rBtnCredito;
    public javax.swing.JTable tblFacturasDescargadasPorVehiculo;
    public javax.swing.JTextField txtCedulaConductor;
    public javax.swing.JTextField txtFacturaAfectada;
    public javax.swing.JTextField txtMedioDePago;
    public javax.swing.JTextField txtNombreConductor;
    public javax.swing.JTextField txtNumeroDeFactura;
    public javax.swing.JTextField txtNumeroDeSoporteRecogida;
    public javax.swing.JTextField txtNumeroSoporteConsignacion;
    public javax.swing.JTextField txtValorConsignacion;
    public javax.swing.JTextField txtValorDescontado;
    public javax.swing.JTextField txtValorRecogida;
    // End of variables declaration//GEN-END:variables

    private void llenarTablaProductosPorFactura() {
        DefaultTableModel modelo1 = (DefaultTableModel) jTableProductosPorFactura.getModel();

        // se nexan los productos a la tabla de productos por factura
        for (CProductosPorFactura obj : facturaActual.getListaProductosPorFactura()) {

            int fila = jTableProductosPorFactura.getRowCount();

            modelo1.addRow(new Object[jTableProductosPorFactura.getRowCount()]);
            jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorProductoXCantidad()), fila, 5);

        }

    }

}
