/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.distribucion.Threads.HiloGuardarBBDDRemota;
import aplicacionlogistica.distribucion.consultas.minutas.MinutasDeDistribucion;
import aplicacionlogistica.distribucion.formularios.poblaciones.FManifestarPedidosPoblaciones;
import aplicacionlogistica.distribucion.imprimir.RepporteRuteroConductoresConPesosyDEscuentos;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductos;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.objetos.personas.Vst_empleados;
import mtto.vehiculos.CCarros;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
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
public class FCrearFacturasDeVenta extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public static boolean band = false;
    public static int valorDespBarraProgreso = 0;

    CUsuarios user;
    public CCarros carro = null;
    public CManifiestosDeDistribucion manifiestoActual = null;
    public CRutasDeDistribucion ruta;
    public CCanalesDeVenta canalDeVenta;
    CFacturas facturaActual = null;
    CClientes cliente = null;
    CProductos producto = null;
    CProductosPorFactura productoPorFactura = null;

    Inicio ini = null;

    public File archivoConListaDeFacturas;

    DefaultListModel modelo, modelo3;

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public DefaultTableModel modelo1, modelo2;

    public Double valorTotalManifiesto = 0.0;
    boolean cargado = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean nuevo = false;
    boolean facturaVerificada = false;

    int filaTabla2;
    int kilometraje = 0;
    public String formaDePago;
    String mensaje = null;
    int contadorDeFacturas = 0;

    double sumadorPesosFacturasq;

    public List<String> listaDeFacturasEnElArchivo;
    public List<Vst_empleados> listaDeAuxiliares = new ArrayList();

    TextAutoCompleter autoTxtVehiculos;
    TextAutoCompleter autoTxtIdClientes;
    TextAutoCompleter autoTxtCodigoProducto;
    TextAutoCompleter autoTxtDescripcionDeProducto;
    TextAutoCompleter autoTxtVendedor;
    TextAutoCompleter autoTxtCiudad;

    String cadenaDeFacturas;

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

    public boolean estaOcupadoGrabando = false;

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public FCrearFacturasDeVenta(Inicio ini) throws Exception {
        this.ini = ini;

        try {
            initComponents();


            /*Atualiza el listado de clientes de la BBDD*/
            // new Thread(new HiloListadoClientes(ini)).start();
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
            Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }

        // SE LLENAN LAS LISTAS DESPLEGABLES
        this.autoTxtIdClientes = new TextAutoCompleter(txtIdeCliente);

        for (CClientes cliente : this.ini.getListaDeClientes()) {
            if (cliente.getActivoCliente() == 1) {
                autoTxtIdClientes.addItem(cliente.getCodigoInterno() + " ");
            }
        }

        this.autoTxtCodigoProducto = new TextAutoCompleter(txtCodigoproducto);
        this.autoTxtDescripcionDeProducto = new TextAutoCompleter(txtDescripcionProducto);

        for (CProductos producto : this.ini.getListaDeProductosCamdun()) {
            if (producto.getActivo() == 1) {
                autoTxtCodigoProducto.addItem(producto.getCodigoProducto() + " ");
                autoTxtDescripcionDeProducto.addItem(producto.getDescripcionProducto() + " ");
            }
        }

        this.autoTxtCiudad = new TextAutoCompleter(txtCiudad);

//        for (CCiudades ciudad : this.ini.getListaDeCiudades()) {
//            if (ciudad.getActivoCiudad() == 1) {
//                autoTxtCiudad.addItem(ciudad.getNombreCiudad() + " ");
//            }
//        }
        this.autoTxtVendedor = new TextAutoCompleter(txtNombreVendedor);
        for (String vendedor : this.ini.getListaDeVendedores()) {

            autoTxtVendedor.addItem(vendedor + " ");

        }

        cargado = true;

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
        txtPrefijo = new javax.swing.JTextField();
        txtNumeroDeFactura = new javax.swing.JTextField();
        txtIdeCliente = new javax.swing.JTextField();
        txtNombreDeCliente = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtBarrio = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtCiudad = new javax.swing.JTextField();
        txtNombreVendedor = new javax.swing.JTextField();
        txtValorFactura = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lblPesoDeLaFactura = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        btnSiguiente = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        lblValorFactura = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtNitCliente = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtNombreEstablecimiento = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtClasificacionCliente = new javax.swing.JTextField();
        pnlAgregarProductos = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableProductosPorFactura = new javax.swing.JTable();
        txtCodigoproducto = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtDescripcionProducto = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtLineaproducto = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtValorUnitarioConIva = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtValorNeto = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtValorUnitarioSinIva = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        txtPesoProducto = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFacturasPorManifiesto = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnNuevo1 = new javax.swing.JButton();
        jBtnBorrarFIla = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();

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
        setTitle("Formulario para Crear Facturas de Venta");
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

        txtPrefijo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPrefijoFocusGained(evt);
            }
        });
        txtPrefijo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrefijoKeyPressed(evt);
            }
        });

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

        txtIdeCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtIdeClienteFocusGained(evt);
            }
        });
        txtIdeCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdeClienteKeyPressed(evt);
            }
        });

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

        txtDireccion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDireccionFocusGained(evt);
            }
        });
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionKeyPressed(evt);
            }
        });

        txtBarrio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarrioFocusGained(evt);
            }
        });
        txtBarrio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarrioActionPerformed(evt);
            }
        });
        txtBarrio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarrioKeyPressed(evt);
            }
        });

        txtTelefono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoFocusGained(evt);
            }
        });
        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyPressed(evt);
            }
        });

        txtCiudad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCiudadFocusGained(evt);
            }
        });
        txtCiudad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCiudadKeyPressed(evt);
            }
        });

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

        txtValorFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorFacturaFocusGained(evt);
            }
        });
        txtValorFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorFacturaKeyPressed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Numero Factura");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("idClient");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("nombre Cliente");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Direccion");

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Barrio");

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("telefono");

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Ciudad");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Vendedor");

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Valor Factura");

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Prefijo");

        jPanel6.setBorder(null);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Peso de las Facturas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N

        lblPesoDeLaFactura.setFont(new java.awt.Font("Cantarell", 0, 36)); // NOI18N
        lblPesoDeLaFactura.setText("0.0 k.");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPesoDeLaFactura, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(lblPesoDeLaFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 13, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        btnSiguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Button-Forward-icon.png"))); // NOI18N
        btnSiguiente.setEnabled(false);
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Valor Factura", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N

        lblValorFactura.setFont(new java.awt.Font("Cantarell", 0, 36)); // NOI18N
        lblValorFactura.setText("$0.0");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblValorFactura, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(lblValorFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSiguiente)
                .addGap(96, 204, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(btnSiguiente)))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("NitClient");

        txtNitCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNitClienteFocusGained(evt);
            }
        });
        txtNitCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNitClienteKeyPressed(evt);
            }
        });

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("nombre Establecimiento");

        txtNombreEstablecimiento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreEstablecimientoFocusGained(evt);
            }
        });
        txtNombreEstablecimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreEstablecimientoActionPerformed(evt);
            }
        });
        txtNombreEstablecimiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreEstablecimientoKeyPressed(evt);
            }
        });

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("Clasificacion");

        txtClasificacionCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtClasificacionClienteFocusGained(evt);
            }
        });
        txtClasificacionCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClasificacionClienteKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlAgregarFacturaLayout = new javax.swing.GroupLayout(pnlAgregarFactura);
        pnlAgregarFactura.setLayout(pnlAgregarFacturaLayout);
        pnlAgregarFacturaLayout.setHorizontalGroup(
            pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrefijo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDeCliente)
                    .addComponent(txtDireccion)
                    .addComponent(txtNitCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreEstablecimiento, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(txtClasificacionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlAgregarFacturaLayout.setVerticalGroup(
            pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrefijo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNitCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreDeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreEstablecimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtClasificacionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(txtValorFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Agregar Facturas", pnlAgregarFactura);

        pnlAgregarProductos.setAutoscrolls(true);
        pnlAgregarProductos.setDoubleBuffered(false);
        pnlAgregarProductos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pnlAgregarProductosFocusGained(evt);
            }
        });

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
                false, true, true, true, true, true
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

        txtCodigoproducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCodigoproductoFocusGained(evt);
            }
        });
        txtCodigoproducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoproductoKeyPressed(evt);
            }
        });

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("codigo");

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Descripcion");

        txtDescripcionProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDescripcionProductoFocusGained(evt);
            }
        });
        txtDescripcionProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDescripcionProductoMouseClicked(evt);
            }
        });
        txtDescripcionProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescripcionProductoKeyPressed(evt);
            }
        });

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("linea");

        txtLineaproducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLineaproductoFocusGained(evt);
            }
        });
        txtLineaproducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLineaproductoKeyPressed(evt);
            }
        });

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Cantidad");

        txtCantidad.setText("0.0");
        txtCantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCantidadFocusGained(evt);
            }
        });
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadKeyPressed(evt);
            }
        });

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("V.U.C.I");

        txtValorUnitarioConIva.setText("0.0");
        txtValorUnitarioConIva.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorUnitarioConIvaFocusGained(evt);
            }
        });
        txtValorUnitarioConIva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorUnitarioConIvaKeyPressed(evt);
            }
        });

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Valor Neto");

        txtValorNeto.setText("0.0");
        txtValorNeto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorNetoFocusGained(evt);
            }
        });
        txtValorNeto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorNetoActionPerformed(evt);
            }
        });
        txtValorNeto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorNetoKeyPressed(evt);
            }
        });

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("V.U.S.I");

        txtValorUnitarioSinIva.setText("0.0");
        txtValorUnitarioSinIva.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorUnitarioSinIvaFocusGained(evt);
            }
        });
        txtValorUnitarioSinIva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorUnitarioSinIvaActionPerformed(evt);
            }
        });
        txtValorUnitarioSinIva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorUnitarioSinIvaKeyPressed(evt);
            }
        });

        jPanel4.setBorder(null);

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Yes.png"))); // NOI18N
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOk)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(154, Short.MAX_VALUE)
                .addComponent(btnOk)
                .addContainerGap())
        );

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Peso Producto");

        txtPesoProducto.setText("0.0");
        txtPesoProducto.setToolTipText("Peso del producto en gramos");
        txtPesoProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPesoProductoFocusGained(evt);
            }
        });
        txtPesoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesoProductoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlAgregarProductosLayout = new javax.swing.GroupLayout(pnlAgregarProductos);
        pnlAgregarProductos.setLayout(pnlAgregarProductosLayout);
        pnlAgregarProductosLayout.setHorizontalGroup(
            pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarProductosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAgregarProductosLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 703, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlAgregarProductosLayout.createSequentialGroup()
                        .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigoproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtValorUnitarioConIva, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtValorUnitarioSinIva, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPesoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLineaproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtValorNeto, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlAgregarProductosLayout.setVerticalGroup(
            pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAgregarProductosLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAgregarProductosLayout.createSequentialGroup()
                        .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigoproducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLineaproducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtValorUnitarioConIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(txtValorUnitarioSinIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPesoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30))
                        .addGap(2, 2, 2)
                        .addGroup(pnlAgregarProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtValorNeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Detalle de la Factura", pnlAgregarProductos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 24, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        if (grabar()) {
            return;
        }


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
                JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + "no existe en servidor local", "Error, factura no existe", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (facturaActual.getNombreDeCliente().contains("A N U L A D O")) {
                JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + "se encuentra anulada", "Error, factura Anulada", JOptionPane.WARNING_MESSAGE);
                facturaActual = null;
                return;
            }

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

                    /* Se agregan las propiedades a l objeto vista facturas por manifiesto */
                    facXMfto.setAdherencia(manifiestoActual.getListaFacturasPorManifiesto().size() + 1);
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
                    // facturaActual.setCanal(canalDeVenta.getIdCanal());
                    facturaActual.setFormaDePago(formaDePago);


                    /* se verifica que la factura esté libre, sino arroja el siguiente mensaje... 
                     =0 indica que la factura esta siendo ocupada por alguien  */
 /* Se graba el registro temporal de factura por manifiesto en el archivo temporal*/
                    String listaDeCampos = facXMfto.getCadenaConLosCampos();

                    ArchivosDeTexto archivo = new ArchivosDeTexto("" + (new File(".").getAbsolutePath()).replace(".", "") + manifiestoActual.getRutArchivofacturasporManifiesto());

                    if (archivo.insertarLineaEnFichero(listaDeCampos, manifiestoActual.getRutArchivofacturasporManifiesto())) {

                        /* se agrega el registro al array  de facturas por manifiesto */
                        manifiestoActual.getListaFacturasPorManifiesto().add(facXMfto);//CfacturasPorManifiesto
                        //  manifiestoActual.setListaFacturasPorManifiesto(listaFacturasPorManifiesto);

                        /*Validamos de donde proviene el dato: bien sea del campo 
                            de texto ó de un archivo plano */
                        if (!desdeArchivo) {
                            /* sí hay datos en la tabla de los productos  de la factura se limpia la tabla */
                            limpiarDatodsDePanelProductosPorFactura();

                            txtNombreDeCliente.setText(facturaActual.getNombreDeCliente());
                            txtDireccion.setText(facturaActual.getDireccionDeCliente());
                            txtTelefono.setText(facturaActual.getTelefonoCliente());
                            txtBarrio.setText(facturaActual.getBarrio());
                            txtNombreVendedor.setText(facturaActual.getVendedor());
                            //lblValorFactura.setText(nf.format(facturaActual.getValorTotalFactura()));

                            DecimalFormat df = new DecimalFormat("#,###.0");
                            lblPesoDeLaFactura.setText(df.format(facturaActual.getPesofactura()) + " Kg");

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
                        /*Se llena el Jtable correspondiente*/
                        llenarJtableFacturasPorManifiesto();

                        /* se ubica el cursor en la fila insertada */
                        jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);

                        DecimalFormat df = new DecimalFormat("#,###.0");
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
            Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void llenarJtableFacturasPorManifiesto() {

        modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();

        /* se anexa el registro a la Jtable de facturas por manifiesto */
        filaTabla2 = jTableFacturasPorManifiesto.getRowCount();

        modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);

        jTableFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);
        jTableFacturasPorManifiesto.setValueAt(facturaActual.getNumeroDeFactura(), filaTabla2, 1);
        jTableFacturasPorManifiesto.setValueAt(facturaActual.getNombreDeCliente(), filaTabla2, 2);
        jTableFacturasPorManifiesto.setValueAt(nf.format(facturaActual.getValorTotalFactura()), filaTabla2, 3);

        valorTotalManifiesto += facturaActual.getValorTotalFactura();

        DecimalFormat df = new DecimalFormat("#,###.0");

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
            Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    private void jTableProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductosPorFacturaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableProductosPorFacturaMouseClicked

    private void tablaDocsVencidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsVencidosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDocsVencidosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (manifiestoActual != null) {

            manifiestoActual.liberarManifiesto(true);

        }

    }//GEN-LAST:event_formInternalFrameClosing

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
                    Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
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
 /* LLenar el Jtable de las facturas por manifiesto */
                        llenarjTableFacturasPorManifiesto();

                        /* ordena la tabla con la nueva adherencia */
                        ordenarTabla();

                    } catch (Exception ex) {
                        Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            } else {
                JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se puede eliminar fila, maniesto en distribución ", "No se puede Borrar fila ", 1, null);
        }
    }

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
                case 3:
                    sacarMinuta.minutaPoManifiesto(manifiestoActual.getNumeroManifiesto());
                case 4:

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

    private void pnlAgregarProductosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pnlAgregarProductosFocusGained

    }//GEN-LAST:event_pnlAgregarProductosFocusGained

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed

        try {

            if (!validarDatosProducto()) {
                JOptionPane.showMessageDialog(this, "El producto no tiene los datos completos :\n" + mensaje, "Faltan datos", JOptionPane.ERROR_MESSAGE, null);
                mensaje = "";
                return;
            }
            if (producto == null) {
                producto = new CProductos(ini);
            }

            agregarProductoALaTabla();
            lipiarCamposDeTexto();

            double pesoFactura = 0.0;
            double valorFactura = 0.0;
            for (CProductosPorFactura prod : facturaActual.getListaCProductosPorFactura()) {
                pesoFactura += prod.getPesoProducto();
                valorFactura += (prod.getCantidad() * prod.getValorUnitarioConIva());
            }
            lblPesoDeLaFactura.setText((pesoFactura / 1000) + " Kg");
            lblValorFactura.setText(nf.format(valorFactura));

            facturaActual.setPesofactura(pesoFactura);

            btnGrabar.setEnabled(true);
            jBtnGrabar.setEnabled(true);

            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);

            this.producto = null;

        } catch (Exception ex) {
            Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnOkActionPerformed

    private void txtNombreDeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDeClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeClienteActionPerformed

    private void txtBarrioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarrioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioActionPerformed

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void txtNumeroDeFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaActionPerformed

    private void txtIdeClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdeClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            boolean encontrado = false;

            for (CClientes cliente : ini.getListaDeClientes()) {
                if (cliente.getCodigoInterno().equals(txtIdeCliente.getText().trim())) {
                    encontrado = true;
                    this.cliente = cliente;
                    llenarDatosCliente();
                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarProductos), false);
                    break;
                } else {
                    try {
                        txtNombreDeCliente.requestFocus();
                        this.cliente = new CClientes(ini);
                        this.cliente.setLatitud("0");
                        this.cliente.setLongitud("0");
                        this.cliente.setCanalDeVenta(1);
                        this.cliente.setRuta(1);
                        this.cliente.setFrecuencia(1);
                        this.cliente.setZona(1);
                        this.cliente.setRegional(1);
                        this.cliente.setAgencia(1);
                        this.cliente.setPorcentajeDescuento(0.0);
                        this.cliente.setActivoCliente(1);

                    } catch (Exception ex) {
                        Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }//GEN-LAST:event_txtIdeClienteKeyPressed

    private void txtCodigoproductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoproductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            for (CProductos producto : ini.getListaDeProductosCamdun()) {
                if (producto.getCodigoProducto().equals(txtCodigoproducto.getText().trim())) {

                    this.producto = producto;
                    this.producto.setCodigoProducto(txtCodigoproducto.getText().trim());
                    llenarDatosProducto();
                    break;
                } else {
                    txtDescripcionProducto.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_txtCodigoproductoKeyPressed

    private void txtValorNetoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorNetoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorNetoActionPerformed

    private void txtNumeroDeFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                List<CProductosPorFactura> lista = new ArrayList<>();

                facturaActual = new CFacturas(ini, txtPrefijo.getText().trim() + txtNumeroDeFactura.getText().trim());

                if (facturaActual.getNumeroDeFactura() != null) {
                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarProductos), false);
                    llenarCajasDeTexto();
                    habilitarCajasDeTexto(false);
                    txtPrefijo.setEnabled(true);
                    txtNumeroDeFactura.setEnabled(true);

                    txtPrefijo.setEditable(true);
                    txtNumeroDeFactura.setEditable(true);
                    txtNumeroDeFactura.requestFocus();

                    if (estaLaFacturaEnElManifiesto()) {
                        btnSiguiente.setEnabled(false);
                    }

                } else {
                    facturaActual = new CFacturas(ini);
                    facturaActual.setListaCProductosPorFactura(lista);
                    facturaActual.setNumeroDeFactura(txtPrefijo.getText().trim() + txtNumeroDeFactura.getText().trim());

                    jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarProductos), false);
                    limpiarCajasDeTexto();
                    habilitarCajasDeTexto(true);
                    btnSiguiente.setEnabled(true);
                    txtIdeCliente.requestFocus();
                }

            } catch (Exception ex) {
                Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtNumeroDeFacturaKeyPressed

    private void txtNitClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNitClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNombreDeCliente.requestFocus();
        }
    }//GEN-LAST:event_txtNitClienteKeyPressed

    private void txtNombreEstablecimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreEstablecimientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreEstablecimientoActionPerformed

    private void txtDescripcionProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDescripcionProductoMouseClicked

    }//GEN-LAST:event_txtDescripcionProductoMouseClicked

    private void txtDescripcionProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            for (CProductos producto : ini.getListaDeProductosCamdun()) {
                if (producto.getDescripcionProducto().equals(txtDescripcionProducto.getText().trim())) {
                    this.producto = producto;

                    this.producto.setCodigoProducto(txtCodigoproducto.getText().trim());
                    llenarDatosProducto();
                    break;
                }
            }
            txtLineaproducto.requestFocus();
        }

    }//GEN-LAST:event_txtDescripcionProductoKeyPressed

    private void txtCantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Double cantidad = Double.parseDouble(txtCantidad.getText().trim());
            Double valor = Double.parseDouble(txtValorUnitarioConIva.getText().trim());

            valor = valor * cantidad;

            txtValorNeto.setText(nf.format(valor));

            txtPesoProducto.requestFocus();

        }
    }//GEN-LAST:event_txtCantidadKeyPressed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        try {
            if (!validarDatosFactura()) {
                JOptionPane.showMessageDialog(this, "la Factura no tiene los datos completos :\n" + mensaje, "Faltan datos", JOptionPane.ERROR_MESSAGE, null);
                mensaje = "";
                return;
            }

            this.cliente.setCodigoInterno(txtIdeCliente.getText().trim());
            this.cliente.setNitCliente(txtNitCliente.getText().trim());
            this.cliente.setNombreEstablecimiento(txtNombreEstablecimiento.getText().trim());
            this.cliente.setNombreDeCliente(txtNombreDeCliente.getText().trim());
            this.cliente.setDireccion(txtDireccion.getText().trim());
            this.cliente.setBarrio(txtBarrio.getText().trim());
            this.cliente.setCiudad(txtCiudad.getText().trim());
            this.cliente.setClasificacion(txtClasificacionCliente.getText().trim());
            this.cliente.setCelularCliente(txtTelefono.getText().trim());
            this.cliente.setFechaDeIngresoCliente(ini.getFechaActualServidor());

            boolean existe = false;
            for (CClientes cli : ini.getListaDeClientes()) {
                if (cli.getCodigoInterno().equals(this.cliente.getCodigoInterno())) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                ini.getListaDeClientes().add(this.cliente);
            }

            autoTxtIdClientes = new TextAutoCompleter(txtIdeCliente);
            for (CClientes cli : ini.getListaDeClientes()) {
                if (cli.getActivoCliente() == 1) {
                    autoTxtIdClientes.addItem(cli.getCodigoInterno() + " ");

                }
            }

            /*Guarda y/o actualiza la informacion del cliente */
            new Thread(new HiloGuardarBBDDRemota(ini, this.cliente.getSentenciaInsertSQL(), "guardar producto")).start();

            /* Se actualiza los datos de la factura*/
            this.facturaActual.setNumeroDeFactura(txtPrefijo.getText().trim() + txtNumeroDeFactura.getText().trim());
            this.facturaActual.setFechaDeVenta("" + ini.getFechaActualServidor());
            this.facturaActual.setCodigoDeCliente(this.cliente.getCodigoInterno());
            this.facturaActual.setNombreDeCliente(cliente.getNombreDeCliente());
            this.facturaActual.setDireccion(this.cliente.getDireccion());
            this.facturaActual.setBarrio(this.cliente.getBarrio());
            this.facturaActual.setCiudad(this.cliente.getCiudad());
            this.facturaActual.setTelefono(this.cliente.getCelularCliente());
            this.facturaActual.setVendedor(txtNombreVendedor.getText().trim());
            this.facturaActual.setFormaDePago("CONTADO");
            this.facturaActual.setCanal(1);
            this.facturaActual.setValorFacturaSinIva(0.0);
            this.facturaActual.setValorIvaFactura(0.0);
            this.facturaActual.setValorTotalFactura(Double.parseDouble(txtValorFactura.getText().trim()));
            this.facturaActual.setValorRechazo(0.0);
            this.facturaActual.setValorDescuento(0.0);
            this.facturaActual.setValorTotalRecaudado(0.0);
            this.facturaActual.setZona(1);
            this.facturaActual.setRegional(1);
            this.facturaActual.setAgencia(1);
            this.facturaActual.setIsFree(1);
            this.facturaActual.setEstadoFactura(1);
            this.facturaActual.setPesofactura(0.0);
            this.facturaActual.setTrasmitido(1);
            this.facturaActual.setNumeroDescuento("0");
            this.facturaActual.setNumeroRecogida("0");

            jTabbedPane1.setEnabled(true);
            jTabbedPane1.setSelectedIndex(1);
            habilitarCajasDeTextoProductos(true);
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);

        } catch (SQLException ex) {
            Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void txtValorUnitarioSinIvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorUnitarioSinIvaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorUnitarioSinIvaActionPerformed

    private void txtValorUnitarioConIvaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorUnitarioConIvaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Double cantidad = Double.parseDouble(txtCantidad.getText().trim());
            Double valor = Double.parseDouble(txtValorUnitarioConIva.getText().trim());

            valor = valor * cantidad;

            txtValorNeto.setText(nf.format(valor));

            txtValorUnitarioSinIva.requestFocus();

        }
    }//GEN-LAST:event_txtValorUnitarioConIvaKeyPressed

    private void txtValorUnitarioConIvaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorUnitarioConIvaFocusGained
        txtValorUnitarioConIva.setSelectionStart(0);
        txtValorUnitarioConIva.setSelectionEnd(txtValorUnitarioConIva.getText().length());
    }//GEN-LAST:event_txtValorUnitarioConIvaFocusGained

    private void txtValorUnitarioSinIvaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorUnitarioSinIvaFocusGained
        txtValorUnitarioSinIva.setSelectionStart(0);
        txtValorUnitarioSinIva.setSelectionEnd(txtValorUnitarioSinIva.getText().length());
    }//GEN-LAST:event_txtValorUnitarioSinIvaFocusGained

    private void txtCantidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCantidadFocusGained
        txtCantidad.setSelectionStart(0);
        txtCantidad.setSelectionEnd(txtCantidad.getText().length());
    }//GEN-LAST:event_txtCantidadFocusGained

    private void txtPesoProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPesoProductoFocusGained
        txtPesoProducto.setSelectionStart(0);
        txtPesoProducto.setSelectionEnd(txtPesoProducto.getText().length());
    }//GEN-LAST:event_txtPesoProductoFocusGained

    private void txtValorNetoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorNetoFocusGained
        txtValorNeto.setSelectionStart(0);
        txtValorNeto.setSelectionEnd(txtValorNeto.getText().length());
    }//GEN-LAST:event_txtValorNetoFocusGained

    private void txtPrefijoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrefijoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNumeroDeFactura.requestFocus();
        }
    }//GEN-LAST:event_txtPrefijoKeyPressed

    private void txtNombreDeClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNombreEstablecimiento.requestFocus();
        }
    }//GEN-LAST:event_txtNombreDeClienteKeyPressed

    private void txtNombreEstablecimientoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreEstablecimientoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtDireccion.requestFocus();
        }
    }//GEN-LAST:event_txtNombreEstablecimientoKeyPressed

    private void txtDireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtBarrio.requestFocus();
        }
    }//GEN-LAST:event_txtDireccionKeyPressed

    private void txtBarrioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtTelefono.requestFocus();
        }
    }//GEN-LAST:event_txtBarrioKeyPressed

    private void txtTelefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCiudad.requestFocus();
        }
    }//GEN-LAST:event_txtTelefonoKeyPressed

    private void txtCiudadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCiudadKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtClasificacionCliente.requestFocus();
        }
    }//GEN-LAST:event_txtCiudadKeyPressed

    private void txtClasificacionClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClasificacionClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNombreVendedor.requestFocus();
        }
    }//GEN-LAST:event_txtClasificacionClienteKeyPressed

    private void txtNombreVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreVendedorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValorFactura.requestFocus();
        }
    }//GEN-LAST:event_txtNombreVendedorKeyPressed

    private void txtLineaproductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLineaproductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValorUnitarioConIva.requestFocus();
        }
    }//GEN-LAST:event_txtLineaproductoKeyPressed

    private void txtValorUnitarioSinIvaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorUnitarioSinIvaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCantidad.requestFocus();
        }
    }//GEN-LAST:event_txtValorUnitarioSinIvaKeyPressed

    private void txtPesoProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesoProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValorNeto.requestFocus();
        }
    }//GEN-LAST:event_txtPesoProductoKeyPressed

    private void txtValorNetoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorNetoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOk.requestFocus();
        }
    }//GEN-LAST:event_txtValorNetoKeyPressed

    private void txtPrefijoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrefijoFocusGained
        txtPrefijo.setSelectionStart(0);
        txtPrefijo.setSelectionEnd(txtPrefijo.getText().length());
    }//GEN-LAST:event_txtPrefijoFocusGained

    private void txtNumeroDeFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaFocusGained
        txtNumeroDeFactura.setSelectionStart(0);
        txtNumeroDeFactura.setSelectionEnd(txtNumeroDeFactura.getText().length());
    }//GEN-LAST:event_txtNumeroDeFacturaFocusGained

    private void txtIdeClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdeClienteFocusGained
//        txtIdeCliente.setSelectionStart(0);
//        txtIdeCliente.setSelectionEnd(txtIdeCliente.getText().length());
    }//GEN-LAST:event_txtIdeClienteFocusGained

    private void txtNitClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNitClienteFocusGained
        txtNitCliente.setSelectionStart(0);
        txtNitCliente.setSelectionEnd(txtNitCliente.getText().length());
    }//GEN-LAST:event_txtNitClienteFocusGained

    private void txtNombreDeClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeClienteFocusGained
//        txtNombreDeCliente.setSelectionStart(0);
//        txtNombreDeCliente.setSelectionEnd(txtNombreDeCliente.getText().length());
    }//GEN-LAST:event_txtNombreDeClienteFocusGained

    private void txtNombreEstablecimientoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreEstablecimientoFocusGained
        txtNombreEstablecimiento.setSelectionStart(0);
        txtNombreEstablecimiento.setSelectionEnd(txtNombreEstablecimiento.getText().length());
    }//GEN-LAST:event_txtNombreEstablecimientoFocusGained

    private void txtDireccionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionFocusGained
        txtDireccion.setSelectionStart(0);
        txtDireccion.setSelectionEnd(txtDireccion.getText().length());
    }//GEN-LAST:event_txtDireccionFocusGained

    private void txtBarrioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarrioFocusGained
        txtBarrio.setSelectionStart(0);
        txtBarrio.setSelectionEnd(txtBarrio.getText().length());
    }//GEN-LAST:event_txtBarrioFocusGained

    private void txtTelefonoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoFocusGained
        txtTelefono.setSelectionStart(0);
        txtTelefono.setSelectionEnd(txtTelefono.getText().length());
    }//GEN-LAST:event_txtTelefonoFocusGained

    private void txtCiudadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCiudadFocusGained
//        txtCiudad.setSelectionStart(0);
//        txtCiudad.setSelectionEnd(txtCiudad.getText().length());
    }//GEN-LAST:event_txtCiudadFocusGained

    private void txtClasificacionClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClasificacionClienteFocusGained
//        txtClasificacionCliente.setSelectionStart(0);
//        txtClasificacionCliente.setSelectionEnd(txtClasificacionCliente.getText().length());
    }//GEN-LAST:event_txtClasificacionClienteFocusGained

    private void txtNombreVendedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreVendedorFocusGained
//        txtNombreVendedor.setSelectionStart(0);
//        txtNombreVendedor.setSelectionEnd(txtNombreVendedor.getText().length());
    }//GEN-LAST:event_txtNombreVendedorFocusGained

    private void txtValorFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorFacturaFocusGained
        txtValorFactura.setSelectionStart(0);
        txtValorFactura.setSelectionEnd(txtValorFactura.getText().length());
    }//GEN-LAST:event_txtValorFacturaFocusGained

    private void txtCodigoproductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoproductoFocusGained
//        txtCodigoproducto.setSelectionStart(0);
//        txtCodigoproducto.setSelectionEnd(txtCodigoproducto.getText().length());

    }//GEN-LAST:event_txtCodigoproductoFocusGained

    private void txtDescripcionProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDescripcionProductoFocusGained
