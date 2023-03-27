/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas;

import aplicacionlogistica.distribucion.Threads.HiloListadoClientes;
import aplicacionlogistica.distribucion.Threads.HiloListadoConsultadeFacturaBitacora;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloConsultarFacturxxx;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
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
public class FConsultarFacturasPorCliente extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

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
    public CFacturas factura;
    public CClientes cliente;
    JInternalFrame form;
    RowsRenderer rowRenderer = new RowsRenderer(6);

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     */
    public FConsultarFacturasPorCliente(Inicio ini) throws Exception {
        this.ini = ini;
        try {
            initComponents();
            tblFacturasPorCliente.setDefaultRenderer(Object.class, rowRenderer);

            lblCirculoDeProgreso.setVisible(false);
            lblCirculoDeProgreso1.setVisible(false);

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
            Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @param form formulario desde donde se carga la interface gráfica
     * @param factura corrresponde a la factura que se va a consultar
     */
    public FConsultarFacturasPorCliente(Inicio ini, JInternalFrame form, CFacturas factura) throws Exception {
        this.factura = factura;
        this.form = form;
        this.ini = ini;
        try {
            initComponents();

            llenarDatosDeLaVista();

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    public FConsultarFacturasPorCliente(Inicio ini, CFacturas factura) throws Exception {
        this.factura = factura;
        try {
            initComponents();
            this.ini = ini;
            llenarDatosDeLaVista();

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    public FConsultarFacturasPorCliente(Inicio ini, JInternalFrame form, String numeroFactura) throws Exception {
        this.ini = ini;
        try {
            initComponents();

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
                llenarDatosDeLaVista();
            } else {
                JOptionPane.showMessageDialog(null, "Número de Factura no encontrado en el sistema ", "Error", JOptionPane.WARNING_MESSAGE);

            }

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    private void llenarDatosDeLaVista() throws Exception {

        limpiarTodo();

        factura.setListaDetalleFactura(false);
        factura.setListaDeMovimientosBitacora();
        factura.setListaDeMovimientosfactura();
        factura.setListaDeProductosRechazados();

        txtCodigoCliente.setText(factura.getNumeroDeFactura());
        txtDireccionDelCliente.setText(factura.getDireccionDeCliente());
        txtBarrioCliente.setText(factura.getBarrio());

        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText(factura.getNombreDeCliente() + " (" + factura.getCodigoDeCliente() + ")");

        txtTelefonoDelCliente.setText(factura.getTelefonoCliente());

        System.out.println("Aca verifica que hayan movimientos en la factura " + factura.getNumeroDeFactura());

//         llenarTablabitacora();
//         llenarTablaProductosRechazados();       
//        llenarTablaDistribucion();
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
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtCodigoCliente = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtNombreDelCliente = new javax.swing.JTextField();
        txtBarrioCliente = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtTelefonoDelCliente = new javax.swing.JTextField();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtDireccionDelCliente = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        txtParteDelNombreDelCliente = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        lblCirculoDeProgreso1 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        lblCargando = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlEntregas = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblFacturasPorCliente = new javax.swing.JTable();
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

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta por Código del cliente"));

        txtCodigoCliente.setEditable(false);
        txtCodigoCliente.setEnabled(false);
        txtCodigoCliente.setName("numerico"); // NOI18N
        txtCodigoCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCodigoClienteFocusGained(evt);
            }
        });
        txtCodigoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoClienteActionPerformed(evt);
            }
        });
        txtCodigoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoClienteKeyPressed(evt);
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
        jLabel40.setText("Código");

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Barrio");

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Teléfono");

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
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCirculoDeProgreso))
                    .addComponent(txtBarrioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccionDelCliente)
                    .addComponent(txtNombreDelCliente)
                    .addComponent(txtTelefonoDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(txtDireccionDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBarrioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefonoDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta por Parte del nombre del cliente"));

        txtParteDelNombreDelCliente.setEditable(false);
        txtParteDelNombreDelCliente.setEnabled(false);
        txtParteDelNombreDelCliente.setName(""); // NOI18N
        txtParteDelNombreDelCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtParteDelNombreDelClienteFocusGained(evt);
            }
        });
        txtParteDelNombreDelCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParteDelNombreDelClienteActionPerformed(evt);
            }
        });
        txtParteDelNombreDelCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtParteDelNombreDelClienteKeyPressed(evt);
            }
        });

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Nombre Cliente");

        lblCirculoDeProgreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtParteDelNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(lblCirculoDeProgreso1)
                .addGap(57, 57, 57))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtParteDelNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCirculoDeProgreso1))
                .addGap(5, 5, 5))
        );

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código Cliente", "nombre Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblClientes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblClientes);
        if (tblClientes.getColumnModel().getColumnCount() > 0) {
            tblClientes.getColumnModel().getColumn(0).setResizable(false);
            tblClientes.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblClientes.getColumnModel().getColumn(1).setResizable(false);
            tblClientes.getColumnModel().getColumn(1).setPreferredWidth(300);
        }

        lblCargando.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCargando.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblCargando, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(14, 30, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCargando, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lblCargando.setVisible(false);

        pnlEntregas.setAutoscrolls(true);
        pnlEntregas.setDoubleBuffered(false);
        pnlEntregas.setEnabled(false);

        tblFacturasPorCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Fecha", "Cliente", "Dirección", "Valor", "Movimiento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFacturasPorCliente.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblFacturasPorCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFacturasPorClienteMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblFacturasPorCliente);
        if (tblFacturasPorCliente.getColumnModel().getColumnCount() > 0) {
            tblFacturasPorCliente.getColumnModel().getColumn(0).setResizable(false);
            tblFacturasPorCliente.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblFacturasPorCliente.getColumnModel().getColumn(1).setResizable(false);
            tblFacturasPorCliente.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblFacturasPorCliente.getColumnModel().getColumn(2).setResizable(false);
            tblFacturasPorCliente.getColumnModel().getColumn(2).setPreferredWidth(110);
            tblFacturasPorCliente.getColumnModel().getColumn(3).setResizable(false);
            tblFacturasPorCliente.getColumnModel().getColumn(3).setPreferredWidth(250);
            tblFacturasPorCliente.getColumnModel().getColumn(4).setResizable(false);
            tblFacturasPorCliente.getColumnModel().getColumn(4).setPreferredWidth(200);
            tblFacturasPorCliente.getColumnModel().getColumn(5).setResizable(false);
            tblFacturasPorCliente.getColumnModel().getColumn(5).setPreferredWidth(200);
            tblFacturasPorCliente.getColumnModel().getColumn(6).setResizable(false);
            tblFacturasPorCliente.getColumnModel().getColumn(6).setPreferredWidth(150);
        }

        javax.swing.GroupLayout pnlEntregasLayout = new javax.swing.GroupLayout(pnlEntregas);
        pnlEntregas.setLayout(pnlEntregasLayout);
        pnlEntregasLayout.setHorizontalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1079, Short.MAX_VALUE)
        );
        pnlEntregasLayout.setVerticalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Facturas", pnlEntregas);

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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addGap(5, 5, 5))
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

    private void tblFacturasPorClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFacturasPorClienteMouseClicked
        /* Se identifica la fila seleccionada*/

