/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios;

import aplicacionlogistica.distribucion.Threads.HiloGuardarManifiestoFacturasAnuladas;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeRechasosParaDescargar;
import aplicacionlogistica.distribucion.Threads.HiloValidadorDeFacturasParaAnular;
import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.PrincipalAuxiliarLogistica;
import aplicacionlogistica.distribucion.PrincipalLogistica;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasParaAnular;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_Factura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Eduardo López Casanova
 */
public class FAnularFacturas extends javax.swing.JInternalFrame {

    Inicio ini;
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    DefaultTableModel modelo1, modelo2, modelo3;
    public String rutaDeArchivoFacturaAnulada = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_FacturasParaAnular.txt";

    //public ArrayList<CFacturasParaAnular> listaDeRechasosParaAnular = null;
    CFacturas facturaActual;
    CClientes cliente;//= new CClientes(this.ini,facturaActual.getCodigoDeCliente());
    public ArrayList<CFacturasParaAnular> listaDeFacturasParaAnular;
    public ArrayList<CFacturas> listaDeFacturas;
    public ArrayList<String> listaDeNumerosDeFacturas;

    String senteciaSqlFacturasAnuladas = null;

    public boolean estaOcupadoGrabando = false;
    
    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param form
     */
    public FAnularFacturas(Inicio  ini) {
      this.ini=ini;
      cargarVista();  
    }
    
    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param form
     */
    public FAnularFacturas(PrincipalAuxiliarLogistica form1) {
      this.ini=form1.ini;
      cargarVista();  
    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     * @param form
     */
    public FAnularFacturas(PrincipalLogistica form) {
        this.ini = form.ini;
        cargarVista();
    }

    private void cargarVista() {
        try {
            
            try {
                initComponents();

                limpiarTodo();
                btnAgregar.setEnabled(false);
                listaDeFacturasParaAnular = new ArrayList();
                lblCirculoDeProgreso.setVisible(false);
                lblCirculoDeProgreso1.setVisible(false);

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
                Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the fCambiarClave.
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlEntregas = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableProductosPorFactura = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        txtNumeroDeFactura = new javax.swing.JTextField();
        lblValorTotalFactura = new org.edisoncor.gui.label.LabelCustom();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtJustificacion = new javax.swing.JTextArea();
        txtDireccionCliente = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        txtVendedor = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        lblCirculoDeProgreso1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFacturasParaAnular = new javax.swing.JTable();
        lblIdentificadorManifiesto = new javax.swing.JLabel();
        lblValorTotalRechaso = new org.edisoncor.gui.label.LabelCustom();

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
        setMaximizable(true);
        setTitle("Formulario para anular Facturas de venta");
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
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
        jTableProductosPorFactura.setComponentPopupMenu(borrarFila);
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

        javax.swing.GroupLayout pnlEntregasLayout = new javax.swing.GroupLayout(pnlEntregas);
        pnlEntregas.setLayout(pnlEntregasLayout);
        pnlEntregasLayout.setHorizontalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlEntregasLayout.setVerticalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Productos", pnlEntregas);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Descargar facturas para anular"));

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText(" Factura");

        txtNumeroDeFactura.setEditable(false);
        txtNumeroDeFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeFacturaFocusGained(evt);
            }
        });
        txtNumeroDeFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroDeFacturaActionPerformed(evt);
            }
        });
        txtNumeroDeFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeFacturaKeyPressed(evt);
            }
        });

        lblValorTotalFactura.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValorTotalFactura.setText("$ 0.0");
        lblValorTotalFactura.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lblValorTotalFactura.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("Justificación");

        txtJustificacion.setColumns(20);
        txtJustificacion.setRows(5);
        jScrollPane6.setViewportView(txtJustificacion);

        txtDireccionCliente.setEditable(false);
        txtDireccionCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDireccionClienteFocusGained(evt);
            }
        });
        txtDireccionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionClienteActionPerformed(evt);
            }
        });
        txtDireccionCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionClienteKeyPressed(evt);
            }
        });

        txtTelefonoCliente.setEditable(false);
        txtTelefonoCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoClienteFocusGained(evt);
            }
        });
        txtTelefonoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoClienteActionPerformed(evt);
            }
        });
        txtTelefonoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoClienteKeyPressed(evt);
            }
        });

        txtNombreCliente.setEditable(false);
        txtNombreCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreClienteFocusGained(evt);
            }
        });
        txtNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteActionPerformed(evt);
            }
        });
        txtNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreClienteKeyPressed(evt);
            }
        });

        txtVendedor.setEditable(false);
        txtVendedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtVendedorFocusGained(evt);
            }
        });
        txtVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVendedorActionPerformed(evt);
            }
        });
        txtVendedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVendedorKeyPressed(evt);
            }
        });

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Cliente");

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Dirección");

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Teléfono");

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Vendedor");

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        lblCirculoDeProgreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(lblValorTotalFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel32)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel27)
                                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(109, 109, 109)
                                .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNombreCliente)
                            .addComponent(txtDireccionCliente)
                            .addComponent(txtVendedor)
                            .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(209, 209, 209))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNumeroDeFactura)
                        .addComponent(jLabel27)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblValorTotalFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(537, 0, 744, 620));

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
                .addContainerGap()
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, -1, -1));

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableFacturasParaAnular.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableFacturasParaAnular.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableFacturasParaAnular.setColumnSelectionAllowed(true);
        jTableFacturasParaAnular.setComponentPopupMenu(borrarFila);
        jTableFacturasParaAnular.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFacturasParaAnularMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableFacturasParaAnular);
        jTableFacturasParaAnular.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTableFacturasParaAnular.getColumnModel().getColumnCount() > 0) {
            jTableFacturasParaAnular.getColumnModel().getColumn(0).setResizable(false);
            jTableFacturasParaAnular.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTableFacturasParaAnular.getColumnModel().getColumn(1).setResizable(false);
            jTableFacturasParaAnular.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTableFacturasParaAnular.getColumnModel().getColumn(2).setResizable(false);
            jTableFacturasParaAnular.getColumnModel().getColumn(2).setPreferredWidth(250);
            jTableFacturasParaAnular.getColumnModel().getColumn(3).setResizable(false);
            jTableFacturasParaAnular.getColumnModel().getColumn(3).setPreferredWidth(120);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        lblIdentificadorManifiesto.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
        lblIdentificadorManifiesto.setText("Total Valor de Facturas anuladas ");
        jPanel5.add(lblIdentificadorManifiesto, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 398, 519, -1));

        lblValorTotalRechaso.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValorTotalRechaso.setText("$ 0.0");
        lblValorTotalRechaso.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lblValorTotalRechaso.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jPanel5.add(lblValorTotalRechaso, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 425, 373, 46));

        jScrollPane1.setViewportView(jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
//        if (manifiestoActual != null) {
//            
//           if(esteManifiestoEsMio){
//              manifiestoActual.liberarManifiesto(true);
//              manifiestoActual = null; 
//               esteManifiestoEsMio=false;
//            }
//        }
//
limpiarTodo();
        btnImprimir.setEnabled(false);
//        cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
//        cbxMovimientoFactura.setSelectedItem("NINGUNO");
        this.dispose();        this.setVisible(false);

    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        lblCirculoDeProgreso.setVisible(true);
        limpiarTodo();
       File  archivo = new File(rutaDeArchivoFacturaAnulada);
       
       if(archivo.exists()){
           recuperarFacturasDesdeArchivo();
           lblCirculoDeProgreso.setVisible(false);
            txtNumeroDeFactura.setEditable(true);
            txtNumeroDeFactura.setEnabled(true);
            txtNumeroDeFactura.requestFocus();
            txtNumeroDeFactura.requestFocus();
       }else{
           new Thread(new HiloListadoDeRechasosParaDescargar(ini, this)).start(); 
       }
        
       
        btnNuevo.setEnabled(false);
        
//      
//       
//        pnlRechazosTotales.setEnabled(true);
//
//        txtManifiesto.setEnabled(true);
//        txtManifiesto.setEditable(true);
//        jTabbedPane1.setEnabled(false);
        
//        txtManifiesto.requestFocus();
//        cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
//        cbxMovimientoFactura.setSelectedItem("NINGUNO");
//        //cbxManifiestosDeDistribucion.setEnabled(true);

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void agregarImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarImagenMouseClicked

    }//GEN-LAST:event_agregarImagenMouseClicked

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        

        int deseaGrabar = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        /* Se valida el deseo de grabar los datos en la BBDD  */
        if (deseaGrabar == 0) {
            btnGrabar.setEnabled(false);
            estaOcupadoGrabando = true;

            /* Se veirifica sí hay conexion a Internet */
            if (ini.verificarConexion()) {
                try {

                    
                    cerrarManifiesto();

                } catch (Exception e) {
                      estaOcupadoGrabando = false;
                    JOptionPane.showMessageDialog(this, "Error al guardar los datos " + e, "Error", JOptionPane.ERROR_MESSAGE, null);
                    
                }
            } else {
                 estaOcupadoGrabando = false;
                JOptionPane.showMessageDialog(this, "Error al guardar los datos, no hay Internet  ", "Error al grabar ", JOptionPane.ERROR_MESSAGE, null);
            }

        }


    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        limpiarTodo();
        btnGrabar.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnImprimir.setEnabled(false);


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jTableFacturasParaAnularMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFacturasParaAnularMouseClicked
        modelo2 = (DefaultTableModel) jTableFacturasParaAnular.getModel();
        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO
        if (jTableFacturasParaAnular.getRowCount() > 0) {

            int fila = jTableFacturasParaAnular.getSelectedRow();

            try {
                String numFactura = modelo2.getValueAt(fila, 1).toString();
                consultarFactura(numFactura);

            } catch (Exception ex) {
                Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            btnGrabar.setEnabled(false);
            JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
        }
    }//GEN-LAST:event_jTableFacturasParaAnularMouseClicked

    private void txtNumeroDeFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaFocusGained
        txtNumeroDeFactura.setSelectionStart(0);
        txtNumeroDeFactura.setSelectionEnd(txtNumeroDeFactura.getText().length());
    }//GEN-LAST:event_txtNumeroDeFacturaFocusGained

    private void txtNumeroDeFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaKeyPressed
        this.facturaActual = null;
        btnAgregar.setEnabled(false);
        // CFacturasDescargadas facturaActualDescargada = null;
        double valorFactura = 0;
        if (evt.getKeyCode() == KeyEvent.VK_F2) {

            consultarLafacturaActual();

        }

        /* Evento al oprimir la tecla enter*/
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            consultarFactura(txtNumeroDeFactura.getText().trim());
            
        }


    }//GEN-LAST:event_txtNumeroDeFacturaKeyPressed

    private void consultarFactura(String numeroFactura) {
        double valorFactura;
        limpiarModulo();
        lblCirculoDeProgreso1.setVisible(true);
        jTabbedPane1.setEnabled(true);
        txtNumeroDeFactura.setSelectionStart(0);
        txtNumeroDeFactura.setSelectionEnd(txtNumeroDeFactura.getText().length());
        try {

            facturaActual = new CFacturas(this.ini, numeroFactura);

            // SE VALIDA QUE LA FACTURA EXISTA EN EL MANIFIESTO
            if (this.facturaActual.getNumeroDeFactura() != null) {

                // SE ASIGNA TODOS LOS PRODUCTOS DE LA FACTURA
                CFacturas fac = new CFacturas(ini);
                fac.setNumeroDeFactura(facturaActual.getNumeroDeFactura());
                fac.setListaCProductosPorFactura(false);

                if (fac.getListaCProductosPorFactura().size() > 0) {

                    // SE LLENA LA TABLA DE PRODUCTOS CONTENIDOS EN LA FACTURA
                    valorFactura = llenarTablaProductosPorFactura();
                    lblValorTotalFactura.setText(nf.format(valorFactura));

                    /*Se valida que la factura no esté reportada  aún*/
                    if (!isFacturaReportada(numeroFactura)) {
                        lblCirculoDeProgreso1.setVisible(true);
                        new Thread(new HiloValidadorDeFacturasParaAnular(ini, this)).start();

                    }

                }
                /* Cuando la facturaActual no existe en el manifiesto */
            } else {

                lblCirculoDeProgreso1.setVisible(false);
                JOptionPane.showInternalMessageDialog(this, "La factura Actual no existe en el sistema", "Error", 2);
                txtJustificacion.setEditable(false);
                txtJustificacion.setEnabled(false);
                txtJustificacion.setText("");
                btnAgregar.setEnabled(false);
                txtNumeroDeFactura.requestFocus();
            }

        } catch (Exception ex) {
            System.out.println("Error en txtNumeroDeFacturaKeyPressed ");
            Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, ex);
            //txtValorDescuento.requestFocus();
        }
        lblCirculoDeProgreso1.setVisible(false);
