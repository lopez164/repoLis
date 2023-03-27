/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.Reportes;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.objetos.CDescuentosPorFactura;
import aplicacionlogistica.costumerService.objetos.CRecogidasPorFactura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasDescargadas;
import aplicacionlogistica.distribucion.formularios.reportes.HiloReportePedidosMovilizadosPorPeriodo;
import java.awt.Desktop;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FReporteDescuentos_Recogidas extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    HiloReportePedidosMovilizadosPorPeriodo datos;
    public Inicio ini;
    int filaTabla2;

    File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    public int cantidadGraficos = 0;
    public List<CDescuentosPorFactura> listaDeDescuentos;
    public List<CRecogidasPorFactura> listaDeRecogidas;
    public Date fechaIncial;
    public Date fechaFinal;

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public FReporteDescuentos_Recogidas(Inicio ini) {
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
            Logger.getLogger(FReporteDescuentos_Recogidas.class.getName()).log(Level.SEVERE, null, ex);
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
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExcel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();
        jTabbedRecogidas_Descuentos = new javax.swing.JTabbedPane();
        pnlDescuentos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDescuentos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaRecogidas = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario de consulta movilizado por periodo");
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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta pedidos movilizados por rango"));

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 851, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFechaInicial, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jFechaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExportarExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(5, 5, 5)
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

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

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jToggleButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jToggleButton1);

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

        jBtnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        jBtnExcel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExcel.setFocusable(false);
        jBtnExcel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExcel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExcelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExcel);

        jBtnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jBtnExit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExit.setFocusable(false);
        jBtnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExitActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExit);

        pnlDescuentos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaDescuentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Factura", "Fecha", "Cliente", "Direccion", "Valor", "numero Dcto.", "valor Descto."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaDescuentos);
        if (tablaDescuentos.getColumnModel().getColumnCount() > 0) {
            tablaDescuentos.getColumnModel().getColumn(0).setMinWidth(50);
            tablaDescuentos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tablaDescuentos.getColumnModel().getColumn(0).setMaxWidth(50);
            tablaDescuentos.getColumnModel().getColumn(1).setMinWidth(80);
            tablaDescuentos.getColumnModel().getColumn(1).setPreferredWidth(80);
            tablaDescuentos.getColumnModel().getColumn(1).setMaxWidth(80);
            tablaDescuentos.getColumnModel().getColumn(2).setMinWidth(90);
            tablaDescuentos.getColumnModel().getColumn(2).setPreferredWidth(90);
            tablaDescuentos.getColumnModel().getColumn(2).setMaxWidth(90);
            tablaDescuentos.getColumnModel().getColumn(3).setMinWidth(200);
            tablaDescuentos.getColumnModel().getColumn(3).setPreferredWidth(200);
            tablaDescuentos.getColumnModel().getColumn(3).setMaxWidth(200);
            tablaDescuentos.getColumnModel().getColumn(4).setMinWidth(200);
            tablaDescuentos.getColumnModel().getColumn(4).setPreferredWidth(200);
            tablaDescuentos.getColumnModel().getColumn(4).setMaxWidth(200);
            tablaDescuentos.getColumnModel().getColumn(5).setMinWidth(90);
            tablaDescuentos.getColumnModel().getColumn(5).setPreferredWidth(90);
            tablaDescuentos.getColumnModel().getColumn(5).setMaxWidth(90);
            tablaDescuentos.getColumnModel().getColumn(6).setMinWidth(100);
            tablaDescuentos.getColumnModel().getColumn(6).setPreferredWidth(100);
            tablaDescuentos.getColumnModel().getColumn(6).setMaxWidth(100);
            tablaDescuentos.getColumnModel().getColumn(7).setMinWidth(110);
            tablaDescuentos.getColumnModel().getColumn(7).setPreferredWidth(110);
            tablaDescuentos.getColumnModel().getColumn(7).setMaxWidth(110);
        }

        javax.swing.GroupLayout pnlDescuentosLayout = new javax.swing.GroupLayout(pnlDescuentos);
        pnlDescuentos.setLayout(pnlDescuentosLayout);
        pnlDescuentosLayout.setHorizontalGroup(
            pnlDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDescuentosLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 937, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        pnlDescuentosLayout.setVerticalGroup(
            pnlDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDescuentosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jTabbedRecogidas_Descuentos.addTab("Panel descuentos", pnlDescuentos);

        tablaRecogidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Factura", "Fecha", "Cliente", "Direccion", "Valor", "numero Dcto.", "valor Descto."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablaRecogidas);
        if (tablaRecogidas.getColumnModel().getColumnCount() > 0) {
            tablaRecogidas.getColumnModel().getColumn(0).setMinWidth(50);
            tablaRecogidas.getColumnModel().getColumn(0).setPreferredWidth(50);
            tablaRecogidas.getColumnModel().getColumn(0).setMaxWidth(50);
            tablaRecogidas.getColumnModel().getColumn(1).setMinWidth(80);
            tablaRecogidas.getColumnModel().getColumn(1).setPreferredWidth(80);
            tablaRecogidas.getColumnModel().getColumn(1).setMaxWidth(80);
            tablaRecogidas.getColumnModel().getColumn(2).setMinWidth(90);
            tablaRecogidas.getColumnModel().getColumn(2).setPreferredWidth(90);
            tablaRecogidas.getColumnModel().getColumn(2).setMaxWidth(90);
            tablaRecogidas.getColumnModel().getColumn(3).setMinWidth(200);
            tablaRecogidas.getColumnModel().getColumn(3).setPreferredWidth(200);
            tablaRecogidas.getColumnModel().getColumn(3).setMaxWidth(200);
            tablaRecogidas.getColumnModel().getColumn(4).setMinWidth(200);
            tablaRecogidas.getColumnModel().getColumn(4).setPreferredWidth(200);
            tablaRecogidas.getColumnModel().getColumn(4).setMaxWidth(200);
            tablaRecogidas.getColumnModel().getColumn(5).setMinWidth(90);
            tablaRecogidas.getColumnModel().getColumn(5).setPreferredWidth(90);
            tablaRecogidas.getColumnModel().getColumn(5).setMaxWidth(90);
            tablaRecogidas.getColumnModel().getColumn(6).setMinWidth(100);
            tablaRecogidas.getColumnModel().getColumn(6).setPreferredWidth(100);
            tablaRecogidas.getColumnModel().getColumn(6).setMaxWidth(100);
            tablaRecogidas.getColumnModel().getColumn(7).setMinWidth(110);
            tablaRecogidas.getColumnModel().getColumn(7).setPreferredWidth(110);
            tablaRecogidas.getColumnModel().getColumn(7).setMaxWidth(110);
        }

        jTabbedRecogidas_Descuentos.addTab("Panel Recogidas", jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 901, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 892, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedRecogidas_Descuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 947, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedRecogidas_Descuentos, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
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
        // HiloCrearChartBarras1 FgraficoPedidosMovilizados = new HiloCrearChartBarras1(this.ini,this,fechaIncial,fechaFinal) ;

        new Thread(new HiloReporteDescuentosRecogidas(this)).start();


    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        try {
            btnExportarExcel.setEnabled(false);
            // new Thread(new HiloReportePedidosMovilizadosPorPeriodo(this, false)).start();
            Desktop.getDesktop().open(file);
            
        } catch (IOException ex) {
            Logger.getLogger(FReporteDescuentos_Recogidas.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        limpiar();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void limpiar() {
        listaDeDescuentos = null;
        listaDeRecogidas = null;
        
        cuadrarFechaJDateChooser();
        limpiarTablaDescuentos();
         limpiarTablaRecogidos();
        barra.setValue(0);
        btnAceptar.setEnabled(true);
        file=null;
    }

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);

    }//GEN-LAST:event_jBtnExitActionPerformed

    private void jBtnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExcelActionPerformed
       
       try {
            btnExportarExcel.setEnabled(false);
            // new Thread(new HiloReportePedidosMovilizadosPorPeriodo(this, false)).start();
            Desktop.getDesktop().open(file);
            
        } catch (IOException ex) {
            Logger.getLogger(FReporteDescuentos_Recogidas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnExcelActionPerformed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
       limpiar();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    public void llenarTablaDescuentos() throws Exception {

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeDescuentos.size());

        DefaultTableModel modelo = (DefaultTableModel) tablaDescuentos.getModel();

        for (CDescuentosPorFactura obj : listaDeDescuentos) {
            filaTabla2 = tablaDescuentos.getRowCount();

            modelo.addRow(new Object[tablaDescuentos.getRowCount()]);
            tablaDescuentos.setValueAt(filaTabla2 + 1, filaTabla2, 0);  // item
            tablaDescuentos.setValueAt(obj.getNumeroFactura(), filaTabla2, 1);  // item
             tablaDescuentos.setValueAt(obj.getFechaDeVenta(), filaTabla2, 2); // nombre del conductor
            tablaDescuentos.setValueAt(obj.getNombreCliente(), filaTabla2, 3); // numero de manifiesto
            tablaDescuentos.setValueAt(obj.getDireccionCliente(), filaTabla2, 4); // fecha de distribucion
            tablaDescuentos.setValueAt(obj.getValorTotalFactura(), filaTabla2, 5); // placa del vehiculo
            tablaDescuentos.setValueAt(obj.getNumeroDocumento(), filaTabla2, 6); // nombre del conductor
            tablaDescuentos.setValueAt(obj.getValorDescuento(), filaTabla2, 7); // nombre del conductor
           
           

        }
    }

    public void llenarTablaRecogidas() throws Exception {

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeRecogidas.size());

        DefaultTableModel modelo = (DefaultTableModel) tablaRecogidas.getModel();

        for (CRecogidasPorFactura obj : listaDeRecogidas) {
            filaTabla2 = tablaRecogidas.getRowCount();

            modelo.addRow(new Object[tablaRecogidas.getRowCount()]);
            tablaRecogidas.setValueAt(filaTabla2 + 1, filaTabla2, 0);  // item
            tablaRecogidas.setValueAt(obj.getNumeroFactura(), filaTabla2, 1);  // item
             tablaRecogidas.setValueAt(obj.getFechaDeVenta(), filaTabla2, 2); // nombre del conductor
            tablaRecogidas.setValueAt(obj.getNombreCliente(), filaTabla2, 3); // numero de manifiesto
            tablaRecogidas.setValueAt(obj.getDireccionCliente(), filaTabla2, 4); // fecha de distribucion
            tablaRecogidas.setValueAt(obj.getValorTotalFactura(), filaTabla2, 5); // placa del vehiculo
            tablaRecogidas.setValueAt(obj.getNumeroDocumento(), filaTabla2, 6); // nombre del conductor
            tablaRecogidas.setValueAt(obj.getValorRecogida(), filaTabla2, 7); // nombre del conductor
           
           

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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedRecogidas_Descuentos;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JPanel pnlDescuentos;
    private javax.swing.JTable tablaDescuentos;
    private javax.swing.JTable tablaRecogidas;
    // End of variables declaration//GEN-END:variables

    public void limpiarTablaDescuentos() {

        DefaultTableModel modelo = (DefaultTableModel) tablaDescuentos.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }
    
     public void limpiarTablaRecogidos() {

        DefaultTableModel modelo = (DefaultTableModel) tablaRecogidas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

}
