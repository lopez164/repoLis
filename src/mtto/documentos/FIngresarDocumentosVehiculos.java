/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import mtto.PrincipalMantenimiento;
import mtto.documentos.objetos.DocumentosPorTipoDeVehiculo;
import mtto.documentos.objetos.DocumentosPorVehiculo;
import mtto.vehiculos.CVehiculos;

/**
 *
 * @author VLI_488
 */
public class FIngresarDocumentosVehiculos extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public boolean actualizar;
    public boolean estaOcupadoGrabando = false;

    String mensaje = null;

    public Inicio ini;
    String suario;
    CUsuarios user;

    public List<DocumentosPorVehiculo> listaDeDocumentosPorVehiculo = null;
    final DefaultListModel model = new DefaultListModel();
    public CVehiculos vehiculo = null;

    String ruta;
    //home/lelopez/Documentos/proyectos/aplicacionLogistica_v2/src/aplicacionlogistica/mantenimientos/tiposDeEmpresa.txt
    public File archivo = null;
    private FileWriter fichero = null;
    private FileReader fr = null;
    private BufferedReader br = null;
    private PrintWriter escrive = null;
    private List<String> lineas;
    public DocumentosPorVehiculo documento;
    public File fileDocumento;
    private String extensionArchivo;

    public TextAutoCompleter autoTxtVehiculos;

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public FIngresarDocumentosVehiculos(Inicio ini) {
        initComponents();
        this.ini = ini;
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
        //cargarLaVista();
        new Thread(new HiloFIngresarDocumentosVehiculos(ini, this, "cargarLaVista")).start();

    }

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     * @param principalMantenimiento
     */
    public FIngresarDocumentosVehiculos(Inicio ini, PrincipalMantenimiento principalMantenimiento) {
        initComponents();
        archivo = new File(ini.getRutaDeApp() + "mantenimientos/tiposDeEmpresa.txt");
        this.ini = ini;
        user = ini.getUser();
        autoTxtVehiculos = new TextAutoCompleter(txtPlaca);
        for (CVehiculos car : this.ini.getListaDeVehiculos()) {
            if (car.getActivoVehiculo() == 1) {
                autoTxtVehiculos.addItem(car.getPlaca() + " ");
            }
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

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtQuienEmite = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtNumeroDocumento = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        dateFechaExpedicion = new com.toedter.calendar.JDateChooser();
        cbxTipoDocumento = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        dateFechaVencimiento = new com.toedter.calendar.JDateChooser();
        txtLugarDeExpedicion = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblMarca = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblModelo = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblKilometraje = new javax.swing.JLabel();
        btnAgregarPdf = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtRutaArchivo = new javax.swing.JTextField();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnImprir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDocumentosPorLinea = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDocumentosPorVehiculo = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrbar = new javax.swing.JButton();
        jbtnImprimir = new javax.swing.JButton();
        jBtnCancelar = new javax.swing.JButton();
        jBtnSalir = new javax.swing.JButton();
        jBtnRefrescar = new javax.swing.JToggleButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario para el ingreso de Documentos de los Vehiculos");
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
        jLabel1.setText("Placa");

        txtPlaca.setEditable(false);
        txtPlaca.setToolTipText("Ingresar placa del vehiculo");
        txtPlaca.setName(""); // NOI18N
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

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Tipo Documento");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Quien Emite");

        txtQuienEmite.setEditable(false);
        txtQuienEmite.setToolTipText("Ingresar dirección");
        txtQuienEmite.setName(""); // NOI18N
        txtQuienEmite.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQuienEmiteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuienEmiteFocusLost(evt);
            }
        });
        txtQuienEmite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuienEmiteActionPerformed(evt);
            }
        });
        txtQuienEmite.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQuienEmiteKeyPressed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("# Documento");

        txtNumeroDocumento.setEditable(false);
        txtNumeroDocumento.setToolTipText("ingresar el #  de teléfono fijo");
        txtNumeroDocumento.setName(""); // NOI18N
        txtNumeroDocumento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDocumentoFocusGained(evt);
            }
        });
        txtNumeroDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDocumentoKeyPressed(evt);
            }
        });

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Expedicion");

        dateFechaExpedicion.setToolTipText("");
        dateFechaExpedicion.setDateFormatString("yyyy/MM/dd");
        dateFechaExpedicion.setEnabled(false);
        dateFechaExpedicion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dateFechaExpedicionFocusLost(evt);
            }
        });
        dateFechaExpedicion.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateFechaExpedicionPropertyChange(evt);
            }
        });
        dateFechaExpedicion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dateFechaExpedicionKeyPressed(evt);
            }
        });

        cbxTipoDocumento.setToolTipText("Seleccionar el tipo de Documento");
        cbxTipoDocumento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTipoDocumentoItemStateChanged(evt);
            }
        });
        cbxTipoDocumento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxTipoDocumentoFocusGained(evt);
            }
        });
        cbxTipoDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxTipoDocumentoKeyPressed(evt);
            }
        });

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Vencimiento");

        dateFechaVencimiento.setToolTipText("Seleccionar la fecha de nacimiento");
        dateFechaVencimiento.setDateFormatString("yyyy/MM/dd");
        dateFechaVencimiento.setEnabled(false);

        txtLugarDeExpedicion.setEditable(false);
        txtLugarDeExpedicion.setToolTipText("ingresar el #  de teléfono fijo");
        txtLugarDeExpedicion.setName(""); // NOI18N
        txtLugarDeExpedicion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLugarDeExpedicionFocusGained(evt);
            }
        });
        txtLugarDeExpedicion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLugarDeExpedicionKeyPressed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Lugar Exp.");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Marca");

        lblMarca.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Modelo");

        lblModelo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Kilometraje");

        lblKilometraje.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        btnAgregarPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/PDF-icon.png"))); // NOI18N
        btnAgregarPdf.setText("Agregar Imagen Documento");
        btnAgregarPdf.setToolTipText("Salir de ingresar empleado...");
        btnAgregarPdf.setEnabled(false);
        btnAgregarPdf.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAgregarPdf.setPreferredSize(new java.awt.Dimension(97, 97));
        btnAgregarPdf.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregarPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarPdfActionPerformed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Ruta");

        txtRutaArchivo.setEditable(false);
        txtRutaArchivo.setToolTipText("ingresar el #  de teléfono fijo");
        txtRutaArchivo.setName(""); // NOI18N
        txtRutaArchivo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRutaArchivoFocusGained(evt);
            }
        });
        txtRutaArchivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRutaArchivoKeyPressed(evt);
            }
        });

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(lblCirculoDeProgreso))
                            .addComponent(lblMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblKilometraje, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNumeroDocumento, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(dateFechaExpedicion, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dateFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbxTipoDocumento, 0, 318, Short.MAX_VALUE)
                                .addComponent(txtLugarDeExpedicion, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnAgregarPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtQuienEmite))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtRutaArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblCirculoDeProgreso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKilometraje, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtQuienEmite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateFechaExpedicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLugarDeExpedicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRutaArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregarPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        dateFechaExpedicion.addPropertyChangeListener(new java.beans.PropertyChangeListener(){
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateFechaExpedicionPropertyChange(evt);

            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnImprir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/printer64x64.png"))); // NOI18N
        btnImprir.setText("Imprimir");
        btnImprir.setToolTipText("Salir de ingresar empleado...");
        btnImprir.setEnabled(false);
        btnImprir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprir.setPreferredSize(new java.awt.Dimension(97, 97));
        btnImprir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImprir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprirActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(btnGrabar, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImprir, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImprir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblDocumentosPorLinea.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Nombre de Documento", "tipo Vehiculo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDocumentosPorLinea.getTableHeader().setReorderingAllowed(false);
        tblDocumentosPorLinea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDocumentosPorLineaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblDocumentosPorLinea);
        if (tblDocumentosPorLinea.getColumnModel().getColumnCount() > 0) {
            tblDocumentosPorLinea.getColumnModel().getColumn(0).setResizable(false);
            tblDocumentosPorLinea.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblDocumentosPorLinea.getColumnModel().getColumn(1).setResizable(false);
            tblDocumentosPorLinea.getColumnModel().getColumn(1).setPreferredWidth(280);
            tblDocumentosPorLinea.getColumnModel().getColumn(2).setResizable(false);
            tblDocumentosPorLinea.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        tblDocumentosPorVehiculo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "numero Doc.", "Nombre de Documento", "Vence", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDocumentosPorVehiculo.getTableHeader().setReorderingAllowed(false);
        tblDocumentosPorVehiculo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDocumentosPorVehiculoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblDocumentosPorVehiculo);
        if (tblDocumentosPorVehiculo.getColumnModel().getColumnCount() > 0) {
            tblDocumentosPorVehiculo.getColumnModel().getColumn(0).setResizable(false);
            tblDocumentosPorVehiculo.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblDocumentosPorVehiculo.getColumnModel().getColumn(1).setResizable(false);
            tblDocumentosPorVehiculo.getColumnModel().getColumn(1).setPreferredWidth(120);
            tblDocumentosPorVehiculo.getColumnModel().getColumn(2).setResizable(false);
            tblDocumentosPorVehiculo.getColumnModel().getColumn(2).setPreferredWidth(280);
            tblDocumentosPorVehiculo.getColumnModel().getColumn(3).setResizable(false);
            tblDocumentosPorVehiculo.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblDocumentosPorVehiculo.getColumnModel().getColumn(4).setResizable(false);
            tblDocumentosPorVehiculo.getColumnModel().getColumn(4).setPreferredWidth(120);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jBtnGrbar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jBtnGrbar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnGrbar.setEnabled(false);
        jBtnGrbar.setFocusable(false);
        jBtnGrbar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGrbar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGrbar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGrbarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGrbar);

        jbtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jbtnImprimir.setToolTipText("Agregar Sucursal");
        jbtnImprimir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtnImprimir.setEnabled(false);
        jbtnImprimir.setFocusable(false);
        jbtnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnImprimirActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnImprimir);

        jBtnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancelar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
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
        jBtnSalir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(12, 12, 12)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void nuevo() {

        limpiar();
        actualizar = false;
        btnNuevo.setEnabled(false);
        btnAgregarPdf.setEnabled(false);
        btnImprir.setEnabled(false);
        txtPlaca.setEnabled(true);
        txtPlaca.setEditable(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = new Date();
        String fechaSistema = dateFormat.format(fechaActual);

        dateFechaExpedicion.setDate(fechaActual);
        dateFechaVencimiento.setDate(fechaActual);

        jBtnRefrescar.setEnabled(false);
        txtPlaca.requestFocus();
    }

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        if (cbxTipoDocumento.getSelectedIndex() == 0) {
            JOptionPane.showInternalMessageDialog(this, "Debe seleccionar un tipo de documento", "Alerta", JOptionPane.WARNING_MESSAGE);
        } else {

            grabar();
        }


    }//GEN-LAST:event_btnGrabarActionPerformed

    private void grabar() throws HeadlessException {
        int x = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", JOptionPane.OK_CANCEL_OPTION);

        if (x == JOptionPane.OK_OPTION) {

            if (validar()) {

                new Thread(new HiloFIngresarDocumentosVehiculos(ini, this, "grabarDocumentos")).start();

            } else {
                JOptionPane.showInternalMessageDialog(this, "Error al validar la informacion del formulario", "Error al guardar", 0);
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
         jBtnRefrescar.setEnabled(true);
    }

    private void txtNumeroDocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDocumentoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            colocarFechaDeVencimiento();
        }
    }//GEN-LAST:event_txtNumeroDocumentoKeyPressed

    private void txtNumeroDocumentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDocumentoFocusGained
        txtNumeroDocumento.setSelectionStart(0);
        txtNumeroDocumento.setSelectionEnd(txtNumeroDocumento.getText().length());
    }//GEN-LAST:event_txtNumeroDocumentoFocusGained

    private void txtQuienEmiteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuienEmiteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            activarCargadorDeArchivos();
        }
    }//GEN-LAST:event_txtQuienEmiteKeyPressed

    private void activarCargadorDeArchivos() {
        if (txtQuienEmite.getText().length() > 0) {
            btnAgregarPdf.setEnabled(true);
            btnImprir.setEnabled(false);
            jbtnImprimir.setEnabled(false);

        }
    }

    private void txtQuienEmiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuienEmiteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuienEmiteActionPerformed

    private void txtQuienEmiteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuienEmiteFocusGained
        txtQuienEmite.setSelectionStart(0);
        txtQuienEmite.setSelectionEnd(txtQuienEmite.getText().length());
    }//GEN-LAST:event_txtQuienEmiteFocusGained

    private void txtPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            new Thread(new HiloFIngresarDocumentosVehiculos(ini, this, "consultarCarro")).start();

        }
    }//GEN-LAST:event_txtPlacaKeyPressed

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void txtPlacaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusGained
        // txtPlaca.setSelectionStart(0);
        //txtPlaca.setSelectionEnd(txtPlaca.getText().length());
    }//GEN-LAST:event_txtPlacaFocusGained

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing

        if (!estaOcupadoGrabando) {

        } else {
            JOptionPane.showInternalMessageDialog(this, "El sistema está ocupado grabado los datos,\n no se puede cerrar el formulario", "Error al cerrar", JOptionPane.ERROR_MESSAGE);

        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void btnImprirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprirActionPerformed

        try {

            Desktop.getDesktop().open(documento.getArhivoDocumento());

        } catch (IOException ex) {
            Logger.getLogger(FIngresarDocumentosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnImprirActionPerformed

    private void salir() {
        this.dispose();
        this.setVisible(false);
    }

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGrbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrbarActionPerformed
        grabar();
    }//GEN-LAST:event_jBtnGrbarActionPerformed

    private void jBtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_jBtnCancelarActionPerformed

    private void jBtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSalirActionPerformed
        salir();
    }//GEN-LAST:event_jBtnSalirActionPerformed

    private void cbxTipoDocumentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTipoDocumentoItemStateChanged
txtQuienEmite.requestFocus();
    }//GEN-LAST:event_cbxTipoDocumentoItemStateChanged

    private void cbxTipoDocumentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxTipoDocumentoFocusGained
        btnImprir.setEnabled(false);
        jbtnImprimir.setEnabled(false);

    }//GEN-LAST:event_cbxTipoDocumentoFocusGained

    private void cbxTipoDocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxTipoDocumentoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTipoDocumentoKeyPressed

    private void jbtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnImprimirActionPerformed
        try {

            documento.getArhivoDocumento();

        } catch (IOException ex) {
            Logger.getLogger(FIngresarDocumentosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jbtnImprimirActionPerformed

    private void tblDocumentosPorLineaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDocumentosPorLineaMouseClicked

        int filaSelleccionada = tblDocumentosPorLinea.getSelectedRow();

        boolean valor = (Boolean) tblDocumentosPorLinea.getValueAt(filaSelleccionada, 0);


    }//GEN-LAST:event_tblDocumentosPorLineaMouseClicked

    private void txtLugarDeExpedicionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLugarDeExpedicionFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLugarDeExpedicionFocusGained

    private void txtLugarDeExpedicionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLugarDeExpedicionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLugarDeExpedicionKeyPressed

    private void btnAgregarPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPdfActionPerformed

        if (!txtPlaca.getText().isEmpty()) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Elija el documento del Vehículo");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Documentos en PDF", "pdf");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(chooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileDocumento = chooser.getSelectedFile();
                extensionArchivo = fileDocumento.getAbsolutePath().substring(fileDocumento.getAbsolutePath().lastIndexOf(".") + 1);
                txtRutaArchivo.setText(fileDocumento.getAbsolutePath());

                btnGrabar.setEnabled(true);

                if (fileDocumento.length() > 2097152) {
                    JOptionPane.showMessageDialog(null, "Archivo es muy grande, debe tener menos de un mega", "Alerta", 1);
                    fileDocumento = null;
                }

            }
        }

    }//GEN-LAST:event_btnAgregarPdfActionPerformed

    private void txtRutaArchivoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRutaArchivoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaArchivoFocusGained

    private void txtRutaArchivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRutaArchivoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaArchivoKeyPressed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void tblDocumentosPorVehiculoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDocumentosPorVehiculoMouseClicked
        //consultarDocumento();

        new Thread(new HiloFIngresarDocumentosVehiculos(ini, this, "consultarDocumento")).start();

    }//GEN-LAST:event_tblDocumentosPorVehiculoMouseClicked


    private void dateFechaExpedicionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateFechaExpedicionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            colocarFechaDeVencimiento();
        }

    }//GEN-LAST:event_dateFechaExpedicionKeyPressed

    private void dateFechaExpedicionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateFechaExpedicionFocusLost
        colocarFechaDeVencimiento();
    }//GEN-LAST:event_dateFechaExpedicionFocusLost

    private void dateFechaExpedicionPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateFechaExpedicionPropertyChange
        try {
            colocarFechaDeVencimiento();
        } catch (Exception e) {

        }

    }//GEN-LAST:event_dateFechaExpedicionPropertyChange

    private void txtQuienEmiteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuienEmiteFocusLost
        activarCargadorDeArchivos();
    }//GEN-LAST:event_txtQuienEmiteFocusLost

    private void jBtnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRefrescarActionPerformed
        new Thread(new HiloFIngresarDocumentosVehiculos(ini, this, "refrescar")).start();       // TODO add your handling code here:
    }//GEN-LAST:event_jBtnRefrescarActionPerformed

    public void colocarFechaDeVencimiento() {

        Date fecha = new Date();
        fecha = dateFechaExpedicion.getCalendar().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(Calendar.DATE, 364);
        fecha = c.getTime();
        dateFechaVencimiento.setDate(fecha);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregarPdf;
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnImprir;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    public javax.swing.JComboBox cbxTipoDocumento;
    public com.toedter.calendar.JDateChooser dateFechaExpedicion;
    public com.toedter.calendar.JDateChooser dateFechaVencimiento;
    public javax.swing.JButton jBtnCancelar;
    public javax.swing.JButton jBtnGrbar;
    public javax.swing.JButton jBtnNuevo;
    public javax.swing.JToggleButton jBtnRefrescar;
    public javax.swing.JButton jBtnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    public javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    public javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JButton jbtnImprimir;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JLabel lblKilometraje;
    public javax.swing.JLabel lblMarca;
    public javax.swing.JLabel lblModelo;
    public javax.swing.JTable tblDocumentosPorLinea;
    public javax.swing.JTable tblDocumentosPorVehiculo;
    public javax.swing.JTextField txtLugarDeExpedicion;
    public javax.swing.JTextField txtNumeroDocumento;
    public javax.swing.JTextField txtPlaca;
    public javax.swing.JTextField txtQuienEmite;
    public javax.swing.JTextField txtRutaArchivo;
    // End of variables declaration//GEN-END:variables

    public void llenarCamposDeTexto() {
        try {

        } catch (Exception ex) {
            Logger.getLogger(FIngresarDocumentosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al actualizar Campos de Formulario Ingresar empleados" + ex);
            //JOptionPane.showInternalMessageDialog(this, mensaje, "Error al actualizar Campos de Formulario Ingresar empleados \n" + ex, 0);

        }

    }

    public boolean validar() {

        boolean verificado = true;
        mensaje = "";
        return verificado;

    }

    public void limpiar() {

        txtPlaca.setText("");
        //txtNombres.setText("");
        //cbxTipoDocumento.setSelectedIndex(0);
        txtQuienEmite.setText("");
        //txtBarrio.setText("");
        txtNumeroDocumento.setText("");
        txtRutaArchivo.setText("");
        //txtCelular.setText("");
        //txtContacto.setText("");

        // txtEmail.setText("");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date fechaActual = new Date();
        String fechaSistema = dateFormat.format(fechaActual);

        dateFechaExpedicion.setDate(fechaActual);

        colocarFechaDeVencimiento();

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); //
        btnNuevo.setText("Nuevo");
        btnGrabar.setEnabled(false);
        btnNuevo.setEnabled(true);
        actualizar = false;

        lblKilometraje.setText("");
        lblMarca.setText("");
        lblModelo.setText("");

        limpiarTabla1();
        limpiarTabla2();
        vehiculo = null;

    }

    public void limpiarTabla1() {

        DefaultTableModel modelo = (DefaultTableModel) tblDocumentosPorLinea.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTabla2() {

        DefaultTableModel modelo = (DefaultTableModel) tblDocumentosPorVehiculo.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void llenarTabla1() throws Exception {

        int filaTabla1;

        DefaultTableModel modelo = (DefaultTableModel) tblDocumentosPorLinea.getModel();

        for (DocumentosPorTipoDeVehiculo obj : vehiculo.getlistaDeDocumentosPorTipoDeVehiculo()) {
            filaTabla1 = tblDocumentosPorLinea.getRowCount();

            modelo.addRow(new Object[tblDocumentosPorLinea.getRowCount()]);

            tblDocumentosPorLinea.setValueAt(filaTabla1 + 1, filaTabla1, 0);  // item
            tblDocumentosPorLinea.setValueAt(obj.getNombreTipoDocumento(), filaTabla1, 1);  // item
            tblDocumentosPorLinea.setValueAt(obj.getNombreTipoVehiculo(), filaTabla1, 2); // numero de manifiesto

        }
    }

    public void llenarTabla2() throws Exception {

        int filaTabla1;

        DefaultTableModel modelo = (DefaultTableModel) tblDocumentosPorVehiculo.getModel();
        String respuesta = "VIGENTE";
        for (DocumentosPorVehiculo obj : vehiculo.getListaDeDocumentosPorVehiculo()) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            filaTabla1 = tblDocumentosPorVehiculo.getRowCount();

            if (obj.getFechaVencimiento().equals("PENDIENTE")) {
                respuesta = "PEDIENTE";

            } else {
                Date fechaActual = new Date();
                String fechaSistema = dateFormat.format(fechaActual);
                respuesta = "VIGENTE";

                Date date1 = dateFormat.parse(obj.getFechaVencimiento());
                Date date2 = dateFormat.parse(fechaSistema);

                if (date1.before(date2)) {
                    respuesta = "VENCIDO";
                }
            }

            /**
             * *********************************************
             */
            modelo.addRow(new Object[tblDocumentosPorVehiculo.getRowCount()]);

            tblDocumentosPorVehiculo.setValueAt(filaTabla1 + 1, filaTabla1, 0);  // item
            tblDocumentosPorVehiculo.setValueAt(obj.getNumeroDocumento(), filaTabla1, 1);  // item
            tblDocumentosPorVehiculo.setValueAt(obj.getNombreTipoDocumento(), filaTabla1, 2); // numero de manifiesto
            tblDocumentosPorVehiculo.setValueAt(obj.getFechaVencimiento(), filaTabla1, 3); // numero de manifiesto
            tblDocumentosPorVehiculo.setValueAt(respuesta, filaTabla1, 4); // numero de manifiesto

        }
    }

    public void habilitar(boolean valor) {

        try {
            txtPlaca.setEnabled(valor);
            //txtNombres.setEnabled(valor);
            cbxTipoDocumento.setEnabled(valor);
            txtQuienEmite.setEnabled(valor);
            //txtBarrio.setEnabled(valor);
            txtNumeroDocumento.setEnabled(valor);
            //txtCelular.setEnabled(valor);
            //txtContacto.setEnabled(valor);

            //txtEmail.setEnabled(valor);
            txtPlaca.setEditable(valor);
            //txtNombres.setEditable(valor);
           // cbxTipoDocumento.setEditable(valor);
            txtQuienEmite.setEditable(valor);
            //txtBarrio.setEditable(valor);
            txtNumeroDocumento.setEditable(valor);
            txtLugarDeExpedicion.setEnabled(valor);
            txtLugarDeExpedicion.setEditable(valor);
            tblDocumentosPorLinea.setEnabled(valor);

            dateFechaExpedicion.setEnabled(valor);
            dateFechaVencimiento.setEnabled(valor);
            System.out.println("Objetos del  formulario actualizados ");
            btnGrabar.setEnabled(valor);

        } catch (Exception ex) {
            System.out.println("Error al actualizar los coponentes del formulario.. " + ex.getMessage());
        }

    }
}
