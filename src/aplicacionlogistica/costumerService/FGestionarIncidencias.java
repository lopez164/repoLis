/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService;

import aplicacionlogistica.distribucion.consultas.*;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.Threads.HiloGestonDeIncidencias;
import aplicacionlogistica.costumerService.objetos.CGestiones;
import aplicacionlogistica.costumerService.objetos.CtiposGestiones;
import aplicacionlogistica.costumerService.objetos.CSvcIncidencias;
import aplicacionlogistica.costumerService.objetos.CTiposDePeticion;
import aplicacionlogistica.distribucion.objetos.CBitacoraFacturas;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CMovimientosManifiestosfacturas;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFacturaDescargados;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import maps.java.StaticMaps;

/**
 *
 * @author VLI_488
 */
public class FGestionarIncidencias extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    //CUsuarios user;
    Inicio ini = null;

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    DefaultTableModel modelo1, modelo2;

    //Double valorTotalAConsignar = 0.0;
    boolean cargado = false;
    // boolean tieneManifiestosAnteriores = false;
    // boolean tieneMnifiestosAsigndos = false;
    boolean nuevo = false;
    boolean actualizar = false;
    boolean liberado = false;
    boolean grabar = false;
    public boolean completado = false;
    private StaticMaps ObjStaticMaps = new StaticMaps();

    int filaSelleccionada;
    int indiceLista = 0;
    int columna = 0;
    String mensaje = null;
   
    
    double nuevoValorFactura = 0;

    JInternalFrame form;

    public CSvcIncidencias incidenciaSvc;
   
    FIncidenciasSvC form1;
    List<CSvcIncidencias> listaDeIncidencias;
    CGestiones gestion;
    CSvcIncidencias objIncidenciaActual;

    //public ArrayList<Vst_ProductosPorFacturaDescargados> listaDeProductosRechazados;
    /**
     * Creates new fReportemovilizadoPorConductor IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public FGestionarIncidencias(Inicio ini) throws Exception {
        this.ini = ini;
        initComponents();
        
                    manager.addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    //como dije, solo las notificaciones del tipo "typed" son las que actualizan los componentes
                    if (e.getID() == KeyEvent.KEY_TYPED) {
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

                        } else if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
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

                    //y listo, regresamos siempre falso para que las demas notificaciones continuen, si regresamos true
                    // significa que el dispatcher consumio el evento
                    return false;
                }
            });
                    
        lblCirculoDeProgreso.setVisible(false);
        txtNumeroFactura.setEnabled(true);
        txtNumeroFactura.setEditable(true);

        cbxTiposGestiones.removeAllItems();
        ini.setListaDeTiposgestione();
        for (CtiposGestiones obj : ini.getListaDeTiposgestione()) {
            cbxTiposGestiones.addItem(obj.getNombreTipoDeGestion());
        }

        cbxTipoMovimiento.removeAllItems();
        for (CMovimientosManifiestosfacturas mov : ini.getListaDeMovimientosFacturas()) {
            cbxTipoMovimiento.addItem(mov.getNombreMovimientosManifiestosfacturas());
        }

        cbxCausalesDeDevolucion.removeAllItems();
        for (CCausalesDeDevolucion cr : ini.getListaDeCausalesDeDevolucion()) {
            cbxCausalesDeDevolucion.addItem(cr.getNombreCausalesDeDevolucion());
        }

        cbxTiposDePeticion.removeAllItems();
        for (CTiposDePeticion cr : ini.getListaDeTiposDePeticion()) {
            cbxTiposDePeticion.addItem(cr.getNombreTiposDePeticones());
        }

        txtNumeroFactura.requestFocus();
        txtNumeroFactura.requestFocus();

    }

    /**
     * Creates new fReportemovilizadoPorConductor IngresarManifiestoDeDistribucion
     *
     * @param form
     * @throws java.lang.Exception
     */
    public FGestionarIncidencias(FIncidenciasSvC form) throws Exception {

        try {
            initComponents();

            this.form1 = form;
            this.incidenciaSvc = form1.incidencia;

            lblCirculoDeProgreso.setVisible(false);
            this.ini = form1.ini;

          //  manifiestoActual = new CManifiestosDeDistribucion(this.ini, Integer.parseInt(this.incidenciaSvc.getObjManifiesto().));

            if (form1.tablaIncidencias.getValueAt(form1.filaSeleccionada, 3).toString().equals("CERRADA")) {
                deshabilitarComponentes();
            }

            //btnNuevo.setEnabled(false);
           // btnCancelar.setEnabled(false);
            cbxTipoMovimiento.setEnabled(false);
            cbxCausalesDeDevolucion.setEnabled(false);
            cbxTiposDePeticion.setEnabled(false);
            txtObservacion.setEnabled(false);

            txtNumeroFactura.setEnabled(true);
            txtNumeroFactura.setEditable(false);
            txtNumeroFactura.setText(incidenciaSvc.getObjFacturaCamdun().getNumeroDeFactura());
            txtVehiculo.setText(incidenciaSvc.getObjManifiesto().getVehiculo());
            txtConductor.setText(incidenciaSvc.getObjManifiesto().getNombreConductor() + " " + incidenciaSvc.getObjManifiesto().getApellidosConductor());

//            switch (manifiestoActual.getListaDeAuxiliares().size()) {
//                case 1:
//                    txtNombreAuxiliar1.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(0).getCedula()));
//                    break;
//                case 2:
//                    txtNombreAuxiliar1.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(0).getCedula()));
//                    txtNombreAuxiliar2.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(1).getCedula()));
//                    break;
//                case 3:
//                    txtNombreAuxiliar1.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(0).getCedula()));
//                    txtNombreAuxiliar2.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(1).getCedula()));
//                    txtNombreAuxiliar3.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(2).getCedula()));
//                    break;
//
//            }
            txtNombreRuta.setText(incidenciaSvc.getObjManifiesto().getNombreDeRuta());
            txtNombreCanal.setText(incidenciaSvc.getObjFacturaCamdun().getCiudad());

            txtCelularConductor.setText(incidenciaSvc.getObjManifiesto().getTelefonoConductor());
            txtCelularCliente.setText(incidenciaSvc.getObjFacturaCamdun().getTelefonoCliente());
            txtCelularVendedor.setText(incidenciaSvc.getObjFacturaCamdun().getTelefonoVendedor());

            consultarFactura();
            crearMapa(incidenciaSvc);

            modelo2 = (DefaultTableModel) tblGestiones.getModel();

            for (CGestiones obj : incidenciaSvc.getListadoDeGestiones()) {
                SimpleDateFormat parseador = new SimpleDateFormat("yyy-MM-dd");
// el que formatea
                SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                Date date = new Date();

                //
                String fecha = parseador.format(obj.getFechaIng());
                System.out.println("esta es la fecha " + fecha);
                filaSelleccionada = tblGestiones.getRowCount();

                modelo2.addRow(new Object[tblGestiones.getRowCount()]);

                tblGestiones.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);  // item
                tblGestiones.setValueAt(incidenciaSvc.getConsecutivo(), filaSelleccionada, 1); // numero de facturaActual
                tblGestiones.setValueAt(obj.getGestion(), filaSelleccionada, 2); // numero de facturaActual
                tblGestiones.setValueAt(obj.getRespuesta(), filaSelleccionada, 3); // numero de facturaActual
                //tblGestiones.setValueAt(formateador.format(obj.getFechaIng()), filaSelleccionada, 4); // numero de facturaActual
                tblGestiones.setValueAt(fecha, filaSelleccionada, 4); // numero de facturaActual
            }

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
        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FGestionarIncidencias.class.getName()).log(Level.SEVERE, null, ex);
            form1.fGestion = false;
            cargado = false;
        }
        form1.fGestion = true;
        cargado = true;
        cbxTiposGestiones.removeAllItems();
        ini.setListaDeTiposgestione();
        for (CtiposGestiones obj : ini.getListaDeTiposgestione()) {
            cbxTiposGestiones.addItem(obj.getNombreTipoDeGestion());
        }

        cbxTipoMovimiento.removeAllItems();
        for (CMovimientosManifiestosfacturas mov : ini.getListaDeMovimientosFacturas()) {
            cbxTipoMovimiento.addItem(mov.getNombreMovimientosManifiestosfacturas());
        }

        cbxCausalesDeDevolucion.removeAllItems();
        for (CCausalesDeDevolucion cr : ini.getListaDeCausalesDeDevolucion()) {
            cbxCausalesDeDevolucion.addItem(cr.getNombreCausalesDeDevolucion());
        }

    }

    /**
     * Creates new fReportemovilizadoPorConductor IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @param incidencia
     */
    public FGestionarIncidencias(Inicio ini, CSvcIncidencias incidencia, int form) throws Exception {

        try {
            initComponents();
            this.incidenciaSvc = incidencia;
            //this.form1 = fReportemovilizadoPorConductor;
            lblCirculoDeProgreso.setVisible(false);
            this.ini = ini;
            txtNumeroFactura.setEnabled(true);
            txtNumeroFactura.setEditable(false);
            txtNumeroFactura.setText(incidencia.getObjFacturaCamdun().getNumeroDeFactura());
            txtVehiculo.setText(incidencia.getObjManifiesto().getVehiculo());
            txtConductor.setText(incidencia.getObjManifiesto().getNombreConductor() + " " + incidencia.getObjManifiesto().getApellidosConductor() );
//            switch (manifiestoActual.getListaDeAuxiliares().size()) {
//                case 1:
//                    txtNombreAuxiliar1.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(0).getCedula()));
//                    break;
//                case 2:
//                    txtNombreAuxiliar1.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(0).getCedula()));
//                    txtNombreAuxiliar2.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(1).getCedula()));
//                    break;
//                case 3:
//                    txtNombreAuxiliar1.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(0).getCedula()));
//                    txtNombreAuxiliar2.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(1).getCedula()));
//                    txtNombreAuxiliar3.setText(getNombresDeEmpleado(manifiestoActual.getListaDeAuxiliares().get(2).getCedula()));
//                    break;
//
//            }

            txtNombreRuta.setText(incidencia.getObjFacturaCamdun().getBarrio());
            txtNombreCanal.setText(incidencia.getObjFacturaCamdun().getCiudad());
            txtCelularConductor.setText(incidencia.getObjManifiesto().getTelefonoConductor());
            txtCelularCliente.setText(incidencia.getObjFacturaCamdun().getTelefonoCliente());
            txtCelularVendedor.setText(incidencia.getObjFacturaCamdun().getTelefonoVendedor());

            consultarFactura();
            crearMapa(incidencia);

            modelo2 = (DefaultTableModel) tblGestiones.getModel();
            for (CGestiones obj : incidencia.getListadoDeGestiones()) {
                SimpleDateFormat parseador = new SimpleDateFormat("yyy-MM-dd");
// el que formatea
                SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                Date date = new Date();

                System.out.println(formateador.format(obj.getFechaIng()));

                filaSelleccionada = tblGestiones.getRowCount();

                modelo2.addRow(new Object[tblGestiones.getRowCount()]);

                tblGestiones.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);  // item
                tblGestiones.setValueAt(incidencia.getConsecutivo(), filaSelleccionada, 1); // numero de facturaActual
                tblGestiones.setValueAt(obj.getGestion(), filaSelleccionada, 2); // numero de facturaActual
                tblGestiones.setValueAt(obj.getRespuesta(), filaSelleccionada, 3); // numero de facturaActual
                tblGestiones.setValueAt(formateador.format(obj.getFechaIng()), filaSelleccionada, 4); // numero de facturaActual
            }

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
        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FGestionarIncidencias.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;
        cbxTiposGestiones.removeAllItems();
        for (CtiposGestiones obj : ini.getListaDeTiposgestione()) {
            cbxTiposGestiones.addItem(obj.getNombreTipoDeGestion());
        }

    }

    /**
     * Creates new fReportemovilizadoPorConductor IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @param form formulario desde donde se carga la interface gráfica
     * @param factura corrresponde a la facturaActual que se va a consultar
     */
    public FGestionarIncidencias(Inicio ini, JInternalFrame form, CFacturas factura) throws Exception {
       
        

        try {
            initComponents();
            
                        manager.addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    //como dije, solo las notificaciones del tipo "typed" son las que actualizan los componentes
                    if (e.getID() == KeyEvent.KEY_TYPED) {
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

                        } else if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
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

                    //y listo, regresamos siempre falso para que las demas notificaciones continuen, si regresamos true
                    // significa que el dispatcher consumio el evento
                    return false;
                }
            });
            this.ini = ini;
