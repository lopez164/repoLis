/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.picking;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloCrearManifiestodestinoFacturas;
import aplicacionlogistica.distribucion.Threads.HiloGuardarFacturasEnPicking;
import aplicacionlogistica.distribucion.Threads.JcProgressBar;
import aplicacionlogistica.distribucion.consultas.FBuscarListadoDeEmpleados;
import aplicacionlogistica.distribucion.integrador.HiloIntegradorTNSPorFactura;
// import aplicacionlogistica.distribucion.consultas.*;
import aplicacionlogistica.distribucion.objetos.CDestinosFacturas;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import mtto.vehiculos.CCarros;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
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
 * Distribución el receptor, el auxiliar, un vehiculo dando una ruta y
 * clasificandolo dentro de un cnal de venta; el sistema se encrga de crear el
 * manifiestos y posteriormente se le asignan las factura que van a salir a
 * distrbucion.
 *
 * @author Luis Eduardo López
 */
public class FManifestarFacturasEnPicking extends javax.swing.JInternalFrame {
    
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    public CEmpleados receptor = null;
    
    public CEmpleados getReceptor() {
        return receptor;
    }
    
    public void setReceptor(CEmpleados receptor) {
        this.receptor = receptor;
    }
    
    public static boolean band = false;
    public static int valorDespBarraProgreso = 0;
    
    CUsuarios user;
    
    public String numeroManifiesto = null;
    
    String facturaActual;
    
    Inicio ini = null;
    
    public File archivoConListaDeFacturas;
    
    DefaultListModel modelo, modelo3;
    
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public DefaultTableModel modelo1, modelo2;
    
    boolean cargado = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean nuevo = false;
    
    int filaTabla2;
    int kilometraje = 0;
    public int formaDePago = 0;
    String mensaje = null;
    int contadorDeFacturas = 0;

    // public ArrayList<Vst_FacturaCamdun> listaDeCFacturasCamdunEnElManifiesto = null; //CFacturasCamdun
    public ArrayList<String> listaDeFacturasPorManifiesto = null;
    
    public ArrayList<String> listaDeFacturasEnElArchivo;
    
    public boolean estaOcupadoGrabando = false;

