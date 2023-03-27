/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.formularios.IngresoAlSistema;
import mtto.vehiculos.Administracion.CLineasPorMarca;
import java.awt.HeadlessException;
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
public class HiloListadoDeLineasDeVehiculos implements Runnable {

    List<CLineasPorMarca> listaDeLineasVehiculos = null;

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
    public HiloListadoDeLineasDeVehiculos(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;

    }

    /**
     * Constructor de clase
     */
    public HiloListadoDeLineasDeVehiculos(Inicio ini) {

        this.ini = ini;

    }

    @Override
    public void run() {

        if (this.barraInf != null) {
            listaForm();
        } else {
            listaVacio();
        }

    }

    private void listaForm() throws HeadlessException {

        try {
            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeLineasDeVehiculos");

            con = ini.getConnRemota();
            
            CLineasPorMarca lineaVeh = new CLineasPorMarca(ini);
            listaDeLineasVehiculos = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(lineaVeh.arrListadoDeLineasPorMarca());

                rst.last();
                int numeroFilas = rst.getRow();
                rst.beforeFirst();

                this.totalFilasDeConsultas += numeroFilas;

                while (rst.next()) {

                    lineaVeh = new CLineasPorMarca(ini);
                    lineaVeh.setIdMarcaDeVehiculo(rst.getInt("idMarcaDeVehiculo"));
                    lineaVeh.setIdlineasVehiculos(rst.getInt("idlineasVehiculos"));
                    lineaVeh.setNombreMarcaDeVehiculos(rst.getString("nombreMarcaDeVehiculo"));
                    lineaVeh.setNombreLineaVehiculo(rst.getString("nombreLineaVehiculo"));
                    lineaVeh.setActivoLinea(rst.getInt("activo"));
                  
                    listaDeLineasVehiculos.add(lineaVeh);

                    k++;
                    System.out.println("Cargando Lineas -> " + k);

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

                IngresoAlSistema.band = true;
                rst.close();
                st.close();
               // con.close();

                ini.setListaDeLineasPorMarca(listaDeLineasVehiculos);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try
        catch (Exception ex) {
            Logger.getLogger(HiloListadoDeLineasDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaVacio() throws HeadlessException {
        ResultSet rst = null;
        Statement st = null;

        Connection con = null;
        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
          //  con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeLineasDeVehiculos");

          con = ini.getConnRemota();
          
            CLineasPorMarca lineaVeh = new CLineasPorMarca(ini);
            listaDeLineasVehiculos = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(lineaVeh.arrListadoDeLineasPorMarca());

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());

                    lineaVeh = new CLineasPorMarca(ini);
                    lineaVeh.setIdMarcaDeVehiculo(rst.getInt("idMarcaDeVehiculo"));
                    lineaVeh.setIdlineasVehiculos(rst.getInt("idlineasVehiculos"));
                    lineaVeh.setNombreMarcaDeVehiculos(rst.getString("nombreMarcaDeVehiculo"));
                    lineaVeh.setNombreLineaVehiculo(rst.getString("nombreLineaVehiculo"));
                    lineaVeh.setActivoLinea(rst.getInt("activo"));
                    System.out.println("Cargando Lineas por Marca de vehiculo -> " + lineaVeh.getNombreLineaVehiculo());

                    listaDeLineasVehiculos.add(lineaVeh);

                    System.out.println("tiempo 2 " + new Date());

                }
                IngresoAlSistema.band = true;
                rst.close();
                st.close();
               // con.close();

                ini.setListaDeLineasPorMarca(listaDeLineasVehiculos);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeLineasDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeLineasDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