//            this.facturaActual = factura;
            this.form = form;
            
 //           llenarDatosDeLaVista();

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FGestionarIncidencias.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    public FGestionarIncidencias(Inicio ini, CFacturas factura) throws Exception {
       // this.facturaActual = factura;
        try {
            initComponents();
            this.ini = ini;
        //    llenarDatosDeLaVista();

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FGestionarIncidencias.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    public FGestionarIncidencias(Inicio ini, JInternalFrame form, String numeroFactura) throws Exception {

        try {
            initComponents();
            this.ini = ini;
            txtNumeroFactura.setEnabled(true);
            txtNumeroFactura.setEditable(true);
            txtNumeroFactura.requestFocus();

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

           // this.facturaActual = new CFacturasPorManifiesto(ini, numeroFactura);
            if ( true) {
                /*Hilo que recupera los movimientos de la facturaActual */
               // new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this.facturaActual)).start();
               // llenarDatosDeLaVista();
            } else {
                JOptionPane.showMessageDialog(null, "Número de Factura no encontrado en el sistema ", "Error", JOptionPane.WARNING_MESSAGE);

            }

        } catch (Exception ex) {
            System.out.println("Error en FConsultarFacturas ");
            Logger.getLogger(FGestionarIncidencias.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargado = true;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        tablaDocsVencidos = new javax.swing.JTable();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlEntregas = new javax.swing.JPanel();
        lblValorTotalFactura = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProductosPorFactura = new javax.swing.JTable();
        pnlMovimientos = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblBitacora = new javax.swing.JTable();
        btnDescargarRechazoTotal2 = new javax.swing.JButton();
        pnlDistribucion = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblMovimientos = new javax.swing.JTable();
        pnlRechazosParciales = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblProductosPorFacturaRechazados = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblDescuentos = new javax.swing.JTable();
        pnlRecogidas = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblRecogidas = new javax.swing.JTable();
        btnDescargarRechazoTotal1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtVehiculo = new javax.swing.JTextField();
        txtConductor = new javax.swing.JTextField();
        txtNombreAuxiliar2 = new javax.swing.JTextField();
        txtNombreAuxiliar1 = new javax.swing.JTextField();
        txtNombreAuxiliar3 = new javax.swing.JTextField();
        txtNombreRuta = new javax.swing.JTextField();
        txtNombreCanal = new javax.swing.JTextField();
        txtCelularConductor = new javax.swing.JTextField();
        txtCelularAuxiliar1 = new javax.swing.JTextField();
        txtCelularAuxiliar3 = new javax.swing.JTextField();
        txtCelularAuxiliar2 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtNombreCiudad = new javax.swing.JTextField();
        txtNumeroManifiesto = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        lblCirculoDeProgreso3 = new javax.swing.JLabel();
        lblCirculoDeProgreso4 = new javax.swing.JLabel();
        lblCirculoDeProgreso5 = new javax.swing.JLabel();
        lblCirculoDeProgreso6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        txtNumeroFactura = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtNombreDelCliente = new javax.swing.JTextField();
        txtBarrioCliente = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtNombreDelVendedor = new javax.swing.JTextField();
        txtTelefonoDelCliente = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        txtFechaVenta = new javax.swing.JTextField();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtDireccionDelCliente = new javax.swing.JTextField();
        txtCelularCliente = new javax.swing.JTextField();
        txtCelularVendedor = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txtValorFactura = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        txtPesoFactura = new javax.swing.JTextField();
        lblCirculoDeProgreso1 = new javax.swing.JLabel();
        lblCirculoDeProgreso2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jToolBar5 = new javax.swing.JToolBar();
        btnHabilitarGestion1 = new javax.swing.JButton();
        btnCancelGestion1 = new javax.swing.JButton();
        btnGrabarGestion1 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        cbxTipoMovimiento = new javax.swing.JComboBox<>();
        cbxCausalesDeDevolucion = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtObservacion = new javax.swing.JTextArea(3,20);
        jLabel6 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cbxTiposDePeticion = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        txtNumeroFactura1 = new javax.swing.JTextField();
        btnGrabarIncidencia = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblProductosPorFacturaParciales = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtGestion = new javax.swing.JTextArea(3,20);
        cbxTiposGestiones = new javax.swing.JComboBox<>();
        jToolBar1 = new javax.swing.JToolBar();
        btnHabilitarGestion = new javax.swing.JButton();
        btnCancelGestion = new javax.swing.JButton();
        btnGrabarGestion = new javax.swing.JButton();
        btnAgregarGestion = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        txtNombreResponsable = new javax.swing.JTextField();
        btnCambioResponsable = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        btnHabilitarCambioResponsabel = new javax.swing.JButton();
        btnCancelResponsable = new javax.swing.JButton();
        btnGrabarResponsable = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btnCierreDeIncidencia = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtCierreDeIncidencia = new javax.swing.JTextArea(3,20);
        jToolBar3 = new javax.swing.JToolBar();
        btnHabilitarCierre = new javax.swing.JButton();
        btnCancelCierre = new javax.swing.JButton();
        btnGrabarCierre = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        rdbtnNoSalvada = new javax.swing.JRadioButton();
        rdbtnSalvada = new javax.swing.JRadioButton();
        jPanel12 = new javax.swing.JPanel();
        rdBtnAnular = new javax.swing.JRadioButton();
        rdBtnReEnvio = new javax.swing.JRadioButton();
        rdBtnEntregado = new javax.swing.JRadioButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblGestiones = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        JLabel_ME_Imagen = new javax.swing.JLabel();
        jToolBar4 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();

        tablaDocsVencidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "NOMBRES DOCUMENTO", "FECHA EXP.", "FECHA VENC.", "ARCHIVO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaDocsVencidos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tablaDocsVencidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDocsVencidosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablaDocsVencidos);
        if (tablaDocsVencidos.getColumnModel().getColumnCount() > 0) {
            tablaDocsVencidos.getColumnModel().getColumn(0).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(1).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(1).setPreferredWidth(250);
            tablaDocsVencidos.getColumnModel().getColumn(2).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaDocsVencidos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaDocsVencidos.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario de Gestion de Incidencias");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
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

        jPanel5.setAutoscrolls(true);

        pnlEntregas.setAutoscrolls(true);
        pnlEntregas.setDoubleBuffered(false);
        pnlEntregas.setEnabled(false);

        lblValorTotalFactura.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblValorTotalFactura.setText("$.");

        tblProductosPorFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Ref", "Producto", "cantidad", "V. Unit", "Valor Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductosPorFactura.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProductosPorFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosPorFacturaMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblProductosPorFactura);
        if (tblProductosPorFactura.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFactura.getColumnModel().getColumn(1).setPreferredWidth(120);
            tblProductosPorFactura.getColumnModel().getColumn(2).setPreferredWidth(250);
            tblProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(90);
            tblProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(120);
            tblProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(180);
        }

        javax.swing.GroupLayout pnlEntregasLayout = new javax.swing.GroupLayout(pnlEntregas);
        pnlEntregas.setLayout(pnlEntregasLayout);
        pnlEntregasLayout.setHorizontalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblValorTotalFactura, javax.swing.GroupLayout.DEFAULT_SIZE, 1089, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 847, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnlEntregasLayout.setVerticalGroup(
            pnlEntregasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEntregasLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValorTotalFactura)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Productos", pnlEntregas);

        tblBitacora.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "item", "fecha", "documento", "causal-obs.", "valor Descto."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBitacora.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblBitacora.setColumnSelectionAllowed(true);
        tblBitacora.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBitacoraMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblBitacora);
        tblBitacora.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblBitacora.getColumnModel().getColumnCount() > 0) {
            tblBitacora.getColumnModel().getColumn(0).setMinWidth(50);
            tblBitacora.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblBitacora.getColumnModel().getColumn(0).setMaxWidth(50);
            tblBitacora.getColumnModel().getColumn(1).setMinWidth(100);
            tblBitacora.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblBitacora.getColumnModel().getColumn(1).setMaxWidth(100);
            tblBitacora.getColumnModel().getColumn(2).setMinWidth(100);
            tblBitacora.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblBitacora.getColumnModel().getColumn(2).setMaxWidth(100);
            tblBitacora.getColumnModel().getColumn(3).setPreferredWidth(700);
            tblBitacora.getColumnModel().getColumn(4).setMinWidth(50);
            tblBitacora.getColumnModel().getColumn(4).setPreferredWidth(50);
            tblBitacora.getColumnModel().getColumn(4).setMaxWidth(50);
        }

        btnDescargarRechazoTotal2.setText("Descargar");
        btnDescargarRechazoTotal2.setEnabled(false);
        btnDescargarRechazoTotal2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarRechazoTotal2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMovimientosLayout = new javax.swing.GroupLayout(pnlMovimientos);
        pnlMovimientos.setLayout(pnlMovimientosLayout);
        pnlMovimientosLayout.setHorizontalGroup(
            pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMovimientosLayout.createSequentialGroup()
                .addGroup(pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMovimientosLayout.createSequentialGroup()
                        .addGap(0, 1039, Short.MAX_VALUE)
                        .addComponent(btnDescargarRechazoTotal2))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1007, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnlMovimientosLayout.setVerticalGroup(
            pnlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMovimientosLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDescargarRechazoTotal2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Movimientos", pnlMovimientos);

        pnlDistribucion.setAutoscrolls(true);
        pnlDistribucion.setDoubleBuffered(false);
        pnlDistribucion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblMovimientos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Mfto", "Fecha Dist", "Vehículo", "Conductor", "Ruta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMovimientos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblMovimientos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMovimientosMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblMovimientos);
        if (tblMovimientos.getColumnModel().getColumnCount() > 0) {
            tblMovimientos.getColumnModel().getColumn(0).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblMovimientos.getColumnModel().getColumn(1).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblMovimientos.getColumnModel().getColumn(2).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblMovimientos.getColumnModel().getColumn(3).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblMovimientos.getColumnModel().getColumn(4).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(4).setPreferredWidth(300);
            tblMovimientos.getColumnModel().getColumn(5).setResizable(false);
            tblMovimientos.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        pnlDistribucion.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 200));

        jTabbedPane1.addTab("Distribución", pnlDistribucion);

        pnlRechazosParciales.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pnlRechazosParciales.setEnabled(false);

        tblProductosPorFacturaRechazados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Ref", "Producto", "cantidad", "Causal de Rechazo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductosPorFacturaRechazados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProductosPorFacturaRechazados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosPorFacturaRechazadosMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblProductosPorFacturaRechazados);
        if (tblProductosPorFacturaRechazados.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(0).setResizable(false);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(1).setResizable(false);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(2).setResizable(false);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(3).setResizable(false);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(4).setResizable(false);
            tblProductosPorFacturaRechazados.getColumnModel().getColumn(4).setPreferredWidth(125);
        }

        javax.swing.GroupLayout pnlRechazosParcialesLayout = new javax.swing.GroupLayout(pnlRechazosParciales);
        pnlRechazosParciales.setLayout(pnlRechazosParcialesLayout);
        pnlRechazosParcialesLayout.setHorizontalGroup(
            pnlRechazosParcialesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRechazosParcialesLayout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 911, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 198, Short.MAX_VALUE))
        );
        pnlRechazosParcialesLayout.setVerticalGroup(
            pnlRechazosParcialesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Productos Devueltos", pnlRechazosParciales);

        tblDescuentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Ref", "Producto", "cantidad", "Causal de Rechazo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDescuentos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblDescuentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDescuentosMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tblDescuentos);
        if (tblDescuentos.getColumnModel().getColumnCount() > 0) {
            tblDescuentos.getColumnModel().getColumn(0).setResizable(false);
            tblDescuentos.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblDescuentos.getColumnModel().getColumn(1).setResizable(false);
            tblDescuentos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblDescuentos.getColumnModel().getColumn(2).setResizable(false);
            tblDescuentos.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblDescuentos.getColumnModel().getColumn(3).setResizable(false);
            tblDescuentos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblDescuentos.getColumnModel().getColumn(4).setResizable(false);
            tblDescuentos.getColumnModel().getColumn(4).setPreferredWidth(125);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(459, 459, 459))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Descuentos", jPanel3);

        tblRecogidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Soporte", "Fact. Rec.", "Cliente.", "valor Descto."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRecogidas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblRecogidas.setColumnSelectionAllowed(true);
        tblRecogidas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRecogidasMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblRecogidas);

        btnDescargarRechazoTotal1.setText("Descargar");
        btnDescargarRechazoTotal1.setEnabled(false);
        btnDescargarRechazoTotal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarRechazoTotal1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRecogidasLayout = new javax.swing.GroupLayout(pnlRecogidas);
        pnlRecogidas.setLayout(pnlRecogidasLayout);
        pnlRecogidasLayout.setHorizontalGroup(
            pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRecogidasLayout.createSequentialGroup()
                .addGroup(pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRecogidasLayout.createSequentialGroup()
                        .addGap(0, 1039, Short.MAX_VALUE)
                        .addComponent(btnDescargarRechazoTotal1))
                    .addGroup(pnlRecogidasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlRecogidasLayout.setVerticalGroup(
            pnlRecogidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRecogidasLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDescargarRechazoTotal1)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Recogidas", pnlRecogidas);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de la Factura", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Vehiculo");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Conductor");

        jLabel5.setText("Aux  1");

        jLabel7.setText("Aux 2");

        jLabel8.setText("Aux 3");

        jLabel10.setText("Ruta");

        jLabel11.setText("Canal");

        txtVehiculo.setEditable(false);
        txtVehiculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVehiculoActionPerformed(evt);
            }
        });

        txtConductor.setEditable(false);

        txtNombreAuxiliar2.setEditable(false);

        txtNombreAuxiliar1.setEditable(false);

        txtNombreAuxiliar3.setEditable(false);
        txtNombreAuxiliar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreAuxiliar3ActionPerformed(evt);
            }
        });

        txtNombreRuta.setEditable(false);
        txtNombreRuta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreRutaActionPerformed(evt);
            }
        });

        txtNombreCanal.setEditable(false);

        txtCelularConductor.setEditable(false);

        txtCelularAuxiliar1.setEditable(false);

        txtCelularAuxiliar3.setEditable(false);

        txtCelularAuxiliar2.setEditable(false);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Ciudad");

        txtNombreCiudad.setEditable(false);

        txtNumeroManifiesto.setEditable(false);
        txtNumeroManifiesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroManifiestoActionPerformed(evt);
            }
        });

        jLabel13.setText("Manifiesto");

        lblCirculoDeProgreso3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Mobile-phone.png"))); // NOI18N
        lblCirculoDeProgreso3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lblCirculoDeProgreso4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Mobile-phone.png"))); // NOI18N

        lblCirculoDeProgreso5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Mobile-phone.png"))); // NOI18N

        lblCirculoDeProgreso6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Mobile-phone.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtNombreAuxiliar2, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                    .addComponent(txtNombreAuxiliar1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                    .addComponent(txtVehiculo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreCanal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreCiudad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtConductor, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                    .addComponent(txtNombreAuxiliar3, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                    .addComponent(txtNombreRuta, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                    .addComponent(txtNumeroManifiesto))
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblCirculoDeProgreso3)
                        .addComponent(lblCirculoDeProgreso4)
                        .addComponent(lblCirculoDeProgreso5))
                    .addComponent(lblCirculoDeProgreso6))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtCelularAuxiliar2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(txtCelularAuxiliar1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCelularConductor)
                    .addComponent(txtCelularAuxiliar3))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtVehiculo))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCirculoDeProgreso3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtConductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCelularConductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNombreAuxiliar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCelularAuxiliar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCirculoDeProgreso4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtNombreAuxiliar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCelularAuxiliar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCirculoDeProgreso5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreAuxiliar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCelularAuxiliar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblCirculoDeProgreso6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreCanal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroManifiesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Formulario de consulta de Manifiesto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N

        txtNumeroFactura.setEditable(false);
        txtNumeroFactura.setName(""); // NOI18N
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

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Cliente");

        txtNombreDelCliente.setEditable(false);
        txtNombreDelCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDelClienteFocusGained(evt);
            }
        });
        txtNombreDelCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDelClienteActionPerformed(evt);
            }
        });
        txtNombreDelCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDelClienteKeyPressed(evt);
            }
        });

        txtBarrioCliente.setEditable(false);
        txtBarrioCliente.setName("numerico"); // NOI18N
        txtBarrioCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarrioClienteFocusGained(evt);
            }
        });
        txtBarrioCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarrioClienteActionPerformed(evt);
            }
        });
        txtBarrioCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarrioClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBarrioClienteKeyReleased(evt);
            }
        });

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Factura #");

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Barrio");

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Teléfono");

        txtNombreDelVendedor.setEditable(false);
        txtNombreDelVendedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDelVendedorFocusGained(evt);
            }
        });
        txtNombreDelVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDelVendedorActionPerformed(evt);
            }
        });
        txtNombreDelVendedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDelVendedorKeyPressed(evt);
            }
        });

        txtTelefonoDelCliente.setEditable(false);
        txtTelefonoDelCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoDelClienteFocusGained(evt);
            }
        });
        txtTelefonoDelCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoDelClienteActionPerformed(evt);
            }
        });
        txtTelefonoDelCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoDelClienteKeyPressed(evt);
            }
        });

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("Vendedor");

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Fecha Venta");

        txtFechaVenta.setEditable(false);
        txtFechaVenta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFechaVentaFocusGained(evt);
            }
        });
        txtFechaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaVentaActionPerformed(evt);
            }
        });
        txtFechaVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFechaVentaKeyPressed(evt);
            }
        });

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Dirección");

        txtDireccionDelCliente.setEditable(false);
        txtDireccionDelCliente.setName("numerico"); // NOI18N
        txtDireccionDelCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDireccionDelClienteFocusGained(evt);
            }
        });
        txtDireccionDelCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionDelClienteActionPerformed(evt);
            }
        });
        txtDireccionDelCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionDelClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDireccionDelClienteKeyReleased(evt);
            }
        });

        txtCelularCliente.setEditable(false);
        txtCelularCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCelularClienteActionPerformed(evt);
            }
        });

        txtCelularVendedor.setEditable(false);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Valor Fact.");

        txtValorFactura.setEditable(false);
        txtValorFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorFacturaFocusGained(evt);
            }
        });
        txtValorFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorFacturaActionPerformed(evt);
            }
        });
        txtValorFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorFacturaKeyPressed(evt);
            }
        });

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Peso Fact");

        txtPesoFactura.setEditable(false);
        txtPesoFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPesoFacturaFocusGained(evt);
            }
        });
        txtPesoFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesoFacturaActionPerformed(evt);
            }
        });
        txtPesoFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesoFacturaKeyPressed(evt);
            }
        });

        lblCirculoDeProgreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Mobile-phone.png"))); // NOI18N

        lblCirculoDeProgreso2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Mobile-phone.png"))); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(lblCirculoDeProgreso))
                    .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefonoDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPesoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccionDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBarrioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDelVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCirculoDeProgreso1)
                    .addComponent(lblCirculoDeProgreso2))
                .addGap(3, 3, 3)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCelularCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCelularVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNumeroFactura)))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(txtNombreDelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCelularCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCirculoDeProgreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccionDelCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBarrioCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefonoDelCliente))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNombreDelVendedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCelularVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCirculoDeProgreso2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValorFactura)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesoFactura)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 264, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        jTabbedPane2.addTab("Datos de la Factura", jPanel5);

        jToolBar5.setRollover(true);

        btnHabilitarGestion1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        btnHabilitarGestion1.setToolTipText("Agregar Gestion");
        btnHabilitarGestion1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnHabilitarGestion1.setEnabled(false);
        btnHabilitarGestion1.setFocusable(false);
        btnHabilitarGestion1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHabilitarGestion1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHabilitarGestion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHabilitarGestion1ActionPerformed(evt);
            }
        });
        jToolBar5.add(btnHabilitarGestion1);

        btnCancelGestion1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        btnCancelGestion1.setToolTipText("");
        btnCancelGestion1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelGestion1.setEnabled(false);
        btnCancelGestion1.setFocusable(false);
        btnCancelGestion1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelGestion1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelGestion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelGestion1ActionPerformed(evt);
            }
        });
        jToolBar5.add(btnCancelGestion1);

        btnGrabarGestion1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        btnGrabarGestion1.setToolTipText("");
        btnGrabarGestion1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGrabarGestion1.setEnabled(false);
        btnGrabarGestion1.setFocusable(false);
        btnGrabarGestion1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabarGestion1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabarGestion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarGestion1ActionPerformed(evt);
            }
        });
        jToolBar5.add(btnGrabarGestion1);

        cbxTipoMovimiento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxTipoMovimiento.setEnabled(false);

        cbxCausalesDeDevolucion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxCausalesDeDevolucion.setEnabled(false);

        jLabel3.setText("Movimiento Factura");

        jLabel4.setText("Causal de Devolucion");

        txtObservacion.setColumns(20);
        txtObservacion.setRows(5);
        txtObservacion.setEnabled(false);
        jScrollPane3.setViewportView(txtObservacion);
        txtCierreDeIncidencia.setLineWrap(true);
        txtCierreDeIncidencia.setWrapStyleWord(true);

        jLabel6.setText("Observaciones");

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 123, Short.MAX_VALUE)
        );

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Tipo De Peticion");

        cbxTiposDePeticion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxTiposDePeticion.setEnabled(false);
        cbxTiposDePeticion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTiposDePeticionActionPerformed(evt);
            }
        });

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Factura #");

        txtNumeroFactura1.setEditable(false);
        txtNumeroFactura1.setName(""); // NOI18N
        txtNumeroFactura1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroFactura1FocusGained(evt);
            }
        });
        txtNumeroFactura1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroFactura1ActionPerformed(evt);
            }
        });
        txtNumeroFactura1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroFactura1KeyPressed(evt);
            }
        });

        btnGrabarIncidencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        btnGrabarIncidencia.setText("Grabar");
        btnGrabarIncidencia.setToolTipText("Guardar registro nuevo ó modificado");
        btnGrabarIncidencia.setEnabled(false);
        btnGrabarIncidencia.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabarIncidencia.setPreferredSize(new java.awt.Dimension(97, 97));
        btnGrabarIncidencia.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabarIncidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarIncidenciaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNumeroFactura1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxTipoMovimiento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbxCausalesDeDevolucion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbxTiposDePeticion, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(btnGrabarIncidencia, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(180, 180, 180))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroFactura1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxTipoMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbxCausalesDeDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(cbxTiposDePeticion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGrabarIncidencia, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(182, 182, 182)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 735, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 308, Short.MAX_VALUE)
        );

        tblProductosPorFacturaParciales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "#", "Ref", "Producto", "Cant.", "Vu sin Iva", " % Dto", "# Rech.", "Valor Total", "Causal de Rechazo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductosPorFacturaParciales.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProductosPorFacturaParciales.getTableHeader().setReorderingAllowed(false);
        tblProductosPorFacturaParciales.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblProductosPorFacturaParcialesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblProductosPorFacturaParcialesFocusLost(evt);
            }
        });
        tblProductosPorFacturaParciales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosPorFacturaParcialesMouseClicked(evt);
            }
        });
        tblProductosPorFacturaParciales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblProductosPorFacturaParcialesKeyPressed(evt);
            }
        });
        jScrollPane11.setViewportView(tblProductosPorFacturaParciales);
        if (tblProductosPorFacturaParciales.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFacturaParciales.getColumnModel().getColumn(3).setPreferredWidth(250);
            tblProductosPorFacturaParciales.getColumnModel().getColumn(9).setPreferredWidth(250);
        }

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 1118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(198, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTabbedPane2.addTab("Crear Incidencia", jPanel1);

        txtGestion.setColumns(20);
        txtGestion.setRows(5);
        txtGestion.setEnabled(false);
        jScrollPane1.setViewportView(txtGestion);
        txtGestion.setLineWrap(true);
        txtGestion.setWrapStyleWord(true);

        cbxTiposGestiones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxTiposGestiones.setEnabled(false);

        jToolBar1.setRollover(true);

        btnHabilitarGestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        btnHabilitarGestion.setToolTipText("Agregar Gestion");
        btnHabilitarGestion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnHabilitarGestion.setEnabled(false);
        btnHabilitarGestion.setFocusable(false);
        btnHabilitarGestion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHabilitarGestion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHabilitarGestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHabilitarGestionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnHabilitarGestion);

        btnCancelGestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        btnCancelGestion.setToolTipText("");
        btnCancelGestion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelGestion.setEnabled(false);
        btnCancelGestion.setFocusable(false);
        btnCancelGestion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelGestion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelGestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelGestionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCancelGestion);

        btnGrabarGestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        btnGrabarGestion.setToolTipText("");
        btnGrabarGestion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGrabarGestion.setEnabled(false);
        btnGrabarGestion.setFocusable(false);
        btnGrabarGestion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabarGestion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabarGestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarGestionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGrabarGestion);

        btnAgregarGestion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Yes.png"))); // NOI18N
        btnAgregarGestion.setEnabled(false);
        btnAgregarGestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarGestionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(cbxTiposGestiones, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregarGestion)
                .addGap(397, 397, 397))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(cbxTiposGestiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnAgregarGestion, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Registro novedades", jPanel2);

        txtNombreResponsable.setEditable(false);
        txtNombreResponsable.setEnabled(false);
        txtNombreResponsable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreResponsableActionPerformed(evt);
            }
        });
        txtNombreResponsable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreResponsableKeyPressed(evt);
            }
        });

        btnCambioResponsable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Yes.png"))); // NOI18N
        btnCambioResponsable.setEnabled(false);
        btnCambioResponsable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambioResponsableActionPerformed(evt);
            }
        });

        jToolBar2.setRollover(true);

        btnHabilitarCambioResponsabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        btnHabilitarCambioResponsabel.setToolTipText("Agregar Gestion");
        btnHabilitarCambioResponsabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnHabilitarCambioResponsabel.setEnabled(false);
        btnHabilitarCambioResponsabel.setFocusable(false);
        btnHabilitarCambioResponsabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHabilitarCambioResponsabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHabilitarCambioResponsabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHabilitarCambioResponsabelActionPerformed(evt);
            }
        });
        jToolBar2.add(btnHabilitarCambioResponsabel);

        btnCancelResponsable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        btnCancelResponsable.setToolTipText("");
        btnCancelResponsable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelResponsable.setEnabled(false);
        btnCancelResponsable.setFocusable(false);
        btnCancelResponsable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelResponsable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelResponsable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelResponsableActionPerformed(evt);
            }
        });
        jToolBar2.add(btnCancelResponsable);

        btnGrabarResponsable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        btnGrabarResponsable.setToolTipText("");
        btnGrabarResponsable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGrabarResponsable.setEnabled(false);
        btnGrabarResponsable.setFocusable(false);
        btnGrabarResponsable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabarResponsable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabarResponsable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarResponsableActionPerformed(evt);
            }
        });
        jToolBar2.add(btnGrabarResponsable);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(txtNombreResponsable, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCambioResponsable)
                .addContainerGap(681, Short.MAX_VALUE))
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreResponsable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCambioResponsable, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Cambiar Responsable", jPanel11);

        btnCierreDeIncidencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Yes.png"))); // NOI18N
        btnCierreDeIncidencia.setEnabled(false);
        btnCierreDeIncidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCierreDeIncidenciaActionPerformed(evt);
            }
        });

        txtCierreDeIncidencia.setColumns(20);
        txtCierreDeIncidencia.setRows(5);
        txtCierreDeIncidencia.setEnabled(false);
        jScrollPane2.setViewportView(txtCierreDeIncidencia);
        txtCierreDeIncidencia.setLineWrap(true);
        txtCierreDeIncidencia.setWrapStyleWord(true);

        jToolBar3.setRollover(true);

        btnHabilitarCierre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        btnHabilitarCierre.setToolTipText("Agregar Gestion");
        btnHabilitarCierre.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnHabilitarCierre.setEnabled(false);
        btnHabilitarCierre.setFocusable(false);
        btnHabilitarCierre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHabilitarCierre.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHabilitarCierre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHabilitarCierreActionPerformed(evt);
            }
        });
        jToolBar3.add(btnHabilitarCierre);

        btnCancelCierre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        btnCancelCierre.setToolTipText("");
        btnCancelCierre.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelCierre.setEnabled(false);
        btnCancelCierre.setFocusable(false);
        btnCancelCierre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelCierre.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelCierre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelCierreActionPerformed(evt);
            }
        });
        jToolBar3.add(btnCancelCierre);

        btnGrabarCierre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        btnGrabarCierre.setToolTipText("");
        btnGrabarCierre.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGrabarCierre.setEnabled(false);
        btnGrabarCierre.setFocusable(false);
        btnGrabarCierre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabarCierre.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabarCierre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarCierreActionPerformed(evt);
            }
        });
        jToolBar3.add(btnGrabarCierre);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Se pudo salvar la Incidencia?"));

        buttonGroup1.add(rdbtnNoSalvada);
        rdbtnNoSalvada.setText("Incidencia No Salvada");
        rdbtnNoSalvada.setEnabled(false);

        buttonGroup1.add(rdbtnSalvada);
        rdbtnSalvada.setText("Incidencia Salvada");
        rdbtnSalvada.setEnabled(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdbtnSalvada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                    .addComponent(rdbtnNoSalvada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(rdbtnSalvada)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rdbtnNoSalvada)
                .addContainerGap())
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Accion a tomar con la Incidencia"));

        buttonGroup2.add(rdBtnAnular);
        rdBtnAnular.setText("Factura para anular");
        rdBtnAnular.setEnabled(false);
        rdBtnAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdBtnAnularActionPerformed(evt);
            }
        });

        buttonGroup2.add(rdBtnReEnvio);
        rdBtnReEnvio.setText("Factura para Re Envio");
        rdBtnReEnvio.setEnabled(false);

        buttonGroup2.add(rdBtnEntregado);
        rdBtnEntregado.setText("Pedido Entregado");
        rdBtnEntregado.setEnabled(false);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rdBtnAnular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rdBtnReEnvio, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addComponent(rdBtnEntregado, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                .addGap(122, 122, 122))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(rdBtnAnular)
                .addGap(1, 1, 1)
                .addComponent(rdBtnReEnvio)
                .addGap(1, 1, 1)
                .addComponent(rdBtnEntregado)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCierreDeIncidencia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(243, 243, 243))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCierreDeIncidencia, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(300, 300, 300))
        );

        jTabbedPane3.addTab("Cierre de Incidencia", jPanel7);

        tblGestiones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "item", "incidencia", "Solicitud", "Respuesta", "Hora"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGestiones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblGestiones.setColumnSelectionAllowed(true);
        tblGestiones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGestionesMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblGestiones);
        tblGestiones.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblGestiones.getColumnModel().getColumnCount() > 0) {
            tblGestiones.getColumnModel().getColumn(0).setMinWidth(50);
            tblGestiones.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblGestiones.getColumnModel().getColumn(0).setMaxWidth(50);
            tblGestiones.getColumnModel().getColumn(1).setMinWidth(80);
            tblGestiones.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblGestiones.getColumnModel().getColumn(1).setMaxWidth(80);
            tblGestiones.getColumnModel().getColumn(2).setPreferredWidth(180);
            tblGestiones.getColumnModel().getColumn(3).setPreferredWidth(700);
            tblGestiones.getColumnModel().getColumn(4).setPreferredWidth(150);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane12)
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1163, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Procedimientos", jPanel4);

        JLabel_ME_Imagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabel_ME_Imagen.setText("Mapa estático");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1906, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(JLabel_ME_Imagen, javax.swing.GroupLayout.DEFAULT_SIZE, 1906, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 549, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addComponent(JLabel_ME_Imagen, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 64, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("datos del Cliente GPS", jPanel10);

        jToolBar4.setRollover(true);

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
        jToolBar4.add(jBtnNuevo);

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
        jToolBar4.add(jBtnGrabar);

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
        jToolBar4.add(jBtnImprimir);

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
        jToolBar4.add(jBtnCancel);

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
        jToolBar4.add(jBtnMinuta);

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
        jToolBar4.add(jBtnExit);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 1164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void tablaDocsVencidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsVencidosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDocsVencidosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
//        if (manifiestoModificadoPorMi != null) {
//            manifiestoModificadoPorMi.setIsFree(1);
//        }
        if (this.form1 != null) {
            this.form1.fGestion = false;
        }


    }//GEN-LAST:event_formInternalFrameClosing

    private void btnDescargarRechazoTotal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarRechazoTotal1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDescargarRechazoTotal1ActionPerformed

    private void tblMovimientosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMovimientosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblMovimientosMouseClicked

    private void txtFechaVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaVentaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaVentaKeyPressed

    private void txtFechaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaVentaActionPerformed

    private void txtFechaVentaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFechaVentaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaVentaFocusGained

    private void txtTelefonoDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteKeyPressed

    private void txtTelefonoDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteActionPerformed

    private void txtTelefonoDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDelClienteFocusGained

    private void txtNombreDelVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDelVendedorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelVendedorKeyPressed

    private void txtNombreDelVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDelVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelVendedorActionPerformed

    private void txtNombreDelVendedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDelVendedorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelVendedorFocusGained

    private void txtBarrioClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioClienteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioClienteKeyReleased

    private void txtBarrioClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioClienteKeyPressed

    }//GEN-LAST:event_txtBarrioClienteKeyPressed

    private void txtBarrioClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarrioClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioClienteActionPerformed

    private void txtBarrioClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarrioClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBarrioClienteFocusGained

    private void txtNombreDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDelClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelClienteKeyPressed

    private void txtNombreDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelClienteActionPerformed

    private void txtNombreDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDelClienteFocusGained

    private void txtNumeroFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroFacturaKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
          cancelar();
          consultarFactura();

        }
    }//GEN-LAST:event_txtNumeroFacturaKeyPressed


    private void txtNumeroFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFacturaActionPerformed

    private void txtNumeroFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroFacturaFocusGained
        txtNumeroFactura.setSelectionStart(0);
        txtNumeroFactura.setSelectionEnd(txtNumeroFactura.getText().length());
    }//GEN-LAST:event_txtNumeroFacturaFocusGained

    private void tblBitacoraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBitacoraMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBitacoraMouseClicked

    private void btnDescargarRechazoTotal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarRechazoTotal2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDescargarRechazoTotal2ActionPerformed

    private void txtDireccionDelClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionDelClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelClienteFocusGained

    private void txtDireccionDelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionDelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelClienteActionPerformed

    private void txtDireccionDelClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionDelClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelClienteKeyPressed

    private void txtDireccionDelClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionDelClienteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDelClienteKeyReleased

    private void tblProductosPorFacturaRechazadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaRechazadosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaRechazadosMouseClicked

    private void tblDescuentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDescuentosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDescuentosMouseClicked

    private void tblRecogidasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRecogidasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblRecogidasMouseClicked

    private void tblGestionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGestionesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGestionesMouseClicked

    private void txtVehiculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVehiculoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVehiculoActionPerformed

    private void txtNombreAuxiliar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar3ActionPerformed

    private void tblProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaMouseClicked

    private void txtCelularClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCelularClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCelularClienteActionPerformed

    private void btnAgregarGestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarGestionActionPerformed
        if (grabarGestiondeIncidencia()) {
            return;
        }
    }//GEN-LAST:event_btnAgregarGestionActionPerformed

    private boolean grabarGestiondeIncidencia() throws HeadlessException {
        int deseaGrabarRegistro;
        if (txtGestion.getText().length() < 1) {
            JOptionPane.showMessageDialog(this, "Campo de texto no puede ser vacio", "Error", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro?", "Guardar registro", JOptionPane.YES_NO_OPTION);
        if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {
            gestion.setActivo(1);
            gestion.setConsecutivo(incidenciaSvc.getConsecutivo());
            for (CtiposGestiones g : ini.getListaDeTiposgestiones()) {
                if (g.getNombreTipoDeGestion().equals(cbxTiposGestiones.getSelectedItem().toString())) {
                    gestion.setIdGestion(g.getIdtiposDeGestion());
                    break;
                }
            }

            gestion.setFlag(-1);
            gestion.setRespuesta(cbxTiposGestiones.getSelectedItem().toString() + ": " + txtGestion.getText().trim());
            SimpleDateFormat parseador = new SimpleDateFormat("yyy-MM-dd");
// el que formatea
            SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            Date date = new Date();

            System.out.println(formateador.format(date));
            gestion.setFechaIng(new Date());

            for (CtiposGestiones obj : ini.getListaDeTiposgestione()) {
                if (cbxTiposGestiones.getSelectedItem().toString().equals(obj.getNombreTipoDeGestion())) {
                    gestion.setIdGestion(obj.getIdtiposDeGestion());
                    break;
                }
            }

            if (gestion.grabarGestion()) {
                modelo2 = (DefaultTableModel) tblGestiones.getModel();

                filaSelleccionada = tblGestiones.getRowCount();

                modelo2.addRow(new Object[tblGestiones.getRowCount()]);
                tblGestiones.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);  // item
                tblGestiones.setValueAt(incidenciaSvc.getConsecutivo(), filaSelleccionada, 1); // numero de facturaActual
                tblGestiones.setValueAt(cbxTiposGestiones.getSelectedItem().toString(), filaSelleccionada, 2); // numero de facturaActual
                tblGestiones.setValueAt(gestion.getRespuesta(), filaSelleccionada, 3); // numero de facturaActual
                tblGestiones.setValueAt(formateador.format(date), filaSelleccionada, 4); // numero de facturaActual

                incidenciaSvc.getListadoDeGestiones().add(gestion);

                /*Se anexa registro a la tabla de movimientos de la faactura */
                modelo1 = (DefaultTableModel) tblBitacora.getModel();
                filaSelleccionada = tblBitacora.getRowCount();
                modelo1.addRow(new Object[tblBitacora.getRowCount()]);
                tblBitacora.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);
                tblBitacora.setValueAt(formateador.format(date), filaSelleccionada, 1);
                tblBitacora.setValueAt(incidenciaSvc.getObjManifiesto().getNumeroManifiesto(), filaSelleccionada, 2);
                tblBitacora.setValueAt(gestion.getRespuesta(), filaSelleccionada, 3);

                JOptionPane.showMessageDialog(this, "Dato guardado con exito", "Dato guardado", JOptionPane.INFORMATION_MESSAGE);

                cbxTiposGestiones.setEnabled(false);
                txtGestion.setEnabled(false);
                txtGestion.setText("");
                btnAgregarGestion.setEnabled(false);
                btnHabilitarGestion.setEnabled(true);

            }
        }
        return false;
    }

    private void btnHabilitarGestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHabilitarGestionActionPerformed

        btnHabilitarGestion.setEnabled(false);
        txtGestion.setText("");
        cbxTiposGestiones.setEnabled(true);

        if (true) {
            //  if (incidenciaSvc.getIdEstado().equals("1")) {
            gestion = new CGestiones(ini);
            gestion.setActivo(1);
            gestion.setConsecutivo(incidenciaSvc.getConsecutivo());
            gestion.setFlag(-1);
            gestion.setRespuesta("");
            gestion.setGestion("");
            cbxTiposGestiones.setEnabled(true);
            txtGestion.setEnabled(true);
            btnAgregarGestion.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Incidencia se encuentra Cerrada", "Incidencia Cerrada", JOptionPane.INFORMATION_MESSAGE);
        }


    }//GEN-LAST:event_btnHabilitarGestionActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
      this.form1.fGestion = false; 
      
    }//GEN-LAST:event_formInternalFrameClosed

    private void txtNombreResponsableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreResponsableActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreResponsableActionPerformed

    private void txtNombreResponsableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreResponsableKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            //if (manifiestoActual.getEstadoManifiesto() < 2) {
            FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(ini, this);
            this.getParent().add(form);
            form.toFront();
            form.setClosable(true);
            form.setVisible(true);
            form.setTitle("Formulario para buscar Empleados por apellidos");
            form.txtApellidosPersona.requestFocus();
            form.show();
            //}
        }
    }//GEN-LAST:event_txtNombreResponsableKeyPressed

    private void btnCambioResponsableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambioResponsableActionPerformed
        if (grabarCambioDeresponsable()) {
            return;
        }

    }//GEN-LAST:event_btnCambioResponsableActionPerformed

    private boolean grabarCambioDeresponsable() throws HeadlessException {
        int deseaGrabarRegistro;
        if (txtNombreResponsable.getText().length() < 1) {
            JOptionPane.showMessageDialog(this, "Campo de texto no puede ser vacio", "Error", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea realizar el cambio de responsable ?", "Guardar registro", JOptionPane.YES_NO_OPTION);
        if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {

            /*Se genera una gestion y se graba en la tabla gestiones */
            gestion.setActivo(1);
            gestion.setConsecutivo(incidenciaSvc.getConsecutivo());
            gestion.setGestion("TRASLADO DE RESPONSABLE");
            gestion.setFlag(-1);
            gestion.setRespuesta("SE REALIZA TRASLADO DE LA INCIDENCIA A " + txtNombreResponsable.getText());
            SimpleDateFormat parseador = new SimpleDateFormat("yyy-MM-dd");

            SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            Date date = new Date();

            System.out.println(formateador.format(date));
            gestion.setFechaIng(new Date());
            gestion.setIdGestion(8);

            if (gestion.grabarGestion()) {
                modelo2 = (DefaultTableModel) tblGestiones.getModel();

                filaSelleccionada = tblGestiones.getRowCount();

                modelo2.addRow(new Object[tblGestiones.getRowCount()]);
                tblGestiones.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);  // item
                tblGestiones.setValueAt(incidenciaSvc.getConsecutivo(), filaSelleccionada, 1); // numero de facturaActual
                tblGestiones.setValueAt("TRASLADO DE RESPONSABLE ", filaSelleccionada, 2); // numero de facturaActual
                tblGestiones.setValueAt(gestion.getRespuesta(), filaSelleccionada, 3); // numero de facturaActual
                tblGestiones.setValueAt(formateador.format(date), filaSelleccionada, 4); // numero de facturaActual

                incidenciaSvc.getListadoDeGestiones().add(gestion);

                /*Se anexa registro a la tabla de movimientos de la faactura */
                modelo1 = (DefaultTableModel) tblBitacora.getModel();
                filaSelleccionada = tblBitacora.getRowCount();
                modelo1.addRow(new Object[tblBitacora.getRowCount()]);
                tblBitacora.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);
                tblBitacora.setValueAt(formateador.format(date), filaSelleccionada, 1);
                tblBitacora.setValueAt(incidenciaSvc.getObjManifiesto().getNumeroManifiesto(), filaSelleccionada, 2);
                tblBitacora.setValueAt(gestion.getRespuesta(), filaSelleccionada, 3);

                cbxTiposGestiones.setEnabled(false);
                txtGestion.setEnabled(false);
                txtGestion.setText("");
                btnAgregarGestion.setEnabled(false);
                btnHabilitarGestion.setEnabled(false);

            }

            if (this.incidenciaSvc.actualizarResponsable()) {
                /*Se actualiza la tabla de incidencias */
                this.form1.tablaIncidencias.setValueAt(incidenciaSvc.getResponsable(), form1.filaSeleccionada, 6);

                txtNombreResponsable.setText("");
                btnCambioResponsable.setEnabled(false);

                JOptionPane.showMessageDialog(this, " Registro actualizado con exito ", "Error", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el dato ", "Error", JOptionPane.ERROR_MESSAGE);
            };

        }
        return false;
    }

    private void btnHabilitarCambioResponsabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHabilitarCambioResponsabelActionPerformed

        if (incidenciaSvc.getIdEstado() == 1) {

            gestion = new CGestiones(ini);

            txtNombreResponsable.setEnabled(true);
            txtNombreResponsable.setEditable(false);
        } else {
            JOptionPane.showMessageDialog(this, "Incidencia se encuentra Cerrada", "Incidencia Cerrada", JOptionPane.INFORMATION_MESSAGE);
        }


    }//GEN-LAST:event_btnHabilitarCambioResponsabelActionPerformed

    private void btnCierreDeIncidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCierreDeIncidenciaActionPerformed
        if (grabarCierreDeIncidencia()) {
            return;
        }
    }//GEN-LAST:event_btnCierreDeIncidenciaActionPerformed

    private boolean grabarCierreDeIncidencia() throws HeadlessException {
        int deseaGrabarRegistro;
        if (txtCierreDeIncidencia.getText().length() < 1) {
            JOptionPane.showMessageDialog(this, "Campo de texto no puede ser vacio", "Error", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea realizar el cambio de responsable ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {
            /*Inserta registro en l tabla gestiones */
 /*Se genera una gestion y se graba en la tabla gestiones */
            gestion.setActivo(1);
            gestion.setConsecutivo(incidenciaSvc.getConsecutivo());

            gestion.setIdGestion(9);
            gestion.setFlag(-1);
            gestion.setRespuesta(txtCierreDeIncidencia.getText());
            SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            Date date = new Date();

            System.out.println(formateador.format(date));
            //gestion.setFechaIng(new Date());

            if (gestion.grabarGestion()) {

                /*se ingresa el registro a la tabla de gestiones*/
                modelo2 = (DefaultTableModel) tblGestiones.getModel();

                filaSelleccionada = tblGestiones.getRowCount();

                modelo2.addRow(new Object[tblGestiones.getRowCount()]);
                tblGestiones.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);  // item
                tblGestiones.setValueAt(incidenciaSvc.getConsecutivo(), filaSelleccionada, 1); // numero de facturaActual
                tblGestiones.setValueAt("CIERRE DE LA INCIDENCIA ", filaSelleccionada, 2); // numero de facturaActual
                tblGestiones.setValueAt(gestion.getRespuesta(), filaSelleccionada, 3); // numero de facturaActual
                tblGestiones.setValueAt(formateador.format(date), filaSelleccionada, 4); // numero de facturaActual

                incidenciaSvc.getListadoDeGestiones().add(gestion);

                /*Se anexa registro a la tabla de movimientos de la facturaActual */
                modelo1 = (DefaultTableModel) tblBitacora.getModel();
                filaSelleccionada = tblBitacora.getRowCount();
                modelo1.addRow(new Object[tblBitacora.getRowCount()]);
                tblBitacora.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);
                tblBitacora.setValueAt(formateador.format(date), filaSelleccionada, 1);
                tblBitacora.setValueAt(incidenciaSvc.getObjManifiesto().getNumeroManifiesto(), filaSelleccionada, 2);
                tblBitacora.setValueAt("CIERRE DE LA INCIDENCIA " + gestion.getRespuesta(), filaSelleccionada, 3);

                btnHabilitarGestion.setEnabled(false);
                cbxTiposGestiones.setEnabled(false);
                txtGestion.setEnabled(false);
                btnAgregarGestion.setEnabled(false);

                btnHabilitarCambioResponsabel.setEnabled(false);
                txtNombreResponsable.setEnabled(false);
                btnCambioResponsable.setEnabled(false);

                btnHabilitarCierre.setEnabled(false);
                txtCierreDeIncidencia.setEnabled(false);
                txtCierreDeIncidencia.setEditable(false);
                btnCierreDeIncidencia.setEnabled(false);

                /*actualiza el campo estado de la tabla svcIncidencia=2 y cierra la incidenciaSvc*/
                incidenciaSvc.cerrarIncidencia();

                /*Se actualiza el jtable de incidenciaSvc en el formulario ppal*/
                form1.tablaIncidencias.setValueAt("CERRADA", form1.filaSeleccionada, 3);
                for (CSvcIncidencias obj : form1.listaDeIncidencias) {
                    if (incidenciaSvc.getConsecutivo().equals(obj.getConsecutivo())) {
                        obj.setIdEstado(2);
                        break;
                    }
                }

                JOptionPane.showMessageDialog(this, " Incidencia cerrada con exito ", "Error", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al cerrar la inciedncia ", "Error", JOptionPane.ERROR_MESSAGE);
            };
        }
        return false;
    }

    private void btnHabilitarCierreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHabilitarCierreActionPerformed

        if (incidenciaSvc.getIdEstado() ==  1) {
            gestion = new CGestiones(ini);

            txtCierreDeIncidencia.setEnabled(true);
            txtCierreDeIncidencia.setEditable(true);
            btnCierreDeIncidencia.setEnabled(true);
            jPanel9.setEnabled(true);
            jPanel12.setEnabled(true);

        } else {
            JOptionPane.showMessageDialog(this, "Incidencia se encuentra Cerrada", "Incidencia Cerrada", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btnHabilitarCierreActionPerformed

    private void rdBtnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdBtnAnularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdBtnAnularActionPerformed

    private void btnGrabarCierreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarCierreActionPerformed
        if (grabarCierreDeIncidencia()) {
            return;
        }
    }//GEN-LAST:event_btnGrabarCierreActionPerformed

    private void btnGrabarResponsableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarResponsableActionPerformed
        if (grabarCambioDeresponsable()) {
            return;
        }
    }//GEN-LAST:event_btnGrabarResponsableActionPerformed

    private void btnGrabarGestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarGestionActionPerformed
        if (grabarGestiondeIncidencia()) {
            return;
        }
    }//GEN-LAST:event_btnGrabarGestionActionPerformed

    private void btnCancelCierreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelCierreActionPerformed

        rdbtnSalvada.setSelected(false);
        rdbtnNoSalvada.setSelected(false);
        rdBtnAnular.setSelected(false);
        rdBtnReEnvio.setSelected(false);
        rdBtnEntregado.setSelected(false);

        txtCierreDeIncidencia.setEnabled(false);
        btnCierreDeIncidencia.setEnabled(false);
        btnHabilitarCierre.setEnabled(true);
    }//GEN-LAST:event_btnCancelCierreActionPerformed

    private void btnCancelResponsableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelResponsableActionPerformed
        txtNombreResponsable.setEnabled(false);
        txtNombreResponsable.setText("");
        btnCambioResponsable.setEnabled(false);
        btnGrabarResponsable.setEnabled(false);
        btnHabilitarCambioResponsabel.setEnabled(true);
    }//GEN-LAST:event_btnCancelResponsableActionPerformed

    private void btnCancelGestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelGestionActionPerformed
        cbxTiposGestiones.setEnabled(false);
        txtGestion.setEnabled(false);
        txtGestion.setText("");
        btnAgregarGestion.setEnabled(false);
        btnGrabarGestion.setEnabled(false);
        btnHabilitarGestion.setEnabled(true);
    }//GEN-LAST:event_btnCancelGestionActionPerformed

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