    /**
     * Creates new fReportemovilizadoPorConductor IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public FManifestarFacturasEnPicking(Inicio ini) throws Exception {
        
        try {
          
            initComponents();
            
            this.ini = ini;
          
            this.user = ini.getUser();
            
            cbxDestinos.removeAllItems();
            cbxPrefijos.removeAllItems();;
            
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
            Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
        }

        // SE LLENAN LAS LISTAS DESPLEGABLES
        for (CDestinosFacturas obj : this.ini.getArrDestinosFacturas()) {
            cbxDestinos.addItem(obj.getNombreDestino());
        }
        
        String strMain =this.ini.getPrefijos();
        String[] arrSplit = strMain.split(",");   
        for (int i = 0;i<= arrSplit.length; i++)    {     
           cbxPrefijos.addItem(arrSplit[i].replace("'", ""));   
        } 
       
        lblCirculoDeProgreso2.setVisible(false);
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
        txtBarroCliente = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lblCantidadFacturas = new javax.swing.JLabel();
        cbxPrefijos = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnCrearManifiesto = new javax.swing.JButton();
        txtNumeroDeManifiesto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbxDestinos = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        lblCirculoDeProgreso2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtNombreDeReceptor = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFacturasPorManifiesto = new javax.swing.JTable();
        brrProgreso = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
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
        setTitle("Formulario para manifestar Facturas para picking");
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

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Numero de Factura");

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Barrio");

        txtNumeroDeFactura.setEditable(false);
        txtNumeroDeFactura.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNumeroDeFactura.setName("vacio"); // NOI18N
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
        txtTelefonoCliente.setEnabled(false);
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
        txtNombreVendedor.setEnabled(false);
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
        txtNombreDeCliente.setEnabled(false);
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

        txtBarroCliente.setEditable(false);
        txtBarroCliente.setColumns(22);
        txtBarroCliente.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtBarroCliente.setEnabled(false);
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
        txtDireccionCliente.setEnabled(false);
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
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(lblCantidadFacturas, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblCantidadFacturas, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
        );

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Prefijo");

        javax.swing.GroupLayout pnlAgregarFacturaLayout = new javax.swing.GroupLayout(pnlAgregarFactura);
        pnlAgregarFactura.setLayout(pnlAgregarFacturaLayout);
        pnlAgregarFacturaLayout.setHorizontalGroup(
            pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreDeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBarroCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxPrefijos, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(124, 124, 124))
        );
        pnlAgregarFacturaLayout.setVerticalGroup(
            pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAgregarFacturaLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxPrefijos)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
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
                .addGroup(pnlAgregarFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Agregar Facturas", pnlAgregarFactura);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Manifiesto de Reparto"));
        jPanel7.setEnabled(false);

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
        jLabel6.setText("Manfiesto N°");

        cbxDestinos.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        cbxDestinos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxDestinos.setEnabled(false);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Destino");

        lblCirculoDeProgreso2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Receptor");

        txtNombreDeReceptor.setEditable(false);
        txtNombreDeReceptor.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreDeReceptor.setToolTipText("Ingresar apellidos completos");
        txtNombreDeReceptor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDeReceptorFocusGained(evt);
            }
        });
        txtNombreDeReceptor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDeReceptorKeyPressed(evt);
            }
        });

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Fecha");

        txtFecha.setEditable(false);
        txtFecha.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtFecha.setToolTipText("Ingresar apellidos completos");
        txtFecha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFechaFocusGained(evt);
            }
        });
        txtFecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFechaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxDestinos, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDeReceptor, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .addComponent(txtNumeroDeManifiesto)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCrearManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(lblCirculoDeProgreso2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(127, 127, 127))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxDestinos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtNombreDeReceptor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroDeManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCrearManifiesto))
                    .addComponent(lblCirculoDeProgreso2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(0, 7, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(brrProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(brrProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))))
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
        jBtnGrabar.setFocusable(false);
        jBtnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGrabarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGrabar);

        jBtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jBtnImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnImprimir.setFocusable(false);
        jBtnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnImprimirActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnImprimir);

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

        jBtnMinuta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Table.png"))); // NOI18N
        jBtnMinuta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnMinuta.setFocusable(false);
        jBtnMinuta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnMinuta.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnMinuta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnMinutaActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnMinuta);

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
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 1125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();
    }//GEN-LAST:event_btnSalirActionPerformed
    
    public void salir() {
        // SE LIBERA EL MANIFIESTO ACTUAL
        if (numeroManifiesto != null) {
            
        }

        // SE CIERRA LA APLICACION
        this.dispose();
        this.setVisible(false);
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        
        nuevo();
        

    }//GEN-LAST:event_btnNuevoActionPerformed
    
    public void nuevo() {
        try {
            cancelar();
            
            nuevo = true;
            limpiar();
            
            jPanel7.setEnabled(true);
            
            cbxDestinos.setEnabled(true);
            
            jTabbedPane1.setEnabled(false);
            btnCrearManifiesto.setEnabled(true);
            btnNuevo.setEnabled(false);
            
            formaDePago = 1; // contado

            receptor = null;
            
            numeroManifiesto = null;
            
            listaDeFacturasPorManifiesto = new ArrayList(); // CfacturasPorManifiesto

            listaDeFacturasEnElArchivo = new ArrayList();
            numeroManifiesto = null;
            
        } catch (Exception ex) {
            Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        
        if (grabar()) {
            return;
        }
        

    }//GEN-LAST:event_btnGrabarActionPerformed
    
    public boolean grabar() throws HeadlessException {
        int deseaGrabarRegistro;
        /*valida sin pendientes del receptor */
        if (false) {
            // if (numeroManifiesto.getCantidadManifiestosPendientesConductor() > 0) {
            JOptionPane.showMessageDialog(this, "El conductor tiene pendientes de ruta,\n no se puede grabar este manifiesto", "Conductor no esta paz y salvo", JOptionPane.ERROR_MESSAGE, null);
            return true;
        }
        
        deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);
        
        if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {

            /* Se valida la conexión a internet para grabar los datos en la BBDD */
            //if (ini.verificarConexion()) {
            if (true) {
                band = true;
                
                new Thread(new HiloGuardarFacturasEnPicking(ini, this)).start();
                new Thread(new JcProgressBar(brrProgreso, 100)).start();
                
            } else {
                
                JOptionPane.showMessageDialog(null, "No hay conexión a internet", "Error al guardar Datos", JOptionPane.WARNING_MESSAGE, null);
                
            }

            //
        }
        
        return false;
    }
    
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
                for (String obj : listaDeFacturasPorManifiesto) {
                    contadorDeCiclos++;
                    
                }

                /* SE GRABAN LOS REGISTROS DE FACTURAS POR MANIFIESTO  EN LA BBDD LOCAL */
                if (ini.insertarDatosLocalmente(sentenciasSQL)) {

                    /* SE HABILITA BOTON DE IMPRIMIR */
                    btnImprimir.setEnabled(true);

                    /* SE DESHABILITA EL BOTON DE GRABAR */
                    btnGrabar.setEnabled(false);
                    
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
        
        txtNombreDeReceptor.setEditable(false);
        
        cbxDestinos.setEnabled(false);
        
        btnCrearManifiesto.setEnabled(false);
        jTabbedPane1.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnImprimir.setEnabled(false);
        btnGrabar.setEnabled(false);
        grabar = false;
        
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
        
        numeroManifiesto = null;
        receptor = null;
        
        listaDeFacturasPorManifiesto = null;
        
        formaDePago = 0;
        
        btnImprimir.setEnabled(false);

        /*Componentes de JTabbePane */
        
    }

    private void jTableFacturasPorManifiestoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFacturasPorManifiestoMouseClicked
        // TODO add your handling code here:


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
            facturaActual = numeroDeFactura;
            if (numeroDeFactura != "" || numeroDeFactura != null || numeroDeFactura.length() > 0) {

                /* SE valida que la factura no esté en el manifiesto */
                if (!estaLaFacturaEnElManifiesto()) {

//                    ArchivosDeTexto archivo = new ArchivosDeTexto("" + (new File(".").getAbsolutePath()).replace(".", "") + numeroManifiesto.getRutArchivofacturasporManifiesto());
//                    if (archivo.insertarLineaEnFichero(listaDeCampos, numeroManifiesto.getRutArchivofacturasporManifiesto())) {
//
//                        /* se agrega el registro al array  de facturas por manifiesto */
                    listaDeFacturasPorManifiesto.add(facturaActual); //CfacturasPorManifiesto
//                        numeroManifiesto.setListaFacturasPorManifiesto(listaDeFacturasPorManifiesto);
//
//                        /*Validamos de donde proviene el dato: bien sea del campo 
//                            de texto ó de un archivo plano */
//                        if (!desdeArchivo) {
//                           
//                           
//                           
//
//                        }
                    /*Se llena el Jtable correspondiente*/
                    llenarJtableFacturasPorManifiesto();
//
//                        /* se ubica el cursor en la fila insertada */
                    jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);
//
//                        /* se imprime el dato en la respectiva etiqueta */
//                      
                    btnGrabar.setEnabled(!grabar);
//
//                    } else {
//                        JOptionPane.showInternalMessageDialog(this, "Error al grabar en el archivo temporal, la  FACTURA # " + numeroDeFactura + "", "Error", JOptionPane.ERROR_MESSAGE);
//
//                    }

                    /* si el registro existe en la tabla TMPfacturasPorManifiesto, hace nada */
                    txtNombreDeCliente.requestFocus();
                    
                }
            }

            /* si no   existe la factura, arroja un mensaje de error. */
            txtNombreDeCliente.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void llenarJtableFacturasPorManifiesto() {

        /* se anexa el registro a la Jtable de facturas por manifiesto */
        filaTabla2 = jTableFacturasPorManifiesto.getRowCount();
        
        modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);
        jTableFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);
        jTableFacturasPorManifiesto.setValueAt(facturaActual, filaTabla2, 1);
        jTableFacturasPorManifiesto.setValueAt("", filaTabla2, 2);
        jTableFacturasPorManifiesto.setValueAt(nf.format(0.0), filaTabla2, 3);
        
    }

    /*
    private void llenarJtableProductosPorFactura(ArrayList<Vst_ProductosPorFactura> arrProduproductosEnLaFactura) {
        modelo1 = (DefaultTableModel) jTableProductosPorFactura.getModel();

        Se obtine el listado de los productos en el array y se recorre
        for (Vst_ProductosPorFactura obj : arrProduproductosEnLaFactura) {

            Vst_ProductosPorFactura pxf = new Vst_ProductosPorFactura();

            int fila = jTableProductosPorFactura.getRowCount();

            modelo1.addRow(new Object[jTableProductosPorFactura.getRowCount()]);

            jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorTotalConIva() / obj.getCantidad()), fila, 4);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorTotalConIva()), fila, 5);
        }
    }

**/
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
            Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    

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

            /* Se validan todos los datos del manifiesdo receptor, vehiculo, ruta, canal...*/
            if (validarManifiesto()) {
                
                new Thread(new HiloCrearManifiestodestinoFacturas(ini, this)).start();
                
            } else {
                JOptionPane.showInternalMessageDialog(this, mensaje, "Error al guardar El Manifiesto de salida a Ruta", 0);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCrearManifiestoActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (numeroManifiesto != null) {
            
        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        
        imprimir();

    }//GEN-LAST:event_btnImprimirActionPerformed
    
    public void imprimir() {
        /*valida sin pendientes del receptor */
        try {
            //  if (numeroManifiesto.getEstadoManifiesto() == 2) {

            if (false) {
                //if (numeroManifiesto.getCantidadManifiestosPendientesConductor() > 0) {
                JOptionPane.showMessageDialog(this, "El conductor tiene pendientes de ruta,\n no se puede Imprimir este manifiesto", "Conductor no esta paz y salvo", JOptionPane.ERROR_MESSAGE, null);
                
                return;
            }

            /*Manifiesto no grabado en la BBDD, trae los registros localmente */
            //     numeroManifiesto.setVstListaFacturasEnDistribucion(vistaFacturasPorManifiesto);
            //  } else {
            /*Manifiesto grabado en la BBDD , trae registros desde allí*/
            //      numeroManifiesto.setVstListaFacturasEnDistribucion();
            //  }

            /* Genera el manifiesto*/
            //ReporteFacturasEnDistribucion1 demo = new ReporteFacturasEnDistribucion1(ini, numeroManifiesto);
            /*Genera el rutero*/
            ReporteFacturasParaPicking demo = new ReporteFacturasParaPicking(ini, listaDeFacturasPorManifiesto, numeroManifiesto, cbxDestinos.getSelectedItem().toString(), txtNombreDeReceptor.getText());
            //reporteSalidaADistribucion demo = new reporteSalidaADistribucion(ini, numeroManifiesto);
        } catch (Exception ex) {
            Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void borrar1FilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrar1FilaMouseClicked
        

    }//GEN-LAST:event_borrar1FilaMouseClicked

    private void borrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarTodoActionPerformed
        int deseaEliminartodo = 20;

        /* SE VERIFICA QUE EL AMNIFIESTO NO ESTE CERRADO */
        deseaEliminartodo = JOptionPane.showConfirmDialog(this, "Desea eliminar todos los registros  de la Tabla ?", "Eliminar Fila", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (deseaEliminartodo == JOptionPane.YES_OPTION) {
            try {
                
                borrarTodasLasfacturas();
                
            } catch (Exception ex) {
                Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

    }//GEN-LAST:event_borrarTodoActionPerformed

    private void borrar1FilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrar1FilaActionPerformed
        int deseaEliminarLaFila;

        /* SE VERIFICA QUE EL AMNIFIESTO NO ESTE CERRADO */
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
                    Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        } else {
            JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
        }

    }//GEN-LAST:event_borrar1FilaActionPerformed

    private void txtNombreDeReceptorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeReceptorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeReceptorFocusGained

    private void txtNombreDeReceptorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeReceptorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            
            FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(ini, this);
            this.getParent().add(form);
            form.toFront();
            form.setClosable(true);
            form.setVisible(true);
            form.setTitle("Formulario para buscar Empleados por apellidos");
            form.txtApellidosPersona.requestFocus();
            btnNuevo.setEnabled(false);
            form.show();
            
        }
        

    }//GEN-LAST:event_txtNombreDeReceptorKeyPressed

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
        
        if (numeroManifiesto != null) {
//            MinutasDeDistribucion sacarMinuta = new MinutasDeDistribucion(ini);
//
//            switch (numeroManifiesto.getEstadoManifiesto()) {
//                case 1:
//
//                case 2:
//
//                case 3:
//                    sacarMinuta.minutaPoManifiesto(numeroManifiesto.getNumeroManifiesto());
//                case 4:

//            }
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

    private void txtFechaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFechaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaFocusGained

    private void txtFechaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaKeyPressed

    private void jTabbedPane1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1FocusGained

    private void txtDireccionClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteKeyPressed

    private void txtDireccionClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteFocusGained

    private void txtBarroClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarroClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarroClienteKeyPressed

    private void txtBarroClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarroClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarroClienteFocusGained

    private void txtNombreDeClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtTelefonoCliente.requestFocus();
        }
    }//GEN-LAST:event_txtNombreDeClienteKeyPressed

    private void txtNombreDeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDeClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeClienteActionPerformed

    private void txtNombreDeClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeClienteFocusGained
        txtNombreDeCliente.setSelectionStart(0);
        txtNombreDeCliente.setSelectionEnd(txtNombreDeCliente.getText().length());
        txtNumeroDeFactura.requestFocus();
    }//GEN-LAST:event_txtNombreDeClienteFocusGained

    private void txtNombreVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreVendedorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //
        }
    }//GEN-LAST:event_txtNombreVendedorKeyPressed

    private void txtNombreVendedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreVendedorFocusGained
        txtNombreVendedor.setSelectionStart(0);
        txtNombreVendedor.setSelectionEnd(txtNombreVendedor.getText().length());
    }//GEN-LAST:event_txtNombreVendedorFocusGained

    private void txtTelefonoClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNombreVendedor.requestFocus();
        }
    }//GEN-LAST:event_txtTelefonoClienteKeyPressed

    private void txtTelefonoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoClienteActionPerformed

    private void txtTelefonoClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoClienteFocusGained
        txtTelefonoCliente.setSelectionStart(0);
        txtTelefonoCliente.setSelectionEnd(txtTelefonoCliente.getText().length());
    }//GEN-LAST:event_txtTelefonoClienteFocusGained

    private void txtNumeroDeFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaKeyPressed
        /*Evento al oprimir la tecla Enter */
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {
                new Thread(new HiloIntegradorTNSPorFactura(cbxPrefijos.getSelectedItem().toString(), txtNumeroDeFactura.getText())).start();
                

//agregarFactura("" + Integer.parseInt(txtNumeroDeFactura.getText().trim()), false, jTableFacturasPorManifiesto.getRowCount() + 1);
                agregarFactura(txtNumeroDeFactura.getText().trim(), false, jTableFacturasPorManifiesto.getRowCount() + 1);

                lblCantidadFacturas.setText("" + listaDeFacturasPorManifiesto.size());

                /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                if (listaDeFacturasPorManifiesto.size() > 1) {
                    btnImprimir.setEnabled(true);
                }

            } catch (Exception ex) {
                Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_txtNumeroDeFacturaKeyPressed

    private void txtNumeroDeFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaActionPerformed

    private void txtNumeroDeFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaFocusGained
        txtNumeroDeFactura.setSelectionStart(0);
        txtNumeroDeFactura.setSelectionEnd(txtNumeroDeFactura.getText().length());
    }//GEN-LAST:event_txtNumeroDeFacturaFocusGained

    /**
     * METODO QUE PERMITE AGREGAR FACTURAS DESDE UN ARCHIVO PLANO
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
                    //listaDeFacturasPorManifiesto.add(linea);
                }
                
               
                
            } catch (IOException ex) {
                Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, e2);
                
            }
        }
    }


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
    public javax.swing.JComboBox cbxDestinos;
    private javax.swing.JComboBox<String> cbxPrefijos;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    private javax.swing.JToggleButton jBtnGrabar;
    private javax.swing.JToggleButton jBtnImprimir;
    private javax.swing.JToggleButton jBtnMinuta;
    private javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableFacturasPorManifiesto;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblCantidadFacturas;
    public javax.swing.JLabel lblCirculoDeProgreso2;
    private javax.swing.JPopupMenu mnuBorrarFilas;
    public javax.swing.JPanel pnlAgregarFactura;
    private javax.swing.JTable tablaDocsVencidos;
    public javax.swing.JTextField txtBarroCliente;
    public javax.swing.JTextField txtDireccionCliente;
    public javax.swing.JTextField txtFecha;
    public javax.swing.JTextField txtNombreDeCliente;
    public javax.swing.JTextField txtNombreDeReceptor;
    public javax.swing.JTextField txtNombreVendedor;
    public javax.swing.JTextField txtNumeroDeFactura;
    public javax.swing.JTextField txtNumeroDeManifiesto;
    public javax.swing.JTextField txtTelefonoCliente;
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
            //cbxPlacas.setSelectedIndex(0);
            //cbxManifiestosDeDistribucion.setSelectedIndex(0);
            
            
            txtNombreDeReceptor.setText("");
            
            txtNumeroDeManifiesto.setText("");
            cbxDestinos.setSelectedIndex(0);
            
            DefaultTableModel model;
            model = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
            if (model.getRowCount() > 0) {
                int a = model.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    model.removeRow(i);
                }
            }
            
            listaDeFacturasPorManifiesto = null;
            
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

            if (cbxDestinos.getSelectedIndex() == 0) {
                mensaje += "No ha seleccionado el canal de distribución " + "  \n";
                verificado = false;
            }

//            if (!fi.equals(ff)) {
//                mensaje += "La fecha del Servidor y el Sistema no coinciden, verificar configuración del sistema " + "  \n";
//                verificado = false;
//            }
            if (receptor == null) {
                mensaje += "No ha asigando quien recibe las facturas " + "  \n";
                verificado = false;
                
            }
//            if (auxiliar1 == null) {
//                mensaje += "No ha asigando el Auxiliar de la ruta " + "  \n";
//                verificado = false;
//            } else if (auxiliar1.getCedula().equals(receptor.getCedula())) {
//                mensaje += "La cédula del receptor y del auxiliar son la misma  " + "  \n";
//                verificado = false;
//            }
//            

        } catch (Exception ex) {
            Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificado;
    }
    
    public synchronized void llenarDatosManifiestoCerrado() throws Exception {
        
        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        txtNombreDeReceptor.setEditable(false);
        
        cbxDestinos.setEnabled(false);
        
        btnCrearManifiesto.setEnabled(false);
        jTabbedPane1.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnImprimir.setEnabled(false);
        btnGrabar.setEnabled(false);
        
        txtNombreDeReceptor.setEnabled(false);
        
       
        
        
        int cantidadFacturasEnManifiesto = 0;
        
        modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
        
        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
//        for (Vst_FacturasPorManifiesto obj : numeroManifiesto.getVstListaFacturasEnDistribucion()) {
//
//            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
//            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
//            filaTabla2 = jTableFacturasPorManifiesto.getRowCount();
//            modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);
//
//            jTableFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
//            jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura
//
//            jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente
//
//            jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   
//            valorTotalManifiesto += obj.getValorARecaudarFactura();
//
//            // se ubica en la fila insertada
//            jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);
//
//            cantidadFacturasEnManifiesto++;
//            valor += obj.getValorARecaudarFactura();
//
//            // }
//            // }
//            //this.repaint();
//        }

        // numeroManifiesto.setValorTotalManifiesto(valor);
        // SE BUSCA EL CONDUCTOR Y SE LLENA EL CAMPO
//        for (CEmpleados obj : ini.getListaDeEmpleados()) {
//            if (numeroManifiesto.getConductor().equals(obj.getCedula())) {
//
//             //   txtnombreDeConductor.setText(obj.getNombres() + " " + obj.getApellidos());
//            }
//        }

        /* Se llenan os campos de texto de los auiliares*/
        // llenarTxtAuxiliares();
        //       txtNumeroDeManifiesto.setText(numeroManifiesto.codificarManifiesto());
        lblCantidadFacturas.setText("" + cantidadFacturasEnManifiesto);
        btnImprimir.setEnabled(true);
        btnImprimir.requestFocus();
        
    }

