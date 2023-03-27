/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.reportes;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.HiloReportePedidosMovilizadosPorPeriodo;
import java.awt.KeyEventDispatcher;
import mtto.ingresoDeRegistros.objetos.ItemsFacturaLogistica;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
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
public class FReporteGastosFlota extends javax.swing.JInternalFrame {
    
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    HiloReportePedidosMovilizadosPorPeriodo datos;
    public Inicio ini;
    int filaTabla2;
    
    public int cantidadGraficos = 0;
    public List<GastosPorVehiculo> listaDeRegistros;
    public Date fechaIncial;
    public Date fechaFinal;
    public String placa = "";
    
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public FReporteGastosFlota(Inicio ini) {
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
            Logger.getLogger(FReporteGastosFlota.class.getName()).log(Level.SEVERE, null, ex);
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
        pnlGrafico1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRegistros = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jBtnExcel = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();

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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Reporte de Gastos de la flota por periodo"));

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFechaInicial, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jFechaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExportarExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(10, 10, 10)
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlGrafico1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "fecha", "#Fact.", "proveedor", "vehiculo", "conductor", "cantidad", "concepto", "valorUnitario", "valorTotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
            tablaRegistros.getColumnModel().getColumn(0).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(0).setPreferredWidth(100);
            tablaRegistros.getColumnModel().getColumn(0).setHeaderValue("fecha");
            tablaRegistros.getColumnModel().getColumn(1).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(1).setPreferredWidth(70);
            tablaRegistros.getColumnModel().getColumn(1).setHeaderValue("#Fact.");
            tablaRegistros.getColumnModel().getColumn(2).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(2).setPreferredWidth(220);
            tablaRegistros.getColumnModel().getColumn(2).setHeaderValue("proveedor");
            tablaRegistros.getColumnModel().getColumn(3).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(3).setPreferredWidth(70);
            tablaRegistros.getColumnModel().getColumn(3).setHeaderValue("vehiculo");
            tablaRegistros.getColumnModel().getColumn(4).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(4).setPreferredWidth(220);
            tablaRegistros.getColumnModel().getColumn(4).setHeaderValue("conductor");
            tablaRegistros.getColumnModel().getColumn(5).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(5).setPreferredWidth(60);
            tablaRegistros.getColumnModel().getColumn(5).setHeaderValue("cantidad");
            tablaRegistros.getColumnModel().getColumn(6).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(6).setPreferredWidth(250);
            tablaRegistros.getColumnModel().getColumn(6).setHeaderValue("concepto");
            tablaRegistros.getColumnModel().getColumn(7).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(7).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(7).setHeaderValue("valorUnitario");
            tablaRegistros.getColumnModel().getColumn(8).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(8).setPreferredWidth(140);
            tablaRegistros.getColumnModel().getColumn(8).setHeaderValue("valorTotal");
        }

        javax.swing.GroupLayout pnlGrafico1Layout = new javax.swing.GroupLayout(pnlGrafico1);
        pnlGrafico1.setLayout(pnlGrafico1Layout);
        pnlGrafico1Layout.setHorizontalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGrafico1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1068, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
        pnlGrafico1Layout.setVerticalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGrafico1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlGrafico1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
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
        
        fechaIncial = ini.getFechaSql(this.jFechaInicial);
        fechaFinal = ini.getFechaSql(this.jFechaFinal);
        String ss = "" + this.jFechaFinal.getDate();
        listaDeRegistros = new ArrayList();
        if (txtPlaca.getText().equals("")) {
            new Thread(new FReporteGastosFlotaHilo(this, "general")).start();
        } else {
            placa = txtPlaca.getText().trim();
            new Thread(new FReporteGastosFlotaHilo(this, "por placa")).start();
        }

        txtPlaca.setEnabled(false);
        txtPlaca.setEditable(false);
        
        
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        btnExportarExcel.setEnabled(false);
        //new Thread(new FReporteGastosFlotaHilo(this, false)).start();


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
        limpiar();
        txtPlaca.setText("");
        txtPlaca.setEnabled(true);
        txtPlaca.setEditable(true);
        btnAceptar.setEnabled(true);
        jBtnExcel.setEnabled(false);
        jFechaFinal.setEnabled(true);
        jFechaInicial.setEnabled(true);

    }//GEN-LAST:event_jBtnNuevoActionPerformed
    
    public void llenarTablabitacora() throws Exception {
        
        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeRegistros.size());
        
        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();
        
        for (GastosPorVehiculo obj : listaDeRegistros) {
            filaTabla2 = tablaRegistros.getRowCount();
            
            modelo.addRow(new Object[tablaRegistros.getRowCount()]);
            
            tablaRegistros.setValueAt(obj.getFechaRecibo(), filaTabla2, 0);  // item
            tablaRegistros.setValueAt(obj.getNumeroRecibo(), filaTabla2, 1); // numero de manifiesto
            tablaRegistros.setValueAt(obj.getNombreSucursal(), filaTabla2, 2); // fecha de distribucion
            tablaRegistros.setValueAt(obj.getPlaca(), filaTabla2, 3); // placa del vehiculo
            tablaRegistros.setValueAt(obj.getNombreConductor(), filaTabla2, 4); // nombre del conductor
            tablaRegistros.setValueAt(obj.getCantidad(), filaTabla2, 5); // nombre del conductor
            tablaRegistros.setValueAt(obj.getDescripcionProductoServicio(), filaTabla2, 6); // nombre del conductor
            tablaRegistros.setValueAt(nf.format(Double.parseDouble(obj.getValorUnitario())), filaTabla2, 7); // nombre del conductor
            tablaRegistros.setValueAt(nf.format(Double.parseDouble(obj.getValorTotal())), filaTabla2, 8); // nombre del conductor
            
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barra;
    private javax.swing.JButton btnAceptar;
    public javax.swing.JButton btnExportarExcel;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExcel;
    private javax.swing.JToggleButton jBtnExit;
    private javax.swing.JButton jBtnNuevo;
    public com.toedter.calendar.JDateChooser jFechaFinal;
    public com.toedter.calendar.JDateChooser jFechaInicial;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel47;
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
    
}
