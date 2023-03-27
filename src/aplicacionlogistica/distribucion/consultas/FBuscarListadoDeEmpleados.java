/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas;

import aplicacionlogistica.distribucion.Threads.PerformanceInfiniteProgressPanel;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FGestionarIncidencias;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRutaConIntegrador;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera;
import aplicacionlogistica.distribucion.formularios.FModificarManifiesto;
import aplicacionlogistica.distribucion.formularios.Hielera.FControlarSalidaFacturasBarCode2;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHieleraConIntegracion;
import aplicacionlogistica.distribucion.formularios.poblaciones.FManifestarPedidosPoblaciones;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteManifiestosMovilizadosPorConductor;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor.FReportemovilizadoPorConductor;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.picking.FManifestarFacturasEnPicking;
import aplicacionlogistica.picking.FRegistroDePedidosFinLineaDePicking;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class FBuscarListadoDeEmpleados extends javax.swing.JInternalFrame {

    Object[] fila = new Object[3];
    DefaultTableModel modelo;
    String cedula = null;
    private PerformanceInfiniteProgressPanel panel;
    String usuario;
    Inicio ini;
    //List<CEmpleados> listaDeEmpleados;
    CEmpleados emp;
    int tipoDeRol;

    FManifestarPedidosEnRuta fManifestarPedidosEnRuta = null;
    FModificarManifiesto fModificarManifiesto= null;
    FManifestarPedidosPoblaciones fManifestarPedidosPoblaciones = null;
    FReportemovilizadoPorConductor fReportemovilizadoPorConductor = null;
    FGestionarIncidencias formSolucionarInciedncia = null;
    FManifestarFacturasEnPicking fFacturasenPicking = null;
    FRegistroDePedidosFinLineaDePicking fRegistroDePedidosFinLineaDePicking = null;
    FReporteManifiestosMovilizadosPorConductor fReporteManifiestosMovilizadosPorConductor = null;
    FManifestarPedidosHielera fManifestarPedidosHielera  = null;
    FDespachoHielera fDespachoHielera = null;
    FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador = null;
    FDespachoHieleraConIntegracion fDespachoHieleraConIntegracion = null;
    FControlarSalidaFacturasBarCode2 fControlarSalidaFacturasBarCode2 =  null;
    

   

    /**
     * Creates new fReportemovilizadoPorConductor FBuscar2
     */
    public FBuscarListadoDeEmpleados() {
        initComponents();
        txtApellidosPersona.requestFocus();
    }

    public FBuscarListadoDeEmpleados(Inicio ini) {
        initComponents();
        this.ini = ini;
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setMaximizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
        txtApellidosPersona.requestFocus();
    }

    public FBuscarListadoDeEmpleados(Inicio ini, FManifestarPedidosEnRuta ing1, int tipoDeRol) {
        initComponents();
        this.ini = ini;
        this.fManifestarPedidosEnRuta = ing1;
        this.tipoDeRol = tipoDeRol;

        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setMaximizable(false);
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((ini.getDimension().width - this.getSize().width) / 2, (ini.getDimension().height - this.getSize().height) / 2);
        txtApellidosPersona.requestFocus();

    }
    
    public FBuscarListadoDeEmpleados( FModificarManifiesto fModificarManifiesto, int tipoDeRol) {
        initComponents();
        this.ini=fModificarManifiesto.ini;
        this.fModificarManifiesto = fModificarManifiesto;
        this.tipoDeRol = tipoDeRol;
       
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setMaximizable(false);
       // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //this.setSize(screenSize);
        this.setLocation((ini.getDimension().width - this.getSize().width) / 2, (ini.getDimension().height - this.getSize().height) / 2);
      

    }
    
    public FBuscarListadoDeEmpleados(Inicio ini, FManifestarPedidosPoblaciones fManifestarPedidosPoblaciones, int tipoDeRol) {
        initComponents();
        this.ini = ini;
        this.fManifestarPedidosPoblaciones = fManifestarPedidosPoblaciones;
        this.tipoDeRol = tipoDeRol;

        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setMaximizable(false);
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((ini.getDimension().width - this.getSize().width) / 2, (ini.getDimension().height - this.getSize().height) / 2);
        txtApellidosPersona.requestFocus();

    }

    public FBuscarListadoDeEmpleados(Inicio ini, FReportemovilizadoPorConductor form) {
        initComponents();
        this.fReportemovilizadoPorConductor = form;
        this.ini = ini;

        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setMaximizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
        txtApellidosPersona.requestFocus();

    }

    public FBuscarListadoDeEmpleados(Inicio ini, FGestionarIncidencias form) {
        initComponents();
        this.formSolucionarInciedncia = form;
        this.ini = ini;

        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setMaximizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
        txtApellidosPersona.requestFocus();

    }
    
     public FBuscarListadoDeEmpleados(Inicio ini, FReporteManifiestosMovilizadosPorConductor form) {
        initComponents();
        this.fReporteManifiestosMovilizadosPorConductor = form;
        this.ini = ini;
        this.setMaximizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
        txtApellidosPersona.requestFocus();

    }

    public FBuscarListadoDeEmpleados(Inicio ini, int caso) {
        initComponents();
        this.ini = ini;
        this.tipoDeRol = caso;

        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        //residente = (FResidentes)this.getParent();
        this.setMaximizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
        //txtApellidosPersona.requestFocus();
        llenarTabla();
    }

    public FBuscarListadoDeEmpleados(Inicio ini, FManifestarFacturasEnPicking fFacturasenPicking) {
        initComponents();
        this.fFacturasenPicking = fFacturasenPicking;
        this.ini = ini;

        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setMaximizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
        txtApellidosPersona.requestFocus();

    }

    public FBuscarListadoDeEmpleados(Inicio ini, FRegistroDePedidosFinLineaDePicking form) {
        initComponents();
        this.fRegistroDePedidosFinLineaDePicking = form;
        this.ini = ini;
        txtApellidosPersona.requestFocus();

    }
    
     public FBuscarListadoDeEmpleados(FManifestarPedidosHielera fManifestarPedidosHielera, int rol) {
        initComponents();
        this.fManifestarPedidosHielera = fManifestarPedidosHielera;
        this.ini = fManifestarPedidosHielera.getIni();
        this.tipoDeRol=rol;
        txtApellidosPersona.requestFocus();

    }
     
     
     
      public FBuscarListadoDeEmpleados(FDespachoHielera guiDespachoHielera, int rol) {
        initComponents();
        this.fDespachoHielera = guiDespachoHielera;
        this.ini = guiDespachoHielera.getIni();
        this.tipoDeRol=rol;
        txtApellidosPersona.requestFocus();

    }
      
       public FBuscarListadoDeEmpleados(Inicio ini, FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador, int tipoDeRol) {
        initComponents();
        this.ini = ini;
        this.fManifestarPedidosEnRutaConIntegrador = fManifestarPedidosEnRutaConIntegrador;
        this.tipoDeRol = tipoDeRol;

        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setMaximizable(false);
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((ini.getDimension().width - this.getSize().width) / 2, (ini.getDimension().height - this.getSize().height) / 2);
        txtApellidosPersona.requestFocus();

    }
       
        public FBuscarListadoDeEmpleados(FDespachoHieleraConIntegracion fDespachoHieleraConIntegracion, int rol) {
        initComponents();
        this.fDespachoHieleraConIntegracion = fDespachoHieleraConIntegracion;
        this.ini = fDespachoHieleraConIntegracion.getIni();
        this.tipoDeRol=rol;
        txtApellidosPersona.requestFocus();

    }
        public FBuscarListadoDeEmpleados(FControlarSalidaFacturasBarCode2 fControlarSalidaFacturasBarCode2) {
        initComponents();
        this.fControlarSalidaFacturasBarCode2 = fControlarSalidaFacturasBarCode2;
        this.ini = fControlarSalidaFacturasBarCode2.getIni();
        txtApellidosPersona.requestFocus();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtApellidosPersona = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnAceptar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        jTextField1.setEditable(false);
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Para la consulta colocar el primer apellido y/o parte del segundo apellido");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Apelllidos");

        txtApellidosPersona.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtApellidosPersonaFocusGained(evt);
            }
        });
        txtApellidosPersona.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtApellidosPersonaKeyPressed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DOCUMENTO N°", "NOMBRES", "APELLIDOS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(200);
        }

        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/clean.png"))); // NOI18N
        btnAceptar.setText("Aceptar");
        btnAceptar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAceptar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/exit.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(6, 6, 6)
                                .addComponent(txtApellidosPersona, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnLimpiar)))))
                .addGap(5, 5, 5))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimpiar)
                    .addComponent(txtApellidosPersona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(6, 6, 6)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        int row = jTable1.getSelectedRow();
        this.cedula = (String.valueOf(jTable1.getValueAt(row, 0)));
        for (CEmpleados obj : ini.getListaDeEmpleados()) {
            if (obj.getCedula().equals(this.cedula)) {
                emp = obj;
                aceptar();
                return;

            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        txtApellidosPersona.setEnabled(true);
        modelo = (DefaultTableModel) jTable1.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        txtApellidosPersona.requestFocus();
       
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        aceptar();
    }//GEN-LAST:event_btnAceptarActionPerformed

    public void aceptar() {
        try {
            boolean encontrado = false;
            boolean ya = true;
            if (cedula != null) {

                if (fManifestarPedidosEnRuta != null) {
                    seleccionarEmpleadoDeRuta(encontrado);
                    this.dispose();
                    return;

                }
                if (fReportemovilizadoPorConductor != null) {
                    this.fReportemovilizadoPorConductor.txtNombreConductor.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fReportemovilizadoPorConductor.conductor = emp;
                    this.dispose();
                    return;
                }

                if (formSolucionarInciedncia != null) {
                    /* actualiza el responsable de la incidenciaSvc */
                    formSolucionarInciedncia.incidenciaSvc.setResponsable(emp.getCedula());
                    for(CEmpleados empleado : ini.getListaDeEmpleados() ){
                        if(empleado.getCedula().equals(empleado.getCedula())){
                           formSolucionarInciedncia.incidenciaSvc.setNombreResponsable(empleado.getNombres() + " " + empleado.getApellidos()); 
                        }
                    }
                    
                    this.formSolucionarInciedncia.txtNombreResponsable.setText(formSolucionarInciedncia.incidenciaSvc.getNombrResponsable());
                    this.formSolucionarInciedncia.incidenciaSvc.setResponsable(emp.getCedula());
                    //this.formSolucionarInciedncia.incidenciaSvc.setNombresResponsable(emp.getNombres() + " " + emp.getApellidos());
                    this.formSolucionarInciedncia.btnCambioResponsable.setEnabled(true);
                    this.dispose();
                    return;
                }
                
                
                    if (fReporteManifiestosMovilizadosPorConductor != null) {
                    /* actualiza el responsable de la incidenciaSvc */
                    this.fReporteManifiestosMovilizadosPorConductor.conductor = emp;
                    this.fReporteManifiestosMovilizadosPorConductor.txtNombreConductor.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fReporteManifiestosMovilizadosPorConductor.btnAceptar.setEnabled(true);
                    this.dispose();
                    return;
                }

                if (fFacturasenPicking != null) {
                    this.fFacturasenPicking.txtNombreDeReceptor.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fFacturasenPicking.setReceptor(emp);
                    this.dispose();
                    return;

                }

                if (fRegistroDePedidosFinLineaDePicking != null) {
                    this.fRegistroDePedidosFinLineaDePicking.txtNombreDeOperario.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fRegistroDePedidosFinLineaDePicking.setOperario(emp);
                    this.fRegistroDePedidosFinLineaDePicking.jTabbedPane1.setEnabled(true);
                    this.fRegistroDePedidosFinLineaDePicking.txtNumeroDeFactura.setEnabled(true);
                    this.fRegistroDePedidosFinLineaDePicking.txtNumeroDeFactura.setEditable(true);
                    this.dispose();
                    return;

                }
                
                 if (fManifestarPedidosPoblaciones != null) {
                    seleccionarEmpleadoEnPoblaciones(encontrado);
                    this.dispose();
                    return;

                }

                if (fModificarManifiesto != null) {
                    seleccionarEmpleadoEnModificarManifiesto(encontrado);
                    this.dispose();
                    return;

                }
                
                 if (fManifestarPedidosHielera != null) {
                    seleccionarEmpleadoEnLaHielera(encontrado);
                    this.dispose();
                    return;

                }
                  if (fDespachoHielera != null) {
                    seleccionarEmpleadoEnDespahoLaHieleraConIntegracion(encontrado);
                    this.dispose();
                    return;

                }
                  
                  
                if (fManifestarPedidosEnRutaConIntegrador != null) {
                    seleccionarEmpleadoConIntegrador(encontrado);
                    this.dispose();
                    return;

                }
                 
                 if (fDespachoHieleraConIntegracion != null) {
                    seleccionarEmpleadoEnDespahoLaHieleraConIntegracion(encontrado);
                    this.dispose();
                    return;

                }
               
                 if (fControlarSalidaFacturasBarCode2 != null) {
                    seleccionarEmpleadoEnControlarSalidaBarcode2(encontrado);
                    this.dispose();
                    return;

                }

            } else {
                JOptionPane.showInternalMessageDialog(this, "No ha elegido un empleado de la lista", "Error ", 0);
            }

        } catch (Exception ex) {
            Logger.getLogger(FBuscarListadoDeEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtApellidosPersonaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidosPersonaKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
            llenarTabla(txtApellidosPersona.getText());

            //new Thread(new HiloListadoDeEmpleados(this.ini, this, txtApellidosPersona.getText())).start();

        }
    }

    public void llenarTabla() {
        //  ArrayList<CEmpleados.IdEmpleados> list = new ArrayList();

        try {

            txtApellidosPersona.setEnabled(false);
            //CEmpleados empl = new CEmpleados(ini);
            // list=empl.listadoDeTodosLosEmpleados(1);

            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (obj.getEmpleadoActivo() != 0) {
                    //CEmpleados operario = new CEmpleados(ini, obj.toString());
                    fila = new Object[4];
                    fila[0] = obj.getCedula();
                    fila[1] = obj.getNombres();
                    fila[2] = obj.getApellidos();
                    //   fila[3] = obj.getAgencia().getNombreAgencia();
                    modelo = (DefaultTableModel) jTable1.getModel();
                    modelo.addRow(fila);
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(FBuscarListadoDeEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtApellidosPersonaKeyPressed

       public void llenarTabla(String apellidos) {
        //  ArrayList<CEmpleados.IdEmpleados> list = new ArrayList();

        try {

           

            txtApellidosPersona.setEnabled(false);
            //CEmpleados empl = new CEmpleados(ini);
            // list=empl.listadoDeTodosLosEmpleados(1);

            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (obj.getEmpleadoActivo() != 0 &&( obj.getApellidos().contains(apellidos) || obj.getNombres().contains(apellidos))) {
                    //CEmpleados operario = new CEmpleados(ini, obj.toString());
                    fila = new Object[4];
                    fila[0] = obj.getCedula();
                    fila[1] = obj.getNombres();
                    fila[2] = obj.getApellidos();
                    //   fila[3] = obj.getAgencia().getNombreAgencia();
                    modelo = (DefaultTableModel) jTable1.getModel();
                    modelo.addRow(fila);
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(FBuscarListadoDeEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                              
    
    private void txtApellidosPersonaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApellidosPersonaFocusGained
        txtApellidosPersona.setSelectionStart(0);
        txtApellidosPersona.setSelectionEnd(txtApellidosPersona.getText().length());
    }//GEN-LAST:event_txtApellidosPersonaFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    public javax.swing.JTextField txtApellidosPersona;
    // End of variables declaration//GEN-END:variables

    private void seleccionarEmpleadoDeRuta(boolean encontrado) {
        boolean ya;
        switch (tipoDeRol) {
            case 1: // operario

                this.fManifestarPedidosEnRuta.txtnombreDeConductor.setText(emp.getNombres() + " " + emp.getApellidos());
                this.fManifestarPedidosEnRuta.conductor = emp;
                this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setEnabled(true);
                this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setEnabled(true);
                this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.requestFocus();
                ya = false;
                //this.dispose();
                break;
            case 2: // auxiliar 1
                /*se valida que el auxiliar no sea el mismo cconductor
                o que el auxiliar ya haya sido asignado en la misma ruta*/
                if (fManifestarPedidosEnRuta.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosEnRuta.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }


                /*Si no ha sido asignado en la ruta  se procede a almacenarlo en la lista de los auxiliares*/
                if (!encontrado) {
                    this.fManifestarPedidosEnRuta.auxiliar1 = emp;
                    this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar1.setText(emp.getNombres() + " " + emp.getApellidos());
                    /* Se anexa el auxiliar a la lista */
                    this.fManifestarPedidosEnRuta.listaDeAuxiliares.add(0, emp);
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }

                break;

            case 3: // auxiliar 2

                if (fManifestarPedidosEnRuta.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosEnRuta.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosEnRuta.auxiliar2 = emp;
                    this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar2.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosEnRuta.listaDeAuxiliares.add(1, emp);
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado  ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

            case 4: // auxiliar 3
                if (fManifestarPedidosEnRuta.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosEnRuta.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosEnRuta.auxiliar3 = emp;
                    this.fManifestarPedidosEnRuta.txtNombreDeAuxiliar3.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosEnRuta.listaDeAuxiliares.add(2, emp);
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosEnRuta.cbxCanales.setEnabled(true);
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;
            case 5: // despachador

                if (fManifestarPedidosEnRuta.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosEnRuta.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosEnRuta.despachador = emp;
                    this.fManifestarPedidosEnRuta.txtNombreDedespachador.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosEnRuta.manifiestoActual.setDespachador(emp.getCedula());
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosEnRuta.cbxCanales.setEnabled(true);
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

        }

        //this.dispose();
    }

    private void seleccionarEmpleadoEnPoblaciones(boolean encontrado) {
        boolean ya;
        switch (tipoDeRol) {
            case 1: // operario

                this.fManifestarPedidosPoblaciones.txtnombreDeConductor.setText(emp.getNombres() + " " + emp.getApellidos());
                this.fManifestarPedidosPoblaciones.conductor = emp;
                this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar1.setEnabled(true);
                this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar1.setEnabled(true);
                this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar1.requestFocus();
                ya = false;
                //this.dispose();
                break;
            case 2: // auxiliar 1
                /*se valida que el auxiliar no sea el mismo cconductor
                o que el auxiliar ya haya sido asignado en la misma ruta*/
                if (fManifestarPedidosPoblaciones.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosPoblaciones.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }


                /*Si no ha sido asignado en la ruta  se procede a almacenarlo en la lista de los auxiliares*/
                if (!encontrado) {
                    this.fManifestarPedidosPoblaciones.auxiliar1 = emp;
                    this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar1.setText(emp.getNombres() + " " + emp.getApellidos());
                    /* Se anexa el auxiliar a la lista */
                    this.fManifestarPedidosPoblaciones.listaDeAuxiliares.add(0, emp);
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar2.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }

                break;

            case 3: // auxiliar 2

                if (fManifestarPedidosPoblaciones.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosPoblaciones.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosPoblaciones.auxiliar2 = emp;
                    this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar2.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosPoblaciones.listaDeAuxiliares.add(1, emp);
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar3.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado  ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

            case 4: // auxiliar 3
                if (fManifestarPedidosPoblaciones.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosPoblaciones.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosPoblaciones.auxiliar3 = emp;
                    this.fManifestarPedidosPoblaciones.txtNombreDeAuxiliar3.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosPoblaciones.listaDeAuxiliares.add(2, emp);
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosPoblaciones.cbxCanales.setEnabled(true);
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;
            case 5: // despachador

                if (fManifestarPedidosPoblaciones.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosPoblaciones.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosPoblaciones.despachador = emp;
                    this.fManifestarPedidosPoblaciones.txtNombreDedespachador.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosPoblaciones.manifiestoActual.setDespachador(emp.getCedula());
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosPoblaciones.cbxCanales.setEnabled(true);
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

        }

        //this.dispose();
    }

    private void seleccionarEmpleadoEnModificarManifiesto(boolean encontrado) {
        boolean ya;
        switch (tipoDeRol) {
            case 1: // conductor

                this.fModificarManifiesto.txtnombreDeConductor.setText(emp.getNombres() + " " + emp.getApellidos());
                this.fModificarManifiesto.conductor = emp;

                this.fModificarManifiesto.manifiestoActual.setConductor(emp.getCedula());

                this.fModificarManifiesto.txtNombreDeAuxiliar1.setEnabled(true);
                this.fModificarManifiesto.txtNombreDeAuxiliar1.setEnabled(true);
                this.fModificarManifiesto.txtNombreDeAuxiliar1.requestFocus();
                ya = false;
                //this.dispose();
                break;
            case 2: // auxiliar 1
                /*se valida que el auxiliar no sea el mismo cconductor
                o que el auxiliar ya haya sido asignado en la misma ruta*/
                if (fModificarManifiesto.manifiestoActual.getConductor().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fModificarManifiesto.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }


                /*Si no ha sido asignado en la ruta  se procede a almacenarlo en la lista de los auxiliares*/
                if (!encontrado) {
                    this.fModificarManifiesto.auxiliar1 = emp;
                    this.fModificarManifiesto.txtNombreDeAuxiliar1.setText(emp.getNombres() + " " + emp.getApellidos());
                    /* Se anexa el auxiliar a la lista */
                    this.fModificarManifiesto.listaDeAuxiliares.add(0, emp);
                    ya = false;
                    this.dispose();
                    this.fModificarManifiesto.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fModificarManifiesto.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fModificarManifiesto.txtNombreDeAuxiliar2.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }

                break;

            case 3: // auxiliar 2

                if (fModificarManifiesto.manifiestoActual.getConductor().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosPoblaciones.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fModificarManifiesto.auxiliar2 = emp;
                    this.fModificarManifiesto.txtNombreDeAuxiliar2.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fModificarManifiesto.listaDeAuxiliares.add(1, emp);
                    ya = false;
                    this.dispose();
                    // this.fModificarManifiesto.txtNombreDeAuxiliar3.setEnabled(true);
                    //this.fModificarManifiesto.txtNombreDeAuxiliar3.setEnabled(true);
                    //this.fModificarManifiesto.txtNombreDeAuxiliar3.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado  ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

            case 4: // auxiliar 3
                if (fModificarManifiesto.manifiestoActual.getConductor().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fModificarManifiesto.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fModificarManifiesto.auxiliar3 = emp;
                    // this.fModificarManifiesto.txtNombreDeAuxiliar3.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fModificarManifiesto.listaDeAuxiliares.add(2, emp);
                    ya = false;
                    this.dispose();
                    this.fModificarManifiesto.cbxCanales.setEnabled(true);
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;
            case 5: // despachador

                if (fModificarManifiesto.manifiestoActual.getConductor().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fModificarManifiesto.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fModificarManifiesto.despachador = emp;
                    this.fModificarManifiesto.txtNombreDedespachador.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fModificarManifiesto.manifiestoActual.setDespachador(emp.getCedula());
                    ya = false;
                    this.dispose();
                    this.fModificarManifiesto.cbxCanales.setEnabled(true);
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

        }

        //this.dispose();
    }

    private void seleccionarEmpleadoEnLaHielera(boolean encontrado) {
        boolean ya;
        switch (tipoDeRol) {
            case 1: // conductor

                this.fManifestarPedidosHielera.txtnombreDeConductor.setText(emp.getNombres() + " " + emp.getApellidos());
                this.fManifestarPedidosHielera.conductor = emp;
                this.fManifestarPedidosHielera.manifiestoActual.setConductor(emp.getCedula());

                this.fManifestarPedidosHielera.txtPlaca.setEnabled(true);
                this.fManifestarPedidosHielera.txtPlaca.setEditable(true);
                this.fManifestarPedidosHielera.txtPlaca.requestFocus();
                ya = false;
                //this.dispose();
                break;
            case 2: // auxiliar 1
                /*se valida que el auxiliar no sea el mismo cconductor
                o que el auxiliar ya haya sido asignado en la misma ruta*/
                if (fManifestarPedidosHielera.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosHielera.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }


                /*Si no ha sido asignado en la ruta  se procede a almacenarlo en la lista de los auxiliares*/
                if (!encontrado) {
                    this.fManifestarPedidosHielera.auxiliar1 = emp;
                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar1.setText(emp.getNombres() + " " + emp.getApellidos());
                    /* Se anexa el auxiliar a la lista */
                    this.fManifestarPedidosHielera.listaDeAuxiliares.add(0, emp);
                    ya = false;
                    this.dispose();

                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar1.setEnabled(false);
                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar1.setEditable(false);

                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar2.setEditable(true);
                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar2.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }

                break;

            case 3: // auxiliar 2

                if (fManifestarPedidosHielera.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosHielera.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosHielera.auxiliar2 = emp;
                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar2.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosHielera.listaDeAuxiliares.add(1, emp);
                    ya = false;
                    this.dispose();

                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar2.setEnabled(false);
                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar2.setEditable(false);

                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar3.requestFocus();

                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado  ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

            case 4: // auxiliar 3
                if (fManifestarPedidosHielera.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosHielera.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosHielera.auxiliar3 = emp;
                    // this.fModificarManifiesto.txtNombreDeAuxiliar3.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosHielera.listaDeAuxiliares.add(2, emp);
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar3.setEnabled(false);
                    this.fManifestarPedidosHielera.txtNombreDeAuxiliar3.setEditable(false);

                    this.fManifestarPedidosHielera.txtNombreDedespachador.setEnabled(true);
                    this.fManifestarPedidosHielera.txtNombreDedespachador.setEnabled(true);
                    this.fManifestarPedidosHielera.txtNombreDedespachador.requestFocus();

                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;
            case 5: // despachador

                if (fManifestarPedidosHielera.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosHielera.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosHielera.despachador = emp;
                    this.fManifestarPedidosHielera.txtNombreDedespachador.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosHielera.manifiestoActual.setDespachador(emp.getCedula());
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosHielera.txtNombreDedespachador.setEnabled(false);
                    this.fManifestarPedidosHielera.txtNombreDedespachador.setEditable(false);

                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

        }

        //this.dispose();
    }

    private void seleccionarEmpleadoConIntegrador(boolean encontrado) {
        boolean ya;
        switch (tipoDeRol) {
            case 1: // operario

                this.fManifestarPedidosEnRutaConIntegrador.txtnombreDeConductor.setText(emp.getNombres() + " " + emp.getApellidos());
                this.fManifestarPedidosEnRutaConIntegrador.conductor = emp;
                this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar1.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar1.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar1.requestFocus();
                ya = false;
                //this.dispose();
                break;
            case 2: // auxiliar 1
                /*se valida que el auxiliar no sea el mismo cconductor
                o que el auxiliar ya haya sido asignado en la misma ruta*/
                if (fManifestarPedidosEnRutaConIntegrador.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosEnRutaConIntegrador.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }


                /*Si no ha sido asignado en la ruta  se procede a almacenarlo en la lista de los auxiliares*/
                if (!encontrado) {
                    this.fManifestarPedidosEnRutaConIntegrador.auxiliar1 = emp;
                    this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar1.setText(emp.getNombres() + " " + emp.getApellidos());
                    /* Se anexa el auxiliar a la lista */
                    this.fManifestarPedidosEnRutaConIntegrador.listaDeAuxiliares.add(0, emp);
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar2.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }

                break;

            case 3: // auxiliar 2

                if (fManifestarPedidosEnRutaConIntegrador.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosEnRutaConIntegrador.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosEnRutaConIntegrador.auxiliar2 = emp;
                    this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar2.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosEnRutaConIntegrador.listaDeAuxiliares.add(1, emp);
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar3.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado  ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

            case 4: // auxiliar 3
                if (fManifestarPedidosEnRutaConIntegrador.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosEnRutaConIntegrador.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosEnRutaConIntegrador.auxiliar3 = emp;
                    this.fManifestarPedidosEnRutaConIntegrador.txtNombreDeAuxiliar3.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosEnRutaConIntegrador.listaDeAuxiliares.add(2, emp);
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosEnRutaConIntegrador.cbxCanales.setEnabled(true);
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;
            case 5: // despachador

                if (fManifestarPedidosEnRutaConIntegrador.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fManifestarPedidosEnRutaConIntegrador.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fManifestarPedidosEnRutaConIntegrador.despachador = emp;
                    this.fManifestarPedidosEnRutaConIntegrador.txtNombreDedespachador.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setDespachador(emp.getCedula());
                    ya = false;
                    this.dispose();
                    this.fManifestarPedidosEnRutaConIntegrador.cbxCanales.setEnabled(true);
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

        }

        //this.dispose();
    }

    private void seleccionarEmpleadoEnDespahoLaHielera(boolean encontrado) {
        boolean ya;
        switch (tipoDeRol) {
            case 1: // conductor

                this.fDespachoHielera.txtnombreDeConductor.setText(emp.getNombres() + " " + emp.getApellidos());
                this.fDespachoHielera.conductor = emp;
                this.fDespachoHielera.manifiestoActual.setConductor(emp.getCedula());

                this.fDespachoHielera.txtPlaca.setEnabled(true);
                this.fDespachoHielera.txtPlaca.setEditable(true);
                this.fDespachoHielera.txtPlaca.requestFocus();
                ya = false;
                //this.dispose();
                break;
            case 2: // auxiliar 1
                /*se valida que el auxiliar no sea el mismo cconductor
                o que el auxiliar ya haya sido asignado en la misma ruta*/
                if (fDespachoHielera.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fDespachoHielera.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }


                /*Si no ha sido asignado en la ruta  se procede a almacenarlo en la lista de los auxiliares*/
                if (!encontrado) {
                    this.fDespachoHielera.auxiliar1 = emp;
                    this.fDespachoHielera.txtNombreDeAuxiliar1.setText(emp.getNombres() + " " + emp.getApellidos());
                    /* Se anexa el auxiliar a la lista */
                    this.fDespachoHielera.listaDeAuxiliares.add(0, emp);
                    ya = false;
                    this.dispose();

                    this.fDespachoHielera.txtNombreDeAuxiliar1.setEnabled(false);
                    this.fDespachoHielera.txtNombreDeAuxiliar1.setEditable(false);

                    this.fDespachoHielera.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fDespachoHielera.txtNombreDeAuxiliar2.setEditable(true);
                    this.fDespachoHielera.txtNombreDeAuxiliar2.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }

                break;

            case 3: // auxiliar 2

                if (fDespachoHielera.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fDespachoHielera.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fDespachoHielera.auxiliar2 = emp;
                    this.fDespachoHielera.txtNombreDeAuxiliar2.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fDespachoHielera.listaDeAuxiliares.add(1, emp);
                    ya = false;
                    this.dispose();

                    this.fDespachoHielera.txtNombreDeAuxiliar2.setEnabled(false);
                    this.fDespachoHielera.txtNombreDeAuxiliar2.setEditable(false);

                    this.fDespachoHielera.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fDespachoHielera.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fDespachoHielera.txtNombreDeAuxiliar3.requestFocus();

                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado  ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

            case 4: // auxiliar 3
                if (fDespachoHielera.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fDespachoHielera.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fDespachoHielera.auxiliar3 = emp;
                    this.fDespachoHielera.txtNombreDeAuxiliar3.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fDespachoHielera.listaDeAuxiliares.add(2, emp);
                    ya = false;
                    this.dispose();
                    this.fDespachoHielera.txtNombreDeAuxiliar3.setEnabled(false);
                    this.fDespachoHielera.txtNombreDeAuxiliar3.setEditable(false);

                    this.fDespachoHielera.txtNombreDedespachador.setEnabled(true);
                    this.fDespachoHielera.txtNombreDedespachador.setEnabled(true);
                    this.fDespachoHielera.txtNombreDedespachador.requestFocus();

                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;
            case 5: // despachador

                if (fDespachoHielera.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fDespachoHielera.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fDespachoHielera.despachador = emp;
                    this.fDespachoHielera.txtNombreDedespachador.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fDespachoHielera.manifiestoActual.setDespachador(emp.getCedula());
                    ya = false;
                    this.dispose();
                    this.fDespachoHielera.txtNombreDedespachador.setEnabled(false);
                    this.fDespachoHielera.txtNombreDedespachador.setEditable(false);

                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

        }

        //this.dispose();
    }
    
    private void seleccionarEmpleadoEnDespahoLaHieleraConIntegracion(boolean encontrado) {
        boolean ya;
        switch (tipoDeRol) {
            case 1: // conductor

                this.fDespachoHieleraConIntegracion.txtnombreDeConductor.setText(emp.getNombres() + " " + emp.getApellidos());
                this.fDespachoHieleraConIntegracion.conductor = emp;
                this.fDespachoHieleraConIntegracion.manifiestoActual.setConductor(emp.getCedula());

                this.fDespachoHieleraConIntegracion.txtPlaca.setEnabled(true);
                this.fDespachoHieleraConIntegracion.txtPlaca.setEditable(true);
                this.fDespachoHieleraConIntegracion.txtPlaca.requestFocus();
                ya = false;
                //this.dispose();
                break;
            case 2: // auxiliar 1
                /*se valida que el auxiliar no sea el mismo cconductor
                o que el auxiliar ya haya sido asignado en la misma ruta*/
                if (fDespachoHieleraConIntegracion.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fDespachoHieleraConIntegracion.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }


                /*Si no ha sido asignado en la ruta  se procede a almacenarlo en la lista de los auxiliares*/
                if (!encontrado) {
                    this.fDespachoHieleraConIntegracion.auxiliar1 = emp;
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setText(emp.getNombres() + " " + emp.getApellidos());
                    /* Se anexa el auxiliar a la lista */
                    this.fDespachoHieleraConIntegracion.listaDeAuxiliares.add(0, emp);
                    ya = false;
                    this.dispose();

                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setEnabled(false);
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setEditable(false);

                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setEnabled(true);
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setEditable(true);
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.requestFocus();
                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }

                break;

            case 3: // auxiliar 2

                if (fDespachoHieleraConIntegracion.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fDespachoHieleraConIntegracion.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fDespachoHieleraConIntegracion.auxiliar2 = emp;
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fDespachoHieleraConIntegracion.listaDeAuxiliares.add(1, emp);
                    ya = false;
                    this.dispose();

                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setEnabled(false);
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setEditable(false);

                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setEnabled(true);
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.requestFocus();

                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado  ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

            case 4: // auxiliar 3
                if (fDespachoHieleraConIntegracion.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    for (CEmpleados aux : this.fDespachoHieleraConIntegracion.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                if (!encontrado) {
                    this.fDespachoHieleraConIntegracion.auxiliar3 = emp;
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fDespachoHieleraConIntegracion.listaDeAuxiliares.add(2, emp);
                    ya = false;
                    this.dispose();
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setEnabled(false);
                    this.fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setEditable(false);

                    this.fDespachoHieleraConIntegracion.txtNombreDedespachador.setEnabled(true);
                    this.fDespachoHieleraConIntegracion.txtNombreDedespachador.setEnabled(true);
                    this.fDespachoHieleraConIntegracion.txtNombreDedespachador.requestFocus();

                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;
            case 5: // despachador

                if (fDespachoHieleraConIntegracion.conductor.getCedula().equals(cedula)) {
                    encontrado = true;
                } else {
                    if(this.fDespachoHieleraConIntegracion.listaDeAuxiliares != null){
                        
                    
                    for (CEmpleados aux : this.fDespachoHieleraConIntegracion.listaDeAuxiliares) {
                        if (aux.getCedula().equals(cedula)) {
                            encontrado = true;
                        }
                    }
                }
                }
                if (!encontrado) {
                    this.fDespachoHieleraConIntegracion.despachador = emp;
                    this.fDespachoHieleraConIntegracion.txtNombreDedespachador.setText(emp.getNombres() + " " + emp.getApellidos());
                    this.fDespachoHieleraConIntegracion.manifiestoActual.setDespachador(emp.getCedula());
                    ya = false;
                    this.dispose();
                    this.fDespachoHieleraConIntegracion.txtNombreDedespachador.setEnabled(false);
                    this.fDespachoHieleraConIntegracion.txtNombreDedespachador.setEditable(false);

                } else {
                    ya = true;
                    JOptionPane.showInternalMessageDialog(this, "Empleado ya fue asignado ", "Error de Empleado", JOptionPane.WARNING_MESSAGE);

                }
                break;

        }

        //this.dispose();
    }
    
    private void seleccionarEmpleadoEnControlarSalidaBarcode2(boolean encontrado) {
        boolean ya;
        

                this.fControlarSalidaFacturasBarCode2.txtnombreConductor.setText(emp.getNombres() + " " + emp.getApellidos());
                this.fControlarSalidaFacturasBarCode2.conductor = emp;
               // this.fControlarSalidaFacturasBarCode2.manifiestoActual.setConductor(emp.getCedula());

                
               

          
        //this.dispose();
    }
}
