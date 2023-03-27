/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios;

import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.distribucion.Threads.HiloGuardarManifiestoDescargado;
import aplicacionlogistica.distribucion.Threads.HiloGuardarRegistro;
import aplicacionlogistica.distribucion.Threads.hiloArmararchivoProductosPorFacturaDescargados;
import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.distribucion.PrincipalLogistica;
import aplicacionlogistica.distribucion.Threads.HiloGuardarManifiestoDescargado100;
import aplicacionlogistica.distribucion.Threads.HiloLiberarManifiesto;
import aplicacionlogistica.distribucion.consultas.FBuscarManifiestos;
import aplicacionlogistica.distribucion.imprimir.ReporteDescargueDeFacturas1;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import aplicacionlogistica.distribucion.objetos.CFacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CMovimientosManifiestosfacturas;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CRecogidasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CSoportesConsignaciones;
import aplicacionlogistica.distribucion.consultas.minutas.MinutasDeDistribucion;
import aplicacionlogistica.distribucion.imprimir.ReporteMinutaDeDescargueDeRuta;
import aplicacionlogistica.distribucion.objetos.CCuentasBancarias;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CEntidadesBancarias;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import mtto.vehiculos.CCarros;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Eduardo López Casanova
 */
public final class DescargarFacturas extends javax.swing.JInternalFrame {

    public CManifiestosDeDistribucion manifiestoActual = null;
    public CCarros carro = null;
    public CFacturasPorManifiesto facturaActual = null;
    public Double valorTotalRecaudado = 0.0;
    public Double valorTotalRecogidas = 0.0;
    public Double valorTotalAConsignar = 0.0;
    double valorRecogida;
    double valorDescontado;
    public Double valorConsignado = 0.0;
    public boolean isFormularioCompleto = false;
    boolean cargado = false;
    boolean nuevo = false;
    boolean actualizar = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean estaOcupadoGrabando = false;
    public boolean isFacturaCredito = false;
    public boolean esteManifiestoEsMio = false;
    public int kilometrosRecorridos;
    public int filaSeleccionada;
    int indiceLista = 0;
    int columna = 0;
    String mensaje = null;
    public int causalrechazo = 0;
    String rutaDeArchivoFacturasDescargadas;
    String rutaDeArchivoProductosDescargados;
    String rutaDeArchivoRecogidasDescargado;
    String rutaDeArchivoFacturasParaVolverAZonificar;
    String rutaDeArchivoSoporteConsignaciones;
    public int tipoMovimientoFactura = 0;
    public int indiceCbx;
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
    FBuscarManifiestos fBuscarManifiestos;

