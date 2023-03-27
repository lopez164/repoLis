/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.ingresoDeRegistros;

import mtto.proveedores.SucursalesPorproveedor;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcelPacheco;
import aplicacionlogistica.distribucion.objetos.personas.CPersonas;
import mtto.ingresoDeRegistros.objetos.FacturasLogisticas;
import mtto.ingresoDeRegistros.objetos.TiposDeMantenimientos;
import mtto.vehiculos.CVehiculos;
import mtto.ingresoDeRegistros.objetos.MantenimientosPorPlaca;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import mtto.Threads.HiloImportarConsumosCombustibeDesdeArchivo;
import mtto.documentos.objetos.GastosPorVehiculo;
import mtto.ingresoDeRegistros.objetos.GastosFlota;
import mtto.proveedores.Cproveedores;
import ui.swing.searchText.DataSearch;
import ui.swing.searchText.EventClick;
import ui.swing.searchText.PanelSearch;
import ui.swing.tableCustom.TableCustom;

/**
 *
 * @author VLI_488
 */
public final class FImportarConsumoCombustible extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public boolean actualizar;
    public File file;

    public boolean cancelar = false;

    /*variable para controlar el combo box*/
    private int ultimoIndiceSeleccionado = 0;

    public boolean estaOcupadoGrabando = false;

    String mensaje = null;

    /*Clases objetivo */
    public MantenimientosPorPlaca mantenimientosPorPlaca;
    public FacturasLogisticas facturaLogistica;
    public TiposDeMantenimientos tiposDeMantenimientos = null;

    /**/
    public Inicio ini;
    public SucursalesPorproveedor sucursalProveedor;

    CVehiculos vehiculo = null;
    CPersonas conductor = null;
    public Cproveedores proveedor= null;

    public ArrayList<String> sqlInsercionRemota = null;

    final DefaultListModel modeloJlist = new DefaultListModel();
    public DefaultTableModel modeloDetalleFactura, modeloFacturas;

    TextAutoCompleter textAutoCompleterProveedores;
    TextAutoCompleter textAutoCompleterVehiculos;

    String texto = "";
    
     public  List<GastosFlota> listaDeGastosFlota = null;
     public  List<GastosPorVehiculo> listaDeGastosPorVehiculo = null;


    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    
    private Connection conexion;
    private PanelSearch search;
    private JPopupMenu menu;
    boolean sucursalElegida = false;

    /*
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     * @param formCrearSucursales
     **/
    public FImportarConsumoCombustible(Inicio ini) {
        this.ini = ini;
        initComponents();
        
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
                txtProveedor.setText(data.getText());
                addStory(data.getText());
                System.out.println("Click Item : " + data.getText());
                //new Thread(new HiloIngresarSucursalProveedores(ini,this.fingresarSucursalDeProveedor, "consultarSucursalProveedor")).start();
                sucursalElegida = true;
                //txtTelefono.requestFocus();

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

        TableCustom.apply(jScrollPane6, TableCustom.TableType.DEFAULT);

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

        CargarVista();

    }

    /**
     * This method is called from within the constructor to initialize the
     * ingresarFacturasDeGastos. WARNING: Do NOT modify this code. The content
     * of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        jbtnGrabarConsumos = new javax.swing.JButton();
        btnAgregarFactura = new javax.swing.JButton();
        btnInsertarFila = new javax.swing.JButton();
        jbtnSubirDatos = new javax.swing.JButton();
        btCancelarOperacion = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        txtCarpetas = new javax.swing.JTextField();
        btnEscogerFolder = new javax.swing.JButton();
        barraSuperior = new javax.swing.JProgressBar();
        txtProveedor = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblConsumos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario para importar consumos de combustible\n");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
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

        jToolBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar1.setRollover(true);

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        btnNuevo.setToolTipText("Agregar Mantenimiento");
        btnNuevo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnNuevo.setFocusable(false);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setMaximumSize(new java.awt.Dimension(27, 27));
        btnNuevo.setMinimumSize(new java.awt.Dimension(27, 27));
        btnNuevo.setPreferredSize(new java.awt.Dimension(24, 24));
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNuevo);

        jbtnGrabarConsumos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jbtnGrabarConsumos.setToolTipText("Grabar Mantenimiento");
        jbtnGrabarConsumos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtnGrabarConsumos.setEnabled(false);
        jbtnGrabarConsumos.setFocusable(false);
        jbtnGrabarConsumos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnGrabarConsumos.setPreferredSize(new java.awt.Dimension(24, 24));
        jbtnGrabarConsumos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnGrabarConsumos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGrabarConsumosActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnGrabarConsumos);

        btnAgregarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Table.png"))); // NOI18N
        btnAgregarFactura.setToolTipText("Habilitar Facturas de  Gasto");
        btnAgregarFactura.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregarFactura.setEnabled(false);
        btnAgregarFactura.setFocusable(false);
        btnAgregarFactura.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAgregarFactura.setPreferredSize(new java.awt.Dimension(24, 24));
        btnAgregarFactura.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarFacturaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAgregarFactura);

        btnInsertarFila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/table_row_insert.png"))); // NOI18N
        btnInsertarFila.setToolTipText("Agregar Factura de Gasto");
        btnInsertarFila.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnInsertarFila.setEnabled(false);
        btnInsertarFila.setFocusable(false);
        btnInsertarFila.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnInsertarFila.setPreferredSize(new java.awt.Dimension(24, 24));
        btnInsertarFila.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnInsertarFila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarFilaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnInsertarFila);

        jbtnSubirDatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Upload.png"))); // NOI18N
        jbtnSubirDatos.setToolTipText("Eliminar Factura de Gasto");
        jbtnSubirDatos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jbtnSubirDatos.setEnabled(false);
        jbtnSubirDatos.setFocusable(false);
        jbtnSubirDatos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnSubirDatos.setPreferredSize(new java.awt.Dimension(24, 24));
        jbtnSubirDatos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtnSubirDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSubirDatosActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtnSubirDatos);

        btCancelarOperacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        btCancelarOperacion.setToolTipText("Cancelar toda la Operacion");
        btCancelarOperacion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btCancelarOperacion.setFocusable(false);
        btCancelarOperacion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btCancelarOperacion.setPreferredSize(new java.awt.Dimension(24, 24));
        btCancelarOperacion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btCancelarOperacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarOperacionActionPerformed(evt);
            }
        });
        jToolBar1.add(btCancelarOperacion);

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        btnSalir.setToolTipText("Salir del Modulo");
        btnSalir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSalir.setFocusable(false);
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setPreferredSize(new java.awt.Dimension(24, 24));
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSalir);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        txtCarpetas.setEditable(false);
        txtCarpetas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCarpetasActionPerformed(evt);
            }
        });

        btnEscogerFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Folder.png"))); // NOI18N
        btnEscogerFolder.setToolTipText("Eliminar Factura de Gasto");
        btnEscogerFolder.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEscogerFolder.setEnabled(false);
        btnEscogerFolder.setFocusable(false);
        btnEscogerFolder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEscogerFolder.setPreferredSize(new java.awt.Dimension(24, 24));
        btnEscogerFolder.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEscogerFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscogerFolderActionPerformed(evt);
            }
        });

        barraSuperior.setStringPainted(true);
        barraSuperior.setVerifyInputWhenFocusTarget(false);

        txtProveedor.setEditable(false);
        txtProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtProveedorMouseClicked(evt);
            }
        });
        txtProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProveedorActionPerformed(evt);
            }
        });
        txtProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProveedorKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProveedorKeyReleased(evt);
            }
        });

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblConsumos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "item", "Recibo", "fecha", "proveedor", "ciudad", "placa", "Tipo Comb.", "cant.", "valor unit.", "valor Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblConsumos.getTableHeader().setReorderingAllowed(false);
        tblConsumos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblConsumosMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblConsumos);
        if (tblConsumos.getColumnModel().getColumnCount() > 0) {
            tblConsumos.getColumnModel().getColumn(0).setMinWidth(80);
            tblConsumos.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblConsumos.getColumnModel().getColumn(0).setMaxWidth(80);
            tblConsumos.getColumnModel().getColumn(1).setMinWidth(100);
            tblConsumos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblConsumos.getColumnModel().getColumn(1).setMaxWidth(100);
            tblConsumos.getColumnModel().getColumn(2).setMinWidth(100);
            tblConsumos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblConsumos.getColumnModel().getColumn(2).setMaxWidth(100);
            tblConsumos.getColumnModel().getColumn(3).setMinWidth(200);
            tblConsumos.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblConsumos.getColumnModel().getColumn(3).setMaxWidth(200);
            tblConsumos.getColumnModel().getColumn(4).setMinWidth(120);
            tblConsumos.getColumnModel().getColumn(4).setPreferredWidth(120);
            tblConsumos.getColumnModel().getColumn(4).setMaxWidth(120);
            tblConsumos.getColumnModel().getColumn(5).setMinWidth(120);
            tblConsumos.getColumnModel().getColumn(5).setPreferredWidth(120);
            tblConsumos.getColumnModel().getColumn(5).setMaxWidth(120);
            tblConsumos.getColumnModel().getColumn(6).setMinWidth(120);
            tblConsumos.getColumnModel().getColumn(6).setPreferredWidth(120);
            tblConsumos.getColumnModel().getColumn(6).setMaxWidth(120);
            tblConsumos.getColumnModel().getColumn(7).setMinWidth(120);
            tblConsumos.getColumnModel().getColumn(7).setPreferredWidth(120);
            tblConsumos.getColumnModel().getColumn(7).setMaxWidth(120);
            tblConsumos.getColumnModel().getColumn(8).setMinWidth(100);
            tblConsumos.getColumnModel().getColumn(8).setPreferredWidth(100);
            tblConsumos.getColumnModel().getColumn(8).setMaxWidth(100);
            tblConsumos.getColumnModel().getColumn(9).setMinWidth(150);
            tblConsumos.getColumnModel().getColumn(9).setPreferredWidth(150);
            tblConsumos.getColumnModel().getColumn(9).setMaxWidth(150);
        }

        jLabel1.setText("Digirar el nombre del Proveedor ");

        txtObservaciones.setEditable(false);
        txtObservaciones.setColumns(20);
        txtObservaciones.setRows(5);
        jScrollPane1.setViewportView(txtObservaciones);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(txtCarpetas, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEscogerFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(barraSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane6))
                        .addGap(11, 11, 11))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(284, 284, 284))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCarpetas, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEscogerFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(barraSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 1016, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing

        if (!estaOcupadoGrabando) {

        } else {
            JOptionPane.showInternalMessageDialog(this, "El sistema está ocupado grabado los datos,\n no se puede cerrar el formulario", "Error al cerrar", JOptionPane.ERROR_MESSAGE);

        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void salir() {
        this.dispose();
        this.setVisible(false);
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
         nuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void jbtnGrabarConsumosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGrabarConsumosActionPerformed
       new Thread(new HiloImportarConsumosCombustibeDesdeArchivo(this,"grabar")).start();
    }//GEN-LAST:event_jbtnGrabarConsumosActionPerformed

    private void btCancelarOperacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarOperacionActionPerformed
        cancelarOperacion();
    }//GEN-LAST:event_btCancelarOperacionActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        cancelarOperacion();
        salir();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnAgregarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarFacturaActionPerformed
        int x = JOptionPane.showConfirmDialog(this, "Desea Agregar facturas al mantenimiento del vehiculo " + vehiculo.getPlaca() + " ?", "AgregarFactura registro", JOptionPane.YES_NO_OPTION);

        if (x == JOptionPane.YES_OPTION) {

            try {

                mantenimientosPorPlaca = new MantenimientosPorPlaca(ini);

                mantenimientosPorPlaca.setIdtipoMantenimiento(1);

                Date dt = new Date();
                mantenimientosPorPlaca.setFechaMantenimiento(dt);
                mantenimientosPorPlaca.setZona(ini.getUser().getZona());
                mantenimientosPorPlaca.setRegional(ini.getUser().getRegional());
                mantenimientosPorPlaca.setAgencia(ini.getUser().getAgencia());
                mantenimientosPorPlaca.setActivo(1);
                mantenimientosPorPlaca.setUsuario(Inicio.deCifrar(ini.getUser().getNombreUsuario()));
                mantenimientosPorPlaca.setFlag(1);

                facturaLogistica = new FacturasLogisticas(ini);

                modeloDetalleFactura = (DefaultTableModel) tblConsumos.getModel();

                btnNuevo.setEnabled(false);
                btnAgregarFactura.setEnabled(false);

                habilitar(false);

            } catch (Exception ex) {
                Logger.getLogger(FImportarConsumoCombustible.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnAgregarFacturaActionPerformed


    private void btnInsertarFilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertarFilaActionPerformed
        // modeloFacturas = (DefaultTableModel) tblListaDeFacturas.getModel();
        modeloDetalleFactura = (DefaultTableModel) tblConsumos.getModel();

        facturaLogistica = new FacturasLogisticas(ini);


    }//GEN-LAST:event_btnInsertarFilaActionPerformed

    private void btnEscogerFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscogerFolderActionPerformed

        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Elija Archivo a importar");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Escoja el archivo en formato office 2007:  .xslx", "xlsx");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(jPanel9);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                this.file = fc.getSelectedFile();
                txtCarpetas.setText(file.getAbsolutePath());
                jbtnSubirDatos.setEnabled(true);
                txtProveedor.setEditable(false);
                // btnFuente.setEnabled(false);
            } catch (Exception ex) {
                Logger.getLogger(FImportarArchivoExcelPacheco.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en fuente de archivos " + ex);
            }
        } else {
            // log.append("acción cancelada por el usuario." + newline);
        }
    }//GEN-LAST:event_btnEscogerFolderActionPerformed

    private void tblConsumosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblConsumosMouseClicked

    }//GEN-LAST:event_tblConsumosMouseClicked

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // this.getDesktopPane().add(fAgregarItemFactura);
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtCarpetasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCarpetasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCarpetasActionPerformed

    private void jbtnSubirDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSubirDatosActionPerformed
        new Thread(new HiloImportarConsumosCombustibeDesdeArchivo(this,"importarConsumosCombustible")).start();
        jbtnSubirDatos.setEnabled(false);
        btnEscogerFolder.setEnabled(false);
        txtCarpetas.setEnabled(false);
    }//GEN-LAST:event_jbtnSubirDatosActionPerformed

    private void txtProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProveedorActionPerformed

    private void txtProveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProveedorKeyPressed
       
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            search.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            search.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = search.getSelectedText();
            if (sucursalElegida) {
                // new Thread(new HiloIngresarSucursalProveedores(ini, this, "consultarSucursalProveedor")).start();
                traerSucursal();
                btnEscogerFolder.setEnabled(true);

            } else {

                //txtNombreSucursal.setText(text);
                // int valor = proveedor.getListaDeSucursales().size();
                // valor++;
                //txtCodigoInternoSucursal.setText("" + valor);
               // habilitar(true);
                menu.setVisible(false);

               

            }

        }
    }//GEN-LAST:event_txtProveedorKeyPressed

    private void txtProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProveedorKeyReleased
       if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtProveedor.getText().trim().toLowerCase();
            search.setData(search(text));
            if (search.getItemSize() > 0) {
                //  * 2 top and bot border
                menu.show(txtProveedor, 0, txtProveedor.getHeight());
                // menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                menu.setPopupSize(txtProveedor.getWidth(), (search.getItemSize() * 35) + 2);

            } else {
                menu.setVisible(false);
            }
        }
    }//GEN-LAST:event_txtProveedorKeyReleased

    private void txtProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtProveedorMouseClicked
     if (search.getItemSize() > 0) {
            menu.show(txtProveedor, 0, txtProveedor.getHeight());
            search.clearSelected();
        }
    }//GEN-LAST:event_txtProveedorMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barraSuperior;
    private javax.swing.JButton btCancelarOperacion;
    private javax.swing.JButton btnAgregarFactura;
    public javax.swing.JButton btnEscogerFolder;
    public javax.swing.JButton btnInsertarFila;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JButton jbtnGrabarConsumos;
    public javax.swing.JButton jbtnSubirDatos;
    public javax.swing.JTable tblConsumos;
    public javax.swing.JTextField txtCarpetas;
    public javax.swing.JTextArea txtObservaciones;
    public javax.swing.JTextField txtProveedor;
    // End of variables declaration//GEN-END:variables

    public void llenarCamposDeTexto() {
        try {

            int i = 0;
            boolean encontrado = false;
        } catch (Exception ex) {
            Logger.getLogger(FImportarConsumoCombustible.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al actualizar Campos de Formulario Ingresar empleados" + ex);
            //JOptionPane.showInternalMessageDialog(this, mensaje, "Error al actualizar Campos de Formulario Ingresar empleados \n" + ex, 0);

        }

    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion
     *
     */
    public void CargarVista() {
        // listaDeMantenimientos = new ArrayList<>();
        // new Thread(new HiloListadoDeTiposDeMantenimiento(this.ini, this.listaDeMantenimientos)).start();

        try {

               this.ini.setListaDeProveedores();
               this.ini.setListaDeSucursales();
               
        } catch (Exception ex) {
            Logger.getLogger(FImportarConsumoCombustible.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void limpiarTablaDetalleFactura() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tblConsumos.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

        } catch (Exception ex) {
        }

    }

    private void limpiarTablaListaDeFacturas() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tblConsumos.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

        } catch (Exception ex) {
        }

    }

    private void nuevo() {

        txtProveedor.setEnabled(true);
        txtProveedor.setEditable(true);
        btnNuevo.setEnabled(false);
        habilitar(true);
       
        
        txtProveedor.requestFocus();

    }

    private void grabar(boolean actualizar) throws HeadlessException {
        int x = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", JOptionPane.OK_CANCEL_OPTION);

        if (x == JOptionPane.OK_OPTION) {

            //  asignrValoresDocumento();
            //  new Thread(new HiloGuardarFacturaLogistica(this)).start();
        }
    }

    private void cancelarOperacion() {
        limpiarOperacion();
        btnNuevo.setEnabled(true);
        btnSalir.setEnabled(true);
    }

    public void limpiarOperacion() {

        btCancelarOperacion.setEnabled(false);
        btnAgregarFactura.setEnabled(false);
        btnEscogerFolder.setEnabled(false);
        jbtnGrabarConsumos.setEnabled(false);
        btnInsertarFila.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnSalir.setEnabled(false);

        jbtnGrabarConsumos.setEnabled(false);
        btnNuevo.setEnabled(true);
        actualizar = false;

        limpiarTablaDetalleFactura();
        limpiarTablaListaDeFacturas();

    }

    public void limpiarFacturas() {

        tblConsumos.setEnabled(false);
        btCancelarOperacion.setEnabled(false);
        btnAgregarFactura.setEnabled(false);
        btnEscogerFolder.setEnabled(false);
        jbtnGrabarConsumos.setEnabled(false);
        btnInsertarFila.setEnabled(false);
        btnNuevo.setEnabled(false);
        btnSalir.setEnabled(false);

        jbtnGrabarConsumos.setEnabled(false);
        btnNuevo.setEnabled(true);
        actualizar = false;

        limpiarTablaDetalleFactura();
        limpiarTablaListaDeFacturas();

    }

    private void habilitar(boolean val) {
    }

    public void elementoCambiado() {
    }

    private void llenarListaDeProveedore() {

        for (SucursalesPorproveedor sucursal : ini.getListaDeSucursales()) {

            if (sucursal.getNombreSucursal().contains(texto)) {

                textAutoCompleterProveedores.addItem(sucursal.getNombreSucursal());

            }
        }
    }

    private void llenarListaDeVehiculos() {

        for (CVehiculos vehiculo : ini.getListaDeCarrosPropios()) {

            textAutoCompleterVehiculos.addItem(vehiculo.getPlaca());

        }
    }

    public void llenarJtableFacturaMtto() {

        /*Se asignan los vaores al objeto factura*/
        facturaLogistica.setSucursalProveedor(sucursalProveedor.getIdSucursal());
        facturaLogistica.setNombreConductor(vehiculo.getNombreConductor());
        facturaLogistica.setApellidosConductor(vehiculo.getApellidosConductor());

        double suma = 0.0;
//        for (ItemsFacturaLogistica item : listaDeProductos) {
//            suma += item.getValorTotal();
//        }
        facturaLogistica.setValorfactura(suma);

        Date dt = new Date();

        facturaLogistica.setFechaFactura("" + dt);
        facturaLogistica.setZona(ini.getUser().getZona());
        facturaLogistica.setRegional(ini.getUser().getRegional());
        facturaLogistica.setAgencia(ini.getUser().getAgencia());
        facturaLogistica.setFormatoFotografia(".jpg");
        facturaLogistica.setActivo(1);
        facturaLogistica.setFlag(1);
        facturaLogistica.setUsuario(ini.Encriptexto(ini.getUser().getNombreUsuario()));

        limpiarTablaListaDeFacturas();

        /*se asignan los productos y servicios a la factura*/
//        facturaLogistica.setListaDeProductos(listaDeProductos);
//
//        for (FacturasLogisticas fac : listaFacturasLogisticas) {
//
//            /* se anexa el registro a la Jtable de facturas por manifiesto */
//            //modeloFacturas = (DefaultTableModel) tblListaDeFacturas.getModel();
//
//           // modeloFacturas.addRow(new Object[tblListaDeFacturas.getRowCount()]);
//
//        }
        limpiarTablaDetalleFactura();

        btnNuevo.setEnabled(true);
        btnNuevo.requestFocus();
        btnNuevo.requestFocus();

    }

    private void cancelarFactura() {

        /*Panel Factura*/
        //txtPlacaFactura.setText("");
        //txtkilometrajeFactura.setText("");
        /*Panel proveedor*/
        limpiarTablaDetalleFactura();

        facturaLogistica = null;

    }

    private void eliminarFacturaDelaTabla() {

        int x = 1; //tblListaDeFacturas.getSelectedRow();
        String numeroFactura = (String) modeloFacturas.getValueAt(x, 1);
        String proveedor = (String) modeloFacturas.getValueAt(x, 2);

        /*Se busca objeto factura en la lista para eliminarla*/
//        for (FacturasLogisticas fac : listaFacturasLogisticas) {
//            if (numeroFactura.equals(fac.getNumeroFactura()) && proveedor.equals(fac.getNombreSucursal())) {
//
//                /*La factura es encontrada en la lista y se elimina*/
//                listaFacturasLogisticas.remove(fac);
//            }
//
//        }

        /*Se elimina la fila de la tabla que contiene la factura*/
        modeloFacturas.removeRow(x);

        limpiarTablaListaDeFacturas();

//        for (FacturasLogisticas fac : listaFacturasLogisticas) {
//
//            /* se anexa el registro a la Jtable de facturas por manifiesto */
//            //modeloFacturas = (DefaultTableModel) tblListaDeFacturas.getModel();
//            int filaTabla2 =  1; //tblListaDeFacturas.getRowCount();
//
//            //modeloFacturas.addRow(new Object[tblListaDeFacturas.getRowCount()]);
//
//        }
        btnEscogerFolder.setEnabled(false);
        cancelarFactura();
    }

    private void eliminarFilaTblProductos() {

        int x = tblConsumos.getSelectedRow();

        String cuenta = (String) modeloDetalleFactura.getValueAt(x, 1);
        String producto = (String) modeloDetalleFactura.getValueAt(x, 2);

        /*Se busca objeto factura en la lista para eliminarla*/
//        for (ItemsFacturaLogistica itm : listaDeProductos) {
//
//            if (cuenta.equals(itm.getNombreCuentaSecundaria()) && producto.equals(itm.getDescripcionProductoServicio())) {
//
//                /*La factura es encontrada en la lista y se elimina*/
//                listaDeProductos.remove(itm);
//                break;
//            }
//
//        }

        /*Se elimina la fila de la tabla que contiene la factura*/
        modeloDetalleFactura.removeRow(x);

        limpiarTablaDetalleFactura();
        double valor = 0.0;

//        for (ItemsFacturaLogistica itm : listaDeProductos) {
//
//            /* se anexa el registro a la Jtable de facturas por manifiesto */
//            //modeloFacturas = (DefaultTableModel) tblListaDeFacturas.getModel();
//            int filaTabla2 = tblConsumos.getRowCount();
//
//            modeloDetalleFactura.addRow(new Object[tblConsumos.getRowCount()]);
//
//            tblConsumos.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);
//            tblConsumos.setValueAt(itm.getNombreCuentaSecundaria(), filaTabla2, 1);
//            tblConsumos.setValueAt(itm.getDescripcionProductoServicio(), filaTabla2, 2);
//            tblConsumos.setValueAt(itm.getCantidad(), filaTabla2, 3);
//            tblConsumos.setValueAt(nf.format(itm.getValorUnitario()), filaTabla2, 4);
//            //form.tblDetalleFactura.setValueAt(nf.format(facturaActual.getValorTotalFactura()), filaTabla2, 3);
//            tblConsumos.setValueAt(nf.format(itm.getValorTotal()), filaTabla2, 5);
//
//            valor += itm.getValorTotal();
//        }
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
                    //+ "cedula ='" + proveedor.getCedula() + "' AND "
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
 private void traerSucursal() {

         
        for (SucursalesPorproveedor suc : ini.getListaDeSucursales()) {
            if (suc.getNombreSucursal().equals(txtProveedor.getText().trim())) {
                this.sucursalProveedor = suc;
                for(Cproveedores proveedor : ini.getListaDeProveedores()){
                    if(suc.getCedula().equals(proveedor.getCedula())){
                        this.proveedor = proveedor;
                        break;
                    }
                    
                }
                
//                txtDireccion.setText(sucursal.getDireccionSucursal());
//                txtBarrio.setText(sucursal.getBarrioSucursal());
//                txtTelefono.setText(sucursal.getTelefonoSucursal());
//                txtCelular.setText(sucursal.getCelularSucursal());
//                txtContacto.setText(sucursal.getContactoSucursal());
//
//                txtNumeroOrden.setEnabled(true);
//                txtNumeroFactura.setEnabled(true);
//                txtPlaca.setEnabled(true);
//                txtNombreConductor.setEnabled(true);
//                txtkilometraje.setEnabled(true);
//                dateFechaIngreso.setEnabled(true);
//                btnAgregar.setEnabled(true);
//
//                txtNumeroOrden.setEditable(true);
//                txtNumeroFactura.setEditable(true);
//                txtPlaca.setEditable(true);
//                txtNombreConductor.setEditable(true);
//                txtkilometraje.setEditable(true);

                //txtCarpetas.setEnabled(true);
               // txtCarpetas.setEditable(true);
               // txtCarpetas.requestFocus();

                break;
            }
        }
    }


}
