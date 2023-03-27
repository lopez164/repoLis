/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.administracion;

import aplicacionlogistica.distribucion.consultas.FBuscarEmpleados;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import aplicacionlogistica.configuracion.organizacion.CRegionales;
import aplicacionlogistica.configuracion.organizacion.CZonas;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.Color;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import ui.swing.searchText.DataSearch;
import ui.swing.searchText.EventClick;
import ui.swing.searchText.PanelSearch;

/**
 *
 * @author VLI_488
 */
public class IngresarEmpleados extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public boolean actualizar;
    public boolean actualizarFoto = false;
    public boolean estaOcupadoGrabando = false;

    String mensaje = null;
    File foto = null;
    public Inicio ini;
    String suario;
    CUsuarios user;
    //public CEmpleados datosEmpleado;
    public CEmpleados empleado = null;
    CDepartamentos departamento;
    CCiudades ciudad;
    CZonas zona;
    CRegionales regional;
    CAgencias agencia;
    public ImageIcon filfoto;
    
    /* Campo de texto con busqueda*/
    private JPopupMenu menu;
    private PanelSearch search;
    private Connection conexion;
    boolean ciudadElegida= false;

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public IngresarEmpleados(Inicio ini) {

        initComponents();
        lblCirculoDeProgreso.setVisible(false);
        this.ini = ini;
        user = this.ini.getUser();
       cbxCiudades.setVisible(false);
       
      // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "IngresarEmpleados");

       conexion = ini.getConnRemota();
        
        menu = new JPopupMenu();
       
        search = new PanelSearch();
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menu.setVisible(false);
                txtCiudad.setText(data.getText());
                addStory(data.getText());
                System.out.println("Click Item : " + data.getText());
                txtTelefono.requestFocus();
                ciudadElegida= true;
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
        panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg")));
        colocarImagen("/aplicacionlogistica/configuracion/imagenes/perfil.jpg");

        habilitar(false);
        //cargarVista();
        new Thread(new HiloIngresarEmpleados(ini, this, "cargarLaVista")).start();

    }

    public void colocarImagen(String pathFoto) {

        panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource(pathFoto)));
        panelFotografia.updateUI();
        javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(panelFotografia);
        panelFotografia.setLayout(panelImage1Layout);
        panelImage1Layout.setHorizontalGroup(
                panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 160, Short.MAX_VALUE));
        panelImage1Layout.setVerticalGroup(
                panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 171, Short.MAX_VALUE));
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
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnSalir = new javax.swing.JToggleButton();
        jBtnRefrescar = new javax.swing.JToggleButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtBarrio = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCelular = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCelularCorporativo = new javax.swing.JTextField();
        cbxCiudades = new javax.swing.JComboBox<>();
        panelFotografia = new org.edisoncor.gui.panel.PanelImage();
        cbxDepartamentos = new javax.swing.JComboBox<>();
        txtCiudad = new ui.swing.searchText.MyTextField();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtEscolaridad = new javax.swing.JTextField();
        cbxGenero = new javax.swing.JComboBox();
        dateCumpleanios = new com.toedter.calendar.JDateChooser();
        txtLugarDeNacimiento = new javax.swing.JTextField();
        cbxEstadoCivil = new javax.swing.JComboBox();
        txtEmail = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        cbxTipoDeSangre = new javax.swing.JComboBox();
        cbxCargos = new javax.swing.JComboBox();
        dateFechaIngreso = new com.toedter.calendar.JDateChooser();
        cbxZonas = new javax.swing.JComboBox();
        cbxAgencias = new javax.swing.JComboBox();
        cbxCentrosDeCosto = new javax.swing.JComboBox();
        cbxTiposDeContrato = new javax.swing.JComboBox();
        cbxEntidadesBancarias = new javax.swing.JComboBox();
        txtNumeroDeCuenta = new javax.swing.JTextField();
        chkActivo = new javax.swing.JCheckBox();
        cbxRegionales = new javax.swing.JComboBox<>();

        agregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Display 16x16.png"))); // NOI18N
        agregarImagen.setText("Agregar Imagen Empleado");
        agregarImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarImagenMouseClicked(evt);
            }
        });
        pumImagen.add(agregarImagen);

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario para el ingreso de Empleados");
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

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Cédula");

        txtCedula.setEditable(false);
        txtCedula.setToolTipText("Ingresar documento de identificación");
        txtCedula.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCedulaFocusGained(evt);
            }
        });
        txtCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaActionPerformed(evt);
            }
        });
        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCedulaKeyPressed(evt);
            }
        });

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Nombres");

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
        jLabel3.setText("Apellidos");

        txtApellidos.setEditable(false);
        txtApellidos.setToolTipText("Ingresar apellidos completos");
        txtApellidos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtApellidosFocusGained(evt);
            }
        });
        txtApellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtApellidosKeyPressed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Barrio");

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

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Dirección");

        txtDireccion.setEditable(false);
        txtDireccion.setToolTipText("Ingresar el barrio");
        txtDireccion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDireccionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDireccionFocusLost(evt);
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

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Ciudad");

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
        jLabel10.setText("Cel. Corporativo");

        txtCelularCorporativo.setEditable(false);
        txtCelularCorporativo.setToolTipText("Ingreasar # teléfono celular corporativo");
        txtCelularCorporativo.setName("numerico"); // NOI18N
        txtCelularCorporativo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCelularCorporativoFocusGained(evt);
            }
        });
        txtCelularCorporativo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCelularCorporativoKeyPressed(evt);
            }
        });

        cbxCiudades.setEditable(true);
        cbxCiudades.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxCiudades.setEnabled(false);
        cbxCiudades.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxCiudadesKeyPressed(evt);
            }
        });

        panelFotografia.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panelFotografia.setToolTipText("Dar click en el boton ''Nuevo'' para activar y agregar imagen con click derecho...!!! ");
        panelFotografia.setComponentPopupMenu(pumImagen);
        panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg"))); // NOI18N

        javax.swing.GroupLayout panelFotografiaLayout = new javax.swing.GroupLayout(panelFotografia);
        panelFotografia.setLayout(panelFotografiaLayout);
        panelFotografiaLayout.setHorizontalGroup(
            panelFotografiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );
        panelFotografiaLayout.setVerticalGroup(
            panelFotografiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 171, Short.MAX_VALUE)
        );

        cbxDepartamentos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxDepartamentos.setEnabled(false);
        cbxDepartamentos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxDepartamentosItemStateChanged(evt);
            }
        });
        cbxDepartamentos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbxDepartamentosFocusLost(evt);
            }
        });
        cbxDepartamentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxDepartamentosMouseClicked(evt);
            }
        });

        txtCiudad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCiudadMouseClicked(evt);
            }
        });
        txtCiudad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCiudadKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCiudadKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(panelFotografia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(150, 150, 150))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombres, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addComponent(txtApellidos)
                    .addComponent(txtDireccion)
                    .addComponent(cbxDepartamentos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtBarrio, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtCelularCorporativo, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxCiudades, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtCiudad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(panelFotografia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbxDepartamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCelularCorporativo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(cbxCiudades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
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
        btnCancelar.setEnabled(false);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
            .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Escolaridad");

        txtEscolaridad.setEditable(false);
        txtEscolaridad.setToolTipText("Ingresar el grado de escolaridad");
        txtEscolaridad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEscolaridadFocusGained(evt);
            }
        });
        txtEscolaridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEscolaridadActionPerformed(evt);
            }
        });
        txtEscolaridad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEscolaridadKeyPressed(evt);
            }
        });

        cbxGenero.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MASCULINO", "FEMENINO" }));
        cbxGenero.setToolTipText("Seleccionar Género");
        cbxGenero.setEnabled(false);
        cbxGenero.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxGeneroItemStateChanged(evt);
            }
        });
        cbxGenero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxGeneroKeyPressed(evt);
            }
        });

        dateCumpleanios.setToolTipText("Seleccionar la fecha de nacimiento");
        dateCumpleanios.setDateFormatString("yyyy/MM/dd");
        dateCumpleanios.setEnabled(false);

        txtLugarDeNacimiento.setEditable(false);
        txtLugarDeNacimiento.setToolTipText("Ingresar el lugar de nacimiento");
        txtLugarDeNacimiento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLugarDeNacimientoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLugarDeNacimientoFocusLost(evt);
            }
        });
        txtLugarDeNacimiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLugarDeNacimientoKeyPressed(evt);
            }
        });

        cbxEstadoCivil.setToolTipText("Sellecionar el estado civil actual");
        cbxEstadoCivil.setEnabled(false);
        cbxEstadoCivil.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxEstadoCivilItemStateChanged(evt);
            }
        });
        cbxEstadoCivil.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxEstadoCivilFocusGained(evt);
            }
        });
        cbxEstadoCivil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxEstadoCivilKeyPressed(evt);
            }
        });

        txtEmail.setEditable(false);
        txtEmail.setToolTipText("Ingresar el email personal ó corporativo");
        txtEmail.setName("minuscula"); // NOI18N
        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailFocusLost(evt);
            }
        });
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmailKeyPressed(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Genero");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Cumpleaños");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Lugar Nacimiento");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Estado Civil");

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Email");

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Tipo de Sangre");

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Cargo");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("fecha Ingreso");

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Zona");

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Regional");

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Agencia");

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Centro de Costo");

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Tipo Contrato");

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("N° de Cuenta");

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Entidad Bancaria");

        cbxTipoDeSangre.setToolTipText("Seleccionar el Tipo de sangre");
        cbxTipoDeSangre.setEnabled(false);
        cbxTipoDeSangre.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoDeSangreItemStateChanged(evt);
            }
        });
        cbxTipoDeSangre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxTipoDeSangreFocusGained(evt);
            }
        });
        cbxTipoDeSangre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxTipoDeSangreKeyPressed(evt);
            }
        });

        cbxCargos.setToolTipText("Seleccionar el cargo");
        cbxCargos.setEnabled(false);
        cbxCargos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxCargosFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbxCargosFocusLost(evt);
            }
        });
        cbxCargos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxCargosKeyPressed(evt);
            }
        });

        dateFechaIngreso.setToolTipText("Ingresar la fecha de inicio del contrato");
        dateFechaIngreso.setDateFormatString("yyyy/MM/dd");
        dateFechaIngreso.setEnabled(false);

        cbxZonas.setToolTipText("Seleccionar la zona");
        cbxZonas.setEnabled(false);
        cbxZonas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxZonasItemStateChanged(evt);
            }
        });
        cbxZonas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxZonasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbxZonasFocusLost(evt);
            }
        });
        cbxZonas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxZonasMouseClicked(evt);
            }
        });
        cbxZonas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxZonasActionPerformed(evt);
            }
        });
        cbxZonas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxZonasKeyPressed(evt);
            }
        });

        cbxAgencias.setToolTipText("Seleccionar la Agencia");
        cbxAgencias.setEnabled(false);
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

        cbxCentrosDeCosto.setToolTipText("Asignar Centro de costo ó Proyecto");
        cbxCentrosDeCosto.setEnabled(false);
        cbxCentrosDeCosto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCentrosDeCostoItemStateChanged(evt);
            }
        });
        cbxCentrosDeCosto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxCentrosDeCostoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbxCentrosDeCostoFocusLost(evt);
            }
        });
        cbxCentrosDeCosto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxCentrosDeCostoKeyPressed(evt);
            }
        });

        cbxTiposDeContrato.setToolTipText("Seleccionar el tipo de contrato");
        cbxTiposDeContrato.setEnabled(false);
        cbxTiposDeContrato.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTiposDeContratoItemStateChanged(evt);
            }
        });
        cbxTiposDeContrato.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxTiposDeContratoFocusGained(evt);
            }
        });
        cbxTiposDeContrato.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxTiposDeContratoKeyPressed(evt);
            }
        });

        cbxEntidadesBancarias.setToolTipText("Seleccionar la entidad Bancaria");
        cbxEntidadesBancarias.setEnabled(false);
        cbxEntidadesBancarias.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxEntidadesBancariasFocusGained(evt);
            }
        });
        cbxEntidadesBancarias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxEntidadesBancariasKeyPressed(evt);
            }
        });

        txtNumeroDeCuenta.setEditable(false);
        txtNumeroDeCuenta.setToolTipText("Ingreasr el # de Cuenta de nómina");
        txtNumeroDeCuenta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeCuentaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumeroDeCuentaFocusLost(evt);
            }
        });
        txtNumeroDeCuenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeCuentaKeyPressed(evt);
            }
        });

        chkActivo.setSelected(true);
        chkActivo.setText("Empleado Activo");
        chkActivo.setEnabled(false);
        chkActivo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkActivoStateChanged(evt);
            }
        });

        cbxRegionales.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxRegionales.setEnabled(false);
        cbxRegionales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxRegionalesMouseClicked(evt);
            }
        });
        cbxRegionales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxRegionalesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel26))
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEscolaridad, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateCumpleanios, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLugarDeNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxTipoDeSangre, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxCargos, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxZonas, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxCentrosDeCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxTiposDeContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroDeCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxEntidadesBancarias, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cbxRegionales, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbxAgencias, javax.swing.GroupLayout.Alignment.LEADING, 0, 223, Short.MAX_VALUE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(chkActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEscolaridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateCumpleanios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLugarDeNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(cbxTipoDeSangre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbxCargos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxZonas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(cbxRegionales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxAgencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxCentrosDeCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxTiposDeContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroDeCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel26)
                    .addComponent(cbxEntidadesBancarias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(chkActivo)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1009, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();
    }//GEN-LAST:event_btnSalirActionPerformed


    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        nuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed


    private void agregarImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarImagenMouseClicked
        File fileFoto = null;
        if (actualizarFoto) {
            if (!txtCedula.getText().isEmpty()) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Elija la imagen de la fotografía del Empleado");
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "JPG & GIF Imagenes", "jpg", "gif", "png");
                chooser.setFileFilter(filter);

                int returnVal = chooser.showOpenDialog(chooser);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    fileFoto = chooser.getSelectedFile();
                    if (fileFoto.length() > 1048576) {
                        JOptionPane.showMessageDialog(this, "Archivo es muy grande, debe tener menos de un mega", "Alerta", 1);
                        fileFoto = null;
                    } else {
                        if (fileFoto != null) {

                            filfoto = new ImageIcon(fileFoto.getPath());
                            this.foto = new File(fileFoto.getPath());
                            panelFotografia.setIcon(filfoto);
                            panelFotografia.updateUI();
                            empleado.setFormatoFotografia(foto.getAbsolutePath().substring(foto.getAbsolutePath().lastIndexOf(".")));
                            empleado.setFotografia(foto);
                            empleado.setImage(filfoto);

                            //colocarImagen(fileFoto.getPath());
                        }
                    }

                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar primero el número de la Cédula", "Alerta", 1);
            }
        }

    }//GEN-LAST:event_agregarImagenMouseClicked

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed

        grabar();

    }//GEN-LAST:event_btnGrabarActionPerformed


    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed


    private void txtTelefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCelular.requestFocus();
        }
    }//GEN-LAST:event_txtTelefonoKeyPressed

    private void txtTelefonoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoFocusGained
        txtTelefono.setSelectionStart(0);
        txtTelefono.setSelectionEnd(txtTelefono.getText().length());
    }//GEN-LAST:event_txtTelefonoFocusGained

    private void chkActivoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkActivoStateChanged
        if (chkActivo.isSelected()) {
            chkActivo.setText("Empleado Activo");
        } else {
            chkActivo.setText("Empleado No Activo");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_chkActivoStateChanged

    private void cbxEntidadesBancariasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxEntidadesBancariasKeyPressed

    }//GEN-LAST:event_cbxEntidadesBancariasKeyPressed

    private void cbxEntidadesBancariasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxEntidadesBancariasFocusGained

    }//GEN-LAST:event_cbxEntidadesBancariasFocusGained

    private void cbxTiposDeContratoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxTiposDeContratoKeyPressed

    }//GEN-LAST:event_cbxTiposDeContratoKeyPressed

    private void cbxTiposDeContratoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTiposDeContratoFocusGained

    }//GEN-LAST:event_cbxTiposDeContratoFocusGained

    private void cbxTiposDeContratoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTiposDeContratoItemStateChanged
        //        txtNumeroDeCuenta.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTiposDeContratoItemStateChanged

    private void cbxCentrosDeCostoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxCentrosDeCostoKeyPressed

    }//GEN-LAST:event_cbxCentrosDeCostoKeyPressed

    private void cbxCentrosDeCostoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxCentrosDeCostoFocusLost

    }//GEN-LAST:event_cbxCentrosDeCostoFocusLost

    private void cbxCentrosDeCostoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxCentrosDeCostoFocusGained

    }//GEN-LAST:event_cbxCentrosDeCostoFocusGained

    private void cbxCentrosDeCostoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCentrosDeCostoItemStateChanged
        //cbxTiposDeContrato.requestFocus();
    }//GEN-LAST:event_cbxCentrosDeCostoItemStateChanged

    private void cbxAgenciasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxAgenciasKeyPressed

    }//GEN-LAST:event_cbxAgenciasKeyPressed

    private void cbxAgenciasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxAgenciasFocusLost

    }//GEN-LAST:event_cbxAgenciasFocusLost

    private void cbxAgenciasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxAgenciasFocusGained
        String region = cbxRegionales.getSelectedItem().toString();

        for (CAgencias agencia : this.ini.getListaDeAgencias()) {
            if (region.equals(agencia.getNombreRegional())) {
                if (agencia.getActivoAgencia() == 1) {
                    cbxAgencias.addItem(agencia.getNombreAgencia());
                }

            }
        }
    }//GEN-LAST:event_cbxAgenciasFocusGained

    private void cbxAgenciasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxAgenciasItemStateChanged
        //cbxCentrosDeCosto.requestFocus();
    }//GEN-LAST:event_cbxAgenciasItemStateChanged

    private void cbxZonasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxZonasKeyPressed

    }//GEN-LAST:event_cbxZonasKeyPressed

    private void cbxZonasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxZonasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxZonasActionPerformed

    private void cbxZonasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxZonasFocusLost

    }//GEN-LAST:event_cbxZonasFocusLost

    private void cbxZonasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxZonasFocusGained

    }//GEN-LAST:event_cbxZonasFocusGained

    private void cbxZonasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxZonasItemStateChanged


    }//GEN-LAST:event_cbxZonasItemStateChanged

    private void cbxCargosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxCargosKeyPressed

    }//GEN-LAST:event_cbxCargosKeyPressed

    private void cbxCargosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxCargosFocusLost

    }//GEN-LAST:event_cbxCargosFocusLost

    private void cbxCargosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxCargosFocusGained

    }//GEN-LAST:event_cbxCargosFocusGained

    private void cbxTipoDeSangreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxTipoDeSangreKeyPressed

    }//GEN-LAST:event_cbxTipoDeSangreKeyPressed

    private void cbxTipoDeSangreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTipoDeSangreFocusGained

    }//GEN-LAST:event_cbxTipoDeSangreFocusGained

    private void cbxTipoDeSangreItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoDeSangreItemStateChanged

    }//GEN-LAST:event_cbxTipoDeSangreItemStateChanged

    private void cbxEstadoCivilKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxEstadoCivilKeyPressed

    }//GEN-LAST:event_cbxEstadoCivilKeyPressed

    private void cbxEstadoCivilFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxEstadoCivilFocusGained

    }//GEN-LAST:event_cbxEstadoCivilFocusGained

    private void cbxEstadoCivilItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxEstadoCivilItemStateChanged

    }//GEN-LAST:event_cbxEstadoCivilItemStateChanged

    private void cbxGeneroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxGeneroKeyPressed

    }//GEN-LAST:event_cbxGeneroKeyPressed

    private void cbxGeneroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxGeneroItemStateChanged
        dateCumpleanios.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_cbxGeneroItemStateChanged

    private void txtNumeroDeCuentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeCuentaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxEntidadesBancarias.requestFocus();
        }
    }//GEN-LAST:event_txtNumeroDeCuentaKeyPressed

    private void txtNumeroDeCuentaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeCuentaFocusLost

    }//GEN-LAST:event_txtNumeroDeCuentaFocusLost

    private void txtNumeroDeCuentaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeCuentaFocusGained
        txtNumeroDeCuenta.setSelectionStart(0);
        txtNumeroDeCuenta.setSelectionEnd(txtNumeroDeCuenta.getText().length());
    }//GEN-LAST:event_txtNumeroDeCuentaFocusGained

    private void txtEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxTipoDeSangre.requestFocus();
        }
    }//GEN-LAST:event_txtEmailKeyPressed

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost

    }//GEN-LAST:event_txtEmailFocusLost

    private void txtEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusGained
        txtEmail.setSelectionStart(0);
        txtEmail.setSelectionEnd(txtEmail.getText().length());
    }//GEN-LAST:event_txtEmailFocusGained

    private void txtLugarDeNacimientoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLugarDeNacimientoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxEstadoCivil.requestFocus();
        }
    }//GEN-LAST:event_txtLugarDeNacimientoKeyPressed

    private void txtLugarDeNacimientoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLugarDeNacimientoFocusLost

    }//GEN-LAST:event_txtLugarDeNacimientoFocusLost

    private void txtLugarDeNacimientoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLugarDeNacimientoFocusGained
        txtLugarDeNacimiento.setSelectionStart(0);
        txtLugarDeNacimiento.setSelectionEnd(txtLugarDeNacimiento.getText().length());
    }//GEN-LAST:event_txtLugarDeNacimientoFocusGained

    private void txtEscolaridadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEscolaridadKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxGenero.requestFocus();
        }
    }//GEN-LAST:event_txtEscolaridadKeyPressed

    private void txtEscolaridadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEscolaridadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEscolaridadActionPerformed

    private void txtEscolaridadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEscolaridadFocusGained
        txtEscolaridad.setSelectionStart(0);
        txtEscolaridad.setSelectionEnd(txtEscolaridad.getText().length());
    }//GEN-LAST:event_txtEscolaridadFocusGained

    private void txtCelularCorporativoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCelularCorporativoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtEscolaridad.requestFocus();
        }
    }//GEN-LAST:event_txtCelularCorporativoKeyPressed

    private void txtCelularCorporativoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCelularCorporativoFocusGained
        txtCelularCorporativo.setSelectionStart(0);
        txtCelularCorporativo.setSelectionEnd(txtCelularCorporativo.getText().length());
    }//GEN-LAST:event_txtCelularCorporativoFocusGained

    private void txtCelularKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCelularKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCelularCorporativo.requestFocus();
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
            txtBarrio.requestFocus();

        }
    }//GEN-LAST:event_txtDireccionKeyPressed

    private void txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionActionPerformed

    private void txtDireccionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionFocusLost

    }//GEN-LAST:event_txtDireccionFocusLost

    private void txtDireccionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionFocusGained
        txtDireccion.setSelectionStart(0);
        txtDireccion.setSelectionEnd(txtDireccion.getText().length());
    }//GEN-LAST:event_txtDireccionFocusGained

    private void txtBarrioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cbxDepartamentos.requestFocus();
        }
    }//GEN-LAST:event_txtBarrioKeyPressed

    private void txtBarrioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarrioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioActionPerformed

    private void txtBarrioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarrioFocusGained
        txtBarrio.setSelectionStart(0);
        txtBarrio.setSelectionEnd(txtBarrio.getText().length());
    }//GEN-LAST:event_txtBarrioFocusGained

    private void txtApellidosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtDireccion.requestFocus();
        }
    }//GEN-LAST:event_txtApellidosKeyPressed

    private void txtApellidosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidosFocusGained
        txtApellidos.setSelectionStart(0);
        txtApellidos.setSelectionEnd(txtApellidos.getText().length());
    }//GEN-LAST:event_txtApellidosFocusGained

    private void txtNombresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombresKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtApellidos.requestFocus();
        }
    }//GEN-LAST:event_txtNombresKeyPressed

    private void txtNombresFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombresFocusGained
        txtNombres.setSelectionStart(0);
        txtNombres.setSelectionEnd(txtNombres.getText().length());
    }//GEN-LAST:event_txtNombresFocusGained

    private void txtCedulaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_F2) {

            FBuscarEmpleados form = new FBuscarEmpleados(this);
            this.getParent().add(form);
            form.toFront();
            form.setClosable(true);
            form.setVisible(true);
            form.setTitle("Formulario para buscar Empleados por apellidos");
            form.txtApellidosPersona.requestFocus();
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            form.show();

        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            /* Se realiza la consulta para buscar el manifiesto que se va a descargar*/
            //new Thread(new HiloConsultarEmpleado(ini, this)).start();
            new Thread(new HiloIngresarEmpleados(ini, this, "consultarEmpleado")).start();

        }
    }//GEN-LAST:event_txtCedulaKeyPressed

    private void txtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaActionPerformed

    private void txtCedulaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaFocusGained
