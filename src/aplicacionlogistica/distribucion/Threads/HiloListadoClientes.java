/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloListadoClientes implements Runnable {

    Inicio ini;
    List<CClientes> listaDeClientes = null;
    String parteDelNombre;
    JLabel label;

    JTable tablaClientes = null;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param parteDelNombre
     */
    public HiloListadoClientes(Inicio ini, String parteDelNombre, JTable tabla,JLabel label) {
        this.parteDelNombre = parteDelNombre;
        this.ini = ini;
        tablaClientes = tabla;
        this.label=label;

    }
    
     /**
     * Constructor de clase
     *
     * @param ini
     * @param parteDelNombre
     */
    public HiloListadoClientes(Inicio ini, String parteDelNombre, JTable tabla) {
        this.parteDelNombre = parteDelNombre;
        this.ini = ini;
        tablaClientes = tabla;
      

    }


    public HiloListadoClientes(Inicio ini) {
        this.ini = ini;

    }

    @Override
    public void run() {
        if (tablaClientes != null) {
            correrHilo();
        }else{
            otroHilo();
        }

    }

    private void correrHilo() throws HeadlessException {
        ResultSet rst = null;
        Statement st;
        //int contadorDeCiclos = 0;
        Connection con;
        try {
            label.setVisible(true);
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoClientes");
            con = ini.getConnRemota();

            CClientes cliente = new CClientes(ini);
            listaDeClientes = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(cliente.getListadoDeClientes(parteDelNombre));
                while (rst.next()) {
                    System.out.println("Cargando cliente -> " + new Date());
                    cliente = new CClientes(ini);

                    cliente.setCodigoInterno(rst.getString("codigoInterno"));
                    cliente.setNitCliente(rst.getString("nitCliente"));
                    cliente.setNombreEstablecimiento(rst.getString("nombreEstablecimiento"));
                    cliente.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    cliente.setDireccion(rst.getString("direccion"));
                    cliente.setBarrio(rst.getString("barrio"));
                    cliente.setCiudad(rst.getString("ciudad"));
                    cliente.setClasificacion(rst.getString("clasificacion"));
                    cliente.setCelularCliente(rst.getString("celularCliente"));
                    cliente.setEmailCliente(rst.getString("emailCliente"));
                    cliente.setFechaDeIngresoCliente(rst.getDate("fechaDeIngresoCliente"));
                    cliente.setLatitud(rst.getString("latitud"));
                    cliente.setLongitud(rst.getString("longitud"));
                    cliente.setCanalDeVenta(rst.getInt("canalDeVenta"));
                    cliente.setRuta(rst.getInt("ruta"));
                    cliente.setFrecuencia(rst.getInt("frecuencia"));
                    cliente.setZona(rst.getInt("zona"));
                    cliente.setRegional(rst.getInt("regional"));
                    cliente.setAgencia(rst.getInt("agencia"));
                    cliente.setPorcentajeDescuento(rst.getInt("porcentajeDescuento"));
                    cliente.setActivoCliente(rst.getInt("activo"));

                    System.out.println("Cargando cliente -> " + cliente.getNombreDeCliente());

                    listaDeClientes.add(cliente);

                    Thread.sleep(2);
                }
                rst.close();
                st.close();
                //con.close();
                ini.setListaDeClientes(listaDeClientes);

                llenarTablaClientes();
            }
           
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
             label.setVisible(false);
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            
             label.setVisible(false);
            Logger.getLogger(HiloListadoClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
         label.setVisible(false);
    }

     private void otroHilo() throws HeadlessException {
        ResultSet rst = null;
        Statement st;
        //int contadorDeCiclos = 0;
        Connection con;
        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoClientes");

            con = ini.getConnRemota();
            CClientes cliente = new CClientes(ini);
            listaDeClientes = new ArrayList();
            ini.setListaDeClientes(listaDeClientes);
            
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(cliente.getListadoDeClientes());
                while (rst.next()) {
                    System.out.println("Cargando cliente -> " + new Date());
                    cliente = new CClientes(ini);

                    cliente.setCodigoInterno(rst.getString("codigoInterno"));
                    cliente.setNitCliente(rst.getString("nitCliente"));
                    cliente.setNombreEstablecimiento(rst.getString("nombreEstablecimiento"));
                    cliente.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    cliente.setDireccion(rst.getString("direccion"));
                    cliente.setBarrio(rst.getString("barrio"));
                    cliente.setCiudad(rst.getString("ciudad"));
                    cliente.setClasificacion(rst.getString("clasificacion"));
                    cliente.setCelularCliente(rst.getString("celularCliente"));
                    cliente.setEmailCliente(rst.getString("emailCliente"));
                    cliente.setFechaDeIngresoCliente(rst.getDate("fechaDeIngresoCliente"));
                    cliente.setLatitud(rst.getString("latitud"));
                    cliente.setLongitud(rst.getString("longitud"));
                    cliente.setCanalDeVenta(rst.getInt("canalDeVenta"));
                    cliente.setRuta(rst.getInt("ruta"));
                    cliente.setFrecuencia(rst.getInt("frecuencia"));
                    cliente.setZona(rst.getInt("zona"));
                    cliente.setRegional(rst.getInt("regional"));
                    cliente.setAgencia(rst.getInt("agencia"));
                    cliente.setPorcentajeDescuento(rst.getInt("porcentajeDescuento"));
                    cliente.setActivoCliente(rst.getInt("activo"));

                    System.out.println("Cargando cliente -> " + cliente.getNombreDeCliente());

                    listaDeClientes.add(cliente);

                    Thread.sleep(2);
                }
                rst.close();
                st.close();
                //con.close();
                ini.setListaDeClientes(listaDeClientes);

              
            }
        }
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void llenarTablaClientes() throws Exception {
        if (ini.getListaDeClientes() != null) {

            DefaultTableModel modelo2 = (DefaultTableModel) tablaClientes.getModel();
            for (CClientes obj : ini.getListaDeClientes()) {
                int filaTabla2 = tablaClientes.getRowCount();

                modelo2.addRow(new Object[tablaClientes.getRowCount()]);

                tablaClientes.setValueAt(obj.getCodigoInterno(), filaTabla2, 0); // numero de factura
                tablaClientes.setValueAt(obj.getNombreDeCliente(), filaTabla2, 1); // numero de factura

            }

        }

    }

}
