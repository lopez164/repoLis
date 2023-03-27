/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import aplicacionlogistica.distribucion.objetos.CMovimientosManifiestosfacturas;
import java.util.List;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeMovimientosManifiestosfacturas implements Runnable {

    List<CMovimientosManifiestosfacturas> listaDeMovimientosManifiestosfacturas = null;

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
     * @param contadorDeRegistros
     */
    public HiloListadoDeMovimientosManifiestosfacturas(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;

    }

    public HiloListadoDeMovimientosManifiestosfacturas(Inicio ini) {

        this.ini = ini;

    }

    @Override
    public void run() {

        try {

            if (this.barraInf != null) {
                int k = 0;
                int y = 0;
                ResultSet rst = null;
                Statement st;
                Connection con;

                // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeMovimientosManifiestosfacturas");

                CMovimientosManifiestosfacturas movimiento = new CMovimientosManifiestosfacturas(ini);

                listaDeMovimientosManifiestosfacturas = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(movimiento.arrListadoDeCMovimientosManifiestosfacturas());

                    rst.last();
                    int numeroFilas = rst.getRow();
                    rst.beforeFirst();

                    this.totalFilasDeConsultas += numeroFilas;

                    while (rst.next()) {

                        movimiento = new CMovimientosManifiestosfacturas(ini);
                        //  idtipoDeMovimiento, nombreTipoDeMovimiento, activo, fechaIng, usuario, flag
                        movimiento.setIdMovimientosManifiestosfacturas(rst.getInt("idtipoDeMovimiento"));
                        movimiento.setNombreMovimientosManifiestosfacturas(rst.getString("nombreTipoDeMovimiento"));
                        movimiento.setActivoMovimientosManifiestosfacturas(rst.getInt("activo"));

                        listaDeMovimientosManifiestosfacturas.add(movimiento);

                        k++;
                        System.out.println("Cargando movimientos -> " + k);

                        /*Le asigna valor a la barra inferior*/
                        y = (int) (k * 100) / numeroFilas;
                        this.barraInf.setValue(y);
                        this.barraInf.repaint();
                        Thread.sleep(1);
                    }

                    /*Asigna el valor a la Barra de progreso superior*/
                    this.contadorDeRegistros += numeroFilas;
                    y = (int) (this.contadorDeRegistros * 100) / totalFilasDeConsultas;
                    this.barraSup.setValue((int) y);//100
                    this.barraSup.repaint();

                    rst.close();
                    st.close();
                    con.close();
                    ini.setListaDeMovimientosFacturas(listaDeMovimientosManifiestosfacturas);
                    Thread.sleep(1);
                }
            } else {

                int k = 0;
                int y = 0;
                ResultSet rst = null;
                Statement st;
                Connection con;
                // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeMovimientosManifiestosfacturas");

                CMovimientosManifiestosfacturas movimiento = new CMovimientosManifiestosfacturas(ini);

                listaDeMovimientosManifiestosfacturas = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(movimiento.arrListadoDeCMovimientosManifiestosfacturas());

                    while (rst.next()) {
                        System.out.println("Cargando  -> " + new Date());
                        movimiento = new CMovimientosManifiestosfacturas(ini);
                        //  idtipoDeMovimiento, nombreTipoDeMovimiento, activo, fechaIng, usuario, flag
                        movimiento.setIdMovimientosManifiestosfacturas(rst.getInt("idtipoDeMovimiento"));
                        movimiento.setNombreMovimientosManifiestosfacturas(rst.getString("nombreTipoDeMovimiento"));
                        movimiento.setActivoMovimientosManifiestosfacturas(rst.getInt("activo"));

                        System.out.println("Cargando Movimientos facturas -> " + movimiento.getNombreMovimientosManifiestosfacturas());

                        listaDeMovimientosManifiestosfacturas.add(movimiento);

                        Thread.sleep(1);

                        System.out.println("tiempo 2 " + new Date());

                    }
                    rst.close();
                    st.close();
                    con.close();
                    ini.setListaDeMovimientosFacturas(listaDeMovimientosManifiestosfacturas);
                    Thread.sleep(1);

                }

            }
        }// fin try // fin try// fin try // fin try
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
           
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeMovimientosManifiestosfacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
