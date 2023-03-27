/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService;

import aplicacionlogistica.distribucion.Threads.HiloConsultarCliente;
import aplicacionlogistica.distribucion.Threads.HiloListadoClientes;
import aplicacionlogistica.distribucion.Threads.HiloListadoConsultadeFacturaBitacora;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.objetos.CSvcIncidencias;
import aplicacionlogistica.costumerService.objetos.CcontrolClass;
import aplicacionlogistica.costumerService.objetos.IconCellRenderer;
import aplicacionlogistica.costumerService.objetos.RowsRenderer;
import aplicacionlogistica.distribucion.PrincipalSuperUsuario;
import aplicacionlogistica.distribucion.PrincipalAdministradorDelSistema;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.objetos.CBitacoraFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFacturaDescargados;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FIncidenciasSvC extends javax.swing.JInternalFrame {
    //  public class FIncidenciasSvC extends javax.swing.JDialog {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    ArrayList<CSvcIncidencias> listaDeIncidencias;
    CcontrolClass controlador;
    PrincipalUsuarioCostumerService formPPalCostumerService;
    PrincipalSuperUsuario formPPalAdmonSistema;
    Inicio ini = null;

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    DefaultTableModel modelo1, modelo2;

    public boolean cargado = false;
    public boolean fGestion = false;
    // boolean tieneManifiestosAnteriores = false;
    // boolean tieneMnifiestosAsigndos = false;
    boolean nuevo = false;
    boolean actualizar = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean completado = false;

    int filaTabla2;
    int indiceLista = 0;
    int columna = 0;
    String mensaje = null;
    public CFacturas factura;
    JInternalFrame form;
    public CSvcIncidencias incidencia;

   
    public ArrayList<Vst_ProductosPorFactura> listaProductosPorFactura;
    public ArrayList<Vst_FacturasPorManifiesto> listaDeMovimientosEnDistribucion;
    public ArrayList<CBitacoraFacturas> listaDeMovimientosBitacora;
    public ArrayList<Vst_ProductosPorFacturaDescargados> listaDeProductosRechazados;
    public int filaSeleccionada;
    public CClientes cliente;

    ImageIcon iconRuta = new ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"));
    ImageIcon iconPhone = new ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Phone.png"));
    ImageIcon iconMail = new ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/E-mail.png"));
    ImageIcon iconClip = new ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Clipboard.png"));

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param formPPal
     * @throws java.lang.Exception
     */
    public FIncidenciasSvC(PrincipalUsuarioCostumerService formPPal) throws Exception {
        try {
            initComponents();
             lblCirculoDeProgreso.setVisible(false);
             lblCirculoDeProgreso1.setVisible(false);
             lblCirculoDeProgreso2.setVisible(false);
             lblCargandoUnMomento.setVisible(false);
                     
            rbtIncidenciasTodas.setSelected(true);
            lblCirculoDeProgreso.setVisible(false);
            this.ini = formPPal.ini;
            this.formPPalCostumerService = formPPal;
            
            /*Asigna las causales de derchazos a una lista*/
            formPPal.ini.setListaDeCausalesDeRechazo();
            txtNumeroFactura.setEnabled(true);
            txtNumeroFactura.setEditable(true);
            txtNumeroFactura.requestFocus();

            manager.addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    //como dije, solo las notificaciones del tipo "typed" son las que actualizan los componentes
                    if (e.getID() == KeyEvent.KEY_TYPED) {
                        // if(e.getSource() instanceof JComponent
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

                        } else {
                            if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
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
                    }

                    //y listo, regresamos siempre falso para que las demas notificaciones continuen, si regresamos true
                    // significa que el dispatcher consumio el evento
                    return false;
                }
            });
        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;
        Date fecha = new Date();
        controlador = new CcontrolClass(ini, fecha);

        /*se valida que hayan registros en la tabla incidencias por fecha y si no las hay 
        se procede a insertar los registros pedientes de solucion */
        if (!controlador.validarIncidenciasPendientes()) {
            /*se agregan los registros a la tabal inciedencias por fecha*/
            controlador.asignarIncidenciasPendientes();
        }
        /*se llaman las incidencias que estan pendientes de solucion */
        listaDeIncidencias = controlador.getListaDeIncidencias(fecha);

        /*Se llena la tabla con la lista de incidencias*/
        llenarTablaIncidencias();

    }

    
     /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param formPPal
     * @throws java.lang.Exception
     */
    public FIncidenciasSvC(PrincipalSuperUsuario formPPal) throws Exception {
        try {
            initComponents();
             lblCirculoDeProgreso.setVisible(false);
             lblCirculoDeProgreso1.setVisible(false);
             lblCirculoDeProgreso2.setVisible(false);
             lblCargandoUnMomento.setVisible(false);
                     
            rbtIncidenciasTodas.setSelected(true);
            lblCirculoDeProgreso.setVisible(false);
            this.ini = formPPal.ini;
            this.formPPalAdmonSistema = formPPal;
            
            /*Asigna las causales de derchazos a una lista*/
            formPPal.ini.setListaDeCausalesDeRechazo();
            txtNumeroFactura.setEnabled(true);
            txtNumeroFactura.setEditable(true);
            txtNumeroFactura.requestFocus();

            manager.addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    //como dije, solo las notificaciones del tipo "typed" son las que actualizan los componentes
                    if (e.getID() == KeyEvent.KEY_TYPED) {
                        // if(e.getSource() instanceof JComponent
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

                        } else {
                            if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
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
                    }

                    //y listo, regresamos siempre falso para que las demas notificaciones continuen, si regresamos true
                    // significa que el dispatcher consumio el evento
                    return false;
                }
            });
        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;
        Date fecha = new Date();
        controlador = new CcontrolClass(ini, fecha);

        /*se valida que hayan registros en la tabla incidencias por fecha y si no las hay 
        se procede a insertar los registros pedientes de solucion */
        if (!controlador.validarIncidenciasPendientes()) {
            /*se agregan los registros a la tabal inciedencias por fecha*/
            controlador.asignarIncidenciasPendientes();
        }
        /*se llaman las incidencias que estan pendientes de solucion */
        listaDeIncidencias = controlador.getListaDeIncidencias(fecha);

        /*Se llena la tabla con la lista de incidencias*/
        llenarTablaIncidencias();

    }

    
    
    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param form fBuscarManifiestos desde donde se carga la interface gráfica
     * @throws java.lang.Exception
     */
    public FIncidenciasSvC(JInternalFrame form) throws Exception {
        this.form = form;

        try {
            initComponents();

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    public FIncidenciasSvC(JInternalFrame form, String numeroFactura) throws Exception {
        try {
            initComponents();
            this.ini = ini;
            txtNumeroFactura.setEnabled(true);
            txtNumeroFactura.setEditable(true);
            txtNumeroFactura.requestFocus();

            manager.addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    //como dije, solo las notificaciones del tipo "typed" son las que actualizan los componentes
                    if (e.getID() == KeyEvent.KEY_TYPED) {
                        // if(e.getSource() instanceof JComponent
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

                        } else {
                            if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
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
                    }

                    //y listo, regresamos siempre falso para que las demas notificaciones continuen, si regresamos true
                    // significa que el dispatcher consumio el evento
                    return false;
                }
            });

            this.factura = new CFacturas(ini, numeroFactura);
            if (this.factura != null) {
                /*Hilo que recupera los movimientos de la facturaActual */
                new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this.factura)).start();
                llenarDatosDeLaVista(this.factura);
            } else {
                JOptionPane.showMessageDialog(null, "Número de Factura no encontrado en el sistema ", "Error", JOptionPane.WARNING_MESSAGE);

            }

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    private void llenarDatosDeLaVista(CFacturas factura1) throws Exception {

//        limpiarTodo();
//        this.facturaActual = factura1;
//       
//        listaProductosPorFactura = new ArrayList();
//        listaDeMovimientosEnDistribucion = new ArrayList();
//        listaDeMovimientosBitacora = new ArrayList();
//
//        this.facturaActual.setVistaFactura(true);
//        this.facturaActual.setVstProductosPorFactura(true);
//        this.facturaActual.setListaDeMovimientosfactura();
//        //this.facturaActual.getVstlistaDeProductosRechazados(facturaActual.getNumeroFactura(), manifiesto);
//        vistafac = this.facturaActual.getVistaFactura();
//        lblValorRecaudado.setText(nf.format(facturaActual.getValorRecaudado()));
//
//        listaProductosPorFactura = this.facturaActual.getVstProductosPorFactura();
//        listaDeMovimientosEnDistribucion = this.facturaActual.getListaDeMovimientosfactura();
//        listaDeProductosRechazados = new ArrayList<>();
//
//        int i = 0;
//        for (Vst_FacturasPorManifiesto obj : listaDeMovimientosEnDistribucion) {
//
//            if (i == (listaDeMovimientosEnDistribucion.size() - 1)) {
//                listaDeProductosRechazados = facturaActual.getVstlistaDeProductosRechazados(facturaActual.getNumeroFactura(), obj.getNumeroManifiesto());
//            }
//            i++;
//        }
//
//        listaDeMovimientosBitacora = this.facturaActual.getListaDeMovimientosBitacora(facturaActual.getNumeroFactura());
//
//        /*Hilo que recupera los movimientos de la facturaActual */
//        // new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this.facturaActual)).start();
//        txtNumeroFactura.setText(vistafac.getNumeroFactura());
//        txtDireccionDelCliente.setText(vistafac.getDireccionDeCliente());
//        txtBarrioCliente.setText(vistafac.getBarrio());
//        txtFechaVenta.setText("" + vistafac.getFechaDeVenta());
//
//        /* nombre del cliente y el código entre parétesis*/
//        txtNombreDelCliente.setText(vistafac.getNombreDeCliente() + " (" + vistafac.getCliente() + ")");
//        txtNombreDelVendedor.setText(vistafac.getVendedor());
//        txtTelefonoDelCliente.setText(vistafac.getTelefonoCliente());
//        llenarTablaProductosPorFactura();
//
//        System.out.println("Aca verifica que hayan movimientos en la facturaActual " + facturaActual.getNumeroFactura());
//
//        llenarTablabitacora();
//        llenarTablaProductosRechazados();
//
//        completado = false;
//        llenarTablaDistribucion();
//
//        lblCirculoDeProgreso.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuIncidencias = new javax.swing.JPopupMenu();
        jMenu1 = new javax.swing.JMenu();
        mostrarIncidencia = new javax.swing.JMenuItem();
        cerrarIncidencia = new javax.swing.JMenuItem();
        crearIncidencia = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaIncidencias = new javax.swing.JTable();
        rbtIncidenciasAbiertas = new javax.swing.JRadioButton();
        rbtIncidenciasCerradas = new javax.swing.JRadioButton();
        rbtIncidenciasPorResponsable = new javax.swing.JRadioButton();
        rbtIncidenciasTodas = new javax.swing.JRadioButton();
        jToolBar3 = new javax.swing.JToolBar();
        jToggleButton6 = new javax.swing.JToggleButton();
        jButton8 = new javax.swing.JButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jToggleButton8 = new javax.swing.JToggleButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtNumeroFactura = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtNombreDelCliente = new javax.swing.JTextField();
        txtBarrioCliente = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtNombreDelVendedor = new javax.swing.JTextField();
        txtTelefonoDelCliente = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        txtFechaVenta = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        lblValorRecaudado = new javax.swing.JLabel();
        lblPrecioFactura2 = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtDireccionDelCliente = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlEntregas = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProductosPorFactura = new javax.swing.JTable();
        lblValorTotalFactura = new javax.swing.JLabel();
        pnlMovimientos = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblBitacora = new javax.swing.JTable();
        btnDescargarRechazoTotal2 = new javax.swing.JButton();
        pnlDistribucion = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblMovimientos = new javax.swing.JTable();
        pnlRechazosParciales = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblProductosPorFacturaRechazados = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblDescuentos = new javax.swing.JTable();
        pnlRecogidas = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblRecogidas = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        txtNumeroDeSoporte = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtFacturaAfectada = new javax.swing.JTextField();
        btnDescargarRechazoTotal1 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        txtValorRecogida = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtValorDescontado = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        txtCodigoCliente = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtNombreDelCliente1 = new javax.swing.JTextField();
        txtBarrioCliente1 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        txtTelefonoDelCliente1 = new javax.swing.JTextField();
        lblCirculoDeProgreso1 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtDireccionDelCliente1 = new javax.swing.JTextField();
        lblCargandoUnMomento = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        txtParteDelNombreDelCliente = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        lblCirculoDeProgreso2 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        pnlEntregas1 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblFacturasPorCliente = new javax.swing.JTable();
        jToolBar2 = new javax.swing.JToolBar();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton5 = new javax.swing.JToggleButton();
        jButton6 = new javax.swing.JButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jButton7 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jMenu1.setText("Seleccionar Incidencia");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        mostrarIncidencia.setText("Gestionar Incidencia");
        mostrarIncidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarIncidenciaActionPerformed(evt);
            }
        });
        jMenu1.add(mostrarIncidencia);

        cerrarIncidencia.setText("Cerrar Incidencia");
        cerrarIncidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarIncidenciaActionPerformed(evt);
            }
        });
        jMenu1.add(cerrarIncidencia);

        crearIncidencia.setText("Crear Incidencia");
        crearIncidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crearIncidenciaActionPerformed(evt);
            }
        });
        jMenu1.add(crearIncidencia);

        menuIncidencias.add(jMenu1);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Formulario Reporte de Incidencias");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N
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

        tablaIncidencias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "item", "medio", "gestiones", "Factura", "Estado", "fecha", "Incidencia #", "Movimiento", "Causal", "Responsable", "Valor Factura"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaIncidencias.setComponentPopupMenu(menuIncidencias);
        jScrollPane1.setViewportView(tablaIncidencias);
        if (tablaIncidencias.getColumnModel().getColumnCount() > 0) {
            tablaIncidencias.getColumnModel().getColumn(0).setPreferredWidth(10);
            tablaIncidencias.getColumnModel().getColumn(1).setPreferredWidth(10);
            tablaIncidencias.getColumnModel().getColumn(2).setPreferredWidth(10);
            tablaIncidencias.getColumnModel().getColumn(3).setPreferredWidth(60);
            tablaIncidencias.getColumnModel().getColumn(4).setPreferredWidth(80);
            tablaIncidencias.getColumnModel().getColumn(5).setPreferredWidth(60);
            tablaIncidencias.getColumnModel().getColumn(6).setPreferredWidth(60);
            tablaIncidencias.getColumnModel().getColumn(7).setPreferredWidth(150);
            tablaIncidencias.getColumnModel().getColumn(8).setPreferredWidth(150);
            tablaIncidencias.getColumnModel().getColumn(9).setPreferredWidth(200);
            tablaIncidencias.getColumnModel().getColumn(10).setPreferredWidth(80);
        }

        buttonGroup1.add(rbtIncidenciasAbiertas);
        rbtIncidenciasAbiertas.setSelected(true);
        rbtIncidenciasAbiertas.setText("Incidencias abiertas");
        rbtIncidenciasAbiertas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtIncidenciasAbiertasActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtIncidenciasCerradas);
        rbtIncidenciasCerradas.setText("Incidencias Cerradas");
        rbtIncidenciasCerradas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtIncidenciasCerradasActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtIncidenciasPorResponsable);
        rbtIncidenciasPorResponsable.setText("Por Responsable");

        buttonGroup1.add(rbtIncidenciasTodas);
        rbtIncidenciasTodas.setText("Todas las incidencias");
        rbtIncidenciasTodas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtIncidenciasTodasActionPerformed(evt);
            }
        });

        jToolBar3.setRollover(true);

        jToggleButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jToggleButton6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton6.setFocusable(false);
        jToggleButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });
        jToolBar3.add(jToggleButton6);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jButton8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton8.setEnabled(false);
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton8);

        jToggleButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jToggleButton7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton7.setEnabled(false);
        jToggleButton7.setFocusable(false);
        jToggleButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jToggleButton7);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jButton9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton9);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Table.png"))); // NOI18N
        jButton10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton10);

        jToggleButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jToggleButton8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton8.setFocusable(false);
        jToggleButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jToggleButton8);

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N
        jButton11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton11);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jButton12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton12);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbtIncidenciasAbiertas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtIncidenciasCerradas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtIncidenciasPorResponsable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtIncidenciasTodas)
                .addGap(697, 832, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtIncidenciasAbiertas)
                    .addComponent(rbtIncidenciasCerradas)
                    .addComponent(rbtIncidenciasPorResponsable)
                    .addComponent(rbtIncidenciasTodas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Incidencias", jPanel4);

        jPanel5.setAutoscrolls(true);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Formulario de consulta de Manifiesto"));

        txtNumeroFactura.setEditable(false);
        txtNumeroFactura.setName(""); // NOI18N
        txtNumeroFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroFacturaFocusGained(evt);
            }
        });
        txtNumeroFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroFacturaActionPerformed(evt);
            }
        });
        txtNumeroFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroFacturaKeyPressed(evt);
            }
        });

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Cliente");

        txtNombreDelCliente.setEditable(false);
        txtNombreDelCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDelClienteFocusGained(evt);
            }
        });
        txtNombreDelCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDelClienteActionPerformed(evt);
            }
        });
        txtNombreDelCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDelClienteKeyPressed(evt);
            }
        });

        txtBarrioCliente.setEditable(false);
        txtBarrioCliente.setName("numerico"); // NOI18N
        txtBarrioCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarrioClienteFocusGained(evt);
            }
        });
        txtBarrioCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarrioClienteActionPerformed(evt);
            }
        });
        txtBarrioCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarrioClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBarrioClienteKeyReleased(evt);
            }
        });

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Factura #");

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Barrio");

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Teléfono");

        txtNombreDelVendedor.setEditable(false);
        txtNombreDelVendedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDelVendedorFocusGained(evt);
            }
        });
        txtNombreDelVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDelVendedorActionPerformed(evt);
            }
        });
        txtNombreDelVendedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDelVendedorKeyPressed(evt);
            }
        });

        txtTelefonoDelCliente.setEditable(false);
        txtTelefonoDelCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoDelClienteFocusGained(evt);
            }
        });
        txtTelefonoDelCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoDelClienteActionPerformed(evt);
            }
        });
        txtTelefonoDelCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoDelClienteKeyPressed(evt);
            }
        });

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("Vendedor");

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Fecha Venta");

        txtFechaVenta.setEditable(false);
        txtFechaVenta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFechaVentaFocusGained(evt);
            }
        });
        txtFechaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaVentaActionPerformed(evt);
            }
        });
        txtFechaVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFechaVentaKeyPressed(evt);
            }
        });

        lblValorRecaudado.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblValorRecaudado.setText("$.");

        lblPrecioFactura2.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        lblPrecioFactura2.setText("Total Recaudado");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblValorRecaudado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(lblPrecioFactura2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(lblValorRecaudado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPrecioFactura2)
                .addGap(15, 15, 15))
        );

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Dirección");

        txtDireccionDelCliente.setEditable(false);
        txtDireccionDelCliente.setName("numerico"); // NOI18N
        txtDireccionDelCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDireccionDelClienteFocusGained(evt);
            }
        });
        txtDireccionDelCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionDelClienteActionPerformed(evt);
            }
        });
        txtDireccionDelCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionDelClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionDelClienteKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDireccionDelCliente)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCirculoDeProgreso))
                            .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBarrioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreDelVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(40, 40, 40)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCirculoDeProgreso)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDireccionDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBarrioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefonoDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDelVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );

        pnlEntregas.setAutoscrolls(true);
        pnlEntregas.setDoubleBuffered(false);
        pnlEntregas.setEnabled(false);

        tblProductosPorFactura.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProductosPorFactura.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProductosPorFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosPorFacturaMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblProductosPorFactura);
        if (tblProductosPorFactura.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFactura.getColumnModel().getColumn(0).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblProductosPorFactura.getColumnModel().getColumn(1).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(2).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblProductosPorFactura.getColumnModel().getColumn(3).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(4).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(125);
            tblProductosPorFactura.getColumnModel().getColumn(5).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        lblValorTotalFactura.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblValorTotalFactura.setText("$.");

        javax.swing.GroupLayout pnlEntregasLayout = new javax.swing.GroupLayout(pnlEntregas);
        pnlEntregas.setLayout(pnlEntregasLayout);
        pnlEntregasLayout.setHorizontalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEntregasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblValorTotalFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 919, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlEntregasLayout.setVerticalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblValorTotalFactura)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Productos", pnlEntregas);

        tblBitacora.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "item", "fecha", "documento", "causal-obs.", "valor Descto."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tblBitacora.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblBitacora.setColumnSelectionAllowed(true);
        tblBitacora.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBitacoraMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblBitacora);
        tblBitacora.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblBitacora.getColumnModel().getColumnCount() > 0) {
            tblBitacora.getColumnModel().getColumn(0).setMinWidth(50);
            tblBitacora.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblBitacora.getColumnModel().getColumn(0).setMaxWidth(50);
            tblBitacora.getColumnModel().getColumn(1).setMinWidth(100);
            tblBitacora.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblBitacora.getColumnModel().getColumn(1).setMaxWidth(100);
            tblBitacora.getColumnModel().getColumn(2).setMinWidth(100);
            tblBitacora.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblBitacora.getColumnModel().getColumn(2).setMaxWidth(100);
            tblBitacora.getColumnModel().getColumn(3).setMinWidth(450);
            tblBitacora.getColumnModel().getColumn(3).setPreferredWidth(450);
            tblBitacora.getColumnModel().getColumn(3).setMaxWidth(450);
            tblBitacora.getColumnModel().getColumn(4).setMinWidth(50);
            tblBitacora.getColumnModel().getColumn(4).setPreferredWidth(50);
            tblBitacora.getColumnModel().getColumn(4).setMaxWidth(50);
        }

        btnDescargarRechazoTotal2.setText("Descargar");
        btnDescargarRechazoTotal2.setEnabled(false);
        btnDescargarRechazoTotal2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarRechazoTotal2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMovimientosLayout = new javax.swing.GroupLayout(pnlMovimientos);
        pnlMovimientos.setLayout(pnlMovimientosLayout);
        pnlMovimientosLayout.setHorizontalGroup(
            pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMovimientosLayout.createSequentialGroup()
                .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMovimientosLayout.createSequentialGroup()
                        .addGap(0, 819, Short.MAX_VALUE)
                        .addComponent(btnDescargarRechazoTotal2))
                    .addGroup(pnlMovimientosLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlMovimientosLayout.setVerticalGroup(
            pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMovimientosLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDescargarRechazoTotal2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Movimientos", pnlMovimientos);

        pnlDistribucion.setAutoscrolls(true);
        pnlDistribucion.setDoubleBuffered(false);
        pnlDistribucion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblMovimientos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Mfto", "Fecha Dist", "Vehículo", "Conductor", "Ruta"
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
        tblMovimientos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblMovimientos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMovimientosMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblMovimientos);
        if (tblMovimientos.getColumnModel().getColumnCount() > 0) {
            tblMovimientos.getColumnModel().getColumn(0).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblMovimientos.getColumnModel().getColumn(1).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblMovimientos.getColumnModel().getColumn(2).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblMovimientos.getColumnModel().getColumn(3).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblMovimientos.getColumnModel().getColumn(4).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(4).setPreferredWidth(300);
            tblMovimientos.getColumnModel().getColumn(5).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        pnlDistribucion.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 240));

        jTabbedPane1.addTab("Distribución", pnlDistribucion);

        pnlRechazosParciales.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pnlRechazosParciales.setEnabled(false);

        tblProductosPorFacturaRechazados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Ref", "Producto", "cantidad", "Causal de Rechazo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tblProductosPorFacturaRechazados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProductosPorFacturaRechazados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosPorFacturaRechazadosMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblProductosPorFacturaRechazados);
        if (tblProductosPorFacturaRechazados.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(0).setResizable(false);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(1).setResizable(false);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(2).setResizable(false);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(3).setResizable(false);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(4).setResizable(false);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(4).setPreferredWidth(125);
        }

        javax.swing.GroupLayout pnlRechazosParcialesLayout = new javax.swing.GroupLayout(pnlRechazosParciales);
        pnlRechazosParciales.setLayout(pnlRechazosParcialesLayout);
        pnlRechazosParcialesLayout.setHorizontalGroup(
            pnlRechazosParcialesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
        );
        pnlRechazosParcialesLayout.setVerticalGroup(
            pnlRechazosParcialesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRechazosParcialesLayout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 75, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Productos Devueltos", pnlRechazosParciales);

        tblDescuentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Ref", "Producto", "cantidad", "Causal de Rechazo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tblDescuentos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblDescuentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDescuentosMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tblDescuentos);
        if (tblDescuentos.getColumnModel().getColumnCount() > 0) {
            tblDescuentos.getColumnModel().getColumn(0).setResizable(false);
            tblDescuentos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblDescuentos.getColumnModel().getColumn(1).setResizable(false);
            tblDescuentos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblDescuentos.getColumnModel().getColumn(2).setResizable(false);
            tblDescuentos.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblDescuentos.getColumnModel().getColumn(3).setResizable(false);
            tblDescuentos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblDescuentos.getColumnModel().getColumn(4).setResizable(false);
            tblDescuentos.getColumnModel().getColumn(4).setPreferredWidth(125);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Descuentos", jPanel3);

        tblRecogidas.setModel(new javax.swing.table.DefaultTableModel(
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
        tblRecogidas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblRecogidas.setColumnSelectionAllowed(true);
        tblRecogidas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRecogidasMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblRecogidas);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Numero Soporte");

        txtNumeroDeSoporte.setEditable(false);
        txtNumeroDeSoporte.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeSoporteFocusGained(evt);
            }
        });
        txtNumeroDeSoporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroDeSoporteActionPerformed(evt);
            }
        });
        txtNumeroDeSoporte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeSoporteKeyPressed(evt);
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
                                .addContainerGap()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                            .addComponent(txtNumeroDeSoporte, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnlRecogidasLayout.createSequentialGroup()
                                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtValorRecogida, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(pnlRecogidasLayout.createSequentialGroup()
                                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtValorDescontado, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 218, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlRecogidasLayout.setVerticalGroup(
            pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRecogidasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtNumeroDeSoporte, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtFacturaAfectada, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtValorRecogida, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValorDescontado, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(btnDescargarRechazoTotal1)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Recogidas", pnlRecogidas);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(447, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Consulta de Facturas", jPanel5);

        jPanel6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel6FocusGained(evt);
            }
        });
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel6MouseClicked(evt);
            }
        });

        jPanel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel7FocusGained(evt);
            }
        });

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta por Código del cliente"));

        txtCodigoCliente.setEditable(false);
        txtCodigoCliente.setName("numerico"); // NOI18N
        txtCodigoCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCodigoClienteFocusGained(evt);
            }
        });
        txtCodigoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoClienteActionPerformed(evt);
            }
        });
        txtCodigoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoClienteKeyPressed(evt);
            }
        });

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Cliente");

        txtNombreDelCliente1.setEditable(false);
        txtNombreDelCliente1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDelCliente1FocusGained(evt);
            }
        });
        txtNombreDelCliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDelCliente1ActionPerformed(evt);
            }
        });
        txtNombreDelCliente1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDelCliente1KeyPressed(evt);
            }
        });

        txtBarrioCliente1.setEditable(false);
        txtBarrioCliente1.setName("numerico"); // NOI18N
        txtBarrioCliente1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarrioCliente1FocusGained(evt);
            }
        });
        txtBarrioCliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarrioCliente1ActionPerformed(evt);
            }
        });
        txtBarrioCliente1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarrioCliente1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBarrioCliente1KeyReleased(evt);
            }
        });

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Código");

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("Barrio");

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Teléfono");

        txtTelefonoDelCliente1.setEditable(false);
        txtTelefonoDelCliente1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoDelCliente1FocusGained(evt);
            }
        });
        txtTelefonoDelCliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoDelCliente1ActionPerformed(evt);
            }
        });
        txtTelefonoDelCliente1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoDelCliente1KeyPressed(evt);
            }
        });

        lblCirculoDeProgreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Dirección");

        txtDireccionDelCliente1.setEditable(false);
        txtDireccionDelCliente1.setName("numerico"); // NOI18N
        txtDireccionDelCliente1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDireccionDelCliente1FocusGained(evt);
            }
        });
        txtDireccionDelCliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionDelCliente1ActionPerformed(evt);
            }
        });
        txtDireccionDelCliente1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionDelCliente1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionDelCliente1KeyReleased(evt);
            }
        });

        lblCargandoUnMomento.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCargandoUnMomento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/cargando_un_momento_por_favor.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(txtCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCirculoDeProgreso1))
                            .addComponent(txtBarrioCliente1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                            .addComponent(txtTelefonoDelCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDireccionDelCliente1)
                            .addComponent(txtNombreDelCliente1)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(lblCargandoUnMomento, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCirculoDeProgreso1)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDelCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDireccionDelCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBarrioCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefonoDelCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(lblCargandoUnMomento, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta por Parte del nombre del cliente"));

        txtParteDelNombreDelCliente.setEditable(false);
        txtParteDelNombreDelCliente.setName(""); // NOI18N
        txtParteDelNombreDelCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtParteDelNombreDelClienteFocusGained(evt);
            }
        });
        txtParteDelNombreDelCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParteDelNombreDelClienteActionPerformed(evt);
            }
        });
        txtParteDelNombreDelCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtParteDelNombreDelClienteKeyPressed(evt);
            }
        });

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("Nombre Cliente");

        lblCirculoDeProgreso2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtParteDelNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(lblCirculoDeProgreso2)
                .addGap(57, 57, 57))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtParteDelNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCirculoDeProgreso2))
                .addContainerGap())
        );

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código Cliente", "nombre Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblClientes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tblClientes);
        if (tblClientes.getColumnModel().getColumnCount() > 0) {
            tblClientes.getColumnModel().getColumn(1).setPreferredWidth(450);
        }

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(121, 121, 121))
        );

        pnlEntregas1.setAutoscrolls(true);
        pnlEntregas1.setDoubleBuffered(false);
        pnlEntregas1.setEnabled(false);

        tblFacturasPorCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Fecha", "Cliente", "Dirección", "Movimiento", "valorFactura"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFacturasPorCliente.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblFacturasPorCliente.setColumnSelectionAllowed(true);
        tblFacturasPorCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFacturasPorClienteMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblFacturasPorCliente);
        tblFacturasPorCliente.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tblFacturasPorCliente.getColumnModel().getColumnCount() > 0) {
            tblFacturasPorCliente.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblFacturasPorCliente.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblFacturasPorCliente.getColumnModel().getColumn(2).setPreferredWidth(90);
            tblFacturasPorCliente.getColumnModel().getColumn(3).setPreferredWidth(270);
            tblFacturasPorCliente.getColumnModel().getColumn(4).setPreferredWidth(270);
            tblFacturasPorCliente.getColumnModel().getColumn(5).setPreferredWidth(200);
            tblFacturasPorCliente.getColumnModel().getColumn(6).setPreferredWidth(110);
        }

        javax.swing.GroupLayout pnlEntregas1Layout = new javax.swing.GroupLayout(pnlEntregas1);
        pnlEntregas1.setLayout(pnlEntregas1Layout);
        pnlEntregas1Layout.setHorizontalGroup(
            pnlEntregas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregas1Layout.createSequentialGroup()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 1078, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );
        pnlEntregas1Layout.setVerticalGroup(
            pnlEntregas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregas1Layout.createSequentialGroup()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Facturas", pnlEntregas1);

        jToolBar2.setRollover(true);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Table.png"))); // NOI18N
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton4);

        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jToggleButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton3.setFocusable(false);
        jToggleButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jToggleButton3);

        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jToggleButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton4.setFocusable(false);
        jToggleButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jToggleButton4);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(229, 229, 229))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Historial Cliente", jPanel6);

        jToolBar1.setRollover(true);

        jToggleButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jToggleButton5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton5.setFocusable(false);
        jToggleButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jButton6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton6.setEnabled(false);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jToggleButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton2.setEnabled(false);
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jToggleButton2);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jButton7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Table.png"))); // NOI18N
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jToggleButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jToggleButton1);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jTabbedPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
//        if (manifiestoModificadoPorMi != null) {
//            manifiestoModificadoPorMi.setIsFree(1);
//        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void txtValorDescontadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorDescontadoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValorDescontado.setText(txtValorDescontado.getText().trim().replace(",", "."));

        }
    }//GEN-LAST:event_txtValorDescontadoKeyPressed

    private void txtValorDescontadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorDescontadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorDescontadoActionPerformed

    private void txtValorDescontadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorDescontadoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorDescontadoFocusGained

    private void txtValorRecogidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorRecogidaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValorRecogida.setText(txtValorRecogida.getText().trim().replace(",", "."));
            txtValorDescontado.requestFocus();
        }
    }//GEN-LAST:event_txtValorRecogidaKeyPressed

    private void txtValorRecogidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorRecogidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorRecogidaActionPerformed

    private void txtValorRecogidaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorRecogidaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorRecogidaFocusGained

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

    private void tblMovimientosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMovimientosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblMovimientosMouseClicked

    private void tblProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaMouseClicked

    private void txtFechaVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaVentaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaVentaKeyPressed

    private void txtFechaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaVentaActionPerformed

    private void txtFechaVentaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFechaVentaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaVentaFocusGained

    private void txtTelefonoDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteKeyPressed

    private void txtTelefonoDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteActionPerformed

    private void txtTelefonoDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteFocusGained

    private void txtNombreDelVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDelVendedorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelVendedorKeyPressed

    private void txtNombreDelVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDelVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelVendedorActionPerformed

    private void txtNombreDelVendedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDelVendedorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelVendedorFocusGained

    private void txtBarrioClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioClienteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioClienteKeyReleased

    private void txtBarrioClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioClienteKeyPressed

    }//GEN-LAST:event_txtBarrioClienteKeyPressed

    private void txtBarrioClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarrioClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioClienteActionPerformed

    private void txtBarrioClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarrioClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioClienteFocusGained

    private void txtNombreDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDelClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelClienteKeyPressed

    private void txtNombreDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelClienteActionPerformed

    private void txtNombreDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelClienteFocusGained

    private void txtNumeroFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroFacturaKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {
                txtDireccionDelCliente.setText("");
                txtBarrioCliente.setText("");
                txtFechaVenta.setText("");
                /* nombre del cliente y el código entre parétesis*/
                txtNombreDelCliente.setText("");
                txtNombreDelVendedor.setText("");
                txtTelefonoDelCliente.setText("");
                limpiarTablaProductosPorFactura();
                limpiarTablaBitacora();
                limpiarTablaMovimientos();
                limpiarTablaProductosRechazadosPorFactura();
                limpiarTablaDescuentos();
                limpiarTablaRecogidas();

                this.factura = new CFacturas(ini, txtNumeroFactura.getText().trim());

                if (this.factura.getNumeroDeFactura() != null) {
                    jTabbedPane2.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif")));
                    lblCirculoDeProgreso.setVisible(true);

                    //llenarDatosDeLaVista(facturaActual);
                    
                    /*Hilo que recupera los movimientos de la facturaActual */
                    new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Número de Factura no encontrado en el sistema ", "Error", JOptionPane.WARNING_MESSAGE);

                }
                

            } catch (Exception ex) {
                Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_txtNumeroFacturaKeyPressed

    private void txtNumeroFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFacturaActionPerformed

    private void txtNumeroFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroFacturaFocusGained
        txtNumeroFactura.setSelectionStart(0);
        txtNumeroFactura.setSelectionEnd(txtNumeroFactura.getText().length());
    }//GEN-LAST:event_txtNumeroFacturaFocusGained

    private void tblBitacoraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBitacoraMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBitacoraMouseClicked

    private void btnDescargarRechazoTotal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarRechazoTotal2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDescargarRechazoTotal2ActionPerformed

    private void txtDireccionDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelClienteFocusGained

    private void txtDireccionDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelClienteActionPerformed

    private void txtDireccionDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionDelClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelClienteKeyPressed

    private void txtDireccionDelClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionDelClienteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelClienteKeyReleased

    private void tblProductosPorFacturaRechazadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaRechazadosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaRechazadosMouseClicked

    private void tblDescuentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDescuentosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDescuentosMouseClicked

    private void txtNumeroDeSoporteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeSoporteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtFacturaAfectada.requestFocus();
        }
    }//GEN-LAST:event_txtNumeroDeSoporteKeyPressed

    private void txtNumeroDeSoporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeSoporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeSoporteActionPerformed

    private void txtNumeroDeSoporteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeSoporteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeSoporteFocusGained

    private void tblRecogidasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRecogidasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblRecogidasMouseClicked

    private void mostrarIncidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarIncidenciaActionPerformed
        /*Si hay una vista de gestion abierta sale de la funcion*/
        if (fGestion) {
            JOptionPane.showMessageDialog(this, "Ya hay una gestion abierta ", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

//        if (tablaIncidencias.getValueAt(filaSeleccionada, 2).toString().equals("CERRADA")) {
//            JOptionPane.showMessageDialog(this, "Incidencia se encuentra cerrada", "Error", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
        incidencia = new CSvcIncidencias();
        try {
            /* se identifica la fila seleccionada */
            filaSeleccionada = tablaIncidencias.getSelectedRow();
            for (CSvcIncidencias obj : listaDeIncidencias) {
                if (obj.getNumeroFactura().equals(tablaIncidencias.getValueAt(filaSeleccionada, 3))) {
                    incidencia = obj;
                    incidencia.cargarListadoDegestiones(obj.getConsecutivo());
                    factura = new CFacturas(ini, obj.getNumeroFactura());
                    incidencia.setObjFacturaCamdun(factura);
                    break;
                }
            }

            FGestionarIncidencias forma = new FGestionarIncidencias(this);

            formPPalCostumerService.escritorio.add(forma);
            forma.setTitle("Formulario para getion de la incidencia # " + incidencia.getConsecutivo() + " de la factura # " + incidencia.getNumeroFactura());
            forma.setLocation(((ini.getDimension().width - forma.getSize().width) / 2), ((ini.getDimension().height - (forma.getSize().height + 43)) / 2));

            forma.toFront();
            forma.show();

        } catch (Exception ex) {
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mostrarIncidenciaActionPerformed

    private void cerrarIncidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarIncidenciaActionPerformed

        /*Si hay una vista de gestion abierta sale de la funcion*/
        if (fGestion) {
            JOptionPane.showMessageDialog(this, "Ya hay una gestion abierta ", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        fGestion = true;
        CSvcIncidencias incidencia = null;
        try {
            /* se identifica la fila seleccionada */
            int fila = tablaIncidencias.getSelectedRow();
            for (CSvcIncidencias obj : listaDeIncidencias) {
                if (obj.getNumeroFactura().equals(tablaIncidencias.getValueAt(fila, 1))) {
                    incidencia = obj;
                    break;
                }
            }

            FCerrarIncidencia forma = new FCerrarIncidencia(ini, incidencia, this);

            formPPalCostumerService.escritorio.add(forma);
            forma.setTitle("Formulario para getion de la incidencia # " + incidencia.getConsecutivo() + " de la factura # " + incidencia.getNumeroFactura());
            forma.setLocation(((ini.getDimension().width - forma.getSize().width) / 2), ((ini.getDimension().height - (forma.getSize().height - 43)) / 2));
            forma.toFront();
            forma.show();
        } catch (Exception ex) {
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_cerrarIncidenciaActionPerformed

    private void rbtIncidenciasAbiertasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtIncidenciasAbiertasActionPerformed
        try {
            limpiarTablaIncidencias();
            llenarTablaIncidencias();
        } catch (Exception ex) {
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbtIncidenciasAbiertasActionPerformed

    private void rbtIncidenciasCerradasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtIncidenciasCerradasActionPerformed
        try {
            limpiarTablaIncidencias();
            llenarTablaIncidencias();
        } catch (Exception ex) {
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbtIncidenciasCerradasActionPerformed

    private void txtCodigoClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoClienteFocusGained
        txtCodigoCliente.setSelectionStart(0);
        txtCodigoCliente.setSelectionEnd(txtCodigoCliente.getText().length());
    }//GEN-LAST:event_txtCodigoClienteFocusGained

    private void txtCodigoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoClienteActionPerformed

    private void txtCodigoClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoClienteKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            consultarCliente();

        }
        lblCirculoDeProgreso.setVisible(false);
    }//GEN-LAST:event_txtCodigoClienteKeyPressed

    private void txtNombreDelCliente1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDelCliente1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelCliente1FocusGained

    private void txtNombreDelCliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDelCliente1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelCliente1ActionPerformed

    private void txtNombreDelCliente1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDelCliente1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelCliente1KeyPressed

    private void txtBarrioCliente1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarrioCliente1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioCliente1FocusGained

    private void txtBarrioCliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarrioCliente1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioCliente1ActionPerformed

    private void txtBarrioCliente1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioCliente1KeyPressed

    }//GEN-LAST:event_txtBarrioCliente1KeyPressed

    private void txtBarrioCliente1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioCliente1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioCliente1KeyReleased

    private void txtTelefonoDelCliente1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoDelCliente1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelCliente1FocusGained

    private void txtTelefonoDelCliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoDelCliente1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelCliente1ActionPerformed

    private void txtTelefonoDelCliente1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoDelCliente1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelCliente1KeyPressed

    private void txtDireccionDelCliente1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionDelCliente1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelCliente1FocusGained

    private void txtDireccionDelCliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionDelCliente1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelCliente1ActionPerformed

    private void txtDireccionDelCliente1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionDelCliente1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelCliente1KeyPressed

    private void txtDireccionDelCliente1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionDelCliente1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelCliente1KeyReleased

    private void txtParteDelNombreDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtParteDelNombreDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParteDelNombreDelClienteFocusGained

    private void txtParteDelNombreDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParteDelNombreDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParteDelNombreDelClienteActionPerformed

    private void txtParteDelNombreDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtParteDelNombreDelClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            limpiarTablaCliente();
            new Thread(new HiloListadoClientes(ini, txtParteDelNombreDelCliente.getText(), tblClientes)).start();
            lblCirculoDeProgreso2.setVisible(false);
        }
    }//GEN-LAST:event_txtParteDelNombreDelClienteKeyPressed

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        /* Se identifica la fila seleccionada*/
        int filaSelleccionada = tblClientes.getSelectedRow();

        /* Se identifica el codigo del producto */
        txtCodigoCliente.setText(tblClientes.getValueAt(filaSelleccionada, 0).toString());
        //consultarCliente();
         new Thread(new HiloConsultarCliente(ini, this)).start();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void tblFacturasPorClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFacturasPorClienteMouseClicked
        /* Se identifica la fila seleccionada*/
        int filaSelleccionada = tblFacturasPorCliente.getSelectedRow();

        consultarLafactura(tblFacturasPorCliente.getValueAt(filaSelleccionada, 1).toString());
    }//GEN-LAST:event_tblFacturasPorClienteMouseClicked

    private void jPanel6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel6FocusGained

    }//GEN-LAST:event_jPanel6FocusGained

    private void jPanel7FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel7FocusGained

    }//GEN-LAST:event_jPanel7FocusGained

    private void jPanel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseClicked
        lblCirculoDeProgreso.setVisible(false);
        lblCirculoDeProgreso1.setVisible(false);

        txtCodigoCliente.setEnabled(true);
        txtCodigoCliente.setEditable(true);
        txtCodigoCliente.requestFocus();

        txtParteDelNombreDelCliente.setEnabled(true);
        txtParteDelNombreDelCliente.setEditable(true);
        txtParteDelNombreDelCliente.requestFocus();
    }//GEN-LAST:event_jPanel6MouseClicked

    private void rbtIncidenciasTodasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtIncidenciasTodasActionPerformed
        try {
            limpiarTablaIncidencias();
            llenarTablaIncidencias();
        } catch (Exception ex) {
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbtIncidenciasTodasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String cadena = "'";
        /*seleccionar las facturas que ya estan en el lstado de incidencias y armar la cadena*/
        for (CSvcIncidencias obj : listaDeIncidencias) {
            cadena += obj.getConsecutivo() + "','";
        }

        if (cadena.length() > 0) {
            cadena = cadena.substring(0, cadena.length() - 2);
        }

        /* realizar la consulta a la tabla incidenciasXfecha donde elige las facturas que no tenemos en el listado de incidencias*/
        ArrayList<CSvcIncidencias> lista = new ArrayList<>();
        lista = controlador.refrescarListadoDeIncidencias(cadena);

        /*llenar la tabla de incidencias con el arraylist de incidencias*/
 /*adicionar los registros a la lista de incidencias actual*/
        refrescarIncidencias(lista);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
      txtParteDelNombreDelCliente.setEditable(true);
      txtCodigoCliente.setEditable(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
     
        
        try {
            FGestionarIncidencias fSolucionarIncidencias = new FGestionarIncidencias(ini);
            PrincipalUsuarioCostumerService.escritorio.add(fSolucionarIncidencias);
            fSolucionarIncidencias.setLocation(((ini.getDimension().width - fSolucionarIncidencias.getSize().width) / 2), ((ini.getDimension().height - fSolucionarIncidencias.getSize().height) / 2) - 30);
            fSolucionarIncidencias.setVisible(true);
            //fReporteDescuentos_Recogidas.cargarInformacion();
            fSolucionarIncidencias.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        salir();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void crearIncidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crearIncidenciaActionPerformed
        try {
            FGestionarIncidencias fSolucionarIncidencias = new FGestionarIncidencias(ini);
            PrincipalUsuarioCostumerService.escritorio.add(fSolucionarIncidencias);
            fSolucionarIncidencias.setLocation(((ini.getDimension().width - fSolucionarIncidencias.getSize().width) / 2), ((ini.getDimension().height - fSolucionarIncidencias.getSize().height) / 2) - 30);
            fSolucionarIncidencias.setVisible(true);
            //fReporteDescuentos_Recogidas.cargarInformacion
            fSolucionarIncidencias.form=this;
            fSolucionarIncidencias.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_crearIncidenciaActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    public synchronized void llenarTablaProductosPorFactura() throws Exception {
        if (listaProductosPorFactura != null) {
            double valorFactura = 0;
            modelo2 = (DefaultTableModel) tblProductosPorFactura.getModel();
            for (Vst_ProductosPorFactura obj : listaProductosPorFactura) {
                filaTabla2 = tblProductosPorFactura.getRowCount();

                modelo2.addRow(new Object[tblProductosPorFactura.getRowCount()]);
                tblProductosPorFactura.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), filaTabla2, 1); // numero de facturaActual
                tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), filaTabla2, 2); // numero de facturaActual
                tblProductosPorFactura.setValueAt(obj.getCantidad(), filaTabla2, 3); // numero de facturaActual
                tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), filaTabla2, 4); // numero de facturaActual
                tblProductosPorFactura.setValueAt(nf.format(obj.getValorTotalConIva()), filaTabla2, 5); // numero de facturaActual

                valorFactura += obj.getValorTotalConIva();

            }
            Thread.sleep(1500);
            lblValorTotalFactura.setText(nf.format(valorFactura));
        }

    }

    public synchronized void llenarTablaDistribucion() throws Exception {
        if (listaDeMovimientosEnDistribucion != null) {
            DefaultTableModel modelo = (DefaultTableModel) tblMovimientos.getModel();

            for (Vst_FacturasPorManifiesto obj : listaDeMovimientosEnDistribucion) {
                filaTabla2 = tblMovimientos.getRowCount();

                modelo.addRow(new Object[tblMovimientos.getRowCount()]);
                tblMovimientos.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblMovimientos.setValueAt(obj.getNumeroManifiesto(), filaTabla2, 1); // numero de manifiesto
                tblMovimientos.setValueAt(obj.getFechaDistribucion(), filaTabla2, 2); // fecha de distribucion
                tblMovimientos.setValueAt(obj.getVehiculo(), filaTabla2, 3); // placa del vehiculo
                tblMovimientos.setValueAt(obj.getNombreConductor(), filaTabla2, 4); // nombre del conductor
                tblMovimientos.setValueAt(obj.getNombreRuta(), filaTabla2, 5); // nombre de la ruta

            }
        }

    }

    public synchronized void llenarTablabitacora() throws Exception {
      

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeMovimientosBitacora.size());

        DefaultTableModel modelo = (DefaultTableModel) tblBitacora.getModel();

        for (CBitacoraFacturas obj : listaDeMovimientosBitacora) {
            filaTabla2 = tblBitacora.getRowCount();

            modelo.addRow(new Object[tblBitacora.getRowCount()]);

            tblBitacora.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
            tblBitacora.setValueAt(obj.getFecha(), filaTabla2, 1); // numero de manifiesto
            tblBitacora.setValueAt(obj.getNumeroDocumento(), filaTabla2, 2); // fecha de distribucion
            tblBitacora.setValueAt(obj.getObservacion(), filaTabla2, 3); // placa del vehiculo
            // tblBitacora.setValueAt(obj.getNombreConductor(), filaSeleccionada, 4); // nombre del conductor

        }

    }

    public synchronized void llenarTablaProductosRechazados() throws Exception {
       

        if (listaDeProductosRechazados != null) {
            System.out.println(new Date() + " acá se calcula la cantidad de productos rechazados =" + listaDeProductosRechazados.size());

            DefaultTableModel modelo = (DefaultTableModel) tblProductosPorFacturaRechazados.getModel();

            for (Vst_ProductosPorFacturaDescargados obj : listaDeProductosRechazados) {
                filaTabla2 = tblProductosPorFacturaRechazados.getRowCount();

                modelo.addRow(new Object[tblProductosPorFacturaRechazados.getRowCount()]);

                tblProductosPorFacturaRechazados.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblProductosPorFacturaRechazados.setValueAt(obj.getCodigoProducto(), filaTabla2, 1); // numero de manifiesto
                tblProductosPorFacturaRechazados.setValueAt(obj.getDescripcionProducto(), filaTabla2, 2); // fecha de distribucion
                tblProductosPorFacturaRechazados.setValueAt(obj.getCantidadRechazada(), filaTabla2, 3); // placa del vehiculo
                tblProductosPorFacturaRechazados.setValueAt(obj.getNombreCausalDeRechazo(), filaTabla2, 4); // nombre del conductor

            }
        }

    }

    public synchronized void llenarTablaIncidencias() throws Exception {

       // listaDeIncidencias = this.controlador.getListaDeIncidencias();

        if (listaDeIncidencias != null) {
            System.out.println(new Date() + " acá se calcula la cantidad de incidencias =" + listaDeIncidencias.size());

            if (rbtIncidenciasAbiertas.isSelected()) {
                seleccionarIncidenciasAbiertas();
            }
            if (rbtIncidenciasCerradas.isSelected()) {
                seleccionarIncidenciasCerradas();

            }
            if (rbtIncidenciasTodas.isSelected()) {
                seleccionarTodasLasIncidencias();
            }

        }

    }

    private void seleccionarTodasLasIncidencias() throws NumberFormatException {
        DefaultTableModel modelo = (DefaultTableModel) tablaIncidencias.getModel();
        tablaIncidencias.setDefaultRenderer(Object.class, new IconCellRenderer());

        for (CSvcIncidencias obj : listaDeIncidencias) {

            filaTabla2 = tablaIncidencias.getRowCount();

            modelo.addRow(new Object[tablaIncidencias.getRowCount()]);

            tablaIncidencias.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
            switch (obj.getMedioDePeticion()) {

                case 1:
                    tablaIncidencias.setValueAt(new JLabel(iconRuta), filaTabla2, 1); // fecha de distribucion
                    break;
                case 2:
                    tablaIncidencias.setValueAt(new JLabel(iconPhone), filaTabla2, 1); // fecha de distribucion
                    break;
                case 3:
                    tablaIncidencias.setValueAt(new JLabel(iconMail), filaTabla2, 1); // fecha de distribucion
                    break;
                case 4:
                    tablaIncidencias.setValueAt(new JLabel(iconClip), filaTabla2, 1); // fecha de distribucion
                    break;

            }
            tablaIncidencias.setValueAt(obj.getCantidadGestiones(), filaTabla2, 2); // fecha de distribucion
            tablaIncidencias.setValueAt(obj.getNumeroFactura(), filaTabla2, 3); // fecha de distribucion
            String valor;
            valor = (obj.getIdEstado()== 1) ? "ABIERTA" : "CERRADA";

            tablaIncidencias.setValueAt(valor, filaTabla2, 4); // fecha de distribucion
            tablaIncidencias.setValueAt(obj.getFechaIncidencia(), filaTabla2, 5); // numero de manifiesto
            tablaIncidencias.setValueAt(obj.getConsecutivo(), filaTabla2, 6); // numero de manifiesto
            tablaIncidencias.setValueAt(obj.getNombreTipoDeMovimiento(), filaTabla2, 7); // numero de manifiesto
            tablaIncidencias.setValueAt(obj.getNombreCausalDeRechazo(), filaTabla2, 8); // numero de manifiesto
            tablaIncidencias.setValueAt(obj.getNombrResponsable(), filaTabla2, 9); // numero de manifiesto 
            tablaIncidencias.setValueAt(nf.format(obj.getObjFacturaCamdun().getValorTotalFactura()), filaTabla2, 10); // nombre del conductor  

        }
    }

    private void refrescarIncidencias(ArrayList<CSvcIncidencias> lista) throws NumberFormatException {

        DefaultTableModel modelo = (DefaultTableModel) tablaIncidencias.getModel();
        tablaIncidencias.setDefaultRenderer(Object.class, new IconCellRenderer());

        for (CSvcIncidencias obj : lista) {

            filaTabla2 = tablaIncidencias.getRowCount();

            modelo.addRow(new Object[tablaIncidencias.getRowCount()]);

            tablaIncidencias.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
            switch (obj.getMedioDePeticion()) {

                case 1:
                    tablaIncidencias.setValueAt(new JLabel(iconRuta), filaTabla2, 1); // fecha de distribucion
                    break;
                case 2:
                    tablaIncidencias.setValueAt(new JLabel(iconPhone), filaTabla2, 1); // fecha de distribucion
                    break;
                case 3:
                    tablaIncidencias.setValueAt(new JLabel(iconMail), filaTabla2, 1); // fecha de distribucion
                    break;
                case 4:
                    tablaIncidencias.setValueAt(new JLabel(iconClip), filaTabla2, 1); // fecha de distribucion
                    break;

            }
            tablaIncidencias.setValueAt(obj.getCantidadGestiones(), filaTabla2, 2); // fecha de distribucion
            tablaIncidencias.setValueAt(obj.getNumeroFactura(), filaTabla2, 3); // fecha de distribucion
            String valor;
            valor = (obj.getIdEstado()==1) ? "ABIERTA" : "CERRADA";

            tablaIncidencias.setValueAt(valor, filaTabla2, 4); // fecha de distribucion
            tablaIncidencias.setValueAt(obj.getFechaIncidencia(), filaTabla2, 5); // numero de manifiesto
            tablaIncidencias.setValueAt(obj.getConsecutivo(), filaTabla2, 6); // numero de manifiesto
            tablaIncidencias.setValueAt(obj.getNombreTipoDeMovimiento(), filaTabla2, 7); // numero de manifiesto
            tablaIncidencias.setValueAt(obj.getNombreCausalDeRechazo(), filaTabla2, 8); // numero de manifiesto
            tablaIncidencias.setValueAt(obj.getNombrResponsable(), filaTabla2, 9); // numero de manifiesto 
            tablaIncidencias.setValueAt(nf.format(obj.getObjFacturaCamdun().getValorTotalFactura()), filaTabla2, 10); // nombre del conductor  

            /*Actualizar listado de inciedencias*/
            listaDeIncidencias.add(obj);

        }
    }

    private void seleccionarIncidenciasAbiertas() throws NumberFormatException {
        DefaultTableModel modelo = (DefaultTableModel) tablaIncidencias.getModel();
        tablaIncidencias.setDefaultRenderer(Object.class, new IconCellRenderer());

        for (CSvcIncidencias obj : listaDeIncidencias) {
            if (obj.getIdEstado()==1) {

                filaTabla2 = tablaIncidencias.getRowCount();

                modelo.addRow(new Object[tablaIncidencias.getRowCount()]);

                tablaIncidencias.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                switch (obj.getMedioDePeticion()) {

                    case 1:
                        tablaIncidencias.setValueAt(new JLabel(iconRuta), filaTabla2, 1); // fecha de distribucion
                        break;
                    case 2:
                        tablaIncidencias.setValueAt(new JLabel(iconPhone), filaTabla2, 1); // fecha de distribucion
                        break;
                    case 3:
                        tablaIncidencias.setValueAt(new JLabel(iconMail), filaTabla2, 1); // fecha de distribucion
                        break;
                    case 4:
                        tablaIncidencias.setValueAt(new JLabel(iconClip), filaTabla2, 1); // fecha de distribucion
                        break;

                }

                tablaIncidencias.setValueAt(obj.getNumeroFactura(), filaTabla2, 2); // fecha de distribucion
                String valor;
                valor = (obj.getIdEstado()==1) ? "ABIERTA" : "CERRADA";

                tablaIncidencias.setValueAt(valor, filaTabla2, 3); // fecha de distribucion
                tablaIncidencias.setValueAt(obj.getFechaIncidencia(), filaTabla2, 4); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getConsecutivo(), filaTabla2, 5); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getNombreTipoDeMovimiento(), filaTabla2, 6); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getNombreCausalDeRechazo(), filaTabla2, 7); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getNombrResponsable(), filaTabla2, 8); // numero de manifiesto 
                tablaIncidencias.setValueAt(nf.format(obj.getObjFacturaCamdun().getValorTotalFactura()), filaTabla2, 9); // nombre del conductor  
            }

        }
    }

    private void seleccionarIncidenciasCerradas() throws NumberFormatException {
        DefaultTableModel modelo = (DefaultTableModel) tablaIncidencias.getModel();
        tablaIncidencias.setDefaultRenderer(Object.class, new IconCellRenderer());

        for (CSvcIncidencias obj : listaDeIncidencias) {
            if (obj.getIdEstado() == 2) {

                filaTabla2 = tablaIncidencias.getRowCount();

                modelo.addRow(new Object[tablaIncidencias.getRowCount()]);

                tablaIncidencias.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                switch (obj.getMedioDePeticion()) {

                    case 1:
                        tablaIncidencias.setValueAt(new JLabel(iconRuta), filaTabla2, 1); // fecha de distribucion
                        break;
                    case 2:
                        tablaIncidencias.setValueAt(new JLabel(iconPhone), filaTabla2, 1); // fecha de distribucion
                        break;
                    case 3:
                        tablaIncidencias.setValueAt(new JLabel(iconMail), filaTabla2, 1); // fecha de distribucion
                        break;
                    case 4:
                        tablaIncidencias.setValueAt(new JLabel(iconClip), filaTabla2, 1); // fecha de distribucion
                        break;

                }

                tablaIncidencias.setValueAt(obj.getNumeroFactura(), filaTabla2, 2); // fecha de distribucion
                String valor;
                valor = (obj.getIdEstado()==1 ) ? "ABIERTA" : "CERRADA";

                tablaIncidencias.setValueAt(valor, filaTabla2, 3); // fecha de distribucion
                tablaIncidencias.setValueAt(obj.getFechaIncidencia(), filaTabla2, 4); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getConsecutivo(), filaTabla2, 5); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getNombreTipoDeMovimiento(), filaTabla2, 6); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getNombreCausalDeRechazo(), filaTabla2, 7); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getNombrResponsable(), filaTabla2, 8); // numero de manifiesto 
                tablaIncidencias.setValueAt(nf.format(obj.getObjFacturaCamdun().getValorTotalFactura()), filaTabla2, 9); // nombre del conductor  
            }

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDescargarRechazoTotal1;
    private javax.swing.JButton btnDescargarRechazoTotal2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenuItem cerrarIncidencia;
    private javax.swing.JMenuItem crearIncidencia;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    public javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTabbedPane jTabbedPane2;
    public javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    public javax.swing.JLabel lblCargandoUnMomento;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JLabel lblCirculoDeProgreso1;
    public javax.swing.JLabel lblCirculoDeProgreso2;
    private javax.swing.JLabel lblPrecioFactura2;
    public javax.swing.JLabel lblValorRecaudado;
    private javax.swing.JLabel lblValorTotalFactura;
    private javax.swing.JPopupMenu menuIncidencias;
    private javax.swing.JMenuItem mostrarIncidencia;
    public javax.swing.JPanel pnlDistribucion;
    private javax.swing.JPanel pnlEntregas;
    private javax.swing.JPanel pnlEntregas1;
    private javax.swing.JPanel pnlMovimientos;
    private javax.swing.JPanel pnlRechazosParciales;
    private javax.swing.JPanel pnlRecogidas;
    private javax.swing.JRadioButton rbtIncidenciasAbiertas;
    private javax.swing.JRadioButton rbtIncidenciasCerradas;
    private javax.swing.JRadioButton rbtIncidenciasPorResponsable;
    private javax.swing.JRadioButton rbtIncidenciasTodas;
    public javax.swing.JTable tablaIncidencias;
    private javax.swing.JTable tblBitacora;
    public javax.swing.JTable tblClientes;
    private javax.swing.JTable tblDescuentos;
    private javax.swing.JTable tblFacturasPorCliente;
    private javax.swing.JTable tblMovimientos;
    private javax.swing.JTable tblProductosPorFactura;
    public javax.swing.JTable tblProductosPorFacturaRechazados;
    private javax.swing.JTable tblRecogidas;
    public javax.swing.JTextField txtBarrioCliente;
    public javax.swing.JTextField txtBarrioCliente1;
    public javax.swing.JTextField txtCodigoCliente;
    public javax.swing.JTextField txtDireccionDelCliente;
    public javax.swing.JTextField txtDireccionDelCliente1;
    public javax.swing.JTextField txtFacturaAfectada;
    public javax.swing.JTextField txtFechaVenta;
    public javax.swing.JTextField txtNombreDelCliente;
    public javax.swing.JTextField txtNombreDelCliente1;
    public javax.swing.JTextField txtNombreDelVendedor;
    public javax.swing.JTextField txtNumeroDeSoporte;
    public javax.swing.JTextField txtNumeroFactura;
    public javax.swing.JTextField txtParteDelNombreDelCliente;
    public javax.swing.JTextField txtTelefonoDelCliente;
    public javax.swing.JTextField txtTelefonoDelCliente1;
    public javax.swing.JTextField txtValorDescontado;
    public javax.swing.JTextField txtValorRecogida;
    // End of variables declaration//GEN-END:variables

    public void limpiarTodo() {
        listaProductosPorFactura = new ArrayList();
        listaDeMovimientosEnDistribucion = new ArrayList();

        listaProductosPorFactura = null;

        txtNumeroFactura.setText("");
        txtBarrioCliente.setText("");
        txtFechaVenta.setText("");
        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText("");
        txtNombreDelVendedor.setText("");
        txtTelefonoDelCliente.setText("");
        limpiarTablaProductosPorFactura();
        limpiarTablaMovimientos();
        limpiarTablaBitacora();
    }

    public void limpiarTablaProductosPorFactura() {

        DefaultTableModel modelo = (DefaultTableModel) tblProductosPorFactura.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaProductosRechazadosPorFactura() {

        DefaultTableModel modelo = (DefaultTableModel) tblProductosPorFacturaRechazados.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaDescuentos() {

        DefaultTableModel modelo = (DefaultTableModel) tblDescuentos.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaRecogidas() {

        DefaultTableModel modelo = (DefaultTableModel) tblRecogidas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaMovimientos() {

        DefaultTableModel modelo = (DefaultTableModel) tblMovimientos.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaBitacora() {

        DefaultTableModel modelo = (DefaultTableModel) tblBitacora.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaIncidencias() {

        DefaultTableModel modelo = (DefaultTableModel) tablaIncidencias.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    private void consultarCliente() {
        // limpiarTodo();
        lblCirculoDeProgreso.setVisible(true);
        lblCargandoUnMomento.setVisible(true);

        try {
            /*Crea objeto desde la BBDD remota*/
            cliente = new CClientes(ini, txtCodigoCliente.getText().trim(), true);
            try {

                if (!this.cliente.getCodigoInterno().equals("")) {
                    limpiarTablaFacturasPorCliente();

                    txtDireccionDelCliente1.setText(cliente.getDireccion());
                    txtBarrioCliente1.setText(cliente.getBarrio());
                    txtNombreDelCliente1.setText(cliente.getNombreDeCliente());
                    txtTelefonoDelCliente1.setText(cliente.getCelularCliente());

                    /*Consulta  las facturas del servidor remoto*/
                    cliente.setListaDeFacturasDescargadas(true);

                    llenarTablaFacturasPorCliente();
                    
                    /*Hilo que recupera los movimientos de la facturaActual */
                    lblCirculoDeProgreso.setVisible(false);
                    //  new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente no existe en el sistema", "Error", JOptionPane.WARNING_MESSAGE);

                }

            } catch (Exception ex) {

                Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void limpiarTablaCliente() {

        DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    private void consultarLafactura(String numeroFactura) {
        try {

            CFacturas facturaActual = new CFacturas(ini, numeroFactura, true);

            if (facturaActual != null) {
                FConsultarFacturasRemoto formulario = new FConsultarFacturasRemoto(ini, this, facturaActual);
                this.getDesktopPane().add(formulario);
                formulario.toFront();
                formulario.setClosable(true);
                formulario.setVisible(true);
                formulario.setTitle("Formulario para consultar Facturas");

                //form.setSize(PrincipalLogistica.escritorio.getSize());
                formulario.setLocation((ini.getDimension().width - formulario.getSize().width) / 2, (ini.getDimension().height - formulario.getSize().height) / 2);
                formulario.show();
            } else {
                JOptionPane.showInternalMessageDialog(this, "La factura no aparece en el sistema", "Factura no visible ", 2);
            }

        } catch (Exception ex) {
            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void limpiarTablaFacturasPorCliente() {

        DefaultTableModel modelo = (DefaultTableModel) tblFacturasPorCliente.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public synchronized void llenarTablaFacturasPorCliente() throws Exception {
        if (cliente.getListaDeFacturasdescargadas().size()>0) {

            RowsRenderer rr = new RowsRenderer(5);
            tblFacturasPorCliente.setDefaultRenderer(Object.class, rr);

            modelo2 = (DefaultTableModel) tblFacturasPorCliente.getModel();
            for (Vst_FacturasDescargadas obj : cliente.getListaDeFacturasdescargadas()) {
                filaTabla2 = tblFacturasPorCliente.getRowCount();

                modelo2.addRow(new Object[tblFacturasPorCliente.getRowCount()]);
                tblFacturasPorCliente.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblFacturasPorCliente.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de facturaActual
                tblFacturasPorCliente.setValueAt(obj.getFechaDeVenta(), filaTabla2, 2); // numero de facturaActual
                tblFacturasPorCliente.setValueAt(obj.getNombreDeCliente(), filaTabla2, 3); // numero de facturaActual
                tblFacturasPorCliente.setValueAt((obj.getDireccion()), filaTabla2, 4); // numero de facturaActual
                tblFacturasPorCliente.setValueAt(obj.getNombreTipoDeMovimiento(), filaTabla2, 5); // numero de facturaActual
                tblFacturasPorCliente.setValueAt(nf.format(obj.getValorTotalFactura()), filaTabla2, 6); // numero de facturaActual

            }

        }else{
             JOptionPane.showMessageDialog(this, "Cliente no presenta movimientos en el sistema", "Error", JOptionPane.WARNING_MESSAGE);
        }

    }
    
     public synchronized void llenarTablaProductosPorFacturaRechazados() throws Exception {
       // listaDeProductosRechazados = this.facturaActual.getVstlistaDeProductosRechazados();

        if (listaDeProductosRechazados != null) {
            System.out.println(new Date() + " acá se calcula la cantidad de productos rechazados =" + listaDeProductosRechazados.size());

            DefaultTableModel modelo = (DefaultTableModel) tblProductosPorFacturaRechazados.getModel();

            for (Vst_ProductosPorFacturaDescargados obj : listaDeProductosRechazados) {
                filaTabla2 = tblProductosPorFacturaRechazados.getRowCount();

                modelo.addRow(new Object[tblProductosPorFacturaRechazados.getRowCount()]);

                tblProductosPorFacturaRechazados.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblProductosPorFacturaRechazados.setValueAt(obj.getCodigoProducto(), filaTabla2, 1); // numero de manifiesto
                tblProductosPorFacturaRechazados.setValueAt(obj.getDescripcionProducto(), filaTabla2, 2); // fecha de distribucion
                tblProductosPorFacturaRechazados.setValueAt(obj.getCantidadRechazada(), filaTabla2, 3); // placa del vehiculo
                tblProductosPorFacturaRechazados.setValueAt(obj.getNombreCausalDeRechazo(), filaTabla2, 4); // nombre del conductor

            }
        }

    }
     private void salir(){
       
        this.dispose();
        this.setVisible(false);
     }

}