//        txtDescripcionProducto.setSelectionStart(0);
//        txtDescripcionProducto.setSelectionEnd(txtDescripcionProducto.getText().length());
    }//GEN-LAST:event_txtDescripcionProductoFocusGained

    private void txtLineaproductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLineaproductoFocusGained
//        txtLineaproducto.setSelectionStart(0);
//        txtLineaproducto.setSelectionEnd(txtLineaproducto.getText().length());
    }//GEN-LAST:event_txtLineaproductoFocusGained

    private void txtValorFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorFacturaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSiguiente.requestFocus();
        }
    }//GEN-LAST:event_txtValorFacturaKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu borraElementos;
    private javax.swing.JMenuItem borrar1Fila;
    private javax.swing.JMenuItem borrarTodo;
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JToggleButton jBtnBorrarFIla;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnImprimir;
    private javax.swing.JToggleButton jBtnMinuta;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JButton jBtnNuevo1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableFacturasPorManifiesto;
    private javax.swing.JTable jTableProductosPorFactura;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblPesoDeLaFactura;
    private javax.swing.JLabel lblValorFactura;
    private javax.swing.JPopupMenu mnuBorrarFilas;
    public javax.swing.JPanel pnlAgregarFactura;
    public javax.swing.JPanel pnlAgregarProductos;
    private javax.swing.JTable tablaDocsVencidos;
    public javax.swing.JTextField txtBarrio;
    private javax.swing.JTextField txtCantidad;
    public javax.swing.JTextField txtCiudad;
    public javax.swing.JTextField txtClasificacionCliente;
    private javax.swing.JTextField txtCodigoproducto;
    private javax.swing.JTextField txtDescripcionProducto;
    public javax.swing.JTextField txtDireccion;
    public javax.swing.JTextField txtIdeCliente;
    private javax.swing.JTextField txtLineaproducto;
    public javax.swing.JTextField txtNitCliente;
    public javax.swing.JTextField txtNombreDeCliente;
    public javax.swing.JTextField txtNombreEstablecimiento;
    public javax.swing.JTextField txtNombreVendedor;
    public javax.swing.JTextField txtNumeroDeFactura;
    private javax.swing.JTextField txtPesoProducto;
    public javax.swing.JTextField txtPrefijo;
    public javax.swing.JTextField txtTelefono;
    public javax.swing.JTextField txtValorFactura;
    private javax.swing.JTextField txtValorNeto;
    private javax.swing.JTextField txtValorUnitarioConIva;
    private javax.swing.JTextField txtValorUnitarioSinIva;
    // End of variables declaration//GEN-END:variables

    public void nuevo() {
        try {
            cancelar();
            nuevo = true;
            limpiar();

            //jTabbedPane1.setEnabled(true);
            jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarFactura), true);
            jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarProductos), false);

            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);

            formaDePago = "CONTADO"; // contado

            facturaActual = null;

            carro = null;
            manifiestoActual = null;
            listaDeAuxiliares = new ArrayList();

            //listaFacturasPorManifiesto = new ArrayList(); // CfacturasPorManifiesto
            listaDeFacturasEnElArchivo = new ArrayList();
            manifiestoActual = new CManifiestosDeDistribucion(ini);
            manifiestoActual.setEstadoManifiesto(0);

            lblPesoDeLaFactura.setText("0.0 Kg");

            txtPrefijo.setEnabled(true);
            txtNumeroDeFactura.setEnabled(true);
            txtIdeCliente.setEnabled(true);
            txtNombreDeCliente.setEnabled(true);
            txtDireccion.setEnabled(true);
            txtBarrio.setEnabled(true);
            txtTelefono.setEnabled(true);
            txtCiudad.setEnabled(true);
            txtNombreVendedor.setEnabled(true);
            txtValorFactura.setEnabled(true);

            txtPrefijo.setEditable(true);
            txtNumeroDeFactura.setEditable(true);
            txtIdeCliente.setEditable(true);
            txtNombreDeCliente.setEditable(true);
            txtDireccion.setEditable(true);
            txtBarrio.setEditable(true);
            txtTelefono.setEditable(true);
            txtCiudad.setEditable(true);
            txtNombreVendedor.setEditable(true);
            txtValorFactura.setEditable(true);

        } catch (Exception ex) {
            Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean grabar() throws HeadlessException {
        int deseaGrabarRegistro;
        /*valida sin pendientes del conductor */
        if (false) {
            // if (manifiestoActual.getCantidadManifiestosPendientesConductor() > 0) {
            JOptionPane.showMessageDialog(this, "El conductor tiene pendientes de ruta,\n no se puede grabar este manifiesto", "Conductor no esta paz y salvo", JOptionPane.ERROR_MESSAGE, null);
            return true;
        }
        if (manifiestoActual.getEstadoManifiesto() == 2) {  // IINICIO DEL IF-> ESTADO DEL MANIFIESTO

            deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

            if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {

                grabarFactura();

                //
            }
        } else {
            btnGrabar.setEnabled(false);
            jBtnGrabar.setEnabled(false);
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
            } else {
                /*Manifiesto grabado en la BBDD , trae registros desde allí*/
                manifiestoActual.setListaFacturasPorManifiesto();
            }


            /* Genera el manifiesto  Genera el rutero*/
            //ReporteFacturasEnDistribucion1 demo = new ReporteFacturasEnDistribucion1(ini, manifiestoActual);
            //reporteSalidaADistribucion demo = new reporteSalidaADistribucion(ini, manifiestoActual);
            //RepporteRuteroConductores demo = new RepporteRuteroConductores(ini, manifiestoActual);
            RepporteRuteroConductoresConPesosyDEscuentos demo = new RepporteRuteroConductoresConPesosyDEscuentos(ini, manifiestoActual);

        } catch (Exception ex) {
            Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cancelar() {
        limpiar();

        nuevo = false;

        jTabbedPane1.setEnabled(false);

        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);

        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);
        jBtnMinuta.setEnabled(false);

        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);

        jBtnBorrarFIla.setEnabled(false);

        grabar = false;

        if (manifiestoActual != null) {
            manifiestoActual.liberarManifiesto(true);
            liberado = true;

        }
        txtNumeroDeFactura.setEnabled(false);
        txtNumeroDeFactura.setEditable(false);

        txtNombreDeCliente.setEditable(false);
        txtBarrio.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtNombreVendedor.setEditable(false);

        txtNumeroDeFactura.requestFocus();
        txtNumeroDeFactura.requestFocus();

        manifiestoActual = null;
        listaDeAuxiliares = null;

        // listaFacturasPorManifiesto = null;
        valorTotalManifiesto = 0.0;
        formaDePago = "CONTADO";

        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png")));
        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png")));

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

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

    private void habilitar(boolean valor) {
        try {
            jTabbedPane1.setEnabled(valor);

            txtNombreDeCliente.setEditable(valor);
            txtTelefono.setEditable(valor);
            txtNombreVendedor.setEditable(valor);

            //dateVencimiento.setEnabled(valor);
            jTableFacturasPorManifiesto.setEnabled(valor);
            tablaDocsVencidos.setEnabled(valor);

        } catch (Exception ex) {
        }
    }

