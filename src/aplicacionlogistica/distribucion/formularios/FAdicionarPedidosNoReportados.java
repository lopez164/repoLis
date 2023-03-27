/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios;

import aplicacionlogistica.distribucion.consultas.FConsultarVehiculos;
import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeVehiculos;
import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloGuardarFacturasPorManifiesto;
import aplicacionlogistica.distribucion.Threads.JcProgressBar;
import aplicacionlogistica.distribucion.formularios.Threads.HiloFAdicionarPedidosNoReportados;
import aplicacionlogistica.distribucion.imprimir.RepporteRuteroConductores;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import mtto.vehiculos.CCarros;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Esta vista, permite que el usuario pueda asignar a un manifiesto de
 * Distribución el conductor, el auxiliar, un vehiculo dando una ruta y
 * clasificandolo dentro de un cnal de venta; el sistema se encrga de crear el
 * manifiestos y posteriormente se le asignan las factura que van a salir a
 * distrbucion.
 *
 * @author Luis Eduardo López
 */
public class FAdicionarPedidosNoReportados extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    
    public CEmpleados conductor = null;
    public CEmpleados auxiliar1 = null;
    public CEmpleados auxiliar2 = null;
    public CEmpleados auxiliar3 = null;
    public CEmpleados despachador = null;

    public static boolean band = false;
    public static int valorDespBarraProgreso = 0;

    CUsuarios user;
    public CCarros carro = null;
    public CManifiestosDeDistribucion manifiestoActual = null;
    public CRutasDeDistribucion ruta;
    public CCanalesDeVenta canalDeVenta;
    public CFacturas facturaActual = null;
    Inicio ini = null;

    public File archivoConListaDeFacturas;

    DefaultListModel modelo, modelo3;

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public DefaultTableModel modelo1, modelo2;

    public Double valorTotalManifiesto = 0.0;
    public boolean cargado = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean nuevo = false;

    public int filaTabla2;
    int kilometraje = 0;
    public String formaDePago = "";
    public String mensaje = null;
    int contadorDeFacturas = 0;

    // public List<Vst_FacturaCamdun> listaDeCFacturasCamdunEnElManifiesto = null; //CFacturasCamdun
    public List<CFacturasPorManifiesto> listaDeFacturasPorManifiesto = null;
   
    public List<String> listaDeNumerosDeFacturasNoEncontrados;
    public List<String> listaDeFacturasEnElArchivo;
    public List<CEmpleados> listaDeAuxiliares = new ArrayList<>();

    public boolean estaOcupadoGrabando = false;

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public FAdicionarPedidosNoReportados(Inicio ini) throws Exception {

        try {
            initComponents();

            this.ini = ini;
            this.user = ini.getUser();
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
            Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        new Thread(new HiloFAdicionarPedidosNoReportados(ini, this, "cargarVista")).start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlAgregarFactura = new javax.swing.JPanel();
        lblValorFactura = new org.edisoncor.gui.label.LabelCustom();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtNumeroDeFactura = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        txtNombreVendedor = new javax.swing.JTextField();
        txtNombreDeCliente = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableProductosPorFactura = new javax.swing.JTable();
        txtBarroCliente = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        btnContado = new javax.swing.JRadioButton();
        btnCredito = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        lblCantidadFacturas = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lblValorRecaudoManifiesto = new org.edisoncor.gui.label.LabelCustom();
        jLabel14 = new javax.swing.JLabel();
        txtNumeroManifiesto = new javax.swing.JTextField();
        lblCirculoDeProgreso1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFacturasPorManifiesto = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        txtnombreDeConductor = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtKmDeSalida = new javax.swing.JTextField();
        lblKilometros = new javax.swing.JLabel();
        brrProgreso = new javax.swing.JProgressBar();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dateManifiesto = new com.toedter.calendar.JDateChooser();
        txtRuta = new javax.swing.JTextField();
        txtCanal = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JButton();
        btnMostrarDocumento = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        btnCancelar1 = new javax.swing.JToggleButton();
        jButton4 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Formulario para Manifestar Pedidos No reportados");
        setAutoscrolls(true);
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

        jPanel5.setAutoscrolls(true);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTabbedPane1.setEnabled(false);
        jTabbedPane1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabbedPane1FocusGained(evt);
            }
        });

        pnlAgregarFactura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlAgregarFactura.setAutoscrolls(true);
        pnlAgregarFactura.setDoubleBuffered(false);

        lblValorFactura.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValorFactura.setText("$ 0.0");
        lblValorFactura.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lblValorFactura.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel27.setText("Numero de Factura");

        jLabel28.setText("Barrio");

        txtNumeroDeFactura.setEditable(false);
        txtNumeroDeFactura.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
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

        txtTelefonoCliente.setEditable(false);
        txtTelefonoCliente.setColumns(22);
        txtTelefonoCliente.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtTelefonoCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoClienteFocusGained(evt);
            }
        });
        txtTelefonoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoClienteActionPerformed(evt);
            }
        });
        txtTelefonoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoClienteKeyPressed(evt);
            }
        });

        txtNombreVendedor.setEditable(false);
        txtNombreVendedor.setColumns(22);
        txtNombreVendedor.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreVendedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreVendedorFocusGained(evt);
            }
        });
        txtNombreVendedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreVendedorKeyPressed(evt);
            }
        });

        txtNombreDeCliente.setEditable(false);
        txtNombreDeCliente.setColumns(22);
        txtNombreDeCliente.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreDeCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDeClienteFocusGained(evt);
            }
        });
        txtNombreDeCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDeClienteActionPerformed(evt);
            }
        });
        txtNombreDeCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDeClienteKeyPressed(evt);
            }
        });

        jLabel33.setText("Cliente");

        jLabel31.setText("Vendedor");

        jLabel34.setText("Dirección");

        jLabel35.setText("Teléfono");

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
            jTableProductosPorFactura.getColumnModel().getColumn(2).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(2).setPreferredWidth(270);
            jTableProductosPorFactura.getColumnModel().getColumn(3).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTableProductosPorFactura.getColumnModel().getColumn(4).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(100);
            jTableProductosPorFactura.getColumnModel().getColumn(5).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        txtBarroCliente.setEditable(false);
        txtBarroCliente.setColumns(22);
        txtBarroCliente.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtBarroCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarroClienteFocusGained(evt);
            }
        });
        txtBarroCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarroClienteKeyPressed(evt);
            }
        });

        txtDireccionCliente.setEditable(false);
        txtDireccionCliente.setColumns(22);
        txtDireccionCliente.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtDireccionCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDireccionClienteFocusGained(evt);
            }
        });
        txtDireccionCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionClienteKeyPressed(evt);
            }
        });

        btnContado.setSelected(true);
        btnContado.setText("Contado");
        btnContado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContadoActionPerformed(evt);
            }
        });

        btnCredito.setText("Crédito");
        btnCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreditoActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lblCantidadFacturas.setFont(new java.awt.Font("Cantarell", 0, 48)); // NOI18N
        lblCantidadFacturas.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblCantidadFacturas, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblCantidadFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlAgregarFacturaLayout = new javax.swing.GroupLayout(pnlAgregarFactura);
        pnlAgregarFactura.setLayout(pnlAgregarFacturaLayout);
        pnlAgregarFacturaLayout.setHorizontalGroup(
            pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAgregarFacturaLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 703, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                        .addComponent(btnContado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCredito))
                    .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblValorFactura, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAgregarFacturaLayout.createSequentialGroup()
                            .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                                            .addGap(3, 3, 3)
                                            .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING)))
                                        .addComponent(jLabel31)))
                                .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGap(10, 10, 10)
                            .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDireccionCliente)
                                .addComponent(txtTelefonoCliente)
                                .addComponent(txtBarroCliente)
                                .addComponent(txtNombreVendedor)
                                .addComponent(txtNombreDeCliente)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlAgregarFacturaLayout.setVerticalGroup(
            pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnContado)
                    .addComponent(btnCredito))
                .addGap(2, 2, 2)
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAgregarFacturaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNombreDeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33))
                        .addGap(1, 1, 1)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34))
                        .addGap(1, 1, 1)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35))
                        .addGap(1, 1, 1)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBarroCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28))
                        .addGap(1, 1, 1)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(txtNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3)
                .addComponent(lblValorFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Agregar Facturas", pnlAgregarFactura);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Manifiesto de Reparto"));
        jPanel7.setEnabled(false);

        lblValorRecaudoManifiesto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValorRecaudoManifiesto.setText("$ 0.0");
        lblValorRecaudoManifiesto.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lblValorRecaudoManifiesto.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Numero Manifiesto");

        txtNumeroManifiesto.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNumeroManifiesto.setEnabled(false);
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

        lblCirculoDeProgreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblValorRecaudoManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNumeroManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(txtNumeroManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(lblValorRecaudoManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableFacturasPorManifiesto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Cliente.", "valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jTableFacturasPorManifiesto.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableFacturasPorManifiesto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFacturasPorManifiestoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableFacturasPorManifiesto);
        if (jTableFacturasPorManifiesto.getColumnModel().getColumnCount() > 0) {
            jTableFacturasPorManifiesto.getColumnModel().getColumn(0).setResizable(false);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(1).setResizable(false);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(2).setResizable(false);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(2).setPreferredWidth(250);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(3).setResizable(false);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(3).setPreferredWidth(130);
        }

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Placa");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Conductor");

        txtPlaca.setEditable(false);
        txtPlaca.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
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

        txtnombreDeConductor.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtnombreDeConductor.setToolTipText("");
        txtnombreDeConductor.setEnabled(false);
        txtnombreDeConductor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtnombreDeConductorFocusGained(evt);
            }
        });
        txtnombreDeConductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnombreDeConductorKeyPressed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Kilometraje");

        txtKmDeSalida.setEditable(false);
        txtKmDeSalida.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtKmDeSalida.setToolTipText("Ingresar documento de identificación");
        txtKmDeSalida.setDragEnabled(true);
        txtKmDeSalida.setName("numerico"); // NOI18N
        txtKmDeSalida.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKmDeSalidaFocusGained(evt);
            }
        });
        txtKmDeSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKmDeSalidaActionPerformed(evt);
            }
        });
        txtKmDeSalida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKmDeSalidaKeyPressed(evt);
            }
        });

        lblKilometros.setText("kms");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Canal");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Ruta");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Fecha");

        dateManifiesto.setToolTipText("Seleccionar la fecha ");
        dateManifiesto.setDateFormatString("yyyy/MM/dd");
        dateManifiesto.setEnabled(false);
        dateManifiesto.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        txtRuta.setEditable(false);
        txtRuta.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtRuta.setToolTipText("Ingresar documento de identificación");
        txtRuta.setDragEnabled(true);
        txtRuta.setName("numerico"); // NOI18N
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

        txtCanal.setEditable(false);
        txtCanal.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtCanal.setToolTipText("Ingresar documento de identificación");
        txtCanal.setDragEnabled(true);
        txtCanal.setName("numerico"); // NOI18N
        txtCanal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCanalFocusGained(evt);
            }
        });
        txtCanal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCanalActionPerformed(evt);
            }
        });
        txtCanal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCanalKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(5, 5, 5))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(dateManifiesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(203, 203, 203))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(txtKmDeSalida, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                                    .addComponent(txtPlaca, javax.swing.GroupLayout.Alignment.LEADING))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblKilometros))
                                            .addComponent(txtnombreDeConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(2, 2, 2)))
                                .addGap(16, 16, 16))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtCanal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                .addComponent(txtRuta, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addComponent(brrProgreso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel3)
                        .addGap(2, 2, 2)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtnombreDeConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKmDeSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblKilometros))))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCanal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(dateManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)))
                .addGap(2, 2, 2)
                .addComponent(brrProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
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
        jBtnGrabar.setToolTipText("Grabar");
        jBtnGrabar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
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

        btnMostrarDocumento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        btnMostrarDocumento.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnMostrarDocumento.setEnabled(false);
        btnMostrarDocumento.setFocusable(false);
        btnMostrarDocumento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMostrarDocumento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnMostrarDocumento);

        jBtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jBtnImprimir.setToolTipText("Imprimir");
        jBtnImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnImprimir.setEnabled(false);
        jBtnImprimir.setFocusable(false);
        jBtnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jBtnImprimir);

        btnCancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        btnCancelar1.setToolTipText("Imprimir");
        btnCancelar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelar1.setFocusable(false);
        btnCancelar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCancelar1);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // SE LIBERA EL MANIFIESTO ACTUAL  
        if (manifiestoActual != null) {

            manifiestoActual.liberarManifiesto(true);
            liberado = true;

        }

        // SE CIERRA LA APLICACION
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        nuevo();


    }//GEN-LAST:event_btnNuevoActionPerformed

    private void nuevo() {
        try {
            cancelar();
            
            nuevo = true;
            limpiar();
            dateManifiesto.setEnabled(true);
            dateManifiesto.setDate(new Date());
            jPanel7.setEnabled(true);
            
            txtnombreDeConductor.setEditable(false);
            
            txtNumeroManifiesto.setEnabled(true);
            txtNumeroManifiesto.setEditable(true);
            txtKmDeSalida.setEditable(false);
            txtKmDeSalida.setEnabled(true);
            txtPlaca.setEnabled(true);
            txtPlaca.setEditable(false);
            dateManifiesto.setEnabled(false);
            jTabbedPane1.setEnabled(false);
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            
            txtPlaca.requestFocus();
            formaDePago = ""; // contado
            btnContado.setSelected(true);
            
            facturaActual = null;
            conductor = null;
            carro = null;
            manifiestoActual = null;
            listaDeAuxiliares = new ArrayList();
            
            lblCirculoDeProgreso1.setVisible(false);
            listaDeFacturasPorManifiesto = new ArrayList(); // CfacturasPorManifiesto
            listaDeNumerosDeFacturasNoEncontrados = new ArrayList();
            listaDeFacturasEnElArchivo = new ArrayList();
            manifiestoActual = new CManifiestosDeDistribucion(ini);
            manifiestoActual.setEstadoManifiesto(0);
            txtNumeroManifiesto.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        int deseaGrabarRegistro;

        /*valida sin pendientes del conductor */
        if (false) {
            // if (manifiestoActual.getCantidadManifiestosPendientesConductor() > 0) {
            JOptionPane.showMessageDialog(this, "El conductor tiene pendientes de ruta,\n no se puede grabar este manifiesto", "Conductor no esta paz y salvo", JOptionPane.ERROR_MESSAGE, null);

            return;
        }

        if (manifiestoActual.getEstadoManifiesto() == 2) {  // IINICIO DEL IF-> ESTADO DEL MANIFIESTO

            deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

            if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {

                manifiestoActual.setHoraDeDespacho(manifiestoActual.getHoraDeDespacho());

                /* Se valida la conexión a internet para grabar los datos en la BBDD */
                //if (ini.verificarConexion()) {
                if (true) {
                    band = true;

                    new Thread(new HiloGuardarFacturasPorManifiesto(ini, this)).start();
                    new Thread(new JcProgressBar(brrProgreso, 100)).start();

                } else {

                    JOptionPane.showMessageDialog(null, "No hay conexión a internet", "Error al guardar Datos", JOptionPane.WARNING_MESSAGE, null);

                }

                //
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos, manifiesto  ya está en distribución", "manifiesto ya fue guardado", JOptionPane.ERROR_MESSAGE, null);
            btnGrabar.setEnabled(false);
            jBtnGrabar.setEnabled(false);
        }  // FIN DEL IF-> ESTADO DEL MANIFIESTO


    }//GEN-LAST:event_btnGrabarActionPerformed

    private void guardarFormulario() throws HeadlessException {
        List<String> sentenciasSQL;
        /* Se valida la conexión a internet para grabar los datos en la BBDD */
        if (ini.verificarConexion()) {

            sentenciasSQL = new ArrayList<>();

//                    cbxCanales.setEnabled(false);
//                    cbxRutaDeDistribucion.setEnabled(false);
            int contadorDeCiclos = 0; // CONTADOR DE LOS CICLOS

            try {

                /* SE CREA CADENA PARA GRABAR LOS REGISTROS EN LA BASE DE DATOS */
                for (CFacturasPorManifiesto obj : listaDeFacturasPorManifiesto) {
                    contadorDeCiclos++;

                    obj.setAdherencia(contadorDeCiclos);
//                    sentenciasSQL.add(obj.getSentenciaInsertSQL());

                }

                /* SE GRABAN LOS REGISTROS DE FACTURAS POR MANIFIESTO  EN LA BBDD LOCAL */
                if (ini.insertarDatosLocalmente(sentenciasSQL)) {

                    /* SE GRABAN LOS REGISTROS DE FACTURAS POR MANIFIESTO  EN LA BBDD REMOTA */
                    ini.insertarBBDDRemota(sentenciasSQL,"facturas por manifiesto");

                    /* SE HABILITA BOTON DE IMPRIMIR */
                    btnImprimir.setEnabled(true);
                    jBtnImprimir.setEnabled(true);

                    /* SE DESHABILITA EL BOTON DE GRABAR */
                    btnGrabar.setEnabled(false);
                    jBtnGrabar.setEnabled(false);

                    /* EL MANIFIESTO SE BLOQUEA PARA QUE NO  PUEDA SER UTILIZADA  POR OTRO USUARIO */
                    manifiestoActual.setIsFree(1);
                    manifiestoActual.liberarManifiesto(true);
                    manifiestoActual.setValorTotalManifiesto(valorTotalManifiesto);

                    /* SE CAMBIA ELESTADO DE ALISTAMIENTO A DISTRIBUCION */
                    manifiestoActual.setEstadoManifiesto(3);

                    /* SE ACTUAIZA LOS DATOS DEL MANIFIESTO ACTUAL */
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date fechaEnviar = new Date();
                   // java.util.Date fechaActual = formato.format(fechaEnviar);

                    manifiestoActual.setHoraDeDespacho("" + new Date());
                    manifiestoActual.setHoraDeLiquidacion("" + new Date());
                    manifiestoActual.actualizarManifiestoDeDistribucions();

                    JOptionPane.showMessageDialog(null, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);

                } else {
                    JOptionPane.showMessageDialog(null, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "Error al guardar los datos " + e, "Error", JOptionPane.ERROR_MESSAGE, null);
            }

        } else {
            JOptionPane.showMessageDialog(null, "No hay conexión a internet", "Error al guardar Datos", JOptionPane.WARNING_MESSAGE, null);
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cancelar() {
        limpiar();

        nuevo = false;
        dateManifiesto.setEnabled(false);

        txtnombreDeConductor.setEditable(false);

        txtKmDeSalida.setEditable(false);
        txtPlaca.setEditable(false);
        txtKmDeSalida.setEnabled(false);
        txtKmDeSalida.setEditable(false);

        dateManifiesto.setEnabled(false);

        jTabbedPane1.setEnabled(false);
        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);
        
        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);
        
        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);
        grabar = false;

        if (manifiestoActual != null) {
            manifiestoActual.liberarManifiesto(true);
            liberado = true;

        }
        txtNumeroDeFactura.setEnabled(false);
        txtNumeroDeFactura.setEditable(false);

        txtNombreDeCliente.setEditable(false);
        txtBarroCliente.setEditable(false);
        txtDireccionCliente.setEditable(false);
        txtTelefonoCliente.setEditable(false);
        txtNombreVendedor.setEditable(false);

        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();

        manifiestoActual = null;
        conductor = null;
        despachador = null;
        listaDeAuxiliares = null;

        listaDeFacturasPorManifiesto = null;
        valorTotalManifiesto = 0.0;
        formaDePago = "";

        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);

    }

    private void jTableFacturasPorManifiestoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFacturasPorManifiestoMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_jTableFacturasPorManifiestoMouseClicked

    private void txtnombreDeConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnombreDeConductorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreDeConductorFocusGained

    private void txtnombreDeConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreDeConductorKeyPressed
        /* Evento en el caso que se oprima la tecla F2 */

    }//GEN-LAST:event_txtnombreDeConductorKeyPressed

    private void txtNumeroDeFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaFocusGained
        txtNumeroDeFactura.setSelectionStart(0);
        txtNumeroDeFactura.setSelectionEnd(txtNumeroDeFactura.getText().length());
    }//GEN-LAST:event_txtNumeroDeFacturaFocusGained

    private void txtNombreDeClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeClienteFocusGained
        txtNombreDeCliente.setSelectionStart(0);
        txtNombreDeCliente.setSelectionEnd(txtNombreDeCliente.getText().length());
        txtNumeroDeFactura.requestFocus();
    }//GEN-LAST:event_txtNombreDeClienteFocusGained

    private void txtTelefonoClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoClienteFocusGained
        txtTelefonoCliente.setSelectionStart(0);
        txtTelefonoCliente.setSelectionEnd(txtTelefonoCliente.getText().length());
    }//GEN-LAST:event_txtTelefonoClienteFocusGained

    private void txtNombreVendedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreVendedorFocusGained
        txtNombreVendedor.setSelectionStart(0);
        txtNombreVendedor.setSelectionEnd(txtNombreVendedor.getText().length());
    }//GEN-LAST:event_txtNombreVendedorFocusGained

    private void txtNumeroDeFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaKeyPressed
        /*Evento al oprimir la tecla Enter */
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {
                //agregarFactura("" + Integer.parseInt(txtNumeroDeFactura.getText().trim()), false, jTableFacturasPorManifiesto.getRowCount() + 1);
              // agregarFactura(txtNumeroDeFactura.getText().trim(), false, jTableFacturasPorManifiesto.getRowCount() + 1);

                new Thread(new HiloFAdicionarPedidosNoReportados(ini, this, "funcionAgregarFactura")).start();
               
            } catch (Exception ex) {
                Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_txtNumeroDeFacturaKeyPressed

    /**
     * Método que asigna las factura leida por teclado ó por código de barras al
     * manifiesto actual
     *
     * @param numeroDeFactura leida por código de barras ó por teclado
     * @param desdeArchivo
     * @param adherencia
     */
    public void agregarFactura(String numeroDeFactura, boolean desdeArchivo, int adherencia) throws Exception {

        facturaActual = null;
        //CFacturasCamdun factura;

        try {

            /* se crea el objeto factura, digitado en el campo de texto */
            facturaActual = new CFacturas(ini, numeroDeFactura);
           // Vst_FacturasPorManifiesto vfxm = new Vst_FacturasPorManifiesto();

            /* SE VALIDA QUE LA FACTURA EXISTA  */
            if (facturaActual.getNumeroDeFactura() != null) {
                //factura = new CFacturas(ini, numeroDeFactura);

                /*Se valida el tipo de movimiento de la factura*/
                switch (facturaActual.getEstadoFactura()) {

                    case 2:
                        /*Entrega total*/
                        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);
                        return;

                    case 3:
                        /*Devolucion total*/
                        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " fue devuelta de ruta", "Error, factura Devuelta", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 4:/*Entrega con novedad*/
                        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 5:/*Entrega total con recogida*/
                        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 6:
                        /*Re Envio*/
                        break;

                }
                
                
                 /* se verifica que la factura esté libre, sino arroja el siguiente mensaje... 
                     =0 indica que la factura esta siendo ocupada por alguien  */
                    if (facturaActual.getIsFree() == 0) {
                        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura
                                + " ya se encuentra en Distribución "
                                + "", "Error", JOptionPane.WARNING_MESSAGE);

                        /* =1  la factura está disponible para agregarla al manifiesto 
                           SE registran las facturas que presentan inconvenientes en el Jlist*/
                        //modelo3.addElement(numeroDeFactura);
                        //jListListaDeFacturasErradas.setModel(modelo3);
                       return;
                    }

                /* SE valida que la factura no esté en el manifiesto */
                if (!estaLaFacturaEnElManifiesto()) {


                    /* se actualiza el canal  y la forma de pago de distribución de la factura */
                    facturaActual.setCanal(canalDeVenta.getIdCanalDeVenta());
                    facturaActual.setFormaDePago(formaDePago);


                   

                    /* se crea un objeto temporal de facturas por manifiesto */
                    CFacturasPorManifiesto facturaPorManifiesto = new CFacturasPorManifiesto(ini);
                    
                    /* Se agregan las propiedades a l objeto vista facturas por manifiesto */
                    facturaPorManifiesto.setNumeroManifiesto(manifiestoActual.getNumeroManifiesto());
                    facturaPorManifiesto.setNumeroFactura(facturaActual.getNumeroDeFactura());
                    facturaPorManifiesto.setNombreDeCliente(facturaActual.getNombreDeCliente());
                    facturaPorManifiesto.setDireccionDeCliente(facturaActual.getDireccionDeCliente());
                    facturaPorManifiesto.setValorTotalFactura(facturaActual.getValorTotalFactura());
                    facturaPorManifiesto.setAdherencia(listaDeFacturasPorManifiesto.size() + 1);
                    facturaPorManifiesto.setNumeroManifiesto(manifiestoActual.getNumeroManifiesto());
                    facturaPorManifiesto.setValorARecaudarFactura(facturaActual.getValorTotalFactura());

                   if (true) {

                        /* se agrega el registro al array  de facturas por manifiesto */
                        listaDeFacturasPorManifiesto.add(facturaPorManifiesto); //CfacturasPorManifiesto
                        manifiestoActual.setListaFacturasPorManifiesto(listaDeFacturasPorManifiesto);

                        /*Validamos de donde proviene el dato: bien sea del campo 
                            de texto ó de un archivo plano */
                       
                            /* sí hay datos en la tabla de los productos  de la factura se limpia la tabla */
                            limpiarDatodsDePanelProductosPorFactura();

                            txtNombreDeCliente.setText(facturaActual.getNombreDeCliente());
                            txtDireccionCliente.setText(facturaActual.getDireccionDeCliente());
                            txtTelefonoCliente.setText(facturaActual.getTelefonoCliente());
                            txtBarroCliente.setText(facturaActual.getBarrio());
                            txtNombreVendedor.setText(facturaActual.getVendedor());
                            lblValorFactura.setText(nf.format(facturaActual.getValorTotalFactura()));

                            /* Se anexa la factura al array.
                                 Se crea un array temporal de los productos que tiene la factura.. */
                            List<Vst_ProductosPorFactura> arrProduproductosEnLaFactura = new ArrayList();

                           CFacturas factura = new CFacturas(ini);
                            factura.setNumeroDeFactura(facturaActual.getNumeroDeFactura());
                          //  arrProduproductosEnLaFactura = factura.getListaProductosPorFactura(); // Vst_ProductosPorFactura

                          facturaActual.setListaCProductosPorFactura(false);
                            /* se llena la tabla de productos por Factura*/
//                            llenarJtableProductosPorFactura(facturaActual.getListaProductosPorFactura());

                       facturaPorManifiesto.grabarFacturasPoManifiesto();
                       
                        /*Se llena el Jtable correspondiente*/
                        llenarJtableFacturasPorManifiesto();

                        /* se ubica el cursor en la fila insertada */
                        jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);

                        /* se imprime el dato en la respectiva etiqueta */
                        lblValorRecaudoManifiesto.setText(nf.format(valorTotalManifiesto));
                        btnGrabar.setEnabled(!grabar);
                        jBtnGrabar.setEnabled(!grabar);

                    } else {
                        JOptionPane.showInternalMessageDialog(this, "Error al grabar en el archivo temporal, la  FACTURA # " + numeroDeFactura + "", "Error", JOptionPane.ERROR_MESSAGE);

                    }

                    /* si el registro existe en la tabla TMPfacturasPorManifiesto, hace nada */
                    txtNombreDeCliente.requestFocus();

                }

                /* si no   existe la factura, arroja un mensaje de error. */
            } else {

                JOptionPane.showInternalMessageDialog(this, "La factura # " + numeroDeFactura
                        + "  no existe en el sistema "
                        + "", "Error", JOptionPane.WARNING_MESSAGE);
            }

            txtNombreDeCliente.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void llenarJtableFacturasPorManifiesto() {

        /* se anexa el registro a la Jtable de facturas por manifiesto */
        filaTabla2 = jTableFacturasPorManifiesto.getRowCount();

        modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);
        jTableFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);
        jTableFacturasPorManifiesto.setValueAt(facturaActual.getNumeroDeFactura(), filaTabla2, 1);
        jTableFacturasPorManifiesto.setValueAt(facturaActual.getNombreDeCliente(), filaTabla2, 2);
        jTableFacturasPorManifiesto.setValueAt(nf.format(facturaActual.getValorTotalFactura()), filaTabla2, 3);

        valorTotalManifiesto += facturaActual.getValorTotalFactura();

    }

    private void llenarJtableProductosPorFactura() {
        modelo1 = (DefaultTableModel) jTableProductosPorFactura.getModel();

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura */
        for (CProductosPorFactura obj : facturaActual.getListaCProductosPorFactura()) {

            int fila = jTableProductosPorFactura.getRowCount();

            modelo1.addRow(new Object[jTableProductosPorFactura.getRowCount()]);

            jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorTotalFactura() / obj.getCantidad()), fila, 4);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorTotalFactura()), fila, 5);
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
    private void agregarFacturasDesdeArchivo(List<String> lista) {

        String numeroDeFactura;
        contadorDeFacturas = 0;
        listaDeNumerosDeFacturasNoEncontrados = null;

        try {
            listaDeNumerosDeFacturasNoEncontrados = new ArrayList<>();

            int adherencia = 1;

            /* Se hace el recorrido por el array para crear los objetos*/
            for (String obj : lista) {
                numeroDeFactura = obj;
                agregarFactura(numeroDeFactura, true, adherencia);
                adherencia++;

            }
        } catch (Exception ex) {
            Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    private void txtNombreDeClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtTelefonoCliente.requestFocus();
        }
    }//GEN-LAST:event_txtNombreDeClienteKeyPressed

    private void txtTelefonoClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNombreVendedor.requestFocus();
        }
    }//GEN-LAST:event_txtTelefonoClienteKeyPressed

    private void txtNombreVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreVendedorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //
        }
    }//GEN-LAST:event_txtNombreVendedorKeyPressed


    private void jTableProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductosPorFacturaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableProductosPorFacturaMouseClicked

    private void txtBarroClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarroClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarroClienteFocusGained

    private void txtBarroClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarroClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarroClienteKeyPressed

    private void txtDireccionClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteFocusGained

    private void txtDireccionClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteKeyPressed

    private void txtKmDeSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKmDeSalidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmDeSalidaActionPerformed

    private void txtKmDeSalidaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKmDeSalidaFocusGained
        txtKmDeSalida.setSelectionStart(0);
        txtKmDeSalida.setSelectionEnd(txtKmDeSalida.getText().length());
    }//GEN-LAST:event_txtKmDeSalidaFocusGained

    private void txtKmDeSalidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKmDeSalidaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            kilometraje = Integer.parseInt(txtKmDeSalida.getText());
            txtnombreDeConductor.requestFocus();
            txtnombreDeConductor.requestFocus();
        }
    }//GEN-LAST:event_txtKmDeSalidaKeyPressed

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void txtPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaKeyPressed

        /*Evento al oprimir la tecla F2*/
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {

                FConsultarVehiculos formulario = new FConsultarVehiculos(ini, this);
                this.getParent().add(formulario);
                formulario.toFront();
                formulario.setClosable(true);
                formulario.setVisible(true);
                formulario.setTitle("Formulario para consultar los vehiculos");
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                //form.setSize(PrincipalLogistica.escritorio.getSize());
                formulario.setLocation((screenSize.width - formulario.getSize().width) / 2, (screenSize.height - formulario.getSize().height) / 2);
                formulario.show();
            }

        }
        /*Evento al oprimir la tecla ENTER */
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                System.out.println("Cargando datos despues del enter -> \n\n");

                btnImprimir.setEnabled(false);
                jBtnImprimir.setEnabled(false);

                try {
                    carro = null;

                    /* SE BUSCA EN VEHICULO EN EL ARRAY DEL SISTEMA */
                    for (CCarros obj : ini.getListaDeVehiculos()) {
                        if (obj.getPlaca().equals(txtPlaca.getText().trim())) {
                            carro = new CCarros(ini);
                            carro = obj;
                            break;
                        }
                    }

                    System.out.println("busca el carro con el manifiesto -> \n\n");

                    try {

                        /* Se valid que el vehículo exista en el sistema*/
                        if (carro == null) {
                            txtPlaca.requestFocus();
                            JOptionPane.showInternalMessageDialog(this, "ESE VEHICULO NO EXISTE EN LA BASE DE DATOS ", "Error", 0);

                        } else {
                            /* INICIAMOS EL NUEVO MANIFIESTO, EN EL CUAL SE CONSULTA LOS MANIFIESTOS
                         QUE TENGA LA PLACA INGRESADA EN EL CAMPO DE TEXTO, DE LOS
                         CUALES SE TOMA EL ÚLTIMO */
                            manifiestoActual = new CManifiestosDeDistribucion(ini, txtPlaca.getText().trim());
                            System.out.println("crea el  el manifiesto -> \n\n");

                            /* Si el vehiculo no tiene manifiestos, es decir no ha trabajado en la operación, entonces se crea el 
                         manifiesto de distribución en blanco y se le asigna un estado =0 
                         si ya tiene manifiestos creados , la consulta trae el último que sacó a distribución  y verifica su estado */
                            if (manifiestoActual.getNumeroManifiesto().equals("0")) {
                                manifiestoActual.setEstadoManifiesto(0);
                                manifiestoActual.setIsFree(1);

                            } else {

                                /* Se crea un un procesoconcurrente para consultar el manifiesto y 
                         verificar su estado */
                                new Thread(new HiloConsultarManifiesto(ini, this)).start();

                            }

                        }

                    } catch (Exception ex) {
                        Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (Exception ex) {
                    Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_txtPlacaKeyPressed

    private void txtPlacaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusGained
        txtPlaca.setSelectionStart(0);
        txtPlaca.setSelectionEnd(txtPlaca.getText().length());
    }//GEN-LAST:event_txtPlacaFocusGained

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (manifiestoActual != null) {

            while (!manifiestoActual.liberarManifiesto(true)) {
                liberado = true;
            }

        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void txtNumeroDeFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed

        /*valida sin pendientes del conductor */
        try {
            if (manifiestoActual.getEstadoManifiesto() == 2) {

                if (false) {
                    //if (manifiestoActual.getCantidadManifiestosPendientesConductor() > 0) {
                    JOptionPane.showMessageDialog(this, "El conductor tiene pendientes de ruta,\n no se puede Imprimir este manifiesto", "Conductor no esta paz y salvo", JOptionPane.ERROR_MESSAGE, null);

                    return;
                }

                /*Manifiesto no grabado en la BBDD, trae los registros localmente */
                manifiestoActual.setListaFacturasPorManifiesto(listaDeFacturasPorManifiesto);
            } else {
                /*Manifiesto grabado en la BBDD , trae registros desde allí*/
                manifiestoActual.setListaFacturasPorManifiesto();
            }

            /* Genera el manifiesto*/
            //ReporteFacturasEnDistribucion1 demo = new ReporteFacturasEnDistribucion1(ini, manifiestoActual);
            /*Genera el rutero*/
            RepporteRuteroConductores demo = new RepporteRuteroConductores(ini, manifiestoActual);

            //reporteSalidaADistribucion demo = new reporteSalidaADistribucion(ini, manifiestoActual);
        } catch (Exception ex) {
            Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnContadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContadoActionPerformed
        if (btnContado.isSelected()) {
            formaDePago = "CONTADO"; // contado
        } else {
            formaDePago = "CREDITO"; // crédito
        }
    }//GEN-LAST:event_btnContadoActionPerformed

    private void btnCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreditoActionPerformed
        if (btnCredito.isSelected()) {
            formaDePago = "CREDITO"; // crédito
        } else {
            formaDePago = "CONTADO"; // contado
        }
    }//GEN-LAST:event_btnCreditoActionPerformed

    private void jTabbedPane1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1FocusGained

    private void txtTelefonoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoClienteActionPerformed

    private void txtNombreDeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDeClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeClienteActionPerformed

    private void txtNumeroManifiestoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroManifiestoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroManifiestoFocusGained

    private void txtNumeroManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroManifiestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroManifiestoActionPerformed

    private void txtNumeroManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroManifiestoKeyPressed
        /*Evento al oprimir la tecla Enter */
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            manifiestoActual = null;

            /* Se realiza la consulta para buscar el manifiesto que se va a descargar*/
            //new Thread(new HiloConsultarManifiesto(ini, this)).start();
            new Thread(new HiloFAdicionarPedidosNoReportados(ini, this, "consultarManifiesto")).start();
            txtNumeroDeFactura.requestFocus();

        }
    }//GEN-LAST:event_txtNumeroManifiestoKeyPressed

    private void txtCanalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCanalFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCanalFocusGained

    private void txtCanalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCanalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCanalActionPerformed

    private void txtCanalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCanalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCanalKeyPressed

    private void txtRutaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRutaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaFocusGained

    private void txtRutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRutaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaActionPerformed

    private void txtRutaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRutaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaKeyPressed

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        limpiar();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
        //        if (listaDeFacturasTrasladadas.isEmpty()) {
            //            JOptionPane.showInternalMessageDialog(this, "No hay datos para trasladar al Manifiesto # " + manifiestoDestino.getNumeroManifiesto(), "! Alerta !", JOptionPane.WARNING_MESSAGE);
            //        } else {
            //            if (grabarTrasladoDeFacturas()) {
                //
                //                return;
                //            }
            //        }
    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        limpiar();
        //        txtManifiestoOrigen.setEnabled(true);
        //        txtManifiestoOrigen.setEditable(true);
        //        txtManifiestoDestino.requestFocus();
        //        txtManifiestoDestino.setEditable(false);
        //        txtManifiestoOrigen.requestFocus();
    }//GEN-LAST:event_jBtnNuevoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar brrProgreso;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JToggleButton btnCancelar1;
    public javax.swing.JRadioButton btnContado;
    public javax.swing.JRadioButton btnCredito;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnImprimir;
    private javax.swing.JToggleButton btnMostrarDocumento;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    public com.toedter.calendar.JDateChooser dateManifiesto;
    public javax.swing.JButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnImprimir;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableFacturasPorManifiesto;
    public javax.swing.JTable jTableProductosPorFactura;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCantidadFacturas;
    public javax.swing.JLabel lblCirculoDeProgreso1;
    private javax.swing.JLabel lblKilometros;
    public org.edisoncor.gui.label.LabelCustom lblValorFactura;
    public org.edisoncor.gui.label.LabelCustom lblValorRecaudoManifiesto;
    private javax.swing.JPanel pnlAgregarFactura;
    public javax.swing.JTextField txtBarroCliente;
    public javax.swing.JTextField txtCanal;
    public javax.swing.JTextField txtDireccionCliente;
    public javax.swing.JTextField txtKmDeSalida;
    public javax.swing.JTextField txtNombreDeCliente;
    public javax.swing.JTextField txtNombreVendedor;
    public javax.swing.JTextField txtNumeroDeFactura;
    public javax.swing.JTextField txtNumeroManifiesto;
    public javax.swing.JTextField txtPlaca;
    public javax.swing.JTextField txtRuta;
    public javax.swing.JTextField txtTelefonoCliente;
    public javax.swing.JTextField txtnombreDeConductor;
    // End of variables declaration//GEN-END:variables

    public void llenarCamposDeTexto(CEmpleados empleado) {
        try {

            //txtApellidos.setText(empleado.getApellidos());
        } catch (Exception ex) {
        }
    }

    private void habilitar(boolean valor) {
        try {
            jTabbedPane1.setEnabled(valor);

            txtNombreDeCliente.setEditable(valor);
            txtTelefonoCliente.setEditable(valor);
            txtNombreVendedor.setEditable(valor);

            //dateVencimiento.setEnabled(valor);
            jTableFacturasPorManifiesto.setEnabled(valor);
           

        } catch (Exception ex) {
        }
    }

    public boolean validar() {
        boolean verificado = true;
        mensaje = "";

        return verificado;
    }

    private boolean validarTabla() {
        boolean validado = true;
        mensaje = "";

        if (txtTelefonoCliente.getText().isEmpty()) {
            mensaje += "No ha introducido el número del documento. \n ";
            validado = false;
        }
        if (txtNombreDeCliente.getText().isEmpty()) {
            mensaje += "No ha introducido la entidad que genera el documento. \n";
            validado = false;
        }
        if (txtNombreVendedor.getText().isEmpty()) {
            mensaje += "No ha introducido donde fué expedido el documento. \n";
            validado = false;
        }

        return validado;
    }

    private void limpiar() {

        try {
            //cbxPlacas.setSelectedIndex(0);
            //cbxManifiestosDeDistribucion.setSelectedIndex(0);

            txtPlaca.setText("");

            txtnombreDeConductor.setText("");

            txtKmDeSalida.setText("");

            DefaultTableModel model;
            model = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
            if (model.getRowCount() > 0) {
                int a = model.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    model.removeRow(i);
                }
            }
            model = (DefaultTableModel) jTableProductosPorFactura.getModel();
            if (model.getRowCount() > 0) {
                int a = model.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    model.removeRow(i);
                }
            }

            listaDeFacturasPorManifiesto = null;

            lblValorRecaudoManifiesto.setText("$ 0.0");
            lblValorFactura.setText("$ 0.0");
        } catch (Exception ex) {
        }
        txtNumeroDeFactura.setText("");
        txtNumeroDeFactura.setText("");
        txtNombreDeCliente.setText("");
        txtBarroCliente.setText("");
        txtDireccionCliente.setText("");
        txtTelefonoCliente.setText("");
        txtNombreVendedor.setText("");
        lblCantidadFacturas.setText("0");

    }

    private void limpiarDocumentoAgregado() {
        txtNumeroDeFactura.setText("");
        txtNombreDeCliente.setText("");
        txtTelefonoCliente.setText("");
        txtNombreVendedor.setText("");
//        txtRutaDelArchivo.setText("");
//        dateExpedicion.setDate(new Date());
//        dateVencimiento.setDate(new Date());
    }

    private boolean validarManifiesto() {
        boolean verificado = true;
        try {

            mensaje = "";
//            String fi = String.valueOf(Inicio.getFechaSql());
//            String ff = String.valueOf(ini.getFechaActualServidor());

//            if (!fi.equals(ff)) {
//                mensaje += "La fecha del Servidor y el Sistema no coinciden, verificar configuración del sistema " + "  \n";
//                verificado = false;
//            }
            if (carro == null) {
                mensaje += "No ha selecccionado el Vehiculo de la Ruta" + "  \n";
                verificado = false;
            }
            if (conductor == null) {
                mensaje += "No ha asigando el conductor de la ruta" + "  \n";
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
            if (despachador == null) {
                mensaje += "No ha asigando un despachador de ruta.El Campo es obligatorio " + "  \n";
                verificado = false;
            }

            if (txtKmDeSalida.getText().isEmpty()) {
                mensaje += "No ha colocado el kilometraje de salidad  del vehículo" + "  \n";
                verificado = false;
            }

            if (canalDeVenta == null) {
                mensaje += "No ha selecccionado El canal de venta " + "  \n";
                verificado = false;
            }
            if (ruta == null) {
                mensaje += "No ha selecccionado la Ruta a distribuir " + "  \n";
                verificado = false;
            }
            if (manifiestoActual == null) {
                mensaje += "Vehiculo ya tiene asignado un manifiesto de Distribución sin cerrar" + "  \n";
                verificado = false;
            }

        } catch (Exception ex) {
            Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificado;
    }

  
    private void llenarDatosManifiesto() throws Exception {
        double valorManifiesto = 0.0;

        /*  Se llenan los campos del conducor */
        for (CEmpleados obj : ini.getListaDeEmpleados()) {
            if (manifiestoActual.getConductor().equals(obj.getCedula())) {

                txtnombreDeConductor.setText(obj.getNombres() + " " + obj.getApellidos());
            }
        }

        /*  Se llenan los campos de l canal de distribución  */
        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            if (obj.getIdCanalDeVenta() == manifiestoActual.getIdCanal()) {
                txtCanal.setText(obj.getNombreCanalDeVenta());

            }
        }

        /*  Se llenan los campos ruta de distribución  */
        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            if (obj.getIdRutasDeDistribucion() == manifiestoActual.getIdRuta()) {
                txtRuta.setText(obj.getNombreRutasDeDistribucion());
                ruta = obj;
            }
        }
        txtKmDeSalida.setText("" + carro.getKilometrajeActual());

        manifiestoActual.setIsFree(0);
        manifiestoActual.liberarManifiesto(false);
        // manifiestoActual.setListaFacturasPorManifiesto();
        listaDeFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto();

        lblValorRecaudoManifiesto.setText(nf.format(manifiestoActual.getValorTotalManifiesto()));

        valorTotalManifiesto = 0.0;
        modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();

        //  SE LLENA EL JTABLE FACTURAS POR MANIFIESTO
        if (listaDeFacturasPorManifiesto != null) {

          

            for (CFacturasPorManifiesto obj : listaDeFacturasPorManifiesto) {

                Vst_Factura fac = new Vst_Factura(ini, obj.getNumeroFactura());
                int fila = jTableFacturasPorManifiesto.getRowCount();
                modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);

                jTableFacturasPorManifiesto.setValueAt("" + obj.getAdherencia(), fila, 0); // item 
                jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), fila, 1); // numero de la factura
                jTableFacturasPorManifiesto.setValueAt(fac.getNombreDeCliente(), fila, 2); // cliente

                jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), fila, 3); // valor de la factura   
                valorTotalManifiesto += obj.getValorARecaudarFactura();

                // se ubica en la fila insertada
                jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);

                lblValorRecaudoManifiesto.setText(nf.format(valorTotalManifiesto));
                lblCantidadFacturas.setText("" + manifiestoActual.getListaFacturasPorManifiesto().size());

                Vst_FacturasPorManifiesto vfxm = new Vst_FacturasPorManifiesto();
                
                vfxm.setNumeroFactura(fac.getNumeroFactura());
                vfxm.setNombreDeCliente(fac.getNombreDeCliente());
                vfxm.setDireccionCliente(fac.getDireccionDeCliente());
                vfxm.setValorTotalFactura(fac.getValorTotalFactura());
                

                valorManifiesto = valorManifiesto + obj.getValorARecaudarFactura();
            }
            manifiestoActual.setValorTotalManifiesto(valorManifiesto);
        }

    }

    public void limpiarDatodsDePanelProductosPorFactura() {

        DefaultTableModel model;
        model = (DefaultTableModel) jTableProductosPorFactura.getModel();
        if (model.getRowCount() > 0) {
            int a = model.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                model.removeRow(i);
            }
        }
        txtNombreDeCliente.setText("");
        txtDireccionCliente.setText("");
        txtTelefonoCliente.setText("");
        txtBarroCliente.setText("");
        txtNombreVendedor.setText("");
    }

    private void ordenarTabla() {
        for (int i = 0; i < jTableFacturasPorManifiesto.getRowCount(); i++) {
            jTableFacturasPorManifiesto.setValueAt(i + 1, i, 0);
        }

    }

    public synchronized void crearNuevoManifiesto(CCarros carro) {
        try {
            limpiar();
            this.manifiestoActual = new CManifiestosDeDistribucion(ini);

            modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
            txtPlaca.setText(carro.getPlaca());
            txtKmDeSalida.setText("" + carro.getKilometrajeActual());

            /* Se verifica que el vehiculo tenga asignado un conductor */
            if (carro.getConductor().length() > 1) {
                try {
                    conductor = new CEmpleados(ini);
                    for (CEmpleados obj : ini.getListaDeEmpleados()) {
                        if (obj.getCedula().equals(carro.getConductor())) {
                            conductor = obj;
                        }

                    }
                    txtnombreDeConductor.setText(conductor.getNombres() + " " + conductor.getApellidos());

                } catch (Exception ex) {
                    Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            txtnombreDeConductor.setEditable(false);

            txtKmDeSalida.setEditable(true);
            txtPlaca.setEnabled(true);
            txtPlaca.setEditable(true);

            dateManifiesto.setEnabled(true);
            jTabbedPane1.setEnabled(true);

            txtnombreDeConductor.setEnabled(true);

            txtKmDeSalida.requestFocus();
            txtKmDeSalida.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //public synchronized void modificarManifiesto() {
    public void modificarManifiesto() throws Exception {

        /* Se verifica que haya un manifiesto instanciado */
        if (manifiestoActual != null) {

            /* Se valida que el manifiesto actual esté disponible , es decir que no esté otro usuario ingresando
             facturas al sistema con el mismo vehículo*/
            //if (manifiestoActual.getIsFree() == 1) {
            if ((manifiestoActual.getIsFree() == 1) || (manifiestoActual.getUsuarioManifiesto().equals(Inicio.deCifrar(ini.getUser().getNombreUsuario())))) {
                try {

                    modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
                    // manifiestoActual.listadoDeFacturas(manifiestoActual.getNumeroManifiesto());

                    listaDeFacturasPorManifiesto = new ArrayList<>(); // CfacturasPorManifiesto

                    /*Se llama al fichero que contiene los numero de las facturas
                     a  que están registradas en el manifiesto*/
                    File fichero = new File(this.manifiestoActual.getRutArchivofacturasporManifiesto());

                    /*Se valida que exista el fichero */
                    if (fichero.exists()) {

                        manifiestoActual.setListaFacturasPorManifiesto(fichero);

                        listaDeFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto();

                        if (listaDeFacturasPorManifiesto.isEmpty() || listaDeFacturasPorManifiesto == null) {
                            btnGrabar.setEnabled(false);
                            jBtnGrabar.setEnabled(false);
                        } else {
                            btnGrabar.setEnabled(true);
                            jBtnGrabar.setEnabled(true);
                        }

                        llenarDatosManifiesto();

                        txtnombreDeConductor.setEditable(false);

                        txtKmDeSalida.setEditable(false);
                        txtPlaca.setEnabled(true);
                        txtPlaca.setEditable(false);
                        dateManifiesto.setEnabled(false);
                        jTabbedPane1.setEnabled(true);

                        btnGrabar.setEnabled(true);
                        jBtnGrabar.setEnabled(true);

                        txtBarroCliente.setEditable(false);
                        txtDireccionCliente.setEditable(false);
                        txtNombreDeCliente.setEditable(false);
                        txtTelefonoCliente.setEditable(false);

                        txtNombreVendedor.setEditable(false);

                        dateManifiesto.setDate(Inicio.getFechaSql(manifiestoActual.getFechaDistribucion()));

                        txtNumeroDeFactura.setEnabled(true);
                        txtNumeroDeFactura.setEditable(true);
                        txtNumeroDeFactura.requestFocus();
                        txtNumeroDeFactura.requestFocus();

                    } else {

                        listaDeFacturasPorManifiesto = new ArrayList<>();
                       

                        llenarDatosManifiesto();

                        txtPlaca.setEditable(false);
                        txtKmDeSalida.setEditable(false);
                        dateManifiesto.setDate(Inicio.getFechaSql(manifiestoActual.getFechaDistribucion()));

                        txtnombreDeConductor.setEditable(false);

                        jTabbedPane1.setEnabled(true);
                        dateManifiesto.setEnabled(false);
                        txtNumeroDeFactura.setEnabled(true);
                        txtNumeroDeFactura.setEditable(true);
                        txtNumeroDeFactura.requestFocus();
                        txtNumeroDeFactura.requestFocus();

                        txtNumeroDeFactura.requestFocus();
                        txtNumeroDeFactura.requestFocus();

                    }
                } catch (Exception ex) {
                    Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                limpiar();
                liberado = false;
                jTabbedPane1.setEnabled(false);
                manifiestoActual = null;
                dateManifiesto.setEnabled(false);
                txtKmDeSalida.setEnabled(false);

                JOptionPane.showMessageDialog(this, "Esa Ruta está siendo despachada por otro usuario ", "Error", 0);
                txtPlaca.requestFocus();

            }
        }
    }

    /**
     * Método que permite validae¿r los datos para poder crear un manifiesto
     *
     * @return verdadero sí los datos son correctos y retorna falso sí los datos
     * no son válidos
     */
    private List<String> listaDeFacturasNoRepetidas() {
        List<String> cadena = new ArrayList<>();
        boolean aparece = false;
        /* Se recorren todos los elementos del listado de  las facturas en el archivo */
        for (String obj : listaDeFacturasEnElArchivo) {
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
                }
                /* al salir del bucle se valida que no existe la facura en el segundo array y se agrega el elmento.  */
                if (!aparece) {
                    cadena.add(obj);
                }
            }

        }

        return cadena;

    }

    /**
     * Método que permite valida si la factura e encuentra ingresada en el
     * manifiesto actual
     *
     * @return verdadero sí la factura ya está ingresada y retorna falso sí la
     * factura no se encuentra ingresada.
     */
    public boolean estaLaFacturaEnElManifiesto() {
        boolean existe = false;
        if (listaDeFacturasPorManifiesto != null) {
            for (CFacturasPorManifiesto obj : listaDeFacturasPorManifiesto) {
                if (facturaActual.getNumeroDeFactura().equals(obj.getNumeroFactura())) {
                    existe = true;
                    break;
                }
            }
        } else {
            listaDeFacturasPorManifiesto = new ArrayList<>();
        }

        return existe;
    }

    /**
     * Método que permite validae¿r los datos para poder crear un manifiesto
     *
     * @return verdadero sí los datos son correctos y retorna falso sí los datos
     * no son válidos
     */
    public boolean validarDatos() {
        boolean isValido = true;
        mensaje = "";
        if (conductor == null) {
            mensaje += "El presente manifiesto no tiene asignado un Conductor \n";
            isValido = false;
        }
        if (auxiliar1 == null) {
            mensaje += "El presente manifiesto no tiene un auxiliar válido \n";
            isValido = false;
        }
        if (carro == null) {
            mensaje += "El presente manifiesto no tiene asignado un Vehículo \n";
            isValido = false;

        }
        if (kilometraje == 0) {
            mensaje += "El kilometraje no es válido \n";
            isValido = false;

        }
        if (canalDeVenta == null) {
            mensaje += "El Canal de distribución no está asignado \n";
            isValido = false;

        }
        if (ruta == null) {
            mensaje += "No se h asignado una ruta válida \n";
            isValido = false;

        }

        return isValido;

    }

    /**
     * métoto que me permite identificar que la factura no esté duplicada
     *
     * @param numeroDefactura es el numero de la factura que se consulta
     * @return true si la factura ya está descargada, falso en caso contrario
     */
    public boolean estaLaFacturaEnElManifiesto(String numeroDefactura) {
        boolean descargada = false;
        for (CFacturasPorManifiesto obj : listaDeFacturasPorManifiesto) {
            if (numeroDefactura.equals(obj.getNumeroFactura())) {
                descargada = true;
            }
        }
        return descargada;
    }

    /**
     * Método quepermite organizar el archivo temporal donde se guardan los
     * datos de las facturas del manifiesto y calcula el valor total a recaudar
     * por el manifiesto y calcula la cantidad de facturas reportadas.
     *
     *
     * @return nada
     */
    private void eliminarRegistroDelArchivo(String numeroDeFactura) throws Exception {
        valorTotalManifiesto = 0.0;
        String rutaDearchivo = manifiestoActual.getRutArchivofacturasporManifiesto(); //"tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_FacturasDescargados.txt";
        ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDearchivo);

        int fila = jTableFacturasPorManifiesto.getSelectedRow();

        /* Aca se borra la linea que contiene el numero de la factura*/
        archivo.borrarLinea(fila);

        int i = 1;

        /* Elimina reegistro del Array que contiene las facturas por manifiesto */
        for (CFacturasPorManifiesto obj : listaDeFacturasPorManifiesto) {
            if (obj.getNumeroFactura().equals(numeroDeFactura)) {
                listaDeFacturasPorManifiesto.remove(obj);

                manifiestoActual.setListaFacturasPorManifiesto(listaDeFacturasPorManifiesto);
                break;
            }
            valorTotalManifiesto += obj.getValorARecaudarFactura();

        }

        /* Elimina reegistro del Array que contiene las CfcturasCamdun del Manifiesto */
        for (CFacturasPorManifiesto obj : listaDeFacturasPorManifiesto) {
            if (obj.getNumeroFactura().equals(numeroDeFactura)) {
                CFacturas fac = new CFacturas(ini, obj.getNumeroFactura());
                /*  libera la factura para ser usada por otro usuario */
                fac.setIsFree(1);
                fac.liberarFactura(true);

                listaDeFacturasPorManifiesto.remove(obj);
//                              manifiestoActual.setListaCFacturasCamdun(listaDeCFacturasCamdunEnElManifiesto);
                break;
            }
        }

        lblValorRecaudoManifiesto.setText(nf.format(valorTotalManifiesto));
        lblCantidadFacturas.setText("" + listaDeFacturasPorManifiesto.size());

    }

    private void eliminarDatosDejList(JList list) {

        DefaultListModel model = new DefaultListModel();
        list.setModel(model);
        //See more at: http://collectioncode.com/java/limpiar-jlist-en-java/#sthash.v1nd3O4o.dpuf

    }

    public void llenarjTableFacturasPorManifiesto() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        File fichero = new File(manifiestoActual.getRutArchivofacturasporManifiesto());

        /* Se arma el array de sentencias SQL para grabar las facturas  descargados */
        if (fichero.exists()) {

            modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();

            for (CFacturasPorManifiesto obj : listaDeFacturasPorManifiesto) {
                Vst_Factura factura = new Vst_Factura(ini, obj.getNumeroFactura());

                filaTabla2 = jTableFacturasPorManifiesto.getRowCount();
                modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);
                jTableFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                jTableFacturasPorManifiesto.setValueAt(factura.getNumeroFactura(), filaTabla2, 1); // numero de factura
                jTableFacturasPorManifiesto.setValueAt(factura.getNombreDeCliente(), filaTabla2, 2); // nombre del nombreDelCliente
                jTableFacturasPorManifiesto.setValueAt(nf.format(factura.getValorTotalFactura()), filaTabla2, 3);

            }

        }

    }

    private void borrarTodasLasfacturas() throws Exception {

        // se borran datos del JTable
        DefaultTableModel model;
        model = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
        if (model.getRowCount() > 0) {
            int a = model.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                model.removeRow(i);
            }
        }

        /*Se elimina el archivo temporal */
        File fichero = new File(manifiestoActual.getRutArchivofacturasporManifiesto());
        fichero.delete();

        // Habilitar las facturas manifestadas
        for (CFacturasPorManifiesto obj : listaDeFacturasPorManifiesto) {
            CFacturas fac = new CFacturas(ini, obj.getNumeroFactura(), false);
            fac.liberarFactura(true);

        }

        // Eliminamos los arrays de facturas
        listaDeFacturasPorManifiesto = null;
        lblValorRecaudoManifiesto.setText("$ 0.0");
        lblValorFactura.setText("$ 0.0");
        lblCantidadFacturas.setText("0");

        limpiarDatodsDePanelProductosPorFactura();

    }

    private void enviarSms(String destinatarios, String texto) {
        try {
            // String usuario="distrilog";
            //String clave="Distri22";
            //String destinatarios = "573164671160";
            //String texto = "mimor+este+es+mi+primer+mensaje+en+la+web,+dedicado+con+todo+mi+amor...";
            // String origen="Distrilog B2B";
            //String urlLink="https://gateway.plusmms.net/send.php?"

            String urlLink = ini.getUrlLinkSMS()
                    + "username=" + ini.getUSuarioSMS() + "&"
                    + "password=" + ini.getClaveSMS() + "&"
                    + "to=" + destinatarios + "&"
                    + "text=" + texto + "&"
                    + "from=" + ini.enviaSMS() + "&"
                    + "coding=0&"
                    + "dlr-mask=8";
            //karely@nrsgateway.com
            URL url = new URL(urlLink);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String mensaje;
            while ((mensaje = br.readLine()) != null) {
                sb.append(mensaje);
            }

            System.out.println(sb.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FAdicionarPedidosNoReportados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
