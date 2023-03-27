/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.administracion;

import aplicacionlogistica.distribucion.consultas.FBuscarVehiculo;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.administrativo.PrincipalAdministrador;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import mtto.vehiculos.CCarros;
import mtto.vehiculos.Administracion.CLineasPorMarca;
import mtto.vehiculos.Administracion.CMarcasDeVehiculos;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author VLI_488
 */
public class IngresarCarros extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public boolean actualizar = false;
    boolean nuevo = false;
    boolean tieneFoto = false;
    public boolean actualizarFoto = false;
    public boolean estaOcupadoGrabando = false;
    String mensaje = null;
    ImageIcon foto = null;
    public Inicio ini;
    String suario;
    
    public boolean cargado=false;

    public CCarros carro;

    CUsuarios user;
    String extension = null;
    File fileFoto = null;

    /**
     * Creates new form IngresarPersonas
     *
     * @param ini objeto de configuración del sistema
     */
    public IngresarCarros(Inicio ini) {
        try {
            initComponents();
            this.ini = ini;
            user = ini.getUser();
            lblCirculoDeProgreso1.setVisible(false);
           

            foto = new ImageIcon("/aplicacionlogistica/configuracion/imagenes/perfil.jpg");
            //  JScrollPane jscp = new JScrollPane(this);
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
            
 new Thread(new HiloIngresarCarros(this.ini, this, "cargarLaVista")).start();
           
        } catch (Exception ex) {
            Logger.getLogger(IngresarCarros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void colocarImagen() {
        javax.swing.GroupLayout panelFotografiaLayout = new javax.swing.GroupLayout(panelFotografia);
        panelFotografia.setLayout(panelFotografiaLayout);
        panelFotografiaLayout.setHorizontalGroup(
                panelFotografiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        panelFotografiaLayout.setVerticalGroup(
                panelFotografiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 240, Short.MAX_VALUE)
        );
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
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnCancelar = new javax.swing.JToggleButton();
        jBtnSalir = new javax.swing.JToggleButton();
        jBtnRefrescar = new javax.swing.JToggleButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtPesoSinCarga = new javax.swing.JTextField();
        txtLargo = new javax.swing.JTextField();
        txtAlto = new javax.swing.JTextField();
        txtTarjetaDePropiedad = new javax.swing.JTextField();
        txtTamanioLLantas = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cbxTipoCombustible = new javax.swing.JComboBox();
        chkVehiculoActivo = new javax.swing.JCheckBox();
        txtAncho = new javax.swing.JTextField();
        txtLongitud = new javax.swing.JTextField();
        txtCantidadLLantas = new javax.swing.JTextField();
        txtSerialChasis = new javax.swing.JTextField();
        txtSerialMotor = new javax.swing.JTextField();
        txtKmCambioAceiteMotor = new javax.swing.JTextField();
        txtKmCambioValvulina = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtKmCambioValvulinaTrasmision = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        cbxAgencias = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        cbxTiposContratos = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        txtPesoTotalAutorizado = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtCapacidadInstalad = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtKmInicial = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        panelFotografia = new org.edisoncor.gui.panel.PanelImage();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        txtPlaca = new javax.swing.JTextField();
        cbxMarcaVehiculo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbxLineaVehiculo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbxTipoVehiculo = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cbxTipoCarroceria = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        cbxTipoDeServicio = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        txtPropietario = new javax.swing.JTextField();
        lblCirculoDeProgreso1 = new javax.swing.JLabel();

        agregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/People.png"))); // NOI18N
        agregarImagen.setText("Agregar Imagen del vehículo");
        agregarImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarImagenMouseClicked(evt);
            }
        });
        pumImagen.add(agregarImagen);

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario para el ingreso de Vehículos");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N

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

        jBtnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancelar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnCancelar.setFocusable(false);
        jBtnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnCancelar);

        jBtnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jBtnSalir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnSalir.setFocusable(false);
        jBtnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSalirActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnSalir);

        jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N
        jBtnRefrescar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnRefrescar.setFocusable(false);
        jBtnRefrescar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnRefrescar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnRefrescarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnRefrescar);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtPesoSinCarga.setEditable(false);
        txtPesoSinCarga.setToolTipText("Ingresar el  peso del vehículo sin carga");
        txtPesoSinCarga.setName("numerico"); // NOI18N
        txtPesoSinCarga.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPesoSinCargaFocusGained(evt);
            }
        });
        txtPesoSinCarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesoSinCargaActionPerformed(evt);
            }
        });
        txtPesoSinCarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesoSinCargaKeyPressed(evt);
            }
        });

        txtLargo.setEditable(false);
        txtLargo.setToolTipText("Ingresar el largo total del vehículo");
        txtLargo.setName("numerico"); // NOI18N
        txtLargo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLargoFocusGained(evt);
            }
        });
        txtLargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLargoActionPerformed(evt);
            }
        });
        txtLargo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLargoKeyPressed(evt);
            }
        });

        txtAlto.setEditable(false);
        txtAlto.setToolTipText("Ingresar el alto de la carrocería");
        txtAlto.setName("numerico"); // NOI18N
        txtAlto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAltoFocusGained(evt);
            }
        });
        txtAlto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAltoActionPerformed(evt);
            }
        });
        txtAlto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAltoKeyPressed(evt);
            }
        });

        txtTarjetaDePropiedad.setEditable(false);
        txtTarjetaDePropiedad.setToolTipText("Ingresar el # de la Tarjeta de propiedad");
        txtTarjetaDePropiedad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTarjetaDePropiedadFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTarjetaDePropiedadFocusLost(evt);
            }
        });
        txtTarjetaDePropiedad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTarjetaDePropiedadKeyPressed(evt);
            }
        });

        txtTamanioLLantas.setEditable(false);
        txtTamanioLLantas.setToolTipText("Ingresar el tamño de las llantas(referencia)");
        txtTamanioLLantas.setName(""); // NOI18N
        txtTamanioLLantas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTamanioLLantasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTamanioLLantasFocusLost(evt);
            }
        });
        txtTamanioLLantas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTamanioLLantasKeyPressed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Peso Sin Carga");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Largo Vehiculo");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Allto Carroceria");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("ancho Carroceria");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Longitud Carroceria");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Tarjeta de propiedad");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Cantidad LLantad");

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Tamaño llantas");

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Serial Chasis");

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Serial Motor");

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Tipo de Combustible");

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Km Cambio Aceite Motor");

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Km Cambio Valv. Caja");

        cbxTipoCombustible.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbxTipoCombustible.setToolTipText("Seleccionar la Agencia");
        cbxTipoCombustible.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoCombustibleItemStateChanged(evt);
            }
        });
        cbxTipoCombustible.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxTipoCombustibleFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbxTipoCombustibleFocusLost(evt);
            }
        });
        cbxTipoCombustible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTipoCombustibleActionPerformed(evt);
            }
        });
        cbxTipoCombustible.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxTipoCombustibleKeyPressed(evt);
            }
        });

        chkVehiculoActivo.setSelected(true);
        chkVehiculoActivo.setText("Vehículo Activo");
        chkVehiculoActivo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkVehiculoActivoStateChanged(evt);
            }
        });

        txtAncho.setEditable(false);
        txtAncho.setToolTipText("Ingresar el  ancho de la carroceria");
        txtAncho.setName("numerico"); // NOI18N
        txtAncho.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAnchoFocusGained(evt);
            }
        });
        txtAncho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAnchoActionPerformed(evt);
            }
        });
        txtAncho.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAnchoKeyPressed(evt);
            }
        });

        txtLongitud.setEditable(false);
        txtLongitud.setToolTipText("Ingresar la longitud de la carroceria");
        txtLongitud.setName("numerico"); // NOI18N
        txtLongitud.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLongitudFocusGained(evt);
            }
        });
        txtLongitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLongitudActionPerformed(evt);
            }
        });
        txtLongitud.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLongitudKeyPressed(evt);
            }
        });

        txtCantidadLLantas.setEditable(false);
        txtCantidadLLantas.setToolTipText("Ingresar la cantidad de llantas");
        txtCantidadLLantas.setName("numerico"); // NOI18N
        txtCantidadLLantas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCantidadLLantasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCantidadLLantasFocusLost(evt);
            }
        });
        txtCantidadLLantas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadLLantasKeyPressed(evt);
            }
        });

        txtSerialChasis.setEditable(false);
        txtSerialChasis.setToolTipText("Ingresar el serial del chasis del vehículo");
        txtSerialChasis.setName(""); // NOI18N
        txtSerialChasis.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSerialChasisFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSerialChasisFocusLost(evt);
            }
        });
        txtSerialChasis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSerialChasisKeyPressed(evt);
            }
        });

        txtSerialMotor.setEditable(false);
        txtSerialMotor.setToolTipText("Ingresar el serial del motor");
        txtSerialMotor.setName(""); // NOI18N
        txtSerialMotor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSerialMotorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSerialMotorFocusLost(evt);
            }
        });
        txtSerialMotor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSerialMotorKeyPressed(evt);
            }
        });

        txtKmCambioAceiteMotor.setEditable(false);
        txtKmCambioAceiteMotor.setToolTipText("Ingresar el ekilometraje para cambio de aceite del motro");
        txtKmCambioAceiteMotor.setName("numerico"); // NOI18N
        txtKmCambioAceiteMotor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKmCambioAceiteMotorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKmCambioAceiteMotorFocusLost(evt);
            }
        });
        txtKmCambioAceiteMotor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKmCambioAceiteMotorKeyPressed(evt);
            }
        });

        txtKmCambioValvulina.setEditable(false);
        txtKmCambioValvulina.setToolTipText("Ingresar el kilometraje para el cambio de la valvulina de la caja");
        txtKmCambioValvulina.setName("numerico"); // NOI18N
        txtKmCambioValvulina.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKmCambioValvulinaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKmCambioValvulinaFocusLost(evt);
            }
        });
        txtKmCambioValvulina.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKmCambioValvulinaKeyPressed(evt);
            }
        });

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Km Cambio Valv. Trasmisión");

        txtKmCambioValvulinaTrasmision.setEditable(false);
        txtKmCambioValvulinaTrasmision.setToolTipText("Ingresar el kilometraje para el cambio de valvulina de la tramision");
        txtKmCambioValvulinaTrasmision.setName("numerico"); // NOI18N
        txtKmCambioValvulinaTrasmision.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKmCambioValvulinaTrasmisionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKmCambioValvulinaTrasmisionFocusLost(evt);
            }
        });
        txtKmCambioValvulinaTrasmision.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKmCambioValvulinaTrasmisionKeyPressed(evt);
            }
        });

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Agencia");

        cbxAgencias.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbxAgencias.setToolTipText("Seleccionar la Agencia");
        cbxAgencias.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxAgenciasItemStateChanged(evt);
            }
        });
        cbxAgencias.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxAgenciasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbxAgenciasFocusLost(evt);
            }
        });
        cbxAgencias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxAgenciasKeyPressed(evt);
            }
        });

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Tipo Contrato");

        cbxTiposContratos.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbxTiposContratos.setToolTipText("Seleccionar la Agencia");
        cbxTiposContratos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTiposContratosItemStateChanged(evt);
            }
        });
        cbxTiposContratos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxTiposContratosFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbxTiposContratosFocusLost(evt);
            }
        });
        cbxTiposContratos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxTiposContratosKeyPressed(evt);
            }
        });

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Peso Total Autorizado");

        txtPesoTotalAutorizado.setEditable(false);
        txtPesoTotalAutorizado.setToolTipText("");
        txtPesoTotalAutorizado.setName("numerico"); // NOI18N
        txtPesoTotalAutorizado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPesoTotalAutorizadoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPesoTotalAutorizadoFocusLost(evt);
            }
        });
        txtPesoTotalAutorizado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesoTotalAutorizadoKeyPressed(evt);
            }
        });

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Capacidad Instalada");

        txtCapacidadInstalad.setEditable(false);
        txtCapacidadInstalad.setToolTipText("Ingresar la longitud de la carroceria");
        txtCapacidadInstalad.setName("numerico"); // NOI18N
        txtCapacidadInstalad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCapacidadInstaladFocusGained(evt);
            }
        });
        txtCapacidadInstalad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCapacidadInstaladActionPerformed(evt);
            }
        });
        txtCapacidadInstalad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCapacidadInstaladKeyPressed(evt);
            }
        });

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("km. Inicial");

        txtKmInicial.setEditable(false);
        txtKmInicial.setToolTipText("Ingresar el kilometraje para el cambio de valvulina de la tramision");
        txtKmInicial.setName("numerico"); // NOI18N
        txtKmInicial.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKmInicialFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKmInicialFocusLost(evt);
            }
        });
        txtKmInicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKmInicialKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPesoSinCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLargo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAlto, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAncho, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCapacidadInstalad, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTarjetaDePropiedad)
                    .addComponent(txtSerialChasis)
                    .addComponent(txtSerialMotor)
                    .addComponent(cbxTipoCombustible, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxAgencias, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxTiposContratos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chkVehiculoActivo, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(txtPesoTotalAutorizado, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(txtKmInicial)
                    .addComponent(txtKmCambioValvulinaTrasmision)
                    .addComponent(txtKmCambioValvulina)
                    .addComponent(txtKmCambioAceiteMotor)
                    .addComponent(txtTamanioLLantas, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                    .addComponent(txtCantidadLLantas))
                .addGap(66, 66, 66))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesoSinCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(txtLargo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAlto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtAncho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtCapacidadInstalad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTarjetaDePropiedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtCantidadLLantas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTamanioLLantas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtSerialChasis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtSerialMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxTipoCombustible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtKmCambioAceiteMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(txtKmCambioValvulina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(txtKmCambioValvulinaTrasmision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(txtKmInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(txtPesoTotalAutorizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(cbxAgencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(cbxTiposContratos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(chkVehiculoActivo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        panelFotografia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelFotografia.setComponentPopupMenu(pumImagen);

        javax.swing.GroupLayout panelFotografiaLayout = new javax.swing.GroupLayout(panelFotografia);
        panelFotografia.setLayout(panelFotografiaLayout);
        panelFotografiaLayout.setHorizontalGroup(
            panelFotografiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 345, Short.MAX_VALUE)
        );
        panelFotografiaLayout.setVerticalGroup(
            panelFotografiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/exit.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setToolTipText("Salir de ingresar Vehículos");
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
                .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
            .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        txtPlaca.setEditable(false);
        txtPlaca.setToolTipText("Ingresar placa del Vehículo");
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

        cbxMarcaVehiculo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbxMarcaVehiculo.setToolTipText("Seleccionar la Marca del vehículo");
        cbxMarcaVehiculo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxMarcaVehiculoItemStateChanged(evt);
            }
        });
        cbxMarcaVehiculo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxMarcaVehiculoFocusGained(evt);
            }
        });
        cbxMarcaVehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMarcaVehiculoActionPerformed(evt);
            }
        });
        cbxMarcaVehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxMarcaVehiculoKeyPressed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Placa");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Marca");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Linea");

        cbxLineaVehiculo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbxLineaVehiculo.setToolTipText("Selecionar la línea del vehículo");
        cbxLineaVehiculo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxLineaVehiculoItemStateChanged(evt);
            }
        });
        cbxLineaVehiculo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxLineaVehiculoFocusGained(evt);
            }
        });
        cbxLineaVehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLineaVehiculoActionPerformed(evt);
            }
        });
        cbxLineaVehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxLineaVehiculoKeyPressed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Modelo");

        txtModelo.setEditable(false);
        txtModelo.setToolTipText("Ingresar el modelo del vehículo");
        txtModelo.setName("numerico"); // NOI18N
        txtModelo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtModeloFocusGained(evt);
            }
        });
        txtModelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtModeloKeyPressed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Tipo");

        cbxTipoVehiculo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbxTipoVehiculo.setToolTipText("Seleccionar el tipo de vehículo");
        cbxTipoVehiculo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoVehiculoItemStateChanged(evt);
            }
        });
        cbxTipoVehiculo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxTipoVehiculoFocusGained(evt);
            }
        });
        cbxTipoVehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTipoVehiculoActionPerformed(evt);
            }
        });
        cbxTipoVehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxTipoVehiculoKeyPressed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Carrocreria");

        cbxTipoCarroceria.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbxTipoCarroceria.setToolTipText("Seleccionar el tipo de carrocería");
        cbxTipoCarroceria.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoCarroceriaItemStateChanged(evt);
            }
        });
        cbxTipoCarroceria.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxTipoCarroceriaFocusGained(evt);
            }
        });
        cbxTipoCarroceria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTipoCarroceriaActionPerformed(evt);
            }
        });
        cbxTipoCarroceria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxTipoCarroceriaKeyPressed(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Servicio");

        cbxTipoDeServicio.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cbxTipoDeServicio.setToolTipText("Seleccionar el tipo de Servicio del vehículo");
        cbxTipoDeServicio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoDeServicioItemStateChanged(evt);
            }
        });
        cbxTipoDeServicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxTipoDeServicioFocusGained(evt);
            }
        });
        cbxTipoDeServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTipoDeServicioActionPerformed(evt);
            }
        });
        cbxTipoDeServicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxTipoDeServicioKeyPressed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Propietario");

        txtPropietario.setEditable(false);
        txtPropietario.setToolTipText("ingresar el  nombre del propietario");
        txtPropietario.setName(""); // NOI18N
        txtPropietario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPropietarioFocusGained(evt);
            }
        });
        txtPropietario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPropietarioKeyPressed(evt);
            }
        });

        lblCirculoDeProgreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPropietario, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxLineaVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxTipoVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxTipoCarroceria, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxTipoDeServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxMarcaVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(panelFotografia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(panelFotografia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbxMarcaVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxLineaVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbxTipoVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbxTipoCarroceria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbxTipoDeServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPropietario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void salir() {
        this.dispose();
        this.setVisible(false);
    }

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void txtPesoSinCargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesoSinCargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoSinCargaActionPerformed

    private void txtModeloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModeloKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxTipoVehiculo.requestFocus();
        }
    }//GEN-LAST:event_txtModeloKeyPressed

    private void txtPropietarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPropietarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPesoSinCarga.requestFocus();
        }
    }//GEN-LAST:event_txtPropietarioKeyPressed

    private void txtPesoSinCargaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesoSinCargaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtLargo.requestFocus();
        }
    }//GEN-LAST:event_txtPesoSinCargaKeyPressed

    private void txtLargoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLargoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtAlto.requestFocus();
        }
    }//GEN-LAST:event_txtLargoKeyPressed

    private void txtAltoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAltoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtAncho.requestFocus();
        }
    }//GEN-LAST:event_txtAltoKeyPressed

    private void txtTarjetaDePropiedadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarjetaDePropiedadKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCantidadLLantas.requestFocus();
        }
    }//GEN-LAST:event_txtTarjetaDePropiedadKeyPressed

    private void txtTamanioLLantasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTamanioLLantasKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtSerialChasis.requestFocus();
        }
    }//GEN-LAST:event_txtTamanioLLantasKeyPressed

    private void cbxTipoCombustibleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoCombustibleItemStateChanged
        //cbxCentrosDeCosto.requestFocus();
    }//GEN-LAST:event_cbxTipoCombustibleItemStateChanged

    private void txtPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {

            FBuscarVehiculo form = new FBuscarVehiculo(ini, this);
            PrincipalAdministrador.escritorio.add(form);
            form.toFront();
            form.setClosable(true);
            form.setVisible(true);
            form.setTitle("Formulario para buscar Vehículos  por Placas");
            form.txtPlaca.requestFocus();
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            form.show();

        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
          //consultarCarro();
          new Thread(new HiloIngresarCarros(ini, this, "consultarCarro")).start();
        }
    }//GEN-LAST:event_txtPlacaKeyPressed

    private void txtTarjetaDePropiedadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTarjetaDePropiedadFocusLost

    }//GEN-LAST:event_txtTarjetaDePropiedadFocusLost

    private void txtTamanioLLantasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTamanioLLantasFocusLost

    }//GEN-LAST:event_txtTamanioLLantasFocusLost

    private void cbxTipoCombustibleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTipoCombustibleFocusGained

    }//GEN-LAST:event_cbxTipoCombustibleFocusGained

    private void cbxTipoCombustibleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTipoCombustibleFocusLost
        try {
            //ini.llenarCentrosDeCosto();
        } catch (Exception ex) {
            Logger.getLogger(IngresarCarros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cbxTipoCombustibleFocusLost

    private void txtPlacaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusGained
        txtPlaca.setSelectionStart(0);
        txtPlaca.setSelectionEnd(txtPlaca.getText().length());
    }//GEN-LAST:event_txtPlacaFocusGained

    private void txtModeloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtModeloFocusGained
        txtModelo.setSelectionStart(0);
        txtModelo.setSelectionEnd(txtModelo.getText().length());
    }//GEN-LAST:event_txtModeloFocusGained

    private void txtPropietarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPropietarioFocusGained
        txtPropietario.setSelectionStart(0);
        txtPropietario.setSelectionEnd(txtPropietario.getText().length());
    }//GEN-LAST:event_txtPropietarioFocusGained

    private void txtPesoSinCargaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPesoSinCargaFocusGained
        txtPesoSinCarga.setSelectionStart(0);
        txtPesoSinCarga.setSelectionEnd(txtPesoSinCarga.getText().length());
    }//GEN-LAST:event_txtPesoSinCargaFocusGained

    private void txtLargoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLargoFocusGained
        txtLargo.setSelectionStart(0);
        txtLargo.setSelectionEnd(txtLargo.getText().length());
    }//GEN-LAST:event_txtLargoFocusGained

    private void txtAltoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAltoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAltoActionPerformed

    private void txtAltoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAltoFocusGained
        txtAlto.setSelectionStart(0);
        txtAlto.setSelectionEnd(txtAlto.getText().length());
    }//GEN-LAST:event_txtAltoFocusGained

    private void txtTarjetaDePropiedadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTarjetaDePropiedadFocusGained
        txtTarjetaDePropiedad.setSelectionStart(0);
        txtTarjetaDePropiedad.setSelectionEnd(txtTarjetaDePropiedad.getText().length());
    }//GEN-LAST:event_txtTarjetaDePropiedadFocusGained

    private void txtTamanioLLantasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTamanioLLantasFocusGained
        txtTamanioLLantas.setSelectionStart(0);
        txtTamanioLLantas.setSelectionEnd(txtTamanioLLantas.getText().length());
    }//GEN-LAST:event_txtTamanioLLantasFocusGained

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void nuevo() {
        if (actualizar) {
            actualizarFoto = true;
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            habilitar(true);
            txtPlaca.setEditable(false);
            nuevo = false;
            btnGrabar.setEnabled(true);
            jBtnGrabar.setEnabled(true);
           

        } else {
            nuevo = true;
            limpiar();
            actualizarFoto = true;
            txtPlaca.setEditable(true);

        }
  jBtnRefrescar.setEnabled(false);
  txtPlaca.requestFocus();
    }

    private void agregarImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarImagenMouseClicked
        if (actualizarFoto) {
            if (!txtPlaca.getText().isEmpty()) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Elija la imagen de la fotografía del Vehículo");
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "JPG & GIF Imagenes", "jpg", "gif", "png");
                chooser.setFileFilter(filter);

                int returnVal = chooser.showOpenDialog(chooser);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    fileFoto = chooser.getSelectedFile();
                    extension = fileFoto.getAbsolutePath().substring(fileFoto.getAbsolutePath().lastIndexOf(".") + 1);

                    if (fileFoto.length() > 1048576) {
                        JOptionPane.showMessageDialog(null, "Archivo es muy grande, debe tener menos de un mega", "Alerta", 1);
                        fileFoto = null;
                    } else {
                        if (fileFoto != null) {
                            foto = new ImageIcon(fileFoto.getPath());
                            //this.foto = fileFoto;
                            panelFotografia.setIcon(foto);
                            panelFotografia.updateUI();

                            tieneFoto = true;

                            carro.setFotografia(fileFoto);
                            carro.setImagen(foto);
                            carro.setTipoMime(extension);

//                            javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(panelFotografia);
//                            panelFotografia.setLayout(panelImage1Layout);
//                            panelImage1Layout.setHorizontalGroup(
//                                    panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                    .addGap(0, 329, Short.MAX_VALUE));
//                            panelImage1Layout.setVerticalGroup(
//                                    panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                    .addGap(0, 405, Short.MAX_VALUE));
                        }
                    }

                }
            } else {
            }
        }

    }//GEN-LAST:event_agregarImagenMouseClicked

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        grabar();
       

    }//GEN-LAST:event_btnGrabarActionPerformed

    private void grabar() throws HeadlessException {
        if (!txtPlaca.getText().isEmpty()) {
            int x = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", 0);
            if (x == 0) {

                new Thread(new HiloIngresarCarros(ini, this, "guardar")).start();
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "No se pueden Guardar Datos con campos vacíos", "Error al guardar", 2);
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cancelar() {
        limpiar();
        nuevo = false;
        actualizar = false;
        habilitar(false);
        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);
        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);
          jBtnRefrescar.setEnabled(true);
    }

    private void cbxTipoCombustibleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxTipoCombustibleKeyPressed

    }//GEN-LAST:event_cbxTipoCombustibleKeyPressed

    private void chkVehiculoActivoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkVehiculoActivoStateChanged
        if (chkVehiculoActivo.isSelected()) {
            chkVehiculoActivo.setText("Vehículo Activo");
        } else {
            chkVehiculoActivo.setText("Vehículo No Activo");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_chkVehiculoActivoStateChanged

    private void cbxMarcaVehiculoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxMarcaVehiculoItemStateChanged
        try {

            if (nuevo || actualizar) {
                CMarcasDeVehiculos marca = new CMarcasDeVehiculos(ini);

                cbxLineaVehiculo.removeAllItems();

                for (CMarcasDeVehiculos obj : ini.getListaDeMarcasDeVehiculos()) {
                    if (obj.getNombreMarcaDeVehiculos().equals(cbxMarcaVehiculo.getSelectedItem().toString())) {
                        marca = obj;
                        break;

                    }
                }

                for (CLineasPorMarca obj : ini.getListaDeLineasPorMarca()) {
                    if (obj.getIdMarcaDeVehiculo()== marca.getIdMarcaDeVehiculos()) {
                        cbxLineaVehiculo.addItem(obj.getNombreLineaVehiculo());
                    }
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(IngresarCarros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cbxMarcaVehiculoItemStateChanged

    private void cbxMarcaVehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxMarcaVehiculoActionPerformed

    }//GEN-LAST:event_cbxMarcaVehiculoActionPerformed

    private void cbxMarcaVehiculoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxMarcaVehiculoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxMarcaVehiculoFocusGained

    private void cbxMarcaVehiculoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxMarcaVehiculoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxMarcaVehiculoKeyPressed

    private void cbxLineaVehiculoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxLineaVehiculoItemStateChanged


    }//GEN-LAST:event_cbxLineaVehiculoItemStateChanged

    private void cbxLineaVehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLineaVehiculoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxLineaVehiculoActionPerformed

    private void cbxLineaVehiculoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxLineaVehiculoFocusGained

    }//GEN-LAST:event_cbxLineaVehiculoFocusGained

    private void cbxLineaVehiculoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxLineaVehiculoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxLineaVehiculoKeyPressed

    private void cbxTipoVehiculoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoVehiculoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoVehiculoItemStateChanged

    private void cbxTipoVehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTipoVehiculoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoVehiculoActionPerformed

    private void cbxTipoVehiculoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTipoVehiculoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoVehiculoFocusGained

    private void cbxTipoVehiculoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxTipoVehiculoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoVehiculoKeyPressed

    private void cbxTipoCarroceriaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoCarroceriaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoCarroceriaItemStateChanged

    private void cbxTipoCarroceriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTipoCarroceriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoCarroceriaActionPerformed

    private void cbxTipoCarroceriaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTipoCarroceriaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoCarroceriaFocusGained

    private void cbxTipoCarroceriaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxTipoCarroceriaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoCarroceriaKeyPressed

    private void cbxTipoDeServicioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoDeServicioItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoDeServicioItemStateChanged

    private void cbxTipoDeServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTipoDeServicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoDeServicioActionPerformed

    private void cbxTipoDeServicioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTipoDeServicioFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoDeServicioFocusGained

    private void cbxTipoDeServicioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxTipoDeServicioKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoDeServicioKeyPressed

    private void txtAnchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAnchoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAnchoActionPerformed

    private void txtAnchoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAnchoFocusGained
        txtAncho.setSelectionStart(0);
        txtAncho.setSelectionEnd(txtAncho.getText().length());
    }//GEN-LAST:event_txtAnchoFocusGained

    private void txtAnchoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnchoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtLongitud.requestFocus();
        }
    }//GEN-LAST:event_txtAnchoKeyPressed

    private void txtLongitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLongitudActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLongitudActionPerformed

    private void txtLongitudFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLongitudFocusGained
        txtLongitud.setSelectionStart(0);
        txtLongitud.setSelectionEnd(txtLongitud.getText().length());
    }//GEN-LAST:event_txtLongitudFocusGained

    private void txtLongitudKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLongitudKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCapacidadInstalad.requestFocus();
        }
    }//GEN-LAST:event_txtLongitudKeyPressed

    private void txtCantidadLLantasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCantidadLLantasFocusGained
        txtCantidadLLantas.setSelectionStart(0);
        txtCantidadLLantas.setSelectionEnd(txtCantidadLLantas.getText().length());
    }//GEN-LAST:event_txtCantidadLLantasFocusGained

    private void txtCantidadLLantasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCantidadLLantasFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadLLantasFocusLost

    private void txtCantidadLLantasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadLLantasKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtTamanioLLantas.requestFocus();
        }
    }//GEN-LAST:event_txtCantidadLLantasKeyPressed

    private void txtSerialChasisFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSerialChasisFocusGained
        txtSerialChasis.setSelectionStart(0);
        txtSerialChasis.setSelectionEnd(txtSerialChasis.getText().length());
    }//GEN-LAST:event_txtSerialChasisFocusGained

    private void txtSerialChasisFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSerialChasisFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerialChasisFocusLost

    private void txtSerialChasisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerialChasisKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtSerialMotor.requestFocus();
        }
    }//GEN-LAST:event_txtSerialChasisKeyPressed

    private void txtSerialMotorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSerialMotorFocusGained
        txtSerialMotor.setSelectionStart(0);
        txtSerialMotor.setSelectionEnd(txtSerialMotor.getText().length());
    }//GEN-LAST:event_txtSerialMotorFocusGained

    private void txtSerialMotorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSerialMotorFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerialMotorFocusLost

    private void txtSerialMotorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerialMotorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxTipoCombustible.requestFocus();
        }
    }//GEN-LAST:event_txtSerialMotorKeyPressed

    private void txtKmCambioAceiteMotorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKmCambioAceiteMotorFocusGained
        txtKmCambioAceiteMotor.setSelectionStart(0);
        txtKmCambioAceiteMotor.setSelectionEnd(txtKmCambioAceiteMotor.getText().length());
    }//GEN-LAST:event_txtKmCambioAceiteMotorFocusGained

    private void txtKmCambioAceiteMotorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKmCambioAceiteMotorFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmCambioAceiteMotorFocusLost

    private void txtKmCambioAceiteMotorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKmCambioAceiteMotorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtKmCambioValvulina.requestFocus();
        }
    }//GEN-LAST:event_txtKmCambioAceiteMotorKeyPressed

    private void txtKmCambioValvulinaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKmCambioValvulinaFocusGained
        txtKmCambioValvulina.setSelectionStart(0);
        txtKmCambioValvulina.setSelectionEnd(txtKmCambioValvulina.getText().length());
    }//GEN-LAST:event_txtKmCambioValvulinaFocusGained

    private void txtKmCambioValvulinaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKmCambioValvulinaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmCambioValvulinaFocusLost

    private void txtKmCambioValvulinaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKmCambioValvulinaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtKmCambioValvulinaTrasmision.requestFocus();
        }
    }//GEN-LAST:event_txtKmCambioValvulinaKeyPressed

    private void txtKmCambioValvulinaTrasmisionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKmCambioValvulinaTrasmisionFocusGained
        txtKmCambioValvulinaTrasmision.setSelectionStart(0);
        txtKmCambioValvulinaTrasmision.setSelectionEnd(txtKmCambioValvulinaTrasmision.getText().length());
    }//GEN-LAST:event_txtKmCambioValvulinaTrasmisionFocusGained

    private void txtKmCambioValvulinaTrasmisionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKmCambioValvulinaTrasmisionFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmCambioValvulinaTrasmisionFocusLost

    private void txtKmCambioValvulinaTrasmisionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKmCambioValvulinaTrasmisionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtKmInicial.requestFocus();
        }
    }//GEN-LAST:event_txtKmCambioValvulinaTrasmisionKeyPressed

    private void cbxAgenciasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxAgenciasItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxAgenciasItemStateChanged

    private void cbxAgenciasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxAgenciasFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxAgenciasFocusGained

    private void cbxAgenciasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxAgenciasFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxAgenciasFocusLost

    private void cbxAgenciasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxAgenciasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxAgenciasKeyPressed

    private void cbxTiposContratosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTiposContratosItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTiposContratosItemStateChanged

    private void cbxTiposContratosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTiposContratosFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTiposContratosFocusGained

    private void cbxTiposContratosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTiposContratosFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTiposContratosFocusLost

    private void cbxTiposContratosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxTiposContratosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTiposContratosKeyPressed

    private void txtPesoTotalAutorizadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPesoTotalAutorizadoFocusGained
        txtPesoTotalAutorizado.setSelectionStart(0);
        txtPesoTotalAutorizado.setSelectionEnd(txtPesoTotalAutorizado.getText().length());
    }//GEN-LAST:event_txtPesoTotalAutorizadoFocusGained

    private void txtPesoTotalAutorizadoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPesoTotalAutorizadoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoTotalAutorizadoFocusLost

    private void txtPesoTotalAutorizadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesoTotalAutorizadoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxAgencias.requestFocus();
        }
    }//GEN-LAST:event_txtPesoTotalAutorizadoKeyPressed

    private void cbxTipoCombustibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTipoCombustibleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoCombustibleActionPerformed

    private void txtCapacidadInstaladActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCapacidadInstaladActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCapacidadInstaladActionPerformed

    private void txtCapacidadInstaladFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCapacidadInstaladFocusGained
        txtCapacidadInstalad.setSelectionStart(0);
        txtCapacidadInstalad.setSelectionEnd(txtCapacidadInstalad.getText().length());
    }//GEN-LAST:event_txtCapacidadInstaladFocusGained

    private void txtCapacidadInstaladKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCapacidadInstaladKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtTarjetaDePropiedad.requestFocus();
        }
    }//GEN-LAST:event_txtCapacidadInstaladKeyPressed

    private void txtLargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLargoActionPerformed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
        grabar();
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jBtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_jBtnCancelarActionPerformed

    private void jBtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSalirActionPerformed
        salir();
    }//GEN-LAST:event_jBtnSalirActionPerformed

    private void txtKmInicialFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKmInicialFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmInicialFocusGained

    private void txtKmInicialFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKmInicialFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmInicialFocusLost

    private void txtKmInicialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKmInicialKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPesoTotalAutorizado.requestFocus();
        }
    }//GEN-LAST:event_txtKmInicialKeyPressed

    private void jBtnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRefrescarActionPerformed
  new Thread(new HiloIngresarCarros(ini, this, "refrescar")).start();       // TODO add your handling code here:
    }//GEN-LAST:event_jBtnRefrescarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    public javax.swing.JComboBox cbxAgencias;
    public javax.swing.JComboBox cbxLineaVehiculo;
    public javax.swing.JComboBox cbxMarcaVehiculo;
    public javax.swing.JComboBox cbxTipoCarroceria;
    public javax.swing.JComboBox cbxTipoCombustible;
    public javax.swing.JComboBox cbxTipoDeServicio;
    public javax.swing.JComboBox cbxTipoVehiculo;
    public javax.swing.JComboBox cbxTiposContratos;
    public javax.swing.JCheckBox chkVehiculoActivo;
    public javax.swing.JToggleButton jBtnCancelar;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JButton jBtnNuevo;
    public javax.swing.JToggleButton jBtnRefrescar;
    public javax.swing.JToggleButton jBtnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso1;
    public org.edisoncor.gui.panel.PanelImage panelFotografia;
    private javax.swing.JPopupMenu pumImagen;
    public javax.swing.JTextField txtAlto;
    public javax.swing.JTextField txtAncho;
    public javax.swing.JTextField txtCantidadLLantas;
    public javax.swing.JTextField txtCapacidadInstalad;
    public javax.swing.JTextField txtKmCambioAceiteMotor;
    public javax.swing.JTextField txtKmCambioValvulina;
    public javax.swing.JTextField txtKmCambioValvulinaTrasmision;
    public javax.swing.JTextField txtKmInicial;
    public javax.swing.JTextField txtLargo;
    public javax.swing.JTextField txtLongitud;
    public javax.swing.JTextField txtModelo;
    public javax.swing.JTextField txtPesoSinCarga;
    public javax.swing.JTextField txtPesoTotalAutorizado;
    public javax.swing.JTextField txtPlaca;
    public javax.swing.JTextField txtPropietario;
    public javax.swing.JTextField txtSerialChasis;
    public javax.swing.JTextField txtSerialMotor;
    public javax.swing.JTextField txtTamanioLLantas;
    public javax.swing.JTextField txtTarjetaDePropiedad;
    // End of variables declaration//GEN-END:variables

    public void llenarCamposDeTexto() {
       

    }

    public void habilitar(boolean valor) {

        try {
            txtPlaca.setEditable(valor);
            txtModelo.setEditable(valor);
            txtPropietario.setEditable(valor);
            txtModelo.setEditable(valor);
            txtPropietario.setEditable(valor);
            txtPesoSinCarga.setEditable(valor);
            txtLargo.setEditable(valor);
            txtAlto.setEditable(valor);
            txtAncho.setEditable(valor);
            txtLongitud.setEditable(valor);
            txtTarjetaDePropiedad.setEditable(valor);
            txtCantidadLLantas.setEditable(valor);
            txtTamanioLLantas.setEditable(valor);
            txtSerialChasis.setEditable(valor);
            txtCapacidadInstalad.setEditable(valor);
            //txtTrailer.setEditable(valor);
            txtSerialMotor.setEditable(valor);
            txtKmCambioAceiteMotor.setEditable(valor);
            txtKmCambioValvulina.setEditable(valor);
            txtKmCambioValvulinaTrasmision.setEditable(valor);
            txtPesoTotalAutorizado.setEditable(valor);
            txtKmInicial.setEnabled(valor);
            txtKmInicial.setEditable(valor);

            cbxMarcaVehiculo.setEnabled(valor);
            cbxLineaVehiculo.setEnabled(valor);
            cbxTipoVehiculo.setEnabled(valor);
            cbxTipoCarroceria.setEnabled(valor);
            cbxTipoDeServicio.setEnabled(valor);
            cbxTipoCombustible.setEnabled(valor);
            cbxAgencias.setEnabled(valor);
            cbxTiposContratos.setEnabled(valor);
              jBtnRefrescar.setEnabled(valor);

        } catch (Exception ex) {
        }

    }

    public boolean validar() {
        boolean verificado = true;
        mensaje = "";
       

        return verificado;
    }

    public boolean guardarRegistroVehiculoCarro() {
        boolean guardado = false;
        try {

           // guardado = carro.grabarCarros();

        } catch (Exception ex) {
            Logger.getLogger(IngresarCarros.class.getName()).log(Level.SEVERE, null, ex);

        }
        return guardado;
    }

    private void limpiar() {

        try {
            txtPlaca.setText("");
            txtModelo.setText("");
            txtPropietario.setText("");
            txtModelo.setText("");
            txtPropietario.setText("");
            txtPesoSinCarga.setText("");
            txtLargo.setText("");
            txtAlto.setText("");
            txtAncho.setText("");
            txtLongitud.setText("");
            txtTarjetaDePropiedad.setText("");
            txtCantidadLLantas.setText("");
            txtTamanioLLantas.setText("");
            txtSerialChasis.setText("");
            //txtTrailer.setText("0");
            txtSerialMotor.setText("");
            txtKmCambioAceiteMotor.setText("");
            txtKmCambioValvulina.setText("");
            txtKmCambioValvulinaTrasmision.setText("");
            txtCapacidadInstalad.setText("");
            txtPesoTotalAutorizado.setText("");
            txtKmInicial.setText("");

            panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg")));
            panelFotografia.updateUI();

            btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); //
            btnNuevo.setText("Nuevo");
            btnNuevo.setEnabled(true);
            jBtnNuevo.setEnabled(true);
            actualizarFoto = false;
            actualizar = false;
            tieneFoto = false;
        } catch (Exception ex) {
        }

    }

}
