/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios;

import aplicacionlogistica.distribucion.consultas.FBuscarListadoDeEmpleados;
import aplicacionlogistica.distribucion.consultas.FConsultarVehiculos;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.distribucion.Threads.HiloGuardarFacturasPorManifiesto;
import aplicacionlogistica.distribucion.Threads.JcProgressBar;
import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Threads.HiloFmanifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.imprimir.RepporteRuteroConductoresConPesosyDEscuentos;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import mtto.vehiculos.CCarros;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import mtto.documentos.objetos.DocumentosPorVehiculo;

/**
 * Esta vista, permite que el usuario pueda asignar a un manifiesto de
 * Distribución el conductor, el auxiliar, un carro dando una ruta y
 * clasificandolo dentro de un cnal de venta; el sistema se encrga de crear el
 * manifiestos y posteriormente se le asignan las factura que van a salir a
 * distrbucion.
 *
 * @author Luis Eduardo López
 */
public class FManifestarPedidosEnRuta extends javax.swing.JInternalFrame {

    public CEmpleados conductor = null;
    public CEmpleados auxiliar1 = null;
    public CEmpleados auxiliar2 = null;
    public CEmpleados auxiliar3 = null;
    public CEmpleados despachador = null;
    public CCarros carro = null;
    public CFacturas facturaActual = null;
    public static boolean band = false;
    public static int valorDespBarraProgreso = 0;
    public File archivoConListaDeFacturas;
    public Double valorTotalManifiesto = 0.0;
    public boolean cargado = false;
    boolean liberado = false;
    public boolean grabar = false;
    public boolean nuevo = false;
    int kilometraje = 0;
    public int formaDePago = 0;
    public String mensaje = null;
    String listaDeFacturas;
    public int contadorDeFacturas = 0;
    public double sumadorPesosFacturas = 0.0;
    public List<String> listaDeFacturasEnElArchivo;
    public List<CEmpleados> listaDeAuxiliares = new ArrayList<>();
    public CManifiestosDeDistribucion manifiestoActual = null;
    public boolean estaOcupadoGrabando = false;
    String cadenaDeFacturas;

    //CUsuarios user;
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    public CRutasDeDistribucion ruta;
    public CCanalesDeVenta canalDeVenta;
    Inicio ini = null;
    public DefaultListModel modelo, modelo3;
    public TextAutoCompleter autoTxtVehiculos;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public DefaultTableModel modelo1, modelo2;
    public int filaTabla2;

    public String getCadenaDeFacturas() {
        return cadenaDeFacturas;
    }

    public void setCadenaDeFacturas(String cadenaDeFacturas) {
        this.cadenaDeFacturas = cadenaDeFacturas;
    }

    public void setCadenaDeFacturas() {
        cadenaDeFacturas = "('";
        for (CFacturasPorManifiesto fac : manifiestoActual.getListaFacturasPorManifiesto()) {
            cadenaDeFacturas += fac.getNumeroFactura() + "','";
        }
        cadenaDeFacturas = cadenaDeFacturas.substring(0, cadenaDeFacturas.length() - 2);
        cadenaDeFacturas += ");";
    }

