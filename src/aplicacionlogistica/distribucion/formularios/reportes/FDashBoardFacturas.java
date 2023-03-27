/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloConsultarFacturxxx;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.integrador.HiloIntegradorTNSLaHielera;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasPorManifiesto;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import ui.swing.tableCustom.TableCustom;

/**
 *
 * @author VLI_488
 */
public class FDashBoardFacturas extends javax.swing.JInternalFrame {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    HiloReportePedidosMovilizadosPorPeriodo datos;
    public Inicio ini;
    int filaTabla2;

    public ArrayList<Vst_FacturasPorManifiesto> listaDeFacturasSinMovimiento = null;
    public int cantidadGraficos = 0;

    public File reporteFinal;

    Timer timer;
    Timer timer2;

    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public FDashBoardFacturas(Inicio ini) {
        this.ini = ini;
        initComponents();
        setResizable(true);
        setSize((int) ini.getDimension().getWidth() + 5, (int) ini.getDimension().getHeight() - 20);
        setLocation(0, -20);

        TableCustom.apply(jScrollPane1, TableCustom.TableType.DEFAULT);

        HiloIntegradorTNSLaHielera hilo = new HiloIntegradorTNSLaHielera(ini, 4);
        traerPedidosPendientes();
        traerPedidosDelDia();

        // new Thread(new HiloIntegradorTNSLaHielera(this.ini)).start();
        int tiempoDeEperaBloqueo = 1000 * 60 * 5;
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                traerPedidosDelDia();
                traerPedidosPendientes();

            }

        };
        timer = new Timer(tiempoDeEperaBloqueo, actionListener);
        timer.start();

        ActionListener actionListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                HiloIntegradorTNSLaHielera hilo = new HiloIntegradorTNSLaHielera(ini, 4);

            }

        };
        timer2 = new Timer(tiempoDeEperaBloqueo, actionListener2);
        timer2.start();

        listaPendientes.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                JList l = (JList) e.getSource();
                ListModel m = l.getModel();
                if (m.getSize() > 0) {
                    int index = l.locationToIndex(e.getPoint());

                    String cadena = m.getElementAt(index).toString();
                    String[] valor = cadena.split(" ", 2);
                    for (CFacturas fac : ini.getListaDeFacturasSinMovimiento()) {
                        if (valor[0].equals(fac.getNumeroDeFactura())) {
                            if (fac.getObservaciones() == null || fac.getObservaciones().contains("null") || fac.getObservaciones().length() == 0) {
                                l.setToolTipText("<html>"
                                        + "<div style=width:300;>"
                                        + "El pedido # " + fac.getNumeroDeFactura() + "<br>"
                                        + " No Tiene observaciones"
                                        + "</div><br>"
                                        + "</html>");
                            } else {
                                l.setToolTipText("<html>"
                                        + "<div style=width:300;>"
                                        + "El pedido # " + fac.getNumeroDeFactura() + "<br>"
                                        + "tiene las siguientes observaciones : <br>"
                                        + fac.getObservaciones()
                                        + "</div><br>"
                                        + "</html>");
                            }

                            break;
                        }
                    }
                }