//    public boolean validar() {
//        boolean verificado = true;
//        mensaje = "";
//
//        return verificado;
//    }
    private boolean validarDatosFactura() {
        boolean validado = true;
        mensaje = "";
        try {
            if (txtNumeroDeFactura.getText().isEmpty()) {
                mensaje += "No ha introducido el número de la factura. \n";
                validado = false;
            } else {
                facturaActual.setNumeroDeFactura(txtPrefijo.getText().trim() + txtNumeroDeFactura.getText().trim());
            }

            if (txtIdeCliente.getText().isEmpty()) {
                mensaje += "No ha introducido el id del cliente. \n";
                validado = false;
            } else {
                facturaActual.setCodigoDeCliente(txtIdeCliente.getText().trim());
                cliente.setCodigoInterno(txtIdeCliente.getText().trim());
            }

            if (txtNombreDeCliente.getText().isEmpty()) {
                mensaje += "No ha introducido el nombre del cliente. \n";
                validado = false;
            } else {
                facturaActual.setCodigoDeCliente(txtNombreDeCliente.getText().trim());
                cliente.setNombreDeCliente(txtNombreDeCliente.getText().trim());
            }

            if (txtDireccion.getText().isEmpty()) {
                mensaje += "No ha introducido la direccion del cliente. \n";
                validado = false;
            } else {
                facturaActual.setDireccion(txtDireccion.getText().trim());
                cliente.setDireccion(txtDireccion.getText().trim());
            }

            if (txtBarrio.getText().isEmpty()) {
                mensaje += "No ha introducido el barrio del cliente. \n";
                validado = false;
            } else {
                facturaActual.setBarrio(txtBarrio.getText().trim());
                cliente.setBarrio(txtBarrio.getText().trim());
            }

            if (txtTelefono.getText().isEmpty()) {
                mensaje += "No ha introducido el telefono del cliente. \n";
                validado = false;
            } else {
                facturaActual.setTelefono(txtTelefono.getText().trim());
                cliente.setCelularCliente(txtTelefono.getText().trim());
            }
            if (txtCiudad.getText().isEmpty()) {
                mensaje += "No ha introducido la ciudad. \n";
                validado = false;
            } else {
                facturaActual.setCiudad(txtCiudad.getText().trim());
                cliente.setCiudad(txtCiudad.getText().trim());
            }
            if (txtNombreVendedor.getText().isEmpty()) {
                mensaje += "No ha introducido el nombre del vendedor. \n";
                validado = false;
            } else {
                facturaActual.setVendedor(txtNombreVendedor.getText().trim());

            }

            if (txtValorFactura.getText().isEmpty()) {
                mensaje += "No ha introducido valor de la factura. \n";
                validado = false;
            } else {
                facturaActual.setValorTotalFactura(Double.parseDouble(txtValorFactura.getText().trim()));

            }
        } catch (Exception ex) {
            validado = false;
            mensaje += ex.toString();
        }
        facturaVerificada = validado;
        return validado;
    }

    private boolean validarDatosProducto() {
        boolean validado = true;
        mensaje = "";
        try {
            if (txtCodigoproducto.getText().isEmpty()) {
                mensaje += "No ha introducido Codigo del Producot. \n";
                validado = false;
            }

            if (txtDescripcionProducto.getText().isEmpty()) {
                mensaje += "No ha icolocado la desripcion del producto. \n";
                validado = false;
            }

            if (txtLineaproducto.getText().isEmpty()) {
                mensaje += "No ha colocado la Linea del producto. \n";
                validado = false;
            }

            if (txtValorUnitarioConIva.getText().isEmpty() || "0.0".equals(txtValorUnitarioConIva.getText())) {
                mensaje += "No ha colocado el valor unitario con iva del producto. \n";
                validado = false;
            }

            if (txtValorUnitarioSinIva.getText().isEmpty() || "0.0".equals(txtValorUnitarioSinIva.getText())) {
                mensaje += "No ha colocado el valor unitario sin iva del producto. \n";
                validado = false;
            }

            if (txtCantidad.getText().isEmpty() || "0.0".equals(txtCantidad.getText())) {
                mensaje += "No ha colocado la cantidad de producto. \n";
                validado = false;
            }

            if (txtPesoProducto.getText().isEmpty() || "0.0".equals(txtPesoProducto.getText())) {
                mensaje += "No ha colocado el peso el producto \n";
                validado = false;
            }

            if (txtValorNeto.getText().isEmpty() || "0.0".equals(txtValorNeto.getText())) {
                mensaje += "No ha colocado el valor neto del producto \n";
                validado = false;
            }

        } catch (Exception ex) {
            validado = false;
            mensaje += ex.toString();
        }
        facturaVerificada = validado;
        return validado;
    }

    private void limpiar() {

        try {
            //cbxPlacas.setSelectedIndex(0);
            //cbxManifiestosDeDistribucion.setSelectedIndex(0);

            DefaultTableModel model;
            model = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
            if (model.getRowCount() > 0) {
                int a = model.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    model.removeRow(i);
                }
            }
            limpiarTablaProductosPorFactura();

        } catch (Exception ex) {
        }
        txtNumeroDeFactura.setText("");
        txtNumeroDeFactura.setText("");
        txtNombreDeCliente.setText("");
        txtBarrio.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtNombreVendedor.setText("");

        pnlAgregarProductos.setEnabled(true);

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

    }

    public void limpiarTablaProductosPorFactura() {
        DefaultTableModel model;
        model = (DefaultTableModel) jTableProductosPorFactura.getModel();
        if (model.getRowCount() > 0) {
            int a = model.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                model.removeRow(i);
            }
        }
    }

    private void limpiarDocumentoAgregado() {
        txtNumeroDeFactura.setText("");
        txtNombreDeCliente.setText("");
        txtTelefono.setText("");
        txtNombreVendedor.setText("");
//        txtRutaDelArchivo.setText("");
//        dateExpedicion.setDate(new Date());
//        dateVencimiento.setDate(new Date());
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
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtBarrio.setText("");
        txtNombreVendedor.setText("");
    }

    private void ordenarTabla() {
        for (int i = 0; i < jTableFacturasPorManifiesto.getRowCount(); i++) {
            jTableFacturasPorManifiesto.setValueAt(i + 1, i, 0);
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

        btnGrabar.setEnabled(false);

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
            Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llenarDatosCliente() {
        txtNombreDeCliente.setText(this.cliente.getNombreDeCliente());
        txtNitCliente.setText(this.cliente.getNitCliente());
        txtNombreEstablecimiento.setText(this.cliente.getNombreEstablecimiento());
        txtDireccion.setText(this.cliente.getDireccion());
        txtBarrio.setText(this.cliente.getBarrio());
        txtTelefono.setText(this.cliente.getCelularCliente());
        txtDireccion.setText(this.cliente.getDireccion());
        txtCiudad.setText(this.cliente.getCiudad());
        txtClasificacionCliente.setText(this.cliente.getClasificacion());

    }

    private void llenarDatosProducto() {

        txtDescripcionProducto.setText(this.producto.getDescripcionProducto());
        txtLineaproducto.setText(this.producto.getLinea());
        txtValorUnitarioConIva.setText("" + this.producto.getValorUnitarioConIva());
        txtValorUnitarioSinIva.setText("" + this.producto.getValorUnitarioSinIva());
        txtPesoProducto.setText("" + this.producto.getPesoProducto());

    }

    private void agregarProductoALaTabla() {
        try {
            modelo2 = (DefaultTableModel) jTableProductosPorFactura.getModel();

            /* llama las propiedades del producto y las guarda en la BBDD*/
            double valor = 0.0;
            this.producto.setCodigoProducto(txtCodigoproducto.getText().trim());
            this.producto.setDescripcionProducto(txtDescripcionProducto.getText().trim());
            this.producto.setLinea(txtLineaproducto.getText().trim());
            this.producto.setValorUnitarioConIva(Double.parseDouble(txtValorUnitarioConIva.getText().trim()));
            this.producto.setValorUnitarioSinIva(Double.parseDouble(txtValorUnitarioSinIva.getText().trim()));
            this.producto.setPesoProducto(Double.parseDouble(txtPesoProducto.getText().trim()));
            this.producto.setIsFree(1);
            this.producto.setActivo(1);

            new Thread(new HiloGuardarBBDDRemota(ini, producto.getSentenciaInsertSQL(), "guardar producto")).start();

            /* Asigna el detalle de la factura */
            CProductosPorFactura productoPorFactura = new CProductosPorFactura(ini);
            productoPorFactura.setNumeroFactura(txtPrefijo.getText().trim() + txtNumeroDeFactura.getText().trim());
            productoPorFactura.setCodigoProducto(txtCodigoproducto.getText().trim());
            productoPorFactura.setDescripcionProducto(txtDescripcionProducto.getText().trim());
            productoPorFactura.setLinea(txtLineaproducto.getText().trim());
            productoPorFactura.setCantidad(Double.parseDouble(txtCantidad.getText().trim()));
            productoPorFactura.setValorUnitarioConIva(producto.getValorUnitarioConIva());
            productoPorFactura.setValorUnitarioSinIva(producto.getValorUnitarioSinIva());

            productoPorFactura.setPesoProducto(producto.getPesoProducto() * productoPorFactura.getCantidad());
            productoPorFactura.setActivo(1);

//            boolean existe = false;
//            for (CProductos pro : ini.getListaDeProductosCamdun()) {
//                if (pro.getCodigoProducto().equals(txtCodigoproducto.getText().trim())) {
//                    existe = true;
//                    break;
//                }
//            }
//          
            //ini.getListaDeProductosCamdun().add(this.producto);
            this.autoTxtCodigoProducto = new TextAutoCompleter(txtCodigoproducto);
            this.autoTxtDescripcionDeProducto = new TextAutoCompleter(txtDescripcionProducto);

            for (CProductos producto : this.ini.getListaDeProductosCamdun()) {
                if (producto.getActivo() == 1) {
                    autoTxtCodigoProducto.addItem(producto.getCodigoProducto() + " ");
                    autoTxtDescripcionDeProducto.addItem(producto.getDescripcionProducto() + " ");
                }
            }

            /*Llena la tabla del formulario productos de la factura*/ filaTabla2 = jTableProductosPorFactura.getRowCount();
            modelo2.addRow(new Object[jTableProductosPorFactura.getRowCount()]);

            jTableProductosPorFactura.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item
            jTableProductosPorFactura.setValueAt(txtCodigoproducto.getText().trim(), filaTabla2, 1); // numero de la factura

            jTableProductosPorFactura.setValueAt(txtDescripcionProducto.getText().trim(), filaTabla2, 2); // cliente

            jTableProductosPorFactura.setValueAt(Double.parseDouble(txtCantidad.getText().trim()), filaTabla2, 3); // valor a recaudar  de la factura

            jTableProductosPorFactura.setValueAt(nf.format(Double.parseDouble(txtValorUnitarioConIva.getText().trim())), filaTabla2, 4); // valor a recaudar  de la factura

            valor = Double.parseDouble(txtValorUnitarioConIva.getText().trim()) * Double.parseDouble(txtCantidad.getText().trim());

            txtValorNeto.setText(nf.format(valor));
            jTableProductosPorFactura.setValueAt(nf.format(valor), filaTabla2, 5); // valor a recaudar  de la factura

            facturaActual.getListaCProductosPorFactura().add(productoPorFactura);
            valor = 0.0;
            double valorFactura = 0.0;

            for (CProductosPorFactura prod : facturaActual.getListaCProductosPorFactura()) {
                valor += prod.getPesoProducto();
                valorFactura += prod.getValorProductoXCantidad();
            }
            facturaActual.setPesofactura(valor);
            lblPesoDeLaFactura.setText((valor / 1000) + " Kg");
            lblValorFactura.setText(nf.format(valorFactura));

            // valorTotalManifiesto += obj.getValorARecaudarFactura();
            // se ubica en la fila insertada
            jTableProductosPorFactura.changeSelection(filaTabla2, 0, false, false);
        } catch (Exception ex) {
            Logger.getLogger(FCrearFacturasDeVenta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void lipiarCamposDeTexto() {
        txtCodigoproducto.setText("");
        txtDescripcionProducto.setText("");
        txtLineaproducto.setText("");
        txtValorUnitarioConIva.setText("0.0");
        txtValorUnitarioSinIva.setText("0.0");
        txtCantidad.setText("0.0");
        txtPesoProducto.setText("0.0");
        txtValorNeto.setText("0.0");
        txtCodigoproducto.requestFocus();
    }

    public void habilitarCajasDeTexto(boolean valor) {

        txtNumeroDeFactura.setEnabled(valor);
        txtBarrio.setEnabled(valor);
        txtDireccion.setEnabled(valor);
        txtNombreDeCliente.setEnabled(valor);
        txtTelefono.setEnabled(valor);
        txtCiudad.setEnabled(valor);
        txtClasificacionCliente.setEnabled(valor);
        txtIdeCliente.setEnabled(valor);
        txtNitCliente.setEnabled(valor);
        txtNombreEstablecimiento.setEnabled(valor);
        txtValorFactura.setEnabled(valor);
        txtPrefijo.setEnabled(valor);
        txtNombreVendedor.setEnabled(valor);

        txtNumeroDeFactura.setEditable(valor);
        txtBarrio.setEditable(valor);
        txtDireccion.setEditable(valor);
        txtNombreDeCliente.setEditable(valor);
        txtTelefono.setEditable(valor);
        txtCiudad.setEditable(valor);
        txtClasificacionCliente.setEditable(valor);
        txtIdeCliente.setEditable(valor);
        txtNitCliente.setEditable(valor);
        txtNombreEstablecimiento.setEditable(valor);
        txtValorFactura.setEditable(valor);
        txtPrefijo.setEditable(valor);
        txtNombreVendedor.setEditable(valor);
    }

    private void limpiarCajasDeTexto() {

        txtBarrio.setText("");
        txtDireccion.setText("");
        txtNombreDeCliente.setText("");
        txtTelefono.setText("");
        txtCiudad.setText("");
        txtClasificacionCliente.setText("");
        txtIdeCliente.setText("");
        txtNitCliente.setText("");
        txtNombreEstablecimiento.setText("");
        txtValorFactura.setText("");
        txtNombreVendedor.setText("");
        txtNumeroDeFactura.requestFocus();
    }

    private void llenarCajasDeTexto() {

        txtBarrio.setText(facturaActual.getBarrio());
        txtDireccion.setText(facturaActual.getDireccionDeCliente());
        txtNombreDeCliente.setText(facturaActual.getNombreDeCliente());
        txtTelefono.setText(facturaActual.getTelefonoCliente());
        txtCiudad.setText(facturaActual.getCiudad());
        txtClasificacionCliente.setText(facturaActual.getClasificacionCliente());
        txtIdeCliente.setText(facturaActual.getCodigoDeCliente());
        txtNitCliente.setText(facturaActual.getNitCliente());
        txtNombreEstablecimiento.setText(facturaActual.getNombreEstablecimiento());
        txtValorFactura.setText("" + facturaActual.getValorTotalFactura());
        txtNombreVendedor.setText(facturaActual.getVendedor());
        txtNumeroDeFactura.requestFocus();
    }

    private void habilitarCajasDeTextoProductos(boolean valor) {
        txtCodigoproducto.setEnabled(valor);
        txtDescripcionProducto.setEnabled(valor);
        txtLineaproducto.setEnabled(valor);
        txtValorUnitarioConIva.setEnabled(valor);
        txtValorUnitarioSinIva.setEnabled(valor);
        txtCantidad.setEnabled(valor);
        txtPesoProducto.setEnabled(valor);
        txtValorNeto.setEnabled(valor);

        txtCodigoproducto.setEditable(valor);
        txtDescripcionProducto.setEditable(valor);
        txtLineaproducto.setEditable(valor);
        txtValorUnitarioConIva.setEditable(valor);
        txtValorUnitarioSinIva.setEditable(valor);
        txtCantidad.setEditable(valor);
        txtPesoProducto.setEditable(valor);
        txtValorNeto.setEditable(valor);

    }

    private void limpiarCajasDeTextoProducto() {
        txtCodigoproducto.setText("");
        txtDescripcionProducto.setText("");
        txtLineaproducto.setText("");
        txtValorUnitarioConIva.setText("0.0");
        txtValorUnitarioSinIva.setText("0.0");
        txtCantidad.setText("0.0");
        txtPesoProducto.setText("0.0");
        txtValorNeto.setText("0.0");
    }

    private boolean grabarFactura() {
        boolean grabado = false;
        try {
            List<String> lista = new ArrayList<>();

            Double pesoFactura = 0.0;
            Double valorSinIva = 0.0;
            for (CProductosPorFactura prodXfact : facturaActual.getListaCProductosPorFactura()) {

                CProductos producto = new CProductos(ini);
                producto.setCodigoProducto(prodXfact.getCodigoProducto());
                producto.setDescripcionProducto(prodXfact.getDescripcionProducto());
                producto.setLinea(prodXfact.getLinea());
                producto.setValorUnitarioConIva(prodXfact.getValorUnitarioConIva());
                producto.setValorUnitarioSinIva(prodXfact.getValorUnitarioSinIva());

                producto.setIsFree(1);
                producto.setPesoProducto(prodXfact.getPesoProducto() / prodXfact.getCantidad());
                producto.setLargoProducto(0.0);
                producto.setAnchoProducto(0.0);
                producto.setAltoProducto(0.0);

                lista.add(producto.getSentenciaInsertSQL());
                lista.add(prodXfact.getSentenciaInsertSQL());
                pesoFactura += prodXfact.getPesoProducto();
                valorSinIva += prodXfact.getValorUnitarioSinIva();
            }

            facturaActual.setPesofactura(pesoFactura);
            facturaActual.setValorFacturaSinIva(valorSinIva);
            Double valor = facturaActual.getValorTotalFactura() - facturaActual.getValorFacturaSinIva();
            facturaActual.setValorIvaFactura(valor);

            new Thread(new HiloGuardarBBDDRemota(ini, facturaActual.getSentenciaInsertSQL(), "guardar Factura")).start();

            /*Agrega la factura  a la BBDD*/
            ini.insertarDatosRemotamente(facturaActual.getSentenciaInsertSQL(), "Fmanifestarpedidospoblaciones");

            /*Agrega el detalle de la factura  a la BBDD*/
            ini.insertarDatosRemotamente(lista, "Fmanifestarpedidospoblaciones");

            limpiarCajasDeTexto();
            limpiarTablaProductosPorFactura();

            limpiarCajasDeTextoProducto();
            habilitarCajasDeTextoProductos(false);

            jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarProductos), false);

            btnSiguiente.setEnabled(false);

            btnNuevo.setEnabled(true);
            jBtnNuevo.setEnabled(true);

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosPoblaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

}
