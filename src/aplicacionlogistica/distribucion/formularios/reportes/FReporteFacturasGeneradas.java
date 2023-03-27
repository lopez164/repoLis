/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.threads.HiloFReporteFacturasGeneradas;
import aplicacionlogistica.distribucion.objetos.CFacturas;
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
public class FReporteFacturasGeneradas extends javax.swing.JInternalFrame {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    HiloReportePedidosMovilizadosPorPeriodo datos;
    public Inicio ini;
    int filaTabla2;

    public ArrayList<CFacturas> listaDeFacturas = null;
    public int cantidadGraficos = 0;
    public Date fechaFinal;
    public File reporteFinal;

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public FReporteFacturasGeneradas(Inicio ini) {
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

            gc = new GregorianCalendar(anio, mes, diaMaximo);
            min = gc.getTimeInMillis();
            jFechaFinal.setDate(new Date());

        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FReporteFacturasGeneradas.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel47 = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jFechaFinal = new com.toedter.calendar.JDateChooser();
        btnAceptar = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();
        barra = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        pnlGrafico1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRegistros = new javax.swing.JTable();
        jToolBar2 = new javax.swing.JToolBar();
        jBtnNuevo1 = new javax.swing.JButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jBtnCancel1 = new javax.swing.JToggleButton();
        jBtnExcel1 = new javax.swing.JToggleButton();
        jBtnExit1 = new javax.swing.JToggleButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario Reporte de Facturas Generadas en la fecha");
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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Reporte de FActuras Generadas en la fecha"));

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setLabelFor(jFechaFinal);
        jLabel47.setText("Fecha");

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jFechaFinal.setToolTipText("Seleccionar la fecha ");
        jFechaFinal.setDateFormatString("yyyy/MM/dd");
        jFechaFinal.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

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
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(503, Short.MAX_VALUE))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFechaFinal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnExportarExcel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlGrafico1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "numeroFactura", "numeroManifiesto", "Conductor", "fechaVenta", "nombreDeCliente", "direccionDeCliente", "Estado", "fecha", "operario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaRegistros);
        if (tablaRegistros.getColumnModel().getColumnCount() > 0) {
            tablaRegistros.getColumnModel().getColumn(0).setPreferredWidth(70);
            tablaRegistros.getColumnModel().getColumn(1).setPreferredWidth(100);
            tablaRegistros.getColumnModel().getColumn(2).setPreferredWidth(70);
            tablaRegistros.getColumnModel().getColumn(3).setPreferredWidth(280);
            tablaRegistros.getColumnModel().getColumn(4).setPreferredWidth(130);
            tablaRegistros.getColumnModel().getColumn(5).setPreferredWidth(280);
            tablaRegistros.getColumnModel().getColumn(6).setPreferredWidth(280);
            tablaRegistros.getColumnModel().getColumn(7).setPreferredWidth(180);
            tablaRegistros.getColumnModel().getColumn(8).setPreferredWidth(225);
            tablaRegistros.getColumnModel().getColumn(9).setPreferredWidth(115);
        }