//         int filaSelleccionada = tblFacturasPorCliente.getSelectedRow();
//        consultarLafactura(tblFacturasPorCliente.getValueAt(filaSelleccionada, 1).toString());
        new Thread(new HiloConsultarFacturxxx(ini, this)).start();

    }//GEN-LAST:event_tblFacturasPorClienteMouseClicked

    private void txtTelefonoDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteKeyPressed

    private void txtTelefonoDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteActionPerformed

    private void txtTelefonoDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteFocusGained

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

    private void txtCodigoClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoClienteKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            llenarDatosCliente();

        }

    }//GEN-LAST:event_txtCodigoClienteKeyPressed

    public void llenarDatosCliente() {
        txtBarrioCliente.setText("");

        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText("");
        txtTelefonoDelCliente.setText("");
        txtDireccionDelCliente.setText("");
        limpiarTablaFacturasPorCliente();

        //(Inicio ini,FConsultarFacturasPorCliente  form)
        new Thread(new HiloConsultarFacturasPorCliente(ini, this)).start();
        //consultarCliente();
    }

    private void consultarCliente() {

        lblCirculoDeProgreso.setVisible(true);

        try {
            /*Crea objeto desde la BBDD remota*/
            cliente = new CClientes(ini, txtCodigoCliente.getText().trim(), true);
            try {

                if (this.cliente.getCodigoInterno() != null) {
                    limpiarTablaFacturasPorCliente();

                    txtDireccionDelCliente.setText(cliente.getDireccion());
                    txtBarrioCliente.setText(cliente.getBarrio());
                    txtNombreDelCliente.setText(cliente.getNombreDeCliente());
                    txtTelefonoDelCliente.setText(cliente.getCelularCliente());

                    /*Consulta  las facturas del servidor remoto*/
                    cliente.setListaDeFacturasPorCliente(true);

                    llenarTablaFacturasPorCliente();
                    /*Hilo que recupera los movimientos de la factura */
                    lblCirculoDeProgreso.setVisible(false);
                    //  new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente no existe en el sistema", "Error", JOptionPane.WARNING_MESSAGE);

                }

            } catch (Exception ex) {

                Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void txtCodigoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoClienteActionPerformed

    private void txtCodigoClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoClienteFocusGained
        txtCodigoCliente.setSelectionStart(0);
        txtCodigoCliente.setSelectionEnd(txtCodigoCliente.getText().length());
    }//GEN-LAST:event_txtCodigoClienteFocusGained

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

    private void txtParteDelNombreDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtParteDelNombreDelClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            limpiarTablaCliente();
            new Thread(new HiloListadoClientes(ini, txtParteDelNombreDelCliente.getText(), tblClientes, lblCirculoDeProgreso1)).start();
        }
    }//GEN-LAST:event_txtParteDelNombreDelClienteKeyPressed

    private void txtParteDelNombreDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParteDelNombreDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParteDelNombreDelClienteActionPerformed

    private void txtParteDelNombreDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtParteDelNombreDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParteDelNombreDelClienteFocusGained

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        /* Se identifica la fila seleccionada*/
        int filaSelleccionada = tblClientes.getSelectedRow();
