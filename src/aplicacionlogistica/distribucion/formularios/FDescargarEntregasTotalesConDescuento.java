/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.FliquidarManifiestos;
import aplicacionlogistica.distribucion.formularios.Hielera.Threads.HiloFLiquidarManifiestos;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class FDescargarEntregasTotalesConDescuento extends javax.swing.JInternalFrame {

    Object[] fila = new Object[3];

    public FliquidarManifiestos fliquidarManifiestos = null;
    public DescargarFacturas fDescargarFacturas = null;
    

    public DefaultTableModel modelo, modelo2;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    NumberFormat percentageFormat = NumberFormat.getPercentInstance(Locale.getDefault());
    //CFacturasPorManifiesto facturaActual;

    int filaSelleccionada, columnaselecionada;
    //int valorBandera;

    //ArrayList<CFacturasPorManifiesto> arrFactPorMfto;
    // ArrayList<CFacturasDescargadas> arrFacturasDescargadas;
    Inicio ini;
    boolean nuevo = false;
    double valorFactura = 0.0;
    double nuevoValorFactura;
    double valorDescuentoFactura;
    double valorrechazoFactura;
    double valorAbono = 0.0;

    double valorDescuento = 0.0;
    double valorRecaudado = 0.0;

    int causalRechazoFactura = 1;
    boolean validadorDeRechazo = false;
    public boolean conDescuento = false;
    int totalRechazos = 0;
    String nombreCliente;

    //CProductosPorFacturaDescargados pxfd;
    int numeroDeManifiesto;
    int cosecutivoFactura;

    int idProducto;
    //public CFacturasPorManifiesto facturaTemporal;
    double porcentajeDescuento = 0.0;

    public FDescargarEntregasTotalesConDescuento() {
        initComponents();

    }
    
    /**
     * Método constructor cuando se está descargando las facturas del
     * manifiesto, en el cualse va a descargar una factura con entregas con
     * rechazos parciales ó descuento en algunos ó todos los items
     *
     *
     * @param ini objeto de configurción del sistema
     * @param fDescargarFacturas es el fBuscarManifiestos que invoca a éste fBuscarManifiestos para descargar
 loa rechazos parciales del sistema.
     * @throws java.lang.Exception
     */
    public FDescargarEntregasTotalesConDescuento(Inicio ini, DescargarFacturas fDescargarFacturas) throws Exception {
        initComponents();

        this.ini = ini;
        this.fDescargarFacturas = fDescargarFacturas;
        nuevo = true;
        if (this.fDescargarFacturas.chkDescuento.isSelected()) {
            txtPorcentajeDescuento.setEditable(true);
            txtPorcentajeDescuento.setEnabled(true);
            txtPorcentajeDescuento.requestFocus();
        }
        llenarFormulario();

    }
   

    /**
     * Método constructor cuando se está descargando las facturas del
     * manifiesto, en el cualse va a descargar una factura con entregas con
     * rechazos parciales ó descuento en algunos ó todos los items
     *
     *
     * @param ini objeto de configurción del sistema
     * @param form es el fBuscarManifiestos que invoca a éste fBuscarManifiestos para descargar
 loa rechazos parciales del sistema.
     * @throws java.lang.Exception
     */
    public FDescargarEntregasTotalesConDescuento(Inicio ini, FliquidarManifiestos form) throws Exception {
        initComponents();

        this.ini = ini;
        this.fliquidarManifiestos = form;
        nuevo = true;
        if (this.fliquidarManifiestos.chkConDescuento.isSelected()) {
            txtPorcentajeDescuento.setEditable(true);
            txtPorcentajeDescuento.setEnabled(true);
            txtPorcentajeDescuento.requestFocus();
        }
        llenarFormulario2();

    }
    
    public void llenarFormulario() throws Exception {

        this.ini = fDescargarFacturas.ini;
        valorFactura = 0;

        txtPorcentajeDescuento.setEnabled(true);
        txtPorcentajeDescuento.setText("0.0");
        pnlFactura.setToolTipText("Factura # " + fDescargarFacturas.facturaActual.getNumeroFactura());
        lblNumeroFactura.setText("Factura # " + fDescargarFacturas.facturaActual.getNumeroFactura());

        txtPorcentajeDescuento.setText("0.0");
        txt_ValorFacSinIva.setText("0.0");
        txt_ValorDescuento.setText("0.0");
        txt_valorRecaudado.setText("0.0");

        lblValorFactura.setText("Total Factura : " + nf.format(0.0));

        /* Se asignan los productos de la factura que estan en el servidor*/
        try {

            txtPorcentajeDescuento.setEditable(true);
            txtPorcentajeDescuento.setEnabled(true);
            txtPorcentajeDescuento.requestFocus();

            try {

                llenarTablaProductodPorFactura();
                ocultarColumnaCero();

                // new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this.descargarFacturasHielera, 2)).start();//-->Error v
                txt_ValorFacSinIva.setText(nf.format(fDescargarFacturas.facturaActual.getValorFacturaSinIva()));
                txt_ValorDescuento.setText("0.0");
                txt_valorRecaudado.setText(nf.format(fDescargarFacturas.facturaActual.getValorTotalFactura()));
                lblValorFactura.setText("Total Factura : " + nf.format(fDescargarFacturas.facturaActual.getValorARecaudarFactura()));

                btnDescargar.setEnabled(true);

                this.requestFocus();
                txtPorcentajeDescuento.requestFocus();
                txtPorcentajeDescuento.requestFocus();

            } catch (Exception ex) {
                System.out.println("Error en txtNumeroDeFacturaKeyPressed ");
                Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(FDescargarEntregasTotalesConDescuento.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void llenarFormulario2() throws Exception {

        this.ini = fliquidarManifiestos.ini;
        valorFactura = 0;

        txtPorcentajeDescuento.setEnabled(true);
        txtPorcentajeDescuento.setText("0.0");
        pnlFactura.setToolTipText("Factura # " + fliquidarManifiestos.facturaActual.getNumeroFactura());
        lblNumeroFactura.setText("Factura # " + fliquidarManifiestos.facturaActual.getNumeroFactura());

        txtPorcentajeDescuento.setText("0.0");
        txt_ValorFacSinIva.setText("0.0");
        txt_ValorDescuento.setText("0.0");
        txt_valorRecaudado.setText("0.0");

        lblValorFactura.setText("Total Factura : " + nf.format(0.0));

        /* Se asignan los productos de la factura que estan en el servidor*/
        try {

            txtPorcentajeDescuento.setEditable(true);
            txtPorcentajeDescuento.setEnabled(true);
            txtPorcentajeDescuento.requestFocus();

            try {

                llenarTablaProductodPorFactura2();
                ocultarColumnaCero();

                // new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this.descargarFacturasHielera, 2)).start();//-->Error v
                txt_ValorFacSinIva.setText(nf.format(fliquidarManifiestos.facturaActual.getValorFacturaSinIva()));
                txt_ValorDescuento.setText("0.0");
                txt_valorRecaudado.setText(nf.format(fliquidarManifiestos.facturaActual.getValorTotalFactura()));
                lblValorFactura.setText("Total Factura : " + nf.format(fliquidarManifiestos.facturaActual.getValorARecaudarFactura()));

                btnDescargar.setEnabled(true);

                this.requestFocus();
                txtPorcentajeDescuento.requestFocus();
                txtPorcentajeDescuento.requestFocus();

            } catch (Exception ex) {
                System.out.println("Error en txtNumeroDeFacturaKeyPressed ");
                Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(FDescargarEntregasTotalesConDescuento.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void llenarTablaProductodPorFactura() {
        modelo = (DefaultTableModel) tblProductosPorFactura.getModel();

        /*Limpia la tabla de los datos*/
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        // se nexan los productos a la tabla de productos por factura
        for (CProductosPorFactura obj : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {

            int fil = tblProductosPorFactura.getRowCount();

            modelo.addRow(new Object[fil]);

            tblProductosPorFactura.setValueAt(obj.getConsecutivoProductoPorFactura(), fil, 0);
            tblProductosPorFactura.setValueAt("" + (fil + 1), fil, 1);
            tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), fil, 2);
            tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fil, 3);
            tblProductosPorFactura.setValueAt(obj.getCantidad(), fil, 4);
            tblProductosPorFactura.setValueAt("" + nf.format(obj.getValorUnitarioSinIva()), fil, 5);
            tblProductosPorFactura.setValueAt(0.0 + " %", fil, 6);
            tblProductosPorFactura.setValueAt("" + nf.format(obj.getValorUnitarioConIva()), fil, 7);
            tblProductosPorFactura.setValueAt(("" + nf.format(obj.getValorProductoXCantidad())), fil, 8);

        }
        txtPorcentajeDescuento.requestFocus();
    }
    
    private void llenarTablaProductodPorFactura2() {
        modelo = (DefaultTableModel) tblProductosPorFactura.getModel();

        /*Limpia la tabla de los datos*/
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        // se nexan los productos a la tabla de productos por factura
        for (CProductosPorFactura obj : fliquidarManifiestos.facturaActual.getListaProductosPorFactura()) {

            int fil = tblProductosPorFactura.getRowCount();

            modelo.addRow(new Object[fil]);

            tblProductosPorFactura.setValueAt(obj.getConsecutivoProductoPorFactura(), fil, 0);
            tblProductosPorFactura.setValueAt("" + (fil + 1), fil, 1);
            tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), fil, 2);
            tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fil, 3);
            tblProductosPorFactura.setValueAt(obj.getCantidad(), fil, 4);
            tblProductosPorFactura.setValueAt("" + nf.format(obj.getValorUnitarioSinIva()), fil, 5);
            tblProductosPorFactura.setValueAt(0.0 + " %", fil, 6);
            tblProductosPorFactura.setValueAt("" + nf.format(obj.getValorUnitarioConIva()), fil, 7);
            tblProductosPorFactura.setValueAt(("" + nf.format(obj.getValorProductoXCantidad())), fil, 8);

        }
        txtPorcentajeDescuento.requestFocus();
    }

    private void ocultarColumnaCero() {

        tblProductosPorFactura.getColumn(tblProductosPorFactura.getColumnName(0)).setWidth(0);
        tblProductosPorFactura.getColumn(tblProductosPorFactura.getColumnName(0)).setMinWidth(0);
        tblProductosPorFactura.getColumn(tblProductosPorFactura.getColumnName(0)).setMaxWidth(0);
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFactura = new javax.swing.JPanel();
        btnDescargar = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProductosPorFactura = new javax.swing.JTable();
        txt_ValorFacSinIva = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtPorcentajeDescuento = new javax.swing.JTextField();
        lblNumeroFactura = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_valorRecaudado = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_ValorDescuento = new javax.swing.JTextField();
        lblValorFactura = new javax.swing.JLabel();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
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
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        pnlFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlFacturaMouseClicked(evt);
            }
        });

        btnDescargar.setText("Descargar");
        btnDescargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarActionPerformed(evt);
            }
        });
        btnDescargar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDescargarKeyPressed(evt);
            }
        });

        tblProductosPorFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "#", "Ref", "Producto", "cantidad", "V. U. Sin Iva", " % Dto", "V.U.Con Iva", "Valor Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductosPorFactura.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProductosPorFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblProductosPorFacturaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblProductosPorFacturaFocusLost(evt);
            }
        });
        tblProductosPorFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosPorFacturaMouseClicked(evt);
            }
        });
        tblProductosPorFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblProductosPorFacturaKeyPressed(evt);
            }
        });
        jScrollPane7.setViewportView(tblProductosPorFactura);
        if (tblProductosPorFactura.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFactura.getColumnModel().getColumn(0).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(0).setPreferredWidth(2);
            tblProductosPorFactura.getColumnModel().getColumn(1).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(1).setPreferredWidth(60);
            tblProductosPorFactura.getColumnModel().getColumn(2).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(3).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(300);
            tblProductosPorFactura.getColumnModel().getColumn(4).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(70);
            tblProductosPorFactura.getColumnModel().getColumn(5).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(6).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(6).setPreferredWidth(80);
            tblProductosPorFactura.getColumnModel().getColumn(7).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(7).setPreferredWidth(90);
            tblProductosPorFactura.getColumnModel().getColumn(8).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(8).setPreferredWidth(100);
        }

        txt_ValorFacSinIva.setEditable(false);
        txt_ValorFacSinIva.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Porcentaje de Descuento : ");

        txtPorcentajeDescuento.setEditable(false);
        txtPorcentajeDescuento.setText("0.0");
        txtPorcentajeDescuento.setEnabled(false);
        txtPorcentajeDescuento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPorcentajeDescuentoFocusGained(evt);
            }
        });
        txtPorcentajeDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPorcentajeDescuentoActionPerformed(evt);
            }
        });
        txtPorcentajeDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPorcentajeDescuentoKeyPressed(evt);
            }
        });

        lblNumeroFactura.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        lblNumeroFactura.setText(".");

        jLabel8.setText("Valor Fac. sin Iva");

        jLabel10.setText("Valor Recaudado");

        txt_valorRecaudado.setEditable(false);
        txt_valorRecaudado.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_valorRecaudado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_valorRecaudadoActionPerformed(evt);
            }
        });

        jLabel7.setText("Valor Descuento");

        txt_ValorDescuento.setEditable(false);
        txt_ValorDescuento.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_ValorDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ValorDescuentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFacturaLayout = new javax.swing.GroupLayout(pnlFactura);
        pnlFactura.setLayout(pnlFacturaLayout);
        pnlFacturaLayout.setHorizontalGroup(
            pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFacturaLayout.createSequentialGroup()
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFacturaLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel41)
                        .addGap(3, 3, 3)
                        .addComponent(txtPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(134, 134, 134)
                        .addComponent(lblNumeroFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlFacturaLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_ValorFacSinIva)
                            .addComponent(txt_ValorDescuento)
                            .addComponent(txt_valorRecaudado))
                        .addGap(37, 37, 37)
                        .addComponent(btnDescargar)
                        .addGap(507, 507, 507))
                    .addGroup(pnlFacturaLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 903, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)))
                .addContainerGap())
        );
        pnlFacturaLayout.setVerticalGroup(
            pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFacturaLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(lblNumeroFactura))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_ValorFacSinIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlFacturaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel8)))
                .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFacturaLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_ValorDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlFacturaLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel7)))
                        .addGap(4, 4, 4)
                        .addGroup(pnlFacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_valorRecaudado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFacturaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDescargar)
                        .addGap(20, 20, 20))))
        );

        lblValorFactura.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblValorFactura.setText("Total Factura : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblValorFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(lblValorFactura)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  

    /**
     * Método liquida y actualiza los datos respectivos de la fila seleccionada
     * de la tabla productos de la factura
     *
     */

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

    }//GEN-LAST:event_formInternalFrameOpened

    private void txtPorcentajeDescuentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPorcentajeDescuentoKeyPressed

        /* Evento al oprimir la tecla enter*/
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (fDescargarFacturas != null) {
                porcentajeDwcuento();
            }
            if (fliquidarManifiestos != null) {
                porcentajeDwcuento2();
            }
        

           
        }
    }//GEN-LAST:event_txtPorcentajeDescuentoKeyPressed

     public void porcentajeDwcuento() throws NumberFormatException {
        /*Se establece el porcentaje de descuento */
        porcentajeDescuento = (Double.parseDouble(txtPorcentajeDescuento.getText().replace(",", "."))) / 100;
        
        /*Se llena la columna de la tabla que corresponde al porcentaje de descuento*/
        for (int i = 0; i < tblProductosPorFactura.getRowCount(); i++) {
            
            tblProductosPorFactura.setValueAt((porcentajeDescuento * 100) + " %", i, 6);
            for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {
                
                if (tblProductosPorFactura.getValueAt(i, 2).toString().equals(pxf.getCodigoProducto())) {
                    pxf.setCantidadEntregadaItem(pxf.getCantidad());
                    pxf.setCantidadRechazadaItem(0);
                    pxf.setValorRechazoItem(0);
                    pxf.setCausalDeRechazo(1);
                    
                    pxf.setPorcentajeDescuento(porcentajeDescuento);
                    pxf.setValorDescuentoItem(pxf.getValorUnitarioSinIva() * pxf.getCantidad() * porcentajeDescuento);
                    pxf.setEntregado(1);
                    pxf.setNombreCausalDeRechazo("NINGUNO");
                    pxf.setValorTotalLiquidacionItem((pxf.getCantidad() * pxf.getValorUnitarioConIva()) - pxf.getValorDescuentoItem());
                    tblProductosPorFactura.setValueAt("" + nf.format(pxf.getValorTotalLiquidacionItem()), i, 8);
                    valorDescuento += pxf.getValorDescuentoItem();
                }
            }
            
        }
        
        txt_ValorFacSinIva.setText(nf.format(fDescargarFacturas.facturaActual.getValorFacturaSinIva()));
        valorDescuento = fDescargarFacturas.facturaActual.getValorFacturaSinIva() * porcentajeDescuento;
        txt_ValorDescuento.setText(nf.format(valorDescuento));
        valorRecaudado = fDescargarFacturas.facturaActual.getValorTotalFactura() - valorDescuento;
        txt_valorRecaudado.setText(nf.format(valorRecaudado));
        
        fDescargarFacturas.facturaActual.setAdherenciaDescargue(fDescargarFacturas.manifiestoActual.getListaFacturasDescargadas().size() + 1);
        fDescargarFacturas.facturaActual.setValorRechazo(0.0);
        fDescargarFacturas.facturaActual.setValorDescuento(valorDescuento);
        fDescargarFacturas.facturaActual.setValorRecaudado(valorRecaudado);
        fDescargarFacturas.facturaActual.setIdTipoDeMovimiento(2);
        fDescargarFacturas.facturaActual.setCausalDeRechazo(1);
        fDescargarFacturas.facturaActual.setActivo(1);
        
        lblValorFactura.setText("Total Factura : " + nf.format(valorRecaudado));
        
        btnDescargar.setEnabled(true);
        // calcularCredito();
    }
    
    public void porcentajeDwcuento2() throws NumberFormatException {
        /*Se establece el porcentaje de descuento */
        porcentajeDescuento = (Double.parseDouble(txtPorcentajeDescuento.getText().replace(",", "."))) / 100;
        
        /*Se llena la columna de la tabla que corresponde al porcentaje de descuento*/
        for (int i = 0; i < tblProductosPorFactura.getRowCount(); i++) {
            
            tblProductosPorFactura.setValueAt((porcentajeDescuento * 100) + " %", i, 6);
            for (CProductosPorFactura pxf : fliquidarManifiestos.facturaActual.getListaProductosPorFactura()) {
                
                if (tblProductosPorFactura.getValueAt(i, 2).toString().equals(pxf.getCodigoProducto())) {
                    pxf.setCantidadEntregadaItem(pxf.getCantidad());
                    pxf.setCantidadRechazadaItem(0);
                    pxf.setValorRechazoItem(0);
                    pxf.setCausalDeRechazo(1);
                    
                    pxf.setPorcentajeDescuento(porcentajeDescuento);
                    pxf.setValorDescuentoItem(pxf.getValorUnitarioSinIva() * pxf.getCantidad() * porcentajeDescuento);
                    pxf.setEntregado(1);
                    pxf.setNombreCausalDeRechazo("NINGUNO");
                    pxf.setValorTotalLiquidacionItem((pxf.getCantidad() * pxf.getValorUnitarioConIva()) - pxf.getValorDescuentoItem());
                    tblProductosPorFactura.setValueAt("" + nf.format(pxf.getValorTotalLiquidacionItem()), i, 8);
                    valorDescuento += pxf.getValorDescuentoItem();
                }
            }
            
        }
        
        txt_ValorFacSinIva.setText(nf.format(fliquidarManifiestos.facturaActual.getValorFacturaSinIva()));
        valorDescuento = fliquidarManifiestos.facturaActual.getValorFacturaSinIva() * porcentajeDescuento;
        txt_ValorDescuento.setText(nf.format(valorDescuento));
        valorRecaudado = fliquidarManifiestos.facturaActual.getValorTotalFactura() - valorDescuento;
        txt_valorRecaudado.setText(nf.format(valorRecaudado));
        
        fliquidarManifiestos.facturaActual.setAdherenciaDescargue(fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size() + 1);
        fliquidarManifiestos.facturaActual.setValorRechazo(0.0);
        fliquidarManifiestos.facturaActual.setValorDescuento(valorDescuento);
        fliquidarManifiestos.facturaActual.setValorRecaudado(valorRecaudado);
        fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(2);
        fliquidarManifiestos.facturaActual.setCausalDeRechazo(1);
        fliquidarManifiestos.facturaActual.setActivo(1);
        
        lblValorFactura.setText("Total Factura : " + nf.format(valorRecaudado));
        
        btnDescargar.setEnabled(true);
        // calcularCredito();
    }

    private void txtPorcentajeDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPorcentajeDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPorcentajeDescuentoActionPerformed

    private void txtPorcentajeDescuentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPorcentajeDescuentoFocusGained

        txtPorcentajeDescuento.setSelectionStart(0);
        txtPorcentajeDescuento.setSelectionEnd(txtPorcentajeDescuento.getText().length());
    }//GEN-LAST:event_txtPorcentajeDescuentoFocusGained

    private void tblProductosPorFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaKeyPressed

    }//GEN-LAST:event_tblProductosPorFacturaKeyPressed

    private void tblProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaMouseClicked

    }//GEN-LAST:event_tblProductosPorFacturaMouseClicked

    private void tblProductosPorFacturaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaFocusLost

    private void tblProductosPorFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaFocusGained

    private void btnDescargarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDescargarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDescargarKeyPressed

    private void btnDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarActionPerformed

        try {
            if(fliquidarManifiestos != null){
           // this.fliquidarManifiestos.descargarEntregasTotalesConDescuento();
             new Thread(new HiloFLiquidarManifiestos(ini, fliquidarManifiestos, "descargarEntregasTotalesConDescuento")).start();
            this.dispose();
            this.setVisible(false);
            }

             if(fDescargarFacturas != null){
            this.fDescargarFacturas.descargarEntregasTotalesConDescuento();
            this.dispose();
            this.setVisible(false);
            }

        } catch (Exception ex) {
            Logger.getLogger(FDescargarEntregasTotalesConDescuento.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnDescargarActionPerformed

    private void txt_valorRecaudadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_valorRecaudadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_valorRecaudadoActionPerformed

    private void txt_ValorDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ValorDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_ValorDescuentoActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        txtPorcentajeDescuento.setText("0.0");
        txt_ValorFacSinIva.setText("0.0");
        txt_ValorDescuento.setText("0.0");
        txt_valorRecaudado.setText("0.0");

        lblValorFactura.setText("Total Factura : " + nf.format(0.0));
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        txtPorcentajeDescuento.setText("0.0");
        txt_ValorFacSinIva.setText("0.0");
        txt_ValorDescuento.setText("0.0");
        txt_valorRecaudado.setText("0.0");

        lblValorFactura.setText("Total Factura : " + nf.format(0.0));
    }//GEN-LAST:event_formInternalFrameClosing

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        txtPorcentajeDescuento.requestFocus();

    }//GEN-LAST:event_formMouseClicked

    private void pnlFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlFacturaMouseClicked
        txtPorcentajeDescuento.requestFocus();
    }//GEN-LAST:event_pnlFacturaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDescargar;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lblNumeroFactura;
    private javax.swing.JLabel lblValorFactura;
    private javax.swing.JPanel pnlFactura;
    public javax.swing.JTable tblProductosPorFactura;
    public javax.swing.JTextField txtPorcentajeDescuento;
    private javax.swing.JTextField txt_ValorDescuento;
    private javax.swing.JTextField txt_ValorFacSinIva;
    private javax.swing.JTextField txt_valorRecaudado;
    // End of variables declaration//GEN-END:variables

}
