/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import java.awt.HeadlessException;
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
public class HiloListadoDeAgencias implements Runnable {

    List<CAgencias> listAgencias = null;

    Inicio ini = null;
    JProgressBar barraInf = null;
    JProgressBar barraSup = null;
    int totalFilasDeConsultas;
    int contadorDeRegistros;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param tiempo
     */
    public HiloListadoDeAgencias(Inicio ini) {

        this.ini = ini;

    }

    public HiloListadoDeAgencias(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;
    }

    @Override
    public void run() {
        if (barraInf != null) {
            correrHilo(1);
        } else {
            correrHilo();
        }

    }

    private void correrHilo() throws HeadlessException {

        try {

            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
           // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeAgencias");

           con = ini.getConnRemota();
           
            CAgencias agencia = new CAgencias(ini);
            listAgencias = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(agencia.arrListdoDeAgencias());
                while (rst.next()) {
                    agencia = new CAgencias(ini);

                    agencia.setIdAgencia(rst.getInt("idagencias"));
                    agencia.setIdRegional(rst.getInt("idRegional"));
                    agencia.setIdZona(rst.getInt("idZona"));
                    agencia.setNombreAgencia(rst.getString("nombreAgencia"));
                    agencia.setCiudad(rst.getInt("idCiudad"));
                    agencia.setNombreRegional(rst.getString("nombreRegional"));
                    agencia.setNombreZona(rst.getString("nombreZona"));
                    agencia.setActivoAgencia(rst.getInt("activo"));

                    System.out.println("Cargando agencias -> " + agencia.getNombreAgencia());

                    listAgencias.add(agencia);
                    Thread.sleep(2);
                }
                rst.close();
                st.close();
               // con.close();
                ini.setListaDeAgencias(listAgencias);

            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeAgencias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void correrHilo(int j) throws HeadlessException {

        try {
            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeAgencias");

            con = ini.getConnRemota();
            
            CAgencias agencia = new CAgencias(ini);
            listAgencias = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(agencia.arrListdoDeAgencias());
                // SE CUENTAN LOS REGISTROS DE LA LISTA
                rst.last();
                int numeroFilas = rst.getRow();
                rst.beforeFirst();

                this.totalFilasDeConsultas += numeroFilas;

                while (rst.next()) {
                    agencia = new CAgencias(ini);

                    agencia.setIdAgencia(rst.getInt("idagencias"));
                    agencia.setIdRegional(rst.getInt("idRegional"));
                    agencia.setIdZona(rst.getInt("idZona"));
                    agencia.setNombreAgencia(rst.getString("nombreAgencia"));
                    agencia.setCiudad(rst.getInt("idCiudad"));
                    agencia.setNombreRegional(rst.getString("nombreRegional"));
                    agencia.setNombreZona(rst.getString("nombreZona"));
                    agencia.setActivoAgencia(rst.getInt("activo"));

                    listAgencias.add(agencia);

                    k++;
                    System.out.println("Cargando Agencias -> " + k);

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
               // con.close();
                ini.setListaDeAgencias(listAgencias);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeAgencias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