//        txtCedula.setSelectionStart(0);
//        txtCedula.setSelectionEnd(txtCedula.getText().length());
    }//GEN-LAST:event_txtCedulaFocusGained

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing

        if (estaOcupadoGrabando) {
            JOptionPane.showInternalMessageDialog(this, "El sistema está ocupado grabado los datos,\n no se puede cerrar el formulario", "Error al cerrar", JOptionPane.ERROR_MESSAGE);

        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        cancelar();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSalirActionPerformed
        salir();
    }//GEN-LAST:event_jBtnSalirActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
        grabar();
// TODO add your handling code here:
    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void cbxDepartamentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxDepartamentosMouseClicked
//        String departamento = cbxDepartamentos.getSelectedItem().toString();
//
//        for (CCiudades ciudad : this.ini.getListaDeCiudades()) {
//            if (departamento.equals(ciudad.getNombreDepartamento())) {
//                if (ciudad.getActivoCiudad() == 1) {
//                    cbxCiudades.addItem(ciudad.getNombreCiudad());
//                }
//            }
//        }
    }//GEN-LAST:event_cbxDepartamentosMouseClicked

    private void cbxZonasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxZonasMouseClicked
        String zone = cbxZonas.getSelectedItem().toString();

        /*Llena la lista desplegable de la regionales */
        for (CRegionales region : this.ini.getListaDeRegionales()) {
            if (zone.equals(region.getNombreZona())) {
                if (region.getActivoRegional() == 1) {
                    cbxRegionales.addItem(region.getNombreRegional());
                }

            }
        }

    }//GEN-LAST:event_cbxZonasMouseClicked

    private void cbxRegionalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxRegionalesMouseClicked

    }//GEN-LAST:event_cbxRegionalesMouseClicked

    private void cbxRegionalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxRegionalesActionPerformed

    }//GEN-LAST:event_cbxRegionalesActionPerformed

    private void cbxDepartamentosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxDepartamentosItemStateChanged
        try {
            for (CDepartamentos dpto : ini.getListaDeDepartamentos()) {
                if (cbxDepartamentos.getSelectedItem() != null) {
                    if (cbxDepartamentos.getSelectedItem().toString().equals(dpto.getNombreDepartamento())) {
                        departamento = dpto;

                       // cbxCiudades.removeAllItems();
//                        for (CCiudades ciu : departamento.getListaDeCiudades()) {
//                            if (ciu.getIdDepartamento() == departamento.getIdDepartamento()) {
//                                cbxCiudades.addItem(ciu.getNombreCiudad());
//                            }
//                        }
                    }
                }
            }
        } catch (Exception ex) {

        }
    }//GEN-LAST:event_cbxDepartamentosItemStateChanged

    private void cbxCiudadesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxCiudadesKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

        } else {
           // String valor =cbxCiudades.gett
            
        }
    }//GEN-LAST:event_cbxCiudadesKeyPressed

    private void txtCiudadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCiudadKeyPressed
         if (evt.getKeyCode() == KeyEvent.VK_UP) {
            search.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            search.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = search.getSelectedText();
            if(ciudadElegida){
                txtTelefono.requestFocus();
            }else{
           
            txtCiudad.setText(text);
            menu.setVisible(false);
            
        }
        }
    }//GEN-LAST:event_txtCiudadKeyPressed

    private void txtCiudadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCiudadMouseClicked
       if (search.getItemSize() > 0) {
            menu.show(txtCiudad, 0, txtCiudad.getHeight());
            search.clearSelected();
        }
    }//GEN-LAST:event_txtCiudadMouseClicked

    private void txtCiudadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCiudadKeyReleased
       if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtCiudad.getText().trim().toLowerCase();
            search.setData(search(text));
            if (search.getItemSize() > 0) {
                //  * 2 top and bot border
                menu.show(txtCiudad, 0, txtCiudad.getHeight());
               // menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                menu.setPopupSize(txtCiudad.getWidth(), (search.getItemSize() * 35) + 2);

            } else {
                menu.setVisible(false);
            }
        }
    }//GEN-LAST:event_txtCiudadKeyReleased

    private void cbxDepartamentosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxDepartamentosFocusLost
     for (CDepartamentos dpto : ini.getListaDeDepartamentos()) {
                if (cbxDepartamentos.getSelectedItem() != null) {
                    if (cbxDepartamentos.getSelectedItem().toString().equals(dpto.getNombreDepartamento())) {
                        departamento = dpto;
                        break;
                    }
                }
     }
    }//GEN-LAST:event_cbxDepartamentosFocusLost

    private void jBtnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRefrescarActionPerformed
        new Thread(new HiloIngresarEmpleados(ini, this, "refrescar")).start();       // TODO add your handling code here:
    }//GEN-LAST:event_jBtnRefrescarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    public javax.swing.JComboBox cbxAgencias;
    public javax.swing.JComboBox cbxCargos;
    public javax.swing.JComboBox cbxCentrosDeCosto;
    public javax.swing.JComboBox<String> cbxCiudades;
    public javax.swing.JComboBox<String> cbxDepartamentos;
    public javax.swing.JComboBox cbxEntidadesBancarias;
    public javax.swing.JComboBox cbxEstadoCivil;
    public javax.swing.JComboBox cbxGenero;
    public javax.swing.JComboBox<String> cbxRegionales;
    public javax.swing.JComboBox cbxTipoDeSangre;
    public javax.swing.JComboBox cbxTiposDeContrato;
    public javax.swing.JComboBox cbxZonas;
    public javax.swing.JCheckBox chkActivo;
    public com.toedter.calendar.JDateChooser dateCumpleanios;
    public com.toedter.calendar.JDateChooser dateFechaIngreso;
    public javax.swing.JToggleButton jBtnCancel;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JButton jBtnNuevo;
    public javax.swing.JToggleButton jBtnRefrescar;
    private javax.swing.JToggleButton jBtnSalir;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public org.edisoncor.gui.panel.PanelImage panelFotografia;
    private javax.swing.JPopupMenu pumImagen;
    public javax.swing.JTextField txtApellidos;
    public javax.swing.JTextField txtBarrio;
    public javax.swing.JTextField txtCedula;
    public javax.swing.JTextField txtCelular;
    public javax.swing.JTextField txtCelularCorporativo;
    public ui.swing.searchText.MyTextField txtCiudad;
    public javax.swing.JTextField txtDireccion;
    public javax.swing.JTextField txtEmail;
    public javax.swing.JTextField txtEscolaridad;
    public javax.swing.JTextField txtLugarDeNacimiento;
    public javax.swing.JTextField txtNombres;
    public javax.swing.JTextField txtNumeroDeCuenta;
    public javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables

    private void nuevo() {
        if (actualizar) {
            actualizarFoto = true;
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            habilitar(true);
            txtCedula.setEditable(false);
            txtCedula.requestFocus();
            txtCedula.requestFocus();

        } else {
            limpiar();
            actualizarFoto = true;
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            txtCedula.setEnabled(true);
            txtCedula.setEditable(true);
            txtCedula.requestFocus();
            txtCedula.requestFocus();

        }

          jBtnRefrescar.setEnabled(false);
    }

    private void grabar() {

        int x = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", JOptionPane.OK_CANCEL_OPTION);
        if (x == 0) {
            // new Thread(new HiloGuardarEmpleado(this)).start();

            new Thread(new HiloIngresarEmpleados(ini, this, "guardar")).start();

        }
    }

    private void cancelar() {
        limpiar();
        habilitar(false);
        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);
          jBtnRefrescar.setEnabled(true);
    }

    private void salir() {
        this.dispose();
        this.setVisible(false);
    }

    public void limpiar() {

        txtCedula.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtBarrio.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtCelular.setText("");
        txtCelularCorporativo.setText("");
        txtEscolaridad.setText("");
        txtLugarDeNacimiento.setText("");
        txtNumeroDeCuenta.setText("");
        txtEmail.setText("");
        txtCiudad.setText("");

        dateCumpleanios.setDate(new Date());
        dateFechaIngreso.setDate(new Date());

        panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg")));
        panelFotografia.updateUI();

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); //
        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png")));

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

        btnNuevo.setText("Nuevo");
        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);
        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);
        actualizarFoto = false;
        actualizar = false;

    }

    public void habilitar(boolean valor) {

        try {
            txtCedula.setEnabled(valor);
            txtNombres.setEnabled(valor);
            txtApellidos.setEnabled(valor);
            txtBarrio.setEnabled(valor);
            txtDireccion.setEnabled(valor);
            txtTelefono.setEnabled(valor);
            txtCelular.setEnabled(valor);
            txtCelularCorporativo.setEnabled(valor);
            txtEscolaridad.setEnabled(valor);
            txtLugarDeNacimiento.setEnabled(valor);
            txtNumeroDeCuenta.setEnabled(valor);
            txtEmail.setEnabled(valor);
            txtCiudad.setEnabled(valor);

            txtCedula.setEditable(valor);
            txtNombres.setEditable(valor);
            txtApellidos.setEditable(valor);
            txtBarrio.setEditable(valor);
            txtDireccion.setEditable(valor);
            txtTelefono.setEditable(valor);
            txtCelular.setEditable(valor);
            txtCelularCorporativo.setEditable(valor);
            txtEscolaridad.setEditable(valor);
            txtLugarDeNacimiento.setEditable(valor);
            txtNumeroDeCuenta.setEditable(valor);
            txtEmail.setEditable(valor);

            cbxGenero.setEnabled(valor);
            cbxDepartamentos.setEnabled(valor);
            cbxCiudades.setEnabled(valor);
           
            cbxEstadoCivil.setEnabled(valor);
            cbxTipoDeSangre.setEnabled(valor);
            cbxCargos.setEnabled(valor);
            cbxZonas.setEnabled(valor);
            cbxRegionales.setEnabled(valor);
            cbxAgencias.setEnabled(valor);
            cbxCentrosDeCosto.setEnabled(valor);
            cbxTiposDeContrato.setEnabled(valor);
            cbxEntidadesBancarias.setEnabled(valor);

            dateCumpleanios.setEnabled(valor);
            dateFechaIngreso.setEnabled(valor);
            System.out.println("Objetos del  formulario actualizados ");
            btnGrabar.setEnabled(valor);
            jBtnGrabar.setEnabled(valor);

            chkActivo.setEnabled(valor);
              jBtnRefrescar.setEnabled(valor);

        } catch (Exception ex) {
            System.out.println("Error al actualizar los coponentes del formulario.. " + ex.getMessage());
        }

    }

    private void seleccionarCiudad(String valor) {
       // a
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
            String sql = "select DISTINCT nombreCiudad, "
                  + "coalesce((select idciudadesstory from ciudadesstory where storyName=nombreCiudad limit 1),'') as Story "
                  + "from ciudades "
                  + "where "
                  + "idDepartamento ='" + departamento.getIdDepartamento() + "' AND "
                  + "activo = 1 AND "
                  + "nombreCiudad like ? order by Story DESC, nombreCiudad limit 7";
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