//            if( index>-1 ) {
//                l.setToolTipText(m.getElementAt(index).toString());
//            }
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlGrafico1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRegistros = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaPendientes = new javax.swing.JList<>();
        lblPendientes = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaAgendados = new javax.swing.JList<>();
        lblAgendados = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setFrameIcon(null);
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

        pnlGrafico1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setToolTipText("");

        tablaRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "numeroFactura", "nombreDeCliente", "direccionDeCliente", "Conductor", "Movimiento Factura"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaRegistros.setColumnSelectionAllowed(true);
        tablaRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaRegistrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaRegistros);
        tablaRegistros.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tablaRegistros.getColumnModel().getColumnCount() > 0) {
            tablaRegistros.getColumnModel().getColumn(0).setPreferredWidth(140);
            tablaRegistros.getColumnModel().getColumn(1).setPreferredWidth(300);
            tablaRegistros.getColumnModel().getColumn(2).setPreferredWidth(300);
            tablaRegistros.getColumnModel().getColumn(3).setPreferredWidth(300);
            tablaRegistros.getColumnModel().getColumn(4).setPreferredWidth(300);
        }

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        listaPendientes.setBackground(new java.awt.Color(249, 24, 7));
        listaPendientes.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        listaPendientes.setForeground(new java.awt.Color(255, 255, 255));
        listaPendientes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(listaPendientes);

        lblPendientes.setBackground(new java.awt.Color(249, 24, 7));
        lblPendientes.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        lblPendientes.setForeground(new java.awt.Color(255, 255, 255));
        lblPendientes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPendientes.setText("Facturas Pendientes");
        lblPendientes.setOpaque(true);

        listaAgendados.setBackground(new java.awt.Color(1, 109, 254));
        listaAgendados.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        listaAgendados.setForeground(new java.awt.Color(255, 255, 255));
        listaAgendados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(listaAgendados);

        lblAgendados.setBackground(new java.awt.Color(1, 109, 254));
        lblAgendados.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        lblAgendados.setForeground(new java.awt.Color(255, 255, 255));
        lblAgendados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAgendados.setText("Facturas Agendados");
        lblAgendados.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(lblPendientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                    .addComponent(lblAgendados, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(lblAgendados, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(196, 196, 196)
                .addComponent(lblPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(407, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout pnlGrafico1Layout = new javax.swing.GroupLayout(pnlGrafico1);
        pnlGrafico1.setLayout(pnlGrafico1Layout);
        pnlGrafico1Layout.setHorizontalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGrafico1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlGrafico1Layout.setVerticalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGrafico1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlGrafico1, javax.swing.GroupLayout.PREFERRED_SIZE, 1360, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlGrafico1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing


    }//GEN-LAST:event_formInternalFrameClosing

    private void tablaRegistrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaRegistrosMouseClicked

        new Thread(new HiloConsultarFacturxxx(ini, this)).start();
        //  new Thread(new HiloConsultarFacturxxx(ini, this)).start();
// TODO add your handling code here:
    }//GEN-LAST:event_tablaRegistrosMouseClicked

    private void limpiar() {
        // listaDeRegistros = null;
        // cuadrarFechaJDateChooser();
        limpiarTabla();

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
            long max = gc.getTimeInMillis();

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

    public void llenarTablaListaDeFacturasSinMovimiento() throws Exception {

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeFacturasSinMovimiento.size());

        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();

        for (Vst_FacturasPorManifiesto obj : listaDeFacturasSinMovimiento) {
            filaTabla2 = tablaRegistros.getRowCount();

            modelo.addRow(new Object[tablaRegistros.getRowCount()]);
            tablaRegistros.setValueAt(filaTabla2 + 1, filaTabla2, 0);  // item
            tablaRegistros.setValueAt(obj.getNumeroFactura(), filaTabla2, 1);  // item
            tablaRegistros.setValueAt(obj.getFechaDeVenta(), filaTabla2, 2); // numero de manifiesto
            //tablaRegistros.setValueAt(obj.getNombreCanalDeVenta(), filaTabla2, 3); // numero de manifiesto
            //tablaRegistros.setValueAt(obj.getIdCliente(), filaTabla2, 3); // fecha de distribucion
            tablaRegistros.setValueAt(obj.getNombreDeCliente(), filaTabla2, 3); // placa del vehiculo
            tablaRegistros.setValueAt(obj.getDireccionCliente(), filaTabla2, 4); // nombre del conductor
            if (obj.getObservaciones() == null || obj.getObservaciones().equals("null")) {
                obj.setObservaciones("");
            }
            tablaRegistros.setValueAt(obj.getObservaciones(), filaTabla2, 5); // nombre del conductor

            if (obj.getIsFree() == 0) {
                tablaRegistros.setValueAt(obj.getNombreConductor(), filaTabla2, 6); // nombre del conductor

            } else {
                tablaRegistros.setValueAt("", filaTabla2, 6); // nombre del conductor

            }
            // tablaRegistros.setValueAt(obj.getCiudadCliente(), filaTabla2, 8); // nombre del conductor
            tablaRegistros.setValueAt(obj.getTelefonoCliente(), filaTabla2, 7); // nombre del conductor
            if (obj.getIsFree() == 1) {
                tablaRegistros.setValueAt("", filaTabla2, 8); // nombre del conductor  
            } else {
                if (obj.getEstadoFactura() == 1) {
                    tablaRegistros.setValueAt("EN ZONA DE REPARTO", filaTabla2, 8); // nombre del conductor

                } else {
                    tablaRegistros.setValueAt(obj.getNombreEstadoFactura(), filaTabla2, 8); // nombre del conductor  
                }

            }

            tablaRegistros.setValueAt(nf.format(obj.getValorFacturaSinIva()), filaTabla2, 9); // nombre del conductor
            tablaRegistros.setValueAt(nf.format(obj.getValorTotalFactura()), filaTabla2, 10); // nombre del conductor

        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAgendados;
    private javax.swing.JLabel lblPendientes;
    private javax.swing.JList<String> listaAgendados;
    private javax.swing.JList<String> listaPendientes;
    public javax.swing.JPanel pnlGrafico1;
    public javax.swing.JTable tablaRegistros;
    // End of variables declaration//GEN-END:variables

    public void consultarLafactura(CFacturas facturaActual) {

        try {
            if (facturaActual != null) {

                FConsultarFacturasRemoto formulario = new FConsultarFacturasRemoto(ini, facturaActual);
                this.getDesktopPane().add(formulario);
                formulario.toFront();
                formulario.setClosable(true);
                formulario.setVisible(true);
                formulario.setTitle("Formulario para consultar Facturas");
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

                //form.setSize(PrincipalLogistica.escritorio.getSize());
                formulario.setLocation((screenSize.width - formulario.getSize().width) / 2, (screenSize.height - formulario.getSize().height) / 2);
                formulario.show();
            } else {
                JOptionPane.showInternalMessageDialog(this, "La factura no aparece en el sistema", "Factura no visible ", 2);
            }

        } catch (Exception ex) {
            Logger.getLogger(FDashBoardFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void traerPedidosPendientes() {

        Thread thPedidosPendients = new Thread(new Runnable() {
            @Override
            public void run() {

                ini.setListaDeFacturasSinMovimiento();

                if (ini.getListaDeFacturasSinMovimiento() != null || ini.getListaDeFacturasSinMovimiento().size() > 0) {
                    System.out.print("trae " + ini.getListaDeFacturasSinMovimiento().size() + " de facturas sin movimiento");
                   
                    DefaultListModel<String> modeloPendientes = new DefaultListModel<>();
                    DefaultListModel<String> modeloAgendados = new DefaultListModel<>();

                    for (CFacturas fac : ini.getListaDeFacturasSinMovimiento()) {
                        if(fac.getObservaciones().contains("###")){
                        modeloAgendados.addElement(fac.getNumeroDeFactura() + "  -->  " + fac.getFechaIng().substring(11, fac.getFechaIng().length()));

                        }else{
                          modeloPendientes.addElement(fac.getNumeroDeFactura() + "  -->  " + fac.getFechaIng().substring(11, fac.getFechaIng().length()));
  
                        }
                        
                    }
                    listaPendientes.setModel(modeloPendientes);
                    listaAgendados.setModel(modeloAgendados);
                    repaint();
                }

            }

        });
        thPedidosPendients.start();

    }

    public void traerPedidosDelDia() {

        Thread thPedidosDelDia = new Thread(new Runnable() {
            @Override
            public void run() {

                ini.setListaDeFacturasDelDia();

                if (ini.getListaDeFacturasDelDia() != null || ini.getListaDeFacturasDelDia().size() > 0) {
                    try {
                        llenarTablaListaDepedidosDelDia();
                    } catch (Exception ex) {
                        Logger.getLogger(FDashBoardFacturas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        });
        thPedidosDelDia.start();

    }

    private void llenarTablaListaDepedidosDelDia() {
        limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();

        for (CFacturas obj : ini.getListaDeFacturasDelDia()) {
            filaTabla2 = tablaRegistros.getRowCount();

            modelo.addRow(new Object[tablaRegistros.getRowCount()]);
            tablaRegistros.setValueAt(obj.getNumeroDeFactura(), filaTabla2, 0);  // item
            tablaRegistros.setValueAt(obj.getNombreDeCliente(), filaTabla2, 1); // placa del vehiculo

            tablaRegistros.setValueAt(obj.getDireccionDeCliente(), filaTabla2, 2); // nombre del conductor

            if (obj.getIsFree() == 0) {

                if (obj.getNombreDeConductor() == null || obj.getNombreDeConductor().length() < 1) {
                    tablaRegistros.setValueAt("SIN ASIGNAR ", filaTabla2, 3); // nombre del conductor

                } else {
                    tablaRegistros.setValueAt(obj.getNombreDeConductor(), filaTabla2, 3); // nombre del conductor

                }

            } else {
                tablaRegistros.setValueAt("SIN ASIGNAR ", filaTabla2, 3); // nombre del conductor

            }
            // tablaRegistros.setValueAt(obj.getCiudadCliente(), filaTabla2, 8); // nombre del conductor

            if (obj.getIsFree() == 1) {
                tablaRegistros.setValueAt("", filaTabla2, 4); // nombre del conductor  
            } else {
                if (obj.getEstadoFactura() == 1) {
                    tablaRegistros.setValueAt("EN ZONA DE REPARTO", filaTabla2, 4); // nombre del conductor

                } else {
                    tablaRegistros.setValueAt(obj.getNombreEstadoFactura(), filaTabla2, 4); // nombre del conductor  
                }

            }

        }
        repaint();
    }
}
