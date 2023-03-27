/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.Reportes;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.objetos.CDescuentosPorFactura;
import aplicacionlogistica.costumerService.objetos.CFacturasAutorizadasDescuentos;
import aplicacionlogistica.costumerService.objetos.CRecogidasPorFactura;
import aplicacionlogistica.distribucion.formularios.reportes.HiloReportePedidosMovilizadosPorPeriodo;
import java.awt.Desktop;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FReporteClientesConDescuento extends javax.swing.JInternalFrame {

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

    public List<CFacturasAutorizadasDescuentos> listaDeFacturasAutorizadasConDescuento;
     NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public FReporteClientesConDescuento(Inicio ini) {
        initComponents();

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

        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FReporteClientesConDescuento.class.getName()).log(Level.SEVERE, null, ex);
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
        barra = new javax.swing.JProgressBar();
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

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario de consulta Facturas con descuentos autorizados");
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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta pedidos con descuentos autorizados"));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo.setEnabled(false);
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
        jToggleButton1.setEnabled(false);
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
                "id", "Factura", "Fecha", "Cliente", "Direccion", "%", "Valor", "# Dcto.", "valor Descto."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaDescuentos);
        if (tablaDescuentos.getColumnModel().getColumnCount() > 0) {
            tablaDescuentos.getColumnModel().getColumn(0).setMinWidth(20);
            tablaDescuentos.getColumnModel().getColumn(0).setPreferredWidth(20);
            tablaDescuentos.getColumnModel().getColumn(0).setMaxWidth(20);
            tablaDescuentos.getColumnModel().getColumn(1).setMinWidth(75);
            tablaDescuentos.getColumnModel().getColumn(1).setPreferredWidth(75);
            tablaDescuentos.getColumnModel().getColumn(1).setMaxWidth(75);
            tablaDescuentos.getColumnModel().getColumn(2).setMinWidth(85);
            tablaDescuentos.getColumnModel().getColumn(2).setPreferredWidth(85);
            tablaDescuentos.getColumnModel().getColumn(2).setMaxWidth(85);
            tablaDescuentos.getColumnModel().getColumn(3).setMinWidth(230);
            tablaDescuentos.getColumnModel().getColumn(3).setPreferredWidth(230);
            tablaDescuentos.getColumnModel().getColumn(3).setMaxWidth(230);
            tablaDescuentos.getColumnModel().getColumn(4).setMinWidth(200);
            tablaDescuentos.getColumnModel().getColumn(4).setPreferredWidth(200);
            tablaDescuentos.getColumnModel().getColumn(4).setMaxWidth(200);
            tablaDescuentos.getColumnModel().getColumn(5).setMinWidth(50);
            tablaDescuentos.getColumnModel().getColumn(5).setPreferredWidth(50);
            tablaDescuentos.getColumnModel().getColumn(5).setMaxWidth(50);
            tablaDescuentos.getColumnModel().getColumn(6).setMinWidth(120);
            tablaDescuentos.getColumnModel().getColumn(6).setPreferredWidth(120);
            tablaDescuentos.getColumnModel().getColumn(6).setMaxWidth(120);
            tablaDescuentos.getColumnModel().getColumn(7).setMinWidth(70);
            tablaDescuentos.getColumnModel().getColumn(7).setPreferredWidth(70);
            tablaDescuentos.getColumnModel().getColumn(7).setMaxWidth(70);
            tablaDescuentos.getColumnModel().getColumn(8).setMinWidth(110);
            tablaDescuentos.getColumnModel().getColumn(8).setPreferredWidth(110);
            tablaDescuentos.getColumnModel().getColumn(8).setMaxWidth(110);
        }

        javax.swing.GroupLayout pnlDescuentosLayout = new javax.swing.GroupLayout(pnlDescuentos);
        pnlDescuentos.setLayout(pnlDescuentosLayout);
        pnlDescuentosLayout.setHorizontalGroup(
            pnlDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDescuentosLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 987, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlDescuentosLayout.setVerticalGroup(
            pnlDescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDescuentosLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jTabbedRecogidas_Descuentos.addTab("Panel Facturas con Descuentos autorizados", pnlDescuentos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedRecogidas_Descuentos))
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

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        limpiar();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void limpiar() {
        listaDeFacturasAutorizadasConDescuento = null;

        cuadrarFechaJDateChooser();
        limpiarTablaDescuentos();

        barra.setValue(0);
        file = null;
    }

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);

    }//GEN-LAST:event_jBtnExitActionPerformed

    private void jBtnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExcelActionPerformed

        try {
            jBtnExcel.setEnabled(false);
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(FReporteClientesConDescuento.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jBtnExcelActionPerformed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        cargarInformacion();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    public void cargarInformacion() {
        jBtnNuevo.setEnabled(false);
        limpiar();
        new Thread(new HiloReporteFacturasAutorizadasConDescuentos(this)).start();
    }

    public void llenarTablaFacturasConDescuentos() throws Exception {

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeFacturasAutorizadasConDescuento.size());

        DefaultTableModel modelo = (DefaultTableModel) tablaDescuentos.getModel();

        for (CFacturasAutorizadasDescuentos obj : listaDeFacturasAutorizadasConDescuento) {
            filaTabla2 = tablaDescuentos.getRowCount();

            modelo.addRow(new Object[tablaDescuentos.getRowCount()]);

            tablaDescuentos.setValueAt(filaTabla2 + 1, filaTabla2, 0);  // item
            tablaDescuentos.setValueAt(obj.getNumeroFactura(), filaTabla2, 1);  // item
            tablaDescuentos.setValueAt(obj.getFechaDeVenta(), filaTabla2, 2); // nombre del conductor
            tablaDescuentos.setValueAt(obj.getNombreDeCliente(), filaTabla2, 3); // numero de manifiesto
            tablaDescuentos.setValueAt(obj.getDireccionDeCliente(), filaTabla2, 4); // fecha de distribucion
            tablaDescuentos.setValueAt(obj.getPorcentajeDesuento(), filaTabla2, 5); // fecha de distribucion 
            tablaDescuentos.setValueAt(nf.format(obj.getValorFacturaSinIva()), filaTabla2, 6); // placa del vehiculo
            tablaDescuentos.setValueAt(obj.getNumeroDescuento(), filaTabla2, 7); // nombre del conductor
            tablaDescuentos.setValueAt(obj.getValorDescuento(), filaTabla2, 8); // nombre del conductor

        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barra;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExcel;
    private javax.swing.JToggleButton jBtnExit;
    private javax.swing.JButton jBtnNuevo;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedRecogidas_Descuentos;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JPanel pnlDescuentos;
    private javax.swing.JTable tablaDescuentos;
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

}
