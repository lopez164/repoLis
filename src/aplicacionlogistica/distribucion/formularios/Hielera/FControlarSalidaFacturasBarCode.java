/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera;

import aplicacionlogistica.distribucion.consultas.*;
import aplicacionlogistica.distribucion.Threads.HiloListadoConsultadeFacturaBitacora;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CProductos;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
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
public class FControlarSalidaFacturasBarCode extends javax.swing.JInternalFrame {
    
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
    
    CFacturas factura;
    
    //FConsultarFacturasPorCliente fconsultarfacturasPorCliente;
    
  // public Vst_FacturaCamdun factura;
    CFacturasPorManifiesto facxMan;
    
    
       public List<Vst_ProductosPorFactura> listaProductosPorFacturaEscaneados;
//    public ArrayList<Vst_FacturasPorManifiesto> listaDeMovimientosEnDistribucion;
//    public ArrayList<CBitacoraFacturas> listaDeMovimientosBitacora;
//    public ArrayList<Vst_ProductosPorFacturaDescargados> listaDeProductosRechazados;

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public FControlarSalidaFacturasBarCode(Inicio ini) throws Exception {
        try {
            initComponents();
             this.ini = ini;
             factura = new CFacturas(ini);
            
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
            Logger.getLogger(FControlarSalidaFacturasBarCode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        String strMain = this.ini.getPrefijos();
        String[] arrSplit = strMain.split(",");
       
        for (int i = 0; i < arrSplit.length; i++) {
            cbxPrefijos.addItem(arrSplit[i].replace("'", ""));
        }
        
         strMain = this.factura.getListaFacturasBitacora("" + Inicio.getFechaSql());
         arrSplit = strMain.split(",");
         
         DefaultTableModel modelo = (DefaultTableModel) jTableFacturas.getModel();

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura */
        for (int i = 0; i < arrSplit.length; i++) {

        int fila = jTableFacturas.getRowCount();

            modelo.addRow(new Object[jTableFacturas.getRowCount()]);

            jTableFacturas.setValueAt("" + (fila + 1), fila, 0);
            jTableFacturas.setValueAt(arrSplit[i], fila, 1);
            
        }
           
       cargado = true;
            
        txtNumeroDeFactura.setEnabled(true);
        txtNumeroDeFactura.setEditable(true);
        txtNumeroDeFactura.requestFocus();
    
        
    }

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @param form formulario desde donde se carga la interface gráfica
     * @param factura corrresponde a la factura que se va a consultar
     */
    public FControlarSalidaFacturasBarCode(Inicio ini, JInternalFrame form, CFacturas factura) throws Exception {
        this.factura = factura;
        this.form = form;
        
        try {
            initComponents();
            this.ini = ini;
            llenarDatosDeLaVista(this.factura);
            
        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FControlarSalidaFacturasBarCode.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;
        
    }
    
    public FControlarSalidaFacturasBarCode(Inicio ini, CFacturas factura) throws Exception {
        this.factura = factura;
        try {
            initComponents();
            this.ini = ini;
            llenarDatosDeLaVista(this.factura);
            
        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FControlarSalidaFacturasBarCode.class.getName()).log(Level.SEVERE, null, ex);
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
    public FControlarSalidaFacturasBarCode(Inicio ini, FConsultarFacturasPorCliente fconsultarfacturasPorCliente, CFacturas factura) throws Exception {
        this.factura = factura;
       // this.fconsultarfacturasPorCliente = fconsultarfacturasPorCliente;
       this.ini = ini;
        try {
            initComponents();
            jBtnNuevo.setEnabled(false);
            jBtnCancel.setEnabled(false);
            txtNumeroDeFactura.setText(factura.getNumeroDeFactura());
            llenarDatosDeLaVista(this.factura);
            
        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FControlarSalidaFacturasBarCode.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;
        
    }
    
    public FControlarSalidaFacturasBarCode(Inicio ini, JInternalFrame form, String numeroFactura) throws Exception {
        
        try {
            initComponents();
            this.ini = ini;
            txtNumeroDeFactura.setEnabled(true);
            txtNumeroDeFactura.setEditable(true);
            txtNumeroDeFactura.requestFocus();
            
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
            Logger.getLogger(FControlarSalidaFacturasBarCode.class.getName()).log(Level.SEVERE, null, ex);
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
        txtNumeroDeFactura.setText(factura.getNumeroDeFactura());
        txtDireccionDelCliente.setText(factura.getDireccionDeCliente());
        txtBarrioCliente.setText(factura.getBarrio());
        txtFechaVenta.setText("" + factura.getFechaDeVenta());

        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText(factura.getNombreDeCliente() + " (" + factura.getCodigoDeCliente()+ ")");
        txtNombreDelVendedor.setText(factura.getVendedor());
        txtTelefonoDelCliente.setText(factura.getTelefonoCliente());
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
        jPanel1 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProductosPorFactura = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        txtNumeroDeFactura = new javax.swing.JTextField();
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
        jLabel43 = new javax.swing.JLabel();
        txtDireccionDelCliente = new javax.swing.JTextField();
        cbxPrefijos = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        btnAceptar = new javax.swing.JButton();
        lblValorTotalFactura = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtCodigoDeBArras = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblProductosPorFactura1 = new javax.swing.JTable();
        jLabel45 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();

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
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
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

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
            tblProductosPorFactura.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblProductosPorFactura.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(2).setPreferredWidth(250);
            tblProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(125);
            tblProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Formulario salida de pedidos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 9))); // NOI18N

        txtNumeroDeFactura.setEditable(false);
        txtNumeroDeFactura.setName(""); // NOI18N
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

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Prefijo");

        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        btnAceptar.setEnabled(false);
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        lblValorTotalFactura.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblValorTotalFactura.setText("$.");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)
                    .addComponent(jLabel42)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxPrefijos, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(lblValorTotalFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDireccionDelCliente)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtBarrioCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                                            .addComponent(txtTelefonoDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNombreDelVendedor)
                                            .addComponent(txtNombreDelCliente))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(232, 232, 232)))
                        .addComponent(btnAceptar)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxPrefijos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
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
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(lblValorTotalFactura))
            .addComponent(btnAceptar)
        );

        jTabbedPane1.addTab("Datos Factura", jPanel8);

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

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Codigo de Barras");

        tblProductosPorFactura1.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProductosPorFactura1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProductosPorFactura1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosPorFactura1MouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblProductosPorFactura1);
        if (tblProductosPorFactura1.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFactura1.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblProductosPorFactura1.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductosPorFactura1.getColumnModel().getColumn(2).setPreferredWidth(250);
            tblProductosPorFactura1.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblProductosPorFactura1.getColumnModel().getColumn(4).setPreferredWidth(125);
            tblProductosPorFactura1.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("Cantidad");

        txtCantidad.setEditable(false);
        txtCantidad.setText("1");
        txtCantidad.setName(""); // NOI18N
        txtCantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCantidadFocusGained(evt);
            }
        });
        txtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadActionPerformed(evt);
            }
        });
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigoDeBArras, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigoDeBArras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Codigo de Barras", jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addGap(3, 3, 3))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3))
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

    private void txtNumeroDeFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaKeyPressed
        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
            try {
                limpiar();
                String numeroFactura = cbxPrefijos.getSelectedItem().toString() + (Integer.parseInt(txtNumeroDeFactura.getText().trim()));

                 factura = new CFacturas(ini, numeroFactura);
                 factura.setListaCProductosPorFactura(false);
                 
                 listaProductosPorFacturaEscaneados = new ArrayList<>();
                
                
                
                 agregarFactura(numeroFactura);
                 actualizarJtablaFacturas(numeroFactura);
               
                
                if (this.facxMan.getNumeroFactura() != null) {

                   
                    
                    btnAceptar.setEnabled(false);
                    btnAceptar.requestFocus();
                    txtNumeroDeFactura.requestFocus();

                } else {
                    JOptionPane.showMessageDialog(this, "Número de Factura no aparece reportada saliendo a Ruta ", "Error", JOptionPane.WARNING_MESSAGE);

                }
                
            } catch (Exception ex) {
                Logger.getLogger(FControlarSalidaFacturasBarCode.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }        
    }//GEN-LAST:event_txtNumeroDeFacturaKeyPressed

    private void limpiar() {
        this.factura=null;
        txtDireccionDelCliente.setText("");
        txtBarrioCliente.setText("");
        txtFechaVenta.setText("");
        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText("");
        txtNombreDelVendedor.setText("");
        txtTelefonoDelCliente.setText("");
        lblValorTotalFactura.setText("$.");
        limpiarTablaProductosPorFactura();
        
    }

    private void txtNumeroDeFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaActionPerformed

    private void txtNumeroDeFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaFocusGained
        txtNumeroDeFactura.setSelectionStart(0);
        txtNumeroDeFactura.setSelectionEnd(txtNumeroDeFactura.getText().length());
    }//GEN-LAST:event_txtNumeroDeFacturaFocusGained

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

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        limpiar();
        txtNumeroDeFactura.setEnabled(true);
        txtNumeroDeFactura.setEditable(true);
        txtNumeroDeFactura.requestFocus();
                
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

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
      
       // this.factura.gr
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void txtCodigoDeBArrasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoDeBArrasFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoDeBArrasFocusGained

    private void txtCodigoDeBArrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoDeBArrasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoDeBArrasActionPerformed

    private void txtCodigoDeBArrasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoDeBArrasKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
           String codBar= txtCodigoDeBArras.getText().trim();
           
           try {
               CProductos producto = new CProductos(ini,null,codBar);
               for(CProductosPorFactura pxf : factura.getListaCProductosPorFactura()){
                   if(producto.getCodigoProducto().equals(pxf.getCodigoProducto())){
                       CProductosPorFactura productoEscaneado = new CProductosPorFactura(ini);
                       productoEscaneado.setNumeroFactura(pxf.getNumeroFactura());
                       productoEscaneado.setCodigoProducto(pxf.getCodigoProducto());
                       productoEscaneado.setValorUnitarioConIva(pxf.getValorUnitarioConIva());
                       productoEscaneado.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
                       
                   }
               }
           } catch (Exception ex) {
               Logger.getLogger(FControlarSalidaFacturasBarCode.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       
       
       } 
    }//GEN-LAST:event_txtCodigoDeBArrasKeyPressed

    private void tblProductosPorFactura1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFactura1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFactura1MouseClicked

    private void txtCantidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCantidadFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadFocusGained

    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadActionPerformed

    private void txtCantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadKeyPressed
    
    public  void llenarTablaProductosPorFactura() throws Exception {
        if (factura.getListaDetalleFactura()!= null) {
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
            lblValorTotalFactura.setText(nf.format(valorFactura));
        }
        
    }
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JMenu borraUnaFila;
    private javax.swing.JPopupMenu borrarFila;
    private javax.swing.JButton btnAceptar;
    public javax.swing.JComboBox<String> cbxPrefijos;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExcel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableFacturas;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblValorTotalFactura;
    private javax.swing.JPopupMenu pumImagen;
    private javax.swing.JTable tablaDocsVencidos;
    private javax.swing.JTable tblProductosPorFactura;
    private javax.swing.JTable tblProductosPorFactura1;
    public javax.swing.JTextField txtBarrioCliente;
    public javax.swing.JTextField txtCantidad;
    public javax.swing.JTextField txtCodigoDeBArras;
    public javax.swing.JTextField txtDireccionDelCliente;
    public javax.swing.JTextField txtFechaVenta;
    public javax.swing.JTextField txtNombreDelCliente;
    public javax.swing.JTextField txtNombreDelVendedor;
    public javax.swing.JTextField txtNumeroDeFactura;
    public javax.swing.JTextField txtTelefonoDelCliente;
    // End of variables declaration//GEN-END:variables

    public void limpiarTodo() {
        this.factura=null;  
        txtNumeroDeFactura.setText("");
        txtBarrioCliente.setText("");
        txtFechaVenta.setText("");
        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText("");
        txtNombreDelVendedor.setText("");
        txtTelefonoDelCliente.setText("");
        limpiarTablaProductosPorFactura();
        
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
   
    
    /**
     * Método que asigna las factura leida por teclado ó por código de barras al
     * manifiesto actual
     *
     * @param numeroDeFactura leida por código de barras ó por teclado
     */
    public void agregarFactura(String numeroDeFactura) throws Exception {

        this.factura  = null;
        try {
            /* se crea el objeto factura, digitado en el campo de texto */
           facxMan = new CFacturasPorManifiesto(ini, numeroDeFactura, "" + Inicio.getFechaSql());
           this.factura = new CFacturas(ini, numeroDeFactura);

            if ( this.facxMan.getNumeroFactura() == null) {
                
                // JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " no existe en servidor local", "Error, factura no existe", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            txtBarrioCliente.setText(facxMan.getBarrio());
            txtDireccionDelCliente.setText(facxMan.getDireccionDeCliente());
            txtFechaVenta.setText("" + facxMan.getFechaDeVenta());
            txtNombreDelCliente.setText(facxMan.getNombreDeCliente());
            txtNombreDelVendedor.setText(facxMan.getVendedor());
            txtTelefonoDelCliente.setText(facxMan.getTelefonoCliente());
            
            this.factura.setListaDetalleFactura(true);
            limpiarTablaProductosPorFactura();
            
          /*Se valida el tipo de movimiento de la factura*/
                switch ( this.factura.getEstadoFactura()) {

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

              

                        /*Se llena el Jtable correspondiente*/
                       llenarJtableProductosPorFactura();
                       btnAceptar.requestFocus();
                       btnAceptar.requestFocus();
                       txtNumeroDeFactura.requestFocus();

                   

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   
  
    private void llenarJtableProductosPorFactura() {
        modelo1 = (DefaultTableModel) tblProductosPorFactura.getModel();

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura */
        for (CProductosPorFactura obj : factura.getListaDetalleFactura()) {

        int fila = tblProductosPorFactura.getRowCount();

            modelo1.addRow(new Object[tblProductosPorFactura.getRowCount()]);

            tblProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            tblProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva() * obj.getCantidad()), fila, 5);
        }
    }
    
    
    private void actualizarJtablaFacturas(String numeroFactura) {

         boolean existe = false;
         
           modelo2 = (DefaultTableModel) jTableFacturas.getModel();
          filaTabla2 = jTableFacturas.getRowCount();
       
        if (filaTabla2 > 0) {
            for (int i = 0; i < filaTabla2; i++) {
                if (numeroFactura.equals(jTableFacturas.getValueAt(i, 1))) {
                    existe = true;
                    break;
                }
            }
        }
        
           
        
            if(!existe){
                 
        /* se anexa el registro a la Jtable de facturas por manifiesto */
        filaTabla2 = jTableFacturas.getRowCount();

        modelo2.addRow(new Object[jTableFacturas.getRowCount()]);

        jTableFacturas.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);
        jTableFacturas.setValueAt(numeroFactura, filaTabla2, 1);
            }
       
        
       
    }
  
}
