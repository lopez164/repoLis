package mtto.proveedores;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import aplicacionlogistica.configuracion.organizacion.CRegionales;
import aplicacionlogistica.configuracion.organizacion.CZonas;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Color;
import java.awt.Component;
import mtto.documentos.Threads.HiloListadoDeCuentasSecundarias;
import mtto.ingresoDeRegistros.objetos.CCuentaSecundariaLogistica;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import ui.swing.searchText.DataSearch;
import ui.swing.searchText.EventClick;
import ui.swing.searchText.PanelSearch;

/**
 *
 * @author VLI_488
 */
public class IngresarSucursalDeProveedor extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public boolean actualizar;
    public boolean estaOcupadoGrabando = false;

    String mensaje = null;

    public Inicio ini;
    String suario;
    CUsuarios user;
    public Cproveedores proveedor;
    public SucursalesPorproveedor sucursal;
    CDepartamentos departamento;
    CCiudades ciudad;
    CZonas zona;
    CRegionales regional;
    CAgencias agencia;
    ArrayList<CCuentaSecundariaLogistica> listaDeCuentasSecundarias = null;
    ArrayList<CCuentaSecundariaLogistica> listaDeServiciosSucursal = null;
    final DefaultListModel model = new DefaultListModel();

    public TextAutoCompleter autoTxtNitProveedores;

    private Connection conexion;
    private PanelSearch search;
    private JPopupMenu menu;
    boolean sucursalElegida = false;
    public boolean validado = false;
    public boolean cargado = false;

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public IngresarSucursalDeProveedor(Inicio ini) {
        initComponents();
        this.ini = ini;
        user = ini.getUser();

        /* codigo de busqueda de sucursales */
        menu = new JPopupMenu();
        conexion = ini.getConnRemota();
        search = new PanelSearch();
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick(new EventClick() {

            /**
             * **************************************
             */
            @Override
            public void itemClick(DataSearch data) {

                menu.setVisible(false);
                txtNombreSucursal.setText(data.getText());
                addStory(data.getText());
                System.out.println("Click Item : " + data.getText());
                //new Thread(new HiloIngresarSucursalProveedores(ini,this.fingresarSucursalDeProveedor, "consultarSucursalProveedor")).start();
                sucursalElegida = true;
                txtTelefono.requestFocus();

            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                search.remove(com);
                removeHistory(data.getText());
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                if (search.getItemSize() == 0) {
                    menu.setVisible(false);
                }
                System.out.println("Remove Item : " + data.getText());
            }

        });

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

        new Thread(new HiloIngresarSucursalProveedores(ini, this, "cargarLaVista")).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNit = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombreSucursal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbxDepartamentos = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        cbxCiudades = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCelular = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtContacto = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        cbxAgencias = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNombreProveedor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCodigoInternoSucursal = new javax.swing.JTextField();
        chkActivo = new javax.swing.JCheckBox();
        txtLatitud = new javax.swing.JTextField();
        txtLongitud = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtBarrio = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCuentasSecundarias = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario para el ingreso de Sucursales del Proveedor");
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

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nit");

        txtNit.setEditable(false);
        txtNit.setToolTipText("Ingresar documento de identificación");
        txtNit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNitFocusGained(evt);
            }
        });
        txtNit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNitActionPerformed(evt);
            }
        });
        txtNit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNitKeyPressed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("nombre Sucursal");

        txtNombreSucursal.setEditable(false);
        txtNombreSucursal.setToolTipText("Ingresar Nombres completos");
        txtNombreSucursal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreSucursalFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombreSucursalFocusLost(evt);
            }
        });
        txtNombreSucursal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNombreSucursalMouseClicked(evt);
            }
        });
        txtNombreSucursal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreSucursalKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreSucursalKeyReleased(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Direccion");

        txtDireccion.setEditable(false);
        txtDireccion.setToolTipText("Ingresar dirección");
        txtDireccion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDireccionFocusGained(evt);
            }
        });
        txtDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionActionPerformed(evt);
            }
        });
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionKeyPressed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Departamento");

        cbxDepartamentos.setToolTipText("Seleccionar el Departamento ó estado");
        cbxDepartamentos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxDepartamentosItemStateChanged(evt);
            }
        });
        cbxDepartamentos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxDepartamentosFocusGained(evt);
            }
        });
        cbxDepartamentos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxDepartamentosKeyPressed(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Ciudad");

        cbxCiudades.setToolTipText("Seleccionar la ciudad");
        cbxCiudades.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCiudadesItemStateChanged(evt);
            }
        });
        cbxCiudades.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxCiudadesFocusGained(evt);
            }
        });
        cbxCiudades.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxCiudadesKeyPressed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Teléfono");

        txtTelefono.setEditable(false);
        txtTelefono.setToolTipText("ingresar el #  de teléfono fijo");
        txtTelefono.setName("numerico"); // NOI18N
        txtTelefono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoFocusGained(evt);
            }
        });
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyPressed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Celular");

        txtCelular.setEditable(false);
        txtCelular.setToolTipText("Ingreasr el # celular particular");
        txtCelular.setName("numerico"); // NOI18N
        txtCelular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCelularFocusGained(evt);
            }
        });
        txtCelular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCelularActionPerformed(evt);
            }
        });
        txtCelular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCelularKeyPressed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Contacto");

        txtContacto.setEditable(false);
        txtContacto.setToolTipText("Ingreasar nombre del contacto");
        txtContacto.setName(""); // NOI18N
        txtContacto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtContactoFocusGained(evt);
            }
        });
        txtContacto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtContactoKeyPressed(evt);
            }
        });

        txtEmail.setEditable(false);
        txtEmail.setToolTipText("Ingreasar correo electronico");
        txtEmail.setName("minuscula"); // NOI18N
        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailFocusGained(evt);
            }
        });
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmailKeyPressed(evt);
            }
        });

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("E-Mail");

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

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Agencia");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Proveedor");

        txtNombreProveedor.setEditable(false);
        txtNombreProveedor.setToolTipText("Ingresar Nombres completos");
        txtNombreProveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreProveedorFocusGained(evt);
            }
        });
        txtNombreProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreProveedorKeyPressed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Id Sucursal");

        txtCodigoInternoSucursal.setEditable(false);
        txtCodigoInternoSucursal.setToolTipText("Ingresar Nombres completos");
        txtCodigoInternoSucursal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCodigoInternoSucursalFocusGained(evt);
            }
        });
        txtCodigoInternoSucursal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoInternoSucursalKeyPressed(evt);
            }
        });

        chkActivo.setSelected(true);
        chkActivo.setText("Sucursal Activo");
        chkActivo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkActivoStateChanged(evt);
            }
        });
        chkActivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActivoActionPerformed(evt);
            }
        });

        txtLatitud.setEditable(false);
        txtLatitud.setToolTipText("Ingreasar correo electronico");
        txtLatitud.setName("minuscula"); // NOI18N
        txtLatitud.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLatitudFocusGained(evt);
            }
        });
        txtLatitud.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLatitudKeyPressed(evt);
            }
        });

        txtLongitud.setEditable(false);
        txtLongitud.setToolTipText("Ingreasar correo electronico");
        txtLongitud.setName("minuscula"); // NOI18N
        txtLongitud.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLongitudFocusGained(evt);
            }
        });
        txtLongitud.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLongitudKeyPressed(evt);
            }
        });

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Latitud");

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("Longitud");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Barrio");

        txtBarrio.setEditable(false);
        txtBarrio.setToolTipText("Ingresar dirección");
        txtBarrio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarrioFocusGained(evt);
            }
        });
        txtBarrio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarrioActionPerformed(evt);
            }
        });
        txtBarrio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarrioKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreSucursal)
                    .addComponent(txtDireccion)
                    .addComponent(cbxDepartamentos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtContacto)
                    .addComponent(txtEmail)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNit, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigoInternoSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxCiudades, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxAgencias, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLatitud, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtBarrio))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCodigoInternoSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxDepartamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxCiudades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxAgencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLatitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkActivo)
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(btnGrabar, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblCuentasSecundarias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Nombre Cuenta", "Nombre de la SubCuenta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblCuentasSecundarias);
        if (tblCuentasSecundarias.getColumnModel().getColumnCount() > 0) {
            tblCuentasSecundarias.getColumnModel().getColumn(0).setMinWidth(50);
            tblCuentasSecundarias.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblCuentasSecundarias.getColumnModel().getColumn(0).setMaxWidth(50);
            tblCuentasSecundarias.getColumnModel().getColumn(1).setMinWidth(150);
            tblCuentasSecundarias.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblCuentasSecundarias.getColumnModel().getColumn(1).setMaxWidth(150);
            tblCuentasSecundarias.getColumnModel().getColumn(2).setMinWidth(280);
            tblCuentasSecundarias.getColumnModel().getColumn(2).setPreferredWidth(280);
            tblCuentasSecundarias.getColumnModel().getColumn(2).setMaxWidth(280);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
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
        jBtnGrabar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnGrabar.setFocusable(false);
        jBtnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGrabarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGrabar);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
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
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void nuevo() {
        if (actualizar) {
            btnNuevo.setEnabled(false);
            habilitar(true);
            txtNombreSucursal.setEditable(false);
            txtNombreSucursal.setEnabled(false);

        } else {
            limpiar();
            llenarJTable();
            txtNit.setEnabled(true);
            txtNit.setEditable(true);
            txtNit.requestFocus();

        }

    }

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        if (validar()) {
            grabar();
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnGrabarActionPerformed

    private void grabar() throws HeadlessException {

        int x = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", JOptionPane.OK_CANCEL_OPTION);
        if (x == JOptionPane.OK_OPTION) {

            new Thread(new HiloIngresarSucursalProveedores(ini, this, "guardarSucursal")).start();

            actualizar = false;

        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cancelar() {
        limpiar();
        //habilitar(false);
        btnNuevo.setEnabled(true);
    }

    private void txtTelefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCelular.requestFocus();
        }
    }//GEN-LAST:event_txtTelefonoKeyPressed

    private void txtTelefonoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoFocusGained
        txtTelefono.setSelectionStart(0);
        txtTelefono.setSelectionEnd(txtTelefono.getText().length());
    }//GEN-LAST:event_txtTelefonoFocusGained

    private void cbxCiudadesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxCiudadesKeyPressed

    }//GEN-LAST:event_cbxCiudadesKeyPressed

    private void cbxCiudadesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxCiudadesFocusGained

    }//GEN-LAST:event_cbxCiudadesFocusGained

    private void cbxCiudadesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCiudadesItemStateChanged
        //txtTelefono.requestFocus();       // TODO add your handling code here:
    }//GEN-LAST:event_cbxCiudadesItemStateChanged

    private void cbxDepartamentosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxDepartamentosKeyPressed

    }//GEN-LAST:event_cbxDepartamentosKeyPressed

    private void cbxDepartamentosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxDepartamentosFocusGained

    }//GEN-LAST:event_cbxDepartamentosFocusGained

    private void cbxDepartamentosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxDepartamentosItemStateChanged
        if (cargado) {
            for (CDepartamentos dpto : ini.getListaDeDepartamentos()) {
                if (cbxDepartamentos.getSelectedItem().toString().equals(dpto.getNombreDepartamento())) {
                    departamento = dpto;

                    departamento.setListaDeCiudades();

                    cbxCiudades.removeAllItems();

                    for (CCiudades ciudad : departamento.getListaDeCiudades()) {
                        if (ciudad.getIdDepartamento() == departamento.getIdDepartamento()) {
                            cbxCiudades.addItem(ciudad.getNombreCiudad());
                        }
                    }
                    break;
                }

            }
        }

    }//GEN-LAST:event_cbxDepartamentosItemStateChanged

    private void txtContactoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtEmail.requestFocus();
        }
    }//GEN-LAST:event_txtContactoKeyPressed

    private void txtContactoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContactoFocusGained
        txtContacto.setSelectionStart(0);
        txtContacto.setSelectionEnd(txtContacto.getText().length());
    }//GEN-LAST:event_txtContactoFocusGained

    private void txtCelularKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCelularKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtContacto.requestFocus();
        }
    }//GEN-LAST:event_txtCelularKeyPressed

    private void txtCelularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCelularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCelularActionPerformed

    private void txtCelularFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCelularFocusGained
        txtCelular.setSelectionStart(0);
        txtCelular.setSelectionEnd(txtCelular.getText().length());
    }//GEN-LAST:event_txtCelularFocusGained

    private void txtDireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            /* if(!cargado){
            cbxDepartamentos.removeAllItems();
            cbxAgencias.removeAllItems();
            for (CDepartamentos departamento : ini.getListaDeDepartamentos()) {
                cbxDepartamentos.addItem(departamento.getNombreDepartamento());
            }s
            for (CAgencias agencia : ini.getListaDeAgencias()) {
                cbxAgencias.addItem(agencia.getNombreAgencia());
            }
            cargado = true;
            }*/
        }
    }//GEN-LAST:event_txtDireccionKeyPressed

    private void txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionActionPerformed

    private void txtDireccionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionFocusGained
        txtDireccion.setSelectionStart(0);
        txtDireccion.setSelectionEnd(txtDireccion.getText().length());
    }//GEN-LAST:event_txtDireccionFocusGained

    private void txtNitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNitKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            for (Cproveedores proveedor : ini.getListaDeProveedores()) {
                if (proveedor.getCedula().equals(txtNit.getText().trim())) {
                    this.proveedor = proveedor;
                    this.proveedor.setListaDeSucursales();
                    txtNit.setEnabled(false);
                    txtNombreProveedor.setText(proveedor.getNombres() + " " + proveedor.getApellidos());
                    txtNombreSucursal.setEditable(true);
                    txtNombreSucursal.setEnabled(true);
                    txtNombreSucursal.requestFocus();
                    break;
                }
            }

        }


    }//GEN-LAST:event_txtNitKeyPressed

    private void txtNitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNitActionPerformed

    private void txtNitFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNitFocusGained
