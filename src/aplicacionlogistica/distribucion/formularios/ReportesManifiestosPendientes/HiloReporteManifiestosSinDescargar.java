/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.ReportesManifiestosPendientes;

import aplicacionlogistica.distribucion.formularios.reportes.ReporteFacturasEnDistribucion.*;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasEnDistribucion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloReporteManifiestosSinDescargar implements Runnable {

   
   
    Inicio ini;
    FReporteManifiestosSinDescargar form;

    // ResultSet rst = null;
    /**
     * Constructor de clase
     *
     * @param tiempo
     * @param ini
     */
    public HiloReporteManifiestosSinDescargar(FReporteManifiestosSinDescargar form) {
        this.form=form;       
        this.ini = form.ini;

    }

   

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        ResultSet rst = null;
        Statement st;
        Connection con;
        int i=0;
        try {

            ArrayList<Vst_FacturasEnDistribucion> listaDeFacturasEnDistribucion = null;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloReporteManifiestosSinDescargar");

            Vst_FacturasEnDistribucion vista = new Vst_FacturasEnDistribucion(ini);
            
            java.sql.Date fecha = (java.sql.Date) ini.getFechaSql(this.form.jFechaInicial);

            listaDeFacturasEnDistribucion = new ArrayList();
            
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(ini.arrListadoDeFacturasEnDistribucion(fecha));
                
                int rstSize=rst.getFetchSize()/100;
                
                while (rst.next()) {
                    if(i>=rstSize){
                        form.barra.setValue(form.barra.getValue() + i);
                        i=0;
                    }
                    System.out.println("Cargando  -> " + new Date());
                    vista = new Vst_FacturasEnDistribucion(ini);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    vista.setConsecutivo(rst.getString("consecutivo")); 
                    vista.setAdherencia(rst.getString("adherencia")); 
                    vista.setNumeroManifiesto(rst.getString("numeroManifiesto")); 
                    vista.setFechaDistribucion(rst.getDate("fechaDistribucion")); 
                    vista.setVehiculo(rst.getString("vehiculo")); 
                    vista.setConductor(rst.getString("conductor")); 
                    vista.setNombreConductor(rst.getString("nombreConductor")); 
                    vista.setDespachador(rst.getString("despachador")); 
                    vista.setNombreDespachador(rst.getString("nombreDespachador")); 
                    vista.setIdRuta(rst.getString("idRuta")); 
                    vista.setNombreDeRuta(rst.getString("nombreDeRuta")); 
                    vista.setTipoRuta(rst.getString("tipoRuta")); 
                    vista.setNumeroFactura(rst.getString("numeroFactura")); 
                    vista.setValorARecaudarFactura(rst.getString("valorARecaudarFactura")); 
                    vista.setFechaIng(rst.getDate("fechaIng")); 
                    vista.setFechaDeVenta(rst.getDate("fechaDeVenta")); 
                    vista.setCliente(rst.getString("cliente")); 
                    vista.setRutaDeCliente(rst.getString("rutaDeCliente")); 
                    vista.setNombreRutaDeCliente(rst.getString("nombreRutaDeCliente")); 
                    vista.setNombreDeCliente(rst.getString("nombreDeCliente")); 
                    vista.setDireccionDeCliente(rst.getString("direccionDeCliente")); 
                    vista.setBarrio(rst.getString("barrio")); 
                    vista.setTelefonoCliente(rst.getString("telefonoCliente")); 
                    vista.setVendedor(rst.getString("vendedor")); 
                    vista.setFormaDePago(rst.getString("formaDePago")); 
                    vista.setIdCanal(rst.getString("idCanal")); 
                    vista.setNombreCanal(rst.getString("nombreCanal")); 
                    vista.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva")); 
                    vista.setValorIvaFactura(rst.getDouble("valorIvaFactura")); 
                    vista.setValorTotalFactura(rst.getDouble("valorTotalFactura")); 
                    vista.setValorRechazo(rst.getDouble("valorRechazo")); 
                    vista.setValorDescuento(rst.getDouble("valorDescuento")); 
                    vista.setValorTotalRecaudado(rst.getDouble("ValorTotalRecaudado")); 
                    vista.setSalidasDistribucion(rst.getInt("salidasDistribucion")); 
                    vista.setFechaReal(rst.getDate("fechaReal")); 
                    vista.setTipoContrato(rst.getString("tipoContrato")); 
                    vista.setTipoVehiculo(rst.getString("tipoVehiculo")); 
                    vista.setUsuario(rst.getString("usuario"));
                  

                   
                    System.out.println("Cargando facturas # -> " + vista.getNumeroFactura());

                    listaDeFacturasEnDistribucion.add(vista);

                    System.out.println("tiempo 2 " + new Date());
                    Thread.sleep(10);

                    i++;
                } 
                 form.barra.setValue(100);
                i=0;
                rst.close();
                st.close();
                con.close();
                ini.setListaDeFacturasEnDistribucion(listaDeFacturasEnDistribucion);
                form.listaDeRegistros=listaDeFacturasEnDistribucion;
                Thread.sleep(1);
                form.llenarTablabitacora();
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloReporteManifiestosSinDescargar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
