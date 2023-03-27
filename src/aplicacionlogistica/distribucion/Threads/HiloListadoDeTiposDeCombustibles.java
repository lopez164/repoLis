/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.formularios.IngresoAlSistema;
import mtto.vehiculos.Administracion.CTiposDeCombustibles;
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
public class HiloListadoDeTiposDeCombustibles implements Runnable {

    List<CTiposDeCombustibles> listaDeTiposDeCombustibles = null;
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
    public HiloListadoDeTiposDeCombustibles(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;
    }

    /**
     * Constructor de clase
     */
    public HiloListadoDeTiposDeCombustibles(Inicio ini) {

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
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeTiposDeCombustibles");

            CTiposDeCombustibles tipComb = new CTiposDeCombustibles(ini);
            listaDeTiposDeCombustibles = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(tipComb.arrListadoDeCombustibles());

                rst.last();
                int numeroFilas = rst.getRow();
                rst.beforeFirst();

                this.totalFilasDeConsultas += numeroFilas;

                while (rst.next()) {

                    tipComb = new CTiposDeCombustibles(ini);

                    tipComb.setIdCombustible(rst.getInt("idTipoCombustible"));
                    tipComb.setNombreCombustible(rst.getString("nombreTipoCombustible"));
                    tipComb.setActivoCombustible(rst.getInt("activo"));

                    listaDeTiposDeCombustibles.add(tipComb);

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
                con.close();
                ini.setListaDeTiposDeCombustibles(listaDeTiposDeCombustibles);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (Exception ex) {
            Logger.getLogger(HiloListadoDeTiposDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaVacio() throws HeadlessException {
        Connection con = null;
        Statement st = null;
        ResultSet rst = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeTiposDeCombustibles");

            CTiposDeCombustibles tipComb = new CTiposDeCombustibles(ini);
            listaDeTiposDeCombustibles = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(tipComb.arrListadoDeCombustibles());

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());
                    tipComb = new CTiposDeCombustibles(ini);

                    tipComb.setIdCombustible(rst.getInt("idTipoCombustible"));
                    tipComb.setNombreCombustible(rst.getString("nombreTipoCombustible"));
                    tipComb.setActivoCombustible(rst.getInt("activo"));

                    System.out.println("Cargando Tipos de Combustibles -> " + tipComb.getNombreCombustible());

                    listaDeTiposDeCombustibles.add(tipComb);

                    System.out.println("tiempo 2 " + new Date());

                }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeTiposDeCombustibles(listaDeTiposDeCombustibles);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeTiposDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeTiposDeCombustibles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