    /**
     * Creates new fReportemovilizadoPorConductor
     * IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public FManifestarPedidosEnRuta(Inicio ini) throws Exception {
        try {
            initComponents();

            /*Atualiza el listado de vehiculos de la BBDD*/
            //new Thread(new HiloListadoDePlacas(ini)).start();
            this.ini = ini;
            lblBarraDeProgreso.setVisible(false);
            dateManifiesto.setDate(new Date());
            cbxRutaDeDistribucion.removeAllItems();
            cbxCanales.removeAllItems();
            cbxPrefijos.removeAllItems();

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
            Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        }

        // cargarVista();
        new Thread(new HiloFmanifestarPedidosEnRuta(ini, this, "cargarVista")).start();
        cancelar();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnuBorrarFilas_ = new javax.swing.JPopupMenu();
        borraElementos = new javax.swing.JMenu();
        borrar1Fila = new javax.swing.JMenuItem();
        borrarTodo = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlAgregarLista = new javax.swing.JPanel();
        btnFile = new javax.swing.JButton();
        txtFile = new javax.swing.JTextField();
        btnAgregarFacturas = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListListaDeFacturasErradas = new javax.swing.JList();
        jScrollPane5 = new javax.swing.JScrollPane();
        listaDeFacturas1 = new javax.swing.JList();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblBarraDeProgreso = new javax.swing.JLabel();
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
        jPanel4 = new javax.swing.JPanel();
        lblCantidadFacturas = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lblPesoDeLaFactura = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        cbxPrefijos = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbxRutaDeDistribucion = new javax.swing.JComboBox();
        btnCrearManifiesto = new javax.swing.JButton();
        txtNumeroDeManifiesto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbxCanales = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        lblCirculoDeProgreso2 = new javax.swing.JLabel();
        lblPesoManifiesto = new org.edisoncor.gui.label.LabelCustom();
        jLabel7 = new javax.swing.JLabel();
        dateManifiesto = new com.toedter.calendar.JDateChooser();
        lblValorRecaudoManifiesto = new org.edisoncor.gui.label.LabelCustom();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        txtnombreDeConductor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtNombreDeAuxiliar1 = new javax.swing.JTextField();
        lblCirculoDeProgreso1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblKilometros = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtNombreDeAuxiliar2 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtNombreDeAuxiliar3 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtNombreDedespachador = new javax.swing.JTextField();
        brrProgreso = new javax.swing.JProgressBar();
        txtKmDeSalida = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableFacturasPorManifiesto = new javax.swing.JTable();
        lblNumeroManifiesto = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnNuevo1 = new javax.swing.JButton();
        jBtnBorrarFIla = new javax.swing.JToggleButton();
        jBtnObservaciones = new javax.swing.JToggleButton();
        jBtnDocVencidos = new javax.swing.JToggleButton();
        jBtnRefrescar = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();
        txtPesoMAximoAutorizado = new javax.swing.JTextField();

        borraElementos.setText("Borrar Una Fila");

        borrar1Fila.setText("Borrar Factura");
        borrar1Fila.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                borrar1FilaMouseClicked(evt);
            }
        });
        borrar1Fila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrar1FilaActionPerformed(evt);
            }
        });
        borraElementos.add(borrar1Fila);

        borrarTodo.setText("Borrar todas las facturas");
        borrarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarTodoActionPerformed(evt);
            }
        });
        borraElementos.add(borrarTodo);

        mnuBorrarFilas_.add(borraElementos);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Formulario para manifestar Facturas en Ruta");
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

        pnlAgregarLista.setAutoscrolls(true);
        pnlAgregarLista.setDoubleBuffered(false);
        pnlAgregarLista.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pnlAgregarListaFocusGained(evt);
            }
        });

        btnFile.setText("Buscar archivo");
        btnFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFileActionPerformed(evt);
            }
        });

        btnAgregarFacturas.setText("Agregar Facturas");
        btnAgregarFacturas.setEnabled(false);
        btnAgregarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarFacturasActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jListListaDeFacturasErradas);

        jScrollPane5.setViewportView(listaDeFacturas1);

        jLabel9.setText("Facturas no encontradas");

        jLabel10.setText("Facturas en archivo");

        lblBarraDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/barraDeProgreso.gif"))); // NOI18N
        lblBarraDeProgreso.setAlignmentY(0.0F);
        lblBarraDeProgreso.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout pnlAgregarListaLayout = new javax.swing.GroupLayout(pnlAgregarLista);
        pnlAgregarLista.setLayout(pnlAgregarListaLayout);
        pnlAgregarListaLayout.setHorizontalGroup(
            pnlAgregarListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarListaLayout.createSequentialGroup()
                .addGroup(pnlAgregarListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAgregarListaLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(pnlAgregarListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAgregarListaLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlAgregarListaLayout.createSequentialGroup()
                                .addComponent(btnFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFile, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlAgregarListaLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlAgregarListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAgregarFacturas)
                            .addComponent(lblBarraDeProgreso))
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAgregarListaLayout.createSequentialGroup()
                        .addGap(431, 431, 431)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlAgregarListaLayout.setVerticalGroup(
            pnlAgregarListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarListaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlAgregarListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnFile)
                    .addGroup(pnlAgregarListaLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(txtFile)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAgregarListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addGap(4, 4, 4)
                .addGroup(pnlAgregarListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAgregarListaLayout.createSequentialGroup()
                        .addComponent(btnAgregarFacturas)
                        .addGap(108, 108, 108)
                        .addComponent(lblBarraDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAgregarListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Agregar Facturas Desde archivo", pnlAgregarLista);

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
        jTableProductosPorFactura.setComponentPopupMenu(mnuBorrarFilas_);
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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Peso de las Facturas"));

        lblPesoDeLaFactura.setFont(new java.awt.Font("Cantarell", 0, 36)); // NOI18N
        lblPesoDeLaFactura.setText("0.0 k.");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPesoDeLaFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblPesoDeLaFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel29.setText("Prefijo");

        javax.swing.GroupLayout pnlAgregarFacturaLayout = new javax.swing.GroupLayout(pnlAgregarFactura);
        pnlAgregarFactura.setLayout(pnlAgregarFacturaLayout);
        pnlAgregarFacturaLayout.setHorizontalGroup(
            pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAgregarFacturaLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 703, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                .addGap(5, 5, 5)
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
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDireccionCliente)
                                    .addComponent(txtTelefonoCliente)
                                    .addComponent(txtBarroCliente)
                                    .addComponent(txtNombreVendedor)
                                    .addComponent(txtNombreDeCliente)
                                    .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxPrefijos, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlAgregarFacturaLayout.setVerticalGroup(
            pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(cbxPrefijos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
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
                        .addComponent(lblValorFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Agregar Facturas", pnlAgregarFactura);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Manifiesto de Reparto"));
        jPanel7.setEnabled(false);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Ruta");

        cbxRutaDeDistribucion.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        cbxRutaDeDistribucion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxRutaDeDistribucion.setEnabled(false);
        cbxRutaDeDistribucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxRutaDeDistribucionActionPerformed(evt);
            }
        });

        btnCrearManifiesto.setText("Crear Manifiesto");
        btnCrearManifiesto.setEnabled(false);
        btnCrearManifiesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearManifiestoActionPerformed(evt);
            }
        });

        txtNumeroDeManifiesto.setEditable(false);
        txtNumeroDeManifiesto.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNumeroDeManifiesto.setToolTipText("Ingresar apellidos completos");
        txtNumeroDeManifiesto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeManifiestoFocusGained(evt);
            }
        });
        txtNumeroDeManifiesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeManifiestoKeyPressed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Manifiesto N°");

        cbxCanales.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        cbxCanales.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxCanales.setEnabled(false);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Canal");

        lblCirculoDeProgreso2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        lblPesoManifiesto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Kilogramos Cargue", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 10))); // NOI18N
        lblPesoManifiesto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPesoManifiesto.setText("0.0 Kg");
        lblPesoManifiesto.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblPesoManifiesto.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Fecha");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        dateManifiesto.setToolTipText("Seleccionar la fecha ");
        dateManifiesto.setDateFormatString("yyyy/MM/dd");
        dateManifiesto.setEnabled(false);
        dateManifiesto.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        lblValorRecaudoManifiesto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Valor Manifiesto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 10))); // NOI18N
        lblValorRecaudoManifiesto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValorRecaudoManifiesto.setText("$ 0.0");
        lblValorRecaudoManifiesto.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblValorRecaudoManifiesto.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxCanales, 0, 266, Short.MAX_VALUE)
                            .addComponent(cbxRutaDeDistribucion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroDeManifiesto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCrearManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCirculoDeProgreso2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblValorRecaudoManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblPesoManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbxCanales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addGap(1, 1, 1)
                                .addComponent(cbxRutaDeDistribucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNumeroDeManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCrearManifiesto)
                            .addComponent(jLabel6)))
                    .addComponent(lblCirculoDeProgreso2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblValorRecaudoManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPesoManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
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
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Placa");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Conductor");

        txtPlaca.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
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

        txtnombreDeConductor.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtnombreDeConductor.setToolTipText("");
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

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Auxiliar 1");

        txtNombreDeAuxiliar1.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreDeAuxiliar1.setToolTipText("Ingresar apellidos completos");
        txtNombreDeAuxiliar1.setAutoscrolls(false);
        txtNombreDeAuxiliar1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDeAuxiliar1FocusGained(evt);
            }
        });
        txtNombreDeAuxiliar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDeAuxiliar1KeyPressed(evt);
            }
        });

        lblCirculoDeProgreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Kilometraje");

        lblKilometros.setText("kms");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Auxiliar 2");

        txtNombreDeAuxiliar2.setEditable(false);
        txtNombreDeAuxiliar2.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreDeAuxiliar2.setToolTipText("Ingresar apellidos completos");
        txtNombreDeAuxiliar2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDeAuxiliar2FocusGained(evt);
            }
        });
        txtNombreDeAuxiliar2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDeAuxiliar2KeyPressed(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Auxiliar 3");

        txtNombreDeAuxiliar3.setEditable(false);
        txtNombreDeAuxiliar3.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreDeAuxiliar3.setToolTipText("Ingresar apellidos completos");
        txtNombreDeAuxiliar3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDeAuxiliar3FocusGained(evt);
            }
        });
        txtNombreDeAuxiliar3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDeAuxiliar3KeyPressed(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Despachador");

        txtNombreDedespachador.setEditable(false);
        txtNombreDedespachador.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreDedespachador.setToolTipText("Ingresar apellidos completos");
        txtNombreDedespachador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDedespachadorFocusGained(evt);
            }
        });
        txtNombreDedespachador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDedespachadorKeyPressed(evt);
            }
        });

        txtKmDeSalida.setEnabled(false);

        jTableFacturasPorManifiesto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Cliente", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableFacturasPorManifiesto.setComponentPopupMenu(mnuBorrarFilas_);
        jTableFacturasPorManifiesto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFacturasPorManifiestoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableFacturasPorManifiesto);
        if (jTableFacturasPorManifiesto.getColumnModel().getColumnCount() > 0) {
            jTableFacturasPorManifiesto.getColumnModel().getColumn(0).setMinWidth(50);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(0).setMaxWidth(50);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(1).setMinWidth(100);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(1).setMaxWidth(100);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(2).setMinWidth(270);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(2).setPreferredWidth(270);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(2).setMaxWidth(270);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(3).setMinWidth(110);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(3).setPreferredWidth(110);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(3).setMaxWidth(110);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtnombreDeConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDeAuxiliar1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDeAuxiliar2, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDeAuxiliar3, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDedespachador, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPlaca, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addComponent(txtKmDeSalida))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblKilometros))))
                .addGap(4, 4, 4))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(brrProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtKmDeSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblKilometros, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtnombreDeConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNombreDeAuxiliar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDeAuxiliar2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtNombreDeAuxiliar3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtNombreDedespachador, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(brrProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        lblNumeroManifiesto.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
        lblNumeroManifiesto.setText("Total a Valor Mcia. en Manifiesto # ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumeroManifiesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblNumeroManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setToolTipText("agregar registro");
        jBtnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo.setFocusable(false);
        jBtnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnNuevo.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnNuevo);

        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jBtnGrabar.setToolTipText("grabar registro");
        jBtnGrabar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnGrabar.setEnabled(false);
        jBtnGrabar.setFocusable(false);
        jBtnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGrabar.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGrabarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGrabar);

        jBtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jBtnImprimir.setToolTipText("imprimir mfto.");
        jBtnImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnImprimir.setEnabled(false);
        jBtnImprimir.setFocusable(false);
        jBtnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnImprimir.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnImprimirActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnImprimir);

        jBtnMinuta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Table.png"))); // NOI18N
        jBtnMinuta.setToolTipText("Consolidado mercancia");
        jBtnMinuta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnMinuta.setEnabled(false);
        jBtnMinuta.setFocusable(false);
        jBtnMinuta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnMinuta.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnMinuta.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnMinuta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnMinutaActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnMinuta);

        jBtnNuevo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/table_row_insert.png"))); // NOI18N
        jBtnNuevo1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo1.setEnabled(false);
        jBtnNuevo1.setFocusable(false);
        jBtnNuevo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnNuevo1.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnNuevo1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnNuevo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevo1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnNuevo1);

        jBtnBorrarFIla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/table_row_delete.png"))); // NOI18N
        jBtnBorrarFIla.setToolTipText("Borrar Fila");
        jBtnBorrarFIla.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnBorrarFIla.setEnabled(false);
        jBtnBorrarFIla.setFocusable(false);
        jBtnBorrarFIla.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnBorrarFIla.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnBorrarFIla.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnBorrarFIla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBorrarFIlaActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnBorrarFIla);

        jBtnObservaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Bubble.png"))); // NOI18N
        jBtnObservaciones.setToolTipText("Borrar Fila");
        jBtnObservaciones.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnObservaciones.setEnabled(false);
        jBtnObservaciones.setFocusable(false);
        jBtnObservaciones.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnObservaciones.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnObservaciones.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnObservaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnObservacionesActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnObservaciones);

        jBtnDocVencidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Radiation.png"))); // NOI18N
        jBtnDocVencidos.setToolTipText("Borrar Fila");
        jBtnDocVencidos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnDocVencidos.setEnabled(false);
        jBtnDocVencidos.setFocusable(false);
        jBtnDocVencidos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnDocVencidos.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnDocVencidos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnDocVencidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnDocVencidosActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnDocVencidos);

        jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N
        jBtnRefrescar.setToolTipText("Cancelar operacion");
        jBtnRefrescar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnRefrescar.setFocusable(false);
        jBtnRefrescar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnRefrescar.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnRefrescar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnRefrescarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnRefrescar);

        jBtnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancel.setToolTipText("Cancelar operacion");
        jBtnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnCancel.setFocusable(false);
        jBtnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCancel.setPreferredSize(new java.awt.Dimension(25, 25));
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
        jBtnExit.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExitActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExit);

        txtPesoMAximoAutorizado.setEditable(false);
        txtPesoMAximoAutorizado.setText("Peso Maximo Autorizado : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(280, 280, 280)
                        .addComponent(txtPesoMAximoAutorizado, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPesoMAximoAutorizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();
    }//GEN-LAST:event_btnSalirActionPerformed


    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        nuevo();


    }//GEN-LAST:event_btnNuevoActionPerformed


    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed

        grabar2();

    }//GEN-LAST:event_btnGrabarActionPerformed

    private void guardarFormulario() throws HeadlessException {
        ArrayList<String> sentenciasSQL;
        /* Se valida la conexión a internet para grabar los datos en la BBDD */
        if (ini.verificarConexion()) {

            sentenciasSQL = new ArrayList<>();

//                    cbxCanales.setEnabled(false);
//                    cbxRutaDeDistribucion.setEnabled(false);
            int contadorDeCiclos = 0; // CONTADOR DE LOS CICLOS

            try {

                /* SE CREA CADENA PARA GRABAR LOS REGISTROS EN LA BASE DE DATOS */
                for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
                    contadorDeCiclos++;

                    obj.setAdherencia(contadorDeCiclos);
                    //sentenciasSQL.add(obj.getSentenciaInsertSQL());

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
                    jBtnObservaciones.setEnabled(false);

                    /* EL MANIFIESTO SE BLOQUEA PARA QUE NO  PUEDA SER UTILIZADA  POR OTRO USUARIO */
                    manifiestoActual.setIsFree(1);
                    manifiestoActual.liberarManifiesto(true);
                    manifiestoActual.setValorTotalManifiesto(valorTotalManifiesto);

                    /* SE CAMBIA ELESTADO DE ALISTAMIENTO A DISTRIBUCION */
                    manifiestoActual.setEstadoManifiesto(3);

                    /* SE ACTUAIZA LOS DATOS DEL MANIFIESTO ACTUAL */
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date fechaEnviar = new Date();
                    String fechaActual = formato.format(fechaEnviar);

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


    private void txtNumeroDeFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaFocusGained
        jBtnBorrarFIla.setEnabled(false);
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
            // funcionAgregarFactura();
            new Thread(new HiloFmanifestarPedidosEnRuta(ini, this, "funcionAgregarFactura")).start();
        }

    }//GEN-LAST:event_txtNumeroDeFacturaKeyPressed

    /**
     * Método que asigna las factura leida por teclado ó por código de barras al
     * manifiesto actual
     *
     */

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

    private void txtNumeroDeManifiestoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeManifiestoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeManifiestoFocusGained

    private void txtNumeroDeManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeManifiestoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeManifiestoKeyPressed

    private void btnCrearManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearManifiestoActionPerformed
        //  crearManifiesto();
        new Thread(new HiloFmanifestarPedidosEnRuta(ini, this, "crearManifiesto")).start();
    }//GEN-LAST:event_btnCrearManifiestoActionPerformed


    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (manifiestoActual != null) {

            manifiestoActual.liberarManifiesto(true);

        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void txtNumeroDeFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed

        imprimir();

    }//GEN-LAST:event_btnImprimirActionPerformed


    private void btnFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileActionPerformed

        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Elija Archivo a importar");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Escoja el archivo en formato texto:  .txt", "txt");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(jPanel1);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                archivoConListaDeFacturas = fc.getSelectedFile();
                txtFile.setText(archivoConListaDeFacturas.getAbsolutePath());
                btnAgregarFacturas.setEnabled(true);
                btnFile.setEnabled(false);
                formaDePago = 1;

                /*  funcion que llena la lista de las facturas */
                agregarElemetos();
                btnFile.setEnabled(false);

                btnAgregarFacturas.setEnabled(true);
            } catch (Exception ex) {
                Logger.getLogger(FImportarArchivoExcel.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en fuente de archivos " + ex);
            }
        } else {
            // log.append("acción cancelada por el usuario." + newline);
        }
    }//GEN-LAST:event_btnFileActionPerformed

    private void btnAgregarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarFacturasActionPerformed
        //agregarFacturasDesdeJList();
        new Thread(new HiloFmanifestarPedidosEnRuta(ini, this, "agregarFacturasDesdeJList")).start();


    }//GEN-LAST:event_btnAgregarFacturasActionPerformed


    private void jTabbedPane1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1FocusGained

    private void pnlAgregarListaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pnlAgregarListaFocusGained
        if (manifiestoActual.getListaFacturasPorManifiesto().isEmpty()) {
            btnFile.setEnabled(false);
            btnAgregarFacturas.setEnabled(false);
        } else {
            btnFile.setEnabled(true);
            btnAgregarFacturas.setEnabled(true);
        }
    }//GEN-LAST:event_pnlAgregarListaFocusGained

    private void borrar1FilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrar1FilaMouseClicked


    }//GEN-LAST:event_borrar1FilaMouseClicked

    private void borrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarTodoActionPerformed
        int deseaEliminartodo = 20;

        /* SE VERIFICA QUE EL AMNIFIESTO NO ESTE CERRADO */
        if (manifiestoActual.getEstadoManifiesto() == 2) {

            deseaEliminartodo = JOptionPane.showConfirmDialog(this, "Desea eliminar todos los registros  de la Tabla ?", "Eliminar Fila", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (deseaEliminartodo == JOptionPane.YES_OPTION) {
                try {

                    borrarTodasLasfacturas();

                } catch (Exception ex) {
                    Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }


    }//GEN-LAST:event_borrarTodoActionPerformed

    private void borrar1FilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrar1FilaActionPerformed
        borrarFila();
    }//GEN-LAST:event_borrar1FilaActionPerformed

    private void borrarFila() throws HeadlessException {
        int deseaEliminarLaFila;

        /* SE VERIFICA QUE EL AMNIFIESTO NO ESTE CERRADO */
        if (manifiestoActual.getEstadoManifiesto() == 2) {

            /* Se valida que  la Jtable tenga filas con da3os*/
            if (jTableFacturasPorManifiesto.getRowCount() > 0) {

                /* se identifica la fila seleccionada */
                int fila = jTableFacturasPorManifiesto.getSelectedRow();

                deseaEliminarLaFila = JOptionPane.showConfirmDialog(this, "Desea eliminar el Registro de la Tabla ?", "Eliminar Fila", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                /*  SI DESEA BORRAR LA FILA */
                if (deseaEliminarLaFila == JOptionPane.YES_OPTION) {
                    try {

                        String numeroDeFactura = modelo2.getValueAt(fila, 1).toString();

                        /* Elimina reegistro del archivo */
                        eliminarRegistroDelArchivo(numeroDeFactura);

                        /* LLenar el Jtable de las facturas por manifiesto */
                        llenarjTableFacturasPorManifiesto();

                        /* ordena la tabla con la nueva adherencia */
                        ordenarTabla();

                    } catch (Exception ex) {
                        Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            } else {
                JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se puede eliminar fila, maniesto en distribución ", "No se puede Borrar fila ", 1, null);
        }
    }

    private void cbxRutaDeDistribucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxRutaDeDistribucionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxRutaDeDistribucionActionPerformed

    private void txtTelefonoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoClienteActionPerformed

    private void txtNombreDeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDeClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeClienteActionPerformed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();

    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnImprimirActionPerformed
        imprimir();
    }//GEN-LAST:event_jBtnImprimirActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        cancelar();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnMinutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnMinutaActionPerformed

        // sacarMinuta();
        new Thread(new HiloFmanifestarPedidosEnRuta(ini, this, "sacarMinuta")).start();


    }//GEN-LAST:event_jBtnMinutaActionPerformed


    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        salir();        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnExitActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
        grabar();
    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jBtnNuevo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnNuevo1ActionPerformed

    private void jBtnBorrarFIlaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBorrarFIlaActionPerformed
        borrarFila();        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnBorrarFIlaActionPerformed

    private void jBtnObservacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnObservacionesActionPerformed
        FAgregarObservacionesManifiesto fAgregarObservacionesManifiesto = new FAgregarObservacionesManifiesto(this);
        this.getParent().add(fAgregarObservacionesManifiesto);
        fAgregarObservacionesManifiesto.toFront();
        fAgregarObservacionesManifiesto.setClosable(true);
        fAgregarObservacionesManifiesto.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        fAgregarObservacionesManifiesto.setTitle("Formulario para ingresar las observacciones del Manifiesto");
        fAgregarObservacionesManifiesto.setLocation((screenSize.width - fAgregarObservacionesManifiesto.getSize().width) / 2, (screenSize.height - fAgregarObservacionesManifiesto.getSize().height) / 2);
        fAgregarObservacionesManifiesto.show();
    }//GEN-LAST:event_jBtnObservacionesActionPerformed

    private void txtNombreDedespachadorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDedespachadorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(ini, this, 5);
                this.getParent().add(form);
                form.toFront();
                form.setClosable(true);
                form.setVisible(true);
                form.setTitle("Formulario para buscar Empleados por apellidos");
                form.txtApellidosPersona.requestFocus();
                btnNuevo.setEnabled(false);
                jBtnNuevo.setEnabled(false);
                form.show();
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                if (auxiliar1 == null) {
                    txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR");
                }

            }
        }
    }//GEN-LAST:event_txtNombreDedespachadorKeyPressed

    private void txtNombreDedespachadorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDedespachadorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDedespachadorFocusGained

    private void txtNombreDeAuxiliar3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(ini, this, 4);
                this.getParent().add(form);
                form.toFront();
                form.setClosable(true);
                form.setVisible(true);
                form.setTitle("Formulario para buscar Empleados por apellidos");
                form.txtApellidosPersona.requestFocus();
                btnNuevo.setEnabled(false);
                jBtnNuevo.setEnabled(false);
                form.show();
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                if (auxiliar3 == null) {
                    txtNombreDeAuxiliar3.setText("SALE A DISTRIBUCION SIN AUXILIAR");
                }
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar3KeyPressed

    private void txtNombreDeAuxiliar3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar3FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar3FocusGained

    private void txtNombreDeAuxiliar2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(ini, this, 3);
                this.getParent().add(form);
                form.toFront();
                form.setClosable(true);
                form.setVisible(true);
                form.setTitle("Formulario para buscar Empleados por apellidos");
                form.txtApellidosPersona.requestFocus();
                btnNuevo.setEnabled(false);
                jBtnNuevo.setEnabled(false);
                form.show();
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                if (auxiliar2 == null) {
                    txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR");
                }
            }
        }
    }//GEN-LAST:event_txtNombreDeAuxiliar2KeyPressed

    private void txtNombreDeAuxiliar2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar2FocusGained

    private void txtNombreDeAuxiliar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            //if (manifiestoActual.getEstadoManifiesto() < 2) {
            FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(ini, this, 2);
            this.getParent().add(form);
            form.toFront();
            form.setClosable(true);
            form.setVisible(true);
            form.setTitle("Formulario para buscar Empleados por apellidos");
            form.txtApellidosPersona.requestFocus();
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            form.show();
            // }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                if (auxiliar1 == null) {
                    txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR");
                }
            }
        }
    }//GEN-LAST:event_txtNombreDeAuxiliar1KeyPressed

    private void txtNombreDeAuxiliar1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar1FocusGained

    private void txtnombreDeConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreDeConductorKeyPressed
        /* Evento en el caso que se oprima la tecla F2 */
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            //if (manifiestoActual.getEstadoManifiesto() < 2) {
            FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(ini, this, 1);
            this.getParent().add(form);
            form.toFront();
            form.setClosable(true);
            form.setVisible(true);
            form.setTitle("Formulario para buscar Empleados por apellidos");
            form.txtApellidosPersona.requestFocus();
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            form.show();
            //}

        }

        /* Evento en el caso que se oprima la tecla enter */
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                if (conductor != null) {
                    if (!conductor.getCedula().equals(carro.getConductor())) {
                        JOptionPane.showMessageDialog(null, "El conductor asignado es diferente al que acaba de ingresar", "Conductor diferente", JOptionPane.WARNING_MESSAGE, null);

                    }
                    txtNombreDeAuxiliar1.setEnabled(true);
                }
            }

        }
    }//GEN-LAST:event_txtnombreDeConductorKeyPressed

    private void txtnombreDeConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnombreDeConductorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreDeConductorFocusGained

    private void txtPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaKeyPressed

        /*Evento al oprimir la tecla F2*/
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            if (manifiestoActual.getEstadoManifiesto() <= 2) {

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
            // consultarManifiesto();
            new Thread(new HiloFmanifestarPedidosEnRuta(ini, this, "consultarManifiesto")).start();
        }
    }//GEN-LAST:event_txtPlacaKeyPressed

    public void consultarManifiesto() {
        if (manifiestoActual.getEstadoManifiesto() <= 2) {
            System.out.println("Cargando datos despues del enter -> \n\n");

            lblCirculoDeProgreso2.setVisible(false);
            btnImprimir.setEnabled(false);
            jBtnImprimir.setEnabled(false);

            try {
                carro = null;

                btnNuevo.setEnabled(false);
                jBtnNuevo.setEnabled(false);

                /* SE BUSCA EN VEHICULO EN EL ARRAY DEL SISTEMA */
                for (CCarros carro : ini.getListaDeVehiculos()) {
                    if (carro.getPlaca().equals(txtPlaca.getText().trim())) {
                        this.carro = carro;
                        break;
                    }
                }

                System.out.println("busca el carro con el manifiesto -> \n\n");

                /* Se valid que el vehículo exista en el sistema*/
                if (this.carro == null) {
                    txtPlaca.requestFocus();
                    JOptionPane.showInternalMessageDialog(this, "ESE VEHICULO NO EXISTE EN LA BASE DE DATOS ", "Error", 0);
                    lblCirculoDeProgreso1.setVisible(false);
                } else {
                    lblCirculoDeProgreso1.setVisible(true);
                    DecimalFormat df = new DecimalFormat("#,###.0");
                    txtPesoMAximoAutorizado.setText("Peso Maximo Autorizado : " + df.format(carro.getPesoTotalAutorizado()) + " Kgs.");
                    txtKmDeSalida.setText(carro.getKilometrajeActual() + "");
                    txtnombreDeConductor.setText(carro.getNombreConductor() + " " + carro.getApellidosConductor());
                    lblCirculoDeProgreso1.setVisible(false);

                    /* INICIAMOS EL NUEVO MANIFIESTO, EN EL CUAL SE CONSULTA LOS MANIFIESTOS
                    QUE TENGA LA PLACA INGRESADA EN EL CAMPO DE TEXTO, DE LOS
                    CUALES SE TOMA EL ÚLTIMO */
                    // manifiestoActual = new CManifiestosDeDistribucion(ini, txtPlaca.getText().trim());
                    System.out.println("crea el  el manifiesto -> \n\n");

                    /* Si el carro no tiene manifiestos, es decir no ha trabajado en la operación, entonces se crea el
                    manifiesto de distribución en blanco y se le asigna un estado =0
                    si ya tiene manifiestos creados , la consulta trae el último que sacó a distribución  y verifica su estado */
 /* Se crea un  procesoconcurrente para consultar el manifiesto y
                    verificar su estado */
                    new Thread(new HiloConsultarManifiesto(ini, this)).start();

                }

            } catch (Exception ex) {
                JOptionPane.showInternalMessageDialog(this, "Error en la consulta del vehiculo", "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
                cancelar();
            }

        }
    }

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void txtPlacaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusGained
        // txtPlaca.setSelectionStart(0);
        // txtPlaca.setSelectionEnd(txtPlaca.getText().length());
    }//GEN-LAST:event_txtPlacaFocusGained

    private void jTableFacturasPorManifiestoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFacturasPorManifiestoMouseClicked
        if (manifiestoActual.getEstadoManifiesto() == 2) {
            jBtnBorrarFIla.setEnabled(true);
        }
    }//GEN-LAST:event_jTableFacturasPorManifiestoMouseClicked

    private void jBtnDocVencidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnDocVencidosActionPerformed

        String mensaje = "";
        if (this.carro.getListaDeDocumentosPorVehiculoVencidos() != null) {
            mensaje = "Tiene novedad en los siguientes Documentos:\n";
            for (DocumentosPorVehiculo dxv : this.carro.getListaDeDocumentosPorVehiculoVencidos()) {
                mensaje += dxv.getFormatoSiglas() + "--> " + dxv.getFechaVencimiento() + ".\n";
            }
            JOptionPane.showMessageDialog(this, mensaje, "Documentos pendienetes", JOptionPane.ERROR_MESSAGE);

        } else {
             mensaje = "No presenta Novedades en  Documentos vencidos\n";
            JOptionPane.showMessageDialog(this, mensaje, "Documentos pendienetes", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_jBtnDocVencidosActionPerformed

    private void jBtnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRefrescarActionPerformed
        new Thread(new HiloFmanifestarPedidosEnRuta(ini, this, "refrescar")).start();

    }//GEN-LAST:event_jBtnRefrescarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu borraElementos;
    private javax.swing.JMenuItem borrar1Fila;
    private javax.swing.JMenuItem borrarTodo;
    public javax.swing.JProgressBar brrProgreso;
    public javax.swing.JButton btnAgregarFacturas;
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnCrearManifiesto;
    public javax.swing.JButton btnFile;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnImprimir;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JComboBox cbxCanales;
    public javax.swing.JComboBox<String> cbxPrefijos;
    public javax.swing.JComboBox cbxRutaDeDistribucion;
    public com.toedter.calendar.JDateChooser dateManifiesto;
    public javax.swing.JToggleButton jBtnBorrarFIla;
    private javax.swing.JToggleButton jBtnCancel;
    public javax.swing.JToggleButton jBtnDocVencidos;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnImprimir;
    public javax.swing.JToggleButton jBtnMinuta;
    public javax.swing.JButton jBtnNuevo;
    public javax.swing.JButton jBtnNuevo1;
    public javax.swing.JToggleButton jBtnObservaciones;
    public javax.swing.JToggleButton jBtnRefrescar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public javax.swing.JList jListListaDeFacturasErradas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableFacturasPorManifiesto;
    public javax.swing.JTable jTableProductosPorFactura;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblBarraDeProgreso;
    public javax.swing.JLabel lblCantidadFacturas;
    public javax.swing.JLabel lblCirculoDeProgreso1;
    public javax.swing.JLabel lblCirculoDeProgreso2;
    private javax.swing.JLabel lblKilometros;
    public javax.swing.JLabel lblNumeroManifiesto;
    public javax.swing.JLabel lblPesoDeLaFactura;
    public org.edisoncor.gui.label.LabelCustom lblPesoManifiesto;
    public org.edisoncor.gui.label.LabelCustom lblValorFactura;
    public org.edisoncor.gui.label.LabelCustom lblValorRecaudoManifiesto;
    public javax.swing.JList listaDeFacturas1;
    private javax.swing.JPopupMenu mnuBorrarFilas_;
    public javax.swing.JPanel pnlAgregarFactura;
    public javax.swing.JPanel pnlAgregarLista;
    public javax.swing.JTextField txtBarroCliente;
    public javax.swing.JTextField txtDireccionCliente;
    public javax.swing.JTextField txtFile;
    public javax.swing.JTextField txtKmDeSalida;
    public javax.swing.JTextField txtNombreDeAuxiliar1;
    public javax.swing.JTextField txtNombreDeAuxiliar2;
    public javax.swing.JTextField txtNombreDeAuxiliar3;
    public javax.swing.JTextField txtNombreDeCliente;
    public javax.swing.JTextField txtNombreDedespachador;
    public javax.swing.JTextField txtNombreVendedor;
    public javax.swing.JTextField txtNumeroDeFactura;
    public javax.swing.JTextField txtNumeroDeManifiesto;
    public javax.swing.JTextField txtPesoMAximoAutorizado;
    public javax.swing.JTextField txtPlaca;
    public javax.swing.JTextField txtTelefonoCliente;
    public javax.swing.JTextField txtnombreDeConductor;
    // End of variables declaration//GEN-END:variables

    public void nuevo() {
        try {
            cancelar();
            nuevo = true;
            //limpiar();
            dateManifiesto.setEnabled(true);
            dateManifiesto.setDate(new Date());
            btnFile.setEnabled(true);
            jPanel7.setEnabled(true);

            txtPlaca.setEnabled(true);
            txtPlaca.setEditable(true);
            cbxCanales.setEnabled(true);
            cbxRutaDeDistribucion.setEnabled(true);
            dateManifiesto.setEnabled(true);
            jTabbedPane1.setEnabled(false);
            btnCrearManifiesto.setEnabled(true);
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            txtPlaca.requestFocus();
            formaDePago = 1; // contado

            listaDeAuxiliares = new ArrayList();

            listaDeFacturasEnElArchivo = new ArrayList();
            manifiestoActual = new CManifiestosDeDistribucion(ini);
            manifiestoActual.setEstadoManifiesto(0);

            lblPesoManifiesto.setText("0.0 Kg");
            lblPesoDeLaFactura.setText("0.0 Kg");
            lblValorRecaudoManifiesto.setText("$0.0");
        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabar() throws HeadlessException {
        int deseaGrabarRegistro;

        // String a = manifiestoActual.getObservaciones();
        /*valida sin pendientes del conductor */
        if (manifiestoActual.getObservaciones().length() <= 2) {
            // if (manifiestoActual.getCantidadManifiestosPendientesConductor() > 0) {
            JOptionPane.showMessageDialog(this, "El manifiesto no tiene la cantidad de Canastas en ruta", "Manifiesto incompleto", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        if (manifiestoActual.getEstadoManifiesto() == 2) {  // IINICIO DEL IF-> ESTADO DEL MANIFIESTO

            deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

            if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {

                manifiestoActual.setCantidadPedidos(manifiestoActual.getListaFacturasPorManifiesto().size());

                /* Se valida la conexión a internet para grabar los datos en la BBDD */
                if (ini.verificarConexion()) {
                    //if (true) {
                    band = true;

                    new Thread(new HiloGuardarFacturasPorManifiesto(ini, this)).start();
                    new Thread(new JcProgressBar(brrProgreso, 100)).start();

                } else {

                    JOptionPane.showMessageDialog(null, "No hay conexión a internet", "Error al guardar Datos", JOptionPane.WARNING_MESSAGE, null);

                }

            }
        } else {
            btnGrabar.setEnabled(false);
            jBtnGrabar.setEnabled(false);
            jBtnObservaciones.setEnabled(false);

            btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
            jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
            JOptionPane.showMessageDialog(null, "Error al guardar los datos, manifiesto  ya está en distribución", "manifiesto ya fue guardado", JOptionPane.ERROR_MESSAGE, null);

        }  // FIN DEL IF-> ESTADO DEL MANIFIESTO
        return false;
    }

    public void imprimir() {
        /*valida sin pendientes del conductor */
        try {
            if (manifiestoActual.getEstadoManifiesto() == 2) {

                if (false) {
                    //if (manifiestoActual.getCantidadManifiestosPendientesConductor() > 0) {
                    JOptionPane.showMessageDialog(this, "El conductor tiene pendientes de ruta,\n no se puede Imprimir este manifiesto", "Conductor no esta paz y salvo", JOptionPane.ERROR_MESSAGE, null);

                    return;
                }

                /*Manifiesto no grabado en la BBDD, trae los registros localmente */
                //manifiestoActual.setListaFacturasPorManifiesto(listaFacturasPorManifiesto);
                File fil = new File(this.manifiestoActual.getRutArchivofacturasporManifiesto());
                manifiestoActual.setListaFacturasPorManifiesto(fil);

            } else {
                /*Manifiesto grabado en la BBDD , trae registros desde allí*/
                manifiestoActual.setListaFacturasPorManifiesto();
            }


            /* Genera el manifiesto  Genera el rutero*/
            RepporteRuteroConductoresConPesosyDEscuentos demo = new RepporteRuteroConductoresConPesosyDEscuentos(ini, manifiestoActual);
            //RepporteRuteroLaHielera demo = new RepporteRuteroLaHielera(ini, manifiestoActual);

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancelar() {
        limpiar();

        nuevo = false;
        dateManifiesto.setEnabled(false);

        txtnombreDeConductor.setEditable(false);

        txtNombreDeAuxiliar1.setEditable(false);
        txtNombreDeAuxiliar2.setEditable(false);
        txtNombreDeAuxiliar3.setEditable(false);
        txtNombreDedespachador.setEditable(false);

        txtKmDeSalida.setEditable(false);
        txtPlaca.setEditable(false);
        txtKmDeSalida.setEnabled(false);
        txtKmDeSalida.setEditable(false);
        cbxCanales.setEnabled(false);
        cbxRutaDeDistribucion.setEnabled(false);
        dateManifiesto.setEnabled(false);

        btnCrearManifiesto.setEnabled(false);
        jTabbedPane1.setEnabled(false);

        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);

        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);

        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);
        jBtnObservaciones.setEnabled(false);

        jBtnBorrarFIla.setEnabled(false);

        jBtnMinuta.setEnabled(false);

        jBtnDocVencidos.setEnabled(false);

        grabar = false;
        lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # ");

        if (manifiestoActual != null) {
            manifiestoActual.liberarManifiesto(true);
            liberado = true;

        }
        /* Vaciar las variables  */
        conductor = null;
        auxiliar1 = null;
        auxiliar2 = null;
        auxiliar3 = null;
        despachador = null;
        carro = null;
        facturaActual = null;
        band = false;
        valorDespBarraProgreso = 0;
        archivoConListaDeFacturas = null;;
        valorTotalManifiesto = 0.0;
        cargado = false;
        liberado = false;
        grabar = false;
        nuevo = false;
        kilometraje = 0;
        formaDePago = 0;
        mensaje = null;
        contadorDeFacturas = 0;
        sumadorPesosFacturas = 0.0;
        listaDeFacturasEnElArchivo = null;;
        listaDeAuxiliares = null;
        manifiestoActual = null;
        estaOcupadoGrabando = false;
        cadenaDeFacturas = null;

        cbxPrefijos.setEnabled(false);
        txtNumeroDeFactura.setEnabled(false);
        txtNumeroDeFactura.setEditable(false);

        txtNombreDeCliente.setEditable(false);
        txtBarroCliente.setEditable(false);
        txtDireccionCliente.setEditable(false);
        txtTelefonoCliente.setEditable(false);
        txtNombreVendedor.setEditable(false);
        txtNumeroDeManifiesto.setEditable(false);

        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();

        manifiestoActual = null;
        conductor = null;
        despachador = null;
        listaDeAuxiliares = null;

        // listaFacturasPorManifiesto = null;
        valorTotalManifiesto = 0.0;
        formaDePago = 0;
        lblCirculoDeProgreso1.setVisible(false);

        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);

        /*Componentes de JTabbePane */
        btnFile.setEnabled(false);
        btnAgregarFacturas.setEnabled(false);
        txtFile.setEnabled(false);
        listaDeFacturas1.setEnabled(false);
        jListListaDeFacturasErradas.setEnabled(false);

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png")));
        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png")));

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

        lblCirculoDeProgreso1.setVisible(false);
        lblCirculoDeProgreso2.setVisible(false);

    }

    public void salir() {
        // SE LIBERA EL MANIFIESTO ACTUAL
        if (manifiestoActual != null) {

            manifiestoActual.liberarManifiesto(true);
            liberado = true;

        }

        // SE CIERRA LA APLICACION
        this.dispose();
        this.setVisible(false);
    }

    public void llenarCamposDeTexto(CEmpleados empleado) {
        try {

            //txtApellidos.setText(empleado.getApellidos());
        } catch (Exception ex) {
        }
    }

    public void habilitar(boolean valor) {

        jTabbedPane1.setEnabled(valor);

        txtNombreDeCliente.setEditable(valor);
        txtTelefonoCliente.setEditable(valor);
        txtNombreVendedor.setEditable(valor);

        //dateVencimiento.setEnabled(valor);
        jTableFacturasPorManifiesto.setEnabled(valor);
        // tablaDocsVencidos.setEnabled(valor);
        jBtnDocVencidos.setEnabled(valor);

        btnFile.setEnabled(valor);

        btnAgregarFacturas.setEnabled(valor);
        txtPlaca.setEnabled(valor);

        txtnombreDeConductor.setEnabled(valor);

        txtNombreDeAuxiliar1.setEnabled(valor);
        txtNombreDeAuxiliar2.setEnabled(valor);
        txtNombreDeAuxiliar3.setEnabled(valor);
        txtNombreDedespachador.setEnabled(valor);

        txtKmDeSalida.setEnabled(valor);
        txtNumeroDeManifiesto.setEnabled(valor);

        jTableFacturasPorManifiesto.setEnabled(valor);

        txtNumeroDeFactura.setEnabled(valor);
        txtNumeroDeFactura.setEnabled(valor);
        txtNombreDeCliente.setEnabled(valor);
        txtBarroCliente.setEnabled(valor);
        txtDireccionCliente.setEnabled(valor);
        txtTelefonoCliente.setEnabled(valor);
        txtNombreVendedor.setEnabled(valor);
        lblCantidadFacturas.setEnabled(valor);

        pnlAgregarLista.setEnabled(valor);

        txtFile.setEnabled(valor);
        listaDeFacturas1.setEnabled(valor);

        jListListaDeFacturasErradas.setEnabled(valor);

        btnFile.setEnabled(valor);
        btnAgregarFacturas.setEnabled(valor);

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
            for (int i = 0; i < jListListaDeFacturasErradas.getModel().getSize() - 1; i++) {
                jListListaDeFacturasErradas.remove(i);
            }
            btnFile.setEnabled(false);
            btnAgregarFacturas.setEnabled(false);
            txtPlaca.setText("");

            txtnombreDeConductor.setText("");

            txtNombreDeAuxiliar1.setText("");
            txtNombreDeAuxiliar2.setText("");
            txtNombreDeAuxiliar3.setText("");
            txtNombreDedespachador.setText("");

            txtKmDeSalida.setText("");
            txtNumeroDeManifiesto.setText("");
            cbxCanales.setSelectedIndex(0);
            cbxRutaDeDistribucion.setSelectedIndex(0);

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

            //listaFacturasPorManifiesto = null;
            lblPesoManifiesto.setText("0.0 Kg");
            lblValorRecaudoManifiesto.setText("$0.0");
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

        pnlAgregarLista.setEnabled(true);

        txtFile.setText("");
        listaDeFacturas1.setEnabled(true);
        eliminarDatosDejList(listaDeFacturas1);
        jListListaDeFacturasErradas.setEnabled(true);
        eliminarDatosDejList(jListListaDeFacturasErradas);

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

        lblCirculoDeProgreso1.setVisible(false);
        lblCirculoDeProgreso2.setVisible(false);
        btnFile.setEnabled(true);
        btnAgregarFacturas.setEnabled(true);

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

    private void llenarTxtAuxiliares() {
        /*se llenan los campos de texto de los nombres de los auxiliares*/
        int indice = 1;

        txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
        txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
        txtNombreDeAuxiliar3.setText("SALE A DISTRIBUCION SIN AUXILIAR3");
        listaDeAuxiliares = manifiestoActual.getListaDeAuxiliares("" + manifiestoActual.getNumeroManifiesto());

        if (listaDeAuxiliares.size() > 0) {
            for (CEmpleados aux : listaDeAuxiliares) {
                switch (indice) {
                    case 1:
                        if (aux.getCedula().equals("0")) {
                            txtNombreDeAuxiliar1.setText("");
                        } else {
                            txtNombreDeAuxiliar1.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;
                        break;
                    case 2:
                        if (aux.getCedula().equals("0")) {
                            txtNombreDeAuxiliar2.setText("");
                        } else {
                            txtNombreDeAuxiliar2.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;
                        break;
                    case 3:
                        if (aux.getCedula().equals("0")) {
                            txtNombreDeAuxiliar3.setText("");
                        } else {
                            txtNombreDeAuxiliar3.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;

                        break;

                }
                /* fin switch */

            }
        }

    }

    private void llenarDatosManifiesto() throws Exception {

        sumadorPesosFacturas = 0.0;

        /*  Se llenan los campos del conducor */
        for (CEmpleados obj : ini.getListaDeEmpleados()) {
            if (manifiestoActual.getConductor().equals(obj.getCedula())) {

                txtnombreDeConductor.setText(obj.getNombres() + " " + obj.getApellidos());
            }
        }

        /*Se llenan los campos de texto de los auxiliares*/
        llenarTxtAuxiliares();

        /*  Se llenan los campos del despachador */
        if (manifiestoActual.getDespachador().equals("0")) {
            txtNombreDedespachador.setText("");

        } else {
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (manifiestoActual.getDespachador().equals(obj.getCedula())) {

                    txtNombreDedespachador.setText(obj.getNombres() + " " + obj.getApellidos());
                }
            }
        }

        /*  Se llenan los campos del canal de distribución  */
        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            if (obj.getIdCanalDeVenta() == manifiestoActual.getIdCanal()) {
                cbxCanales.setSelectedItem(obj.getNombreCanalDeVenta());
                canalDeVenta = obj;
            }
        }
        /*  Se llenan los campos ruta de distribución  */
        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            if (obj.getIdRutasDeDistribucion() == manifiestoActual.getIdRuta()) {
                cbxRutaDeDistribucion.setSelectedItem(obj.getNombreRutasDeDistribucion());
                ruta = obj;
            }
        }
        txtKmDeSalida.setText("" + carro.getKilometrajeActual());
        txtNumeroDeManifiesto.setText(manifiestoActual.codificarManifiesto());

        manifiestoActual.setIsFree(0);

        //System.out.println("libera manifiesto en formulario");
        manifiestoActual.liberarManifiesto(false);

        System.out.println("llama la lista de facturas por manifiesto");

//listaFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto();
        lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + txtNumeroDeManifiesto.getText().trim());
        DecimalFormat df = new DecimalFormat("#,###.0");
        lblPesoManifiesto.setText(df.format(manifiestoActual.getPesoKgManifiesto() / 1000) + " Kg");
        lblValorRecaudoManifiesto.setText(nf.format(manifiestoActual.getValorTotalManifiesto()));

        valorTotalManifiesto = 0.0;
        DefaultTableModel modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();

        //  SE LLENA EL JTABLE FACTURAS POR MANIFIESTO
        if (manifiestoActual.getListaFacturasPorManifiesto() != null) {

            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {

                int fila = jTableFacturasPorManifiesto.getRowCount();
                modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);

                jTableFacturasPorManifiesto.setValueAt("" + obj.getAdherencia(), fila, 0); // item 
                jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), fila, 1); // numero de la factura
                jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), fila, 2); // cliente

                jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), fila, 3); // valor de la factura   

                // se ubica en la fila insertada
                jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);

                sumadorPesosFacturas += obj.getPesoFactura();
                valorTotalManifiesto += obj.getValorTotalFactura();
                Thread.sleep(10);

            }

            df = new DecimalFormat("#,###.0");
            lblPesoManifiesto.setText(df.format(manifiestoActual.getPesoKgManifiesto() / 1000) + " Kg");
            lblValorRecaudoManifiesto.setText(nf.format(manifiestoActual.getValorTotalManifiesto()));
            lblCantidadFacturas.setText("" + manifiestoActual.getListaFacturasPorManifiesto().size());
            manifiestoActual.setValorTotalManifiesto(valorTotalManifiesto);

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

                    //listaFacturasPorManifiesto = new ArrayList<>(); // CfacturasPorManifiesto

                    /*Se llama al fichero que contiene los numero de las facturas
                     a  que están registradas en el manifiesto*/
                    File fichero = new File(this.manifiestoActual.getRutArchivofacturasporManifiesto());

                    /*Se valida que exista el fichero */
                    if (fichero.exists()) {

                        manifiestoActual.setListaFacturasPorManifiesto(fichero);

                        //listaFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto();
                        if (manifiestoActual.getListaFacturasPorManifiesto().isEmpty() || manifiestoActual.getListaFacturasPorManifiesto() == null) {
                            btnGrabar.setEnabled(false);
                            jBtnGrabar.setEnabled(false);
                            jBtnObservaciones.setEnabled(false);
                        } else {
                            btnGrabar.setEnabled(true);
                            jBtnGrabar.setEnabled(true);
                            jBtnObservaciones.setEnabled(true);
                            btnImprimir.setEnabled(true);
                            jBtnImprimir.setEnabled(true);
                        }

                        llenarDatosManifiesto();

                        txtnombreDeConductor.setEditable(false);
                        txtNombreDeAuxiliar1.setEditable(false);
                        txtNombreDeAuxiliar2.setEditable(false);
                        txtNombreDeAuxiliar3.setEditable(false);

                        txtPlaca.setEnabled(true);
                        txtPlaca.setEditable(false);
                        cbxCanales.setEnabled(false);
                        cbxRutaDeDistribucion.setEnabled(false);
                        dateManifiesto.setEnabled(false);
                        jTabbedPane1.setEnabled(true);
                        jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarLista), true);
                        btnCrearManifiesto.setEnabled(false);

                        btnGrabar.setEnabled(true);
                        jBtnGrabar.setEnabled(true);
                        jBtnObservaciones.setEnabled(true);

                        txtBarroCliente.setEditable(false);
                        txtDireccionCliente.setEditable(false);
                        txtNombreDeCliente.setEditable(false);
                        txtTelefonoCliente.setEditable(false);
                        txtNumeroDeManifiesto.setEditable(false);
                        txtNombreVendedor.setEditable(false);

                        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
                        String strFecha = manifiestoActual.getFechaDistribucion();
                        Date fecha = null;
                        fecha = formatoDelTexto.parse(strFecha);
                        //dateManifiesto.setDate(manifiestoActual.getFechaDistribucion());

                        dateManifiesto.setDate(fecha);

                        txtKmDeSalida.setEditable(false);
                        cbxPrefijos.setEnabled(true);
                        txtNumeroDeFactura.setEnabled(true);
                        txtNumeroDeFactura.setEditable(true);
                        txtNumeroDeFactura.requestFocus();
                        txtNumeroDeFactura.requestFocus();

                    } else {

                        //listaFacturasPorManifiesto = new ArrayList<>();
                        llenarDatosManifiesto();

                        //txtPlaca.setEditable(false);
                        txtKmDeSalida.setEditable(false);

                        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
                        String strFecha = manifiestoActual.getFechaDistribucion();
                        Date fecha = null;
                        fecha = formatoDelTexto.parse(strFecha);

                        //dateManifiesto.setDate(manifiestoActual.getFechaDistribucion());
                        dateManifiesto.setDate(fecha);

                        txtnombreDeConductor.setEditable(false);
                        txtNombreDeAuxiliar1.setEditable(false);
                        txtNombreDeAuxiliar2.setEditable(false);
                        txtNombreDeAuxiliar3.setEditable(false);

                        jTabbedPane1.setEnabled(true);
                        jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarLista), true);
                        txtFile.setEditable(false);
                        btnFile.setEnabled(true);
                        cbxCanales.setEnabled(false);
                        cbxRutaDeDistribucion.setEnabled(false);
                        dateManifiesto.setEnabled(false);
                        cbxPrefijos.setEnabled(true);
                        txtNumeroDeFactura.setEnabled(true);
                        txtNumeroDeFactura.setEditable(true);
                        txtNumeroDeFactura.requestFocus();
                        txtNumeroDeFactura.requestFocus();
                        btnCrearManifiesto.setEnabled(false);

                        txtNumeroDeFactura.requestFocus();
                        txtNumeroDeFactura.requestFocus();

                    }

                } catch (Exception ex) {
                    Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                limpiar();
                liberado = false;
                jTabbedPane1.setEnabled(false);
                manifiestoActual = null;
                btnCrearManifiesto.setEnabled(false);
                cbxCanales.setEnabled(false);
                cbxRutaDeDistribucion.setEnabled(false);
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
    private ArrayList<String> listaDeFacturasNoRepetidas() {
        ArrayList<String> cadena = new ArrayList<>();
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
        if (manifiestoActual.getListaFacturasPorManifiesto() != null) {
            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
                if (facturaActual.getNumeroDeFactura().equals(obj.getNumeroFactura())) {
                    existe = true;
                    break;
                }
            }
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
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
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
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
            if (obj.getNumeroFactura().equals(numeroDeFactura)) {
                manifiestoActual.getListaFacturasPorManifiesto().remove(obj);

                //manifiestoActual.setListaFacturasPorManifiesto(listaFacturasPorManifiesto);
                break;
            }
            valorTotalManifiesto += obj.getValorARecaudarFactura();

        }

        /* Elimina reegistro del Array que contiene las CfcturasCamdun del Manifiesto */
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
            if (obj.getNumeroFactura().equals(numeroDeFactura)) {
                CFacturas fac = new CFacturas(ini, obj.getNumeroFactura());
                /*  libera la factura para ser usada por otro usuario */
                fac.setIsFree(1);
                fac.liberarFactura(true);
                sumadorPesosFacturas -= obj.getPesoFactura();

                manifiestoActual.getListaFacturasPorManifiesto().remove(obj);
//                              manifiestoActual.setListaCFacturasCamdun(listaDeCFacturasCamdunEnElManifiesto);
                break;
            }
        }

        DecimalFormat df = new DecimalFormat("#,###.0");
        lblPesoManifiesto.setText(df.format(manifiestoActual.getPesoKgManifiesto() / 1000) + " Kg");
        lblValorRecaudoManifiesto.setText(nf.format(manifiestoActual.getValorTotalManifiesto()));
        lblCantidadFacturas.setText("" + manifiestoActual.getListaFacturasPorManifiesto().size());

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

            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
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
        /*Se elimina el archivo temporal */
        File fichero = new File(manifiestoActual.getRutArchivofacturasporManifiesto());
        fichero.delete();

        // se borran datos del JTable
        DefaultTableModel model;
        model = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
        if (model.getRowCount() > 0) {
            int a = model.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                model.removeRow(i);
            }
        }

        // Habilitar las facturas manifestadas
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
            CFacturas fac = new CFacturas(ini, obj.getNumeroFactura(), false);
            fac.liberarFactura(true);

        }

        manifiestoActual.setListaFacturasPorManifiesto(new File(manifiestoActual.getRutArchivofacturasporManifiesto()));
        // Eliminamos los arrays de facturas
        //listaFacturasPorManifiesto = null;
        lblPesoManifiesto.setText("0.0 Kg");
        lblValorRecaudoManifiesto.setText("$0.0");
        lblValorFactura.setText("$ 0.0");
        lblCantidadFacturas.setText("0");
        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);
        jBtnObservaciones.setEnabled(false);

        limpiarDatodsDePanelProductosPorFactura();
        eliminarDatosDejList(listaDeFacturas1);
        eliminarDatosDejList(jListListaDeFacturasErradas);
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
            Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * METODO QUE PERMITE AGRGAR FACTURAS DESDE UN ARCHIVO PLANO
     *
     */
    private void agregarElemetos() {
        listaDeFacturasEnElArchivo = new ArrayList<>();
        FileReader fr = null;
        BufferedReader br = null;
        modelo = new DefaultListModel();
        btnAgregarFacturas.setEnabled(false);
        try {
            fr = new FileReader(archivoConListaDeFacturas);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            try {
                while ((linea = br.readLine()) != null) {   //codigo tomado de http://chuwiki.chuidiang.org/index.php?title=Lectura_y_Escritura_de_Ficheros_en_Java para leer ficheros de texto
                    modelo.addElement(linea);
                    listaDeFacturasEnElArchivo.add(linea);
                }

                listaDeFacturas1.setModel(modelo);

            } catch (IOException ex) {
                Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                Logger.getLogger(FManifestarPedidosEnRuta.class.getName()).log(Level.SEVERE, null, e2);

            }
        }
    }

    public boolean grabar2() throws HeadlessException {
        int deseaGrabarRegistro;

        // String a = manifiestoActual.getObservaciones();
        /*valida sin pendientes del conductor */
        if (manifiestoActual.getObservaciones().length() <= 2) {
            // if (manifiestoActual.getCantidadManifiestosPendientesConductor() > 0) {
            JOptionPane.showMessageDialog(this, "El manifiesto no tiene la cantidad de Canastas en ruta", "Manifiesto incompleto", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        if (manifiestoActual.getEstadoManifiesto() == 2) {  // IINICIO DEL IF-> ESTADO DEL MANIFIESTO

            deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

            if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {

                manifiestoActual.setCantidadPedidos(manifiestoActual.getListaFacturasPorManifiesto().size());

                /* Se valida la conexión a internet para grabar los datos en la BBDD */
                if (ini.verificarConexion()) {
                    //if (true) {
                    band = true;

                    new Thread(new HiloFmanifestarPedidosEnRuta(ini, this, "grabarFacturasPorManifiesto")).start();
                    new Thread(new JcProgressBar(brrProgreso, 100)).start();

                } else {

                    JOptionPane.showMessageDialog(null, "No hay conexión a internet", "Error al guardar Datos", JOptionPane.WARNING_MESSAGE, null);

                }

            }
        } else {
            btnGrabar.setEnabled(false);
            jBtnGrabar.setEnabled(false);
            jBtnObservaciones.setEnabled(false);

            btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
            jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
            JOptionPane.showMessageDialog(null, "Error al guardar los datos, manifiesto  ya está en distribución", "manifiesto ya fue guardado", JOptionPane.ERROR_MESSAGE, null);

        }  // FIN DEL IF-> ESTADO DEL MANIFIESTO
        return false;
    }

}
