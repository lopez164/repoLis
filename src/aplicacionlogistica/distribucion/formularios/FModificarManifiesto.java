/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.Threads.HiloModificarManifiesto;
import aplicacionlogistica.distribucion.imprimir.RepporteRuteroConductoresConPesosyDEscuentos;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.awt.Color;
import java.awt.Component;
import mtto.vehiculos.CCarros;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import mtto.vehiculos.CVehiculos;
import ui.swing.searchText.DataSearch;
import ui.swing.searchText.EventClick;
import ui.swing.searchText.PanelSearch;

/**
 * Esta vista, permite que el usuario pueda asignar a un manifiesto de
 * Distribución el conductor, el auxiliar, un vehiculo dando una ruta y
 * clasificandolo dentro de un cnal de venta; el sistema se encrga de crear el
 * manifiestos y posteriormente se le asignan las factura que van a salir a
 * distrbucion.
 *
 * @author Luis Eduardo López
 */
public class FModificarManifiesto extends javax.swing.JInternalFrame {
    
    public CEmpleados conductor = null;
    public CEmpleados auxiliar1 = null;
    public CEmpleados auxiliar2 = null;
    public CEmpleados auxiliar3 = null;
    public CEmpleados despachador = null;
    public List<CEmpleados> listaDeAuxiliares = new ArrayList<>();
    //public CCarros carro = null;
    public CManifiestosDeDistribucion manifiestoActual = null;
    public CRutasDeDistribucion ruta;
    public CCanalesDeVenta canalDeVenta;
    
    public Inicio ini = null;
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public DefaultTableModel modelo1, modelo2;
    
    int filaTabla2;
    int kilometraje = 0;
    String mensaje = null;

    // public TextAutoCompleter autoTxtVehiculos;
    private JPopupMenu menuPlacas;
    private JPopupMenu menuConductor;
    private JPopupMenu menuAuxiliar1;
    private JPopupMenu menuAuxiliar2;
    private JPopupMenu menuDespachador;
    
    private PanelSearch searchPlaca;
    private PanelSearch searchConductor;
    private PanelSearch searchAuxiliar1;
    private PanelSearch searchAuxiliar2;
    private PanelSearch searchDespachador;
    
    boolean elementoElegido = false;

