/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera;

import aplicacionlogistica.distribucion.consultas.FBuscarListadoDeEmpleados;
import aplicacionlogistica.distribucion.consultas.FConsultarVehiculos;
import aplicacionlogistica.distribucion.Threads.HiloCrearManifiesto;
import aplicacionlogistica.distribucion.Threads.JcProgressBar;
import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.minutas.MinutasDeDistribucion;
import aplicacionlogistica.distribucion.imprimir.RepporteRuteroConductoresConPesosyDEscuentos;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import mtto.vehiculos.CCarros;
import mtto.vehiculos.CVehiculos;
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
public class FManifestarPedidosHielera extends javax.swing.JInternalFrame {

    public CEmpleados conductor = null;
    public CEmpleados auxiliar1 = null;
    public CEmpleados auxiliar2 = null;
    public CEmpleados auxiliar3 = null;
    public CEmpleados despachador = null;
    public CCarros carro = null;
    CFacturas facturaActual = null;
    public static boolean band = false;
    public static int valorDespBarraProgreso = 0;
    public File archivoConListaDeFacturas;
    public Double valorTotalManifiesto = 0.0;
    boolean cargado = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean nuevo = false;
    int kilometraje = 0;
    public String formaDePago;
    String mensaje = null;
    int contadorDeFacturas = 0;
    public double sumadorPesosFacturas = 0.0;
    public List<String> listaDeFacturasEnElArchivo;
    public List<CEmpleados> listaDeAuxiliares = new ArrayList<>();
    public CManifiestosDeDistribucion manifiestoActual = null;
    public boolean estaOcupadoGrabando = false;
    String cadenaDeFacturas;
    public int cantDeSalidas = 1;
    public int adherenciaGenral = 1;
    public List<CManifiestosDeDistribucion> listaDemanifiestos;

    //CUsuarios user;
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    public CRutasDeDistribucion ruta;
    public CCanalesDeVenta canalDeVenta;
    Inicio ini = null;
    DefaultListModel modelo, modelo3;
    TextAutoCompleter autoTxtVehiculos;
    TextAutoCompleter autoTxtConductores;
    TextAutoCompleter autoTxtAuxiliar1;
    TextAutoCompleter autoTxtAuxiliar2;
    TextAutoCompleter autoTxtAuxiliar3;
    TextAutoCompleter autoTxtDespachador;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public DefaultTableModel modelo1, modelo2;
    int filaTabla2;

    public Inicio getIni() {
        return ini;
    }

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
     * Creates new fReportemovilizadoPorConductor IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public FManifestarPedidosHielera(Inicio ini) throws Exception {
        this.ini = ini;

        try {
            initComponents();

            /*Atualiza el listado de vehiculos de la BBDD*/
            //new Thread(new HiloListadoDePlacas(ini)).start();
            this.ini = ini;
            // lblBarraDeProgreso.setVisible(false);

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
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
        }

        // SE LLENAN LAS LISTAS DESPLEGABLES
        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            if (obj.getActivoCanal() == 1) {
                cbxCanales.addItem(obj.getNombreCanalDeVenta());
            }

        }
        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            if (obj.getActivoRutasDeDistribucion() == 1) {
                cbxRutaDeDistribucion.addItem(obj.getNombreRutasDeDistribucion());
            }
        }

        this.autoTxtVehiculos = new TextAutoCompleter(txtPlaca);

        for (CVehiculos car : this.ini.getListaDeVehiculos()) {
            if (car.getActivoVehiculo() == 1) {
                autoTxtVehiculos.addItem(car.getPlaca() + " ");
            }
        }

        autoTxtConductores = new TextAutoCompleter(txtnombreDeConductor);

        for (CEmpleados conductor : this.ini.getListaDeEmpleados()) {
            if (conductor.getEmpleadoActivo() == 1) {
                autoTxtConductores.addItem(conductor.getNombres() + " " + conductor.getApellidos());
            }
        }

        autoTxtAuxiliar1 = new TextAutoCompleter(txtNombreDeAuxiliar1);

        for (CEmpleados auxiliar1 : this.ini.getListaDeEmpleados()) {
            if (auxiliar1.getEmpleadoActivo() == 1) {
                autoTxtAuxiliar1.addItem(auxiliar1.getNombres() + " " + auxiliar1.getApellidos());
            }
        }

        autoTxtAuxiliar2 = new TextAutoCompleter(txtNombreDeAuxiliar2);

        for (CEmpleados auxiliar2 : this.ini.getListaDeEmpleados()) {
            if (auxiliar2.getEmpleadoActivo() == 1) {
                autoTxtAuxiliar2.addItem(auxiliar2.getNombres() + " " + auxiliar2.getApellidos());
            }
        }

        autoTxtAuxiliar3 = new TextAutoCompleter(txtNombreDeAuxiliar3);

        for (CEmpleados auxiliar3 : this.ini.getListaDeEmpleados()) {
            if (auxiliar3.getEmpleadoActivo() == 1) {
                autoTxtAuxiliar3.addItem(auxiliar3.getNombres() + " " + auxiliar3.getApellidos());
            }
        }

        autoTxtDespachador = new TextAutoCompleter(txtNombreDedespachador);

        for (CEmpleados despachador : this.ini.getListaDeEmpleados()) {
            if (despachador.getEmpleadoActivo() == 1) {
                autoTxtDespachador.addItem(despachador.getNombres() + " " + despachador.getApellidos());
            }
        }

        String strMain = this.ini.getPrefijos();
        String[] arrSplit = strMain.split(",");
        for (int i = 0; i < arrSplit.length; i++) {
            cbxPrefijos.addItem(arrSplit[i].replace("'", ""));
        }

        lblCirculoDeProgreso1.setVisible(false);
        lblCirculoDeProgreso2.setVisible(false);

        cargado = true;
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

        mnuBorrarFilas = new javax.swing.JPopupMenu();
        borraElementos = new javax.swing.JMenu();
        borrar1Fila = new javax.swing.JMenuItem();
        borrarTodo = new javax.swing.JMenuItem();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaDocsVencidos = new javax.swing.JTable();
        buttonGroup1 = new javax.swing.ButtonGroup();
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
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFacturasPorManifiesto = new javax.swing.JTable();
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
        lblNumeroManifiesto = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnNuevo1 = new javax.swing.JButton();
        jBtnBorrarFIla = new javax.swing.JToggleButton();
        jBtnObservaciones = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();
        txtPesoMAximoAutorizado = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

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

        mnuBorrarFilas.add(borraElementos);

        tablaDocsVencidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "NOMBRES DOCUMENTO", "FECHA EXP.", "FECHA VENC.", "ARCHIVO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
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
        tablaDocsVencidos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tablaDocsVencidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDocsVencidosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablaDocsVencidos);
        if (tablaDocsVencidos.getColumnModel().getColumnCount() > 0) {
            tablaDocsVencidos.getColumnModel().getColumn(0).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(1).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(1).setPreferredWidth(250);
            tablaDocsVencidos.getColumnModel().getColumn(2).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaDocsVencidos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaDocsVencidos.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

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

        pnlAgregarFactura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlAgregarFactura.setAutoscrolls(true);
        pnlAgregarFactura.setDoubleBuffered(false);

        lblValorFactura.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValorFactura.setText("$ 0.0");
        lblValorFactura.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lblValorFactura.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Numero de Factura");

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
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

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Cliente");

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Vendedor");

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Dirección");

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
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
        jTableProductosPorFactura.setComponentPopupMenu(mnuBorrarFilas);
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
            .addComponent(lblCantidadFacturas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCantidadFacturas, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Peso de las Facturas"));

        lblPesoDeLaFactura.setFont(new java.awt.Font("Cantarell", 0, 36)); // NOI18N
        lblPesoDeLaFactura.setText("0.0 k.");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPesoDeLaFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblPesoDeLaFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Prefijo");

        javax.swing.GroupLayout pnlAgregarFacturaLayout = new javax.swing.GroupLayout(pnlAgregarFactura);
        pnlAgregarFactura.setLayout(pnlAgregarFacturaLayout);
        pnlAgregarFacturaLayout.setHorizontalGroup(
            pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxPrefijos, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombreDeCliente)
                                    .addComponent(txtDireccionCliente)
                                    .addComponent(txtTelefonoCliente)
                                    .addComponent(txtBarroCliente)
                                    .addComponent(txtNombreVendedor)))
                            .addComponent(lblValorFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlAgregarFacturaLayout.setVerticalGroup(
            pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(cbxPrefijos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNombreDeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33))
                        .addGap(1, 1, 1)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34))
                        .addGap(1, 1, 1)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(1, 1, 1)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBarroCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28))
                        .addGap(1, 1, 1)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(txtNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblValorFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                        .addComponent(lblValorRecaudoManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(lblPesoManifiesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jTableFacturasPorManifiesto.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
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
        jTableFacturasPorManifiesto.setComponentPopupMenu(mnuBorrarFilas);
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

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Auxiliar 1");

        txtNombreDeAuxiliar1.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreDeAuxiliar1.setToolTipText("Ingresar apellidos completos");
        txtNombreDeAuxiliar1.setAutoscrolls(false);
        txtNombreDeAuxiliar1.setEnabled(false);
        txtNombreDeAuxiliar1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDeAuxiliar1FocusGained(evt);
            }
        });
        txtNombreDeAuxiliar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDeAuxiliar1ActionPerformed(evt);
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
        txtNombreDeAuxiliar2.setEnabled(false);
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
        txtNombreDeAuxiliar3.setEnabled(false);
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
        txtNombreDedespachador.setEnabled(false);
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane3)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGap(5, 5, 5)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)))
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtnombreDeConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNombreDeAuxiliar1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNombreDeAuxiliar2, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNombreDeAuxiliar3, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNombreDedespachador, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtKmDeSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblKilometros)
                                        .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addComponent(brrProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtnombreDeConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtKmDeSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKilometros))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreDeAuxiliar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblNumeroManifiesto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(3, 3, 3))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblNumeroManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtPesoMAximoAutorizado, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPesoMAximoAutorizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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


    private void jTableFacturasPorManifiestoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFacturasPorManifiestoMouseClicked
        jBtnBorrarFIla.setEnabled(true);


    }//GEN-LAST:event_jTableFacturasPorManifiestoMouseClicked

    private void txtnombreDeConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnombreDeConductorFocusGained
