/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.FBuscarListadoDeEmpleados;
import aplicacionlogistica.distribucion.formularios.reportes.HiloReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Desktop;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FReportemovilizadoPorConductor extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    HiloReportePedidosMovilizadosPorPeriodo datos;
    public Inicio ini;
    int filaTabla2;

    public int cantidadGraficos = 0;
    public ArrayList<FReporteMovilizadoPorConductorModelo> listaDeRegistros;
    public String fechaIncial;
    public String fechaFinal;
    public CEmpleados conductor;
    public String[] header;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    TextAutoCompleter autoTxtConductores;
    public String rutaArchivo = "";
   

   

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public FReportemovilizadoPorConductor(Inicio ini) {
        initComponents();

        lblCirculoDeProgreso.setVisible(false);
        //jLabel1.setVisible(false);
        txtNombreConductor.setEditable(false);
        this.ini = ini;

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
        //panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg")));
        try {
            lblCirculoDeProgreso.setVisible(false);
            // HiloCrearChartBarras1 FgraficoPedidosMovilizados = new HiloCrearChartBarras1(this.ini,pnlGrafico1) ;
            Calendar calendario = Calendar.getInstance();
            int diaMinimo = calendario.getActualMinimum(Calendar.DAY_OF_MONTH);
            int diaMaximo = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
            int anio = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            GregorianCalendar gc = new GregorianCalendar(anio, mes, diaMinimo);
            long min = gc.getTimeInMillis();

            jFechaInicial.setDate(new Date(min));
            gc = new GregorianCalendar(anio, mes, diaMaximo, 23, 59, 59);
            min = gc.getTimeInMillis();
            jFechaFinal.setDate(new Date(min));

            autoTxtConductores = new TextAutoCompleter(txtNombreConductor);

            for (CEmpleados conductor : this.ini.getListaDeEmpleados()) {
                if (conductor.getEmpleadoActivo() == 1) {
                    autoTxtConductores.addItem(conductor.getNombres() + " " + conductor.getApellidos());
                }
            }

        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FReportemovilizadoPorConductor.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel8 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jFechaFinal = new com.toedter.calendar.JDateChooser();
        jFechaInicial = new com.toedter.calendar.JDateChooser();
        btnAceptar = new javax.swing.JButton();
        barra = new javax.swing.JProgressBar();
        txtNombreConductor = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        pnlGrafico1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRegistros = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnAgregar = new javax.swing.JButton();
        btnMostrarDocumento = new javax.swing.JToggleButton();
        btnImprimir1 = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        btnExportarExcel = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JToggleButton();
        jButton4 = new javax.swing.JButton();
        btnGrabar1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Consulta de Pedidos movilizados por conductores");
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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Consulta pedidos movilizados por rango", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel40.setLabelFor(jFechaInicial);
        jLabel40.setText("Fecha Incial");

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel47.setLabelFor(jFechaFinal);
        jLabel47.setText("Fecha Final");

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        lblCirculoDeProgreso.setEnabled(false);

        jFechaFinal.setToolTipText("Seleccionar la fecha ");
        jFechaFinal.setDate(new Date());
        jFechaFinal.setDateFormatString("yyyy-MM-dd HH:mm:ss");
        jFechaFinal.setEnabled(false);
        jFechaFinal.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jFechaInicial.setToolTipText("Seleccionar la fecha ");
        jFechaInicial.setDateFormatString("yyyy-MM-dd HH:mm:ss");
        jFechaInicial.setEnabled(false);
        jFechaInicial.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Yes.png"))); // NOI18N
        btnAceptar.setEnabled(false);
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        txtNombreConductor.setEnabled(false);
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

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setLabelFor(jFechaFinal);
        jLabel48.setText("Nombre de Conductor");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jFechaInicial, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jFechaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombreConductor)
                            .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE))
                        .addGap(4, 4, 4)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 908, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(1, 1, 1)
                        .addComponent(jFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAceptar))))
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlGrafico1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Mfto.", "Factura", "Vehiculo", "Valor", "Movi", "Salida", "uuario", "valor", "Entrada", "usuario", "tiemp"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaRegistros.getTableHeader().setReorderingAllowed(false);
        tablaRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaRegistrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaRegistros);
        if (tablaRegistros.getColumnModel().getColumnCount() > 0) {
            tablaRegistros.getColumnModel().getColumn(0).setPreferredWidth(55);
            tablaRegistros.getColumnModel().getColumn(1).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(2).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(3).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(4).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(5).setPreferredWidth(100);
            tablaRegistros.getColumnModel().getColumn(6).setPreferredWidth(140);
            tablaRegistros.getColumnModel().getColumn(7).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(8).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(9).setPreferredWidth(140);
            tablaRegistros.getColumnModel().getColumn(10).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(11).setPreferredWidth(50);
        }

        javax.swing.GroupLayout pnlGrafico1Layout = new javax.swing.GroupLayout(pnlGrafico1);
        pnlGrafico1.setLayout(pnlGrafico1Layout);
        pnlGrafico1Layout.setHorizontalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        pnlGrafico1Layout.setVerticalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGrafico1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE))
        );

        jToolBar1.setRollover(true);

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        btnAgregar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregar.setFocusable(false);
        btnAgregar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAgregar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAgregar);

        btnMostrarDocumento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        btnMostrarDocumento.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnMostrarDocumento.setEnabled(false);
        btnMostrarDocumento.setFocusable(false);
        btnMostrarDocumento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMostrarDocumento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnMostrarDocumento);

        btnImprimir1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        btnImprimir1.setToolTipText("Imprimir");
        btnImprimir1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnImprimir1.setFocusable(false);
        btnImprimir1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprimir1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImprimir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimir1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnImprimir1);

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

        btnExportarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        btnExportarExcel.setEnabled(false);
        btnExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarExcelActionPerformed(evt);
            }
        });
        jToolBar1.add(btnExportarExcel);

        btnCancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        btnCancelar1.setToolTipText("Imprimir");
        btnCancelar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelar1.setFocusable(false);
        btnCancelar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCancelar1);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        btnGrabar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        btnGrabar1.setToolTipText("Grabar");
        btnGrabar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGrabar1.setEnabled(false);
        btnGrabar1.setFocusable(false);
        btnGrabar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabar1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGrabar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 1130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 4, Short.MAX_VALUE)))
                .addGap(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing


    }//GEN-LAST:event_formInternalFrameClosing

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        btnAceptar.setEnabled(false);
        btnExportarExcel.setEnabled(false);
        String ss;
        String[] lista;

        ss = "" + this.jFechaInicial.getDate();
        lista = ss.split(" ");
        fechaIncial = lista[5] + "-" + mes(lista[1]) + "-" + lista[2] + " " + lista[3];

        ss = "" + this.jFechaFinal.getDate();
        lista = ss.split(" ");
        fechaFinal = lista[5] + "-" + mes(lista[1]) + "-" + lista[2] + " " + lista[3];

        new Thread(new HiloFReporteMovilzadoPorConductor(ini, this, "consultarDatos")).start();


    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed

        try {
            Desktop.getDesktop().open(new File(rutaArchivo));
        } catch (IOException ex) {
            Logger.getLogger(FReportemovilizadoPorConductor.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void txtNombreConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorActionPerformed

    private void txtNombreConductorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreConductorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            //if (manifiestoActual.getEstadoManifiesto() < 2) {
            FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(ini, this);
            this.getParent().add(form);
            form.toFront();
            form.setClosable(true);
            form.setVisible(true);
            form.setTitle("Formulario para buscar Empleados por apellidos");
            form.txtApellidosPersona.requestFocus();
            //btnNuevo.setEnabled(false);
            form.show();
            //}

        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String cond;
            for (CEmpleados emp : ini.getListaDeEmpleados()) {
                cond = emp.getNombres() + " " + emp.getApellidos();
                if (cond.equals(txtNombreConductor.getText().trim())) {
                    this.conductor = emp;
                    btnAceptar.setEnabled(true);
                    txtNombreConductor.setEnabled(false);
                    jFechaFinal.setEnabled(false);
                    jFechaInicial.setEnabled(false);
                    break;

                }

            }
            //consultarDatos();

        }
    }//GEN-LAST:event_txtNombreConductorKeyPressed

    private void tablaRegistrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaRegistrosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaRegistrosMouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        limpiar();
        jFechaInicial.setEnabled(true);
        jFechaFinal.setEnabled(true);
        txtNombreConductor.setEnabled(true);
        txtNombreConductor.setEditable(true);
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void jBtnMinutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnMinutaActionPerformed

