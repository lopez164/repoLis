/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService;

import aplicacionlogistica.distribucion.formularios.FDescargarRechazosParciales;
import aplicacionlogistica.distribucion.formularios.FDescargarEntregasTotalesConDescuento;
import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.distribucion.Threads.HiloGuardarManifiestoDescargado;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.FBuscarManifiestos;
import aplicacionlogistica.distribucion.imprimir.ReporteDescargueDeFacturas1;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFacturaDescargados;
import aplicacionlogistica.distribucion.objetos.CRecogidasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CSoportesConsignaciones;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import aplicacionlogistica.distribucion.PrincipalLogistica;
import aplicacionlogistica.distribucion.consultas.minutas.MinutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import mtto.vehiculos.CCarros;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Eduardo López Casanova
 */
public final class FConsultaManifiestosDescargados extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public CManifiestosDeDistribucion manifiestoActual = null;
    public CCarros carro = null;

    public Vst_FacturasDescargadas facturaActual = null;

    public Inicio ini = null;
    //int numeroDeFacturasEnManifiesto = 0;
    //int cantidadFacturasDescargadasManifiesto = 0;
    //int cantidadFacturasPendientesPorDescargar = 0;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    DefaultTableModel modelo1, modelo2, modelo3;

    public Double valorTotalRecaudado = 0.0;
    public Double valorTotalRecogidas = 0.0;
    public Double valorTotalAConsignar = 0.0;
    double valorRecogida;
    double valorDescontado;

    public Double valorConsignado = 0.0;

    boolean cargado = false;
    boolean nuevo = false;
    boolean actualizar = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean estaOcupadoGrabando = false;

    public boolean isFacturaCredito = false;
    public boolean esteManifiestoEsMio = false;

    //int kilometrosEntrada;
    public int kilometrosRecorridos;
    int filaSeleccionada;
    int indiceLista = 0;
    int columna = 0;
    String mensaje = null;
    String nombreDelCliente = null;

    String rutaDeArchivoFacturasDescargadas;
    String rutaDeArchivoProductosDescargados;
    String rutaDeArchivoRecogidasDescargado;
    String rutaDeArchivoFacturasParaVolverAZonificar;
    String rutaDeArchivoSoporteConsignaciones;

    PrincipalLogistica form;

    int tipoMovimientoFactura = 0;

    public List<CFacturasPorManifiesto> listaFacturasPorManifiesto;
    public List<CFacturasPorManifiesto> listaFacturasDescargadas;
    public List<CFacturasPorManifiesto> listaFacturasPendientesPorDescargar;

    public List<CRecogidasPorManifiesto> listaDeRecogidasPorManifiesto;
    public List<CProductosPorFacturaDescargados> listaDeCProductosPorFacturaDescargados;
    public List<String> listaDeFacturasNoManifestadas;
    public List<CSoportesConsignaciones> listaDeSoportesDeConsignaciones;

    public List<CFacturas> listaFacturaTemporal;

    //public Vst_FacturasPorManifiesto facturaDescargada = null;
    String senteciaSqlFacturasDescargadas = null;
    String senteciaSqlProductosPorFacturaDescargados = null;
    String senteciaSqlSoportesConsignaciones = null;

    FDescargarRechazosParciales fDescargueRechazosParciales;
    FDescargarEntregasTotalesConDescuento fDescargueEntregaTotalConDescuento;
    FBuscarManifiestos formulario;

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param form
     */
    public FConsultaManifiestosDescargados(PrincipalLogistica form) {
        this.form = form;
        this.ini = form.ini;
        CargarVista();
    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param ini
     */
    public FConsultaManifiestosDescargados(Inicio ini) {
        this.ini = ini;
        initComponents();

        CargarVista();
    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     */
    public void CargarVista() {
        try {
            try {

                manifiestoActual = null;

                lblCirculoDeProgreso.setVisible(false);

                manager.addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        //como dije, solo las notificaciones del tipo "typed" son las que actualizan los componentes
                        if (e.getID() == KeyEvent.KEY_TYPED) {
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
                System.out.println("Error en dispatchKeyEvent ");
                Logger.getLogger(FConsultaManifiestosDescargados.class.getName()).log(Level.SEVERE, null, ex);
            }
            cargado = true;
        } catch (Exception ex) {
            Logger.getLogger(FConsultaManifiestosDescargados.class.getName()).log(Level.SEVERE, null, ex);
        }

        jBtnMinuta.setEnabled(false);

    }

    /**
     * This method is called from within the constructor to initialize the
     * fCambiarClave. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlEntregas = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableProductosPorFactura = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        txtNumeroManifiesto = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtKilometrosEntrada = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtKilometrosSalida = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtNombreConductor = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtRuta = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFacturasPorVehiculo = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario para Consulta de Manifiesto Descargado");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(1322, 761));
        setMinimumSize(new java.awt.Dimension(111400, 32));
        setVisible(true);
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

        jPanel5.setMaximumSize(new java.awt.Dimension(1322, 761));

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTabbedPane1.setEnabled(false);

        pnlEntregas.setAutoscrolls(true);
        pnlEntregas.setDoubleBuffered(false);
        pnlEntregas.setEnabled(false);

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
            jTableProductosPorFactura.getColumnModel().getColumn(1).setHeaderValue("Ref");
            jTableProductosPorFactura.getColumnModel().getColumn(2).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(2).setPreferredWidth(270);
            jTableProductosPorFactura.getColumnModel().getColumn(3).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTableProductosPorFactura.getColumnModel().getColumn(4).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(100);
            jTableProductosPorFactura.getColumnModel().getColumn(5).setResizable(false);
            jTableProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

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
                .addGap(2, 2, 2)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlEntregasLayout = new javax.swing.GroupLayout(pnlEntregas);
        pnlEntregas.setLayout(pnlEntregasLayout);
        pnlEntregasLayout.setHorizontalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane7)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlEntregasLayout.setVerticalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Productos x factura", pnlEntregas);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta de Manifiesto Descargado"));

        txtNumeroManifiesto.setEnabled(false);
        txtNumeroManifiesto.setInheritsPopupMenu(true);
        txtNumeroManifiesto.setName("numerico"); // NOI18N
        txtNumeroManifiesto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroManifiestoFocusGained(evt);
            }
        });
        txtNumeroManifiesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroManifiestoActionPerformed(evt);
            }
        });
        txtNumeroManifiesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroManifiestoKeyPressed(evt);
            }
        });

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Placa");

        txtPlaca.setEditable(false);
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

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Km. Salida");

        txtKilometrosEntrada.setEditable(false);
        txtKilometrosEntrada.setEnabled(false);
        txtKilometrosEntrada.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKilometrosEntradaFocusGained(evt);
            }
        });
        txtKilometrosEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKilometrosEntradaActionPerformed(evt);
            }
        });
        txtKilometrosEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKilometrosEntradaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKilometrosEntradaKeyReleased(evt);
            }
        });

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Manifiesto");

        txtKilometrosSalida.setEditable(false);
        txtKilometrosSalida.setEnabled(false);
        txtKilometrosSalida.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKilometrosSalidaFocusGained(evt);
            }
        });
        txtKilometrosSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKilometrosSalidaActionPerformed(evt);
            }
        });
        txtKilometrosSalida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKilometrosSalidaKeyPressed(evt);
            }
        });

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Km. Entrada");

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Conductor");

        txtNombreConductor.setEditable(false);
        txtNombreConductor.setEnabled(false);
        txtNombreConductor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreConductorFocusGained(evt);
            }
        });
        txtNombreConductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreConductorActionPerformed(evt);
            }
        });
        txtNombreConductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreConductorKeyPressed(evt);
            }
        });

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Ruta");

        txtRuta.setEditable(false);
        txtRuta.setEnabled(false);
        txtRuta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRutaFocusGained(evt);
            }
        });
        txtRuta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRutaActionPerformed(evt);
            }
        });
        txtRuta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRutaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtNumeroManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtKilometrosEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKilometrosSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNumeroManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addGap(7, 7, 7)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKilometrosEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKilometrosSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableFacturasPorVehiculo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Cliente.", "Mov.", "valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
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
        jTableFacturasPorVehiculo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableFacturasPorVehiculo.setColumnSelectionAllowed(true);
        jTableFacturasPorVehiculo.getTableHeader().setReorderingAllowed(false);
        jTableFacturasPorVehiculo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFacturasPorVehiculoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableFacturasPorVehiculo);
        jTableFacturasPorVehiculo.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTableFacturasPorVehiculo.getColumnModel().getColumnCount() > 0) {
            jTableFacturasPorVehiculo.getColumnModel().getColumn(0).setResizable(false);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(0).setPreferredWidth(45);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(1).setResizable(false);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(1).setPreferredWidth(75);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(2).setResizable(false);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(2).setPreferredWidth(210);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(3).setResizable(false);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(3).setPreferredWidth(55);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(4).setResizable(false);
            jTableFacturasPorVehiculo.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
        jBtnGrabar.setEnabled(false);
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
        jBtnMinuta.setEnabled(false);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();

    }//GEN-LAST:event_btnSalirActionPerformed

    public void salir() {
        if (manifiestoActual != null) {

            if (esteManifiestoEsMio) {
                manifiestoActual.liberarManifiesto(true);
                manifiestoActual = null;
                esteManifiestoEsMio = false;
                limpiar();
            }
        }

        btnImprimir.setEnabled(false);

        this.dispose();
        this.setVisible(false);
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        nuevo();

    }//GEN-LAST:event_btnNuevoActionPerformed

    public void nuevo() {
        cancelar();
        nuevo = true;

        txtNumeroManifiesto.setEnabled(true);
        txtNumeroManifiesto.setEditable(true);
        jTabbedPane1.setEnabled(false);
        btnNuevo.setEnabled(false);
        txtNumeroManifiesto.requestFocus();

        btnGrabar.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnImprimir.setEnabled(false);
        jBtnMinuta.setEnabled(false);

        //cbxManifiestosDeDistribucion.setEnabled(true);a
        /*Actualizamos la tabla de manifiestos  pendienetes por descargar  */
        new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3)).start();
    }

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed


    }//GEN-LAST:event_btnGrabarActionPerformed


    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        cancelar();


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cancelar() {
        if (manifiestoActual != null) {

            if (esteManifiestoEsMio) {
                manifiestoActual.liberarManifiesto(true);
                manifiestoActual = null;
                esteManifiestoEsMio = false;
            }

        }
        limpiar();
    }

    private void jTableFacturasPorVehiculoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFacturasPorVehiculoMouseClicked

    }//GEN-LAST:event_jTableFacturasPorVehiculoMouseClicked

    private double llenarTablaProductosPorFactura(CFacturas facturaTemporal) {
        modelo1 = (DefaultTableModel) jTableProductosPorFactura.getModel();
        double valorFactura = 0.0;

        // se nexan los productos a la tabla de productos por factura
        for (CProductosPorFactura obj : facturaTemporal.getListaCProductosPorFactura()) {

            int fila = jTableProductosPorFactura.getRowCount();

            modelo1.addRow(new Object[jTableProductosPorFactura.getRowCount()]);
            jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()*obj.getCantidad()), fila, 5);
           
            this.nombreDelCliente = obj.getNombreDeCliente();

        }
        return valorFactura;
    }

    /**
     * Método que elimina la factura y los productos de una facturas
     * descargadas, que se encuentra almacenada en un archivo
     *
     * @param numFactura es el número de la factura que se va a modificar .
     */
    private void calcularValoraAConsignar() {
        /*Se calcula el valor recaudado del manifiesto y se imprime en el label*/
        valorTotalRecaudado = 0.0;
        valorTotalRecogidas = 0.0;
        manifiestoActual.setListaFacturasDescargadas(new File(manifiestoActual.getRutaArchivoDescargueFacturas()));
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasDescargadas()) {
            valorTotalRecaudado += obj.getValorRecaudado();

        }

        manifiestoActual.setListaDeRecogidasPorManifiesto(new File(manifiestoActual.getRutArchivoRecogidasporManifiesto()));
        for (CRecogidasPorManifiesto obj : manifiestoActual.getListaDeRecogidasPorManifiesto()) {
            valorTotalRecogidas += obj.getValorRecaudadoRecogida();
        }

        valorTotalAConsignar = valorTotalRecaudado - valorTotalRecogidas;
    }


    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (!estaOcupadoGrabando) {
            if (manifiestoActual != null) {
                if (esteManifiestoEsMio) {
                    manifiestoActual.liberarManifiesto(true);
                    manifiestoActual = null;
                    esteManifiestoEsMio = false;
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "El sistema está ocupado grabado los datos,\n no se puede cerrar el formulario", "Error al cerrar", JOptionPane.ERROR_MESSAGE);

        }


    }//GEN-LAST:event_formInternalFrameClosing

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed

        imprimir();
    }//GEN-LAST:event_btnImprimirActionPerformed

    public void imprimir() {
        manifiestoActual.setListaFacturasDescargadas(listaFacturasDescargadas);
        manifiestoActual.setListaDeSoportesConsignaciones(listaDeSoportesDeConsignaciones);

        ReporteDescargueDeFacturas1 demo = new ReporteDescargueDeFacturas1(ini, manifiestoActual);

    }

    private void txtNumeroManifiestoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroManifiestoFocusGained
        txtNumeroManifiesto.setSelectionStart(0);
        txtNumeroManifiesto.setSelectionEnd(txtNumeroManifiesto.getText().length());
    }//GEN-LAST:event_txtNumeroManifiestoFocusGained

    private void txtNumeroManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroManifiestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroManifiestoActionPerformed

    private void txtNumeroManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroManifiestoKeyPressed

        /*Evento al oprimir la tecla Enter */
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            manifiestoActual = null;

            /* Se realiza la consulta para buscar el manifiesto que se va a descargar*/
            new Thread(new HiloConsultarManifiesto(ini, this)).start();

        }

    }//GEN-LAST:event_txtNumeroManifiestoKeyPressed

    protected void consultarManifiesto_() {
//        /**
//         * Colocar hilo donde se muestra el gif del proceso
//         */
//        try {
//
//            manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()));
//
//            // se valida que el numeroDeManifiesto exista
//            // si  no existe el numeroDeManifiesto
//            if (manifiestoActual.getNumeroManifiesto() == 0) {
//                manifiestoActual = null;
//                JOptionPane.showInternalMessageDialog(this, "ESE MANIFIESTO NO EXISTE EN LA BASE DE DATOS ", "Error", JOptionPane.ERROR_MESSAGE);
//                txtManifiesto.requestFocus();
//                txtManifiesto.requestFocus();
//
//                // si el manifiesto existe   
//            } else {
//
//                // LISTAS DONDE SE GUARDAN TODAS LAS FACTURAS DEL MANIFIESTO
////                listaDeCFacturasEnManifiesto = new ArrayList(); // CFacturas
//                listaDeFacturasPorManifiesto = new ArrayList(); //CFacturasPorManifiesto
//                vistaListaDeCFacturas = new ArrayList(); // Vst_FacturasPorManifiesto
//
//                // LISTAS DONDE SE GUARDAN  LAS FACTURAS DESCARGADAS
//                listaCFacturasDescargadas = new ArrayList(); // CFacturas
//
//                // LISTAS DONDE SE GUARDAN TODAS LAS FACTURAS PENDIENTES POR DESCARGAR
//                listaCFacturasPendientesPorDescargar = new ArrayList(); //  CFacturas
//
//                // LISTA DONDE SE GUARDA TODOS LOS PRODUCTOS DESCARGADOS
//                listaDeCProductosPorFacturaDescargados = new ArrayList();  //CProductosPorFacturaDescargados
//
//                // Se valida que el manifiesto no esté siendo usado por otra persona en el descargue
//                if (manifiestoActual.getIsFree() == 0) {
//                    JOptionPane.showInternalMessageDialog(this, "Este manifiesto No se encuentra disponible,"
//                            + "está siendo utilizado  por otro usuario ", "Error", JOptionPane.ERROR_MESSAGE);
//
//                    // Si el mnifiesto se encuentra disponible
//                } else {
//
//                    // Se bloquea el manifiesto para que no sea utilizado.
//                    manifiestoActual.liberarManifiesto(false);
//
//                    manifiestoActual.setListaCFacturasCamdun();
//                    // listaDeCFacturasEnManifiesto = manifiestoActual.getListaCFacturasCamdun(); // CFacturas
//
//                    manifiestoActual.setListaFacturasPorManifiesto();
//                    listaDeFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto(); //CFacturasPorManifiesto
//
//                    manifiestoActual.setVstListadoDeFacturasEnManifiesto();
//                    vistaListaDeCFacturas = manifiestoActual.getVstListadoDeFacturasEnManifiesto(); // Vst_FacturasPorManifiesto
//
//                    //********************************************************
//                    //String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + ".txt";
//                    String rutaDeArchivoFacturas = "tmp/tmp_" + manifiestoActual.codificarManifiesto() + ".txt";
//                    String rutaDeArchivoProductosDescargados = "tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_productosDescargados.txt";
//
//                    File fichero = new File(rutaDeArchivoFacturas);
//
//                    //  SI EL FICHERO EXISTE, ES PORQUE YA SE HABIA INICIADO UN PROCESO DE DESCARGUE DE FACTURAS
//                    if (fichero.exists()) {
//
//                        // SE RECUPERAN LOS DATOS DE LAS FACTURAS DESCARGADAS
//                        manifiestoActual.setListaFacturasDescargadas(fichero);
//                        listaFacturasDescargadas = manifiestoActual.getListaFacturasDescargadas(); // CFacturasDescargadas
//
//                        recuperarFacturasPendientesPorDescargar();
//
//                        fichero = new File(manifiestoActual.getRutaArchivoDescargueporductosPorFactura());
//                        manifiestoActual.setListaCProductosPorFacturaDescargados(fichero);
//                        listaDeCProductosPorFacturaDescargados = manifiestoActual.getListaCProductosPorFacturaDescargados();
//
//                        // SINO EXISTE EL FICHERO PUEDE SUCEDER 2 CASOS
//                        // 1. EL MANIFIESTO SE ENCUENTRA EN DISTRIBUCIO OSEA ESTADO=3 Y HASTA AHORA SE
//                        //      INCIA EL PROCESO DE DESCARGUE DEL MANIFIESTO
//                        // 2. EL MANIFIESTO  YA SE ENCUENTRA DESCARGADO
//                    } else // SE VALIDA SI EL MANIFEISTO ESTA EN DISTRIBUCION
//                    if (manifiestoActual.getEstadoManifiesto() == 3) {
//
//                        // SE CREA EL ARCHIVO PARA GUARDAR LOS REGISTROS TEMPORALMENTE
//                        //rutaDeArchivoFacturas = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + ".txt";
//                        fichero = new File(manifiestoActual.getRutaArchivoDescargueFacturas());
//                        recuperarFacturasPendientesPorDescargar();
//
//                        // MANIFIESTO DESCARGADO    
//                    } else {
//
//                        manifiestoActual.setListaFacturasDescargadas();
//                        listaFacturasDescargadas = manifiestoActual.getListaFacturasDescargadas();
//
//                        //recuperarFacturasPendientesPorDescargar();
//
//                        manifiestoActual.setListaCProductosPorFacturaDescargados();
//                        listaDeCProductosPorFacturaDescargados = manifiestoActual.getListaCProductosPorFacturaDescargados();
//
//                    }
//
//                    /* SE BLOQUEA MANIFIESTO PARA QUE NO PUEDA SER UTLIZADO POR OTRO USUARIO */
//                    manifiestoActual.liberarManifiesto(false);
//
//                    txtManifiesto.setEditable(false);
//                    txtPlaca.setText(manifiestoActual.getVehiculo());
//                    txtKilometrosEntrada.setEnabled(true);
//                    txtKilometrosEntrada.setEditable(true);
//
//                    cbxMovimientoFactura.setEnabled(true);
//                    chkDescuento.setEnabled(true);
//                    rBtnContado.setEnabled(true);
//                    rBtnCredito.setEnabled(true);
//                    cbxMovimientoFactura.setSelectedItem("NINGUNO");
//                    cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
//
//                    jTabbedPane1.setEnabled(true);
//
//                    /* SWITCH PARA DETERMINAR EL MOVIMIENTO DEL MANIFIESTO */
//                    switch (manifiestoActual.getEstadoManifiesto()) {
//                        case 0:
//
//                            break;
//                        // MANIFIESTO SOLAMENTE CREADO
//                        case 1:
//                            JOptionPane.showInternalMessageDialog(this, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
//                                    + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", 0);
//                            break;
//
//                        // MANIFIESTO CREAD PER NO HA SALIDO A DISTRIBUCION
//                        case 2:
//                            JOptionPane.showInternalMessageDialog(this, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
//                                    + "NO SE HA GRABADO EN EL SISTEMA", "Error", 0);
//                            break;
//
//                        // MANIFIESTO GRABADO Y ESTA EN DISTRIBUCION
//                        case 3:
//                            if (manifiestoActual.getListaFacturasDescargadas() == null) {
//                                listaFacturasDescargadas = new ArrayList<>();
//                                //manifiestoActual.setListaFacturasDescargadas(listaFacturasDescargadas);
//                            }
//
//                            if (manifiestoActual.getListaFacturasDescargadas().size() > 0) {
//                                llenarjTableFacturasPorVehiculo();
//                                txtKilometrosEntrada.requestFocus();
//
//                            } else {
//                                lblFacturasPendientes.setText(" " + (manifiestoActual.getListaFacturasPorManifiesto().size()
//                                        - manifiestoActual.getListaFacturasDescargadas().size()));
//
//                                txtKilometrosSalida.setText("" + manifiestoActual.getKmSalida());
//                                txtKilometrosEntrada.requestFocus();
//                            }
//                            break;
//
//                        // MANIFIESTO DESCARGADO Y CUADRADO
//                        case 4:
//
//                            llenarjTableFacturasPorVehiculo();
//
//                            JOptionPane.showInternalMessageDialog(this, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
//                                    + "YA ESTA DESCARGADO DEL SISTEMA", "Error", 0);
//                            btnImprimir.setEnabled(true);
//
//                            break;
//
//                    }
//
//                }
//
//            }
//
//        } catch (Exception ex) {
//            System.out.println("Error en txtManifiestoKeyPressed ");
//            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        /**
//         * Detener hilo donde se muestra el gif del proceso
//         */
    }

    protected void llenarManifiesto(CManifiestosDeDistribucion man) {
        this.manifiestoActual = man;
        try {

            //listaFacturasDescargadas = man.getListaFacturasDescargadas();
            man.setListaFacturasDescargadas();
            // listaDeFacturasPorManifiesto = man.getListaFacturasPorManifiesto();
            // numeroDeFacturasEnManifiesto = listaDeFacturasPorManifiesto.size();
            //cantidadFacturasDescargadasManifiesto = listaFacturasDescargadas.size();
            txtNumeroManifiesto.setEditable(false);
            txtPlaca.setText(man.getVehiculo());
            txtKilometrosSalida.setText("" + man.getKmSalida());
            txtKilometrosEntrada.setEnabled(true);
            txtKilometrosEntrada.setEditable(true);

            // cbxMovimientoFactura.setEnabled(true);
            if (man.getListaFacturasDescargadas().size() > 0) {
                llenarjTableFacturasPorVehiculo();
            } else {
                txtKilometrosSalida.setText("" + man.getKmSalida());
                txtKilometrosEntrada.requestFocus();
            }

            txtKilometrosEntrada.requestFocus();
            txtKilometrosEntrada.requestFocus();

        } catch (Exception ex) {
            System.out.println("Error en txtManifiestoKeyPressed ");
            Logger.getLogger(FConsultaManifiestosDescargados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void llenarjTableFacturasPorVehiculo() throws Exception {

        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();
        if (modelo2.getRowCount() > 0) {
            int a = modelo2.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo2.removeRow(i);
            }
        }

        txtNumeroManifiesto.setEditable(false);

        for (CFacturasPorManifiesto obj2 : listaFacturasDescargadas) {

            filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();
            modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
            jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
            jTableFacturasPorVehiculo.setValueAt(obj2.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
            jTableFacturasPorVehiculo.setValueAt(obj2.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
            switch (obj2.getIdTipoDeMovimiento()) {
                case 1:
                    jTableFacturasPorVehiculo.setValueAt(obj2.getIdTipoDeMovimiento(), filaSeleccionada, 3); // nombre del nombreDelCliente
                    break;

                case 2:
                    if (obj2.getValorRecaudado() == 0.0) {
                        jTableFacturasPorVehiculo.setValueAt("E. T. Cr", filaSeleccionada, 3); // nombre del nombreDelCliente
                    } else {
                        if (obj2.getValorDescuento() == 0.0) {
                            jTableFacturasPorVehiculo.setValueAt("E. T. Cn", filaSeleccionada, 3); // nombre del nombreDelCliente
                        } else {
                            jTableFacturasPorVehiculo.setValueAt("E. T. %", filaSeleccionada, 3); // nombre del nombreDelCliente
                        }

                    }

                    break;
                case 3:
                    jTableFacturasPorVehiculo.setValueAt("D. T.", filaSeleccionada, 3); // nombre del nombreDelCliente
                    break;
                case 4:

                    if (obj2.getValorRecaudado() > 0) {
                        jTableFacturasPorVehiculo.setValueAt("E. N. Cn", filaSeleccionada, 3); // nombre del nombreDelCliente
                    } else {
                        jTableFacturasPorVehiculo.setValueAt("E. N. Cr", filaSeleccionada, 3); // nombre del nombreDelCliente
                    }

                    break;
                case 5:
                    jTableFacturasPorVehiculo.setValueAt("", filaSeleccionada, 3); // nombre del nombreDelCliente
                    break;
                case 6:
                    jTableFacturasPorVehiculo.setValueAt("R. E.", filaSeleccionada, 3); // nombre del nombreDelCliente
                    break;

            }

            jTableFacturasPorVehiculo.setValueAt(nf.format(obj2.getValorRecaudado()), filaSeleccionada, 4);

            valorTotalRecaudado += obj2.getValorRecaudado();

        }

    }

    public void llenarjTableFacturasDescargadas() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        modelo2 = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();
        txtNumeroManifiesto.setEditable(false);

        for (CFacturasPorManifiesto obj2 : listaFacturasDescargadas) {
            filaSeleccionada = jTableFacturasPorVehiculo.getRowCount();
            modelo2.addRow(new Object[jTableFacturasPorVehiculo.getRowCount()]);
            jTableFacturasPorVehiculo.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
            jTableFacturasPorVehiculo.setValueAt(obj2.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
            jTableFacturasPorVehiculo.setValueAt(obj2.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
            jTableFacturasPorVehiculo.setValueAt(nf.format(obj2.getValorRecaudado()), filaSeleccionada, 3);

            valorTotalRecaudado += obj2.getValorRecaudado();

        }

    }


    private void txtPlacaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaFocusGained

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void txtPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaKeyPressed

    private void txtKilometrosEntradaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKilometrosEntradaFocusGained
        txtKilometrosEntrada.setSelectionStart(0);
        txtKilometrosEntrada.setSelectionEnd(txtKilometrosEntrada.getText().length());
    }//GEN-LAST:event_txtKilometrosEntradaFocusGained

    private void txtKilometrosEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKilometrosEntradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKilometrosEntradaActionPerformed

    private void txtKilometrosEntradaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKilometrosEntradaKeyPressed


    }//GEN-LAST:event_txtKilometrosEntradaKeyPressed

    private void txtKilometrosSalidaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKilometrosSalidaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKilometrosSalidaFocusGained

    private void txtKilometrosSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKilometrosSalidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKilometrosSalidaActionPerformed

    private void txtKilometrosSalidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKilometrosSalidaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKilometrosSalidaKeyPressed

    private void txtKilometrosEntradaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKilometrosEntradaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKilometrosEntradaKeyReleased

    private void cerrarManifiesto() {

        new Thread(new HiloGuardarManifiestoDescargado(ini, this)).start();

    }


    private void jTableProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductosPorFacturaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableProductosPorFacturaMouseClicked


    private void txtNombreConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreConductorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorFocusGained

    private void txtNombreConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorActionPerformed

    private void txtNombreConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreConductorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorKeyPressed

    private void txtRutaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRutaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaFocusGained

    private void txtRutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRutaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaActionPerformed

    private void txtRutaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRutaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaKeyPressed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed

    }//GEN-LAST:event_jBtnGrabarActionPerformed

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

                case 3:
                    sacarMinuta.minutaPoManifiesto(manifiestoActual.getNumeroManifiesto());
                    break;
                case 4:
                    sacarMinuta.minutaPoManifiesto(manifiestoActual.getNumeroManifiesto());
                    break;

            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay manifiesto de ruta selecccionado ", "Error", 0);
        }
    }//GEN-LAST:event_jBtnMinutaActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        salir();        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnExitActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    private javax.swing.JToggleButton jBtnGrabar;
    private javax.swing.JToggleButton jBtnImprimir;
    private javax.swing.JToggleButton jBtnMinuta;
    private javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableFacturasPorVehiculo;
    private javax.swing.JTable jTableProductosPorFactura;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    private javax.swing.JPanel pnlEntregas;
    public javax.swing.JTextField txtKilometrosEntrada;
    public javax.swing.JTextField txtKilometrosSalida;
    public javax.swing.JTextField txtNombreConductor;
    public javax.swing.JTextField txtNumeroManifiesto;
    public javax.swing.JTextField txtPlaca;
    public javax.swing.JTextField txtRuta;
    // End of variables declaration//GEN-END:variables

    private void limpiar() {

        txtNumeroManifiesto.setEditable(false);
        txtKilometrosEntrada.setEnabled(false);
        txtKilometrosEntrada.setEditable(false);

        txtNumeroManifiesto.setEnabled(false);

        txtKilometrosEntrada.setEnabled(false);
        txtKilometrosSalida.setEnabled(false);
        txtPlaca.setEnabled(false);
        txtNombreConductor.setEnabled(false);
        txtRuta.setEnabled(false);

        txtNumeroManifiesto.setText("");

        txtKilometrosEntrada.setText("");
        txtKilometrosSalida.setText("");

        txtPlaca.setText("");
        txtNombreConductor.setText("");
        txtRuta.setText("");
        valorTotalRecaudado = 0.0;

        /* elimina lo registros de las tablas de la vista */
        limpiarTablas();


        /* Elimina los datos de los arrays*/
        limpiarArrays();

        valorTotalRecogidas = 0.0;
        valorTotalAConsignar = 0.0;
        valorTotalRecaudado = 0.0;
        valorConsignado = 0.0;
        mensaje = "";

        //cantidadFacturasDescargadasManifiesto = 0;
        manifiestoActual = null;
        facturaActual = null;

        esteManifiestoEsMio = false;

        btnGrabar.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnImprimir.setEnabled(false);

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

    }

    private void limpiarArrays() {

        /*Se vacían las listas respectivas */
        //listaFacturasPorManifiesto = new ArrayList();
        listaFacturasDescargadas = new ArrayList();
        listaFacturasPendientesPorDescargar = new ArrayList();

        listaDeRecogidasPorManifiesto = new ArrayList();
        listaDeCProductosPorFacturaDescargados = new ArrayList();
        listaDeFacturasNoManifestadas = new ArrayList();
        listaFacturaTemporal = new ArrayList();
        listaDeSoportesDeConsignaciones = new ArrayList();

    }

    private void limpiarTablas() {
        DefaultTableModel modelo = (DefaultTableModel) jTableFacturasPorVehiculo.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        modelo = (DefaultTableModel) jTableProductosPorFactura.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    private void ordenartblFacturasPorVehiculo() {
        for (int i = 0; i < jTableFacturasPorVehiculo.getRowCount(); i++) {
            jTableFacturasPorVehiculo.setValueAt(i + 1, i, 0);
        }
    }

    public boolean estaDescargadaLaFactura(String numeroDefactura) {
        boolean descargada = false;
        for (CFacturasPorManifiesto obj : listaFacturasDescargadas) {
            if (numeroDefactura.equals(obj.getNumeroFactura())) {
                descargada = true;
                break;
            }
        }

        return descargada;
    }

    public boolean estaDescargadaRecogida(String numeroDefactura) {
        boolean descargada = false;
        for (CRecogidasPorManifiesto obj : listaDeRecogidasPorManifiesto) {
            if (numeroDefactura.equals(obj.getNumeroFactura())) {
                descargada = true;
                break;
            }
        }

        return descargada;
    }

    public void llenarDatosVista() {

        txtNumeroManifiesto.setEnabled(true);

        txtKilometrosEntrada.setEnabled(true);
        txtKilometrosSalida.setEnabled(true);
        txtPlaca.setEnabled(true);
        txtNombreConductor.setEnabled(true);
        txtRuta.setEnabled(true);

        txtNumeroManifiesto.setEditable(false);

        txtKilometrosEntrada.setEditable(false);
        txtKilometrosSalida.setEditable(false);
        txtPlaca.setEditable(false);
        txtNombreConductor.setEditable(false);
        txtRuta.setEditable(false);

        valorTotalAConsignar = valorTotalRecaudado - valorTotalRecogidas;

        txtPlaca.setText(manifiestoActual.getVehiculo());

        txtKilometrosEntrada.setText("" + manifiestoActual.getKmEntrada());
        txtKilometrosSalida.setText("" + manifiestoActual.getKmSalida());

        kilometrosRecorridos = manifiestoActual.getKmEntrada() - manifiestoActual.getKmSalida();

        if (manifiestoActual.getEstadoManifiesto() == 4) {
            lblCirculoDeProgreso.setVisible(false);

            /*Se habilitan las pestañas para visualizar las recogidas */
            jTabbedPane1.setEnabled(true);

            // txtManifiesto.setEnabled(false);
            // txtKilometrosEntrada.setEnabled(false);
            //txtNumeroDeFactura.setEnabled(false);
            txtNombreConductor.setText(manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor());
            txtRuta.setText(manifiestoActual.getNombreDeRuta());

            btnGrabar.setEnabled(false);

            btnImprimir.setEnabled(true);

            btnImprimir.requestFocus();

            JOptionPane.showInternalMessageDialog(this, "Manifiesto descargado y completo", "Manifiesto completo", JOptionPane.INFORMATION_MESSAGE);
        }

    }

}