//            txtJustificacion.requestFocus();
    }

    private void consultarLafacturaActual() {
        try {
            CFacturas fac = new CFacturas(ini, txtNumeroDeFactura.getText().trim());

            if (facturaActual != null) {
//                FConsultarFacturas formulario = new FConsultarFacturas(ini, this, fac);
//                PrincipalLogistica.escritorio.add(formulario);
//                formulario.toFront();
//                formulario.setClosable(true);
//                formulario.setVisible(true);
//                formulario.setTitle("Formulario para consultar Facturas");
//                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//                //form.setSize(PrincipalLogistica.escritorio.getSize());
//                formulario.setLocation((screenSize.width - PrincipalLogistica.escritorio.getSize().width) / 2, (screenSize.height - PrincipalLogistica.escritorio.getSize().height) / 2);
//                formulario.show();
            } else {
                JOptionPane.showInternalMessageDialog(this, "La factura Actual no aparece en el sistema", "Factura no visible ", 2);
                txtJustificacion.setEditable(false);
                txtJustificacion.setEnabled(false);
                txtJustificacion.setText("");
                btnAgregar.setEnabled(false);
                txtNumeroDeFactura.requestFocus();
            }

        } catch (Exception ex) {
            Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void armarArchivoFacturasAnuladas() throws Exception {

        ArchivosDeTexto archivo;

        for (CFacturasParaAnular obj : listaDeFacturasParaAnular) {

            //ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_productosDescargados.txt";
            String rutaDeArchivoFacturasAnuladas = "" + (new File(".").getAbsolutePath()).replace(".", "") + rutaDeArchivoFacturaAnulada;

            archivo = new ArchivosDeTexto(rutaDeArchivoFacturasAnuladas);

            senteciaSqlFacturasAnuladas = obj.getCadenaConLosCampos();
            archivo.insertarLineaEnFichero(senteciaSqlFacturasAnuladas, rutaDeArchivoFacturasAnuladas);

        }
    }

    private void validarFacturaParaAnular() throws Exception {

        listaDeNumerosDeFacturas = new ArrayList();

        for (CFacturas obj : listaDeFacturas) {
            listaDeNumerosDeFacturas.add(obj.getNumeroDeFactura());

        }

    }

    private void borraUnaFilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borraUnaFilaActionPerformed

    }//GEN-LAST:event_borraUnaFilaActionPerformed

    private void borraUnaFilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borraUnaFilaMouseClicked

        modelo2 = (DefaultTableModel) jTableFacturasParaAnular.getModel();
        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO
        if (jTableFacturasParaAnular.getRowCount() > 0) {

            int fila = jTableFacturasParaAnular.getSelectedRow();

            int borrarFila = JOptionPane.showOptionDialog(this, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "No");

            // HA SELECCIONADO BORRAR LA FILA
            if (borrarFila == 0) {

                try {
                    String numFactura = modelo2.getValueAt(fila, 1).toString();

                    // IDENTIFICAMOS EL OBJETO FACTURA PARA REALIZAR LOS RESPECTIVOS CAMBIOS
                    for (CFacturas obj : listaDeFacturas) {
                        if (obj.getNumeroDeFactura().equals(numFactura)) {
                            facturaActual = obj;
                            break;
                        }
                    }

                    //Borra la fila seleccionada del JTable 
                    modelo2.removeRow(fila);

                    /* Eliminamos la fila del archivo */
                    ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoFacturaAnulada);

                    for (CFacturasParaAnular obj : listaDeFacturasParaAnular) {
                        if (obj.getNumeroFactura().equals(numFactura)) {

                            listaDeFacturasParaAnular.remove(obj);
                            archivo.borrarLinea(obj.getCadenaConLosCampos());
                            break;

                        }
                    }
                    for (CFacturas obj : listaDeFacturas) {
                        if (obj.getNumeroDeFactura().equals(numFactura)) {

                            listaDeFacturas.remove(obj);

                            break;

                        }
                    }

                    // ordena la tabla con la nueva adherencia
                    ordenarjTableFacturasParaAnular();

                    double valorTotal = 0.0;

                    for (CFacturas obj : listaDeFacturas) {
                        valorTotal += obj.getValorTotalFactura();

                    }
                    lblValorTotalRechaso.setText(nf.format(valorTotal));

                } catch (Exception ex) {
                    Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else {
            btnGrabar.setEnabled(false);
            JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
        }
    }//GEN-LAST:event_borraUnaFilaMouseClicked

    private void tablaDocsVencidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsVencidosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDocsVencidosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (!estaOcupadoGrabando) {

        } else {
            JOptionPane.showInternalMessageDialog(this, "El sistema está ocupado grabado los datos,\n no se puede cerrar el formulario", "Error al cerrar", JOptionPane.ERROR_MESSAGE);

        }


    }//GEN-LAST:event_formInternalFrameClosing

    private void txtNumeroDeFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
//        ReporteDescargueDeFacturas1 demo = new ReporteDescargueDeFacturas1(ini, manifiestoActual);
    }//GEN-LAST:event_btnImprimirActionPerformed


    private void jTableProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductosPorFacturaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableProductosPorFacturaMouseClicked

    private void txtDireccionClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteFocusGained

    private void txtDireccionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteActionPerformed

    private void txtDireccionClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteKeyPressed

    private void txtTelefonoClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoClienteFocusGained

    private void txtTelefonoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoClienteActionPerformed

    private void txtTelefonoClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoClienteKeyPressed

    private void txtNombreClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteFocusGained

    private void txtNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteActionPerformed

    private void txtNombreClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteKeyPressed

    private void txtVendedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVendedorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVendedorFocusGained

    private void txtVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVendedorActionPerformed

    private void txtVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVendedorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVendedorKeyPressed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        if (txtJustificacion.getText().isEmpty()) {
            JOptionPane.showInternalMessageDialog(this, "Llenar el espacio de la justificación para anular la factura", "Datos incompletos", 2);
        } else {
            btnAgregar.setEnabled(false);
            try {
                CFacturasParaAnular facPorAnular= new CFacturasParaAnular(ini);
                facPorAnular.setCausalDeRechazo(txtJustificacion.getText().trim());
                facPorAnular.setNumeroFactura(txtNumeroDeFactura.getText().trim());
                
                if(agregarFacturaAlArchivo(facPorAnular)){
                    agregarRegistroAlJTable(facPorAnular);
                    
                }
               

                // SE LLENA LAS RESPECTIVAS ETIQUETAS
                lblValorTotalFactura.setText(nf.format(facturaActual.getValorTotalFactura()));
                txtJustificacion.setEnabled(false);
                txtJustificacion.setEditable(false);
                txtNumeroDeFactura.setEnabled(true);
                txtNumeroDeFactura.setEditable(true);
                txtNumeroDeFactura.requestFocus();
                txtNumeroDeFactura.requestFocus();

            } catch (Exception ex) {
                Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnAgregarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JMenu borraUnaFila;
    private javax.swing.JPopupMenu borrarFila;
    public javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableFacturasParaAnular;
    private javax.swing.JTable jTableProductosPorFactura;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JLabel lblCirculoDeProgreso1;
    public javax.swing.JLabel lblIdentificadorManifiesto;
    private org.edisoncor.gui.label.LabelCustom lblValorTotalFactura;
    public org.edisoncor.gui.label.LabelCustom lblValorTotalRechaso;
    private javax.swing.JPanel pnlEntregas;
    private javax.swing.JPopupMenu pumImagen;
    private javax.swing.JTable tablaDocsVencidos;
    public javax.swing.JTextField txtDireccionCliente;
    public javax.swing.JTextArea txtJustificacion;
    public javax.swing.JTextField txtNombreCliente;
    public javax.swing.JTextField txtNumeroDeFactura;
    public javax.swing.JTextField txtTelefonoCliente;
    public javax.swing.JTextField txtVendedor;
    // End of variables declaration//GEN-END:variables

    /**
     * Método encargado de llenar el componente jtable de los productos y los
     * datos del cliente en los respectivos componentes.
     *
     */
    private double llenarTablaProductosPorFactura() throws Exception {
        cliente = new CClientes(ini, facturaActual.getCodigoDeCliente());

        modelo1 = (DefaultTableModel) jTableProductosPorFactura.getModel();
        double valorFactura = 0.0;

        txtNombreCliente.setText(cliente.getNombreDeCliente());
        txtDireccionCliente.setText(cliente.getDireccion());
        txtTelefonoCliente.setText(cliente.getCelularCliente());
        txtVendedor.setText(facturaActual.getVendedor());

        CFacturas fac = new CFacturas(ini);
        fac.setNumeroDeFactura(facturaActual.getNumeroDeFactura());
        fac.setListaCProductosPorFactura(false);

        // se nexan los productos a la tabla de productos por facturaActual
        for (CProductosPorFactura obj : fac.getListaCProductosPorFactura()) {

            int fila = jTableProductosPorFactura.getRowCount();

            modelo1.addRow(new Object[jTableProductosPorFactura.getRowCount()]);
            jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()*obj.getCantidad()), fila, 5);

          

        }
        return valorFactura;
    }

  

    /**
     * Método encargado guardar los datos en el sistema
     */
    private void cerrarManifiesto() {

        new Thread(new HiloGuardarManifiestoFacturasAnuladas(ini, this)).start();

    }

    private void limpiarTodo() {

        /*limpiar componentes */
        txtNumeroDeFactura.setText("");
        txtNumeroDeFactura.setEnabled(false);
        txtJustificacion.setText("");
        txtJustificacion.setEnabled(false);
        btnAgregar.setEnabled(false);
        txtNombreCliente.setText("");
        txtDireccionCliente.setText("");
        txtTelefonoCliente.setText("");
        txtVendedor.setText("");
        limpiarTblProductosPorFactura();
        limpiarjTableFacturasParaAnular();
        lblValorTotalRechaso.setText(nf.format(0.0));
        lblValorTotalFactura.setText(nf.format(0.0));

        /*limpiar variables*/
       
        facturaActual = null;
        cliente = null;
        listaDeFacturasParaAnular = null;
        listaDeFacturas = null;
        listaDeNumerosDeFacturas = null;
        senteciaSqlFacturasAnuladas = null;
        estaOcupadoGrabando = false;

    }

    private void limpiarTblProductosPorFactura() {

        DefaultTableModel modelo = (DefaultTableModel) jTableProductosPorFactura.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    private void limpiarjTableFacturasParaAnular() {

        DefaultTableModel modelo = (DefaultTableModel) jTableFacturasParaAnular.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    private void ordenarjTableFacturasParaAnular() {
        for (int i = 0; i < jTableFacturasParaAnular.getRowCount(); i++) {
            jTableFacturasParaAnular.setValueAt(i + 1, i, 0);
        }
    }

    public boolean isFacturaReportada(String numeroFactura) {
        boolean siExiste = false;
        if(listaDeFacturasParaAnular != null){
           for (CFacturasParaAnular obj : listaDeFacturasParaAnular) {
            if (obj.getNumeroFactura().equals(numeroFactura)) {
                siExiste = true;
                break;
            }
        } 
        }
        
        return siExiste;
    }

    

    private void limpiarModulo() {
        txtJustificacion.setText("");
        txtJustificacion.setEnabled(false);
        btnAgregar.setEnabled(false);
        txtNombreCliente.setText("");
        txtDireccionCliente.setText("");
        txtTelefonoCliente.setText("");
        txtVendedor.setText("");
        limpiarTblProductosPorFactura();

    }

    private void agregarRegistroAlJTable( CFacturasParaAnular fxa) {

       if(!isFacturaReportada(fxa.getNumeroFactura())){
            
           if (listaDeFacturasParaAnular != null) {
               listaDeFacturasParaAnular.add(fxa);
           } else {
               listaDeFacturasParaAnular = new ArrayList<>();
               listaDeFacturasParaAnular.add(fxa);
           }

            DefaultTableModel modelo = (DefaultTableModel) jTableFacturasParaAnular.getModel();

            int filaTabla2 = jTableFacturasParaAnular.getRowCount();

            modelo.addRow(new Object[jTableFacturasParaAnular.getRowCount()]);

            jTableFacturasParaAnular.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
            jTableFacturasParaAnular.setValueAt(facturaActual.getNumeroDeFactura(), filaTabla2, 1); // numero de facturaActual
            jTableFacturasParaAnular.setValueAt(facturaActual.getNombreDeCliente(), filaTabla2, 2); // nombre del nombreDelCliente
            jTableFacturasParaAnular.setValueAt(nf.format(facturaActual.getValorTotalFactura()), filaTabla2, 3);
       }
    
       

    }
    public synchronized boolean   agregarFacturaAlArchivo(CFacturasParaAnular fac) {
        boolean grabado = false;
        /* Se graba el registro temporal de factura por manifiesto en el archivo temporal*/
        String listaDeCampos = fac.getCadenaConLosCampos();

        ArchivosDeTexto archivo = new ArchivosDeTexto("" + (new File(".").getAbsolutePath()).replace(".", "") + rutaDeArchivoFacturaAnulada);

        if (archivo.insertarLineaEnFichero(listaDeCampos, rutaDeArchivoFacturaAnulada)) {
            grabado = true;
        }
        return grabado;
    }
    
    private void recuperarFacturasDesdeArchivo(){
        File archivo = new File(rutaDeArchivoFacturaAnulada);
     
      FileReader fr = null;
      BufferedReader br = null;
     
        try {
            System.out.println(rutaDeArchivoFacturaAnulada);

        
            listaDeFacturasParaAnular= new ArrayList<>();
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                
                try {
                    String[] cadena = linea.split(",");
                    CFacturasParaAnular fxa = new CFacturasParaAnular(ini);

                    fxa.setNumeroFactura(cadena[0]);
                    fxa.setCausalDeRechazo(cadena[1]);
                    
                    facturaActual = new CFacturas(this.ini, fxa.getNumeroFactura());
                    agregarRegistroAlJTable(fxa);
                   

                    System.out.println(cadena[0]);

                } catch (Exception e) {
                    Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, e);
                    System.out.println(e);
                }

               
            }
            br.close();
            fr.close();
            if(listaDeFacturasParaAnular.size()>0) btnGrabar.setEnabled(true);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (Exception ex) {
            Logger.getLogger(FAnularFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        
         
     }
}
