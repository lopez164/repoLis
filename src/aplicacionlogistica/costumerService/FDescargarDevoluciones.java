/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService;

import aplicacionlogistica.distribucion.formularios.FDescargarRechazosParciales;
import aplicacionlogistica.distribucion.formularios.FDescargarEntregasTotalesConDescuento;
import aplicacionlogistica.distribucion.*;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.distribucion.Threads.HiloGuardarManifiestoDescargado;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.distribucion.Threads.Hilo_HT_productosPorFactura;
import aplicacionlogistica.distribucion.Threads.hiloArmararchivoProductosPorFacturaDescargados;
import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.FBuscarManifiestos;
import aplicacionlogistica.distribucion.imprimir.ReporteDescargueDeFacturas1;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CMovimientosManifiestosfacturas;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CProductosPorFacturaDescargados;
import aplicacionlogistica.distribucion.objetos.CRecogidasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CSoportesConsignaciones;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import aplicacionlogistica.distribucion.PrincipalLogistica;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import mtto.vehiculos.CCarros;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
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
public final class FDescargarDevoluciones extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public CManifiestosDeDistribucion manifiestoActual = null;
    public CCarros carro = null;

    public CFacturasPorManifiesto facturaActual = null;

    public Inicio ini = null;
    //int numeroDeFacturasEnManifiesto = 0;
    //int cantidadFacturasDescargadasManifiesto = 0;
    //int cantidadFacturasPendientesPorDescargar = 0;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    DefaultTableModel modelo1, modelo2, modelo3;

    public Double valorTotalRecaudado = 0.0;
    public Double valorTotalRecogidas = 0.0;
    public Double valorTotalAConsignar = 0.0;
    double valorRecogida;
    double valorDescontado;

    public Double valorConsignado = 0.0;

    boolean cargado = false;
    boolean nuevo = false;
    boolean actualizar = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean estaOcupadoGrabando = false;

    public boolean isFacturaCredito = false;
    public boolean esteManifiestoEsMio = false;

    //int kilometrosEntrada;
    public int kilometrosRecorridos;
    int filaSeleccionada;
    int indiceLista = 0;
    int columna = 0;
    String mensaje = null;
    String nombreDelCliente = null;

    String rutaDeArchivoFacturasDescargadas;
    String rutaDeArchivoProductosDescargados;
    String rutaDeArchivoRecogidasDescargado;
    String rutaDeArchivoFacturasParaVolverAZonificar;
    String rutaDeArchivoSoporteConsignaciones;

    PrincipalLogistica form;

    int tipoMovimientoFactura = 0;

    public List<CFacturasPorManifiesto> listaFacturasPorManifiesto;
    public List<CFacturasPorManifiesto> listaFacturasDescargadas;
    public List<CFacturasPorManifiesto> listaFacturasPendientesPorDescargar;

    public List<CRecogidasPorManifiesto> listaDeRecogidasPorManifiesto;
    public List<CProductosPorFactura> listaDeCProductosPorFacturaDescargados;
    public List<String> listaDeFacturasNoManifestadas;
    public List<CSoportesConsignaciones> listaDeSoportesDeConsignaciones;

    public List<CFacturas> listaFacturaTemporal;

    //public Vst_FacturasPorManifiesto facturaDescargada = null;
    String senteciaSqlFacturasDescargadas = null;
    String senteciaSqlProductosPorFacturaDescargados = null;
    String senteciaSqlSoportesConsignaciones = null;

    FDescargarRechazosParciales fDescargueRechazosParciales;
    
    FDescargarEntregasTotalesConDescuento fDescargueEntregaTotalConDescuento;
    FBuscarManifiestos formulario;

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param form
     */
    public FDescargarDevoluciones(PrincipalLogistica form) {
        this.form = form;
        this.ini = form.ini;
        CargarVista();
    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param ini
     */
    public FDescargarDevoluciones(Inicio ini) {
        this.ini = ini;
        initComponents();
        cbxMovimientoFactura.removeAllItems();
        cbxCausalDeRechazoFactura.removeAllItems();
       

        for (CMovimientosManifiestosfacturas obj : ini.getListaDeMovimientosFacturas()) {
            cbxMovimientoFactura.addItem(obj.getNombreMovimientosManifiestosfacturas());
        }
        //CausalesDeRechazo
        for (CCausalesDeDevolucion obj : ini.getListaDeCausalesDeDevolucion()) {
            cbxCausalDeRechazoFactura.addItem(obj.getNombreCausalesDeDevolucion());
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

                /*Actualizamos la lista de cuentas bancarias */
                new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3)).start();

               
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
                Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
            }
            cargado = true;
        } catch (Exception ex) {
            Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }
         

        fDescargueEntregaTotalConDescuento = new FDescargarEntregasTotalesConDescuento();
        fDescargueRechazosParciales = new FDescargarRechazosParciales(this);
        formulario = new FBuscarManifiestos(ini);

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
        jPanel7 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFacturasPorVehiculo = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlEntregas = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableProductosPorFactura = new javax.swing.JTable();
        pnlRechazosTotales = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        cbxCausalDeRechazoFactura = new javax.swing.JComboBox();
        btnDescargarRechazoTotal = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        txtManifiesto = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbxMovimientoFactura = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        txtNumeroDeFactura = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        rBtnCredito = new javax.swing.JRadioButton();
        rBtnContado = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListFacturasNoManifestadas = new javax.swing.JList();
        jLabel41 = new javax.swing.JLabel();
        txtNombreConductor = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtRuta = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        lblFacturasPendientes = new javax.swing.JLabel();
        lblValorAConsignar = new javax.swing.JLabel();

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

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Formulario para Descargar Devoluciones");
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
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
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
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlEntregasLayout.setVerticalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Entregas Totales", pnlEntregas);

        pnlRechazosTotales.setAutoscrolls(true);
        pnlRechazosTotales.setDoubleBuffered(false);
        pnlRechazosTotales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Causal de Rechazo");
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

        jTabbedPane1.addTab("Rechazos Totales", pnlRechazosTotales);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("DEscargar facturas"));

        txtManifiesto.setEnabled(false);
        txtManifiesto.setInheritsPopupMenu(true);
        txtManifiesto.setName("numerico"); // NOI18N
        txtManifiesto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtManifiestoFocusGained(evt);
            }
        });
        txtManifiesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtManifiestoActionPerformed(evt);
            }
        });
        txtManifiesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtManifiestoKeyPressed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tipo De Movimiento");

        cbxMovimientoFactura.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
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

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Placa");

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

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Manifiesto");

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxMovimientoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(158, 158, 158)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(rBtnContado)
                        .addGap(18, 18, 18)
                        .addComponent(rBtnCredito))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(5, 5, 5)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxMovimientoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rBtnCredito)
                    .addComponent(rBtnContado))
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lblFacturasPendientes.setFont(new java.awt.Font("Dialog", 1, 40)); // NOI18N
        lblFacturasPendientes.setText(".");
        lblFacturasPendientes.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblFacturasPendientesMouseMoved(evt);
            }
        });

        lblValorAConsignar.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        lblValorAConsignar.setText(".");
        lblValorAConsignar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblValorAConsignarMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblFacturasPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(256, 256, 256)
                        .addComponent(lblValorAConsignar, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFacturasPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblValorAConsignar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        if (manifiestoActual != null) {

            if (esteManifiestoEsMio) {
                manifiestoActual.liberarManifiesto(true);
                manifiestoActual = null;
                esteManifiestoEsMio = false;
                limpiar();
            }
        }

        btnImprimir.setEnabled(false);
        cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
        cbxMovimientoFactura.setSelectedItem("NINGUNO");
        this.dispose();
        this.setVisible(false);

    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        limpiar();
        nuevo = true;
        pnlRechazosTotales.setEnabled(true);

        txtManifiesto.setEnabled(true);
        txtManifiesto.setEditable(true);
        jTabbedPane1.setEnabled(false);
        btnNuevo.setEnabled(false);
        txtManifiesto.requestFocus();
        cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
        cbxMovimientoFactura.setSelectedItem("NINGUNO");
        
       
        btnGrabar.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnImprimir.setEnabled(false);


        //cbxManifiestosDeDistribucion.setEnabled(true);a

        /*Actualizamos la tabla de manifiestos  pendienetes por descargar  */
        new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3)).start();

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        /*Se valida que lista de Facturas Descargadas este completa*/
        if (listaFacturasPendientesPorDescargar.size() > 0) {
            JOptionPane.showMessageDialog(this, "No se han descargado todas las facturas", "Error", JOptionPane.WARNING_MESSAGE);
            return;
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
                if (ini.verificarConexion()) {

                    try {

                        cbxMovimientoFactura.setEnabled(false);
                        rBtnContado.setEnabled(false);
                        rBtnCredito.setEnabled(false);
                        txtNumeroDeFactura.setEditable(false);
                        btnGrabar.setEnabled(false);
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

    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        if (manifiestoActual != null) {

            if (esteManifiestoEsMio) {
                manifiestoActual.liberarManifiesto(true);
                manifiestoActual = null;
                esteManifiestoEsMio = false;
            }

        }
        limpiar();
        


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jTableFacturasPorVehiculoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFacturasPorVehiculoMouseClicked

    }//GEN-LAST:event_jTableFacturasPorVehiculoMouseClicked

    private void txtNumeroDeFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaFocusGained
        txtNumeroDeFactura.setSelectionStart(0);
        txtNumeroDeFactura.setSelectionEnd(txtNumeroDeFactura.getText().length());
    }//GEN-LAST:event_txtNumeroDeFacturaFocusGained

    private void txtNumeroDeFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaKeyPressed
        try {
            this.facturaActual = null;
            CFacturas facturaTemporal = new CFacturas(ini);

            double valorFactura = 0;
            String numeroDeFactura;
            if ((evt.getKeyCode() == KeyEvent.VK_F2) && (txtNumeroDeFactura.getText().length() > 0)) {

                if (verificarQueFacturaExiste()) {
                    consultarLafactura();
                }

            }

            /* Evento al oprimir la tecla enter*/
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) && (txtNumeroDeFactura.getText().length() > 0)) {

                //numeroDeFactura="" + Integer.parseInt(txtNumeroDeFactura.getText().trim());
                numeroDeFactura = txtNumeroDeFactura.getText().trim();

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
                    for (CFacturasPorManifiesto obj : listaFacturasPorManifiesto) {
                        if (obj.getNumeroFactura().equals(numeroDeFactura)) {

                            /*Encuentra la factura en el listado del manifiesto*/
                            facturaActual = obj;
                            break;
                        }
                    }

                    // SE VALIDA QUE LA FACTURA EXISTA EN EL MANIFIESTO
                    if (facturaActual != null) {

                        facturaTemporal = new CFacturas(ini, facturaActual.getNumeroFactura());

                        CFacturasDescargadas facturaDescargada = new CFacturasDescargadas(ini);

                        /*consulta local de los de los productos de la factura para llenar
                        la tabla de los productos de la factura*/
                        facturaTemporal.setListaCProductosPorFactura(false);

                        if (facturaTemporal.getListaCProductosPorFactura().size() > 0) {

                            /* Se traen los productos de la factura en la BBDD remota */
                            new Thread(new Hilo_HT_productosPorFactura(ini, facturaTemporal)).start();
                            listaFacturaTemporal.add(facturaTemporal);

                            // SE LLENA LA TABLA DE PRODUCTOS CONTENIDOS EN LA FACTURA
                            valorFactura = llenarTablaProductosPorFactura(facturaTemporal);


                            // SE VALIDA QUE SE HAYA ELEGIDO UN MOVIMIENTO PARA LA FACTURA
                            if (!"NINGUNO".equals(cbxMovimientoFactura.getSelectedItem().toString())) {

                                switch (tipoMovimientoFactura) {
                                    case 1: // caso no utilizado, pues identifica un movimiento no valido
                                        /* NINGUNO */
                                        break;

                                    // DESCARGUE DE TIPO DE MOVIMIENTO ENTREGA TOTAL
                                    case 2:
                                        // SE VALIDA SI LA FACTURA NO ESTA DESCARGADA Y SE DESCARGA LA ENTREGA TOTAL

                                        if (!estaDescargadaLaFactura(numeroDeFactura)) {

                                            /*Utilizando el objeto Cfacturas se bloqua la factura para que no pueda ser anexada a otro manifiesto*/
                                            //facturaTemporal.liberarFactura(false);

                                            /*Se activa cuando hay descuento en la facura*/
                                           
                                            txtNumeroDeFactura.requestFocus();
                                        }
                                        break;

                                    // DESCARGUE DE RECHAZO TOTAL
                                    case 3:
                                        if (!estaDescargadaLaFactura(numeroDeFactura)) {

                                            

                                        }

                                        break;
                                    case 4: //DESCARGUE DE RECHAZO PARCIAL

                                       

                                        if (!estaDescargadaLaFactura(numeroDeFactura)) {

                                            fDescargueRechazosParciales.toFront();
                                            fDescargueRechazosParciales.setClosable(true);
                                            fDescargueRechazosParciales.setVisible(true);
                                            fDescargueRechazosParciales.setTitle("Formulario para Descargar Entregas parciales con y sin Descuentos");
                                            fDescargueRechazosParciales.setLocation((ini.getDimension().width - fDescargueRechazosParciales.getSize().width) / 2, (ini.getDimension().height - fDescargueRechazosParciales.getSize().height) / 2);

                                            fDescargueRechazosParciales.dDescargarDevoluciones = this;
                                           // fDescargueRechazosParciales.facturaActual = this.facturaActual;
                                            fDescargueRechazosParciales.llenarFormulario();
                                            this.getDesktopPane().add(fDescargueRechazosParciales);
                                            txtNumeroDeFactura.setEnabled(false);
                                            fDescargueRechazosParciales.toFront();
                                        }

                                        break;

                                    case 5: //  DESCARGUE DE RECOGIDAS

                                      
                                        break;

                                    //  VUELVE A LA ZONA
                                    case 6:
                                        // SE VALIDA SI LA FACTURA NO ESTA DESCARGADA Y SE DESCARGA LA ENTREGA TOTAL

                                        if (!estaDescargadaLaFactura(numeroDeFactura)) {
                                            facturaTemporal.liberarFactura(false);

                                            this.descargarReEnvios(facturaDescargada);
                                            txtNumeroDeFactura.requestFocus();

                                        }
                                        break;

                                }

                                valorTotalAConsignar = valorTotalRecaudado - valorTotalRecogidas;

                             

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
                        /*Se valida que el dato no exista en el Jlist, ni en el archivo*/
                        if (!verificarQueFacturaExiste()) {
                            listaDeFacturasNoManifestadas.add(numeroDeFactura);

                            anexarFacturaActualALalista();

                            ArchivosDeTexto archivo = new ArchivosDeTexto();
                            archivo.insertarLineaEnFichero(numeroDeFactura, manifiestoActual.getRutArchivoFacturasSinManifestar());
                            JOptionPane.showInternalMessageDialog(this, "La factura No existe en el manifiesto N° " + manifiestoActual.codificarManifiesto(), "Factura no existe en el sistema", JOptionPane.ERROR_MESSAGE);                //txtNumeroDeFactura.requestFocus();
                        }

                    }
                } catch (Exception ex) {
                    System.out.println("Error en txtNumeroDeFacturaKeyPressed ");
                    Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
                    //txtNumeroDeFactura.requestFocus();
                }

                txtNumeroDeFactura.requestFocus();
            }
        } catch (Exception ex) {
            Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_txtNumeroDeFacturaKeyPressed

    private void consultarLafactura() {
        facturaActual = null;
        try {
            // CFacturas fac = new CFacturas(ini, txtNumeroDeFactura.getText().trim());
            // SE CREA EL OBJETO FACTURA
            for (CFacturasPorManifiesto obj : listaFacturasPorManifiesto) {
                if (obj.getNumeroFactura().equals(txtNumeroDeFactura.getText().trim())) {
                    facturaActual = obj;

                    break;
                }
            }
            if (facturaActual != null) {

                FConsultarFacturasRemoto formulario = new FConsultarFacturasRemoto(ini, new CFacturas(ini, facturaActual.getNumeroFactura()));
                this.getDesktopPane().add(formulario);
                formulario.toFront();
                formulario.setClosable(true);
                formulario.setVisible(true);
                formulario.setTitle("Formulario para consultar Facturas");
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                //form.setSize(PrincipalLogistica.escritorio.getSize());
                formulario.setLocation((screenSize.width - formulario.getSize().width) / 2, (screenSize.height - formulario.getSize().height) / 2);
                formulario.show();
            } else {
                JOptionPane.showInternalMessageDialog(this, "La factura no aparece en el manifiesto", "Factura no visible ", 2);
            }

        } catch (Exception ex) {
            Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private double llenarTablaProductosPorFactura(CFacturas facturaTemporal) {
        modelo1 = (DefaultTableModel) jTableProductosPorFactura.getModel();
        double valorFactura = 0.0;

        // se nexan los productos a la tabla de productos por factura
        for (CProductosPorFactura obj : facturaTemporal.getListaCProductosPorFactura()) {

            int fila = jTableProductosPorFactura.getRowCount();

            modelo1.addRow(new Object[jTableProductosPorFactura.getRowCount()]);
            jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()*obj.getCantidad()), fila, 5);
          
           

        }
        return valorFactura;
    }

    /**
     * Método encargado de realizar el descargue de las entregas totales de una
     * ruta
     *
     * @param facturaDescargada corresponde al numero de la factura que salió a
     * distribución
     *
     */
    private void descargarEntregasTotales(CFacturas facturaTemporal) throws Exception {
        double valorRechazoFactura = 0.0;
        double valorDescuento = 0.0;
        double valorIvaFactura = facturaActual.getValorTotalFactura() - facturaActual.getValorFacturaSinIva();
        double valorTotalRecaudarFactura = 0.0;

        CFacturasDescargadas facturaDescargada = new CFacturasDescargadas(ini);

        if (!isFacturaCredito) {

            valorDescuento = (facturaActual.getValorFacturaSinIva() * 0.00); // nombreDelCliente.getPorcentajeDescuento());
            valorTotalRecaudarFactura = (facturaActual.getValorTotalFactura() - valorDescuento) - valorRechazoFactura;
            facturaActual.setValorDescuento(valorDescuento);
            facturaActual.setValorRecaudado(valorTotalRecaudarFactura);

            // SI LA FACTURA ES DE CREDITO
        } else {
            valorDescuento = (0.0);
            valorTotalRecaudarFactura = 0.0;
            facturaActual.setValorDescuento(0.0);
            facturaActual.setValorRecaudado(0.0);

        }

        facturaDescargada.setConsecutivo(facturaActual.getConsecutivo());
        facturaDescargada.setNumeroManifiesto(manifiestoActual.getNumeroManifiesto());
        facturaDescargada.setAdherenciaDescargue(filaSeleccionada + 1);
        facturaDescargada.setNumeroFactura(facturaActual.getNumeroFactura());
        facturaDescargada.setValorRechazo(0.0);
        facturaDescargada.setValorDescuento(valorDescuento);
        facturaDescargada.setValorRecaudado(valorTotalRecaudarFactura);
        facturaDescargada.setIdTipoDeMovimiento(2);
        facturaDescargada.setCausalDeRechazo(1);
        facturaDescargada.setActivo(1);

        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

        // se anexa el registro a la tabla de facturas por numeroDeManifiesto
        filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

        modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
        jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente

        if (facturaActual.getValorRecaudado() == 0.0) {
            jTableFacturasPorVehiculo.setValueAt("E. T. Cr", filaSeleccionada, 3); // nombre del nombreDelCliente
        } else {
            jTableFacturasPorVehiculo.setValueAt("E. T. Cn", filaSeleccionada, 3); // nombre del nombreDelCliente
        }

        jTableFacturasPorVehiculo.setValueAt(nf.format(facturaActual.getValorRecaudado()), filaSeleccionada, 4);

        // se ubica en la fila insertada
        jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

//            for(Vst_ProductosPorFactura obj : facturaTemporal.getListaProductosPorFactura()){
//                
//            }
//            JOptionPane.showInternalMessageDialog(this, "El sistema el formulario", "Error al cerrar", JOptionPane.ERROR_MESSAGE);
        facturaDescargada.setConsecutivo(facturaActual.getConsecutivo());
        facturaDescargada.setNumeroManifiesto(manifiestoActual.getNumeroManifiesto());
        facturaDescargada.setAdherenciaDescargue(filaSeleccionada + 1);
        facturaDescargada.setNumeroFactura(facturaActual.getNumeroFactura());
        facturaDescargada.setValorRechazo(0.0);
        facturaDescargada.setValorDescuento(valorDescuento);
        facturaDescargada.setValorRecaudado(valorTotalRecaudarFactura);
        facturaDescargada.setIdTipoDeMovimiento(2);
        facturaDescargada.setCausalDeRechazo(1);
        facturaDescargada.setActivo(1);

        //rutaDeArchivoFacturasDescargadas = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_FacturasDescargados.txt";
        rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
        // ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + ".txt";
        senteciaSqlFacturasDescargadas = facturaDescargada.getCadenaConLosCampos();

        Inicio.GuardaConsultaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);
        //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
        // ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturasDescargadas);

        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        // archivo.insertarConsultaEnFichero(senteciaSqlFacturasDescargadas, ruta);
        // archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);
        // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
        for (CFacturasPorManifiesto obj : listaFacturasPendientesPorDescargar) {

            if (obj.getNumeroFactura().equals(facturaDescargada.getNumeroFactura())) {
                obj.setValorRecaudado(facturaDescargada.getValorRecaudado());
                obj.setIdTipoDeMovimiento(2);

                if (facturaDescargada.getValorRecaudado() == 0.0) {
                    obj.setTipoDeDEscargue("E. T. Cr");

                } else {
                    obj.setTipoDeDEscargue("E. T. Cn");
                }

                /*Se añade la factura a la lista de facturas a Descargar*/
                listaFacturasDescargadas.add(obj);

                /*Se elimina la factura de la lista pendientes por descargar*/
                listaFacturasPendientesPorDescargar.remove(obj);
                break;
            }
        }

        calcularValoraAConsignar();

      

        lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());
        // rBtnContado.setSelected(true);

        if (listaFacturasPendientesPorDescargar.isEmpty()) {
            btnGrabar.setEnabled(true);
            cbxCausalDeRechazoFactura.setEnabled(false);
            btnDescargarRechazoTotal.setEnabled(false);
           

            btnGrabar.requestFocus();
        }
        // armarArchivoProductosPorFacturadescargados(facturaDescargada);
       // new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this, 2, facturaTemporal, facturaDescargada.getConsecutivo())).start();//-->Error v 

        if (listaFacturasPorManifiesto.size() == listaFacturasDescargadas.size()) {
            if (valorTotalAConsignar >= valorConsignado) {
                btnGrabar.setEnabled(true);
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
    public void descargarEntregasTotalesConDescuento(CFacturasDescargadas facturaDescargada) throws Exception {

        // SI LA FACTURA ES DE CONTADO
        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

        // se anexa el registro a la tabla de facturas por numeroDeManifiesto
        filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

        modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
        jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
        jTableFacturasPorVehiculo.setValueAt(facturaDescargada.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt("E. T. %", filaSeleccionada, 3); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt(nf.format(facturaDescargada.getValorRecaudado()), filaSeleccionada, 4);

        // se ubica en la fila insertada
        jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

        rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
        senteciaSqlFacturasDescargadas = facturaDescargada.getCadenaConLosCampos();

        //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
        ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturasDescargadas);

        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        //archivo.insertarConsultaEnFichero(senteciaSqlFacturasDescargadas, ruta);
        archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);

        // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
        for (CFacturasPorManifiesto obj : listaFacturasPendientesPorDescargar) {
            if (obj.getNumeroFactura().equals(facturaDescargada.getNumeroFactura())) {
                obj.setValorRecaudado(facturaDescargada.getValorRecaudado());
                obj.setTipoDeDEscargue("E. T. %");
                obj.setIdTipoDeMovimiento(2);
                /*Se añade la factura a la lista de facturas a Descargar*/
                listaFacturasDescargadas.add(obj);

                /*Se elimina la factura de la lista pendientes por descargar*/
                listaFacturasPendientesPorDescargar.remove(obj);
                break;
            }
        }

        calcularValoraAConsignar();

      

        // armarArchivoProductosPorFacturadescargados(facturaDescargada);
        //new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this, 2)).start(); //--> error
        lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());
        rBtnContado.setSelected(true);
      

        if (listaFacturasPendientesPorDescargar.isEmpty()) {
            btnGrabar.setEnabled(true);
            cbxCausalDeRechazoFactura.setEnabled(false);
            btnDescargarRechazoTotal.setEnabled(false);
            

            btnGrabar.requestFocus();
        }

        if (listaFacturasPorManifiesto.size() == listaFacturasDescargadas.size()) {
            if (valorTotalAConsignar >= valorConsignado) {
                btnGrabar.setEnabled(true);
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
    private void descargarRechazosTotales(CFacturasDescargadas facturaDescargada) throws Exception {

        if (!estaDescargadaLaFactura(facturaDescargada.getNumeroFactura())) {

            modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

            // se anexa el registro a la tabla de facturas por numeroDeManifiesto
            filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

            modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
            jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
            jTableFacturasPorVehiculo.setValueAt(facturaActual.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
            jTableFacturasPorVehiculo.setValueAt(this.nombreDelCliente, filaSeleccionada, 2); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt("D. T.", filaSeleccionada, 3); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt(nf.format(0.0), filaSeleccionada, 4);

            // se ubica en la fila insertada
            jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

            int causalrechazo = 0;

            for (CCausalesDeDevolucion obj : ini.getListaDeCausalesDeDevolucion()) {
                if (obj.getNombreCausalesDeDevolucion().equals(cbxCausalDeRechazoFactura.getSelectedItem())) {
                    causalrechazo = obj.getIdCausalesDeDevolucion();
                    facturaDescargada.setCausalDeRechazo(causalrechazo);
                }
            }

            facturaDescargada.setValorDescuento(0.0);
            facturaDescargada.setValorRecaudado(0.0);
            facturaDescargada.setIdTipoDeMovimiento(3);
            facturaDescargada.setCausalDeRechazo(causalrechazo);
            facturaDescargada.setActivo(1);

            // = "tmp/tmp_" + this.formDescargarFacturas.manifiestoActual.codificarManifiesto() + "_FacturasDescargados.txt";
            //ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + ".txt";
            // rutaDeArchivoFacturasDescargadas = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_FacturasDescargados.txt";
            //rutaDeArchivoFacturasDescargadas = "" + (new File(".").getAbsolutePath()).replace(".", "") + manifiestoActual.getRutaArchivoDescargueFacturas();
            rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
            senteciaSqlFacturasDescargadas = facturaDescargada.getCadenaConLosCampos();

            //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
            ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturasDescargadas);

            // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
            //archivo.insertarConsultaEnFichero(senteciaSqlFacturasDescargadas, ruta);
            archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);

            // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
            for (CFacturasPorManifiesto obj : listaFacturasPendientesPorDescargar) {
                if (obj.getNumeroFactura().equals(facturaDescargada.getNumeroFactura())) {
                    obj.setValorRecaudado(facturaDescargada.getValorRecaudado());
                    obj.setTipoDeDEscargue("D. T.");
                    obj.setIdTipoDeMovimiento(3);
                    /*Se añade la factura a la lista de facturas a Descargar*/
                    listaFacturasDescargadas.add(obj);

                    /*Se elimina la factura de la lista pendientes por descargar*/
                    listaFacturasPendientesPorDescargar.remove(obj);
                    break;
                }
            }

            calcularValoraAConsignar();

           
            rBtnContado.setSelected(true);

            // armarArchivoProductosPorFacturadescargados(facturaDescargada);
            for (CFacturas facturaTemporal : listaFacturaTemporal) {
                if (facturaTemporal.getNumeroDeFactura().equals(facturaDescargada.getNumeroFactura())) {
//                    new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this, 3, facturaTemporal, facturaDescargada.getConsecutivo())).start();
                    break;
                }
            }

            lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());
            rBtnContado.setSelected(true);
            rBtnContado.setSelected(true);

            if (listaFacturasPendientesPorDescargar.isEmpty()) {
                btnGrabar.setEnabled(true);
                cbxCausalDeRechazoFactura.setEnabled(false);
                btnDescargarRechazoTotal.setEnabled(false);
                btnGrabar.requestFocus();
            }

            if (listaFacturasPorManifiesto.size() == listaFacturasDescargadas.size()) {
                if (valorTotalAConsignar >= valorConsignado) {
                    btnGrabar.setEnabled(true);
                }
            }

            txtNumeroDeFactura.requestFocus();
        }
    }

    /**
     * Método encargado de realizar el descargue de los rechazos parciales de
     * una ruta
     *
     * @param arrProductosDescargados corresponde al listado de los productos
     * que tiene la factura
     *
     */
    protected void descargarRechazosParciales(List<CProductosPorFacturaDescargados> arrProductosDescargados, CFacturasDescargadas facturaDescargada) throws Exception {

        if (!estaDescargadaLaFactura(facturaDescargada.getNumeroFactura())) {

            modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

            // se anexa el registro a la tabla de facturas por numeroDeManifiesto
            filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

            modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
            jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
            jTableFacturasPorVehiculo.setValueAt(facturaActual.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
            jTableFacturasPorVehiculo.setValueAt(facturaActual.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
            if (facturaDescargada.getValorRecaudado() > 0.0) {
                jTableFacturasPorVehiculo.setValueAt("E. N. Cn", filaSeleccionada, 3); // nombre del nombreDelCliente
            } else {
                jTableFacturasPorVehiculo.setValueAt("E. N. Cr", filaSeleccionada, 3); // nombre del nombreDelCliente
            }

            jTableFacturasPorVehiculo.setValueAt(nf.format(facturaDescargada.getValorRecaudado()), filaSeleccionada, 4);

            // se ubica en la fila insertada
            jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

            rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
            senteciaSqlFacturasDescargadas = facturaDescargada.getCadenaConLosCampos();

            //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
            ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturasDescargadas);

            // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
            //archivo.insertarConsultaEnFichero(senteciaSqlFacturasDescargadas, ruta);
            archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);

            // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
            for (CFacturasPorManifiesto obj : listaFacturasPendientesPorDescargar) {
                if (obj.getNumeroFactura().equals(facturaDescargada.getNumeroFactura())) {
                    obj.setValorRecaudado(facturaDescargada.getValorRecaudado());
                    if (facturaDescargada.getValorRecaudado() == 0.0) {
                        obj.setTipoDeDEscargue("E. N. Cr");
                    } else {
                        obj.setTipoDeDEscargue("E. N. Cn");
                    }

                    obj.setIdTipoDeMovimiento(4);
                    /*Se añade la factura a la lista de facturas a Descargar*/
                    listaFacturasDescargadas.add(obj);

                    /*Se elimina la factura de la lista pendientes por descargar*/
                    listaFacturasPendientesPorDescargar.remove(obj);
                    break;
                }
            }


            /* Se calcula el valor recaudado */
            calcularValoraAConsignar();

      

            txtNumeroDeFactura.setEnabled(true);

            lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());
            rBtnContado.setSelected(true);

            if (listaFacturasPendientesPorDescargar.isEmpty()) {
                btnGrabar.setEnabled(true);
                cbxCausalDeRechazoFactura.setEnabled(false);
                btnDescargarRechazoTotal.setEnabled(false);
                btnGrabar.requestFocus();
            }

            if (listaFacturasPorManifiesto.size() == listaFacturasDescargadas.size()) {
                if (valorTotalAConsignar >= valorConsignado) {
                    btnGrabar.setEnabled(true);
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
     * @param nombreCliente corresponde al nombre del cliente que hizo el pedido

 //protected void agregarRecogida(CFacturas fac, String
 nombreCliente) {
     */
    protected void agregarRecogida(CFacturasDescargadas fac, String nombreCliente) {

        CRecogidasPorManifiesto rxm = new CRecogidasPorManifiesto(ini);

        rxm.setIdRecogidasPorManifiesto(fac.getConsecutivo());
        rxm.setIdNumeroManifiesto(fac.getNumeroManifiesto());
        rxm.setNumeroFactura(fac.getNumeroFactura());
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

 

        /*Se agrega la recogida a l lista repectiva*/
        listaDeRecogidasPorManifiesto.add(rxm);
        manifiestoActual.setListaDeRecogidasPorManifiestoDescargadas(listaDeRecogidasPorManifiesto);

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
    private void descargarReEnvios(CFacturasDescargadas facturaDescargada) throws Exception {

        facturaActual.setValorDescuento(0.0);
        facturaActual.setValorRecaudado(0.0);
        //txtNumeroDeFactura.setText(nf.format(0.0));

        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

        // se anexa el registro a la tabla de facturas por numeroDeManifiesto
        filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();

        modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
        jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
        jTableFacturasPorVehiculo.setValueAt(facturaActual.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
        jTableFacturasPorVehiculo.setValueAt(this.nombreDelCliente, filaSeleccionada, 2); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt("R. E.", filaSeleccionada, 3); // nombre del nombreDelCliente
        jTableFacturasPorVehiculo.setValueAt(nf.format(0.0), filaSeleccionada, 4);

        // se ubica en la fila insertada
        jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);

       
        facturaDescargada.setConsecutivo(facturaActual.getConsecutivo());
        facturaDescargada.setNumeroManifiesto(facturaActual.getNumeroManifiesto());
        facturaDescargada.setAdherenciaDescargue(listaFacturasDescargadas.size() + 1);
        facturaDescargada.setNumeroFactura(facturaActual.getNumeroFactura());
        facturaDescargada.setValorRechazo(0.0);
        facturaDescargada.setValorDescuento(0.0);
        facturaDescargada.setValorRecaudado(0.0);
        facturaDescargada.setIdTipoDeMovimiento(6); // vuelve a zona
        facturaDescargada.setCausalDeRechazo(18); // FUERA DE FECHA U HORARIO
        facturaDescargada.setActivo(1);

        rutaDeArchivoFacturasDescargadas = manifiestoActual.getRutaArchivoDescargueFacturas();
        senteciaSqlFacturasDescargadas = facturaDescargada.getCadenaConLosCampos();

        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        Inicio.GuardaConsultaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);

        // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
        for (CFacturasPorManifiesto obj : listaFacturasPendientesPorDescargar) {
            if (obj.getNumeroFactura().equals(facturaDescargada.getNumeroFactura())) {
                obj.setValorRecaudado(facturaDescargada.getValorRecaudado());
                obj.setTipoDeDEscargue("R. E.");
                obj.setIdTipoDeMovimiento(6);
                /*Se añade la factura a la lista de facturas a Descargar*/
                listaFacturasDescargadas.add(obj);

                /*Se elimina la factura de la lista pendientes por descargar*/
                listaFacturasPendientesPorDescargar.remove(obj);
                break;
            }
        }

        calcularValoraAConsignar();

        // armarArchivoProductosPorFacturadescargados(facturaDescargada);
   //     new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this, 6)).start();

        lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());

        if (listaFacturasPendientesPorDescargar.isEmpty()) {
            btnGrabar.setEnabled(true);
            cbxCausalDeRechazoFactura.setEnabled(false);
            btnDescargarRechazoTotal.setEnabled(false);
            btnGrabar.requestFocus();
        }

        if (listaFacturasPorManifiesto.size() == listaFacturasDescargadas.size()) {
            if (valorTotalAConsignar >= valorConsignado) {
                btnGrabar.setEnabled(true);
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

        /*Se agrega la consignacion a la lista repectiva*/
        listaDeSoportesDeConsignaciones.add(soporte);
        manifiestoActual.setListaDeSoportesConsignaciones(listaDeSoportesDeConsignaciones);

  
        if (listaFacturasPorManifiesto.size() == listaFacturasDescargadas.size()) {
            if (valorTotalAConsignar >= valorConsignado) {
                btnGrabar.setEnabled(true);
            }
        }

    }

    private void armarArchivoProductosPorFacturadescargados(CFacturasDescargadas facturaDescargada, CFacturas facturaTemporal) throws Exception {
        ArchivosDeTexto archivo;
        for (CProductosPorFactura obj : facturaTemporal.getListaCProductosPorFactura()) {
            CProductosPorFacturaDescargados pxfd = new CProductosPorFacturaDescargados(ini);

            pxfd.setConsecutivoFacturasDescargadas(facturaDescargada.getConsecutivo());

            /* Acá se declara un objeto producto por factura y nos traemos el consecutivo asignado en la
             importacio de datos desde excel, guardado en la BBDD remota*/
            CProductosPorFactura pxf = new CProductosPorFactura(ini);
           // pxf.setConsecutivo(obj.getNumeroFactura(), obj.getCodigoProducto());

            /* acá se asigna el consecutivo respectivo al objeto producto por factura descargado, asignando
             el consecutivo recuperado en la operaciń anterior*/
            pxfd.setConsecutivoProductosPorFactura(pxf.getConsecutivoProductoPorFactura());

            pxfd.setValorDescuentoFactura(0.0);
            pxfd.setCantidadRechazadaItem(0);
            pxfd.setValorRechazoFactura(0.0);
            pxfd.setCantidadEntregadaItem(obj.getCantidad());
            pxfd.setValorTotalRecaudadoProducto(obj.getValorUnitarioConIva()*obj.getCantidad());
            pxfd.setEntregado(1);
            pxfd.setCausalDeRechazo(1);
            pxfd.setActivo(1);

            //ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_productosDescargados.txt";
            rutaDeArchivoProductosDescargados = manifiestoActual.getRutaArchivoDescargueporductosPorFactura();

            archivo = new ArchivosDeTexto(rutaDeArchivoProductosDescargados);

            senteciaSqlProductosPorFacturaDescargados = pxfd.getCadenaConLosCampos();
            archivo.insertarLineaEnFichero(senteciaSqlProductosPorFacturaDescargados, rutaDeArchivoProductosDescargados);

            listaDeCProductosPorFacturaDescargados.add(pxfd);

        }
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
            for (CFacturasPorManifiesto obj : listaFacturasDescargadas) {
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
                    borrado = true;

                }
            }

        } catch (Exception ex) {
            Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
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
                    manifiestoActual.liberarManifiesto(true);
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

//        manifiestoActual.setVistaFacturasDescargadas(listaFacturasDescargadas);
        manifiestoActual.setListaDeSoportesConsignaciones(listaDeSoportesDeConsignaciones);

        ReporteDescargueDeFacturas1 demo = new ReporteDescargueDeFacturas1(ini, manifiestoActual);
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void txtManifiestoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtManifiestoFocusGained
        txtManifiesto.setSelectionStart(0);
        txtManifiesto.setSelectionEnd(txtManifiesto.getText().length());
    }//GEN-LAST:event_txtManifiestoFocusGained

    private void txtManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtManifiestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManifiestoActionPerformed

    private void txtManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtManifiestoKeyPressed
        /*Evento al oprimir la tecla F2*/
        if (evt.getKeyCode() == KeyEvent.VK_F2) {

            manifiestoActual = null;
            //formulario.fDescargarDe = this;
            formulario.setIni(this.ini);
            formulario.llenarTabla();

            formulario.setClosable(true);
            formulario.setVisible(true);
            formulario.setTitle("Formulario para Buscar manifiestos pendientes de Descargue");
            
            formulario.setLocation((this.ini.getDimension().width - formulario.getSize().width) / 2, (this.ini.getDimension().height - formulario.getSize().height) / 2);

            this.getDesktopPane().add(formulario);
            formulario.show();
            formulario.toFront();

        }
        /*Evento al oprimir la tecla Enter */
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            manifiestoActual = null;

            /* Se realiza la consulta para buscar el manifiesto que se va a descargar*/
            new Thread(new HiloConsultarManifiesto(ini, this)).start();

        }

    }//GEN-LAST:event_txtManifiestoKeyPressed

    protected void consultarManifiesto_() {
//        /**
//         * Colocar hilo donde se muestra el gif del proceso
//         */
//        try {
//
//            manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()));
//
//            // se valida que el numeroDeManifiesto exista
//            // si  no existe el numeroDeManifiesto
//            if (manifiestoActual.getNumeroManifiesto() == 0) {
//                manifiestoActual = null;
//                JOptionPane.showInternalMessageDialog(this, "ESE MANIFIESTO NO EXISTE EN LA BASE DE DATOS ", "Error", JOptionPane.ERROR_MESSAGE);
//                txtManifiesto.requestFocus();
//                txtManifiesto.requestFocus();
//
//                // si el manifiesto existe   
//            } else {
//
//                // LISTAS DONDE SE GUARDAN TODAS LAS FACTURAS DEL MANIFIESTO
////                listaDeCFacturasEnManifiesto = new ArrayList(); // CFacturas
//                listaDeFacturasPorManifiesto = new ArrayList(); //CFacturasPorManifiesto
//                vistaListaDeCFacturas = new ArrayList(); // Vst_FacturasPorManifiesto
//
//                // LISTAS DONDE SE GUARDAN  LAS FACTURAS DESCARGADAS
//                listaCFacturasDescargadas = new ArrayList(); // CFacturas
//
//                // LISTAS DONDE SE GUARDAN TODAS LAS FACTURAS PENDIENTES POR DESCARGAR
//                listaCFacturasPendientesPorDescargar = new ArrayList(); //  CFacturas
//
//                // LISTA DONDE SE GUARDA TODOS LOS PRODUCTOS DESCARGADOS
//                listaDeCProductosPorFacturaDescargados = new ArrayList();  //CProductosPorFacturaDescargados
//
//                // Se valida que el manifiesto no esté siendo usado por otra persona en el descargue
//                if (manifiestoActual.getIsFree() == 0) {
//                    JOptionPane.showInternalMessageDialog(this, "Este manifiesto No se encuentra disponible,"
//                            + "está siendo utilizado  por otro usuario ", "Error", JOptionPane.ERROR_MESSAGE);
//
//                    // Si el mnifiesto se encuentra disponible
//                } else {
//
//                    // Se bloquea el manifiesto para que no sea utilizado.
//                    manifiestoActual.liberarManifiesto(false);
//
//                    manifiestoActual.setListaCFacturasCamdun();
//                    // listaDeCFacturasEnManifiesto = manifiestoActual.getListaCFacturasCamdun(); // CFacturas
//
//                    manifiestoActual.setListaFacturasPorManifiesto();
//                    listaDeFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto(); //CFacturasPorManifiesto
//
//                    manifiestoActual.setVstListadoDeFacturasEnManifiesto();
//                    vistaListaDeCFacturas = manifiestoActual.getVstListadoDeFacturasEnManifiesto(); // Vst_FacturasPorManifiesto
//
//                    //********************************************************
//                    //String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + ".txt";
//                    String rutaDeArchivoFacturas = "tmp/tmp_" + manifiestoActual.codificarManifiesto() + ".txt";
//                    String rutaDeArchivoProductosDescargados = "tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_productosDescargados.txt";
//
//                    File fichero = new File(rutaDeArchivoFacturas);
//
//                    //  SI EL FICHERO EXISTE, ES PORQUE YA SE HABIA INICIADO UN PROCESO DE DESCARGUE DE FACTURAS
//                    if (fichero.exists()) {
//
//                        // SE RECUPERAN LOS DATOS DE LAS FACTURAS DESCARGADAS
//                        manifiestoActual.setListaFacturasDescargadas(fichero);
//                        listaFacturasDescargadas = manifiestoActual.getListaFacturasDescargadas(); // CFacturasDescargadas
//
//                        recuperarFacturasPendientesPorDescargar();
//
//                        fichero = new File(manifiestoActual.getRutaArchivoDescargueporductosPorFactura());
//                        manifiestoActual.setListaCProductosPorFacturaDescargados(fichero);
//                        listaDeCProductosPorFacturaDescargados = manifiestoActual.getListaCProductosPorFacturaDescargados();
//
//                        // SINO EXISTE EL FICHERO PUEDE SUCEDER 2 CASOS
//                        // 1. EL MANIFIESTO SE ENCUENTRA EN DISTRIBUCIO OSEA ESTADO=3 Y HASTA AHORA SE
//                        //      INCIA EL PROCESO DE DESCARGUE DEL MANIFIESTO
//                        // 2. EL MANIFIESTO  YA SE ENCUENTRA DESCARGADO
//                    } else // SE VALIDA SI EL MANIFEISTO ESTA EN DISTRIBUCION
//                    if (manifiestoActual.getEstadoManifiesto() == 3) {
//
//                        // SE CREA EL ARCHIVO PARA GUARDAR LOS REGISTROS TEMPORALMENTE
//                        //rutaDeArchivoFacturas = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + ".txt";
//                        fichero = new File(manifiestoActual.getRutaArchivoDescargueFacturas());
//                        recuperarFacturasPendientesPorDescargar();
//
//                        // MANIFIESTO DESCARGADO    
//                    } else {
//
//                        manifiestoActual.setListaFacturasDescargadas();
//                        listaFacturasDescargadas = manifiestoActual.getListaFacturasDescargadas();
//
//                        //recuperarFacturasPendientesPorDescargar();
//
//                        manifiestoActual.setListaCProductosPorFacturaDescargados();
//                        listaDeCProductosPorFacturaDescargados = manifiestoActual.getListaCProductosPorFacturaDescargados();
//
//                    }
//
//                    /* SE BLOQUEA MANIFIESTO PARA QUE NO PUEDA SER UTLIZADO POR OTRO USUARIO */
//                    manifiestoActual.liberarManifiesto(false);
//
//                    txtManifiesto.setEditable(false);
//                    txtPlaca.setText(manifiestoActual.getVehiculo());
//                    txtKilometrosEntrada.setEnabled(true);
//                    txtKilometrosEntrada.setEditable(true);
//
//                    cbxMovimientoFactura.setEnabled(true);
//                    chkDescuento.setEnabled(true);
//                    rBtnContado.setEnabled(true);
//                    rBtnCredito.setEnabled(true);
//                    cbxMovimientoFactura.setSelectedItem("NINGUNO");
//                    cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
//
//                    jTabbedPane1.setEnabled(true);
//
//                    /* SWITCH PARA DETERMINAR EL MOVIMIENTO DEL MANIFIESTO */
//                    switch (manifiestoActual.getEstadoManifiesto()) {
//                        case 0:
//
//                            break;
//                        // MANIFIESTO SOLAMENTE CREADO
//                        case 1:
//                            JOptionPane.showInternalMessageDialog(this, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
//                                    + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", 0);
//                            break;
//
//                        // MANIFIESTO CREAD PER NO HA SALIDO A DISTRIBUCION
//                        case 2:
//                            JOptionPane.showInternalMessageDialog(this, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
//                                    + "NO SE HA GRABADO EN EL SISTEMA", "Error", 0);
//                            break;
//
//                        // MANIFIESTO GRABADO Y ESTA EN DISTRIBUCION
//                        case 3:
//                            if (manifiestoActual.getListaFacturasDescargadas() == null) {
//                                listaFacturasDescargadas = new ArrayList<>();
//                                //manifiestoActual.setListaFacturasDescargadas(listaFacturasDescargadas);
//                            }
//
//                            if (manifiestoActual.getListaFacturasDescargadas().size() > 0) {
//                                llenarjTableFacturasPorVehiculo();
//                                txtKilometrosEntrada.requestFocus();
//
//                            } else {
//                                lblFacturasPendientes.setText(" " + (manifiestoActual.getListaFacturasPorManifiesto().size()
//                                        - manifiestoActual.getListaFacturasDescargadas().size()));
//
//                                txtKilometrosSalida.setText("" + manifiestoActual.getKmSalida());
//                                txtKilometrosEntrada.requestFocus();
//                            }
//                            break;
//
//                        // MANIFIESTO DESCARGADO Y CUADRADO
//                        case 4:
//
//                            llenarjTableFacturasPorVehiculo();
//
//                            JOptionPane.showInternalMessageDialog(this, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
//                                    + "YA ESTA DESCARGADO DEL SISTEMA", "Error", 0);
//                            btnImprimir.setEnabled(true);
//
//                            break;
//
//                    }
//
//                }
//
//            }
//
//        } catch (Exception ex) {
//            System.out.println("Error en txtManifiestoKeyPressed ");
//            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        /**
//         * Detener hilo donde se muestra el gif del proceso
//         */
    }

    protected void llenarManifiesto(CManifiestosDeDistribucion man) {
        this.manifiestoActual = man;
        try {

            //listaFacturasDescargadas = man.getListaFacturasDescargadas();
            man.setListaFacturasPorManifiesto();
            // listaDeFacturasPorManifiesto = man.getListaFacturasPorManifiesto();
            // numeroDeFacturasEnManifiesto = listaDeFacturasPorManifiesto.size();
            //cantidadFacturasDescargadasManifiesto = listaFacturasDescargadas.size();
            txtManifiesto.setEditable(false);
            txtPlaca.setText(man.getVehiculo());
           

            // cbxMovimientoFactura.setEnabled(true);
            if (man.getListaFacturasDescargadas().size() > 0) {
                llenarjTableFacturasPorVehiculo();
            } else {
               
            }

            

        } catch (Exception ex) {
            System.out.println("Error en txtManifiestoKeyPressed ");
            Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void llenarjTableFacturasPorVehiculo() throws Exception {

        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();
        if (modelo2.getRowCount() > 0) {
            int a = modelo2.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo2.removeRow(i);
            }
        }

        txtManifiesto.setEditable(false);

        for (CFacturasPorManifiesto obj2 : manifiestoActual.getListaFacturasDescargadas()) {

            filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();
            modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
            jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
            jTableFacturasPorVehiculo.setValueAt(obj2.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
            jTableFacturasPorVehiculo.setValueAt(obj2.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
            switch (obj2.getIdTipoDeMovimiento()) {
                case 1:
                    jTableFacturasPorVehiculo.setValueAt(obj2.getIdTipoDeMovimiento(), filaSeleccionada, 3); // nombre del nombreDelCliente
                    break;

                case 2:
                    if (obj2.getValorRecaudado() == 0.0) {
                        jTableFacturasPorVehiculo.setValueAt("E. T. Cr", filaSeleccionada, 3); // nombre del nombreDelCliente
                    } else {
                        if (obj2.getValorDescuento() == 0.0) {
                            jTableFacturasPorVehiculo.setValueAt("E. T. Cn", filaSeleccionada, 3); // nombre del nombreDelCliente
                        } else {
                            jTableFacturasPorVehiculo.setValueAt("E. T. %", filaSeleccionada, 3); // nombre del nombreDelCliente
                        }

                    }

                    break;
                case 3:
                    jTableFacturasPorVehiculo.setValueAt("D. T.", filaSeleccionada, 3); // nombre del nombreDelCliente
                    break;
                case 4:

                    if (obj2.getValorRecaudado() > 0) {
                        jTableFacturasPorVehiculo.setValueAt("E. N. Cn", filaSeleccionada, 3); // nombre del nombreDelCliente
                    } else {
                        jTableFacturasPorVehiculo.setValueAt("E. N. Cr", filaSeleccionada, 3); // nombre del nombreDelCliente
                    }

                    break;
                case 5:
                    jTableFacturasPorVehiculo.setValueAt("", filaSeleccionada, 3); // nombre del nombreDelCliente
                    break;
                case 6:
                    jTableFacturasPorVehiculo.setValueAt("R. E.", filaSeleccionada, 3); // nombre del nombreDelCliente
                    break;

            }

            jTableFacturasPorVehiculo.setValueAt(nf.format(obj2.getValorRecaudado()), filaSeleccionada, 4);

            valorTotalRecaudado += obj2.getValorRecaudado();

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
        txtManifiesto.setEditable(false);

        for (CFacturasPorManifiesto obj2 : listaFacturasDescargadas) {
            filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();
            modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
            jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
            jTableFacturasPorVehiculo.setValueAt(obj2.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
            jTableFacturasPorVehiculo.setValueAt(obj2.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt(nf.format(obj2.getValorRecaudado()), filaSeleccionada, 3);

            valorTotalRecaudado += obj2.getValorRecaudado();

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

    private void cbxMovimientoFacturaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxMovimientoFacturaItemStateChanged
        tipoMovimientoFactura = 0;

        if (nuevo) {
            for (CMovimientosManifiestosfacturas obj : ini.getListaDeMovimientosManifiestosfacturas()) {
                if (cbxMovimientoFactura.getSelectedItem().toString().equals(obj.getNombreMovimientosManifiestosfacturas())) {
                    tipoMovimientoFactura = obj.getIdMovimientosManifiestosfacturas();
                }
            }
            jTabbedPane1.setEnabled(true);
            switch (tipoMovimientoFactura) {
                case 0:
                    txtNumeroDeFactura.setEnabled(false);
                    txtNumeroDeFactura.setEnabled(false);
                   
                    break;
                // NO SE HA SELECCIONADO NINGUN TIPO DE MOVIMIENTO
                case 1:
                    
                    txtNumeroDeFactura.setEnabled(false);
                    txtNumeroDeFactura.setEnabled(false);
                    break;
                // ENTREGA TOTAL
                case 2:

                    txtNumeroDeFactura.setEnabled(true);
                    txtNumeroDeFactura.setEnabled(true);
                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlEntregas), true);
                    jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlEntregas));
                    
                    txtNumeroDeFactura.requestFocus();
                    break;
                // RECHAZO TOTAL
                case 3: //rechazos totales
                    //chkDescuento.setEnabled(true);
                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlRechazosTotales), true);
                    jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlRechazosTotales));
                    cbxCausalDeRechazoFactura.setEnabled(true);
                    btnDescargarRechazoTotal.setEnabled(true);
                   
                    txtNumeroDeFactura.requestFocus();
                    break;
                // ENTREGA CON NOVEDAD
                case 4:
                    
                   txtNumeroDeFactura.setEditable(true);
                    txtNumeroDeFactura.setEnabled(true);

                    txtNumeroDeFactura.requestFocus();
                    break;
                //DEVOLUCIONES
                case 5:
                    
                   
                    txtNumeroDeFactura.requestFocus();
                    break;

                // VOLVER A ZONIFICAR
                case 6:

                   
                    txtNumeroDeFactura.setEnabled(true);
                    txtNumeroDeFactura.setEnabled(true);
                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlEntregas), true);
                    jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlEntregas));
                    txtNumeroDeFactura.requestFocus();
                    break;

            }
        }
    }//GEN-LAST:event_cbxMovimientoFacturaItemStateChanged

    private void cbxMovimientoFacturaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxMovimientoFacturaFocusLost
        txtNumeroDeFactura.setEnabled(true);
        txtNumeroDeFactura.setEditable(true);
        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();
