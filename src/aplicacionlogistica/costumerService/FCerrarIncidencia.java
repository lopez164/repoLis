/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService;

import aplicacionlogistica.distribucion.Threads.HiloListadoConsultadeFacturaBitacora;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.objetos.CGestiones;
import aplicacionlogistica.costumerService.objetos.CSvcIncidencias;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author VLI_488
 */
public class FCerrarIncidencia extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    CUsuarios user;

    Inicio ini = null;

    //public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
   // DefaultTableModel modelo1, modelo2;

    //Double valorTotalAConsignar = 0.0;
    boolean cargado = false;
    // boolean tieneManifiestosAnteriores = false;
    // boolean tieneMnifiestosAsigndos = false;
    boolean nuevo = false;
    boolean actualizar = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean completado = false;
   // private StaticMaps ObjStaticMaps = new StaticMaps();

//    int filaTabla2;
//    int indiceLista = 0;
//    int columna = 0;
    //String mensaje = null;
    public CFacturas factura;
    //JInternalFrame form;
    public CSvcIncidencias incidencia;
    CGestiones gestion;
    FIncidenciasSvC form1;
    //NewJDialog form2;

  
    //public ArrayList<Vst_ProductosPorFactura> listaProductosPorFactura;
    //public ArrayList<Vst_FacturasPorManifiesto> listaDeMovimientosEnDistribucion;
    //public ArrayList<CBitacoraFacturas> listaDeMovimientosBitacora;
    //public ArrayList<Vst_ProductosPorFacturaDescargados> listaDeProductosRechazados;

   
    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @param incidencia
     */
    public FCerrarIncidencia(Inicio ini, CSvcIncidencias incidencia, FIncidenciasSvC form) throws Exception {

        try {
            initComponents();
            this.incidencia = incidencia;
            this.form1 = form;
//            lblCirculoDeProgreso.setVisible(false);
            this.ini = ini;
            this.user = ini.getUser();
//            txtNumeroFactura.setEnabled(true);
//            txtNumeroFactura.setEditable(false);
//            txtNumeroFactura.setText(incidencia.getNumeroFactura());
            txtVehiculo.setText(incidencia.getObjManifiesto().getVehiculo());
            txtConductor.setText(incidencia.getObjManifiesto().getNombreConductor() + " " + incidencia.getObjManifiesto().getApellidosConductor() );
            txtVendedor.setText(incidencia.getObjFacturaCamdun().getVendedor());
            txtCliente.setText(incidencia.getObjFacturaCamdun().getNombreDeCliente() + "  (" + incidencia.getObjFacturaCamdun().getCodigoDeCliente()+ ")");
            txtDireccionCliente.setText(incidencia.getObjFacturaCamdun().getDireccionDeCliente());
            txtBarrio.setText(incidencia.getObjFacturaCamdun().getBarrio());
            txtCiudad.setText(incidencia.getObjFacturaCamdun().getCiudad());
            txtCelularConductor.setText(incidencia.getObjManifiesto().getTelefonoConductor());
            txtCelularVendedor.setText(incidencia.getObjFacturaCamdun().getTelefonoVendedor());
            txtCelularCliente.setText(incidencia.getObjFacturaCamdun().getTelefonoCliente());

            consultarFactura();
//         
            txtNumeroIncidencia.setText(incidencia.getConsecutivo());
            txtNumeroFactura.setText(incidencia.getNumeroFactura());

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
            Logger.getLogger(FCerrarIncidencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;
//        cbxTiposGestiones.removeAllItems();
//        for (CtiposGestiones obj : ini.getListaDeTiposgestione()) {
//            cbxTiposGestiones.addItem(obj.getNombreTipoDeGestion());
//        }

    }

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @param form formulario desde donde se carga la interface gráfica
     * @param factura corrresponde a la factura que se va a consultar
     */
//    public FCerrarIncidencia(Inicio ini, JInternalFrame form, CFacturas factura) throws Exception {
//        this.factura = factura;
//        this.form = form;
//
//        try {
//            initComponents();
//            this.ini = ini;
//            llenarDatosDeLaVista(this.factura);
//
//        } catch (Exception ex) {
//            System.out.println("Error en FConsultarFacturas ");
//            Logger.getLogger(FCerrarIncidencia.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        cargado = true;
//
//    }

    public FCerrarIncidencia(Inicio ini, CFacturas factura) throws Exception {
        this.factura = factura;
        try {
            initComponents();
            this.ini = ini;
            llenarDatosDeLaVista(this.factura);

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FCerrarIncidencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    public FCerrarIncidencia(Inicio ini, JInternalFrame form, String numeroFactura) throws Exception {

        try {
            initComponents();
            this.ini = ini;
//            txtNumeroFactura.setEnabled(true);
//            txtNumeroFactura.setEditable(true);
//            txtNumeroFactura.requestFocus();
//            
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
            Logger.getLogger(FCerrarIncidencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    private void llenarDatosDeLaVista(CFacturas factura1) throws Exception {

        limpiarTodo();
        this.factura = factura1;
       
//        listaProductosPorFactura = new ArrayList();
//        listaDeMovimientosEnDistribucion = new ArrayList();
//        listaDeMovimientosBitacora = new ArrayList();

//        listaDeProductosRechazados = new ArrayList<>();

//        int i = 0;
//        for (Vst_FacturasPorManifiesto obj : listaDeMovimientosEnDistribucion) {
//
//            if (i == (listaDeMovimientosEnDistribucion.size() - 1)) {
//                listaDeProductosRechazados = factura.getVstlistaDeProductosRechazados(factura.getNumeroFactura(), obj.getNumeroManifiesto());
//            }
//            i++;
//        }
//
//        listaDeMovimientosBitacora = this.factura.getListaDeMovimientosBitacora(factura.getNumeroFactura());

        /*Hilo que recupera los movimientos de la factura */
        // new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this.factura)).start();
//        txtNumeroFactura.setText(vistafac.getNumeroFactura());
//        txtDireccionDelCliente.setText(vistafac.getDireccionDeCliente());
//        txtBarrioCliente.setText(vistafac.getBarrio());
//        txtFechaVenta.setText("" + vistafac.getFechaDeVenta());
//
//        /* nombre del cliente y el código entre parétesis*/
//        txtNombreDelCliente.setText(vistafac.getNombreDeCliente() + " (" + vistafac.getCliente() + ")");
//        txtNombreDelVendedor.setText(vistafac.getVendedor());
//        txtTelefonoDelCliente.setText(vistafac.getTelefonoCliente());
//        llenarTablaProductosPorFactura();
//        
        System.out.println("Aca verifica que hayan movimientos en la factura " + factura.getNumeroDeFactura());

        completado = false;

//        lblCirculoDeProgreso.setVisible(false);
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
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtVehiculo = new javax.swing.JTextField();
        txtConductor = new javax.swing.JTextField();
        txtCliente = new javax.swing.JTextField();
        txtVendedor = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        txtBarrio = new javax.swing.JTextField();
        txtCiudad = new javax.swing.JTextField();
        txtCelularConductor = new javax.swing.JTextField();
        txtCelularVendedor = new javax.swing.JTextField();
        txtCelularCliente = new javax.swing.JTextField();
        txtNumeroFactura = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNumeroIncidencia = new javax.swing.JTextField();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        btnCierreDeIncidencia = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        txtNombreEmpleado = new javax.swing.JTextField();
        btnCierreDeIncidencia1 = new javax.swing.JButton();

        agregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/logo.jpg"))); // NOI18N
        agregarImagen.setText("Agregar Imagen Empleado");
        agregarImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarImagenMouseClicked(evt);
            }
        });
        pumImagen.add(agregarImagen);

        borraUnaFila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/borrar.png"))); // NOI18N
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
        setTitle("Formulario de Cierre de Incidencias");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/turbo_64x64.png"))); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
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

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la Factura"));

        jLabel1.setText("Vehiculo");

        jLabel2.setText("Conductor");

        jLabel5.setText("Vendedor");

        jLabel7.setText("Cliente");

        jLabel8.setText("Direccion");

        jLabel10.setText("Barrio");

        jLabel11.setText("Ciudad");

        txtVehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVehiculoActionPerformed(evt);
            }
        });

        txtDireccionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionClienteActionPerformed(evt);
            }
        });

        txtCelularVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCelularVendedorActionPerformed(evt);
            }
        });

        txtNumeroFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroFacturaActionPerformed(evt);
            }
        });

        jLabel3.setText("Factura");

        jLabel4.setText("Inciedencia");

        txtNumeroIncidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroIncidenciaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel3)))))
                .addGap(22, 22, 22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtConductor)
                    .addComponent(txtVendedor)
                    .addComponent(txtCliente)
                    .addComponent(txtDireccionCliente)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtBarrio, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCiudad, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtNumeroIncidencia, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNumeroFactura, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtVehiculo, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(44, 44, 44)))
                        .addGap(144, 144, 144)))
                .addGap(45, 45, 45)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCelularConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCelularVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCelularCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(159, 159, 159))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNumeroIncidencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtConductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCelularConductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCelularVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCelularCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnCierreDeIncidencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/Yes.png"))); // NOI18N
        btnCierreDeIncidencia.setEnabled(false);
        btnCierreDeIncidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCierreDeIncidenciaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCierreDeIncidencia)
                .addGap(388, 388, 388))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCierreDeIncidencia, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(123, 123, 123))
        );

        jTabbedPane3.addTab("Cierre de Incidencia", jPanel7);

        txtNombreEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreEmpleadoActionPerformed(evt);
            }
        });
        txtNombreEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreEmpleadoKeyPressed(evt);
            }
        });

        btnCierreDeIncidencia1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/Yes.png"))); // NOI18N
        btnCierreDeIncidencia1.setEnabled(false);
        btnCierreDeIncidencia1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCierreDeIncidencia1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCierreDeIncidencia1)
                    .addComponent(txtNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(391, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(txtNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCierreDeIncidencia1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(156, 156, 156))
        );

        jTabbedPane3.addTab("Cambiar Responsable", jPanel9);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jTabbedPane3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        jTabbedPane2.addTab("Datos de la Incidencia", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
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

// this.form1.fGestion=false;
    }//GEN-LAST:event_formInternalFrameClosing

    private void consultarFactura() {
        try {
//            txtDireccionDelCliente.setText("");
//            txtBarrioCliente.setText("");
//            txtFechaVenta.setText("");
            /* nombre del cliente y el código entre parétesis*/
//            txtNombreDelCliente.setText("");
//            txtNombreDelVendedor.setText("");
//            txtTelefonoDelCliente.setText("");
//            limpiarTablaProductosPorFactura();
//            limpiarTablaBitacora();
//            limpiarTablaMovimientos();
//            limpiarTablaProductosRechazadosPorFactura();
//            limpiarTablaDescuentos();
//            limpiarTablaRecogidas();
//            
//            this.factura = new CFacturas(ini, txtNumeroFactura.getText().trim());

            if (this.factura.getNumeroDeFactura() != null) {
//                lblCirculoDeProgreso.setVisible(true);

                //llenarDatosDeLaVista(factura);
                /*Hilo que recupera los movimientos de la factura */
//                new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();
            } else {
                JOptionPane.showMessageDialog(this, "Número de Factura no encontrado en el sistema ", "Error", JOptionPane.WARNING_MESSAGE);

            }

        } catch (Exception ex) {
            Logger.getLogger(FCerrarIncidencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void txtVehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVehiculoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVehiculoActionPerformed

    private void txtDireccionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteActionPerformed

    private void txtCelularVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCelularVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCelularVendedorActionPerformed

    private void btnCierreDeIncidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCierreDeIncidenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCierreDeIncidenciaActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        this.form1.fGestion = false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void btnCierreDeIncidencia1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCierreDeIncidencia1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCierreDeIncidencia1ActionPerformed

    private void txtNumeroFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFacturaActionPerformed

    private void txtNumeroIncidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroIncidenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroIncidenciaActionPerformed

    private void txtNombreEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreEmpleadoActionPerformed

    private void txtNombreEmpleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreEmpleadoKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            //if (manifiestoActual.getEstadoManifiesto() < 2) {
//            FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(ini, this);
//            this.getParent().add(form);
//            form.toFront();
//            form.setClosable(true);
//            form.setVisible(true);
//            form.setTitle("Formulario para buscar Empleados por apellidos");
//            form.txtApellidosPersona.requestFocus();
//            form.show();
            //} 
        }
    }//GEN-LAST:event_txtNombreEmpleadoKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JMenu borraUnaFila;
    private javax.swing.JPopupMenu borrarFila;
    private javax.swing.JButton btnCierreDeIncidencia;
    private javax.swing.JButton btnCierreDeIncidencia1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JPopupMenu pumImagen;
    private javax.swing.JTable tablaDocsVencidos;
    private javax.swing.JTextField txtBarrio;
    private javax.swing.JTextField txtCelularCliente;
    private javax.swing.JTextField txtCelularConductor;
    private javax.swing.JTextField txtCelularVendedor;
    private javax.swing.JTextField txtCiudad;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtConductor;
    private javax.swing.JTextField txtDireccionCliente;
    public javax.swing.JTextField txtNombreEmpleado;
    private javax.swing.JTextField txtNumeroFactura;
    private javax.swing.JTextField txtNumeroIncidencia;
    private javax.swing.JTextField txtVehiculo;
    private javax.swing.JTextField txtVendedor;
    // End of variables declaration//GEN-END:variables

    public void limpiarTodo() {
//        listaProductosPorFactura = new ArrayList();
//        listaDeMovimientosEnDistribucion = new ArrayList();
//
//        listaProductosPorFactura = null;

//        txtNumeroFactura.setText("");
//        txtBarrioCliente.setText("");
//        txtFechaVenta.setText("");
//        /* nombre del cliente y el código entre parétesis*/
//        txtNombreDelCliente.setText("");
//        txtNombreDelVendedor.setText("");
//        txtTelefonoDelCliente.setText("");
//        
//        limpiarTablaProductosPorFactura();
//        limpiarTablaMovimientos();
//        limpiarTablaBitacora();
//        
    }

}