//        txtnombreDeConductor.setSelectionStart(0);
        //       txtnombreDeConductor.setSelectionEnd(txtnombreDeConductor.getText().length());
    }//GEN-LAST:event_txtnombreDeConductorFocusGained

    private void txtnombreDeConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreDeConductorKeyPressed
        /* Evento en el caso que se oprima la tecla F2 */
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            //if (manifiestoActual.getEstadoManifiesto() < 2) {
            FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(this, 1);
            this.getParent().getParent().add(form);
            form.toFront();
            form.setClosable(true);
            form.setVisible(true);
            form.setMaximizable(false);
            form.setResizable(false);
            form.moveToFront();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            form.setLocation(screenSize.width / 2, (screenSize.height) / 2);
            form.setTitle("Formulario para buscar Empleados por apellidos");
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            form.show();
            form.txtApellidosPersona.requestFocus();
            //}

        }


        /* Evento en el caso que se oprima la tecla enter */
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                Date fecha = Inicio.getFechaSql(dateManifiesto);
                for (CEmpleados conductor : ini.getListaDeEmpleados()) {
                    String cadena = conductor.getNombres() + " " + conductor.getApellidos();
                    if (txtnombreDeConductor.getText().trim().equals(cadena)) {
                        this.conductor = conductor;
                        break;
                    }

                }

                validarManifeisto(fecha);

            } catch (Exception ex) {
                Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_txtnombreDeConductorKeyPressed

    public void validarManifeisto(Date fecha) throws Exception {
        if (conductor != null && carro != null) {
            CManifiestosDeDistribucion manifiestosDeDistribucion = new CManifiestosDeDistribucion(ini,
                    this.conductor.getCedula(), carro.getPlaca(), "" + fecha);

            if (manifiestosDeDistribucion.getNumeroManifiesto() == null) {
                // crearNuevoManifiesto(carro);

            } else {
                manifiestoActual = manifiestosDeDistribucion;

                this.setTitle(carro.getPlaca());
                llenarDatosManifiesto();
                txtNombreDeAuxiliar1.setEnabled(false);
                cbxCanales.setEnabled(false);
                cbxRutaDeDistribucion.setEnabled(false);
                dateManifiesto.setEnabled(false);
                btnCrearManifiesto.setEnabled(false);

                if (manifiestoActual.getEstadoManifiesto() < 4) {
                    cbxPrefijos.setEnabled(true);
                    txtNumeroDeFactura.setEnabled(true);
                    txtNumeroDeFactura.setEditable(true);
                    txtNumeroDeFactura.requestFocus();
                } else {
                    cbxPrefijos.setEnabled(false);
                    txtNumeroDeFactura.setEnabled(false);
                    txtNumeroDeFactura.setEditable(false);

                }
            }
        } else {
            txtPlaca.setEnabled(true);
            txtPlaca.setEditable(true);
            txtPlaca.requestFocus();
        }
    }

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
            jBtnBorrarFIla.setEnabled(false);

            try {
                String numeroFactura = cbxPrefijos.getSelectedItem().toString() + (Integer.parseInt(txtNumeroDeFactura.getText().trim()));

                agregarFactura(numeroFactura, false, jTableFacturasPorManifiesto.getRowCount() + 1);

                lblCantidadFacturas.setText("" + manifiestoActual.getListaFacturasPorManifiesto().size());

                /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                if (manifiestoActual.getListaFacturasPorManifiesto().size() > 1) {
                    btnImprimir.setEnabled(true);
                    jBtnImprimir.setEnabled(true);
                }

            } catch (Exception ex) {
                Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
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

        try {

            /* se crea el objeto factura, digitado en el campo de texto */
            facturaActual = new CFacturas(ini, numeroDeFactura);

            if (facturaActual.getNumeroDeFactura() == null) {
                JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " no existe en servidor local", "Error, factura no existe", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (facturaActual.getNombreDeCliente().contains("A N U L A D O")) {
                JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + "s e encuentra anulada", "Error, factura Anulada", JOptionPane.WARNING_MESSAGE);
                facturaActual = null;
                return;
            }

            /* SE VALIDA QUE LA FACTURA EXISTA  */
            if (facturaActual.getNumeroDeFactura() != null) {

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

                    case 8:
                        /*ANULADA*/
                        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + "  fue ANULADA", "Error, factura ANULADA", JOptionPane.ERROR_MESSAGE);
                        return;

                }

                CFacturasPorManifiesto facXMfto = new CFacturasPorManifiesto(ini);

                if (facturaActual.getIsFree() == 0) {
                    JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura
                            + " ya se encuentra en Distribución "
                            + "", "Error", JOptionPane.WARNING_MESSAGE);

                    /* =1  la factura está disponible para agregarla al manifiesto 
                           SE registran las facturas que presentan inconvenientes en el Jlist*/
                    //modelo3.addElement(numeroDeFactura);
                    //jListListaDeFacturasErradas.setModel(modelo3);
                    // return;
                }


                /* SE valida que la factura no esté en el manifiesto */
                if (!estaLaFacturaEnElManifiesto()) {
                    jBtnMinuta.setEnabled(true);

                    /* Se agregan las propiedades a l objeto vista facturas por manifiesto */
                    facXMfto.setAdherencia(adherenciaGenral);
                    facXMfto.setNumeroManifiesto(manifiestoActual.getNumeroManifiesto());
                    facXMfto.setNumeroFactura(facturaActual.getNumeroDeFactura());
                    facXMfto.setNombreDeCliente(facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(facturaActual.getDireccionDeCliente());
                    facXMfto.setValorTotalFactura(facturaActual.getValorTotalFactura());
                    facXMfto.setPesoFactura(facturaActual.getPesofactura());
                    facXMfto.setConsecutivo(0);
                    facXMfto.setIdCanal(manifiestoActual.getIdCanal());
                    facXMfto.setNombreCanal(manifiestoActual.getNombreCanal());
                    facXMfto.setValorARecaudarFactura(facturaActual.getValorTotalFactura());
                    facXMfto.setFechaDistribucion(manifiestoActual.getFechaDistribucion());
                    facXMfto.setVehiculo(manifiestoActual.getVehiculo());
                    facXMfto.setConductor(manifiestoActual.getConductor());
                    facXMfto.setNombreConductor(manifiestoActual.getNombreConductor());
                    facXMfto.setNombreDeCliente(facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(facturaActual.getDireccionDeCliente());
                    facXMfto.setValorFacturaSinIva(facturaActual.getValorFacturaSinIva());
                    facXMfto.setValorTotalFactura(facturaActual.getValorTotalFactura());
                    facXMfto.setValorRechazo(0.0);
                    facXMfto.setValorDescuento(0.0);
                    facXMfto.setValorRecaudado(0.0);
                    facXMfto.setVendedor(facturaActual.getVendedor());


                    /* se actualiza el canal  y la forma de pago de distribución de la factura */
                    facturaActual.setFormaDePago(formaDePago);
                    sumadorPesosFacturas += facXMfto.getPesoFactura();
                    //valorTotalManifiesto += facXMfto.getValorTotalFactura();
                    //manifiestoActual.setPesoKgManifiesto(sumadorPesosFacturas);
                    //manifiestoActual.setValorTotalManifiesto(valorTotalManifiesto);

                    /* se verifica que la factura esté libre, sino arroja el siguiente mensaje... 
                     =0 indica que la factura esta siendo ocupada por alguien  */
 /* se agrega el registro al array  de facturas por manifiesto */
                    manifiestoActual.getListaFacturasPorManifiesto().add(facXMfto);

                    /*Validamos de donde proviene el dato: bien sea del campo 
                            de texto ó de un archivo plano */
                    if (!desdeArchivo) {
                        /* sí hay datos en la tabla de los productos  de la factura se limpia la tabla */
                        limpiarDatodsDePanelProductosPorFactura();

                        txtNombreDeCliente.setText(facturaActual.getNombreDeCliente());
                        txtDireccionCliente.setText(facturaActual.getDireccionDeCliente());
                        txtTelefonoCliente.setText(facturaActual.getTelefonoCliente());
                        txtBarroCliente.setText(facturaActual.getBarrio());
                        txtNombreVendedor.setText(facturaActual.getVendedor());
                        lblValorFactura.setText(nf.format(facturaActual.getValorTotalFactura()));

                        DecimalFormat df = new DecimalFormat("#,###.0");
                        lblPesoDeLaFactura.setText(df.format((facturaActual.getPesofactura()) / 1000) + " Kg");

                        /* Se anexa la factura al array.
                                 Se crea un array temporal de los productos que tiene la factura.. */
                        List<CProductosPorFactura> listaProduproductosEnLaFactura = new ArrayList();

                        CFacturas factura = new CFacturas(ini);
                        factura.setNumeroDeFactura(facturaActual.getNumeroDeFactura());

                        factura.setListaCProductosPorFactura(false); // Vst_ProductosPorFactura
                        listaProduproductosEnLaFactura = factura.getListaCProductosPorFactura();

                        /* se llena la tabla de productos por Factura*/
                        llenarJtableProductosPorFactura(listaProduproductosEnLaFactura);

                    }
                    sumadorPesosFacturas += facturaActual.getPesofactura();

                    /*Se llena el Jtable correspondiente*/
                    llenarJtableFacturasPorManifiesto();

                    /* se ubica el cursor en la fila insertada */
                    jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);

                    /* se imprime el dato en la respectiva etiqueta */
                    DecimalFormat df = new DecimalFormat("#,###.0");
                    lblPesoManifiesto.setText(df.format(sumadorPesosFacturas / 1000) + " Kg");
                    lblValorRecaudoManifiesto.setText(nf.format(valorTotalManifiesto));
                    btnGrabar.setEnabled(!grabar);
                    jBtnGrabar.setEnabled(!grabar);
                    jBtnObservaciones.setEnabled(!grabar);

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
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
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

    private void llenarJtableProductosPorFactura(List<CProductosPorFactura> arrProduproductosEnLaFactura) {
        modelo1 = (DefaultTableModel) jTableProductosPorFactura.getModel();

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura */
        for (CProductosPorFactura obj : arrProduproductosEnLaFactura) {

            Vst_ProductosPorFactura pxf = new Vst_ProductosPorFactura();

            int fila = jTableProductosPorFactura.getRowCount();

            modelo1.addRow(new Object[jTableProductosPorFactura.getRowCount()]);

            jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva() * obj.getCantidad()), fila, 5);
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
        contadorDeFacturas = 0;

        try {

            int adherencia = 1;

            /* Se hace el recorrido por el array para crear los objetos*/
            for (String obj : lista) {
                numeroDeFactura = obj;
                agregarFactura(numeroDeFactura, true, adherencia);
                adherencia++;

            }
        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
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


    private void txtNombreDeAuxiliar1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar1FocusGained

    private void txtNombreDeAuxiliar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            //if (manifiestoActual.getEstadoManifiesto() < 2) {
            FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(this, 2);
            this.getParent().add(form);
            form.toFront();
            form.setClosable(true);
            form.setVisible(true);
            form.setMaximizable(false);
            form.setResizable(false);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            form.setLocation((screenSize.width - form.getSize().width) / 2, (screenSize.height - form.getSize().height) / 2);
            form.setTitle("Formulario para buscar Empleados por apellidos");
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            form.show();
            form.txtApellidosPersona.requestFocus();
            // }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                for (CEmpleados auxiliar1 : ini.getListaDeEmpleados()) {
                    String cadena = auxiliar1.getNombres() + " " + auxiliar1.getApellidos();
                    if (txtNombreDeAuxiliar1.getText().trim().equals(cadena)) {
                        this.auxiliar1 = auxiliar1;
                        listaDeAuxiliares.add(auxiliar1);
                        break;
                    }

                }

                if (auxiliar1 == null) {
                    txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR");

                }
                txtNombreDeAuxiliar1.setEnabled(false);
                txtNombreDeAuxiliar2.setEnabled(true);
                txtNombreDeAuxiliar2.setEditable(true);
                txtNombreDeAuxiliar2.requestFocus();
            }

        }

    }//GEN-LAST:event_txtNombreDeAuxiliar1KeyPressed

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

    private void tablaDocsVencidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsVencidosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDocsVencidosMouseClicked

    private void txtNumeroDeManifiestoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeManifiestoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeManifiestoFocusGained

    private void txtNumeroDeManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeManifiestoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeManifiestoKeyPressed

    private void btnCrearManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearManifiestoActionPerformed
        try {

            canalDeVenta = null;
            ruta = null;


            /*Se crea un objeto ruta de distribucion*/
            for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                if (obj.getNombreRutasDeDistribucion().equals(cbxRutaDeDistribucion.getSelectedItem().toString())) {
                    ruta = new CRutasDeDistribucion(ini);
                    ruta = obj;
                    break;
                }
            }

            /*Se crea un objeto canal de distribucion*/
            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                if (obj.getNombreCanalDeVenta().equals(cbxCanales.getSelectedItem().toString())) {
                    canalDeVenta = new CCanalesDeVenta(ini);
                    canalDeVenta = obj;
                    break;
                }
            }

            sumadorPesosFacturas = 0.0;
            /* Se validan todos los datos del manifiesdo conductor, vehiculo, ruta, canal...*/
            if (validarManifiesto()) {

                new Thread(new HiloCrearManifiesto(ini, this)).start();

            } else {
                JOptionPane.showInternalMessageDialog(this, mensaje, "Error al guardar El Manifiesto de salida a Ruta", 0);
            }

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCrearManifiestoActionPerformed

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
       
    }//GEN-LAST:event_txtPlacaKeyPressed

    public void validarManifiesto2(Date fecha) {
        if (conductor != null && carro != null) {
            try {
                CManifiestosDeDistribucion manifiestosDeDistribucion = new CManifiestosDeDistribucion(ini,
                        this.conductor.getCedula(), carro.getPlaca(),this.despachador.getCedula() ,"" + fecha);

                if (manifiestosDeDistribucion.getNumeroManifiesto() == null) {
                    // crearNuevoManifiesto(carro);
                    this.setTitle(txtPlaca.getText());
                    txtKmDeSalida.setText("100");
                    txtNombreDeAuxiliar1.setEnabled(true);
                    txtNombreDeAuxiliar1.setEditable(true);
                    txtNombreDeAuxiliar1.requestFocus();
                } else {
                    manifiestoActual = manifiestosDeDistribucion;
                    this.setTitle(carro.getPlaca());
                    llenarDatosManifiesto();
                    txtnombreDeConductor.setEnabled(false);
                    txtPlaca.setEnabled(false);
                    txtNombreDeAuxiliar1.setEnabled(false);
                    cbxCanales.setEnabled(false);
                    cbxRutaDeDistribucion.setEnabled(false);
                    dateManifiesto.setEnabled(false);
                    btnCrearManifiesto.setEnabled(false);
                    if (manifiestoActual.getEstadoManifiesto() < 4) {
                        cbxPrefijos.setEnabled(true);
                        txtNumeroDeFactura.setEnabled(true);
                        txtNumeroDeFactura.setEditable(true);
                        txtNumeroDeFactura.requestFocus();
                    } else {
                        cbxPrefijos.setEnabled(false);
                        txtNumeroDeFactura.setEnabled(false);
                        txtNumeroDeFactura.setEditable(false);

                    }

                }

            } catch (Exception ex) {
                Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void txtPlacaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusGained
        // txtPlaca.setSelectionStart(0);
        // txtPlaca.setSelectionEnd(txtPlaca.getText().length());
    }//GEN-LAST:event_txtPlacaFocusGained

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


    private void jTabbedPane1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1FocusGained

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
                    Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
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
                        Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            } else {
                JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se puede eliminar fila, maniesto en distribución ", "No se puede Borrar fila ", 1, null);
        }
    }

    private void txtNombreDeAuxiliar2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar2FocusGained

    private void txtNombreDeAuxiliar2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(this, 3);
                this.getParent().add(form);
                form.toFront();
                form.setClosable(true);
                form.setVisible(true);
                form.setMaximizable(false);
                form.setResizable(false);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                form.setLocation((screenSize.width - form.getSize().width) / 2, (screenSize.height - form.getSize().height) / 2);
                form.setTitle("Formulario para buscar Empleados por apellidos");
                btnNuevo.setEnabled(false);
                jBtnNuevo.setEnabled(false);
                form.show();
                form.txtApellidosPersona.requestFocus();
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                for (CEmpleados auxiliar2 : ini.getListaDeEmpleados()) {
                    String cadena = auxiliar2.getNombres() + " " + auxiliar2.getApellidos();
                    if (txtNombreDeAuxiliar2.getText().trim().equals(cadena)) {
                        this.auxiliar2 = auxiliar2;
                        listaDeAuxiliares.add(auxiliar2);
                        break;
                    }

                }
                if (auxiliar2 == null) {
                    txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR");

                }

            }
            txtNombreDeAuxiliar2.setEnabled(false);
            txtNombreDeAuxiliar3.setEnabled(true);
            txtNombreDeAuxiliar3.setEditable(true);
            txtNombreDeAuxiliar3.requestFocus();

        }

    }//GEN-LAST:event_txtNombreDeAuxiliar2KeyPressed

    private void txtNombreDeAuxiliar3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar3FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar3FocusGained

    private void txtNombreDeAuxiliar3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(this, 4);
                this.getParent().add(form);
                form.toFront();
                form.setClosable(true);
                form.setVisible(true);
                form.setMaximizable(false);
                form.setResizable(false);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                form.setLocation((screenSize.width - form.getSize().width) / 2, (screenSize.height - form.getSize().height) / 2);
                form.setTitle("Formulario para buscar Empleados por apellidos");
                btnNuevo.setEnabled(false);
                jBtnNuevo.setEnabled(false);
                form.show();
                form.txtApellidosPersona.requestFocus();
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                for (CEmpleados auxiliar3 : ini.getListaDeEmpleados()) {
                    String cadena = auxiliar3.getNombres() + " " + auxiliar3.getApellidos();
                    if (txtNombreDeAuxiliar3.getText().trim().equals(cadena)) {
                        this.auxiliar3 = auxiliar3;
                        listaDeAuxiliares.add(auxiliar3);
                        break;
                    }

                }

                if (auxiliar3 == null) {
                    txtNombreDeAuxiliar3.setText("SALE A DISTRIBUCION SIN AUXILIAR");

                }

                txtNombreDeAuxiliar3.setEnabled(false);
                txtNombreDedespachador.setEnabled(true);
                txtNombreDedespachador.setEditable(true);
                txtNombreDedespachador.requestFocus();
            }

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar3KeyPressed

    private void txtNombreDedespachadorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDedespachadorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDedespachadorFocusGained

    private void txtNombreDedespachadorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDedespachadorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            if (manifiestoActual.getEstadoManifiesto() < 2) {
                FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(this, 5);
                this.getParent().add(form);
                form.toFront();
                form.setClosable(true);
                form.setVisible(true);
                form.setMaximizable(false);
                form.setResizable(false);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                form.setLocation((screenSize.width - form.getSize().width) / 2, (screenSize.height - form.getSize().height) / 2);
                form.setTitle("Formulario para buscar Empleados por apellidos");
                btnNuevo.setEnabled(false);
                jBtnNuevo.setEnabled(false);
                form.show();
                form.txtApellidosPersona.requestFocus();
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            for (CEmpleados despachador : ini.getListaDeEmpleados()) {
                String cadena = despachador.getNombres() + " " + despachador.getApellidos();
                if (txtNombreDedespachador.getText().trim().equals(cadena)) {
                    this.despachador = despachador;
                    break;
                }

            }
            if (despachador == null) {
                JOptionPane.showMessageDialog(this, "No ha seleccionado el despachador", "Alerta", JOptionPane.INFORMATION_MESSAGE);
            } else {

                txtNombreDedespachador.setEnabled(false);
                txtNombreDedespachador.setEditable(true);
                cbxCanales.requestFocus();
            }
        }
        
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            /* SE BUSCA EN VEHICULO EN EL ARRAY DEL SISTEMA */
            for (CCarros carro : ini.getListaDeVehiculos()) {
                if (carro.getPlaca().equals(txtPlaca.getText().trim())) {
                    this.carro = carro;
                    this.carro.setConductor(this.conductor.getCedula());
                    break;
                }
            }
            Date fecha = Inicio.getFechaSql(dateManifiesto);

            validarManifiesto2(fecha);

        }
    }//GEN-LAST:event_txtNombreDedespachadorKeyPressed

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

        if (manifiestoActual != null) {
            MinutasDeDistribucion sacarMinuta = new MinutasDeDistribucion(ini);

            switch (manifiestoActual.getEstadoManifiesto()) {
                case 1:

                case 2:
                    sacarMinuta.minutaPorManifiestoAbierto(manifiestoActual.getListaFacturasPorManifiesto());
                    break;
                case 3:
                    sacarMinuta.minutaPoManifiesto(manifiestoActual.getNumeroManifiesto());
                    break;
                case 4:
                    sacarMinuta.minutaPoManifiesto(manifiestoActual.getNumeroManifiesto());
                    break;

                default:
                    break;

            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay manifiesto de ruta selecccionado ", "Error", 0);
        }
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
//        FAgregarObservacionesManifiesto fAgregarObservacionesManifiesto = new FAgregarObservacionesManifiesto(this);
//        this.getParent().add(fAgregarObservacionesManifiesto);
//        fAgregarObservacionesManifiesto.toFront();
//        fAgregarObservacionesManifiesto.setClosable(true);
//        fAgregarObservacionesManifiesto.setVisible(true);
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        fAgregarObservacionesManifiesto.setTitle("Formulario para ingresar las observacciones del Manifiesto");
//        fAgregarObservacionesManifiesto.setLocation((screenSize.width - fAgregarObservacionesManifiesto.getSize().width) / 2, (screenSize.height - fAgregarObservacionesManifiesto.getSize().height) / 2);
//        fAgregarObservacionesManifiesto.show();
    }//GEN-LAST:event_jBtnObservacionesActionPerformed

    private void txtNombreDeAuxiliar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar1ActionPerformed

    }//GEN-LAST:event_txtNombreDeAuxiliar1ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked

        String nombreConductor;
        String placa;

        String strMain = jList1.getSelectedValue();
        String[] arrSplit = strMain.split("-");

        nombreConductor = arrSplit[0];
        placa = arrSplit[1];
        try {

            for (CManifiestosDeDistribucion man : listaDemanifiestos) {

                if (man.getNombreConductor().equals(nombreConductor) && man.getVehiculo().equals(placa)) {
                    manifiestoActual = man;
                  for(CCarros car : ini.getListaDeVehiculos()){
                  if(car.getPlaca().equals("placa")){
                      this.carro=car;
                      break;
                  }
                      
                  }
                    
                    break;
                }
                limpiar();
                
                llenarDatosManifiesto();
            }
        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jList1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu borraElementos;
    private javax.swing.JMenuItem borrar1Fila;
    private javax.swing.JMenuItem borrarTodo;
    public javax.swing.JProgressBar brrProgreso;
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnCrearManifiesto;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JComboBox cbxCanales;
    public javax.swing.JComboBox<String> cbxPrefijos;
    public javax.swing.JComboBox cbxRutaDeDistribucion;
    public com.toedter.calendar.JDateChooser dateManifiesto;
    private javax.swing.JToggleButton jBtnBorrarFIla;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnImprimir;
    public javax.swing.JToggleButton jBtnMinuta;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JButton jBtnNuevo1;
    public javax.swing.JToggleButton jBtnObservaciones;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableFacturasPorManifiesto;
    private javax.swing.JTable jTableProductosPorFactura;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblCantidadFacturas;
    public javax.swing.JLabel lblCirculoDeProgreso1;
    public javax.swing.JLabel lblCirculoDeProgreso2;
    private javax.swing.JLabel lblKilometros;
    public javax.swing.JLabel lblNumeroManifiesto;
    private javax.swing.JLabel lblPesoDeLaFactura;
    private org.edisoncor.gui.label.LabelCustom lblPesoManifiesto;
    private org.edisoncor.gui.label.LabelCustom lblValorFactura;
    private org.edisoncor.gui.label.LabelCustom lblValorRecaudoManifiesto;
    private javax.swing.JPopupMenu mnuBorrarFilas;
    public javax.swing.JPanel pnlAgregarFactura;
    private javax.swing.JTable tablaDocsVencidos;
    public javax.swing.JTextField txtBarroCliente;
    public javax.swing.JTextField txtDireccionCliente;
    public javax.swing.JTextField txtKmDeSalida;
    public javax.swing.JTextField txtNombreDeAuxiliar1;
    public javax.swing.JTextField txtNombreDeAuxiliar2;
    public javax.swing.JTextField txtNombreDeAuxiliar3;
    public javax.swing.JTextField txtNombreDeCliente;
    public javax.swing.JTextField txtNombreDedespachador;
    public javax.swing.JTextField txtNombreVendedor;
    public javax.swing.JTextField txtNumeroDeFactura;
    public javax.swing.JTextField txtNumeroDeManifiesto;
    private javax.swing.JTextField txtPesoMAximoAutorizado;
    public javax.swing.JTextField txtPlaca;
    public javax.swing.JTextField txtTelefonoCliente;
    public javax.swing.JTextField txtnombreDeConductor;
    // End of variables declaration//GEN-END:variables

    public void nuevo() {
        try {
            cancelar();
            nuevo = true;
            //limpiar();

            dateManifiesto.setDate(new Date());

            jPanel7.setEnabled(true);

            txtnombreDeConductor.setEnabled(true);
            txtnombreDeConductor.setEditable(true);
            cbxCanales.setEnabled(true);
            cbxRutaDeDistribucion.setEnabled(true);
            dateManifiesto.setEnabled(true);
            jTabbedPane1.setEnabled(false);
            btnCrearManifiesto.setEnabled(true);
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            txtPlaca.requestFocus();
            formaDePago = "CONTADO"; // contado

            listaDeAuxiliares = new ArrayList();

            listaDeFacturasEnElArchivo = new ArrayList();
            manifiestoActual = new CManifiestosDeDistribucion(ini);
            manifiestoActual.setEstadoManifiesto(0);

            lblPesoManifiesto.setText("0.0 Kg");
            lblPesoDeLaFactura.setText("0.0 Kg");
            lblValorRecaudoManifiesto.setText("$0.0");

            txtnombreDeConductor.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
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

                    // new Thread(new HiloGuardarFacturasPorManifiesto(ini, this)).start();
                    // new Thread(new JcProgressBar(brrProgreso, 100)).start();
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
                // manifiestoActual.setListaFacturasPorManifiesto(listaFacturasPorManifiesto);
            } else {
                /*Manifiesto grabado en la BBDD , trae registros desde allí*/
                manifiestoActual.setListaFacturasPorManifiesto();
            }


            /* Genera el manifiesto  Genera el rutero*/
            RepporteRuteroConductoresConPesosyDEscuentos demo = new RepporteRuteroConductoresConPesosyDEscuentos(ini, manifiestoActual);
            //RepporteRuteroLaHielera demo = new RepporteRuteroLaHielera(ini, manifiestoActual);

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
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
        formaDePago = "CONTADO";
        mensaje = null;
        contadorDeFacturas = 0;
        sumadorPesosFacturas = 0.0;
        listaDeFacturasEnElArchivo = null;;
        listaDeAuxiliares = null;
        manifiestoActual = null;
        estaOcupadoGrabando = false;
        cadenaDeFacturas = null;
        adherenciaGenral = 0;
        cantDeSalidas = 0;

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
        formaDePago = "CONTADO";
        lblCirculoDeProgreso1.setVisible(false);

        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);

        /*Componentes de JTabbePane */
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
        try {
            jTabbedPane1.setEnabled(valor);

            txtNombreDeCliente.setEditable(valor);
            txtTelefonoCliente.setEditable(valor);
            txtNombreVendedor.setEditable(valor);

            //dateVencimiento.setEnabled(valor);
            jTableFacturasPorManifiesto.setEnabled(valor);
            tablaDocsVencidos.setEnabled(valor);

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

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

        lblCirculoDeProgreso1.setVisible(false);
        lblCirculoDeProgreso2.setVisible(false);

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

            if (cbxCanales.getSelectedIndex() == 0) {
                mensaje += "No ha seleccionado el canal de distribución " + "  \n";
                verificado = false;
            }

            if (cbxRutaDeDistribucion.getSelectedIndex() == 0) {
                mensaje += "No ha seleccionado la Ruta de Distribución " + "  \n";
                verificado = false;
            }

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
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificado;
    }

    public void llenarDatosManifiestoCerrado() throws Exception {

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        txtnombreDeConductor.setEditable(false);

        txtNombreDeAuxiliar1.setEditable(false);
        txtNombreDeAuxiliar2.setEditable(false);
        txtNombreDeAuxiliar3.setEditable(false);
        txtNombreDedespachador.setEditable(false);
        txtKmDeSalida.setEditable(false);
        txtPlaca.setEditable(false);
        txtKmDeSalida.setEditable(false);
        cbxCanales.setEnabled(false);
        cbxRutaDeDistribucion.setEnabled(false);
        dateManifiesto.setEnabled(false);
        btnCrearManifiesto.setEnabled(false);
        jTabbedPane1.setEnabled(false);
        btnNuevo.setEnabled(false);
        jBtnNuevo.setEnabled(false);

        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);

        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);
        jBtnObservaciones.setEnabled(false);

        dateManifiesto.setEnabled(false);

        txtPlaca.setEnabled(false);
        txtnombreDeConductor.setEnabled(false);
        txtNombreDeAuxiliar1.setEnabled(false);
        txtNombreDeAuxiliar2.setEnabled(false);
        txtNombreDeAuxiliar3.setEnabled(false);
        txtNombreDedespachador.setEnabled(false);

        jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarFactura), false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
        //manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        // manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        manifiestoActual.setListaFacturasPorManifiesto();
        //listaDeCFacturasCamdunEnElManifiesto = manifiestoActual.getVstListaFacturasEnDistribucion(); // CfacturasCamdun
        //listaDeFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto(); //CFacturasPorManifiesto

        // manifiestoActual.setListaDeAuxiliares();
        listaDeAuxiliares = manifiestoActual.getListaDeAuxiliares("" + manifiestoActual.getNumeroManifiesto());

        manifiestoActual.setListaDeDescuentos();
        manifiestoActual.setListaDeRecogidas();

        // SE BUSCA EL CONDUCTOR Y SE LLENA EL CAMPO
        txtnombreDeConductor.setText(manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor());


        /* Se llenan os campos de texto de los auiliares*/
        llenarTxtAuxiliares();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (manifiestoActual.getDespachador().equals("0")) {
            txtNombreDedespachador.setText("");

        } else {
            txtNombreDedespachador.setText(manifiestoActual.getNombreDespachador() + " " + manifiestoActual.getApellidosDespachador());

        }

        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
        cbxCanales.setSelectedItem(manifiestoActual.getNombreCanal());

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        cbxRutaDeDistribucion.setSelectedItem(manifiestoActual.getNombreDeRuta());

        txtKmDeSalida.setText("" + manifiestoActual.getKmSalida());
        txtNumeroDeManifiesto.setText(manifiestoActual.codificarManifiesto());

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
        String strFecha = manifiestoActual.getFechaDistribucion();
        Date fecha = null;
        fecha = formatoDelTexto.parse(strFecha);
        //dateManifiesto.setDate(manifiestoActual.getFechaDistribucion());
        Date dt = new Date();
        dateManifiesto.setDate(fecha);

        int cantidadFacturasEnManifiesto = 0;
        sumadorPesosFacturas = 0.0;

        modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();

        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            filaTabla2 = jTableFacturasPorManifiesto.getRowCount();
            modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);

            jTableFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   
            valorTotalManifiesto += obj.getValorARecaudarFactura();

            // se ubica en la fila insertada
            jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);

            cantidadFacturasEnManifiesto++;
            valor += obj.getValorARecaudarFactura();
            sumadorPesosFacturas += obj.getPesoFactura();

            // }
            // }
            //this.repaint();
        }
        manifiestoActual.setValorTotalManifiesto(valor);

        lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + txtNumeroDeManifiesto.getText().trim());
        DecimalFormat df = new DecimalFormat("#,###.0");
        lblPesoManifiesto.setText(df.format(sumadorPesosFacturas / 1000) + " Kg");
        lblValorRecaudoManifiesto.setText(nf.format(valorTotalManifiesto));
        lblCantidadFacturas.setText("" + cantidadFacturasEnManifiesto);

        btnImprimir.setEnabled(true);
        jBtnImprimir.setEnabled(true);

        btnImprimir.requestFocus();

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
                            txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
                        } else {
                            txtNombreDeAuxiliar1.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;
                        break;
                    case 2:
                        if (aux.getCedula().equals("0")) {
                            txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
                        } else {
                            txtNombreDeAuxiliar2.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;
                        break;
                    case 3:
                        if (aux.getCedula().equals("0")) {
                            txtNombreDeAuxiliar3.setText("SALE A DISTRIBUCION SIN AUXILIAR3");
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
        lblPesoManifiesto.setText(df.format(sumadorPesosFacturas / 1000) + " Kg");
        lblValorRecaudoManifiesto.setText(nf.format(manifiestoActual.getValorTotalManifiesto()));

        llenarJtableFacturasXmanifiesto();

    }

    public void llenarJtableFacturasXmanifiesto() {
        DefaultTableModel modelo = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        DecimalFormat df;
        valorTotalManifiesto = 0.0;
        modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
        adherenciaGenral = 1;
        //  SE LLENA EL JTABLE FACTURAS POR MANIFIESTO
        if (manifiestoActual.getListaFacturasPorManifiesto() != null) {

            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {

                adherenciaGenral = obj.getAdherencia() + 1;

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

            }

            df = new DecimalFormat("#,###.0");
            lblPesoManifiesto.setText(df.format(sumadorPesosFacturas / 1000) + " Kg");
            lblValorRecaudoManifiesto.setText(nf.format(valorTotalManifiesto));
            lblCantidadFacturas.setText("" + manifiestoActual.getListaFacturasPorManifiesto().size());
            manifiestoActual.setValorTotalManifiesto(valorTotalManifiesto);

        }
    }

    private void limpiarDatodsDePanelProductosPorFactura() {

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
            //limpiar();
            //this.manifiestoActual = new CManifiestosDeDistribucion(ini);

            //modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
            //txtPlaca.setText(carro.getPlaca());
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
                    Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            /* SE DesHABILITA BOTON DE IMPRIMIR */
            btnImprimir.setEnabled(false);
            jBtnImprimir.setEnabled(false);
            jBtnMinuta.setEnabled(false);

            txtnombreDeConductor.setEditable(false);

            txtNombreDeAuxiliar1.setEditable(false);
            txtNombreDeAuxiliar2.setEditable(false);
            txtNombreDeAuxiliar3.setEditable(false);

            txtKmDeSalida.setEditable(true);
            txtPlaca.setEnabled(true);
            txtPlaca.setEditable(true);
            cbxCanales.setEnabled(true);
            cbxRutaDeDistribucion.setEnabled(true);
            dateManifiesto.setEnabled(true);
            jTabbedPane1.setEnabled(true);
            jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarFactura), true);
            btnCrearManifiesto.setEnabled(true);

            txtNombreDedespachador.setEnabled(true);

            txtnombreDeConductor.setEnabled(true);
            txtNombreDeAuxiliar1.setEnabled(true);
            txtNombreDeAuxiliar2.setEnabled(true);
            txtNombreDeAuxiliar3.setEnabled(true);
            txtNombreDedespachador.setEnabled(true);

            if (manifiestoActual != null) {
                manifiestoActual.setEstadoManifiesto(1);
            }

            txtKmDeSalida.requestFocus();
            txtKmDeSalida.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized void crearNuevoManifiesto(String cedulaConductor, String fecha) {
        try {

            canalDeVenta = null;
            ruta = null;


            /*Se crea un objeto ruta de distribucion*/
            for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                if (obj.getNombreRutasDeDistribucion().equals(cbxRutaDeDistribucion.getSelectedItem().toString())) {
                    ruta = new CRutasDeDistribucion(ini);
                    ruta = obj;
                    break;
                }
            }

            /*Se crea un objeto canal de distribucion*/
            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                if (obj.getNombreCanalDeVenta().equals(cbxCanales.getSelectedItem().toString())) {
                    canalDeVenta = new CCanalesDeVenta(ini);
                    canalDeVenta = obj;
                    break;
                }
            }

            manifiestoActual = new CManifiestosDeDistribucion(ini);
            manifiestoActual.setFechaDistribucion("" + Inicio.getFechaSql(dateManifiesto));
            manifiestoActual.setVehiculo(txtPlaca.getText());
            manifiestoActual.setConductor(conductor.getCedula());
            manifiestoActual.setDespachador(despachador.getCedula());
            manifiestoActual.setIdCanal(canalDeVenta.getIdCanalDeVenta());
            manifiestoActual.setIdRuta(ruta.getIdRutasDeDistribucion());
            manifiestoActual.setEstadoManifiesto(2);
            manifiestoActual.setKmSalida(Integer.parseInt(txtKmDeSalida.getText()));
            manifiestoActual.setKmEntrada(Integer.parseInt(txtKmDeSalida.getText()));
            manifiestoActual.setKmRecorrido(0);
            manifiestoActual.setZona(ini.getUser().getZona());
            manifiestoActual.setRegional(ini.getUser().getRegional());
            manifiestoActual.setAgencia(ini.getUser().getAgencia());
            manifiestoActual.setIsFree(1);
            manifiestoActual.setValorTotalManifiesto(0.0);
            manifiestoActual.setValorRecaudado(0.0);
            manifiestoActual.setHoraDeDespacho("CURRENT_TIMESTAMP()");
            manifiestoActual.setHoraDeLiquidacion("CURRENT_TIMESTAMP()");
            manifiestoActual.setPesoKgManifiesto(0.0);
            manifiestoActual.setCantidadPedidos(0);
            manifiestoActual.setActivo(1);
            manifiestoActual.setFechaReal("CURRENT_TIMESTAMP()");
            manifiestoActual.setUsuarioManifiesto(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            //manifiestoActual.setF(1);
            manifiestoActual.setObservaciones("SIN NOVEDAD");

            sumadorPesosFacturas = 0.0;
            /* Se validan todos los datos del manifiesdo conductor, vehiculo, ruta, canal...*/
            if (validarManifiesto()) {

                new Thread(new HiloCrearManifiesto(ini, this)).start();

            } else {
                JOptionPane.showInternalMessageDialog(this, mensaje, "Error al guardar El Manifiesto de salida a Ruta", 0);
            }

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
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
                        jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarFactura), true);
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
                        jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarFactura), true);
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
                    Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
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
        lblPesoManifiesto.setText(df.format(sumadorPesosFacturas / 1000) + " Kg");
        lblValorRecaudoManifiesto.setText(nf.format(valorTotalManifiesto));
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
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
            CFacturas fac = new CFacturas(ini, obj.getNumeroFactura(), false);
            fac.liberarFactura(true);

        }

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
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
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

            } catch (IOException ex) {
                Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, e2);

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
        if (manifiestoActual.getEstadoManifiesto() == 2 || manifiestoActual.getEstadoManifiesto() == 3) {  // IINICIO DEL IF-> ESTADO DEL MANIFIESTO

            //deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);
            //if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {
            if (true) {

                manifiestoActual.setCantidadPedidos(manifiestoActual.getListaFacturasPorManifiesto().size());


                /* Se valida la conexión a internet para grabar los datos en la BBDD */
                if (ini.verificarConexion()) {
                    //if (true) {
                    band = true;

                   // new Thread(new HiloGuardarFacturasPorManifiesto_2(ini, this)).start();
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

    private String buscarmanifiesto() {
        CManifiestosDeDistribucion manifiesto = null;
        if (manifiestoActual.getConductor() != null || manifiestoActual.getVehiculo() != null) {

            try {
                Date dt = new Date();
                dt = ini.getFechaSql(this.dateManifiesto);
                manifiesto = new CManifiestosDeDistribucion(ini, conductor.getCedula(), txtPlaca.getText(), "" + dt);

            } catch (Exception ex) {
                Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return manifiesto.getNumeroManifiesto();
    }

}
