/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosMovilizadosPorPeriodo;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class FIntegradorExcelFile extends javax.swing.JInternalFrame {

   
    public Date fechaIncial;
    public static boolean band = false;
    public static int valorDespBarraProgreso = 0;
    boolean actualizar = false;
    IntegradorWin integrador;
    public String mensaje;
    public Inicio ini;
    
    public boolean cancelar=false;

    /**
     * Creates new form FBuscar2
     */
    public FIntegradorExcelFile() {
        initComponents();

    }

    public FIntegradorExcelFile(Inicio ini) {
        this.ini = ini;
        initComponents();
        jBtnCirculo.setVisible(false);
        jFechaInicial.setDate(new Date());
       

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jFechaInicial = new com.toedter.calendar.JDateChooser();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jBtnCirculo = new javax.swing.JToggleButton();
        jBtnIniciar = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        chkDetalleFacturas = new javax.swing.JCheckBox();
        chkClientes = new javax.swing.JCheckBox();
        chkProductos = new javax.swing.JCheckBox();
        chkFacturas = new javax.swing.JCheckBox();
        barra1 = new javax.swing.JProgressBar();
        barra2 = new javax.swing.JProgressBar();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        chkTrasmisionCompleta = new javax.swing.JCheckBox();
        lblBarra2 = new javax.swing.JLabel();
        lblBarra1 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Formulario para la sincronizacion de BBDD");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N

        jFechaInicial.setToolTipText("Seleccionar la fecha ");
        jFechaInicial.setDateFormatString("yyyy/MM/dd");
        jFechaInicial.setEnabled(false);
        jFechaInicial.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Fecha Incial");

        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane1.setViewportView(txtLog);

        jBtnCirculo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        jBtnCirculo.setBorder(null);
        jBtnCirculo.setFocusable(false);
        jBtnCirculo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCirculo.setPreferredSize(new java.awt.Dimension(24, 24));
        jBtnCirculo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnCirculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCirculoActionPerformed(evt);
            }
        });

        jBtnIniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/OK.png"))); // NOI18N
        jBtnIniciar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnIniciar.setEnabled(false);
        jBtnIniciar.setFocusable(false);
        jBtnIniciar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnIniciar.setPreferredSize(new java.awt.Dimension(24, 24));
        jBtnIniciar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnIniciarActionPerformed(evt);
            }
        });

        chkDetalleFacturas.setSelected(true);
        chkDetalleFacturas.setText("Detalle Facturas");

        chkClientes.setSelected(true);
        chkClientes.setText("Clientes");

        chkProductos.setSelected(true);
        chkProductos.setText("Productos");

        chkFacturas.setSelected(true);
        chkFacturas.setText("Facturas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(chkClientes)
                .addGap(18, 18, 18)
                .addComponent(chkProductos)
                .addGap(18, 18, 18)
                .addComponent(chkFacturas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkDetalleFacturas)
                .addGap(238, 238, 238))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkDetalleFacturas)
                    .addComponent(chkClientes)
                    .addComponent(chkProductos)
                    .addComponent(chkFacturas))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnCirculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnCirculo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addContainerGap())
        );

        barra1.setStringPainted(true);

        barra2.setStringPainted(true);

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

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        btnGrabar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGrabar.setEnabled(false);
        btnGrabar.setFocusable(false);
        btnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabar.setPreferredSize(new java.awt.Dimension(24, 24));
        btnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGrabar);

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

        chkTrasmisionCompleta.setText("Sincronizacion Completa");
        chkTrasmisionCompleta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTrasmisionCompletaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(chkTrasmisionCompleta)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(chkTrasmisionCompleta)
                .addGap(1, 1, 1))
        );

        lblBarra2.setText(".");

        lblBarra1.setText(".");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(barra1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(barra2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGap(5, 5, 5))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblBarra2)
                            .addContainerGap()))
                    .addComponent(lblBarra1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(lblBarra1)
                .addGap(1, 1, 1)
                .addComponent(barra1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(lblBarra2)
                .addGap(1, 1, 1)
                .addComponent(barra2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        limpiar();
        cancelar=false;
        chkClientes.setSelected(true);
        chkDetalleFacturas.setSelected(true);
        chkFacturas.setSelected(true);
        chkProductos.setSelected(true);
        jBtnNuevo.setEnabled(false);
        jBtnIniciar.setEnabled(true);
        jFechaInicial.setEnabled(true);
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        limpiar();
        jBtnNuevo.setEnabled(true);
        jBtnIniciar.setEnabled(false);
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_jBtnExitActionPerformed

    private void jBtnCirculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCirculoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnCirculoActionPerformed

    private void jBtnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnIniciarActionPerformed
        String fecha = "" + ini.getFechaSql(this.jFechaInicial);
        String ruta = ini.getRutaDeApp();
        band = false;
        cancelar=false;
        jBtnNuevo.setEnabled(false);
       

        //new Thread(new JcProgressBarIntegrador(this.barra, 500)).start();
       // new Thread(new JcProgressBarIntegrador(this.barra1, 500)).start();
        //new Thread(new HiloIntegrador(this)).start();
        
      
            new Thread(new HiloIntegradorTNSSQLFile(this)).start();
        
       
        jBtnIniciar.setEnabled(false);

    }//GEN-LAST:event_jBtnIniciarActionPerformed

    private void chkTrasmisionCompletaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTrasmisionCompletaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkTrasmisionCompletaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barra1;
    public javax.swing.JProgressBar barra2;
    private javax.swing.JToggleButton btnGrabar;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JCheckBox chkClientes;
    public javax.swing.JCheckBox chkDetalleFacturas;
    public javax.swing.JCheckBox chkFacturas;
    public javax.swing.JCheckBox chkProductos;
    public javax.swing.JCheckBox chkTrasmisionCompleta;
    private javax.swing.JToggleButton jBtnCancel;
    public javax.swing.JToggleButton jBtnCirculo;
    private javax.swing.JToggleButton jBtnExit;
    private javax.swing.JToggleButton jBtnIniciar;
    private javax.swing.JButton jBtnNuevo;
    public com.toedter.calendar.JDateChooser jFechaInicial;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblBarra1;
    public javax.swing.JLabel lblBarra2;
    public javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables

    private boolean habilitar(boolean valor) {
        boolean valida = false;

        return valida;
    }

    private void limpiar() {
        txtLog.setText("");
        band = false;
        barra1.setValue(0);
        barra2.setValue(0);
        jBtnIniciar.setEnabled(true);
        jBtnNuevo.setEnabled(false);
        jBtnCirculo.setVisible(false);
        cancelar=true;
        jFechaInicial.setEnabled(false);
        mensaje="";
        
    }

    private void cuadrarFechaJDateChooser() {
        //panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/configuracion/imagenes/perfil.jpg")));
        try {

            // HiloCrearChartBarras1 FgraficoPedidosMovilizados = new HiloCrearChartBarras1(this.ini,pnlGrafico1) ;
            Calendar calendario = Calendar.getInstance();
            int diaMinimo = calendario.getActualMinimum(Calendar.DAY_OF_MONTH);
            int anio = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            GregorianCalendar gc = new GregorianCalendar(anio, mes, diaMinimo);
            long min = gc.getTimeInMillis();

        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FReportePedidosMovilizadosPorPeriodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      public void finDeTrasmision(Date fecha1) {
        Date fecha2 = new Date();
        int segundos = (int) ((fecha2.getTime() - fecha1.getTime()) / 1000);
        mensaje += "Fin del proceso de trasmision de datos : " + new Date() + "\n";
        
       txtLog.setText(mensaje);
        if (segundos < 60) {
            mensaje += "total tiempo trascurrido en segundos =  " + segundos + "\n\n\n";
        }
        if (segundos >= 60 && segundos < 3600) {
            segundos = segundos / 60;
            mensaje += "total tiempo trascurrido en minutos  = " + segundos + "\n\n\n";;
        }   // mensaje += "total tiempo trascurrido en Minutos = " + segundos + "\n";;
       txtLog.setText(mensaje);
       band = true;
       jBtnCirculo.setVisible(false);
       barra1.setValue(100);
       barra2.setValue(100);
       JOptionPane.showInternalMessageDialog(this, "El proceso de sincronizacon  ha finalizado con exito", "Fin de la Trasmision", JOptionPane.INFORMATION_MESSAGE);

    }
      
      public static void main(){
          
      }

}