    TextAutoCompleter autoTxtNumeroManifiesto;

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
    public DescargarFacturas(PrincipalLogistica form) {
        this.form = form;
        this.ini = form.ini;
        CargarVista();
    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param ini
     */
    public DescargarFacturas(Inicio ini) {
        this.ini = ini;

        initComponents();
        cbxMovimientoFactura.removeAllItems();
        cbxCausalDeRechazoFactura.removeAllItems();
        cbxEntidadesBancarias.removeAllItems();
        cbxNumeroDeCuenta.removeAllItems();
        CbxPrefijo.removeAllItems();

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
            CbxPrefijo.addItem(arrSplit[i].replace("'", ""));
        }

        CargarVista();
    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     */
    public void CargarVista() {
        try {
            try {

                manifiestoActual = null;

                lblCirculoDeProgreso.setVisible(false);

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
                Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }
            cargado = true;
            for (CEntidadesBancarias obj : ini.getListaDeEntidadesBancarias()) {
                cbxEntidadesBancarias.addItem(obj.getNombreEntidadBancaria());
            }

            if (this.ini.getListaDeManifiestossinDescargar() != null) {
                this.autoTxtNumeroManifiesto = new TextAutoCompleter(txtNumeroManifiesto);
                for (CManifiestosDeDistribucion manifiesto : this.ini.getListaDeManifiestossinDescargar()) {

                    if (manifiesto.getEstadoManifiesto() != 4) {
                        autoTxtNumeroManifiesto.addItem(manifiesto.getNumeroManifiesto() + "");
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

        fDescargueEntregaTotalConDescuento = new FDescargarEntregasTotalesConDescuento();
        fDescargueRechazosParciales = new FDescargarRechazosParciales();
        fBuscarManifiestos = new FBuscarManifiestos(ini);
        jBtnMinuta.setEnabled(false);

        //this.autoTxtNumeroManifiesto = new TextAutoCompleter(txtNumeroManifiesto);
    }

    /**
     * This method is called from within the constructor to initialize the
     * fCambiarClave. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        borrarFactura = new javax.swing.JPopupMenu();
        mnuBorrarFacturas = new javax.swing.JMenu();
        mnuBorraUnaFactura = new javax.swing.JMenuItem();
        mnuBorraTodasLasFacturas = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        borrarRecogida = new javax.swing.JPopupMenu();
        mnuBorrarRecogida = new javax.swing.JMenuItem();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
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
        jButton1 = new javax.swing.JButton();
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
        jPanel8 = new javax.swing.JPanel();
        lblKilometrosRecorridos = new javax.swing.JLabel();
        txtNumeroManifiesto = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbxMovimientoFactura = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        txtNumeroDeFactura = new javax.swing.JTextField();
        lblValorFactura = new org.edisoncor.gui.label.LabelCustom();
        jLabel38 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtKilometrosEntrada = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtKilometrosSalida = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        lblFacturasPendientes = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        chkDescuento = new javax.swing.JCheckBox();
        rBtnCredito = new javax.swing.JRadioButton();
        rBtnContado = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListFacturasNoManifestadas = new javax.swing.JList();
        jLabel41 = new javax.swing.JLabel();
        txtNombreConductor = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtRuta = new javax.swing.JTextField();
        jProgressBar1 = new javax.swing.JProgressBar();
        CbxPrefijo = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnGrabar100 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFacturasPorVehiculo = new javax.swing.JTable();
        lblIdentificadorManifiesto = new javax.swing.JLabel();
        lblValorRecaudoManifiesto = new org.edisoncor.gui.label.LabelCustom();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnGrabar100 = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnBorrarFila = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();

        mnuBorrarFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delete.png"))); // NOI18N
        mnuBorrarFacturas.setText("Borrar Registros");
        mnuBorrarFacturas.setToolTipText("");
        mnuBorrarFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuBorrarFacturasMouseClicked(evt);
            }
        });
        mnuBorrarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuBorrarFacturasActionPerformed(evt);
            }
        });

        mnuBorraUnaFactura.setText("Borrar una Factura");
        mnuBorraUnaFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuBorraUnaFacturaMouseClicked(evt);
            }
        });
        mnuBorraUnaFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuBorraUnaFacturaActionPerformed(evt);
            }
        });
        mnuBorrarFacturas.add(mnuBorraUnaFactura);

        mnuBorraTodasLasFacturas.setText("Borrar Facturas");
        mnuBorraTodasLasFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuBorraTodasLasFacturasMouseClicked(evt);
            }
        });
        mnuBorraTodasLasFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuBorraTodasLasFacturasActionPerformed(evt);
            }
        });
        mnuBorrarFacturas.add(mnuBorraTodasLasFacturas);

        borrarFactura.add(mnuBorrarFacturas);

        mnuBorrarRecogida.setText("Borrar Recogida");
        mnuBorrarRecogida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuBorrarRecogidaMouseClicked(evt);
            }
        });
        mnuBorrarRecogida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuBorrarRecogidaActionPerformed(evt);
            }
        });
        borrarRecogida.add(mnuBorrarRecogida);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Formulario para Descargar las facturas del  manifiestos de Distribución");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(1322, 761));
        setMinimumSize(new java.awt.Dimension(111400, 32));
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

        jPanel5.setMaximumSize(new java.awt.Dimension(1322, 761));

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlEntregasLayout.setVerticalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        jTabbedPane1.addTab("Entregas Totales", pnlEntregas);

        pnlRechazosTotales.setAutoscrolls(true);
        pnlRechazosTotales.setDoubleBuffered(false);
        pnlRechazosTotales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Causal de Devolucion");
        pnlRechazosTotales.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, -1, -1));

        cbxCausalDeRechazoFactura.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxCausalDeRechazoFactura.setEnabled(false);
        cbxCausalDeRechazoFactura.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCausalDeRechazoFacturaItemStateChanged(evt);
            }
        });
        pnlRechazosTotales.add(cbxCausalDeRechazoFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 300, -1));

        btnDescargarRechazoTotal.setText("Descargar");
        btnDescargarRechazoTotal.setEnabled(false);
        btnDescargarRechazoTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarRechazoTotalActionPerformed(evt);
            }
        });
        pnlRechazosTotales.add(btnDescargarRechazoTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 100, -1, -1));

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
        jTableRecogidas.setComponentPopupMenu(borrarRecogida);
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
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(btnDescargarRechazoTotal1)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Recogidas", pnlRecogidas);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/OK.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                        .addGap(14, 14, 14)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cbxNumeroDeCuenta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNumeroSoporteConsignacion, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                            .addComponent(txtValorConsignacion, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(dateOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMedioDePago, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxEntidadesBancarias, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4))
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblValorConsignado, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)))
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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Descargar facturas"));

        lblKilometrosRecorridos.setBackground(new java.awt.Color(204, 204, 255));
        lblKilometrosRecorridos.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKilometrosRecorridos.setBorder(new org.edisoncor.gui.util.DropShadowBorder());

        txtNumeroManifiesto.setEnabled(false);
        txtNumeroManifiesto.setInheritsPopupMenu(true);
        txtNumeroManifiesto.setName("numerico"); // NOI18N
        txtNumeroManifiesto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroManifiestoFocusGained(evt);
            }
        });
        txtNumeroManifiesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroManifiestoActionPerformed(evt);
            }
        });
        txtNumeroManifiesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroManifiestoKeyPressed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText(" Movimiento");

        cbxMovimientoFactura.setEnabled(false);
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
        cbxMovimientoFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxMovimientoFacturaKeyPressed(evt);
            }
        });

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Número de Factura");

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

        lblValorFactura.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValorFactura.setText("$ 0.0");
        lblValorFactura.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lblValorFactura.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Placa Vehiculo");

        txtPlaca.setEditable(false);
        txtPlaca.setEnabled(false);
        txtPlaca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPlacaFocusGained(evt);
            }
        });
        txtPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlacaActionPerformed(evt);
            }
        });
        txtPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPlacaKeyPressed(evt);
            }
        });

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Km. Salida");

        txtKilometrosEntrada.setEditable(false);
        txtKilometrosEntrada.setEnabled(false);
        txtKilometrosEntrada.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKilometrosEntradaFocusGained(evt);
            }
        });
        txtKilometrosEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKilometrosEntradaActionPerformed(evt);
            }
        });
        txtKilometrosEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKilometrosEntradaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKilometrosEntradaKeyReleased(evt);
            }
        });

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("# Manifiesto");

        txtKilometrosSalida.setEditable(false);
        txtKilometrosSalida.setEnabled(false);
        txtKilometrosSalida.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKilometrosSalidaFocusGained(evt);
            }
        });
        txtKilometrosSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKilometrosSalidaActionPerformed(evt);
            }
        });
        txtKilometrosSalida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKilometrosSalidaKeyPressed(evt);
            }
        });

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Km. Entrada");

        lblFacturasPendientes.setFont(new java.awt.Font("Dialog", 1, 40)); // NOI18N
        lblFacturasPendientes.setText(".");
        lblFacturasPendientes.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblFacturasPendientesMouseMoved(evt);
            }
        });

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        chkDescuento.setText("Con descuento");
        chkDescuento.setEnabled(false);
        chkDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDescuentoActionPerformed(evt);
            }
        });

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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        );

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Conductor");

        txtNombreConductor.setEditable(false);
        txtNombreConductor.setEnabled(false);
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

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Ruta");

        txtRuta.setEditable(false);
        txtRuta.setEnabled(false);
        txtRuta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRutaFocusGained(evt);
            }
        });
        txtRuta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRutaActionPerformed(evt);
            }
        });
        txtRuta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRutaKeyPressed(evt);
            }
        });

        CbxPrefijo.setToolTipText("");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addComponent(txtNumeroManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addComponent(txtKilometrosEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel39)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtKilometrosSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lblKilometrosRecorridos, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbxMovimientoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(CbxPrefijo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                            .addComponent(lblValorFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(56, 56, 56))))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblFacturasPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(chkDescuento)
                                .addGap(18, 18, 18)
                                .addComponent(rBtnContado)
                                .addGap(18, 18, 18)
                                .addComponent(rBtnCredito)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtNumeroManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKilometrosEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKilometrosSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(lblKilometrosRecorridos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxMovimientoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(1, 1, 1)
                        .addComponent(lblValorFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rBtnCredito)
                            .addComponent(rBtnContado)
                            .addComponent(chkDescuento))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CbxPrefijo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFacturasPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

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
        btnGrabar.setToolTipText("Guardar registro nuevo ó modificado");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnGrabar100, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGrabar100, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableFacturasPorVehiculo.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableFacturasPorVehiculo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableFacturasPorVehiculo.setColumnSelectionAllowed(true);
        jTableFacturasPorVehiculo.setComponentPopupMenu(borrarFactura);
        jTableFacturasPorVehiculo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFacturasPorVehiculoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableFacturasPorVehiculo);
        jTableFacturasPorVehiculo.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTableFacturasPorVehiculo.getColumnModel().getColumnCount() > 0) {
            jTableFacturasPorVehiculo.getColumnModel().getColumn(0).setResizable(false);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(1).setResizable(false);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(2).setResizable(false);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(2).setPreferredWidth(250);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(3).setResizable(false);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(3).setPreferredWidth(60);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(4).setResizable(false);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(4).setPreferredWidth(120);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        lblIdentificadorManifiesto.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
        lblIdentificadorManifiesto.setText("Total a recaudar en Manifiesto # ");

        lblValorRecaudoManifiesto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValorRecaudoManifiesto.setText("$ 0.0");
        lblValorRecaudoManifiesto.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lblValorRecaudoManifiesto.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblIdentificadorManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblValorRecaudoManifiesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(5, 5, 5)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblIdentificadorManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblValorRecaudoManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
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

        jBtnBorrarFila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/table_row_delete.png"))); // NOI18N
        jBtnBorrarFila.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnBorrarFila.setEnabled(false);
        jBtnBorrarFila.setFocusable(false);
        jBtnBorrarFila.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnBorrarFila.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnBorrarFila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBorrarFilaActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnBorrarFila);

        jBtnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnCancel.setFocusable(false);
        jBtnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnCancel);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();

    }//GEN-LAST:event_btnSalirActionPerformed

    public void salir() {
        if (manifiestoActual != null) {

            if (esteManifiestoEsMio) {
                new Thread(new HiloLiberarManifiesto(manifiestoActual, true)).start();//-->Error v 
                //manifiestoActual.liberarManifiesto(true);
                //manifiestoActual = null;
                esteManifiestoEsMio = false;
                limpiar();
            }
        }

        btnImprimir.setEnabled(false);
        cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
        cbxMovimientoFactura.setSelectedItem("NINGUNO");
        this.dispose();
        this.setVisible(false);
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        nuevo();

    }//GEN-LAST:event_btnNuevoActionPerformed

    public void nuevo() {
        cancelar();

        nuevo = true;
        pnlRechazosTotales.setEnabled(true);

        txtNumeroManifiesto.setEnabled(true);
        txtNumeroManifiesto.setEditable(true);
        jTabbedPane1.setEnabled(false);
        btnNuevo.setEnabled(false);
        jBtnNuevo.setEnabled(false);
        txtNumeroManifiesto.requestFocus();
        cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
        cbxMovimientoFactura.setSelectedItem("NINGUNO");

        dateOperacion.setEnabled(true);
        jButton1.setEnabled(true);
        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);

        btnNuevo.setEnabled(false);
        jBtnNuevo.setEnabled(false);

        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);

        jBtnMinuta.setEnabled(false);

        this.autoTxtNumeroManifiesto = new TextAutoCompleter(txtNumeroManifiesto);
        for (CManifiestosDeDistribucion manifiesto : this.ini.getListaDeManifiestossinDescargar()) {

            if (manifiesto.getEstadoManifiesto() != 4) {
                autoTxtNumeroManifiesto.addItem(manifiesto.getNumeroManifiesto() + "     ");
            }
        }
    }

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed

        if (grabar()) {
            return;
        }

    }//GEN-LAST:event_btnGrabarActionPerformed

    public boolean grabar() throws HeadlessException {
        /*Se valida que lista de Facturas Descargadas este completa*/
        if (manifiestoActual.getListaFacturasPendientesPorDescargar().size() > 0) {
            JOptionPane.showMessageDialog(this, "No se han descargado todas las facturas", "Error", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        if (valorConsignado < valorTotalAConsignar) {
//          JOptionPane.showMessageDialog(this, "No se puede guardar manifiesto,\n "
//                  + "el valor consignado es menor al valor de la ruta ", "Error en la consignacion", JOptionPane.ERROR_MESSAGE, null);
//          return;
        }
        /* Se valida que el manifiesto se encuentre en estado = 3 osea en distribución*/
        if (manifiestoActual.getEstadoManifiesto() == 3) {
            int deseaGrabar = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

            /* Se valida el deseo de grabar los datos en la BBDD  */
            if (deseaGrabar == JOptionPane.OK_OPTION) {

                /* Se veirifica sí hay conexion a Internet */
                if (CConexiones.hayConexion(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota())) {

                    try {

                        cbxMovimientoFactura.setEnabled(false);
                        chkDescuento.setEnabled(false);
                        chkDescuento.setSelected(false);
                        rBtnContado.setEnabled(false);
                        rBtnCredito.setEnabled(false);
                        txtNumeroDeFactura.setEditable(false);
                        btnGrabar.setEnabled(false);
                        jBtnGrabar.setEnabled(false);
                        cerrarManifiesto();

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error al guardar los datos " + e, "Error", JOptionPane.ERROR_MESSAGE, null);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar los datos, no hay Internet  ", "Error al grabar ", JOptionPane.ERROR_MESSAGE, null);
                }

            }

        } else {
            JOptionPane.showMessageDialog(this, "Este manifiesto no se puede guardar . Verificar con el administrador", "Error al grabar ", JOptionPane.ERROR_MESSAGE, null);
        }
        return false;
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        cancelar();


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cancelar() {
        if (manifiestoActual != null) {

            if (esteManifiestoEsMio) {
                new Thread(new HiloLiberarManifiesto(manifiestoActual, true)).start();//-->Error v 
                //manifiestoActual.liberarManifiesto(true);
                //manifiestoActual = null;
                esteManifiestoEsMio = false;
                jBtnMinuta.setEnabled(false);
                jProgressBar1.setValue(0);

                lblCirculoDeProgreso.setVisible(false);
                btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            }

        }
        this.autoTxtNumeroManifiesto = new TextAutoCompleter(txtNumeroManifiesto);
        for (CManifiestosDeDistribucion manifiesto : this.ini.getListaDeManifiestossinDescargar()) {

            if (manifiesto.getEstadoManifiesto() != 4) {
                autoTxtNumeroManifiesto.addItem(manifiesto.getNumeroManifiesto() + "     ");
            }
        }
        limpiar();
    }

    private void jTableFacturasPorVehiculoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFacturasPorVehiculoMouseClicked
        jBtnBorrarFila.setEnabled(true);
    }//GEN-LAST:event_jTableFacturasPorVehiculoMouseClicked

    private void txtNumeroDeFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaFocusGained
        txtNumeroDeFactura.setSelectionStart(0);
        txtNumeroDeFactura.setSelectionEnd(txtNumeroDeFactura.getText().length());
    }//GEN-LAST:event_txtNumeroDeFacturaFocusGained

    private void txtNumeroDeFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaKeyPressed

        try {

            if ((evt.getKeyCode() == KeyEvent.VK_F2) && (txtNumeroDeFactura.getText().length() > 0)) {

                if (verificarQueFacturaExiste()) {
                    consultarLafactura();
                }

            }

            /* Evento al oprimir la tecla enter*/
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) && (txtNumeroDeFactura.getText().length() > 0)) {

                btnGrabar100.setEnabled(false);
                jBtnGrabar100.setEnabled(false);
                jBtnBorrarFila.setEnabled(false);
                this.facturaActual = null;

                String numeroDeFactura;

                numeroDeFactura = CbxPrefijo.getSelectedItem().toString() + txtNumeroDeFactura.getText().trim();
                //numeroDeFactura = txtNumeroDeFactura.getText().trim();

                if (rBtnContado.isSelected()) {
                    isFacturaCredito = false;
                } else {
                    isFacturaCredito = true;
                }

                jTabbedPane1.setEnabled(true);
                txtNumeroDeFactura.setSelectionStart(0);
                txtNumeroDeFactura.setSelectionEnd(txtNumeroDeFactura.getText().length());
                try {

                    limpiarTblProductosPorFactura();

                    // SE CREA EL OBJETO FACTURA
                    for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
                        if (obj.getNumeroFactura().equals(numeroDeFactura)) {

                            /*Encuentra la factura en el listado del manifiesto*/
                            facturaActual = obj;
                            break;
                        }
                    }

                    // SE VALIDA QUE LA FACTURA EXISTA EN EL MANIFIESTO
                    if (facturaActual != null) {

                        if (!facturaActual.getNumeroDescuento().equals("0")) {
                            tipoMovimientoFactura = 2;
                            chkDescuento.setSelected(true);
                        }

                        /*consulta local de los de los productos de la factura para llenar
                        la tabla de los productos de la factura*/
                        if (facturaActual.getListaProductosPorFactura().size() > 0) {

                            // SE LLENA LA TABLA DE PRODUCTOS CONTENIDOS EN LA FACTURA
                            llenarTablaProductosPorFactura();

                            // SE LLENA LAS RESPECTIVAS ETIQUETAS
                            lblValorFactura.setText(nf.format(facturaActual.getValorTotalFactura()));

                            // SE VALIDA QUE SE HAYA ELEGIDO UN MOVIMIENTO PARA LA FACTURA
                            //if (!"NINGUNO".equals(cbxMovimientoFactura.getSelectedItem().toString())) {
                            if (cbxMovimientoFactura.getSelectedIndex() > 0) {

                                switch (tipoMovimientoFactura) {
                                    case 1: // caso no utilizado, pues identifica un movimiento no valido
                                        /* NINGUNO */
                                        break;

                                    // DESCARGUE DE TIPO DE MOVIMIENTO ENTREGA TOTAL
                                    case 2:
                                        facturaActual.setIdTipoDeMovimiento(2);

                                        if (!estaDescargadaLaFactura(numeroDeFactura)) {

                                            /*Utilizando el objeto Cfacturas se bloqua la factura para que no pueda ser anexada a otro manifiesto*/
                                            //facturaTemporal.liberarFactura(false);

                                            /*Se activa cuando hay descuento en la facura*/
                                            if (chkDescuento.isSelected()) {

                                                fDescargueEntregaTotalConDescuento.toFront();
                                                fDescargueEntregaTotalConDescuento.setClosable(true);
                                                fDescargueEntregaTotalConDescuento.setVisible(true);
                                                fDescargueEntregaTotalConDescuento.setTitle("Formulario para Descargar entregas Totales con Descuentos factura #" + facturaActual.getNumeroFactura());
                                                fDescargueEntregaTotalConDescuento.setLocation((ini.getDimension().width - fDescargueEntregaTotalConDescuento.getSize().width) / 2, (ini.getDimension().height - fDescargueEntregaTotalConDescuento.getSize().height) / 2);
                                                fDescargueEntregaTotalConDescuento.fDescargarFacturas = this;

                                                fDescargueEntregaTotalConDescuento.llenarFormulario();
                                                this.getDesktopPane().add(fDescargueEntregaTotalConDescuento);
                                                CbxPrefijo.setEnabled(true);
                                                txtNumeroDeFactura.setEnabled(true);
                                                fDescargueEntregaTotalConDescuento.toFront();

                                                fDescargueEntregaTotalConDescuento.txtPorcentajeDescuento.requestFocus();

                                                //fDescargueEntregaTotalConDescuento.show();
                                            } else {
                                                descargarEntregasTotales();
                                            }
                                            //txtNumeroDeFactura.requestFocus();
                                        }
                                        break;

                                    // DESCARGUE DE RECHAZO TOTAL
                                    case 3:
                                        facturaActual.setIdTipoDeMovimiento(3);
                                        if (!estaDescargadaLaFactura(numeroDeFactura)) {

                                            chkDescuento.setEnabled(false);
                                            chkDescuento.setSelected(false);

                                        }

                                        break;
                                    case 4: //DESCARGUE DE RECHAZO PARCIAL
                                        facturaActual.setIdTipoDeMovimiento(4);

                                        chkDescuento.setEnabled(true);
                                        jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlRechazosParciales), true);
                                        jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlRechazosParciales));

                                        if (!estaDescargadaLaFactura(numeroDeFactura)) {
                                            fDescargueRechazosParciales = new FDescargarRechazosParciales(this, 1);
                                            fDescargueRechazosParciales.setClosable(true);
                                            fDescargueRechazosParciales.setTitle("Formulario para Descargar Entregas parciales con y sin Descuentos");
                                            fDescargueRechazosParciales.setLocation((ini.getDimension().width - fDescargueRechazosParciales.getSize().width) / 2, (ini.getDimension().height - fDescargueRechazosParciales.getSize().height) / 2);
                                            this.getDesktopPane().add(fDescargueRechazosParciales);
                                            CbxPrefijo.setEnabled(false);
                                            txtNumeroDeFactura.setEnabled(false);
                                            fDescargueRechazosParciales.setVisible(true);
                                            fDescargueRechazosParciales.toFront();
                                        }

                                        break;

                                    case 5: //  DESCARGUE DE RECOGIDAS
                                        facturaActual.setIdTipoDeMovimiento(5);

                                        if (!estaDescargadaRecogida(numeroDeFactura)) {

                                            chkDescuento.setEnabled(false);
                                            jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlRecogidas), true);
                                            jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlRecogidas));

                                            txtNumeroDeSoporteRecogida.setEditable(true);
                                            txtFacturaAfectada.setEditable(true);
                                            txtValorRecogida.setEditable(true);
                                            txtValorDescontado.setEditable(true);

                                            txtNumeroDeSoporteRecogida.setEnabled(true);
                                            txtFacturaAfectada.setEnabled(true);
                                            txtValorRecogida.setEnabled(true);
                                            txtValorDescontado.setEnabled(true);

                                            txtNumeroDeSoporteRecogida.requestFocus();
                                        } else {
                                            JOptionPane.showInternalMessageDialog(this, "La Recogida aparece descargada del Mfto. N° " + manifiestoActual.codificarManifiesto(), "Error", 0);
                                        }

                                        break;

                                    //  RE ENVIOS
                                    case 6:
                                        facturaActual.setIdTipoDeMovimiento(6);
                                        // SE VALIDA SI LA FACTURA NO ESTA DESCARGADA Y SE DESCARGA LA ENTREGA TOTAL

                                        if (!estaDescargadaLaFactura(numeroDeFactura)) {
                                            //       facturaActual.liberarFactura(false);

                                            this.descargarReEnvios();
                                            txtNumeroDeFactura.requestFocus();

                                        }
                                        break;

                                    // NO VISITADOS
                                    case 7:
                                        facturaActual.setIdTipoDeMovimiento(7);
                                        // SE VALIDA SI LA FACTURA NO ESTA DESCARGADA Y SE DESCARGA LA ENTREGA TOTAL

                                        if (!estaDescargadaLaFactura(numeroDeFactura)) {
//                                            facturaTemporal.liberarFactura(false);

                                            this.descargarNoVisitados();
                                            txtNumeroDeFactura.requestFocus();

                                        }
                                        break;

                                }