//        txtNit.setSelectionStart(0);
//       txtNit.setSelectionEnd(txtNit.getText().length());
    }//GEN-LAST:event_txtNitFocusGained

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing

        if (!estaOcupadoGrabando) {

        } else {
            JOptionPane.showInternalMessageDialog(this, "El sistema está ocupado grabado los datos,\n no se puede cerrar el formulario", "Error al cerrar", JOptionPane.ERROR_MESSAGE);

        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void salir() {

        this.setVisible(false);
    }

    private void txtEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailFocusGained

    private void txtEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailKeyPressed

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

    private void chkActivoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkActivoStateChanged
        if (chkActivo.isSelected()) {
            chkActivo.setText("Empleado Activo");
        } else {
            chkActivo.setText("Empleado No Activo");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_chkActivoStateChanged

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
        grabar();
    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        cancelar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        salir();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void chkActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkActivoActionPerformed

    private void txtNombreProveedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreProveedorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreProveedorFocusGained

    private void txtNombreProveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreProveedorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreProveedorKeyPressed

    private void txtCodigoInternoSucursalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoInternoSucursalFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoInternoSucursalFocusGained

    private void txtCodigoInternoSucursalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoInternoSucursalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {

            FBuscarSucursales formulario = new FBuscarSucursales(ini, this);
            this.getParent().add(formulario);
            formulario.toFront();
            formulario.setClosable(true);
            formulario.setVisible(true);
            formulario.setTitle("Formulario para consultar sucursales");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            //form.setSize(PrincipalLogistica.escritorio.getSize());
            formulario.setLocation((screenSize.width - formulario.getSize().width) / 2, (screenSize.height - formulario.getSize().height) / 2);
            formulario.show();

        }
    }//GEN-LAST:event_txtCodigoInternoSucursalKeyPressed

    private void txtNombreSucursalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreSucursalKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            search.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            search.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = search.getSelectedText();
            if (sucursalElegida) {
                new Thread(new HiloIngresarSucursalProveedores(ini, this, "consultarSucursalProveedor")).start();

            } else {

                //txtNombreSucursal.setText(text);
                int valor = proveedor.getListaDeSucursales().size();
                valor++;

                txtCodigoInternoSucursal.setText("" + valor);
                habilitar(true);
                menu.setVisible(false);

                txtDireccion.setEnabled(true);
                txtDireccion.setEditable(true);
                txtDireccion.requestFocus();

            }

        }
    }//GEN-LAST:event_txtNombreSucursalKeyPressed

    private void txtNombreSucursalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreSucursalFocusGained
        txtNombreSucursal.setSelectionStart(0);
        txtNombreSucursal.setSelectionEnd(txtNombreSucursal.getText().length());
    }//GEN-LAST:event_txtNombreSucursalFocusGained

    private void txtNombreSucursalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreSucursalKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtNombreSucursal.getText().trim().toLowerCase();
            search.setData(search(text));
            if (search.getItemSize() > 0) {
                //  * 2 top and bot border
                menu.show(txtNombreSucursal, 0, txtNombreSucursal.getHeight());
                // menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                menu.setPopupSize(txtNombreSucursal.getWidth(), (search.getItemSize() * 35) + 2);

            } else {
                menu.setVisible(false);
            }
        }
    }//GEN-LAST:event_txtNombreSucursalKeyReleased

    private void txtLatitudFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLatitudFocusGained
        txtLatitud.setSelectionStart(0);
        txtLatitud.setSelectionEnd(txtLatitud.getText().length());       // TODO add your handling code here:
    }//GEN-LAST:event_txtLatitudFocusGained

    private void txtLatitudKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLatitudKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLatitudKeyPressed

    private void txtLongitudFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLongitudFocusGained
        txtLongitud.setSelectionStart(0);
        txtLongitud.setSelectionEnd(txtLongitud.getText().length());        // TODO add your handling code here:
    }//GEN-LAST:event_txtLongitudFocusGained

    private void txtLongitudKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLongitudKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLongitudKeyPressed

    private void txtNombreSucursalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreSucursalFocusLost
        new Thread(new HiloIngresarSucursalProveedores(ini, this, "consultarSucursalProveedor")).start();
    }//GEN-LAST:event_txtNombreSucursalFocusLost

    private void txtNombreSucursalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreSucursalMouseClicked
        if (search.getItemSize() > 0) {
            menu.show(txtNombreSucursal, 0, txtNombreSucursal.getHeight());
            search.clearSelected();
        }
    }//GEN-LAST:event_txtNombreSucursalMouseClicked

    private void txtBarrioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarrioFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioFocusGained

    private void txtBarrioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarrioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioActionPerformed

    private void txtBarrioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    public javax.swing.JComboBox cbxAgencias;
    public javax.swing.JComboBox cbxCiudades;
    public javax.swing.JComboBox cbxDepartamentos;
    public javax.swing.JCheckBox chkActivo;
    public javax.swing.JButton jBtnGrabar;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JTable tblCuentasSecundarias;
    public javax.swing.JTextField txtBarrio;
    public javax.swing.JTextField txtCelular;
    public javax.swing.JTextField txtCodigoInternoSucursal;
    public javax.swing.JTextField txtContacto;
    public javax.swing.JTextField txtDireccion;
    public javax.swing.JTextField txtEmail;
    public javax.swing.JTextField txtLatitud;
    public javax.swing.JTextField txtLongitud;
    public javax.swing.JTextField txtNit;
    public javax.swing.JTextField txtNombreProveedor;
    public javax.swing.JTextField txtNombreSucursal;
    public javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables

    public void llenarCamposDeTexto() {
        try {

            sucursal.setListaDeCuentasSecundarias();
            txtNombreSucursal.setText(sucursal.getNombreSucursal());
//            for (int i = 0; i < cbxTipoEmpresa.getItemCount(); i++) {
//                String xxx = cbxTipoEmpresa.getItemAt(i).toString();
//                if (sucursal.getApellidos().equals(xxx)) {
//                    cbxTipoEmpresa.setSelectedIndex(i);
//                    break;
//                }
//            }

            txtDireccion.setText(sucursal.getDireccionSucursal());
            //txtBarrio.setText(sucursal.getBarrio());

            for (CCiudades obj : ini.getListaDeCiudades()) {
                if (obj.getIdCiudad() == sucursal.getCiudadSucursal()) {

                    cbxDepartamentos.setSelectedItem(obj.getNombreDepartamento());
                    cbxCiudades.setSelectedItem(obj.getNombreCiudad());
                }
            }

            txtTelefono.setText(sucursal.getTelefonoSucursal());
            txtCelular.setText(sucursal.getCelularSucursal());
            txtContacto.setText(sucursal.getCelularCorporativo());
            txtEmail.setText(sucursal.getEmailSucursal());

            //dateFechaIngreso.setDate(sucursal.getFechaDeIngreso());
            int i = 0;
            boolean encontrado = false;
            for (CAgencias obj : ini.getListaDeAgencias()) {
                if (sucursal.getAgencia() == obj.getIdAgencia()) {
                    cbxAgencias.setSelectedIndex(i);
                    encontrado = true;
                }
                if (encontrado) {
                    break;
                }
                i++;
            }

            if (sucursal.getActivo() == 1) {
                chkActivo.setSelected(true);
                chkActivo.setText("Proveedor Activo");
            } else {
                chkActivo.setSelected(false);
                chkActivo.setText("Proveedor No Activo");
            }

            for (i = 0; i < tblCuentasSecundarias.getRowCount(); i++) {
                for (CCuentaSecundariaLogistica cuenta : sucursal.getListaDeCuentasSecundarias()) {
                    if (cuenta.getNombreCuentaSecundaria().equals(tblCuentasSecundarias.getValueAt(i, 2).toString())) {
                        tblCuentasSecundarias.setValueAt(true, i, 0);
                    }
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(IngresarSucursalDeProveedor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al actualizar Campos de Formulario Ingresar empleados" + ex);
            //JOptionPane.showInternalMessageDialog(this, mensaje, "Error al actualizar Campos de Formulario Ingresar empleados \n" + ex, 0);

        }

    }

    public void habilitar(boolean valor) {

        try {

            txtNombreSucursal.setEnabled(valor);
            //cbxTipoEmpresa.setEnabled(valor);
            txtDireccion.setEnabled(valor);
            // txtBarrio.setEnabled(valor);
            txtTelefono.setEnabled(valor);
            txtCelular.setEnabled(valor);
            txtContacto.setEnabled(valor);

            txtEmail.setEnabled(valor);

            txtNombreSucursal.setEditable(valor);
            // cbxTipoEmpresa.setEditable(valor);
            txtDireccion.setEditable(valor);
            //txtBarrio.setEditable(valor);
            txtTelefono.setEditable(valor);
            txtCelular.setEditable(valor);
            txtContacto.setEditable(valor);

            txtEmail.setEditable(valor);

            cbxDepartamentos.setEnabled(valor);
            cbxCiudades.setEnabled(valor);
            cbxAgencias.setEnabled(valor);
            tblCuentasSecundarias.setEnabled(valor);
            txtLatitud.setEnabled(valor);
            txtLongitud.setEnabled(valor);
            txtLatitud.setEditable(valor);
            txtLongitud.setEditable(valor);
            txtBarrio.setEditable(valor);
            txtBarrio.setEnabled(valor);

            // dateFechaIngreso.setEnabled(valor);
            System.out.println("Objetos del  formulario actualizados ");
            btnGrabar.setEnabled(true);

        } catch (Exception ex) {
            System.out.println("Error al actualizar los coponentes del formulario.. " + ex.getMessage());
        }

    }

    public boolean validar() {
        boolean verificado = true;
        mensaje = "";

        if (txtNit.getText().isEmpty()) {
            mensaje += "Debe digitar el número de NIT para crear un Proveedor ";
            verificado = false;
        }

        if (txtNombreSucursal.getText().isEmpty()) {
            mensaje += "No ha colocado el nombre del sucursal" + "  \n";
            verificado = false;
        }
//            if (cbxTipoEmpresa.getSelectedIndex() == 0) {
//                mensaje += "No ha colocado el tipo de empresa" + "  \n";
//                verificado = false;
//            }
        if (txtDireccion.getText().isEmpty()) {
            mensaje += "No ha colocado la dirección del sucursal" + "  \n";
            verificado = false;
        }
//            if (txtBarrio.getText().isEmpty()) {
//                mensaje += "No ha colocado el barrio del sucursal" + "  \n";
//                verificado = false;
//            }

        if (cbxDepartamentos.getSelectedIndex() == -1) {
            mensaje += "No ha selecccionado el departamento " + "  \n";
            verificado = false;
        }

        if (cbxCiudades.getSelectedIndex() == -1) {
            mensaje += "No ha seleccionado la ciudad " + "  \n";
            verificado = false;
        }
        if (txtTelefono.getText().isEmpty()) {
            mensaje += "No ha ingresado el telefono " + "  \n";
            verificado = false;
        }
        if (txtCelular.getText().isEmpty()) {
            mensaje += "No ha ingresado el celular " + "  \n";
            verificado = false;
        }
        if (txtContacto.getText().isEmpty()) {
            mensaje += "No ha ingresado el celular corporativo " + "  \n";
            verificado = false;
        }

        if (txtEmail.getText().isEmpty()) {
            mensaje += "No ha ingresado el Email " + "  \n";
            verificado = false;
        }
        if (!Inicio.validateEmail(txtEmail.getText())) {
            mensaje += "El formato del email no es válido" + "  \n";
            verificado = false;

        }
        boolean encontrado = false;
        for (int i = 0; i < tblCuentasSecundarias.getRowCount(); i++) {
            boolean valor = (boolean) tblCuentasSecundarias.getValueAt(i, 0);

            for (CCuentaSecundariaLogistica cuenta : ini.getListadeCuentasSecundarias()) {
                if (cuenta.getNombreCuentaSecundaria().equals(tblCuentasSecundarias.getValueAt(i, 2).toString())) {
                    if (valor) {
                        encontrado = true;
                        break;
                    }

                }
            }

        }

        if (!encontrado) {
            mensaje += "No se han asignado cuentas a la sucursal" + "  \n";
            verificado = false;
        }

        System.out.println("Se han validado los datos del sucursal... ");

        return verificado;
    }

    public void limpiar() {

        habilitar(false);

        txtNit.setText("");
        txtNombreProveedor.setText("");
        txtCodigoInternoSucursal.setText("");
        txtNombreSucursal.setText("");
        cbxDepartamentos.setSelectedIndex(0);
//        cbxCiudades.setSelectedIndex(0);
        txtDireccion.setText("");
        txtBarrio.setText("");
        txtTelefono.setText("");
        txtCelular.setText("");
        txtContacto.setText("");

        txtEmail.setText("");
        btnNuevo.setText("Nuevo");
        btnGrabar.setEnabled(false);
        btnNuevo.setEnabled(true);

        actualizar = false;
        sucursalElegida = false;
        validado = false;

        txtLatitud.setText("");
        txtLongitud.setText("");

        this.proveedor = null;
        this.sucursal = null;
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png")));

        llenarJTable();

    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     */
    public void CargarVista() {
        try {

            //cbxDepartamentos.removeAllItems();
            // cbxCiudades.removeAllItems();
            listaDeCuentasSecundarias = new ArrayList<>();
            new Thread(new HiloListadoDeCuentasSecundarias(ini, listaDeCuentasSecundarias, null)).start();

            for (CDepartamentos obj : ini.getListaDeDepartamentos()) {
                cbxDepartamentos.addItem(obj.getNombreDepartamento());
            }

            for (CAgencias obj : ini.getListaDeAgencias()) {
                cbxAgencias.addItem(obj.getNombreAgencia());
            }

            try {

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
                Logger.getLogger(IngresarSucursalDeProveedor.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(IngresarSucursalDeProveedor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void llenarJTable() {
        limpiarTabla();
        DefaultTableModel modelo1 = (DefaultTableModel) tblCuentasSecundarias.getModel();

        for (CCuentaSecundariaLogistica obj : ini.getListadeCuentasSecundarias()) {

            int fila = tblCuentasSecundarias.getRowCount();

            modelo1.addRow(new Object[7]);

            //  modelo1.addRow(new Object[jTableMovimientos.getRowCount()]);
            tblCuentasSecundarias.setValueAt(false, fila, 0);  // numero de factura
            tblCuentasSecundarias.setValueAt(obj.getNombreCuentaPrincipal(), fila, 1);  // numero de factura
            tblCuentasSecundarias.setValueAt(obj.getNombreCuentaSecundaria(), fila, 2);  // numero de factura

        }

    }

    private void limpiarTabla() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tblCuentasSecundarias.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

        } catch (Exception ex) {
        }

    }

    private void addStory(String text) {
        try {
            boolean add = true;
            PreparedStatement p = conexion.prepareStatement(
                    "select idciudadesstory "
                    + "from ciudadesstory "
                    + "where storyName=? limit 1");
            p.setString(1, text);
            ResultSet r = p.executeQuery();
            if (r.first()) {
                add = false;
            }
            r.close();
            p.close();
            if (add) {
                p = conexion.prepareStatement("insert into ciudadesstory (storyName) values (?)");
                p.setString(1, text);
                p.execute();
                p.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeHistory(String text) {
        try {
            PreparedStatement p = conexion.prepareStatement("delete from ciudadesstory where storyName=? limit 1");
            p.setString(1, text);
            p.execute();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<DataSearch> search(String search) {
        List<DataSearch> list = new ArrayList<>();
        try {
            String sql = "select DISTINCT nombreSucursal, "
                    + "coalesce((select idSucursalstory from sucursalesstory where storyName=nombreSucursal limit 1),'') as Story "
                    + "from proveedoressucursales "
                    + "where "
                    + "cedula ='" + proveedor.getCedula() + "' AND "
                    + "activo = 1 AND "
                    + "nombreSucursal like ? order by Story DESC, nombreSucursal limit 7";
            // PreparedStatement p = con.prepareStatement("select DISTINCT ProductName, coalesce((select StoryID from story where ProductName=StoryName limit 1),'') as Story from product where ProductName like ? order by Story DESC, ProductName limit 7");
            PreparedStatement p = conexion.prepareStatement(sql);
//                    "select DISTINCT nombreCiudad, "
//                  + "coalesce((select idciudadesstory from ciudadesstory where storyName=nombreCiudad limit 1),'') as Story "
//                  + "from ciudades "
//                  + "where "
//                  + "idDepartamento ='" + departamento + "' AND "
//                  + "activo = 1 AND "
//                  + "nombreCiudad like ? order by Story DESC, nombreCiudad limit 7");

            p.setString(1, "%" + search + "%");
            ResultSet r = p.executeQuery();
            while (r.next()) {
                String text = r.getString(1);
                boolean story = !r.getString(2).equals("");
                list.add(new DataSearch(text, story));
            }
            r.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