//    private void llenarTxtAuxiliares() {
//        /*se llenan los campos de texto de los nombres de los auxiliares*/
//        int indice = 1;
//
////        txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
////        txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
////        txtNombreDeAuxiliar3.setText("SALE A DISTRIBUCION SIN AUXILIAR3");
//        listaDeAuxiliares = numeroManifiesto.getListaDeAuxiliares("" + numeroManifiesto.getNumeroManifiesto());
//
//        for (CEmpleados aux : listaDeAuxiliares) {
//            switch (indice) {
//                case 1:
//                    if (aux.getCedula().equals("0")) {
//                        txtNombreDeAuxiliar1.setText("");
//                    } else {
//                        txtNombreDeAuxiliar1.setText(aux.getNombres() + " " + aux.getApellidos());
//                    }
//                    indice++;
//                    break;
//                case 2:
//                    if (aux.getCedula().equals("0")) {
//                        txtNombreDeAuxiliar2.setText("");
//                    } else {
//                        txtNombreDeAuxiliar2.setText(aux.getNombres() + " " + aux.getApellidos());
//                    }
//                    indice++;
//                    break;
//                case 3:
//                    if (aux.getCedula().equals("0")) {
//                        txtNombreDeAuxiliar3.setText("");
//                    } else {
//                        txtNombreDeAuxiliar3.setText(aux.getNombres() + " " + aux.getApellidos());
//                    }
//                    indice++;
//
//                    break;
//
//            }
//            /* fin switch */
//
//        }
//    }
//    private void limpiarDatodsDePanelProductosPorFactura() {
//
//        DefaultTableModel model;
//        model = (DefaultTableModel) jTableProductosPorFactura.getModel();
//        if (model.getRowCount() > 0) {
//            int a = model.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                model.removeRow(i);
//            }
//        }
//        txtNombreDeCliente.setText("");
//        txtDireccionCliente.setText("");
//        txtTelefonoCliente.setText("");
//        txtBarroCliente.setText("");
//        txtNombreVendedor.setText("");
//    }
    private void ordenarTabla() {
        for (int i = 0; i < jTableFacturasPorManifiesto.getRowCount(); i++) {
            jTableFacturasPorManifiesto.setValueAt(i + 1, i, 0);
        }
        
    }
    
    public synchronized void crearNuevoManifiesto(CCarros carro) {
        try {
            limpiar();
            this.numeroManifiesto = "";
            
            modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();


            /* Se verifica que el vehiculo tenga asignado un receptor */
            if (carro.getConductor().length() > 1) {
                try {
                    receptor = new CEmpleados(ini);
                    for (CEmpleados obj : ini.getListaDeEmpleados()) {
                        if (obj.getCedula().equals(carro.getConductor())) {
                            receptor = obj;
                        }
                        
                    }
                    
                } catch (Exception ex) {
                    Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            cbxDestinos.setEnabled(true);
            
            jTabbedPane1.setEnabled(true);
         
            btnCrearManifiesto.setEnabled(true);
            
            txtNombreDeReceptor.setEnabled(true);
            
        } catch (Exception ex) {
            Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    //public synchronized void modificarManifiesto() {
//    public void modificarManifiesto() throws Exception {
//
//        /* Se verifica que haya un manifiesto instanciado */
//        if (numeroManifiesto != null) {
//
//            /* Se valida que el manifiesto actual esté disponible , es decir que no esté otro usuario ingresando
//             facturas al sistema con el mismo vehículo*/
//            //if (numeroManifiesto.getIsFree() == 1) {
//            if ((numeroManifiesto.getIsFree() == 1) || (numeroManifiesto.getUsuarioManifiesto().equals(Inicio.deCifrar(ini.getUser().getNombreUsuario())))) {
//                try {
//
//                    modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
//                    // numeroManifiesto.listadoDeFacturas(numeroManifiesto.getNumeroManifiesto());
//
//                    listaDeFacturasPorManifiesto = new ArrayList<>(); // CfacturasPorManifiesto
//
//                    /*Se llama al fichero que contiene los numero de las facturas
//                     a  que están registradas en el manifiesto*/
//                    File fichero = new File(this.numeroManifiesto.getRutArchivofacturasporManifiesto());
//
//                    /*Se valida que exista el fichero */
//                    if (fichero.exists()) {
//
//                        numeroManifiesto.setListaFacturasPorManifiesto(fichero);
//
//                        listaDeFacturasPorManifiesto = numeroManifiesto.getListaFacturasPorManifiesto();
//
//                        if (listaDeFacturasPorManifiesto.isEmpty() || listaDeFacturasPorManifiesto == null) {
//                            btnGrabar.setEnabled(false);
//                        } else {
//                            btnGrabar.setEnabled(true);
//                        }
//
//                        llenarDatosManifiesto();
//
//                        
//
//                       
//                        cbxDestinos.setEnabled(false);
//                       
//                        jTabbedPane1.setEnabled(true);
//                        jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarLista), true);
//                        btnCrearManifiesto.setEnabled(false);
//                        btnGrabar.setEnabled(true);
//
//                        txtBarroCliente.setEditable(false);
//                        txtDireccionCliente.setEditable(false);
//                        txtNombreDeCliente.setEditable(false);
//                        txtTelefonoCliente.setEditable(false);
//                        txtNumeroDeManifiesto.setEditable(false);
//                        txtNombreVendedor.setEditable(false);
//
//                    
//
//                        txtNumeroDeFactura.setEnabled(true);
//                        txtNumeroDeFactura.setEditable(true);
//                        txtNumeroDeFactura.requestFocus();
//                        txtNumeroDeFactura.requestFocus();
//
//                    } else {
//
//                        listaDeFacturasPorManifiesto = new ArrayList<>();
//                        vistaFacturasPorManifiesto = new ArrayList<>();
//
//                        llenarDatosManifiesto();
//
//                       
//
//                        jTabbedPane1.setEnabled(true);
//                        jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfComponent(pnlAgregarLista), true);
//                        txtFile.setEditable(false);
//                        btnFile.setEnabled(true);
//                        cbxDestinos.setEnabled(false);
//                       
//                        txtNumeroDeFactura.setEnabled(true);
//                        txtNumeroDeFactura.setEditable(true);
//                        txtNumeroDeFactura.requestFocus();
//                        txtNumeroDeFactura.requestFocus();
//                        btnCrearManifiesto.setEnabled(false);
//
//                        txtNumeroDeFactura.requestFocus();
//                        txtNumeroDeFactura.requestFocus();
//
//                    }
//                } catch (Exception ex) {
//                    Logger.getLogger(FManifestarFacturasEnPicking.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            } else {
//                limpiar();
//                liberado = false;
//                jTabbedPane1.setEnabled(false);
//                numeroManifiesto = null;
//                btnCrearManifiesto.setEnabled(false);
//                cbxDestinos.setEnabled(false);
//               
//
//                JOptionPane.showMessageDialog(this, "Esa Ruta está siendo despachada por otro usuario ", "Error", 0);
//               
//
//            }
//        }
//    }
//
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
        if (listaDeFacturasPorManifiesto != null) {
            for (String obj : listaDeFacturasPorManifiesto) {
                if (facturaActual.equals(obj)) {
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
        if (receptor == null) {
            mensaje += "El presente manifiesto no tiene asignado un Conductor \n";
            isValido = false;
        }
        
        if (kilometraje == 0) {
            mensaje += "El kilometraje no es válido \n";
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
        for (String obj : listaDeFacturasPorManifiesto) {
            if (numeroDefactura.equals(obj)) {
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

        //     String rutaDearchivo = numeroManifiesto.getRutArchivofacturasporManifiesto(); //"tmp/tmp_" + numeroManifiesto.codificarManifiesto() + "_FacturasDescargados.txt";
        //    ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDearchivo);
        int fila = jTableFacturasPorManifiesto.getSelectedRow();

        /* Aca se borra la linea que contiene el numero de la factura*/
        // archivo.borrarLinea(fila);
        int i = 1;

        /* Elimina reegistro del Array que contiene las facturas por manifiesto */
        for (String obj : listaDeFacturasPorManifiesto) {
            if (obj == numeroDeFactura) {
                listaDeFacturasPorManifiesto.remove(obj);

                //       numeroManifiesto.setListaFacturasPorManifiesto(listaDeFacturasPorManifiesto);
                break;
            }
            
        }

        /* Elimina reegistro del Array que contiene las CfcturasCamdun del Manifiesto */
//        for (Vst_FacturasPorManifiesto obj : vistaFacturasPorManifiesto) {
//            if (obj.getNumeroFactura().equals(numeroDeFactura)) {
//                CFacturasCamdun fac = new CFacturasCamdun(ini, obj.getNumeroFactura());
//                /*  libera la factura para ser usada por otro usuario */
//                fac.setIsFree(1);
//                fac.liberarFactura(true);
//
//                vistaFacturasPorManifiesto.remove(obj);
////                              numeroManifiesto.setListaCFacturasCamdun(listaDeCFacturasCamdunEnElManifiesto);
//                break;
        //           }
        //       }
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

        //    File fichero = new File(numeroManifiesto.getRutArchivofacturasporManifiesto());

        /* Se arma el array de sentencias SQL para grabar las facturas  descargados */
//        if (fichero.exists()) {
//
//            modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
//
//            for (CFacturasPorManifiesto obj : listaDeFacturasPorManifiesto) {
//                Vst_FacturaCamdun factura = new Vst_FacturaCamdun(ini, obj.getNumeroFactura());
//
//                filaTabla2 = jTableFacturasPorManifiesto.getRowCount();
//                modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);
//                jTableFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
//                jTableFacturasPorManifiesto.setValueAt(factura.getNumeroFactura(), filaTabla2, 1); // numero de factura
//                jTableFacturasPorManifiesto.setValueAt(factura.getNombreDeCliente(), filaTabla2, 2); // nombre del nombreDelCliente
//                jTableFacturasPorManifiesto.setValueAt(nf.format(factura.getValorTotalFactura()), filaTabla2, 3);
//
//            }
        //       }
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
        //   File fichero = new File(numeroManifiesto.getRutArchivofacturasporManifiesto());
        //   fichero.delete();
        // Eliminamos los arrays de facturas
        listaDeFacturasPorManifiesto = null;
        
        lblCantidadFacturas.setText("0");
        
       
    }
    
   
    
}
