/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
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
public class HiloListadoDeRutasDeDistribucion implements Runnable {

   List<CRutasDeDistribucion> listaDeRutasDeDistribucion = null;
  
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
    public HiloListadoDeRutasDeDistribucion(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;

    }

    public HiloListadoDeRutasDeDistribucion(Inicio ini) {
        this.ini = ini;

    }

    @Override
    public void run() {


        if ( this.barraInf != null) {
           

            try {
                int k = 0; 
                int y = 0;
                ResultSet rst = null;
                Statement st;
                Connection con;

                // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeRutasDeDistribucion");
                CRutasDeDistribucion rutDist = new CRutasDeDistribucion(ini);

                listaDeRutasDeDistribucion = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(rutDist.arrListadoDeRutasDeDistribucion());

                    rst.last();
                    int numeroFilas = rst.getRow();
                    rst.beforeFirst();

                    this.totalFilasDeConsultas += numeroFilas;

                    while (rst.next()) {
                       
                        rutDist = new CRutasDeDistribucion(ini);

                        rutDist.setIdRutasDeDistribucion(rst.getInt("idRuta"));
                        rutDist.setNombreRutasDeDistribucion(rst.getString("nombreDeRuta"));
                        rutDist.setTipoRuta(rst.getString("tipoRuta"));
                        rutDist.setActivoRutasDeDistribucion(rst.getInt("activo"));

                        listaDeRutasDeDistribucion.add(rutDist);

                           k++;
                         System.out.println("Cargando Rutas -> " + k);
                        
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
                    ini.setListaDeRutasDeDistribucion(listaDeRutasDeDistribucion);
                    Thread.sleep(1);
                }
            } // fin try // fin try // fin try // fin try // fin try // fin try // fin try // fin try
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
               
            } catch (Exception ex) {
                Logger.getLogger(HiloListadoDeRutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                
                int k = 0; 
                int y = 0;
                ResultSet rst = null;
                Statement st;
                Connection con;

                // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeRutasDeDistribucion");
                CRutasDeDistribucion rutDist = new CRutasDeDistribucion(ini);

                listaDeRutasDeDistribucion = new ArrayList();
                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(rutDist.arrListadoDeRutasDeDistribucion());

                    while (rst.next()) {
                        System.out.println("Cargando  -> " + new Date());
                        rutDist = new CRutasDeDistribucion(ini);
                        
                        rutDist.setIdRutasDeDistribucion(rst.getInt("idRuta"));
                        rutDist.setNombreRutasDeDistribucion(rst.getString("nombreDeRuta"));
                        rutDist.setAgencia(rst.getInt("agencia"));
                        rutDist.setFechaIng(rst.getString("fechaIng"));
                        rutDist.setUsuario(rst.getString("usuario"));
                        rutDist.setFlag(rst.getInt("flag"));
                        rutDist.setTipoRuta(rst.getString("tipoRuta"));
                        rutDist.setActivoRutasDeDistribucion(rst.getInt("activo"));

                        System.out.println("Cargando Rutas de Distribución -> " + rutDist.getNombreRutasDeDistribucion());

                        listaDeRutasDeDistribucion.add(rutDist);

                    }
                    rst.close();
                    st.close();
                    con.close();
                    ini.setListaDeRutasDeDistribucion(listaDeRutasDeDistribucion);
                    Thread.sleep(10);
                }
            } catch (Exception ex) {
                Logger.getLogger(HiloListadoDeRutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

    }
}
