/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.administrativo;

import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.imprimir.RepporteRuteroConductores;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.Threads.HiloHabilitarManifiesto;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.objetos.personas.Vst_empleados;
import mtto.vehiculos.CCarros;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FentregaDeRutasAdministracion extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    CEmpleados conductor;
    CUsuarios user;
    public CManifiestosDeDistribucion manifiestoActual = null;
    CCarros carro = null;
    Inicio ini = null;

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public DefaultTableModel modelo1, modelo2;

    Double valorTotalAConsignar = 0.0;
    boolean cargado = false;
    // boolean tieneManifiestosAnteriores = false;
    // boolean tieneMnifiestosAsigndos = false;
    boolean nuevo = false;
    boolean actualizar = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean estaOcupadoGrabando = false;
    int kilometrosEntrada;
    int kilometrosRecorridos;

    int filaTabla2;
    int indiceLista = 0;
    int columna = 0;
    String mensaje = null;

    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    public CEmpleados getConductor() {
        return conductor;
    }

    public void setConductor(CEmpleados conductor) {
        this.conductor = conductor;
    }

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     */
    public FentregaDeRutasAdministracion(Inicio ini) throws Exception {
        CManifiestosDeDistribucion manif = new CManifiestosDeDistribucion(ini);

        try {
            initComponents();
            this.ini = ini;
            this.user = ini.getUser();

            limpiar();

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
            Logger.getLogger(FentregaDeRutasAdministracion.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtNumeroDocumento = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtCedulaConductoro = new javax.swing.JTextField();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        txtManifiesto = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtNombreConductor = new javax.swing.JTextField();
        txtCedulaConductor = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        txtRuta = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        txtCanalVentas = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jSplitPane1 = new javax.swing.JSplitPane();
        btnAgregar = new javax.swing.JButton();
        btnNuevo1 = new javax.swing.JButton();
        btnGrabar1 = new javax.swing.JButton();
        btnMostrarDocumento = new javax.swing.JToggleButton();
        btnImprimir1 = new javax.swing.JToggleButton();
        btnCancelar1 = new javax.swing.JToggleButton();
        jButton4 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Formulario para Entrega de Rutas a Administracion");
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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        txtNumeroDocumento.setName("numerico"); // NOI18N
        txtNumeroDocumento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDocumentoFocusGained(evt);
            }
        });
        txtNumeroDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroDocumentoActionPerformed(evt);
            }
        });
        txtNumeroDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDocumentoKeyPressed(evt);
            }
        });

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Usuario");

        txtUsuario.setEditable(false);
        txtUsuario.setEnabled(false);
        txtUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioFocusGained(evt);
            }
        });
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
        });

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Numero de Documento");

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Conductor");

        txtCedulaConductoro.setEditable(false);
        txtCedulaConductoro.setEnabled(false);
        txtCedulaConductoro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCedulaConductoroFocusGained(evt);
            }
        });
        txtCedulaConductoro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaConductoroActionPerformed(evt);
            }
        });
        txtCedulaConductoro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCedulaConductoroKeyPressed(evt);
            }
        });

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNumeroDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedulaConductoro, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(lblCirculoDeProgreso)
                .addGap(171, 171, 171))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNumeroDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedulaConductoro, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
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
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Item", "Manifiesto", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setColumnSelectionAllowed(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(50);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
            jTable1.getColumnModel().getColumn(1).setMinWidth(120);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(120);
            jTable1.getColumnModel().getColumn(2).setMinWidth(160);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(160);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(160);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Numero de manifiesto"));

        txtManifiesto.setEnabled(false);
        txtManifiesto.setName("numerico"); // NOI18N
        txtManifiesto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtManifiestoFocusGained(evt);
            }
        });
        txtManifiesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtManifiestoActionPerformed(evt);
            }
        });
        txtManifiesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtManifiestoKeyPressed(evt);
            }
        });

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Placa");

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

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Manifiesto");

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Conductor");

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

        txtCedulaConductor.setEditable(false);
        txtCedulaConductor.setEnabled(false);
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

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Nombre Cond.");

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Ruta");

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

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Canal");

        txtCanalVentas.setEditable(false);
        txtCanalVentas.setEnabled(false);
        txtCanalVentas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCanalVentasFocusGained(evt);
            }
        });
        txtCanalVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCanalVentasActionPerformed(evt);
            }
        });
        txtCanalVentas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCanalVentasKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedulaConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCanalVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedulaConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCanalVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 901, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel5);

        jToolBar1.setRollover(true);

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        btnAgregar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregar.setFocusable(false);
        btnAgregar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAgregar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jSplitPane1.setLeftComponent(btnAgregar);

        btnNuevo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        btnNuevo1.setToolTipText("Nuevo");
        btnNuevo1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnNuevo1.setFocusable(false);
        btnNuevo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevo1ActionPerformed(evt);
            }
        });
        jSplitPane1.setRightComponent(btnNuevo1);

        btnGrabar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        btnGrabar1.setToolTipText("Grabar");
        btnGrabar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGrabar1.setEnabled(false);
        btnGrabar1.setFocusable(false);
        btnGrabar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabar1ActionPerformed(evt);
            }
        });
        jSplitPane1.setRightComponent(btnGrabar1);

        jToolBar1.add(jSplitPane1);

        btnMostrarDocumento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        btnMostrarDocumento.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnMostrarDocumento.setEnabled(false);
        btnMostrarDocumento.setFocusable(false);
        btnMostrarDocumento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMostrarDocumento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnMostrarDocumento);

        btnImprimir1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        btnImprimir1.setToolTipText("Imprimir");
        btnImprimir1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnImprimir1.setEnabled(false);
        btnImprimir1.setFocusable(false);
        btnImprimir1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprimir1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnImprimir1);

        btnCancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        btnCancelar1.setToolTipText("Imprimir");
        btnCancelar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelar1.setFocusable(false);
        btnCancelar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCancelar1);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed

        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        limpiar();
        nuevo = true;
        CDocumentoEntregasDeRutas listado = new CDocumentoEntregasDeRutas((this.ini));
        if (listado.buscarManifiestosDescargados()) {
            listado.grabarDocumentoEntrgaDeRuta();
            
            txtNumeroDocumento.setEnabled(true);
            txtNumeroDocumento.setEditable(true);

            btnNuevo.setEnabled(false);
            txtNumeroDocumento.requestFocus();
        }


    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed

        int deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {
            //grabarHabilitarManifiesto();
            txtNumeroDocumento.setEditable(false);
            // new Thread(new HiloHabilitarManifiesto(this)).start();
        }

    }//GEN-LAST:event_btnGrabarActionPerformed

    private void grabarHabilitarManifiesto() throws HeadlessException {

        if (manifiestoActual.habilitarManifiesto()) {

//                btnGrabar1.setEnabled(false);
//                btnMostrarDocumento.setEnabled(true);
//                btnImprimir.setEnabled(true);
            txtNumeroDocumento.setEditable(false);

            JOptionPane.showInternalMessageDialog(this, "Datos guardados perfectamente en la BBDD ", "Ok", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showInternalMessageDialog(this, "Error al guardar los Datos en la  BBDD ", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiar();

//        txtCedulaConductor.setEnabled(false);
//        txtnombreDeConductor.setEditable(false);
//        txtCedulaAuxiliarDeReparto.setEnabled(false);
//        txtNombreDeAuxiliar.setEditable(false);
//        txtKmDeSalida.setEditable(false);
        txtNumeroDocumento.setEnabled(false);
        txtNumeroDocumento.setEditable(false);

//        cbxRutaDeDistribucion.setEnabled(false);
//        btnCrearManifiesto.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnImprimir.setEnabled(false);
        btnGrabar.setEnabled(false);
        grabar = false;
        btnNuevo.setEnabled(true);
        btnNuevo.requestFocus();

//        manifiestoModificadoPorMi.setIsFree(1);

    }//GEN-LAST:event_btnCancelarActionPerformed


    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
//        if (manifiestoModificadoPorMi != null) {
//            manifiestoModificadoPorMi.setIsFree(1);
//        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        try {

            RepporteRuteroConductores demo = new RepporteRuteroConductores(ini, manifiestoActual);
        } catch (Exception ex) {
            Logger.getLogger(FentregaDeRutasAdministracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void txtNumeroDocumentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDocumentoFocusGained
        txtNumeroDocumento.setSelectionStart(0);
        txtNumeroDocumento.setSelectionEnd(txtNumeroDocumento.getText().length());
    }//GEN-LAST:event_txtNumeroDocumentoFocusGained

    private void txtNumeroDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDocumentoActionPerformed

    private void txtNumeroDocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDocumentoKeyPressed
//        if (evt.getKeyCode() == KeyEvent.VK_F2) {
//            FBuscarManifiestos form = new FBuscarManifiestos(ini, this);
//            PrincipalAuxiliarLogistica.escritorio.add(form);
//            form.toFront();
//            form.setClosable(true);
//            form.setVisible(true);
//            form.setTitle("Formulario para Buscar manifiestos de la última semana");
//            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            //form.setSize(PrincipalLogistica.escritorio.getSize());
//            form.setLocation((ini.getDimension().width - form.getSize().width) / 2, (ini.getDimension().height - form.getSize().height) / 2);
//            form.show();
//
//        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            lblCirculoDeProgreso.setVisible(true);
            // new Thread(new HiloConsultarManifiesto(this)).start();

        }

    }//GEN-LAST:event_txtNumeroDocumentoKeyPressed

    public void consultarManifiesto() {
        try {

            // manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()), "CURRENT_DATE");
            manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtNumeroDocumento.getText().trim()));

            // se valida que el manifiesto exista
            if (manifiestoActual.getVehiculo() == null) { // si  no existe el manifiesto

                JOptionPane.showInternalMessageDialog(this, "Ese manifiesto no Existe en la BB DD ", "Error", JOptionPane.WARNING_MESSAGE);
                txtNumeroDocumento.requestFocus();
                txtNumeroDocumento.requestFocus();
            } else { //  si existe el vehículo, existe el manifiesto

                manifiestoActual.setListaFacturasPorManifiesto();
                manifiestoActual.setListaDeAuxiliares();

                txtNumeroDocumento.setEditable(false);
                CCarros car = new CCarros(ini, manifiestoActual.getVehiculo());
                txtUsuario.setText(manifiestoActual.getVehiculo());
                txtCedulaConductoro.setText(manifiestoActual.getConductor());

                for (CEmpleados obj : ini.getListaDeEmpleados()) {
                    if (obj.getCedula().equals(manifiestoActual.getConductor())) {
                        txtNombreConductor.setText(obj.getNombres() + " " + obj.getApellidos());
                        break;
                    }
                }

//                txtCodigoManifiesto.setText(manifiestoActual.codificarManifiesto());
                // INDIC EL ESTADO ACTUAL DEL MANIFIESTO
                switch (manifiestoActual.getEstadoManifiesto()) {
                    case 0:

                        break;
                    case 1:
                        JOptionPane.showInternalMessageDialog(this, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
                                + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", 0);
                        break;

                    case 2: // MANIFIESTO CREADO, PERO NO HA SALIDO A DISTRIBUCION
                        JOptionPane.showInternalMessageDialog(this, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
                                + "NO SE HA GRABADO EN EL SISTEMA", "Error", 0);
                        break;
                    case 3: // MANIFIESTO EN DISTRIBUCION
                        llenarDatosManifiestoCerrado();
                        break;
                    case 4:// MANIFIESTO YA DESCARGADO
                        llenarDatosManifiestoCerrado();

                        break;

                }

            }

        } catch (Exception ex) {
            System.out.println("Error en txtManifiestoKeyPressed ");
            Logger.getLogger(FentregaDeRutasAdministracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void txtUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioFocusGained

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioKeyPressed


    private void txtCedulaConductoroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaConductoroFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductoroFocusGained

    private void txtCedulaConductoroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaConductoroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductoroActionPerformed

    private void txtCedulaConductoroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaConductoroKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductoroKeyPressed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        limpiar();

    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnNuevo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNuevo1ActionPerformed

    private void btnGrabar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabar1ActionPerformed
//        if (listaDeFacturasTrasladadas.isEmpty()) {
//            JOptionPane.showInternalMessageDialog(this, "No hay datos para trasladar al Manifiesto # " + manifiestoDestino.getNumeroManifiesto(), "! Alerta !", JOptionPane.WARNING_MESSAGE);
//        } else {
//            if (grabarTrasladoDeFacturas()) {
//
//                return;
//            }
//        }
    }//GEN-LAST:event_btnGrabar1ActionPerformed

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        limpiar();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtManifiestoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtManifiestoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManifiestoFocusGained

    private void txtManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtManifiestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManifiestoActionPerformed

    private void txtManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtManifiestoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManifiestoKeyPressed

    private void txtPlacaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaFocusGained

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void txtPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaKeyPressed

    private void txtNombreConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreConductorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorFocusGained

    private void txtNombreConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorActionPerformed

    private void txtNombreConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreConductorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorKeyPressed

    private void txtCedulaConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaConductorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorFocusGained

    private void txtCedulaConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorActionPerformed

    private void txtCedulaConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaConductorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorKeyPressed

    private void txtRutaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRutaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaFocusGained

    private void txtRutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRutaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaActionPerformed

    private void txtRutaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRutaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaKeyPressed

    private void txtCanalVentasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCanalVentasFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCanalVentasFocusGained

    private void txtCanalVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCanalVentasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCanalVentasActionPerformed

    private void txtCanalVentasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCanalVentasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCanalVentasKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregar;
    public javax.swing.JButton btnCancelar;
    private javax.swing.JToggleButton btnCancelar1;
    public javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnGrabar1;
    public javax.swing.JButton btnImprimir;
    public javax.swing.JToggleButton btnImprimir1;
    private javax.swing.JToggleButton btnMostrarDocumento;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnNuevo1;
    public javax.swing.JButton btnSalir;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JTextField txtCanalVentas;
    public javax.swing.JTextField txtCedulaConductor;
    public javax.swing.JTextField txtCedulaConductoro;
    public javax.swing.JTextField txtManifiesto;
    public javax.swing.JTextField txtNombreConductor;
    public javax.swing.JTextField txtNumeroDocumento;
    public javax.swing.JTextField txtPlaca;
    public javax.swing.JTextField txtRuta;
    public javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

    private void limpiar() {
        try {
            lblCirculoDeProgreso.setVisible(false);

            txtCanalVentas.setEnabled(true);
            txtCanalVentas.setEditable(false);
            txtCedulaConductoro.setEnabled(true);
            txtCedulaConductoro.setEditable(false);

            txtNombreConductor.setEnabled(true);
            txtNombreConductor.setEditable(false);

            txtUsuario.setEnabled(true);
            txtUsuario.setEditable(false);
            txtRuta.setEnabled(true);
            txtRuta.setEditable(false);

            btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
            txtNumeroDocumento.setText("");
            txtUsuario.setText("");
            txtCedulaConductoro.setText("");
            txtNombreConductor.setText("");

            txtRuta.setText("");
            txtCanalVentas.setText("");
            valorTotalAConsignar = 0.0;

        } catch (Exception ex) {
        }

    }

    private boolean validarManifiesto() {
        boolean verificado = true;
        try {

            mensaje = "";
            String fi = String.valueOf(Inicio.getFechaSql());
            String ff = String.valueOf(ini.getFechaActualServidor());
            if (!fi.equals(ff)) {
                mensaje += "La fecha del Servidor y el Sistema no coinciden, verificar configuración del sistema " + "  \n";
                verificado = false;
            }
            if (txtNumeroDocumento.getText().isEmpty()) {
                mensaje += "No ha selecccionado el Vehiculo de la Ruta" + "  \n";
                verificado = false;
            }

        } catch (SQLException ex) {
            System.out.println("Error en validarManifiesto ");
            Logger.getLogger(FentregaDeRutasAdministracion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificado;
    }

    public synchronized void llenarDatosManifiestoCerrado() throws Exception {
        double valorTotalManifiesto = 0.0;

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        txtCanalVentas.setEnabled(true);
        txtCanalVentas.setEditable(false);
        txtCedulaConductoro.setEnabled(true);
        txtCedulaConductoro.setEditable(false);

        txtNombreConductor.setEnabled(true);
        txtNombreConductor.setEditable(false);

        txtUsuario.setEnabled(true);
        txtUsuario.setEditable(false);
        txtRuta.setEnabled(true);
        txtRuta.setEditable(false);

        btnNuevo.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnImprimir.setEnabled(false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
//        manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        //  manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        int cantidadFacturasEnManifiesto = 0;

        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            if (obj.getIdCanalDeVenta() == manifiestoActual.getIdCanal()) {
                txtCanalVentas.setText(obj.getNombreCanalDeVenta());
                break;

            }
        }

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            if (obj.getIdRutasDeDistribucion() == manifiestoActual.getIdRuta()) {
                txtRuta.setText(obj.getNombreRutasDeDistribucion());
            }
        }

        // txtCodigoManifiesto.setText(manifiestoActual.codificarManifiesto());
        txtNumeroDocumento.setEnabled(false);
        btnGrabar.setEnabled(true);
        btnImprimir.setEnabled(true);
        btnImprimir.requestFocus();

    }

}
