/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
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
public class HiloListadoDeDepartamentos implements Runnable {

    List<CDepartamentos> listaDeDepartementos = null;

    Inicio ini = null;
    JProgressBar barraInf = null;
    JProgressBar barraSup = null;
    int totalFilasDeConsultas;
    int contadorDeRegistros;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloListadoDeDepartamentos(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param barraInf
     * @param barraSup
     * @param totalFilas
     * @param contadorDeRegistros
     */
    public HiloListadoDeDepartamentos(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
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
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeDepartamentos");

            con = ini.getConnRemota();
            
            CDepartamentos dep = new CDepartamentos(ini);

            listaDeDepartementos = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(dep.rstListadoDeDpartamentos());
                rst.last();
                int numeroFilas = rst.getRow();
                rst.beforeFirst();

                this.totalFilasDeConsultas += numeroFilas;

                while (rst.next()) {

                    dep = new CDepartamentos(ini);
                    //  idDepartamento, idPais, nombreDepartamento, activo, fechaIng, usuario, flag
                    dep.setIdDepartamento(rst.getInt("idDepartamento"));
                    dep.setNombreDepartamento(rst.getString("nombreDepartamento"));
                    dep.setActivoDepartamento(rst.getInt("activo"));
                    new Thread(new HiloListadoDeCiudadesPorDepaartamento(ini,dep)).start();

                    listaDeDepartementos.add(dep);

                    k++;
                    System.out.println("Cargando Departamentos -> " + k);

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
                ini.setListaDeDepartamentos(listaDeDepartementos);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try
        catch (Exception ex) {
            Logger.getLogger(HiloListadoDeDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaVacio() throws HeadlessException {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeDepartamentos");

            con = ini.getConnRemota();
            
            CDepartamentos dep = new CDepartamentos(ini);

            listaDeDepartementos = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(dep.rstListadoDeDpartamentos());
                while (rst.next()) {
                    System.out.println("Cargando  " + new Date());
                    dep = new CDepartamentos(ini);
                    //  idDepartamento, idPais, nombreDepartamento, activo, fechaIng, usuario, flag
                    dep.setIdDepartamento(rst.getInt("idDepartamento"));
                    dep.setNombreDepartamento(rst.getString("nombreDepartamento"));
                    dep.setActivoDepartamento(rst.getInt("activo"));
                    new Thread(new HiloListadoDeCiudadesPorDepaartamento(ini,dep)).start();
                   
                    System.out.println("Cargando Departameno -> " + dep.getNombreDepartamento());
                    listaDeDepartementos.add(dep);
                }
                rst.close();
                st.close();
                //con.close();
                ini.setListaDeDepartamentos(listaDeDepartementos);
                Thread.sleep(1);
            }
        } // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            try {
                rst.close();
                st.close();
                //con.close();
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            } catch (SQLException ex) {
                Logger.getLogger(HiloListadoDeDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeDepartamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
