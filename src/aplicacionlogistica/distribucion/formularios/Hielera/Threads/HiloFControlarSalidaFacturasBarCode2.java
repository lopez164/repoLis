/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.minutas.MinutasDeDistribucion;
import aplicacionlogistica.distribucion.formularios.Hielera.FControlarSalidaFacturasBarCode2;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CProductosPorMinuta;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloFControlarSalidaFacturasBarCode2 implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FControlarSalidaFacturasBarCode2 fControlarSalidaFacturasBarCode2 = null;
    String caso;
    List<String> listaDeNOmbresDeConductores;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFControlarSalidaFacturasBarCode2(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fDespachoHielera
     * @param comando
     */
    public HiloFControlarSalidaFacturasBarCode2(Inicio ini, FControlarSalidaFacturasBarCode2 fControlarSalidaFacturasBarCode2, String comando) {
        this.ini = ini;
        this.fControlarSalidaFacturasBarCode2 = fControlarSalidaFacturasBarCode2;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "llenarComboBoxNombresDeConductores":
                        llenarComboBoxNombresDeConductores();

                        break;
                    case "asignarConductor":
                        asignarConductor();
                        break;
                    case "cancelar":
                        //cancelar();
                        break;
                    case "agregarLafactura":
                        //consultarLafactura();
                        // descargarFactura();
                        break;
                    case "imprimir":
                        // imprimir();
                        break;

                    case "getionarSoporteConsignacion":
                        //getionarSoporteConsignacion();
                        break;

                    default:
                        JOptionPane.showInternalMessageDialog(this.fControlarSalidaFacturasBarCode2, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloFControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llenarComboBoxNombresDeConductores() {
        this.fControlarSalidaFacturasBarCode2.cbxNOmbresConductores.removeAllItems();
        setListaDeNombresDeConductores();

        if (listaDeNOmbresDeConductores.size() > 0) {
            this.fControlarSalidaFacturasBarCode2.cbxNOmbresConductores.addItem("Seleccione nombre de Conductor");
            for (String nombre : listaDeNOmbresDeConductores) {
                this.fControlarSalidaFacturasBarCode2.cbxNOmbresConductores.addItem(nombre);
            }
            this.fControlarSalidaFacturasBarCode2.cbxNOmbresConductores.setEnabled(true);

        } else {
            this.fControlarSalidaFacturasBarCode2.cbxNOmbresConductores.addItem("No hay conductores asignados");
            this.fControlarSalidaFacturasBarCode2.jBtnNuevo.setEnabled(true);
            this.fControlarSalidaFacturasBarCode2.cbxNOmbresConductores.setEnabled(false);
        }

    }

    public void setListaDeNombresDeConductores() {
        ResultSet rst = null;
        Statement st = null;
        Connection con;
        String sql = null;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CEmpleados.setListaFacturasPorConductor");

        con = ini.getConnRemota();
        
        listaDeNOmbresDeConductores = new ArrayList<>();

       // String valor = ini.getPropiedades().getProperty("idOperador");

        if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
            sql = "select distinct(nombreConductor) "
                    + "FROM vst_defintivofacturaspormanifiesto "
                    + "WHERE  "
                    + "despachado='0' and  "
                    + "fechaDistribucion = current_date "
                    + "order by  nombreConductor asc; ";

        } else {
            sql = "select distinct(nombreConductor) "
                    + "FROM vst_defintivofacturaspormanifiesto "
                    + "WHERE  "
                    + "despachado='0' and  ";

            if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {

                sql += "fechaDistribucion=(select DATE_SUB(curdate(),INTERVAL 1 day)) ";

            } else {
                sql += "fechaDistribucion = current_date ";
            }

            sql += "order by  nombreConductor asc; ";
        }
        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {

                    listaDeNOmbresDeConductores.add(rst.getString("nombreConductor"));
                }

                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(HiloFControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(HiloFControlarSalidaFacturasBarCode2.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private void asignarConductor() {
        List<CProductosPorMinuta> listaDeProductos;

        this.fControlarSalidaFacturasBarCode2.conductor.setListaDeFacturasPorConductorSinDespachar();

        if (this.fControlarSalidaFacturasBarCode2.conductor.getListaDeFacturasPorConductorSinDespachar().size() > 0) {

            this.fControlarSalidaFacturasBarCode2.numeroManifesto = Integer.parseInt(this.fControlarSalidaFacturasBarCode2.conductor.getListaDeFacturasPorConductorSinDespachar().get(0).getNumeroManifiesto());

            DefaultTableModel modelo = (DefaultTableModel) this.fControlarSalidaFacturasBarCode2.jTableFacturas.getModel();

            this.fControlarSalidaFacturasBarCode2.listaDeFacturas = "";

            /*Llena la tabla de las facturas */
            for (CFacturasPorManifiesto fac : this.fControlarSalidaFacturasBarCode2.conductor.getListaDeFacturasPorConductorSinDespachar()) {
                this.fControlarSalidaFacturasBarCode2.listaDeFacturas += "'" + fac.getNumeroFactura() + "',";
                int fila = this.fControlarSalidaFacturasBarCode2.jTableFacturas.getRowCount();
                modelo.addRow(new Object[this.fControlarSalidaFacturasBarCode2.jTableFacturas.getRowCount()]);

                this.fControlarSalidaFacturasBarCode2.jTableFacturas.setValueAt("" + (fila + 1), fila, 0);
                this.fControlarSalidaFacturasBarCode2.jTableFacturas.setValueAt(fac.getNumeroFactura(), fila, 1);

            }
            this.fControlarSalidaFacturasBarCode2.listaDeFacturas
                    = this.fControlarSalidaFacturasBarCode2.listaDeFacturas.substring(0, this.fControlarSalidaFacturasBarCode2.listaDeFacturas.length() - 1);

            // SACAR MINUTA DEL CONDUCTOR
            this.fControlarSalidaFacturasBarCode2.minutaConductor = new MinutasDeDistribucion(ini);
            listaDeProductos = this.fControlarSalidaFacturasBarCode2.minutaConductor.getMinutaPorConductor(
                    this.fControlarSalidaFacturasBarCode2.listaDeFacturas);

            this.fControlarSalidaFacturasBarCode2.conductor.setMinutaConductor(listaDeProductos);
            this.fControlarSalidaFacturasBarCode2.minutaFacturada = new ArrayList<>();

            boolean encontrado = false;

            for (CProductosPorMinuta minuta : this.fControlarSalidaFacturasBarCode2.conductor.getMinutaConductor()) {
                encontrado = false;
                String[] arrSplit = ini.getPropiedades().getProperty("productosNoAdmitidos").split(",");
                for (String cad : arrSplit) {
                    if (minuta.getDescripcionProducto().contains(cad)) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    // if (!(minuta.getDescripcionProducto().contains("DOMICILI") || minuta.getDescripcionProducto().contains("CERNIDO"))) {
                    this.fControlarSalidaFacturasBarCode2.minutaFacturada.add(minuta);
                }
            }

            /* Se llena la tabla con los productos de las facturas
                    manifestadas al conductor
             */
            this.fControlarSalidaFacturasBarCode2.llenarJtableProductos(this.fControlarSalidaFacturasBarCode2.minutaFacturada);

            this.fControlarSalidaFacturasBarCode2.txtCedulaConductor.setEnabled(false);
            this.fControlarSalidaFacturasBarCode2.txtnombreConductor.setEnabled(false);

            this.fControlarSalidaFacturasBarCode2.txtCantidadProducto.setEnabled(true);
            this.fControlarSalidaFacturasBarCode2.txtCodigoDeBArras.setEnabled(true);

            this.fControlarSalidaFacturasBarCode2.txtCantidadProducto.setEditable(true);
            this.fControlarSalidaFacturasBarCode2.txtCodigoDeBArras.setEditable(true);

            this.fControlarSalidaFacturasBarCode2.txtCodigoDeBArras.requestFocus();
            this.fControlarSalidaFacturasBarCode2.txtCodigoDeBArras.requestFocus();

        } else {
            JOptionPane.showInternalMessageDialog(this.fControlarSalidaFacturasBarCode2, "El conductor no tiene  facturas pendientes para salir a ruta", "Error, sin facturas", JOptionPane.WARNING_MESSAGE);

        }

    }

}