//        new Thread(new HiloFConsultarManifiesto(ini, this, "sacarMinuta")).start();

    }//GEN-LAST:event_jBtnMinutaActionPerformed

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
          limpiar();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
          limpiar();
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnGrabar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabar1ActionPerformed
        //        if (listaDeFacturasTrasladadas.isEmpty()) {
        //            JOptionPane.showInternalMessageDialog(this, "No hay datos para trasladar al Manifiesto # " + manifiestoDestino.getNumeroManifiesto(), "! Alerta !", JOptionPane.WARNING_MESSAGE);
        //        } else {
        //            if (grabarTrasladoDeFacturas()) {
        //
        //                return;
        //            }
        //        }
    }//GEN-LAST:event_btnGrabar1ActionPerformed

    private void btnImprimir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimir1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImprimir1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barra;
    private javax.swing.JButton btnAceptar;
    public javax.swing.JButton btnAgregar;
    private javax.swing.JToggleButton btnCancelar1;
    public javax.swing.JButton btnExportarExcel;
    private javax.swing.JButton btnGrabar1;
    public javax.swing.JToggleButton btnImprimir1;
    private javax.swing.JToggleButton btnMostrarDocumento;
    private javax.swing.JToggleButton jBtnMinuta;
    private javax.swing.JButton jButton4;
    public com.toedter.calendar.JDateChooser jFechaFinal;
    public com.toedter.calendar.JDateChooser jFechaInicial;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JPanel pnlGrafico1;
    public javax.swing.JTable tablaRegistros;
    public javax.swing.JTextField txtNombreConductor;
    // End of variables declaration//GEN-END:variables

    private String mes(String mes) {
        String valor = null;
        switch (mes) {
            case "Jan":
                valor = "01";
                break;
            case "Feb":
                valor = "02";
                break;

            case "Mar":
                valor = "03";
                break;

            case "Apr":
                valor = "04";
                break;

            case "May":
                valor = "05";
                break;

            case "Jun":
                valor = "06";
                break;

            case "Jul":
                valor = "07";
                break;

            case "Aug":
                valor = "08";
                break;

            case "Sep":
                valor = "09";
                break;

            case "Oct":
                valor = "10";
                break;

            case "Nov":
                valor = "11";
                break;
            case "Dec":
                valor = "12";
                break;

        }
        return valor;
    }
    private void limpiar(){
        txtNombreConductor.setEnabled(false);
        txtNombreConductor.setText("");
        cantidadGraficos = 0;
        listaDeRegistros = null;
        fechaIncial = null;
        fechaFinal = null;
        header = null;
        rutaArchivo = null;
        limpiarTablaDeRegistros();


    }
    
    private void limpiarTablaDeRegistros(){
   
        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
       
    }
    }