// TODO add your handling code here:
    }//GEN-LAST:event_cbxMovimientoFacturaFocusLost

    private void cerrarManifiesto() {

        new Thread(new HiloGuardarManifiestoDescargado(ini, this)).start();

    }


    private void cbxMovimientoFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxMovimientoFacturaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNumeroDeFactura.setEnabled(true);
            txtNumeroDeFactura.setEnabled(true);
            txtNumeroDeFactura.requestFocus();        // TODO add your handling code here:
        }
    }//GEN-LAST:event_cbxMovimientoFacturaKeyPressed

    private void lblFacturasPendientesMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFacturasPendientesMouseMoved

        String toolTipHTML = ""
                + "<html><body><pre>";
        try {
            if ((listaFacturasPendientesPorDescargar != null) || listaFacturasPendientesPorDescargar.size() > 0) { // Vst_FacturasPorManifiesto
                int i = 1;
                for (CFacturasPorManifiesto obj : listaFacturasPendientesPorDescargar) {
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
            Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_lblFacturasPendientesMouseMoved

    private void rBtnContadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rBtnContadoActionPerformed

    }//GEN-LAST:event_rBtnContadoActionPerformed

    private void rBtnCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rBtnCreditoActionPerformed

        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();
    }//GEN-LAST:event_rBtnCreditoActionPerformed

    private void rBtnCreditoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rBtnCreditoMouseClicked
        if (rBtnCredito.isSelected()) {
            isFacturaCredito = true;
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

    private void mnuBorraUnaFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuBorraUnaFacturaMouseClicked
        if (borrarUnaFactura()) {
            return;
        }
    }//GEN-LAST:event_mnuBorraUnaFacturaMouseClicked

    private boolean borrarUnaFactura() throws HeadlessException {
        boolean borrado = false;
        int borrarFila;
        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();

        /* se identifica la fila seleccionada de la tabla*/
        filaSeleccionada = jTableFacturasPorVehiculo.getSelectedRow();

        /*Se identifica el numero de la factura contenida en la fila seleccionada*/
        String numFactura = modelo2.getValueAt(filaSeleccionada, 1).toString();

        if (manifiestoActual.getEstadoManifiesto() == 4) {
            return true;
        }
        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO
        if (jTableFacturasPorVehiculo.getRowCount() > 0) {

            // borrarFila = JOptionPane.showOptionDialog(this, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "No");
            borrarFila = JOptionPane.showConfirmDialog(this, "Desea eliminar la factura # " + numFactura + " de la Tabla ?", "Eliminar Fcatura # " + numFactura, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            // HA SELECCIONADO BORRAR LA FILA
            if (borrarFila == JOptionPane.YES_OPTION) {

                try {

                    //Borra los datos del archivo almacenado en el disco
                    if (eliminarFacturaDelArchivo(numFactura)) {

                        //Borra la fila seleccionada del JTable
                        modelo2.removeRow(filaSeleccionada);

                        for (CFacturasPorManifiesto fact : listaFacturasDescargadas) {
                            if (fact.getNumeroFactura().equals(numFactura)) {

                                /*Elimina el objeto al array de facturas descargadas*/
                                listaFacturasDescargadas.remove(fact);

                                /*Agrega el objeto al arrray de facturas pendientes por descargar*/
                                listaFacturasPendientesPorDescargar.add(fact);
                                borrado = true;
                                break;
                            }
                        }

                        /*se reasigna las adeherencia de descargue */
                        int i = 1;
                        for (CFacturasPorManifiesto fact : listaFacturasDescargadas) {
                            fact.setAdherencia(i);
                            i++;

                        }

                        lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());

                    }

                    // ordena la tabla con la nueva adherencia
                    llenarjTableFacturasPorVehiculo();

                    calcularValoraAConsignar();

                    /*Se actualiza la lista de los productos descargados*/
                    manifiestoActual.setListaCProductosPorFacturaDescargados(manifiestoActual.getRutaArchivoDescargueporductosPorFactura());
                    listaDeCProductosPorFacturaDescargados = manifiestoActual.getListaCProductosPorFacturaDescargados();

                } catch (Exception ex) {
                    Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
                }

                btnGrabar.setEnabled(false);
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

                    /*Borra los datos del archivo almacenado en el disco */
 /* Elimina los datos de los arrays*/
                    limpiarArrays();

                    /* Se llena el aaray de las facturas del manifiestos*/
                    for (CFacturasPorManifiesto obj : listaFacturasPorManifiesto) {
                        listaFacturasPendientesPorDescargar.add(obj);
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

                  
                    lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());

                    manifiestoActual.setListaCProductosPorFacturaDescargados(manifiestoActual.getRutaArchivoDescargueporductosPorFactura());
                    listaDeCProductosPorFacturaDescargados = null;

                } catch (Exception ex) {
                    Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
                }

                btnGrabar.setEnabled(false);
            }

        } else {
            JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
        }
        return false;
    }

    private void mnuBorraUnaFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuBorraUnaFacturaActionPerformed
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

    private void jTableProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductosPorFacturaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableProductosPorFacturaMouseClicked

    private void btnDescargarRechazoTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarRechazoTotalActionPerformed
        if (cbxCausalDeRechazoFactura.getSelectedItem().equals("NINGUNO")) {
            JOptionPane.showInternalMessageDialog(this, "No ha seleccionado una causal de Rechazo", "Alerta ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            CFacturasDescargadas facturaDescargada = new CFacturasDescargadas(ini);

            for (CFacturasPorManifiesto obj : listaFacturasPorManifiesto) {

                if (obj.getNumeroFactura().equals(facturaActual.getNumeroFactura())) {

                    facturaDescargada.setConsecutivo(obj.getConsecutivo());
                    facturaDescargada.setNumeroManifiesto(obj.getNumeroManifiesto());
                    facturaDescargada.setNumeroFactura(obj.getNumeroFactura());
                    facturaDescargada.setAdherenciaDescargue(listaFacturasDescargadas.size() + 1);
                    facturaDescargada.setValorRechazo(obj.getValorTotalFactura());
                    facturaDescargada.setIdZona(ini.getIdZona());
                    facturaDescargada.setIdRegional(ini.getIdRegional());
                    facturaDescargada.setIdAgencia(ini.getIdAgencia());
                    facturaDescargada.setActivo(1);

                    break;
                }
            }

            // SE VALIDA QUE LA FACTURA NO ESTE DESCARGADA
            if (!estaDescargadaLaFactura(facturaActual.getNumeroFactura())) {

                descargarRechazosTotales(facturaDescargada);
                //facturaTemporal.liberarFactura(false);

            }

        } catch (Exception ex) {
            Logger.getLogger(FDescargarDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("\n\n Error en btnDescargarRechazoTotalActionPerformed \n");

            JOptionPane.showInternalMessageDialog(this, "La factura no aparece en el manifiesto", "Error", 0);

        }
        txtNumeroDeFactura.requestFocus();
    }//GEN-LAST:event_btnDescargarRechazoTotalActionPerformed

    private void cbxCausalDeRechazoFacturaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCausalDeRechazoFacturaItemStateChanged

    }//GEN-LAST:event_cbxCausalDeRechazoFacturaItemStateChanged

    private void lblValorAConsignarMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValorAConsignarMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValorAConsignarMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu borrarFactura;
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnDescargarRechazoTotal;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JComboBox cbxCausalDeRechazoFactura;
    public javax.swing.JComboBox cbxMovimientoFactura;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JList jListFacturasNoManifestadas;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableFacturasPorVehiculo;
    private javax.swing.JTable jTableProductosPorFactura;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JLabel lblFacturasPendientes;
    public javax.swing.JLabel lblValorAConsignar;
    private javax.swing.JMenuItem mnuBorraTodasLasFacturas;
    private javax.swing.JMenuItem mnuBorraUnaFactura;
    private javax.swing.JMenu mnuBorrarFacturas;
    private javax.swing.JPanel pnlEntregas;
    public javax.swing.JPanel pnlRechazosTotales;
    public javax.swing.JRadioButton rBtnContado;
    public javax.swing.JRadioButton rBtnCredito;
    public javax.swing.JTextField txtManifiesto;
    public javax.swing.JTextField txtNombreConductor;
    public javax.swing.JTextField txtNumeroDeFactura;
    public javax.swing.JTextField txtPlaca;
    public javax.swing.JTextField txtRuta;
    // End of variables declaration//GEN-END:variables

    private void limpiar() {

        txtManifiesto.setEditable(false);
       

        cbxMovimientoFactura.setEnabled(false);
        cbxMovimientoFactura.setSelectedItem("NINGUNO");
        cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
        

        txtManifiesto.setEnabled(false);
        txtNumeroDeFactura.setEnabled(false);
        txtNumeroDeFactura.setEnabled(false);
        
        txtPlaca.setEnabled(false);
        txtNombreConductor.setEnabled(false);
        txtRuta.setEnabled(false);
       

        txtManifiesto.setText("");
        txtNumeroDeFactura.setText("");
        txtNumeroDeFactura.setEnabled(false);
        txtNumeroDeFactura.setEditable(false);
        
        txtPlaca.setText("");
        txtNombreConductor.setText("");
        txtRuta.setText("");
        

        
        valorTotalRecaudado = 0.0;
        cbxMovimientoFactura.setEnabled(false);
        
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

       

        //cantidadFacturasDescargadasManifiesto = 0;
        lblFacturasPendientes.setText("0");
        manifiestoActual = null;
        facturaActual = null;

        esteManifiestoEsMio = false;

        eliminarDatosDejListFacturasNoManifestadas();
       
        btnGrabar.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnImprimir.setEnabled(false);
        
        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

    }

    private void limpiarArrays() {

        /*Se vacían las listas respectivas */
        //listaFacturasPorManifiesto = new ArrayList();
        listaFacturasDescargadas = new ArrayList();
        listaFacturasPendientesPorDescargar = new ArrayList();

        listaDeRecogidasPorManifiesto = new ArrayList();
        listaDeCProductosPorFacturaDescargados = new ArrayList();
        listaDeFacturasNoManifestadas = new ArrayList();
        listaFacturaTemporal = new ArrayList();
        listaDeSoportesDeConsignaciones = new ArrayList();

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
        for (CFacturasPorManifiesto obj : listaFacturasDescargadas) {
            if (numeroDefactura.equals(obj.getNumeroFactura())) {
                descargada = true;
                break;
            }
        }

        return descargada;
    }

    public boolean estaDescargadaRecogida(String numeroDefactura) {
        boolean descargada = false;
        for (CRecogidasPorManifiesto obj : listaDeRecogidasPorManifiesto) {
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

   

    public void eliminarDatosDejListFacturasNoManifestadas() {

        DefaultListModel model = new DefaultListModel();
        jListFacturasNoManifestadas.setModel(model);
//See more at: http://collectioncode.com/java/limpiar-jlist-en-java/#sthash.v1nd3O4o.dpuf

    }

    public void llenarDatosVista() {

        txtManifiesto.setEnabled(true);
        txtNumeroDeFactura.setEnabled(true);
        txtNumeroDeFactura.setEnabled(true);
        
        txtPlaca.setEnabled(true);
        txtNombreConductor.setEnabled(true);
        txtRuta.setEnabled(true);

        txtManifiesto.setEditable(false);
        txtNumeroDeFactura.setEditable(false);
        
        txtPlaca.setEditable(false);
        txtNombreConductor.setEditable(false);
        txtRuta.setEditable(false);

        valorTotalAConsignar = valorTotalRecaudado - valorTotalRecogidas;

       
        txtPlaca.setText(manifiestoActual.getVehiculo());

       

        kilometrosRecorridos = manifiestoActual.getKmEntrada() - manifiestoActual.getKmSalida();
               txtNumeroDeFactura.setEditable(true);

        lblFacturasPendientes.setText(" " + listaFacturasPendientesPorDescargar.size());
        
        if (manifiestoActual.getEstadoManifiesto() == 4) {
            lblCirculoDeProgreso.setVisible(false);
            lblFacturasPendientes.setText(" " + 0);
            /*Se habilitan las pestañas para visualizar las recogidas */
            jTabbedPane1.setEnabled(true);
           

            // txtManifiesto.setEnabled(false);
            // txtKilometrosEntrada.setEnabled(false);
            //txtNumeroDeFactura.setEnabled(false);
            txtNombreConductor.setText(listaFacturasPorManifiesto.get(0).getNombreConductor());
            txtRuta.setText(listaFacturasPorManifiesto.get(0).getNombreDeRuta());
            cbxMovimientoFactura.setEnabled(false);
            txtNumeroDeFactura.setEnabled(false);
           
            rBtnContado.setEnabled(false);
            rBtnCredito.setEnabled(false);
            cbxCausalDeRechazoFactura.setEnabled(false);
            btnDescargarRechazoTotal.setEnabled(false);
            btnGrabar.setEnabled(false);
           
            
            btnImprimir.setEnabled(true);
           
            btnImprimir.requestFocus();

            JOptionPane.showInternalMessageDialog(this, "Manifiesto descargado y completo", "Manifiesto completo", JOptionPane.INFORMATION_MESSAGE);
        }

        if (manifiestoActual.getEstadoManifiesto() == 3) {
            lblCirculoDeProgreso.setVisible(false);
            txtNombreConductor.setText(listaFacturasPorManifiesto.get(0).getNombreConductor());
            txtRuta.setText(listaFacturasPorManifiesto.get(0).getNombreDeRuta());

            if (listaFacturasPendientesPorDescargar.isEmpty()) {
                JOptionPane.showInternalMessageDialog(this, "Manifiesto descargado y completo", "Manifiesto completo", JOptionPane.INFORMATION_MESSAGE);
                btnGrabar.setEnabled(true);

                this.lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());
                txtNumeroDeFactura.setEnabled(true);

                btnGrabar.requestFocus();
            } else {
               
                btnGrabar.setEnabled(false);
                btnImprimir.setEnabled(false);
                cbxMovimientoFactura.setEnabled(false);
               
                rBtnContado.setEnabled(false);
                rBtnCredito.setEnabled(false);

                
                rBtnContado.setEnabled(true);
                rBtnCredito.setEnabled(true);
                this.lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());
               
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
}
