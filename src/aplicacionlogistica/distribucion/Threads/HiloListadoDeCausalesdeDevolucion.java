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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import java.util.List;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeCausalesdeDevolucion implements Runnable {

    List<CCausalesDeDevolucion> listaDeCausalesDeRechazo = null;

    Inicio ini = null;
    JProgressBar barraInf = null;
    JProgressBar barraSup = null;
    int totalFilasDeConsultas;
    int contadorDeRegistros;

    /**
     * Constructor de clase
     */
    public HiloListadoDeCausalesdeDevolucion(Inicio ini) {

        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param barraInf
     * @param barraSup
     * @param totalFilas
     */
    public HiloListadoDeCausalesdeDevolucion(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;

    }

    @Override
    public void run() {

        if (this.barraInf != null) {

            try {
                int k = 0;
                int y = 0;
                ResultSet rst = null;
                Statement st;
                Connection con;
                // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeCausalesdeRechazo");

                con = ini.getConnRemota();
                CCausalesDeDevolucion causarechazo = new CCausalesDeDevolucion(ini);
                listaDeCausalesDeRechazo = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(causarechazo.arrListadoDeCCausalesDeRechazo());

                    rst.last();
                    int numeroFilas = rst.getRow();
                    rst.beforeFirst();

                    this.totalFilasDeConsultas += numeroFilas;

                    while (rst.next()) {
                       
                        causarechazo = new CCausalesDeDevolucion(ini);
                        // idcausalesDeRechazo, nombreCausalDeRechazo, activo, fechaIng, usuario, flag
                        causarechazo.setIdCausalesDeDevolucion(rst.getInt("idcausalesDeRechazo"));
                        causarechazo.setNombreCausalesDeDEvolucion(rst.getString("nombreCausalDeRechazo"));
                        causarechazo.setActivoCausalesDeDEvolucion(rst.getInt("activo"));

                        listaDeCausalesDeRechazo.add(causarechazo);

                        k++;
                        System.out.println("Cargando Causal de Devolucion  -> " + k);

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
                    //con.close();
                    ini.setListaDeCausalesDeDevolucion(listaDeCausalesDeRechazo);
                    Thread.sleep(1);
                } // fin try // fin try
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

            } catch (Exception ex) {
                Logger.getLogger(HiloListadoDeCausalesdeDevolucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {

                ResultSet rst = null;
                Statement st;
                Connection con;
                // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeCausalesdeRechazo");

                con = ini.getConnRemota();
                
                CCausalesDeDevolucion causarechazo = new CCausalesDeDevolucion(ini);
                listaDeCausalesDeRechazo = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(causarechazo.arrListadoDeCCausalesDeRechazo());

                    while (rst.next()) {
                        //System.out.println("Cargando  -> " + new Date());
                        causarechazo = new CCausalesDeDevolucion(ini);
                        // idcausalesDeRechazo, nombreCausalDeRechazo, activo, fechaIng, usuario, flag
                        causarechazo.setIdCausalesDeDevolucion(rst.getInt("idcausalesDeRechazo"));
                        causarechazo.setNombreCausalesDeDEvolucion(rst.getString("nombreCausalDeRechazo"));
                        causarechazo.setActivoCausalesDeDEvolucion(rst.getInt("activo"));

                        listaDeCausalesDeRechazo.add(causarechazo);
                       System.out.println("Cargando Causal de Devolucion  -> " + causarechazo.getIdCausalesDeDevolucion());

                    }
                    rst.close();
                    st.close();
                    //con.close();
                    ini.setListaDeCausalesDeDevolucion(listaDeCausalesDeRechazo);
                    Thread.sleep(1);
                }
            } catch (Exception ex) {
                Logger.getLogger(HiloListadoDeCausalesdeDevolucion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