                                valorTotalAConsignar = valorTotalRecaudado - valorTotalRecogidas;

                                lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

                                //   } /* Fin if valida si las (cantidadFacturasPendientesPorDescargar > 0) */
                            } else {
                                JOptionPane.showInternalMessageDialog(this, "Debe escoger un movimiento para la factura digitada ", "Error", 0);
                                //txtNumeroDeFactura.requestFocus();
                            }
                            //txtNumeroDeFactura.requestFocus();
                        } else {

                            JOptionPane.showInternalMessageDialog(this, "La factura no tiene productos asignadosto", "Error", JOptionPane.ERROR_MESSAGE);
                            // txtNumeroDeFactura.requestFocus();
                        }
                        /* Cuando la factura no existe en el manifiesto */
                    } else {
                        if (listaDeFacturasNoManifestadas == null) {
                            listaDeFacturasNoManifestadas = new ArrayList<>();
                        }
                        /*Se valida que el dato no exista en el Jlist, ni en el archivo*/
                        if (!verificarQueFacturaExiste()) {
                            listaDeFacturasNoManifestadas.add(numeroDeFactura);

                            /*Agrega el numero de factura al objeto JList*/
                            anexarFacturaActualALalista();

                            /*Agrega la factura al fichero*/
                            ArchivosDeTexto archivo = new ArchivosDeTexto();
                            archivo.insertarLineaEnFichero(numeroDeFactura, manifiestoActual.getRutArchivoFacturasSinManifestar());
                            JOptionPane.showInternalMessageDialog(this, "La factura No existe en el manifiesto N° " + manifiestoActual.codificarManifiesto(), "Factura no existe en el sistema", JOptionPane.ERROR_MESSAGE);                //txtNumeroDeFactura.requestFocus();
                        }

                    }
                } catch (Exception ex) {
                    System.out.println("Error en txtNumeroDeFacturaKeyPressed ");
                    Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
                    //txtNumeroDeFactura.requestFocus();
                }

                txtNumeroDeFactura.requestFocus();
            }
        } catch (Exception ex) {
            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_txtNumeroDeFacturaKeyPressed

    private void consultarLafactura() {
        facturaActual = null;
        try {
            // CFacturas fac = new CFacturas(ini, txtNumeroDeFactura.getText().trim());
            // SE CREA EL OBJETO FACTURA
            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
                if (obj.getNumeroFactura().equals(txtNumeroDeFactura.getText().trim())) {
                    facturaActual = obj;

                    break;
                }
            }
            if (facturaActual != null) {

                FConsultarFacturasRemoto formularios = new FConsultarFacturasRemoto(ini, new CFacturas(ini, facturaActual.getNumeroFactura()));
                this.getDesktopPane().add(formularios);
                formularios.toFront();
                formularios.setClosable(true);
                formularios.setVisible(true);
                formularios.setTitle("Formulario para consultar Facturas");
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                //form.setSize(PrincipalLogistica.escritorio.getSize());
                formularios.setLocation((screenSize.width - formularios.getSize().width) / 2, (screenSize.height - formularios.getSize().height) / 2);
                formularios.show();
            } else {
                JOptionPane.showInternalMessageDialog(this, "La factura no aparece en el manifiesto", "Factura no visible ", 2);
            }

        } catch (Exception ex) {
            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llenarTablaProductosPorFactura() {
        modelo1 = (DefaultTableModel) jTableProductosPorFactura.getModel();

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

    /**
     * Método encargado de realizar el descargue de las entregas totales de una
     * ruta
     *
     * @param facturaDescargada corresponde al numero de la factura que salió a
     * distribución
     *
     */
    private void descargarEntregasTotales() throws Exception {

        if (!isFacturaCredito) {

            facturaActual.setValorRecaudado(facturaActual.getValorTotalFactura());
            facturaActual.setTipoDeDEscargue("E. T. Cn");

            // SI LA FACTURA ES DE CREDITO
        } else {

            facturaActual.setValorRecaudado(0.0);
            facturaActual.setTipoDeDEscargue("E. T. Cr");

        }

        facturaActual.setAdherenciaDescargue(manifiestoActual.getListaFacturasDescargadas().size() + 1);
        facturaActual.setValorRechazo(0.0);
        facturaActual.setValorDescuento(0.0);
        //facturaActual.setValorRecaudado(facturaActual.getValorTotalFactura());
        facturaActual.setIdTipoDeMovimiento(2);
        facturaActual.setCausalDeRechazo(1);
        facturaActual.setActivo(1);

        /*Se añade la factura a la lista de facturas a Descargar*/
        manifiestoActual.getListaFacturasDescargadas().add(facturaActual);

        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

        // se anexa el registro a la tabla de facturas por numeroDeManifiesto
        filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

        modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
        jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getTipoDeDEscargue(), filaSeleccionada, 3); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt(nf.format(facturaActual.getValorRecaudado()), filaSeleccionada, 4);

        // se ubica en la fila insertada
        jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

        //rutaDeArchivoFacturasDescargadas = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_FacturasDescargados.txt";
        rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
        // ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + ".txt";
        senteciaSqlFacturasDescargadas = facturaActual.getCadenaConLosCamposParaDescargue();

        Inicio.GuardaConsultaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);

        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPendientesPorDescargar()) {

            if (obj.getNumeroFactura().equals(facturaActual.getNumeroFactura())) {

                /*Se elimina la factura de la lista pendientes por descargar*/
                manifiestoActual.getListaFacturasPendientesPorDescargar().remove(obj);
                break;
            }
        }

        calcularValoraAConsignar();

        lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

        lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());

        if (manifiestoActual.getListaFacturasPendientesPorDescargar().isEmpty()) {
            btnGrabar.setEnabled(true);
            jBtnGrabar.setEnabled(true);
            cbxCausalDeRechazoFactura.setEnabled(false);
            btnDescargarRechazoTotal.setEnabled(false);
            chkDescuento.setSelected(false);
            chkDescuento.setEnabled(false);

            btnGrabar.requestFocus();
        }

        // new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();//-->Error v 
        if (manifiestoActual.getListaFacturasPorManifiesto().size() == manifiestoActual.getListaFacturasDescargadas().size()) {
            if (valorTotalAConsignar >= valorConsignado) {
                btnGrabar.setEnabled(true);
                jBtnGrabar.setEnabled(true);
            }
        }

        txtNumeroDeFactura.requestFocus();

    }

    /**
     * Método encargado de realizar el descargue de las entregas totales de una
     * ruta
     *
     *
     */
    public void descargarEntregasTotalesConDescuento() throws Exception {

        facturaActual.setAdherenciaDescargue(manifiestoActual.getListaFacturasDescargadas().size() + 1);

        /*Se añade la factura a la lista de facturas a Descargar*/
        manifiestoActual.getListaFacturasDescargadas().add(facturaActual);

        // SI LA FACTURA ES DE CONTADO
        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

        // se anexa el registro a la tabla de facturas por numeroDeManifiesto
        filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

        modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
        jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt("E. T. %", filaSeleccionada, 3); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt(nf.format(facturaActual.getValorRecaudado()), filaSeleccionada, 4);

        // se ubica en la fila insertada
        jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

        rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
        senteciaSqlFacturasDescargadas = facturaActual.getCadenaConLosCamposParaDescargue();

        //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
        ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturasDescargadas);

        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        //archivo.insertarConsultaEnFichero(senteciaSqlFacturasDescargadas, ruta);
        archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);

        // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPendientesPorDescargar()) {
            //aqui
            if (obj.getNumeroFactura().equals(facturaActual.getNumeroFactura())) {

                /*Se elimina la factura de la lista pendientes por descargar*/
                manifiestoActual.getListaFacturasPendientesPorDescargar().remove(obj);
                break;
            }
        }

        calcularValoraAConsignar();

        lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

        // armarArchivoProductosPorFacturadescargados(facturaDescargada);
        //new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this, 2)).start(); //--> error
        lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());
        rBtnContado.setSelected(true);
        //chkDescuento.setSelected(false);

        if (manifiestoActual.getListaFacturasPendientesPorDescargar().isEmpty()) {
            btnGrabar.setEnabled(true);
            jBtnGrabar.setEnabled(true);
            cbxCausalDeRechazoFactura.setEnabled(false);
            btnDescargarRechazoTotal.setEnabled(false);
            chkDescuento.setSelected(false);
            chkDescuento.setEnabled(false);

            btnGrabar.requestFocus();
        }

        //new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();//-->Error v 
        if (manifiestoActual.getListaFacturasPorManifiesto().size() == manifiestoActual.getListaFacturasDescargadas().size()) {
            if (valorTotalAConsignar >= valorConsignado) {
                btnGrabar.setEnabled(true);
                jBtnGrabar.setEnabled(true);
            }
        }

        txtNumeroDeFactura.requestFocus();

    }

    /**
     * Método encargado de realizar el descargue de los rechazos totales de una
     * ruta
     *
     * @param facturaDescargada corresponde al numero de la factura que salió a
     * distribución
     *
     */
    private void descargarRechazosTotales() throws Exception {

        if (!estaDescargadaLaFactura(facturaActual.getNumeroFactura())) {

            facturaActual.setAdherenciaDescargue(manifiestoActual.getListaFacturasDescargadas().size() + 1);

            modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

            // se anexa el registro a la tabla de facturas por numeroDeManifiesto
            filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

            modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
            jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
            jTableFacturasPorVehiculo.setValueAt(facturaActual.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
            jTableFacturasPorVehiculo.setValueAt(facturaActual.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt("D. T.", filaSeleccionada, 3); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt(nf.format(0.0), filaSeleccionada, 4);

            // se ubica en la fila insertada
            jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

            causalrechazo = 0;

            for (CCausalesDeDevolucion obj : ini.getListaDeCausalesDeDevolucion()) {
                if (obj.getNombreCausalesDeDevolucion().equals(cbxCausalDeRechazoFactura.getSelectedItem())) {
                    causalrechazo = obj.getIdCausalesDeDevolucion();
                    facturaActual.setCausalDeRechazo(causalrechazo);
                    break;
                }
            }

            facturaActual.setValorRechazo(facturaActual.getValorTotalFactura());
            facturaActual.setValorDescuento(0.0);
            facturaActual.setValorRecaudado(0.0);
            facturaActual.setIdTipoDeMovimiento(3);
            facturaActual.setTipoDeDEscargue("D. T.");
            facturaActual.setCausalDeRechazo(causalrechazo);
            facturaActual.setActivo(1);

            manifiestoActual.getListaFacturasDescargadas().add(facturaActual);

            rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
            senteciaSqlFacturasDescargadas = facturaActual.getCadenaConLosCamposParaDescargue();

            //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
            ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturasDescargadas);

            // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
            //archivo.insertarConsultaEnFichero(senteciaSqlFacturasDescargadas, ruta);
            archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);

            // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPendientesPorDescargar()) {
                if (obj.getNumeroFactura().equals(facturaActual.getNumeroFactura())) {

                    /*Se elimina la factura de la lista pendientes por descargar*/
                    manifiestoActual.getListaFacturasPendientesPorDescargar().remove(obj);
                    break;
                }
            }

            calcularValoraAConsignar();

            lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));
            rBtnContado.setSelected(true);

            // armarArchivoProductosPorFacturadescargados(facturaDescargada);
            new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();

            lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());
            rBtnContado.setSelected(true);
            rBtnContado.setSelected(true);

            if (manifiestoActual.getListaFacturasPendientesPorDescargar().isEmpty()) {
                btnGrabar.setEnabled(true);
                jBtnGrabar.setEnabled(true);
                chkDescuento.setSelected(false);
                chkDescuento.setEnabled(false);
                cbxCausalDeRechazoFactura.setEnabled(false);
                btnDescargarRechazoTotal.setEnabled(false);
                btnGrabar.requestFocus();
            }

            if (manifiestoActual.getListaFacturasPorManifiesto().size() == manifiestoActual.getListaFacturasDescargadas().size()) {
                if (valorTotalAConsignar >= valorConsignado) {
                    btnGrabar.setEnabled(true);
                    jBtnGrabar.setEnabled(true);
                }
            }

            txtNumeroDeFactura.requestFocus();
        }
    }

    /**
     * Método encargado de realizar el descargue de los rechazos parciales de
     * una ruta
     *
     *
     * @throws java.lang.Exception
     */
    public void descargarRechazosParciales() throws Exception {

        if (!estaDescargadaLaFactura(facturaActual.getNumeroFactura())) {

            modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

            // se anexa el registro a la tabla de facturas por numeroDeManifiesto
            filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

            modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
            jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
            jTableFacturasPorVehiculo.setValueAt(facturaActual.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
            jTableFacturasPorVehiculo.setValueAt(facturaActual.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt(facturaActual.getTipoDeDEscargue(), filaSeleccionada, 3); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt(nf.format(facturaActual.getValorRecaudado()), filaSeleccionada, 4);
            // se ubica en la fila insertada
            jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

            /*Se añade la factura a la lista de facturas a Descargar*/
            manifiestoActual.getListaFacturasDescargadas().add(facturaActual);

            rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
            senteciaSqlFacturasDescargadas = facturaActual.getCadenaConLosCamposParaDescargue();

            //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
            ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturasDescargadas);

            // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
            archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);

            // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPendientesPorDescargar()) {
                boolean ex = false;
                if (obj.getNumeroFactura().equals(facturaActual.getNumeroFactura())) {

                    /*Se elimina la factura de la lista pendientes por descargar*/
                    manifiestoActual.getListaFacturasPendientesPorDescargar().remove(obj);
                    ex = true;

                }
                if (ex) {
                    break;
                }
            }


            /* Se calcula el valor recaudado */
            calcularValoraAConsignar();

            lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

            CbxPrefijo.setEnabled(true);
            txtNumeroDeFactura.setEnabled(true);

            lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());
            //rBtnContado.setSelected(true);

            // armarArchivoProductosPorFacturadescargados(facturaDescargada);
            new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();

            if (manifiestoActual.getListaFacturasPendientesPorDescargar().isEmpty()) {
                btnGrabar.setEnabled(true);
                jBtnGrabar.setEnabled(true);
                chkDescuento.setSelected(false);
                chkDescuento.setEnabled(false);
                cbxCausalDeRechazoFactura.setEnabled(false);
                btnDescargarRechazoTotal.setEnabled(false);
                btnGrabar.requestFocus();
            }

            if (manifiestoActual.getListaFacturasPorManifiesto().size() == manifiestoActual.getListaFacturasDescargadas().size()) {
                if (valorTotalAConsignar >= valorConsignado) {
                    btnGrabar.setEnabled(true);
                    jBtnGrabar.setEnabled(true);
                }
            }

            txtNumeroDeFactura.requestFocus();

        }

    }

    /**
     * Método encargado de realizar el descargue de las recogidas realizadas en
     * una ruta
     *
     * @param fac corresponde al numero de la factura que salió a distribución
     */
    protected void agregarRecogida(CFacturasPorManifiesto fac) {

        CRecogidasPorManifiesto rxm = new CRecogidasPorManifiesto(ini);

        rxm.setIdRecogidasPorManifiesto(fac.getConsecutivo());
        rxm.setIdNumeroManifiesto(fac.getNumeroManifiesto());
        rxm.setNumeroFactura(fac.getNumeroFactura());
        rxm.setFacturaAfectada(txtFacturaAfectada.getText().trim());
        rxm.setNumeroDeSoporte(txtNumeroDeSoporteRecogida.getText().trim());
        rxm.setValorRecogida(valorRecogida);
        rxm.setValorRecaudadoRecogida(valorDescontado);

        //rutaDeArchivoRecogidasDescargado = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_RecogidasDescargados.txt";
        rutaDeArchivoRecogidasDescargado = manifiestoActual.getRutArchivoRecogidasporManifiesto();

        senteciaSqlFacturasDescargadas = rxm.getCadenaConLosCampos();

        //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
        ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoRecogidasDescargado);

        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoRecogidasDescargado);

        valorTotalRecogidas += valorDescontado;

        valorTotalAConsignar = valorTotalRecaudado - valorTotalRecogidas;

        lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

        /*Se agrega la recogida a l lista repectiva*/
        manifiestoActual.getListaDeRecogidasPorManifiesto().add(rxm);
        //manifiestoActual.setListaDeRecogidasPorManifiestoDescargadas(listaDeRecogidasPorManifiesto);

        /* sellana el  jtable rcogidas*/
        modelo3 = (DefaultTableModel) jTableRecogidas.getModel();

        int fila = jTableRecogidas.getRowCount();

        modelo3.addRow(new Object[jTableRecogidas.getRowCount()]);

        jTableRecogidas.setValueAt("" + (fila + 1), fila, 0);  // item
        jTableRecogidas.setValueAt(facturaActual.getNumeroFactura(), fila, 1); // numero de factura
        jTableRecogidas.setValueAt(txtNumeroDeSoporteRecogida.getText().trim(), fila, 2); // numero de soporte
        jTableRecogidas.setValueAt(txtFacturaAfectada.getText().trim(), fila, 3); // numero de factura recogida
        jTableRecogidas.setValueAt(fac.getNombreDeCliente(), fila, 4); // nombre del nombreDelCliente
        jTableRecogidas.setValueAt(nf.format(valorDescontado), fila, 5); // valor de la recogida

        // se ubica el cursor en la fila insertada
        jTableRecogidas.changeSelection(fila, 0, false, false);

        /*se deshabilitan  se limpian los campos de texto respectivo*/
        txtFacturaAfectada.setEditable(false);
        txtNumeroDeSoporteRecogida.setEditable(false);
        txtValorRecogida.setEditable(false);

        txtFacturaAfectada.setEnabled(false);
        txtNumeroDeSoporteRecogida.setEnabled(false);
        txtValorRecogida.setEnabled(false);

        txtFacturaAfectada.setText("");
        txtNumeroDeSoporteRecogida.setText("");
        txtValorRecogida.setText("");
        txtValorDescontado.setText("");
        rBtnContado.setSelected(true);
        btnAgregarRecogida.setEnabled(false);

        txtNumeroDeFactura.requestFocus();
    }

    /**
     * Método encargado de realizar el descargue de las facturas que vuelven a
     * salir a Distribución
     *
     * @param facturaDescargada corresponde al numero de la factura que salió a
     * distribución
     *
     */
    private void descargarReEnvios() throws Exception {

        facturaActual.setValorDescuento(0.0);
        facturaActual.setValorRecaudado(0.0);
        facturaActual.setTipoDeDEscargue("R. E.");
        //txtNumeroDeFactura.setText(nf.format(0.0));

        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

        // se anexa el registro a la tabla de facturas por numeroDeManifiesto
        filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

        modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
        jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getTipoDeDEscargue(), filaSeleccionada, 3); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt(nf.format(0.0), filaSeleccionada, 4);

        // se ubica en la fila insertada
        jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

        lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

        facturaActual.setValorRechazo(0.0);
        facturaActual.setValorDescuento(0.0);
        facturaActual.setValorRecaudado(0.0);
        facturaActual.setIdTipoDeMovimiento(6); // vuelve a zona
        facturaActual.setCausalDeRechazo(18); // FUERA DE FECHA U HORARIO
        facturaActual.setActivo(1);

        manifiestoActual.getListaFacturasDescargadas().add(facturaActual);

        rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
        senteciaSqlFacturasDescargadas = facturaActual.getCadenaConLosCamposParaDescargue();

        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        Inicio.GuardaConsultaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);

        // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPendientesPorDescargar()) {
            if (obj.getNumeroFactura().equals(facturaActual.getNumeroFactura())) {

                /*Se elimina la factura de la lista pendientes por descargar*/
                manifiestoActual.getListaFacturasPendientesPorDescargar().remove(obj);
                break;
            }
        }

        calcularValoraAConsignar();

        lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

        // armarArchivoProductosPorFacturadescargados(facturaDescargada);
        new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();

        lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());

        if (manifiestoActual.getListaFacturasPendientesPorDescargar().isEmpty()) {
            btnGrabar.setEnabled(true);
            jBtnGrabar.setEnabled(true);

            chkDescuento.setSelected(false);
            chkDescuento.setEnabled(false);
            cbxCausalDeRechazoFactura.setEnabled(false);
            btnDescargarRechazoTotal.setEnabled(false);
            btnGrabar.requestFocus();
        }

        if (manifiestoActual.getListaFacturasPorManifiesto().size() == manifiestoActual.getListaFacturasDescargadas().size()) {
            if (valorTotalAConsignar >= valorConsignado) {
                btnGrabar.setEnabled(true);
                jBtnGrabar.setEnabled(true);
            }
        }

        txtNumeroDeFactura.requestFocus();

    }

    /**
     * Método encargado de realizar el descargue de las facturas que vuelven a
     * salir a Distribución
     *
     * @param facturaDescargada corresponde al numero de la factura que salió a
     * distribución
     *
     */
    private void descargarNoVisitados() throws Exception {

        facturaActual.setTipoDeDEscargue("N. V.");
        facturaActual.setValorDescuento(0.0);
        facturaActual.setValorRecaudado(0.0);
        //txtNumeroDeFactura.setText(nf.format(0.0));

        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

        // se anexa el registro a la tabla de facturas por numeroDeManifiesto
        filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

        modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
        jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getTipoDeDEscargue(), filaSeleccionada, 3); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt(nf.format(0.0), filaSeleccionada, 4);

        // se ubica en la fila insertada
        jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

        lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

        manifiestoActual.getListaFacturasDescargadas().add(facturaActual);
        facturaActual.setAdherenciaDescargue(manifiestoActual.getListaFacturasDescargadas().size() + 1);
        facturaActual.setValorRechazo(0.0);
        facturaActual.setValorDescuento(0.0);
        facturaActual.setValorRecaudado(0.0);
        facturaActual.setIdTipoDeMovimiento(7); // vuelve a zona
        facturaActual.setCausalDeRechazo(18); // FUERA DE FECHA U HORARIO
        facturaActual.setActivo(1);

        rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
        senteciaSqlFacturasDescargadas = facturaActual.getCadenaConLosCamposParaDescargue();

        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        Inicio.GuardaConsultaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);

        // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPendientesPorDescargar()) {
            if (obj.getNumeroFactura().equals(facturaActual.getNumeroFactura())) {

                /*Se elimina la factura de la lista pendientes por descargar*/
                manifiestoActual.getListaFacturasPendientesPorDescargar().remove(obj);
                break;
            }
        }

        calcularValoraAConsignar();

        lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

        // armarArchivoProductosPorFacturadescargados(facturaDescargada);
        new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();

        lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());

        if (manifiestoActual.getListaFacturasPendientesPorDescargar().isEmpty()) {
            btnGrabar.setEnabled(true);
            jBtnGrabar.setEnabled(true);
            chkDescuento.setSelected(false);
            chkDescuento.setEnabled(false);
            cbxCausalDeRechazoFactura.setEnabled(false);
            btnDescargarRechazoTotal.setEnabled(false);
            btnGrabar.requestFocus();
        }

        if (manifiestoActual.getListaFacturasPorManifiesto().size() == manifiestoActual.getListaFacturasDescargadas().size()) {
            if (valorTotalAConsignar >= valorConsignado) {
                btnGrabar.setEnabled(true);
                jBtnGrabar.setEnabled(true);
            }
        }

        txtNumeroDeFactura.requestFocus();

    }

    /**
     * Método encargado de realizar el descargue de las recogidas realizadas en
     * una ruta
     *
     * @param soporte
     */
    protected void agregarConsignacion(CSoportesConsignaciones soporte) {

        //rutaDeArchivoRecogidasDescargado = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_RecogidasDescargados.txt";
        rutaDeArchivoSoporteConsignaciones = manifiestoActual.getRutaArchivoSoportesDeConsignaciones();

        senteciaSqlSoportesConsignaciones = soporte.getCadenaConLosCampos();

        //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
        ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoSoporteConsignaciones);

        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        archivo.insertarLineaEnFichero(senteciaSqlSoportesConsignaciones, rutaDeArchivoSoporteConsignaciones);
        senteciaSqlSoportesConsignaciones = null;

        valorConsignado += soporte.getValor();

        lblValorConsignado.setText(nf.format(valorConsignado));

        /*Se agrega la consignacion a la lista repectiva*/
        manifiestoActual.getListaDeSoportesConsignaciones().add(soporte);
        //manifiestoActual.setListaDeSoportesConsignaciones(listaDeSoportesDeConsignaciones);

        /* sellana el  jtable rcogidas*/
        DefaultTableModel modelo = (DefaultTableModel) jTableListaDeConsignaciones.getModel();

        int fila = jTableListaDeConsignaciones.getRowCount();

        modelo.addRow(new Object[fila]);

        jTableListaDeConsignaciones.setValueAt(soporte.getNumeroSoporte(), fila, 0);  // item
        jTableListaDeConsignaciones.setValueAt(soporte.getNombreDelBanco(), fila, 1); // numero de factura
        jTableListaDeConsignaciones.setValueAt(soporte.getMedioDePago(), fila, 2); // numero de soporte
        jTableListaDeConsignaciones.setValueAt(nf.format(soporte.getValor()), fila, 3); // valor de la recogida

        // se ubica el cursor en la fila insertada
        jTableListaDeConsignaciones.changeSelection(fila, 0, false, false);

        txtNumeroSoporteConsignacion.setText("");
        txtMedioDePago.setText("");
        txtValorConsignacion.setText("");

        if (manifiestoActual.getListaFacturasPorManifiesto().size() == manifiestoActual.getListaFacturasDescargadas().size()) {
            if (valorTotalAConsignar >= valorConsignado) {
                btnGrabar.setEnabled(true);
                jBtnGrabar.setEnabled(true);
            }
        }

        txtNumeroSoporteConsignacion.requestFocus();
    }


    private void mnuBorrarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuBorrarFacturasActionPerformed

        if (manifiestoActual.getEstadoManifiesto() == 4) {
            return;
        }
    }//GEN-LAST:event_mnuBorrarFacturasActionPerformed

    private void mnuBorrarFacturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuBorrarFacturasMouseClicked

    }//GEN-LAST:event_mnuBorrarFacturasMouseClicked

    /**
     * Método que elimina la factura y los productos de una facturas
     * descargadas, que se encuentra almacenada en un archivo
     *
     * @param numFactura es el número de la factura que se va a modificar .
     */
    private boolean eliminarFacturaDelArchivo(String numFactura) {
        boolean borrado = false;
        try {
            // IDENTIFICAMOS EL OBJETO FACTURA DESCARGADA  PARA REALIZAR LOS RESPECTIVOS CAMBIOS
            // for (CFacturasDescargadas obj : arrayTemporalFacDes) {listaFacturasDescargadas
            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasDescargadas()) {
                if (obj.getNumeroFactura().equals(numFactura)) {

                    /*Se elimina la linea del archivo donde esta la factura*/
                    CFacturasDescargadas facturaDescargada = new CFacturasDescargadas(ini);

                    facturaDescargada.setConsecutivo(obj.getConsecutivo());
                    facturaDescargada.setNumeroManifiesto(obj.getNumeroManifiesto());
                    facturaDescargada.setAdherenciaDescargue(obj.getAdherencia());
                    facturaDescargada.setNumeroFactura(obj.getNumeroFactura());
                    facturaDescargada.setValorRechazo(obj.getValorRechazo());
                    facturaDescargada.setValorDescuento(obj.getValorDescuento());
                    facturaDescargada.setValorRecaudado(obj.getValorRecaudado());
                    facturaDescargada.setIdTipoDeMovimiento(obj.getIdTipoDeMovimiento());
                    facturaDescargada.setCausalDeRechazo(obj.getCausalDeRechazo());
                    facturaDescargada.setActivo(1);

                    borrado = facturaDescargada.borrarFacturaDescargada(filaSeleccionada, this.manifiestoActual);
                    // arrayTemporalFacDes.remove(obj);
                    //borrado = true;

                }
            }

        } catch (Exception ex) {
            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return borrado;
    }

    private void calcularValoraAConsignar() {
        /*Se calcula el valor recaudado del manifiesto y se imprime en el label*/
        valorTotalRecaudado = 0.0;
        valorTotalRecogidas = 0.0;
        manifiestoActual.setListaFacturasDescargadas(new File(manifiestoActual.getRutaArchivoDescargueFacturas()));
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasDescargadas()) {
            valorTotalRecaudado += obj.getValorRecaudado();

        }

        manifiestoActual.setListaDeRecogidasPorManifiesto(new File(manifiestoActual.getRutArchivoRecogidasporManifiesto()));
        for (CRecogidasPorManifiesto obj : manifiestoActual.getListaDeRecogidasPorManifiesto()) {
            valorTotalRecogidas += obj.getValorRecaudadoRecogida();
        }

        valorTotalAConsignar = valorTotalRecaudado - valorTotalRecogidas;
    }


    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (!estaOcupadoGrabando) {
            if (manifiestoActual != null) {
                if (esteManifiestoEsMio) {
                    new Thread(new HiloLiberarManifiesto(manifiestoActual, true)).start();//-->Error v 
                    // manifiestoActual.liberarManifiesto(true);
                    manifiestoActual = null;
                    esteManifiestoEsMio = false;
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "El sistema está ocupado grabado los datos,\n no se puede cerrar el formulario", "Error al cerrar", JOptionPane.ERROR_MESSAGE);

        }


    }//GEN-LAST:event_formInternalFrameClosing

    private void txtNumeroDeFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed

        imprimir();
    }//GEN-LAST:event_btnImprimirActionPerformed

    public void imprimir() {
        //manifiestoActual.setVistaFacturasDescargadas(manifiestoActual.getListaFacturasDescargadas());
        //manifiestoActual.setListaDeSoportesConsignaciones(listaDeSoportesDeConsignaciones);

        ReporteDescargueDeFacturas1 demo = new ReporteDescargueDeFacturas1(ini, manifiestoActual);
    }

    private void txtNumeroManifiestoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroManifiestoFocusGained
//        txtNumeroManifiesto.setSelectionStart(0);
//        txtNumeroManifiesto.setSelectionEnd(txtNumeroManifiesto.getText().length());
    }//GEN-LAST:event_txtNumeroManifiestoFocusGained

    private void txtNumeroManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroManifiestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroManifiestoActionPerformed

    private void txtNumeroManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroManifiestoKeyPressed

        /*Evento al oprimir la tecla F2 */
        if (evt.getKeyCode() == KeyEvent.VK_F2) {

            fBuscarManifiestos = new FBuscarManifiestos(ini, this);
            fBuscarManifiestos.setClosable(true);
            fBuscarManifiestos.setVisible(true);
            fBuscarManifiestos.setTitle("Formulario para Buscar manifiestos pendientes de Descargue");
            //form.setSize(PrincipalLogistica.escritorio.getSize());
            fBuscarManifiestos.setLocation((this.ini.getDimension().width - fBuscarManifiestos.getSize().width) / 2, (this.ini.getDimension().height - fBuscarManifiestos.getSize().height) / 2);

            this.getDesktopPane().add(fBuscarManifiestos);
            fBuscarManifiestos.show();
            fBuscarManifiestos.toFront();

        }

        /*Evento al oprimir la tecla Enter */
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //manifiestoActual = null;

            /* Se realiza la consulta para buscar el manifiesto que se va a descargar*/
            new Thread(new HiloConsultarManifiesto(ini, this)).start();

        }

    }//GEN-LAST:event_txtNumeroManifiestoKeyPressed

    public void llenarjTableFacturasPorVehiculo() throws Exception {

        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();
        if (modelo2.getRowCount() > 0) {
            int a = modelo2.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo2.removeRow(i);
            }
        }

        txtNumeroManifiesto.setEditable(false);

        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasDescargadas()) {

            int fila = jTableFacturasPorVehiculo.getRowCount();
            modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
            jTableFacturasPorVehiculo.setValueAt("" + (fila + 1), fila, 0);  // item
            jTableFacturasPorVehiculo.setValueAt(obj.getNumeroFactura(), fila, 1); // numero de factura
            jTableFacturasPorVehiculo.setValueAt(obj.getNombreDeCliente(), fila, 2); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt(obj.getTipoDeDEscargue(), fila, 3); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt(nf.format(obj.getValorRecaudado()), fila, 4);

            valorTotalRecaudado += obj.getValorRecaudado();

        }

    }

    public void llenarjTableFacturasDescargadas() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();
        txtNumeroManifiesto.setEditable(false);

        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasDescargadas()) {
            filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();
            modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
            jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
            jTableFacturasPorVehiculo.setValueAt(obj.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
            jTableFacturasPorVehiculo.setValueAt(obj.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt(nf.format(obj.getValorRecaudado()), filaSeleccionada, 3);

            valorTotalRecaudado += obj.getValorRecaudado();

        }

    }

    public void llenarjTableRecogidasPorVehiculo() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) jTableRecogidas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        modelo2 = (DefaultTableModel) jTableRecogidas.getModel();
        txtNumeroManifiesto.setEditable(false);

        if (manifiestoActual.getListaDeRecogidasPorManifiesto() != null) {

            for (CRecogidasPorManifiesto obj : manifiestoActual.getListaDeRecogidasPorManifiesto()) {

                filaSeleccionada = jTableRecogidas.getRowCount();

                modelo2.addRow(new Object[jTableRecogidas.getRowCount()]);
                jTableRecogidas.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
                jTableRecogidas.setValueAt(obj.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
                jTableRecogidas.setValueAt(obj.getNumeroDeSoporte(), filaSeleccionada, 2); // nombre del nombreDelCliente
                jTableRecogidas.setValueAt(obj.getFacturaAfectada(), filaSeleccionada, 3); // nombre del nombreDelCliente
                for (CFacturasPorManifiesto fac : manifiestoActual.getListaFacturasPorManifiesto()) {
                    if (obj.getNumeroFactura().equals(obj.getNumeroFactura())) {
                        jTableRecogidas.setValueAt(fac.getNombreDeCliente(), filaSeleccionada, 4); // nombre del nombreDelCliente
                        break;
                    }
                }
                //jTableRecogidas.setValueAt(obj.get, filaSeleccionada, 4); // nombre del nombreDelCliente
                jTableRecogidas.setValueAt(nf.format(obj.getValorRecaudadoRecogida()), filaSeleccionada, 5);

                valorTotalRecogidas += obj.getValorRecaudadoRecogida();
            }
        }

    }

    public void llenarjTableRecogidasPorManifiesto() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) jTableRecogidas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        modelo2 = (DefaultTableModel) jTableRecogidas.getModel();
        txtNumeroManifiesto.setEditable(false);

        if (manifiestoActual.getListaDeRecogidasPorManifiesto() != null) {

            for (CRecogidasPorManifiesto obj : manifiestoActual.getListaDeRecogidasPorManifiesto()) {

                Vst_Factura facturaActual = new Vst_Factura(ini, obj.getNumeroFactura(), false);

                filaSeleccionada = jTableRecogidas.getRowCount();

                modelo2.addRow(new Object[jTableRecogidas.getRowCount()]);
                jTableRecogidas.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
                jTableRecogidas.setValueAt(obj.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
                jTableRecogidas.setValueAt(obj.getNumeroDeSoporte(), filaSeleccionada, 2); // nombre del nombreDelCliente
                jTableRecogidas.setValueAt(obj.getFacturaAfectada(), filaSeleccionada, 3); // nombre del nombreDelCliente
                jTableRecogidas.setValueAt(facturaActual.getNombreDeCliente(), filaSeleccionada, 4); // nombre del nombreDelCliente
                jTableRecogidas.setValueAt(nf.format(obj.getValorRecaudadoRecogida()), filaSeleccionada, 5);

                valorTotalRecogidas += obj.getValorRecaudadoRecogida();
            }
        }

    }

    public void llenarjTableListaDeConsignaciones() throws Exception {

        modelo2 = (DefaultTableModel) jTableListaDeConsignaciones.getModel();
        if (modelo2.getRowCount() > 0) {
            int a = modelo2.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo2.removeRow(i);
            }
        }

        txtNumeroManifiesto.setEditable(false);

        for (CSoportesConsignaciones obj2 : manifiestoActual.getListaDeSoportesConsignaciones()) {

            filaSeleccionada = jTableListaDeConsignaciones.getRowCount();
            modelo2.addRow(new Object[jTableListaDeConsignaciones.getRowCount()]);

            //jTableListaDeConsignaciones.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
            jTableListaDeConsignaciones.setValueAt(obj2.getNumeroSoporte(), filaSeleccionada, 0); // numero de factura
            jTableListaDeConsignaciones.setValueAt(obj2.getNombreDelBanco(), filaSeleccionada, 1); // nombre del nombreDelCliente
            // jTableListaDeConsignaciones.setValueAt(obj2.getNumeroDeCuenta(), filaSeleccionada, 2);
            jTableListaDeConsignaciones.setValueAt(obj2.getMedioDePago(), filaSeleccionada, 2);
            jTableListaDeConsignaciones.setValueAt(nf.format(obj2.getValor()), filaSeleccionada, 3);

            valorConsignado += obj2.getValor();

        }

    }

    public void llenarjListFacturasSinManifestar() throws Exception {

        eliminarDatosDejListFacturasNoManifestadas();

        final DefaultListModel model = new DefaultListModel();

        for (String obj : listaDeFacturasNoManifestadas) {

            model.addElement(obj);
        }
        jListFacturasNoManifestadas.setModel(model);

    }

    private void txtPlacaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaFocusGained

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void txtPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaKeyPressed

    private void txtKilometrosEntradaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKilometrosEntradaFocusGained
        txtKilometrosEntrada.setSelectionStart(0);
        txtKilometrosEntrada.setSelectionEnd(txtKilometrosEntrada.getText().length());
    }//GEN-LAST:event_txtKilometrosEntradaFocusGained

    private void txtKilometrosEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKilometrosEntradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKilometrosEntradaActionPerformed

    private void txtKilometrosEntradaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKilometrosEntradaKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            manifiestoActual.setKmEntrada(Integer.parseInt(txtKilometrosEntrada.getText().trim()));
            manifiestoActual.setKmRecorrido(Integer.parseInt(txtKilometrosEntrada.getText().trim()) - manifiestoActual.getKmSalida());
            lblKilometrosRecorridos.setText("" + manifiestoActual.getKmRecorrido());

            /* Si el kilometraje es cero, nos da un error porque el recorrido debe ser superior a "0"*/
            if (manifiestoActual.getKmRecorrido() <= 0) {
                JOptionPane.showInternalMessageDialog(this, "EL KILOMETRAJE DE ENTRADA ESTA MAL TOMADO ,\n"
                        + "EL RECORRIDO  NO PUEDE SER NEGATIVO", "Error", 0);
                /* Cuando el recorrido es mayor que cero */
            } else {
                this.setResizable(false);

                /* Se valida si se quiere actualizar el kilometraje */
                int actualizarKilometraje = JOptionPane.showConfirmDialog(this, "El kilometraje está correcto ?", "Guardar registro", 0);

                /* Caso en que entre por verdadero para actualizar el kilometraje*/
                if (actualizarKilometraje == 0) {

//                    for (CCarros obj : ini.getListaDeVehiculos()) {
//                        if (obj.getPlaca().equals(manifiestoActual.getVehiculo())) {
//                            carro = obj;
//                            break;
//                        }
//
//                    }
                    CCarros carro = new CCarros(ini, manifiestoActual.getVehiculo());

                    if (carro == null) {
                        JOptionPane.showInternalMessageDialog(this, "EL vehiculo no existe", "Error", 0);
                        return;
                    }
                    manifiestoActual.setKmEntrada(Integer.parseInt(txtKilometrosEntrada.getText().trim()));
                    manifiestoActual.setKmRecorrido(kilometrosRecorridos);

                    carro.setKilometrajeActual(Integer.parseInt(txtKilometrosEntrada.getText().trim()));

                    /* Actualiza los datos del kilometraje del vehiculo a la BBDD*/
                    String sql = carro.StringSQLUpdatekilometraje();
                    new Thread(new HiloGuardarRegistro(ini, sql)).start();

                    txtKilometrosEntrada.setEditable(false);
                    cbxMovimientoFactura.setEnabled(true);
                    CbxPrefijo.setEnabled(true);
                    txtNumeroDeFactura.setEnabled(true);
                    //chkDescuento.setEnabled(true);
                    rBtnContado.setEnabled(true);
                    rBtnCredito.setEnabled(true);

                    txtNumeroSoporteConsignacion.setEnabled(true);
                    txtMedioDePago.setEnabled(true);
                    txtValorConsignacion.setEnabled(true);
                    dateOperacion.setEnabled(true);
                    cbxEntidadesBancarias.setEnabled(true);
                    cbxNumeroDeCuenta.setEnabled(true);
                    dateOperacion.setEnabled(true);

                    cbxMovimientoFactura.requestFocus();

                }

            }

        }

    }//GEN-LAST:event_txtKilometrosEntradaKeyPressed

    private void cbxMovimientoFacturaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxMovimientoFacturaItemStateChanged
        tipoMovimientoFactura = 0;
        indiceCbx = cbxMovimientoFactura.getSelectedIndex();
        if (nuevo) {
            for (CMovimientosManifiestosfacturas obj : ini.getListaDeMovimientosManifiestosfacturas()) {
                if (cbxMovimientoFactura.getSelectedItem().toString().equals(obj.getNombreMovimientosManifiestosfacturas())) {
                    tipoMovimientoFactura = obj.getIdMovimientosManifiestosfacturas();
                }
            }
            jTabbedPane1.setEnabled(true);
            switch (tipoMovimientoFactura) {
                case 0:
                    CbxPrefijo.setEnabled(false);
                    txtNumeroDeFactura.setEnabled(false);
                    txtNumeroDeFactura.setEnabled(false);
                    chkDescuento.setEnabled(false);
                    break;
                // NO SE HA SELECCIONADO NINGUN TIPO DE MOVIMIENTO
                case 1:
                    chkDescuento.setEnabled(false);
                    CbxPrefijo.setEnabled(false);
                    txtNumeroDeFactura.setEnabled(false);
                    txtNumeroDeFactura.setEnabled(false);
                    break;
                // ENTREGA TOTAL
                case 2:

                    CbxPrefijo.setEnabled(true);
                    txtNumeroDeFactura.setEnabled(true);
                    txtNumeroDeFactura.setEditable(true);
                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlEntregas), true);
                    jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlEntregas));
                    chkDescuento.setEnabled(true);
                    txtNumeroDeFactura.requestFocus();
                    break;
                // RECHAZO TOTAL
                case 3: //rechazos totales
                    //chkDescuento.setEnabled(true);
                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlRechazosTotales), true);
                    jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlRechazosTotales));
                    cbxCausalDeRechazoFactura.setEnabled(true);
                    btnDescargarRechazoTotal.setEnabled(true);
                    CbxPrefijo.setEnabled(true);
                    txtNumeroDeFactura.setEnabled(true);
                    chkDescuento.setEnabled(false);
                    txtNumeroDeFactura.requestFocus();
                    break;
                // ENTREGA CON NOVEDAD
                case 4:
                    chkDescuento.setEnabled(true);
                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlRechazosParciales), true);
                    jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlRechazosParciales));
                    CbxPrefijo.setEnabled(true);
                    txtNumeroDeFactura.setEditable(true);
                    txtNumeroDeFactura.setEnabled(true);

                    txtNumeroDeFactura.requestFocus();
                    break;
                //Recogidas
                case 5:
                    chkDescuento.setEnabled(false);
                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlRecogidas), true);
                    jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlRecogidas));

                    txtNumeroDeFactura.requestFocus();
                    break;

                // VOLVER A ZONIFICAR
                case 6:

                    chkDescuento.setEnabled(false);
                    CbxPrefijo.setEnabled(true);
                    txtNumeroDeFactura.setEnabled(true);
                    txtNumeroDeFactura.setEnabled(true);
                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlEntregas), true);
                    jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlEntregas));
                    chkDescuento.setEnabled(false);
                    txtNumeroDeFactura.requestFocus();
                    break;

            }
        }
    }//GEN-LAST:event_cbxMovimientoFacturaItemStateChanged

    private void cbxMovimientoFacturaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxMovimientoFacturaFocusLost
        CbxPrefijo.setEnabled(true);
        txtNumeroDeFactura.setEnabled(true);
        txtNumeroDeFactura.setEditable(true);
        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();
