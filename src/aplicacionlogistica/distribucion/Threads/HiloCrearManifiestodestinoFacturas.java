/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.picking.FManifestarFacturasEnPicking;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloCrearManifiestodestinoFacturas implements Runnable {

    Inicio ini;

    FManifestarFacturasEnPicking fManifestarFacturasEnPicking = null;

    public HiloCrearManifiestodestinoFacturas(Inicio ini, FManifestarFacturasEnPicking form) {

        this.fManifestarFacturasEnPicking = form;
        this.ini = ini;

    } // jslslpzmjC12  jslslpzmjc1212 

    @Override
    public void run() {

        if (this.fManifestarFacturasEnPicking != null) {
            crearManifiestoEnPicking();
        }

    }

    public void crearManifiestoEnPicking() {
        try {
            /* S hace visible el gif que indica que hay un proceso en ejecución */
            this.fManifestarFacturasEnPicking.lblCirculoDeProgreso2.setVisible(true);

            this.fManifestarFacturasEnPicking.modelo2 = (DefaultTableModel) this.fManifestarFacturasEnPicking.jTableFacturasPorManifiesto.getModel();

            this.fManifestarFacturasEnPicking.btnCrearManifiesto.setEnabled(false);

            //   this.fManifestarFacturasEnPicking.vistaFacturasPorManifiesto = new ArrayList();

            /*  SE LLENAN LAS PROPIEDADES DE  EL MANIFIESTO */
            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechax;
            fechax = fecha.format(new Date());

            // GRABA EL MANIFIESTO DE DISTRIBUCION
            if (grabarManifiestoDefacturasEnPicking()) {

//                this.fManifestarFacturasEnPicking.numeroManifiesto.crearRutasDeArchivos();

                /*Se crea la ruta donde se guardarán temporalmente las facturas */
//                fManifestarFacturasEnPicking.archivoConListaDeFacturas = new File(this.fManifestarFacturasEnPicking.numeroManifiesto.getRutArchivofacturasporManifiesto());
                // HABILITA CAMPO DE TEXTO PARA EMPEZAR A INGRESAR LAS FACTURAS
                this.fManifestarFacturasEnPicking.txtNumeroDeFactura.setEnabled(true);
                this.fManifestarFacturasEnPicking.txtNumeroDeFactura.setEditable(true);

                // SE CODIFICA EL MANIFIESTO Y SE LLENA EL CAMPO DE TEXTO RESPECTIVO
//                String cadenaManifiestoCodificado = this.fManifestarFacturasEnPicking.numeroManifiesto.codificarManifiesto();
//              this.fManifestarFacturasEnPicking.txtNumeroDeManifiesto.setText(cadenaManifiestoCodificado);
                //SE CREAN LOS ARRAY PARA GUARDAR LAS FACTURAS
                this.fManifestarFacturasEnPicking.listaDeFacturasPorManifiesto = new ArrayList<>(); //CfacturasPorManifiesto
                //this.fManifestarFacturasEnPicking.listaDeCFacturasCamdunEnElManifiesto = new ArrayList<>();//CFacturas
//                this.fManifestarFacturasEnPicking.numeroManifiesto.setListaFacturasPorManifiesto(this.fManifestarFacturasEnPicking.listaDeCFacturasPorManifiesto);
//               this.fManifestarFacturasEnPicking.numeroManifiesto.setListaCFacturasCamdun(this.fManifestarFacturasEnPicking.listaDeCFacturasCamdunEnElManifiesto);
                this.fManifestarFacturasEnPicking.jTabbedPane1.setEnabled(true);
                
               

                   /* DE DESHABILITA EL MANIFIESTO PARA EVITAR QUE OTRO USUARIO LO MODIFIQUE.*/
                this.fManifestarFacturasEnPicking.txtBarroCliente.setEditable(false);
                this.fManifestarFacturasEnPicking.txtDireccionCliente.setEditable(false);
                this.fManifestarFacturasEnPicking.txtNombreDeCliente.setEditable(false);
                this.fManifestarFacturasEnPicking.txtTelefonoCliente.setEditable(false);
                this.fManifestarFacturasEnPicking.txtNumeroDeManifiesto.setEditable(false);
                this.fManifestarFacturasEnPicking.txtNombreVendedor.setEditable(false);

                JOptionPane.showInternalMessageDialog(this.fManifestarFacturasEnPicking, "Manifiesto guardado correctamente ", "Manifiesto guardado correctamente ", JOptionPane.INFORMATION_MESSAGE);

                this.fManifestarFacturasEnPicking.formaDePago = 1; // contado
//                this.fManifestarFacturasEnPicking.btnContado.setSelected(true);

                this.fManifestarFacturasEnPicking.lblCirculoDeProgreso2.setVisible(false);

                this.fManifestarFacturasEnPicking.txtNumeroDeFactura.requestFocus();
                this.fManifestarFacturasEnPicking.txtNumeroDeFactura.requestFocus();
            } else {
                JOptionPane.showInternalMessageDialog(this.fManifestarFacturasEnPicking, "Error al guardar manifiesto", "Error ", JOptionPane.ERROR_MESSAGE);
//                this.fManifestarFacturasEnPicking.numeroManifiesto.liberarManifiesto(true);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloCrearManifiestodestinoFacturas.class.getName()).log(Level.SEVERE, null, ex);
//            this.fManifestarFacturasEnPicking.numeroManifiesto.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }
    }

    /**
     * Método que permite guardar los registros en la base de datos
     *
     * @return true si graba sin noveda, retorna false si hubo un error al
     * grabar
     */
    public boolean grabarManifiestoDefacturasEnPicking() {

        String clave = UUID.randomUUID().toString();

        boolean grabado = false;
        try {

            String sql = "INSERT INTO manifiestosdepicking"
                    + "(clave,"
                    + "destino,"
                    + "destinatario,"
                    + "usuario,"
                    + "direccionIp,"
                    + "nombreEstacion)" 
                    + "VALUES"
                    + "('" + clave + "',"
                    + "'" + this.fManifestarFacturasEnPicking.cbxDestinos.getSelectedItem() + "',"
                    + "'" + this.fManifestarFacturasEnPicking.txtNombreDeReceptor.getText() + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "',"
                    + "'" + ini.getDirecionIpLocal() + "',"
                    + "'" + ini.getNombreEstacionLocal() + "') "
                    + "ON DUPLICATE KEY UPDATE  flag=1 ;";

            if (ini.insertarDatosRemotamente(sql)) {

                sql = "SELECT * "
                        + "FROM manifiestosdepicking "
                        + "WHERE "
                        + "clave='" + clave + "'";

                Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloCrearManifiestodestinoFacturas.grabarManifiestoDefacturasEnPicking");

                if (con != null) {

                    Statement st = con.createStatement();
                    ResultSet rst = st.executeQuery(sql);
                    if (rst.next()) {
                        this.fManifestarFacturasEnPicking.numeroManifiesto = "" + rst.getInt("idmanifiestosdepicking");
                        this.fManifestarFacturasEnPicking.txtNumeroDeManifiesto.setText("" + rst.getInt("idmanifiestosdepicking"));

                    } else {
                        this.fManifestarFacturasEnPicking.numeroManifiesto = null;
                    }
                    
                    String fecha = ("" + rst.getDate("fechaIng"));
                    fecha = fecha.substring(0, 10);
                    this.fManifestarFacturasEnPicking.txtFecha.setText(fecha);
                    
                    rst.close();
                    st.close();
                    con.close();
                    this.fManifestarFacturasEnPicking.listaDeFacturasPorManifiesto= new ArrayList<>(); 
                   
                }

                grabado = true;
                this.fManifestarFacturasEnPicking.txtNumeroDeFactura.requestFocus();

            } else {
                grabado = false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
}