//
//        /* Se identifica el codigo del cliente */
        txtCodigoCliente.setText(tblClientes.getValueAt(filaSelleccionada, 0).toString());
//        txtBarrioCliente.setText(title);
//        consultarCliente();

        llenarDatosCliente();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();


    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void nuevo() {
        limpiarTodo();
        txtCodigoCliente.setEnabled(true);
        txtCodigoCliente.setEditable(true);
        txtParteDelNombreDelCliente.setEnabled(true);
        txtParteDelNombreDelCliente.setEditable(true);

        txtParteDelNombreDelCliente.requestFocus();
        txtParteDelNombreDelCliente.requestFocus();
    }

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        limpiarTodo();
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
        limpiarTodo();
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_jBtnExitActionPerformed

    public synchronized void llenarTablaFacturasPorCliente() throws Exception {
        if (cliente.getListaDeFacturasPorCliente() != null) {

            modelo2 = (DefaultTableModel) tblFacturasPorCliente.getModel();
            for (CFacturas obj : cliente.getListaDeFacturasPorCliente()) {
                filaTabla2 = tblFacturasPorCliente.getRowCount();

                switch (obj.getEstadoFactura()) {
                    case 1:
                        obj.setNombreEstadoFactura("Sin Movimiento");
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;

                }
                modelo2.addRow(new Object[tblFacturasPorCliente.getRowCount()]);
                tblFacturasPorCliente.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblFacturasPorCliente.setValueAt(obj.getNumeroDeFactura(), filaTabla2, 1); // numero de factura
                tblFacturasPorCliente.setValueAt(obj.getFechaDeVenta(), filaTabla2, 2); // numero de factura
                tblFacturasPorCliente.setValueAt(obj.getNombreDeCliente(), filaTabla2, 3); // numero de factura
                tblFacturasPorCliente.setValueAt((obj.getDireccionDeCliente()), filaTabla2, 4); // numero de factura
                tblFacturasPorCliente.setValueAt(nf.format(obj.getValorTotalFactura()), filaTabla2, 5); // numero de factura
                tblFacturasPorCliente.setValueAt(obj.getNombreEstadoFactura(), filaTabla2, 6); // numero de factura

            }

        }

    }

    private void llenarTablaClientes() throws Exception {
        if (ini.getListaDeClientes() != null) {

            modelo2 = (DefaultTableModel) tblClientes.getModel();
            for (CClientes obj : ini.getListaDeClientes()) {
                filaTabla2 = tblClientes.getRowCount();

                modelo2.addRow(new Object[tblClientes.getRowCount()]);

                tblClientes.setValueAt(obj.getCodigoInterno(), filaTabla2, 0); // numero de factura
                tblClientes.setValueAt(obj.getNombreDeCliente(), filaTabla2, 1); // numero de factura

            }

        }

    }

