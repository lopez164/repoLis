/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeCanalesDeVenta implements Runnable {

   List<CCanalesDeVenta> listaDeCanalesDeVenta = null;

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
    
    public HiloListadoDeCanalesDeVenta(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;


    }
     public HiloListadoDeCanalesDeVenta(Inicio ini) {
        this.ini = ini;

    }

    @Override
    public void run() {
       

        if ( this.barraInf != null) {

                int k = 0; 
                int y = 0;
                ResultSet rst = null;
                Statement st;
                Connection con;

            try {
                // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeCanalesDeVenta");

                con = ini.getConnRemota();
                
                CCanalesDeVenta canVenta = new CCanalesDeVenta(ini);
                listaDeCanalesDeVenta = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(canVenta.arrListadoDeCanaleDeVenta());

                    rst.last();
                    int numeroFilas = rst.getRow();
                    rst.beforeFirst();

                     this.totalFilasDeConsultas += numeroFilas;

                    while (rst.next()) {
                       
                        canVenta = new CCanalesDeVenta(ini);

                        canVenta.setIdCanalDeVenta(rst.getInt("idCanalDeVenta"));
                        canVenta.setNombreCanalDeVenta(rst.getString("nombreCanalDeVenta"));
                        canVenta.setActivoCanal(rst.getInt("activo"));

                        System.out.println("Cargando Canales de Venta -> " + canVenta.getNombreCanalDeVenta());

                        listaDeCanalesDeVenta.add(canVenta);

                         k++;
                         System.out.println("Cargando usuarios -> " + k);
                        
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
                    ini.setListaDeCanalesDeVenta(listaDeCanalesDeVenta);
                    Thread.sleep(1);
                }
            } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
               
            } catch (Exception ex) {
                Logger.getLogger(HiloListadoDeCanalesDeVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                
                ResultSet rst = null;
                Statement st;
                Connection con;
                //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeCanalesDeVenta");

                con = ini.getConnRemota();
                
                CCanalesDeVenta canVenta = new CCanalesDeVenta(ini);
                listaDeCanalesDeVenta = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(canVenta.arrListadoDeCanaleDeVenta());

                    while (rst.next()) {
                        canVenta = new CCanalesDeVenta(ini);

                        canVenta.setIdCanalDeVenta(rst.getInt("idCanalDeVenta"));
                        canVenta.setNombreCanalDeVenta(rst.getString("nombreCanalDeVenta"));
                        canVenta.setActivoCanal(rst.getInt("activo"));

                        listaDeCanalesDeVenta.add(canVenta);

                    }
                    rst.close();
                    st.close();
                   // con.close();
                    ini.setListaDeCanalesDeVenta(listaDeCanalesDeVenta);
                    Thread.sleep(1);
                }
            } catch (Exception ex) {
                Logger.getLogger(HiloListadoDeCanalesDeVenta.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