        javax.swing.GroupLayout pnlGrafico1Layout = new javax.swing.GroupLayout(pnlGrafico1);
        pnlGrafico1.setLayout(pnlGrafico1Layout);
        pnlGrafico1Layout.setHorizontalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        pnlGrafico1Layout.setVerticalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );

        jToolBar2.setRollover(true);

        jBtnNuevo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo1.setFocusable(false);
        jBtnNuevo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnNuevo1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnNuevo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevo1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jBtnNuevo1);

        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jToggleButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToggleButton2.setEnabled(false);
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jToggleButton2);

        jBtnCancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnCancel1.setFocusable(false);
        jBtnCancel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCancel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancel1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jBtnCancel1);

        jBtnExcel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        jBtnExcel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExcel1.setEnabled(false);
        jBtnExcel1.setFocusable(false);
        jBtnExcel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExcel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExcel1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jBtnExcel1);

        jBtnExit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jBtnExit1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExit1.setFocusable(false);
        jBtnExit1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExit1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExit1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jBtnExit1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing


    }//GEN-LAST:event_formInternalFrameClosing

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        limpiar();
        btnAceptar.setEnabled(false);
        btnExportarExcel.setEnabled(false);

        fechaFinal = ini.getFechaSql(this.jFechaFinal);
        String ss = "" + this.jFechaFinal.getDate();
        // HiloCrearChartBarras1 FgraficoPedidosMovilizados = new HiloCrearChartBarras1(this.ini,this,fechaIncial,fechaFinal) ;

        new Thread(new HiloFReporteFacturasGeneradas(this, true)).start();


    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        try {
            //btnExportarExcel.setEnabled(false);
            //new Thread(new HiloListadoDeFacturasSinMovimiento(this, false)).start();
            //this.form.reporteFinal=new File("tmp/" + clave +".csv");

            Desktop.getDesktop().open(reporteFinal);

        } catch (IOException ex) {
            Logger.getLogger(FReporteFacturasGeneradas.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void jBtnNuevo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevo1ActionPerformed
        limpiar();
        btnAceptar.setEnabled(true);
        jFechaFinal.setEnabled(true);

    }//GEN-LAST:event_jBtnNuevo1ActionPerformed

    private void jBtnCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancel1ActionPerformed
        limpiar();
    }//GEN-LAST:event_jBtnCancel1ActionPerformed

    private void jBtnExcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExcel1ActionPerformed
        try {
            //btnExportarExcel.setEnabled(false);
            //new Thread(new HiloListadoDeFacturasSinMovimiento(this, false)).start();
            //this.form.reporteFinal=new File("tmp/" + clave +".csv");

            Desktop.getDesktop().open(reporteFinal);

        } catch (IOException ex) {
            Logger.getLogger(FReporteFacturasGeneradas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnExcel1ActionPerformed

    private void jBtnExit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExit1ActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_jBtnExit1ActionPerformed

    private void limpiar() {
        // listaDeRegistros = null;
        // cuadrarFechaJDateChooser();
        limpiarTabla();
        barra.setValue(0);

        btnAceptar.setEnabled(false);
        jBtnExcel1.setEnabled(false);
        btnExportarExcel.setEnabled(false);
        jBtnNuevo1.setEnabled(true);
        jFechaFinal.setEnabled(false);
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

            gc = new GregorianCalendar(anio, mes, diaMaximo);
            long max = gc.getTimeInMillis();

            jFechaFinal.setDate(new Date(max));

        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FReportePedidosEnDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void limpiarTabla() {

        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void llenarTablaListaDeFacturas() throws Exception {

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeFacturas.size());

        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();

        for (CFacturas obj : listaDeFacturas) {
            filaTabla2 = tablaRegistros.getRowCount();

            modelo.addRow(new Object[tablaRegistros.getRowCount()]);

            tablaRegistros.setValueAt(filaTabla2 + 1, filaTabla2, 0);  // item
            tablaRegistros.setValueAt(obj.getNumeroDeFactura(), filaTabla2, 1);  // item
//            tablaRegistros.setValueAt(obj.getNumeroManifiesto(), filaTabla2, 2); // numero de manifiesto
 //           tablaRegistros.setValueAt(obj.getNombreConductor(), filaTabla2, 3); // numero de manifiesto
            tablaRegistros.setValueAt(obj.getFechaDeVenta(), filaTabla2, 4); // fecha de distribucion
            tablaRegistros.setValueAt(obj.getNombreDeCliente(), filaTabla2, 5); // placa del vehiculo
            tablaRegistros.setValueAt(obj.getDireccionDeCliente(), filaTabla2, 6); // nombre del conductor
            tablaRegistros.setValueAt("PEDIDO VERIFICADO", filaTabla2, 7); // nombre del conductor

            // tablaRegistros.setValueAt(obj.getCiudadCliente(), filaTabla2, 8); // nombre del conductor
            //tablaRegistros.setValueAt("" + obj.getFechaDespachado(), filaTabla2, 8); // nombre del conductor
            // tablaRegistros.setValueAt(obj.getUsuariodespachador(), filaTabla2, 9); // nombre del conductor
            tablaRegistros.setValueAt(obj.getPlazoDias(), filaTabla2, 9); // numero de manifiesto
            tablaRegistros.setValueAt(obj.getEstadoFactura(), filaTabla2, 10); // nombre del conductor  
            tablaRegistros.setValueAt(nf.format(obj.getValorFacturaSinIva()), filaTabla2, 11); // nombre del conductor
            tablaRegistros.setValueAt(nf.format(obj.getValorTotalFactura()), filaTabla2, 12); // nombre del conductor

        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barra;
    public javax.swing.JButton btnAceptar;
    public javax.swing.JButton btnExportarExcel;
    private javax.swing.JToggleButton jBtnCancel1;
    public javax.swing.JToggleButton jBtnExcel1;
    private javax.swing.JToggleButton jBtnExit1;
    private javax.swing.JButton jBtnNuevo1;
    public com.toedter.calendar.JDateChooser jFechaFinal;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToolBar jToolBar2;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JPanel pnlGrafico1;
    private javax.swing.JTable tablaRegistros;
    // End of variables declaration//GEN-END:variables

}
