/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas;

import aplicacionlogistica.distribucion.Threads.HiloListadoConsultadeFacturaBitacora;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CBitacoraFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFacturaDescargados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import ui.swing.tableCustom.TableCustom;

/**
 *
 * @author VLI_488
 */
public class FConsultarFacturasRemoto extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    CUsuarios user;

    Inicio ini = null;

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    DefaultTableModel modelo1, modelo2;

    Double valorTotalAConsignar = 0.0;
    boolean cargado = false;
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

    JInternalFrame form;

    FConsultarFacturasPorCliente fconsultarfacturasPorCliente;

    public CFacturas factura;

//    public ArrayList<Vst_ProductosPorFactura> listaProductosPorFactura;
//    public ArrayList<Vst_FacturasPorManifiesto> listaDeMovimientosEnDistribucion;
//    public ArrayList<CBitacoraFacturas> listaDeMovimientosBitacora;
//    public ArrayList<Vst_ProductosPorFacturaDescargados> listaDeProductosRechazados;
    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     */
    public FConsultarFacturasRemoto(Inicio ini) throws Exception {
        try {
            initComponents();
           // getContentPane().setBackground(Color.WHITE);
            
            setFormatoTablas();

            lblCirculoDeProgreso.setVisible(false);
            this.ini = ini;
            this.user = ini.getUser();
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
            Logger.getLogger(FConsultarFacturasRemoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @param form formulario desde donde se carga la interface gráfica
     * @param factura corrresponde a la factura que se va a consultar
     * @throws java.lang.Exception
     */
    public FConsultarFacturasRemoto(Inicio ini, JInternalFrame form, CFacturas factura) throws Exception {
        this.factura = factura;
        this.form = form;

        try {
            initComponents();
             setFormatoTablas();
            this.ini = ini;
            llenarDatosDeLaVista(this.factura);

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FConsultarFacturasRemoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    public FConsultarFacturasRemoto(Inicio ini, CFacturas factura) throws Exception {
        this.factura = factura;
        try {
            initComponents();
            setFormatoTablas();
            this.ini = ini;
            this.txtNumeroFactura.setText(factura.getNumeroDeFactura());
            // llenarDatosDeLaVista(this.factura);
            new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FConsultarFacturasRemoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    private void setFormatoTablas() {
        TableCustom.apply(jScrollEntregas, TableCustom.TableType.MULTI_LINE);
        TableCustom.apply(jScrollMovimientos, TableCustom.TableType.MULTI_LINE);
        TableCustom.apply(jScrollDistribucuon, TableCustom.TableType.MULTI_LINE);
        TableCustom.apply(jScrollParciales, TableCustom.TableType.MULTI_LINE);
        TableCustom.apply(jScrollDescuentos, TableCustom.TableType.MULTI_LINE);
        TableCustom.apply(jScrollRecogidas, TableCustom.TableType.MULTI_LINE);
    }

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @param fconsultarfacturasPorCliente
     * @param factura corrresponde a la factura que se va a consultar
     * @throws java.lang.Exception
     */
    public FConsultarFacturasRemoto(Inicio ini, FConsultarFacturasPorCliente fconsultarfacturasPorCliente, CFacturas factura) throws Exception {
        this.factura = factura;
        this.fconsultarfacturasPorCliente = fconsultarfacturasPorCliente;
        this.ini = ini;
        try {
            initComponents();
            setFormatoTablas();
            jBtnNuevo.setEnabled(false);
            jBtnCancel.setEnabled(false);
            txtNumeroFactura.setText(factura.getNumeroDeFactura());
            // llenarDatosDeLaVista(this.factura);
            new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FConsultarFacturasRemoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    public FConsultarFacturasRemoto(Inicio ini, JInternalFrame form, String numeroFactura) throws Exception {

        try {
            initComponents();
            setFormatoTablas();
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
                /*Hilo que recupera los movimientos de la factura */
                new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this.factura)).start();
                llenarDatosDeLaVista(this.factura);
            } else {
                JOptionPane.showMessageDialog(null, "Número de Factura no encontrado en el sistema ", "Error", JOptionPane.WARNING_MESSAGE);

            }

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FConsultarFacturasRemoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    private void llenarDatosDeLaVista(CFacturas factura1) throws Exception {

        limpiarTodo();
        this.factura = factura1;
        this.factura.setListaDetalleFactura(true);

        this.factura.setListaDeMovimientosfactura();
        this.factura.setListaDeMovimientosBitacora();
        this.factura.setListaDeProductosRechazados();

        lblValorRecaudado.setText(nf.format(factura.getValorTotalRecaudado()));


        /*Hilo que recupera los movimientos de la factura */
        // new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this.factura)).start();
        txtNumeroFactura.setText(factura.getNumeroDeFactura());
        txtDireccionDelCliente.setText(factura.getDireccionDeCliente());
        txtBarrioCliente.setText(factura.getBarrio());
        txtFechaVenta.setText("" + factura.getFechaDeVenta());

        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText(factura.getNombreDeCliente() + " (" + factura.getCodigoDeCliente() + ")");
        txtNombreDelVendedor.setText(factura.getVendedor());
        txtTelefonoDelCliente.setText(factura.getTelefonoCliente());
        llenarTablaProductosPorFactura();
        llenarTablaDistribucion();
        llenarTablabitacora();
        llenarTablaProductosRechazados();

        System.out.println("Aca verifica que hayan movimientos en la factura " + factura.getNumeroDeFactura());

        completado = false;

        lblCirculoDeProgreso.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pumImagen = new javax.swing.JPopupMenu();
        agregarImagen = new javax.swing.JMenu();
        borrarFila = new javax.swing.JPopupMenu();
        borraUnaFila = new javax.swing.JMenu();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaDocsVencidos = new javax.swing.JTable();
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
        lblPrecioFactura2 = new javax.swing.JLabel();
        lblValorCobrado = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtDireccionDelCliente = new javax.swing.JTextField();
        lblValorRecaudado = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlEntregas = new javax.swing.JPanel();
        jScrollEntregas = new javax.swing.JScrollPane();
        tblProductosPorFactura = new javax.swing.JTable();
        pnlMovimientos = new javax.swing.JPanel();
        jScrollMovimientos = new javax.swing.JScrollPane();
        tblBitacora = new javax.swing.JTable();
        pnlDistribucion = new javax.swing.JPanel();
        jScrollDistribucuon = new javax.swing.JScrollPane();
        tblMovimientos = new javax.swing.JTable();
        pnlRechazosParciales = new javax.swing.JPanel();
        jScrollParciales = new javax.swing.JScrollPane();
        tblProductosPorFacturaRechazados = new javax.swing.JTable();
        pnlDescuentos = new javax.swing.JPanel();
        jScrollDescuentos = new javax.swing.JScrollPane();
        tblDescuentos = new javax.swing.JTable();
        pnlRecogidas = new javax.swing.JPanel();
        jScrollRecogidas = new javax.swing.JScrollPane();
        tblRecogidas = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        txtNumeroDeSoporte = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtFacturaAfectada = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtValorRecogida = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtValorDescontado = new javax.swing.JTextField();
        lblValorTotalFactura = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExcel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();

        agregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Display 16x16.png"))); // NOI18N
        agregarImagen.setText("Agregar Imagen Empleado");
        agregarImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarImagenMouseClicked(evt);
            }
        });
        pumImagen.add(agregarImagen);

        borraUnaFila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delete.png"))); // NOI18N
        borraUnaFila.setText("Borra una Fila");
        borraUnaFila.setToolTipText("");
        borraUnaFila.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                borraUnaFilaMouseClicked(evt);
            }
        });
        borraUnaFila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borraUnaFilaActionPerformed(evt);
            }
        });
        borrarFila.add(borraUnaFila);

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
        setResizable(true);
        setTitle("Formulario para consulta de facturas");
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

        lblPrecioFactura2.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        lblPrecioFactura2.setText("Total Recaudado :");

        lblValorCobrado.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        lblValorCobrado.setText("$0.0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblValorCobrado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPrecioFactura2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPrecioFactura2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblValorCobrado)
                .addContainerGap(31, Short.MAX_VALUE))
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
                .addGap(5, 5, 5)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBarrioCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addComponent(txtTelefonoDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDelVendedor)
                    .addComponent(txtNombreDelCliente)
                    .addComponent(txtDireccionDelCliente))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDireccionDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBarrioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreDelVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lblValorRecaudado.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblValorRecaudado.setText("$.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblValorRecaudado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(lblValorRecaudado)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jScrollEntregas.setViewportView(tblProductosPorFactura);
        if (tblProductosPorFactura.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFactura.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblProductosPorFactura.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(125);
            tblProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        javax.swing.GroupLayout pnlEntregasLayout = new javax.swing.GroupLayout(pnlEntregas);
        pnlEntregas.setLayout(pnlEntregasLayout);
        pnlEntregasLayout.setHorizontalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollEntregas, javax.swing.GroupLayout.PREFERRED_SIZE, 848, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        pnlEntregasLayout.setVerticalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jScrollEntregas, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jTabbedPane1.addTab("Productos", pnlEntregas);

        tblBitacora.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "item", "fecha Ing.", "fecha", "documento", "causal-obs."
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
        jScrollMovimientos.setViewportView(tblBitacora);
        tblBitacora.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblBitacora.getColumnModel().getColumnCount() > 0) {
            tblBitacora.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblBitacora.getColumnModel().getColumn(1).setPreferredWidth(170);
            tblBitacora.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblBitacora.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblBitacora.getColumnModel().getColumn(4).setPreferredWidth(350);
        }

        javax.swing.GroupLayout pnlMovimientosLayout = new javax.swing.GroupLayout(pnlMovimientos);
        pnlMovimientos.setLayout(pnlMovimientosLayout);
        pnlMovimientosLayout.setHorizontalGroup(
            pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollMovimientos, javax.swing.GroupLayout.DEFAULT_SIZE, 885, Short.MAX_VALUE)
        );
        pnlMovimientosLayout.setVerticalGroup(
            pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollMovimientos, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
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
        jScrollDistribucuon.setViewportView(tblMovimientos);
        if (tblMovimientos.getColumnModel().getColumnCount() > 0) {
            tblMovimientos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblMovimientos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblMovimientos.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblMovimientos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblMovimientos.getColumnModel().getColumn(4).setPreferredWidth(300);
            tblMovimientos.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        pnlDistribucion.add(jScrollDistribucuon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 180));

        jTabbedPane1.addTab("Distribución", pnlDistribucion);

        pnlRechazosParciales.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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
        jScrollParciales.setViewportView(tblProductosPorFacturaRechazados);
        if (tblProductosPorFacturaRechazados.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(4).setPreferredWidth(270);
        }

        javax.swing.GroupLayout pnlRechazosParcialesLayout = new javax.swing.GroupLayout(pnlRechazosParciales);
        pnlRechazosParciales.setLayout(pnlRechazosParcialesLayout);
        pnlRechazosParcialesLayout.setHorizontalGroup(
            pnlRechazosParcialesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRechazosParcialesLayout.createSequentialGroup()
                .addComponent(jScrollParciales, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlRechazosParcialesLayout.setVerticalGroup(
            pnlRechazosParcialesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollParciales, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
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
        jScrollDescuentos.setViewportView(tblDescuentos);
        if (tblDescuentos.getColumnModel().getColumnCount() > 0) {
            tblDescuentos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblDescuentos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblDescuentos.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblDescuentos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblDescuentos.getColumnModel().getColumn(4).setPreferredWidth(230);
        }

        javax.swing.GroupLayout pnlDescuentosLayout = new javax.swing.GroupLayout(pnlDescuentos);
        pnlDescuentos.setLayout(pnlDescuentosLayout);
        pnlDescuentosLayout.setHorizontalGroup(
            pnlDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDescuentosLayout.createSequentialGroup()
                .addComponent(jScrollDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 796, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );
        pnlDescuentosLayout.setVerticalGroup(
            pnlDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDescuentosLayout.createSequentialGroup()
                .addComponent(jScrollDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 50, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Descuentos", pnlDescuentos);

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
        jScrollRecogidas.setViewportView(tblRecogidas);
        tblRecogidas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblRecogidas.getColumnModel().getColumnCount() > 0) {
            tblRecogidas.getColumnModel().getColumn(0).setResizable(false);
            tblRecogidas.getColumnModel().getColumn(0).setPreferredWidth(90);
            tblRecogidas.getColumnModel().getColumn(1).setResizable(false);
            tblRecogidas.getColumnModel().getColumn(2).setResizable(false);
            tblRecogidas.getColumnModel().getColumn(3).setResizable(false);
            tblRecogidas.getColumnModel().getColumn(4).setResizable(false);
            tblRecogidas.getColumnModel().getColumn(4).setPreferredWidth(200);
            tblRecogidas.getColumnModel().getColumn(5).setResizable(false);
            tblRecogidas.getColumnModel().getColumn(5).setPreferredWidth(120);
        }

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
                    .addGroup(pnlRecogidasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollRecogidas, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(258, 258, 258))
        );
        pnlRecogidasLayout.setVerticalGroup(
            pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRecogidasLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtNumeroDeSoporte, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(jLabel31))
                .addGap(2, 2, 2)
                .addComponent(jScrollRecogidas, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
        );

        jTabbedPane1.addTab("Recogidas", pnlRecogidas);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lblValorTotalFactura.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblValorTotalFactura.setText("$.");

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

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jToggleButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton1.setEnabled(false);
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jToggleButton1);

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

        jBtnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        jBtnExcel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExcel.setEnabled(false);
        jBtnExcel.setFocusable(false);
        jBtnExcel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExcel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExcelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExcel);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblValorTotalFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValorTotalFactura)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agregarImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarImagenMouseClicked

    }//GEN-LAST:event_agregarImagenMouseClicked


    private void borraUnaFilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borraUnaFilaActionPerformed
    }//GEN-LAST:event_borraUnaFilaActionPerformed

    private void borraUnaFilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borraUnaFilaMouseClicked


    }//GEN-LAST:event_borraUnaFilaMouseClicked

    private void tablaDocsVencidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsVencidosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDocsVencidosMouseClicked

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
                limpiar();
                new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();

            } catch (Exception ex) {
                Logger.getLogger(FConsultarFacturasRemoto.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_txtNumeroFacturaKeyPressed

    private void limpiar() {
        this.factura = null;
        txtDireccionDelCliente.setText("");
        txtBarrioCliente.setText("");
        txtFechaVenta.setText("");
        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText("");
        txtNombreDelVendedor.setText("");
        txtTelefonoDelCliente.setText("");
        lblValorRecaudado.setText("$.");
        lblValorTotalFactura.setText("$.");
        limpiarTablaProductosPorFactura();
        limpiarTablaBitacora();
        limpiarTablaMovimientos();
        limpiarTablaProductosRechazadosPorFactura();
        limpiarTablaDescuentos();
        limpiarTablaRecogidas();
    }

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

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        limpiar();
        txtNumeroFactura.setEnabled(true);
        txtNumeroFactura.setEditable(true);
        txtNumeroFactura.requestFocus();

    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        limpiar();
        jBtnNuevo.setEnabled(true);

    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExcelActionPerformed
//
//        try {
//            btnExportarExcel.setEnabled(false);
//            // new Thread(new FReporteMovilizadoHilo(this, false)).start();
//            Desktop.getDesktop().open(file);
//
//        } catch (IOException ex) {
//            Logger.getLogger(FReporteDescuentos_Recogidas.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_jBtnExcelActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_jBtnExitActionPerformed

    public void llenarTablaProductosPorFactura() throws Exception {
        if (factura.getListaDetalleFactura() != null) {
            double valorFactura = 0;
            modelo2 = (DefaultTableModel) tblProductosPorFactura.getModel();
            for (CProductosPorFactura obj : factura.getListaDetalleFactura()) {
                filaTabla2 = tblProductosPorFactura.getRowCount();

                modelo2.addRow(new Object[tblProductosPorFactura.getRowCount()]);
                tblProductosPorFactura.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), filaTabla2, 1); // numero de factura
                tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), filaTabla2, 2); // numero de factura
                tblProductosPorFactura.setValueAt(obj.getCantidad(), filaTabla2, 3); // numero de factura
                tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), filaTabla2, 4); // numero de factura
                tblProductosPorFactura.setValueAt(nf.format(obj.getValorTotalItemConIva()), filaTabla2, 5); // numero de factura

                valorFactura += obj.getValorTotalLiquidacionItem();

            }
            Thread.sleep(10);
            lblValorTotalFactura.setText(nf.format(valorFactura));
        }

    }

    public void llenarTablaDistribucion() throws Exception {
        if (factura.getListaDeMovimientosfactura() != null) {
            DefaultTableModel modelo = (DefaultTableModel) tblMovimientos.getModel();

            for (CFacturasPorManifiesto obj : factura.getListaDeMovimientosfactura()) {
                filaTabla2 = tblMovimientos.getRowCount();

                modelo.addRow(new Object[tblMovimientos.getRowCount()]);
                tblMovimientos.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblMovimientos.setValueAt(obj.getNumeroManifiesto(), filaTabla2, 1); // numero de manifiesto
                tblMovimientos.setValueAt(obj.getFechaIng(), filaTabla2, 2); // fecha de distribucion
                tblMovimientos.setValueAt(obj.getVehiculo(), filaTabla2, 3); // placa del vehiculo
                tblMovimientos.setValueAt(obj.getNombreConductor(), filaTabla2, 4); // nombre del conductor
                tblMovimientos.setValueAt(obj.getNombreDeRuta(), filaTabla2, 5); // nombre de la ruta

            }
        }

    }

    public void llenarTablabitacora() throws Exception {

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + factura.getListaDeMovimientosBitacora().size());

        DefaultTableModel modelo = (DefaultTableModel) tblBitacora.getModel();

        for (CBitacoraFacturas obj : this.factura.getListaDeMovimientosBitacora()) {
            filaTabla2 = tblBitacora.getRowCount();

            modelo.addRow(new Object[tblBitacora.getRowCount()]);

            tblBitacora.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
            tblBitacora.setValueAt(obj.getFechaIng(), filaTabla2, 1); // numero de manifiesto
            tblBitacora.setValueAt(obj.getFecha(), filaTabla2, 2); // numero de manifiesto
            tblBitacora.setValueAt(obj.getNumeroDocumento(), filaTabla2, 3); // fecha de distribucion
            tblBitacora.setValueAt(obj.getObservacion(), filaTabla2, 4); // placa del vehiculo
            // tblBitacora.setValueAt(obj.getNombreConductor(), filaTabla2, 4); // nombre del conductor

        }

    }

    public void llenarTablaProductosRechazados() throws Exception {

        if (factura.getListaDeProductosRechazados() != null) {
            System.out.println(new Date() + " acá se calcula la cantidad de productos rechazados =" + factura.getListaDeProductosRechazados().size());

            DefaultTableModel modelo = (DefaultTableModel) tblProductosPorFacturaRechazados.getModel();

            for (Vst_ProductosPorFacturaDescargados obj : factura.getListaDeProductosRechazados()) {
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JMenu borraUnaFila;
    private javax.swing.JPopupMenu borrarFila;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExcel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollDescuentos;
    private javax.swing.JScrollPane jScrollDistribucuon;
    private javax.swing.JScrollPane jScrollEntregas;
    private javax.swing.JScrollPane jScrollMovimientos;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollParciales;
    private javax.swing.JScrollPane jScrollRecogidas;
    public javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    private javax.swing.JLabel lblPrecioFactura2;
    public javax.swing.JLabel lblValorCobrado;
    public javax.swing.JLabel lblValorRecaudado;
    public javax.swing.JLabel lblValorTotalFactura;
    private javax.swing.JPanel pnlDescuentos;
    public javax.swing.JPanel pnlDistribucion;
    private javax.swing.JPanel pnlEntregas;
    private javax.swing.JPanel pnlMovimientos;
    private javax.swing.JPanel pnlRechazosParciales;
    private javax.swing.JPanel pnlRecogidas;
    private javax.swing.JPopupMenu pumImagen;
    private javax.swing.JTable tablaDocsVencidos;
    private javax.swing.JTable tblBitacora;
    private javax.swing.JTable tblDescuentos;
    private javax.swing.JTable tblMovimientos;
    private javax.swing.JTable tblProductosPorFactura;
    private javax.swing.JTable tblProductosPorFacturaRechazados;
    private javax.swing.JTable tblRecogidas;
    public javax.swing.JTextField txtBarrioCliente;
    public javax.swing.JTextField txtDireccionDelCliente;
    public javax.swing.JTextField txtFacturaAfectada;
    public javax.swing.JTextField txtFechaVenta;
    public javax.swing.JTextField txtNombreDelCliente;
    public javax.swing.JTextField txtNombreDelVendedor;
    public javax.swing.JTextField txtNumeroDeSoporte;
    public javax.swing.JTextField txtNumeroFactura;
    public javax.swing.JTextField txtTelefonoDelCliente;
    public javax.swing.JTextField txtValorDescontado;
    public javax.swing.JTextField txtValorRecogida;
    // End of variables declaration//GEN-END:variables

    public void limpiarTodo() {
        this.factura = null;
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
        limpiarTablaDescuentos();
        limpiarTablaRecogidas();
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
}
