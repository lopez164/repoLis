/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.objetos.CSvcIncidencias;
import aplicacionlogistica.costumerService.objetos.CcontrolClass;
import aplicacionlogistica.distribucion.objetos.CBitacoraFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFacturaDescargados;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lelopez
 */
public class NewJDialog extends javax.swing.JDialog {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    List<CSvcIncidencias> listaDeIncidencias;
    CcontrolClass controlador;
    PrincipalUsuarioCostumerService formPPal;
    Inicio ini = null;

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    DefaultTableModel modelo1, modelo2;

    public boolean cargado = false;
    public boolean fGestion = false;
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
    public CFacturas factura;
    JInternalFrame form;

    public Vst_Factura vistafac;
    public List<Vst_ProductosPorFactura> listaProductosPorFactura;
    public List<Vst_FacturasPorManifiesto> listaDeMovimientosEnDistribucion;
    public List<CBitacoraFacturas> listaDeMovimientosBitacora;
    public List<Vst_ProductosPorFacturaDescargados> listaDeProductosRechazados;

    
    /**
     * Creates new form NewJDialog
     * @param parent
     * @param modal
     * @param ini
     */
    public NewJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /**
     * Creates new form NewJDialog
     * @param parent
     * @param modal
     * @param ini
     */
    public NewJDialog(java.awt.Frame parent, boolean modal,Inicio ini) {
        super(parent, modal);
        try {
            initComponents();

            lblCirculoDeProgreso.setVisible(false);
            this.ini = ini;
            this.formPPal = formPPal;
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
            cargado = true;
            Date fecha = new Date();
            controlador = new CcontrolClass(ini, fecha);

            controlador.getListaDeIncidencias(fecha);
            listaDeIncidencias = controlador.getListaDeIncidencias();
            llenarTablaIncidencias();
            
        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuIncidencias = new javax.swing.JPopupMenu();
        jMenu1 = new javax.swing.JMenu();
        mostrarIncidencia = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaIncidencias = new javax.swing.JTable();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
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
        lblValorRecaudado = new javax.swing.JLabel();
        lblPrecioFactura2 = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtDireccionDelCliente = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlEntregas = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProductosPorFactura = new javax.swing.JTable();
        lblValorTotalFactura = new javax.swing.JLabel();
        pnlMovimientos = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblBitacora = new javax.swing.JTable();
        btnDescargarRechazoTotal2 = new javax.swing.JButton();
        pnlDistribucion = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblMovimientos = new javax.swing.JTable();
        pnlRechazosParciales = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblProductosPorFacturaRechazados = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblDescuentos = new javax.swing.JTable();
        pnlRecogidas = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblRecogidas = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        txtNumeroDeSoporte = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtFacturaAfectada = new javax.swing.JTextField();
        btnDescargarRechazoTotal1 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        txtValorRecogida = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtValorDescontado = new javax.swing.JTextField();

        jMenu1.setText("Seleccionar Incidencia");

        mostrarIncidencia.setText("Gestionar Incidencia");
        mostrarIncidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarIncidenciaActionPerformed(evt);
            }
        });
        jMenu1.add(mostrarIncidencia);

        menuIncidencias.add(jMenu1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/16x16/Refresh.png"))); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        tablaIncidencias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "item", "Incidencia #", "Movimiento", "Causal", "Estado", "Factura", "Manifiesto", "vehiculo", "Conductor", "Vendedor", "Valor Factura"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaIncidencias.setComponentPopupMenu(menuIncidencias);
        jScrollPane1.setViewportView(tablaIncidencias);

        jRadioButton1.setText("Incidencias abiertas");

        jRadioButton2.setText("Incidencias Cerradas");

        jRadioButton3.setText("Todas las incidencias");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton3))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1336, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Incidencias", jPanel4);

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

        lblValorRecaudado.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblValorRecaudado.setText("$.");

        lblPrecioFactura2.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        lblPrecioFactura2.setText("Total Recaudado");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblValorRecaudado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(lblPrecioFactura2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(lblValorRecaudado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPrecioFactura2)
                .addGap(15, 15, 15))
        );

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/circuloDeprogreso.gif"))); // NOI18N

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
                .addGap(26, 26, 26)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDireccionDelCliente)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCirculoDeProgreso))
                            .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBarrioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreDelVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(40, 40, 40)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCirculoDeProgreso)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDireccionDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBarrioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefonoDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDelVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
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
        jScrollPane7.setViewportView(tblProductosPorFactura);

        lblValorTotalFactura.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblValorTotalFactura.setText("$.");

        javax.swing.GroupLayout pnlEntregasLayout = new javax.swing.GroupLayout(pnlEntregas);
        pnlEntregas.setLayout(pnlEntregasLayout);
        pnlEntregasLayout.setHorizontalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1342, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEntregasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblValorTotalFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlEntregasLayout.setVerticalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblValorTotalFactura)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Productos", pnlEntregas);

        tblBitacora.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "item", "fecha", "documento", "causal-obs.", "valor Descto."
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
        jScrollPane6.setViewportView(tblBitacora);

        btnDescargarRechazoTotal2.setText("Descargar");
        btnDescargarRechazoTotal2.setEnabled(false);
        btnDescargarRechazoTotal2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarRechazoTotal2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMovimientosLayout = new javax.swing.GroupLayout(pnlMovimientos);
        pnlMovimientos.setLayout(pnlMovimientosLayout);
        pnlMovimientosLayout.setHorizontalGroup(
            pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMovimientosLayout.createSequentialGroup()
                .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMovimientosLayout.createSequentialGroup()
                        .addGap(0, 1243, Short.MAX_VALUE)
                        .addComponent(btnDescargarRechazoTotal2))
                    .addGroup(pnlMovimientosLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlMovimientosLayout.setVerticalGroup(
            pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMovimientosLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDescargarRechazoTotal2)
                .addContainerGap())
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
        jScrollPane8.setViewportView(tblMovimientos);

        pnlDistribucion.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 240));

        jTabbedPane1.addTab("Distribución", pnlDistribucion);

        pnlRechazosParciales.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
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
        jScrollPane9.setViewportView(tblProductosPorFacturaRechazados);

        javax.swing.GroupLayout pnlRechazosParcialesLayout = new javax.swing.GroupLayout(pnlRechazosParciales);
        pnlRechazosParciales.setLayout(pnlRechazosParcialesLayout);
        pnlRechazosParcialesLayout.setHorizontalGroup(
            pnlRechazosParcialesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1322, Short.MAX_VALUE)
        );
        pnlRechazosParcialesLayout.setVerticalGroup(
            pnlRechazosParcialesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRechazosParcialesLayout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 55, Short.MAX_VALUE))
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
        jScrollPane10.setViewportView(tblDescuentos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 1342, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Descuentos", jPanel3);

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
        jScrollPane5.setViewportView(tblRecogidas);

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

        btnDescargarRechazoTotal1.setText("Descargar");
        btnDescargarRechazoTotal1.setEnabled(false);
        btnDescargarRechazoTotal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarRechazoTotal1ActionPerformed(evt);
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
                                .addContainerGap()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 688, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGap(0, 642, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlRecogidasLayout.setVerticalGroup(
            pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRecogidasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtNumeroDeSoporte, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtFacturaAfectada, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtValorRecogida, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValorDescontado, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(btnDescargarRechazoTotal1)
                .addContainerGap())
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
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Consulta de Facturas", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 633, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumeroFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroFacturaFocusGained
        txtNumeroFactura.setSelectionStart(0);
        txtNumeroFactura.setSelectionEnd(txtNumeroFactura.getText().length());
    }//GEN-LAST:event_txtNumeroFacturaFocusGained

    private void txtNumeroFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFacturaActionPerformed

    private void txtNumeroFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroFacturaKeyPressed

//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//
//            try {
//                txtDireccionDelCliente.setText("");
//                txtBarrioCliente.setText("");
//                txtFechaVenta.setText("");
//                /* nombre del cliente y el código entre parétesis*/
//                txtNombreDelCliente.setText("");
//                txtNombreDelVendedor.setText("");
//                txtTelefonoDelCliente.setText("");
//                limpiarTablaProductosPorFactura();
//                limpiarTablaBitacora();
//                limpiarTablaMovimientos();
//                limpiarTablaProductosRechazadosPorFactura();
//                limpiarTablaDescuentos();
//                limpiarTablaRecogidas();
//
//                this.factura=new CFacturas(ini, txtNumeroFactura.getText().trim());
//
//                if(this.factura.getNumeroFactura() != null ){
//                    lblCirculoDeProgreso.setVisible(true);
//
//                    //llenarDatosDeLaVista(factura);
//                    /*Hilo que recupera los movimientos de la factura */
//                    // new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();
//
//                }else{
//                    JOptionPane.showMessageDialog(this, "Número de Factura no encontrado en el sistema " , "Error", JOptionPane.WARNING_MESSAGE);
//
//                }
//
//            } catch (Exception ex) {
//                Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
    }//GEN-LAST:event_txtNumeroFacturaKeyPressed

    private void txtNombreDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelClienteFocusGained

    private void txtNombreDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelClienteActionPerformed

    private void txtNombreDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDelClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelClienteKeyPressed

    private void txtBarrioClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarrioClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioClienteFocusGained

    private void txtBarrioClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarrioClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioClienteActionPerformed

    private void txtBarrioClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioClienteKeyPressed

    }//GEN-LAST:event_txtBarrioClienteKeyPressed

    private void txtBarrioClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioClienteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioClienteKeyReleased

    private void txtNombreDelVendedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDelVendedorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelVendedorFocusGained

    private void txtNombreDelVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDelVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelVendedorActionPerformed

    private void txtNombreDelVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDelVendedorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelVendedorKeyPressed

    private void txtTelefonoDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteFocusGained

    private void txtTelefonoDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteActionPerformed

    private void txtTelefonoDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteKeyPressed

    private void txtFechaVentaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFechaVentaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaVentaFocusGained

    private void txtFechaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaVentaActionPerformed

    private void txtFechaVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaVentaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaVentaKeyPressed

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

    private void tblProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaMouseClicked

    private void tblBitacoraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBitacoraMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBitacoraMouseClicked

    private void btnDescargarRechazoTotal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarRechazoTotal2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDescargarRechazoTotal2ActionPerformed

    private void tblMovimientosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMovimientosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblMovimientosMouseClicked

    private void tblProductosPorFacturaRechazadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaRechazadosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaRechazadosMouseClicked

    private void tblDescuentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDescuentosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDescuentosMouseClicked

    private void tblRecogidasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRecogidasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblRecogidasMouseClicked

    private void txtNumeroDeSoporteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeSoporteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeSoporteFocusGained

    private void txtNumeroDeSoporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeSoporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeSoporteActionPerformed

    private void txtNumeroDeSoporteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeSoporteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtFacturaAfectada.requestFocus();
        }
    }//GEN-LAST:event_txtNumeroDeSoporteKeyPressed

    private void txtFacturaAfectadaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFacturaAfectadaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFacturaAfectadaFocusGained

    private void txtFacturaAfectadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFacturaAfectadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFacturaAfectadaActionPerformed

    private void txtFacturaAfectadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaAfectadaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValorRecogida.requestFocus();
        }
    }//GEN-LAST:event_txtFacturaAfectadaKeyPressed

    private void btnDescargarRechazoTotal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarRechazoTotal1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDescargarRechazoTotal1ActionPerformed

    private void txtValorRecogidaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorRecogidaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorRecogidaFocusGained

    private void txtValorRecogidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorRecogidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorRecogidaActionPerformed

    private void txtValorRecogidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorRecogidaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValorRecogida.setText(txtValorRecogida.getText().trim().replace(",", "."));
            txtValorDescontado.requestFocus();
        }
    }//GEN-LAST:event_txtValorRecogidaKeyPressed

    private void txtValorDescontadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorDescontadoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorDescontadoFocusGained

    private void txtValorDescontadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorDescontadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorDescontadoActionPerformed

    private void txtValorDescontadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorDescontadoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValorDescontado.setText(txtValorDescontado.getText().trim().replace(",", "."));

        }
    }//GEN-LAST:event_txtValorDescontadoKeyPressed

    private void mostrarIncidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarIncidenciaActionPerformed
        /*Si hay una vista de gestion abierta sale de la funcion*/
        if(fGestion){
            JOptionPane.showMessageDialog(this, "Ya hay una gestion abierta " , "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        fGestion=true;
        CSvcIncidencias incidencia = null;
        try {
            /* se identifica la fila seleccionada */
            int fila = tablaIncidencias.getSelectedRow();
            for(CSvcIncidencias obj : listaDeIncidencias){
                if(obj.getNumeroFactura().equals(tablaIncidencias.getValueAt(fila, 5))){
                    incidencia=obj;
                    break;
                }
            }

//            FSolucionarIncidencias forma = new FSolucionarIncidencias(ini,incidencia,this);
//
//            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            formPPalCostumerService.escritorio.add(forma);
//            forma.setTitle("Formulario para getion de la incidencia # " + incidencia.getConsecutivo() + " de la factura # "+ incidencia.getNumeroFactura());
//            forma.setLocation(((screenSize.width - forma.getSize().width) / 2), ((screenSize.height - forma.getSize().height) / 2));
//            forma.show();
        } catch (Exception ex) {
            Logger.getLogger(FIncidenciasSvC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mostrarIncidenciaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NewJDialog dialog = new NewJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDescargarRechazoTotal1;
    private javax.swing.JButton btnDescargarRechazoTotal2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    public javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    private javax.swing.JLabel lblPrecioFactura2;
    public javax.swing.JLabel lblValorRecaudado;
    private javax.swing.JLabel lblValorTotalFactura;
    private javax.swing.JPopupMenu menuIncidencias;
    private javax.swing.JMenuItem mostrarIncidencia;
    public javax.swing.JPanel pnlDistribucion;
    private javax.swing.JPanel pnlEntregas;
    private javax.swing.JPanel pnlMovimientos;
    private javax.swing.JPanel pnlRechazosParciales;
    private javax.swing.JPanel pnlRecogidas;
    private javax.swing.JTable tablaIncidencias;
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

    public synchronized void llenarTablaProductosPorFactura() throws Exception {
        if (listaProductosPorFactura != null) {
            double valorFactura = 0;
            modelo2 = (DefaultTableModel) tblProductosPorFactura.getModel();
            for (Vst_ProductosPorFactura obj : listaProductosPorFactura) {
                filaTabla2 = tblProductosPorFactura.getRowCount();

                modelo2.addRow(new Object[tblProductosPorFactura.getRowCount()]);
                tblProductosPorFactura.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), filaTabla2, 1); // numero de factura
                tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), filaTabla2, 2); // numero de factura
                tblProductosPorFactura.setValueAt(obj.getCantidad(), filaTabla2, 3); // numero de factura
                tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), filaTabla2, 4); // numero de factura
                tblProductosPorFactura.setValueAt(nf.format(obj.getValorTotalConIva()), filaTabla2, 5); // numero de factura

                valorFactura += obj.getValorTotalConIva();

            }
            Thread.sleep(1500);
            lblValorTotalFactura.setText(nf.format(valorFactura));
        }

    }

    public synchronized void llenarTablaDistribucion() throws Exception {
        if (listaDeMovimientosEnDistribucion != null) {
            DefaultTableModel modelo = (DefaultTableModel) tblMovimientos.getModel();

            for (Vst_FacturasPorManifiesto obj : listaDeMovimientosEnDistribucion) {
                filaTabla2 = tblMovimientos.getRowCount();

                modelo.addRow(new Object[tblMovimientos.getRowCount()]);
                tblMovimientos.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblMovimientos.setValueAt(obj.getNumeroManifiesto(), filaTabla2, 1); // numero de manifiesto
                tblMovimientos.setValueAt(obj.getFechaDistribucion(), filaTabla2, 2); // fecha de distribucion
                tblMovimientos.setValueAt(obj.getVehiculo(), filaTabla2, 3); // placa del vehiculo
                tblMovimientos.setValueAt(obj.getNombreConductor(), filaTabla2, 4); // nombre del conductor
                tblMovimientos.setValueAt(obj.getNombreRuta(), filaTabla2, 5); // nombre de la ruta

            }
        }

    }

    public synchronized void llenarTablabitacora() throws Exception {
        listaDeMovimientosBitacora = this.factura.getListaDeMovimientosBitacora();

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeMovimientosBitacora.size());

        DefaultTableModel modelo = (DefaultTableModel) tblBitacora.getModel();

        for (CBitacoraFacturas obj : listaDeMovimientosBitacora) {
            filaTabla2 = tblBitacora.getRowCount();

            modelo.addRow(new Object[tblBitacora.getRowCount()]);

            tblBitacora.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
            tblBitacora.setValueAt(obj.getFecha(), filaTabla2, 1); // numero de manifiesto
            tblBitacora.setValueAt(obj.getNumeroDocumento(), filaTabla2, 2); // fecha de distribucion
            tblBitacora.setValueAt(obj.getObservacion(), filaTabla2, 3); // placa del vehiculo
            // tblBitacora.setValueAt(obj.getNombreConductor(), filaTabla2, 4); // nombre del conductor

        }

    }

    public synchronized void llenarTablaProductosRechazados() throws Exception {
       // listaDeProductosRechazados = this.factura.getVstlistaDeProductosRechazados();

        if (listaDeProductosRechazados != null) {
            System.out.println(new Date() + " acá se calcula la cantidad de productos rechazados =" + listaDeProductosRechazados.size());

            DefaultTableModel modelo = (DefaultTableModel) tblProductosPorFacturaRechazados.getModel();

            for (Vst_ProductosPorFacturaDescargados obj : listaDeProductosRechazados) {
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

    public synchronized void llenarTablaIncidencias() throws Exception {
        listaDeIncidencias = this.controlador.getListaDeIncidencias();

        if (listaDeIncidencias != null) {
            System.out.println(new Date() + " acá se calcula la cantidad de productos rechazados =" + listaDeIncidencias.size());

            DefaultTableModel modelo = (DefaultTableModel) tablaIncidencias.getModel();

            for (CSvcIncidencias obj : listaDeIncidencias) {
                filaTabla2 = tablaIncidencias.getRowCount();

                modelo.addRow(new Object[tablaIncidencias.getRowCount()]);

                tablaIncidencias.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tablaIncidencias.setValueAt(obj.getConsecutivo(), filaTabla2, 1); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getNombreTipoDeMovimiento(), filaTabla2, 2); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getNombreCausalDeRechazo(), filaTabla2, 3); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getIdEstado(), filaTabla2, 4); // numero de manifiesto
                tablaIncidencias.setValueAt(obj.getNumeroFactura(), filaTabla2, 5); // fecha de distribucion
                tablaIncidencias.setValueAt(obj.getNumeroManifiesto(), filaTabla2, 6); // placa del vehiculo
                tablaIncidencias.setValueAt(obj.getObjManifiesto().getVehiculo(), filaTabla2, 7); // nombre del conductor
                tablaIncidencias.setValueAt(obj.getObjManifiesto(). getNombreConductor() + " " + obj.getObjManifiesto(). getApellidosConductor(), filaTabla2, 8); // nombre del conductor
                tablaIncidencias.setValueAt(obj.getObjFacturaCamdun().getVendedor(), filaTabla2, 9); // nombre del conductor
                tablaIncidencias.setValueAt(nf.format(obj.getObjFacturaCamdun().getValorTotalFactura()), filaTabla2, 10); // nombre del conductor

            }
        }
    }
}
