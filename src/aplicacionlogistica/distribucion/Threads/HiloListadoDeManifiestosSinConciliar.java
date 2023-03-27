/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
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
public class HiloListadoDeManifiestosSinConciliar implements Runnable {

   
   
    Inicio ini;

    // ResultSet rst = null;
    /**
     * Constructor de clase
     *
     * @param tiempo
     * @param ini
     */
    public HiloListadoDeManifiestosSinConciliar(Inicio ini) {
               this.ini = ini;

    }

   

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        ResultSet rst = null;
        Statement st;
        Connection con;
        try {

            ArrayList<CManifiestosDeDistribucion> arrManifiestosSinConciliar = null;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeManifiestosSinConciliar");

            CManifiestosDeDistribucion mfto = new CManifiestosDeDistribucion(ini);

            arrManifiestosSinConciliar = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(ini.arrListadoDeManifiestosSinConciliar());
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());
                    mfto = new CManifiestosDeDistribucion(ini);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    mfto.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    mfto.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    mfto.setVehiculo(rst.getString("vehiculo"));
                    mfto.setTipoContrato(rst.getString("tipoContrato"));
                    mfto.setConductor(rst.getString("conductor"));
                    mfto.setNombreConductor(rst.getString("nombreConductor"));
                    mfto.setDespachador(rst.getString("despachador"));
                    mfto.setNombreDespachador(rst.getString("nombreDespachador"));
                    mfto.setIdCanal(rst.getInt("idCanal"));
                    mfto.setNombreCanal(rst.getString("nombreCanal"));
                    mfto.setIdRuta(rst.getInt("idRuta"));
                    mfto.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    mfto.setTipoRuta(rst.getString("tipoRuta"));
                    mfto.setEstadoManifiesto(rst.getInt("estadoManifiesto"));
                    mfto.setKmSalida(rst.getInt("kmSalida"));
                    mfto.setKmEntrada(rst.getInt("kmEntrada"));
                    mfto.setKmRecorrido(rst.getInt("kmRecorrido"));
                    mfto.setIsFree(rst.getInt("isFree"));
                    mfto.setValorTotalManifiesto(rst.getDouble("valorTotalManifiesto"));
                    mfto.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    mfto.setValorConsignado(rst.getDouble("valorConsignado"));
                    mfto.setHoraDeDespacho(rst.getString("horaDeDespacho"));
                    mfto.setHoraDeLiquidacion(rst.getString("horaDeLiquidacion"));
                    mfto.setActivo(rst.getInt("activo"));
                    mfto.setUsuarioManifiesto(rst.getString("usuarioManifiesto"));
                    mfto.setFechaReal(rst.getString("fechaReal"));
                    mfto.setTrazabilidad(rst.getInt("trazabilidad"));

                    System.out.println("Cargando Manifiestos # -> " + mfto.getNumeroManifiesto());

                    arrManifiestosSinConciliar.add(mfto);

                    System.out.println("tiempo 2 " + new Date());
                    Thread.sleep(10);

                }     
                rst.close();
                st.close();
                con.close();
                ini.setListaDeManifiestossinConciliar(arrManifiestosSinConciliar);
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeManifiestosSinConciliar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
