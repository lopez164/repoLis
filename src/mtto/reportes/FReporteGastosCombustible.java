/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.reportes;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.HiloReportePedidosMovilizadosPorPeriodo;
import chart.barChart.BarModelChart;
import chart.lineChart.ModelChartLine;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import mtto.documentos.objetos.GastosPorVehiculo;

/**
 *
 * @author VLI_488
 */
public class FReporteGastosCombustible extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    HiloReportePedidosMovilizadosPorPeriodo datos;
    public Inicio ini;
    int filaTabla2;

    public int cantidadGraficos = 0;
    public List<GastosPorVehiculo> listaDeRegistros;
    public Date fechaIncial;
    public Date fechaFinal;
    public String placa = "";
    double cantidadCombustible = 0.0;
    double TotalKilometros = 0.0;

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public FReporteGastosCombustible(Inicio ini) {
        initComponents();

        lblCirculoDeProgreso.setVisible(false);
        jLabel1.setVisible(false);

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

        cuadrarFechaJDateChooser();
        nuevo();

    }

    private void cuadrarFechaJDateChooser() {
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
            gc = new GregorianCalendar(anio, mes, diaMaximo);
            min = gc.getTimeInMillis();
            jFechaFinal.setDate(new Date(min));

        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FReporteGastosCombustible.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel40 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jFechaFinal = new com.toedter.calendar.JDateChooser();
        jFechaInicial = new com.toedter.calendar.JDateChooser();
        btnAceptar = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();
        barra = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        txtPlaca = new ui.swing.textField.TextField();
        chkExportar = new javax.swing.JCheckBox();
        pnlGrafico1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRegistros = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jBtnExcel = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        chart = new chart.barChart.BarChart();
        chartLine = new chart.lineChart.ChartLine();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario de consulta gastos de flota por periodo");
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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Reporte de Gastos de combustible"));

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setLabelFor(jFechaInicial);
        jLabel40.setText("Fecha Incial");

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setLabelFor(jFechaFinal);
        jLabel47.setText("Fecha Final");

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        lblCirculoDeProgreso.setEnabled(false);

        jFechaFinal.setToolTipText("Seleccionar la fecha ");
        jFechaFinal.setDateFormatString("yyyy/MM/dd");
        jFechaFinal.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jFechaInicial.setToolTipText("Seleccionar la fecha ");
        jFechaInicial.setDateFormatString("yyyy/MM/dd");
        jFechaInicial.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Yes.png"))); // NOI18N
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnExportarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        btnExportarExcel.setEnabled(false);
        btnExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarExcelActionPerformed(evt);
            }
        });

        jLabel1.setText("Exportando a excel ");

        txtPlaca.setLabelText("Placa del vehiculo");

        chkExportar.setText("Exportar Excel");
        chkExportar.setEnabled(false);
        chkExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkExportarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkExportar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkExportar))
                    .addComponent(jFechaInicial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFechaFinal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExportarExcel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlGrafico1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "fecha", "#Fact.", "proveedor", "vehiculo", "conductor", "cantidad", "concepto", "valorUnitario", "valorTotal", "kilometrajes", "recorrido", "consumo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaRegistros.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaRegistros.setAutoscrolls(true);
        tablaRegistros.getTableHeader().setResizingAllowed(false);
        tablaRegistros.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tablaRegistros);
        if (tablaRegistros.getColumnModel().getColumnCount() > 0) {
            tablaRegistros.getColumnModel().getColumn(0).setPreferredWidth(100);
            tablaRegistros.getColumnModel().getColumn(1).setPreferredWidth(70);
            tablaRegistros.getColumnModel().getColumn(2).setPreferredWidth(220);
            tablaRegistros.getColumnModel().getColumn(3).setPreferredWidth(70);
            tablaRegistros.getColumnModel().getColumn(4).setPreferredWidth(220);
            tablaRegistros.getColumnModel().getColumn(5).setPreferredWidth(60);
            tablaRegistros.getColumnModel().getColumn(6).setMinWidth(200);
            tablaRegistros.getColumnModel().getColumn(6).setPreferredWidth(200);
            tablaRegistros.getColumnModel().getColumn(6).setMaxWidth(200);
            tablaRegistros.getColumnModel().getColumn(7).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(8).setPreferredWidth(140);
            tablaRegistros.getColumnModel().getColumn(9).setPreferredWidth(100);
            tablaRegistros.getColumnModel().getColumn(10).setPreferredWidth(100);
            tablaRegistros.getColumnModel().getColumn(11).setMinWidth(100);
            tablaRegistros.getColumnModel().getColumn(11).setPreferredWidth(100);
            tablaRegistros.getColumnModel().getColumn(11).setMaxWidth(100);
        }

        javax.swing.GroupLayout pnlGrafico1Layout = new javax.swing.GroupLayout(pnlGrafico1);
        pnlGrafico1.setLayout(pnlGrafico1Layout);
        pnlGrafico1Layout.setHorizontalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGrafico1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlGrafico1Layout.setVerticalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
        );

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo.setFocusable(false);
        jBtnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnNuevo.setPreferredSize(new java.awt.Dimension(24, 24));
        jBtnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnNuevo);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jToggleButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setPreferredSize(new java.awt.Dimension(24, 24));
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jToggleButton1);

        jBtnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        jBtnExcel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExcel.setFocusable(false);
        jBtnExcel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExcel.setPreferredSize(new java.awt.Dimension(24, 24));
        jBtnExcel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExcelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExcel);

        jBtnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnCancel.setFocusable(false);
        jBtnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCancel.setPreferredSize(new java.awt.Dimension(24, 24));
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
        jBtnExit.setPreferredSize(new java.awt.Dimension(24, 24));
        jBtnExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExitActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExit);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chartLine, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chartLine, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlGrafico1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlGrafico1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing


    }//GEN-LAST:event_formInternalFrameClosing

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        btnAceptar.setEnabled(false);
        btnExportarExcel.setEnabled(false);

        fechaIncial = ini.getFechaSql(this.jFechaInicial);
        fechaFinal = ini.getFechaSql(this.jFechaFinal);
        String ss = "" + this.jFechaFinal.getDate();
        listaDeRegistros = new ArrayList();
        if (txtPlaca.getText().equals("")) {
            // new Thread(new FReporteGastosFlotaHilo(this, "general")).start();
        } else {
            placa = txtPlaca.getText().trim();
            new Thread(new FReporteGastosFlotaHilo(this, "combustible")).start();
        }

        txtPlaca.setEnabled(false);
        txtPlaca.setEditable(false);
        chkExportar.setEnabled(false);
        jFechaFinal.setEnabled(false);
        jFechaInicial.setEnabled(false);


    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        btnExportarExcel.setEnabled(false);
