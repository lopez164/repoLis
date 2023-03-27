/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CNivelesDeAcceso;
import aplicacionlogistica.distribucion.objetos.CTiposDeAcceso;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import aplicacionlogistica.configuracion.organizacion.CRegionales;
import aplicacionlogistica.configuracion.organizacion.CZonas;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloAuxIngresoAlSistema implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    Inicio ini;
    ArrayList<CDepartamentos> arrDepartamentos = null;
    ArrayList<CCiudades> arrCiudades = null;
    ArrayList<CZonas> arrZonas = null;
    ArrayList<CRegionales> arrRegionales = null;
    ArrayList<CAgencias> arrAgenciass = null;
    ArrayList<CUsuarios> arrUsuarios = null;
    ArrayList<CTiposDeAcceso> arrTiposDeAcceso = null;
    ArrayList<CNivelesDeAcceso> arrNivelesDeAcceso = null;
  

    /**
     * Constructor de clase
     */
    public HiloAuxIngresoAlSistema(Inicio ini,int tiempo, String form) {
        this.tiempo = tiempo;
       
        this.ini=ini;

    }

    @Override
    public void run() {
        /*
        int k = 1;
        int j = 0;
        double l = 0.0;
        try {
            arrDepartamentos = new ArrayList();
            CDepartamentos dptos = new CDepartamentos(ini);
            ArrayList<CDepartamentos.IdDepartamentos> lista = new ArrayList();
            lista = dptos.listadoDeDepartamentos();
            double m = l;
            j = lista.size();
            l = 10.0 / (double) j;
            form.barraSuperior.setStringPainted(true);
            for (CDepartamentos.IdDepartamentos obj : lista) {
                System.out.println("Haciendo algo divertido...(Departamentos) -> " + k);
                CDepartamentos dpto = new CDepartamentos(ini, obj.getDepartamento());
                arrDepartamentos.add(dpto);
                k++;
                form.barraSuperior.setValue((int) m);
                form.barraSuperior.repaint();
                Thread.sleep(1);
                m = m + l;
            }
            arrCiudades = new ArrayList();
            CCiudades ciudad = new CCiudades(ini);
            ArrayList<CCiudades.IdCiudades> lista2 = new ArrayList();
            lista2 = ciudad.listadoDeCiudades();

            j = lista2.size();
            l = 10.0 / (double) j;
            for (CCiudades.IdCiudades obj : lista2) {
                System.out.println("Haciendo algo divertido...(Ciudades) -> " + k);
                CCiudades ciu = new CCiudades(ini, obj.getCiudad());
                arrCiudades.add(ciu);
                k++;
                form.barraSuperior.setValue((int) m);
                form.barraSuperior.repaint();
                Thread.sleep(1);
                m = m + l;
            }
            arrZonas = new ArrayList();
            CZonas zona = new CZonas(ini);
            ArrayList<CZonas.IdZonas> lista6 = new ArrayList();
            lista6 = zona.listadoDeZonas();
            j = lista6.size();
            l = 10.0 / (double) j;
            for (CZonas.IdZonas obj : lista6) {
                System.out.println("Haciendo algo divertido...(Zonas) -> " + k);
                CZonas zon = new CZonas(ini, obj.getZona());
                arrZonas.add(zon);
                k++;
                form.barraSuperior.setValue((int) m);
                form.barraSuperior.repaint();
                m = m + l;
                Thread.sleep(1);
            }
            arrRegionales = new ArrayList();
            CRegionales regional = new CRegionales(ini);
            ArrayList<CRegionales.IdRegionales> lista7 = new ArrayList();
            lista7 = regional.listadoDeRegionales();
            j = lista7.size();
            l = 10.0 / (double) j;
            for (CRegionales.IdRegionales obj : lista7) {
                System.out.println("Haciendo algo divertido...(Regionales) -> " + k);
                CRegionales reg = new CRegionales(ini, obj.getRegional());
                arrRegionales.add(reg);
                k++;
                form.barraSuperior.setValue((int) m);
                form.barraSuperior.repaint();
                m = m + l;
                Thread.sleep(1);
            }

            arrAgenciass = new ArrayList();
            CAgencias agencia = new CAgencias(ini);
            ArrayList<CAgencias.IdAgencias> lista8 = new ArrayList();
            lista8 = agencia.listadoDeAgencias();
            j = lista8.size();
            l = 8.0 / (double) j;
            for (CAgencias.IdAgencias obj : lista8) {
                System.out.println("Haciendo algo divertido...(Agencias) -> " + k);
                CAgencias agen = new CAgencias(ini, obj.getAgencia());
                arrAgenciass.add(agen);
                k++;
                form.barraSuperior.setValue((int) m);
                form.barraSuperior.repaint();
                m = m + l;
                Thread.sleep(1);
            }

            arrUsuarios = new ArrayList();
            CUsuarios usu = new CUsuarios(ini);
            ArrayList<CUsuarios.IdUsuarios> lista12 = new ArrayList();
            lista12 = usu.listadoDeUsuarios();
            j = lista12.size();
            l = 3.0 / (double) j;
            for (CUsuarios.IdUsuarios obj : lista12) {
                System.out.println("Haciendo algo divertido...(Usuarios) -> " + k);
                CUsuarios usua = new CUsuarios(ini, obj.getUsuario());
                arrUsuarios.add(usua);
                k++;
                form.barraSuperior.setValue((int) m);//100
                form.barraSuperior.repaint();
                m = m + l;
                Thread.sleep(1);
            }

            arrTiposDeAcceso = new ArrayList();
            CTiposDeAcceso tipAcc = new CTiposDeAcceso(ini);
            ArrayList<CTiposDeAcceso.IdTiposDeAcceso> lista13 = new ArrayList();
            lista13 = tipAcc.listadoDeTiposDeAcceso();
            j = lista13.size();
            l = 3.0 / (double) j;
            for (CTiposDeAcceso.IdTiposDeAcceso obj : lista13) {
                System.out.println("Haciendo algo divertido...(Usuarios) -> " + k);
                CTiposDeAcceso tA = new CTiposDeAcceso(ini, obj.getTiposDeAcceso());
                arrTiposDeAcceso.add(tA);
                k++;
                form.barraSuperior.setValue((int) m);//100
                form.barraSuperior.repaint();
                m = m + l;
                Thread.sleep(1);
            }

            arrNivelesDeAcceso = new ArrayList();
            CNivelesDeAcceso nivAcc = new CNivelesDeAcceso(ini);
            ArrayList<CNivelesDeAcceso.IdNivelesDeAcceso> lista14 = new ArrayList();
            lista14 = nivAcc.listadoDeNivelesDeAcceso();
            j = lista14.size();
            l = 3.0 / (double) j;
            for (CNivelesDeAcceso.IdNivelesDeAcceso obj : lista14) {
                System.out.println("Haciendo algo divertido...(niveles de acceso) -> " + k);
                CNivelesDeAcceso nA = new CNivelesDeAcceso(ini, obj.getNivelesDeAcceso());
                arrNivelesDeAcceso.add(nA);
                k++;
                form.barraSuperior.setValue((int) m);//100
                form.barraSuperior.repaint();
                m = m + l;
                Thread.sleep(1);
            }

          
            form.barraSuperior.setValue(100);//100
            form.barraSuperior.repaint();
            Thread.sleep(1);
            band = true;
            ini.setArrUsuarios(arrUsuarios);
            ini.setArrAgenciass(arrAgenciass);
            ini.setArrCiudades(arrCiudades);
            ini.setArrDepartamentos(arrDepartamentos);
            ini.setArrRegionales(arrRegionales);
            ini.setArrZonas(arrZonas);
            ini.setArrTiposDeAcceso(arrTiposDeAcceso);
            ini.setArrNivelesDeAcceso(arrNivelesDeAcceso);
            form.txtUsuario.setEnabled(true);
            form.txtClave.setEnabled(true);
            form.txtUsuario.requestFocus();
        } // fin try // fin try
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            band = true;
        } catch (Exception ex) {
            Logger.getLogger(HiloAuxIngresoAlSistema.class.getName()).log(Level.SEVERE, null, ex);
        }  
        **/
    }
}
