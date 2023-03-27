/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import mtto.ingresoDeRegistros.objetos.TiposDeMantenimientos;
import java.sql.Connection;
import java.sql.ResultSet;
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
public class HiloListadoDeTiposDeMantenimiento implements Runnable {

    List<TiposDeMantenimientos> listaDeTiposDeMantenimientos = null;

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
    public HiloListadoDeTiposDeMantenimiento(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;
    }

    public HiloListadoDeTiposDeMantenimiento(Inicio ini) {
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
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

                listaDeTiposDeMantenimientos = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(TiposDeMantenimientos.arrListadoDeMantenimientos());

                    rst.last();
                    int numeroFilas = rst.getRow();
                    rst.beforeFirst();
                    
                    this.totalFilasDeConsultas += numeroFilas;

                    //form.totalNumeroDeRegistros += numeroFilas;
                    while (rst.next()) {

                        TiposDeMantenimientos tipo = new TiposDeMantenimientos(ini);
                        //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                        tipo.setIdMantenimiento(rst.getInt("idMantenimiento"));
                        tipo.setNombreMantenimiento(rst.getString("nombreMantenimiento"));
                        tipo.setActivo(rst.getInt("activo"));

                        listaDeTiposDeMantenimientos.add(tipo);

                        k++;
                        System.out.println("Cargando mantenimientos -> " + k);

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
                    this.ini.setListaDeTiposDeMantenimientos(listaDeTiposDeMantenimientos);
                    Thread.sleep(1);
                } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (Exception ex) {
                Logger.getLogger(HiloListadoDeTiposDeMantenimiento.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            ResultSet rst = null;
            Statement st;
            int numeroFilas;
            double contadorDeFilas = 0;
            int porcentajeBarra;
            Connection con;
            try {

                // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

                listaDeTiposDeMantenimientos = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(TiposDeMantenimientos.arrListadoDeMantenimientos());

                    
                    rst.beforeFirst();

                    //form.totalNumeroDeRegistros += numeroFilas;
                    while (rst.next()) {

                        TiposDeMantenimientos tipo = new TiposDeMantenimientos(ini);
                        //idCargo, nombreCargo, activo, fechaIng, usuario, flag
                        tipo.setIdMantenimiento(rst.getInt("idMantenimiento"));
                        tipo.setNombreMantenimiento(rst.getString("nombreMantenimiento"));
                        tipo.setActivo(rst.getInt("activo"));

                        System.out.println("Cargando mantenimientos -> " + tipo.getNombreMantenimiento());

                        listaDeTiposDeMantenimientos.add(tipo);

                        Thread.sleep(1);

                    }
                    rst.close();
                    st.close();
                    con.close();
                    this.ini.setListaDeTiposDeMantenimientos(listaDeTiposDeMantenimientos);
                    Thread.sleep(1);
                }
            } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (Exception ex) {
                Logger.getLogger(HiloListadoDeTiposDeMantenimiento.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
