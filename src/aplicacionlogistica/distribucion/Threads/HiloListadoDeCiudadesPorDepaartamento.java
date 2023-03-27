/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
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
public class HiloListadoDeCiudadesPorDepaartamento implements Runnable {

    

    List<CCiudades> listaDeCiudades = null;
    
     Inicio ini = null;
    JProgressBar barraInf = null;
    JProgressBar barraSup = null;
    int totalFilasDeConsultas;
    int contadorDeRegistros;
    int idDepartamento;
    CDepartamentos departamento;

    /**
     * Constructor de clase
     */
    public HiloListadoDeCiudadesPorDepaartamento(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param departamento
     */
    public HiloListadoDeCiudadesPorDepaartamento(Inicio ini, CDepartamentos departamento) {
        this.ini = ini;
        this.departamento=departamento;
    }

    @Override
    public void run() {
      
            listaVacio();
        

    }

    private void listaVacio() throws HeadlessException {
        ResultSet rst = null;
        Statement st = null;
        Connection con = null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
           // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeCiudades");

           con = ini.getConnRemota();
           
        // idciudades, idDepartamento, nombreCiudad, activo, fechaIng, usuario, flag
        String sql = "SELECT  c.idciudades,c. idDepartamento, departamentos.nombreDepartamento,"
                + "c.nombreCiudad, c.activo, c.fechaIng, c.usuario, c.flag "
                + "FROM ciudades c"
                + "join departamentos d on c.idDepartamento=d.idDepartamento"
                + "WHERE "
                + "c.idDepartamento='" + departamento.getIdDepartamento() + "' "
                + "and c.activo = 1 "
                + "ORDER BY ciudades.nombreCiudad asc;";
            CCiudades ciu = new CCiudades(ini);
            listaDeCiudades = new ArrayList();
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(ciu.rstListadoDeCiudades());

                while (rst.next()) {
                    System.out.println("Cargando  -> " + new Date());
                    ciu = new CCiudades(ini);
                    ciu.setIdCiudad(rst.getInt("idciudades"));
                    ciu.setIdDepartamento(rst.getInt("idDepartamento"));
                    ciu.setNombreDepartamento(rst.getString("nombreDepartamento"));
                    ciu.setNombreCiudad(rst.getString("nombreCiudad"));
                    ciu.setActivoCiudad(rst.getInt("activo"));
                    System.out.println("Cargando Ciudades -> " + ciu.getNombreCiudad());

                    listaDeCiudades.add(ciu);

                }
                rst.close();
                st.close();
                //con.close();

                departamento.setListaDeCiudades(listaDeCiudades);
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
                Logger.getLogger(HiloListadoDeCiudadesPorDepaartamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeCiudadesPorDepaartamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