//        new Thread(new FReporteGastosFlotaHilo(this, false)).start();


    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        limpiar();
        txtPlaca.setText("");
        txtPlaca.setEnabled(false);
        txtPlaca.setEditable(false);
        btnAceptar.setEnabled(true);
        jBtnExcel.setEnabled(false);
        jFechaInicial.setEnabled(false);
        jFechaFinal.setEnabled(false);
        chkExportar.setEnabled(false);
        chart.clear();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void limpiar() {
        listaDeRegistros = null;
        cuadrarFechaJDateChooser();
        limpiarTabla();
        barra.setValue(0);
        btnAceptar.setEnabled(true);
        placa = "";

    }

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);

    }//GEN-LAST:event_jBtnExitActionPerformed

    private void jBtnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExcelActionPerformed
        btnExportarExcel.setEnabled(false);
        //  new Thread(new FReporteGastosFlotaHilo(this, false)).start();
    }//GEN-LAST:event_jBtnExcelActionPerformed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();

    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void nuevo() {
        limpiar();
        txtPlaca.setText("");
        txtPlaca.setEnabled(true);
        txtPlaca.setEditable(true);
        btnAceptar.setEnabled(true);
        jBtnExcel.setEnabled(false);
        jFechaFinal.setEnabled(true);
        jFechaInicial.setEnabled(true);
        chkExportar.setEnabled(true);
    }

    private void chkExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkExportarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkExportarActionPerformed

    public void llenarTablabitacora() throws Exception {

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeRegistros.size());

        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();
        int recorrido = 0;
        int i = 0;

        /*Inicia el proceso de la grafica*/
        chart.start();
        
       

        for (GastosPorVehiculo obj : listaDeRegistros) {
            filaTabla2 = tablaRegistros.getRowCount();

            modelo.addRow(new Object[tablaRegistros.getRowCount()]);

            int filaInicial = 0, filaSiguiente = 0;

            tablaRegistros.setValueAt(obj.getFechaRecibo(), filaTabla2, 0);  // item
            tablaRegistros.setValueAt(obj.getNumeroRecibo(), filaTabla2, 1); // numero de manifiesto
            tablaRegistros.setValueAt(obj.getNombreSucursal(), filaTabla2, 2); // fecha de distribucion
            tablaRegistros.setValueAt(obj.getPlaca(), filaTabla2, 3); // placa del vehiculo
            tablaRegistros.setValueAt(obj.getNombreConductor(), filaTabla2, 4); // nombre del conductor
            tablaRegistros.setValueAt(obj.getCantidad(), filaTabla2, 5); // nombre del conductor
            tablaRegistros.setValueAt(obj.getDescripcionProductoServicio(), filaTabla2, 6); // nombre del conductor
            tablaRegistros.setValueAt(nf.format(Double.parseDouble(obj.getValorUnitario())), filaTabla2, 7); // nombre del conductor
            tablaRegistros.setValueAt(nf.format(Double.parseDouble(obj.getValorTotal())), filaTabla2, 8); // nombre del conductor
            tablaRegistros.setValueAt(obj.getKilometraje(), filaTabla2, 9); // nombre del conductor

            if (i == 0) {
                filaInicial = tablaRegistros.getRowCount();
            } else {
                filaInicial = tablaRegistros.getRowCount() - 2;
                filaSiguiente = tablaRegistros.getRowCount() - 1;
                String val2 = tablaRegistros.getValueAt(filaInicial, 9).toString();
                String val1 = tablaRegistros.getValueAt(filaSiguiente, 9).toString();

                recorrido = Integer.parseInt(tablaRegistros.getValueAt(filaSiguiente, 9).toString())
                        - Integer.parseInt(tablaRegistros.getValueAt(filaInicial, 9).toString());
            }

            tablaRegistros.setValueAt("" + recorrido, filaTabla2, 10); // nombre del conductor
            
            obj.setRecorrido(recorrido);

            double consumo = recorrido / Double.parseDouble(tablaRegistros.getValueAt(filaSiguiente, 5).toString());
            DecimalFormat df = new DecimalFormat("#.##");

            if (i > 0) {
                tablaRegistros.setValueAt("" + df.format(consumo) + " Km/gl.", filaTabla2, 11); // nombre del conductor
                obj.setConsumoPorGalon(consumo);
            }
            i++;
        }
        //txtCantidaCombustible.setText("" + calcularCantidad());
       // txtTotalKilometros.setText("" + calcularKilometraje());

        i = 0;
        int k = 1;
        //while (rs.next()) {
        for (GastosPorVehiculo obj : listaDeRegistros) {
            
                chart.addLegend(obj.getFechaRecibo().substring(0, obj.getFechaRecibo().length()), getColor(i++));
                if (i == 26) {
                    i = 0;
                }
            

            k++;
        }

        i = 0;
        double[] lista = new double[k];
        for (GastosPorVehiculo obj : listaDeRegistros) {
            //while (rs.next()) {
            //lista[i] = rs.getDouble("cant");
            
                lista[i] = obj.getConsumoPorGalon();
              
            
            i++; 
        }
        chart.addData(new BarModelChart("", lista));
        initChartLine();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barra;
    private javax.swing.JButton btnAceptar;
    public javax.swing.JButton btnExportarExcel;
    private chart.barChart.BarChart chart;
    private chart.lineChart.ChartLine chartLine;
    public javax.swing.JCheckBox chkExportar;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExcel;
    private javax.swing.JToggleButton jBtnExit;
    private javax.swing.JButton jBtnNuevo;
    public com.toedter.calendar.JDateChooser jFechaFinal;
    public com.toedter.calendar.JDateChooser jFechaInicial;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JPanel pnlGrafico1;
    private javax.swing.JTable tablaRegistros;
    private ui.swing.textField.TextField txtPlaca;
    // End of variables declaration//GEN-END:variables

    public void limpiarTabla() {

        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    private double calcularCantidad() {
        int i = 0;
        double valor = 0.0;
        for (GastosPorVehiculo obj : listaDeRegistros) {
            if (i > 0) {
                valor += Double.parseDouble(obj.getCantidad());
            }
            i++;

        }
        return valor;
    }

    private Double calcularKilometraje() {
        double valor = 0.0;
        double inicial = Double.parseDouble(listaDeRegistros.get(0).getKilometraje());
        double siguiente = Double.parseDouble(listaDeRegistros.get(listaDeRegistros.size() - 1).getKilometraje());
        valor = siguiente - inicial;
        return valor;
    }

    private Color getColor(int index) {
        Color color = new Color(255, 255, 255);
        switch (index) {
            case 0:
                color = new Color(69, 120, 177);
                break;
            case 1:
                color = new Color(236, 217, 21);
                break;
            case 2:
                color = new Color(179, 238, 154);
                break;
            case 3:
                color = new Color(99, 149, 202);
                break;
            case 4:
                color = new Color(241, 134, 191);
                break;
            case 5:
                color = new Color(162, 249, 52);
                break;
            case 6:
                color = new Color(199, 247, 255);
                break;
            case 7:
                color = new Color(241, 165, 89);
                break;
            case 8:
                color = new Color(186, 255, 182);

                break;
            case 9:
                color = new Color(206, 17, 253);
                break;
            case 10:
                color = new Color(210, 211, 111);
                break;
            case 11:
                color = new Color(240, 225, 232);
                break;
            case 12:
                color = new Color(255, 255, 190);
                break;
            case 13:
                color = new Color(225, 107, 97);
                break;
            case 14:
                color = new Color(178, 183, 246);
                break;
            case 15:
                color = new Color(153, 231, 150);
                break;
            case 16:
                color = new Color(255, 255, 138);
                break;
            case 17:
                color = new Color(225, 196, 209);
                break;
            case 18:
                color = new Color(241, 139, 126);
                break;
            case 19:
                color = new Color(231, 231, 143);
                break;
            case 20:
                color = new Color(209, 167, 186);
                break;
            case 21:
                color = new Color(167, 213, 237);
                break;
            case 22:
                color = new Color(143, 227, 255);
                break;
            case 23:
                color = new Color(240, 239, 176);
                break;
            case 24:
                color = new Color(120, 207, 119);
                break;
            case 25:
                color = new Color(134, 181, 220);
                break;
            case 26:
                color = new Color(255, 171, 157);
                break;
            case 27:
                color = new Color(255, 242, 205);
                break;
            case 28:
                color = new Color(222, 179, 223);
                break;
            case 29:
                color = new Color(166, 161, 237);
                break;
            case 30:
                color = new Color(186, 213, 249);
                break;
            case 31:
                color = new Color(255, 213, 178);
                break;
            case 32:
                color = new Color(255, 171, 157);
                break;

        }

        return color;
    }
     public void initChartLine() {
       
      
        List<ModelChartLine> list = new ArrayList<>();
         for (GastosPorVehiculo obj : listaDeRegistros) {
            list.add(new ModelChartLine(obj.getFechaRecibo(), obj.getRecorrido()));
        }
//        list.add(new ModelChartLine("Monday", 10));
//        list.add(new ModelChartLine("Tuesday", 150));
//        list.add(new ModelChartLine("Wednesday", 80));
//        list.add(new ModelChartLine("Thursday", 100));
//        list.add(new ModelChartLine("Friday", 125));
//        list.add(new ModelChartLine("Saturday", 80));
//        list.add(new ModelChartLine("Sunday", 200));
        chartLine.setModel(list); 
    }
}
