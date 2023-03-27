/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.proveedores;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import aplicacionlogistica.configuracion.organizacion.CRegionales;
import aplicacionlogistica.configuracion.organizacion.CZonas;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Dimension;
import mtto.ingresoDeRegistros.objetos.CCuentaSecundariaLogistica;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import mtto.Administracion.FBuscarProveedores;

/**
 *
 * @author VLI_488
 */
public class IngresarProveedores extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public boolean actualizar;
    public boolean actualizarFoto = false;
    public boolean estaOcupadoGrabando = false;
    public boolean cargado = false;

    String mensaje = null;

    public Inicio ini;
    String suario;
    CUsuarios user;
    public Cproveedores proveedor;
    CDepartamentos departamento;
    CCiudades ciudad;
    CZonas zona;
    CRegionales regional;
    CAgencias agencia;
    List<CCuentaSecundariaLogistica> listaDeServiciosSucursal = null;
    final DefaultListModel model = new DefaultListModel();

    String ruta;
    //home/lelopez/Documentos/proyectos/aplicacionLogistica_v2/src/aplicacionlogistica/mantenimientos/tiposDeEmpresa.txt
    // private File archivo = null;
    private FileWriter fichero = null;
    private FileReader fr = null;
    private BufferedReader br = null;
    private PrintWriter escrive = null;
    private List<String> lineas;
    IngresarSucursalDeProveedor formCrearSucursales;
    public TextAutoCompleter autoTxtNitProveedores;

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     * @param formCrearSucursales
     */
    public IngresarProveedores(Inicio ini, IngresarSucursalDeProveedor formCrearSucursales) {
        initComponents();
        // archivo = new File(ini.getRutaDeApp() + "mantenimientos/tiposDeEmpresa.txt");
        this.ini = ini;
        this.formCrearSucursales = formCrearSucursales;
        user = ini.getUser();

    }

    public IngresarProveedores(Inicio ini) {
        initComponents();
        // archivo = new File(ini.getRutaDeApp() + "mantenimientos/tiposDeEmpresa.txt");
        this.ini = ini;
        user = ini.getUser();
        cbxAgencias.removeAllItems();
        cbxCiudades.removeAllItems();
        cbxDepartamentos.removeAllItems();
        cbxTipoEmpresa.removeAllItems();
        lblCirculoDeProgreso.setVisible(false);

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

        // cargarLaVista();
        new Thread(new HiloIngresarProveedores(ini, this, "cargarLaVista")).start();

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
        txtNitProveedor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtBarrio = new javax.swing.JTextField();
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
        jLabel29 = new javax.swing.JLabel();
        dateFechaIngreso = new com.toedter.calendar.JDateChooser();
        cbxAgencias = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        cbxTipoEmpresa = new javax.swing.JComboBox();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        txtLatitud = new javax.swing.JTextField();
        txtLongitud = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        chkActivo = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCuentasSecundarias = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jBtnRefrescar = new javax.swing.JToggleButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario para el ingreso de Proveedores");
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

        txtNitProveedor.setEditable(false);
        txtNitProveedor.setToolTipText("Ingresar documento de identificación");
        txtNitProveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNitProveedorFocusGained(evt);
            }
        });
        txtNitProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNitProveedorActionPerformed(evt);
            }
        });
        txtNitProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNitProveedorKeyPressed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Razon Social");

        txtNombres.setEditable(false);
        txtNombres.setToolTipText("Ingresar Nombres completos");
        txtNombres.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombresFocusGained(evt);
            }
        });
        txtNombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombresKeyPressed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Tipo Empresa");

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

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Barrio");

        txtBarrio.setEditable(false);
        txtBarrio.setToolTipText("Ingresar el barrio");
        txtBarrio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarrioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBarrioFocusLost(evt);
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

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Ingreso");

        dateFechaIngreso.setToolTipText("Seleccionar la fecha de nacimiento");
        dateFechaIngreso.setDateFormatString("yyyy/MM/dd");
        dateFechaIngreso.setEnabled(false);

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

        cbxTipoEmpresa.setToolTipText("Seleccionar la ciudad");
        cbxTipoEmpresa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoEmpresaItemStateChanged(evt);
            }
        });
        cbxTipoEmpresa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxTipoEmpresaFocusGained(evt);
            }
        });
        cbxTipoEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxTipoEmpresaKeyPressed(evt);
            }
        });

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        lblCirculoDeProgreso.setEnabled(false);

        txtLatitud.setText("0");
        txtLatitud.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLatitudFocusGained(evt);
            }
        });
        txtLatitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLatitudActionPerformed(evt);
            }
        });
        txtLatitud.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLatitudKeyPressed(evt);
            }
        });

        txtLongitud.setText("0");
        txtLongitud.setEnabled(false);
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

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Latitud");

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("Longitud");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombres, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                            .addComponent(txtDireccion)
                            .addComponent(txtBarrio)
                            .addComponent(cbxDepartamentos, 0, 354, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(txtNitProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)
                                        .addComponent(lblCirculoDeProgreso))
                                    .addComponent(cbxCiudades, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dateFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtContacto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(12, 12, 12))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxTipoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxAgencias, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtLongitud, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtLatitud, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNitProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblCirculoDeProgreso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxTipoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(2, 2, 2)
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtContacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxAgencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLatitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)))
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
                .addGap(18, 18, 18)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        chkActivo.setSelected(true);
        chkActivo.setText("Proveedor Activo");
        chkActivo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkActivoStateChanged(evt);
            }
        });

        tblCuentasSecundarias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Nombre Cuenta PPal.", "Nombre de la SubCuenta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCuentasSecundarias.getTableHeader().setReorderingAllowed(false);
        tblCuentasSecundarias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCuentasSecundariasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblCuentasSecundarias);
        if (tblCuentasSecundarias.getColumnModel().getColumnCount() > 0) {
            tblCuentasSecundarias.getColumnModel().getColumn(0).setResizable(false);
            tblCuentasSecundarias.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblCuentasSecundarias.getColumnModel().getColumn(1).setResizable(false);
            tblCuentasSecundarias.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblCuentasSecundarias.getColumnModel().getColumn(2).setResizable(false);
            tblCuentasSecundarias.getColumnModel().getColumn(2).setPreferredWidth(280);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .addComponent(chkActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkActivo)
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

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/insertar-fila.png"))); // NOI18N
        jButton5.setToolTipText("Agregar Sucursal");
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.setEnabled(false);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(24, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void nuevo() {
        if (actualizar) {
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            habilitar(true);
            txtNitProveedor.setEditable(false);

        } else {
            limpiar();
            llenarJTable();
            actualizarFoto = true;
            txtNitProveedor.setEnabled(true);
            txtNitProveedor.setEditable(true);

        }
        jBtnRefrescar.setEnabled(false);
        txtNitProveedor.requestFocus();
    }

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed

        grabar();

    }//GEN-LAST:event_btnGrabarActionPerformed

    private void grabar() throws HeadlessException {
        int x = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", JOptionPane.OK_CANCEL_OPTION);
        if (x == JOptionPane.OK_OPTION) {
            if (validar()) {
                int i = 0;

                new Thread(new HiloIngresarProveedores(ini, this, "guardar")).start();

            } else {
                JOptionPane.showInternalMessageDialog(this, mensaje, "Error al guardar", 0);
            }
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cancelar() {
        limpiar();
        habilitar(false);
        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);
         jBtnRefrescar.setEnabled(true);
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

    }//GEN-LAST:event_txtCelularActionPerformed

    private void txtCelularFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCelularFocusGained
        txtCelular.setSelectionStart(0);
        txtCelular.setSelectionEnd(txtCelular.getText().length());
    }//GEN-LAST:event_txtCelularFocusGained

    private void txtBarrioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxDepartamentos.requestFocus();

        }
    }//GEN-LAST:event_txtBarrioKeyPressed

    private void txtBarrioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarrioActionPerformed

    }//GEN-LAST:event_txtBarrioActionPerformed

    private void txtBarrioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarrioFocusLost

    }//GEN-LAST:event_txtBarrioFocusLost

    private void txtBarrioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarrioFocusGained
        txtBarrio.setSelectionStart(0);
        txtBarrio.setSelectionEnd(txtBarrio.getText().length());
    }//GEN-LAST:event_txtBarrioFocusGained

    private void txtDireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtBarrio.requestFocus();
        }
    }//GEN-LAST:event_txtDireccionKeyPressed

    private void txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionActionPerformed

    }//GEN-LAST:event_txtDireccionActionPerformed

    private void txtDireccionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionFocusGained
        txtDireccion.setSelectionStart(0);
        txtDireccion.setSelectionEnd(txtDireccion.getText().length());
    }//GEN-LAST:event_txtDireccionFocusGained

    private void txtNombresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombresKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxTipoEmpresa.requestFocus();

        }
    }//GEN-LAST:event_txtNombresKeyPressed

    private void txtNombresFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombresFocusGained
        txtNombres.setSelectionStart(0);
        txtNombres.setSelectionEnd(txtNombres.getText().length());
    }//GEN-LAST:event_txtNombresFocusGained

    private void txtNitProveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNitProveedorKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            FBuscarProveedores formulario = new FBuscarProveedores(ini, this);
            this.getParent().add(formulario);
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            formulario.toFront();
            formulario.setClosable(true);
            formulario.setVisible(true);
            formulario.setTitle("Formulario para consultar Proveedoress");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            formulario.setLocation((screenSize.width - formulario.getSize().width) / 2, (screenSize.height - formulario.getSize().height) / 2);
            formulario.show();

        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // consultarProveedor();
            new Thread(new HiloIngresarProveedores(ini, this, "consultarProveedor")).start();
        }
    }//GEN-LAST:event_txtNitProveedorKeyPressed


    private void txtNitProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNitProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNitProveedorActionPerformed

    private void txtNitProveedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNitProveedorFocusGained
        txtNitProveedor.setSelectionStart(0);
        txtNitProveedor.setSelectionEnd(txtNitProveedor.getText().length());
    }//GEN-LAST:event_txtNitProveedorFocusGained

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
        this.dispose();
        this.setVisible(false);
    }

    private void txtEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusGained
        txtEmail.setSelectionStart(0);
        txtEmail.setSelectionEnd(txtEmail.getText().length());        // TODO add your handling code here:

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
            chkActivo.setText("Proveedor Activo");
        } else {
            chkActivo.setText("Proveedor No Activo");
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

    private void cbxTipoEmpresaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoEmpresaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoEmpresaItemStateChanged

    private void cbxTipoEmpresaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTipoEmpresaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoEmpresaFocusGained

    private void cbxTipoEmpresaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxTipoEmpresaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoEmpresaKeyPressed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        formCrearSucursales.setVisible(true);
        formCrearSucursales.show();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void tblCuentasSecundariasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCuentasSecundariasMouseClicked

        int filaSelleccionada = tblCuentasSecundarias.getSelectedRow();

        boolean valor = (Boolean) tblCuentasSecundarias.getValueAt(filaSelleccionada, 0);

        if (valor) {
            tblCuentasSecundarias.setValueAt(false, filaSelleccionada, 0);

            for (CCuentaSecundariaLogistica obj : ini.getListadeCuentasSecundarias()) {
                if (obj.getNombreCuentaSecundaria().equals(tblCuentasSecundarias.getValueAt(filaSelleccionada, 2))) {
                    obj.setActivo(0);
                    break;
                }

            }
        } else {
            tblCuentasSecundarias.setValueAt(true, filaSelleccionada, 0);

            for (CCuentaSecundariaLogistica obj : ini.getListadeCuentasSecundarias()) {
                if (obj.getNombreCuentaSecundaria().equals(tblCuentasSecundarias.getValueAt(filaSelleccionada, 2))) {
                    obj.setActivo(1);
                    btnGrabar.setEnabled(true);
                    jBtnGrabar.setEnabled(true);
                    break;
                }
            }
        }

    }//GEN-LAST:event_tblCuentasSecundariasMouseClicked

    private void txtLongitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLongitudActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLongitudActionPerformed

    private void txtLatitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLatitudActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLatitudActionPerformed

    private void txtLatitudKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLatitudKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtLongitud.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtLatitudKeyPressed

    private void txtLatitudFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLatitudFocusGained
        txtLatitud.setSelectionStart(0);
        txtLatitud.setSelectionEnd(txtLatitud.getText().length());
    }//GEN-LAST:event_txtLatitudFocusGained

    private void txtLongitudFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLongitudFocusGained
        txtLongitud.setSelectionStart(0);
        txtLongitud.setSelectionEnd(txtLongitud.getText().length());
    }//GEN-LAST:event_txtLongitudFocusGained

    private void jBtnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRefrescarActionPerformed
        new Thread(new HiloIngresarProveedores(ini, this, "refrescar")).start();       // TODO add your handling code here:
    }//GEN-LAST:event_jBtnRefrescarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    public javax.swing.JComboBox cbxAgencias;
    public javax.swing.JComboBox cbxCiudades;
    public javax.swing.JComboBox cbxDepartamentos;
    public javax.swing.JComboBox cbxTipoEmpresa;
    public javax.swing.JCheckBox chkActivo;
    public com.toedter.calendar.JDateChooser dateFechaIngreso;
    public javax.swing.JButton jBtnGrabar;
    public javax.swing.JButton jBtnNuevo;
    public javax.swing.JToggleButton jBtnRefrescar;
    public javax.swing.JButton jButton3;
    public javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
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
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JTable tblCuentasSecundarias;
    public javax.swing.JTextField txtBarrio;
    public javax.swing.JTextField txtCelular;
    public javax.swing.JTextField txtContacto;
    public javax.swing.JTextField txtDireccion;
    public javax.swing.JTextField txtEmail;
    public javax.swing.JTextField txtLatitud;
    public javax.swing.JTextField txtLongitud;
    public javax.swing.JTextField txtNitProveedor;
    public javax.swing.JTextField txtNombres;
    public javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables

    public void habilitar(boolean valor) {

        try {
            txtNitProveedor.setEnabled(valor);
            txtNombres.setEnabled(valor);
            cbxTipoEmpresa.setEnabled(valor);
            txtDireccion.setEnabled(valor);
            txtBarrio.setEnabled(valor);
            txtTelefono.setEnabled(valor);
            txtCelular.setEnabled(valor);
            txtContacto.setEnabled(valor);
            txtLatitud.setEnabled(valor);
            txtLongitud.setEnabled(valor);

            txtEmail.setEnabled(valor);

            txtNitProveedor.setEditable(valor);
            txtNombres.setEditable(valor);
            //cbxTipoEmpresa.setEditable(valor);
            txtDireccion.setEditable(valor);
            txtBarrio.setEditable(valor);
            txtTelefono.setEditable(valor);
            txtCelular.setEditable(valor);
            txtContacto.setEditable(valor);
            txtLatitud.setEditable(valor);
            txtLongitud.setEditable(valor);

            txtEmail.setEditable(valor);

            cbxDepartamentos.setEnabled(valor);
            cbxCiudades.setEnabled(valor);
            cbxAgencias.setEnabled(valor);
            tblCuentasSecundarias.setEnabled(valor);

            dateFechaIngreso.setEnabled(valor);
            btnGrabar.setEnabled(valor);
            jBtnGrabar.setEnabled(valor);
            jBtnRefrescar.setEnabled(valor);

        } catch (Exception ex) {
            System.out.println("Error al actualizar los coponentes del formulario.. " + ex.getMessage());
        }

    }

    public boolean validar() {
        boolean verificado = true;
        mensaje = "";
        if (proveedor != null) {

            listaDeServiciosSucursal = new ArrayList<>();

            for (int i = 0; i < tblCuentasSecundarias.getRowCount(); i++) {
                boolean valor = (boolean) tblCuentasSecundarias.getValueAt(i, 0);
                if (valor) {
                    for (CCuentaSecundariaLogistica cuenta : ini.getListadeCuentasSecundarias()) {
                        if (cuenta.getNombreCuentaSecundaria().equals(tblCuentasSecundarias.getValueAt(i, 2).toString())) {
                            listaDeServiciosSucursal.add(cuenta);
                        }
                    }

                }
            }

            // proveedor.setListadeCuentasSecundarias(listaDeServiciosSucursal);
            if (txtNombres.getText().isEmpty()) {
                mensaje += "No ha colocado el nombre del proveedor" + "  \n";
                verificado = false;
            }
            if (cbxTipoEmpresa.getSelectedIndex() == 0) {
                mensaje += "No ha colocado el tipo de empresa" + "  \n";
                verificado = false;
            }
            if (txtDireccion.getText().isEmpty()) {
                mensaje += "No ha colocado la dirección del proveedor" + "  \n";
                verificado = false;
            }
            if (txtBarrio.getText().isEmpty()) {
                mensaje += "No ha colocado el barrio del proveedor" + "  \n";
                verificado = false;
            }

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

        } else {
            mensaje += "Debe digitar el número de NIT para crear un Proveedor ";
            verificado = false;
        }

        System.out.println("Se han validado los datos del proveedor... ");

        return verificado;
    }

    public void limpiar() {

        txtNitProveedor.setText("");
        txtNombres.setText("");
        cbxTipoEmpresa.setSelectedIndex(0);
        txtDireccion.setText("");
        txtBarrio.setText("");
        txtTelefono.setText("");
        txtCelular.setText("");
        txtContacto.setText("");
        txtLatitud.setText("0");
        txtLongitud.setText("0");

        txtEmail.setText("");

        dateFechaIngreso.setDate(new Date());
        dateFechaIngreso.setDate(new Date());

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); //
        btnNuevo.setText("Nuevo");
        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);
        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);
        actualizarFoto = false;
        actualizar = false;
        listaDeServiciosSucursal = null;

        limpiarTabla();

    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     */
    public void aCargarVista() {
        try {

            //cbxDepartamentos.removeAllItems();
            // cbxCiudades.removeAllItems();
            //listaDeCuentasSecundarias = new ArrayList<>();
            //new Thread(new HiloListadoDeCuentasSecundarias(ini, listaDeCuentasSecundarias, null)).start();
            ini.setListadeCuentasSecundarias();

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
                Logger.getLogger(IngresarProveedores.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(IngresarProveedores.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void llenarJTable() {
        limpiarTabla();
        DefaultTableModel modelo1 = (DefaultTableModel) tblCuentasSecundarias.getModel();

        for (CCuentaSecundariaLogistica obj : ini.getListadeCuentasSecundarias()) {

            int fila = tblCuentasSecundarias.getRowCount();

            obj.setActivo(0);
            modelo1.addRow(new Object[7]);

            //  modelo1.addRow(new Object[jTableMovimientos.getRowCount()]);
            tblCuentasSecundarias.setValueAt(false, fila, 0);  // numero de factura
            tblCuentasSecundarias.setValueAt(obj.getNombreCuentaPrincipal(), fila, 1);  // numero de factura
            tblCuentasSecundarias.setValueAt(obj.getNombreCuentaSecundaria(), fila, 2);  // numero de factura

        }

    }

    private void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tblCuentasSecundarias.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
    }

}
