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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloListaDeNumeroDeManifiestosPendientes implements Runnable {

    List<CManifiestosDeDistribucion> listaDeManifiestos = null;

    Inicio ini = null;
    JProgressBar barraInf = null;
    JProgressBar barraSup = null;
    int totalFilasDeConsultas;
    int contadorDeRegistros;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param barraInf
     * @param barraSup
     * @param totalFilas
     */
    public HiloListaDeNumeroDeManifiestosPendientes(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;

    }

    public HiloListaDeNumeroDeManifiestosPendientes(Inicio ini) {

        this.ini = ini;

    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        if (this.barraInf != null) {

            try {

                int k = 0;
                int y = 0;
                ResultSet rst = null;
                Statement st;
                Connection con;

                // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                //c on = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListaDeNumeroDeManifiestosPendientes");

                con = ini.getConnRemota();
                
                CManifiestosDeDistribucion mfto = new CManifiestosDeDistribucion(ini);

                listaDeManifiestos = new ArrayList();
                if (con != null) {
                    String sql = "SELECT * "
                            + "FROM vst_manifiestoDeDistribucion "
                            + "WHERE  estadoManifiesto<='3'  order by  numeroManifiesto asc;";

                    st = con.createStatement();
                    rst = st.executeQuery(sql);

                    rst.last();
                    int numeroFilas = rst.getRow();
                    rst.beforeFirst();

                    this.totalFilasDeConsultas += numeroFilas;

                    while (rst.next()) {
                        mfto = new CManifiestosDeDistribucion(ini);

                        mfto.setActivo(rst.getInt("activo"));
                        mfto.setAgencia(rst.getInt("agencia"));
                        mfto.setCantidadPedidos(rst.getInt("cantidadPedidos"));
                        mfto.setConductor(rst.getString("conductor"));
                        mfto.setDespachador(rst.getString("despachador"));
                        mfto.setEstadoManifiesto(rst.getInt("estadoManifiesto"));
                        mfto.setFechaDistribucion(rst.getString("fechaDistribucion"));
                        mfto.setFechaReal(rst.getString("fechaReal"));
                        mfto.setHoraDeDespacho(rst.getString("horaDeDespacho"));
                        mfto.setHoraDeLiquidacion(rst.getString("horaDeLiquidacion"));
                        mfto.setIdCanal(rst.getInt("idCanal"));
                        mfto.setNombreCanal(rst.getString("nombreCanal"));
                        mfto.setIdRuta(rst.getInt("idRuta"));
                        mfto.setNombreDeRuta(rst.getString("nombreDeRuta"));
                        mfto.setIsFree(rst.getInt("isFree"));
                        mfto.setKmEntrada(rst.getInt("kmEntrada"));
                        mfto.setKmSalida(rst.getInt("kmSalida"));
                        mfto.setKmRecorrido(rst.getInt("kmRecorrido"));
                        mfto.setConductor(rst.getString("conductor"));
                        mfto.setNombreConductor(rst.getString("nombreConductor"));
                        mfto.setApellidosConductor(rst.getString("apellidosConductor"));
                        mfto.setDespachador(rst.getString("despachador"));
                        mfto.setNombreDespachador(rst.getString("nombreDespachador"));
                        mfto.setApellidosDespachador(rst.getString("apellidosDespachador"));
                        mfto.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                        mfto.setRegional(rst.getInt("regional"));
                        mfto.setTipoContrato(rst.getString("tipoContrato"));
                        mfto.setTipoRuta(rst.getString("tipoRuta"));
                        mfto.setTipoVehiculo(rst.getString("tipoVehiculo"));
                        mfto.setTrazabilidad(rst.getInt("trazabilidad"));
                        mfto.setUsuarioManifiesto(rst.getString("usuarioManifiesto"));
                        mfto.setValorConsignado(rst.getDouble("valorConsignado"));
                        mfto.setValorRecaudado(rst.getDouble("valorRecaudado"));
                        mfto.setValorTotalManifiesto(rst.getDouble("valorTotalManifiesto"));
                        mfto.setPesoKgManifiesto(rst.getDouble("pesoManifiesto"));
                        mfto.setVehiculo(rst.getString("vehiculo"));
                        mfto.setZona(rst.getInt("zona"));

                        listaDeManifiestos.add(mfto);
                        k++;
                        System.out.println("Cargando manifiestos -> " + k);

                        /*Le asigna valor a la barra inferior*/
                        y = (int) (k * 100) / numeroFilas;
                        this.barraInf.setValue(y);
                        this.barraInf.repaint();
                        Thread.sleep(1);
                    }

                    if (totalFilasDeConsultas > 0) {
                        /*Asigna el valor a la Barra de progreso superior*/
                        this.contadorDeRegistros += numeroFilas;
                        y = (int) (this.contadorDeRegistros * 100) / totalFilasDeConsultas;
                        this.barraSup.setValue((int) y);//100
                        this.barraSup.repaint();
                    }
                    rst.close();
                    st.close();
                    // con.close();
                      ini.setListaDeManifiestossinDescargar(listaDeManifiestos);
                    Thread.sleep(1);
                }
            } catch (Exception ex) {
                Logger.getLogger(HiloListaDeNumeroDeManifiestosPendientes.class.getName()).log(Level.SEVERE, null, ex);

            }
        } else {

            ResultSet rst = null;
            Statement st = null;
            Connection con = null;
            try {

                // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                //c on = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListaDeNumeroDeManifiestosPendientes");

                con = ini.getConnRemota();
                
                CManifiestosDeDistribucion mfto = new CManifiestosDeDistribucion(ini);

                listaDeManifiestos = new ArrayList();
                if (con != null) {
                    String sql = "SELECT numeroManifiesto "
                            + "FROM vst_manifiestoDeDistribucion "
                            + "WHERE  estadoManifiesto='3'  order by  numeroManifiesto asc;";

                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    while (rst.next()) {

                        String cadena;
                        //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                        cadena = rst.getString("numeroManifiesto");

                        //  listaDeManifiestos.add(cadena);
                        System.out.println("tiempo 2 " + new Date());
                        Thread.sleep(10);

                    }
                    rst.close();
                    st.close();
                    //con.close();
                    //        ini.setListaDeNumeroDeManifiestosPendientes(listaDeManifiestos);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                try {
                    System.err.println(e.getMessage());
                    JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
                    rst.close();
                    st.close();
                   // con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(HiloListaDeNumeroDeManifiestosPendientes.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (Exception ex) {
                Logger.getLogger(HiloListaDeNumeroDeManifiestosPendientes.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
    }
}
