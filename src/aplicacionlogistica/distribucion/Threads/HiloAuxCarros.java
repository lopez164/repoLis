/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import mtto.vehiculos.Administracion.CMarcasDeVehiculos;
import mtto.vehiculos.Administracion.CTiposDeCarrocerias;
import mtto.vehiculos.Administracion.CTiposDeCombustibles;
import mtto.vehiculos.Administracion.CTiposDeContratosVehiculos;
import mtto.vehiculos.Administracion.CTiposDeServicio;
import mtto.vehiculos.Administracion.CTiposDeVehiculos;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloAuxCarros implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    Inicio ini;
   
    ArrayList<CMarcasDeVehiculos> arrMarcasVehiculos = null;
    ArrayList<CTiposDeVehiculos> arrTiposDeVehiculos = null;
    ArrayList<CTiposDeCarrocerias> arrTiposDeCarrocerias = null;
    ArrayList<CTiposDeServicio> arrTiposDeServicio = null;
    ArrayList<CTiposDeCombustibles> arrCombustibles = null;
    ArrayList<CAgencias> arrAgenciass = null;
    ArrayList<CTiposDeContratosVehiculos> arrContratos = null;
    IngresarCarros form;

    /**
     * Constructor de clase
     */
    public HiloAuxCarros(Inicio ini,int tiempo, IngresarCarros form) {
        this.tiempo = tiempo;
        this.form = form;
this.ini=ini;
    }

    @Override
    public void run() {
    /*    int k = 1;
       
        try {
            arrMarcasVehiculos = new ArrayList();
            CMarcasDeVehiculos mVeh = new CMarcasDeVehiculos(ini);
            ArrayList<CMarcasDeVehiculos.IdMarcaDeVehiculoss> lista = new ArrayList();
            lista = mVeh.listadoDeMarcaDeVehiculoss();
            for (CMarcasDeVehiculos.IdMarcaDeVehiculoss obj : lista) {
                System.out.println("Haciendo algo divertido...(MarcasDeVehiculos) -> " + k);
                CMarcasDeVehiculos marca = new CMarcasDeVehiculos(ini, obj.getMarcaDeVehiculos());
                arrMarcasVehiculos.add(marca);
                Thread.sleep(1);
            }
            arrTiposDeVehiculos = new ArrayList();
            CTiposDeVehiculos vehic = new CTiposDeVehiculos(ini);
            ArrayList<CTiposDeVehiculos.IdVehiculos> lista2 = new ArrayList();
            lista2 = vehic.listadoDeVehiculos();

            for (CTiposDeVehiculos.IdVehiculos obj : lista2) {
                System.out.println("Haciendo algo divertido...(TiposDeVehiculos) -> " + k);
                CTiposDeVehiculos tipVe = new CTiposDeVehiculos(ini, obj.getVehiculo());
                arrTiposDeVehiculos.add(tipVe);
                Thread.sleep(1);
            }

            arrTiposDeCarrocerias = new ArrayList();
            CTiposDeCarrocerias tipCar = new CTiposDeCarrocerias(ini);
            ArrayList<CTiposDeCarrocerias.IdCarrocerias> lista3 = new ArrayList();
            lista3 = tipCar.listadoDeCarrocerias();
            for (CTiposDeCarrocerias.IdCarrocerias obj : lista3) {
                System.out.println("Haciendo algo divertido...(Tipos De Carrocerias) -> " + k);
                CTiposDeCarrocerias tCar = new CTiposDeCarrocerias(ini, obj.getCarroceria());
                arrTiposDeCarrocerias.add(tCar);
                Thread.sleep(1);
            }

            arrTiposDeServicio = new ArrayList();
            CTiposDeServicio tipoSangre = new CTiposDeServicio(ini);
            ArrayList<CTiposDeServicio.IdTipoDeServicios> lista4 = new ArrayList();
            lista4 = tipoSangre.listadoDeTipoDeServicios();
            for (CTiposDeServicio.IdTipoDeServicios obj : lista4) {
                System.out.println("Haciendo algo divertido...(Tipos De Servicio) -> " + k);
                CTiposDeServicio tipSang = new CTiposDeServicio(ini, obj.getTipoDeServicio());
                arrTiposDeServicio.add(tipSang);
                Thread.sleep(1);
            }

            arrCombustibles = new ArrayList();
            CTiposDeCombustibles cargo = new CTiposDeCombustibles(ini);
            ArrayList<CTiposDeCombustibles.IdCombustibles> lista5 = new ArrayList();
            lista5 = cargo.listadoDeCombustibles();
            for (CTiposDeCombustibles.IdCombustibles obj : lista5) {
                System.out.println("Haciendo algo divertido...(Tipos De Combustibles) -> " + k);
                CTiposDeCombustibles carg = new CTiposDeCombustibles(ini, obj.getCombustible());
                arrCombustibles.add(carg);
                Thread.sleep(1);
            }
            

            arrAgenciass = new ArrayList();
            CAgencias agencia = new CAgencias(ini);
            ArrayList<CAgencias.IdAgencias> lista8 = new ArrayList();
            lista8 = agencia.listadoDeAgencias();
            for (CAgencias.IdAgencias obj : lista8) {
                System.out.println("Haciendo algo divertido...(Agencias) -> " + k);
                CAgencias agen = new CAgencias(ini, obj.getAgencia());
                arrAgenciass.add(agen);
                Thread.sleep(1);
            }

            

            arrContratos = new ArrayList();
            CTiposDeContratosVehiculos tipCV= new CTiposDeContratosVehiculos(ini);
            ArrayList<CTiposDeContratosVehiculos.IdTipoDeContratos> lista10 = new ArrayList();
            lista10 = tipCV.listadoDeTipoDeContratos();
            for (CTiposDeContratosVehiculos.IdTipoDeContratos obj : lista10) {
                System.out.println("Haciendo algo divertido...(Tipos De Contratos Vehiculos) -> " + k);
                CTiposDeContratosVehiculos tcp = new CTiposDeContratosVehiculos(ini, obj.getTipoDeContrato());
                arrContratos.add(tcp);
                 Thread.sleep(1);
            }

            Thread.sleep(1);
            band = true;
            ini.setArrAgenciass(arrAgenciass);
            ini.setArrTiposDeContratosVehiculos(arrContratos);
            ini.setArrMarcasDeVehiculos(arrMarcasVehiculos);
            ini.setArrTiposDeCarrocerias(arrTiposDeCarrocerias);
            ini.setArrTiposDeServicios(arrTiposDeServicio);
            ini.setArrTiposDeVehiculos(arrTiposDeVehiculos);
            ini.setArrTiposDeCombustibles(arrCombustibles);
            
           
        } // fin try // fin try
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            band = true;
        } catch (Exception ex) {
            Logger.getLogger(HiloAuxCarros.class.getName()).log(Level.SEVERE, null, ex);
        }  
            **/
    }
}
