/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CCuentasBancarias;
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
public class HiloListadoDeCuentasBancarias implements Runnable {

   List<CCuentasBancarias> listaDeCuentasBancarias = null;

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
    public HiloListadoDeCuentasBancarias(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;
    }

    @Override
    public void run() {


        try {
            int k = 0; 
                int y = 0;
                ResultSet rst = null;
                Statement st;
                Connection con;

            if ( this.barraInf != null) {

                //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeCuentasBancarias");

                con = ini.getConnRemota();
                
                CCuentasBancarias cuenta = new CCuentasBancarias(ini);
                listaDeCuentasBancarias = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(cuenta.arrListadoDeCuentasBancarias());

                    rst.last();
                    int numeroFilas = rst.getRow();
                    rst.beforeFirst();

                   this.totalFilasDeConsultas += numeroFilas;

                    while (rst.next()) {
                      
                        cuenta = new CCuentasBancarias(ini);
                         cuenta.setIdcuentasBancarias(rst.getInt("idCuenta"));
                        cuenta.setActivo(rst.getInt("activo"));
                        cuenta.setIdBanco(rst.getInt("idBanco"));
                        cuenta.setNombreDeBanco(rst.getString("nombreBanco"));
                        cuenta.setTipoDeCuenta(rst.getString("tipoCuenta"));
                        cuenta.setNumeroDeCuenta(rst.getString("numeroDeCuenta"));


                        listaDeCuentasBancarias.add(cuenta);

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
                    ini.setLIstaDeCuentasBancarias(listaDeCuentasBancarias);
                    Thread.sleep(1);
                }
            } else {
                //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeCuentasBancarias");

                con = ini.getConnRemota();
                
                CCuentasBancarias cuenta = new CCuentasBancarias(ini);
                listaDeCuentasBancarias = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(cuenta.arrListadoDeCuentasBancarias());
                    while (rst.next()) {
                        System.out.println("Cargando  -> " + new Date());
                        cuenta = new CCuentasBancarias(ini);
                        	
                        
                        cuenta.setIdcuentasBancarias(rst.getInt("idCuenta"));
                        cuenta.setActivo(rst.getInt("activo"));
                        cuenta.setIdBanco(rst.getInt("idBanco"));
                        cuenta.setNombreDeBanco(rst.getString("nombreBanco"));
                        cuenta.setTipoDeCuenta(rst.getString("tipoCuenta"));
                        cuenta.setNumeroDeCuenta(rst.getString("numeroDeCuenta"));

                        System.out.println("Cargando Cuenteas Bancarias -> " + cuenta.getNumeroDeCuenta());

                        listaDeCuentasBancarias.add(cuenta);

                        Thread.sleep(1);

                        System.out.println("tiempo 2 " + new Date());

                    }
                    rst.close();
                    st.close();
                    //con.close();
                   ini.setLIstaDeCuentasBancarias(listaDeCuentasBancarias);
                    Thread.sleep(1);
                }
            }
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());

        } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
           
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCuentasBancarias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
