/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera.consultas;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.Imprimir.RepporteRuteroFacturasPorConductor;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.Threads.HiloFConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FConsultarPedidosConductorHielera extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    public CEmpleados conductor;
    CUsuarios user;

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
    int kilometrosEntrada;
    int kilometrosRecorridos;

    int filaTabla2;
    int indiceLista = 0;
    int columna = 0;
    String mensaje = null;
    TextAutoCompleter autoTxtConductores;

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
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param ini
     */
    public FConsultarPedidosConductorHielera(Inicio ini) throws Exception {
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
            Logger.getLogger(FConsultarPedidosConductorHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

        autoTxtConductores = new TextAutoCompleter(txtNombreConductor);

        for (CEmpleados conductor : this.ini.getListaDeEmpleados()) {
            if (conductor.getEmpleadoActivo() == 1) {
                autoTxtConductores.addItem(conductor.getNombres() + " " + conductor.getApellidos());
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the
     * fCambiarClave. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
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
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnExportar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnCancelar = new javax.swing.JToggleButton();
        jBtnSalir = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtNombreConductor = new javax.swing.JTextField();
        txtCedulaConductor = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        dateManifiesto = new com.toedter.calendar.JDateChooser();
        jLabel39 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnExportar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblFacturasPorManifiesto = new javax.swing.JTable();
        barra = new javax.swing.JProgressBar();

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
        setTitle("Formulario para consulta de los  manifiestos de Distribución");
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

        jBtnExportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        jBtnExportar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExportar.setEnabled(false);
        jBtnExportar.setFocusable(false);
        jBtnExportar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExportar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jBtnExportar);

        jBtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jBtnImprimir.setToolTipText("Imprimir");
        jBtnImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnImprimir.setEnabled(false);
        jBtnImprimir.setFocusable(false);
        jBtnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jBtnImprimir);

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

        jBtnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancelar.setToolTipText("Imprimir");
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

        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jBtnGrabar.setToolTipText("Grabar");
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

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Formulario de consulta de Manifiesto"));

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

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Nombre Cond.");

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        dateManifiesto.setToolTipText("Seleccionar la fecha ");
        dateManifiesto.setDateFormatString("yyyy/MM/dd");
        dateManifiesto.setEnabled(false);
        dateManifiesto.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        dateManifiesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dateManifiestoKeyPressed(evt);
            }
        });

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Fecha");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedulaConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(lblCirculoDeProgreso)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedulaConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        btnExportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/exportarExcel.jpg"))); // NOI18N
        btnExportar.setText("Exportar");
        btnExportar.setToolTipText("Guardar registro nuevo ó modificado");
        btnExportar.setEnabled(false);
        btnExportar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExportar.setPreferredSize(new java.awt.Dimension(97, 97));
        btnExportar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarActionPerformed(evt);
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
                .addComponent(btnExportar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
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
                    .addComponent(btnExportar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 1, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblFacturasPorManifiesto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Cliente.", "valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFacturasPorManifiesto.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblFacturasPorManifiesto.setColumnSelectionAllowed(true);
        tblFacturasPorManifiesto.setComponentPopupMenu(borrarFila);
        tblFacturasPorManifiesto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFacturasPorManifiestoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblFacturasPorManifiesto);
        tblFacturasPorManifiesto.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tblFacturasPorManifiesto.getColumnModel().getColumnCount() > 0) {
            tblFacturasPorManifiesto.getColumnModel().getColumn(0).setResizable(false);
            tblFacturasPorManifiesto.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblFacturasPorManifiesto.getColumnModel().getColumn(1).setResizable(false);
            tblFacturasPorManifiesto.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblFacturasPorManifiesto.getColumnModel().getColumn(2).setResizable(false);
            tblFacturasPorManifiesto.getColumnModel().getColumn(2).setPreferredWidth(250);
            tblFacturasPorManifiesto.getColumnModel().getColumn(3).setPreferredWidth(120);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                .addGap(1, 1, 1))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 1076, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(487, 487, 487))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 23, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 23, Short.MAX_VALUE)))
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

    public void consultarManifiesto() {
//        try {
//
//            // manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()), "CURRENT_DATE");
////            manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()));
//            // se valida que el manifiesto exista
//            if (manifiestoActual.getVehiculo() == null) { // si  no existe el manifiesto
//
//                JOptionPane.showInternalMessageDialog(this, "Ese manifiesto no Existe en la BB DD ", "Error", JOptionPane.WARNING_MESSAGE);
//                txtNombreConductor.requestFocus();
//                txtNombreConductor.requestFocus();
//            } else { //  si existe el vehículo, existe el manifiesto
//
//                manifiestoActual.setListaFacturasPorManifiesto();
//                manifiestoActual.setListaDeAuxiliares();
//
//                txtNombreConductor.setEditable(false);
//                CCarros car = new CCarros(ini, manifiestoActual.getVehiculo());
//                txtPlaca.setText(manifiestoActual.getVehiculo());
//                txtCedulaConductor.setText(manifiestoActual.getConductor());
//
//                for (CEmpleados obj : ini.getListaDeEmpleados()) {
//                    if (obj.getCedula().equals(manifiestoActual.getConductor())) {
//                        txtNombreConductor.setText(obj.getNombres() + " " + obj.getApellidos());
//                        break;
//                    }
//                }
//
//                //txtCodigoManifiesto.setText(manifiestoActual.codificarManifiesto());
//            }
//
//        } catch (Exception ex) {
//            System.out.println("Error en txtManifiestoKeyPressed ");
//            Logger.getLogger(FConsultarPedidosConductorHielera.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }


    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        limpiar();

    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
//        if (listaDeFacturasTrasladadas.isEmpty()) {
//            JOptionPane.showInternalMessageDialog(this, "No hay datos para trasladar al Manifiesto # " + manifiestoDestino.getNumeroManifiesto(), "! Alerta !", JOptionPane.WARNING_MESSAGE);
//        } else {
//            if (grabarTrasladoDeFacturas()) {
//
//                return;
//            }
//        }
    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jBtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelarActionPerformed
        limpiar();
    }//GEN-LAST:event_jBtnCancelarActionPerformed

    private void jBtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSalirActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_jBtnSalirActionPerformed

    private void jBtnMinutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnMinutaActionPerformed

        
         new Thread(new HiloFConsultarPedidosConductorHielera(ini, this, "sacarMinuta")).start();    

    }//GEN-LAST:event_jBtnMinutaActionPerformed

    private void tblFacturasPorManifiestoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFacturasPorManifiestoMouseClicked
        // TODO add your handling code here:

        int row = tblFacturasPorManifiesto.getSelectedRow();
        // this.cedula=(String.valueOf(jTable1.getValueAt(row, 0)));
    }//GEN-LAST:event_tblFacturasPorManifiestoMouseClicked

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        try {

            // RepporteRuteroConductores demo = new RepporteRuteroConductores(ini, manifiestoActual);
            RepporteRuteroFacturasPorConductor demo = new RepporteRuteroFacturasPorConductor(ini, conductor);

        } catch (Exception ex) {
            Logger.getLogger(FConsultarPedidosConductorHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarActionPerformed

        // new Thread(new HiloListadoDeFacturasPorManifiesto(ini, this, true)).start();
        new Thread(new HiloFConsultarPedidosConductorHielera(ini, this, "exportarExcel")).start();
    }//GEN-LAST:event_btnExportarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        limpiar();
        nuevo = true;
        dateManifiesto.setDate(new Date());
        dateManifiesto.setEnabled(true);
        txtNombreConductor.setEnabled(true);
        txtNombreConductor.setEditable(true);
        btnNuevo.setEnabled(false);
        txtNombreConductor.requestFocus();
        jBtnMinuta.setEnabled(false);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiar();
        conductor = null;
        //        txtCedulaConductor.setEnabled(false);
        //        txtnombreDeConductor.setEditable(false);
        //        txtCedulaAuxiliarDeReparto.setEnabled(false);
        //        txtNombreDeAuxiliar.setEditable(false);
        //        txtKmDeSalida.setEditable(false);
        txtNombreConductor.setEnabled(false);
        txtNombreConductor.setEditable(false);

        //        cbxRutaDeDistribucion.setEnabled(false);
        //        btnCrearManifiesto.setEnabled(false);
        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);
        jBtnImprimir.setEnabled(false);
        btnImprimir.setEnabled(false);
        btnExportar.setEnabled(false);
        jBtnExportar.setEnabled(false);
        
        jBtnMinuta.setEnabled(false);
        
        grabar = false;
        btnNuevo.setEnabled(true);
        btnNuevo.requestFocus();
       

        //        manifiestoModificadoPorMi.setIsFree(1);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed

        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void dateManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateManifiestoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // consultarConductor();
            new Thread(new HiloFConsultarPedidosConductorHielera(ini, this, "consultarConductor")).start();
        }
    }//GEN-LAST:event_dateManifiestoKeyPressed

    private void txtCedulaConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaConductorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorKeyPressed

    private void txtCedulaConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorActionPerformed

    private void txtCedulaConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaConductorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorFocusGained

    private void txtNombreConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreConductorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // consultarConductor();
            new Thread(new HiloFConsultarPedidosConductorHielera(ini, this, "consultarConductor")).start();

        }
    }//GEN-LAST:event_txtNombreConductorKeyPressed

    private void txtNombreConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorActionPerformed

    private void txtNombreConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreConductorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorFocusGained

    private void txtPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaKeyPressed

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