//        if (manifiestoActual != null) {
//
//            switch (manifiestoActual.getEstadoManifiesto()) {
//                case 1:
//
//                case 2:
//
//                case 3:
//
//                    break;
//                case 4:
//
//                    break;
//
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "No hay manifiesto de ruta selecccionado ", "Error", 0);
//        }
    }//GEN-LAST:event_jBtnMinutaActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        salir();        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnExitActionPerformed

    private void btnHabilitarGestion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHabilitarGestion1ActionPerformed
        cbxTipoMovimiento.setEnabled(true);
        cbxCausalesDeDevolucion.setEnabled(true);
        cbxTiposDePeticion.setEnabled(true);
        txtObservacion.setEnabled(true);
        btnGrabarIncidencia.setEnabled(true);
        
             // TODO add your handling code here:
    }//GEN-LAST:event_btnHabilitarGestion1ActionPerformed

    private void btnCancelGestion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelGestion1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelGestion1ActionPerformed

    private void btnGrabarGestion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarGestion1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGrabarGestion1ActionPerformed

    private void txtValorFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorFacturaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorFacturaFocusGained

    private void txtValorFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorFacturaActionPerformed

    private void txtValorFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorFacturaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorFacturaKeyPressed

    private void btnGrabarIncidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarIncidenciaActionPerformed
    
        new Thread(new HiloGestonDeIncidencias(ini, this, "GrabarIncidencia")).start();  
    }//GEN-LAST:event_btnGrabarIncidenciaActionPerformed

    private void txtNumeroManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroManifiestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroManifiestoActionPerformed

    private void txtPesoFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPesoFacturaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoFacturaFocusGained

    private void txtPesoFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesoFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoFacturaActionPerformed

    private void txtPesoFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesoFacturaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoFacturaKeyPressed

    private void txtNumeroFactura1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroFactura1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFactura1FocusGained

    private void txtNumeroFactura1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroFactura1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFactura1ActionPerformed

    private void txtNumeroFactura1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroFactura1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFactura1KeyPressed

    private void cbxTiposDePeticionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTiposDePeticionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxTiposDePeticionActionPerformed

    private void tblProductosPorFacturaParcialesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaParcialesFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaParcialesFocusGained

    private void tblProductosPorFacturaParcialesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaParcialesFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaParcialesFocusLost

    private void tblProductosPorFacturaParcialesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaParcialesMouseClicked
//        if (fDescargarFacturas != null) {
//            seleccionarFila1();
//        }
//        if (fLiquidarManifiestos != null) {
//            seleccionarFila2();
//        }

    }//GEN-LAST:event_tblProductosPorFacturaParcialesMouseClicked

    private void tblProductosPorFacturaParcialesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaParcialesKeyPressed

    }//GEN-LAST:event_tblProductosPorFacturaParcialesKeyPressed

    private void txtNombreRutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreRutaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreRutaActionPerformed

    public synchronized void llenarTablaProductosPorFactura() throws Exception {
        if (incidenciaSvc.getObjFacturaCamdun().getListaDetalleFactura() != null) {
            double valorFactura = 0;
            modelo2 = (DefaultTableModel) tblProductosPorFactura.getModel();
            for (CProductosPorFactura obj : incidenciaSvc.getObjFacturaCamdun().getListaDetalleFactura()) {
                filaSelleccionada = tblProductosPorFactura.getRowCount();

                modelo2.addRow(new Object[tblProductosPorFactura.getRowCount()]);
                tblProductosPorFactura.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);  // item
                tblProductosPorFactura.setValueAt(obj.getCodigoProducto(), filaSelleccionada, 1); // numero de facturaActual
                tblProductosPorFactura.setValueAt(obj.getDescripcionProducto(), filaSelleccionada, 2); // numero de facturaActual
                tblProductosPorFactura.setValueAt(obj.getCantidad(), filaSelleccionada, 3); // numero de facturaActual
                tblProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), filaSelleccionada, 4); // numero de facturaActual
                tblProductosPorFactura.setValueAt(nf.format(obj.getValorTotalLiquidacionItem()), filaSelleccionada, 5); // numero de facturaActual

                valorFactura += obj.getValorTotalLiquidacionItem();

            }
            Thread.sleep(1500);
            lblValorTotalFactura.setText(nf.format(valorFactura));
        }

    } // incidenciaSvc.getObjFacturaCamdun().getListaDetalleFactura()

    public synchronized void llenarTablaDistribucion() throws Exception {
        if (incidenciaSvc.getObjFacturaCamdun().getListaDeMovimientosfactura() != null) {
            DefaultTableModel modelo = (DefaultTableModel) tblMovimientos.getModel();

            for (CFacturasPorManifiesto obj : incidenciaSvc.getObjFacturaCamdun().getListaDeMovimientosfactura()) {
                filaSelleccionada = tblMovimientos.getRowCount();

                modelo.addRow(new Object[tblMovimientos.getRowCount()]);
                tblMovimientos.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);  // item
                tblMovimientos.setValueAt(obj.getNumeroManifiesto(), filaSelleccionada, 1); // numero de manifiesto
                tblMovimientos.setValueAt(obj.getFechaDistribucion(), filaSelleccionada, 2); // fecha de distribucion
                tblMovimientos.setValueAt(obj.getVehiculo(), filaSelleccionada, 3); // placa del vehiculo
                tblMovimientos.setValueAt(obj.getNombreConductor(), filaSelleccionada, 4); // nombre del conductor
                tblMovimientos.setValueAt(obj.getNombreDeRuta(), filaSelleccionada, 5); // nombre de la ruta

            }
        }

    }

    public synchronized void llenarTablabitacora() throws Exception {
        //listaDeMovimientosBitacora = this.facturaActual.getListaDeMovimientosBitacora();

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + incidenciaSvc.getObjFacturaCamdun().getListaDeMovimientosBitacora().size());

        DefaultTableModel modelo = (DefaultTableModel) tblBitacora.getModel();

        for (CBitacoraFacturas obj : incidenciaSvc.getObjFacturaCamdun().getListaDeMovimientosBitacora()) {
            filaSelleccionada = tblBitacora.getRowCount();

            modelo.addRow(new Object[tblBitacora.getRowCount()]);

            tblBitacora.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 0);  // item
            tblBitacora.setValueAt(obj.getFecha(), filaSelleccionada, 1); // numero de manifiesto
            tblBitacora.setValueAt(obj.getNumeroDocumento(), filaSelleccionada, 2); // fecha de distribucion
            tblBitacora.setValueAt(obj.getObservacion(), filaSelleccionada, 3); // placa del vehiculo
            // tblBitacora.setValueAt(obj.getNombreConductor(), filaSelleccionada, 4); // nombre del conductor

        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabel_ME_Imagen;
    private javax.swing.JButton btnAgregarGestion;
    public javax.swing.JButton btnCambioResponsable;
    private javax.swing.JButton btnCancelCierre;
    private javax.swing.JButton btnCancelGestion;
    private javax.swing.JButton btnCancelGestion1;
    private javax.swing.JButton btnCancelResponsable;
    private javax.swing.JButton btnCierreDeIncidencia;
    private javax.swing.JButton btnDescargarRechazoTotal1;
    private javax.swing.JButton btnDescargarRechazoTotal2;
    private javax.swing.JButton btnGrabarCierre;
    private javax.swing.JButton btnGrabarGestion;
    private javax.swing.JButton btnGrabarGestion1;
    public javax.swing.JButton btnGrabarIncidencia;
    private javax.swing.JButton btnGrabarResponsable;
    private javax.swing.JButton btnHabilitarCambioResponsabel;
    private javax.swing.JButton btnHabilitarCierre;
    private javax.swing.JButton btnHabilitarGestion;
    public javax.swing.JButton btnHabilitarGestion1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    public javax.swing.JComboBox<String> cbxCausalesDeDevolucion;
    public javax.swing.JComboBox<String> cbxTipoMovimiento;
    public javax.swing.JComboBox<String> cbxTiposDePeticion;
    private javax.swing.JComboBox<String> cbxTiposGestiones;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    private javax.swing.JToggleButton jBtnImprimir;
    private javax.swing.JToggleButton jBtnMinuta;
    private javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    public javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JLabel lblCirculoDeProgreso1;
    public javax.swing.JLabel lblCirculoDeProgreso2;
    public javax.swing.JLabel lblCirculoDeProgreso3;
    public javax.swing.JLabel lblCirculoDeProgreso4;
    public javax.swing.JLabel lblCirculoDeProgreso5;
    public javax.swing.JLabel lblCirculoDeProgreso6;
    private javax.swing.JLabel lblValorTotalFactura;
    public javax.swing.JPanel pnlDistribucion;
    private javax.swing.JPanel pnlEntregas;
    private javax.swing.JPanel pnlMovimientos;
    private javax.swing.JPanel pnlRechazosParciales;
    private javax.swing.JPanel pnlRecogidas;
    private javax.swing.JRadioButton rdBtnAnular;
    private javax.swing.JRadioButton rdBtnEntregado;
    private javax.swing.JRadioButton rdBtnReEnvio;
    private javax.swing.JRadioButton rdbtnNoSalvada;
    private javax.swing.JRadioButton rdbtnSalvada;
    private javax.swing.JTable tablaDocsVencidos;
    private javax.swing.JTable tblBitacora;
    private javax.swing.JTable tblDescuentos;
    private javax.swing.JTable tblGestiones;
    private javax.swing.JTable tblMovimientos;
    private javax.swing.JTable tblProductosPorFactura;
    public javax.swing.JTable tblProductosPorFacturaParciales;
    private javax.swing.JTable tblProductosPorFacturaRechazados;
    private javax.swing.JTable tblRecogidas;
    public javax.swing.JTextField txtBarrioCliente;
    private javax.swing.JTextField txtCelularAuxiliar1;
    private javax.swing.JTextField txtCelularAuxiliar2;
    private javax.swing.JTextField txtCelularAuxiliar3;
    public javax.swing.JTextField txtCelularCliente;
    public javax.swing.JTextField txtCelularConductor;
    public javax.swing.JTextField txtCelularVendedor;
    private javax.swing.JTextArea txtCierreDeIncidencia;
    public javax.swing.JTextField txtConductor;
    public javax.swing.JTextField txtDireccionDelCliente;
    public javax.swing.JTextField txtFechaVenta;
    private javax.swing.JTextArea txtGestion;
    private javax.swing.JTextField txtNombreAuxiliar1;
    private javax.swing.JTextField txtNombreAuxiliar2;
    private javax.swing.JTextField txtNombreAuxiliar3;
    public javax.swing.JTextField txtNombreCanal;
    public javax.swing.JTextField txtNombreCiudad;
    public javax.swing.JTextField txtNombreDelCliente;
    public javax.swing.JTextField txtNombreDelVendedor;
    public javax.swing.JTextField txtNombreResponsable;
    public javax.swing.JTextField txtNombreRuta;
    public javax.swing.JTextField txtNumeroFactura;
    public javax.swing.JTextField txtNumeroFactura1;
    public javax.swing.JTextField txtNumeroManifiesto;
    public javax.swing.JTextArea txtObservacion;
    public javax.swing.JTextField txtPesoFactura;
    public javax.swing.JTextField txtTelefonoDelCliente;
    public javax.swing.JTextField txtValorFactura;
    public javax.swing.JTextField txtVehiculo;
    // End of variables declaration//GEN-END:variables

  

    public void limpiarTodo() {

        txtNumeroFactura.setText("");
        txtBarrioCliente.setText("");
        txtFechaVenta.setText("");
        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText("");
        txtNombreDelVendedor.setText("");
        txtTelefonoDelCliente.setText("");

        limpiarTablaProductosPorFactura();
        limpiarTablaMovimientos();
        limpiarTablaBitacora();

    }

    public void limpiarTablaProductosPorFactura() {

        DefaultTableModel modelo = (DefaultTableModel) tblProductosPorFactura.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaProductosRechazadosPorFactura() {

        DefaultTableModel modelo = (DefaultTableModel) tblProductosPorFacturaRechazados.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaDescuentos() {

        DefaultTableModel modelo = (DefaultTableModel) tblDescuentos.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaRecogidas() {

        DefaultTableModel modelo = (DefaultTableModel) tblRecogidas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaMovimientos() {

        DefaultTableModel modelo = (DefaultTableModel) tblMovimientos.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public void limpiarTablaBitacora() {

        DefaultTableModel modelo = (DefaultTableModel) tblBitacora.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    private void crearMapa(CSvcIncidencias incidencia) throws MalformedURLException, UnsupportedEncodingException {
        String direccion = "";
        if (!incidencia.getObjFacturaCamdun().getLatitud().equals("0")) {
            direccion = incidencia.getObjFacturaCamdun().getLatitud() + "," + incidencia.getObjFacturaCamdun().getLongitud();
        }

        if (direccion.length() > 0) {
            this.JLabel_ME_Imagen.setText("");
            Image imagenMapa = ObjStaticMaps.getStaticMap(direccion, 16,
                    new Dimension(831, 485), 1,
                    StaticMaps.Format.png,
                    StaticMaps.Maptype.terrain);
            if (imagenMapa != null) {
                ImageIcon imgIcon = new ImageIcon(imagenMapa);
                Icon iconImage = (Icon) imgIcon;
                JLabel_ME_Imagen.setIcon(iconImage);
            }
        }
    }

    private void deshabilitarComponentes() {
        txtNumeroFactura.setEnabled(false);
        txtNumeroFactura.setEditable(false);
        txtNumeroFactura.setText(incidenciaSvc.getObjFacturaCamdun().getNumeroDeFactura());
        txtVehiculo.setText(incidenciaSvc.getObjManifiesto().getVehiculo());
        txtConductor.setText(incidenciaSvc.getObjManifiesto().getNombreConductor() + " " + incidenciaSvc.getObjManifiesto().getApellidosConductor() );

        txtNombreAuxiliar1.setEnabled(false);
        txtNombreAuxiliar2.setEnabled(false);
        txtNombreAuxiliar3.setEnabled(false);
        txtNombreRuta.setText(incidenciaSvc.getObjManifiesto().getNombreDeRuta());
        txtNombreCanal.setText(incidenciaSvc.getObjFacturaCamdun().getCiudad());
        txtCelularConductor.setText(incidenciaSvc.getObjManifiesto().getTelefonoConductor());
        txtCelularCliente.setText(incidenciaSvc.getObjFacturaCamdun().getTelefonoCliente());
        txtCelularVendedor.setText(incidenciaSvc.getObjFacturaCamdun().getTelefonoVendedor());

        btnHabilitarGestion.setEnabled(false);
        cbxTiposGestiones.setEnabled(false);
        txtGestion.setEnabled(false);
        btnAgregarGestion.setEnabled(false);

        btnHabilitarCambioResponsabel.setEnabled(false);
        txtNombreResponsable.setEnabled(false);
        btnCambioResponsable.setEnabled(false);

        btnHabilitarCierre.setEnabled(false);
        txtCierreDeIncidencia.setEnabled(false);
        txtCierreDeIncidencia.setEditable(false);
        btnCierreDeIncidencia.setEnabled(false);

    }

    public synchronized void llenarTablaProductosPorFacturaRechazados() throws Exception {

        if (incidenciaSvc.getObjFacturaCamdun().getListaDeProductosRechazados() != null) {
            double valorFactura = 0;
            DefaultTableModel modelo3 = (DefaultTableModel) tblProductosPorFacturaRechazados.getModel();

            for (Vst_ProductosPorFacturaDescargados obj : incidenciaSvc.getObjFacturaCamdun().getListaDeProductosRechazados()) {

                // SE LLENA LA TABLA DE PRODUCTOS POR FACTURA Rechazados
                int row = tblProductosPorFacturaRechazados.getRowCount();

                modelo3.addRow(new Object[row]);

                tblProductosPorFacturaRechazados.setValueAt("" + (row + 1), row, 0);
                tblProductosPorFacturaRechazados.setValueAt(obj.getCodigoProducto(), row, 1); // código del producto

                tblProductosPorFacturaRechazados.setValueAt(obj.getDescripcionProducto(), row, 2);// descripción del producto
                tblProductosPorFacturaRechazados.setValueAt(obj.getCantidadRechazada(), row, 3);                       // cantidad de producto en la facturaActual
                tblProductosPorFacturaRechazados.setValueAt(nf.format(obj.getValorRechazo()), row, 4);// valor unitario

            }
            Thread.sleep(1500);
            lblValorTotalFactura.setText(nf.format(valorFactura));
        }

    }

    private String getNombresDeEmpleado(String cedula) {
        String nombres = "";
        if (!cedula.equals("0")) {
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (obj.getCedula().equals(cedula)) {
                    nombres = obj.getNombres() + " " + obj.getApellidos();
                    break;
                }
            }
        }

        return nombres;
    }

    private void consultarFactura() {
        try {
         cancelar();
    new Thread(new HiloGestonDeIncidencias(ini, this, "buscarFactura")).start();
           // this.facturaActual = new CFacturasPorManifiesto(ini, txtNumeroFactura.getText().trim());
           

           

            lblCirculoDeProgreso.setVisible(false);

            /*Hilo que recupera los movimientos de la facturaActual */
            // new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();
//            } else {
//                JOptionPane.showMessageDialog(this, "Número de Factura no encontrado en el sistema ", "Error", JOptionPane.WARNING_MESSAGE);
//
//            }
        } catch (Exception ex) {
            Logger.getLogger(FGestionarIncidencias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

    private void nuevo() {
      

    }

    private void grabar() {

    }

    private void cancelar() {
        txtDireccionDelCliente.setText("");
        txtBarrioCliente.setText("");
        txtFechaVenta.setText("");
        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText("");
        txtNombreDelVendedor.setText("");
        txtTelefonoDelCliente.setText("");
        
        limpiarTablaProductosPorFactura();
        limpiarTablaBitacora();
        limpiarTablaMovimientos();
        limpiarTablaProductosRechazadosPorFactura();
        limpiarTablaDescuentos();
        limpiarTablaRecogidas();


        txtVehiculo.setText("");
        txtConductor.setText("");
        txtCelularConductor.setText("");
        txtNombreRuta.setText("");
        txtNombreCanal.setText("");
        txtNumeroManifiesto.setText("");
        txtDireccionDelCliente.setText("");
        txtBarrioCliente.setText("");
        txtFechaVenta.setText("");
        txtCelularCliente.setText("");
        txtTelefonoDelCliente.setText("");
        txtCelularVendedor.setText("");
        txtNombreCiudad.setText("");

        /* nombre del cliente y el código entre parétesis*/
        txtNombreDelCliente.setText("");
        txtNombreDelVendedor.setText("");
        txtNombreDelVendedor.setText("");
        txtValorFactura.setText("");
        txtPesoFactura.setText("");

    }

    private void imprimir() {

    }

    private void salir() {
        this.dispose();
        this.setVisible(false);
    }
    
    public void llenarTablaProductosPorFacturaParciales() {
      DefaultTableModel  modelo = (DefaultTableModel) tblProductosPorFacturaParciales.getModel();

        /*Limpia la tabla de los datos*/
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        nuevoValorFactura = 0;

        // se nexan los productos a la tabla de productos por facturaActual
        for (CProductosPorFactura pxf :incidenciaSvc.getObjFacturaCamdun().getListaDetalleFactura()) {

            int fil = tblProductosPorFacturaParciales.getRowCount();

            modelo.addRow(new Object[fil]);

            tblProductosPorFacturaParciales.setValueAt(pxf.getConsecutivo(), fil, 0);
            tblProductosPorFacturaParciales.setValueAt("" + (fil + 1), fil, 1);
            tblProductosPorFacturaParciales.setValueAt(pxf.getCodigoProducto(), fil, 2);
            tblProductosPorFacturaParciales.setValueAt(pxf.getDescripcionProducto(), fil, 3);
            tblProductosPorFacturaParciales.setValueAt(pxf.getCantidad(), fil, 4);
            tblProductosPorFacturaParciales.setValueAt("" + nf.format(pxf.getValorUnitarioSinIva()), fil, 5);
            tblProductosPorFacturaParciales.setValueAt(0.0 + " %", fil, 6);
            tblProductosPorFacturaParciales.setValueAt("" + 0.0, fil, 7);
            tblProductosPorFacturaParciales.setValueAt(("" + nf.format(pxf.getValorTotalLiquidacionItem())), fil, 8);

            JComboBox combo = CreameCombo();
            TableColumn col = tblProductosPorFacturaParciales.getColumnModel().getColumn(9);
            //col.setMinWidth(300);
            //col.setMaxWidth(300);
            col.setResizable(false);
            col = tblProductosPorFacturaParciales.getColumnModel().getColumn(9);
            col.setCellEditor(new DefaultCellEditor(combo));//AGREGO EL COMBO AL CELLEDITOR

//            pxf.setPorcentajeDescuento(0.0);
//            pxf.setCantidadEntregadaItem(pxf.getCantidad());
//            pxf.setCantidadRechazadaItem(0.0);
//            pxf.setCausalDeRechazo(1);
//            pxf.setValorDescuentoItem(0.0);
//            pxf.setEntregado(1);
//            pxf.setNombreCausalDeRechazo("NINGUNO");
//            pxf.setPorcentajeDescuento(0.0);
//            pxf.setValorTotalLiquidacionItem(pxf.getCantidadEntregadaItem() * pxf.getValorUnitarioConIva());
//            pxf.setValorRechazoItem(0.0);

//            nuevoValorFactura = fDescargarFacturas.facturaActual.getValorTotalFactura();

        }

       // lblValorFactura.setText("Total Factura : " + nf.format(nuevoValorFactura));

    }
    
     private JComboBox CreameCombo() {
        //JComboBox combo = new JComboBox(new String[]{"Hector", "Maria Julia", "Daniel", "Salomon", "Pancrasia", "Prudencia", "Martin", "Daniela", "Mario"}) {
        String[] lista = new String[ini.getListaDeCausalesDeDevolucion().size()];

        int i = 0;
        for (CCausalesDeDevolucion obj : ini.getListaDeCausalesDeDevolucion()) {
            lista[i] = (obj.getNombreCausalesDeDevolucion());
            i++;
        }
        JComboBox combo = new JComboBox(lista) {
            public void updateUI() {
                super.updateUI();
                setBorder(BorderFactory.createEmptyBorder());
                setUI(new BasicComboBoxUI() {
                    @Override
                    protected JButton createArrowButton() {
                        JButton button = super.createArrowButton();
                        button.setContentAreaFilled(false);
                        button.setBorder(BorderFactory.createEmptyBorder());
                        return button;
                    }
                });
//                
//              
            }
        };
        return combo;
    }
}
