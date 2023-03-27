/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService;

import aplicacionlogistica.distribucion.formularios.*;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CProductosPorFacturaDescargados;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class FCrearIncidencia extends javax.swing.JInternalFrame {

    Object[] fila = new Object[3];
    public DefaultTableModel modelo;
    //private PerformanceInfiniteProgressPanel panel;

    DescargarFacturas form = null;
    public FDescargarDevoluciones dDescargarDevoluciones;
    public DefaultTableModel modelo1, modelo2;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    NumberFormat percentageFormat = NumberFormat.getPercentInstance(Locale.getDefault());
    public CFacturas facturaTemporal = null;
    public CManifiestosDeDistribucion manifiestoActual;
    public CFacturas facturaActual = null;

    CFacturasDescargadas facturaDescargada;

    int filaSelleccionada, columnaselecionada;
    int valorBandera;

    //ArrayList<CFacturasPorManifiesto> arrFactPorMfto;
    // ArrayList<CFacturasDescargadas> arrFacturasDescargadas;
    Inicio ini;
    boolean nuevo = false;
    double valorFactura = 0.0;
    double nuevoValorFactura;
    double valorDescuentoFactura;
    double valorrechazoFactura;
    double valorAbono = 0.0;
    int causalRechazoFactura = 1;
    boolean validadorDeRechazo = false;
    public boolean conDescuento = false;
    int totalRechazos = 0;
    String nombreCliente;

    //CProductosPorFacturaDescargados pxfd;
    String numeroDeManifiesto;
    int cosecutivoFactura;
    String numeroDeFactura;
    int idProducto;

    double itemValorUnitario = 0.0;
    double itemValorUnitarioConIva = 0.0;
    double itemPorcentajeDescuento = 0.0;
    double itemCantidadRechazada = 0;
    double itemValorTotalItem;
    int itemCantidad;
    int itemCausalDeRechazo;
    double porcentajeDescuento = 0.0;

   ArrayList<Vst_ProductosPorFactura> listaDeProductoPorFactura;

    public FCrearIncidencia() {
        //this.ini = form.ini;
        initComponents();
    }

    public FCrearIncidencia(CFacturas facturaTemporal) {
        this.facturaTemporal = facturaTemporal;

        initComponents();

    }


    public void llenarFormulario() throws Exception {
        if (form != null) {
            this.ini = form.ini;
            facturaDescargada = new CFacturasDescargadas(ini);

           

            try {

                if (form.isFacturaCredito) {
                    txtNumeroFactura.setEditable(true);
                    txtNumeroFactura.setEnabled(true);
                    txtNumeroFactura.requestFocus();
                }

                facturaDescargada.setConsecutivo(form.facturaActual.getConsecutivo());
                facturaDescargada.setNumeroManifiesto(form.manifiestoActual.getNumeroManifiesto());
                facturaDescargada.setAdherenciaDescargue(form.manifiestoActual.getListaFacturasDescargadas().size() + 1);
                facturaDescargada.setNumeroFactura(facturaTemporal.getNumeroDeFactura());
                facturaDescargada.setValorARecaudarFactura(facturaTemporal.getValorTotalFactura());
                facturaDescargada.setValorRechazo(0.0);
                facturaDescargada.setValorDescuento(0.0);
                facturaDescargada.setValorRecaudado(0.0);
                facturaDescargada.setIdTipoDeMovimiento(4);
                facturaDescargada.setCausalDeRechazo(1);

                listaDeProductoPorFactura = new ArrayList();
                llenarTablaProductosPorFactura();
                ocultarColumnaCero();

            } catch (Exception ex) {
                Logger.getLogger(FCrearIncidencia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
         if (dDescargarDevoluciones != null) {
              this.ini = dDescargarDevoluciones.ini;
            facturaDescargada = new CFacturasDescargadas(ini);

           

            try {

                if (dDescargarDevoluciones.isFacturaCredito) {
                    txtNumeroFactura.setEditable(true);
                    txtNumeroFactura.setEnabled(true);
                    txtNumeroFactura.requestFocus();
                }

                facturaDescargada.setConsecutivo(dDescargarDevoluciones.facturaActual.getConsecutivo());
                facturaDescargada.setNumeroManifiesto(dDescargarDevoluciones.manifiestoActual.getNumeroManifiesto());
                facturaDescargada.setAdherenciaDescargue(dDescargarDevoluciones.listaFacturasDescargadas.size() + 1);
                facturaDescargada.setNumeroFactura(facturaTemporal.getNumeroDeFactura());
                facturaDescargada.setValorARecaudarFactura(facturaTemporal.getValorTotalFactura());
                facturaDescargada.setValorRechazo(0.0);
                facturaDescargada.setValorDescuento(0.0);
                facturaDescargada.setValorRecaudado(0.0);
                facturaDescargada.setIdTipoDeMovimiento(1);
                facturaDescargada.setCausalDeRechazo(1);

                listaDeProductoPorFactura = new ArrayList();
                llenarTablaProductosPorFactura();
                ocultarColumnaCero();

            } catch (Exception ex) {
                Logger.getLogger(FCrearIncidencia.class.getName()).log(Level.SEVERE, null, ex);
            }
             
         }
    }

   

    /**
     * Método constructor cuando se está descargando las facturas del
     * manifiesto, en el cualse va a descargar una factura con entregas con
     * rechazos parciales ó descuento en algunos ó todos los items
     *
     *
     * @param form es el fBuscarManifiestos que invoca a éste fBuscarManifiestos para descargar
 loa rechazos parciales del sistema.
     * @param a parametro de nada, para distraer constructor
     */
    public FCrearIncidencia(Inicio ini) {
        initComponents();
        this.form = form;
        this.ini = ini;

        manifiestoActual = form.manifiestoActual;
        facturaTemporal = facturaTemporal;
        numeroDeManifiesto = form.manifiestoActual.getNumeroManifiesto();
        //lblFactura.setText("Factura # " + facturaTemporal.getNumeroFactura());

        try {

            if (form.isFacturaCredito) {
                txtNumeroFactura.setEditable(true);
                txtNumeroFactura.setEnabled(true);
                txtNumeroFactura.requestFocus();
            }

            // se llena el cbx de causales de rechazo
            CFacturasDescargadas facturaDescargada = new CFacturasDescargadas(ini);

            manifiestoActual.setListaFacturasPorManifiesto();

            // Se identifica la factura que se digitó, dentro del numeroDeManifiesto
            for (CFacturasPorManifiesto obj : manifiestoActual.getListaFacturasPorManifiesto()) {
                if (obj.getNumeroFactura().equals(facturaTemporal.getNumeroDeFactura())) {
                    facturaTemporal = facturaTemporal;
                    numeroDeManifiesto = obj.getNumeroManifiesto();
                    cosecutivoFactura = obj.getConsecutivo();
                    break;

                }
            }

//        
            try {

                facturaDescargada.setNumeroManifiesto(form.manifiestoActual.getNumeroManifiesto());
                facturaDescargada.setNumeroFactura(facturaTemporal.getNumeroDeFactura());

                modelo1 = (DefaultTableModel) tblProductosPorFactura.getModel();

                // se nexan los productos a la tabla de productos por factura
                //listaTemporalDeProductosDescargados = new ArrayList<>();
                for (Vst_ProductosPorFactura obj : listaDeProductoPorFactura) {

                    CProductosPorFacturaDescargados pxfd = new CProductosPorFacturaDescargados(ini);

                    pxfd.setConsecutivoFacturasDescargadas(cosecutivoFactura);

                    /* Acá se declara un objeto producto por factura y nos traemos el consecutivo asignado en la 
                     importacio de datos desde excel, guardado en la BBDD remota*/
                    CProductosPorFactura pxf = new CProductosPorFactura(ini);

                    /* se verifica el consecutivo del producto en la BBDD Remota */
//                    pxf.setConsecutivoProductoPorFactura(obj.getNumeroFactura(), obj.getCodigoProducto());

                    /* acá se asigna el consecutivo respectivo al objeto producto por factura descargado, asignando 
                     el consecutivo recuperado en la operaciń anterior*/
//                    pxfd.setConsecutivoProductosPorFactura(pxf.getConsecutivo());

                    // PROPIEDADES DE PRODUCTOS POR FACTURA
//                    pxfd.setConsecutivo(pxf.getConsecutivo());
//                    pxfd.setFactura(obj.getNumeroFactura());
//                    pxfd.setCodigoProducto(obj.getCodigoProducto());
//                    pxfd.setCantidad(obj.getCantidad());
//                    pxfd.setValorUnitarioSinIva(obj.getValorUnitarioSinIva());
//                    pxfd.setValorTotalItem(obj.getValorTotalItem());
//                    pxfd.setValorUnitarioConIva(obj.getValorUnitarioConIva());
//                    pxfd.setValorTotalConIva(obj.getValorTotalConIva());
//
//                    // PROPIEDADES DE PRODUCTOS POR FACTURA DESCARGADOS
//                    pxfd.setValorDescuento(0);
//                    pxfd.setCantidadRechazada(0);
//                    pxfd.setValorRechazo(0);
//                    pxfd.setCantidadEntregada(obj.getCantidad());
//                    pxfd.setTotalRecaudadoProducto(obj.getValorTotalConIva());
//                    pxfd.setEntregado(1);
//                    pxfd.setCausalDeRechazo(1);
//                    pxfd.setActivo(1);
                    // Se anexa el objeto a el array de productos por factura descatgados sin datos todavia
//
//                    // SE LLENA LA TABLA DE PRODUCTOS POR FACTURA
//                    int row = tblProductosPorFactura.getRowCount();
//
//                    modelo1.addRow(new Object[row]);
//
//                    tblProductosPorFactura.setValueAt(obj.getConsecutivo(), row, 0);
//                    tblProductosPorFactura.setValueAt("" + (row + 1), row, 1);
//                    tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), row, 2); // código del producto
//
//                    tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), row, 3);// descripción del producto
//                    tblProductosPorFactura.setValueAt(obj.getCantidad(), row, 4);                       // cantidad de producto en la factura
//                    tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioSinIva()), row, 5);// valor unitario
//
//                    tblProductosPorFactura.setValueAt(percentageFormat.format(cliente.getPorcentajeDescuento()), row, 6); // porcentje de descuento
//                    tblProductosPorFactura.setValueAt("" + 0, row, 7);
//                    tblProductosPorFactura.setValueAt(nf.format(obj.getValorTotalConIva()), row, 8); // valor del prducto con el iva
//
//                    JComboBox combo = CreameCombo();
//                    TableColumn col = tblProductosPorFactura.getColumnModel().getColumn(9);
//                    col.setMinWidth(120);
//                    col.setMaxWidth(120);
//                    col.setResizable(false);
//                    col = tblProductosPorFactura.getColumnModel().getColumn(9);
//                    col.setCellEditor(new DefaultCellEditor(combo));//AGREGO EL COMBO AL CELLEDITOR
//
//                    //tblProductosPorFactura.setValueAt("NINGUNO", row, 9);
//                    valorFactura += obj.getValorTotalConIva();
                } // FINAL DEL CICLO FOR

                lblValorFactura.setText("Total Factura : " + nf.format(valorFactura));

            } catch (Exception ex) {
                System.out.println("Error en txtNumeroDeFacturaKeyPressed ");
                Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);
            }

            // para que l columna cero(0) no sea visible
            ocultarColumnaCero();

        } catch (Exception ex) {
            Logger.getLogger(FCrearIncidencia.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método constructor cuando se está descargando las facturas del
     * manifiesto, en el cualse va a descargar una factura con entregas con
     * rechazos parciales ó descuento en algunos ó todos los items
     *
     *
     *
     * @param form es el fBuscarManifiestos que invoca a éste fBuscarManifiestos para descargar
 loa rechazos parciales del sistema.
     */
    public FCrearIncidencia(DescargarFacturas form) {
//        initComponents();
//        this.ini = form.ini;
//        this.form = form;
//
//        facturaTemporal = facturaTemporal;
//        // new Thread(new Hilo_HT_productosPorFactura(ini, facturaTemporal)).start();
//        manifiestoActual = form.manifiestoActual;
//        listaTemporalDeProductosDescargados = new ArrayList();
//        lblFactura.setText("Factura # " + facturaTemporal.getNumeroFactura());
//
//        // se llena el cbx de causales de rechazo
//        cbxCausalesDeRechazo.removeAllItems();
//        for (CCausalesDeRechazo obj : ini.getArrCausalesDeRechazo()) {
//            cbxCausalesDeRechazo.addItem(obj.getNombreCausalesDeRechazol());
//        }
//        cbxCausalesDeRechazo.setSelectedItem("NINGUNO");
//
//        if (form.isFacturaCredito) {
//            txtValorAbono.setEditable(true);
//            txtValorAbono.setEnabled(true);
//            txtValorAbono.requestFocus();
//        }
//
//        nuevo = true;
//
//        /* Se identifica la factura que se digitó, dentro del numeroDeManifiesto y
//         nos traemmos el numero del manifiesto y el consecutivo asigando en la factura 
//         */
//        for (Vst_FacturasPorManifiesto obj : manifiestoActual.getVistaFacturasEnDistribucion()) {
//            if (obj.getNumeroFactura().equals(facturaTemporal.getNumeroFactura())) {
//                numeroDeManifiesto = obj.getNumeroManifiesto();
//                cosecutivoFactura = obj.getConsecutivo();
//                break;
//
//            }
//        }
//
//        /* Se toma el objeto vista de productos por factura, en la cual se va a construir
//         una array de objetos productos por factura descargados, teniendo como base la
//         factura que se digitó en la vista de descargue de facturas
//         */
//        for (Vst_ProductosPorFactura obj : facturaTemporal.getListaProductosPorFactura()) {
//
//            try {
//
//                /*Se realiza una instancia del objeeto productos por facturas descargados
//                 con el fin de asignar los respectivos valores
//                 */
//                CProductosPorFacturaDescargados pxfd = new CProductosPorFacturaDescargados(ini);
//
//                /*Se le asignan los valores respectivos al objeto*/
//                pxfd.setFactura(obj.getNumeroFactura());
//                pxfd.setCodigoProducto(obj.getCodigoProducto());
//                pxfd.setCantidad(obj.getCantidad());
//                pxfd.setValorUnitarioSinIva(obj.getValorUnitarioSinIva());
//                pxfd.setValorTotalItem(obj.getValorTotalItem());
//                pxfd.setValorUnitarioConIva(obj.getValorUnitarioConIva());
//                pxfd.setValorTotalConIva(obj.getValorTotalConIva());
//
//                // PROPIEDADES DE PRODUCTOS POR FACTURA DESCARGADOS
//                pxfd.setValorDescuento(0);
//                pxfd.setCantidadRechazada(0);
//                pxfd.setValorRechazo(0);
//                pxfd.setCantidadEntregada(obj.getCantidad());
//                pxfd.setTotalRecaudadoProducto(obj.getValorTotalConIva());
//                pxfd.setEntregado(1);
//                pxfd.setCausalDeRechazo(1);
//                pxfd.setActivo(1);
//
//                /*Se incorpora el objeto al array respectivo*/
//                listaTemporalDeProductosDescargados.add(pxfd);
//
//            } catch (Exception ex) {
//                Logger.getLogger(FDescargarRechazosParciales.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        ocultarColumnaCero();
//        llenarTablaProductosPorFactura();

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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProductosPorFactura = new javax.swing.JTable();
        lblValorFactura = new javax.swing.JLabel();
        txt_columnaId = new javax.swing.JTextField();
        txt_columnaCodigoProducto = new javax.swing.JTextField();
        txt_columnaNombreProducto = new javax.swing.JTextField();
        txt_columnaCantidadProducto = new javax.swing.JTextField();
        txt_columnaValorTotalItem = new javax.swing.JTextField();
        txtNumeroFactura = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();

        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N
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

        tblProductosPorFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "#", "Ref", "Producto", "Cant.", "Vu sin Iva", "Valor Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductosPorFactura.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProductosPorFactura.getTableHeader().setReorderingAllowed(false);
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
            tblProductosPorFactura.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblProductosPorFactura.getColumnModel().getColumn(1).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(1).setPreferredWidth(60);
            tblProductosPorFactura.getColumnModel().getColumn(2).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(2).setPreferredWidth(120);
            tblProductosPorFactura.getColumnModel().getColumn(3).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(250);
            tblProductosPorFactura.getColumnModel().getColumn(4).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(60);
            tblProductosPorFactura.getColumnModel().getColumn(5).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(6).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(6).setPreferredWidth(100);
        }

        lblValorFactura.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblValorFactura.setText("Total Factura : ");

        txt_columnaId.setEditable(false);
        txt_columnaId.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N

        txt_columnaCodigoProducto.setEditable(false);
        txt_columnaCodigoProducto.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N

        txt_columnaNombreProducto.setEditable(false);
        txt_columnaNombreProducto.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_columnaNombreProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_columnaNombreProductoActionPerformed(evt);
            }
        });

        txt_columnaCantidadProducto.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_columnaCantidadProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_columnaCantidadProductoFocusGained(evt);
            }
        });
        txt_columnaCantidadProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_columnaCantidadProductoActionPerformed(evt);
            }
        });
        txt_columnaCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_columnaCantidadProductoKeyPressed(evt);
            }
        });

        txt_columnaValorTotalItem.setEditable(false);
        txt_columnaValorTotalItem.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_columnaValorTotalItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_columnaValorTotalItemActionPerformed(evt);
            }
        });

        txtNumeroFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroFacturaFocusGained(evt);
            }
        });
        txtNumeroFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroFacturaActionPerformed(evt);
            }
        });
        txtNumeroFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroFacturaKeyPressed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setLabelFor(txt_columnaId);
        jLabel1.setText("Direccion Cliente");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setLabelFor(txt_columnaCodigoProducto);
        jLabel3.setText("Nombre Cliente");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setLabelFor(txt_columnaNombreProducto);
        jLabel4.setText("Barrio");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setLabelFor(txt_columnaCantidadProducto);
        jLabel5.setText("Telefono");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Vendedor");

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("# Factura");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)))))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(248, 248, 248))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_columnaNombreProducto)
                                        .addGap(75, 75, 75)))
                                .addComponent(lblValorFactura))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_columnaId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                                .addComponent(txt_columnaCantidadProducto, javax.swing.GroupLayout.Alignment.LEADING))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_columnaValorTotalItem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                                .addComponent(txt_columnaCodigoProducto, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 741, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_columnaNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblValorFactura)))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_columnaCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_columnaCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_columnaId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_columnaValorTotalItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(2, 2, 2)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Datos Factura", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 773, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Datos de Distribucion", jPanel2);

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

        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jBtnGrabar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnGrabar.setEnabled(false);
        jBtnGrabar.setFocusable(false);
        jBtnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGrabarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGrabar);

        jBtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jBtnImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnImprimir.setEnabled(false);
        jBtnImprimir.setFocusable(false);
        jBtnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnImprimirActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnImprimir);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void tblProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaMouseClicked
        //btnDescargar.setEnabled(false);

        /* Se identifica la fila seleccionada*/
        filaSelleccionada = tblProductosPorFactura.getSelectedRow();

        /* Se identifica el codigo del producto */
        idProducto = Integer.parseInt(tblProductosPorFactura.getValueAt(filaSelleccionada, 2).toString());

      

        nuevo = true;

        // pxfd=obj;
        try {
            /* incicio try 1*/

            txt_columnaCantidadProducto.setEnabled(true);
            

            txt_columnaId.setText(tblProductosPorFactura.getValueAt(filaSelleccionada, 1).toString());
            

            
         
            
//            txt_columnaValorTotalItem.setText("" + nf.format(facturaTemporal.getListaProductosPorFactura().get(filaSelleccionada).getValorTotalConIva()));
          


            double nuevoValorFactura = 0;

           
            lblValorFactura.setText("Total Factura : " + nf.format(nuevoValorFactura));

        

        } catch (Exception ex) {
            Logger.getLogger(FCrearIncidencia.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_tblProductosPorFacturaMouseClicked

    private void tblProductosPorFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaKeyPressed

    }//GEN-LAST:event_tblProductosPorFacturaKeyPressed

    private void tblProductosPorFacturaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaFocusLost

    private void tblProductosPorFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaFocusGained

    private void txt_columnaCantidadProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_columnaCantidadProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_columnaCantidadProductoActionPerformed

    private void txt_columnaValorTotalItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_columnaValorTotalItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_columnaValorTotalItemActionPerformed

    private void txt_columnaNombreProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_columnaNombreProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_columnaNombreProductoActionPerformed

 

    /**
     * Método liquida y actualiza los datos respectivos de la fila seleccionada
     * de la tabla productos de la factura
     *
     */
    private void perderElFocoRechazos() throws NumberFormatException {

    }

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

    }//GEN-LAST:event_formInternalFrameOpened

    private void txtNumeroFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroFacturaFocusGained
        txtNumeroFactura.setSelectionStart(0);
        txtNumeroFactura.setSelectionEnd(txtNumeroFactura.getText().length());
    }//GEN-LAST:event_txtNumeroFacturaFocusGained

    private void txtNumeroFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFacturaActionPerformed

    private void txtNumeroFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroFacturaKeyPressed

        /* Evento al oprimir la tecla enter*/
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //calcularCredito();a
            porcentajeDescuento = Double.parseDouble(txtNumeroFactura.getText().trim());
            CalcularValorTabla();
        }
    }//GEN-LAST:event_txtNumeroFacturaKeyPressed
    private void CalcularValorTabla() {
        llenarTabla();

    }

    private void llenarTabla() {
        int i = 0;
        for (CProductosPorFactura obj : this.facturaTemporal.getListaCProductosPorFactura()) {
            tblProductosPorFactura.setValueAt(porcentajeDescuento + " %", i, 6);

            i++;
        }
        i = 0;
        nuevoValorFactura = 0;
        for (Vst_ProductosPorFactura obj : listaDeProductoPorFactura) {
           

        }

    }

    private void txt_columnaCantidadProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_columnaCantidadProductoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
      }

    }//GEN-LAST:event_txt_columnaCantidadProductoKeyPressed

    

    private void txt_columnaCantidadProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_columnaCantidadProductoFocusGained
        txt_columnaCantidadProducto.setSelectionStart(0);
       
    }//GEN-LAST:event_txt_columnaCantidadProductoFocusGained

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        limpiar();
        if(form!=null){
             form.txtNumeroDeFactura.setEnabled(true);
        }else{
           dDescargarDevoluciones.txtNumeroDeFactura.setEnabled(true); 
        }
        
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        limpiar();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
        grabar();
    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jBtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnImprimirActionPerformed
        imprimir();
    }//GEN-LAST:event_jBtnImprimirActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        cancelar();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnMinutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnMinutaActionPerformed

        if (manifiestoActual != null) {
           

            switch (manifiestoActual.getEstadoManifiesto()) {
                case 1:

                case 2:

                case 3:
               
                break;
                case 4:
               
                break;

            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay manifiesto de ruta selecccionado ", "Error", 0);
        }
    }//GEN-LAST:event_jBtnMinutaActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        salir();        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnExitActionPerformed

   

   


   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    private javax.swing.JToggleButton jBtnImprimir;
    private javax.swing.JToggleButton jBtnMinuta;
    private javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblValorFactura;
    public javax.swing.JTable tblProductosPorFactura;
    private javax.swing.JTextField txtNumeroFactura;
    private javax.swing.JTextField txt_columnaCantidadProducto;
    private javax.swing.JTextField txt_columnaCodigoProducto;
    private javax.swing.JTextField txt_columnaId;
    private javax.swing.JTextField txt_columnaNombreProducto;
    private javax.swing.JTextField txt_columnaValorTotalItem;
    // End of variables declaration//GEN-END:variables

    private void llenarTablaProductosPorFactura() {
        modelo = (DefaultTableModel) tblProductosPorFactura.getModel();

        /*Limpia la tabla de los datos*/
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        nuevoValorFactura = 0;

       
        // se nexan los productos a la tabla de productos por factura
        for (Vst_ProductosPorFactura obj : listaDeProductoPorFactura) {

          
            int fil = tblProductosPorFactura.getRowCount();

            modelo.addRow(new Object[fil]);

            tblProductosPorFactura.setValueAt(obj.getConsecutivo(), fil, 0);
            tblProductosPorFactura.setValueAt("" + (fil + 1), fil, 1);
            tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), fil, 2);
            tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fil, 3);
            tblProductosPorFactura.setValueAt(obj.getCantidad(), fil, 4);
            tblProductosPorFactura.setValueAt("" + nf.format(obj.getValorUnitarioSinIva()), fil, 5);
            tblProductosPorFactura.setValueAt(0.0 + " %", fil, 6);
            tblProductosPorFactura.setValueAt("" + 0, fil, 7);
            tblProductosPorFactura.setValueAt(("" + nf.format(obj.getValorTotalConIva())), fil, 8);
            valorFactura += obj.getValorTotalConIva();

         
            


        }

        lblValorFactura.setText("Total Factura : " + nf.format(nuevoValorFactura));

    }

   

    public void limpiar() {
        Inicio ini = null;
        nuevo = false;
        valorFactura = 0.0;
        nuevoValorFactura = 0.0;
        valorDescuentoFactura = 0.0;
        valorrechazoFactura = 0.0;
        valorAbono = 0.0;
        causalRechazoFactura = 1;
        validadorDeRechazo = false;
        conDescuento = false;
        totalRechazos = 0;
        itemValorUnitario = 0.0;
        itemValorUnitarioConIva = 0.0;
        itemPorcentajeDescuento = 0.0;
        itemCantidadRechazada = 0;
        itemValorTotalItem = 0.0;
        itemCantidad = 0;
        itemCausalDeRechazo = 0;
       

        txt_columnaCantidadProducto.setText("");
      
        txt_columnaCodigoProducto.setText("");
        txt_columnaId.setText("");
        txt_columnaNombreProducto.setText("");
    
        txt_columnaValorTotalItem.setText("");
       

        modelo = (DefaultTableModel) tblProductosPorFactura.getModel();

        /*Limpia la tabla de los datos*/
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }
    
    private void nuevo(){
        
    }
    
    private void grabar(){
        
    }
    
    private void cancelar(){
                
    }
    
    private void imprimir(){
        
    }
    
    private void salir(){
        
    }

}
