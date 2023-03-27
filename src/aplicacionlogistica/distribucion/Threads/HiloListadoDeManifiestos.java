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
public class HiloListadoDeManifiestos implements Runnable {

    private int tiempo = 5;
    int estadoManifiesto = 0;
    Inicio ini;

    ArrayList<CManifiestosDeDistribucion> arrManifiestos = null;

    ResultSet rst = null;

    /**
     * Constructor de clase
     */
    public HiloListadoDeManifiestos(Inicio ini, int tiempo) {
        this.tiempo = tiempo;
        this.ini = ini;

    }

    public HiloListadoDeManifiestos(Inicio ini, int tiempo, int estadoManifiesto) {
        this.tiempo = tiempo;
        this.ini = ini;
        this.estadoManifiesto = estadoManifiesto;

    }

    @Override
    public void run() {

        ResultSet rst = null;
        Statement st;
        try {
            Connection con;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeManifiestos");

            CManifiestosDeDistribucion mfto = new CManifiestosDeDistribucion(ini);
            arrManifiestos = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(ini.arrListadoDeManifiestos());
                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());
                    mfto = new CManifiestosDeDistribucion(ini);
                    //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                    mfto.setNumeroManifiesto(rst.getString("consecutivo"));
                    mfto.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    mfto.setVehiculo(rst.getString("vehiculo"));
                    mfto.setConductor(rst.getString("conductor"));
                    mfto.setIdCanal(rst.getInt("canal"));
                    mfto.setIdRuta(rst.getInt("ruta"));
                    mfto.setEstadoManifiesto(rst.getInt("estadoManifiesto"));
                    mfto.setKmSalida(rst.getInt("kmSalida"));
                    mfto.setKmEntrada(rst.getInt("kmEntrada"));
                    mfto.setKmRecorrido(rst.getInt("kmRecorrido"));
                    mfto.setZona(rst.getInt("zona"));
                    mfto.setRegional(rst.getInt("regional"));
                    mfto.setAgencia(rst.getInt("agencia"));
                    mfto.setIsFree(rst.getInt("isFree"));
                    mfto.setValorTotalManifiesto(rst.getDouble("valorTotalManifiesto"));
                    mfto.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    mfto.setHoraDeDespacho(rst.getString("horaDeDespacho"));
                    mfto.setHoraDeLiquidacion(rst.getString("horaDeLiquidacion"));
                    mfto.setPesoKgManifiesto(rst.getDouble("pesoManifiesto"));
                    mfto.setCantidadPedidos(rst.getInt("cantidadPedidos"));
                    mfto.setActivo(rst.getInt("activo"));

                    System.out.println("Cargando Manifiestos # -> " + mfto.getNumeroManifiesto());

                    mfto.setListaDeAuxiliares("" + mfto.getNumeroManifiesto());

                    arrManifiestos.add(mfto);

                    System.out.println("tiempo 2 " + new Date());
                    Thread.sleep(10);

                }
                rst.close();
                st.close();
                con.close();
                ini.setArrManifiestos(arrManifiestos);
                Thread.sleep(1);
            }
        } // fin try // fin try
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
