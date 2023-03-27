/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.administracion;

import aplicacionlogistica.distribucion.consultas.FBuscarEmpleados;
import aplicacionlogistica.distribucion.Threads.HiloGuardarDocumentosPorEmpleado;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.administrativo.PrincipalAdministrador;
import aplicacionlogistica.administrativo.PrincipalTalentoHumanoAdministrador;
import aplicacionlogistica.administrativo.PrincipalTalentoHumanoAuxiliar;
import aplicacionlogistica.distribucion.formularios.graficos.ChartPieDocumentosPorEmpleado;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CCargos;
import aplicacionlogistica.distribucion.objetos.CDocumentos;
import aplicacionlogistica.distribucion.objetos.CDocumentosPorCargo;
import aplicacionlogistica.distribucion.objetos.CDocumentosPorEmpleado;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.Desktop;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class IngresarDocumentosEmpleados extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    File archivo;
    CEmpleados empleado;
    CCargos cargo;
    CDocumentos documento;
    CDocumentosPorCargo docsPorCargo;
    CDocumentosPorEmpleado docsPorEmpleado;
    ArrayList<CDocumentos> arrTodosDocumentosPendientes;
    ArrayList<CDocumentos> arrDocumentosFaltantes;
    ArrayList<CDocumentosPorEmpleado> arrDocumentosVencidos;
    ArrayList<CDocumentosPorEmpleado> arrDocumentosVigentes;
    ArrayList<CDocumentosPorEmpleado> arrDocumentosPorEmpleado;
    ArrayList<CDocumentos> arrDocumentosEmpleado;
    int indiceLista = 0;
    DefaultTableModel modelo1;
    int columna = 0;
    boolean actualizar = false;
    String usuario;
    String mensaje = null;
    CUsuarios user;
   Inicio ini;

    public void setEmpleado(CEmpleados empleado) {
        this.empleado = empleado;
    }

    public ArrayList<CDocumentos> getArrDocumentosEmpleado() {
        return arrDocumentosEmpleado;
    }

    public ArrayList<CDocumentos> getArrDocumentosFaltantes() {
        return arrDocumentosFaltantes;
    }

    public ArrayList<CDocumentosPorEmpleado> getArrDocumentosVencidos() {
        return arrDocumentosVencidos;
    }

    public ArrayList<CDocumentosPorEmpleado> getArrDocumentosVigentes() {
        return arrDocumentosVigentes;
    }

    public ArrayList<CDocumentosPorEmpleado> getArrDocumentosPorEmpleado() {
        return arrDocumentosPorEmpleado;
    }

    /**
     * Creates new form IngresarPersonas
     */
    public String getUsuario() {
        return usuario;
    }

    public IngresarDocumentosEmpleados(Inicio ini) {
        initComponents();
        this.ini = ini;
        this.user = ini.getUser();
       
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtCedula = new javax.swing.JTextField();
        txtNombres = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        btnAgregarArchivo = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaIngresoDocumentos = new javax.swing.JTable();
        btnAgregarRegistro = new javax.swing.JButton();
        lblRuta = new org.edisoncor.gui.label.LabelCustom();
        dateExpedicion = new com.toedter.calendar.JDateChooser();
        dateVencimiento = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        lblVencimimento = new javax.swing.JLabel();
        txtNombreDocumento = new javax.swing.JTextField();
        txtNumeroDocumento = new javax.swing.JTextField();
        txtLugarDeExpedicion = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtRutaDelArchivo = new javax.swing.JTextField();
        txtEntidadEmisora = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaDocsVencidos = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaDocsFaltantes = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaDocsVigentes = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        lblRuta1 = new org.edisoncor.gui.label.LabelCustom();
        txtCargo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstDocumentosPorCargo = new javax.swing.JList();

        agregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Display 16x16.png"))); // NOI18N
        agregarImagen.setText("Agregar Imagen Empleado");
        agregarImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarImagenMouseClicked(evt);
            }
        });
        pumImagen.add(agregarImagen);

        borraUnaFila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Remove.png"))); // NOI18N
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

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Formulario para asignar los Documentos a los Empleados");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtCedula.setEditable(false);
        txtCedula.setToolTipText("Ingresar documento de identificación");
        txtCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaActionPerformed(evt);
            }
        });
        txtCedula.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCedulaFocusGained(evt);
            }
        });
        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCedulaKeyPressed(evt);
            }
        });

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

        jLabel1.setText("Cédula");

        jLabel2.setText("Nombres y Apellidos");

        jTabbedPane1.setEnabled(false);

        jPanel4.setAutoscrolls(true);
        jPanel4.setDoubleBuffered(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAgregarArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/file_url_48x48.png"))); // NOI18N
        btnAgregarArchivo.setToolTipText("Elija un  archivo..");
        btnAgregarArchivo.setEnabled(false);
        btnAgregarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarArchivoActionPerformed(evt);
            }
        });
        jPanel4.add(btnAgregarArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(502, 114, 62, 69));

        tablaIngresoDocumentos.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaIngresoDocumentos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tablaIngresoDocumentos.setComponentPopupMenu(borrarFila);
        tablaIngresoDocumentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaIngresoDocumentosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaIngresoDocumentos);
        if (tablaIngresoDocumentos.getColumnModel().getColumnCount() > 0) {
            tablaIngresoDocumentos.getColumnModel().getColumn(1).setResizable(false);
            tablaIngresoDocumentos.getColumnModel().getColumn(1).setPreferredWidth(250);
            tablaIngresoDocumentos.getColumnModel().getColumn(2).setResizable(false);
            tablaIngresoDocumentos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaIngresoDocumentos.getColumnModel().getColumn(3).setResizable(false);
            tablaIngresoDocumentos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaIngresoDocumentos.getColumnModel().getColumn(4).setResizable(false);
            tablaIngresoDocumentos.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 629, 290));

        btnAgregarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/addfolder-32x32.png"))); // NOI18N
        btnAgregarRegistro.setToolTipText("Agregar archivo..");
        btnAgregarRegistro.setEnabled(false);
        btnAgregarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarRegistroActionPerformed(evt);
            }
        });
        jPanel4.add(btnAgregarRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 114, 62, 69));

        lblRuta.setText(" ");
        lblRuta.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel4.add(lblRuta, new org.netbeans.lib.awtextra.AbsoluteConstraints(636, 209, -1, -1));

        dateExpedicion.setDateFormatString("yyyy/MM/dd");
        dateExpedicion.setEnabled(false);
        dateExpedicion.setMinimumSize(new java.awt.Dimension(87, 20));
        dateExpedicion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                dateExpedicionFocusGained(evt);
            }
        });
        dateExpedicion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dateExpedicionKeyPressed(evt);
            }
        });
        jPanel4.add(dateExpedicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(319, 114, 173, -1));

        dateVencimiento.setDateFormatString("yyyy/MM/dd");
        dateVencimiento.setEnabled(false);
        dateVencimiento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateVencimientoMouseClicked(evt);
            }
        });
        dateVencimiento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                dateVencimientoFocusGained(evt);
            }
        });
        jPanel4.add(dateVencimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(319, 140, 173, -1));

        jLabel27.setText("Tipo  de Documento");
        jPanel4.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 14, -1, -1));

        jLabel28.setText("Número de Documento");
        jPanel4.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 68, -1, -1));

        jLabel29.setText("Lugar de Expedición");
        jPanel4.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 91, -1, -1));

        jLabel30.setText("Fecha de Expedición");
        jPanel4.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 114, -1, -1));

        lblVencimimento.setText("Fecha de Vencimiento");
        jPanel4.add(lblVencimimento, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 139, -1, -1));

        txtNombreDocumento.setEditable(false);
        txtNombreDocumento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDocumentoFocusGained(evt);
            }
        });
        txtNombreDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDocumentoKeyPressed(evt);
            }
        });
        jPanel4.add(txtNombreDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(319, 11, 320, -1));

        txtNumeroDocumento.setEditable(false);
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
        jPanel4.add(txtNumeroDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(319, 62, 320, -1));

        txtLugarDeExpedicion.setEditable(false);
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
        jPanel4.add(txtLugarDeExpedicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(319, 88, 320, -1));

        jLabel32.setText("Ruta de Archivo");
        jPanel4.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 192, -1, -1));

        txtRutaDelArchivo.setEditable(false);
        txtRutaDelArchivo.setMaximumSize(new java.awt.Dimension(6, 20));
        txtRutaDelArchivo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRutaDelArchivoFocusGained(evt);
            }
        });
        jPanel4.add(txtRutaDelArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(319, 189, 313, -1));

        txtEntidadEmisora.setEditable(false);
        txtEntidadEmisora.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEntidadEmisoraFocusGained(evt);
            }
        });
        txtEntidadEmisora.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEntidadEmisoraKeyPressed(evt);
            }
        });
        jPanel4.add(txtEntidadEmisora, new org.netbeans.lib.awtextra.AbsoluteConstraints(319, 37, 320, -1));

        jLabel33.setText("Entidad que lo Emite");
        jPanel4.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 40, -1, -1));

        jTabbedPane1.addTab("Agregar Documentos", jPanel4);

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

        jTabbedPane1.addTab(" Vencidos", jScrollPane4);

        tablaDocsFaltantes.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaDocsFaltantes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tablaDocsFaltantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDocsFaltantesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tablaDocsFaltantes);
        if (tablaDocsFaltantes.getColumnModel().getColumnCount() > 0) {
            tablaDocsFaltantes.getColumnModel().getColumn(0).setResizable(false);
            tablaDocsFaltantes.getColumnModel().getColumn(1).setResizable(false);
            tablaDocsFaltantes.getColumnModel().getColumn(1).setPreferredWidth(250);
            tablaDocsFaltantes.getColumnModel().getColumn(2).setResizable(false);
            tablaDocsFaltantes.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaDocsFaltantes.getColumnModel().getColumn(3).setResizable(false);
            tablaDocsFaltantes.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaDocsFaltantes.getColumnModel().getColumn(4).setResizable(false);
            tablaDocsFaltantes.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        jTabbedPane1.addTab("Faltantes", jScrollPane5);

        tablaDocsVigentes.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaDocsVigentes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tablaDocsVigentes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDocsVigentesMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tablaDocsVigentes);
        if (tablaDocsVigentes.getColumnModel().getColumnCount() > 0) {
            tablaDocsVigentes.getColumnModel().getColumn(1).setPreferredWidth(250);
            tablaDocsVigentes.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaDocsVigentes.getColumnModel().getColumn(3).setResizable(false);
            tablaDocsVigentes.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaDocsVigentes.getColumnModel().getColumn(4).setResizable(false);
            tablaDocsVigentes.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        jTabbedPane1.addTab("Vigentes", jScrollPane6);

        jPanel6.setAutoscrolls(true);
        jPanel6.setDoubleBuffered(false);
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblRuta1.setText(" ");
        lblRuta1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel6.add(lblRuta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(636, 209, -1, -1));

        jTabbedPane1.addTab("Indicadores", jPanel6);

        txtCargo.setEditable(false);
        txtCargo.setToolTipText("Ingresar apellidos completos");
        txtCargo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCargoFocusGained(evt);
            }
        });
        txtCargo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCargoKeyPressed(evt);
            }
        });

        jLabel4.setText("Cargo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCedula, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                            .addComponent(txtNombres, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCargo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
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
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lstDocumentosPorCargo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstDocumentosPorCargo.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstDocumentosPorCargoValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstDocumentosPorCargo);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(200, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 76, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaActionPerformed
        llenarDatos();
    }//GEN-LAST:event_txtCedulaActionPerformed

    private void txtNombresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombresKeyPressed
    }//GEN-LAST:event_txtNombresKeyPressed

    private void txtApellidosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidosKeyPressed
    }//GEN-LAST:event_txtApellidosKeyPressed

    private void txtCedulaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                if (!txtCedula.getText().isEmpty()) {
                    CEmpleados empleado = new CEmpleados(ini, txtCedula.getText());
                    if (empleado.getNombres() == null) {
                        habilitar(true);
                        txtNombres.requestFocus();
                        btnNuevo.setEnabled(false);
                    } else {
                        llenarCamposDeTexto(empleado);
                        habilitar(false);
                        actualizar = true;
                        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
                        btnNuevo.setText("Actualizar");
                    }

                    txtNombres.requestFocus();
                }

            } catch (Exception ex) {
                Logger.getLogger(IngresarDocumentosEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtCedulaKeyPressed

    private void txtCedulaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaFocusGained
        txtCedula.setSelectionStart(0);
        txtCedula.setSelectionEnd(txtCedula.getText().length());
    }//GEN-LAST:event_txtCedulaFocusGained

    private void txtNombresFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombresFocusGained
        txtNombres.setSelectionStart(0);
        txtNombres.setSelectionEnd(txtNombres.getText().length());
    }//GEN-LAST:event_txtNombresFocusGained

    private void txtApellidosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidosFocusGained
        txtApellidos.setSelectionStart(0);
        txtApellidos.setSelectionEnd(txtApellidos.getText().length());
    }//GEN-LAST:event_txtApellidosFocusGained

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        if (actualizar) {

            btnNuevo.setEnabled(false);
            habilitar(true);
            txtCedula.setEditable(false);

        } else {
            limpiar();
            
            switch (user.getTipoAcceso()) {
                case 0:
                    break;
                case 1:

                    break;
                case 2:
                    FBuscarEmpleados form = new FBuscarEmpleados(this);
                    PrincipalAdministrador.escritorio.add(form);
                    form.toFront();
                    form.setClosable(true);
                    form.setVisible(true);
                    form.setTitle("Formulario para buscar Empleados por apellidos");
                    form.txtApellidosPersona.requestFocus();
                    btnNuevo.setEnabled(false);
                    form.show();
                    break;
                case 3:
                    FBuscarEmpleados form1 = new FBuscarEmpleados(this);
                    PrincipalTalentoHumanoAdministrador.escritorio.add(form1);
                    form1.toFront();
                    form1.setClosable(true);
                    form1.setVisible(true);
                    form1.setTitle("Formulario para buscar Empleados por apellidos");
                    form1.txtApellidosPersona.requestFocus();
                    btnNuevo.setEnabled(false);
                    form1.show();
                    break;
                case 4:
                    FBuscarEmpleados form2 = new FBuscarEmpleados(this);
                    PrincipalTalentoHumanoAuxiliar.escritorio.add(form2);
                    form2.toFront();
                    form2.setClosable(true);
                    form2.setVisible(true);
                    form2.setTitle("Formulario para buscar Empleados por apellidos");
                    form2.txtApellidosPersona.requestFocus();
                    btnNuevo.setEnabled(false);
                    form2.show();
                    break;
                default:
                    break;
            }



        }
        //txtCedula.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void agregarImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarImagenMouseClicked
        /*File fileFoto = null;
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
         if (fileFoto != null) {
         ImageIcon filfoto = new ImageIcon(fileFoto.getPath());
         this.foto = fileFoto;
         //panelFotografia.setIcon(filfoto);
         tieneFoto = true;
         /*javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(panelFotografia);
         panelFotografia.setLayout(panelImage1Layout);
         panelImage1Layout.setHorizontalGroup(
         panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 329, Short.MAX_VALUE));
         panelImage1Layout.setVerticalGroup(
         panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 405, Short.MAX_VALUE)); 
         }

         }
         } else {
                
         }
         }**/
    }//GEN-LAST:event_agregarImagenMouseClicked

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        if (!txtCedula.getText().isEmpty()) {
            if (!arrDocumentosPorEmpleado.isEmpty()) {
                int x = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", 0);
                if (x == 0) {
                    boolean grabado = false;
                    for (CDocumentosPorEmpleado obj : arrDocumentosPorEmpleado) {
                        try {
                            if (obj.grabarDocumentosPorEmpleado()) {
                                grabado = true;
                            } else {
                                grabado = false;
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(IngresarDocumentosEmpleados.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (grabado) {
                         new Thread(new HiloGuardarDocumentosPorEmpleado(ini,5,arrDocumentosPorEmpleado)).start();
                        JOptionPane.showMessageDialog(null, "Datos Guardados correctamente", "Guardado", 1, null);

                    } else {
                        JOptionPane.showMessageDialog(null, "Error al guardar los datos", "Error", 1, null);
                    }
                }
            } else {
                JOptionPane.showInternalMessageDialog(this, "No se han agregado documentos al empleado " + empleado.getNombres() + " "
                        + empleado.getApellidos(), "Error al guardar", 2);
            }

        } else {
            JOptionPane.showInternalMessageDialog(this, "No se pueden Guardar Datos con campos vacíos", "Error al guardar", 2);
        }

    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiar();
        habilitar(false);
        btnNuevo.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAgregarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarArchivoActionPerformed
        if (empleado != null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Elija Archivo soporte del Documento");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Escoja el archivo en formato " + documento.getFormato(),
                    documento.getFormato());
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(chooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                archivo = chooser.getSelectedFile();
                if(archivo.length()>1048576){
                   JOptionPane.showMessageDialog(null, "Archivo es muy grande, debe tener menos de un mega","Alerta",1);
                   archivo=null;
                }else{
                   txtRutaDelArchivo.setText(archivo.getPath());
                   btnAgregarRegistro.setEnabled(true); 
                }
                

            }
        } else {
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregarArchivoActionPerformed

    private void tablaIngresoDocumentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaIngresoDocumentosMouseClicked
        // TODO add your handling code here:

        int row = tablaIngresoDocumentos.getSelectedRow();
        // this.cedula=(String.valueOf(jTable1.getValueAt(row, 0)));
    }//GEN-LAST:event_tablaIngresoDocumentosMouseClicked

    private void btnAgregarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarRegistroActionPerformed
        if (validarTabla()) {
            try {
                int fila = tablaIngresoDocumentos.getRowCount();
                modelo1.addRow(new Object[tablaIngresoDocumentos.getRowCount()]);
                tablaIngresoDocumentos.setValueAt(tablaIngresoDocumentos.getRowCount(), fila, 0);
                tablaIngresoDocumentos.setValueAt(txtNombreDocumento.getText(), fila, 1);
                Date fechaExp = new Date();
                fechaExp = ini.getFechaSql(dateExpedicion);
                tablaIngresoDocumentos.setValueAt(fechaExp, fila, 2);
                Date fechaVence = new Date();
                fechaVence = ini.getFechaSql(dateVencimiento);
                boolean vence = false;
                if (documento.getTieneVencimiento() == 1) {
                    tablaIngresoDocumentos.setValueAt(fechaVence, fila, 3);
                    vence = true;
                } else {
                    fechaVence = null;
                    tablaIngresoDocumentos.setValueAt(" ", fila, 3);
                }
                tablaIngresoDocumentos.setValueAt(true, fila, 4);
                btnAgregarArchivo.setEnabled(false);
                btnAgregarRegistro.setEnabled(false);
                dateExpedicion.setEnabled(false);
                dateVencimiento.setEnabled(false);
                CDocumentosPorEmpleado docsPorEmpleado = new CDocumentosPorEmpleado(ini);
                docsPorEmpleado.setEmpleado(empleado);
                docsPorEmpleado.setCargo(empleado.getIdCargo());
                docsPorEmpleado.setDocumento(documento);
                docsPorEmpleado.setNumeroDocumento(txtNumeroDocumento.getText());
                docsPorEmpleado.setEntidadEmisora(txtEntidadEmisora.getText());
                docsPorEmpleado.setFechaExpedicion(fechaExp);
                docsPorEmpleado.setLugarExpedicion(txtLugarDeExpedicion.getText());
                docsPorEmpleado.setTieneVencimiento(vence);
                docsPorEmpleado.setFechaVencimiento(fechaVence);
                docsPorEmpleado.setArchivoSoporteDocumento(archivo);
                //docsPorEmpleado.setUsuar(user.getNombreUsuario());
                int dot = archivo.getAbsolutePath().lastIndexOf(".");
                docsPorEmpleado.setFormatoArchivo(archivo.getAbsolutePath().substring(dot + 1));
                arrDocumentosPorEmpleado.add(docsPorEmpleado);
                archivo = null;
            } catch (Exception e) {
            }

        } else {
            JOptionPane.showMessageDialog(null, mensaje);
        }

    }//GEN-LAST:event_btnAgregarRegistroActionPerformed

    private void tablaDocsVencidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsVencidosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDocsVencidosMouseClicked

    private void tablaDocsFaltantesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsFaltantesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDocsFaltantesMouseClicked

    private void tablaDocsVigentesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsVigentesMouseClicked
        try {
            int row = tablaDocsVigentes.getSelectedRow();

            CDocumentos doc = new CDocumentos(ini, tablaDocsVigentes.getValueAt(row, 1).toString());

            CDocumentosPorEmpleado dxp = new CDocumentosPorEmpleado(empleado, doc);
            File file;
            file = dxp.getArchivoSoporteDocumento();
            Desktop.getDesktop().open(file);        // TODO add your handling code here:
        } catch (IOException ex) {
            // Logger.getLogger(FAgregarDocumentos1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            //Logger.getLogger(FAgregarDocumentos1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tablaDocsVigentesMouseClicked

    private void txtCargoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCargoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCargoFocusGained

    private void txtCargoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCargoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCargoKeyPressed

    private void lstDocumentosPorCargoValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstDocumentosPorCargoValueChanged
        indiceLista = 0;
        int contador = 0; // contador para verificar que el elemento no exista en la tabla(=0) sí existe (>=1)
        for (int i = 0; i < tablaIngresoDocumentos.getRowCount(); i++) {
            String str = tablaIngresoDocumentos.getValueAt(i, 1).toString();
            if (lstDocumentosPorCargo.getSelectedValue().toString().equals(tablaIngresoDocumentos.getValueAt(i, 1).toString())) {
                contador++;
                JOptionPane.showMessageDialog(null, "Elemento ya ingresado en la tabla", "Valor duplicado", 1);
            }
        }
        txtNombreDocumento.setEditable(false);
        if (contador == 0) {
            limpiarDocumentoAgregado();
            int i = 0;
            for (CDocumentos obj : arrTodosDocumentosPendientes) {
                String str = lstDocumentosPorCargo.getSelectedValue().toString();
                //String str2=obj.getNombreTipoDocumento();
                if (obj.getNombreTipoDocumento().equals(str)) {
                    txtNombreDocumento.setText(obj.getNombreTipoDocumento());
                    documento = obj;
                    txtEntidadEmisora.setEditable(true);
                    txtNumeroDocumento.setEditable(true);
                    txtEntidadEmisora.setText("");
                    lblRuta.setText("");
                    txtNumeroDocumento.setText("");
                    btnAgregarArchivo.setEnabled(true);
                    dateExpedicion.setEnabled(true);
                    if (documento.getTieneVencimiento() == 1) {
                        dateVencimiento.setEnabled(true);
                        //dateVencimiento.setVisible(true);
                        // lblVencimimento.setVisible(true);
                    } else {
                        dateVencimiento.setEnabled(false);
                        // dateVencimiento.setVisible(false);
                        // lblVencimimento.setVisible(false);
                    }
                    indiceLista = i;
                }

                i++;
            }
            habilitar(true);
            txtEntidadEmisora.requestFocus();

        }
        contador = 0;

    }//GEN-LAST:event_lstDocumentosPorCargoValueChanged

    private void txtNombreDocumentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDocumentoFocusGained
        txtNombreDocumento.setSelectionStart(0);
        txtNombreDocumento.setSelectionEnd(txtNombreDocumento.getText().length());
    }//GEN-LAST:event_txtNombreDocumentoFocusGained

    private void txtEntidadEmisoraFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEntidadEmisoraFocusGained
        txtEntidadEmisora.setSelectionStart(0);
        txtEntidadEmisora.setSelectionEnd(txtEntidadEmisora.getText().length());
    }//GEN-LAST:event_txtEntidadEmisoraFocusGained

    private void txtNumeroDocumentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDocumentoFocusGained
        txtNumeroDocumento.setSelectionStart(0);
        txtNumeroDocumento.setSelectionEnd(txtNumeroDocumento.getText().length());
    }//GEN-LAST:event_txtNumeroDocumentoFocusGained

    private void txtLugarDeExpedicionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLugarDeExpedicionFocusGained
        txtLugarDeExpedicion.setSelectionStart(0);
        txtLugarDeExpedicion.setSelectionEnd(txtLugarDeExpedicion.getText().length());
    }//GEN-LAST:event_txtLugarDeExpedicionFocusGained

    private void txtRutaDelArchivoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRutaDelArchivoFocusGained
        txtRutaDelArchivo.setSelectionStart(0);
        txtRutaDelArchivo.setSelectionEnd(txtRutaDelArchivo.getText().length());
    }//GEN-LAST:event_txtRutaDelArchivoFocusGained

    private void txtNombreDocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDocumentoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtEntidadEmisora.requestFocus();
        }
    }//GEN-LAST:event_txtNombreDocumentoKeyPressed

    private void txtEntidadEmisoraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntidadEmisoraKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNumeroDocumento.requestFocus();
        }
    }//GEN-LAST:event_txtEntidadEmisoraKeyPressed

    private void txtNumeroDocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDocumentoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtLugarDeExpedicion.requestFocus();
        }
    }//GEN-LAST:event_txtNumeroDocumentoKeyPressed

    private void txtLugarDeExpedicionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLugarDeExpedicionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //
        }
    }//GEN-LAST:event_txtLugarDeExpedicionKeyPressed

    private void dateExpedicionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateExpedicionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateExpedicionKeyPressed

    private void dateExpedicionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateExpedicionFocusGained
        dateExpedicion.setSize(87, 21);
    }//GEN-LAST:event_dateExpedicionFocusGained

    private void dateVencimientoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateVencimientoFocusGained
        dateVencimiento.setSize(87, 21);
    }//GEN-LAST:event_dateVencimientoFocusGained

    private void borraUnaFilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borraUnaFilaActionPerformed
    }//GEN-LAST:event_borraUnaFilaActionPerformed

    private void borraUnaFilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borraUnaFilaMouseClicked
        int x;
        if (tablaIngresoDocumentos.getRowCount() > 0) {
            int fila = tablaIngresoDocumentos.getSelectedRow();
            x = JOptionPane.showOptionDialog(null, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No", "Cancelar"}, "No");
            if (x == 0) {
                modelo1.removeRow(fila);
                String doc;
                arrDocumentosPorEmpleado.remove(fila);
                for (int i = 0; i < tablaIngresoDocumentos.getRowCount(); i++) {
                    tablaIngresoDocumentos.setValueAt(i + 1, i, 0);
                }
            }
        } else {
            JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
        }
    }//GEN-LAST:event_borraUnaFilaMouseClicked

    private void dateVencimientoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateVencimientoMouseClicked
        dateVencimiento.setSize(87, 21);

    }//GEN-LAST:event_dateVencimientoMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JMenu borraUnaFila;
    private javax.swing.JPopupMenu borrarFila;
    private javax.swing.JButton btnAgregarArchivo;
    private javax.swing.JButton btnAgregarRegistro;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private com.toedter.calendar.JDateChooser dateExpedicion;
    private com.toedter.calendar.JDateChooser dateVencimiento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    public javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    public javax.swing.JTabbedPane jTabbedPane1;
    private org.edisoncor.gui.label.LabelCustom lblRuta;
    private org.edisoncor.gui.label.LabelCustom lblRuta1;
    private javax.swing.JLabel lblVencimimento;
    private javax.swing.JList lstDocumentosPorCargo;
    private javax.swing.JPopupMenu pumImagen;
    private javax.swing.JTable tablaDocsFaltantes;
    private javax.swing.JTable tablaDocsVencidos;
    private javax.swing.JTable tablaDocsVigentes;
    private javax.swing.JTable tablaIngresoDocumentos;
    private javax.swing.JTextField txtApellidos;
    public javax.swing.JTextField txtCargo;
    public javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtEntidadEmisora;
    private javax.swing.JTextField txtLugarDeExpedicion;
    private javax.swing.JTextField txtNombreDocumento;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtNumeroDocumento;
    private javax.swing.JTextField txtRutaDelArchivo;
    // End of variables declaration//GEN-END:variables

    public void llenarCamposDeTexto(CEmpleados empleado) {
        try {
            txtNombres.setText(empleado.getNombres());
            txtApellidos.setText(empleado.getApellidos());
        } catch (Exception ex) {
        }
    }

    private void habilitar(boolean valor) {
        try {
            jTabbedPane1.setEnabled(valor);

            txtEntidadEmisora.setEditable(valor);
            txtNumeroDocumento.setEditable(valor);
            txtLugarDeExpedicion.setEditable(valor);
            dateExpedicion.setEnabled(valor);
            //dateVencimiento.setEnabled(valor);
            btnAgregarArchivo.setEnabled(valor);
            btnAgregarRegistro.setEnabled(valor);
            tablaIngresoDocumentos.setEnabled(valor);
            tablaDocsVencidos.setEnabled(valor);
            tablaDocsFaltantes.setEnabled(valor);
            tablaDocsVigentes.setEnabled(valor);
        } catch (Exception ex) {
        }
    }

    public boolean validar() {
        boolean verificado = true;
        mensaje = "";
        if (txtNombres.getText().isEmpty()) {
            mensaje += "No ha colocado el nombre del empleado" + "  \n";
            verificado = false;
        }
        if (txtApellidos.getText().isEmpty()) {
            mensaje += "No ha colocado los apellidos del empleado" + "  \n";
            verificado = false;
        }


        return verificado;
    }

    private boolean validarTabla() {
        boolean validado = true;
        mensaje = "";
        if (archivo == null) {
            mensaje += "No ha escogido un archivo de soporte del documento actual.  \n";
            validado = false;
        }
        if (txtNumeroDocumento.getText().isEmpty()) {
            mensaje += "No ha introducido el número del documento. \n ";
            validado = false;
        }
        if (txtEntidadEmisora.getText().isEmpty()) {
            mensaje += "No ha introducido la entidad que genera el documento. \n";
            validado = false;
        }
        if (txtLugarDeExpedicion.getText().isEmpty()) {
            mensaje += "No ha introducido donde fué expedido el documento. \n";
            validado = false;
        }

        return validado;
    }

    /* public boolean guardarRegistrosDocumentos() {
     boolean guardado = false;
     try {

     CEmpleados empleado = new CEmpleados(usuario);
     empleado.setCedula(txtCedula.getText().trim());
     empleado.setNombres(txtNombres.getText().trim());
     empleado.setApellidos(txtApellidos.getText().trim());
           
            

     guardado = empleado.grabarEmpleados();


     } catch (Exception ex) {
     Logger.getLogger(IngresarDocumentosEmpleados.class.getName()).log(Level.SEVERE, null, ex);
     }
     return guardado;
     } **/
    private void limpiar() {

        try {
            txtCedula.setText("");
            txtNombres.setText("");
            txtApellidos.setText("");
            txtCargo.setText("");
            DefaultListModel model = new DefaultListModel();
            lstDocumentosPorCargo.setModel(model);
            DefaultTableModel modelo = (DefaultTableModel) tablaDocsFaltantes.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

            modelo = (DefaultTableModel) tablaDocsVencidos.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

            modelo = (DefaultTableModel) tablaDocsVigentes.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }
            btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); //
            btnNuevo.setText("Nuevo");
            btnNuevo.setEnabled(true);
            jPanel6.removeAll();
        } catch (Exception ex) {
        }

    }

    public void llenarDatos() {
        Date fechaActual = new Date();
        arrDocumentosVigentes = new ArrayList<>();
        arrDocumentosVencidos = new ArrayList<>();
        arrDocumentosFaltantes = new ArrayList<>();
        //this.cargo = empleado.getCargo(); // se define el cargo que ocupa actualmente el empleado
//        docsPorCargo = new CDocumentosPorCargo(ini,empleado.getCargo());
        docsPorEmpleado = new CDocumentosPorEmpleado(ini);              // se define los documentos que actualmente tiene el empleado
//        arrDocumentosFaltantes = docsPorCargo.getDocumentos(empleado.getCargo(), empleado, false); // trae todos los documentos que  le faltan al empleado 
        ArrayList<CDocumentos> docsVigentes = new ArrayList();
        docsVigentes = docsPorEmpleado.getDocumentos(empleado);
        for (CDocumentos obj : docsVigentes) {
            /* primero verifica si el documento tiene vencimiento
             * si tiene vencimienton pregunta sí está vencido
             *   si está vencido incorpora el registro a un array de documentos vencidos
             *   sino esta vencido lo incorpora a un array de documentos vigentes
             * sino tiene vencimiento lo anexa a un array de documentos vigentes
             */

            if (obj.getTieneVencimiento() == 1) { // verifica si el documento  tiene vencimiento el documento
                CDocumentosPorEmpleado dxe = new CDocumentosPorEmpleado(empleado, obj);
                if (dxe.getFechaVencimiento().before(fechaActual)) { // verifica si está venmcido
                    arrDocumentosVencidos.add(dxe);   // lo incorpora al array de documentos vencidos
                } else {                         // no tiene vencimiento el documento
                    arrDocumentosVigentes.add(dxe);//lo incorpora al array de documentos vigentes
                }
            } else {  // este es el caso en que el documentos no tiene vencimiento
                CDocumentosPorEmpleado dxe = new CDocumentosPorEmpleado(empleado, obj);
                arrDocumentosVigentes.add(dxe); //lo incorpora al array de documentos vigentes
            }

        }
        Iterator i = arrDocumentosFaltantes.iterator();
        // DefaultListModel modelLista = new DefaultListModel();
        //modelLista.removeAllElements();
        arrTodosDocumentosPendientes = new ArrayList();
        for (CDocumentos obj : arrDocumentosFaltantes) {
            arrTodosDocumentosPendientes.add(obj);
        }
        for (CDocumentosPorEmpleado obj : arrDocumentosVencidos) {
            arrTodosDocumentosPendientes.add(obj.getDocumento());
        }
        Iterator it = arrTodosDocumentosPendientes.iterator();
        DefaultListModel modelLista = new DefaultListModel();
        modelLista.removeAllElements();



        for (CDocumentos obj : arrTodosDocumentosPendientes) {
            if (it.hasNext()) {
                modelLista.addElement(obj.getNombreTipoDocumento());
                it.next();
            }
        }



        lstDocumentosPorCargo.setModel(modelLista);
        llenarTablaDocumentosVigentes();
        llenarTablaDocumentosFaltantes();
        llenarTablaDocumentosVencidos();
//        cargo = empleado.getCargo();
        txtCargo.setText(cargo.getNombreCargo());
        modelo1 = (DefaultTableModel) tablaIngresoDocumentos.getModel();
        arrDocumentosPorEmpleado = new ArrayList<>();
        arrDocumentosEmpleado = new ArrayList<>();
        ChartPieDocumentosPorEmpleado chrP1 = new ChartPieDocumentosPorEmpleado("Gráfico Gestión de Documentos del empleado "
                + empleado.getNombres() + " " + empleado.getApellidos(), this);
        tablaDocsVigentes.setEnabled(true);

    }

    private void limpiarDocumentoAgregado() {
        txtNombreDocumento.setText("");
        txtEntidadEmisora.setText("");
        txtNumeroDocumento.setText("");
        txtLugarDeExpedicion.setText("");
        txtRutaDelArchivo.setText("");
        dateExpedicion.setDate(new Date());
        dateVencimiento.setDate(new Date());
    }

    private void llenarTablaDocumentosVigentes() {
        DefaultTableModel modelo = new DefaultTableModel();
        int fil = 0;
        modelo = (DefaultTableModel) tablaDocsVigentes.getModel();
        for (CDocumentosPorEmpleado obj : arrDocumentosVigentes) {
            modelo.addRow(new Object[fil]);
            tablaDocsVigentes.setValueAt(fil + 1, fil, 0);
            tablaDocsVigentes.setValueAt(obj.getDocumento().getNombreTipoDocumento().toString(), fil, 1);
            tablaDocsVigentes.setValueAt(obj.getFechaExpedicion(), fil, 2);
            if (obj.getDocumento().getTieneVencimiento() == 1) {
                tablaDocsVigentes.setValueAt(obj.getFechaVencimiento(), fil, 3);
            } else {
                tablaDocsVigentes.setValueAt(obj.getFechaVencimiento(), fil, 3);
            }
            tablaDocsVigentes.setValueAt(true, fil, 4);
            fil++;

        }
        tablaDocsVigentes.setEnabled(true);
    }

    private void llenarTablaDocumentosVencidos() {
        DefaultTableModel modelo = new DefaultTableModel();
        int fil = 0;
        modelo = (DefaultTableModel) tablaDocsVencidos.getModel();
        for (CDocumentosPorEmpleado obj : arrDocumentosVencidos) {
            modelo.addRow(new Object[fil]);
            tablaDocsVencidos.setValueAt(fil + 1, fil, 0);
            tablaDocsVencidos.setValueAt(obj.getDocumento().getNombreTipoDocumento().toString(), fil, 1);
            tablaDocsVencidos.setValueAt(obj.getFechaExpedicion(), fil, 2);
            if (obj.getDocumento().getTieneVencimiento() == 1) {
                tablaDocsVencidos.setValueAt(obj.getFechaVencimiento(), fil, 3);
            } else {
                tablaDocsVencidos.setValueAt(obj.getFechaVencimiento(), fil, 3);
            }
            tablaDocsVencidos.setValueAt(true, fil, 4);

            fil++;

        }
    }

    private void llenarTablaDocumentosFaltantes() {
        DefaultTableModel modelo = new DefaultTableModel();
        int fil = 0;
        modelo = (DefaultTableModel) tablaDocsFaltantes.getModel();
        for (CDocumentos obj : arrDocumentosFaltantes) {
            modelo.addRow(new Object[fil]);
            tablaDocsFaltantes.setValueAt(fil + 1, fil, 0);
            tablaDocsFaltantes.setValueAt(obj.getNombreTipoDocumento(), fil, 1);
            tablaDocsFaltantes.setValueAt(" ", fil, 2);
            tablaDocsFaltantes.setValueAt(" ", fil, 3);
            fil++;

        }
    }
}