//    public synchronized  void llenarTablaDistribucion() throws Exception {
//        if (listaDeMovimientosEnDistribucion != null) {
//            // DefaultTableModel modelo = (DefaultTableModel) tblMovimientos.getModel();
//           
//
//            for (Vst_FacturasPorManifiesto obj : listaDeMovimientosEnDistribucion) {
//                filaSeleccionada = tblMovimientos.getRowCount();
//
//                modelo.addRow(new Object[tblMovimientos.getRowCount()]);
//                tblMovimientos.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
//                tblMovimientos.setValueAt(obj.getNumeroManifiesto(), filaSeleccionada, 1); // numero de manifiesto
//                tblMovimientos.setValueAt(obj.getFechaDistribucion(), filaSeleccionada, 2); // fecha de distribucion
//                tblMovimientos.setValueAt(obj.getVehiculo(), filaSeleccionada, 3); // placa del vehiculo
//                tblMovimientos.setValueAt(obj.getNombreConductor(), filaSeleccionada, 4); // nombre del conductor
//                tblMovimientos.setValueAt(obj.getNombreRuta(), filaSeleccionada, 5); // nombre de la ruta
//
//            }
//        }
//
//    }
//
//    public synchronized  void llenarTablabitacora() throws Exception {
//        listaDeMovimientosBitacora = this.factura.getListaDeMovimientosBitacora();
//        
//        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeMovimientosBitacora.size());
//
//        DefaultTableModel modelo = (DefaultTableModel) tblBitacora.getModel();
//        
//        for (CBitacoraFacturas obj : listaDeMovimientosBitacora) {
//            filaSeleccionada = tblBitacora.getRowCount();
//
//            modelo.addRow(new Object[tblBitacora.getRowCount()]);
//           
//            tblBitacora.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
//            tblBitacora.setValueAt(obj.getFechaIng(), filaSeleccionada, 1); // numero de manifiesto
//            tblBitacora.setValueAt(obj.getNumeroDocumento(), filaSeleccionada, 2); // fecha de distribucion
//            tblBitacora.setValueAt(obj.getObservacion(), filaSeleccionada, 3); // placa del vehiculo
//            // tblBitacora.setValueAt(obj.getNombreConductor(), filaSeleccionada, 4); // nombre del conductor
//
//        }
//
//    }
//   
//    public synchronized  void llenarTablaProductosRechazados() throws Exception {
//        listaDeProductosRechazados = this.factura.getVstlistaDeProductosRechazados();
//
//        if (listaDeProductosRechazados != null) {
//            System.out.println(new Date() + " acá se calcula la cantidad de productos rechazados =" + listaDeProductosRechazados.size());
//
//            DefaultTableModel modelo = (DefaultTableModel) tblProductosPorFacturaRechazados.getModel();
//
//            for (Vst_ProductosPorFacturaDescargados obj : listaDeProductosRechazados) {
//                filaSeleccionada = tblProductosPorFacturaRechazados.getRowCount();
//
//                modelo.addRow(new Object[tblProductosPorFacturaRechazados.getRowCount()]);
//
//                tblProductosPorFacturaRechazados.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
//                tblProductosPorFacturaRechazados.setValueAt(obj.getCodigoProducto(), filaSeleccionada, 1); // numero de manifiesto
//                tblProductosPorFacturaRechazados.setValueAt(obj.getDescripcionProducto(), filaSeleccionada, 2); // fecha de distribucion
//                tblProductosPorFacturaRechazados.setValueAt(obj.getCantidadRechazada(), filaSeleccionada, 3); // placa del vehiculo
//                tblProductosPorFacturaRechazados.setValueAt(obj.getNombreCausalDeRechazo(), filaSeleccionada, 4); // nombre del conductor
//
//            }
//        }
//
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JMenu borraUnaFila;
    private javax.swing.JPopupMenu borrarFila;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExcel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    public javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCargando;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JLabel lblCirculoDeProgreso1;
    private javax.swing.JPanel pnlEntregas;
    private javax.swing.JPopupMenu pumImagen;
    private javax.swing.JTable tablaDocsVencidos;
    public javax.swing.JTable tblClientes;
    public javax.swing.JTable tblFacturasPorCliente;
    public javax.swing.JTextField txtBarrioCliente;
    public javax.swing.JTextField txtCodigoCliente;
    public javax.swing.JTextField txtDireccionDelCliente;
    public javax.swing.JTextField txtNombreDelCliente;
    public javax.swing.JTextField txtParteDelNombreDelCliente;
    public javax.swing.JTextField txtTelefonoDelCliente;
    // End of variables declaration//GEN-END:variables

    public void limpiarTodo() {

        txtCodigoCliente.setText("");
        txtBarrioCliente.setText("");
        txtNombreDelCliente.setText("");
        txtDireccionDelCliente.setText("");
        txtTelefonoDelCliente.setText("");
        txtCodigoCliente.setEnabled(false);
        txtCodigoCliente.setEditable(false);

        txtParteDelNombreDelCliente.setText("");
        txtParteDelNombreDelCliente.setEnabled(false);
        txtParteDelNombreDelCliente.setEditable(false);
        limpiarTablaFacturasPorCliente();
        //limpiarTablaMovimientos();
        //limpiarTablaBitacora();
    }

    public void limpiarTablaFacturasPorCliente() {

        DefaultTableModel modelo = (DefaultTableModel) tblFacturasPorCliente.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaCliente() {

        DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

//   public void limpiarTablaProductosRechazadosPorFactura() {
//
//        DefaultTableModel modelo = (DefaultTableModel) tblProductosPorFacturaRechazados.getModel();
//        if (modelo.getRowCount() > 0) {
//            int a = modelo.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
//        }
//
//    }
//   
//    public void limpiarTablaDescuentos() {
//
//        DefaultTableModel modelo = (DefaultTableModel) tblDescuentos.getModel();
//        if (modelo.getRowCount() > 0) {
//            int a = modelo.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
//        }
//
//    } 
//            
//    public void limpiarTablaRecogidas() {
//
//        DefaultTableModel modelo = (DefaultTableModel) tblRecogidas.getModel();
//        if (modelo.getRowCount() > 0) {
//            int a = modelo.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
//        }
//
//    }
//   
// public void limpiarTablaMovimientos() {
//
//        DefaultTableModel modelo = (DefaultTableModel) tblMovimientos.getModel();
//        if (modelo.getRowCount() > 0) {
//            int a = modelo.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
//        }
//
//    } 
//             public void limpiarTablaBitacora() {
//
//        DefaultTableModel modelo = (DefaultTableModel) tblBitacora.getModel();
//        if (modelo.getRowCount() > 0) {
//            int a = modelo.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
//        }
//
//    } 
    public void consultarLafactura(String numeroFactura) {
        CFacturas facturaActual = null;

        try {
            for (CFacturas fact : cliente.getListaDeFacturasPorCliente()) {
                if (fact.getNumeroDeFactura().equals(numeroFactura)) {
                    facturaActual = fact;
                }
            }

            if (facturaActual != null) {

                FConsultarFacturasRemoto formulario = new FConsultarFacturasRemoto(ini, facturaActual);
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
                JOptionPane.showInternalMessageDialog(this, "La factura no aparece en el sistema", "Factura no visible ", 2);
            }

        } catch (Exception ex) {
            Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
