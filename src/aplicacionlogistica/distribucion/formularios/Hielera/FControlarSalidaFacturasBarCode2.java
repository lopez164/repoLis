package aplicacionlogistica.distribucion.formularios.Hielera;

import aplicacionlogistica.distribucion.consultas.*;
import aplicacionlogistica.distribucion.Threads.HiloListadoConsultadeFacturaBitacora;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.minutas.MinutasDeDistribucion;
import aplicacionlogistica.distribucion.formularios.Hielera.Threads.HiloFControlarSalidaFacturasBarCode2;
import aplicacionlogistica.distribucion.formularios.Hielera.Threads.HiloGuardarAlertasSalidaDeProducto;
import aplicacionlogistica.distribucion.integrador.HiloIntegradorTNSLaHielera;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CProductos;
import aplicacionlogistica.distribucion.objetos.CProductosPorMinuta;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FControlarSalidaFacturasBarCode2 extends javax.swing.JInternalFrame {

    int controlCbx = 0;
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    CUsuarios user;
    public CEmpleados conductor;

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

    CFacturas factura;

    //FConsultarFacturasPorCliente fconsultarfacturasPorCliente;
    // public Vst_FacturaCamdun factura;
    CFacturasPorManifiesto facxMan;
    public int numeroManifesto;

    public TextAutoCompleter autoTxtConductores;

    public MinutasDeDistribucion minutaConductor = null;

    public List<CProductosPorMinuta> minutaEscaneada;
    public List<CProductosPorMinuta> minutaFacturada;

    public String listaDeFacturas = null;

    String tecla = null;

    CProductos productoEscaneado = null;
    CProductosPorMinuta nuevoPro = null;
    String codigoProducto = "";

    CProductosPorMinuta nuevoProdEscaneado = null;

//    publi=c ArrayList<Vst_FacturasPorManifiesto> listaDeMovimientosEnDistribucion;
//    public ArrayList<CBitacoraFacturas> listaDeMovimientosBitacora;
//    public ArrayList<Vst_ProductosPorFacturaDescargados> listaDeProductosRechazados;
    public Inicio getIni() {
        return ini;
    }

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public FControlarSalidaFacturasBarCode2(Inicio ini) throws Exception {
        try {
            initComponents();
            this.ini = ini;
            factura = new CFacturas(ini);

            new Thread(new HiloIntegradorTNSLaHielera(this.ini)).start();
            txtnombreConductor.setVisible(false);
            cbxNOmbresConductores.setVisible(true);

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
            Logger.getLogger(FControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
        }

        String strMain = this.ini.getPrefijos();
        String[] arrSplit = strMain.split(",");

//        for (int i = 0; i < arrSplit.length; i++) {
//            cbxPrefijos.addItem(arrSplit[i].replace("'", ""));
//        }
        strMain = this.factura.getListaFacturasBitacora("" + Inicio.getFechaSql());
        arrSplit = strMain.split(",");

        DefaultTableModel modelo = (DefaultTableModel) jTableFacturas.getModel();

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura 
        for (int i = 0; i < arrSplit.length; i++) {

            int fila = jTableFacturas.getRowCount();

            modelo.addRow(new Object[jTableFacturas.getRowCount()]);

            jTableFacturas.setValueAt("" + (fila + 1), fila, 0);
            jTableFacturas.setValueAt(arrSplit[i], fila, 1);

        } */
        txtCedulaConductor.setEnabled(true);
        txtCedulaConductor.setEditable(true);

        autoTxtConductores = new TextAutoCompleter(txtnombreConductor);

        for (CEmpleados conductor : this.ini.getListaDeEmpleados()) {
            if (conductor.getEmpleadoActivo() == 1) {
                autoTxtConductores.addItem(conductor.getNombres() + " " + conductor.getApellidos());
            }
        }
        cargado = true;

        this.moveToFront();
        txtnombreConductor.setEnabled(true);
        txtnombreConductor.setEditable(true);
        txtnombreConductor.requestFocus();

    }

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @param form formulario desde donde se carga la interface gráfica
     * @param factura corrresponde a la factura que se va a consultar
     */
    public FControlarSalidaFacturasBarCode2(Inicio ini, JInternalFrame form, CFacturas factura) throws Exception {
        this.factura = factura;
        this.form = form;

        try {
            initComponents();
            this.ini = ini;
            llenarDatosDeLaVista(this.factura);

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    public FControlarSalidaFacturasBarCode2(Inicio ini, CFacturas factura) throws Exception {
        this.factura = factura;
        try {
            initComponents();
            this.ini = ini;
            llenarDatosDeLaVista(this.factura);

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @param fconsultarfacturasPorCliente
     * @param factura corrresponde a la factura que se va a consultar
     */
    public FControlarSalidaFacturasBarCode2(Inicio ini, FConsultarFacturasPorCliente fconsultarfacturasPorCliente, CFacturas factura) throws Exception {
        this.factura = factura;
        // this.fconsultarfacturasPorCliente = fconsultarfacturasPorCliente;
        this.ini = ini;
        try {
            initComponents();
            jBtnNuevo.setEnabled(false);
            jBtnCancel.setEnabled(false);
            txtnombreConductor.setText(factura.getNumeroDeFactura());
            llenarDatosDeLaVista(this.factura);

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    public FControlarSalidaFacturasBarCode2(Inicio ini, JInternalFrame form, String numeroFactura) throws Exception {

        try {
            initComponents();
            this.ini = ini;
            txtnombreConductor.setEnabled(true);
            txtnombreConductor.setEditable(true);
            txtnombreConductor.requestFocus();

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
            Logger.getLogger(FControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    private void llenarDatosDeLaVista(CFacturas factura1) throws Exception {

        limpiarTodo();
        this.factura = factura1;
        this.factura.setListaDetalleFactura(false);
        this.factura.setListaDeMovimientosfactura();
        this.factura.setListaDeMovimientosBitacora();
        this.factura.setListaDeProductosRechazados();


        /*Hilo que recupera los movimientos de la factura */
        // new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this.factura)).start();
        txtnombreConductor.setText(factura.getNumeroDeFactura());

        /* nombre del cliente y el código entre parétesis*/
        llenarTablaProductosPorFactura();

        System.out.println("Aca verifica que hayan movimientos en la factura " + factura.getNumeroDeFactura());

        completado = false;

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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableFacturas = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExcel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProductosPorFactura = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        txtnombreConductor = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblProductosEscaneados = new javax.swing.JTable();
        jLabel48 = new javax.swing.JLabel();
        txtCodigoDeBArras = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        txtCantidadProducto = new javax.swing.JTextField();
        btnGrabar = new javax.swing.JButton();
        txtCedulaConductor = new javax.swing.JTextField();
        cbxNOmbresConductores = new javax.swing.JComboBox<>();

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
        setTitle("Formulario Verificacion salida de pedidos");
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
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jPanel5.setAutoscrolls(true);

        jTableFacturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "# Factura"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableFacturas);
        if (jTableFacturas.getColumnModel().getColumnCount() > 0) {
            jTableFacturas.getColumnModel().getColumn(0).setMinWidth(70);
            jTableFacturas.getColumnModel().getColumn(0).setPreferredWidth(70);
            jTableFacturas.getColumnModel().getColumn(0).setMaxWidth(70);
            jTableFacturas.getColumnModel().getColumn(1).setMinWidth(130);
            jTableFacturas.getColumnModel().getColumn(1).setPreferredWidth(130);
            jTableFacturas.getColumnModel().getColumn(1).setMaxWidth(130);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
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
        tblProductosPorFactura.getTableHeader().setReorderingAllowed(false);
        tblProductosPorFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosPorFacturaMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblProductosPorFactura);
        if (tblProductosPorFactura.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFactura.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblProductosPorFactura.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(2).setPreferredWidth(250);
            tblProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(125);
            tblProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Formulario salida de pedidos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 9))); // NOI18N

        txtnombreConductor.setEditable(false);
        txtnombreConductor.setName(""); // NOI18N
        txtnombreConductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreConductorActionPerformed(evt);
            }
        });
        txtnombreConductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnombreConductorKeyPressed(evt);
            }
        });

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Conductor");

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("C.C.");

        tblProductosEscaneados.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProductosEscaneados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProductosEscaneados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosEscaneadosMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblProductosEscaneados);
        if (tblProductosEscaneados.getColumnModel().getColumnCount() > 0) {
            tblProductosEscaneados.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblProductosEscaneados.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductosEscaneados.getColumnModel().getColumn(2).setPreferredWidth(250);
            tblProductosEscaneados.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblProductosEscaneados.getColumnModel().getColumn(4).setPreferredWidth(125);
            tblProductosEscaneados.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Codigo de Barras  ");

        txtCodigoDeBArras.setEditable(false);
        txtCodigoDeBArras.setName(""); // NOI18N
        txtCodigoDeBArras.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCodigoDeBArrasFocusGained(evt);
            }
        });
        txtCodigoDeBArras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoDeBArrasActionPerformed(evt);
            }
        });
        txtCodigoDeBArras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoDeBArrasKeyPressed(evt);
            }
        });

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Cantidad");

        txtCantidadProducto.setEditable(false);
        txtCantidadProducto.setText("1");
        txtCantidadProducto.setName(""); // NOI18N
        txtCantidadProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCantidadProductoFocusGained(evt);
            }
        });
        txtCantidadProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadProductoActionPerformed(evt);
            }
        });
        txtCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyPressed(evt);
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

        txtCedulaConductor.setEditable(false);
        txtCedulaConductor.setName(""); // NOI18N
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

        cbxNOmbresConductores.setEnabled(false);
        cbxNOmbresConductores.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxNOmbresConductoresItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtCodigoDeBArras, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(275, 275, 275))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                    .addComponent(txtCedulaConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(228, 228, 228)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(cbxNOmbresConductores, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(txtnombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)))
                        .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 778, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(txtCedulaConductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxNOmbresConductores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigoDeBArras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 781, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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

    private void tblProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaMouseClicked

    private void asignarConductor() {

        if (this.conductor.getListaDeFacturasPorConductorSinDespachar() != null) {
            return;
        }
        List<CProductosPorMinuta> listaDeProductos;
        // Date fecha = Inicio.getFechaSql(dateManifiesto);
        for (CEmpleados chofer : ini.getListaDeEmpleados()) {
            String cadena = chofer.getNombres() + " " + chofer.getApellidos();
            if (txtnombreConductor.getText().trim().equals(cadena)) {
                this.conductor = chofer;
                this.conductor.setListaDeFacturasPorConductorSinDespachar();

                if (this.conductor.getListaDeFacturasPorConductorSinDespachar().size() > 0) {

                    this.numeroManifesto = Integer.parseInt(this.conductor.getListaDeFacturasPorConductorSinDespachar().get(0).getNumeroManifiesto());

                    DefaultTableModel modelo = (DefaultTableModel) jTableFacturas.getModel();

                    listaDeFacturas = "";

                    /*Llena la tabla de las facturas */
                    for (CFacturasPorManifiesto fac : this.conductor.getListaDeFacturasPorConductorSinDespachar()) {
                        listaDeFacturas += "'" + fac.getNumeroFactura() + "',";
                        int fila = jTableFacturas.getRowCount();
                        modelo.addRow(new Object[jTableFacturas.getRowCount()]);

                        jTableFacturas.setValueAt("" + (fila + 1), fila, 0);
                        jTableFacturas.setValueAt(fac.getNumeroFactura(), fila, 1);

                    }
                    // SACAR MINUTA DEL CONDUCTOR
                    minutaConductor = new MinutasDeDistribucion(ini);
                    listaDeProductos = minutaConductor.getMinutaPorConductor(listaDeFacturas.substring(0, listaDeFacturas.length() - 1));
                    this.conductor.setMinutaConductor(listaDeProductos);
                    minutaFacturada = new ArrayList<>();

                    boolean encontrado = false;

                    for (CProductosPorMinuta minuta : this.conductor.getMinutaConductor()) {
                        encontrado = false;
                        String[] arrSplit = ini.getPropiedades().getProperty("productosNoAdmitidos").split(",");
                        for (String cad : arrSplit) {
                            if (minuta.getDescripcionProducto().contains(cad)) {
                                encontrado = true;
                                break;
                            }
                        }
                        if (!encontrado) {
                            // if (!(minuta.getDescripcionProducto().contains("DOMICILI") || minuta.getDescripcionProducto().contains("CERNIDO"))) {
                            minutaFacturada.add(minuta);
                        }
                    }

                    /* Se llena la tabla con los productos de las facturas
                    manifestadas al conductor
                     */
                    llenarJtableProductos(minutaFacturada);

                    txtCedulaConductor.setEnabled(false);
                    txtnombreConductor.setEnabled(false);

                    txtCantidadProducto.setEnabled(true);
                    txtCodigoDeBArras.setEnabled(true);

                    txtCantidadProducto.setEditable(true);
                    txtCodigoDeBArras.setEditable(true);

                    txtCodigoDeBArras.requestFocus();
                    txtCodigoDeBArras.requestFocus();

                } else {
                    JOptionPane.showInternalMessageDialog(this, "El conductor no tiene  facturas pendientes para salir a ruta", "Error, sin facturas", JOptionPane.WARNING_MESSAGE);

                }

            }
        }
    }

    private void asignarConductor(String nombreConductor) {
        List<CProductosPorMinuta> listaDeProductos;
        // Date fecha = Inicio.getFechaSql(dateManifiesto);

        if (this.conductor.getListaDeFacturasPorConductorSinDespachar() != null) {
            return;
        }

        this.conductor.setListaDeFacturasPorConductorSinDespachar();

        if (this.conductor.getListaDeFacturasPorConductorSinDespachar().size() > 0) {

            this.numeroManifesto = Integer.parseInt(this.conductor.getListaDeFacturasPorConductorSinDespachar().get(0).getNumeroManifiesto());

            DefaultTableModel modelo = (DefaultTableModel) jTableFacturas.getModel();

            listaDeFacturas = "";

            /*Llena la tabla de las facturas */
            for (CFacturasPorManifiesto fac : this.conductor.getListaDeFacturasPorConductorSinDespachar()) {
                listaDeFacturas += "'" + fac.getNumeroFactura() + "',";
                int fila = jTableFacturas.getRowCount();
                modelo.addRow(new Object[jTableFacturas.getRowCount()]);

                jTableFacturas.setValueAt("" + (fila + 1), fila, 0);
                jTableFacturas.setValueAt(fac.getNumeroFactura(), fila, 1);

            }
            listaDeFacturas = listaDeFacturas.substring(0, listaDeFacturas.length() - 1);

            // SACAR MINUTA DEL CONDUCTOR
            minutaConductor = new MinutasDeDistribucion(ini);
            listaDeProductos = minutaConductor.getMinutaPorConductor(listaDeFacturas.substring(0, listaDeFacturas.length() - 1));
            this.conductor.setMinutaConductor(listaDeProductos);
            minutaFacturada = new ArrayList<>();

            boolean encontrado = false;

            for (CProductosPorMinuta minuta : this.conductor.getMinutaConductor()) {
                encontrado = false;
                String[] arrSplit = ini.getPropiedades().getProperty("productosNoAdmitidos").split(",");
                for (String cad : arrSplit) {
                    if (minuta.getDescripcionProducto().contains(cad)) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    // if (!(minuta.getDescripcionProducto().contains("DOMICILI") || minuta.getDescripcionProducto().contains("CERNIDO"))) {
                    minutaFacturada.add(minuta);
                }
            }

            /* Se llena la tabla con los productos de las facturas
                    manifestadas al conductor
             */
            llenarJtableProductos(minutaFacturada);

            txtCedulaConductor.setEnabled(false);
            txtnombreConductor.setEnabled(false);

            txtCantidadProducto.setEnabled(true);
            txtCodigoDeBArras.setEnabled(true);

            txtCantidadProducto.setEditable(true);
            txtCodigoDeBArras.setEditable(true);

            txtCodigoDeBArras.requestFocus();
            txtCodigoDeBArras.requestFocus();

        } else {
            JOptionPane.showInternalMessageDialog(this, "El conductor no tiene  facturas pendientes para salir a ruta", "Error, sin facturas", JOptionPane.WARNING_MESSAGE);

        }

    }

    private void limpiar() {

        cargado = false;
        nuevo = false;
        actualizar = false;
        liberado = false;
        grabar = false;
        completado = false;
        mensaje = null;
        factura = null;
        facxMan = null;
        minutaConductor = null;
        conductor = null;
        minutaEscaneada = null;
        minutaFacturada = null;
        txtCedulaConductor.setText("");
        txtnombreConductor.setText("");
        /* nombre del cliente y el código entre parétesis*/
        limpiarTablaProductosPorFactura();
        limpiarTablaProductosEscaneados();
        limpiarTablaFacturas();
        txtCodigoDeBArras.setEnabled(false);
        txtCantidadProducto.setEnabled(false);
        cbxNOmbresConductores.setEnabled(false);
        conductor = null;

    }

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        try {
            limpiar();
            cbxNOmbresConductores.removeAllItems();
            jBtnNuevo.setEnabled(false);

            // new Thread( new FControlarSalidaFacturasBarCode2(ini,this, "llenarCbxNombresConductores")).start();
            new Thread(new HiloFControlarSalidaFacturasBarCode2(ini, this, "llenarComboBoxNombresDeConductores")).start();

        } catch (Exception ex) {
            Logger.getLogger(FControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    private void tblProductosEscaneadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosEscaneadosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosEscaneadosMouseClicked

    private void txtCodigoDeBArrasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoDeBArrasFocusGained
        txtCodigoDeBArras.setSelectionStart(0);
        txtCodigoDeBArras.setSelectionEnd(txtCodigoDeBArras.getText().length());
    }//GEN-LAST:event_txtCodigoDeBArrasFocusGained

    private void txtCodigoDeBArrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoDeBArrasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoDeBArrasActionPerformed

    private void txtCodigoDeBArrasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoDeBArrasKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            boolean encontrado = false;
            String valores = null;
            List<CProductosPorMinuta> productoPorMInuta;
            // nuevoPro = null;

            try {
                productoEscaneado = new CProductos(ini);
                /* Se valida que el productoEscaneado exista en la Base de Datos*/
                valores = productoEscaneado.validarProducto(txtCodigoDeBArras.getText().trim());

                if (valores != null) {
                    String[] propiedades = valores.split("#");

                    productoEscaneado.setCodigoProducto(propiedades[0]);
                    productoEscaneado.setDescripcionProducto(propiedades[1]);
                    productoEscaneado.setLinea(propiedades[2]);
                    productoEscaneado.setValorUnitarioSinIva(Double.parseDouble(propiedades[3]));
                    productoEscaneado.setValorUnitarioConIva(Double.parseDouble(propiedades[4]));
                    productoEscaneado.setIsFree(Integer.parseInt(propiedades[5]));
                    productoEscaneado.setPesoProducto(Double.parseDouble(propiedades[6]));
                    productoEscaneado.setLargoProducto(Double.parseDouble(propiedades[7]));
                    productoEscaneado.setAnchoProducto(Double.parseDouble(propiedades[8]));
                    productoEscaneado.setAltoProducto(Double.parseDouble(propiedades[9]));
                    productoEscaneado.setActivo(Integer.parseInt(propiedades[10]));
                    // "CURRENT_TIMESTAMP()"  ",'"
                    //producto.set("usuario");
                    //producto.setfl("flag");
                    productoEscaneado.setCodigoDeBarras(propiedades[14]);


                    /* se busca el codigo del productoEscaneado dentro de la lista que se le 
                    asigno al conductor */
                    for (CProductosPorMinuta prodsXconductor : conductor.getMinutaConductor()) /* se verifica que el productoEscaneado este en la factura */ {

                        if (productoEscaneado.getCodigoProducto().equals(prodsXconductor.getCodigoProducto())) {
                            encontrado = true;
                            break;
                        }
                    }
                    /*Producto validado en la lista de despacho(minuta) y no fue
                    encontrado o asignado al conductor*/
                    if (!encontrado) {
                        String mensaje = "El producto  : " + productoEscaneado.getDescripcionProducto() + " con codigo " + productoEscaneado.getCodigoProducto() + ", "
                                + " No se encuentra en la lista de Despacho ";

                        new Thread(new HiloGuardarAlertasSalidaDeProducto(ini, this.numeroManifesto, listaDeFacturas.replace("'", ""), mensaje)).start();

                        JOptionPane.showInternalMessageDialog(this, "El producto  : " + productoEscaneado.getDescripcionProducto() + "\n"
                                + " No se encuentra en la lista de Despacho ", "Error, Fuera de Despacho", JOptionPane.ERROR_MESSAGE);
                        productoEscaneado = null;
                        txtCantidadProducto.setEnabled(false);
                        txtCodigoDeBArras.requestFocus();

                    } else {
                        txtCantidadProducto.setEnabled(true);
                        txtCantidadProducto.setEditable(true);
                        txtCantidadProducto.requestFocus();
                        txtCantidadProducto.requestFocus();
                    }
                    /*El codigo del productoEscaneado escaneado no existe en el sistema*/
                } else {
                    String mensaje = "El producto  de codigo: " + txtCodigoDeBArras.getText().trim() + ""
                            + " No se encuentra dentro del Sistema ";

                    new Thread(new HiloGuardarAlertasSalidaDeProducto(ini, this.numeroManifesto, listaDeFacturas.replace("'", ""), mensaje)).start();

                    JOptionPane.showInternalMessageDialog(this, "El producto  de codigo: " + txtCodigoDeBArras.getText().trim() + "\n"
                            + " No se encuentra dentro del Sistema ", "Error,No existe en el sistema ", JOptionPane.ERROR_MESSAGE);

                    productoEscaneado = null;
                    txtCantidadProducto.setEnabled(false);
                    txtCodigoDeBArras.requestFocus();

                }
            } catch (Exception ex) {
                Logger.getLogger(FControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_txtCodigoDeBArrasKeyPressed

    private boolean validarCantidades(Double cantidad, CProductosPorMinuta prod) {

        if (cantidad > prod.getCantidad()) {
            String mensaje = "El producto  : " + prod.getDescripcionProducto() + " con codigo " + prod.getCodigoProducto() + ", "
                    + "tiene diferencias en las cantidades del producto, digita " + cantidad + "  unidades y el sistema reporta "
                    + prod.getCantidad() + " unidades de ese producto";

            new Thread(new HiloGuardarAlertasSalidaDeProducto(ini, this.numeroManifesto, listaDeFacturas.replace("'", ""), mensaje)).start();

            JOptionPane.showInternalMessageDialog(this, "El producto  : " + prod.getDescripcionProducto() + "\n"
                    + "Hay diferencias en las cantidades del producto ", "Error, Diferencia en las cantidades", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean validarMinutas() {

        boolean iguales = false;
        /*validar tamaño de las  listas de productos*/
        if (minutaFacturada.size() != minutaEscaneada.size()) {
            return false;
        }

        int x = 0;
        /*Valida que las productos esten en ambas listass*/
        for (CProductosPorMinuta productosEscaneados : minutaEscaneada) {
            for (CProductosPorMinuta prodxminuta : minutaFacturada) {
                if (productosEscaneados.getCodigoProducto().equals(prodxminuta.getCodigoProducto())) {
                    x++;
                    break;
                }

            }
        }

        if (x != minutaFacturada.size()) {
            return false;
        }

        x = 0;
        /*Valida que las cantidades de los productos sean iguales*/
        for (CProductosPorMinuta productosEscaneados : minutaFacturada) {
            for (CProductosPorMinuta prodxminuta : minutaEscaneada) {
                if (productosEscaneados.getCodigoProducto().equals(prodxminuta.getCodigoProducto())) {
                    if (productosEscaneados.getCantidad() == prodxminuta.getCantidad()) {
                        x++;
                    }
                    break;
                }

            }
        }

        if (x == minutaFacturada.size()) {
            iguales = true;
        }
        return iguales;
    }

    private void txtCantidadProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCantidadProductoFocusGained
        txtCantidadProducto.setSelectionStart(0);
        txtCodigoDeBArras.setSelectionEnd(txtCantidadProducto.getText().length());

    }//GEN-LAST:event_txtCantidadProductoFocusGained

    private void txtCantidadProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadProductoActionPerformed

    private void txtCantidadProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadProductoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            boolean encontrado = false;
            String valores = null;
            List<CProductosPorMinuta> productoPorMInuta;
            CProductosPorMinuta nuevoProdEscaneado = null;

            try {

                /* se busca el codigo del productoEscaneado dentro de la lista que se le 
                    asigno al conductor */
                for (CProductosPorMinuta productoEnMinuta : conductor.getMinutaConductor()) {
                    /* se verifica que el productoEscaneado este en la factura */ {
                        if (productoEscaneado.getCodigoProducto().equals(productoEnMinuta.getCodigoProducto())) {
                            encontrado = true;
                            nuevoProdEscaneado = new CProductosPorMinuta();

                            // nuevoProdEscaneado.setCantidad(0);
                            nuevoProdEscaneado.setCodigoProducto(productoEnMinuta.getCodigoProducto());
                            nuevoProdEscaneado.setDescripcionProducto(productoEnMinuta.getDescripcionProducto());
                            nuevoProdEscaneado.setPeso(productoEnMinuta.getPeso());
                            nuevoProdEscaneado.setValor(productoEnMinuta.getValor());

                            /*para el primer registro */
                            if (minutaEscaneada == null) {
                                minutaEscaneada = new ArrayList();
                                Double cantidadTxt = Double.parseDouble(txtCantidadProducto.getText().trim());

                                /* se valida la cantidad digitada con el productoEscaneado en Minuta*/
                                if (validarCantidades(cantidadTxt, productoEnMinuta)) {
                                    txtCantidadProducto.setEnabled(true);
                                    txtCantidadProducto.setText("1");
                                     productoEscaneado = null;
                                     txtCodigoDeBArras.requestFocus();
                                    /* se emite msj de error y sale */

                                    return;
                                } else {
                                    nuevoProdEscaneado.setCantidad(cantidadTxt);
                                    minutaEscaneada.add(nuevoProdEscaneado);
                                    //minutaEscaneada.get(0).setCantidad(cantidadTxt);
                                    colocarRegistroEnLaTabla(nuevoProdEscaneado);
                                }
                            } else {
                                int i = 0;
                                boolean enc = false;
                                /*Se busca si el productoEscaneado ya fue escaneado por el lector*/
                                for (CProductosPorMinuta productosEscaneados : minutaEscaneada) {

                                    if (productosEscaneados.getCodigoProducto().equals(nuevoProdEscaneado.getCodigoProducto())) {
                                        enc = true;
                                        Double cantidad = productosEscaneados.getCantidad() + Double.parseDouble(txtCantidadProducto.getText().trim());

                                        if (validarCantidades(cantidad, productoEnMinuta)) {
                                            txtCantidadProducto.setEnabled(true);
                                            txtCantidadProducto.setText("1");
                                            productoEscaneado = null;
                                            txtCodigoDeBArras.requestFocus();

                                            return;
                                        } else {
                                            //minutaEscaneada.get(i).setCantidad(cantidad);
                                            productosEscaneados.setCantidad(cantidad);
                                            tblProductosEscaneados.setValueAt(productosEscaneados.getCantidad(), i, 3);
                                        }

                                        break;
                                    }
                                    i++;
                                }
                                if (!enc) {
                                    // minutaEscaneada.add(nuevoProdEscaneado);
                                    Double cantidad = Double.parseDouble(txtCantidadProducto.getText().trim());

                                    if (validarCantidades(cantidad, productoEnMinuta)) {
                                        txtCantidadProducto.setEnabled(true);
                                        txtCantidadProducto.setText("1");
                                         productoEscaneado = null;
                                        txtCodigoDeBArras.requestFocus();

                                        return;
                                    } else {
                                        nuevoProdEscaneado.setCantidad(cantidad);
                                        minutaEscaneada.add(nuevoProdEscaneado);
                                        // minutaEscaneada.get(i).setCantidad(cantidad);
                                        colocarRegistroEnLaTabla(nuevoProdEscaneado);
                                    }

                                }

                            }

                        }
                    }
                }
                /*Producto validado en la lista de despacho(minuta) y no fue
                    encontrado o asignado al conductor*/
                if (!encontrado) {

                    String mensaje = "El producto  : " + productoEscaneado.getDescripcionProducto() + "\n"
                            + " No se encuentra en la lista de Despacho ";

                    JOptionPane.showInternalMessageDialog(this, mensaje, "Error, Fuera de Despacho", JOptionPane.ERROR_MESSAGE);

                    new Thread(new HiloGuardarAlertasSalidaDeProducto(ini,
                            this.numeroManifesto,
                            listaDeFacturas.replace("'", ""),
                            mensaje)).start();

                }
                /*El codigo del productoEscaneado escaneado no existe en el sistema*/

            } catch (Exception ex) {
                Logger.getLogger(FControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (validarMinutas()) {
                btnGrabar.setEnabled(true);
                txtCantidadProducto.setEnabled(true);
                txtCodigoDeBArras.setEnabled(true);
                btnGrabar.requestFocus();
            } else {
                txtCantidadProducto.setText("1");
                txtCantidadProducto.requestFocus();
                productoEscaneado = null;
                txtCodigoDeBArras.requestFocus();
            }

        }
//// else{
////          codigoProducto += KeyEvent.getKeyText(evt.getKeyCode());
////           System.out.println(codigoProducto);
////       }       
////        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadProductoKeyPressed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed

        // grabar();
        new Thread(new HiloIntegradorTNSLaHielera(this.ini)).start();

        int deseaGrabarRegistro;
        deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {
            try {
                if (conductor.despachoDeProductosPorFacturas2(listaDeFacturas)) {
                    txtCantidadProducto.setEnabled(false);
                    txtnombreConductor.setEnabled(false);
                    txtCodigoDeBArras.setEnabled(false);
                    btnGrabar.setEnabled(false);

                    //JOptionPane.showMessageDialog(this, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                    limpiarTablaProductosEscaneados();
                    limpiarTablaProductosPorFactura();
                    limpiarTablafacturas();

                    listaDeFacturas = null;
                    jBtnNuevo.setEnabled(true);
                    cbxNOmbresConductores.removeAllItems();
                    conductor = null;

                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);

                }

            } catch (Exception ex) {
                Logger.getLogger(FControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }//GEN-LAST:event_btnGrabarActionPerformed

    private void txtCedulaConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaConductorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorFocusGained

    private void txtCedulaConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorActionPerformed

    private void txtCedulaConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaConductorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            for (CEmpleados chofer : ini.getListaDeEmpleados()) {
                if (chofer.getCedula().equals(txtCedulaConductor.getText().trim())) {
                    txtnombreConductor.setText(chofer.getNombres() + " " + chofer.getApellidos());
                    //asignarConductor();
                    break;
                }
            }
        }
    }//GEN-LAST:event_txtCedulaConductorKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed


    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyReleased

    private void cbxNOmbresConductoresItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxNOmbresConductoresItemStateChanged
        setConductor();
    }//GEN-LAST:event_cbxNOmbresConductoresItemStateChanged

    private void txtnombreConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreConductorKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String nombre = txtnombreConductor.getText().trim();
            for (CEmpleados cond : ini.getListaDeEmpleados()) {
                if (nombre.equals(cond.getNombres() + " " + cond.getApellidos())) {
                    this.conductor = cond;
                    new Thread(new HiloFControlarSalidaFacturasBarCode2(ini, this, "asignarConductor")).start();
                    break;
                }

            }
        }
    }//GEN-LAST:event_txtnombreConductorKeyPressed

    private void txtnombreConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreConductorActionPerformed

    private void setConductor() {
        if (this.conductor == null) {

            if (cbxNOmbresConductores.getSelectedIndex() > 0) {
                controlCbx++;
                listaDeFacturas = "";
                String nombreDEConductor;
                List<CProductosPorMinuta> listaDeProductos;
                try {
                    nombreDEConductor = cbxNOmbresConductores.getSelectedItem().toString();
                    for (CEmpleados chofer : ini.getListaDeEmpleados()) {
                        String cadena = chofer.getNombres() + " " + chofer.getApellidos();

                        if (nombreDEConductor.equals(cadena)) {
                            this.conductor = chofer;
                            break;
                        }
                    }

                    if (this.conductor != null) {
                        new Thread(new HiloFControlarSalidaFacturasBarCode2(ini, this, "asignarConductor")).start();
                        //asignarConductor(nombreDEConductor);
                        cbxNOmbresConductores.setEnabled(false);
                    }

                } catch (Exception ex) {
                    Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

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

                valorFactura += obj.getValorTotalItemConIva();

            }
            Thread.sleep(10);
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JMenu borraUnaFila;
    private javax.swing.JPopupMenu borrarFila;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JComboBox<String> cbxNOmbresConductores;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExcel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    public javax.swing.JTable jTableFacturas;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPopupMenu pumImagen;
    private javax.swing.JTable tablaDocsVencidos;
    public javax.swing.JTable tblProductosEscaneados;
    public javax.swing.JTable tblProductosPorFactura;
    public javax.swing.JTextField txtCantidadProducto;
    public javax.swing.JTextField txtCedulaConductor;
    public javax.swing.JTextField txtCodigoDeBArras;
    public javax.swing.JTextField txtnombreConductor;
    // End of variables declaration//GEN-END:variables

    public void limpiarTodo() {
        this.factura = null;
        txtnombreConductor.setText("");
        limpiarTablaProductosPorFactura();

    }

    public void limpiarTablafacturas() {

        DefaultTableModel modelo = (DefaultTableModel) jTableFacturas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

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

    public void limpiarTablaProductosEscaneados() {

        DefaultTableModel modelo = (DefaultTableModel) tblProductosEscaneados.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaFacturas() {

        DefaultTableModel modelo = (DefaultTableModel) jTableFacturas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    /**
     * Método que asigna las factura leida por teclado ó por código de barras al
     * manifiesto actual
     *
     * @throws java.lang.Exception
     */
    public void agregarFactura() throws Exception {

        try {

            limpiarTablaProductosPorFactura();
            limpiarTablaProductosEscaneados();

            /*Se valida el tipo de movimiento de la factura*/
            switch (this.factura.getEstadoFactura()) {

                case 2:
                    /*Entrega total*/
                    JOptionPane.showInternalMessageDialog(this, "La Factura  # " + factura.getNumeroDeFactura() + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);
                    return;

                case 3:
                    /*Devolucion total*/
                    JOptionPane.showInternalMessageDialog(this, "La Factura  # " + factura.getNumeroDeFactura() + " fue devuelta de ruta", "Error, factura Devuelta", JOptionPane.WARNING_MESSAGE);

                    return;
                case 4:/*Entrega con novedad*/
                    JOptionPane.showInternalMessageDialog(this, "La Factura  # " + factura.getNumeroDeFactura() + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                    return;
                case 5:/*Entrega total con recogida*/
                    JOptionPane.showInternalMessageDialog(this, "La Factura  # " + factura.getNumeroDeFactura() + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                    return;
                case 6:
                    /*Re Envio*/
                    break;

                case 8:
                    /*ANULADA*/
                    JOptionPane.showInternalMessageDialog(this, "La Factura  # " + factura.getNumeroDeFactura() + "  fue ANULADA", "Error, factura ANULADA", JOptionPane.ERROR_MESSAGE);
                    return;

            }

            /* Se llena el Jtable correspondiente */
            llenarJtableProductosEscaneados();
            llenarJtableProductosPorFactura();

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void llenarJtableProductosEscaneados() {

        modelo1 = (DefaultTableModel) tblProductosEscaneados.getModel();

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura */
        for (CProductosPorFactura obj : factura.getListaCProductosPorFactura()) {
            if (!obj.getDescripcionProducto().contains("DOMICILI")) {
                try {
                    CProductosPorFactura prod = new CProductosPorFactura(ini);
                    prod.setActivo(obj.getActivo());
                    prod.setAgencia(obj.getAgencia());
                    prod.setCanal(obj.getCanal());
                    prod.setCantidad(0);
                    prod.setCantidadEntregadaItem(0);
                    prod.setCantidadRechazadaItem(0);
                    prod.setCausalDeRechazo(1);
                    prod.setCodigoDeBarras(obj.getCodigoDeBarras());
                    prod.setCodigoProducto(obj.getCodigoProducto());
                    prod.setConsecutivo(obj.getConsecutivo());
                    prod.setConsecutivoFactXMfto(obj.getConsecutivoFactXMfto());
                    prod.setConsecutivoProductoPorFactura(obj.getConsecutivoProductoPorFactura());
                    prod.setDescripcionProducto(obj.getDescripcionProducto());
                    prod.setEntregado(obj.getEntregado());
                    prod.setFechaDeVenta(obj.getFechaDeVenta());
                    prod.setFormaDePago(obj.getFormaDePago());
                    prod.setIdCliente(obj.getIdCliente());
                    prod.setIdRegional(obj.getIdRegional());
                    prod.setIdZona(obj.getIdZona());
                    prod.setIni(obj.getIni());
                    prod.setLinea(obj.getLinea());
                    prod.setNombreCausalDeRechazo(obj.getNombreCausalDeRechazo());
                    prod.setNombreDeCliente(obj.getNombreDeCliente());
                    prod.setNombreVendedor(obj.getNombreVendedor());
                    prod.setNumeroFactura(obj.getNumeroFactura());
                    prod.setPesoProducto(obj.getPesoProducto());
                    prod.setPesoTotalFactura(obj.getPesoTotalFactura());
                    prod.setPorcentajeDescuento(obj.getPorcentajeDescuento());
                    prod.setValorDescuentoFactura(obj.getValorDescuentoFactura());
                    prod.setValorDescuentoItem(obj.getValorDescuentoItem());
                    prod.setValorFacturaSinIva(obj.getValorFacturaSinIva());
                    prod.setValorIvaFactura(obj.getValorIvaFactura());
                    prod.setValorProductoXCantidad(obj.getValorProductoXCantidad());
                    prod.setValorRecaudadoFactura(obj.getValorRecaudadoFactura());

                    int fila = tblProductosEscaneados.getRowCount();

                    modelo1.addRow(new Object[tblProductosEscaneados.getRowCount()]);

                    tblProductosEscaneados.setValueAt("" + (fila + 1), fila, 0);
                    tblProductosEscaneados.setValueAt(obj.getCodigoProducto(), fila, 1);
                    tblProductosEscaneados.setValueAt(obj.getDescripcionProducto(), fila, 2);
                    tblProductosEscaneados.setValueAt(0, fila, 3);
                    tblProductosEscaneados.setValueAt(nf.format(0), fila, 4);
                    tblProductosEscaneados.setValueAt(nf.format(0), fila, 5);

                } catch (Exception ex) {
                    Logger.getLogger(FControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    private void llenarJtableProductosPorFactura() {
        modelo1 = (DefaultTableModel) tblProductosPorFactura.getModel();

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura */
        for (CProductosPorMinuta obj : conductor.getMinutaConductor()) {

            int fila = tblProductosPorFactura.getRowCount();

            agregarRegistroALaTabla(modelo1, fila, obj);
        }
    }

    public void llenarJtableProductos(List<CProductosPorMinuta> productos) {
        DefaultTableModel modelo1 = (DefaultTableModel) tblProductosPorFactura.getModel();
        boolean encontrado = false;

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura */
        for (CProductosPorMinuta obj : productos) {

            int fila = tblProductosPorFactura.getRowCount();
            String[] arrSplit = ini.getPropiedades().getProperty("productosNoAdmitidos").split(",");
            for (String cad : arrSplit) {
                if (obj.getDescripcionProducto().contains(cad)) {
                    encontrado = true;
                    break;
                }
            }

            // if (!(obj.getDescripcionProducto().contains("DOMICILI") || obj.getDescripcionProducto().contains("CERNIDO"))) {
            if (!encontrado) {

                modelo1.addRow(new Object[tblProductosPorFactura.getRowCount()]);

                tblProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
                //tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1); // codigo del Producot
                 tblProductosPorFactura.setValueAt("", fila, 1); // codigo del Producot
                tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2); // desripcion del productoEscaneado
                //tblProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3); // cantidad
                 tblProductosPorFactura.setValueAt("", fila, 3); // cantidad
                tblProductosPorFactura.setValueAt("", fila, 4);
                tblProductosPorFactura.setValueAt(nf.format(obj.getValor()), fila, 5);
                //tblProductosPorFactura.setValueAt(productoEscaneado[3], fila, 5);
            }

        }
    }

    private void actualizarJtablaFacturas(String numeroFactura) {

        boolean existe = false;

        DefaultTableModel modelo2 = (DefaultTableModel) jTableFacturas.getModel();
        filaTabla2 = jTableFacturas.getRowCount();

        if (filaTabla2 > 0) {
            for (int i = 0; i < filaTabla2; i++) {
                if (numeroFactura.equals(jTableFacturas.getValueAt(i, 1))) {
                    existe = true;
                    break;
                }
            }
        }

        if (!existe) {

            /* se anexa el registro a la Jtable de facturas por manifiesto */
            filaTabla2 = jTableFacturas.getRowCount();

            modelo2.addRow(new Object[jTableFacturas.getRowCount()]);

            jTableFacturas.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);
            jTableFacturas.setValueAt(numeroFactura, filaTabla2, 1);
        }

    }

    private void colocarRegistroEnLaTabla(CProductosPorMinuta obj) {
        DefaultTableModel modeloTabla = (DefaultTableModel) tblProductosEscaneados.getModel();
        boolean encontrado = false;

        int fila = tblProductosEscaneados.getRowCount();
        /*Si la tabla no tiene registros*/
        if (tblProductosEscaneados.getRowCount() == 0) {

            modeloTabla.addRow(new Object[tblProductosPorFactura.getRowCount()]);

            obj.setCantidad(obj.getCantidad());
            tblProductosEscaneados.setValueAt("" + (fila + 1), fila, 0);
            tblProductosEscaneados.setValueAt(obj.getCodigoProducto(), fila, 1);
            tblProductosEscaneados.setValueAt(obj.getDescripcionProducto(), fila, 2);
            tblProductosEscaneados.setValueAt(obj.getCantidad(), fila, 3);
            //tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            //tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva() * obj.getCantidad()), fila, 5);

            //agregarRegistroALaTabla(modeloTabla, fila, obj);
        } else {
            /* Verifica que el registro se encuentre en la tabla */
            for (int i = 0; i < tblProductosEscaneados.getRowCount(); i++) {
                if (obj.getCodigoProducto().equals(tblProductosEscaneados.getValueAt(i, 1).toString())) {
                    encontrado = true;
                    /*El acumulador del productoEscaneado se incrementa */
                    obj.setCantidad(obj.getCantidad() + 1);
                    /*Se refleja el acumulador en la tabla */
                    tblProductosEscaneados.setValueAt(obj.getCantidad(), i, 3);
                    break;
                }
            }
            if (!encontrado) {

                modeloTabla.addRow(new Object[tblProductosPorFactura.getRowCount()]);

                tblProductosEscaneados.setValueAt("" + (fila + 1), fila, 0);
                tblProductosEscaneados.setValueAt(obj.getCodigoProducto(), fila, 1);
                tblProductosEscaneados.setValueAt(obj.getDescripcionProducto(), fila, 2);
                tblProductosEscaneados.setValueAt(obj.getCantidad(), fila, 3);
            }

        }

        /* for (CProductosPorFactura obj : factura.getListaDetalleFactura()) {

            int fila = tblProductosEscaneados.getRowCount();

            modelo1.addRow(new Object[tblProductosPorFactura.getRowCount()]);

            tblProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            tblProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva() * obj.getCantidad()), fila, 5);
        }*/
    }

    private void agregarRegistroALaTabla(DefaultTableModel modelo11, int fila, CProductosPorMinuta obj) {
        boolean encontrado = false;

        String[] arrSplit = ini.getPropiedades().getProperty("productosNoAdmitidos").split(",");
        for (String cad : arrSplit) {
            if (obj.getDescripcionProducto().contains(cad)) {
                encontrado = true;
                break;
            }
        }
        // if (!obj.getDescripcionProducto().contains("DOMICILI")) {
        if (!encontrado) {
            modelo11.addRow(new Object[tblProductosPorFactura.getRowCount()]);
        }

        tblProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
        tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
        tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
        tblProductosPorFactura.setValueAt("", fila, 3);
//            tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
        //           tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva() * obj.getCantidad()), fila, 5);
    }

}