//    private void llenarTabla() throws Exception {
//        modelo2 = (DefaultTableModel) tblFacturasPorManifiesto.getModel();
//        txtManifiesto.setEditable(false);
//        for (CFacturasPorManifiesto obj : arrFactPorMfto) {
//            filaTabla2 = tblFacturasPorManifiesto.getRowCount();
//
//            modelo2.addRow(new Object[tblFacturasPorManifiesto.getRowCount()]);
//            tblFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
//            tblFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de factura
//            CFacturasCamdun fac = new CFacturasCamdun(ini, obj.getNumeroFactura());
//            CClientesCamdun cliente = new CClientesCamdun(ini, fac.getCodigoDeCliente());
//            tblFacturasPorManifiesto.setValueAt(cliente.getNombreDeCliente(), filaTabla2, 2); // nombre del cliente
//            tblFacturasPorManifiesto.setValueAt(nf.format(fac.getValorTotalFactura()), filaTabla2, 3);
//
//            valorTotalAConsignar += fac.getValorTotalFactura();
//            lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));
//        }
//
//        kilometrosRecorridos = manifiestoActual.getKmEntrada() - manifiestoActual.getKmSalida();
//
//        txtManifiesto.setEditable(false);
//        txtManifiesto.setEnabled(false);
//        btnExportar.setEnabled(true);
//        btnImprimir.setEnabled(true);
//
//    }

    private void txtPlacaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    public javax.swing.JProgressBar barra;
    private javax.swing.JMenu borraUnaFila;
    private javax.swing.JPopupMenu borrarFila;
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnExportar;
    public javax.swing.JButton btnImprimir;
    public javax.swing.JButton btnNuevo;
    public javax.swing.JButton btnSalir;
    public com.toedter.calendar.JDateChooser dateManifiesto;
    private javax.swing.JToggleButton jBtnCancelar;
    public javax.swing.JToggleButton jBtnExportar;
    private javax.swing.JButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnImprimir;
    public javax.swing.JToggleButton jBtnMinuta;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JButton jBtnSalir;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    private javax.swing.JPopupMenu pumImagen;
    private javax.swing.JTable tablaDocsVencidos;
    public javax.swing.JTable tblFacturasPorManifiesto;
    public javax.swing.JTextField txtCedulaConductor;
    public javax.swing.JTextField txtNombreConductor;
    public javax.swing.JTextField txtPlaca;
    // End of variables declaration//GEN-END:variables

    private void limpiar() {
        try {
            
            lblCirculoDeProgreso.setVisible(false);
            barra.setValue(0);

            txtCedulaConductor.setEnabled(true);
            txtCedulaConductor.setEditable(false);

            txtNombreConductor.setEnabled(true);
            txtNombreConductor.setEditable(false);

            txtPlaca.setEnabled(true);
            txtPlaca.setEditable(false);

            txtNombreConductor.setText("");
            txtPlaca.setText("");
            txtCedulaConductor.setText("");
            txtNombreConductor.setText("");
            valorTotalAConsignar = 0.0;
            barra.setValue(0);

            DefaultTableModel modelo = (DefaultTableModel) tblFacturasPorManifiesto.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

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
            if (txtNombreConductor.getText().isEmpty()) {
                mensaje += "No ha selecccionado el Vehiculo de la Ruta" + "  \n";
                verificado = false;
            }

        } catch (SQLException ex) {
            System.out.println("Error en validarManifiesto ");
            Logger.getLogger(FConsultarPedidosConductorHielera.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificado;
    }

    private void llenarDatosManifiesto(CManifiestosDeDistribucion man) throws Exception {
//        for (CEmpleados obj : arrEmpleados) {
//            if (manifiestoActual.getConductor().equals(obj.getCedula())) {
//                txtCedulaConductor.setText(obj.getCedula());
//                txtnombreDeConductor.setText(obj.getNombres() + " " + obj.getApellidos());
//            }
//        }
//        if (manifiestoActual.getAuxiliarDeReparto1().equals("0")) {
////            txtCedulaAuxiliarDeReparto.setText("0");
////            txtNombreDeAuxiliar.setText("SALE A DISTRIBUCION SIN AUXILIAR");
//        } else {
////            for (CEmpleados obj : arrEmpleados) {
////                if (manifiestoActual.getAuxiliarDeReparto().equals(obj.getCedula())) {
////                    txtCedulaAuxiliarDeReparto.setText(obj.getCedula());
////                    txtNombreDeAuxiliar.setText(obj.getNombres() + " " + obj.getApellidos());
//            //             }
//
//            //      }
//        }
//        for (CCanales obj : arrCanales) {
//            if (obj.getIdCanal() == manifiestoActual.getIdCanal()) {
//                cbxCanales.setSelectedItem(obj.getNombreCanal());
//                this.canal = manifiestoActual.getIdCanal();
//            }
//        }

//        for (CRutasDeDistribucion obj : arrRutas) {
//            if (obj.getIdRutasDeDistribucion() == manifiestoActual.getIdRuta()) {
//                cbxRutaDeDistribucion.setSelectedItem(obj.getNombreRutasDeDistribucion());
//            }
//        }
//        txtKmDeSalida.setText("" + manifiestoActual.getKmSalida());
//        txtNumeroDeManifiesto.setText(codificarManifiesto(manifiestoActual));
//        manifiestoActual.setIsFree(0);
//        manifiestoModificadoPorMi = manifiestoActual;
//        jLabel7.setText("Total a recaudar en Manifiesto # " + txtNumeroDeManifiesto.getText().trim());
//        lblValorRecaudoManifiesto.setText(nf.format(manifiestoActual.getValorTotalManifiesto()));
//        valorTotalAConsignar = 0.0;
//        modelo2 = (DefaultTableModel) tblFacturasPorVehiculo.getModel();
//        arrFactPorMfto = new ArrayList<>();
//        for (CFacturasCamdun obj : arrFacturas) {
//            CFacturasPorManifiesto TMPfxm = new CFacturasPorManifiesto(ini);
//            filaTabla2 = tblFacturasPorVehiculo.getRowCount();
//            modelo2.addRow(new Object[tblFacturasPorVehiculo.getRowCount()]);
//
//            tblFacturasPorVehiculo.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
//            tblFacturasPorVehiculo.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura
//            CClientesCamdun cli = new CClientesCamdun(ini, obj.getCodigoDeCliente());
//            tblFacturasPorVehiculo.setValueAt(cli.getNombreDeCliente(), filaTabla2, 2); // cliente
//            tblFacturasPorVehiculo.setValueAt(nf.format(obj.getValorTotalFactura()), filaTabla2, 3); // valor de la factura
//            valorTotalAConsignar = valorTotalAConsignar + obj.getValorTotalFactura();
//            lblValorRecaudoManifiesto.setText(nf.format(valorTotalAConsignar));
//
//            TMPfxm.setNumeroManifiesto(manifiestoActual.getNumeroManifiesto());
//            TMPfxm.setNumeroFactura(obj.getNumeroFactura());
//            TMPfxm.setAdherencia(filaTabla2 + 1);
//            TMPfxm.setValorFactura(obj.getValorTotalFactura());
//            arrFactPorMfto.add(TMPfxm);
//        }
    }

    private void ordenarTabla() {
        for (int i = 0; i < tblFacturasPorManifiesto.getRowCount(); i++) {
            tblFacturasPorManifiesto.setValueAt(i + 1, i, 0);
        }
    }

    public synchronized void llenarDatosManifiestoCerrado() throws Exception {
        double valorTotalManifiesto = 0.0;

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        txtCedulaConductor.setEnabled(true);
        txtCedulaConductor.setEditable(false);
        txtNombreConductor.setEnabled(true);
        txtNombreConductor.setEditable(false);
        txtPlaca.setEnabled(true);
        txtPlaca.setEditable(false);

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

        modelo2 = (DefaultTableModel) tblFacturasPorManifiesto.getModel();

        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : conductor.getListaDeFacturaPorConductor()) {

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            filaTabla2 = tblFacturasPorManifiesto.getRowCount();
            modelo2.addRow(new Object[tblFacturasPorManifiesto.getRowCount()]);

            tblFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            tblFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            tblFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            tblFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   
            valorTotalManifiesto += obj.getValorARecaudarFactura();

            // se ubica en la fila insertada
            tblFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);

            cantidadFacturasEnManifiesto++;

            // }
            // }
            //this.repaint();
        }

        /* Se llenan os campos de texto de los auiliares*/
        txtNombreConductor.setEnabled(false);
        btnExportar.setEnabled(true);
        btnImprimir.setEnabled(true);
        btnImprimir.requestFocus();

    }

}