    //double sumadorPesosFacturas = 0.0;
    /**
     * Creates new fReportemovilizadoPorConductor
     * IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public FModificarManifiesto(Inicio ini) throws Exception {
        this.ini = ini;
        
        try {
            initComponents();
            this.ini = ini;
            dateManifiesto.setDate(new Date());
            cbxRutaDeDistribucion.removeAllItems();
            cbxCanales.removeAllItems();
            
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
            if (ini.getListaDeVehiculos() == null) {
                ini.setListaDeVehiculos(1);
            }
            
            menuPlacas = new JPopupMenu();
            menuConductor = new JPopupMenu();
            menuAuxiliar1 = new JPopupMenu();
            menuAuxiliar2 = new JPopupMenu();
            menuDespachador = new JPopupMenu();
            
            searchPlaca = new PanelSearch();
            menuPlacas.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
            menuPlacas.add(searchPlaca);
            menuPlacas.setFocusable(false);
            searchPlaca.addEventClick(new EventClick() {
                @Override
                public void itemClick(DataSearch data) {
                    menuPlacas.setVisible(false);
                    txtPlaca.setText(data.getText());
                   txtKmDeSalida.setText("0");
                    //addStory(data.getText());
                    System.out.println("Click Item : " + data.getText());
                    elementoElegido = true;
                                       txtKmDeSalida.requestFocus();
                    
                }
                
                @Override
                public void itemRemove(Component com, DataSearch data) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            
            searchConductor = new PanelSearch();
            menuConductor.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
            menuConductor.add(searchConductor);
            menuConductor.setFocusable(false);
            searchConductor.addEventClick(new EventClick() {
                @Override
                public void itemClick(DataSearch data) {
                    menuConductor.setVisible(false);
                    txtnombreDeConductor.setText(data.getText());
                    addStory(data.getText());
                    System.out.println("Click Item : " + data.getText());
                    txtKmDeSalida.requestFocus();
                    elementoElegido = true;
                }
                
                @Override
                public void itemRemove(Component com, DataSearch data) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            
            searchAuxiliar1 = new PanelSearch();
            menuAuxiliar1.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
            menuAuxiliar1.add(searchAuxiliar1);
            menuAuxiliar1.setFocusable(false);
            searchAuxiliar1.addEventClick(new EventClick() {
                @Override
                public void itemClick(DataSearch data) {
                    menuAuxiliar1.setVisible(false);
                    txtNombreDeAuxiliar1.setText(data.getText());
                    addStory(data.getText());
                    System.out.println("Click Item : " + data.getText());
                    txtKmDeSalida.requestFocus();
                    elementoElegido = true;
                }
                
                @Override
                public void itemRemove(Component com, DataSearch data) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            
            searchAuxiliar2 = new PanelSearch();
            menuAuxiliar2.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
            menuAuxiliar2.add(searchAuxiliar2);
            menuAuxiliar2.setFocusable(false);
            searchAuxiliar2.addEventClick(new EventClick() {
                @Override
                public void itemClick(DataSearch data) {
                    menuAuxiliar2.setVisible(false);
                    txtNombreDeAuxiliar2.setText(data.getText());
                    addStory(data.getText());
                    System.out.println("Click Item : " + data.getText());
                    txtKmDeSalida.requestFocus();
                    elementoElegido = true;
                }
                
                @Override
                public void itemRemove(Component com, DataSearch data) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            
            searchDespachador = new PanelSearch();
            menuDespachador.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
            menuDespachador.add(searchDespachador);
            menuDespachador.setFocusable(false);
            searchDespachador.addEventClick(new EventClick() {
                @Override
                public void itemClick(DataSearch data) {
                    menuDespachador.setVisible(false);
                    txtNombreDedespachador.setText(data.getText());
                    addStory(data.getText());
                    System.out.println("Click Item : " + data.getText());
                    txtKmDeSalida.requestFocus();
                    elementoElegido = true;
                }
                
                @Override
                public void itemRemove(Component com, DataSearch data) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });

            // SE LLENAN LAS LISTAS DESPLEGABLES
            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                if (obj.getActivoCanal() == 1) {
                    cbxCanales.addItem(obj.getNombreCanalDeVenta());
                }
                
            }
            for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                if (obj.getActivoRutasDeDistribucion() == 1) {
                    cbxRutaDeDistribucion.addItem(obj.getNombreRutasDeDistribucion());
                }
            }
            
            lblCirculoDeProgreso1.setVisible(false);
            
        } catch (Exception ex) {
            Logger.getLogger(FModificarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
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

        mnuBorrarFilas = new javax.swing.JPopupMenu();
        borraElementos = new javax.swing.JMenu();
        borrar1Fila = new javax.swing.JMenuItem();
        borrarTodo = new javax.swing.JMenuItem();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaDocsVencidos = new javax.swing.JTable();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbxRutaDeDistribucion = new javax.swing.JComboBox();
        cbxCanales = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dateManifiesto = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFacturasPorManifiesto = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtnombreDeConductor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtNombreDeAuxiliar1 = new javax.swing.JTextField();
        lblCirculoDeProgreso1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblKilometros = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtNombreDeAuxiliar2 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtNombreDedespachador = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNumeroDeManifiesto = new javax.swing.JTextField();
        txtPlaca = new javax.swing.JTextField();
        txtKmDeSalida = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCantidadDePedidos = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPesoKgManifiesto = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtValorTotalManifiesto = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();
        btnActualizar = new javax.swing.JToggleButton();

        borraElementos.setText("Borrar Una Fila");

        borrar1Fila.setText("Borrar Factura");
        borrar1Fila.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                borrar1FilaMouseClicked(evt);
            }
        });
        borrar1Fila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrar1FilaActionPerformed(evt);
            }
        });
        borraElementos.add(borrar1Fila);

        borrarTodo.setText("Borrar todas las facturas");
        borrarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarTodoActionPerformed(evt);
            }
        });
        borraElementos.add(borrarTodo);

        mnuBorrarFilas.add(borraElementos);

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
        setTitle("Formulario para modifcar manifiestos en Ruta");
        setAutoscrolls(true);
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

        jPanel5.setAutoscrolls(true);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

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
                .addGap(2, 2, 2)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Manifiesto de Reparto"));
        jPanel7.setEnabled(false);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Ruta");

        cbxRutaDeDistribucion.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        cbxRutaDeDistribucion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxRutaDeDistribucion.setEnabled(false);
        cbxRutaDeDistribucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxRutaDeDistribucionActionPerformed(evt);
            }
        });

        cbxCanales.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        cbxCanales.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxCanales.setEnabled(false);
        cbxCanales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCanalesActionPerformed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Canal");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Fecha");

        dateManifiesto.setToolTipText("Seleccionar la fecha ");
        dateManifiesto.setDateFormatString("yyyy/MM/dd");
        dateManifiesto.setEnabled(false);
        dateManifiesto.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxCanales, 0, 266, Short.MAX_VALUE)
                    .addComponent(cbxRutaDeDistribucion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxCanales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(1, 1, 1)
                        .addComponent(cbxRutaDeDistribucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(0, 92, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableFacturasPorManifiesto.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jTableFacturasPorManifiesto.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableFacturasPorManifiesto.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableFacturasPorManifiesto.setComponentPopupMenu(mnuBorrarFilas);
        jTableFacturasPorManifiesto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFacturasPorManifiestoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableFacturasPorManifiesto);
        if (jTableFacturasPorManifiesto.getColumnModel().getColumnCount() > 0) {
            jTableFacturasPorManifiesto.getColumnModel().getColumn(0).setResizable(false);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(1).setResizable(false);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(1).setPreferredWidth(65);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(2).setResizable(false);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(2).setPreferredWidth(230);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(3).setResizable(false);
            jTableFacturasPorManifiesto.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Placa");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Conductor");

        txtnombreDeConductor.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtnombreDeConductor.setToolTipText("");
        txtnombreDeConductor.setEnabled(false);
        txtnombreDeConductor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtnombreDeConductorFocusGained(evt);
            }
        });
        txtnombreDeConductor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtnombreDeConductorMouseClicked(evt);
            }
        });
        txtnombreDeConductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreDeConductorActionPerformed(evt);
            }
        });
        txtnombreDeConductor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnombreDeConductorKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtnombreDeConductorKeyReleased(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Auxiliar 1");

        txtNombreDeAuxiliar1.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreDeAuxiliar1.setToolTipText("Ingresar apellidos completos");
        txtNombreDeAuxiliar1.setAutoscrolls(false);
        txtNombreDeAuxiliar1.setEnabled(false);
        txtNombreDeAuxiliar1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDeAuxiliar1FocusGained(evt);
            }
        });
        txtNombreDeAuxiliar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNombreDeAuxiliar1MouseClicked(evt);
            }
        });
        txtNombreDeAuxiliar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDeAuxiliar1ActionPerformed(evt);
            }
        });
        txtNombreDeAuxiliar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDeAuxiliar1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreDeAuxiliar1KeyReleased(evt);
            }
        });

        lblCirculoDeProgreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Kilometraje");

        lblKilometros.setText("kms");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Auxiliar 2");

        txtNombreDeAuxiliar2.setEditable(false);
        txtNombreDeAuxiliar2.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreDeAuxiliar2.setToolTipText("Ingresar apellidos completos");
        txtNombreDeAuxiliar2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDeAuxiliar2FocusGained(evt);
            }
        });
        txtNombreDeAuxiliar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNombreDeAuxiliar2MouseClicked(evt);
            }
        });
        txtNombreDeAuxiliar2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDeAuxiliar2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreDeAuxiliar2KeyReleased(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Despachador");

        txtNombreDedespachador.setEditable(false);
        txtNombreDedespachador.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNombreDedespachador.setToolTipText("Ingresar apellidos completos");
        txtNombreDedespachador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDedespachadorFocusGained(evt);
            }
        });
        txtNombreDedespachador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNombreDedespachadorMouseClicked(evt);
            }
        });
        txtNombreDedespachador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDedespachadorActionPerformed(evt);
            }
        });
        txtNombreDedespachador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDedespachadorKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreDedespachadorKeyReleased(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Manfiesto N°");

        txtNumeroDeManifiesto.setEditable(false);
        txtNumeroDeManifiesto.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtNumeroDeManifiesto.setToolTipText("Ingresar apellidos completos");
        txtNumeroDeManifiesto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeManifiestoFocusGained(evt);
            }
        });
        txtNumeroDeManifiesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeManifiestoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumeroDeManifiestoKeyReleased(evt);
            }
        });

        txtPlaca.setEnabled(false);
        txtPlaca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPlacaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPlacaFocusLost(evt);
            }
        });
        txtPlaca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPlacaMouseClicked(evt);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPlacaKeyReleased(evt);
            }
        });

        txtKmDeSalida.setEnabled(false);
        txtKmDeSalida.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKmDeSalidaFocusGained(evt);
            }
        });
        txtKmDeSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKmDeSalidaActionPerformed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Cant. Pedidos");

        txtCantidadDePedidos.setEditable(false);
        txtCantidadDePedidos.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtCantidadDePedidos.setToolTipText("Ingresar apellidos completos");
        txtCantidadDePedidos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCantidadDePedidosFocusGained(evt);
            }
        });
        txtCantidadDePedidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadDePedidosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantidadDePedidosKeyReleased(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Peso Total");

        txtPesoKgManifiesto.setEditable(false);
        txtPesoKgManifiesto.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtPesoKgManifiesto.setToolTipText("Ingresar apellidos completos");
        txtPesoKgManifiesto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPesoKgManifiestoFocusGained(evt);
            }
        });
        txtPesoKgManifiesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesoKgManifiestoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesoKgManifiestoKeyReleased(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Valor Mfto.");

        txtValorTotalManifiesto.setEditable(false);
        txtValorTotalManifiesto.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        txtValorTotalManifiesto.setToolTipText("Ingresar apellidos completos");
        txtValorTotalManifiesto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorTotalManifiestoFocusGained(evt);
            }
        });
        txtValorTotalManifiesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorTotalManifiestoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtValorTotalManifiestoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreDedespachador)
                            .addComponent(txtNombreDeAuxiliar2)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtPesoKgManifiesto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                                        .addComponent(txtCantidadDePedidos, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(txtValorTotalManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtPlaca, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtKmDeSalida)
                                            .addComponent(txtNumeroDeManifiesto, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                                        .addGap(1, 1, 1)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblKilometros)
                                            .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, 0))
                            .addComponent(txtnombreDeConductor)
                            .addComponent(txtNombreDeAuxiliar1))))
                .addGap(58, 58, 58))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNumeroDeManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidadDePedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPesoKgManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorTotalManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblKilometros)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKmDeSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnombreDeConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDeAuxiliar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNombreDeAuxiliar2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDedespachador, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setToolTipText("agregar registro");
        jBtnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo.setFocusable(false);
        jBtnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnNuevo.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnNuevo);

        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jBtnGrabar.setToolTipText("grabar registro");
        jBtnGrabar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnGrabar.setEnabled(false);
        jBtnGrabar.setFocusable(false);
        jBtnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGrabar.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGrabarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGrabar);

        jBtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jBtnImprimir.setToolTipText("imprimir mfto.");
        jBtnImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnImprimir.setEnabled(false);
        jBtnImprimir.setFocusable(false);
        jBtnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnImprimir.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnImprimirActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnImprimir);

        jBtnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancel.setToolTipText("Cancelar operacion");
        jBtnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnCancel.setFocusable(false);
        jBtnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCancel.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnCancel);

        jBtnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jBtnExit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExit.setFocusable(false);
        jBtnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExit.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExitActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExit);

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Sync.png"))); // NOI18N
        btnActualizar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnActualizar.setFocusable(false);
        btnActualizar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnActualizar.setPreferredSize(new java.awt.Dimension(25, 25));
        btnActualizar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnActualizar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        
        nuevo();
        

    }//GEN-LAST:event_btnNuevoActionPerformed
    

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        
        new Thread(new HiloModificarManifiesto(ini, this, "grabarManifiesto")).start();
        

    }//GEN-LAST:event_btnGrabarActionPerformed
    

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
        

    }//GEN-LAST:event_btnCancelarActionPerformed
    

    private void jTableFacturasPorManifiestoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFacturasPorManifiestoMouseClicked
        

    }//GEN-LAST:event_jTableFacturasPorManifiestoMouseClicked

    private void txtnombreDeConductorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnombreDeConductorFocusGained
        txtnombreDeConductor.setSelectionStart(0);
        txtnombreDeConductor.setSelectionEnd(txtnombreDeConductor.getText().length());
    }//GEN-LAST:event_txtnombreDeConductorFocusGained

    private void txtnombreDeConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreDeConductorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            searchConductor.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            searchConductor.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = searchConductor.getSelectedText();
            if (elementoElegido) {
                txtKmDeSalida.requestFocus();
            } else {
                
                txtnombreDeConductor.setText(text);
                menuConductor.setVisible(false);
                
            }
        }
    }//GEN-LAST:event_txtnombreDeConductorKeyPressed
    

    private void txtNombreDeAuxiliar1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar1FocusGained
        txtNombreDeAuxiliar1.setSelectionStart(0);
        txtNombreDeAuxiliar1.setSelectionEnd(txtNombreDeAuxiliar1.getText().length());
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar1FocusGained

    private void txtNombreDeAuxiliar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            searchAuxiliar1.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            searchAuxiliar1.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = searchAuxiliar1.getSelectedText();
            if (elementoElegido) {
                txtKmDeSalida.requestFocus();
            } else {
                
                txtNombreDeAuxiliar1.setText(text);
                menuAuxiliar1.setVisible(false);
            }
        }
    }//GEN-LAST:event_txtNombreDeAuxiliar1KeyPressed

    private void tablaDocsVencidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsVencidosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDocsVencidosMouseClicked

    private void txtNumeroDeManifiestoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeManifiestoFocusGained
        txtNumeroDeManifiesto.setSelectionStart(0);
        txtNumeroDeManifiesto.setSelectionEnd(txtNumeroDeManifiesto.getText().length());
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeManifiestoFocusGained

    private void txtNumeroDeManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeManifiestoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            new Thread(new HiloModificarManifiesto(ini, this, "buscarManifiesto")).start();
            txtNumeroDeManifiesto.setEnabled(false);
        }
    }//GEN-LAST:event_txtNumeroDeManifiestoKeyPressed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        if (manifiestoActual != null) {
            
            manifiestoActual.liberarManifiesto(true);
            
        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        
        imprimir();

    }//GEN-LAST:event_btnImprimirActionPerformed
    

    private void borrar1FilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrar1FilaMouseClicked
        

    }//GEN-LAST:event_borrar1FilaMouseClicked

    private void borrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarTodoActionPerformed

    }//GEN-LAST:event_borrarTodoActionPerformed

    private void borrar1FilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrar1FilaActionPerformed

    }//GEN-LAST:event_borrar1FilaActionPerformed
    

    private void txtNombreDeAuxiliar2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar2FocusGained
        txtNombreDeAuxiliar2.setSelectionStart(0);
        txtNombreDeAuxiliar2.setSelectionEnd(txtNombreDeAuxiliar2.getText().length());
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar2FocusGained

    private void txtNombreDeAuxiliar2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            searchAuxiliar2.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            searchAuxiliar2.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = searchAuxiliar2.getSelectedText();
            if (elementoElegido) {
                txtKmDeSalida.requestFocus();
            } else {
                
                txtNombreDeAuxiliar2.setText(text);
                menuAuxiliar2.setVisible(false);
                
            }
        }
    }//GEN-LAST:event_txtNombreDeAuxiliar2KeyPressed

    private void txtNombreDedespachadorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDedespachadorFocusGained
        txtNombreDedespachador.setSelectionStart(0);
        txtNombreDedespachador.setSelectionEnd(txtNombreDedespachador.getText().length());// TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDedespachadorFocusGained

    private void txtNombreDedespachadorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDedespachadorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            searchDespachador.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            searchDespachador.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = searchDespachador.getSelectedText();
            if (elementoElegido) {
                txtKmDeSalida.requestFocus();
            } else {
                
                txtNombreDedespachador.setText(text);
                menuDespachador.setVisible(false);
                
            }
        }
    }//GEN-LAST:event_txtNombreDedespachadorKeyPressed

    private void cbxRutaDeDistribucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxRutaDeDistribucionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxRutaDeDistribucionActionPerformed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();

    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnImprimirActionPerformed
        imprimir();
    }//GEN-LAST:event_jBtnImprimirActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        cancelar();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        salir();        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnExitActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
        grabar();
    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void txtnombreDeConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreDeConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreDeConductorActionPerformed

    private void txtNombreDedespachadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDedespachadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDedespachadorActionPerformed

    private void cbxCanalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCanalesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxCanalesActionPerformed

    private void txtKmDeSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKmDeSalidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmDeSalidaActionPerformed

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void txtNumeroDeManifiestoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeManifiestoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeManifiestoKeyReleased

    private void txtNombreDeAuxiliar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar1ActionPerformed

    private void txtPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            searchPlaca.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            searchPlaca.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = searchPlaca.getSelectedText();
            if (elementoElegido) {
              //   new Thread(new HiloModificarManifiesto(ini, this, "actualizarKilometraje")).start();
                //txtKmDeSalida.requestFocus();
            } else {
                
                txtPlaca.setText(text);
                menuPlacas.setVisible(false);
               

                
            }
        }
    }//GEN-LAST:event_txtPlacaKeyPressed

    private void txtPlacaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPlacaMouseClicked
        if (searchPlaca.getItemSize() > 0) {
            menuPlacas.show(txtPlaca, 0, txtPlaca.getHeight());
            searchPlaca.clearSelected();
        }
    }//GEN-LAST:event_txtPlacaMouseClicked

    private void txtPlacaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtPlaca.getText().trim();
            searchPlaca.setData(searchPlaca(text));
            if (searchPlaca.getItemSize() > 0) {
                //  * 2 top and bot border
                menuPlacas.show(txtPlaca, 0, txtPlaca.getHeight());
                // menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                menuPlacas.setPopupSize(txtPlaca.getWidth(), (searchPlaca.getItemSize() * 35) + 2);
                
            } else {
                menuPlacas.setVisible(false);
            }
        }
    }//GEN-LAST:event_txtPlacaKeyReleased

    private void txtnombreDeConductorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreDeConductorKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtnombreDeConductor.getText().trim();
            searchConductor.setData(searchConductor(text));
            if (searchConductor.getItemSize() > 0) {
                //  * 2 top and bot border
                menuConductor.show(txtnombreDeConductor, 0, txtnombreDeConductor.getHeight());
                // menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                menuConductor.setPopupSize(txtnombreDeConductor.getWidth(), (searchConductor.getItemSize() * 35) + 2);
                
            } else {
                menuConductor.setVisible(false);
            }
        }       // TODO add your handling code here:
    }//GEN-LAST:event_txtnombreDeConductorKeyReleased

    private void txtnombreDeConductorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtnombreDeConductorMouseClicked
        if (searchConductor.getItemSize() > 0) {
            menuConductor.show(txtnombreDeConductor, 0, txtnombreDeConductor.getHeight());
            searchConductor.clearSelected();
        }
    }//GEN-LAST:event_txtnombreDeConductorMouseClicked

    private void txtNombreDeAuxiliar1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar1KeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtNombreDeAuxiliar1.getText().trim();
            searchAuxiliar1.setData(searchAuxiliar1(text));
            if (searchAuxiliar1.getItemSize() > 0) {
                //  * 2 top and bot border
                menuAuxiliar1.show(txtNombreDeAuxiliar1, 0, txtNombreDeAuxiliar1.getHeight());
                // menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                menuAuxiliar1.setPopupSize(txtNombreDeAuxiliar1.getWidth(), (searchAuxiliar1.getItemSize() * 35) + 2);
                
            } else {
                menuAuxiliar1.setVisible(false);
            }
        }       // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar1KeyReleased

    private void txtNombreDeAuxiliar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar1MouseClicked
        if (searchAuxiliar1.getItemSize() > 0) {
            menuAuxiliar1.show(txtNombreDeAuxiliar1, 0, txtNombreDeAuxiliar1.getHeight());
            searchAuxiliar1.clearSelected();
        }
    }//GEN-LAST:event_txtNombreDeAuxiliar1MouseClicked

    private void txtNombreDeAuxiliar2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar2KeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtNombreDeAuxiliar2.getText().trim();
            searchAuxiliar2.setData(searchAuxiliar2(text));
            if (searchAuxiliar2.getItemSize() > 0) {
                //  * 2 top and bot border
                menuAuxiliar2.show(txtNombreDeAuxiliar2, 0, txtNombreDeAuxiliar2.getHeight());
                // menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                menuAuxiliar2.setPopupSize(txtNombreDeAuxiliar2.getWidth(), (searchAuxiliar2.getItemSize() * 35) + 2);
                
            } else {
                menuAuxiliar2.setVisible(false);
            }
        }       // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar2KeyReleased

    private void txtNombreDeAuxiliar2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreDeAuxiliar2MouseClicked
        if (searchAuxiliar2.getItemSize() > 0) {
            menuAuxiliar2.show(txtNombreDeAuxiliar2, 0, txtNombreDeAuxiliar2.getHeight());
            searchAuxiliar2.clearSelected();
        }       // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeAuxiliar2MouseClicked

    private void txtNombreDedespachadorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDedespachadorKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtNombreDedespachador.getText().trim();
            searchDespachador.setData(searchDespachador1(text));
            if (searchDespachador.getItemSize() > 0) {
                //  * 2 top and bot border
                menuDespachador.show(txtNombreDedespachador, 0, txtNombreDedespachador.getHeight());
                // menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                menuDespachador.setPopupSize(txtNombreDedespachador.getWidth(), (searchDespachador.getItemSize() * 35) + 2);
                
            } else {
                menuDespachador.setVisible(false);
            }
        }
    }//GEN-LAST:event_txtNombreDedespachadorKeyReleased

    private void txtNombreDedespachadorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreDedespachadorMouseClicked
        if (searchDespachador.getItemSize() > 0) {
            menuDespachador.show(txtNombreDedespachador, 0, txtNombreDedespachador.getHeight());
            searchDespachador.clearSelected();
        }
    }//GEN-LAST:event_txtNombreDedespachadorMouseClicked

    private void txtPlacaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusGained
        txtPlaca.setSelectionStart(0);
        txtPlaca.setSelectionEnd(txtPlaca.getText().length());// TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaFocusGained

    private void txtKmDeSalidaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKmDeSalidaFocusGained
       CCarros carro = new CCarros(ini);
                    carro.setPlaca(txtPlaca.getText());
                    carro.setKilometrajeActual();
                    txtKmDeSalida.setText("" + carro.getKilometrajeActual());    
        txtKmDeSalida.setSelectionStart(0);
        txtKmDeSalida.setSelectionEnd(txtKmDeSalida.getText().length());// TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKmDeSalidaFocusGained

    private void txtPlacaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaFocusLost
                        

    }//GEN-LAST:event_txtPlacaFocusLost

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
  
      new Thread(new HiloModificarManifiesto(ini, this, "atualizarManifiestos")).start();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtCantidadDePedidosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCantidadDePedidosFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadDePedidosFocusGained

    private void txtCantidadDePedidosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadDePedidosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadDePedidosKeyPressed

    private void txtCantidadDePedidosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadDePedidosKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadDePedidosKeyReleased

    private void txtPesoKgManifiestoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPesoKgManifiestoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoKgManifiestoFocusGained

    private void txtPesoKgManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesoKgManifiestoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoKgManifiestoKeyPressed

    private void txtPesoKgManifiestoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesoKgManifiestoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoKgManifiestoKeyReleased

    private void txtValorTotalManifiestoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorTotalManifiestoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorTotalManifiestoFocusGained

    private void txtValorTotalManifiestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorTotalManifiestoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorTotalManifiestoKeyPressed

    private void txtValorTotalManifiestoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorTotalManifiestoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorTotalManifiestoKeyReleased
    
    public boolean validarCarro() {
        boolean existe = false;
        for (CVehiculos veh : ini.getListaDeVehiculos()) {
            if (txtPlaca.getText().trim().equals(veh.getPlaca())) {
                existe = true;
                break;
            }
        }
        return existe;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu borraElementos;
    private javax.swing.JMenuItem borrar1Fila;
    private javax.swing.JMenuItem borrarTodo;
    public javax.swing.JToggleButton btnActualizar;
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnImprimir;
    public javax.swing.JButton btnNuevo;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JComboBox cbxCanales;
    public javax.swing.JComboBox cbxRutaDeDistribucion;
    public com.toedter.calendar.JDateChooser dateManifiesto;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnImprimir;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JTable jTableFacturasPorManifiesto;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso1;
    private javax.swing.JLabel lblKilometros;
    private javax.swing.JPopupMenu mnuBorrarFilas;
    private javax.swing.JTable tablaDocsVencidos;
    public javax.swing.JTextField txtCantidadDePedidos;
    public javax.swing.JTextField txtKmDeSalida;
    public javax.swing.JTextField txtNombreDeAuxiliar1;
    public javax.swing.JTextField txtNombreDeAuxiliar2;
    public javax.swing.JTextField txtNombreDedespachador;
    public javax.swing.JTextField txtNumeroDeManifiesto;
    public javax.swing.JTextField txtPesoKgManifiesto;
    public javax.swing.JTextField txtPlaca;
    public javax.swing.JTextField txtValorTotalManifiesto;
    public javax.swing.JTextField txtnombreDeConductor;
    // End of variables declaration//GEN-END:variables

    public void nuevo() {
        try {
            cancelar();
            limpiar();
            dateManifiesto.setEnabled(true);
            dateManifiesto.setDate(new Date());
            
            jPanel7.setEnabled(true);
            
            txtnombreDeConductor.setEditable(false);
            
            txtNombreDeAuxiliar1.setEditable(false);
            txtNumeroDeManifiesto.setEnabled(true);
            txtNumeroDeManifiesto.setEditable(true);
            cbxCanales.setEnabled(false);
            cbxRutaDeDistribucion.setEnabled(false);
            dateManifiesto.setEnabled(false);
            
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            
            conductor = null;
            manifiestoActual = null;
            listaDeAuxiliares = new ArrayList();
            
            manifiestoActual = new CManifiestosDeDistribucion(ini);
            manifiestoActual.setEstadoManifiesto(0);
            
            txtNumeroDeManifiesto.requestFocus();
            
        } catch (Exception ex) {
            Logger.getLogger(FModificarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean grabar() throws HeadlessException {
        int deseaGrabarRegistro = 0;
        
        deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea Actualizar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);
        
        if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {
            
            try {
                /*Se crea un objeto ruta de distribucion*/
                for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                    if (obj.getNombreRutasDeDistribucion().equals(cbxRutaDeDistribucion.getSelectedItem().toString())) {
                        ruta = new CRutasDeDistribucion(ini);
                        ruta = obj;
                        break;
                    }
                }

                /*Se crea un objeto canal de distribucion*/
                for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                    if (obj.getNombreCanalDeVenta().equals(cbxCanales.getSelectedItem().toString())) {
                        canalDeVenta = new CCanalesDeVenta(ini);
                        canalDeVenta = obj;
                        break;
                    }
                }
                
                txtKmDeSalida.setEditable(false);
                txtPlaca.setEnabled(false);
                txtPlaca.setEditable(false);

                /*  SE LLENAN LAS PROPIEDADES DE  EL MANIFIESTO */
                Date dt = new Date();
                dt = ini.getFechaSql(dateManifiesto);
                manifiestoActual.setFechaDistribucion("" + dt);
                
                manifiestoActual.setVehiculo(txtPlaca.getText().trim());
                manifiestoActual.setConductor(conductor.getCedula());
                
                manifiestoActual.setDespachador(despachador.getCedula());
                
                manifiestoActual.setIdCanal(canalDeVenta.getIdCanalDeVenta());
                
                manifiestoActual.setIdRuta(ruta.getIdRutasDeDistribucion());
                
                manifiestoActual.setKmSalida(Integer.parseInt(txtKmDeSalida.getText().trim()));
                
                SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String fechax;
                fechax = fecha.format(new Date());
                
                manifiestoActual.setUsuarioManifiesto(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));

                /*SE ASIGNAN LOS AUXILIARES */
                manifiestoActual.setListaDeAuxiliares(listaDeAuxiliares);
                
                if (manifiestoActual.actualizarManifiestoDeDistribucions()) {
                    JOptionPane.showMessageDialog(null, "Manifiesto de distribucion modificado satisfactoriamente", "manifiesto modificado", JOptionPane.INFORMATION_MESSAGE);
                    btnGrabar.setEnabled(false);
                    jBtnGrabar.setEnabled(false);
                    btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
                    jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
                    
                    new Thread(new HiloListadoDeManifiestosSinDescargar(ini)).start();;
                    
                    return true;
                } else {
                    btnGrabar.setEnabled(false);
                    jBtnGrabar.setEnabled(false);
                    btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
                    jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
                    JOptionPane.showMessageDialog(null, "Error al guardar los datos", "manifiesto ya fue guardado", JOptionPane.ERROR_MESSAGE, null);
                    return false;
                }  // FIN DEL IF-> ESTADO DEL MANIFIESTO
            } catch (Exception ex) {
                Logger.getLogger(FModificarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        return false;
        
    }
    
    public void imprimir() {
        /*valida sin pendientes del conductor */
        try {

            /* Genera el manifiesto  Genera el rutero*/
            RepporteRuteroConductoresConPesosyDEscuentos demo = new RepporteRuteroConductoresConPesosyDEscuentos(ini, manifiestoActual);
            
        } catch (Exception ex) {
            Logger.getLogger(FModificarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cancelar() {
        limpiar();
        
        dateManifiesto.setEnabled(false);
        
        txtnombreDeConductor.setEditable(false);
        
        txtNombreDeAuxiliar1.setEditable(false);
        txtNombreDeAuxiliar2.setEditable(false);
        txtNombreDedespachador.setEditable(false);
        
        txtKmDeSalida.setEditable(false);
        txtPlaca.setEditable(false);
        txtKmDeSalida.setEnabled(false);
        txtKmDeSalida.setEditable(false);
        cbxCanales.setEnabled(false);
        cbxRutaDeDistribucion.setEnabled(false);
        dateManifiesto.setEnabled(false);
        
        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);
        
        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);
        
        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);
        
        if (manifiestoActual != null) {
            manifiestoActual.liberarManifiesto(true);
        }
        
        txtNumeroDeManifiesto.setEditable(false);
        manifiestoActual = null;
        conductor = null;
        despachador = null;
        listaDeAuxiliares = null;
        
        lblCirculoDeProgreso1.setVisible(false);
        
        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);
        
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png")));
        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png")));
        
        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
        
        lblCirculoDeProgreso1.setVisible(false);
        
    }
    
    public void salir() {
        limpiar();

        // SE CIERRA LA APLICACION
        this.dispose();
        this.setVisible(false);
    }
    
    public boolean validar() {
        boolean verificado = true;
        mensaje = "";
        
        return verificado;
    }
    
    private void limpiar() {
        
        try {
            
            txtPlaca.setText("");
            
            txtnombreDeConductor.setText("");
            
            txtNombreDeAuxiliar1.setText("");
            txtNombreDeAuxiliar2.setText("");
            
            txtNombreDedespachador.setText("");
            
            txtKmDeSalida.setText("");
            txtNumeroDeManifiesto.setText("");
            cbxCanales.setSelectedIndex(0);
            cbxRutaDeDistribucion.setSelectedIndex(0);
            
            DefaultTableModel model;
            model = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
            if (model.getRowCount() > 0) {
                int a = model.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    model.removeRow(i);
                }
            }
            
            conductor = null;
            auxiliar1 = null;
            auxiliar2 = null;
            auxiliar3 = null;
            despachador = null;
            listaDeAuxiliares = new ArrayList<>();
            manifiestoActual = null;
            ruta = null;;
            canalDeVenta = null;;
            
        } catch (Exception ex) {
            
        }
        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
        
        lblCirculoDeProgreso1.setVisible(false);
        
    }
    
    private boolean validarManifiesto() {
        boolean verificado = true;
        try {
            
            mensaje = "";
            
            if (cbxCanales.getSelectedIndex() == 0) {
                mensaje += "No ha seleccionado el canal de distribución " + "  \n";
                verificado = false;
            }
            
            if (cbxRutaDeDistribucion.getSelectedIndex() == 0) {
                mensaje += "No ha seleccionado la Ruta de Distribución " + "  \n";
                verificado = false;
            }
            
            if (conductor == null) {
                mensaje += "No ha asigando el conductor de la ruta" + "  \n";
                verificado = false;
                
            }
            if (despachador == null) {
                mensaje += "No ha asigando un despachador de ruta.El Campo es obligatorio " + "  \n";
                verificado = false;
            }
            
            if (txtKmDeSalida.getText().isEmpty()) {
                mensaje += "No ha colocado el kilometraje de salidad  del vehículo" + "  \n";
                verificado = false;
            }
            
            if (canalDeVenta == null) {
                mensaje += "No ha selecccionado El canal de venta " + "  \n";
                verificado = false;
            }
            if (ruta == null) {
                mensaje += "No ha selecccionado la Ruta a distribuir " + "  \n";
                verificado = false;
            }
            if (manifiestoActual == null) {
                mensaje += "Vehiculo ya tiene asignado un manifiesto de Distribución sin cerrar" + "  \n";
                verificado = false;
            }
            
        } catch (Exception ex) {
            Logger.getLogger(FModificarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificado;
    }
    
    void llenarDatosManifiestoCerrado() throws Exception {
        
        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        txtnombreDeConductor.setEditable(true);
        
        txtNombreDeAuxiliar1.setEditable(false);
        txtNombreDeAuxiliar2.setEditable(false);
        
        txtNombreDedespachador.setEditable(false);
        txtKmDeSalida.setEditable(false);
        txtPlaca.setEditable(false);
        txtKmDeSalida.setEditable(false);
        cbxCanales.setEnabled(true);
        cbxRutaDeDistribucion.setEnabled(true);
        dateManifiesto.setEnabled(true);
        
        btnNuevo.setEnabled(false);
        jBtnNuevo.setEnabled(false);
        
        btnImprimir.setEnabled(false);
        jBtnImprimir.setEnabled(false);
        
        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);
        
        dateManifiesto.setEnabled(false);
        
        txtPlaca.setEnabled(true);
        txtnombreDeConductor.setEnabled(true);
        txtNombreDeAuxiliar1.setEnabled(true);
        txtNombreDeAuxiliar2.setEnabled(true);
        
        txtNombreDedespachador.setEnabled(true);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
        //manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        // manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        // manifiestoActual.setListaDeAuxiliares();
        listaDeAuxiliares = manifiestoActual.getListaDeAuxiliares("" + manifiestoActual.getNumeroManifiesto());

        // SE BUSCA EL CONDUCTOR Y SE LLENA EL CAMPO
        txtnombreDeConductor.setText(manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor());


        /* Se llenan os campos de texto de los auiliares*/
        llenarTxtAuxiliares();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (manifiestoActual.getDespachador().equals("0")) {
            txtNombreDedespachador.setText("");
            
        } else {
            txtNombreDedespachador.setText(manifiestoActual.getNombreDespachador() + " " + manifiestoActual.getApellidosDespachador());
            
        }

        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
        cbxCanales.setSelectedItem(manifiestoActual.getNombreCanal());

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        cbxRutaDeDistribucion.setSelectedItem(manifiestoActual.getNombreDeRuta());
        
        txtPlaca.setText(manifiestoActual.getVehiculo());
        txtKmDeSalida.setText("" + manifiestoActual.getKmSalida());
        //txtNumeroDeManifiesto.setText(manifiestoActual.codificarManifiesto());

        dateManifiesto.setDate(Inicio.getFechaSql(manifiestoActual.getFechaDistribucion()));
        
        int cantidadFacturasEnManifiesto = 0;
        //sumadorPesosFacturas = 0.0;

        modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
        
        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
            
            filaTabla2 = jTableFacturasPorManifiesto.getRowCount();
            modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);
            
            jTableFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   

            // se ubica en la fila insertada
            jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);
            
            cantidadFacturasEnManifiesto++;
            valor += obj.getValorARecaudarFactura();
            
        }
        manifiestoActual.setValorTotalManifiesto(valor);
        
        btnImprimir.setEnabled(true);
        jBtnImprimir.setEnabled(true);
        
        btnImprimir.requestFocus();
        
    }
    
    public void llenarDatosManifiestoAModificar() throws Exception {
        
        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        txtnombreDeConductor.setEditable(true);
        txtnombreDeConductor.setEnabled(true);
        
        txtPlaca.setEditable(true);
        txtPlaca.setEnabled(true);
        
        txtKmDeSalida.setEditable(true);
        txtKmDeSalida.setEnabled(true);
        
        txtNombreDeAuxiliar1.setEditable(false);
        txtNombreDeAuxiliar1.setEnabled(true);
        
        txtNombreDeAuxiliar2.setEditable(false);
        txtNombreDeAuxiliar2.setEnabled(true);
        
        txtNombreDedespachador.setEditable(false);
        txtNombreDedespachador.setEnabled(true);
        
        cbxCanales.setEnabled(true);
        
        cbxRutaDeDistribucion.setEnabled(true);
        dateManifiesto.setEnabled(true);
        
        btnNuevo.setEnabled(false);
        jBtnNuevo.setEnabled(false);
        
        btnImprimir.setEnabled(true);
        jBtnImprimir.setEnabled(true);
        
        btnGrabar.setEnabled(true);
        jBtnGrabar.setEnabled(true);
        
        dateManifiesto.setEnabled(true);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
        //manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        // manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");
        
        modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();
        
        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
            
            filaTabla2 = jTableFacturasPorManifiesto.getRowCount();
            modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);
            
            jTableFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   

            // se ubica en la fila insertada
            jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);
            
            valor += obj.getValorARecaudarFactura();
        }
        //
        String placa = manifiestoActual.getVehiculo();
        txtPlaca.setText(placa);
        
        txtKmDeSalida.setText("" + manifiestoActual.getKmSalida());

        // SE BUSCA EL CONDUCTOR Y SE LLENA EL CAMPO
        conductor = new CEmpleados();
        conductor.setCedula(manifiestoActual.getConductor());
        txtnombreDeConductor.setText(manifiestoActual.getNombreConductor() + " " + manifiestoActual.getApellidosConductor());
        
        listaDeAuxiliares = manifiestoActual.getListaDeAuxiliares("" + manifiestoActual.getNumeroManifiesto());


        /* Se llenan os campos de texto de los auiliares*/
        llenarTxtAuxiliares();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (manifiestoActual.getDespachador().equals("0")) {
            txtNombreDedespachador.setText("");
            
        } else {
            despachador = new CEmpleados();
            despachador.setCedula(manifiestoActual.getDespachador());
            
            txtNombreDedespachador.setText(manifiestoActual.getNombreDespachador() + " " + manifiestoActual.getApellidosDespachador());
            
        }

        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
        cbxCanales.setSelectedItem(manifiestoActual.getNombreCanal());

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        cbxRutaDeDistribucion.setSelectedItem(manifiestoActual.getNombreDeRuta());
        
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        String strFecha = manifiestoActual.getFechaDistribucion();
        Date fecha = null;
        fecha = formatoDelTexto.parse(strFecha);
        dateManifiesto.setDate(fecha);

        // dateManifiesto.setDate(Inicio.getFechaSql(manifiestoActual.getFechaDistribucion()));
        manifiestoActual.setValorTotalManifiesto(valor);
        
        btnImprimir.setEnabled(true);
        jBtnImprimir.setEnabled(true);
        
        btnImprimir.requestFocus();
        
    }
    
    private void llenarTxtAuxiliares() {
        /*se llenan los campos de texto de los nombres de los auxiliares*/
        int indice = 1;
        
        txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
        txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
        
        listaDeAuxiliares = manifiestoActual.getListaDeAuxiliares("" + manifiestoActual.getNumeroManifiesto());
        
        if (listaDeAuxiliares.size() > 0) {
            for (CEmpleados aux : listaDeAuxiliares) {
                switch (indice) {
                    case 1:
                        if (aux.getCedula().equals("0")) {
                            txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
                        } else {
                            txtNombreDeAuxiliar1.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;
                        break;
                    case 2:
                        if (aux.getCedula().equals("0")) {
                            txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
                        } else {
                            txtNombreDeAuxiliar2.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;
                        break;
                    
                }
                /* fin switch */
                
            }
        }
        
    }
    
    private void llenarDatosManifiesto() throws Exception {

        /*  Se llenan los campos del conducor */
        for (CEmpleados obj : ini.getListaDeEmpleados()) {
            if (manifiestoActual.getConductor().equals(obj.getCedula())) {
                
                txtnombreDeConductor.setText(obj.getNombres() + " " + obj.getApellidos());
            }
        }

        /*Se llenan los campos de texto de los auxiliares*/
        llenarTxtAuxiliares();

        /*  Se llenan los campos del despachador */
        if (manifiestoActual.getDespachador().equals("0")) {
            txtNombreDedespachador.setText("");
            
        } else {
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (manifiestoActual.getDespachador().equals(obj.getCedula())) {
                    
                    txtNombreDedespachador.setText(obj.getNombres() + " " + obj.getApellidos());
                }
            }
        }

        /*  Se llenan los campos del canal de distribución  */
        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            if (obj.getIdCanalDeVenta() == manifiestoActual.getIdCanal()) {
                cbxCanales.setSelectedItem(obj.getNombreCanalDeVenta());
                canalDeVenta = obj;
            }
        }
        /*  Se llenan los campos ruta de distribución  */
        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            if (obj.getIdRutasDeDistribucion() == manifiestoActual.getIdRuta()) {
                cbxRutaDeDistribucion.setSelectedItem(obj.getNombreRutasDeDistribucion());
                ruta = obj;
            }
        }
        txtNumeroDeManifiesto.setText(manifiestoActual.codificarManifiesto());
        
        manifiestoActual.setIsFree(0);
        
        manifiestoActual.liberarManifiesto(false);
        
        System.out.println("llama la lista de facturas por manifiesto");
        
        DecimalFormat df = new DecimalFormat("#,###.0");
        
        modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();

        //  SE LLENA EL JTABLE FACTURAS POR MANIFIESTO
        if (manifiestoActual.getListaFacturasPorManifiesto() != null) {
            
            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
                
                int fila = jTableFacturasPorManifiesto.getRowCount();
                modelo2.addRow(new Object[jTableFacturasPorManifiesto.getRowCount()]);
                
                jTableFacturasPorManifiesto.setValueAt("" + obj.getAdherencia(), fila, 0); // item 
                jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), fila, 1); // numero de la factura
                jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), fila, 2); // cliente

                jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), fila, 3); // valor de la factura   

                // se ubica en la fila insertada
                jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);
                
            }
            
        }
        
    }
    
    public void modificarManifiesto() throws Exception {

        /* Se verifica que haya un manifiesto instanciado */
        if (manifiestoActual != null) {

            /* Se valida que el manifiesto actual esté disponible , es decir que no esté otro usuario ingresando
             facturas al sistema con el mismo vehículo*/
            //if (manifiestoActual.getIsFree() == 1) {
            if ((manifiestoActual.getIsFree() == 1) || (manifiestoActual.getUsuarioManifiesto().equals(Inicio.deCifrar(ini.getUser().getNombreUsuario())))) {
                try {
                    
                    modelo2 = (DefaultTableModel) jTableFacturasPorManifiesto.getModel();

                    /*Se llama al fichero que contiene los numero de las facturas
                     a  que están registradas en el manifiesto*/
                    File fichero = new File(this.manifiestoActual.getRutArchivofacturasporManifiesto());

                    /*Se valida que exista el fichero */
                    if (fichero.exists()) {
                        
                        manifiestoActual.setListaFacturasPorManifiesto(fichero);
                        
                        if (manifiestoActual.getListaFacturasPorManifiesto().isEmpty() || manifiestoActual.getListaFacturasPorManifiesto() == null) {
                            btnGrabar.setEnabled(false);
                            jBtnGrabar.setEnabled(false);
                        } else {
                            btnGrabar.setEnabled(true);
                            jBtnGrabar.setEnabled(true);
                        }
                        
                        llenarDatosManifiesto();
                        
                        txtnombreDeConductor.setEditable(false);
                        txtNombreDeAuxiliar1.setEditable(false);
                        txtNombreDeAuxiliar2.setEditable(false);
                        
                        txtKmDeSalida.setEditable(false);
                        txtPlaca.setEnabled(true);
                        txtPlaca.setEditable(false);
                        cbxCanales.setEnabled(false);
                        cbxRutaDeDistribucion.setEnabled(false);
                        dateManifiesto.setEnabled(false);
                        
                        btnGrabar.setEnabled(true);
                        jBtnGrabar.setEnabled(true);
                        
                        txtNumeroDeManifiesto.setEditable(false);
                        
                        dateManifiesto.setDate(Inicio.getFechaSql(manifiestoActual.getFechaDistribucion()));
                        
                    } else {
                        
                        llenarDatosManifiesto();
                        
                        txtKmDeSalida.setEditable(false);
                        dateManifiesto.setDate(Inicio.getFechaSql(manifiestoActual.getFechaDistribucion()));
                        
                        txtnombreDeConductor.setEditable(false);
                        txtNombreDeAuxiliar1.setEditable(false);
                        txtNombreDeAuxiliar2.setEditable(false);
                        
                        cbxCanales.setEnabled(false);
                        cbxRutaDeDistribucion.setEnabled(false);
                        dateManifiesto.setEnabled(false);
                        
                    }
                    
                } catch (Exception ex) {
                    Logger.getLogger(FModificarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                limpiar();
                manifiestoActual = null;
                
                cbxCanales.setEnabled(false);
                cbxRutaDeDistribucion.setEnabled(false);
                dateManifiesto.setEnabled(false);
                txtKmDeSalida.setEnabled(false);
                
                JOptionPane.showMessageDialog(this, "Esa Ruta está siendo despachada por otro usuario ", "Error", 0);
                txtPlaca.requestFocus();
                
            }
        }
    }

    /**
     * Método que permite validae¿r los datos para poder crear un manifiesto
     *
     * @return verdadero sí los datos son correctos y retorna falso sí los datos
     * no son válidos
     */
    public boolean validarDatos() {
        boolean isValido = true;
        mensaje = "";
        if (conductor == null) {
            mensaje += "El presente manifiesto no tiene asignado un Conductor \n";
            isValido = false;
        }
        if (auxiliar1 == null) {
            mensaje += "El presente manifiesto no tiene un auxiliar válido \n";
            isValido = false;
        }
       
        if (kilometraje == 0) {
            mensaje += "El kilometraje no es válido \n";
            isValido = false;
            
        }
        if (canalDeVenta == null) {
            mensaje += "El Canal de distribución no está asignado \n";
            isValido = false;
            
        }
        if (ruta == null) {
            mensaje += "No se h asignado una ruta válida \n";
            isValido = false;
            
        }
        
        return isValido;
        
    }
    
    private void addStory(String text) {
        /* try {
            boolean add = true;
            PreparedStatement p = conexion.prepareStatement(
                    "select idconsultasstory "
                            + "from consultasstory "
                            + "where storyName=? limit 1");
            p.setString(1, text);
            ResultSet r = p.executeQuery();
            if (r.first()) {
                add = false;
            }
            r.close();
            p.close();
            if (add) {
                p = conexion.prepareStatement("insert into consultasstory (storyName) values (?)");
                p.setString(1, text);
                p.execute();
                p.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
    
    private void removeHistory(String text) {
        /* try {
            PreparedStatement p = conexion.prepareStatement("delete from consultasstory where storyName=? limit 1");
            p.setString(1, text);
            p.execute();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } */
    }
    
    private List<DataSearch> searchPlaca(String search) {
        List<DataSearch> list = new ArrayList<>();
        int i = 0;
        for (CCarros car : ini.getListaDeVehiculos()) {
            
            if (car.getPlaca().contains(search)) {
                if (i <= 7) {
                    i++;
                    list.add(new DataSearch(car.getPlaca(), false));
                } else {
                    break;
                }
            }
        }
        
        return list;
    }
    
    private List<DataSearch> searchConductor(String search) {
        List<DataSearch> list = new ArrayList<>();
        int i = 0;
        for (CEmpleados empleado : ini.getListaDeEmpleados()) {
            
            String nombres = empleado.getNombres() + " " + empleado.getApellidos();
            if (nombres.contains(search)) {
                if (i <= 7) {
                    i++;
                    list.add(new DataSearch(nombres, false));
                } else {
                    break;
                }
            }
        }
        
        return list;
    }
    
    private List<DataSearch> searchAuxiliar1(String search) {
        List<DataSearch> list = new ArrayList<>();
        int i = 0;
        for (CEmpleados empleado : ini.getListaDeEmpleados()) {
            
            String nombres = empleado.getNombres() + " " + empleado.getApellidos();
            if (nombres.contains(search)) {
                if (i <= 7) {
                    i++;
                    list.add(new DataSearch(nombres, false));
                } else {
                    break;
                }
            }
        }
        
        return list;
    }
    
    private List<DataSearch> searchAuxiliar2(String search) {
        List<DataSearch> list = new ArrayList<>();
        int i = 0;
        for (CEmpleados empleado : ini.getListaDeEmpleados()) {
            
            String nombres = empleado.getNombres() + " " + empleado.getApellidos();
            if (nombres.contains(search)) {
                if (i <= 7) {
                    i++;
                    list.add(new DataSearch(nombres, false));
                } else {
                    break;
                }
            }
        }
        
        return list;
    }
    
     private List<DataSearch> searchDespachador1(String search) {
        List<DataSearch> list = new ArrayList<>();
        int i = 0;
        for (CEmpleados empleado : ini.getListaDeEmpleados()) {
            
            String nombres = empleado.getNombres() + " " + empleado.getApellidos();
            if (nombres.contains(search)) {
                if (i <= 7) {
                    i++;
                    list.add(new DataSearch(nombres, false));
                } else {
                    break;
                }
            }
        }
        
        return list;
    }
}