// TODO add your handling code here:
    }//GEN-LAST:event_cbxMovimientoFacturaFocusLost

    private void txtKilometrosSalidaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKilometrosSalidaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKilometrosSalidaFocusGained

    private void txtKilometrosSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKilometrosSalidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKilometrosSalidaActionPerformed

    private void txtKilometrosSalidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKilometrosSalidaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKilometrosSalidaKeyPressed

    private void txtKilometrosEntradaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKilometrosEntradaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKilometrosEntradaKeyReleased

    private void cerrarManifiesto() {

        new Thread(new HiloGuardarManifiestoDescargado(ini, this, false)).start();

    }


    private void cbxMovimientoFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxMovimientoFacturaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            CbxPrefijo.setEnabled(true);
            txtNumeroDeFactura.setEnabled(true);
            txtNumeroDeFactura.setEnabled(true);
            txtNumeroDeFactura.requestFocus();        // TODO add your handling code here:
        }
    }//GEN-LAST:event_cbxMovimientoFacturaKeyPressed

    private void lblFacturasPendientesMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFacturasPendientesMouseMoved

        String toolTipHTML = ""
                + "<html><body><pre>";

        try {
            if ((manifiestoActual.getListaFacturasPendientesPorDescargar() != null) || manifiestoActual.getListaFacturasPendientesPorDescargar().size() > 0) { // Vst_FacturasPorManifiesto
                int i = 1;
                for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPendientesPorDescargar()) {
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
            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
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
        int x = JOptionPane.showConfirmDialog(this, "Desea ingresar la Recogida ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        /* Si elige adicionar la regogida */
        if (x == JOptionPane.YES_OPTION) {
            try {

                for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasDescargadas()) {
                    if (obj.getNumeroFactura().equals(facturaActual.getNumeroFactura())) {
//                        facturaDescargada.setConsecutivo(obj.getConsecutivo());
//                        facturaDescargada.setNumeroManifiesto(obj.getNumeroManifiesto());
//                        facturaDescargada.setNumeroFactura(obj.getNumeroFactura());
                        agregarRecogida(obj);

                    }
                }
                // agregarRecogida(facturaDescargada);

            } catch (Exception ex) {
                Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
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
            JOptionPane.showInternalMessageDialog(this, "No ha seleccionado una causal de Devolucion", "Alerta ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            //double valorFactura = llenarTablaProductosPorFactura();
            lblValorFactura.setText(nf.format(facturaActual.getValorTotalFactura()));

            // SE VALIDA QUE LA FACTURA NO ESTE DESCARGADA
            if (!estaDescargadaLaFactura(facturaActual.getNumeroFactura())) {

                descargarRechazosTotales();
            }

        } catch (Exception ex) {
            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("\n\n Error en btnDescargarRechazoTotalActionPerformed \n");

            JOptionPane.showInternalMessageDialog(this, "La factura no aparece en el manifiesto", "Error", 0);

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
            chkDescuento.setSelected(false);
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

    private void mnuBorrarRecogidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuBorrarRecogidaActionPerformed
        int borrarFila;
        modelo2 = (DefaultTableModel) jTableRecogidas.getModel();
        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO

        if (jTableRecogidas.getRowCount() > 0) {

            int fila = jTableRecogidas.getSelectedRow();

            borrarFila = JOptionPane.showOptionDialog(null, "Desea eliminar la Recogida  de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "No");

            // HA SELECCIONADO BORRAR LA FILA
            if (borrarFila == 0) {
                String numFactura;

                numFactura = modelo2.getValueAt(fila, 1).toString();

                /*Borra el registro del archivo de las recogidas */
                manifiestoActual.eliminarRecogidaDelArchivo(numFactura);


                /*Borra la fila de la tabla de las recogidas*/
                DefaultTableModel modelo = (DefaultTableModel) jTableRecogidas.getModel();
                if (modelo.getRowCount() > 0) {

                    modelo.removeRow(fila);

                }

                /*  calcular el nuevo Valor */
                calcularValoraAConsignar();
                lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

            }
        }
    }//GEN-LAST:event_mnuBorrarRecogidaActionPerformed

    private void mnuBorrarRecogidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuBorrarRecogidaMouseClicked

    }//GEN-LAST:event_mnuBorrarRecogidaMouseClicked

    private void chkDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDescuentoActionPerformed

        rBtnContado.setSelected(true);
        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();
    }//GEN-LAST:event_chkDescuentoActionPerformed

    private void mnuBorraUnaFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuBorraUnaFacturaMouseClicked
        if (!cbxMovimientoFactura.isEnabled()) {
            return;
        }
        if (borrarUnaFactura()) {
            return;
        }
    }//GEN-LAST:event_mnuBorraUnaFacturaMouseClicked

    private boolean borrarUnaFactura() throws HeadlessException {
        boolean borrado = false;
        int borrarFila;

        /*Si el manifiesto ya fue descargado aborta operacion*/
        if (manifiestoActual.getEstadoManifiesto() == 4) {
            return true;
        }

        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO
        if (jTableFacturasPorVehiculo.getRowCount() > 0) {
            modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

            /* se identifica la fila seleccionada de la tabla*/
            filaSeleccionada = jTableFacturasPorVehiculo.getSelectedRow();

            /*Se identifica el numero de la factura contenida en la fila seleccionada*/
            String numFactura = modelo2.getValueAt(filaSeleccionada, 1).toString();

            // borrarFila = JOptionPane.showOptionDialog(this, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "No");
            borrarFila = JOptionPane.showConfirmDialog(this, "Desea eliminar la factura # " + numFactura + " de la Tabla ?", "Eliminar Fcatura # " + numFactura, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            // HA SELECCIONADO BORRAR LA FILA
            if (borrarFila == JOptionPane.YES_OPTION) {

                try {

                    /*Borra los datos del archivo almacenado en el disco */
                    if (eliminarFacturaDelArchivo(numFactura)) {

                        /*Actualiza los valores de la factura sin descargar*/
                        for (CFacturasPorManifiesto fact : manifiestoActual.getListaFacturasPorManifiesto()) {

                            if (fact.getNumeroFactura().equals(numFactura)) {

                                /*Elimina el objeto al array de facturas descargadas*/
                                fact.setCausalDeRechazo(1);
                                fact.setIdMovimientoFactura(1);
                                fact.setIdTipoDeMovimiento(1);
                                fact.setAdherenciaDescargue(0);
                                fact.setNumeroDescuento("0");
                                fact.setNumeroRecogida("0");
                                fact.setValorARecaudarFactura(fact.getValorTotalFactura());
                                fact.setValorDescuento(0.0);
                                fact.setValorRecaudado(0.0);
                                fact.setValorRechazo(0.0);

                                /*Actualiza los valores de los productos de la factura*/
                                for (CProductosPorFactura pxf : fact.getListaProductosPorFactura()) {
                                    pxf.setValorDescuentoItem(0.0);
                                    pxf.setValorRechazoItem(0.0);
                                    pxf.setCantidadRechazadaItem(0.0);
                                    pxf.setCausalDeRechazo(1);
                                    pxf.setEntregado(0);
                                    pxf.setPorcentajeDescuento(0.0);
                                    pxf.setValorTotalLiquidacionItem(0.0);

                                }

                                break;
                            }
                        }

                        for (CFacturasPorManifiesto fact : manifiestoActual.getListaFacturasDescargadas()) {
                            if (fact.getNumeroFactura().equals(numFactura)) {

                                /*Elimina el objeto al array de facturas descargadas*/
                                manifiestoActual.getListaFacturasDescargadas().remove(fact);

                                /*Agrega el objeto al arrray de facturas pendientes por descargar*/
                                manifiestoActual.getListaFacturasPendientesPorDescargar().add(fact);
                                borrado = true;
                                break;
                            }
                        }

                        /*se reasigna las adeherencia de descargue */
                        int i = 1;
                        for (CFacturasPorManifiesto fact : manifiestoActual.getListaFacturasDescargadas()) {
                            fact.setAdherenciaDescargue(i);
                            i++;

                        }

                        //lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());
                    }

                    /*Borra la fila seleccionada del JTable */
                    // modelo2.removeRow(filaSeleccionada);
                    // ordena la tabla con la nueva adherencia
                    llenarjTableFacturasPorVehiculo();

                    calcularValoraAConsignar();

                    lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());
                    lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));

                    File fichero = new File(manifiestoActual.getRutaArchivoDescargueporductosPorFactura());

                    if (fichero.exists()) {

                        /*Se actualiza la lista de los productos descargados*/
                        manifiestoActual.setListaCProductosPorFacturaDescargados(fichero);
                        // listaDeCProductosPorFacturaDescargados = manifiestoActual.getListaCProductosPorFacturaDescargados();

                    }

                } catch (Exception ex) {
                    Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }

                btnGrabar.setEnabled(false);
                jBtnGrabar.setEnabled(false);
            }

        } else {
            borrado = false;
            JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);

        }
        return borrado;
    }

    private void mnuBorraTodasLasFacturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuBorraTodasLasFacturasMouseClicked
        if (borrarTodasLasFacturas()) {
            return;
        }
    }//GEN-LAST:event_mnuBorraTodasLasFacturasMouseClicked

    private boolean borrarTodasLasFacturas() throws HeadlessException {
        int borrarFila;
        if (manifiestoActual.getEstadoManifiesto() == 4) {
            return true;
        }
        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO
        if (jTableFacturasPorVehiculo.getRowCount() > 0) {
            modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

            // borrarFila = JOptionPane.showOptionDialog(this, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "No");
            borrarFila = JOptionPane.showConfirmDialog(this, "Desea eliminar los  Registro de la Tabla ?", "Eliminar Registros", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            // HA SELECCIONADO BORRAR LA FILA
            if (borrarFila == JOptionPane.OK_OPTION) {

                try {

                    /* Elimina los datos de los arrays*/
                    limpiarArrays();

                    /*Actualiza los valores de las facturas sin descargar*/
                    for (CFacturasPorManifiesto fact : manifiestoActual.getListaFacturasPorManifiesto()) {
                        /*Elimina el objeto al array de facturas descargadas*/
                        fact.setCausalDeRechazo(1);
                        fact.setIdMovimientoFactura(1);
                        fact.setIdTipoDeMovimiento(1);
                        fact.setAdherenciaDescargue(0);
                        fact.setNumeroDescuento("0");
                        fact.setNumeroRecogida("0");
                        fact.setValorARecaudarFactura(fact.getValorTotalFactura());
                        fact.setValorDescuento(0.0);
                        fact.setValorRecaudado(0.0);
                        fact.setValorRechazo(0.0);

                        /*Actualiza los valores de los productos de la factura*/
                        for (CProductosPorFactura pxf : fact.getListaProductosPorFactura()) {
                            pxf.setValorDescuentoItem(0.0);
                            pxf.setValorRechazoItem(0.0);
                            pxf.setCantidadRechazadaItem(0.0);
                            pxf.setCausalDeRechazo(1);
                            pxf.setEntregado(0);
                            pxf.setPorcentajeDescuento(0.0);
                            pxf.setValorTotalLiquidacionItem(0.0);

                        }

                        break;

                    }

                    /*Elimina los datos de las tablas */
                    limpiarTablas();

                    /*Se borran los archivos con los registros */
                    eliminararchivos();
                    File fichero;
                    fichero = new File(manifiestoActual.getRutaArchivoDescargueFacturas());
                    fichero = new File(manifiestoActual.getRutaArchivoDescargueporductosPorFactura());
                    fichero = new File(manifiestoActual.getRutArchivoRecogidasporManifiesto());
                    fichero = new File(manifiestoActual.getRutArchivoFacturasSinManifestar());

                    lblValorRecaudoManifiesto.setText(nf.format(0.0));

                    lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());

                    manifiestoActual.setListaCProductosPorFacturaDescargados(manifiestoActual.getRutaArchivoDescargueporductosPorFactura());

                } catch (Exception ex) {
                    Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
                    btnGrabar.setEnabled(false);
                    jBtnGrabar.setEnabled(false);
                }

                btnGrabar.setEnabled(false);
                jBtnGrabar.setEnabled(false);
            }

        } else {
            JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
        }
        return false;
    }

    private void mnuBorraUnaFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuBorraUnaFacturaActionPerformed
        if (!cbxMovimientoFactura.isEnabled()) {
            return;
        }
        if (manifiestoActual.getEstadoManifiesto() == 3) {
            if (borrarUnaFactura()) {
                return;
            }

        }

    }//GEN-LAST:event_mnuBorraUnaFacturaActionPerformed

    private void mnuBorraTodasLasFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuBorraTodasLasFacturasActionPerformed
        if (manifiestoActual.getEstadoManifiesto() == 3) {
            if (borrarTodasLasFacturas()) {
                return;
            }
        }
    }//GEN-LAST:event_mnuBorraTodasLasFacturasActionPerformed

    private void txtNombreConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreConductorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorFocusGained

    private void txtNombreConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorActionPerformed

    private void txtNombreConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreConductorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorKeyPressed

    private void txtRutaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRutaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaFocusGained

    private void txtRutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRutaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaActionPerformed

    private void txtRutaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRutaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaKeyPressed

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        CSoportesConsignaciones soporte = new CSoportesConsignaciones(ini);
        soporte.setFechaOperacion(ini.getFechaSql(dateOperacion));

        for (CCuentasBancarias obj : ini.getListaDeCuentasBancarias()) {
            if (obj.getNumeroDeCuenta().equals(cbxNumeroDeCuenta.getSelectedItem().toString())) {
                soporte.setIdBanco(obj.getIdBanco());
                soporte.setNombreDelBanco(obj.getNombreDeBanco());
                soporte.setIdCuentaBancaria(obj.getIdcuentasBancarias());
                break;
            }
        }

        soporte.setMedioDePago(txtMedioDePago.getText().trim());
        soporte.setNumeroDeCuenta(cbxNumeroDeCuenta.getSelectedItem().toString());
        soporte.setNumeroManifiesto(Integer.parseInt(txtNumeroManifiesto.getText().trim()));
        soporte.setNumeroSoporte(txtNumeroSoporteConsignacion.getText().trim());
        soporte.setValor(Double.parseDouble(txtValorConsignacion.getText().trim()));

        if (!validarSoporte(soporte)) {

            agregarConsignacion(soporte);

        } else {
            if (mensaje.equals("")) {
                JOptionPane.showInternalMessageDialog(this, "Soporte ya ingresado en este manifiesto ", "Soporte ya registrado", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showInternalMessageDialog(this, mensaje, "Soporte ya ingresado en la BBDD", JOptionPane.ERROR_MESSAGE);
                mensaje = "";
            }
        }
        txtNumeroSoporteConsignacion.setText("");
        txtMedioDePago.setText("");
        txtValorConsignacion.setText("");

        txtNumeroSoporteConsignacion.requestFocus();

    }//GEN-LAST:event_jButton1ActionPerformed

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
        nuevo();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
        grabar();
    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jBtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnImprimirActionPerformed
        imprimir();
    }//GEN-LAST:event_jBtnImprimirActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        cancelar();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnMinutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnMinutaActionPerformed

        int seleccion = JOptionPane.showOptionDialog(null, "Seleccione Tipo de Minuta",
                "Selector de opciones", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,// null para icono por defecto.
                new Object[]{"Completa", "Por Codigos", "Cancelar"}, "opcion 1");

        if (seleccion != -1) {
            System.out.println("seleccionada opcion " + (seleccion + 1));
            manifiestoActual.sacarMinutaManifiesto(seleccion);
        }    }//GEN-LAST:event_jBtnMinutaActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        salir();        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnExitActionPerformed

    private void btnGrabar100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabar100ActionPerformed
        int deseaGrabar = JOptionPane.showConfirmDialog(this, "Desea guardar todos los registros de una sola vez ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        /* Se valida el deseo de grabar los datos en la BBDD  */
        if (deseaGrabar == JOptionPane.YES_OPTION) {

            new Thread(new HiloGuardarManifiestoDescargado100(ini, this)).start();
        }
    }//GEN-LAST:event_btnGrabar100ActionPerformed

    private void jBtnGrabar100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabar100ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnGrabar100ActionPerformed

    private void jBtnBorrarFilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBorrarFilaActionPerformed
        if (!cbxMovimientoFactura.isEnabled()) {
            return;
        }
        if (manifiestoActual.getEstadoManifiesto() == 3) {
            if (borrarUnaFactura()) {
                return;
            }

        }
    }//GEN-LAST:event_jBtnBorrarFilaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox<String> CbxPrefijo;
    private javax.swing.JPopupMenu borrarFactura;
    private javax.swing.JPopupMenu borrarRecogida;
    private javax.swing.JButton btnAgregarRecogida;
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnDescargarRechazoTotal;
    private javax.swing.JButton btnDescargarRechazoTotal1;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnGrabar100;
    public javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JComboBox cbxCausalDeRechazoFactura;
    private javax.swing.JComboBox<String> cbxEntidadesBancarias;
    public javax.swing.JComboBox cbxMovimientoFactura;
    private javax.swing.JComboBox<String> cbxNumeroDeCuenta;
    public javax.swing.JCheckBox chkDescuento;
    public com.toedter.calendar.JDateChooser dateOperacion;
    public javax.swing.JToggleButton jBtnBorrarFila;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnGrabar100;
    private javax.swing.JToggleButton jBtnImprimir;
    public javax.swing.JToggleButton jBtnMinuta;
    private javax.swing.JButton jBtnNuevo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList jListFacturasNoManifestadas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    public javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableFacturasPorVehiculo;
    private javax.swing.JTable jTableListaDeConsignaciones;
    private javax.swing.JTable jTableProductosPorFactura;
    private javax.swing.JTable jTableRecogidas;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JLabel lblFacturasPendientes;
    public javax.swing.JLabel lblIdentificadorManifiesto;
    public javax.swing.JLabel lblKilometrosRecorridos;
    public javax.swing.JLabel lblValorConsignado;
    private org.edisoncor.gui.label.LabelCustom lblValorFactura;
    public org.edisoncor.gui.label.LabelCustom lblValorRecaudoManifiesto;
    private javax.swing.JMenuItem mnuBorraTodasLasFacturas;
    private javax.swing.JMenuItem mnuBorraUnaFactura;
    private javax.swing.JMenu mnuBorrarFacturas;
    private javax.swing.JMenuItem mnuBorrarRecogida;
    private javax.swing.JPanel pnlEntregas;
    private javax.swing.JPanel pnlRechazosParciales;
    public javax.swing.JPanel pnlRechazosTotales;
    private javax.swing.JPanel pnlRecogidas;
    public javax.swing.JRadioButton rBtnContado;
    public javax.swing.JRadioButton rBtnCredito;
    public javax.swing.JTextField txtFacturaAfectada;
    public javax.swing.JTextField txtKilometrosEntrada;
    public javax.swing.JTextField txtKilometrosSalida;
    public javax.swing.JTextField txtMedioDePago;
    public javax.swing.JTextField txtNombreConductor;
    public javax.swing.JTextField txtNumeroDeFactura;
    public javax.swing.JTextField txtNumeroDeSoporteRecogida;
    public javax.swing.JTextField txtNumeroManifiesto;
    public javax.swing.JTextField txtNumeroSoporteConsignacion;
    public javax.swing.JTextField txtPlaca;
    public javax.swing.JTextField txtRuta;
    public javax.swing.JTextField txtValorConsignacion;
    public javax.swing.JTextField txtValorDescontado;
    public javax.swing.JTextField txtValorRecogida;
    // End of variables declaration//GEN-END:variables

    private void limpiar() {

        manifiestoActual = null;
        carro = null;
        facturaActual = null;
        valorTotalRecaudado = 0.0;
        valorTotalRecogidas = 0.0;
        valorTotalAConsignar = 0.0;
        valorRecogida = 0.0;;
        valorDescontado = 0.0;
        valorConsignado = 0.0;
        isFormularioCompleto = false;
        cargado = false;
        nuevo = false;
        actualizar = false;
        liberado = false;
        grabar = false;
        estaOcupadoGrabando = false;
        isFacturaCredito = false;
        esteManifiestoEsMio = false;
        kilometrosRecorridos = 0;
        filaSeleccionada = 0;
        indiceLista = 0;
        columna = 0;
        mensaje = null;
        causalrechazo = 0;
        rutaDeArchivoFacturasDescargadas = null;
        rutaDeArchivoProductosDescargados = null;
        rutaDeArchivoRecogidasDescargado = null;
        rutaDeArchivoFacturasParaVolverAZonificar = null;
        rutaDeArchivoSoporteConsignaciones = null;
        tipoMovimientoFactura = 0;
        listaDeFacturasNoManifestadas = null;
        senteciaSqlFacturasDescargadas = null;
        senteciaSqlProductosPorFacturaDescargados = null;
        senteciaSqlSoportesConsignaciones = null;
        modelo1 = null;
        modelo2 = null;
        modelo3 = null;

        txtNumeroManifiesto.setEditable(false);
        txtKilometrosEntrada.setEnabled(false);
        txtKilometrosEntrada.setEditable(false);

        cbxMovimientoFactura.setEnabled(false);
        cbxMovimientoFactura.setSelectedItem("NINGUNO");
        cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
        cbxEntidadesBancarias.setEnabled(false);
        cbxNumeroDeCuenta.setEnabled(false);
        chkDescuento.setEnabled(false);

        txtNumeroManifiesto.setEnabled(false);
        CbxPrefijo.setEnabled(false);
        txtNumeroDeFactura.setEnabled(false);
        txtNumeroDeFactura.setEnabled(false);
        txtKilometrosEntrada.setEnabled(false);
        txtKilometrosSalida.setEnabled(false);
        txtPlaca.setEnabled(false);
        txtNombreConductor.setEnabled(false);
        txtRuta.setEnabled(false);
        txtNumeroSoporteConsignacion.setEnabled(false);
        txtMedioDePago.setEnabled(false);
        txtValorConsignacion.setEnabled(false);
        dateOperacion.setEnabled(false);

        txtNumeroManifiesto.setText("");
        txtNumeroDeFactura.setText("");
        CbxPrefijo.setEnabled(false);
        txtNumeroDeFactura.setEnabled(false);
        txtNumeroDeFactura.setEditable(false);
        txtKilometrosEntrada.setText("");
        txtKilometrosSalida.setText("");
        lblKilometrosRecorridos.setText("");
        txtPlaca.setText("");
        txtNombreConductor.setText("");
        txtRuta.setText("");
        txtNumeroSoporteConsignacion.setText("");
        txtMedioDePago.setText("");
        txtValorConsignacion.setText("");

        lblIdentificadorManifiesto.setText("Total a recaudar en Manifiesto # ");
        valorTotalRecaudado = 0.0;
        cbxMovimientoFactura.setEnabled(false);
        chkDescuento.setEnabled(false);
        chkDescuento.setSelected(false);
        rBtnContado.setEnabled(false);
        rBtnCredito.setEnabled(false);

        rBtnContado.setSelected(true);

        cbxCausalDeRechazoFactura.setEnabled(false);

        /* elimina lo registros de las tablas de la vista */
        limpiarTablas();


        /* Elimina los datos de los arrays*/
        limpiarArrays();

        valorTotalRecogidas = 0.0;
        valorTotalAConsignar = 0.0;
        valorTotalRecaudado = 0.0;
        valorConsignado = 0.0;
        mensaje = "";

        lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));
        lblValorFactura.setText("$ 0.0");
        lblValorConsignado.setText("$ 0.0");
        //numeroDeFacturasEnManifiesto = 0;

        //cantidadFacturasDescargadasManifiesto = 0;
        lblFacturasPendientes.setText("0");
        //manifiestoActual = null;
        facturaActual = null;

        esteManifiestoEsMio = false;

        eliminarDatosDejListFacturasNoManifestadas();
        dateOperacion.setEnabled(false);
        jButton1.setEnabled(false);
        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);

        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);

        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

    }

    private void limpiarArrays() {

        if (manifiestoActual != null) {
            /*Se vacían las listas respectivas */
            //listaFacturasPorManifiesto = new ArrayList();
            //listaFacturasDescargadas = new ArrayList();
            manifiestoActual.setListaFacturasPendientesPorDescargar(new ArrayList<CFacturasPorManifiesto>());
            manifiestoActual.setListaFacturasDescargadas(new ArrayList<CFacturasPorManifiesto>());
            //listaDeRecogidasPorManifiesto = new ArrayList();
            listaDeFacturasNoManifestadas = new ArrayList();
            //listaDeSoportesDeConsignaciones = new ArrayList();
        }

    }

    private void limpiarTablas() {
        DefaultTableModel modelo = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        modelo = (DefaultTableModel) jTableProductosPorFactura.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        modelo = (DefaultTableModel) jTableRecogidas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        modelo = (DefaultTableModel) jTableListaDeConsignaciones.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
    }

    private void limpiarTblProductosPorFactura() {

        DefaultTableModel modelo = (DefaultTableModel) jTableProductosPorFactura.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    private void ordenartblFacturasPorVehiculo() {
        for (int i = 0; i < jTableFacturasPorVehiculo.getRowCount(); i++) {
            jTableFacturasPorVehiculo.setValueAt(i + 1, i, 0);
        }
    }

    public boolean estaDescargadaLaFactura(String numeroDefactura) {
        boolean descargada = false;
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasDescargadas()) {
            if (numeroDefactura.equals(obj.getNumeroFactura())) {
                descargada = true;
                break;
            }
        }

        return descargada;
    }

    public boolean estaDescargadaRecogida(String numeroDefactura) {
        boolean descargada = false;
        for (CRecogidasPorManifiesto obj : manifiestoActual.getListaDeRecogidasPorManifiesto()) {
            if (numeroDefactura.equals(obj.getNumeroFactura())) {
                descargada = true;
                break;
            }
        }

        return descargada;
    }

    public void anexarFacturaActualALalista() {

        final DefaultListModel model = new DefaultListModel();
        for (String obj : listaDeFacturasNoManifestadas) {

            model.addElement(obj);
        }
        jListFacturasNoManifestadas.setModel(model);

    }

    public boolean verificarQueFacturaExiste() {
        boolean siExiste = false;
        for (String valor : listaDeFacturasNoManifestadas) {
            if (valor.equals(txtNumeroDeFactura.getText().trim())) {
                siExiste = true;
                break;
            }
        }
        return siExiste;
    }

    public boolean validarSoporte(CSoportesConsignaciones soporte) {
        boolean siExiste = false;
        if (manifiestoActual.getListaDeSoportesConsignaciones() != null) {
            for (CSoportesConsignaciones obj : manifiestoActual.getListaDeSoportesConsignaciones()) {
                if (obj.getNumeroSoporte().equals(txtNumeroSoporteConsignacion.getText().trim())) {
                    siExiste = true;
                    return true;
                }
            }
        }

        mensaje = soporte.validarConsignacion();

        return siExiste;
    }

    public void eliminarDatosDejListFacturasNoManifestadas() {

        DefaultListModel model = new DefaultListModel();
        jListFacturasNoManifestadas.setModel(model);
        //See more at: http://collectioncode.com/java/limpiar-jlist-en-java/#sthash.v1nd3O4o.dpuf

    }

    public void llenarDatosVista() {

        txtNumeroManifiesto.setEnabled(true);
        CbxPrefijo.setEnabled(false);
        txtNumeroDeFactura.setEnabled(false);
        txtNumeroDeFactura.setEnabled(false);
        txtKilometrosEntrada.setEnabled(true);
        txtKilometrosSalida.setEnabled(true);
        txtPlaca.setEnabled(true);
        txtNombreConductor.setEnabled(true);
        txtRuta.setEnabled(true);

        txtNumeroManifiesto.setEditable(false);
        txtNumeroDeFactura.setEditable(false);
        txtKilometrosEntrada.setEditable(false);
        txtKilometrosSalida.setEditable(false);
        txtPlaca.setEditable(false);
        txtNombreConductor.setEditable(false);
        txtRuta.setEditable(false);

        valorTotalAConsignar = valorTotalRecaudado - valorTotalRecogidas;

        lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));
        txtPlaca.setText(manifiestoActual.getVehiculo());

        txtKilometrosEntrada.setText("" + manifiestoActual.getKmEntrada());
        txtKilometrosSalida.setText("" + manifiestoActual.getKmSalida());

        kilometrosRecorridos = manifiestoActual.getKmEntrada() - manifiestoActual.getKmSalida();
        lblKilometrosRecorridos.setText("" + kilometrosRecorridos);
        txtNumeroDeFactura.setEditable(true);

        lblIdentificadorManifiesto.setText("Total a recaudar en Manifiesto # " + manifiestoActual.codificarManifiesto());

        if (manifiestoActual.getEstadoManifiesto() == 4) {

            try {
                /*Se llenan las jtables respectivas */
                llenarjTableFacturasPorVehiculo();

                lblCirculoDeProgreso.setVisible(false);
                lblFacturasPendientes.setText(" " + 0);
                /*Se habilitan las pestañas para visualizar las recogidas */
                jTabbedPane1.setEnabled(true);
                lblValorRecaudoManifiesto.setText(nf.format(this.valorTotalRecaudado - this.valorTotalRecogidas));

                // txtManifiesto.setEnabled(false);
                // txtKilometrosEntrada.setEnabled(false);
                //txtNumeroDeFactura.setEnabled(false);
                txtNombreConductor.setText(manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor());
                txtRuta.setText(manifiestoActual.getNombreDeRuta());
                cbxMovimientoFactura.setEnabled(false);
                CbxPrefijo.setEnabled(false);
                txtNumeroDeFactura.setEnabled(false);
                chkDescuento.setEnabled(false);
                chkDescuento.setSelected(false);
                chkDescuento.setSelected(false);
                rBtnContado.setEnabled(false);
                rBtnCredito.setEnabled(false);
                cbxCausalDeRechazoFactura.setEnabled(false);
                btnDescargarRechazoTotal.setEnabled(false);
                btnGrabar.setEnabled(false);
                jButton1.setEnabled(false);
                dateOperacion.setEnabled(false);
                jBtnMinuta.setEnabled(true);
                btnImprimir.setEnabled(true);

                btnImprimir.requestFocus();

                JOptionPane.showInternalMessageDialog(this, "Manifiesto descargado y completo", "Manifiesto completo", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (manifiestoActual.getEstadoManifiesto() == 3) {
            lblCirculoDeProgreso.setVisible(false);
            txtNombreConductor.setText(manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor());
            txtRuta.setText(manifiestoActual.getNombreDeRuta());
            jTabbedPane1.setEnabled(true);

            if (manifiestoActual.getListaFacturasPendientesPorDescargar().isEmpty()) {
                cbxMovimientoFactura.setEnabled(true);
                CbxPrefijo.setEnabled(true);
                txtKilometrosEntrada.setEnabled(true);
                txtNumeroDeFactura.setEnabled(true);
                chkDescuento.setEnabled(true);
                rBtnContado.setEnabled(true);
                rBtnCredito.setEnabled(true);
                JOptionPane.showInternalMessageDialog(this, "Manifiesto descargado y completo", "Manifiesto completo", JOptionPane.INFORMATION_MESSAGE);
                btnGrabar.setEnabled(true);
                this.lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());

                btnGrabar.requestFocus();
            } else {
                txtKilometrosEntrada.setEditable(true);
                btnGrabar.setEnabled(false);
                btnImprimir.setEnabled(false);
                cbxMovimientoFactura.setEnabled(false);
                chkDescuento.setEnabled(false);
                chkDescuento.setSelected(false);
                rBtnContado.setEnabled(false);
                rBtnCredito.setEnabled(false);
                chkDescuento.setEnabled(false);
                rBtnContado.setEnabled(true);
                rBtnCredito.setEnabled(true);
                this.lblFacturasPendientes.setText("" + manifiestoActual.getListaFacturasPendientesPorDescargar().size());

                txtKilometrosEntrada.requestFocus();
                txtKilometrosEntrada.requestFocus();
            }

        }

    }

    public void eliminararchivos() {
        /* Se prrocede a borrar el archivo temporal de las facturas descargadas*/
        //String rutaDeArchivoFacturas = "tmp/tmp_" + this.fCambiarClave.manifiestoActual.codificarManifiesto() + ".txt";
        ArchivosDeTexto archivo = new ArchivosDeTexto(manifiestoActual.getRutaArchivoDescargueFacturas());
        archivo.borrarArchivo();

        /* Se prrocede a borrar el archivo temporal de los productos por facturas descargadas*/
        //String rutaDeArchivoProductosDescargados = "tmp/tmp_" + this.fCambiarClave.manifiestoActual.codificarManifiesto() + "_productosDescargados.txt";
        archivo = new ArchivosDeTexto(manifiestoActual.getRutaArchivoDescargueporductosPorFactura());
        archivo.borrarArchivo();

        /* Se prrocede a borrar el archivo temporal de las Recogidas*/
        //String rutaDeArchivoRecogidas = "tmp/tmp_" + this.fCambiarClave.manifiestoActual.codificarManifiesto() + "_productosDescargados.txt";
        archivo = new ArchivosDeTexto(manifiestoActual.getRutArchivoRecogidasporManifiesto());
        archivo.borrarArchivo();

        /* Se prrocede a borrar el archivo temporal de las facturas no manifestadas */
        archivo = new ArchivosDeTexto(manifiestoActual.getRutArchivoFacturasSinManifestar());
        archivo.borrarArchivo();

        /* Se prrocede a borrar el archivo temporal de las facturas no manifestadas */
        archivo = new ArchivosDeTexto(manifiestoActual.getRutaArchivoSoportesDeConsignaciones());
        archivo.borrarArchivo();
    }

    public void asignarTipoDescargue() {
        if (manifiestoActual.getListaFacturasDescargadas() != null) {
            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasDescargadas()) {

                switch (obj.getIdTipoDeMovimiento()) {
                    case 2:
                        if (obj.getValorRecaudado() == 0.0) {
                            obj.setTipoDeDEscargue("E. T. Cr");

                        } else {
                            if (obj.getValorDescuento() == 0.0) {
                                obj.setTipoDeDEscargue("E. T. Cn");
                            } else {

                                obj.setTipoDeDEscargue("E. T. %");
                            }

                        }
                        break;
                    case 3:
                        obj.setTipoDeDEscargue("D. T.");
                        break;
                    case 4:
                        if (obj.getValorRecaudado() == 0.0) {
                            obj.setTipoDeDEscargue("E. N. Cr");
                        } else {
                            obj.setTipoDeDEscargue("E. N. Cn");
                        }
                        break;
                    case 5:
                        break;
                    case 6:
                        obj.setTipoDeDEscargue("R. E.");
                        break;

                    case 7:
                        obj.setTipoDeDEscargue("N. V.");
                        break;
                }

            }
        }

    }
}
